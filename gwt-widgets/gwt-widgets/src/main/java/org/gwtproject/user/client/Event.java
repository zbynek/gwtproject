/*
 * Copyright 2009 Google Inc.
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
package org.gwtproject.user.client;

import elemental2.dom.HTMLElement;
import jsinterop.annotations.JsConstructor;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.HasNativeEvent;
import org.gwtproject.event.shared.HandlerManager;
import org.gwtproject.event.shared.HandlerRegistration;

/**
 * An opaque handle to a native DOM Event. An <code>Event</code> cannot be created directly.
 * Instead, use the <code>Event</code> type when returning a native DOM event from JSNI methods. An
 * <code>Event</code> passed back into JSNI becomes the original DOM event the <code>Event</code>
 * was created from, and can be accessed in JavaScript code as expected. This is typically done by
 * calling methods in the {@link DOM} class.
 */
public class Event {

  /** Fired when an element loses keyboard focus. */
  public static final int ONBLUR = 0x01000;
  /** Fired when the value of an input element changes. */
  public static final int ONCHANGE = 0x00400;
  /** Fired when the user clicks on an element. */
  public static final int ONCLICK = 0x00001;
  /** Fired when the user double-clicks on an element. */
  public static final int ONDBLCLICK = 0x00002;
  /** Fired when an image encounters an error. */
  public static final int ONERROR = 0x10000;
  /** Fired when an element receives keyboard focus. */
  public static final int ONFOCUS = 0x00800;
  /** Fired when the user gesture changes. */
  public static final int ONGESTURECHANGE = 0x2000000;
  /** Fired when the user gesture ends. */
  public static final int ONGESTUREEND = 0x4000000;
  /** Fired when the user gesture starts. */
  public static final int ONGESTURESTART = 0x1000000;
  /** Fired when the user depresses a key. */
  public static final int ONKEYDOWN = 0x00080;
  /**
   * Fired when the a character is generated from a keypress (either directly or through
   * auto-repeat).
   */
  public static final int ONKEYPRESS = 0x00100;
  /** Fired when the user releases a key. */
  public static final int ONKEYUP = 0x00200;
  /** Fired when an element (normally an IMG) finishes loading. */
  public static final int ONLOAD = 0x08000;
  /** Fired when an element that has mouse capture loses it. */
  public static final int ONLOSECAPTURE = 0x02000;
  /** Fired when the user depresses a mouse button over an element. */
  public static final int ONMOUSEDOWN = 0x00004;
  /** Fired when the mouse is moved within an element's area. */
  public static final int ONMOUSEMOVE = 0x00040;
  /** Fired when the mouse is moved out of an element's area. */
  public static final int ONMOUSEOUT = 0x00020;
  /** Fired when the mouse is moved into an element's area. */
  public static final int ONMOUSEOVER = 0x00010;
  /** Fired when the user releases a mouse button over an element. */
  public static final int ONMOUSEUP = 0x00008;
  /** Fired when the user scrolls the mouse wheel over an element. */
  public static final int ONMOUSEWHEEL = 0x20000;
  /** Fired when the user pastes text into an input element. */
  public static final int ONPASTE = 0x80000;
  /** Fired when a scrollable element's scroll offset changes. */
  public static final int ONSCROLL = 0x04000;
  /** Fired when the user cancels touching an element. */
  public static final int ONTOUCHCANCEL = 0x800000;
  /** Fired when the user ends touching an element. */
  public static final int ONTOUCHEND = 0x400000;
  /** Fired when the user moves while touching an element. */
  public static final int ONTOUCHMOVE = 0x200000;
  /** Fired when the user starts touching an element. */
  public static final int ONTOUCHSTART = 0x100000;
  /** Fired when the user requests an element's context menu (usually by right-clicking). */
  public static final int ONCONTEXTMENU = 0x40000;
  /** A bit-mask covering both focus events (focus and blur). */
  public static final int FOCUSEVENTS = ONFOCUS | ONBLUR;
  /** A bit-mask covering all keyboard events (down, up, and press). */
  public static final int KEYEVENTS = ONKEYDOWN | ONKEYPRESS | ONKEYUP;
  /**
   * A bit-mask covering all mouse events (down, up, move, over, and out), but not click, dblclick,
   * or wheel events.
   */
  public static final int MOUSEEVENTS =
      ONMOUSEDOWN | ONMOUSEUP | ONMOUSEMOVE | ONMOUSEOVER | ONMOUSEOUT;
  /** A bit-mask covering all touch events (start, move, end, cancel). */
  public static final int TOUCHEVENTS = ONTOUCHSTART | ONTOUCHMOVE | ONTOUCHEND | ONTOUCHCANCEL;
  /** A bit-mask covering all gesture events (start, change, end). */
  public static final int GESTUREEVENTS = ONGESTURESTART | ONGESTURECHANGE | ONGESTUREEND;
  /**
   * Value returned by accessors when the actual integer value is undefined. In Development Mode,
   * most accessors assert that the requested attribute is reliable across all supported browsers.
   *
   * @see Event
   */
  @Deprecated public static final int UNDEFINED = 0;
  /**
   * The list of {@link NativePreviewHandler}. We use a list instead of a handler manager for
   * efficiency and because we want to fire the handlers in reverse order. When the last handler is
   * removed, handlers is reset to null.
   */
  static HandlerManager handlers;

