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

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.ScrollEvent;
import org.gwtproject.event.dom.client.ScrollHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;

/** A simple panel that wraps its contents in a scrollable area. */
public class ScrollPanel extends SimplePanel
    implements RequiresResize, ProvidesResize, HasScrolling {

  private final HTMLElement containerElem;
  private final HTMLElement scrollableElem;

  /** Creates an empty scroll panel. */
  public ScrollPanel() {
    this.scrollableElem = getElement();
    this.containerElem = DOM.createDiv();
    scrollableElem.appendChild(containerElem);
    initialize();
  }

  /**
   * Creates a new scroll panel with the given child widget.
   *
   * @param child the widget to be wrapped by the scroll panel
   */
  public ScrollPanel(Widget child) {
    this();
    setWidget(child);
  }

  /**
   * Creates an empty scroll panel using the specified root, scrollable, and container elements.
   *
   * @param root the root element of the Widget
   * @param scrollable the scrollable element, which can be the same as the root element
   * @param container the container element that holds the child
   */
  protected ScrollPanel(HTMLElement root, HTMLElement scrollable, HTMLElement container) {
    super(root);
    this.scrollableElem = scrollable;
    this.containerElem = container;
    initialize();
  }

  public HandlerRegistration addScrollHandler(ScrollHandler handler) {
    /*
     * Sink the event on the scrollable element, which may not be the root
     * element.
     */
    Event.sinkEvents(getScrollableElement(), Event.ONSCROLL);
    return addHandler(handler, ScrollEvent.getType());
  }

  /**
   * Ensures that the specified item is visible, by adjusting the panel's scroll position.
   *
   * @param item the item whose visibility is to be ensured
   */
  public void ensureVisible(UIObject item) {
    HTMLElement scroll = getScrollableElement();
    HTMLElement element = item.getElement();
    ensureVisibleImpl(scroll, element);
  }

  /**
   * Gets the horizontal scroll position.
   *
   * @return the horizontal scroll position, in pixels
   */
  public int getHorizontalScrollPosition() {
    return (int) getScrollableElement().scrollLeft;
  }

  public int getMaximumHorizontalScrollPosition() {
    return ScrollImpl.get().getMaximumHorizontalScrollPosition(getScrollableElement());
  }

  public int getMaximumVerticalScrollPosition() {
    return getScrollableElement().scrollHeight - getScrollableElement().clientHeight;
  }

  public int getMinimumHorizontalScrollPosition() {
    return ScrollImpl.get().getMinimumHorizontalScrollPosition(getScrollableElement());
  }

  public int getMinimumVerticalScrollPosition() {
    return 0;
  }

  /**
   * Gets the vertical scroll position.
   *
   * @return the vertical scroll position, in pixels
   * @deprecated as of GWT 2.3, replaced by {@link #getVerticalScrollPosition()}
   */
  @Deprecated
  public int getScrollPosition() {
    return (int) getScrollableElement().scrollTop;
  }

  public int getVerticalScrollPosition() {
    return getScrollPosition();
  }

  public void onResize() {
    Widget child = getWidget();
    if ((child != null) && (child instanceof RequiresResize)) {
      ((RequiresResize) child).onResize();
    }
  }

  /** Scroll to the bottom of this panel. */
  public void scrollToBottom() {
    setVerticalScrollPosition(getMaximumVerticalScrollPosition());
  }

  /** Scroll to the far left of this panel. */
  public void scrollToLeft() {
    setHorizontalScrollPosition(getMinimumHorizontalScrollPosition());
  }

  /** Scroll to the far right of this panel. */
  public void scrollToRight() {
    setHorizontalScrollPosition(getMaximumHorizontalScrollPosition());
  }

  /** Scroll to the top of this panel. */
  public void scrollToTop() {
    setVerticalScrollPosition(getMinimumVerticalScrollPosition());
  }

  /**
   * Sets whether this panel always shows its scroll bars, or only when necessary.
   *
   * @param alwaysShow <code>true</code> to show scroll bars at all times
   */
  public void setAlwaysShowScrollBars(boolean alwaysShow) {
    getScrollableElement().style.overflow = alwaysShow ? "scroll" : "auto";
  }

  /**
   * Sets the object's height. This height does not include decorations such as border, margin, and
   * padding.
   *
   * @param height the object's new height, in absolute CSS units (e.g. "10px", "1em" but not "50%")
   */
  @Override
  public void setHeight(String height) {
    super.setHeight(height);
  }

  /**
   * Sets the horizontal scroll position.
   *
   * @param position the new horizontal scroll position, in pixels
   */
  public void setHorizontalScrollPosition(int position) {
    getScrollableElement().scrollLeft = position;
  }

  /**
   * Sets the vertical scroll position.
   *
   * @param position the new vertical scroll position, in pixels
   * @deprecated as of GWT 2.3, replaced by {@link #setVerticalScrollPosition(int)}
   */
  @Deprecated
  public void setScrollPosition(int position) {
    getScrollableElement().scrollTop = position;
  }

   public void setVerticalScrollPosition(int position) {
    setScrollPosition(position);
  }

  /**
   * Sets the object's width. This width does not include decorations such as border, margin, and
   * padding.
   *
   * @param width the object's new width, in absolute CSS units (e.g. "10px", "1em", but not "50%")
   */
  @Override
  public void setWidth(String width) {
    super.setWidth(width);
  }

  @Override
  protected HTMLElement getContainerElement() {
    return containerElem;
  }

  /**
   * Get the scrollable element. That is the element with its overflow set to 'auto' or 'scroll'.
   *
   * @return the scrollable element
   */
  protected HTMLElement getScrollableElement() {
    return scrollableElem;
  }

  @Override
  protected void onAttach() {
    super.onAttach();

    /*
     * Attach the event listener in onAttach instead of onLoad so users cannot
     * accidentally override it. If the scrollable element is the same as the
     * root element, then we set the event listener twice (once in
     * super.onAttach() and once here), which is fine.
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

  private void ensureVisibleImpl(HTMLElement scroll, HTMLElement e) {
    if (e == null) {
      return;
    }

    HTMLElement item = e;
    int realOffset = 0;
    while (item != null && (item != scroll)) {
      realOffset += item.offsetTop;
      item = Js.uncheckedCast(item.offsetParent);
    }
    scroll.scrollTop = realOffset - scroll.offsetHeight / 2.0;
  }

  /** Initialize the widget. */
  private void initialize() {
    setAlwaysShowScrollBars(false);

    // Prevent IE standard mode bug when a AbsolutePanel is contained.
    scrollableElem.style.position = "relative";
    containerElem.style.position = "relative";

    // Hack to account for the IE6/7 scrolling bug described here:
    //
    // http://stackoverflow.com/questions/139000/div-with-overflowauto-and-a-100-wide-table-problem
    scrollableElem.style.setProperty("zoom", "1");
    containerElem.style.setProperty("zoom", "1");

    // Initialize the scrollable element.
    ScrollImpl.get().initialize(scrollableElem, containerElem);
  }
}
