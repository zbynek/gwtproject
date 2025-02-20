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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.gwtproject.cell.client.Cell;
import org.gwtproject.cell.client.Cell.Context;
import org.gwtproject.cell.client.ValueUpdater;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.DivElement;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.EventTarget;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.CssResource;
import org.gwtproject.resources.client.CssResource.ImportedWithPrefix;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.resources.client.ImageResource.ImageOptions;
import org.gwtproject.resources.client.ImageResource.RepeatStyle;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.gwtproject.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.ui.AttachDetachException;
import org.gwtproject.user.client.ui.DeckPanel;
import org.gwtproject.user.client.ui.HTML;
import org.gwtproject.user.client.ui.SimplePanel;
import org.gwtproject.user.client.ui.Widget;
import org.gwtproject.view.client.CellPreviewEvent;
import org.gwtproject.view.client.ProvidesKey;
import org.gwtproject.view.client.SelectionModel;

/**
 * A single column list of cells.
 *
 * <p>
 *
 * <h3>Examples</h3>
 *
 * <dl>
 *   <dt>Trivial example
 *   <dd>{@example com.google.gwt.examples.cellview.CellListExample}
 *   <dt>Handling user input with ValueUpdater
 *   <dd>{@example com.google.gwt.examples.cellview.CellListValueUpdaterExample}
 *   <dt>Pushing data with List Data Provider (backed by {@link List})
 *   <dd>{@example com.google.gwt.examples.view.ListDataProviderExample}
 *   <dt>Pushing data asynchronously with Async Data Provider
 *   <dd>{@example com.google.gwt.examples.view.AsyncDataProviderExample}
 *   <dt>Writing a custom data provider
 *   <dd>{@example com.google.gwt.examples.view.RangeChangeHandlerExample}
 *   <dt>Using a key provider to track objects as they change
 *   <dd>{@example com.google.gwt.examples.view.KeyProviderExample}
 * </dl>
 *
 * @param <T> the data type of list items
 */
public class CellList<T> extends AbstractHasData<T> {

  /** A ClientBundle that provides images for this widget. */
  public interface Resources extends ClientBundle {

    Resources INSTANCE = new CellList_ResourcesImpl();
    /** The background used for selected items. */
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellListSelectedBackground();

    /** The styles used in this widget. */
    @Source(Style.DEFAULT_CSS)
    Style cellListStyle();
  }

  /** Styles used by this widget. */
  @ImportedWithPrefix("gwt-CellList")
  public interface Style extends CssResource {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/cellview/client/CellList.gss";

    /** Applied to even items. */
    String cellListEvenItem();

    /** Applied to the keyboard selected item. */
    String cellListKeyboardSelectedItem();

    /** Applied to odd items. */
    String cellListOddItem();

    /** Applied to selected items. */
    String cellListSelectedItem();

    /** Applied to the widget. */
    String cellListWidget();
  }

  interface Template extends SafeHtmlTemplates {

    CellList.Template INSTANCE = new CellList_TemplateImpl();

    SafeHtml div(int idx, String classes, SafeHtml cellContents);
  }

  /** The default page size. */
  private static final int DEFAULT_PAGE_SIZE = 25;

  private static Resources DEFAULT_RESOURCES;

  private static Resources getDefaultResources() {
    if (DEFAULT_RESOURCES == null) {
      DEFAULT_RESOURCES = new CellList_ResourcesImpl();
    }
    return DEFAULT_RESOURCES;
  }

  private final Cell<T> cell;
  private boolean cellIsEditing;
  private final Element childContainer;
  private SafeHtml emptyListMessage = SafeHtmlUtils.fromSafeConstant("");
  private final SimplePanel emptyListWidgetContainer = new SimplePanel();
  private final SimplePanel loadingIndicatorContainer = new SimplePanel();

  /** A {@link DeckPanel} to hold widgets associated with various loading states. */
  private final DeckPanel messagesPanel = new DeckPanel();

  private final Style style;

  private ValueUpdater<T> valueUpdater;

