/*
 * Copyright 2010 Google Inc.
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

import java.util.ArrayList;
import java.util.Collection;
import org.gwtproject.cell.client.AbstractCell;
import org.gwtproject.editor.client.IsEditor;
import org.gwtproject.editor.client.LeafValueEditor;
import org.gwtproject.editor.client.adapters.TakesValueEditor;
import org.gwtproject.event.logical.shared.ValueChangeEvent;
import org.gwtproject.event.logical.shared.ValueChangeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.text.shared.Renderer;
import org.gwtproject.text.shared.ToStringRenderer;
import org.gwtproject.user.cellview.client.CellList;
import org.gwtproject.view.client.SelectionChangeEvent;
import org.gwtproject.view.client.SingleSelectionModel;

/**
 * Allows the user to pick a single value from a list.
 *
 * @param <T> the type of value
 */
public class ValuePicker<T> extends Composite
    implements HasConstrainedValue<T>, IsEditor<LeafValueEditor<T>> {

  private static class DefaultCell<T> extends AbstractCell<T> {
    private final Renderer<? super T> renderer;

    DefaultCell(Renderer<? super T> renderer) {
      this.renderer = renderer;
    }

    @Override
    public void render(Context context, T value, SafeHtmlBuilder sb) {
      sb.appendEscaped(renderer.render(value));
    }
  }

  private T value;

  private final CellList<T> cellList;
  private SingleSelectionModel<T> smodel = new SingleSelectionModel<T>();
  private LeafValueEditor<T> editor;

  public ValuePicker(CellList<T> cellList) {
    this.cellList = cellList;
    initWidget(cellList);
    cellList.setSelectionModel(smodel);
    smodel.addSelectionChangeHandler(
        new SelectionChangeEvent.Handler() {
          public void onSelectionChange(SelectionChangeEvent event) {
            setValue(smodel.getSelectedObject(), true);
          }
        });
  }

  public ValuePicker(Renderer<? super T> renderer) {
    this(new CellList<T>(new DefaultCell<T>(renderer)));
  }

  public ValuePicker() {
    this(ToStringRenderer.instance());
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /** Returns a {@link TakesValueEditor} backed by the ValuePicker. */
  public LeafValueEditor<T> asEditor() {
    if (editor == null) {
      editor = TakesValueEditor.of(this);
    }
    return editor;
  }

  /** Returns this view. */
  @Override
  public ValuePicker<T> asWidget() {
    return this;
  }

  public int getPageSize() {
    return cellList.getPageSize();
  }

  public T getValue() {
    return value;
  }

  public void setAcceptableValues(Collection<T> values) {
    cellList.setRowData(new ArrayList<T>(values));
  }

  public void setPageSize(int size) {
    cellList.setPageSize(size);
  }

  public void setValue(T value) {
    setValue(value, false);
  }

  public void setValue(T value, boolean fireEvents) {
    T current = getValue();
    if ((current == value) || (current != null && current.equals(value))) {
      return;
    }
    this.value = value;
    smodel.setSelected(value, true);
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }
}
