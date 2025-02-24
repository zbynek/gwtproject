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

import jsinterop.base.JsPropertyMap;
import org.gwtproject.canvas.dom.client.Context;
import org.gwtproject.canvas.dom.client.Context2d;
import org.gwtproject.dom.client.CanvasElement;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.PartialSupport;
import org.gwtproject.user.client.ui.FocusWidget;
import org.gwtproject.user.client.ui.RootPanel;

/**
 * A widget representing a &lt;canvas&gt; element.
 *
 * <p>This widget may not be supported on all browsers.
 */
@PartialSupport
public class Canvas extends FocusWidget {

  /**
   * Return a new {@link Canvas} if supported, and null otherwise.
   *
   * @return a new {@link Canvas} if supported, and null otherwise
   */
  public static Canvas createIfSupported() {
    CanvasElement element = Document.get().createCanvasElement();
    if (!isSupportedRunTime(element)) {
      return null;
    }
    return new Canvas(element);
  }

  /**
   * Wrap an existing canvas element. The element must already be attached to the document. If the
   * element is removed from the document, you must call {@link RootPanel#detachNow(Widget)}. Note:
   * This method can return null if there is no support for canvas by the current browser.
   *
   * @param element the element to wrap
   * @return the {@link Canvas} widget or null if canvas is not supported by the current browser.
   */
  public static Canvas wrap(CanvasElement element) {
    if (!isSupported(element)) {
      return null;
    }
    assert Document.get().getBody().isOrHasChild(element);
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
    return isSupported(Document.get().createCanvasElement());
  }

  private static boolean isSupported(CanvasElement element) {
    return isSupportedRunTime(element);
  }

  /** Protected constructor. Use {@link #createIfSupported()} to create a Canvas. */
  private Canvas(CanvasElement element) {
    setElement(element);
  }

  /**
   * Returns the attached Canvas Element.
   *
   * @return the Canvas Element
   */
  public CanvasElement getCanvasElement() {
    return this.getElement().cast();
  }

  /**
   * Gets the rendering context that may be used to draw on this canvas.
   *
   * @param contextId the context id as a String
   * @return the canvas rendering context
   */
  public Context getContext(String contextId) {
    return getCanvasElement().getContext(contextId);
  }

  /**
   * Returns a 2D rendering context.
   *
   * <p>This is a convenience method, see {@link #getContext(String)}.
   *
   * @return a 2D canvas rendering context
   */
  public Context2d getContext2d() {
    return getCanvasElement().getContext2d();
  }

  /**
   * Gets the height of the internal canvas coordinate space.
   *
   * @return the height, in pixels
   * @see #setCoordinateSpaceHeight(int)
   */
  public int getCoordinateSpaceHeight() {
    return getCanvasElement().getHeight();
  }

  /**
   * Gets the width of the internal canvas coordinate space.
   *
   * @return the width, in pixels
   * @see #setCoordinateSpaceWidth(int)
   */
  public int getCoordinateSpaceWidth() {
    return getCanvasElement().getWidth();
  }

  /**
   * Sets the height of the internal canvas coordinate space.
   *
   * @param height the height, in pixels
   * @see #getCoordinateSpaceHeight()
   */
  public void setCoordinateSpaceHeight(int height) {
    getCanvasElement().setHeight(height);
  }

  /**
   * Sets the width of the internal canvas coordinate space.
   *
   * @param width the width, in pixels
   * @see #getCoordinateSpaceWidth()
   */
  public void setCoordinateSpaceWidth(int width) {
    getCanvasElement().setWidth(width);
  }

  /**
   * Returns a data URL for the current content of the canvas element.
   *
   * @return a data URL for the current content of this element.
   */
  public String toDataUrl() {
    return getCanvasElement().toDataUrl();
  }

  /**
   * Returns a data URL for the current content of the canvas element, with a specified type.
   *
   * @param type the type of the data url, e.g., image/jpeg or image/png.
   * @return a data URL for the current content of this element with the specified type.
   */
  public String toDataUrl(String type) {
    return getCanvasElement().toDataUrl(type);
  }

  /**
   * Using a run-time check, return true if the {@link CanvasElement} is supported.
   *
   * @return true if supported, false otherwise.
   */
  // TODO: probably safe to assume that everyone supports Canvas
  private static boolean isSupportedRunTime(CanvasElement element) {
    return ((JsPropertyMap) element).has("getContext");
  }
}
