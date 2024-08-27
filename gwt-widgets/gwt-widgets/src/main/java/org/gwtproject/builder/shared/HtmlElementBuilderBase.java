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

import org.gwtproject.safehtml.shared.SafeHtml;

/**
 * Implementation of {@link ElementBuilderBase} that delegates to an {@link HtmlBuilderImpl}.
 *
 * <p>Subclasses of {@link HtmlElementBuilderBase} act as typed wrappers around a shared {@link
 * ElementBuilderBase} implementation that handles the actual building. The wrappers merely delegate
 * to the shared implementation, so wrapper instances can be reused, avoiding object creation. This
 * approach is necessary so that the return value of common methods, such as {@link #id(String)},
 * return a typed builder instead of the generic {@link ElementBuilderBase}.
 *
 * @param <R> the builder type returned from build methods
 */
public class HtmlElementBuilderBase<R extends ElementBuilderBase<?>>
    extends AbstractElementBuilderBase<R> {

  private final HtmlBuilderImpl delegate;

  /**
   * Construct a new {@link HtmlElementBuilderBase}.
   *
   * @param delegate the delegate that builds the element
   */
  HtmlElementBuilderBase(HtmlBuilderImpl delegate) {
    this(delegate, false);
  }

  /**
   * Construct a new {@link HtmlElementBuilderBase}.
   *
   * @param delegate the delegate that builds the element
   * @param isEndTagForbidden true if the end tag is forbidden for this element
   */
  HtmlElementBuilderBase(HtmlBuilderImpl delegate, boolean isEndTagForbidden) {
    super(delegate, isEndTagForbidden);
    this.delegate = delegate;
  }

  /** Return the HTML as a {@link SafeHtml} string. */
  public SafeHtml asSafeHtml() {
    return delegate.asSafeHtml();
  }

  @Override
  public R attribute(String name, int value) {
    delegate.attribute(name, value);
    return getReturnBuilder();
  }

  @Override
  public R attribute(String name, String value) {
    delegate.attribute(name, value);
    return getReturnBuilder();
  }

  @Override
  public R className(String className) {
    return trustedAttribute("class", className);
  }

  @Override
  public R dir(String dir) {
    return trustedAttribute("dir", dir);
  }

  @Override
  public R draggable(String draggable) {
    return trustedAttribute("draggable", draggable);
  }

  @Override
  public R id(String id) {
    return trustedAttribute("id", id);
  }

  @Override
  public R lang(String lang) {
    return trustedAttribute("lang", lang);
  }


  @Override
  public DivBuilder startDiv() {
    return delegate.startDiv();
  }


  @Override
  public TableCellBuilder startTD() {
    return delegate.startTD();
  }


  @Override
  public TableCellBuilder startTH() {
    return delegate.startTH();
  }


  @Override
  public TableRowBuilder startTR() {
    return delegate.startTR();
  }

  @Override
  public R tabIndex(int tabIndex) {
    return trustedAttribute("tabIndex", tabIndex);
  }

  @Override
  public R title(String title) {
    return trustedAttribute("title", title);
  }

  @Override
  public ElementBuilder trustedStart(String tagName) {
    return delegate.trustedStart(tagName);
  }

  /** Add an attribute with a trusted name. */
  R trustedAttribute(String name, int value) {
    delegate.trustedAttribute(name, value);
    return getReturnBuilder();
  }

  /** Add an attribute with a trusted name. The name is still escaped. */
  R trustedAttribute(String name, String value) {
    delegate.trustedAttribute(name, value);
    return getReturnBuilder();
  }
}
