/*
 * Copyright 2008 Google Inc.
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

import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.IFrameElement;
import org.gwtproject.regexp.shared.RegExp;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.uibinder.client.UiConstructor;
import org.gwtproject.user.client.DOM;

/**
 * A {@link Frame} that has a 'name' associated with it. This allows the frame to be the target of a
 * {@link FormPanel}
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-Frame { }
 * </ul>
 */
public class NamedFrame extends Frame {
  interface IFrameTemplate extends SafeHtmlTemplates {
    IFrameTemplate INSTANCE = new NamedFrame_IFrameTemplateImpl();

    SafeHtml get(String name);
  }

  // Used inside JSNI, so please don't delete this field just because
  // your compiler or IDE says it's unused.
  private static RegExp PATTERN_NAME;

  static {
    initStatics();
  }

  /**
   * Creates an HTML IFRAME element with a name.
   *
   * @param name the name of the frame, which must contain at least one non-whitespace character and
   *     must not contain reserved HTML markup characters such as '<code>&lt;</code>', '<code>&gt;
   *     </code>', or '<code>&amp;</code>'
   * @return the newly-created element
   * @throws IllegalArgumentException if the supplied name is not allowed
   */
  private static IFrameElement createIFrame(String name) {
    if (name == null || !isValidName(name.trim())) {
      throw new IllegalArgumentException(
          "expecting one or more non-whitespace chars with no '<', '>', or '&'");
    }

    // Use innerHTML to implicitly create the <iframe>. This is necessary
    // because most browsers will not respect a dynamically-set iframe name.
    Element div = DOM.createDiv();
    div.setInnerSafeHtml(IFrameTemplate.INSTANCE.get(name));
    return div.getFirstChild().cast();
  }

  private static void initStatics() {
    PATTERN_NAME = RegExp.compile("^[^<>&\\'\\\"]+$");
  }

  /**
   * @param name the specified frame name to be checked
   * @return <code>true</code> if the name is valid, <code>false</code> if not
   */
  private static boolean isValidName(String name) {
    return PATTERN_NAME.test(name);
  }

  /**
   * Constructs a frame with the given name.
   *
   * @param name the name of the frame, which must contain at least one non-whitespace character and
   *     must not contain reserved HTML markup characters such as '<code>&lt;</code>', '<code>&gt;
   *     </code>', or '<code>&amp;</code>'
   * @throws IllegalArgumentException if the supplied name is not allowed
   */
  @UiConstructor
  public NamedFrame(String name) {
    super(createIFrame(name));
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Gets the name associated with this frame.
   *
   * @return the frame's name
   */
  public String getName() {
    return getElement().getPropertyString("name");
  }
}
