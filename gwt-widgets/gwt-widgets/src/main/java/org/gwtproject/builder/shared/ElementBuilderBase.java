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
import org.gwtproject.safehtml.shared.SafeHtml;

/**
 * Base class for element builders used to builder DOM elements.
 *
 * @param <T> the builder type returns from build methods
 */
public interface ElementBuilderBase<T extends ElementBuilderBase<?>> {

  /**
   * Add an integer attribute to the object.
   *
   * @return this builder
   */
  T attribute(String name, int value);

  /**
   * Add a string attribute to the object.
   *
   * @return this builder
   */
  T attribute(String name, String value);

  /**
   * The class attribute of the element. This attribute has been renamed due to conflicts with the
   * "class" keyword exposed by many languages.
   *
   * @return this builder
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-class">W3C
   *     HTML Specification</a>
   */
  T className(String className);

  /**
   * Specifies the base direction of directionally neutral text and the directionality of tables.
   *
   * @return this builder
   */
  T dir(String dir);

  /**
   * Changes the draggable attribute.
   *
   * @param draggable a String constant
   * @return this builder
   */
  T draggable(String draggable);

  /** End the current element without checking its type. */
  void end();

  /**
   * End the current element after checking that its tag is the specified tagName.
   *
   * @param tagName the expected tagName of the current element
   * @see #end()
   */
  void end(String tagName);

  /**
   * End the current element.
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endDiv();


  /**
   * End the current element.
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endTBody();

  /**
   * End the current element.
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endTD();

  /**
   * End the current element. . *
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endTFoot();

  /**
   * End the current element.
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endTH();

  /**
   * End the current element.
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endTHead();

  /**
   * End the current element.
   *
   * @throws IllegalStateException if the current element has the wrong tag
   * @see #end()
   */
  void endTR();

  /**
   * Return the built DOM as an {@link HTMLElement}.
   *
   * <p>Any lingering open elements are automatically closed. Once you call {@link #finish()}, you
   * can not longer call any other methods in this class.
   *
   * @return the {@link HTMLElement} that was built
   * @throws IllegalStateException if called twice
   */
  HTMLElement finish();

  /** Get the element depth of the current builder. */
  int getDepth();

  /**
   * Append html within the node.
   *
   * <p>Once you append HTML to the element, you can no longer set attributes.
   *
   * @param html the HTML to append
   * @return this builder
   */
  T html(SafeHtml html);

  /**
   * Set the id.
   *
   * @param id the id
   * @return this builder
   */
  T id(String id);

  /**
   * Check if child elements are supported.
   *
   * @return true if supported, false if not.
   */
  boolean isChildElementSupported();

  /**
   * Check if an end tag is forbidden for this element. If the end tag is forbidden, then setting
   * inner html or text or appending an element will trigger an {@link
   * UnsupportedOperationException}.
   *
   * @return true if forbidden, false if not
   */
  boolean isEndTagForbidden();

  /**
   * Language code defined in RFC 1766.
   *
   * @return this builder
   */
  T lang(String lang);

  /**
   * Append a div element.
   *
   * @return the builder for the new element
   */
  DivBuilder startDiv();

  /**
   * Append a td element.
   *
   * @return the builder for the new element
   */
  TableCellBuilder startTD();

  /**
   * Append a th element.
   *
   * @return the builder for the new element
   */
  TableCellBuilder startTH();

  /**
   * Append a tablerow element.
   *
   * @return the builder for the new element
   */
  TableRowBuilder startTR();


  /**
   * Start the {@link StylesBuilder} used to add style properties to the style attribute of the
   * current element.
   *
   * @return the {@link StylesBuilder}
   */
  StylesBuilder style();

  /**
   * Set the tab index.
   *
   * @param tabIndex the tab index
   * @return this builder
   */
  T tabIndex(int tabIndex);

  /**
   * Append text within the node.
   *
   * <p>Once you append text to the element, you can no longer set attributes.
   *
   * <p>A string-based implementation will escape the text to prevent HTML/javascript code from
   * executing. DOM based implementations are not required to escape the text if they directly set
   * the innerText of an element.
   *
   * @param text the text to append
   * @return this builder
   */
  T text(String text);

  /**
   * The element's advisory title.
   *
   * @return this builder
   */
  T title(String title);

  /**
   * Append a new element with the specified trusted tag name. The tag name will will not be checked
   * or escaped. The calling code should be carefully reviewed to ensure that the provided tag name
   * will not cause a security issue if including in an HTML document. In general, this means
   * limiting the code to HTML tagName constants supported by the HTML specification.
   *
   * @param tagName the tag name
   * @return the {@link ElementBuilder} for the new element
   */
  ElementBuilder trustedStart(String tagName);
}
