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
 * Abstract base class for implementations of {@link ElementBuilderBase}.
 *
 * <p>Subclasses of {@link AbstractElementBuilderBase} act as typed wrappers around a shared
 * implementation that handles the actual building. The wrappers merely delegate to the shared
 * implementation, so wrapper instances can be reused, avoiding object creation. This approach is
 * necessary so that the return value of common methods, such as {@link #id(String)}, return a typed
 * builder instead of the generic {@link ElementBuilderBase}.
 *
 * @param <R> the builder type returned from build methods
 */
public abstract class AbstractElementBuilderBase<R extends ElementBuilderBase<?>>
    implements ElementBuilderBase<R> {

  private final ElementBuilderImpl delegate;
  private final boolean isEndTagForbidden;
  private final R returnBuilder;

  @SuppressWarnings("unchecked")
  protected AbstractElementBuilderBase(ElementBuilderImpl delegate, boolean isEndTagForbidden) {
    this.delegate = delegate;
    this.isEndTagForbidden = isEndTagForbidden;

    // Cache the return builder to avoid repeated cast checks.
    this.returnBuilder = (R) this;
  }

  @Override
  public void end() {
    delegate.end();
  }

  @Override
  public void end(String tagName) {
    delegate.end(tagName);
  }

  @Override
  public void endDiv() {
    end("div");
  }

  @Override
  public void endTBody() {
    end("tbody");
  }

  @Override
  public void endTD() {
    end("td");
  }

  @Override
  public void endTFoot() {
    end("tfoot");
  }

  @Override
  public void endTH() {
    end("th");
  }

  @Override
  public void endTHead() {
    end("thead");
  }

  @Override
  public void endTR() {
    end("tr");
  }

  @Override
  public HTMLElement finish() {
    return delegate.finish();
  }

  @Override
  public int getDepth() {
    return delegate.getDepth();
  }

  @Override
  public R html(SafeHtml html) {
    delegate.html(html);
    return getReturnBuilder();
  }

  @Override
  public boolean isChildElementSupported() {
    return !isEndTagForbidden;
  }

  @Override
  public boolean isEndTagForbidden() {
    return isEndTagForbidden;
  }

  @Override
  public StylesBuilder style() {
    return delegate.style();
  }

  @Override
  public R text(String text) {
    delegate.text(text);
    return getReturnBuilder();
  }

  /**
   * Get the builder to return from build methods.
   *
   * @return the return builder
   */
  protected R getReturnBuilder() {
    return returnBuilder;
  }
}
