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
package org.gwtproject.validation.rebind.beaninfo;

import javax.lang.model.type.TypeMirror;

/** @author Dmitrii Tikhomirov Created by treblereel 8/20/19 */
public interface PropertyDescriptor extends ElementDescriptor<TypeMirror> {
  /**
   * Is the property marked by the <code>@Valid</code> annotation.
   *
   * @return <code>true</code> if the annotation is present, <code>false</code> otherwise.
   */
  boolean isCascaded();

  /**
   * Name of the property acording to the Java Bean specification.
   *
   * @return property name.
   */
  String getPropertyName();
}
