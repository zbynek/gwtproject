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

import static org.gwtproject.uibinder.processor.AptUtil.asDeclaredType;
import static org.gwtproject.uibinder.processor.AptUtil.asQualifiedNameable;
import static org.gwtproject.uibinder.processor.AptUtil.asTypeElement;
import static org.gwtproject.uibinder.processor.AptUtil.getTypeUtils;
import static org.gwtproject.uibinder.processor.AptUtil.isAssignableFrom;

import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.gwtproject.uibinder.processor.ext.UnableToCompleteException;
import org.gwtproject.uibinder.processor.model.OwnerClass;
import org.gwtproject.uibinder.processor.model.OwnerField;

/**
 * This class implements an easy way to bind widget event handlers to methods annotated with
 * UiHandler so that the user doesn't need to worry about writing code to implement these bindings.
 *
 * <p>For instance, the class defined below:
 *
 * <pre>
 *   public class MyClass {
 *     @UiField Label label;
 *
 *     @UiBinder({"label", "link"})
 *     public void doClick(ClickEvent e) {
 *       // do something
 *     }
 *   }
 * </pre>
 *
 * will generate a piece of code like:
 *
 * <pre>
 *    ClickHandler handler0 = new ClickHandler() {
 *      @Override
 *      public void onClick(ClickEvent event) {
 *        owner.doClick(event);
 *      }
 *   });
 *   label.addClickHandler(handler0);
 *   link.addClickHandler(handler0);
 * </pre>
 *
 * Notice that the <b>link</b> object doesn't need to be annotated with UiField as long as it exists
 * (annotated with ui:field) in the template.
 */
class HandlerEvaluator {

  private static final String HANDLER_BASE_NAME =
      "handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames";
  /*
   * TODO(rjrjr) The correct fix is to put the handlers in a locally defined
   * class, making the generated code look like this
   *
   * http://docs.google.com/Doc?docid=0AQfnKgX9tAdgZGZ2cTM5YjdfMmQ4OTk0eGhz&hl=en
   *
   * But that needs to wait for a refactor to get most of this stuff out of here
   * and into com .google .gwt.uibinder.rebind.model
   */
  private int varCounter = 0;

  private final MortalLogger logger;

  private final TypeMirror handlerRegistrationJClass;
  private final TypeMirror eventHandlerJClass;
  private final OwnerClass ownerClass;
  private final boolean useLazyWidgetBuilders = true;

  /**
   * The verbose testable constructor.
   *
   * @param ownerClass a descriptor of the UI owner class
   * @param logger the logger for warnings and errors
   */
  HandlerEvaluator(OwnerClass ownerClass, MortalLogger logger) {
    this.ownerClass = ownerClass;
    this.logger = logger;

    handlerRegistrationJClass =
        AptUtil.getElementUtils()
            .getTypeElement(UiBinderApiPackage.current().getHandlerRegistrationFqn())
            .asType();
    eventHandlerJClass =
        AptUtil.getElementUtils()
            .getTypeElement(UiBinderApiPackage.current().getEventHandlerFqn())
            .asType();
  }

