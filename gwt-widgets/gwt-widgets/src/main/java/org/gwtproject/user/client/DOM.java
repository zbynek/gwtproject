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

import elemental2.dom.DomGlobal;
import elemental2.dom.EventInit;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLImageElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLSelectElement;
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableColElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.HTMLTableRowElement;
import elemental2.dom.HTMLTableSectionElement;
import elemental2.dom.MouseEvent;
import elemental2.dom.MouseEventInit;
import elemental2.dom.Node;
import jsinterop.base.Js;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.event.logical.shared.ResizeEvent;
import org.gwtproject.event.logical.shared.ResizeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;
import org.gwtproject.user.client.impl.DOMImpl;
import org.gwtproject.user.client.impl.DOMImplStandardBase;
import org.gwtproject.user.client.ui.PotentialElement;

/**
 * This class provides a set of static methods that allow you to manipulate the browser's Document
 * Object Model (DOM). It contains methods for manipulating both {@link HTMLElement elements} and {@link
 * Event events}.
 */
public class DOM {

  // The current event being fired
  private static Event currentEvent = null;
  static final DOMImpl impl = new DOMImplStandardBase();
  private static HTMLElement sCaptureElem;
  private static double gwtUid = 0.0;

  /**
   * Appends one element to another's list of children.
   *
   * <p>If the child element is a {@link PotentialElement}, it is first resolved.
   *
   * @param parent the parent element
   * @param child its new child
   * @see PotentialElement#resolve(HTMLElement)
   */
  public static void appendChild(HTMLElement parent, HTMLElement child) {
    assert !isPotential(parent) : "Cannot append to a PotentialElement";

    // If child isn't a PotentialElement, resolve() returns
    // the Element itself.
    parent.appendChild(resolve(child));
  }

  /**
   * Clones an element.
   *
   * @param elem the element to be cloned
   * @param deep should children be cloned as well?
   */
  public static HTMLElement clone(HTMLElement elem, boolean deep) {
    return Js.uncheckedCast(elem.cloneNode(deep));
  }

  /**
   * Compares two elements for equality. Note that this method is now deprecated because reference
   * identity accurately reports equality.
   *
   * @param elem1 the first element to be compared
   * @param elem2 the second element to be compared
   * @return <code>true</code> if they are in fact the same element
   * @deprecated Use identity comparison.
   */
  @Deprecated
  public static boolean compare(HTMLElement elem1, HTMLElement elem2) {
    return elem1 == elem2;
  }

  /**
   * Creates an HTML A element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createAnchor() {
    return createElement("a");
  }

  /**
   * Creates an HTML BUTTON element.
   *
   * @return the newly-created element
   */
  @SuppressWarnings("deprecation")
  public static HTMLElement createButton() {
    HTMLButtonElement btn = createElement("button");
    btn.type = "button";
    return btn;
  }

  public static HTMLElement createSubmitButton() {
    HTMLButtonElement btn = createElement("button");
    btn.type = "submit";
    return btn;
  }

  public static NativeEvent createHtmlEvent(String type, boolean canBubble, boolean cancelable) {
    EventInit details = EventInit.create();
    details.setBubbles(canBubble);
    details.setCancelable(cancelable);
    elemental2.dom.Event evt = new elemental2.dom.Event(type, details);
    return Js.uncheckedCast(evt);
  }

  public static MouseEvent createClickEvent(int detail, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey) {
    return createMouseEvent("click", true, true, detail, screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey, 1, (HTMLElement)null);
  }

  public static MouseEvent createMouseEvent(String type, boolean canBubble, boolean cancelable, int detail,
          int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey,
          boolean metaKey, int button, HTMLElement relatedTarget) {
    byte button1;
    if (button == 1) {
      button1 = 0;
    } else if (button == 4) {
      button1 = 1;
    } else {
      button1 = 2;
    }

    MouseEventInit init = MouseEventInit.create();
    init.setButton(button1);
    init.setBubbles(canBubble);
    init.setCancelable(cancelable);
    init.setDetail(detail);
    init.setScreenX((double)screenX);
    init.setScreenY((double)screenY);
    init.setClientX((double)clientX);
    init.setClientY((double)clientY);
    init.setCtrlKey(ctrlKey);
    init.setAltKey(altKey);
    init.setShiftKey(shiftKey);
    init.setMetaKey(metaKey);
    init.setRelatedTarget(Js.uncheckedCast(relatedTarget));
    return new MouseEvent(type, init);
  }

  public static NativeEvent createLoadEvent() {
    return createHtmlEvent("load", false, false);
  }

  /**
   * Creates an HTML CAPTION element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createCaption() {
    return createElement("caption");
  }

  /**
   * Creates an HTML COL element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createCol() {
    return createElement("col");
  }

  /**
   * Creates an HTML COLGROUP element.
   *
   * @return the newly-created element
   */
  public static HTMLTableColElement createColGroup() {
    return createElement("colgroup");
  }

