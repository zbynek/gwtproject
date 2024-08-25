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
package org.gwtproject.canvas.client;

import elemental2.core.JsObject;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLCanvasElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.ui.FocusWidget;
import org.gwtproject.user.client.ui.RootPanel;

/**
 * A widget representing a &lt;canvas&gt; element.
 *
 * <p>This widget may not be supported on all browsers.
 */
public class Canvas extends FocusWidget {

  /**
   * Return a new {@link Canvas} if supported, and null otherwise.
   *
   * @return a new {@link Canvas} if supported, and null otherwise
   */
  public static Canvas createIfSupported() {
    return new Canvas(DOM.createCanvas());
  }

  /**
   * Wrap an existing canvas element. The element must already be attached to the document. If the
   * element is removed from the document, you must call {@link RootPanel#detachNow(Widget)}. Note:
   * This method can return null if there is no support for canvas by the current browser.
   *
   * @param element the element to wrap
   * @return the {@link Canvas} widget or null if canvas is not supported by the current browser.
   */
  public static Canvas wrap(HTMLCanvasElement element) {
    if (!isSupported(element)) {
      return null;
    }
    assert DomGlobal.document.body.contains(element);
    Canvas canvas = new Canvas(element);

    // Mark it attached and remember it for cleanup.
    canvas.onAttach();
    RootPanel.detachOnWindowClose(canvas);

    return canvas;
  }

  /**
   * Runtime check for whether the canvas element is supported in this browser.
   *
   * @return whether the canvas element is supported
   */
  public static boolean isSupported() {
    return true;
  }

  private static boolean isSupported(HTMLCanvasElement element) {
    return isSupportedRunTime(element);
  }

  /** Protected constructor. Use {@link #createIfSupported()} to create a Canvas. */
  private Canvas(HTMLCanvasElement element) {
    setElement(element);
  }

  /**
   * Returns the attached Canvas Element.
   *
   * @return the Canvas Element
   */
  public HTMLCanvasElement getCanvasElement() {
    return Js.uncheckedCast(this.getElement());
  }

  /**
   * Gets the rendering context that may be used to draw on this canvas.
   *
   * @param contextId the context id as a String
   * @return the canvas rendering context
   */
  public JsObject getContext(String contextId) {
    return getCanvasElement().getContext(contextId);
  }

  /**
   * Returns a 2D rendering context.
   *
   * <p>This is a convenience method, see {@link #getContext(String)}.
   *
   * @return a 2D canvas rendering context
   */
  public CanvasRenderingContext2D getContext2d() {
    return Js.uncheckedCast(getCanvasElement().getContext("2d"));
  }

  /**
   * Gets the height of the internal canvas coordinate space.
   *
   * @return the height, in pixels
   * @see #setCoordinateSpaceHeight(int)
   */
  public int getCoordinateSpaceHeight() {
    return getCanvasElement().height;
  }

  /**
   * Gets the width of the internal canvas coordinate space.
   *
   * @return the width, in pixels
   * @see #setCoordinateSpaceWidth(int)
   */
  public int getCoordinateSpaceWidth() {
    return getCanvasElement().width;
  }

  /**
   * Sets the height of the internal canvas coordinate space.
   *
   * @param height the height, in pixels
   * @see #getCoordinateSpaceHeight()
   */
  public void setCoordinateSpaceHeight(int height) {
    getCanvasElement().height = height;
  }

  /**
   * Sets the width of the internal canvas coordinate space.
   *
   * @param width the width, in pixels
   * @see #getCoordinateSpaceWidth()
   */
  public void setCoordinateSpaceWidth(int width) {
    getCanvasElement().width = width;
  }

  /**
   * Returns a data URL for the current content of the canvas element.
   *
   * @return a data URL for the current content of this element.
   */
  public String toDataUrl() {
    return getCanvasElement().toDataURL();
  }

  /**
   * Returns a data URL for the current content of the canvas element, with a specified type.
   *
   * @param type the type of the data url, e.g., image/jpeg or image/png.
   * @return a data URL for the current content of this element with the specified type.
   */
  public String toDataUrl(String type) {
    return getCanvasElement().toDataURL(type);
  }

  /**
   * Using a run-time check, return true if the {@link HTMLCanvasElement} is supported.
   *
   * @return true if supported, false otherwise.
   */
  // TODO: probably safe to assume that everyone supports Canvas
  private static boolean isSupportedRunTime(HTMLCanvasElement element) {
    return ((JsPropertyMap) element).has("getContext");
  }
}