  /**
   * Runs the evaluator in the given class according to the valid fields extracted from the template
   * (via attribute ui:field).
   *
   * @param writer the writer used to output the results
   * @param fieldManager the field manager instance
   * @param uiOwner the name of the class evaluated here that owns the template
   */
  public void run(IndentedWriter writer, FieldManager fieldManager, String uiOwner)
      throws UnableToCompleteException {

    // Iterate through all methods defined in the class.
    for (ExecutableElement method : ownerClass.getUiHandlers()) {
      // Evaluate the method.
      String boundMethod = method.getSimpleName().toString();
      if (method.getModifiers().contains(Modifier.PRIVATE)) {
        logger.die("Method '%s' cannot be private.", boundMethod);
      }

      // Retrieves both event and handler types.
      List<? extends VariableElement> parameters = method.getParameters();
      if (parameters.size() != 1) {
        logger.die("Method '%s' must have a single event parameter defined.", boundMethod);
      }

      TypeElement eventType = AptUtil.asTypeElement(parameters.get(0).asType());
      if (eventType == null) {
        logger.die("Parameter type is not a class.");
      }

      TypeMirror handlerType = getHandlerForEvent(eventType.asType());
      if (handlerType == null) {
        logger.die(
            "Parameter '%s' is not an event (subclass of GwtEvent).",
            asQualifiedNameable(eventType).getQualifiedName());
      }

      // Cool to add the handler in the output.
      String handlerVarName = HANDLER_BASE_NAME + (++varCounter);
      writeHandler(writer, uiOwner, handlerVarName, handlerType, eventType.asType(), boundMethod);

      // Adds the handler created above.
      AnnotationMirror annotation =
          AptUtil.getAnnotation(method, UiBinderApiPackage.current().getUiHandlerFqn());
      List<AnnotationValue> values =
          (List<AnnotationValue>) AptUtil.getAnnotationValues(annotation).get("value").getValue();

      for (AnnotationValue objectNameValue : values) {
        String objectName = objectNameValue.getValue().toString();
        // Is the field object valid?
        FieldWriter fieldWriter = fieldManager.lookup(objectName);
        if (fieldWriter == null) {
          logger.die(
              ("Method '%s' can not be bound. You probably missed ui:field='%s' "
                  + "in the template."),
              boundMethod,
              objectName);
        }
        TypeMirror objectType = fieldWriter.getInstantiableType();
        List<? extends TypeParameterElement> typeParameters =
            asTypeElement(objectType).getTypeParameters();
        if (!typeParameters.isEmpty()) {
          objectType = tryEnhancingTypeInfo(objectName, objectType);
        }

        // Retrieves the "add handler" method in the object.
        ExecutableElement addHandlerMethodType =
            getAddHandlerMethodForObject(objectType, handlerType);
        if (addHandlerMethodType == null) {
          logger.die(
              "Field '%s' does not have an 'add%s' method associated.",
              objectName, asQualifiedNameable(handlerType).getSimpleName());
        }

        // Cool to tie the handler into the object.
        writeAddHandler(
            writer,
            fieldManager,
            handlerVarName,
            addHandlerMethodType.getSimpleName().toString(),
            objectName);
      }
    }
  }

  private TypeMirror tryEnhancingTypeInfo(String objectName, TypeMirror objectType) {
    OwnerField uiField = ownerClass.getUiField(objectName);
    if (uiField != null) {

      if (uiField.getRawType() instanceof Parameterizable) {
        // Even field is parameterized, it might be a super class. In that case, if we use the field
        // type then we might miss some add handlers methods from the objectType itself; something
        // we don't want to happen!
        // uiField.getRawType()

        TypeMirror erasedType = AptUtil.getTypeUtils().erasure(uiField.getRawType());

        if (AptUtil.getTypeUtils().isSameType(erasedType, objectType)) {
          // Now we proved type from UiField is more specific, let's use that one
          return erasedType;
        }
      }
    }
    return objectType;
  }