  /**
   * Adds a {@link NativePreviewHandler} that will receive all events before they are fired to their
   * handlers. Note that the handler will receive <u>all</u> native events, including those received
   * due to bubbling, whereas normal event handlers only receive explicitly sunk events.
   *
   * <p>Unlike other event handlers, {@link NativePreviewHandler} are fired in the reverse order
   * that they are added, such that the last {@link NativePreviewEvent} that was added is the first
   * to be fired.
   *
   * <p>Please note that nondeterministic behavior will result if more than one GWT application
   * registers preview handlers. See <a href=
   * 'http://code.google.com/p/google-web-toolkit/issues/detail?id=3892'>issue 3892</a> for details.
   *
   * @param handler the {@link NativePreviewHandler}
   * @return {@link HandlerRegistration} used to remove this handler
   */
  public static HandlerRegistration addNativePreviewHandler(final NativePreviewHandler handler) {
    assert handler != null : "Cannot add a null handler";
    DOM.maybeInitializeEventSystem();
    // Initialize the type
    NativePreviewEvent.getType();
    if (handlers == null) {
      handlers = new HandlerManager(null, true);
      NativePreviewEvent.singleton = new NativePreviewEvent();
    }
    return handlers.addHandler(NativePreviewEvent.TYPE, handler);
  }

  /**
   * Converts the {@link elemental2.dom.Event} to Event. This is always safe.
   *
   * @param event the event to downcast
   */
  public static Event as(elemental2.dom.Event event) {
    return Js.uncheckedCast(event);
  }

  /**
   * Fire a {@link NativePreviewEvent} for the native event.
   *
   * @param nativeEvent the native event
   * @return true to fire the event normally, false to cancel the event
   */
  public static boolean fireNativePreviewEvent(elemental2.dom.Event nativeEvent) {
    return NativePreviewEvent.fire(handlers, nativeEvent);
  }

  /**
   * Gets the {@link EventListener} that will receive events for the given element. Only one such
   * listener may exist for a single element.
   *
   * @param elem the element whose listener is to be set
   * @return the element's event listener
   */
  public static EventListener getEventListener(HTMLElement elem) {
    return DOM.getEventListener(Js.uncheckedCast(elem));
  }

  /**
   * Gets the current set of events sunk by a given element.
   *
   * @param elem the element whose events are to be retrieved
   * @return a bitfield describing the events sunk on this element (its possible values are
   *     described in {@link Event})
   */
  public static int getEventsSunk(HTMLElement elem) {
    return DOM.getEventsSunk(Js.uncheckedCast(elem));
  }

  /**
   * Gets the enumerated type of this event given a valid event type name.
   *
   * @param typeName the typeName to be tested
   * @return the event's enumerated type, or -1 if not defined
   */
  public static int getTypeInt(String typeName) {
    return DOM.impl.eventGetTypeInt(typeName);
  }

  /**
   * Releases mouse capture on the given element. Calling this method has no effect if the element
   * does not currently have mouse capture.
   *
   * @param elem the element to release capture
   * @see #setCapture(HTMLElement)
   */
  public static void releaseCapture(HTMLElement elem) {
    DOM.releaseCapture(elem);
  }

  /**
   * Sets mouse-capture on the given element. This element will directly receive all mouse events
   * until {@link #releaseCapture(HTMLElement)} is called on it.
   *
   * @param elem the element on which to set mouse capture
   */
  public static void setCapture(HTMLElement elem) {
    DOM.setCapture(elem);
  }

  /**
   * Sets the {@link EventListener} to receive events for the given element. Only one such listener
   * may exist for a single element.
   *
   * @param elem the element whose listener is to be set
   * @param listener the listener to receive {@link Event events}
   */
  public static void setEventListener(HTMLElement elem, EventListener listener) {
    DOM.setEventListener(elem, listener);
  }

  /**
   * Sets the current set of events sunk by a given element. These events will be fired to the
   * nearest {@link EventListener} specified on any of the element's parents.
   *
   * @param elem the element whose events are to be retrieved
   * @param eventBits a bitfield describing the events sunk on this element (its possible values are
   *     described in {@link Event})
   */
  public static void sinkEvents(HTMLElement elem, int eventBits) {
    DOM.sinkEvents(Js.uncheckedCast(elem), eventBits);
  }

