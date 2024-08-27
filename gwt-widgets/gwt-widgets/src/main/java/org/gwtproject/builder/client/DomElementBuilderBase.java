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

import jsinterop.base.Js;
import org.gwtproject.builder.shared.*;
import elemental2.dom.HTMLElement;

/**
 * Implementation of {@link org.gwtproject.dom.builder.shared.ElementBuilderBase} that delegates to
 * a {@link DomBuilderImpl}.
 *
 * <p>Subclasses of {@link DomElementBuilderBase} operate directly on the {@link HTMLElement} being
 * built.
 *
 * @param <R> the builder type returned from build methods
 * @param <E> the {@link HTMLElement} type
 */
public class DomElementBuilderBase<R extends ElementBuilderBase<?>, E extends HTMLElement>
    extends AbstractElementBuilderBase<R> {

  private final DomBuilderImpl delegate;

  /**
   * Construct a new {@link DomElementBuilderBase}.
   *
   * @param delegate the delegate that builds the element
   */
  DomElementBuilderBase(DomBuilderImpl delegate) {
    this(delegate, false);
  }

  /**
   * Construct a new {@link DomElementBuilderBase}.
   *
   * @param delegate the delegate that builds the element
   * @param isEndTagForbidden true if the end tag is forbidden for this element
   */
  DomElementBuilderBase(DomBuilderImpl delegate, boolean isEndTagForbidden) {
    super(delegate, isEndTagForbidden);
    this.delegate = delegate;
  }

  @Override
  public R attribute(String name, int value) {
    assertCanAddAttribute().setAttribute(name, String.valueOf(value));
    return getReturnBuilder();
  }

  @Override
  public R attribute(String name, String value) {
    assertCanAddAttribute().setAttribute(name, value);
    return getReturnBuilder();
  }

  @Override
  public R className(String className) {
    assertCanAddAttribute().className = className;
    return getReturnBuilder();
  }

  @Override
  public R dir(String dir) {
    assertCanAddAttribute().dir = dir;
    return getReturnBuilder();
  }

  @Override
  public R draggable(String draggable) {
    assertCanAddAttribute().setAttribute("draggable", draggable);
    return getReturnBuilder();
  }

  @Override
  public R id(String id) {
    assertCanAddAttribute().id = id;
    return getReturnBuilder();
  }

  @Override
  public R lang(String lang) {
    assertCanAddAttribute().lang = lang;
    return getReturnBuilder();
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
    assertCanAddAttribute().tabIndex = tabIndex;
    return getReturnBuilder();
  }

  @Override
  public R title(String title) {
    assertCanAddAttribute().title = title;
    return getReturnBuilder();
  }

  @Override
  public ElementBuilder trustedStart(String tagName) {
    return delegate.trustedStart(tagName);
  }

  /**
   * Assert that the builder is in a state where an attribute can be added.
   *
   * @return the element on which the attribute can be set
   */
  protected E assertCanAddAttribute() {
    /*
     * An explicit parameterized return type on cast() is required by some javac
     * compilers.
     */
    return Js.<E>uncheckedCast(delegate.assertCanAddAttribute());
  }

  DomBuilderImpl getDelegate() {
    return delegate;
  }
}
