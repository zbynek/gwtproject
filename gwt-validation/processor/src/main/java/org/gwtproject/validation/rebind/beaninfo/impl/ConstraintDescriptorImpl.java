/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.validation.rebind.beaninfo.impl;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.validation.Payload;
import javax.validation.groups.Default;
import org.gwtproject.validation.context.AptContext;
import org.gwtproject.validation.rebind.beaninfo.ConstraintDescriptor;

/** @author Dmitrii Tikhomirov Created by treblereel 8/20/19 */
public class ConstraintDescriptorImpl implements ConstraintDescriptor {

  private AnnotationMirror annotation;

  private Element source;

  private Map<String, Object> holder = new HashMap<>();

  private Map<String, DefaultValueHolder> defaultValues = new HashMap<>();

  private AptContext context;

  public ConstraintDescriptorImpl(
      AnnotationMirror annotationMirror, Element source, AptContext context) {
    this.annotation = annotationMirror;
    this.source = source;
    this.context = context;

    for (ExecutableElement meth :
        ElementFilter.methodsIn(
            annotationMirror.getAnnotationType().asElement().getEnclosedElements())) {
      AnnotationValue defaultValue = meth.getDefaultValue();
      if (defaultValue != null) {
        defaultValues.put(
            meth.getSimpleName().toString(),
            new DefaultValueHolder(
                meth.getReturnType(), parseValue(meth.getReturnType(), defaultValue.getValue())));
      }
    }

    annotationMirror
        .getElementValues()
        .forEach(
            (k, v) ->
                this.holder.put(
                    k.getSimpleName().toString(), parseValue(k.getReturnType(), v.getValue())));
  }

  private Object parseValue(TypeMirror typeMirror, Object value) {
    if (TypeKind.ARRAY.equals(typeMirror.getKind())) {
      ArrayType arrayType = (ArrayType) typeMirror;
      String componentType = arrayType.getComponentType().toString();
      TypeElement type = MoreTypes.asTypeElement(arrayType.getComponentType());
      type.getKind().equals(ElementKind.ENUM);
      if (type.getKind().equals(ElementKind.ENUM)) {
        List result = new ArrayList<>();
        for (Object clazz : (java.util.AbstractCollection) value) {
          result.add(clazz);
        }
        return result.toArray(new Object[result.size()]);
      } else if (type.getKind().equals(ElementKind.CLASS)) {
        List<Class> result = new ArrayList<>();
        for (Class clazz : (java.util.AbstractCollection<Class<?>>) value) {
          result.add(clazz);
        }
        return result.toArray(new Class[result.size()]);
      }

      List<Object> result = new ArrayList<>();
      if (componentType.startsWith("java.lang.Class")) {
        for (Object obj : (java.util.AbstractCollection<Object>) value) {
          result.add(obj);
        }
      }
      return result.toArray(new Object[result.size()]);
    } else {
      return value;
    }
  }

  @Override
  public AnnotationMirror getAnnotation() {
    return annotation;
  }

  @Override
  public ClassWrapper[] getGroups() {
    List<String> groups = new ArrayList<>();

    if (!holder.containsKey("groups")) {
      groups.add(Default.class.getCanonicalName());
    }
    List<ClassWrapper> classes = new ArrayList<>();
    for (String s : groups) {
      classes.add(new ClassWrapper(s));
    }
    return classes.toArray(new ClassWrapper[classes.size()]);
  }

  @Override
  public Set<Class<? extends Payload>> getPayload() {
    return Collections.emptySet();
  }

  @Override
  public List<String> getConstraintValidatorClasses() {
    return context.getValidators(
        MoreElements.asType(annotation.getAnnotationType().asElement())
            .getQualifiedName()
            .toString());
  }

  @Override
  public Map<String, DefaultValueHolder> getAttributes() {
    for (Map.Entry<String, Object> entry : holder.entrySet()) {
      if (defaultValues.containsKey(entry.getKey())) {
        defaultValues.get(entry.getKey()).value = entry.getValue();
      } else {
        defaultValues.put(
            entry.getKey(),
            new DefaultValueHolder(
                entry.getValue().getClass().getCanonicalName(), entry.getValue()));
      }
    }
    return defaultValues;
  }

  @Override
  public Set<ConstraintDescriptor> getComposingConstraints() {
    return Collections.emptySet();
  }

  @Override
  public boolean isReportAsSingleViolation() {
    return false;
  }

  public Element getSource() {
    return source;
  }

  public static class ClassWrapper {

    private String clazz;

    public ClassWrapper(String clazz) {
      this.clazz = clazz;
    }

    public String getClazz() {
      return clazz;
    }
  }

  public static class DefaultValueHolder {

    public String type;

    public Object value;

    public boolean isEnumArray = false;

    public TypeElement enumArrayType;

    DefaultValueHolder(String type, Object value) {
      this.type = type;
      this.value = value;
    }

    DefaultValueHolder(TypeMirror type, Object value) {
      this.type = type.toString();
      this.value = value;

      if (type.getKind().equals(TypeKind.ARRAY)) {
        boolean isEnumArray =
            MoreTypes.asTypeElement(MoreTypes.asArray(type).getComponentType())
                .getKind()
                .equals(ElementKind.ENUM);
        if (isEnumArray) {
          this.isEnumArray = true;
          this.enumArrayType = MoreTypes.asTypeElement(MoreTypes.asArray(type).getComponentType());
        }
      }
    }
  }
}