  /**
   * Creates an HTML DIV element.
   *
   * @return the newly-created element
   */
  public static HTMLDivElement createDiv() {
    return createElement("div");
  }

  /**
   * Creates an HTML element.
   *
   * @param tagName the HTML tag of the element to be created
   * @return the newly-created element
   */
  public static <T extends HTMLElement> T createElement(String tagName) {
    return Js.uncheckedCast(DomGlobal.document.createElement(tagName));
  }

  /**
   * Creates an HTML FIELDSET element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createFieldSet() {
    return createElement("fieldset");
  }

  /**
   * Creates an HTML FORM element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createForm() {
    return createElement("form");
  }

  /**
   * Creates an HTML IFRAME element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createIFrame() {
    return createElement("iframe");
  }

  /**
   * Creates an HTML IMG element.
   *
   * @return the newly-created element
   */
  public static HTMLImageElement createImg() {
    return createElement("img");
  }

  /**
   * Creates an HTML INPUT type='CHECK' element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createInputCheck() {
    return createInput("check");
  }

  /**
   * Creates an HTML INPUT type='PASSWORD' element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createInputPassword() {
    return createInput("password");
  }

  /**
   * Creates an HTML INPUT type='RADIO' element.
   *
   * @param name the name of the group with which this radio button will be associated
   * @return the newly-created element
   */
  public static HTMLElement createInputRadio(String name) {
    return createInput("radio");
  }

  /**
   * Creates an HTML INPUT type='TEXT' element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createInputText() {
    return createInput("text");
  }

  public static HTMLInputElement createInput(String type) {
    HTMLInputElement input = createElement("input");
    input.type = type;
    return input;
  }

  /**
   * Creates an HTML LABEL element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createLabel() {
    return createElement("label");
  }

  /**
   * Creates an HTML LEGEND element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createLegend() {
    return createElement("legend");
  }

  /**
   * Creates an HTML OPTION element.
   *
   * @return the newly-created element
   */
  public static HTMLOptionElement createOption() {
    return createElement("option");
  }

  /**
   * Creates a single-selection HTML SELECT element. Equivalent to
   *
   * <pre>
   * createSelect(false)
   * </pre>
   *
   * @return the newly-created element
   */
  public static HTMLSelectElement createSelect() {
    return createElement("select");
  }

  /**
   * Creates an HTML SELECT element.
   *
   * @param multiple true if multiple selection of options is allowed
   * @return the newly-created element
   */
  public static HTMLElement createSelect(boolean multiple) {
    HTMLSelectElement selectElement = createSelect();
    selectElement.multiple = multiple;
    return selectElement;
  }

  /**
   * Creates an HTML SPAN element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createSpan() {
    return createElement("span");
  }

  /**
   * Creates an HTML TABLE element.
   *
   * @return the newly-created element
   */
  public static HTMLTableElement createTable() {
    return createElement("table");
  }

  /**
   * Creates an HTML TBODY element.
   *
   * @return the newly-created element
   */
  public static HTMLTableSectionElement createTBody() {
    return createElement("tbody");
  }

  /**
   * Creates an HTML TD element.
   *
   * @return the newly-created element
   */
  public static HTMLTableCellElement createTD() {
    return createElement("td");
  }

  /**
   * Creates an HTML TEXTAREA element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createTextArea() {
    return createElement("textarea");
  }

  /**
   * Creates an HTML TFOOT element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createTFoot() {
    return createElement("tfoot");
  }

  /**
   * Creates an HTML TH element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createTH() {
    return createElement("th");
  }

  /**
   * Creates an HTML THEAD element.
   *
   * @return the newly-created element
   */
  public static HTMLElement createTHead() {
    return createElement("thead");
  }

  /**
   * Creates an HTML TR element.
   *
   * @return the newly-created element
   */
  public static HTMLTableRowElement createTR() {
    return createElement("tr");
  }

  public static HTMLCanvasElement createCanvas() {
    return createElement("canvas");
  }

  /**
   * Generates a unique DOM id. The id is of the form "gwt-id-<unique integer>".
   *
   * @return a unique DOM id
   */
  public static String createUniqueId() {
    return "gwt-uid-" + gwtUid++;
  }

  /**
   * Cancels bubbling for the given event. This will stop the event from being propagated to parent
   * elements.
   *
   * @param evt the event on which to cancel bubbling
   * @param cancel <code>true</code> to cancel bubbling
   */
  public static void eventCancelBubble(Event evt, boolean cancel) {
    impl.eventCancelBubble(evt, cancel);
  }

  /**
   * Gets whether the ALT key was depressed when the given event occurred.
   *
   * @param evt the event to be tested
   * @return <code>true</code> if ALT was depressed when the event occurred
   * @deprecated Use {@link Event#getAltKey()} instead.
   */
  @Deprecated
  public static boolean eventGetAltKey(Event evt) {
    return evt.getAltKey();
  }

