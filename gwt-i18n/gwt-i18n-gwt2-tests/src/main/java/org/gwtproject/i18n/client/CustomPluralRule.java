/*
 * Copyright 2008 Google Inc.
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

package org.gwtproject.i18n.client;

/** @author Dmitrii Tikhomirov Created by treblereel 1/7/20 */
/** A custom plural rule that returns "0", "1", or "other". */
class CustomPluralRule implements PluralRule {
  @Override
  public PluralForm[] pluralForms() {
    return new PluralForm[] {
      new PluralForm("other", "other"), new PluralForm("0", "first"), new PluralForm("1", "second")
    };
  }

  @Override
  public int select(int n) {
    if (0 <= n && n <= 1) {
      return n + 1;
    }
    return 0;
  }
}
