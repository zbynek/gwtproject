/*
 * Copyright © 2019 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.text.client;

import java.text.ParseException;
import org.gwtproject.i18n.client.NumberFormat;
import org.gwtproject.text.shared.Parser;

public class IntegerParser implements Parser<Integer> {
  private static IntegerParser INSTANCE;

  public static Parser<Integer> instance() {
    if (INSTANCE == null) {
      INSTANCE = new IntegerParser();
    }

    return INSTANCE;
  }

  protected IntegerParser() {}

  public Integer parse(CharSequence object) throws ParseException {
    if ("".equals(object.toString())) {
      return null;
    } else {
      try {
        return (int) Math.rint(NumberFormat.getDecimalFormat().parse(object.toString()));
      } catch (NumberFormatException var3) {
        throw new ParseException(var3.getMessage(), 0);
      }
    }
  }
}
