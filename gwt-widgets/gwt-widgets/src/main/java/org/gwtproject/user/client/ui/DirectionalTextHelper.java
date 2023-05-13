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

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;
import org.gwtproject.safehtml.shared.annotations.SuppressIsSafeHtmlCastCheck;

/**
 * A helper class for displaying bidi (i.e. potentially opposite-direction) text or HTML in an
 * element. Note: this class assumes that callers perform all their text/html and direction
 * manipulations through it alone.
 */
public class DirectionalTextHelper {

  /** The target element. */
  private final HTMLElement element;

  /**
   * Whether direction was explicitly set on the last {@code setTextOrHtml} call. If so, {@link
   * #setDirectionEstimator} will refrain from modifying the direction until {@link #setTextOrHtml}
   * is called without specifying an explicit direction.
   */
  private boolean isDirectionExplicitlySet;

  /**
   * Whether the element is inline (e.g. a &lt;span&gt; element, but not a block element like
   * &lt;div&gt;). This is needed because direction is handled differently for inline elements and
   * for non-inline elements.
   */
  private final boolean isElementInline;

  /**
   * Whether the element contains a nested &lt;span&gt; element used to indicate the content's
   * direction.
   *
   * <p>The element itself is used for this purpose when it is a block element (i.e.
   * !isElementInline), but doing so on an inline element often results in garbling what follows it.
   * Thus, when the element is inline, a nested &lt;span&gt; must be used to carry the content's
   * direction, with an LRM or RLM character afterwards to prevent the garbling.
   */
  private boolean isSpanWrapped;


  /**
   * @param element The widget's element holding text.
   * @param isElementInline Whether the element is an inline element.
   */
  public DirectionalTextHelper(HTMLElement element, boolean isElementInline) {
    this.element = element;
    this.isElementInline = isElementInline;
    isSpanWrapped = false;
    // setDirectionEstimator shouldn't refresh appearance of initial empty text.
    isDirectionExplicitlySet = true;
  }


  /**
   * Get the inner text of the element, taking the inner span wrap into consideration, if needed.
   *
   * @return the text
   */
  public String getText() {
    return getTextOrHtml(false /* isHtml */);
  }

  /**
   * Get the inner html of the element, taking the inner span wrap into consideration, if needed.
   *
   * @return the html
   */
  public String getHtml() {
    return getTextOrHtml(true /* isHtml */);
  }

  /**
   * Get the inner text or html of the element, taking the inner span wrap into consideration, if
   * needed. Prefer using {@link #getText} or {@link #getHtml} instead of this method.
   *
   * @param isHtml true to get the inner html, false to get the inner text
   * @return the text or html
   */
  public String getTextOrHtml(boolean isHtml) {
    HTMLElement elem = isSpanWrapped ? Js.uncheckedCast(element.firstElementChild) : element;
    return isHtml ? elem.innerHTML : elem.textContent;
  }

  /**
   * Note: if the element already has non-empty content, this will update its direction according to
   * the new estimator's result. This may cause flicker, and thus should be avoided;
   * DirectionEstimator should be set before the element has any content.
   */
  @SuppressIsSafeHtmlCastCheck
  public void setDirectionEstimator() {
    /*
     * Refresh appearance unless direction was explicitly set on last
     * setTextOrHtml call.
     */
    if (!isDirectionExplicitlySet) {
      setHtml(getHtml()); // TODO: mXSS
    }
  }

  /**
   * Sets the element's content to the given value (plain text). If direction estimation is off, the
   * direction is verified to match the element's initial direction. Otherwise, the direction is
   * affected as described at setText(String, Direction)}.
   *
   * @param content the element's new content
   */
  @SuppressIsSafeHtmlCastCheck
  public void setText(String content) {
    setTextOrHtml(content, false /* isHtml */);
  }

  /**
   * Sets the element's content to the given value (html). If direction estimation is off, the
   * direction is verified to match the element's initial direction. Otherwise, the direction is
   * affected as described setHtml(String, Direction)}.
   *
   * @param content the element's new content
   */
  public void setHtml(SafeHtml content) {
    setHtml(content.asString());
  }

  /**
   * Sets the element's content to the given value (html). If direction estimation is off, the
   * direction is verified to match the element's initial direction. Otherwise, the direction is
   * affected as described at setHtml(String, Direction)}.
   *
   * @param content the element's new content
   */
  public void setHtml(@IsSafeHtml String content) {
    setTextOrHtml(content, true /* isHtml */);
  }

  /**
   * Sets the element's content to the given value (either plain text or HTML), applying the given
   * direction.
   *
   * @param content the element's new content
   * @param isHtml whether the content is HTML
   */
  public void setTextOrHtml(@IsSafeHtml String content, boolean isHtml) {
    setInnerTextOrHtml(content, isHtml);
  }

  private void setInnerTextOrHtml(@IsSafeHtml String content, boolean isHtml) {
    if (isHtml) {
      element.innerHTML = content;
    } else {
      element.textContent = content;
    }
  }
}
