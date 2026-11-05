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

import org.gwtproject.user.client.DOM;

/**
 * Key up and key down are both events based upon a given key code.
 *
 * @param <H> handler type
 */
public abstract class KeyCodeEvent<H> extends KeyEvent<H> {

  /**
   * Is this a key down arrow?
   *
   * @return whether this is a down arrow key event
   */
  public boolean isDownArrow() {
    return "ArrowDown".equals(getNativeEvent().code);
  }

  /**
   * Gets the native key code.
   *
   * @return the key code
   * @deprecated use {@link #getNativeEvent} together with {@link elemental2.dom.KeyboardEvent#code}
   *     instead
   */
  @Deprecated
  public int getNativeKeyCode() {
    return DOM.getKeyCode(getNativeEvent());
  }

  /**
   * Is this a left arrow?
   *
   * @return whether this is a left arrow key event
   */
  public boolean isLeftArrow() {
    return "ArrowLeft".equals(getNativeEvent().code);
  }

  /**
   * Is this a right arrow?
   *
   * @return whether this is a right arrow key event
   */
  public boolean isRightArrow() {
    return "ArrowRight".equals(getNativeEvent().code);
  }

  /**
   * Is this a up arrow?
   *
   * @return whether this is a right arrow key event
   */
  public boolean isUpArrow() {
    return "ArrowUp".equals(getNativeEvent().code);
  }

  @Override
  public String toDebugString() {
    return super.toDebugString() + "[" + getNativeEvent().code + "]";
  }
}