  /**
   * Gets the mouse buttons that were depressed when the given event occurred.
   *
   * @param evt the event to be tested
   * @return a bit-field, defined by {@link Event#BUTTON_LEFT}, {@link Event#BUTTON_MIDDLE}, and
   *     {@link Event#BUTTON_RIGHT}
   * @deprecated Use {@link Event#getButton()} instead.
   */
  @Deprecated
  public static int eventGetButton(Event evt) {
    return evt.getButton();
  }

  /**
   * Gets the mouse x-position within the browser window's client area.
   *
   * @param evt the event to be tested
   * @return the mouse x-position
   * @deprecated Use {@link Event#getClientX()} instead.
   */
  @Deprecated
  public static int eventGetClientX(Event evt) {
    return evt.getClientX();
  }

  /**
   * Gets the mouse y-position within the browser window's client area.
   *
   * @param evt the event to be tested
   * @return the mouse y-position
   * @deprecated Use {@link Event#getClientY()} instead.
   */
  @Deprecated
  public static int eventGetClientY(Event evt) {
    return evt.getClientY();
  }

  /**
   * Gets whether the CTRL key was depressed when the given event occurred.
   *
   * @param evt the event to be tested
   * @return <code>true</code> if CTRL was depressed when the event occurred
   * @deprecated Use {@link Event#getCtrlKey()} instead.
   */
  @Deprecated
  public static boolean eventGetCtrlKey(Event evt) {
    return evt.getCtrlKey();
  }

  /**
   * Gets the current event that is being fired. The current event is only available within the
   * lifetime of the onBrowserEvent function. Once the onBrowserEvent method returns, the current
   * event is reset to null.
   *
   * @return the current event
   */
  public static Event eventGetCurrentEvent() {
    return currentEvent;
  }

  /**
   * Gets the current target element of the given event. This is the element whose listener fired
   * last, not the element which fired the event initially.
   *
   * @param evt the event
   * @return the event's current target element
   * @see DOM#eventGetTarget(Event)
   */
  public static HTMLElement eventGetCurrentTarget(Event evt) {
    return evt.getCurrentEventTarget().cast();
  }

  /**
   * Gets the element from which the mouse pointer was moved (valid for {@link Event#ONMOUSEOVER}
   * and {@link Event#ONMOUSEOUT}).
   *
   * @param evt the event to be tested
   * @return the element from which the mouse pointer was moved
   */
  public static HTMLElement eventGetFromElement(Event evt) {
    return impl.eventGetFromElement(evt);
  }

  /**
   * Gets the key code associated with this event.
   *
   * <p>For {@link Event#ONKEYPRESS}, this method returns the Unicode value of the character
   * generated. For {@link Event#ONKEYDOWN} and {@link Event#ONKEYUP}, it returns the code
   * associated with the physical key.
   *
   * @param evt the event to be tested
   * @return the Unicode character or key code.
   * @deprecated Use {@link Event#getKeyCode()} instead.
   */
  @Deprecated
  public static int eventGetKeyCode(Event evt) {
    return evt.getKeyCode();
  }

  /**
   * Gets whether the META key was depressed when the given event occurred.
   *
   * @param evt the event to be tested
   * @return <code>true</code> if META was depressed when the event occurred
   * @deprecated Use {@link Event#getMetaKey()} instead.
   */
  @Deprecated
  public static boolean eventGetMetaKey(Event evt) {
    return evt.getMetaKey();
  }

  /**
   * Gets the velocity of the mouse wheel associated with the event along the Y axis.
   *
   * <p>The velocity of the event is an artificial measurement for relative comparisons of wheel
   * activity. It is affected by some non-browser factors, including choice of input hardware and
   * mouse acceleration settings. The sign of the velocity measurement agrees with the screen
   * coordinate system; negative values are towards the origin and positive values are away from the
   * origin. Standard scrolling speed is approximately ten units per event.
   *
   * @param evt the event to be examined.
   * @return The velocity of the mouse wheel.
   * @deprecated Use {@link Event#getMouseWheelVelocityY()} instead.
   */
  @Deprecated
  public static int eventGetMouseWheelVelocityY(Event evt) {
    return evt.getMouseWheelVelocityY();
  }

  /**
   * Gets the key-repeat state of this event. Only IE supports this attribute.
   *
   * @param evt the event to be tested
   * @return <code>true</code> if this key event was an auto-repeat
   * @deprecated not supported in any browser but IE
   */
  @Deprecated
  public static boolean eventGetRepeat(Event evt) {
    return impl.eventGetRepeat(evt);
  }

  /**
   * Gets the mouse x-position on the user's display.
   *
   * @param evt the event to be tested
   * @return the mouse x-position
   * @deprecated Use {@link Event#getScreenX()} instead.
   */
  @Deprecated
  public static int eventGetScreenX(Event evt) {
    return evt.getScreenX();
  }

