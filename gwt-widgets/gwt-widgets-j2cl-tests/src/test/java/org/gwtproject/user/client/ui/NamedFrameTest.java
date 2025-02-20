/*
 * Copyright 2007 Google Inc.
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

/** Tests the {@link NamedFrame} widget. */
@J2clTestInput(NamedFrameTest.class)
public class NamedFrameTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.Widgets";
  }

  public void testNormalName() {
    String name = "testFrame";
    NamedFrame frame = new NamedFrame(name);
    assertEquals(name, frame.getName());
  }

  public void testEmptyName() {
    try {
      new NamedFrame("");
      fail("Empty frame name not allowed");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  public void testWhitespaceName() {
    try {
      new NamedFrame("  ");
      fail("Whitespace-only frame name not allowed");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  public void testHTMLName() {
    try {
      new NamedFrame("<b>yuck</b>");
      fail("html in frame name not allowed");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  public void testQuotesInName() {
    try {
      new NamedFrame("he said \"yuck\"");
      fail("double-quotes in frame name not allowed");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  public void testApostropheInName() {
    try {
      new NamedFrame("he said 'yuck'");
      fail("apostrophe in frame name not allowed");
    } catch (IllegalArgumentException e) {
      // Success
    }
  }

  public void testDefaultSrc() {
    assertEquals("about:blank", new NamedFrame("defaultSrc").getUrl());
  }
}