  /**
   * Construct a new {@link CellList}.
   *
   * @param cell the cell used to render each item
   */
  public CellList(final Cell<T> cell) {
    this(cell, getDefaultResources(), null);
  }

  /**
   * Construct a new {@link CellList} with the specified {@link Resources}.
   *
   * @param cell the cell used to render each item
   * @param resources the resources used for this widget
   */
  public CellList(final Cell<T> cell, Resources resources) {
    this(cell, resources, null);
  }

  /**
   * Construct a new {@link CellList} with the specified {@link ProvidesKey key provider}.
   *
   * @param cell the cell used to render each item
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   */
  public CellList(final Cell<T> cell, ProvidesKey<T> keyProvider) {
    this(cell, getDefaultResources(), keyProvider);
  }

  /**
   * Construct a new {@link CellList} with the specified {@link Resources} and {@link ProvidesKey
   * key provider}.
   *
   * @param cell the cell used to render each item
   * @param resources the resources used for this widget
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   */
  public CellList(final Cell<T> cell, Resources resources, ProvidesKey<T> keyProvider) {
    super(Document.get().createDivElement(), DEFAULT_PAGE_SIZE, keyProvider);
    this.cell = cell;
    this.style = resources.cellListStyle();
    this.style.ensureInjected();

    String widgetStyle = this.style.cellListWidget();
    if (widgetStyle != null) {
      // The widget style is null when used in CellBrowser.
      addStyleName(widgetStyle);
    }

    // Add the child container.
    childContainer = Document.get().createDivElement();
    DivElement outerDiv = getElement().cast();
    outerDiv.appendChild(childContainer);

    // Attach the message panel.
    outerDiv.appendChild(messagesPanel.getElement());
    adopt(messagesPanel);
    messagesPanel.add(emptyListWidgetContainer);
    messagesPanel.add(loadingIndicatorContainer);

    // Sink events that the cell consumes.
    org.gwtproject.user.cellview.client.CellBasedWidgetImpl.get()
        .sinkEvents(this, cell.getConsumedEvents());
  }

  /**
   * Get the message that is displayed when there is no data.
   *
   * @return the empty message
   * @see #setEmptyListMessage(SafeHtml)
   * @deprecated as of GWT 2.3, use {@link #getEmptyListWidget()} instead
   */
  @Deprecated
  public SafeHtml getEmptyListMessage() {
    return emptyListMessage;
  }

  /**
   * Get the widget displayed when the list has no rows.
   *
   * @return the empty list widget
   */
  public Widget getEmptyListWidget() {
    return emptyListWidgetContainer.getWidget();
  }

  /**
   * Get the widget displayed when the data is loading.
   *
   * @return the loading indicator
   */
  public Widget getLoadingIndicator() {
    return loadingIndicatorContainer.getWidget();
  }

  /**
   * Get the {@link Element} for the specified index. If the element has not been created, null is
   * returned.
   *
   * @param indexOnPage the index on the page
   * @return the element, or null if it doesn't exists
   * @throws IndexOutOfBoundsException if the index is outside of the current page
   */
  public Element getRowElement(int indexOnPage) {
    getPresenter().flush();
    checkRowBounds(indexOnPage);
    if (childContainer.getChildCount() > indexOnPage) {
      return childContainer.getChild(indexOnPage).cast();
    }
    return null;
  }

  /**
   * Set the message to display when there is no data.
   *
   * @param html the message to display when there are no results
   * @see #getEmptyListMessage()
   * @deprecated as of GWT 2.3, use {@link
   *     #setEmptyListWidget(org.gwtproject.user.client.ui.Widget)} instead
   */
  @Deprecated
  public void setEmptyListMessage(SafeHtml html) {
    this.emptyListMessage = html;
    setEmptyListWidget(html == null ? null : new HTML(html));
  }

  /**
   * Set the widget to display when the list has no rows.
   *
   * @param widget the empty data widget
   */
  public void setEmptyListWidget(Widget widget) {
    emptyListWidgetContainer.setWidget(widget);
  }