  /**
   * Gets the mouse y-position on the user's display.
   *
   * @param evt the event to be tested
   * @return the mouse y-position
   * @deprecated Use {@link Event#getScreenY()} instead.
   */
  @Deprecated
  public static int eventGetScreenY(Event evt) {
    return evt.getScreenY();
  }

  /**
   * Gets whether the shift key was depressed when the given event occurred.
   *
   * @param evt the event to be tested
   * @return <code>true</code> if shift was depressed when the event occurred
   * @deprecated Use {@link Event#getShiftKey()} instead.
   */
  @Deprecated
  public static boolean eventGetShiftKey(Event evt) {
    return evt.getShiftKey();
  }

  /**
   * Returns the element that was the actual target of the given event.
   *
   * @param evt the event to be tested
   * @return the target element
   */
  public static HTMLElement eventGetTarget(Event evt) {
    return evt.getEventTarget().cast();
  }

  /**
   * Gets the element to which the mouse pointer was moved (only valid for {@link Event#ONMOUSEOUT}
   * and {@link Event#ONMOUSEOVER}).
   *
   * @param evt the event to be tested
   * @return the element to which the mouse pointer was moved
   */
  public static HTMLElement eventGetToElement(Event evt) {
    return impl.eventGetToElement(evt);
  }

  /**
   * Gets the enumerated type of this event (as defined in {@link Event}).
   *
   * @param evt the event to be tested
   * @return the event's enumerated type, or -1 if not defined
   */
  public static int eventGetType(Event evt) {
    return impl.eventGetTypeInt(evt);
  }

  /**
   * Gets the type of the given event as a string.
   *
   * @param evt the event to be tested
   * @return the event's type name
   * @deprecated Use {@link Event#getType()} instead.
   */
  @Deprecated
  public static String eventGetTypeString(Event evt) {
    return evt.getType();
  }

  /**
   * Prevents the browser from taking its default action for the given event.
   *
   * @param evt the event whose default action is to be prevented
   * @deprecated Use {@link Event#preventDefault()} instead.
   */
  @Deprecated
  public static void eventPreventDefault(Event evt) {
    evt.preventDefault();
  }

  /**
   * Sets the key code associated with the given keyboard event.
   *
   * @param evt the event whose key code is to be set
   * @param key the new key code
   * @deprecated this method only works in IE and should not have been added to the API
   */
  @Deprecated
  public static void eventSetKeyCode(Event evt, char key) {
    impl.eventSetKeyCode(evt, key);
  }

  /**
   * Returns a stringized version of the event. This string is for debugging purposes and will NOT
   * be consistent on different browsers.
   *
   * @param evt the event to stringize
   * @return a string form of the event
   * @deprecated Use {@link Event#getString()} instead.
   */
  @Deprecated
  public static String eventToString(Event evt) {
    return evt.getString();
  }


  /**
   * Gets any named property from an element, as a string.
   *
   * @param elem the element whose property is to be retrieved
   * @param attr the name of the property
   * @return the property's value
   * @deprecated Use the more appropriately named {@link HTMLElement#getAttribute(String)}  instead.
   */
  @Deprecated
  public static String getAttribute(HTMLElement elem, String attr) {
    return (String) Js.asPropertyMap(elem).get(attr);
  }

  /**
   * Gets a boolean property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param attr the name of the property to be set
   * @return the property's value as a boolean
   * @deprecated Use the more appropriately named {@link HTMLElement#getAttribute(String)}
   *     instead.
   */
  @Deprecated
  public static boolean getBooleanAttribute(HTMLElement elem, String attr) {
    return (Boolean) Js.asPropertyMap(elem).get(attr);
  }

  /**
   * Gets the element that currently has mouse capture.
   *
   * @return a handle to the capture element, or <code>null</code> if none exists
   */
  public static HTMLElement getCaptureElement() {
    return sCaptureElem;
  }

  /**
   * Gets an element's n-th child element.
   *
   * @param parent the element whose child is to be retrieved
   * @param index the index of the child element
   * @return the n-th child element
   */
  public static HTMLElement getChild(HTMLElement parent, int index) {
    return impl.getChild(parent, index);
  }

  /**
   * Gets the number of child elements present in a given parent element.
   *
   * @param parent the element whose children are to be counted
   * @return the number of children
   */
  public static int getChildCount(HTMLElement parent) {
    return impl.getChildCount(parent);
  }

  /**
   * Gets the index of a given child element within its parent.
   *
   * @param parent the parent element
   * @param child the child element
   * @return the child's index within its parent, or <code>-1</code> if it is not a child of the
   *     given parent
   */
  public static int getChildIndex(HTMLElement parent, HTMLElement child) {
    return impl.getChildIndex(parent, child);
  }

  /**
   * Gets the named attribute from the element.
   *
   * @param elem the element whose property is to be retrieved
   * @param attr the name of the attribute
   * @return the value of the attribute
   * @deprecated Use {@link HTMLElement#getAttribute(String)} instead.
   */
  @Deprecated
  public static String getElementAttribute(HTMLElement elem, String attr) {
    return elem.getAttribute(attr);
  }

