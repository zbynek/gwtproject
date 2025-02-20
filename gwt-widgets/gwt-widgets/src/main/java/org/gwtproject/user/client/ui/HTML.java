/*
 * Copyright 2006 Google Inc.
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

import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.i18n.shared.DirectionEstimator;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;

/**
 * A widget that can contain arbitrary HTML.
 *
 * <p>This widget uses a &lt;div&gt; element, causing it to be displayed with block layout.
 *
 * <p>If you only need a simple label (text, but not HTML), then the {@link Label} widget is more
 * appropriate, as it disallows the use of HTML, which can lead to potential security issues if not
 * used properly.
 *
 * <p>
 *
 * <h3>Built-in Bidi Text Support</h3>
 *
 * This widget is capable of automatically adjusting its direction according to its content. This
 * feature is controlled by {@link #setDirectionEstimator} or passing a DirectionEstimator parameter
 * to the constructor, and is off by default.
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-HTML { }
 * </ul>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.HTMLExample}
 */
public class HTML extends Label implements HasDirectionalHtml, HasDirectionalSafeHtml {

  /**
   * Creates an HTML widget that wraps an existing &lt;div&gt; or &lt;span&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static HTML wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    HTML html = new HTML(element);

    // Mark it attached and remember it for cleanup.
    html.onAttach();
    RootPanel.detachOnWindowClose(html);

    return html;
  }

  /** Creates an empty HTML widget. */
  public HTML() {
    super(Document.get().createDivElement());
    setStyleName("gwt-HTML");
  }

  /**
   * Initializes the widget's HTML from a given {@link SafeHtml} object.
   *
   * @param html the new widget's HTML contents
   */
  public HTML(SafeHtml html) {
    this(html.asString());
  }

  /**
   * Creates an HTML widget with the specified contents and with the specified direction.
   *
   * @param html the new widget's SafeHtml contents
   * @param dir the content's direction. Note: {@code Direction.DEFAULT} means direction should be
   *     inherited from the widget's parent element.
   */
  public HTML(SafeHtml html, Direction dir) {
    this(html.asString(), dir);
  }

  /**
   * Creates an HTML widget with the specified HTML contents and specifies a direction estimator.
   *
   * @param html the new widget's SafeHtml contents
   * @param directionEstimator A DirectionEstimator object used for automatic direction adjustment.
   *     For convenience, {@link Label#DEFAULT_DIRECTION_ESTIMATOR} can be used.
   */
  public HTML(SafeHtml html, DirectionEstimator directionEstimator) {
    this();
    setDirectionEstimator(directionEstimator);
    setHTML(html);
  }

  /**
   * Creates an HTML widget with the specified HTML contents.
   *
   * @param html the new widget's HTML contents
   */
  public HTML(@IsSafeHtml String html) {
    this();
    setHTML(html);
  }

  /**
   * Creates an HTML widget with the specified HTML contents and with the specified direction.
   *
   * @param html the new widget's HTML contents
   * @param dir the content's direction. Note: {@code Direction.DEFAULT} means direction should be
   *     inherited from the widget's parent element.
   */
  public HTML(@IsSafeHtml String html, Direction dir) {
    this();
    setHTML(html, dir);
  }

  /**
   * Creates an HTML widget with the specified contents, optionally treating it as HTML, and
   * optionally disabling word wrapping.
   *
   * @param html the widget's contents
   * @param wordWrap <code>false</code> to disable word wrapping
   */
  public HTML(@IsSafeHtml String html, boolean wordWrap) {
    this(html);
    setWordWrap(wordWrap);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be either a &lt;div&gt; or &lt;span&gt; element.
   *
   * @param element the element to be used
   */
  protected HTML(Element element) {
    // super(element) asserts that element is either a &lt;div&gt; or
    // &lt;span&gt;.
    super(element);
  }

  public String getHTML() {
    return directionalTextHelper.getHtml();
  }

  /**
   * Sets the label's content to the given HTML. See {@link #setText(String)} for details on
   * potential effects on direction and alignment.
   *
   * @param html the new widget's HTML content
   */
  public void setHTML(@IsSafeHtml String html) {
    directionalTextHelper.setHtml(html);
    updateHorizontalAlignment();
  }

  /**
   * Sets the label's content to the given HTML, applying the given direction. See {@link
   * #setText(String, Direction) setText(String, Direction)} for details on potential effects on
   * alignment.
   *
   * @param html the new widget's HTML content
   * @param dir the content's direction. Note: {@code Direction.DEFAULT} means direction should be
   *     inherited from the widget's parent element.
   */
  public void setHTML(@IsSafeHtml String html, Direction dir) {
    directionalTextHelper.setHtml(html, dir);
    updateHorizontalAlignment();
  }

  /**
   * Sets this object's contents via known-safe HTML.
   *
   * @see org.gwtproject.safehtml.client.HasSafeHtml#setHTML(SafeHtml)
   * @param html the html to set.
   */
  public void setHTML(SafeHtml html) {
    setHTML(html.asString());
  }

  public void setHTML(SafeHtml html, Direction dir) {
    setHTML(html.asString(), dir);
  }

  protected String getTextOrHtml(boolean isHtml) {
    return directionalTextHelper.getTextOrHtml(isHtml);
  }
}
