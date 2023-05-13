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
import elemental2.dom.HTMLTextAreaElement;
import jsinterop.base.Js;
import org.gwtproject.user.client.DOM;

/**
 * A text box that allows multiple lines of text to be entered.
 *
 * <p><img class='gallery' src='doc-files/TextArea.png'/>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-TextArea { primary style }
 *   <li>.gwt-TextArea-readonly { dependent style set when the text area is read-only }
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
public class TextArea extends TextBoxBase {

  /**
   * Creates a TextArea widget that wraps an existing &lt;textarea&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static TextArea wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    TextArea textArea = new TextArea(element);

    // Mark it attached and remember it for cleanup.
    textArea.onAttach();
    RootPanel.detachOnWindowClose(textArea);

    return textArea;
  }

  /** Creates an empty text area. */
  public TextArea() {
    super(DOM.createTextArea());
    setStyleName("gwt-TextArea");
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be a &lt;textarea&gt; element.
   *
   * @param element the element to be used
   */
  protected TextArea(HTMLElement element) {
    super(element);
    assertTagName("textarea");
  }

  /**
   * Gets the requested width of the text box (this is not an exact value, as not all characters are
   * created equal).
   *
   * @return the requested width, in characters
   */
  public int getCharacterWidth() {
    return getTextAreaElement().cols;
  }

  @Override
  public int getCursorPos() {
    return getImpl().getTextAreaCursorPos(getElement());
  }

  @Override
  public int getSelectionLength() {
    return getImpl().getTextAreaSelectionLength(getElement());
  }

  /**
   * Gets the number of text lines that are visible.
   *
   * @return the number of visible lines
   */
  public int getVisibleLines() {
    return getTextAreaElement().rows;
  }

  /**
   * Sets the requested width of the text box (this is not an exact value, as not all characters are
   * created equal).
   *
   * @param width the requested width, in characters
   */
  public void setCharacterWidth(int width) {
    getTextAreaElement().cols = width;
  }

  /**
   * Sets the number of text lines that are visible.
   *
   * @param lines the number of visible lines
   */
  public void setVisibleLines(int lines) {
    getTextAreaElement().rows = lines;
  }

  private HTMLTextAreaElement getTextAreaElement() {
    return Js.uncheckedCast(getElement());
  }
}
