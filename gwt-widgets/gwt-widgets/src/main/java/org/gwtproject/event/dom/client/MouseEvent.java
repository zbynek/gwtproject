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

import org.gwtproject.dom.client.Element;

/**
 * Abstract class representing mouse events.
 *
 * @param <H> handler type
 */
public abstract class MouseEvent<H> extends HumanInputEvent<H> {

  /**
   * Gets the button value. NOT based on DOM MouseEvent.button, but on MouseEvent.buttons mask (Left
   * = 1, Right = 2, Middle = 4).
   *
   * <p>Note: this is unreliable for events not caused by the depression or release of a mouse
   * button.
   *
   * @return the button value
   */
  public int getNativeButton() {
    int button = getNativeEvent().button;
    if (button == 1) {
      return 4;
    } else if (button == 2) {
      return 2;
    }
    return 1;
  }

  /**
   * Gets the mouse x-position on the user's display.
   *
   * @return the mouse x-position
   */
  public int getScreenX() {
    return (int) getNativeEvent().screenX;
  }

  /**
   * Gets the mouse y-position on the user's display.
   *
   * @return the mouse y-position
   */
  public int getScreenY() {
    return (int) getNativeEvent().screenY;
  }

  public elemental2.dom.MouseEvent getNativeEvent() {
    return (elemental2.dom.MouseEvent) super.getNativeEvent();
  }

  /**
   * Gets the mouse x-position relative to the event's current target element.
   *
   * @return the relative x-position
   */
  public int getX() {
    Element relativeElem = getRelativeElement();
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
  public int getRelativeX(Element target) {
    elemental2.dom.MouseEvent e = getNativeEvent();
    return (int) e.clientX
        - target.getAbsoluteLeft()
        + target.getScrollLeft()
        + target.getOwnerDocument().getScrollLeft();
  }

  /**
   * Gets the mouse x-position within the browser window's client area.
   *
   * @return the mouse x-position
   */
  public int getClientX() {
    return (int) getNativeEvent().clientX;
  }

  /**
   * Gets the mouse y-position relative to the event's current target element.
   *
   * @return the relative y-position
   */
  public int getY() {
    Element relativeElem = getRelativeElement();
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
  public int getRelativeY(Element target) {
    elemental2.dom.MouseEvent e = getNativeEvent();
    return (int) e.clientY
        - target.getAbsoluteTop()
        + target.getScrollTop()
        + target.getOwnerDocument().getScrollTop();
  }

  /**
   * Gets the mouse y-position within the browser window's client area.
   *
   * @return the mouse y-position
   */
  public int getClientY() {
    return (int) getNativeEvent().clientY;
  }

  public boolean isAltKeyDown() {
    return getNativeEvent().altKey;
  }

  @Override
  public boolean isControlKeyDown() {
    return getNativeEvent().ctrlKey;
  }

  @Override
  public boolean isMetaKeyDown() {
    return getNativeEvent().metaKey;
  }

  @Override
  public boolean isShiftKeyDown() {
    return getNativeEvent().shiftKey;
  }
}
