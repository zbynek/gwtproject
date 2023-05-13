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
package org.gwtproject.user.client.ui;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLSelectElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.ChangeEvent;
import org.gwtproject.event.dom.client.ChangeHandler;
import org.gwtproject.event.dom.client.HasChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.DOM;

/**
 * A widget that presents a list of choices to the user, either as a list box or as a drop-down
 * list.
 *
 * <p><img class='gallery' src='doc-files/ListBox.png'/>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-ListBox { }
 * </ul>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.ListBoxExample}
 *
 * <p>
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * <p>The items of a ListBox element are laid out in &lt;g:item> elements. Each item contains text
 * that will be added to the list of available items that will be shown, either in the drop down or
 * list. (Note that the tags of the item elements are not capitalized. This is meant to signal that
 * the item is not a runtime object, and so cannot have a <code>ui:field</code> attribute.) It is
 * also possible to explicitly specify item's value using value attribute as shown below.
 *
 * <p>For example:
 *
 * <pre>
 * &lt;g:ListBox>
 *  &lt;g:item>
 *    first
 *  &lt;/g:item>
 *  &lt;g:item value='2'>
 *    second
 *  &lt;/g:item>
 * &lt;/g:ListBox>
 * </pre>
 *
 * <p>
 *
 * <h3>Important usage note</h3>
 *
 * <b>Subclasses should neither read nor write option text directly from the option elements created
 * by this class, since such text may need to be wrapped in Unicode bidi formatting characters. They
 * can use the getOptionText and/or setOptionText methods for this purpose instead.</b>
 */