  /**
   * Gets the element associated with the given unique id within the entire document.
   *
   * @param id the id whose associated element is to be retrieved
   * @return the associated element, or <code>null</code> if none is found
   */
  public static HTMLElement getElementById(String id) {
    return Js.uncheckedCast(DomGlobal.document.getElementById(id));
  }

  /**
   * Gets any named property from an element, as a boolean.
   *
   * @param elem the element whose property is to be retrieved
   * @param prop the name of the property
   * @return the property's value as a boolean
   * @deprecated Use {@link HTMLElement#getAttribute(String)} instead.
   */
  @Deprecated
  public static boolean getElementPropertyBoolean(HTMLElement elem, String prop) {
    return (Boolean) Js.asPropertyMap(elem).get(prop);
  }

  /**
   * Gets any named property from an element, as an int.
   *
   * @param elem the element whose property is to be retrieved
   * @param prop the name of the property
   * @return the property's value as an int
   * @deprecated Use {@link HTMLElement#getAttribute(String)} instead.
   */
  @Deprecated
  public static int getElementPropertyInt(HTMLElement elem, String prop) {
    return (Integer) Js.asPropertyMap(elem).get(prop);
  }

  /**
   * Gets the {@link EventListener} that will receive events for the given element. Only one such
   * listener may exist for a single element.
   *
   * @param elem the element whose listener is to be set
   * @return the element's event listener
   */
  public static EventListener getEventListener(HTMLElement elem) {
    return DOMImpl.getEventListener(elem);
  }

  /**
   * Gets the current set of events sunk by a given element.
   *
   * @param elem the element whose events are to be retrieved
   * @return a bitfield describing the events sunk on this element (its possible values are
   *     described in {@link Event})
   */
  public static int getEventsSunk(HTMLElement elem) {
    return impl.getEventsSunk(elem);
  }

  /**
   * Gets the first child element of the given element.
   *
   * @param elem the element whose child is to be retrieved
   * @return the child element
   */
  public static HTMLElement getFirstChild(HTMLElement elem) {
    return Js.uncheckedCast(elem.firstChild);
  }

  /**
   * Gets the src attribute of an img element. This method is paired with {@link #setImgSrc(HTMLElement,
   * String)} so that it always returns the correct url.
   *
   * @param img a non-null img whose src attribute is to be read.
   * @return the src url of the img
   */
  public static String getImgSrc(HTMLElement img) {
    return Js.<HTMLImageElement>uncheckedCast(img).src;
  }

  /**
   * Gets an HTML representation of an element's children.
   *
   * @param elem the element whose HTML is to be retrieved
   * @return the HTML representation of the element's children
   * @deprecated Use {@link HTMLElement#innerHTML} instead.
   */
  @Deprecated
  public static String getInnerHTML(HTMLElement elem) {
    return elem.innerHTML;
  }

  /**
   * Gets the text contained within an element. If the element has child elements, only the text
   * between them will be retrieved.
   *
   * @param elem the element whose inner text is to be retrieved
   * @return the text inside this element
   * @deprecated Use {@link HTMLElement#textContent} instead.
   */
  @Deprecated
  public static String getInnerText(HTMLElement elem) {
    return elem.textContent;
  }

  /**
   * Gets an integer property on a given element.
   *
   * @param elem the element whose property is to be retrieved
   * @param attr the name of the property to be retrieved
   * @return the property's value as an integer
   * @deprecated Use the more appropriately named {@link HTMLElement#getAttribute(String)} instead.
   */
  @Deprecated
  public static int getIntAttribute(HTMLElement elem, String attr) {
    return Js.asInt(Js.asPropertyMap(elem).get(attr));
  }

  /**
   * Gets an integer attribute on a given element's style.
   *
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the attribute to be retrieved
   * @return the style attribute's value as an integer
   */
  public static int getIntStyleAttribute(HTMLElement elem, String attr) {
    String val = elem.style.getPropertyValue(attr);
    return val != null ? Integer.parseInt(val) : 0;
  }

  /**
   * Gets an element's next sibling element.
   *
   * @param elem the element whose sibling is to be retrieved
   * @return the sibling element
   */
  public static HTMLElement getNextSibling(HTMLElement elem) {
    return Js.uncheckedCast(elem.nextSibling);
  }

  /**
   * Gets an element's parent element.
   *
   * @param elem the element whose parent is to be retrieved
   * @return the parent element
   */
  public static HTMLElement getParent(HTMLElement elem) {
    return Js.uncheckedCast(elem.parentElement);
  }

  /**
   * Gets an attribute of the given element's style.
   *
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the style attribute to be retrieved
   * @return the style attribute's value
   * @deprecated Use {@link elemental2.dom.CSSStyleDeclaration}.
   */
  @Deprecated
  public static String getStyleAttribute(HTMLElement elem, String attr) {
    return elem.style.getPropertyValue(attr);
  }

