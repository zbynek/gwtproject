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
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.event.dom.client.ClickEvent;
import org.gwtproject.event.dom.client.ClickHandler;
import org.gwtproject.safehtml.client.HasSafeHtml;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeUri;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeUri;
import org.gwtproject.safehtml.shared.annotations.SuppressIsSafeHtmlCastCheck;
import org.gwtproject.user.client.DOM;

/**
 * A widget that represents a simple &lt;a&gt; element.
 *
 *
 * <p>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-Anchor { }
 * </ul>
 *
 */
public class Anchor extends FocusWidget
    implements HasHorizontalAlignment,
        HasName,
        HasHTML,
        HasWordWrap,
        HasSafeHtml {

  /**
   * The default HREF is a no-op javascript statement. We need an href to ensure that the browser
   * renders the anchor with native styles, such as underline and font color.
   */
  private static final String DEFAULT_HREF = "javascript:;";

  /**
   * Creates an Anchor widget that wraps an existing &lt;a&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static Anchor wrap(HTMLElement element) {
    // Assert that the element is attached.
    assert DomGlobal.document.body.contains(element);

    Anchor anchor = new Anchor(element);

    // Mark it attached and remember it for cleanup.
    anchor.onAttach();
    RootPanel.detachOnWindowClose(anchor);

    return anchor;
  }

  private final DirectionalTextHelper directionalTextHelper;

  private HorizontalAlignmentConstant horzAlign;

  /**
   * Creates an empty anchor.
   *
   * <p>The anchor's href is <em>not</em> set, which means that the widget will not not be styled
   * with the browser's native link styles (such as underline and font color). Use {@link
   * #Anchor(boolean)} to add a default no-op href that does not open a link but ensures the native
   * link styles are applied.
   *
   * @see #Anchor(boolean)
   */
  public Anchor() {
    this(false);
  }

  /**
   * Creates an anchor.
   *
   * <p>The anchor's href is optionally set to <code>javascript:;</code>, based on the expectation
   * that listeners will be added to the anchor.
   *
   * @param useDefaultHref true to set the default href to <code>javascript:;</code>, false to leave
   *     it blank
   */
  public Anchor(boolean useDefaultHref) {
    setElement(DOM.createAnchor());
    setStyleName("gwt-Anchor");
    directionalTextHelper = new DirectionalTextHelper(getAnchorElement(), /* is inline */ true);
    if (useDefaultHref) {
      setHref(DEFAULT_HREF);
    }

    // The following click handler is used to support users of CSP (Content
    // Security Policy). When a CSP policy is in place, clicking on a link with
    // the DEFAULT_HREF as the href can trigger a CSP violation. As a
    // work-around, we prevent execution using preventDefault() when the href is
    // set to DEFAULT_HREF.
    if ("true".equals(System.getProperty("gwt.cspCompatModeEnabled"))) {
      addClickHandler(
          new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (DEFAULT_HREF.equals(getAnchorElement().href)) {
                event.preventDefault();
              }
            }
          });
    }
  }

  /**
   * Creates an anchor for scripting.
   *
   * @param html the anchor's html
   */
  public Anchor(SafeHtml html) {
    this(html.asString(), true);
  }

  /**
   * Creates an anchor for scripting.
   *
   * <p>The anchor's href is set to <code>javascript:;</code>, based on the expectation that
   * listeners will be added to the anchor.
   *
   * @param text the anchor's text
   */
  public Anchor(String text) {
    this(text, DEFAULT_HREF);
  }

  /**
   * Creates an anchor for scripting.
   *
   * <p>The anchor's href is set to <code>javascript:;</code>, based on the expectation that
   * listeners will be added to the anchor.
   *
   * @param text the anchor's text
   * @param asHtml <code>true</code> to treat the specified text as html
   */
  public Anchor(@IsSafeHtml String text, boolean asHtml) {
    this(text, asHtml, DEFAULT_HREF);
  }

  /**
   * Creates an anchor with its html and href (target URL) specified.
   *
   * @param html the anchor's html
   * @param href the url to which it will link
   */
  public Anchor(SafeHtml html, @IsSafeUri String href) {
    this(html.asString(), true, href);
  }

  /**
   * Creates an anchor with its html and href (target URL) specified.
   *
   * @param html the anchor's html
   * @param href the url to which it will link
   */
  public Anchor(SafeHtml html, SafeUri href) {
    this(html.asString(), true, href.asString());
  }

  /**
   * Creates an anchor with its text and href (target URL) specified.
   *
   * @param text the anchor's text
   * @param href the url to which it will link
   */
  @SuppressIsSafeHtmlCastCheck
  public Anchor(String text, @IsSafeUri String href) {
    this(text, false, href);
  }

  /**
   * Creates an anchor with its text and href (target URL) specified.
   *
   * @param text the anchor's text
   * @param asHTML <code>true</code> to treat the specified text as html
   * @param href the url to which it will link
   */
  public Anchor(@IsSafeHtml String text, boolean asHTML, @IsSafeUri String href) {
    this();
    directionalTextHelper.setTextOrHtml(text, asHTML);
    setHref(href);
  }

  /**
   * Creates a source anchor (link to URI).
   *
   * <p>That is, an anchor with an href attribute specifying the destination URI.
   *
   * @param html the anchor's html
   * @param href the url to which it will link
   * @param target the target frame (e.g. "_blank" to open the link in a new window)
   */
  public Anchor(SafeHtml html, @IsSafeUri String href, String target) {
    this(html.asString(), true, href, target);
  }

  /**
   * Creates a source anchor (link to URI).
   *
   * <p>That is, an anchor with an href attribute specifying the destination URI.
   *
   * @param html the anchor's html
   * @param href the url to which it will link
   * @param target the target frame (e.g. "_blank" to open the link in a new window)
   */
  public Anchor(SafeHtml html, SafeUri href, String target) {
    this(html.asString(), true, href.asString(), target);
  }

  /**
   * Creates a source anchor with a frame target.
   *
   * @param text the anchor's text
   * @param href the url to which it will link
   * @param target the target frame (e.g. "_blank" to open the link in a new window)
   */
  @SuppressIsSafeHtmlCastCheck
  public Anchor(String text, @IsSafeUri String href, String target) {
    this(text, false, href, target);
  }

  /**
   * Creates a source anchor (link to URI).
   *
   * <p>That is, an anchor with an href attribute specifying the destination URI.
   *
   * @param text the anchor's text
   * @param asHtml asHTML <code>true</code> to treat the specified text as html
   * @param href the url to which it will link
   * @param target the target frame (e.g. "_blank" to open the link in a new window)
   */
  public Anchor(@IsSafeHtml String text, boolean asHtml, @IsSafeUri String href, String target) {
    this(text, asHtml, href);
    setTarget(target);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be an &lt;a&gt; element.
   *
   * @param element the element to be used
   */
  protected Anchor(HTMLElement element) {
    assert "a".equalsIgnoreCase(element.tagName);
    setElement(element);
    directionalTextHelper = new DirectionalTextHelper(getAnchorElement(), /* is inline */ true);
  }

  @Override
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horzAlign;
  }

  /**
   * Gets the anchor's href (the url to which it links).
   *
   * @return the anchor's href
   */
  public String getHref() {
    return getAnchorElement().href;
  }

  @Override
  public String getHTML() {
    return getElement().innerHTML;
  }

  @Override
  public String getName() {
    return getAnchorElement().name;
  }

  @Override
  public int getTabIndex() {
    return getAnchorElement().tabIndex;
  }

  /**
   * Gets the anchor's target frame (the frame in which navigation will occur when the link is
   * selected).
   *
   * @return the target frame
   */
  public String getTarget() {
    return getAnchorElement().target;
  }

  @Override
  public String getText() {
    return directionalTextHelper.getText();
  }


  @Override
  public boolean getWordWrap() {
    return !"nowrap".equals(getElement().style.whiteSpace);
  }

  @Override
  public void setAccessKey(char key) {
    getAnchorElement().accessKey = Character.toString(key);
  }

  @Override
  public void setFocus(boolean focused) {
    if (focused) {
      getAnchorElement().focus();
    } else {
      getAnchorElement().blur();
    }
  }

  @Override
  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    horzAlign = align;
    getElement().style.setProperty("textAlign", align.getTextAlignString());
  }

  /**
   * Sets the anchor's href (the url to which it links).
   *
   * @param href the anchor's href
   */
  public void setHref(SafeUri href) {
    getAnchorElement().href = href.asString();
  }

  /**
   * Sets the anchor's href (the url to which it links).
   *
   * @param href the anchor's href
   */
  public void setHref(@IsSafeUri String href) {
    getAnchorElement().href = href;
  }

  @Override
  public void setHTML(SafeHtml html) {
    directionalTextHelper.setHtml(html);
  }

  @Override
  public void setHTML(@IsSafeHtml String html) {
    directionalTextHelper.setHtml(html);
  }

  @Override
  public void setName(String name) {
    getAnchorElement().name = name;
  }

  @Override
  public void setTabIndex(int index) {
    getAnchorElement().tabIndex = index;
  }

  /**
   * Sets the anchor's target frame (the frame in which navigation will occur when the link is
   * selected).
   *
   * @param target the target frame
   */
  public void setTarget(String target) {
    getAnchorElement().target = target;
  }

  @Override
  public void setText(String text) {
    directionalTextHelper.setText(text);
  }

  @Override
  public void setWordWrap(boolean wrap) {
    getElement().style.whiteSpace = wrap ? "normal" : "nowrap";
  }

  private HTMLAnchorElement getAnchorElement() {
    return Js.uncheckedCast(getElement());
  }
}
