/*
 * Copyright © 2019 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.builder.shared;

import org.gwtproject.user.client.Unit;


/**
 * Builds the style attribute on an element.
 *
 * <p>The HTML implementation of class appends the style properties to the HTML string. The DOM
 * implementation of this class sets the element's styles directly.
 */
public interface StylesBuilder {

  /** Set the bottom css property. */
  StylesBuilder bottom(double value, Unit unit);

  /**
   * End the current style attribute.
   *
   * @throws IllegalStateException if the style attribute is already closed
   */
  void endStyle();

  /** Set the height css property. */
  StylesBuilder height(double value, Unit unit);

  /** Set the left css property. */
  StylesBuilder left(double value, Unit unit);

  /** Set the line-height css property. */
  StylesBuilder lineHeight(double value, Unit unit);

  /** Set the margin css property. */
  StylesBuilder margin(double value, Unit unit);

  /** Set the margin-bottom css property. */
  StylesBuilder marginBottom(double value, Unit unit);

  /** Set the margin-left css property. */
  StylesBuilder marginLeft(double value, Unit unit);

  /** Set the margin-right css property. */
  StylesBuilder marginRight(double value, Unit unit);

  /** Set the margin-top css property. */
  StylesBuilder marginTop(double value, Unit unit);

  /** Set the padding css property. */
  StylesBuilder padding(double value, Unit unit);

  /** Set the padding-bottom css property. */
  StylesBuilder paddingBottom(double value, Unit unit);

  /** Set the padding-left css property. */
  StylesBuilder paddingLeft(double value, Unit unit);

  /** Set the padding-right css property. */
  StylesBuilder paddingRight(double value, Unit unit);

  /** Set the padding-top css property. */
  StylesBuilder paddingTop(double value, Unit unit);

  /** Set the right css property. */
  StylesBuilder right(double value, Unit unit);

  /** Set the top css property. */
  StylesBuilder top(double value, Unit unit);

  /**
   * Set a style property from a trusted name and a trusted value, i.e., without escaping the name
   * and value. No checks are performed. The calling code should be carefully reviewed to ensure the
   * argument will satisfy the {@link org.gwtproject.safecss.shared.SafeStyles} contract when they
   * are composed into the form: "&lt;name&gt;:&lt;value&gt;;".
   *
   * <p>SafeStyles may never contain literal angle brackets. Otherwise, it could be unsafe to place
   * a SafeStyles into a &lt;style&gt; tag (where it can't be HTML escaped). For example, if the
   * SafeStyles containing " <code>font: 'foo &lt;style&gt;&lt;script&gt;evil&lt;/script&gt;</code>
   * '" is used in a style sheet in a &lt;style&gt; tag, this could then break out of the style
   * context into HTML.
   *
   * @param unit the units of the value
   * @return this {@link StylesBuilder}
   */
  StylesBuilder trustedProperty(String name, double value, Unit unit);

  /**
   * Set a style property from a trusted name and a trusted value, i.e., without escaping the name
   * and value. No checks are performed. The calling code should be carefully reviewed to ensure the
   * argument will satisfy the {@link org.gwtproject.safecss.shared.SafeStyles} contract when they
   * are composed into the form: "&lt;name&gt;:&lt;value&gt;;".
   *
   * <p>SafeStyles may never contain literal angle brackets. Otherwise, it could be unsafe to place
   * a SafeStyles into a &lt;style&gt; tag (where it can't be HTML escaped). For example, if the
   * SafeStyles containing " <code>font: 'foo &lt;style&gt;&lt;script&gt;evil&lt;/script&gt;</code>
   * '" is used in a style sheet in a &lt;style&gt; tag, this could then break out of the style
   * context into HTML.
   *
   * @return this {@link StylesBuilder}
   */
  StylesBuilder trustedProperty(String name, String value);
}
