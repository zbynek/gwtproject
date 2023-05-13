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
package org.gwtproject.user.client.ui;

import elemental2.dom.HTMLElement;
import org.gwtproject.safehtml.client.HasSafeHtml;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;

/** Abstract base class for {@link Button}, {@link CheckBox}, {@link RadioButton}. */
public abstract class ButtonBase extends FocusWidget implements HasHTML, HasSafeHtml {

  /**
   * Creates a new ButtonBase that wraps the given browser element.
   *
   * @param elem the DOM element to be wrapped
   */
  protected ButtonBase(HTMLElement elem) {
    super(elem);
  }

  public String getHTML() {
    return getElement().innerHTML;
  }

  public String getText() {
    return getElement().textContent;
  }

  public void setHTML(@IsSafeHtml String html) {
    getElement().innerHTML = html;
  }

  public void setHTML(SafeHtml html) {
    setHTML(html.asString());
  }

  public void setText(String text) {
    getElement().textContent = text;
  }
}
