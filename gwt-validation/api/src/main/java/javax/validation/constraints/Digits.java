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
package javax.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated element must be a number within accepted range Supported types are:
 *
 * <ul>
 *   <li><code>BigDecimal</code>
 *   <li><code>BigInteger</code>
 *   <li><code>String</code>
 *   <li><code>byte</code>, <code>short</code>, <code>int</code>, <code>long</code>, and their
 *       respective wrapper types
 * </ul>
 *
 * <p><code>null</code> elements are considered valid
 *
 * @author Emmanuel Bernard
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface Digits {
  String message() default "{javax.validation.constraints.Digits.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** @return maximum number of integral digits accepted for this number. */
  int integer();

  /** @return maximum number of fractional digits accepted for this number. */
  int fraction();

  /**
   * Defines several <code>@Digits</code> annotations on the same element
   *
   * @see Digits
   * @author Emmanuel Bernard
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    Digits[] value();
  }
}
