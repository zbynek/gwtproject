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

import org.gwtproject.builder.shared.TableSectionBuilder;
import elemental2.dom.HTMLTableSectionElement;
import org.gwtproject.safehtml.shared.SafeHtml;

/** DOM-based implementation of {@link TableSectionBuilder}. */
public class DomTableSectionBuilder
    extends DomElementBuilderBase<TableSectionBuilder, HTMLTableSectionElement>
    implements TableSectionBuilder {

  DomTableSectionBuilder(DomBuilderImpl delegate) {
    super(delegate);
  }

  @Override
  public TableSectionBuilder align(String align) {
    assertCanAddAttribute().align = align;
    return this;
  }

  @Override
  public TableSectionBuilder ch(String ch) {
    assertCanAddAttribute().ch = ch;
    return this;
  }

  @Override
  public TableSectionBuilder chOff(String chOff) {
    assertCanAddAttribute().chOff = chOff;
    return this;
  }

  @Override
  public TableSectionBuilder html(SafeHtml html) {
    throw new UnsupportedOperationException(UNSUPPORTED_HTML);
  }

  @Override
  public TableSectionBuilder text(String text) {
    throw new UnsupportedOperationException(UNSUPPORTED_HTML);
  }

  @Override
  public TableSectionBuilder vAlign(String vAlign) {
    assertCanAddAttribute().vAlign = vAlign;
    return this;
  }
}
