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
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.gwtproject.user.client.DOM;

import static org.gwtproject.event.shared.BrowserEvents
.CLICK;
import static org.gwtproject.event.shared.BrowserEvents
.KEYDOWN;

/**
 * A cell that renders a button and takes a delegate to perform actions on mouseUp.
 *
 * @param <C> the type that this Cell represents
 */
public class ActionCell<C> extends AbstractCell<C> {

  /**
   * The delegate that will handle events from the cell.
   *
   * @param <T> the type that this delegate acts on
   */
  public static interface Delegate<T> {
    /**
     * Perform the desired action on the given object.
     *
     * @param object the object to be acted upon
     */
    void execute(T object);
  }

  private final SafeHtml html;
  private final Delegate<C> delegate;

  /**
   * Construct a new {@link ActionCell}.
   *
   * @param message the message to display on the button
   * @param delegate the delegate that will handle events
   */
  public ActionCell(SafeHtml message, Delegate<C> delegate) {
    super(CLICK, KEYDOWN);
    this.delegate = delegate;
    this.html =
        new SafeHtmlBuilder()
            .appendHtmlConstant("<button type=\"button\" tabindex=\"-1\">")
            .append(message)
            .appendHtmlConstant("</button>")
            .toSafeHtml();
  }

  /**
   * Construct a new {@link ActionCell} with a text String that does not contain HTML markup.
   *
   * @param text the text to display on the button
   * @param delegate the delegate that will handle events
   */
  public ActionCell(String text, Delegate<C> delegate) {
    this(SafeHtmlUtils.fromString(text), delegate);
  }

  @Override
  public void onBrowserEvent(
      Cell.Context context,
      HTMLElement parent,
      C value,
      elemental2.dom.Event event,
      org.gwtproject.cell.client.ValueUpdater<C> valueUpdater) {
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
  public void render(Cell.Context context, C value, SafeHtmlBuilder sb) {
    sb.append(html);
  }

  @Override
  protected void onEnterKeyDown(
      Cell.Context context,
      HTMLElement parent,
      C value,
      elemental2.dom.Event event,
      org.gwtproject.cell.client.ValueUpdater<C> valueUpdater) {
    delegate.execute(value);
  }
}
