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

package org.gwtproject.i18n.rg.resource;

import java.util.SortedSet;
import java.util.TreeSet;
import org.gwtproject.i18n.ext.SelectionProperty;

/** @author Dmitrii Tikhomirov Created by treblereel 12/5/18 */
public class StandardSelectionProperty implements SelectionProperty {

  private final String name, defaultValue;
  private SortedSet<String> values = new TreeSet<>();

  private final String activeValue;

  public StandardSelectionProperty(String name, String activeValue, String defaultValue) {
    this.activeValue = activeValue;
    this.name = name;
    this.defaultValue = defaultValue;
    for (String s : activeValue.split(",")) {
      values.add(s);
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getCurrentValue() {
    return activeValue;
  }

  @Override
  public SortedSet<String> getPossibleValues() {
    return values;
  }

  @Override
  public String getFallbackValue() {
    return defaultValue;
  }
}
