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
package org.gwtproject.view.client;

import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.event.shared.HasHandlers;

/**
 * A widget that implements this interface is a public source of {@link CellPreviewEvent} events.
 *
 * @param <T> the data type of the values in the widget
 */
public interface HasCellPreviewHandlers<T> extends HasHandlers {
  /**
   * Adds a {@link CellPreviewEvent} handler.
   *
   * @param handler the handler
   * @return the registration for the event
   */
  HandlerRegistration addCellPreviewHandler(CellPreviewEvent.Handler<T> handler);
}
