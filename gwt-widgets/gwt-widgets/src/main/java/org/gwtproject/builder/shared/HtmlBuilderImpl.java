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
package org.gwtproject.builder.shared;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.safecss.shared.SafeStyles;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.gwtproject.user.client.DOM;

/**
 * Implementation of methods in {@link ElementBuilderBase} used to render HTML as a string, using
 * innerHtml to generate an element.
 */
class HtmlBuilderImpl extends ElementBuilderImpl {

  /*
   * Common element builders, and those most likely to appear in a loop, are
   * created on initialization to avoid null checks. Less common element
   * builders are created lazily to avoid unnecessary object creation.
   */
  private final HtmlDivBuilder divBuilder = new HtmlDivBuilder(this);
  private final HtmlElementBuilder elementBuilder = new HtmlElementBuilder(this);
  private final StylesBuilder stylesBuilder = new HtmlStylesBuilder(this);
  private final HtmlTableCellBuilder tableCellBuilder = new HtmlTableCellBuilder(this);
  private final HtmlTableRowBuilder tableRowBuilder = new HtmlTableRowBuilder(this);
  private HtmlTableSectionBuilder tableSectionBuilder;

  /**
   * Used to builder the HTML string. We cannot use {@link
   * org.gwtproject.safehtml.shared.SafeHtmlBuilder} because it does some rudimentary checks that
   * the HTML tags are complete. Instead, we escape values before appending them.
   */
  private final StringBuilder sb = new StringBuilder();

  /** Return the HTML as a {@link SafeHtml} string. */
  public SafeHtml asSafeHtml() {
    // End all open tags.
    endAllTags();

    /*
     * sb is trusted because we only append trusted strings or escaped strings
     * to it.
     */
    return SafeHtmlUtils.fromTrustedString(sb.toString());
  }

  public void attribute(String name, int value) {
    trustedAttribute(escape(name), value);
  }

  public void attribute(String name, String value) {
    trustedAttribute(escape(name), value);
  }

  public HtmlDivBuilder startDiv() {
    trustedStart("div", divBuilder);
    return divBuilder;
  }

  public HtmlTableSectionBuilder startTBody() {
    return startTableSection("tbody");
  }

  public HtmlTableCellBuilder startTD() {
    trustedStart("td", tableCellBuilder);
    return tableCellBuilder;
  }

  public HtmlTableSectionBuilder startTFoot() {
    return startTableSection("tfoot");
  }

  public HtmlTableCellBuilder startTH() {
    trustedStart("th", tableCellBuilder);
    return tableCellBuilder;
  }

  public HtmlTableSectionBuilder startTHead() {
    return startTableSection("thead");
  }

  public HtmlTableRowBuilder startTR() {
    trustedStart("tr", tableRowBuilder);
    return tableRowBuilder;
  }

  @Override
  public StylesBuilder style() {
    return stylesBuilder;
  }

  public StylesBuilder styleProperty(SafeStyles style) {
    assertCanAddStylePropertyImpl();
    sb.append(style.asString());
    return style();
  }

  /** Add a trusted attribute without escaping the name. */
  public void trustedAttribute(String name, int value) {
    assertCanAddAttributeImpl();
    sb.append(" ").append(name).append("=\"").append(value).append("\"");
  }

  /** Add a trusted attribute without escaping the name. The value is still escaped. */
  public void trustedAttribute(String name, String value) {
    assertCanAddAttributeImpl();
    sb.append(" ").append(name).append("=\"").append(escape(value)).append("\"");
  }

  public HtmlElementBuilder trustedStart(String tagName) {
    trustedStart(tagName, elementBuilder);
    return elementBuilder;
  }

  @Override
  protected void doCloseStartTagImpl() {
    sb.append(">");
  }

  @Override
  protected void doCloseStyleAttributeImpl() {
    sb.append("\"");
  }

  @Override
  protected void doEndStartTagImpl() {
    sb.append(" />");
  }

  @Override
  protected void doEndTagImpl(String tagName) {
    /*
     * Add an end tag.
     *
     * Some browsers do not behave correctly if you self close (ex <select />)
     * certain tags, so we always add the end tag unless the element
     * specifically forbids an end tag (see doEndStartTagImpl()).
     *
     * The tag name is safe because it comes from the stack, and tag names are
     * checked before they are added to the stack.
     */
    sb.append("</").append(tagName).append(">");
  }

  @Override
  protected HTMLElement doFinishImpl() {
    HTMLElement tmp = DOM.createDiv();
    tmp.innerHTML = asSafeHtml().toString();
    return Js.uncheckedCast(tmp.firstChild);
  }

  @Override
  protected void doHtmlImpl(SafeHtml html) {
    sb.append(html.asString());
  }

  @Override
  protected void doOpenStyleImpl() {
    sb.append(" style=\"");
  }

  @Override
  protected void doTextImpl(String text) {
    sb.append(escape(text));
  }

  /**
   * Escape a string.
   *
   * @param s the string to escape
   */
  private String escape(String s) {
    return SafeHtmlUtils.htmlEscape(s);
  }

  /** Start a table section of the specified tag name. */
  private HtmlTableSectionBuilder startTableSection(String tagName) {
    if (tableSectionBuilder == null) {
      tableSectionBuilder = new HtmlTableSectionBuilder(this);
    }
    trustedStart(tagName, tableSectionBuilder);
    return tableSectionBuilder;
  }

  /** Start a tag using the specified builder. The tagName is not checked or escaped. */
  private void trustedStart(String tagName, ElementBuilderBase<?> builder) {
    onStart(tagName, builder);
    sb.append("<").append(tagName);
  }
}
