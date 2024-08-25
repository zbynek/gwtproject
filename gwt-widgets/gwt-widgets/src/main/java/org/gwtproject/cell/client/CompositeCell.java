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
package org.gwtproject.cell.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.dom.client.EventTarget;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.user.client.DOM;

/**
 * A {@link Cell} that is composed of other {@link Cell}s.
 *
 * <p>When this cell is rendered, it will render each component {@link Cell} inside a span. If the
 * component {@link Cell} uses block level elements (such as a Div), the component cells will stack
 * vertically.
 *
 * @param <C> the type that this Cell represents
 */
public class CompositeCell<C> extends AbstractCell<C> {

  /** The events consumed by this cell. */
  private Set<String> consumedEvents;

  /** Indicates whether or not this cell depends on selection. */
  private boolean dependsOnSelection;

  /** Indicates whether or not this cell handles selection. */
  private boolean handlesSelection;

  /**
   * The cells that compose this {@link org.gwtproject.cell.client.Cell}.
   *
   * <p>NOTE: Do not add add/insert/remove hasCells methods to the API. This cell assumes that the
   * index of the cellParent corresponds to the index in the hasCells array.
   */
  private List<HasCell<C, ?>> hasCells;

  private CompositeCell() {
    super();
  }

  /**
   * Construct a new {@link CompositeCell}.
   *
   * @param hasCells the cells that makeup the composite
   */
  public CompositeCell(List<HasCell<C, ?>> hasCells) {
    this();

    // Create a new, readonly copy so cells cannot be added or removed.
    this.hasCells = Collections.unmodifiableList(new ArrayList<>(hasCells));

    // Get the consumed events and depends on selection.
    Set<String> theConsumedEvents = null;
    for (HasCell<C, ?> hasCell : hasCells) {
      Cell<?> cell = hasCell.getCell();
      Set<String> events = cell.getConsumedEvents();
      if (events != null) {
        if (theConsumedEvents == null) {
          theConsumedEvents = new HashSet<>();
        }
        theConsumedEvents.addAll(events);
      }
      if (cell.dependsOnSelection()) {
        dependsOnSelection = true;
      }
      if (cell.handlesSelection()) {
        handlesSelection = true;
      }
    }
    if (theConsumedEvents != null) {
      this.consumedEvents = Collections.unmodifiableSet(theConsumedEvents);
    }
  }

  @Override
  public boolean dependsOnSelection() {
    return dependsOnSelection;
  }

  @Override
  public Set<String> getConsumedEvents() {
    return consumedEvents;
  }

  @Override
  public boolean handlesSelection() {
    return handlesSelection;
  }

  @Override
  public boolean isEditing(
      org.gwtproject.cell.client.Cell.Context context, HTMLElement parent, C value) {
    HTMLElement curChild = Js.uncheckedCast(getContainerElement(parent).firstElementChild);
    for (HasCell<C, ?> hasCell : hasCells) {
      if (isEditingImpl(context, curChild, value, hasCell)) {
        return true;
      }
      curChild = Js.uncheckedCast(curChild.nextElementSibling);
    }
    return false;
  }

  @Override
  public void onBrowserEvent(
      org.gwtproject.cell.client.Cell.Context context,
      HTMLElement parent,
      C value,
      NativeEvent event,
      org.gwtproject.cell.client.ValueUpdater<C> valueUpdater) {
    int index = 0;
    EventTarget eventTarget = event.getEventTarget();
    if (DOM.isElement(eventTarget)) {
      elemental2.dom.Element target = eventTarget.cast();
      HTMLElement container = getContainerElement(parent);
      HTMLElement wrapper = Js.uncheckedCast(container.firstElementChild);
      while (wrapper != null) {
        if (wrapper.contains(target)) {
          onBrowserEventImpl(context, wrapper, value, event, valueUpdater, hasCells.get(index));
        }

        index++;
        wrapper = Js.uncheckedCast(wrapper.nextElementSibling);
      }
    }
  }

  @Override
  public void render(org.gwtproject.cell.client.Cell.Context context, C value, SafeHtmlBuilder sb) {
    for (HasCell<C, ?> hasCell : hasCells) {
      render(context, value, sb, hasCell);
    }
  }

