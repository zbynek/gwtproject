/*
 * Copyright 2006 Google Inc.
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

/**
 * Implementation interface for creating and manipulating focusable elements that aren't naturally
 * focusable in all browsers, such as DIVs.
 */
public class FocusImpl {

  private static FocusImpl implPanel = new FocusImpl();

  /**
   * This instance may not be a {@link FocusImplStandard}, because that special case is only needed
   * for things that aren't naturally focusable on some browsers, such as DIVs. This exact class
   * works for truly focusable widgets on those browsers.
   *
   * <p>The compiler will optimize out the conditional.
   */
  private static FocusImpl implWidget =
      (implPanel instanceof FocusImplStandard) ? new FocusImpl() : implPanel;

  /**
   * Returns the focus implementation class for creating and manipulating focusable elements that
   * aren't naturally focusable in all browsers, such as DIVs.
   */
  public static FocusImpl getFocusImplForPanel() {
    return implPanel;
  }

  /**
   * Returns the focus implementation class for manipulating focusable elements that are naturally
   * focusable in all browsers, such as text boxes.
   */
  public static FocusImpl getFocusImplForWidget() {
    return implWidget;
  }

  /** Not externally instantiable or extensible. */
  FocusImpl() {}

  public void blur(HTMLElement elem) {
    elem.blur();
  }

  public HTMLElement createFocusable() {
    HTMLElement e = DOM.createDiv();
    e.tabIndex = 0;
    return e;
  }

  public void focus(HTMLElement elem) {
    elem.focus();
  }

  public int getTabIndex(HTMLElement elem) {
    return elem.tabIndex;
  }

  public void setAccessKey(HTMLElement elem, char key) {
    Js.asPropertyMap(elem).set("accessKey", Character.toString(key));
  }

  public void setTabIndex(HTMLElement elem, int index) {
    elem.tabIndex = index;
  }
}
