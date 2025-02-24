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
package org.gwtproject.i18n.client;

import com.google.gwt.junit.client.GWTTestCase;
import org.gwtproject.i18n.shared.cldr.LocaleInfo;
import org.gwtproject.i18n.shared.cldr.LocalizedNames;

/** Tests LocalizedNames defaults. */
public class LocalizedNames_default_Test extends GWTTestCase {

  private LocalizedNames names;

  @Override
  public String getModuleName() {
    return "org.gwtproject.i18n.I18NTest";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    names = LocaleInfo.getCurrentLocale().getLocalizedNames();
  }

  /*  public void testLikelyRegionCodes() {
    String[] regionCodes = names.getLikelyRegionCodes();
    assertEquals("Default locale should have no likely locales", 0,
        regionCodes.length);
  }

  public void testRegionName() {
    assertEquals("United States", names.getRegionName("US"));
    assertEquals("Canada", names.getRegionName("CA"));
  }

  public void testSortedRegionCodes() {
    String[] regionCodes = names.getSortedRegionCodes();
    assertTrue("Should have at least 200 region codes",
        regionCodes.length >= 200);
  }*/
}