  /**
   * Writes a handler entry using the given writer.
   *
   * @param writer the writer used to output the results
   * @param uiOwner the name of the class evaluated here that owns the template
   * @param handlerVarName the name of the handler variable
   * @param handlerType the handler we want to create
   * @param eventType the event associated with the handler
   * @param boundMethod the method bound in the handler
   */
  protected void writeHandler(
      IndentedWriter writer,
      String uiOwner,
      String handlerVarName,
      TypeMirror handlerType,
      TypeMirror eventType,
      String boundMethod)
      throws UnableToCompleteException {

    // Retrieves the single method (usually 'onSomething') related to all
    // handlers. Ex: onClick in ClickHandler, onBlur in BlurHandler ...
    List<ExecutableElement> methods =
        ElementFilter.methodsIn(asTypeElement(handlerType).getEnclosedElements());
    if (methods.size() != 1) {
      logger.die(
          "'%s' has more than one method defined.",
          asQualifiedNameable(handlerType).getQualifiedName());
    }

    // Checks if the method has an Event as parameter. Ex: ClickEvent in
    // onClick, BlurEvent in onBlur ...
    List<? extends VariableElement> parameters = methods.get(0).getParameters();
    if (parameters.size() != 1
        || !AptUtil.getTypeUtils().isSameType(parameters.get(0).asType(), eventType)) {
      logger.die(
          "Method '%s' needs '%s' as parameter",
          methods.get(0).getSimpleName(), asQualifiedNameable(eventType).getQualifiedName());
    }

    writer.newline();
    // Create the anonymous class extending the raw type to avoid errors under the new JDT
    // if the type has a wildcard.
    writer.write(
        "final %1$s %2$s = new %1$s() {",
        asQualifiedNameable(handlerType).getQualifiedName(), handlerVarName);
    writer.indent();
    writer.write(
        "public void %1$s(%2$s event) {",
        methods.get(0).getSimpleName(),
        // Use the event raw type to match the signature as we are using implementing the raw type
        // interface.
        asQualifiedNameable(eventType).getQualifiedName());
    writer.indent();
    // Cast the event to the parameterized type to avoid warnings..
    writer.write(
        "%1$s.%2$s((%3$s) event);",
        uiOwner, boundMethod, asQualifiedNameable(eventType).getQualifiedName());
    writer.outdent();
    writer.write("}");
    writer.outdent();
    writer.write("};");
  }

  /**
   * Adds the created handler to the given object (field).
   *
   * @param writer the writer used to output the results
   * @param handlerVarName the name of the handler variable
   * @param addHandlerMethodName the "add handler" method name associated with the object
   * @param objectName the name of the object we want to tie the handler
   */
  void writeAddHandler(
      IndentedWriter writer,
      FieldManager fieldManager,
      String handlerVarName,
      String addHandlerMethodName,
      String objectName) {
    if (useLazyWidgetBuilders) {
      fieldManager
          .require(objectName)
          .addStatement("%1$s.%2$s(%3$s);", objectName, addHandlerMethodName, handlerVarName);
    } else {
      writer.write("%1$s.%2$s(%3$s);", objectName, addHandlerMethodName, handlerVarName);
    }
  }

