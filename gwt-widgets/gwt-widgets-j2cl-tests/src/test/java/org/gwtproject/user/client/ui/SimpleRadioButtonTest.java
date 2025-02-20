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
package org.gwtproject.user.client.ui;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.j2cl.junit.apt.J2clTestInput;

/** Tests for {@link SimpleRadioButton}. */
@J2clTestInput(SimpleRadioButtonTest.class)
public class SimpleRadioButtonTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.UserTest";
  }

  public void testProperties() {
    SimpleRadioButton radio = new SimpleRadioButton("myName");
    assertEquals("myName", radio.getName());

    radio.setTabIndex(42);
    assertEquals(42, radio.getTabIndex());

    radio.setEnabled(false);
    assertEquals(false, radio.isEnabled());

    // Test the 'checked' state across attachment and detachment
    // (this value has a tendency to get lost on some browsers).
    radio.setChecked(true);
    assertEquals(true, radio.isChecked());

    RootPanel.get().add(radio);
    assertEquals(true, radio.isChecked());

    RootPanel.get().remove(radio);
    assertEquals(true, radio.isChecked());
  }
}
