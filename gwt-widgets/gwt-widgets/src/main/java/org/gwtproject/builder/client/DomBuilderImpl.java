/*
 * Copyright © 2019 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.builder.client;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTableSectionElement;
import jsinterop.base.Js;
import org.gwtproject.builder.shared.ElementBuilderBase;
import org.gwtproject.builder.shared.ElementBuilderImpl;
import org.gwtproject.builder.shared.StylesBuilder;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.user.client.DOM;

/**
 * Implementation of methods in {@link ElementBuilderBase} used to render Elements using DOM
 * manipulation.
 */
class DomBuilderImpl extends ElementBuilderImpl {

  /*
   * Common element builders are created on initialization to avoid null checks.
   * Less common element builders are created lazily to avoid unnecessary object
   * creation.
   */
  private final DomDivBuilder divBuilder = new DomDivBuilder(this);
  private final DomElementBuilder elementBuilder = new DomElementBuilder(this);
  private final StylesBuilder stylesBuilder = new DomStylesBuilder(this);
  private final DomTableCellBuilder tableCellBuilder = new DomTableCellBuilder(this);
  private final DomTableRowBuilder tableRowBuilder = new DomTableRowBuilder(this);
  private DomTableSectionBuilder tableSectionBuilder;

  /** The root element of the DOM structure being built. */
  private HTMLElement rootElement;

  /** The element at the top of the stack. We use DOM manipulation to move up and down the stack. */
  private HTMLElement currentElement;


  public DomDivBuilder startDiv() {
    start(DOM.createDiv(), divBuilder);
    return divBuilder;
  }

  public DomTableSectionBuilder startTBody() {
    return startTableSection(DOM.createTBody());
  }

  public DomTableCellBuilder startTD() {
    start(DOM.createTD(), tableCellBuilder);
    return tableCellBuilder;
  }

  public DomTableSectionBuilder startTFoot() {
    return startTableSection(DOM.createTFoot());
  }

  public DomTableCellBuilder startTH() {
    start(DOM.createTH(), tableCellBuilder);
    return tableCellBuilder;
  }

  public DomTableSectionBuilder startTHead() {
    return startTableSection(DOM.createTHead());
  }

  public DomTableRowBuilder startTR() {
    start(DOM.createTR(), tableRowBuilder);
    return tableRowBuilder;
  }

  @Override
  public StylesBuilder style() {
    return stylesBuilder;
  }

  public DomElementBuilder trustedStart(String tagName) {
    /*
     * Validate the tag before trying to create the element, or the browser may
     * throw a JS error and prevent us from triggering an
     * IllegalArgumentException.
     */
    assertValidTagName(tagName);
    start(DOM.createElement(tagName), elementBuilder);
    return elementBuilder;
  }

  @Override
  protected void doCloseStartTagImpl() {
    // No-op.
  }

  @Override
  protected void doCloseStyleAttributeImpl() {
    // No-op.
  }

  @Override
  protected void doEndStartTagImpl() {
    popElement();
  }

  @Override
  protected void doEndTagImpl(String tagName) {
    popElement();
  }

  @Override
  protected HTMLElement doFinishImpl() {
    return rootElement;
  }

  @Override
  protected void doHtmlImpl(SafeHtml html) {
    getCurrentElement().innerHTML = html.asString();
  }

  @Override
  protected void doOpenStyleImpl() {
    // No-op.
  }

  @Override
  protected void doTextImpl(String text) {
    getCurrentElement().textContent = text;
  }

  @Override
  protected void lockCurrentElement() {
    // Overridden for visibility.
    super.lockCurrentElement();
  }

  /**
   * Assert that the builder is in a state where an attribute can be added.
   *
   * @return the element on which the attribute can be set
   * @throw {@link IllegalStateException} if the start tag is closed
   */
  HTMLElement assertCanAddAttribute() {
    assertCanAddAttributeImpl();
    return getCurrentElement();
  }

  /**
   * Assert that the builder is in a state where a style property can be added.
   *
   * @return the style on which the property can be set
   * @throw {@link IllegalStateException} if the style is not accessible
   */
  CSSStyleDeclaration assertCanAddStyleProperty() {
    assertCanAddStylePropertyImpl();
    return getCurrentElement().style;
  }

  /** Get the element current being built. */
  HTMLElement getCurrentElement() {
    if (currentElement == null) {
      throw new IllegalStateException("There are no elements on the stack.");
    }
    return currentElement;
  }

  /** Pop to the previous element in the stack. */
  private void popElement() {
    currentElement = Js.uncheckedCast(getCurrentElement().parentElement);
  }

  /**
   * Start a child element.
   *
   * @param element the element to start
   * @param builder the builder used to builder the new element
   */
  private void start(HTMLElement element, ElementBuilderBase<?> builder) {
    onStart(element.tagName, builder);

    // Set the root element.
    if (rootElement == null) {
      // This is the new root element.
      rootElement = element;
    } else {
      // Appending to the current element.
      getCurrentElement().appendChild(element);
    }

    // Add the element to the stack.
    currentElement = element;
  }

  /** Start a table section using the specified {@link HTMLTableSectionElement}. */
  private DomTableSectionBuilder startTableSection(HTMLTableSectionElement section) {
    if (tableSectionBuilder == null) {
      tableSectionBuilder = new DomTableSectionBuilder(this);
    }
    start(section, tableSectionBuilder);
    return tableSectionBuilder;
  }
}
