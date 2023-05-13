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

import elemental2.core.Function;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.ClickHandler;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;
import org.gwtproject.user.client.DOM;

/**
 * A standard push-button widget.
 *
 * <p><img class='gallery' src='doc-files/Button.png'/>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-Button
 *   <dd>the outer element
 * </dl>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.ButtonExample}
 */
public class Button extends ButtonBase {

  /**
   * Creates a Button widget that wraps an existing &lt;button&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static Button wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    Button button = new Button(element);

    // Mark it attached and remember it for cleanup.
    button.onAttach();
    RootPanel.detachOnWindowClose(button);

    return button;
  }

  /** Creates a button with no caption. */
  public Button() {
    super(DOM.createButton());
    setStyleName("gwt-Button");
  }

  /**
   * Creates a button with the given HTML caption.
   *
   * @param html the HTML caption
   */
  public Button(SafeHtml html) {
    this(html.asString());
  }

  /**
   * Creates a button with the given HTML caption.
   *
   * @param html the HTML caption
   */
  public Button(@IsSafeHtml String html) {
    this();
    setHTML(html);
  }

  /**
   * Creates a button with the given HTML caption and click listener.
   *
   * @param html the html caption
   * @param handler the click handler
   */
  public Button(SafeHtml html, ClickHandler handler) {
    this(html.asString(), handler);
  }

  /**
   * Creates a button with the given HTML caption and click listener.
   *
   * @param html the HTML caption
   * @param handler the click handler
   */
  public Button(@IsSafeHtml String html, ClickHandler handler) {
    this(html);
    addClickHandler(handler);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be a &lt;button&gt; element.
   *
   * @param element the element to be used
   */
  protected Button(HTMLElement element) {
    super(element);
    assertTagName("button");
  }

  /** Programmatic equivalent of the user clicking the button. */
  public void click() {
    Function clickF = Js.uncheckedCast(Js.asPropertyMap(getElement()).get("click"));
    clickF.call(getElement());
  }

  /**
   * Get the underlying button element.
   *
   * @return the {@link HTMLButtonElement}
   */
  protected HTMLButtonElement getButtonElement() {
    return Js.uncheckedCast(getElement());
  }
}