public class ListBox extends FocusWidget
    implements HasChangeHandlers, HasName {

  private static final String BIDI_ATTR_NAME = "bidiwrapped";

  private static final int INSERT_AT_END = -1;

  /**
   * Creates a ListBox widget that wraps an existing &lt;select&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   * @return list box
   */
  public static ListBox wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    ListBox listBox = new ListBox(element);

    // Mark it attached and remember it for cleanup.
    listBox.onAttach();
    RootPanel.detachOnWindowClose(listBox);

    return listBox;
  }

  /** Creates an empty list box in single selection mode. */
  public ListBox() {
    super(DOM.createSelect());
    setStyleName("gwt-ListBox");
  }

  /**
   * Creates an empty list box.
   *
   * @param isMultipleSelect specifies if multiple selection is enabled
   * @deprecated use {@link #setMultipleSelect(boolean)} instead.
   */
  @Deprecated
  public ListBox(boolean isMultipleSelect) {
    this();
    setMultipleSelect(isMultipleSelect);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be a &lt;select&gt; element.
   *
   * @param element the element to be used
   */
  protected ListBox(HTMLElement element) {
    super(element);
    assertTagName("select");
  }

  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  /**
   * Adds an item to the list box. This method has the same effect as
   *
   * <pre>
   * addItem(item, item)
   * </pre>
   *
   * @param item the text of the item to be added
   */
  public void addItem(String item) {
    insertItem(item, INSERT_AT_END);
  }

  /**
   * Adds an item to the list box, specifying an initial value for the item.
   *
   * @param item the text of the item to be added
   * @param value the item's value
   *     <code>null</code>
   */
  public void addItem(String item, String value) {
    insertItem(item, value, INSERT_AT_END);
  }


  /** Removes all items from the list box. */
  public void clear() {
    getSelectElement().options.length = 0;
  }

  /**
   * Gets the number of items present in the list box.
   *
   * @return the number of items
   */
  public int getItemCount() {
    return getSelectElement().options.getLength();
  }

  /**
   * Gets the text associated with the item at the specified index.
   *
   * @param index the index of the item whose text is to be retrieved
   * @return the text associated with the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public String getItemText(int index) {
    checkIndex(index);
    return getOptionText(getSelectElement().options.getAt(index));
  }

  /**
   * Gets the text for currently selected item. If multiple items are selected, this method will
   * return the text of the first selected item.
   *
   * @return the text for selected item, or {@code null} if none is selected
   */
  public String getSelectedItemText() {
    int index = getSelectedIndex();
    return index == -1 ? null : getItemText(index);
  }

  public String getName() {
    return getSelectElement().name;
  }

  /**
   * Gets the currently-selected item. If multiple items are selected, this method will return the
   * first selected item ({@link #isItemSelected(int)} can be used to query individual items).
   *
   * @return the selected index, or <code>-1</code> if none is selected
   */
  public int getSelectedIndex() {
    return getSelectElement().selectedIndex;
  }

  /**
   * Gets the value associated with the item at a given index.
   *
   * @param index the index of the item to be retrieved
   * @return the item's associated value
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public String getValue(int index) {
    checkIndex(index);
    return getSelectElement().options.getAt(index).value;
  }

  /**
   * Gets the value for currently selected item. If multiple items are selected, this method will
   * return the value of the first selected item.
   *
   * @return the value for selected item, or {@code null} if none is selected
   */
  public String getSelectedValue() {
    int index = getSelectedIndex();
    return index == -1 ? null : getValue(index);
  }

  /**
   * Gets the number of items that are visible. If only one item is visible, then the box will be
   * displayed as a drop-down list.
   *
   * @return the visible item count
   */
  public int getVisibleItemCount() {
    return getSelectElement().size;
  }

  /**
   * Inserts an item into the list box. Has the same effect as
   *
   * <pre>
   * insertItem(item, item, index)
   * </pre>
   *
   * @param item the text of the item to be inserted
   * @param index the index at which to insert it
   */
  public void insertItem(String item, int index) {
    insertItem(item, item, index);
  }

  /**
   * Inserts an item into the list box, specifying its direction and an initial value for the item.
   * If the index is less than zero, or greater than or equal to the length of the list, then the
   * item will be appended to the end of the list.
   *
   * @param item the text of the item to be inserted
   * @param value the item's value, to be submitted
   * @param index the index at which to insert it
   */
  public void insertItem(String item, String value, int index) {
    HTMLSelectElement select = getSelectElement();
    HTMLOptionElement option = DOM.createOption();
    setOptionText(option, item);
    option.value = value;

    int itemCount = select.options.length;
    if (index < 0 || index > itemCount) {
      index = itemCount;
    }
    if (index == itemCount) {
      select.add(option, null);
    } else {
      HTMLOptionElement before = select.options.getAt(index);
      select.add(option, before);
    }
  }

  /**
   * Determines whether an individual list item is selected.
   *
   * @param index the index of the item to be tested
   * @return <code>true</code> if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public boolean isItemSelected(int index) {
    checkIndex(index);
    return getSelectElement().options.getAt(index).selected;
  }

  /**
   * Gets whether this list allows multiple selection.
   *
   * @return <code>true</code> if multiple selection is allowed
   */
  public boolean isMultipleSelect() {
    return getSelectElement().multiple;
  }

  /**
   * Removes the item at the specified index.
   *
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void removeItem(int index) {
    checkIndex(index);
    getSelectElement().remove(index);
  }

  /**
   * Sets whether an individual list item is selected.
   *
   * <p>Note that setting the selection programmatically does <em>not</em> cause the {@link
   * ChangeHandler#onChange(ChangeEvent)} event to be fired.
   *
   * @param index the index of the item to be selected or unselected
   * @param selected <code>true</code> to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemSelected(int index, boolean selected) {
    checkIndex(index);
    getSelectElement().options.getAt(index).selected = selected;
  }

  /**
   * Sets the text associated with the item at a given index.
   *
   * @param index the index of the item to be set
   * @param text the item's new text
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemText(int index, String text) {
    checkIndex(index);
    if (text == null) {
      throw new NullPointerException("Cannot set an option to have null text");
    }
    setOptionText(getSelectElement().options.getAt(index), text);
  }

  /**
   * Sets whether this list allows multiple selections. <em>NOTE: Using this method can spuriously
   * fail on Internet Explorer 6.0.</em>
   *
   * @param multiple <code>true</code> to allow multiple selections
   */
  public void setMultipleSelect(boolean multiple) {
    getSelectElement().multiple = multiple;
  }

  public void setName(String name) {
    getSelectElement().name = name;
  }

  /**
   * Sets the currently selected index.
   *
   * <p>After calling this method, only the specified item in the list will remain selected. For a
   * ListBox with multiple selection enabled, see {@link #setItemSelected(int, boolean)} to select
   * multiple items at a time.
   *
   * <p>Note that setting the selected index programmatically does <em>not</em> cause the {@link
   * ChangeHandler#onChange(ChangeEvent)} event to be fired.
   *
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    getSelectElement().selectedIndex = index;
  }

  /**
   * Sets the value associated with the item at a given index. This value can be used for any
   * purpose, but is also what is passed to the server when the list box is submitted as part of a
   *.
   *
   * @param index the index of the item to be set
   * @param value the item's new value; cannot be <code>null</code>
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setValue(int index, String value) {
    checkIndex(index);
    getSelectElement().options.getAt(index).value = value;
  }

  /**
   * Sets the number of items that are visible. If only one item is visible, then the box will be
   * displayed as a drop-down list.
   *
   * @param visibleItems the visible item count
   */
  public void setVisibleItemCount(int visibleItems) {
    getSelectElement().size = visibleItems;
  }

  /**
   * Retrieves the text of an option element. If the text was set by {@link #setOptionText} and was
   * wrapped with Unicode bidi formatting characters, also removes those additional formatting
   * characters.
   *
   * @param option an option element
   * @return the element's text
   */
  protected String getOptionText(HTMLOptionElement option) {
    String text = option.textContent;
    if (option.hasAttribute(BIDI_ATTR_NAME) && text.length() > 1) {
      text = text.substring(1, text.length() - 1);
    }
    return text;
  }

  /**
   * <b>Affected Elements:</b>
   *
   * <ul>
   *   <li>-item# = the option at the specified index.
   * </ul>
   *
   * @see UIObject#onEnsureDebugId(String)
   */
  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);

    // Set the id of each option
    int numItems = getItemCount();
    for (int i = 0; i < numItems; i++) {
      ensureDebugId(getSelectElement().options.getAt(i), baseID, "item" + i);
    }
  }

  /**
   * Sets the text of an option element. If the direction of the text is opposite to the page's
   * direction, also wraps it with Unicode bidi formatting characters to prevent garbling, and
   * indicates that this was done by setting the option's <code>BIDI_ATTR_NAME</code> custom
   * attribute.
   *
   * @param option an option element
   * @param text text to be set to the element
   */
  protected void setOptionText(HTMLOptionElement option, String text) {
    option.textContent = text;
    option.removeAttribute(BIDI_ATTR_NAME);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  private HTMLSelectElement getSelectElement() {
    return Js.uncheckedCast(getElement());
  }
}
