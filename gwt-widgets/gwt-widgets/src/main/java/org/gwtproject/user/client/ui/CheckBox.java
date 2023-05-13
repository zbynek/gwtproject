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
package org.gwtproject.user.client.ui;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLLabelElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.ClickEvent;
import org.gwtproject.event.dom.client.ClickHandler;
import org.gwtproject.event.logical.shared.ValueChangeEvent;
import org.gwtproject.event.logical.shared.ValueChangeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.safehtml.client.HasSafeHtml;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;

/**
 * A standard check box widget.
 *
 * <p>This class also serves as a base class for {@link RadioButton}.
 *
 * <p><img class='gallery' src='doc-files/CheckBox.png'/>
 *
 * <p>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-CheckBox
 *   <dd>the outer element
 *   <dt>.gwt-CheckBox-disabled
 *   <dd>applied when Checkbox is disabled
 * </dl>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.CheckBoxExample}
 */
public class CheckBox extends ButtonBase
    implements HasName,
        HasValue<Boolean>,
        HasWordWrap,
        HasSafeHtml {

  final DirectionalTextHelper directionalTextHelper;
  HTMLInputElement inputElem;
  HTMLLabelElement labelElem;
  private boolean valueChangeHandlerInitialized;

  /** Creates a check box with no label. */
  public CheckBox() {
    this(DOM.createInputCheck());
    setStyleName("gwt-CheckBox");
  }

  /**
   * Creates a check box with the specified text label.
   *
   * @param label the check box's label
   */
  public CheckBox(SafeHtml label) {
    this(label.asString(), true);
  }

  /**
   * Creates a check box with the specified text label.
   *
   * @param label the check box's label
   */
  public CheckBox(String label) {
    this();
    setText(label);
  }

  /**
   * Creates a check box with the specified text label.
   *
   * @param label the check box's label
   * @param asHTML <code>true</code> to treat the specified label as html
   */
  public CheckBox(@IsSafeHtml String label, boolean asHTML) {
    this();
    if (asHTML) {
      setHTML(label);
    } else {
      setText(label);
    }
  }

  protected CheckBox(HTMLElement elem) {
    super(DOM.createSpan());

    inputElem = Js.uncheckedCast(elem);
    labelElem = Js.uncheckedCast(DomGlobal.document.createElement("label"));

    getElement().appendChild(inputElem);
    getElement().appendChild(labelElem);

    String uid = DOM.createUniqueId();
    inputElem.id = uid;
    labelElem.htmlFor = uid;

    directionalTextHelper = new DirectionalTextHelper(labelElem, true);

    // Accessibility: setting tab index to be 0 by default, ensuring element
    // appears in tab sequence. FocusWidget's setElement method already
    // calls setTabIndex, which is overridden below. However, at the time
    // that this call is made, inputElem has not been created. So, we have
    // to call setTabIndex again, once inputElem has been created.
    setTabIndex(0);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
    // Is this the first value change handler? If so, time to add handlers
    if (!valueChangeHandlerInitialized) {
      ensureDomEventHandlers();
      valueChangeHandlerInitialized = true;
    }
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * Returns the value property of the input element that backs this widget. This is the value that
   * will be associated with the CheckBox name and submitted to the server if a form
   * that holds it is submitted and the box is checked.
   *
   * <p>Don't confuse this with {@link #getValue}, which returns true or false if the widget is
   * checked.
   */
  public String getFormValue() {
    return inputElem.value;
  }

  @Override
  public String getHTML() {
    return directionalTextHelper.getHtml();
  }

  @Override
  public String getName() {
    return inputElem.name;
  }

  @Override
  public int getTabIndex() {
    return inputElem.tabIndex;
  }

  @Override
  public String getText() {
    return directionalTextHelper.getText();
  }


  /**
   * Determines whether this check box is currently checked.
   *
   * <p>Note that this <em>does not</em> return the value property of the checkbox input element
   * wrapped by this widget. For access to that property, see {@link #getFormValue()}
   *
   * @return <code>true</code> if the check box is checked, false otherwise. Will not return null
   */
  @Override
  public Boolean getValue() {
    if (isAttached()) {
      return inputElem.checked;
    } else {
      return inputElem.defaultChecked;
    }
  }

  @Override
  public boolean getWordWrap() {
    return !"nowrap".equals(getElement().style.whiteSpace);
  }

  /**
   * Determines whether this check box is currently checked.
   *
   * @return <code>true</code> if the check box is checked
   * @deprecated Use {@link #getValue} instead
   */
  @Deprecated
  public boolean isChecked() {
    // Funny comparison b/c getValue could in theory return null
    return getValue() == true;
  }

  @Override
  public boolean isEnabled() {
    return !inputElem.disabled;
  }

  @Override
  public void setAccessKey(char key) {
    inputElem.accessKey = "" + key;
  }

  /**
   * Checks or unchecks this check box. Does not fire {@link ValueChangeEvent}. (If you want the
   * event to fire, use {@link #setValue(Boolean, boolean)})
   *
   * @param checked <code>true</code> to check the check box.
   * @deprecated Use {@link #setValue(Boolean)} instead
   */
  @Deprecated
  public void setChecked(boolean checked) {
    setValue(checked);
  }

  @Override
  public void setEnabled(boolean enabled) {
    inputElem.disabled = !enabled;
    if (enabled) {
      removeStyleDependentName("disabled");
    } else {
      addStyleDependentName("disabled");
    }
  }

  @Override
  public void setFocus(boolean focused) {
    if (focused) {
      inputElem.focus();
    } else {
      inputElem.blur();
    }
  }

  /**
   * Set the value property on the input element that backs this widget. This is the value that will
   * be associated with the CheckBox's name and submitted to the server if a form that
   * holds it is submitted and the box is checked.
   *
   * <p>Don't confuse this with {@link #setValue}, which actually checks and unchecks the box.
   *
   * @param value
   */
  public void setFormValue(String value) {
    inputElem.setAttribute("value", value);
  }

  @Override
  public void setHTML(@IsSafeHtml String html) {
    directionalTextHelper.setHtml(html);
  }

  @Override
  public void setName(String name) {
    inputElem.name = name;
  }

  @Override
  public void setTabIndex(int index) {
    // Need to guard against call to setTabIndex before inputElem is
    // initialized. This happens because FocusWidget's (a superclass of
    // CheckBox) setElement method calls setTabIndex before inputElem is
    // initialized. See CheckBox's protected constructor for more information.
    if (inputElem != null) {
      inputElem.tabIndex = index;
    }
  }

  @Override
  public void setText(String text) {
    directionalTextHelper.setText(text);
  }

  /**
   * Checks or unchecks the check box.
   *
   * <p>Note that this <em>does not</em> set the value property of the checkbox input element
   * wrapped by this widget. For access to that property, see {@link #setFormValue(String)}
   *
   * @param value true to check, false to uncheck; null value implies false
   */
  @Override
  public void setValue(Boolean value) {
    setValue(value, false);
  }

  /**
   * Checks or unchecks the check box, firing {@link ValueChangeEvent} if appropriate.
   *
   * <p>Note that this <em>does not</em> set the value property of the checkbox input element
   * wrapped by this widget. For access to that property, see {@link #setFormValue(String)}
   *
   * @param value true to check, false to uncheck; null value implies false
   * @param fireEvents If true, and value has changed, fire a {@link ValueChangeEvent}
   */
  @Override
  public void setValue(Boolean value, boolean fireEvents) {
    if (value == null) {
      value = Boolean.FALSE;
    }

    Boolean oldValue = getValue();
    inputElem.checked = value;
    inputElem.defaultChecked = value;
    if (value.equals(oldValue)) {
      return;
    }
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  @Override
  public void setWordWrap(boolean wrap) {
    getElement().style.whiteSpace = wrap ? "normal" : "nowrap";
  }

  // Unlike other widgets the CheckBox sinks on its inputElement, not
  // its wrapper
  @Override
  public void sinkEvents(int eventBitsToAdd) {
    if (isOrWasAttached()) {
      Event.sinkEvents(inputElem, eventBitsToAdd | Event.getEventsSunk(inputElem));
    } else {
      super.sinkEvents(eventBitsToAdd);
    }
  }

  protected void ensureDomEventHandlers() {
    addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            // Checkboxes always toggle their value, no need to compare
            // with old value. Radio buttons are not so lucky, see
            // overrides in RadioButton
            ValueChangeEvent.fire(CheckBox.this, getValue());
          }
        });
  }

  /**
   * <b>Affected Elements:</b>
   *
   * <ul>
   *   <li>-label = label next to checkbox.
   * </ul>
   *
   * @see UIObject#onEnsureDebugId(String)
   */
  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);
    ensureDebugId(labelElem, baseID, "label");
    ensureDebugId(inputElem, baseID, "input");
    labelElem.htmlFor = inputElem.id;
  }

  /**
   * This method is called when a widget is attached to the browser's document. onAttach needs
   * special handling for the CheckBox case. Must still call {@link Widget#onAttach()} to preserve
   * the <code>onAttach</code> contract.
   */
  @Override
  protected void onLoad() {
    DOM.setEventListener(inputElem, this);
  }

  /**
   * This method is called when a widget is detached from the browser's document. Overridden because
   * of IE bug that throws away checked state and in order to clear the event listener off of the
   * <code>inputElem</code>.
   */
  @Override
  protected void onUnload() {
    // Clear out the inputElem's event listener (breaking the circular
    // reference between it and the widget).
    DOM.setEventListener(inputElem, null);
    setValue(getValue());
  }

  /**
   * Replace the current input element with a new one. Preserves all state except for the name
   * property, for nasty reasons related to radio button grouping. (See implementation of {@link
   * RadioButton#setName}.)
   *
   * @param elem the new input element
   */
  protected void replaceInputElement(HTMLElement elem) {
    HTMLInputElement newInputElem = Js.uncheckedCast(elem);
    // Collect information we need to set
    int tabIndex = getTabIndex();
    boolean checked = getValue();
    boolean enabled = isEnabled();
    String formValue = getFormValue();
    String uid = inputElem.id;
    String accessKey = inputElem.accessKey;
    int sunkEvents = Event.getEventsSunk(inputElem);

    // Clear out the old input element
    DOM.setEventListener(inputElem, null);

    getElement().replaceChild(newInputElem, inputElem);

    // Sink events on the new element
    Event.sinkEvents(elem, Event.getEventsSunk(inputElem));
    Event.sinkEvents(inputElem, 0);
    inputElem = newInputElem;

    // Setup the new element
    Event.sinkEvents(inputElem, sunkEvents);
    inputElem.id = uid;
    if (!"".equals(accessKey)) {
      inputElem.accessKey = accessKey;
    }
    setTabIndex(tabIndex);
    setValue(checked);
    setEnabled(enabled);
    setFormValue(formValue);

    // Set the event listener
    if (isAttached()) {
      DOM.setEventListener(inputElem, this);
    }
  }
}
