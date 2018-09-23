/*
 * Copyright 2009 Google Inc.
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

import org.gwtproject.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests for {@link FileUpload}.
 * 
 */
public class FileUploadTest extends GWTTestCase {

  private static class TestHandler implements ClickHandler {
    boolean clicked;
    EventTarget target;

    public void onClick(ClickEvent event) {
      target = event.getNativeEvent().getEventTarget();
      clicked = true;
    }
  }

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.Widgets";
  }

  public void testDisable() {
    FileUpload fileUpload = new FileUpload();
    assertTrue(fileUpload.isEnabled());
    fileUpload.setEnabled(false);
    assertFalse(fileUpload.isEnabled());
    fileUpload.setEnabled(true);
    assertTrue(fileUpload.isEnabled());
  }

  public void testClick() {
    FileUpload fileUpload = new FileUpload();
    RootPanel.get().add(fileUpload);

    TestHandler h = new TestHandler();
    fileUpload.addClickHandler(h);

    fileUpload.click();
    assertTrue(h.clicked);

    assertEquals(fileUpload.getElement(), h.target);
  }
}
