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

import elemental2.dom.HTMLElement;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;

/**
 * An interface for UI elements that can be built by first generating a piece of HTML and afterwards
 * wrapping a root widget.
 *
 * <p>This interface is very experimental and in active development, so the exact API is likely to
 * change. Very likely. In fact, it will definitely change. You've been warned.
 */
public interface IsRenderable {

  /**
   * Replace the previous contents of the receiver with the given element, presumed to have been
   * created and stamped via a previous call to {@link #render}.
   */
  void claimElement(HTMLElement element);

  /**
   * Perform any initialization needed when the widget is not attached to the document. Assumed to
   * be called after {@link #claimElement}.
   */
  void initializeClaimedElement();

}
