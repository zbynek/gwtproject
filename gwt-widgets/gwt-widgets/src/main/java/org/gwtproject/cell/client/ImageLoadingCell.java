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
package org.gwtproject.cell.client;

import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.EventTarget;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.dom.style.shared.Display;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.text.shared.AbstractSafeHtmlRenderer;
import org.gwtproject.text.shared.SafeHtmlRenderer;
import org.gwtproject.user.client.ui.AbstractImagePrototype;

/**
 * An {@link org.gwtproject.cell.client.AbstractCell} used to render an image. A loading indicator
 * is used until the image is fully loaded. The String value is the url of the image.
 */
public class ImageLoadingCell extends org.gwtproject.cell.client.AbstractCell<String> {

  /** The renderers used by this cell. */
  public interface Renderers {

    /**
     * Get the renderer used to render an error message when the image does not load. By default,
     * the broken image is rendered.
     *
     * @return the {@link SafeHtmlRenderer} used when the image doesn't load
     */
    SafeHtmlRenderer<String> getErrorRenderer();

    /**
     * Get the renderer used to render the image. This renderer must render an <code>img</code>
     * element, which triggers the <code>load</code> or <code>
     * error</code> event that this cell handles.
     *
     * @return the {@link SafeHtmlRenderer} used to render the image
     */
    SafeHtmlRenderer<String> getImageRenderer();

    /**
     * Get the renderer used to render a loading message. By default, an animated loading icon is
     * rendered.
     *
     * @return the {@link SafeHtmlRenderer} used to render the loading html
     */
    SafeHtmlRenderer<String> getLoadingRenderer();
  }

  interface Template extends SafeHtmlTemplates {

    ImageLoadingCell.Template INSTANCE = new ImageLoadingCell_TemplateImpl();

    SafeHtml image(SafeHtml imageHtml);

    SafeHtml img(String url);

    SafeHtml loading(SafeHtml loadingHtml);
  }

  /** The default {@link SafeHtmlRenderer SafeHtmlRenderers}. */
  public static class DefaultRenderers implements Renderers {

    private static SafeHtmlRenderer<String> IMAGE_RENDERER;
    private static SafeHtmlRenderer<String> LOADING_RENDERER;

    public DefaultRenderers() {
      if (IMAGE_RENDERER == null) {
        IMAGE_RENDERER =
            new AbstractSafeHtmlRenderer<String>() {
              public SafeHtml render(String object) {
                return Template.INSTANCE.img(object);
              }
            };
      }
      if (LOADING_RENDERER == null) {
        Resources resources = new ImageLoadingCell_ResourcesImpl();
        ImageResource res = resources.loading();
        final SafeHtml loadingHtml = AbstractImagePrototype.create(res).getSafeHtml();
        LOADING_RENDERER =
            new AbstractSafeHtmlRenderer<String>() {
              public SafeHtml render(String object) {
                return loadingHtml;
              }
            };
      }
    }

    /**
     * Returns the renderer for a broken image.
     *
     * @return a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
     */
    public SafeHtmlRenderer<String> getErrorRenderer() {
      // Show the broken image on error.
      return getImageRenderer();
    }

    /**
     * Returns the renderer for an image.
     *
     * @return a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
     */
    public SafeHtmlRenderer<String> getImageRenderer() {
      return IMAGE_RENDERER;
    }

    /**
     * Returns the renderer for a loading image.
     *
     * @return a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
     */
    public SafeHtmlRenderer<String> getLoadingRenderer() {
      return LOADING_RENDERER;
    }
  }

  /** The images used by the {@link DefaultRenderers}. */
  interface Resources extends ClientBundle {
    Resources INSTANCE = new ImageLoadingCell_ResourcesImpl();

    ImageResource loading();
  }

  private final SafeHtmlRenderer<String> errorRenderer;
  private final SafeHtmlRenderer<String> imageRenderer;
  private final SafeHtmlRenderer<String> loadingRenderer;

  /**
   * Construct an {@link org.gwtproject.cell.client.ImageResourceCell} using the {@link
   * DefaultRenderers}.
   *
   * <p>The {@link DefaultRenderers} will be constructed using {@link GWT#create(Class)}, which
   * allows you to replace the class using a deferred binding.
   */
  public ImageLoadingCell() {
    this(new DefaultRenderers());
  }

  /**
   * Construct an {@link org.gwtproject.cell.client.ImageResourceCell} using the specified {@link
   * SafeHtmlRenderer SafeHtmlRenderers}.
   *
   * @param renderers an instance of {@link ImageLoadingCell.Renderers Renderers}
   */
  public ImageLoadingCell(Renderers renderers) {
    super(BrowserEvents.LOAD, BrowserEvents.ERROR);
    this.errorRenderer = renderers.getErrorRenderer();
    this.imageRenderer = renderers.getImageRenderer();
    this.loadingRenderer = renderers.getLoadingRenderer();
  }

  @Override
  public void onBrowserEvent(
      Context context,
      Element parent,
      String value,
      NativeEvent event,
      org.gwtproject.cell.client.ValueUpdater<String> valueUpdater) {
    // The loading indicator can fire its own load or error event, so we check
    // that the event actually occurred on the main image.
    String type = event.getType();
    if (BrowserEvents.LOAD.equals(type) && eventOccurredOnImage(event, parent)) {
      // Remove the loading indicator.
      parent.getFirstChildElement().getStyle().setDisplay(Display.NONE);

      // Show the image.
      Element imgWrapper = parent.getChild(1).cast();
      imgWrapper.getStyle().setProperty("height", "auto");
      imgWrapper.getStyle().setProperty("width", "auto");
      imgWrapper.getStyle().setProperty("overflow", "auto");
    } else if (BrowserEvents.ERROR.equals(type) && eventOccurredOnImage(event, parent)) {
      // Replace the loading indicator with an error message.
      parent.getFirstChildElement().setInnerSafeHtml(errorRenderer.render(value));
    }
  }

  @Override
  public void render(Context context, String value, SafeHtmlBuilder sb) {
    // We can't use ViewData because we don't know the caching policy of the
    // browser. The browser may fetch the image every time we render.
    if (value != null) {
      sb.append(Template.INSTANCE.loading(loadingRenderer.render(value)));
      sb.append(Template.INSTANCE.image(imageRenderer.render(value)));
    }
  }

  /**
   * Check whether or not an event occurred within the wrapper around the image element.
   *
   * @param event the event
   * @param parent the parent element
   * @return true if the event targets the image
   */
  private boolean eventOccurredOnImage(NativeEvent event, Element parent) {
    EventTarget eventTarget = event.getEventTarget();
    if (!Element.is(eventTarget)) {
      return false;
    }
    Element target = eventTarget.cast();

    // Make sure the target occurred within the div around the image.
    Element imgWrapper = parent.getFirstChildElement().getNextSiblingElement();
    return imgWrapper.isOrHasChild(target);
  }
}
