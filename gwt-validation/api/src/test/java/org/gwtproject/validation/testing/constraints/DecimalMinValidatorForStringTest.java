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

import javax.validation.constraints.DecimalMin;
import org.gwtproject.validation.client.constraints.DecimalMinValidatorForString;

/** Tests for {@link DecimalMinValidatorForString}. */
public class DecimalMinValidatorForStringTest
    extends ConstraintValidatorTestCase<DecimalMin, String> {
  private static String BELOW = "922392239223.08";
  private static String SAME = "922392239223.09";
  private static String ABOVE = "922392239223.10";

  @SuppressWarnings("unused")
  @DecimalMin("922392239223.09")
  private double defaultField;

  @Override
  protected DecimalMinValidatorForString createValidator() {
    return new DecimalMinValidatorForString();
  }

  public void testIsValid_below() {
    assertConstraintValidator(BELOW, false);
  }

  public void testIsValid_same() {
    assertConstraintValidator(SAME, true);
  }

  public void testIsValid_above() {
    assertConstraintValidator(ABOVE, true);
  }

  public void testIsValid_invalid() {
    assertConstraintValidator("invalid", false);
  }

  @Override
  protected Class<DecimalMin> getAnnotationClass() {
    return DecimalMin.class;
  }
}
