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

import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;

/**
 * A widget that can contain arbitrary HTML.
 *
 * <p>This widget uses a &lt;span&gt; element, causing it to be displayed with inline layout.
 *
 * <p>If you only need a simple label (text, but not HTML), then the {@link Label} widget is more
 * appropriate, as it disallows the use of HTML, which can lead to potential security issues if not
 * used properly.
 *
 * <p>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-InlineHTML { }
 * </ul>
 */
public class InlineHTML extends HTML {

  /**
   * Creates an InlineHTML widget that wraps an existing &lt;div&gt; or &lt;span&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param element the element to be wrapped
   */
  public static InlineHTML wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    InlineHTML html = new InlineHTML(element);

    // Mark it attached and remember it for cleanup.
    html.onAttach();
    RootPanel.detachOnWindowClose(html);

    return html;
  }

  /** Creates an empty HTML widget. */
  public InlineHTML() {
    super(Document.get().createSpanElement());
    setStyleName("gwt-InlineHTML");
  }

  /**
   * Initializes the widget's HTML from a given {@link SafeHtml} object.
   *
   * @param html the new widget's HTML contents
   */
  public InlineHTML(SafeHtml html) {
    this(html.asString());
  }

  /**
   * Creates an HTML widget with the specified HTML contents.
   *
   * @param html the new widget's HTML contents
   */
  public InlineHTML(@IsSafeHtml String html) {
    this();
    setHTML(html);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing element. This element
   * must be either a &lt;div&gt; &lt;span&gt; element.
   *
   * @param element the element to be used
   */
  protected InlineHTML(Element element) {
    // super(element) also asserts that element is either a &lt;div&gt; or
    // &lt;span&gt;.
    super(element);
  }
}
