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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.event.dom.client.KeyCodes;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;

/**
 * A default implementation of the {@link org.gwtproject.cell.client.Cell} interface.
 *
 * <p>
 *
 * <h3>Examples</h3>
 *
 * <dl>
 *   <dt>Read only cell
 *   <dd>{@example com.google.gwt.examples.cell.CellExample}
 *   <dt>Cell with events
 *   <dd>{@example com.google.gwt.examples.cell.CellWithEventsExample}
 *   <dt>Interactive cell
 *   <dd>{@example com.google.gwt.examples.cell.InteractionCellExample}
 *   <dt>Editable cell
 *   <dd>{@example com.google.gwt.examples.cell.EditableCellExample}
 * </dl>
 *
 * @param <C> the type that this Cell represents
 */
public abstract class AbstractCell<C> implements Cell<C> {

  /** The unmodifiable set of events consumed by this cell. */
  private Set<String> consumedEvents;

  /**
   * Construct a new {@link AbstractCell} with the specified consumed events. The input arguments
   * are passed by copy.
   *
   * @param consumedEvents the {@link BrowserEvents events} that this cell consumes
   * @see BrowserEvents
   */
  private AbstractCell() {}

  public AbstractCell(String... consumedEvents) {
    this();
    Set<String> events = null;
    if (consumedEvents != null && consumedEvents.length > 0) {
      events = new HashSet<>();
      for (String event : consumedEvents) {
        events.add(event);
      }
    }
    init(events);
  }

  /**
   * Construct a new {@link AbstractCell} with the specified consumed events.
   *
   * @param consumedEvents the events that this cell consumes
   */
  public AbstractCell(Set<String> consumedEvents) {
    this();
    init(consumedEvents);
  }

  public boolean dependsOnSelection() {
    return false;
  }

  public Set<String> getConsumedEvents() {
    return consumedEvents;
  }

  public boolean handlesSelection() {
    return false;
  }

  /**
   * Returns false. Subclasses that support editing should override this method to return the
   * current editing status.
   */
  public boolean isEditing(Context context, Element parent, C value) {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * <p>If you override this method to add support for events, remember to pass the event types that
   * the cell expects into the constructor.
   */
  public void onBrowserEvent(
      Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    String eventType = event.getType();
    // Special case the ENTER key for a unified user experience.
    if (BrowserEvents.KEYDOWN.equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
      onEnterKeyDown(context, parent, value, event, valueUpdater);
    }
  }

  public abstract void render(Context context, C value, SafeHtmlBuilder sb);

  /**
   * {@inheritDoc}
   *
   * <p>This method is a no-op and returns false. If your cell is editable or can be focused by the
   * user, override this method to reset focus when the containing widget is refreshed.
   */
  public boolean resetFocus(Context context, Element parent, C value) {
    return false;
  }

  public void setValue(Context context, Element parent, C value) {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    render(context, value, sb);
    parent.setInnerSafeHtml(sb.toSafeHtml());
  }

  /**
   * Called when the user triggers a <code>keydown</code> event with the ENTER key while focused on
   * the cell. If your cell interacts with the user, you should override this method to provide a
   * consistent user experience. Your widget must consume <code>keydown</code> events for this
   * method to be called.
   *
   * @param context the {@link Context} of the cell
   * @param parent the parent Element
   * @param value the value associated with the cell
   * @param event the native browser event
   * @param valueUpdater a {@link ValueUpdater}, or null if not specified
   */
  protected void onEnterKeyDown(
      Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {}

  /**
   * Initialize the cell.
   *
   * @param consumedEvents the events that the cell consumes
   */
  private void init(Set<String> consumedEvents) {
    if (consumedEvents != null) {
      this.consumedEvents = Collections.unmodifiableSet(consumedEvents);
    }
  }
}
