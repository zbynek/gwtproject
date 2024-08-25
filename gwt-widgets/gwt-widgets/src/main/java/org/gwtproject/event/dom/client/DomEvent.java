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

import java.util.ArrayList;
import java.util.List;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import elemental2.dom.Element;
import org.gwtproject.event.shared.Event;
import org.gwtproject.event.shared.HasHandlers;

/**
 * {@link DomEvent} is a subclass of {@link Event} that provides events that underlying native
 * browser event object as well as a subclass of {@link Type} that understands GWT event bits used
 * by sinkEvents().
 *
 * @param <H> handler type
 */
public abstract class DomEvent<H> extends Event<H> implements HasNativeEvent {

  private static JsPropertyMap<List<Type<?>>> registered;
  private elemental2.dom.Event nativeEvent;
  private HTMLElement relativeElem;

  /**
   * Fires the given native event on the specified handlers.
   *
   * @param nativeEvent the native event
   * @param handlerSource the source of the handlers to fire
   */
  public static void fireNativeEvent(elemental2.dom.Event nativeEvent, HasHandlers handlerSource) {
    fireNativeEvent(nativeEvent, handlerSource, null);
  }

  /**
   * Fires the given native event on the specified handlers.
   *
   * @param nativeEvent the native event
   * @param handlerSource the source of the handlers to fire
   * @param relativeElem the element relative to which event coordinates will be measured
   */
  public static void fireNativeEvent(
      elemental2.dom.Event nativeEvent, HasHandlers handlerSource, HTMLElement relativeElem) {
    assert nativeEvent != null : "nativeEvent must not be null";
    if (registered != null) {
      List<Type<?>> types = registered.get(nativeEvent.type);
      if (types != null) {
        for (Type<?> type : types) {
          // Store and restore native event just in case we are in recursive
          // loop.
          elemental2.dom.Event currentNative = type.flyweight.nativeEvent;
          HTMLElement currentRelativeElem = type.flyweight.relativeElem;
          Object currentSource = type.flyweight.getSource();
          type.flyweight.setNativeEvent(nativeEvent);
          type.flyweight.setRelativeElement(relativeElem);
          handlerSource.fireEvent(type.flyweight);
          type.flyweight.setNativeEvent(currentNative);
          type.flyweight.setRelativeElement(currentRelativeElem);
          type.flyweight.setSource(currentSource);
        }
      }
    }
  }

  // This method can go away once we have eager clinits.
  static void init() {
    registered = Js.uncheckedCast(JsPropertyMap.of());
  }

  @Override
  public abstract Type<H> getAssociatedType();

  public final elemental2.dom.Event getNativeEvent() {
    return nativeEvent;
  }

  /**
   * Sets the native event associated with this dom event. In general, dom events should be fired
   * using the static firing methods.
   *
   * @param nativeEvent the native event
   */
  public final void setNativeEvent(elemental2.dom.Event nativeEvent) {
    this.nativeEvent = nativeEvent;
  }

  /**
   * Gets the element relative to which event coordinates will be measured. If this element is
   * <code>null</code>, event coordinates will be measured relative to the window's client area.
   *
   * @return the event's relative element
   */
  public final HTMLElement getRelativeElement() {
    return relativeElem;
  }

  /**
   * Gets the element relative to which event coordinates will be measured.
   *
   * @param relativeElem the event's relative element
   */
  public void setRelativeElement(HTMLElement relativeElem) {
    this.relativeElem = relativeElem;
  }

  /** Prevents the wrapped native event's default action. */
  public void preventDefault() {
    nativeEvent.preventDefault();
  }

  /** Stops the propagation of the underlying native event. */
  public void stopPropagation() {
    nativeEvent.stopPropagation();
  }

  /**
   * Type class used by dom event subclasses. Type is specialized for dom in order to carry
   * information about the native event.
   *
   * @param <H> handler type
   */
  public static class Type<H> extends Event.Type<H> {

    private DomEvent<H> flyweight;
    private String name;

    /**
     * This constructor allows dom event types to be triggered by the
     * fireNativeEvent method. It should only be used by
     * implementors supporting new dom events.
     *
     * <p>Any such dom event type must act as a flyweight around a native event object.
     *
     * @param eventName the raw native event name
     * @param flyweight the instance that will be used as a flyweight to wrap a native event
     */
    public Type(String eventName, DomEvent<H> flyweight) {
      this.flyweight = flyweight;
      // Until we have eager clinits implemented, we are manually initializing
      // DomEvent here.
      if (registered == null) {
        init();
      }
      List<Type<?>> types = registered.get(eventName);
      if (types == null) {
        types = new ArrayList<Type<?>>();
        registered.set(eventName, types);
      }
      types.add(this);
      name = eventName;
    }

    /**
     * Gets the name associated with this event type.
     *
     * @return the name of this event type
     */
    public String getName() {
      return name;
    }
  }
}
