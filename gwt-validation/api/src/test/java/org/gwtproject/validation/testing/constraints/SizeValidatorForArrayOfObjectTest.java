/*
 * Copyright 2010 Google Inc.
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
package org.gwtproject.validation.testing.constraints;

import java.util.Date;
import javax.validation.constraints.Size;
import org.gwtproject.validation.client.constraints.SizeValidatorForArrayOfObject;

/** Tests for {@link SizeValidatorForArrayOfObject}. */
public class SizeValidatorForArrayOfObjectTest extends ConstraintValidatorTestCase<Size, Object[]> {

  @SuppressWarnings("unused")
  @Size(min = 2, max = 5)
  private Date defaultField;

  @Override
  protected SizeValidatorForArrayOfObject createValidator() {
    return new SizeValidatorForArrayOfObject();
  }

  public void testAssertIsValid_short() {
    assertConstraintValidator(createArray(1), false);
  }

  public void testAssertIsValid_min() {
    assertConstraintValidator(createArray(2), true);
  }

  public void testAssertIsValid_max() {
    assertConstraintValidator(createArray(5), true);
  }

  public void testAssertIsValid_long() {
    assertConstraintValidator(createArray(6), false);
  }

  private Object[] createArray(int size) {
    Object[] array = new Object[size];
    for (int i = 0; i < size; i++) {
      array[i] = Integer.valueOf(i);
    }
    return array;
  }

  @Override
  protected Class<Size> getAnnotationClass() {
    return Size.class;
  }
}
