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

import org.gwtproject.builder.shared.TableRowBuilder;
import elemental2.dom.HTMLTableRowElement;
import org.gwtproject.safehtml.shared.SafeHtml;

/** DOM-based implementation of {@link TableRowBuilder}. */
public class DomTableRowBuilder extends DomElementBuilderBase<TableRowBuilder, HTMLTableRowElement>
    implements TableRowBuilder {

  DomTableRowBuilder(DomBuilderImpl delegate) {
    super(delegate);
  }

  @Override
  public TableRowBuilder align(String align) {
    assertCanAddAttribute().align = align;
    return this;
  }

  @Override
  public TableRowBuilder ch(String ch) {
    assertCanAddAttribute().ch = ch;
    return this;
  }

  @Override
  public TableRowBuilder chOff(String chOff) {
    assertCanAddAttribute().chOff = chOff;
    return this;
  }

  @Override
  public TableRowBuilder html(SafeHtml html) {
    throw new UnsupportedOperationException(UNSUPPORTED_HTML);
  }

  @Override
  public TableRowBuilder text(String text) {
    throw new UnsupportedOperationException(UNSUPPORTED_HTML);
  }

  @Override
  public TableRowBuilder vAlign(String vAlign) {
    assertCanAddAttribute().vAlign = vAlign;
    return this;
  }
}
