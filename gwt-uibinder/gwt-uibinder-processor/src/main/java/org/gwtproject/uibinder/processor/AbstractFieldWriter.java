/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.uibinder.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.gwtproject.uibinder.processor.ext.UnableToCompleteException;
import org.gwtproject.uibinder.processor.model.OwnerField;

/**
 * Most of the implementation of {@link FieldWriter}. Subclasses are responsible for {@link
 * FieldWriter#getQualifiedSourceName()} and {@link FieldWriter#getInstantiableType()}.
 */
abstract class AbstractFieldWriter implements FieldWriter {

  private static final String NO_DEFAULT_CTOR_ERROR =
      "%1$s has no default (zero args) constructor. To fix this, you can define"
          + " a @UiFactory method on the UiBinder's owner, or annotate a constructor of %2$s with"
          + " @UiConstructor.";

  private final FieldManager manager;
  private final Set<FieldWriter> needs = new LinkedHashSet<FieldWriter>();
  private final List<String> statements = new ArrayList<String>();
  private final List<String> attachStatements = new ArrayList<String>();
  private final List<String> detachStatements = new ArrayList<String>();

  private final String name;
  private String ownerAssignmentStatement;
  private String initializer;
  private boolean written;
  private int buildPrecedence;
  private final MortalLogger logger;
  private final FieldWriterType fieldType;
  private String html;

  private static ThreadLocal<ProcessingEnvironment> processingEnvironmentThreadLocal =
      new ThreadLocal<>();

  AbstractFieldWriter(
      FieldManager manager, FieldWriterType fieldType, String name, MortalLogger logger) {
    if (name == null) {
      throw new RuntimeException("name cannot be null");
    }
    this.manager = manager;
    this.name = name;
    this.ownerAssignmentStatement = name;
    this.logger = logger;
    this.buildPrecedence = 1;
    this.fieldType = fieldType;
  }

  @Override
  public void addAttachStatement(String format, Object... args) {
    attachStatements.add(String.format(format, args));
  }

  @Override
  public void addDetachStatement(String format, Object... args) {
    detachStatements.add(String.format(format, args));
  }

  @Override
  public void addStatement(String format, Object... args) {
    statements.add(String.format(format, args));
  }

  @Override
  public int getBuildPrecedence() {
    return buildPrecedence;
  }

  @Override
  public FieldWriterType getFieldType() {
    return fieldType;
  }

  public String getHtml() {
    return html + ".asString()";
  }

