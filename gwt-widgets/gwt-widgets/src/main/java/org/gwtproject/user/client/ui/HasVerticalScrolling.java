/*
 * Copyright 2011 Google Inc.
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

/** Implemented by widgets that support vertical scrolling. */
public interface HasVerticalScrolling {

  /**
   * Get the maximum position of vertical scrolling. This is usually the <code>
   * scrollHeight - clientHeight</code>.
   *
   * @return the maximum vertical scroll position
   */
  int getMaximumVerticalScrollPosition();

  /**
   * Get the minimum position of vertical scrolling.
   *
   * @return the minimum vertical scroll position
   */
  int getMinimumVerticalScrollPosition();

  /**
   * Gets the vertical scroll position.
   *
   * @return the vertical scroll position, in pixels
   */
  int getVerticalScrollPosition();

  /**
   * Sets the vertical scroll position.
   *
   * @param position the new vertical scroll position, in pixels
   */
  void setVerticalScrollPosition(int position);
}
