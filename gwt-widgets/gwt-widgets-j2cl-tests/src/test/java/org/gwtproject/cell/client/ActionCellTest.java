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
import org.gwtproject.cell.client.ActionCell.Delegate;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.NativeEvent;

/** Tests for {@link org.gwtproject.cell.client.ActionCell}. */
@J2clTestInput(ActionCellTest.class)
public class ActionCellTest extends CellTestBase<String> {

  @Override
  public String getModuleName() {
    return "";
  }

  /**
   * A mock {@link Delegate} used for testing.
   *
   * @param <T> the value type
   */
  private static class MockDelegate<T> implements Delegate<T> {
    private T lastObject;

    public void assertLastObject(T expected) {
      assertEquals(expected, lastObject);
    }

    @Override
    public void execute(T object) {
      assertNull(lastObject);
      lastObject = object;
    }
  }

  public void testOnBrowserEvent() {
    MockDelegate<String> delegate = new MockDelegate<String>();
    org.gwtproject.cell.client.ActionCell<String> cell =
        new org.gwtproject.cell.client.ActionCell<String>("hello", delegate);
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    testOnBrowserEvent(cell, getExpectedInnerHtml(), event, "test", null, true);
    delegate.assertLastObject("test");
  }

  /** Test that events outside of the button element are ignored. */
  public void testOnBrowserEventOutsideButton() {
    MockDelegate<String> delegate = new MockDelegate<String>();
    org.gwtproject.cell.client.ActionCell<String> cell =
        new org.gwtproject.cell.client.ActionCell<String>("hello", delegate);
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    testOnBrowserEvent(cell, getExpectedInnerHtml(), event, "test", null, false);
    delegate.assertLastObject(null);
  }

  @Override
  protected Cell<String> createCell() {
    Delegate<String> delegate = new MockDelegate<String>();
    return new ActionCell<String>("clickme", delegate);
  }

  @Override
  protected String createCellValue() {
    return "ignored";
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
    return "<button type=\"button\" tabindex=\"-1\">clickme</button>";
  }

  @Override
  protected String getExpectedInnerHtmlNull() {
    return getExpectedInnerHtml();
  }
}
