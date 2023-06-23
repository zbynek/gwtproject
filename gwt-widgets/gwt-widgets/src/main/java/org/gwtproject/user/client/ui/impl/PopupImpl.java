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
package org.gwtproject.user.client.ui.impl;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.user.client.DOM;

/** Implementation class used by PopupPanel. */
public class PopupImpl {

  public HTMLElement createElement() {
    return DOM.createDiv();
  }

  public HTMLElement getContainerElement(HTMLElement popup) {
    return popup;
  }

  public HTMLElement getStyleElement(HTMLElement popup) {
    return Js.uncheckedCast(popup.parentElement);
  }

  /**
   * @param popup the popup
   * @param rect the clip rect
   */
  public void setClip(HTMLElement popup, String rect) {
    popup.style.setProperty("clip", rect);
  }
}
