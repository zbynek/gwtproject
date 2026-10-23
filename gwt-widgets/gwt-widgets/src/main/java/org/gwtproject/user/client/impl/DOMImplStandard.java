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

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import elemental2.dom.EventTarget;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.Node;
import org.gwtproject.user.client.DOM;

/**
 * Base implementation of {@link org.gwtproject.user.client.impl.DOMImpl} shared by those browsers
 * that come a bit closer to supporting a common standard (ie, not legacy IEs).
 */
public class DOMImplStandard extends DOMImpl {

  private static Element captureElem;
  private static JsPropertyMap<EventListener> bitlessEventDispatchers =
      getBitlessEventDispatchers();
  private static JsPropertyMap<EventListener> captureEventDispatchers =
      getCaptureEventDispatchers();

  private static void ensureInit() {
    if (eventSystemIsInitialized) {
      throw new IllegalStateException("Event system already initialized");
    }

    // Ensure that any default extensions for the browser is registered via
    // static initializers in deferred binding of DOMImpl:
    new DOMImplStandard();
  }

  public static void dispatchEvent(elemental2.dom.Event evt) {
    Element element = getFirstAncestorWithListener(evt);
    if (element == null) {
      return;
    }
    DOM.dispatchEvent(evt, element.getNodeType() != 1 ? null : element, getEventListener(element));
  }

  private static Element getFirstAncestorWithListener(elemental2.dom.Event evt) {
    Element curElem = Js.uncheckedCast(evt.currentTarget);
    while (curElem != null && getEventListener(curElem) == null) {
      curElem = curElem.getParentNode().cast();
    }
    return curElem;
  }

  private static void dispatchDragEvent(elemental2.dom.Event evt) {
    // Some drag events must call preventDefault to prevent native text selection.
    evt.preventDefault();
    dispatchEvent(evt);
  }

  private static void dispatchUnhandledEvent(elemental2.dom.Event evt) {
    Element element = Js.uncheckedCast(evt.currentTarget);
    element.setPropertyString("__gwtLastUnhandledEvent", evt.type);
    dispatchEvent(evt);
  }

  private static void dispatchCapturedEvent(elemental2.dom.Event evt) {
    DOM.previewEvent(evt);
  }

  private static void dispatchCapturedMouseEvent(elemental2.dom.Event evt) {
    boolean cancelled = !DOM.previewEvent(evt);
    if (cancelled || captureElem == null) {
      return;
    }
    if (DOM.dispatchEvent(evt, captureElem)) {
      evt.stopPropagation();
    }
  }

  private static JsPropertyMap<EventListener> getBitlessEventDispatchers() {
    JsPropertyMap<EventListener> map = JsPropertyMap.of();
    map.set("_default_", (event) -> dispatchEvent(event));
    map.set("dragenter", (event) -> dispatchDragEvent(event));
    map.set("dragover", (event) -> dispatchDragEvent(event));
    return map;
  }

  private static JsPropertyMap<EventListener> getCaptureEventDispatchers() {
    JsPropertyMap<EventListener> map = JsPropertyMap.of();
    // Mouse events
    map.set("click", (event) -> dispatchCapturedMouseEvent(event));
    map.set("dblclick", (event) -> dispatchCapturedMouseEvent(event));
    map.set("mousedown", (event) -> dispatchCapturedMouseEvent(event));
    map.set("mouseup", (event) -> dispatchCapturedMouseEvent(event));
    map.set("mousemove", (event) -> dispatchCapturedMouseEvent(event));
    map.set("mouseover", (event) -> dispatchCapturedMouseEvent(event));
    map.set("mouseout", (event) -> dispatchCapturedMouseEvent(event));
    map.set("mousewheel", (event) -> dispatchCapturedMouseEvent(event));
    // Keyboard events
    map.set("keydown", (event) -> dispatchCapturedEvent(event));
    map.set("keyup", (event) -> dispatchCapturedEvent(event));
    map.set("keypress", (event) -> dispatchCapturedEvent(event));
    // Touch events
    map.set("touchstart", (event) -> dispatchCapturedMouseEvent(event));
    map.set("touchend", (event) -> dispatchCapturedMouseEvent(event));
    map.set("touchmove", (event) -> dispatchCapturedMouseEvent(event));
    map.set("touchcancel", (event) -> dispatchCapturedMouseEvent(event));
    map.set("gesturestart", (event) -> dispatchCapturedMouseEvent(event));
    map.set("gestureend", (event) -> dispatchCapturedMouseEvent(event));
    map.set("gesturechange", (event) -> dispatchCapturedMouseEvent(event));
    return map;
  }

  @Override
  public Element eventGetFromElement(elemental2.dom.Event evt) {
    if (evt.type.equals(BrowserEvents.MOUSEOVER)) {
      return Js.uncheckedCast(((MouseEvent) evt).relatedTarget);
    }
    if (evt.type.equals(BrowserEvents.MOUSEOUT)) {
      return Js.uncheckedCast(evt.target);
    }
    return null;
  }

  @Override
  public Element eventGetToElement(elemental2.dom.Event evt) {
    if (evt.type.equals(BrowserEvents.MOUSEOVER)) {
      return Js.uncheckedCast(evt.target);
    }

    if (evt.type.equals(BrowserEvents.MOUSEOUT)) {
      return Js.uncheckedCast(((MouseEvent) evt).relatedTarget);
    }

    return null;
  }

