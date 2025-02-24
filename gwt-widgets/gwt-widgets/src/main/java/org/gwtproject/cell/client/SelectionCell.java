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
package org.gwtproject.cell.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.dom.client.SelectElement;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;

/** A {@link Cell} used to render a drop-down list. */
public class SelectionCell extends org.gwtproject.cell.client.AbstractInputCell<String, String> {

  interface Template extends SafeHtmlTemplates {

    SelectionCell.Template INSTANCE = new SelectionCell_TemplateImpl();

    SafeHtml deselected(String option);

    SafeHtml selected(String option);
  }

  private HashMap<String, Integer> indexForOption = new HashMap<String, Integer>();

  private final List<String> options;

  /**
   * Construct a new {@link SelectionCell} with the specified options.
   *
   * @param options the options in the cell
   */
  public SelectionCell(List<String> options) {
    super(BrowserEvents.CHANGE);
    this.options = new ArrayList<>(options);
    int index = 0;
    for (String option : options) {
      indexForOption.put(option, index++);
    }
  }

  @Override
  public void onBrowserEvent(
      Cell.Context context,
      Element parent,
      String value,
      NativeEvent event,
      ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    String type = event.getType();
    if (BrowserEvents.CHANGE.equals(type)) {
      Object key = context.getKey();
      SelectElement select = parent.getFirstChild().cast();
      String newValue = options.get(select.getSelectedIndex());
      setViewData(key, newValue);
      finishEditing(parent, newValue, key, valueUpdater);
      if (valueUpdater != null) {
        valueUpdater.update(newValue);
      }
    }
  }

  @Override
  public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
    // Get the view data.
    Object key = context.getKey();
    String viewData = getViewData(key);
    if (viewData != null && viewData.equals(value)) {
      clearViewData(key);
      viewData = null;
    }

    int selectedIndex = getSelectedIndex(viewData == null ? value : viewData);
    sb.appendHtmlConstant("<select tabindex=\"-1\">");
    int index = 0;
    for (String option : options) {
      if (index++ == selectedIndex) {
        sb.append(Template.INSTANCE.selected(option));
      } else {
        sb.append(Template.INSTANCE.deselected(option));
      }
    }
    sb.appendHtmlConstant("</select>");
  }

  private int getSelectedIndex(String value) {
    Integer index = indexForOption.get(value);
    if (index == null) {
      return -1;
    }
    return index.intValue();
  }
}
