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
package org.gwtproject.user.client.impl;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.EventListener;

/** Native implementation associated with {@link DOM}. */
public abstract class DOMImpl {

  protected static boolean eventSystemIsInitialized;

  public static EventListener getEventListener(HTMLElement elem) {
    JsPropertyMap _this = Js.asPropertyMap(elem);
    if (_this.has("__listener")) {
      EventListener maybeListener = Js.uncheckedCast(_this.get("__listener"));
      return isMyListener(maybeListener) ? maybeListener : null;
    }
    return null;
  }

  public static void setEventListener(HTMLElement elem, EventListener listener) {
    Js.asPropertyMap(elem).set("__listener", listener);
  }

  /**
   * Returns <code>true</code>if the object is an instance of EventListener and the object belongs
   * to this module.
   *
   * <p>Note that this method should only be called from JSNI, otherwise it can be inlined and
   * compiler can remove instanceOf checks. E.g.
   *
   * <pre>
   * EventListener listener = getEventListenerFromSomeJsniCode();
   * if (isMyListener(listener)) {
   *   // This block will always be executed because the compiler proves that the instance of checks
   *   // are not required after inlining isMyListener.
   * }
   * </pre>
   */
  private static boolean isMyListener(Object object) {
    return (object instanceof org.gwtproject.user.client.EventListener);
  }

  public void eventCancelBubble(Event event, boolean cancel) {
    if (cancel == false) {
      event.stopPropagation();
    }
  }

  public abstract HTMLElement eventGetFromElement(Event evt);

  public boolean eventGetRepeat(Event evt) {
    return Js.asPropertyMap(evt).has("repeat");
  }

  public abstract HTMLElement eventGetToElement(Event evt);

  public final int eventGetTypeInt(Event evt) {
    return eventGetTypeInt(evt.getType());
  }

  public int eventGetTypeInt(String eventType) {
    switch (eventType) {
      case "blur":
        return 0x01000;
      case "change":
        return 0x00400;
      case "click":
        return 0x00001;
      case "dblclick":
        return 0x00002;
      case "focus":
        return 0x00800;
      case "keydown":
        return 0x00080;
      case "keypress":
        return 0x00100;
      case "keyup":
        return 0x00200;
      case "load":
        return 0x08000;
      case "losecapture":
        return 0x02000;
      case "mousedown":
        return 0x00004;
      case "mousemove":
        return 0x00040;
      case "mouseout":
        return 0x00020;
      case "mouseover":
        return 0x00010;
      case "mouseup":
        return 0x00008;
      case "scroll":
        return 0x04000;
      case "error":
        return 0x10000;
      case "mousewheel":
        return 0x20000;
      case "DOMMouseScroll":
        return 0x20000;
      case "contextmenu":
        return 0x40000;
      case "paste":
        return 0x80000;
      case "touchstart":
        return 0x100000;
      case "touchmove":
        return 0x200000;
      case "touchend":
        return 0x400000;
      case "touchcancel":
        return 0x800000;
      case "gesturestart":
        return 0x1000000;
      case "gesturechange":
        return 0x2000000;
      case "gestureend":
        return 0x4000000;
      default:
        return -1;
    }
  }

  public void eventSetKeyCode(Event evt, char key) {
    Js.asPropertyMap(evt).set("keyCode", key);
  }

  public abstract HTMLElement getChild(HTMLElement elem, int index);

  public abstract int getChildCount(HTMLElement elem);

  public abstract int getChildIndex(HTMLElement parent, HTMLElement child);

  public int getEventsSunk(HTMLElement elem) {
    JsPropertyMap<?> map = Js.asPropertyMap(elem);
    if (map.has("__eventBits")) {
      return map.getAsAny("__eventBits").asInt();
    }
    return 0;
  }

  public abstract void insertChild(HTMLElement parent, HTMLElement child, int index);

  public void maybeInitializeEventSystem() {
    if (!eventSystemIsInitialized) {
      initEventSystem();
      eventSystemIsInitialized = true;
    }
  }

  public abstract void releaseCapture(HTMLElement elem);

  public abstract void setCapture(HTMLElement elem);

  public abstract void sinkBitlessEvent(HTMLElement elem, String eventTypeName);

  public abstract void sinkEvents(HTMLElement elem, int eventBits);

  /** Initializes the event dispatch system. */
  protected abstract void initEventSystem();
}