  /** Handler interface for {@link NativePreviewEvent} events. */
  public interface NativePreviewHandler {

    /**
     * Called when {@link NativePreviewEvent} is fired.
     *
     * @param event the {@link NativePreviewEvent} that was fired
     */
    void onPreviewNativeEvent(NativePreviewEvent event);
  }

  /** Represents a preview of a native {@link Event}. */
  public static class NativePreviewEvent extends org.gwtproject.event.shared.Event<NativePreviewHandler>
      implements HasNativeEvent {

    /** Handler type. */
    private static Type<NativePreviewHandler> TYPE;

    /** The singleton instance of {@link NativePreviewEvent}. */
    private static NativePreviewEvent singleton;
    /** A boolean indicating that the native event should be canceled. */
    private boolean isCanceled = false;
    /**
     * A boolean indicating whether or not canceling the native event should be prevented. This
     * supercedes {@link #isCanceled}.
     */
    private boolean isConsumed = false;
    /** A boolean indicating that the current handler is at the top of the event preview stack. */
    private boolean isFirstHandler = false;
    /** The event being previewed. */
    private elemental2.dom.Event nativeEvent;

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static Type<NativePreviewHandler> getType() {
      if (TYPE == null) {
        TYPE = new Type<>();
      }
      return TYPE;
    }

    /**
     * Fire a {@link NativePreviewEvent} for the native event.
     *
     * @param handlers the {@link HandlerManager}
     * @param nativeEvent the native event
     * @return true to fire the event normally, false to cancel the event
     */
    private static boolean fire(HandlerManager handlers, elemental2.dom.Event nativeEvent) {
      if (TYPE != null && handlers != null && handlers.isEventHandled(TYPE)) {
        // Cache the current values in the singleton in case we are in the
        // middle of handling another event.
        boolean lastIsCanceled = singleton.isCanceled;
        boolean lastIsConsumed = singleton.isConsumed;
        boolean lastIsFirstHandler = singleton.isFirstHandler;
        elemental2.dom.Event lastNativeEvent = singleton.nativeEvent;

        // Revive the event
        singleton.revive();
        singleton.setNativeEvent(nativeEvent);

        // Fire the event
        handlers.fireEvent(singleton);

        boolean ret = !(singleton.isCanceled() && !singleton.isConsumed());

        // Restore the state of the singleton.
        singleton.isCanceled = lastIsCanceled;
        singleton.isConsumed = lastIsConsumed;
        singleton.isFirstHandler = lastIsFirstHandler;
        singleton.nativeEvent = lastNativeEvent;
        return ret;
      }
      return true;
    }

    /**
     * Cancel the native event and prevent it from firing. Note that the event can still fire if
     * another handler calls {@link #consume()}.
     *
     * <p>Classes overriding this method should still call super.cancel().
     */
    public void cancel() {
      isCanceled = true;
    }

    /**
     * Consume the native event and prevent it from being canceled, even if it has already been
     * canceled by another handler. {@link NativePreviewHandler} that fire first have priority over
     * later handlers, so all handlers should check if the event has already been canceled before
     * calling this method.
     */
    public void consume() {
      isConsumed = true;
    }

    @Override
    public final Type<NativePreviewHandler> getAssociatedType() {
      return TYPE;
    }

    public elemental2.dom.Event getNativeEvent() {
      return nativeEvent;
    }

    /**
     * Set the native event.
     *
     * @param nativeEvent the native {@link Event} being previewed.
     */
    private void setNativeEvent(elemental2.dom.Event nativeEvent) {
      this.nativeEvent = nativeEvent;
    }

    /**
     * Gets the type int corresponding to the native event that triggered this preview.
     *
     * @return the type int associated with this native event
     */
    public final int getTypeInt() {
      return DOM.impl.eventGetTypeInt(getNativeEvent().type);
    }

    /**
     * Has the event already been canceled? Note that {@link #isConsumed()} will still return true
     * if the native event has also been consumed.
     *
     * @return true if the event has been canceled
     * @see #cancel()
     */
    public boolean isCanceled() {
      return isCanceled;
    }

    /**
     * Has the native event been consumed? Note that {@link #isCanceled()} will still return true if
     * the native event has also been canceled.
     *
     * @return true if the event has been consumed
     * @see #consume()
     */
    public boolean isConsumed() {
      return isConsumed;
    }

    /**
     * Is the current handler the first to preview this event?
     *
     * @return true if the current handler is the first to preview the event
     */
    public boolean isFirstHandler() {
      return isFirstHandler;
    }

    @Override
    protected void dispatch(NativePreviewHandler handler) {
      handler.onPreviewNativeEvent(this);
      singleton.isFirstHandler = false;
    }


    protected void revive() {
      setSource(null);
      isCanceled = false;
      isConsumed = false;
      isFirstHandler = true;
      nativeEvent = null;
    }
  }
}
