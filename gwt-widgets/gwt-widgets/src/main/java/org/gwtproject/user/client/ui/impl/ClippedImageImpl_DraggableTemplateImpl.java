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
package org.gwtproject.user.client.ui.impl;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLImageElement;
import org.gwtproject.user.client.DOM;

/**
 * This class is generated from
 * org.gwtproject.user.client.ui.impl.ClippedImageImpl.DraggableTemplate, do not edit manually
 */
public class ClippedImageImpl_DraggableTemplateImpl
    implements org.gwtproject.user.client.ui.impl.ClippedImageImpl.DraggableTemplate {

  /** @Template("<img src='{0}' style='{1}' border='0' draggable='true'>") */
  public HTMLElement image(
          org.gwtproject.safehtml.shared.SafeUri arg0, int width, int height, String background) {
    HTMLImageElement img = DOM.createImg();
    img.src = arg0.asString();
    img.style.setProperty("width", width + "px");
    img.style.setProperty("height", height + "px");
    img.style.background = background;
    img.draggable = true;
    img.border = "0";
    return img;
  }
}