  /**
   * Set the widget to display when the data is loading.
   *
   * @param widget the loading indicator
   */
  public void setLoadingIndicator(Widget widget) {
    loadingIndicatorContainer.setWidget(widget);
  }

  /**
   * Set the value updater to use when cells modify items.
   *
   * @param valueUpdater the {@link ValueUpdater}
   */
  public void setValueUpdater(ValueUpdater<T> valueUpdater) {
    this.valueUpdater = valueUpdater;
  }

  @Override
  protected boolean dependsOnSelection() {
    return cell.dependsOnSelection();
  }

  @Override
  protected void doAttachChildren() {
    try {
      doAttach(messagesPanel);
    } catch (Throwable e) {
      throw new AttachDetachException(Collections.singleton(e));
    }
  }

  @Override
  protected void doDetachChildren() {
    try {
      doDetach(messagesPanel);
    } catch (Throwable e) {
      throw new AttachDetachException(Collections.singleton(e));
    }
  }

  /**
   * Fire an event to the cell.
   *
   * @param context the {@link Context} of the cell
   * @param event the event that was fired
   * @param parent the parent of the cell
   * @param value the value of the cell
   */
  protected void fireEventToCell(Context context, Event event, Element parent, T value) {
    Set<String> consumedEvents = cell.getConsumedEvents();
    if (consumedEvents != null && consumedEvents.contains(event.getType())) {
      boolean cellWasEditing = cell.isEditing(context, parent, value);
      cell.onBrowserEvent(context, parent, value, event, valueUpdater);
      cellIsEditing = cell.isEditing(context, parent, value);
      if (cellWasEditing && !cellIsEditing) {
        org.gwtproject.user.cellview.client.CellBasedWidgetImpl.get()
            .resetFocus(
                new Scheduler.ScheduledCommand() {
                  @Override
                  public void execute() {
                    setFocus(true);
                  }
                });
      }
    }
  }

  /** Return the cell used to render each item. */
  protected Cell<T> getCell() {
    return cell;
  }

  /**
   * Get the parent element that wraps the cell from the list item. Override this method if you add
   * structure to the element.
   *
   * @param item the row element that wraps the list item
   * @return the parent element of the cell
   */
  protected Element getCellParent(Element item) {
    return item;
  }

  @Override
  protected Element getChildContainer() {
    return childContainer;
  }

  @Override
  protected Element getKeyboardSelectedElement() {
    // Do not use getRowElement() because that will flush the presenter.
    int rowIndex = getKeyboardSelectedRow();
    if (rowIndex >= 0 && childContainer.getChildCount() > rowIndex) {
      return childContainer.getChild(rowIndex).cast();
    }
    return null;
  }

  @Override
  protected boolean isKeyboardNavigationSuppressed() {
    return cellIsEditing;
  }

  @Override
  protected void onBrowserEvent2(Event event) {
    // Get the event target.
    EventTarget eventTarget = event.getEventTarget();
    if (!Element.is(eventTarget)) {
      return;
    }
    final Element target = event.getEventTarget().cast();

    // Forward the event to the cell.
    String idxString = "";
    Element cellTarget = target;
    while ((cellTarget != null) && ((idxString = cellTarget.getAttribute("__idx")).length() == 0)) {
      cellTarget = cellTarget.getParentElement();
    }
    if (idxString.length() > 0) {
      // Select the item if the cell does not consume events. Selection occurs
      // before firing the event to the cell in case the cell operates on the
      // currently selected item.
      String eventType = event.getType();
      boolean isClick = BrowserEvents.CLICK.equals(eventType);
      int idx = Integer.parseInt(idxString);
      int indexOnPage = idx - getPageStart();
      if (!isRowWithinBounds(indexOnPage)) {
        // If the event causes us to page, then the index will be out of bounds.
        return;
      }

      // Get the cell parent before doing selection in case the list is redrawn.
      boolean isSelectionHandled =
          cell.handlesSelection()
              || HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.BOUND_TO_SELECTION
                  == getKeyboardSelectionPolicy();
      Element cellParent = getCellParent(cellTarget);
      T value = getVisibleItem(indexOnPage);
      Context context = new Context(idx, 0, getValueKey(value));
      CellPreviewEvent<T> previewEvent =
          CellPreviewEvent.fire(
              this, event, this, context, value, cellIsEditing, isSelectionHandled);

      // Fire the event to the cell if the list has not been refreshed.
      if (!previewEvent.isCanceled()) {
        fireEventToCell(context, event, cellParent, value);
      }
    }
  }

