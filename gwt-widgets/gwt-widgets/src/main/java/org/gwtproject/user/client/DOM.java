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
import elemental2.dom.Node;
import jsinterop.base.Js;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.ImageElement;
import org.gwtproject.dom.client.OptionElement;
import org.gwtproject.dom.client.SelectElement;
import org.gwtproject.event.logical.shared.ResizeEvent;
import org.gwtproject.event.logical.shared.ResizeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.impl.DOMImpl;
import org.gwtproject.user.client.impl.DOMImplStandard;
import org.gwtproject.user.client.ui.PotentialElement;

/**
 * This class provides a set of static methods that allow you to manipulate the browser's Document
 * Object Model (DOM). It contains methods for manipulating both {@link Element elements} and {@link
 * Event events}.
 */
public class DOM {

  static final DOMImpl impl = new DOMImplStandard();
  private static Element sCaptureElem;

  /**
   * Appends one element to another's list of children.
   *
   * <p>If the child element is a {@link PotentialElement}, it is first resolved.
   *
   * @param parent the parent element
   * @param child its new child
   * @see PotentialElement#resolve(Element)
   */
  public static void appendChild(Element parent, Element child) {
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
  public static Element clone(Element elem, boolean deep) {
    return elem.cloneNode(deep).cast();
  }

  /**
   * Creates an HTML A element.
   *
   * @return the newly-created element
   */
  public static Element createAnchor() {
    return Document.get().createAnchorElement().cast();
  }

  /**
   * Creates an HTML CAPTION element.
   *
   * @return the newly-created element
   */
  public static Element createCaption() {
    return Document.get().createCaptionElement().cast();
  }

  /**
   * Creates an HTML COL element.
   *
   * @return the newly-created element
   */
  public static Element createCol() {
    return Document.get().createColElement().cast();
  }

  /**
   * Creates an HTML COLGROUP element.
   *
   * @return the newly-created element
   */
  public static Element createColGroup() {
    return Document.get().createColGroupElement().cast();
  }

  /**
   * Creates an HTML DIV element.
   *
   * @return the newly-created element
   */
  public static Element createDiv() {
    return Document.get().createDivElement().cast();
  }

  /**
   * Creates an HTML element.
   *
   * @param tagName the HTML tag of the element to be created
   * @return the newly-created element
   */
  public static Element createElement(String tagName) {
    return Document.get().createElement(tagName).cast();
  }

  /**
   * Creates an HTML FIELDSET element.
   *
   * @return the newly-created element
   */
  public static Element createFieldSet() {
    return Document.get().createFieldSetElement().cast();
  }

  /**
   * Creates an HTML FORM element.
   *
   * @return the newly-created element
   */
  public static Element createForm() {
    return Document.get().createFormElement().cast();
  }

  /**
   * Creates an HTML IFRAME element.
   *
   * @return the newly-created element
   */
  public static Element createIFrame() {
    return Document.get().createIFrameElement().cast();
  }

  /**
   * Creates an HTML IMG element.
   *
   * @return the newly-created element
   */
  public static Element createImg() {
    return Document.get().createImageElement().cast();
  }

  /**
   * Creates an HTML INPUT type='CHECK' element.
   *
   * @return the newly-created element
   */
  public static Element createInputCheck() {
    return Document.get().createCheckInputElement().cast();
  }

  /**
   * Creates an HTML INPUT type='PASSWORD' element.
   *
   * @return the newly-created element
   */
  public static Element createInputPassword() {
    return Document.get().createPasswordInputElement().cast();
  }

  /**
   * Creates an HTML INPUT type='RADIO' element.
   *
   * @param name the name of the group with which this radio button will be associated
   * @return the newly-created element
   */
  public static Element createInputRadio(String name) {
    return Document.get().createRadioInputElement(name).cast();
  }

  /**
   * Creates an HTML INPUT type='TEXT' element.
   *
   * @return the newly-created element
   */
  public static Element createInputText() {
    return Document.get().createTextInputElement().cast();
  }

  /**
   * Creates an HTML LABEL element.
   *
   * @return the newly-created element
   */
  public static Element createLabel() {
    return Document.get().createLabelElement().cast();
  }

  /**
   * Creates an HTML LEGEND element.
   *
   * @return the newly-created element
   */
  public static Element createLegend() {
    return Document.get().createLegendElement().cast();
  }

  /**
   * Creates an HTML OPTION element.
   *
   * @return the newly-created element
   */
  public static Element createOption() {
    return Document.get().createOptionElement().cast();
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
  public static Element createSelect() {
    return Document.get().createSelectElement().cast();
  }

  /**
   * Creates an HTML SELECT element.
   *
   * @param multiple true if multiple selection of options is allowed
   * @return the newly-created element
   */
  public static Element createSelect(boolean multiple) {
    SelectElement selectElement = Document.get().createSelectElement();
    selectElement.setMultiple(multiple);
    return selectElement.cast();
  }

  /**
   * Creates an HTML SPAN element.
   *
   * @return the newly-created element
   */
  public static Element createSpan() {
    return Document.get().createSpanElement().cast();
  }

  /**
   * Creates an HTML TABLE element.
   *
   * @return the newly-created element
   */
  public static Element createTable() {
    return Document.get().createTableElement().cast();
  }

  /**
   * Creates an HTML TBODY element.
   *
   * @return the newly-created element
   */
  public static Element createTBody() {
    return Document.get().createTBodyElement().cast();
  }

  /**
   * Creates an HTML TD element.
   *
   * @return the newly-created element
   */
  public static Element createTD() {
    return Document.get().createTDElement().cast();
  }

  /**
   * Creates an HTML TEXTAREA element.
   *
   * @return the newly-created element
   */
  public static Element createTextArea() {
    return Document.get().createTextAreaElement().cast();
  }

  /**
   * Creates an HTML TFOOT element.
   *
   * @return the newly-created element
   */
  public static Element createTFoot() {
    return Document.get().createTFootElement().cast();
  }

  /**
   * Creates an HTML TH element.
   *
   * @return the newly-created element
   */
  public static Element createTH() {
    return Document.get().createTHElement().cast();
  }

  /**
   * Creates an HTML THEAD element.
   *
   * @return the newly-created element
   */
  public static Element createTHead() {
    return Document.get().createTHeadElement().cast();
  }

  /**
   * Creates an HTML TR element.
   *
   * @return the newly-created element
   */
  public static Element createTR() {
    return Document.get().createTRElement().cast();
  }

  /**
   * Generates a unique DOM id. The id is of the form "gwt-id-<unique integer>".
   *
   * @return a unique DOM id
   */
  public static String createUniqueId() {
    return Document.get().createUniqueId();
  }

  /**
   * Gets the current target element of the given event. This is the element whose listener fired
   * last, not the element which fired the event initially.
   *
   * @param evt the event
   * @return the event's current target element
   * @see DOM#eventGetTarget(elemental2.dom.Event)
   */
  public static Element eventGetCurrentTarget(elemental2.dom.Event evt) {
    return Js.uncheckedCast(evt.currentTarget);
  }

  /**
   * Gets the element from which the mouse pointer was moved (valid for {@link Event#ONMOUSEOVER}
   * and {@link Event#ONMOUSEOUT}).
   *
   * @param evt the event to be tested
   * @return the element from which the mouse pointer was moved
   */
  public static Element eventGetFromElement(elemental2.dom.Event evt) {
    return impl.eventGetFromElement(evt);
  }

  /**
   * Returns the element that was the actual target of the given event.
   *
   * @param evt the event to be tested
   * @return the target element
   */
  public static Element eventGetTarget(elemental2.dom.Event evt) {
    return Js.uncheckedCast(evt.target);
  }

  /**
   * Gets the element to which the mouse pointer was moved (only valid for {@link Event#ONMOUSEOUT}
   * and {@link Event#ONMOUSEOVER}).
   *
   * @param evt the event to be tested
   * @return the element to which the mouse pointer was moved
   */
  public static Element eventGetToElement(elemental2.dom.Event evt) {
    return impl.eventGetToElement(evt);
  }

  /**
   * Gets the enumerated type of this event (as defined in {@link Event}).
   *
   * @param evt the event to be tested
   * @return the event's enumerated type, or -1 if not defined
   */
  public static int eventGetType(elemental2.dom.Event evt) {
    return impl.eventGetTypeInt(evt);
  }

  /**
   * Gets the element that currently has mouse capture.
   *
   * @return a handle to the capture element, or <code>null</code> if none exists
   */
  public static Element getCaptureElement() {
    return sCaptureElem;
  }

  /**
   * Gets an element's n-th child element.
   *
   * @param parent the element whose child is to be retrieved
   * @param index the index of the child element
   * @return the n-th child element
   */
  public static Element getChild(Element parent, int index) {
    return impl.getChild(parent, index);
  }

  /**
   * Gets the number of child elements present in a given parent element.
   *
   * @param parent the element whose children are to be counted
   * @return the number of children
   */
  public static int getChildCount(Element parent) {
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
  public static int getChildIndex(Element parent, Element child) {
    return impl.getChildIndex(parent, child);
  }

  /**
   * Gets the element associated with the given unique id within the entire document.
   *
   * @param id the id whose associated element is to be retrieved
   * @return the associated element, or <code>null</code> if none is found
   */
  public static Element getElementById(String id) {
    return Document.get().getElementById(id);
  }

  /**
   * Gets the {@link EventListener} that will receive events for the given element. Only one such
   * listener may exist for a single element.
   *
   * @param elem the element whose listener is to be set
   * @return the element's event listener
   */
  public static EventListener getEventListener(Element elem) {
    return DOMImpl.getEventListener(elem);
  }

  /**
   * Gets the current set of events sunk by a given element.
   *
   * @param elem the element whose events are to be retrieved
   * @return a bitfield describing the events sunk on this element (its possible values are
   *     described in {@link Event})
   */
  public static int getEventsSunk(Element elem) {
    return impl.getEventsSunk(elem);
  }

  /**
   * Gets the first child element of the given element.
   *
   * @param elem the element whose child is to be retrieved
   * @return the child element
   */
  public static Element getFirstChild(Element elem) {
    return elem.getFirstChildElement();
  }

  /**
   * Gets the src attribute of an img element. This method is paired with {@link #setImgSrc(Element,
   * String)} so that it always returns the correct url.
   *
   * @param img a non-null img whose src attribute is to be read.
   * @return the src url of the img
   */
  public static String getImgSrc(Element img) {
    return img.<ImageElement>cast().getSrc();
  }

  /**
   * Gets an integer attribute on a given element's style.
   *
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the attribute to be retrieved
   * @return the style attribute's value as an integer
   */
  public static int getIntStyleAttribute(Element elem, String attr) {
    String val = elem.getStyle().getProperty(attr);
    return val != null ? Integer.parseInt(val) : 0;
  }

  /**
   * Gets an element's next sibling element.
   *
   * @param elem the element whose sibling is to be retrieved
   * @return the sibling element
   */
  public static Element getNextSibling(Element elem) {
    return elem.getNextSibling().cast();
  }

  /**
   * Gets an element's parent element.
   *
   * @param elem the element whose parent is to be retrieved
   * @return the parent element
   */
  public static Element getParent(Element elem) {
    return elem.getParentElement();
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
   * @see PotentialElement#resolve(Element)
   */
  public static void insertBefore(Element parent, Element child, Element before) {
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
   * @see PotentialElement#resolve(Element)
   */
  public static void insertChild(Element parent, Element child, int index) {
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
  public static void insertListItem(Element selectElem, String item, String value, int index) {
    assert !isPotential(selectElem) : "Cannot insert into a PotentialElement";

    SelectElement select = selectElem.cast();
    OptionElement option = Document.get().createOptionElement();
    option.setText(item);
    option.setValue(value);

    if ((index == -1) || (index == select.getLength())) {
      select.add(option, null);
    } else {
      OptionElement before = select.getOptions().getItem(index);
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

  private static Element resolve(Element maybePotential) {
    if ((Js.asPropertyMap(maybePotential).has("__gwt_resolve"))) {
      return (Element) Js.asPropertyMap(maybePotential).get("__gwt_resolve");
    } else {
      return maybePotential;
    }
  }

  /**
   * Releases mouse/touch/gesture capture on the given element. Calling this method has no effect if
   * the element does not currently have mouse/touch/gesture capture.
   *
   * @param elem the element to release capture
   * @see #setCapture(Element)
   */
  public static void releaseCapture(Element elem) {
    if ((sCaptureElem != null) && elem == sCaptureElem) {
      sCaptureElem = null;
    }
    impl.releaseCapture(elem);
  }

  /**
   * Sets mouse/touch/gesture capture on the given element. This element will directly receive all
   * mouse events until {@link #releaseCapture(Element)} is called on it.
   *
   * @param elem the element on which to set mouse/touch/gesture capture
   */
  public static void setCapture(Element elem) {
    sCaptureElem = elem;
    impl.setCapture(elem);
  }

  /**
   * Sets the {@link EventListener} to receive events for the given element. Only one such listener
   * may exist for a single element.
   *
   * @param elem the element whose listener is to be set
   * @param listener the listener to receive {@link Event events}
   */
  public static void setEventListener(Element elem, EventListener listener) {
    DOMImpl.setEventListener(elem, listener);
  }

  /**
   * Sets the src attribute of an img element. This method ensures that imgs only ever have their
   * contents requested one single time from the server.
   *
   * @param img a non-null img whose src attribute will be set.
   * @param src a non-null url for the img
   */
  public static void setImgSrc(Element img, String src) {
    img.<ImageElement>cast().setSrc(src);
  }

  /**
   * Sets an integer attribute on the given element's style.
   *
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new integer value
   */
  public static void setIntStyleAttribute(Element elem, String attr, int value) {
    elem.getStyle().setProperty(attr, Integer.toString(value));
  }

  /**
   * Sets the option text of the given select object.
   *
   * @param select the select object whose option text is being set
   * @param text the text to set
   * @param index the index of the option whose text should be set
   */
  public static void setOptionText(Element select, String text, int index) {
    select.<SelectElement>cast().getOptions().getItem(index).setText(text);
  }

  /**
   * Sinks a named event. Events will be fired to the nearest {@link EventListener} specified on any
   * of the element's parents.
   *
   * @param elem the element whose events are to be retrieved
   * @param eventTypeName name of the event to sink on this element
   */
  public static void sinkBitlessEvent(Element elem, String eventTypeName) {
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
  public static void sinkEvents(Element elem, int eventBits) {
    impl.sinkEvents(elem, eventBits);
  }

  public static boolean isElement(Object o) {
    return o != null && Js.<Node>uncheckedCast(o).nodeType == Node.ELEMENT_NODE;
  }

  /**
   * This method is called directly by native code when any event is fired.
   *
   * @param evt the handle to the event being fired.
   * @param elem the handle to the element that received the event.
   * @param listener the listener associated with the element that received the event.
   */
  public static void dispatchEvent(elemental2.dom.Event evt, Element elem, EventListener listener) {
    // Preserve the current event in case we are in a reentrant event dispatch.
    dispatchEventImpl(evt, elem, listener);
  }

  /**
   * This method is a similar to {@link #dispatchEvent(elemental2.dom.Event, Element,
   * EventListener)} but only dispatches if an event listener is set to element.
   *
   * @param evt the handle to the event being fired.
   * @param elem the handle to the element that received the event.
   * @return {@code true} if the event was dispatched
   */
  public static boolean dispatchEvent(elemental2.dom.Event evt, Element elem) {
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
  public static boolean previewEvent(elemental2.dom.Event evt) {
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

  private static void dispatchEventImpl(
      elemental2.dom.Event evt, Element elem, EventListener listener) {
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

  public static int getKeyCode(elemental2.dom.Event event) {
    return Js.asPropertyMap(event).getAsAny("keyCode").asInt();
  }

  private static class WindowResizeEvent extends ResizeEvent {
    public WindowResizeEvent() {
      super(
          DomGlobal.document.documentElement.clientWidth,
          DomGlobal.document.documentElement.clientHeight);
    }
  }
}
