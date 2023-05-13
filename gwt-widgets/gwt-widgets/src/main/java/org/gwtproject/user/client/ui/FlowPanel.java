/*
 * Copyright 2007 Google Inc.
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

import elemental2.dom.DomGlobal;
import jsinterop.base.Js;
import org.gwtproject.user.client.DOM;

/**
 * A panel that formats its child widgets using the default HTML layout behavior.
 *
 * <p><img class='gallery' src='doc-files/FlowPanel.png'/>
 */
public class FlowPanel extends ComplexPanel implements InsertPanel.ForIsWidget {
  /** Creates an empty flow panel. */
  public FlowPanel() {
    this("div");
  }

  /** Creates an empty flow panel with a custom tag. */
  public FlowPanel(String tag) {
    setElement(Js.uncheckedCast(DomGlobal.document.createElement(tag)));
  }

  /**
   * Adds a new child widget to the panel.
   *
   * @param w the widget to be added
   */
  @Override
  public void add(Widget w) {
    add(w, getElement());
  }

  @Override
  public void clear() {
    try {
      doLogicalClear();
    } finally {
      DOM.removeAllChildren(getElement());
    }
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    insert(asWidgetOrNull(w), beforeIndex);
  }

  /**
   * Inserts a widget before the specified index.
   *
   * @param w the widget to be inserted
   * @param beforeIndex the index before which it will be inserted
   * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
   */
  @Override
  public void insert(Widget w, int beforeIndex) {
    insert(w, getElement(), beforeIndex, true);
  }
}
