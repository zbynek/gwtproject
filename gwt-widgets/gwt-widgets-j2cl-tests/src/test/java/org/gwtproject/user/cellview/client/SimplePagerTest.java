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
package org.gwtproject.user.cellview.client;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.gwtproject.aria.client.Property;
import org.gwtproject.aria.client.Roles;
import org.gwtproject.aria.client.State;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NodeList;
import org.gwtproject.user.cellview.client.SimplePager.ImageButtonsConstants;
import org.gwtproject.view.client.HasRows;
import org.gwtproject.view.client.MockHasData;

/** Tests for {@link SimplePager}. */
@J2clTestInput(SimplePagerTest.class)
public class SimplePagerTest extends AbstractPagerTest {
  private ImageButtonsConstants imageButtonConstants;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    imageButtonConstants = new SimplePager_ImageButtonsConstants_en_US();
  }

  public void testAriaAttributesAdded_firstLoad() {
    SimplePager pager = createPager();
    NodeList<Element> nodeList = pager.getElement().getElementsByTagName("img");
    for (int i = 0; i < nodeList.getLength(); i++) {
      Element imgElem = nodeList.getItem(i);
      assertEquals(Roles.getButtonRole().getName(), imgElem.getAttribute("role"));
      String label = imgElem.getAttribute(Property.LABEL.getName());
      assertNotNull(label);
      if (label.equals(imageButtonConstants.firstPage())
          || label.equals(imageButtonConstants.prevPage())
          || label.equals(imageButtonConstants.nextPage())) {
        assertEquals("true", imgElem.getAttribute(State.DISABLED.getName()));
      } else {
        assertEquals("", imgElem.getAttribute(State.DISABLED.getName()));
      }
    }
  }

  public void testNextButtonsDisabled() {
    SimplePager pager = createPager();

    // Buttons are disabled by default.
    assertTrue(pager.isPreviousButtonDisabled());
    assertTrue(pager.isNextButtonDisabled());

    // Set the display.
    HasRows display = new MockHasData<String>();
    display.setRowCount(1000);
    pager.setDisplay(display);

    // First Page.
    display.setVisibleRange(0, 20);
    assertTrue(pager.isPreviousButtonDisabled());
    assertFalse(pager.isNextButtonDisabled());

    // Middle Page.
    display.setVisibleRange(100, 20);
    assertFalse(pager.isPreviousButtonDisabled());
    assertFalse(pager.isNextButtonDisabled());

    // Last Page.
    display.setVisibleRange(980, 20);
    assertFalse(pager.isPreviousButtonDisabled());
    assertTrue(pager.isNextButtonDisabled());
  }

  public void testNextButtonsDisabledWithEmptyDisplay() {
    SimplePager pager = createPager();

    // Set the display.
    HasRows display = new MockHasData<String>();

    // Set range limited
    pager.setRangeLimited(true);
    pager.setPageSize(30);

    pager.setDisplay(display);

    assertTrue(pager.isPreviousButtonDisabled());
    assertTrue(pager.isNextButtonDisabled());
  }

  public void testNextButtonsDisabledWithEmptyDataGrid() {
    SimplePager pager = createPager();

    // Set the display.
    DataGrid<String> display = new DataGrid<String>();

    // Set range limited
    pager.setRangeLimited(true);
    pager.setPageSize(30);

    pager.setDisplay(display);

    assertTrue(pager.isPreviousButtonDisabled());
    assertTrue(pager.isNextButtonDisabled());
  }

  @Override
  protected SimplePager createPager() {
    return new SimplePager();
  }
}
