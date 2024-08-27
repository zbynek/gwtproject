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

import org.gwtproject.builder.shared.TableColBuilder;
import elemental2.dom.HTMLTableColElement;

/** DOM-based implementation of {@link TableColBuilder}. */
public class DomTableColBuilder extends DomElementBuilderBase<TableColBuilder, HTMLTableColElement>
    implements TableColBuilder {

  DomTableColBuilder(DomBuilderImpl delegate, boolean group) {
    super(delegate, !group);
  }

  @Override
  public TableColBuilder align(String align) {
    assertCanAddAttribute().align = align;
    return this;
  }

  @Override
  public TableColBuilder ch(String ch) {
    assertCanAddAttribute().ch = ch;
    return this;
  }

  @Override
  public TableColBuilder chOff(String chOff) {
    assertCanAddAttribute().chOff = chOff;
    return this;
  }

  @Override
  public TableColBuilder span(int span) {
    assertCanAddAttribute().span = span;
    return this;
  }

  @Override
  public TableColBuilder vAlign(String vAlign) {
    assertCanAddAttribute().vAlign = vAlign;
    return this;
  }

  @Override
  public TableColBuilder width(String width) {
    assertCanAddAttribute().width = width;
    return this;
  }
}