  /**
   * Inserts an element as a child of the given parent element, before another child of that parent.
   *
   * <p>If the child element is a {@link PotentialElement}, it is first resolved.
   *
   * @param parent the parent element
   * @param child the child element to add to <code>parent</code>
   * @param before an existing child element of <code>parent</code> before which <code>child</code>
   *     will be inserted
   * @see PotentialElement#resolve(HTMLElement)
   */
  public static void insertBefore(HTMLElement parent, HTMLElement child, HTMLElement before) {
    assert !isPotential(parent) : "Cannot insert into a PotentialElement";

    // If child isn't a PotentialElement, resolve() returns
    // the Element itself.
    parent.insertBefore(resolve(child), before);
  }

  /**
   * Inserts an element as a child of the given parent element.
   *
   * <p>If the child element is a {@link PotentialElement}, it is first resolved.
   *
   * @param parent the parent element
   * @param child the child element to add to <code>parent</code>
   * @param index the index before which the child will be inserted (any value greater than the
   *     number of existing children will cause the child to be appended)
   * @see PotentialElement#resolve(HTMLElement)
   */
  public static void insertChild(HTMLElement parent, HTMLElement child, int index) {
    assert !isPotential(parent) : "Cannot insert into a PotentialElement";

    // If child isn't a PotentialElement, resolve() returns
    // the Element itself.
    impl.insertChild(parent, resolve(child), index);
  }

  /**
   * Creates an <code>&lt;option&gt;</code> element and inserts it as a child of the specified
   * <code>&lt;select&gt;</code> element. If the index is less than zero, or greater than or equal
   * to the length of the list, then the option element will be appended to the end of the list.
   *
   * @param selectElem the <code>&lt;select&gt;</code> element
   * @param item the text of the new item; cannot be <code>null</code>
   * @param value the <code>value</code> attribute for the new <code>&lt;option&gt;</code>; cannot
   *     be <code>null</code>
   * @param index the index at which to insert the child
   */
  public static void insertListItem(HTMLElement selectElem, String item, String value, int index) {
    assert !isPotential(selectElem) : "Cannot insert into a PotentialElement";

    HTMLSelectElement select = Js.uncheckedCast(selectElem);
    HTMLOptionElement option = createOption();
    option.textContent = item;
    option.value = value;

    if ((index == -1) || (index == select.length)) {
      select.add(option, null);
    } else {
      HTMLOptionElement before = select.options.getAt(index);
      select.add(option, before);
    }
  }

  public static boolean isPotential(Object o) {
    try {
      return (o != null) && Js.asPropertyMap(o).has("__gwt_resolve");
    } catch (Exception e) {

    }
    return false;
  }

  private static HTMLElement resolve(HTMLElement maybePotential) {
    if ((Js.asPropertyMap(maybePotential).has("__gwt_resolve"))) {
      return (HTMLElement) Js.asPropertyMap(maybePotential).get("__gwt_resolve");
    } else {
      return maybePotential;
    }
  }

  /**
   * Releases mouse/touch/gesture capture on the given element. Calling this method has no effect if
   * the element does not currently have mouse/touch/gesture capture.
   *
   * @param elem the element to release capture
   * @see #setCapture(HTMLElement)
   */
  public static void releaseCapture(HTMLElement elem) {
    if ((sCaptureElem != null) && elem == sCaptureElem) {
      sCaptureElem = null;
    }
    impl.releaseCapture(elem);
  }

  /**
   * Removes the named attribute from the given element.
   *
   * @param elem the element whose attribute is to be removed
   * @param attr the name of the element to remove
   * @deprecated Use {@link HTMLElement#removeAttribute(String)} instead.
   */
  @Deprecated
  public static void removeElementAttribute(HTMLElement elem, String attr) {
    elem.removeAttribute(attr);
  }

  /**
   * Scrolls the given element into view.
   *
   * <p>This method crawls up the DOM hierarchy, adjusting the scrollLeft and scrollTop properties
   * of each scrollable element to ensure that the specified element is completely in view. It
   * adjusts each scroll position by the minimum amount necessary.
   *
   * @param elem the element to be made visible
   * @deprecated Use {@link HTMLElement#scrollIntoView()} instead.
   */
  @Deprecated
  public static void scrollIntoView(HTMLElement elem) {
    elem.scrollIntoView();
  }

  /**
   * Sets a property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param attr the name of the property to be set
   * @param value the new property value
   * @deprecated Use the more appropriately named {@link HTMLElement#setAttribute(String, String)}
   *     instead.
   */
  @Deprecated
  public static void setAttribute(HTMLElement elem, String attr, String value) {
    Js.asPropertyMap(elem).set(attr, value);
  }

  /**
   * Sets a boolean property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param attr the name of the property to be set
   * @param value the property's new boolean value
   * @deprecated Use the more appropriately named {@link HTMLElement#setAttribute(String,
   *     boolean)} instead.
   */
  @Deprecated
  public static void setBooleanAttribute(HTMLElement elem, String attr, boolean value) {
    Js.asPropertyMap(elem).set(attr, value);
  }