  @Override
  public boolean resetFocus(
          org.gwtproject.cell.client.Cell.Context context, HTMLElement parent, C value) {
    HTMLElement curChild = Js.uncheckedCast(getContainerElement(parent).firstElementChild);
    for (HasCell<C, ?> hasCell : hasCells) {
      // The first child that takes focus wins. Only one child should ever be in
      // edit mode, so this is safe.
      if (resetFocusImpl(context, curChild, value, hasCell)) {
        return true;
      }
      curChild = Js.uncheckedCast(curChild.nextElementSibling);
    }
    return false;
  }

  @Override
  public void setValue(org.gwtproject.cell.client.Cell.Context context, HTMLElement parent, C object) {
    HTMLElement curChild = Js.uncheckedCast(getContainerElement(parent).firstElementChild);
    for (HasCell<C, ?> hasCell : hasCells) {
      setValueImpl(context, curChild, object, hasCell);
      curChild = Js.uncheckedCast(curChild.nextElementSibling);
    }
  }

  /** Returns the readonly list of {@link HasCell}'s that make up this composite. */
  public List<HasCell<C, ?>> getHasCells() {
    return hasCells;
  }

  /**
   * Get the element that acts as the container for all children. If children are added directly to
   * the parent, the parent is the container. If children are added in a table row, the row is the
   * parent.
   *
   * @param parent the parent element of the cell
   * @return the container element
   */
  protected HTMLElement getContainerElement(HTMLElement parent) {
    return parent;
  }

  /**
   * Render the composite cell as HTML into a {@link SafeHtmlBuilder}, suitable for passing to
   * {@link HTMLElement#innerHTML} on a container element.
   *
   * <p>Note: If your cell contains natively focusable elements, such as buttons or input elements,
   * be sure to set the tabIndex to -1 so that they do not steal focus away from the containing
   * widget.
   *
   * @param context the {@link org.gwtproject.cell.client.Cell.Context Context} of the cell
   * @param value the cell value to be rendered
   * @param sb the {@link SafeHtmlBuilder} to be written to
   * @param hasCell a {@link HasCell} instance containing the cells to be rendered within this cell
   */
  protected <X> void render(
      org.gwtproject.cell.client.Cell.Context context,
      C value,
      SafeHtmlBuilder sb,
      HasCell<C, X> hasCell) {
    org.gwtproject.cell.client.Cell<X> cell = hasCell.getCell();
    sb.appendHtmlConstant("<span>");
    cell.render(context, hasCell.getValue(value), sb);
    sb.appendHtmlConstant("</span>");
  }

  private <X> boolean isEditingImpl(
      org.gwtproject.cell.client.Cell.Context context,
      HTMLElement cellParent,
      C object,
      HasCell<C, X> hasCell) {
    return hasCell.getCell().isEditing(context, cellParent, hasCell.getValue(object));
  }

  private <X> void onBrowserEventImpl(
      final org.gwtproject.cell.client.Cell.Context context,
      HTMLElement parent,
      final C object,
      NativeEvent event,
      final org.gwtproject.cell.client.ValueUpdater<C> valueUpdater,
      final HasCell<C, X> hasCell) {
    org.gwtproject.cell.client.Cell<X> cell = hasCell.getCell();
    String eventType = event.getType();
    Set<String> cellConsumedEvents = cell.getConsumedEvents();
    if (cellConsumedEvents == null || !cellConsumedEvents.contains(eventType)) {
      // If this sub-cell doesn't consume this event.
      return;
    }
    org.gwtproject.cell.client.ValueUpdater<X> tempUpdater = null;
    final FieldUpdater<C, X> fieldUpdater = hasCell.getFieldUpdater();
    if (fieldUpdater != null) {
      tempUpdater =
          new org.gwtproject.cell.client.ValueUpdater<X>() {
            @Override
            public void update(X value) {
              fieldUpdater.update(context.getIndex(), object, value);
              if (valueUpdater != null) {
                valueUpdater.update(object);
              }
            }
          };
    }
    cell.onBrowserEvent(context, parent, hasCell.getValue(object), event, tempUpdater);
  }

  private <X> boolean resetFocusImpl(
      Cell.Context context, HTMLElement cellParent, C value, HasCell<C, X> hasCell) {
    X cellValue = hasCell.getValue(value);
    return hasCell.getCell().resetFocus(context, cellParent, cellValue);
  }

  private <X> void setValueImpl(
      Cell.Context context, HTMLElement cellParent, C object, HasCell<C, X> hasCell) {
    hasCell.getCell().setValue(context, cellParent, hasCell.getValue(object));
  }
}
