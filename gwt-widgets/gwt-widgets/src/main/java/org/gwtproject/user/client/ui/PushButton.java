/*
 * Copyright 2007 Google Inc.
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

import org.gwtproject.event.dom.client.ClickHandler;

/**
 * A normal push button with custom styling.
 *
 * <p><img class='gallery' src='doc-files/PushButton.png'/>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class="css">
 *   <li>.gwt-PushButton-up/down/up-hovering/down-hovering/up-disabled/down-disabled {.html-face}
 * </ul>
 *
 * <p>
 *
 * <h3>Example</h3>
 *
 * {@example com.google.gwt.examples.PushButtonExample}
 */
public class PushButton extends CustomButton {

  private static final String STYLENAME_DEFAULT = "gwt-PushButton";

  {
    setStyleName(STYLENAME_DEFAULT);
  }

  /** Constructor for <code>PushButton</code>. */
  public PushButton() {
    super();
  }

  /**
   * Constructor for <code>PushButton</code>.
   *
   * @param upImage image for the default(up) face of the button
   */
  public PushButton(Image upImage) {
    super(upImage);
  }

  /**
   * Constructor for <code>PushButton</code>. The supplied image is used to construct the default
   * face of the button.
   *
   * @param upImage image for the default (up) face of the button
   * @param handler teh click handler
   */
  public PushButton(Image upImage, ClickHandler handler) {
    super(upImage, handler);
  }

  /**
   * Constructor for <code>PushButton</code>.
   *
   * @param upImage image for the default(up) face of the button
   * @param downImage image for the down face of the button
   */
  public PushButton(Image upImage, Image downImage) {
    super(upImage, downImage);
  }

  /**
   * Constructor for <code>PushButton</code>.
   *
   * @param upImage image for the default(up) face of the button
   * @param downImage image for the down face of the button
   * @param handler the click handler
   */
  public PushButton(Image upImage, Image downImage, ClickHandler handler) {
    super(upImage, downImage, handler);
  }

  /**
   * Constructor for <code>PushButton</code>. The supplied text is used to construct the default
   * face of the button.
   *
   * @param upText the text for the default (up) face of the button.
   */
  public PushButton(String upText) {
    super(upText);
  }

  /**
   * Constructor for <code>PushButton</code>. The supplied text is used to construct the default
   * face of the button.
   *
   * @param upText the text for the default (up) face of the button
   * @param handler the click handler
   */
  public PushButton(String upText, ClickHandler handler) {
    super(upText, handler);
  }

  /**
   * Constructor for <code>PushButton</code>.
   *
   * @param upText the text for the default (up) face of the button
   * @param downText the text for down face of the button
   */
  public PushButton(String upText, String downText) {
    super(upText, downText);
  }

  /**
   * Constructor for <code>PushButton</code>.
   *
   * @param upText the text for the default (up) face of the button
   * @param downText the text for down face of the button
   * @param handler the click handler
   */
  public PushButton(String upText, String downText, ClickHandler handler) {
    super(upText, downText, handler);
  }

  @Override
  protected void onClick() {
    setDown(false);
    super.onClick();
  }

  @Override
  protected void onClickCancel() {
    setDown(false);
  }

  @Override
  protected void onClickStart() {
    setDown(true);
  }
}
