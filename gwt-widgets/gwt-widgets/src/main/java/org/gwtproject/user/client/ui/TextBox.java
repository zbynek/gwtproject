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

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import jsinterop.base.Js;
import org.gwtproject.user.client.DOM;

/**
 * A standard single-line text box.
 *
 * <p><img class='gallery' src='doc-files/TextBox.png'/>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-TextBox { primary style }
 *   <li>.gwt-TextBox-readonly { dependent style set when the text box is read-only }
 * </ul>
 *
 * <p>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.TextBoxExample}
 */
public class TextBox extends TextBoxBase {

  /**
   * Creates a TextBox widget that wraps an existing &lt;input type='text'&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static TextBox wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    TextBox textBox = new TextBox(element);

    // Mark it attached and remember it for cleanup.
    textBox.onAttach();
    RootPanel.detachOnWindowClose(textBox);

    return textBox;
  }

  /** Creates an empty text box. */
  public TextBox() {
    this(DOM.createInputText(), "gwt-TextBox");
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be an &lt;input&gt; element whose type is 'text'.
   *
   * @param element the element to be used
   */
  protected TextBox(HTMLElement element) {
    super(element);
    assertTagName("input");
    assert getElement().getAttribute("type").equalsIgnoreCase("text");
  }

  TextBox(HTMLElement element, String styleName) {
    super(element);
    if (styleName != null) {
      setStyleName(styleName);
    }
  }

  /**
   * Gets the maximum allowable length of the text box.
   *
   * @return the maximum length, in characters
   */
  public int getMaxLength() {
    return getInputElement().maxLength;
  }

  /**
   * Gets the number of visible characters in the text box.
   *
   * @return the number of visible characters
   */
  public int getVisibleLength() {
    return getInputElement().size;
  }

  /**
   * Sets the maximum allowable length of the text box.
   *
   * @param length the maximum length, in characters
   */
  public void setMaxLength(int length) {
    getInputElement().maxLength = length;
  }

  /**
   * Sets the number of visible characters in the text box.
   *
   * @param length the number of visible characters
   */
  public void setVisibleLength(int length) {
    getInputElement().size = length;
  }

  private HTMLInputElement getInputElement() {
    return Js.uncheckedCast(getElement());
  }
}