  /**
   * Sets mouse/touch/gesture capture on the given element. This element will directly receive all
   * mouse events until {@link #releaseCapture(HTMLElement)} is called on it.
   *
   * @param elem the element on which to set mouse/touch/gesture capture
   */
  public static void setCapture(HTMLElement elem) {
    sCaptureElem = elem;
    impl.setCapture(elem);
  }

  /**
   * Sets an attribute on a given element.
   *
   * @param elem element whose attribute is to be set
   * @param attr the name of the attribute
   * @param value the value to which the attribute should be set
   * @deprecated Use {@link HTMLElement#setAttribute(String, String)} instead.
   */
  @Deprecated
  public static void setElementAttribute(HTMLElement elem, String attr, String value) {
    elem.setAttribute(attr, value);
  }

  /**
   * Sets a property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param prop the name of the property to be set
   * @param value the new property value
   * @deprecated Use {@link HTMLElement#setAttribute(String, String)} instead.
   */
  @Deprecated
  public static void setElementProperty(HTMLElement elem, String prop, String value) {
    Js.asPropertyMap(elem).set(prop, value);
  }

  /**
   * Sets a boolean property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param prop the name of the property to be set
   * @param value the new property value as a boolean
   * @deprecated Use {@link HTMLElement#setAttribute(String, boolean)} instead.
   */
  @Deprecated
  public static void setElementPropertyBoolean(HTMLElement elem, String prop, boolean value) {
    Js.asPropertyMap(elem).set(prop, value);
  }

  /**
   * Sets an int property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param prop the name of the property to be set
   * @param value the new property value as an int
   * @deprecated Use {@link HTMLElement#setAttribute(String, double)} instead.
   */
  @Deprecated
  public static void setElementPropertyInt(HTMLElement elem, String prop, int value) {
    Js.asPropertyMap(elem).set(prop, (double) value);
  }

  /**
   * Sets the {@link EventListener} to receive events for the given element. Only one such listener
   * may exist for a single element.
   *
   * @param elem the element whose listener is to be set
   * @param listener the listener to receive {@link Event events}
   */
  public static void setEventListener(HTMLElement elem, EventListener listener) {
    DOMImpl.setEventListener(Js.uncheckedCast(elem), listener);
  }

  /**
   * Sets the src attribute of an img element. This method ensures that imgs only ever have their
   * contents requested one single time from the server.
   *
   * @param img a non-null img whose src attribute will be set.
   * @param src a non-null url for the img
   */
  public static void setImgSrc(HTMLElement img, String src) {
    Js.<HTMLImageElement>uncheckedCast(img).src = src;
  }

  /**
   * Sets the HTML contained within an element.
   *
   * @param elem the element whose inner HTML is to be set
   * @param html the new html
   * @deprecated Use {@link HTMLElement#innerHTML} instead.
   */
  @Deprecated
  public static void setInnerHTML(HTMLElement elem, @IsSafeHtml String html) {
    elem.innerHTML = html;
  }

  /**
   * Sets the text contained within an element. If the element already has children, they will be
   * destroyed.
   *
   * @param elem the element whose inner text is to be set
   * @param text the new text
   * @deprecated Use {@link HTMLElement#textContent} instead.
   */
  @Deprecated
  public static void setInnerText(HTMLElement elem, String text) {
    elem.textContent = text;
  }

  /**
   * Sets an integer property on the given element.
   *
   * @param elem the element whose property is to be set
   * @param attr the name of the property to be set
   * @param value the property's new integer value
   * @deprecated Use the more appropriately named {@link HTMLElement#setAttribute(String, double)}
   *     instead.
   */
  @Deprecated
  public static void setIntAttribute(HTMLElement elem, String attr, int value) {
    Js.asPropertyMap(elem).set(attr, (double) value);
  }

  /**
   * Sets an integer attribute on the given element's style.
   *
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new integer value
   */
  public static void setIntStyleAttribute(HTMLElement elem, String attr, int value) {
    elem.style.setProperty(attr, Integer.toString(value));
  }

  /**
   * Sets the option text of the given select object.
   *
   * @param select the select object whose option text is being set
   * @param text the text to set
   * @param index the index of the option whose text should be set
   */
  public static void setOptionText(HTMLSelectElement select, String text, int index) {
    select.options.getAt(index).textContent = text;
  }

  /**
   * Sets an attribute on the given element's style.
   *
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new value
   * @deprecated Use {@link elemental2.dom.CSSStyleDeclaration}
   *     instead.
   */
  @Deprecated
  public static void setStyleAttribute(HTMLElement elem, String attr, String value) {
    elem.style.setProperty(attr, value);
  }

