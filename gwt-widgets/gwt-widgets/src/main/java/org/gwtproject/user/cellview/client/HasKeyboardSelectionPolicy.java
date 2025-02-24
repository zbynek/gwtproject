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
package org.gwtproject.user.cellview.client;

import org.gwtproject.view.client.SelectionModel;

/**
 * Implemented by widgets that have a {@link HasKeyboardSelectionPolicy.KeyboardSelectionPolicy}.
 */
public interface HasKeyboardSelectionPolicy {

  /** The policy that determines how keyboard selection will work. */
  enum KeyboardSelectionPolicy {
    /** Keyboard selection is disabled. */
    DISABLED,

    /** Keyboard selection is enabled. */
    ENABLED,

    /** Keyboard selection is bound to the {@link SelectionModel}. */
    BOUND_TO_SELECTION
  }

  /**
   * Get the {@link KeyboardSelectionPolicy}.
   *
   * @return the selection policy
   * @see #setKeyboardSelectionPolicy(KeyboardSelectionPolicy)
   */
  KeyboardSelectionPolicy getKeyboardSelectionPolicy();

  /**
   * Set the {@link KeyboardSelectionPolicy}.
   *
   * @param policy the selection policy
   * @see #getKeyboardSelectionPolicy()
   */
  void setKeyboardSelectionPolicy(KeyboardSelectionPolicy policy);
}
