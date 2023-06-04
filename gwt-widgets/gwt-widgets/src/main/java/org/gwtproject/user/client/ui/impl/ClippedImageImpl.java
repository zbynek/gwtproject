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
package org.gwtproject.user.client.ui.impl;

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import jsinterop.annotations.JsFunction;
import jsinterop.base.Js;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.safecss.shared.SafeStyles;
import org.gwtproject.safecss.shared.SafeStylesBuilder;
import org.gwtproject.safecss.shared.SafeStylesUtils;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeUri;
import org.gwtproject.safehtml.shared.UriUtils;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.ui.Image;

/**
 * Uses a combination of a clear image and a background image to clip all except a desired portion
 * of an underlying image.
 *
 * <p>Do not use this class - it is used for implementation only, and its methods may change in the
 * future.
 */
public class ClippedImageImpl {

  interface DraggableTemplate extends SafeHtmlTemplates {
    HTMLElement image(SafeUri clearImage, int width, int height, String background);
  }

  interface Template extends SafeHtmlTemplates {
    HTMLElement image(SafeUri clearImage, int width, int height, String background);
  }

  protected static final SafeUri clearImage = UriUtils.fromTrustedString(
          "data:image/gif;base64,R0lGODlhAQABAIAAAP///////yH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==");
  private static Template template;
  private static DraggableTemplate draggableTemplate;

  public void adjust(HTMLElement img, SafeUri url, int left, int top, int width, int height) {
    String style = "url(\"" + url.asString() + "\") no-repeat " + (-left + "px ") + (-top + "px");
    img.style.setProperty("background", style);
    img.style.setProperty("width", width + "px");
    img.style.setProperty("height", height + "px");
  }

  public HTMLElement createStructure(SafeUri url, int left, int top, int width, int height) {
    HTMLElement tmp = DOM.createSpan();
    HTMLElement img = getSafeHtml(url, left, top, width, height);
    tmp.appendChild(img);
    img.onload = createOnLoadHandlerFunction();
    return img;
  }

  public static Element.OnloadFn createOnLoadHandlerFunction() {

       return new Element.OnloadFn() {
          @Override
          public void onInvoke(Event p0) {
            Js.asPropertyMap(this).set("__gwtLastUnhandledEvent", "load");
          }
        };
  }

  public HTMLElement getImgElement(Image image) {
    return image.getElement();
  }

  public HTMLElement getSafeHtml(SafeUri url, int left, int top, int width, int height) {
    return getSafeHtml(url, left, top, width, height, false);
  }

  public HTMLElement getSafeHtml(
      SafeUri url, int left, int top, int width, int height, boolean isDraggable) {
    String background =
            "url(" + url.asString() + ") " + "no-repeat " + (-left + "px ") + (-top + "px");

    if (!isDraggable) {
      return getTemplate()
          .image(clearImage, width, height, background);
    } else {
      return getDraggableTemplate()
          .image(clearImage, width, height, background);
    }
  }

  private DraggableTemplate getDraggableTemplate() {
    // no need to synchronize, JavaScript in the browser is single-threaded
    if (draggableTemplate == null) {
      draggableTemplate = new ClippedImageImpl_DraggableTemplateImpl();
    }
    return draggableTemplate;
  }

  private Template getTemplate() {
    // no need to synchronize, JavaScript in the browser is single-threaded
    if (template == null) {
      template = new ClippedImageImpl_TemplateImpl();
    }
    return template;
  }

  @FunctionalInterface
  @JsFunction
  interface Fn {
    void onInvoke();
  }
}
