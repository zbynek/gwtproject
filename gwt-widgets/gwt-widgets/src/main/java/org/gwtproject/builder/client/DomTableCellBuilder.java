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

import org.gwtproject.builder.shared.TableCellBuilder;
import elemental2.dom.HTMLTableCellElement;

/** DOM-based implementation of {@link TableCellBuilder}. */
public class DomTableCellBuilder extends DomElementBuilderBase<TableCellBuilder, HTMLTableCellElement>
    implements TableCellBuilder {

  DomTableCellBuilder(DomBuilderImpl delegate) {
    super(delegate);
  }

  @Override
  public TableCellBuilder align(String align) {
    assertCanAddAttribute().align = align;
    return this;
  }

  @Override
  public TableCellBuilder ch(String ch) {
    assertCanAddAttribute().ch = ch;
    return this;
  }

  @Override
  public TableCellBuilder chOff(String chOff) {
    assertCanAddAttribute().chOff = chOff;
    return this;
  }

  @Override
  public TableCellBuilder colSpan(int colSpan) {
    assertCanAddAttribute().colSpan = colSpan;
    return this;
  }

  @Override
  public TableCellBuilder headers(String headers) {
    assertCanAddAttribute().headers = headers;
    return this;
  }

  @Override
  public TableCellBuilder rowSpan(int rowSpan) {
    assertCanAddAttribute().rowSpan = rowSpan;
    return this;
  }

  @Override
  public TableCellBuilder vAlign(String vAlign) {
    assertCanAddAttribute().vAlign = vAlign;
    return this;
  }
}