  public String getInitializer() {
    return initializer;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getNextReference() {
    return manager.convertFieldToGetter(name);
  }

  public TypeMirror getReturnType(String[] path, MonitoredLogger logger) {
    if (!name.equals(path[0])) {
      throw new RuntimeException(this + " asked to evaluate another field's path: " + path[0]);
    }

    List<String> pathList = Arrays.asList(path).subList(1, path.length);
    return getReturnType(getAssignableType(), pathList, logger);
  }

  public String getSafeHtml() {
    return html;
  }

  public void needs(FieldWriter f) {
    needs.add(f);
  }

  @Override
  public void setBuildPrecedence(int precedence) {
    this.buildPrecedence = precedence;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public void setInitializer(String initializer) {
    this.initializer = initializer;
  }

  @Override
  public void setOwnerAssignmentStatement(String ownerAssignmentStatement) {
    this.ownerAssignmentStatement = ownerAssignmentStatement;
  }

  @Override
  public String toString() {
    return String.format("[%s %s = %s]", this.getClass().getName(), name, initializer);
  }

  public void write(IndentedWriter w) throws UnableToCompleteException {
    if (written) {
      return;
    }

    for (FieldWriter f : needs) {
      f.write(w);
    }

    if (initializer == null) {
      TypeMirror type = getInstantiableType();
      TypeElement typeElement = AptUtil.asTypeElement(type);
      if (type != null) {
        if ((ElementKind.INTERFACE.equals(typeElement))
            && (AptUtil.findConstructor(type, new TypeMirror[0]) == null)) {
          logger.die(
              NO_DEFAULT_CTOR_ERROR, typeElement.getQualifiedName(), typeElement.getSimpleName());
        }
      }
    }

    if (null == initializer) {
      if (UiBinderApiPackage.current().isGwtCreateSupported()) {
        initializer =
            String.format(
                "(%1$s) %2$s.create(%1$s.class)",
                getQualifiedSourceName(), UiBinderApiPackage.current().getGWTFqn());
      } else {
        initializer = String.format("new %1$s()", getQualifiedSourceName());
      }
    }

    w.write("%s %s = %s;", getQualifiedSourceName(), name, initializer);

    this.written = true;
  }

  @Override
  public void writeFieldBuilder(IndentedWriter w, int getterCount, OwnerField ownerField) {
    if (getterCount > 1) {
      w.write(
          "%s;  // more than one getter call detected. Type: %s, precedence: %s",
          FieldManager.getFieldBuilder(name), getFieldType(), getBuildPrecedence());
      return;
    }

    if (getterCount == 0 && ownerField != null) {
      w.write(
          "%s;  // no getter call detected but must bind to ui:field. "
              + "Type: %s, precedence: %s",
          FieldManager.getFieldBuilder(name), getFieldType(), getBuildPrecedence());
    }
  }

  @Override
  public void writeFieldDefinition(IndentedWriter w, OwnerField ownerField, int getterCount)
      throws UnableToCompleteException {

    TypeElement renderablePanelType =
        AptUtil.getElementUtils()
            .getTypeElement(UiBinderApiPackage.current().getRenderablePanelFqn());

    boolean outputAttachDetachCallbacks =
        getAssignableType() != null
            && AptUtil.isAssignableTo(getAssignableType(), renderablePanelType.asType());

    // Check initializer. Provided value takes precedence over initializer.
    if (ownerField != null && ownerField.isProvided()) {
      initializer = String.format("owner.%s", name);
    } else if (initializer == null) {
      TypeMirror type = getInstantiableType();
      if (type != null) {
        if ((ElementKind.INTERFACE.equals(type.getKind()))
            && (AptUtil.findConstructor(type, new TypeMirror[0]) == null)) {
          TypeElement typeElement = AptUtil.asTypeElement(type);
          logger.die(
              NO_DEFAULT_CTOR_ERROR, typeElement.getQualifiedName(), typeElement.getSimpleName());
        }
      }
      if (UiBinderApiPackage.current().isGwtCreateSupported()) {
        initializer =
            String.format(
                "(%1$s) %2$s.create(%1$s.class)",
                getQualifiedSourceName(), UiBinderApiPackage.current().getGWTFqn());
      } else {
        if (type != null) {
          TypeElement element = AptUtil.getElementUtils().getTypeElement(getQualifiedSourceName());
          if (type.getKind().equals(ElementKind.INTERFACE)
              || AptUtil.getTypeUtils()
                  .asElement(type)
                  .getModifiers()
                  .contains(Modifier.ABSTRACT)) {
            StringBuffer sb = new StringBuffer();
            sb.append(AptUtil.getPackageElement(element).getQualifiedName().toString());
            sb.append(".");
            sb.append(
                (element.getEnclosingElement().getKind().isClass()
                        || element.getEnclosingElement().getKind().isInterface())
                    ? element.getEnclosingElement().getSimpleName().toString() + "_"
                    : "");
            sb.append(element.getSimpleName().toString());

            TypeElement Messages =
                AptUtil.getProcessingEnvironment()
                    .getElementUtils()
                    .getTypeElement("org.gwtproject.i18n.client.Messages");
            // TODO
            if (Messages != null
                && AptUtil.getProcessingEnvironment()
                    .getTypeUtils()
                    .isSubtype(type, Messages.asType())) {
              initializer = String.format("%1$sFactory.get()", sb.toString());
            } else {
              initializer = String.format("new %1$sImpl()", sb.toString());
            }
          } else {
            initializer = String.format("new %1$s()", getQualifiedSourceName());
          }
        } else {
          initializer = String.format("new %1$s()", getQualifiedSourceName() + "Impl");
        }
      }
    }

    w.newline();
    w.write("/**");
    w.write(
        " * Getter for %s called %s times. Type: %s. Build precedence: %s.",
        name, getterCount, getFieldType(), getBuildPrecedence());
    w.write(" */");
    if (getterCount > 1) {
      w.write("private %1$s %2$s;", getQualifiedSourceName(), name);
    }

    w.write("private %s %s {", getQualifiedSourceName(), FieldManager.getFieldGetter(name));
    w.indent();
    w.write("return %s;", (getterCount > 1) ? name : FieldManager.getFieldBuilder(name));
    w.outdent();
    w.write("}");

    w.write("private %s %s {", getQualifiedSourceName(), FieldManager.getFieldBuilder(name));
    w.indent();

    w.write("// Creation section.");
    if (getterCount > 1) {
      w.write("%s = %s;", name, initializer);
    } else {
      w.write("final %s %s = %s;", getQualifiedSourceName(), name, initializer);
    }
    if (ownerField != null && ownerField.isProvided()) {
      w.write("assert %1$s != null : \"UiField %1$s with 'provided = true' was null\";", name);
    }

    w.write("// Setup section.");
    writeStatements(w, statements);

    if (attachStatements.size() > 0) {
      w.newline();
      if (outputAttachDetachCallbacks) {
        w.write("// Attach section.");
        // TODO(rdcastro): This is too coupled with RenderablePanel.
        // Make this nicer.
        w.write("%s.wrapInitializationCallback = ", getName());
        w.indent();
        w.indent();
        w.write("new %s() {", UiBinderApiPackage.current().getCommandFqn());
        w.outdent();
        w.write("@Override public void execute() {");
        w.indent();

        writeStatements(w, attachStatements);

        w.outdent();
        w.write("}");
        w.outdent();
        w.write("};");
      } else {
        String attachedVar = "__attachRecord__";

        w.write("{");
        w.indent();
        w.write("// Attach section.");
        String elementToAttach =
            AptUtil.isAssignableTo(getInstantiableType(), getDomElement().asType())
                ? name
                : name + ".getElement()";

        w.write(
            "%1$s.TempAttachment %2$s = %1$s.attachToDom(%3$s);",
            UiBinderApiPackage.current().getUiBinderUtilFqn(), attachedVar, elementToAttach);

        w.newline();

        writeStatements(w, attachStatements);

        w.newline();
        // If we forced an attach, we should always detach, regardless of whether
        // there are any detach statements.
        w.write("// Detach section.");
        w.write("%s.detach();", attachedVar);
        w.outdent();
        w.write("}");
      }
    }

    if (detachStatements.size() > 0) {
      if (outputAttachDetachCallbacks) {
        w.write("%s.detachedInitializationCallback = ", getName());
        w.indent();
        w.indent();
        w.write("new %s() {", UiBinderApiPackage.current().getCommandFqn());
        w.outdent();
        w.write("@Override public void execute() {");
        w.indent();
      }

      writeStatements(w, detachStatements);

      if (outputAttachDetachCallbacks) {
        w.outdent();
        w.write("}");
        w.outdent();
        w.write("};");
      }
    }

    if ((ownerField != null) && !ownerField.isProvided()) {
      w.newline();
      // If the type of the field is annotated with JsType, then use a dynamic cast
      // to convert it from Element. We assume the developer knows what they are doing
      // and that the JsType represents some form of native DOM element.
      // For more information, see the design doc here: http://goo.gl/eRjoD9
      // TODO: When we know better how this is used, we might want to loosen the annotation
      // constraint (e.g. it might be sufficient for the declared type to extend another
      // interface that is a JsType).
      TypeMirror rawType = ownerField.getRawType();
      if (!AptUtil.isAssignableTo(rawType, getDomElement().asType())
          && rawType.getAnnotation(jsinterop.annotations.JsType.class) != null) {
        w.write(
            "this.owner.%1$s = (%2$s) (Object) %1$s;",
            name, AptUtil.asTypeElement(rawType).getQualifiedName());
      } else {
        w.write("this.owner.%1$s = %2$s;", name, ownerAssignmentStatement);
      }
    }

    w.newline();
    w.write("return %s;", name);
    w.outdent();
    w.write("}");
  }

  private ExecutableElement findMethod(TypeElement type, String methodName) {
    // TODO Move this and getClassHierarchyBreadthFirst to JClassType
    for (TypeElement nextType : UiBinderWriter.getClassHierarchyBreadthFirst(type)) {
      return AptUtil.findMethod(nextType.asType(), methodName, new TypeMirror[0]);
    }
    return null;
  }

  /** Gets a reference to the type object representing Element. */
  private TypeElement getDomElement() {
    TypeElement domElement =
        AptUtil.getElementUtils().getTypeElement(UiBinderApiPackage.current().getDomElementFqn());
    assert domElement != null;
    return domElement;
  }

  private TypeMirror getReturnType(TypeMirror type, List<String> path, MonitoredLogger logger) {
    // TODO(rjrjr,bobv) This is derived from CssResourceGenerator.validateValue
    // We should find a way share code

    Iterator<String> i = path.iterator();
    while (i.hasNext()) {
      String pathElement = i.next();

      TypeElement referenceType = AptUtil.asTypeElement(type);
      if (referenceType == null) {
        logger.error("Cannot resolve member " + pathElement + " on non-reference type " + type);
        return null;
      }

      ExecutableElement m = findMethod(referenceType, pathElement);
      if (m == null) {
        logger.error("Could not find no-arg method named " + pathElement + " in type " + type);
        return null;
      }

      type = m.getReturnType();
    }
    return type;
  }

  private static void writeStatements(IndentedWriter w, Iterable<String> statements) {
    for (String s : statements) {
      w.write(s);
    }
  }
}
