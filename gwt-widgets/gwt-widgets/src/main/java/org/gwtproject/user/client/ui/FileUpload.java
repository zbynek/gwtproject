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
import elemental2.dom.HTMLInputElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.ChangeEvent;
import org.gwtproject.event.dom.client.ChangeHandler;
import org.gwtproject.event.dom.client.HasChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.DOM;

/**
 * A widget that wraps the HTML &lt;input type='file'&gt; element.
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.FormPanelExample}
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-FileUpload {}
 *       <h3>NOTICE about styling</h3>
 *       <p>The developer should be aware that most browsers do not allow styling many properties of
 *       the rendered input-file element because of security restrictions.<br>
 *       You can style certain properties like position, visibility, opacity, etc. But size, color,
 *       backgrounds etc. will not work either using css or calling widget methods like setSize().
 * </dl>
 */
public class FileUpload extends FocusWidget implements HasName, HasChangeHandlers, HasEnabled {

  /**
   * Creates a FileUpload widget that wraps an existing &lt;input type='file'&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static FileUpload wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    FileUpload fileUpload = new FileUpload(element);

    // Mark it attached and remember it for cleanup.
    fileUpload.onAttach();
    RootPanel.detachOnWindowClose(fileUpload);

    return fileUpload;
  }

  /** Constructs a new file upload widget. */
  public FileUpload() {
    this(DOM.createInput("file"));
    setStyleName("gwt-FileUpload");
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be an &lt;input&gt; element whose type is 'file'.
   *
   * @param element the element to be used
   */
  protected FileUpload(HTMLElement element) {
    setElement(element);
    assertTagName("input");
    assert getElement().getAttribute("type").equalsIgnoreCase("file");
  }

  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  /**
   * Gets the filename selected by the user. This property has no mutator, as browser security
   * restrictions preclude setting it.
   *
   * @return the widget's filename
   */
  public String getFilename() {
    return getInputElement().value;
  }

  public String getName() {
    return getInputElement().name;
  }

  /**
   * Gets whether this widget is enabled.
   *
   * @return <code>true</code> if the widget is enabled
   */
  public boolean isEnabled() {
    return Js.isFalsy(Js.asPropertyMap(getElement()).get("disabled"));
  }

  /**
   * Sets whether this widget is enabled.
   *
   * @param enabled <code>true</code> to enable the widget, <code>false</code> to disable it
   */
  public void setEnabled(boolean enabled) {
    Js.asPropertyMap(getElement()).set("disabled", !enabled);
  }

  public void setName(String name) {
    getInputElement().name = name;
  }

  private HTMLInputElement getInputElement() {
    return Js.uncheckedCast(getElement());
  }

  /**
   * Programmatic equivalent of the user clicking the button, opening the file selection browser.
   *
   * <p>NOTE: in certain browsers programmatic click is disabled if the element display is none, for
   * instance in webkit you have to move the element off screen.
   */
  public void click() {
    getInputElement().click();
  }
}
