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
package org.gwtproject.cell.client;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.NativeEvent;

/** Tests for {@link org.gwtproject.cell.client.ClickableTextCell}. */
@J2clTestInput(ClickableTextCellTest.class)
public class ClickableTextCellTest extends CellTestBase<String> {

  public void testOnBrowserEvent() {
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    testOnBrowserEvent(getExpectedInnerHtml(), event, "value", "value");
  }

  @Override
  protected Cell<String> createCell() {
    return new ClickableTextCell();
  }

  @Override
  protected String createCellValue() {
    return "clickme";
  }

  @Override
  protected boolean dependsOnSelection() {
    return false;
  }

  @Override
  protected String[] getConsumedEvents() {
    return new String[] {"click", "keydown"};
  }

  @Override
  protected String getExpectedInnerHtml() {
    return "clickme";
  }

  @Override
  protected String getExpectedInnerHtmlNull() {
    return "";
  }

  @Override
  public String getModuleName() {
    return "";
  }
}
