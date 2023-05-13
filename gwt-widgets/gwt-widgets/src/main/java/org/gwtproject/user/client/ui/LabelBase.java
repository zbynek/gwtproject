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
import org.gwtproject.user.client.DOM;

/**
 * Abstract base class for all text display widgets.
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * @param <T> the value type
 */
public class LabelBase<T> extends Widget
    implements HasWordWrap, HasAutoHorizontalAlignment {

  /** The widget's DirectionalTextHelper object. */
  final DirectionalTextHelper directionalTextHelper;

  /**
   * The widget's auto horizontal alignment policy.
   *
   * @see HasAutoHorizontalAlignment
   */
  private AutoHorizontalAlignmentConstant autoHorizontalAlignment;

  /** The widget's horizontal alignment. */
  private HorizontalAlignmentConstant horzAlign;

  protected LabelBase(boolean inline) {
    this(inline ? DOM.createSpan() : DOM.createDiv(), inline);
  }

  protected LabelBase(HTMLElement element) {
    this(element, "span".equalsIgnoreCase(element.tagName));
  }

  private LabelBase(HTMLElement element, boolean isElementInline) {
    assert (isElementInline ? "span" : "div").equalsIgnoreCase(element.tagName);
    setElement(element);
    directionalTextHelper = new DirectionalTextHelper(getElement(), isElementInline);
  }

  public AutoHorizontalAlignmentConstant getAutoHorizontalAlignment() {
    return autoHorizontalAlignment;
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horzAlign;
  }

  public boolean getWordWrap() {
    return !"nowrap".equals(getElement().style.whiteSpace);
  }

  public void setAutoHorizontalAlignment(AutoHorizontalAlignmentConstant autoAlignment) {
    autoHorizontalAlignment = autoAlignment;
    updateHorizontalAlignment();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Note: A subsequent call to {@link #setAutoHorizontalAlignment} may override the horizontal
   * alignment set by this method.
   *
   * <p>Note: For {@code null}, the horizontal alignment is cleared, allowing it to be determined by
   * the standard HTML mechanisms such as inheritance and CSS rules.
   *
   * @see #setAutoHorizontalAlignment
   */
  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    setAutoHorizontalAlignment(align);
  }

  public void setWordWrap(boolean wrap) {
    getElement().style.whiteSpace = wrap ? "normal" : "nowrap";
  }

  /**
   * Sets the horizontal alignment of the widget according to the current AutoHorizontalAlignment
   * setting. Should be invoked whenever the horizontal alignment may be affected, i.e. on every
   * modification of the content or its direction.
   */
  protected void updateHorizontalAlignment() {
    HorizontalAlignmentConstant align;
    if (autoHorizontalAlignment == null) {
      align = null;
    } else if (autoHorizontalAlignment instanceof HorizontalAlignmentConstant) {
      align = (HorizontalAlignmentConstant) autoHorizontalAlignment;
    } else {
      /*
       * autoHorizontalAlignment is a truly automatic policy, i.e. either
       * ALIGN_CONTENT_START or ALIGN_CONTENT_END
       */
      align =
          autoHorizontalAlignment == ALIGN_CONTENT_START
              ? HorizontalAlignmentConstant.startOf()
              : HorizontalAlignmentConstant.endOf();
    }

    if (align != horzAlign) {
      horzAlign = align;
      getElement().style
          .setProperty("textAlign", horzAlign == null ? "" : horzAlign.getTextAlignString());
    }
  }
}
