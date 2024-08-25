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

import org.gwtproject.event.shared.BrowserEvents;

/** Represents a native gesture start event. */
public class GestureStartEvent extends DomEvent<GestureStartHandler> {

  /** Event type for gesture start events. Represents the meta-data associated with this event. */
  private static final Type<GestureStartHandler> TYPE =
      new Type<>(BrowserEvents.GESTURESTART, new GestureStartEvent());

  /**
   * Protected constructor.
   */
  protected GestureStartEvent() {}

  /**
   * Gets the event type associated with gesture start events.
   *
   * @return the handler type
   */
  public static Type<GestureStartHandler> getType() {
    return TYPE;
  }

  @Override
  public final Type<GestureStartHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(GestureStartHandler handler) {
    handler.onGestureStart(this);
  }
}
