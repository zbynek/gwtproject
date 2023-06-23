/*
 * Copyright 2011 Google Inc.
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

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import jsinterop.annotations.JsFunction;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import elemental2.dom.EventTarget;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.event.logical.shared.HasResizeHandlers;
import org.gwtproject.event.logical.shared.ResizeEvent;
import org.gwtproject.event.logical.shared.ResizeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.layout.client.Layout;
import org.gwtproject.layout.client.Layout.Layer;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.EventListener;
import org.gwtproject.user.client.ui.ResizeLayoutPanel.Impl.Delegate;

/**
 * A simple panel that {@link ProvidesResize} to its one child, but does not {@link RequiresResize}.
 * Use this to embed layout panels in any location within your application.
 */
public class ResizeLayoutPanel extends SimplePanel implements ProvidesResize, HasResizeHandlers {

  /** Implementation of resize event. */
  abstract static class Impl {
    /** Delegate event handler. */
    abstract static interface Delegate {
      /** Called when the element is resized. */
      void onResize();
    }

    boolean isAttached;
    HTMLElement parent;
    private Delegate delegate;

    /**
     * Initialize the implementation.
     *
     * @param elem the element to listen for resize
     * @param delegate the {@link Delegate} to inform when resize occurs
     */
    public void init(HTMLElement elem, Delegate delegate) {
      this.parent = elem;
      this.delegate = delegate;
    }

    /** Called on attach. */
    public void onAttach() {
      isAttached = true;
    }

    /** Called on detach. */
    public void onDetach() {
      isAttached = false;
    }

    /** Handle a resize event. */
    protected void handleResize() {
      if (isAttached && delegate != null) {
        delegate.onResize();
      }
    }
  }

  /** Implementation of resize event. */
  static class ImplStandard extends Impl implements EventListener {
    /**
     * Chrome does not fire an onresize event if the dimensions are too small to render a scrollbar.
     */
    private static final String MIN_SIZE = "20px";

    private HTMLElement collapsible;
    private HTMLElement collapsibleInner;
    private HTMLElement expandable;
    private HTMLElement expandableInner;
    private int lastOffsetHeight = -1;
    private int lastOffsetWidth = -1;
    private boolean resettingScrollables;

    @Override
    public void init(HTMLElement elem, Delegate delegate) {
      super.init(elem, delegate);

      /*
       * Set the minimum dimensions to ensure that scrollbars are rendered and
       * fire onscroll events.
       */
      elem.style.setProperty("minWidth", MIN_SIZE);
      elem.style.setProperty("minHeight", MIN_SIZE);

      /*
       * Detect expansion. In order to detect an increase in the size of the
       * widget, we create an absolutely positioned, scrollable div with
       * height=width=100%. We then add an inner div that has fixed height and
       * width equal to 100% (converted to pixels) and set scrollLeft/scrollTop
       * to their maximum. When the outer div expands, scrollLeft/scrollTop
       * automatically becomes a smaller number and trigger an onscroll event.
       */
      expandable = DOM.createDiv();
      expandable.style.visibility = "hidden";
      expandable.style.position = "absolute";
      expandable.style.height = CSSProperties.HeightUnionType.of("100%");
      expandable.style.width = CSSProperties.WidthUnionType.of("100%");
      expandable.style.overflow = "scroll";
      expandable.style.zIndex = CSSProperties.ZIndexUnionType.of(-1);
      elem.appendChild(expandable);
      expandableInner = DOM.createDiv();
      expandable.appendChild(expandableInner);
      DOM.sinkEvents(expandable, Event.ONSCROLL);

      /*
       * Detect collapse. In order to detect a decrease in the size of the
       * widget, we create an absolutely positioned, scrollable div with
       * height=width=100%. We then add an inner div that has height=width=200%
       * and max out the scrollTop/scrollLeft. When the height or width
       * decreases, the inner div loses 2px for every 1px that the scrollable
       * div loses, so the scrollTop/scrollLeft decrease and we get an onscroll
       * event.
       */
      collapsible = DOM.createDiv();
      collapsible.style.visibility = "hidden";
      collapsible.style.position = "absolute";
      collapsible.style.height = CSSProperties.HeightUnionType.of("100%");
      collapsible.style.width = CSSProperties.WidthUnionType.of("100%");
      collapsible.style.overflow = "scroll";
      collapsible.style.zIndex = CSSProperties.ZIndexUnionType.of(-1);
      elem.appendChild(collapsible);
      collapsibleInner = DOM.createDiv();
      collapsibleInner.style.width = CSSProperties.WidthUnionType.of("200%");
      collapsibleInner.style.height = CSSProperties.HeightUnionType.of("200%");;
      collapsible.appendChild(collapsibleInner);
      DOM.sinkEvents(collapsible, Event.ONSCROLL);
    }

    @Override
    public void onAttach() {
      super.onAttach();
      DOM.setEventListener(expandable, this);
      DOM.setEventListener(collapsible, this);

      /*
       * Update the scrollables in a deferred command so the browser calculates
       * the offsetHeight/Width correctly.
       */
      Scheduler.get()
          .scheduleDeferred(
              new ScheduledCommand() {
                public void execute() {
                  resetScrollables();
                }
              });
    }

    public void onBrowserEvent(elemental2.dom.Event event) {
      if (!resettingScrollables && Event.ONSCROLL == DOM.eventGetType(event)) {
        EventTarget eventTarget = event.target;
        if (!DOM.isElement(eventTarget)) {
          return;
        }
        HTMLElement target = Js.uncheckedCast(eventTarget);
        if (target == collapsible || target == expandable) {
          handleResize();
        }
      }
    }

