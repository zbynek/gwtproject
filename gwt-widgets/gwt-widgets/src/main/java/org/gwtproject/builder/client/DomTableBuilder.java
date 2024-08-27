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

import org.gwtproject.builder.shared.TableBuilder;
import elemental2.dom.HTMLTableElement;
import org.gwtproject.safehtml.shared.SafeHtml;

/** DOM-based implementation of {@link TableBuilder}. */
public class DomTableBuilder extends DomElementBuilderBase<TableBuilder, HTMLTableElement>
    implements TableBuilder {

  DomTableBuilder(DomBuilderImpl delegate) {
    super(delegate, false);
  }

  @Override
  public TableBuilder border(int border) {
    assertCanAddAttribute().border = border + "";
    return this;
  }

  @Override
  public TableBuilder cellPadding(int cellPadding) {
    assertCanAddAttribute().cellPadding = cellPadding + "";
    return this;
  }

  @Override
  public TableBuilder cellSpacing(int cellSpacing) {
    assertCanAddAttribute().cellSpacing = cellSpacing + "";
    return this;
  }

  @Override
  public TableBuilder frame(String frame) {
    assertCanAddAttribute().frame = frame;
    return this;
  }

  @Override
  public TableBuilder html(SafeHtml html) {
    throw new UnsupportedOperationException(UNSUPPORTED_HTML);
  }

  @Override
  public TableBuilder rules(String rules) {
    assertCanAddAttribute().rules = rules;
    return this;
  }

  @Override
  public TableBuilder text(String text) {
    throw new UnsupportedOperationException(UNSUPPORTED_HTML);
  }

  @Override
  public TableBuilder width(String width) {
    assertCanAddAttribute().width = width;
    return this;
  }
}