  /**
   * Checks if a specific handler is valid for a given object and return the method that ties them.
   * The object must override a method that returns
   *
   * <p>HandlerRegistration and receives a single input parameter of the same type of handlerType.
   *
   * <p>Output an error in case more than one method match the conditions described above.
   *
   * <pre>
   *   <b>Examples:</b>
   *    - HandlerRegistration addClickHandler(ClickHandler handler)
   *    - HandlerRegistration addMouseOverHandler(MouseOverHandler handler)
   *    - HandlerRegistration addSubmitCompleteHandler(
   *          FormPanel.SubmitCompleteHandler handler)
   * </pre>
   *
   * @param objectType the object type we want to check
   * @param handlerType the handler type we want to check in the object
   * @return the method that adds handlerType into objectType, or <b>null</b> if no method was found
   */
  private ExecutableElement getAddHandlerMethodForObject(
      TypeMirror objectType, TypeMirror handlerType) throws UnableToCompleteException {
    ExecutableElement handlerMethod = null;
    ExecutableElement alternativeHandlerMethod = null;
    ExecutableElement alternativeHandlerMethod2 = null;
    List<? extends ExecutableElement> inheritableMethods =
        AptUtil.getInheritableMethods(objectType);
    for (ExecutableElement method : inheritableMethods) {

      // Condition 1: returns HandlerRegistration?
      TypeElement returnClassType = asTypeElement(method.getReturnType());
      if (returnClassType != null
          && isAssignableFrom(handlerRegistrationJClass, returnClassType.asType())) {

        // Condition 2: single parameter of the same type of handlerType?
        List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.size() != 1) {
          continue;
        }

        TypeMirror methodParam = parameters.get(0).asType();
        if (methodParam == null) {
          continue;
        }

        if (getTypeUtils().isSameType(handlerType, methodParam)) {

          // Condition 3: does more than one method match the condition?
          if (handlerMethod != null) {
            logger.die(
                ("This handler cannot be generated. Methods '%s' and '%s' are "
                    + "ambiguous. Which one to pick?"),
                method,
                handlerMethod);
          }

          handlerMethod = method;
          continue;
        }

        /**
         * Normalize the parameter and check for an alternative handler method. Might be the case
         * where the given objectType is generic. In this situation we need to normalize the method
         * parameter to test for equality. For instance:
         *
         * <p>handlerType => TableHandler<String> subjectHandler => Alt 1: TableHandler or Alt 2:
         * TableHandler<T>
         *
         * <p>This is done as an alternative handler method to preserve the original logic.
         */
        if (isAssignableFrom(handlerType, methodParam)) {
          // Alt 1: TableHandler<String> => TableHandler or TableHandler<?> => TableHandler<String>
          alternativeHandlerMethod = method;
        } else if ((handlerType instanceof Parameterizable)
            && !asTypeElement(objectType).getTypeParameters().isEmpty()) {
          // Alt 2: TableHandler<String> => TableHandler<T>
          if (AptUtil.getTypeUtils()
              .isSameType(
                  AptUtil.getTypeUtils().erasure(methodParam),
                  AptUtil.getTypeUtils().erasure(handlerType))) {
            // Unfortunately this is overly lenient but it was always like this
            alternativeHandlerMethod2 = method;
          }
        }
      }
    }

    return (handlerMethod != null)
        ? handlerMethod
        : (alternativeHandlerMethod != null) ? alternativeHandlerMethod : alternativeHandlerMethod2;
  }

  /**
   * Retrieves the handler associated with the event.
   *
   * @param eventType the given event
   * @return the associated handler, <code>null</code> if not found
   */
  private TypeMirror getHandlerForEvent(TypeMirror eventType) {

    // All handlers event must have an overrided method getAssociatedType().
    // We take advantage of this information to get the associated handler.
    // Ex:
    // com .google .gwt.event.dom.client.ClickEvent
    // ---> com .google .gwt.event.dom.client.ClickHandler
    //
    // com .google .gwt.event.dom.client.BlurEvent
    // ---> com .google .gwt.event.dom.client.BlurHandler

    if (eventType == null) {
      return null;
    }
    ExecutableElement method =
        AptUtil.findMethod(eventType, "getAssociatedType", new TypeMirror[0]);
    if (method == null) {
      logger.warn(
          "Method 'getAssociatedType()' could not be found in the event '%s'.",
          asQualifiedNameable(eventType).getQualifiedName());
      return null;
    }

    TypeMirror returnType = method.getReturnType();
    if (returnType == null) {
      logger.warn(
          "The method 'getAssociatedType()' in the event '%s' returns void.",
          asQualifiedNameable(eventType).getQualifiedName());
      return null;
    }

    if (!(asTypeElement(returnType) instanceof Parameterizable)) {
      logger.warn(
          "The method 'getAssociatedType()' in '%s' does not return Type<? extends EventHandler>.",
          asQualifiedNameable(eventType).getQualifiedName());
      return null;
    }
    DeclaredType isParameterized = asDeclaredType(returnType);

    List<? extends TypeMirror> argTypes = isParameterized.getTypeArguments();
    if ((argTypes.size() != 1) && !AptUtil.isAssignableTo(argTypes.get(0), eventHandlerJClass)) {
      logger.warn(
          "The method 'getAssociatedType()' in '%s' does not return Type<? extends EventHandler>.",
          asQualifiedNameable(eventType).getQualifiedName());
      return null;
    }

    return argTypes.get(0);
  }
}
