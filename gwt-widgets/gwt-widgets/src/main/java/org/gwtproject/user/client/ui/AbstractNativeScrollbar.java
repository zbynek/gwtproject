/*
 * Copyright 2011 Google Inc.
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

import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import org.gwtproject.event.dom.client.HasScrollHandlers;
import org.gwtproject.event.dom.client.ScrollEvent;
import org.gwtproject.event.dom.client.ScrollHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;

/** Abstract parent class for scrollbars implemented using the native browser scrollbars. */
public abstract class AbstractNativeScrollbar extends Widget implements HasScrollHandlers {

  private static int nativeHeight = -1;
  private static int nativeWidth = -1;
  private static boolean nativeRtl = false;

  /**
   * Get the height of a native horizontal scrollbar.
   *
   * <p>This method assumes that all native scrollbars on the page have the same height.
   *
   * @return the height in pixels
   */
  public static int getNativeScrollbarHeight() {
    maybeRecalculateNativeScrollbarSize();
    return nativeHeight;
  }

  /**
   * Get the width of a native vertical scrollbar.
   *
   * <p>This method assumes that all native vertical scrollbars on the page have the same width.
   *
   * @return the height in pixels
   */
  public static int getNativeScrollbarWidth() {
    maybeRecalculateNativeScrollbarSize();
    return nativeWidth;
  }

  /**
   * Check whether or not the native vertical scrollbar is aligned on the left side of the
   * scrollable element in RTL mode.
   *
   * @return true if left aligned, false if not
   */
  public static boolean isScrollbarLeftAlignedInRtl() {
    maybeRecalculateNativeScrollbarSize();
    return nativeRtl;
  }

  /** Recalculate the height and width of a native scrollbar. */
  private static void maybeRecalculateNativeScrollbarSize() {
    // Check if the size has already been calculated.
    if (nativeHeight > -1) {
      return;
    }

    // Create a scrollable element and attach it to the body.
    HTMLElement scrollable = DOM.createDiv();
    scrollable.style.position = "absolute";
    scrollable.style.top = "-1000px";
    scrollable.style.left = "-1000px";
    scrollable.style.height = CSSProperties.HeightUnionType.of("100px");
    scrollable.style.width = CSSProperties.WidthUnionType.of("100px");
    scrollable.style.overflow = "scroll";
    scrollable.style.setProperty("direction", "rtl");
    DomGlobal.document.body.appendChild(scrollable);

    // Add some content.
    HTMLElement content = DOM.createDiv();
    content.textContent = "content";
    scrollable.appendChild(content);

    // Measure the height and width.
    nativeHeight = scrollable.offsetHeight - scrollable.clientHeight;
    nativeWidth = scrollable.offsetWidth - scrollable.clientWidth;
    nativeRtl = (DOM.getAbsoluteLeft(content) > DOM.getAbsoluteLeft(scrollable));

    // Detach the scrollable element.
    scrollable.remove();
  }

  public HandlerRegistration addScrollHandler(ScrollHandler handler) {
    // Sink the event on the scrollable element, not the root element.
    Event.sinkEvents(getScrollableElement(), Event.ONSCROLL);
    return addHandler(handler, ScrollEvent.getType());
  }

  /**
   * Get the scrollable element.
   *
   * @return the scrollable element
   */
  protected abstract HTMLElement getScrollableElement();

  @Override
  protected void onAttach() {
    super.onAttach();

    /*
     * Attach the event listener in onAttach instead of onLoad so users cannot
     * accidentally override it.
     */
    Event.setEventListener(getScrollableElement(), this);
  }

  @Override
  protected void onDetach() {
    /*
     * Detach the event listener in onDetach instead of onUnload so users cannot
     * accidentally override it.
     */
    Event.setEventListener(getScrollableElement(), null);

    super.onDetach();
  }
}
