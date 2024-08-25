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
package org.gwtproject.user.cellview.client;

import java.util.HashSet;
import java.util.Set;

import elemental2.dom.HTMLElement;
import jsinterop.annotations.JsFunction;
import jsinterop.base.Js;
import org.gwtproject.event.shared.BrowserEvents;
import elemental2.dom.EventTarget;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.EventListener;
import org.gwtproject.user.client.ui.Widget;

/** Standard implementation used by most cell based widgets. */
public class CellBasedWidgetImplStandard extends CellBasedWidgetImpl {

  /** The method used to dispatch non-bubbling events. */
  private static FnWithOneArg dispatchNonBubblingEvent;

  /**
   * Handle an event from a cell. Used by {@link #initEventSystem()}.
   *
   * @param event the event to handle.
   */
  private static void handleNonBubblingEvent(elemental2.dom.Event event) {
    // Get the event target.
    EventTarget eventTarget = event.target;
    if (!DOM.isElement(eventTarget)) {
      return;
    }
    HTMLElement target = Js.uncheckedCast(eventTarget);

    // Get the event listener, which is the first widget that handles the
    // specified event type.
    String typeName = event.type;
    EventListener listener = DOM.getEventListener(target);
    while (target != null && listener == null) {
      target = Js.uncheckedCast(target.parentElement);
      if (target != null && isNonBubblingEventHandled(target, typeName)) {
        // The target handles the event, so this must be the event listener.
        listener = DOM.getEventListener(target);
      }
    }

    // Fire the event.
    if (listener != null) {
      DOM.dispatchEvent(event, target, listener);
    }
  }

  /**
   * Check if the specified element handles the a non-bubbling event.
   *
   * @param elem the element to check
   * @param typeName the non-bubbling event
   * @return true if the event is handled, false if not
   */
  private static boolean isNonBubblingEventHandled(HTMLElement elem, String typeName) {
    return "true".equals(elem.getAttribute("__gwtCellBasedWidgetImplDispatching" + typeName));
  }

  /** The set of non bubbling event types. */
  private final Set<String> nonBubblingEvents;

  public CellBasedWidgetImplStandard() {
    // Initialize the set of non-bubbling events.
    nonBubblingEvents = new HashSet<>();
    nonBubblingEvents.add(BrowserEvents.FOCUS);
    nonBubblingEvents.add(BrowserEvents.BLUR);
    nonBubblingEvents.add(BrowserEvents.LOAD);
    nonBubblingEvents.add(BrowserEvents.ERROR);
  }

  @Override
  protected int sinkEvent(Widget widget, String typeName) {
    if (nonBubblingEvents.contains(typeName)) {
      // Initialize the event system.
      if (dispatchNonBubblingEvent == null) {
        initEventSystem();
      }

      // Sink the non-bubbling event.
      HTMLElement elem = widget.getElement();
      if (!isNonBubblingEventHandled(elem, typeName)) {
        elem.setAttribute("__gwtCellBasedWidgetImplDispatching" + typeName, "true");
        sinkEventImpl(elem, typeName);
      }
      return -1;
    } else {
      return super.sinkEvent(widget, typeName);
    }
  }

  /** Initialize the event system. */
  private void initEventSystem() {
    dispatchNonBubblingEvent =
        Js.uncheckedCast((FnWithOneArg) (event) -> handleNonBubblingEvent(Js.uncheckedCast(event)));
  }

  /**
   * Sink an event on the element.
   *
   * @param elem the element to sink the event on
   * @param typeName the name of the event to sink
   */
  private void sinkEventImpl(HTMLElement elem, String typeName) {
    ((elemental2.dom.EventTarget) Js.uncheckedCast(elem))
        .addEventListener(typeName, Js.uncheckedCast(dispatchNonBubblingEvent), true);
  }

  @FunctionalInterface
  @JsFunction
  interface FnWithNoArg {
    void onInvoke();
  }

  @FunctionalInterface
  @JsFunction
  interface FnWithOneArg {
    void onInvoke(Object key);
  }
}