  /**
   * Called when the loading state changes.
   *
   * @param state the new loading state
   */
  @Override
  protected void onLoadingStateChanged(LoadingState state) {
    Widget message = null;
    if (state == LoadingState.LOADING) {
      // Loading indicator.
      message = loadingIndicatorContainer;
    } else if (state == LoadingState.LOADED && getPresenter().isEmpty()) {
      // Empty table.
      message = emptyListWidgetContainer;
    }

    // Switch out the message to display.
    if (message != null) {
      messagesPanel.showWidget(messagesPanel.getWidgetIndex(message));
    }

    // Show the correct container.
    showOrHide(getChildContainer(), message == null);
    messagesPanel.setVisible(message != null);

    // Fire an event.
    super.onLoadingStateChanged(state);
  }

  @Override
  protected void renderRowValues(
      SafeHtmlBuilder sb, List<T> values, int start, SelectionModel<? super T> selectionModel) {
    String keyboardSelectedItem = " " + style.cellListKeyboardSelectedItem();
    String selectedItem = " " + style.cellListSelectedItem();
    String evenItem = style.cellListEvenItem();
    String oddItem = style.cellListOddItem();
    int keyboardSelectedRow = getKeyboardSelectedRow() + getPageStart();
    int length = values.size();
    int end = start + length;
    for (int i = start; i < end; i++) {
      T value = values.get(i - start);
      boolean isSelected = selectionModel == null ? false : selectionModel.isSelected(value);

      StringBuilder classesBuilder = new StringBuilder();
      classesBuilder.append(i % 2 == 0 ? evenItem : oddItem);
      if (isSelected) {
        classesBuilder.append(selectedItem);
      }
      if (i == keyboardSelectedRow) {
        classesBuilder.append(keyboardSelectedItem);
      }

      SafeHtmlBuilder cellBuilder = new SafeHtmlBuilder();
      Context context = new Context(i, 0, getValueKey(value));
      cell.render(context, value, cellBuilder);
      sb.append(Template.INSTANCE.div(i, classesBuilder.toString(), cellBuilder.toSafeHtml()));
    }
  }

  @Override
  protected boolean resetFocusOnCell() {
    int row = getKeyboardSelectedRow();
    if (isRowWithinBounds(row)) {
      Element rowElem = getKeyboardSelectedElement();
      Element cellParent = getCellParent(rowElem);
      T value = getVisibleItem(row);
      Context context = new Context(row + getPageStart(), 0, getValueKey(value));
      return cell.resetFocus(context, cellParent, value);
    }
    return false;
  }

  @Override
  protected void setKeyboardSelected(int index, boolean selected, boolean stealFocus) {
    if (!isRowWithinBounds(index)) {
      return;
    }

    Element elem = getRowElement(index);
    if (!selected || isFocused || stealFocus) {
      setStyleName(elem, style.cellListKeyboardSelectedItem(), selected);
    }
    setFocusable(elem, selected);
    if (selected && stealFocus && !cellIsEditing) {
      elem.focus();
      onFocus();
    }
  }

  /**
   * @deprecated this method is never called by AbstractHasData, render the selected styles in
   *     {@link #renderRowValues(SafeHtmlBuilder, List, int, SelectionModel)}
   */
  @Override
  @Deprecated
  protected void setSelected(Element elem, boolean selected) {
    setStyleName(elem, style.cellListSelectedItem(), selected);
  }
}