  @Override
  public Element getChild(Element elem, int index) {
    int count = 0;
    Node child = elem.getFirstChild();
    while (child != null) {
      if (child.getNodeType() == 1) {
        if (index == count) {
          return (Element) child;
        }
        ++count;
      }
      child = child.getNextSibling();
    }
    return null;
  }

  @Override
  public int getChildCount(Element elem) {
    int count = 0;
    Node child = elem.getFirstChild();
    while (child != null) {
      if (child.getNodeType() == 1) {
        ++count;
      }
      child = child.getNextSibling();
    }
    return count;
  }

  @Override
  public int getChildIndex(Element parent, Element toFind) {
    int count = 0;
    Node child = parent.getFirstChild();
    while (child != null) {
      if (child == toFind) {
        return count;
      }
      if (child.getNodeType() == 1) {
        ++count;
      }

      child = child.getNextSibling();
    }
    return -1;
  }

  @Override
  public void insertChild(Element parent, Element toAdd, int index) {
    int count = 0;
    Node child = parent.getFirstChild();
    Node before = null;
    while (child != null) {
      if (child.getNodeType() == 1) {
        if (count == index) {
          before = child;
          break;
        }
        ++count;
      }
      child = child.getNextSibling();
    }
    parent.insertBefore(toAdd, before);
  }

  @Override
  public void releaseCapture(Element elem) {
    maybeInitializeEventSystem();
    if (captureElem == elem) {
      captureElem = null;
    }
  }

  @Override
  public void setCapture(Element elem) {
    maybeInitializeEventSystem();
    captureElem = elem;
  }

  @Override
  public void sinkBitlessEvent(Element elem, String eventTypeName) {
    maybeInitializeEventSystem();
    sinkBitlessEventImpl(elem, eventTypeName);
  }

  @Override
  public void sinkEvents(Element elem, int bits) {
    maybeInitializeEventSystem();
    sinkEventsImpl(elem, bits);
  }

  @Override
  protected void initEventSystem() {
    captureEventDispatchers.forEach(
        (key) -> DomGlobal.window.addEventListener(key, captureEventDispatchers.get(key), true));
  }

  protected void sinkBitlessEventImpl(Element elem, String eventTypeName) {
    JsPropertyMap<EventListener> map = bitlessEventDispatchers;
    Object dispatcher;
    if (map.has(eventTypeName)) {
      dispatcher = map.get(eventTypeName);
    } else {
      dispatcher = map.get("_default_");
    }

    ((EventTarget) Js.uncheckedCast(elem))
        .addEventListener(eventTypeName, Js.uncheckedCast(dispatcher), false);
  }

  protected void sinkEventsImpl(Element elem, int bits) {
    JsPropertyMap<EventListener> map = Js.uncheckedCast(elem);
    int chMask =
        (map.has("__eventBits") ? Integer.parseInt(map.get("__eventBits").toString()) : 0) ^ bits;
    Js.asPropertyMap(elem).set("__eventBits", bits);

    if ((chMask & 0x00001) != 0) {
      map.set("onclick", ((bits & 0x00001) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00002) != 0) {
      map.set("ondblclick", ((bits & 0x00002) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00004) != 0) {
      map.set("onmousedown", ((bits & 0x00004) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00008) != 0) {
      map.set("onmouseup", ((bits & 0x00008) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00010) != 0) {
      map.set("onmouseover", ((bits & 0x00010) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00020) != 0) {
      map.set("onmouseout", ((bits & 0x00020) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00040) != 0) {
      map.set("onmousemove", ((bits & 0x00040) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00080) != 0) {
      map.set("onkeydown", ((bits & 0x00080) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00100) != 0) {
      map.set("onkeypress", ((bits & 0x00100) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00200) != 0) {
      map.set("onkeyup", ((bits & 0x00200) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00400) != 0) {
      map.set("onchange", ((bits & 0x00400) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x00800) != 0) {
      map.set("onfocus", ((bits & 0x00400) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x01000) != 0) {
      map.set("onblur", ((bits & 0x01000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x02000) != 0) {
      map.set("onlosecapture", ((bits & 0x02000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x04000) != 0) {
      map.set("onscroll", ((bits & 0x04000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x08000) != 0) {
      map.set("onload", ((bits & 0x08000) != 0) ? event -> dispatchUnhandledEvent(event) : null);
    }

    if ((chMask & 0x10000) != 0) {
      map.set("onerror", ((bits & 0x10000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x20000) != 0) {
      map.set("onwheel", ((bits & 0x20000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x40000) != 0) {
      map.set("oncontextmenu", ((bits & 0x40000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x80000) != 0) {
      map.set("onpaste", ((bits & 0x80000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x100000) != 0) {
      map.set("ontouchstart", ((bits & 0x100000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x200000) != 0) {
      map.set("ontouchmove", ((bits & 0x200000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x400000) != 0) {
      map.set("ontouchend", ((bits & 0x400000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x800000) != 0) {
      map.set("ontouchcancel", ((bits & 0x800000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x1000000) != 0) {
      map.set("ongesturestart", ((bits & 0x1000000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x2000000) != 0) {
      map.set("ongesturechange", ((bits & 0x2000000) != 0) ? event -> dispatchEvent(event) : null);
    }

    if ((chMask & 0x4000000) != 0) {
      map.set("ongestureend", ((bits & 0x4000000) != 0) ? event -> dispatchEvent(event) : null);
    }
  }
}