  /**
   * Sinks a named event. Events will be fired to the nearest {@link EventListener} specified on any
   * of the element's parents.
   *
   * @param elem the element whose events are to be retrieved
   * @param eventTypeName name of the event to sink on this element
   */
  public static void sinkBitlessEvent(HTMLElement elem, String eventTypeName) {
    impl.sinkBitlessEvent(elem, eventTypeName);
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
    impl.sinkEvents(Js.uncheckedCast(elem), eventBits);
  }

  /**
   * Returns a stringized version of the element. This string is for debugging purposes and will NOT
   * be consistent on different browsers.
   *
   * @param elem the element to stringize
   * @return a string form of the element
   * @deprecated Use {@link HTMLElement#outerHTML} instead.
   */
  @Deprecated
  public static String toString(HTMLElement elem) {
    return elem.outerHTML;
  }

  /**
   * This method is called directly by native code when any event is fired.
   *
   * @param evt the handle to the event being fired.
   * @param elem the handle to the element that received the event.
   * @param listener the listener associated with the element that received the event.
   */
  public static void dispatchEvent(Event evt, HTMLElement elem, EventListener listener) {
    // Preserve the current event in case we are in a reentrant event dispatch.
    Event prevCurrentEvent = currentEvent;
    currentEvent = evt;

    dispatchEventImpl(evt, elem, listener);

    currentEvent = prevCurrentEvent;
  }

  /**
   * This method is a similar to {@link #dispatchEvent(Event, HTMLElement, EventListener)} but only
   * dispatches if an event listener is set to element.
   *
   * @param evt the handle to the event being fired.
   * @param elem the handle to the element that received the event.
   * @return {@code true} if the event was dispatched
   */
  public static boolean dispatchEvent(Event evt, HTMLElement elem) {
    EventListener eventListener = getEventListener(elem);
    if (eventListener == null) {
      return false;
    }
    dispatchEvent(evt, elem, eventListener);
    return true;
  }

  /** Initialize the event system if it has not already been initialized. */
  static void maybeInitializeEventSystem() {
    impl.maybeInitializeEventSystem();
  }

  /**
   * This method is called directly by native code when event preview is being used.
   *
   * @param evt a handle to the event being previewed
   * @return <code>false</code> to cancel the event
   */
  public static boolean previewEvent(Event evt) {
    // Fire a NativePreviewEvent to NativePreviewHandlers
    boolean ret = Event.fireNativePreviewEvent(evt);
    // If the preview cancels the event, stop it from bubbling and performing
    // its default action. Check for a null evt to allow unit tests to run.
    if (!ret && evt != null) {
      evt.stopPropagation();
      evt.preventDefault();
    }

    return ret;
  }

  private static void dispatchEventImpl(Event evt, HTMLElement elem, EventListener listener) {
    // If this element has capture...
    if (elem == sCaptureElem) {
      // ... and it's losing capture, clear sCaptureElem.
      if (eventGetType(evt) == Event.ONLOSECAPTURE) {
        sCaptureElem = null;
      }
    }

    // Pass the event to the listener.
    listener.onBrowserEvent(evt);
  }

  public static HandlerRegistration addWindowResizeHandler(ResizeHandler handler) {
    elemental2.dom.EventListener listener = (evt) -> handler.onResize(new WindowResizeEvent());
    DomGlobal.window.addEventListener("resize", listener);
    return () -> DomGlobal.window.removeEventListener("resize", listener);
  }

  public static int getAbsoluteTop(HTMLElement el) {
    double subPixelAbsoluteTop = el.getBoundingClientRect().top + getScrollingElement(el.ownerDocument).scrollTop;
    return Js.coerceToInt(subPixelAbsoluteTop);
  }

  public static int getAbsoluteLeft(HTMLElement el) {
    double subPixelAbsoluteTop = el.getBoundingClientRect().left + getScrollingElement(el.ownerDocument).scrollLeft;
    return Js.coerceToInt(subPixelAbsoluteTop);
  }

  private static HTMLElement getScrollingElement(elemental2.dom.Document doc) {
    HTMLElement scrollingElement = Js.uncheckedCast(doc.scrollingElement);
    if (scrollingElement != null) {
      return scrollingElement;
    } else {
      // could also check for compatMode
      return doc.documentElement;
    }
  }

  public static void removeAllChildren(HTMLElement element) {
    for (int i = element.childNodes.length - 1; i >= 0; i--) {
      element.removeChild(element.childNodes.getAt(i));
    }
  }

  public static boolean isElement(Object o) {
    return isNode(o) ? Js.<Node>uncheckedCast(o).nodeType == Node.ELEMENT_NODE : false;
  }

  public static boolean isNode(Object o) {
      try {
        return Js.isTruthy(o) && Js.isTruthy(Js.asPropertyMap(o).get("nodeType"));
      } catch (Exception var2) {
        return false;
      }
  }

  private static class WindowResizeEvent extends ResizeEvent {
    public WindowResizeEvent() {
      super(DomGlobal.document.documentElement.clientWidth,
              DomGlobal.document.documentElement.clientHeight);
    }
  }
}
