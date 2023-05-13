/*
 * Copyright © 2019 The GWT Authors
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
package org.gwtproject.layout.client;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.layout.client.Layout.Layer;
import org.gwtproject.user.client.DOM;

/**
 * Default implementation, which works with all browsers except for IE6. It uses only the "top",
 * "left", "bottom", "right", "width", and "height" CSS properties.
 *
 * <p>Note: This implementation class has state, so {@link Layout} must create a new instance for
 * each layout-parent.
 */
class LayoutImpl {

  private static HTMLDivElement fixedRuler;

  static {
    fixedRuler = createRuler("cm", "cm");
    DomGlobal.document.body.appendChild(fixedRuler);
  }

  protected HTMLDivElement relativeRuler;

  protected static HTMLDivElement createRuler(String widthUnit,String heightUnit) {
    HTMLDivElement ruler = DOM.createDiv();
    ruler.innerHTML = "&nbsp;";
    CSSStyleDeclaration style = ruler.style;
    style.position = "absolute";
    style.zIndex = CSSProperties.ZIndexUnionType.of(-32767);

    // Position the ruler off the top edge, double the size just to be
    // extra sure it doesn't show up on the screen.
    style.top = -20 + heightUnit;

    // Note that we are making the ruler element 10x10, because some browsers
    // generate non-integral ratios (e.g., 1em == 13.3px), so we need a little
    // extra precision.
    style.width = CSSProperties.WidthUnionType.of(10 + widthUnit);
    style.height = CSSProperties.HeightUnionType.of(10 + heightUnit);

    style.visibility = "hidden";
    ruler.setAttribute("aria-hidden", "true");
    return ruler;
  }

  public HTMLElement attachChild(HTMLElement parent, HTMLElement child,
                             HTMLElement before) {
    HTMLDivElement container = DOM.createDiv();
    container.appendChild(child);

    container.style.position = "absolute";
    container.style.overflow = "hidden";

    fillParent(child);

    HTMLElement beforeContainer = null;
    if (before != null) {
      beforeContainer = Js.uncheckedCast(before.parentElement);
      assert beforeContainer.parentElement == parent
          : "Element to insert before must be a sibling";
    }
    parent.insertBefore(container, beforeContainer);
    return container;
  }

  public void fillParent(HTMLElement elem) {
    CSSStyleDeclaration style = elem.style;
    style.position = "absolute";
    style.left = "0";
    style.top = "0";
    style.right = "0";
    style.bottom = "0";
  }

  /** @param parent the parent element */
  public void finalizeLayout(HTMLElement parent) {}

  public double getUnitSizeInPixels(HTMLElement parent, Unit unit, boolean vertical) {
    if (unit == null) {
      return 1;
    }

    switch (unit) {
      case PCT:
        return (vertical ? parent.clientHeight : parent.clientWidth) / 100.0;
      case EM:
        return relativeRuler.offsetWidth / 10.0;
      case EX:
        return relativeRuler.offsetWidth / 10.0;
      case CM:
        return fixedRuler.offsetWidth * 0.1; // 1.0 cm / cm
      case MM:
        return fixedRuler.offsetWidth * 0.01; // 0.1 cm / mm
      case IN:
        return fixedRuler.offsetWidth * 0.254; // 2.54 cm / in
      case PT:
        return fixedRuler.offsetWidth * 0.00353; // 0.0353 cm / pt
      case PC:
        return fixedRuler.offsetWidth * 0.0423; // 0.423 cm / pc
      default:
      case PX:
        return 1;
    }
  }

  public void initParent(HTMLElement parent) {
    parent.style.position = "relative";
    parent.appendChild(relativeRuler = createRuler("em", "ex"));
  }

  public void layout(Layer layer) {
    CSSStyleDeclaration style = layer.container.style;

    if (layer.visible) {
      style.display = null;
    } else {
      style.display = "none";
    }

    style.setProperty("left", layer.setLeft ? (layer.left + layer.leftUnit.getType()) : "");
    style.setProperty("top", layer.setTop ? (layer.top + layer.topUnit.getType()) : "");
    style.setProperty("right", layer.setRight ? (layer.right + layer.rightUnit.getType()) : "");
    style.setProperty("bottom", layer.setBottom ? (layer.bottom + layer.bottomUnit.getType()) : "");
    style.setProperty("width", layer.setWidth ? (layer.width + layer.widthUnit.getType()) : "");
    style.setProperty("height", layer.setHeight ? (layer.height + layer.heightUnit.getType()) : "");

    style = layer.child.style;
    switch (layer.hPos) {
      case BEGIN:
        style.left = "0";
        style.right = null;
        break;
      case END:
        style.left = null;
        style.right = "0";
        break;
      case STRETCH:
        style.left = "0";
        style.right = "0";
        break;
    }

    switch (layer.vPos) {
      case BEGIN:
        style.top = "0";
        style.bottom = null;
        break;
      case END:
        style.top = null;
        style.bottom = "0";
        break;
      case STRETCH:
        style.top = "0";
        style.bottom = "0";
        break;
    }
  }

  @SuppressWarnings("unused")
  public void onAttach(HTMLElement parent) {
    // Do nothing. This exists only to help LayoutImplIE6 avoid memory leaks.
  }

  @SuppressWarnings("unused")
  public void onDetach(HTMLElement parent) {
    // Do nothing. This exists only to help LayoutImplIE6 avoid memory leaks.
  }

  public void removeChild(HTMLElement container, HTMLElement child) {
    container.remove();

    // We want this code to be resilient to the child having already been
    // removed from its container (perhaps by widget code).
    if (child.parentElement == container) {
      child.remove();
    }

    // Cleanup child styles set by fillParent().
    CSSStyleDeclaration style = child.style;
    style.position = null;
    style.left = null;
    style.top = null;
    style.width = null;
    style.height = null;
  }
}
