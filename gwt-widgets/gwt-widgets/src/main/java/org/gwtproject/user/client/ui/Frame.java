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
import elemental2.dom.HTMLFrameElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.HasLoadHandlers;
import org.gwtproject.event.dom.client.LoadEvent;
import org.gwtproject.event.dom.client.LoadHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.safehtml.shared.SafeUri;
import org.gwtproject.safehtml.shared.annotations.IsTrustedResourceUri;
import org.gwtproject.user.client.DOM;

/**
 * A widget that wraps an IFRAME element, which can contain an arbitrary web site.
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-Frame { }
 * </ul>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.FrameExample}
 */
public class Frame extends Widget implements HasLoadHandlers {

  static final String DEFAULT_STYLENAME = "gwt-Frame";

  /**
   * Creates a Frame widget that wraps an existing &lt;frame&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static Frame wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    Frame frame = new Frame(element);

    // Mark it attached and remember it for cleanup.
    frame.onAttach();
    RootPanel.detachOnWindowClose(frame);

    return frame;
  }

  /** Creates an empty frame. */
  public Frame() {
    setElement(DOM.createIFrame());
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Creates a frame that displays the resource at the specified URL.
   *
   * @param url the URL of the resource to be displayed
   */
  public Frame(@IsTrustedResourceUri String url) {
    this();
    setUrl(url);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be an &lt;iframe&gt; element.
   *
   * @param element the element to be used
   */
  protected Frame(HTMLElement element) {
    setElement(element);
    assertTagName("iframe");
  }

  /**
   * Adds a {@link LoadEvent} load handler which will be called when the frame loads.
   *
   * @param handler the load handler
   * @return {@link HandlerRegistration} that can be used to remove this handler
   */
  public HandlerRegistration addLoadHandler(LoadHandler handler) {
    return addDomHandler(handler, LoadEvent.getType());
  }

  /**
   * Gets the URL of the frame's resource.
   *
   * @return the frame's URL
   */
  public String getUrl() {
    return getFrameElement().src;
  }

  /**
   * Sets the URL of the resource to be displayed within the frame.
   *
   * @param url the frame's new URL
   */
  public void setUrl(@IsTrustedResourceUri String url) {
    getFrameElement().src = url;
  }

  /**
   * Sets the URL of the resource to be displayed within the frame.
   *
   * @param url the frame's new URL
   */
  public void setUrl(@IsTrustedResourceUri SafeUri url) {
    getFrameElement().src = url.asString();
  }

  private HTMLFrameElement getFrameElement() {
    return Js.uncheckedCast(getElement());
  }
}