    @Override
    public void onDetach() {
      super.onDetach();
      DOM.setEventListener(expandable, null);
      DOM.setEventListener(collapsible, null);
      lastOffsetHeight = -1;
      lastOffsetWidth = -1;
    }

    @Override
    protected void handleResize() {
      if (resetScrollables()) {
        super.handleResize();
      }
    }

    /**
     * Reset the positions of the scrollable elements.
     *
     * @return true if the size changed, false if not
     */
    private boolean resetScrollables() {
      /*
       * Older versions of safari trigger a synchronous scroll event when we
       * update scrollTop/scrollLeft, so we set a boolean to ignore that event.
       */
      if (resettingScrollables) {
        return false;
      }
      resettingScrollables = true;

      /*
       * Reset expandable element. Scrollbars are not rendered if the div is too
       * small, so we need to set the dimensions of the inner div to a value
       * greater than the offsetWidth/Height.
       */
      int offsetHeight = parent.offsetHeight;
      int offsetWidth = parent.offsetWidth;
      int height = offsetHeight + 100;
      int width = offsetWidth + 100;
      expandableInner.style.height = CSSProperties.HeightUnionType.of(height + "px");
      expandableInner.style.width = CSSProperties.WidthUnionType.of(width + "px");
      expandable.scrollTop = height;
      expandable.scrollLeft = width;

      // Reset collapsible element.
      collapsible.scrollTop = collapsible.scrollHeight + 100;
      collapsible.scrollLeft = collapsible.scrollWidth + 100;

      if (lastOffsetHeight != offsetHeight || lastOffsetWidth != offsetWidth) {
        lastOffsetHeight = offsetHeight;
        lastOffsetWidth = offsetWidth;
        resettingScrollables = false;
        return true;
      }
      resettingScrollables = false;
      return false;
    }
  }

  /** Implementation of resize event used by IE. */
  // FIXME: Unused in deferred binding, but might be usable in all browsers?
  static class ImplTrident extends Impl {

    @Override
    public void init(HTMLElement elem, Delegate delegate) {
      super.init(elem, delegate);
      initResizeEventListener(elem);
    }

    @Override
    public void onAttach() {
      super.onAttach();
      setResizeEventListener(parent, this);
    }

    @Override
    public void onDetach() {
      super.onDetach();
      setResizeEventListener(parent, null);
    }

    /**
     * Initialize the onresize listener. This method doesn't create a memory leak because we don't
     * set a back reference to the Impl class until we attach to the DOM.
     */
    private void initResizeEventListener(HTMLElement elem) {
      Fn func =
          () -> {
            if (((JsPropertyMap) elem).has("__resizeImpl")) {
              ((ResizeLayoutPanel.Impl) ((JsPropertyMap) elem).get("__resizeImpl")).handleResize();
            }
          };
      ((FnVarArgs) ((JsPropertyMap) elem).get("attachEvent")).onInvoke("onresize", func);
    }

    /** Set the event listener that handles resize events. */
    private void setResizeEventListener(HTMLElement elem, Impl listener) {
      ((JsPropertyMap) elem).set("__resizeImpl", listener);
    }
  }

  private final Impl impl = new ImplStandard();
  private Layer layer;
  private final Layout layout;
  private final ScheduledCommand resizeCmd =
      new ScheduledCommand() {
        public void execute() {
          resizeCmdScheduled = false;
          handleResize();
        }
      };
  private boolean resizeCmdScheduled = false;

  public ResizeLayoutPanel() {
    layout = new Layout(getElement());
    impl.init(
        getElement(),
        new Delegate() {
          public void onResize() {
            scheduleResize();
          }
        });
  }

  public HandlerRegistration addResizeHandler(ResizeHandler handler) {
    return addHandler(handler, ResizeEvent.getType());
  }

  @Override
  public boolean remove(Widget w) {
    // Validate.
    if (widget != w) {
      return false;
    }

    // Orphan.
    try {
      orphan(w);
    } finally {
      // Physical detach.
      layout.removeChild(layer);
      layer = null;

      // Logical detach.
      widget = null;
    }
    return true;
  }

  @Override
  public void setWidget(Widget w) {
    // Validate
    if (w == widget) {
      return;
    }

    // Detach new child.
    if (w != null) {
      w.removeFromParent();
    }

    // Remove old child.
    if (widget != null) {
      remove(widget);
    }

    // Logical attach.
    widget = w;

    if (w != null) {
      // Physical attach.
      layer = layout.attachChild(widget.getElement(), widget);
      layer.setTopHeight(0.0, Unit.PX, 100.0, Unit.PCT);
      layer.setLeftWidth(0.0, Unit.PX, 100.0, Unit.PCT);

      adopt(w);

      // Update the layout.
      layout.layout();
      scheduleResize();
    }
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    impl.onAttach();
    layout.onAttach();
    scheduleResize();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    impl.onDetach();
    layout.onDetach();
  }

  private void handleResize() {
    if (!isAttached()) {
      return;
    }

    // Provide resize to child.
    if (widget instanceof RequiresResize) {
      ((RequiresResize) widget).onResize();
    }

    // Fire resize event.
    ResizeEvent.fire(this, getOffsetWidth(), getOffsetHeight());
  }

  /**
   * Schedule a resize handler. We schedule the event so the DOM has time to update the offset
   * sizes, and to avoid duplicate resize events from both a height and width resize.
   */
  private void scheduleResize() {
    if (isAttached() && !resizeCmdScheduled) {
      resizeCmdScheduled = true;
      Scheduler.get().scheduleDeferred(resizeCmd);
    }
  }

  @FunctionalInterface
  @JsFunction
  interface Fn {
    void onInvoke();
  }

  @FunctionalInterface
  @JsFunction
  interface FnVarArgs {
    void onInvoke(Object... arg1);
  }
}
