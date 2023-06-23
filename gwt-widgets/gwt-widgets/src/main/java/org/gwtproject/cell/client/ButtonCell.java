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

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import elemental2.dom.EventTarget;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.text.shared.SafeHtmlRenderer;
import org.gwtproject.text.shared.SimpleSafeHtmlRenderer;
import org.gwtproject.user.client.DOM;

import static org.gwtproject.event.dom.client.BrowserEvents.CLICK;
import static org.gwtproject.event.dom.client.BrowserEvents.KEYDOWN;

/** A {@link Cell} used to render a button. */
public class ButtonCell extends AbstractSafeHtmlCell<String> {

  /** Construct a new ButtonCell that will use a {@link SimpleSafeHtmlRenderer}. */
  public ButtonCell() {
    this(SimpleSafeHtmlRenderer.getInstance());
  }

  /**
   * Construct a new ButtonCell that will use a given {@link SafeHtmlRenderer}.
   *
   * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
   */
  public ButtonCell(SafeHtmlRenderer<String> renderer) {
    super(renderer, CLICK, KEYDOWN);
  }

  @Override
  public void onBrowserEvent(
      Cell.Context context,
      HTMLElement parent,
      String value,
      elemental2.dom.Event event,
      ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    if (CLICK.equals(event.type)) {
      EventTarget eventTarget = event.target;
      if (!DOM.isElement(eventTarget)) {
        return;
      }
      if (parent.firstElementChild.contains(Js.uncheckedCast(eventTarget))) {
        // Ignore clicks that occur outside of the main element.
        onEnterKeyDown(context, parent, value, event, valueUpdater);
      }
    }
  }

  @Override
  public void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
    sb.appendHtmlConstant("<button type=\"button\" tabindex=\"-1\">");
    if (data != null) {
      sb.append(data);
    }
    sb.appendHtmlConstant("</button>");
  }

  @Override
  protected void onEnterKeyDown(
      Cell.Context context,
      HTMLElement parent,
      String value,
      elemental2.dom.Event event,
      ValueUpdater<String> valueUpdater) {
    if (valueUpdater != null) {
      valueUpdater.update(value);
    }
  }
}
