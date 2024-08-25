/*
 * Copyright © 2019 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.event.dom.client;


import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.user.client.DOM;

/**
 * Abstract class representing mouse events.
 *
 * @param <H> handler type
 */
public abstract class MouseEvent<H> extends HumanInputEvent<H> {

  /**
   * Gets the button value.
   *
   * <p>Note: this is unreliable for events not caused by the depression or release of a mouse
   * button.
   *
   * @return the button value
   */
  public int getNativeButton() {
    return getNativeMouseEvent().button;
  }

  private elemental2.dom.MouseEvent getNativeMouseEvent() {
    return Js.uncheckedCast(getNativeEvent());
  }

  /**
   * Gets the mouse x-position on the user's display.
   *
   * @return the mouse x-position
   */
  public int getScreenX() {
    return (int) getNativeMouseEvent().screenX;
  }

  /**
   * Gets the mouse y-position on the user's display.
   *
   * @return the mouse y-position
   */
  public int getScreenY() {
    return (int) getNativeMouseEvent().screenY;
  }

  /**
   * Gets the mouse x-position relative to the event's current target element.
   *
   * @return the relative x-position
   */
  public int getX() {
    HTMLElement relativeElem = getRelativeElement();
    if (relativeElem != null) {
      return getRelativeX(relativeElem);
    }
    return getClientX();
  }

  /**
   * Gets the mouse x-position relative to a given element.
   *
   * @param target the element whose coordinate system is to be used
   * @return the relative x-position
   */
  public int getRelativeX(HTMLElement target) {
    elemental2.dom.MouseEvent e = getNativeMouseEvent();
    return (int) (e.clientX
        - DOM.getAbsoluteLeft(target)
        + target.scrollLeft
        + target.ownerDocument.scrollingElement.scrollLeft);
  }

  /**
   * Gets the mouse x-position within the browser window's client area.
   *
   * @return the mouse x-position
   */
  public int getClientX() {
    return (int) getNativeMouseEvent().clientX;
  }

  /**
   * Gets the mouse y-position relative to the event's current target element.
   *
   * @return the relative y-position
   */
  public int getY() {
    HTMLElement relativeElem = getRelativeElement();
    if (relativeElem != null) {
      return getRelativeY(relativeElem);
    }
    return getClientY();
  }

  /**
   * Gets the mouse y-position relative to a given element.
   *
   * @param target the element whose coordinate system is to be used
   * @return the relative y-position
   */
  public int getRelativeY(HTMLElement target) {
    elemental2.dom.MouseEvent e = getNativeMouseEvent();
    return (int) (e.clientY
        - DOM.getAbsoluteTop(target)
        + target.scrollTop
        + target.ownerDocument.scrollingElement.scrollTop);
  }

  /**
   * Gets the mouse y-position within the browser window's client area.
   *
   * @return the mouse y-position
   */
  public int getClientY() {
    return (int) getNativeMouseEvent().clientY;
  }
}
