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

import jsinterop.annotations.JsFunction;
import jsinterop.base.Js;
import org.gwtproject.core.client.JavaScriptObject;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.safecss.shared.SafeStyles;
import org.gwtproject.safecss.shared.SafeStylesBuilder;
import org.gwtproject.safecss.shared.SafeStylesUtils;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeUri;
import org.gwtproject.safehtml.shared.UriUtils;
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
    SafeHtml image(SafeUri clearImage, SafeStyles style);
  }

  interface Template extends SafeHtmlTemplates {
    SafeHtml image(SafeUri clearImage, SafeStyles style);
  }

  protected static final SafeUri clearImage = UriUtils.fromTrustedString("clear.cache.gif"); // TODO
  private static Template template;
  private static DraggableTemplate draggableTemplate;

  public void adjust(Element img, SafeUri url, int left, int top, int width, int height) {
    String style = "url(\"" + url.asString() + "\") no-repeat " + (-left + "px ") + (-top + "px");
    img.getStyle().setProperty("background", style);
    img.getStyle().setPropertyPx("width", width);
    img.getStyle().setPropertyPx("height", height);
  }

  public Element createStructure(SafeUri url, int left, int top, int width, int height) {
    Element tmp = Document.get().createSpanElement();
    tmp.setInnerSafeHtml(getSafeHtml(url, left, top, width, height));

    Element elem = tmp.getFirstChildElement();
    elem.setPropertyJSO("onload", createOnLoadHandlerFunction());
    return elem;
  }

  public static JavaScriptObject createOnLoadHandlerFunction() {
    return Js.uncheckedCast(
        new Fn() {
          @Override
          public void onInvoke() {
            Js.asPropertyMap(this).set("__gwtLastUnhandledEvent", "load");
          }
        });
  }

  public Element getImgElement(Image image) {
    return image.getElement();
  }

  public SafeHtml getSafeHtml(SafeUri url, int left, int top, int width, int height) {
    return getSafeHtml(url, left, top, width, height, false);
  }

  public SafeHtml getSafeHtml(
      SafeUri url, int left, int top, int width, int height, boolean isDraggable) {
    SafeStylesBuilder builder = new SafeStylesBuilder();
    builder
        .width(width, Unit.PX)
        .height(height, Unit.PX)
        .trustedNameAndValue(
            "background",
            "url(" + url.asString() + ") " + "no-repeat " + (-left + "px ") + (-top + "px"));

    if (!isDraggable) {
      return getTemplate()
          .image(clearImage, SafeStylesUtils.fromTrustedString(builder.toSafeStyles().asString()));
    } else {
      return getDraggableTemplate()
          .image(clearImage, SafeStylesUtils.fromTrustedString(builder.toSafeStyles().asString()));
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
