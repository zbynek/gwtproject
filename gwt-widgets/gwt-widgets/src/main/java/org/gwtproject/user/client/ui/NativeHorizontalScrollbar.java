/*
 * Copyright 2011 Google Inc.
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
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.CommonResources;
import org.gwtproject.resources.client.CssResource;
import org.gwtproject.resources.client.CssResource.ImportedWithPrefix;
import org.gwtproject.uibinder.client.UiBinder;
import org.gwtproject.uibinder.client.UiField;

/** A horizontal scrollbar implemented using the browsers native scrollbar. */
public class NativeHorizontalScrollbar extends AbstractNativeScrollbar
    implements HorizontalScrollbar {

  interface NativeHorizontalScrollbarUiBinder extends UiBinder<Element, NativeHorizontalScrollbar> {
    NativeHorizontalScrollbarUiBinder INSTANCE =
        new NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl();
  }
  /** A ClientBundle of resources used by this widget. */
  public interface Resources extends ClientBundle {

    Resources INSTANCE = new NativeHorizontalScrollbar_ResourcesImpl();

    /** The styles used in this widget. */
    @Source(Style.DEFAULT_CSS)
    Style nativeHorizontalScrollbarStyle();
  }

  /**
   * A variation of {@link Resources} that renders the scrollbar semi-transparent until it is
   * hovered.
   */
  public interface ResourcesTransparant extends Resources {

    ResourcesTransparant INSTANCE = new NativeHorizontalScrollbar_ResourcesTransparantImpl();
    /** The styles used in this widget. */
    @Source(StyleTransparant.DEFAULT_CSS)
    Style nativeHorizontalScrollbarStyle();
  }

  /** Styles used by this widget. */
  @ImportedWithPrefix("gwt-NativeHorizontalScrollbar")
  public interface Style extends CssResource {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/client/ui/NativeHorizontalScrollbar.gss";

    /** Applied to the scrollbar. */
    String nativeHorizontalScrollbar();
  }

  /**
   * A variation of {@link Style} that renders the scrollbar semi-transparent until it is hovered.
   */
  public interface StyleTransparant extends Style {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/client/ui/NativeHorizontalScrollbarTransparent.gss";
  }

  private static Resources DEFAULT_RESOURCES;

  private static NativeHorizontalScrollbarUiBinder uiBinder =
      new NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl();

  /** Get the default resources for this widget. */
  private static Resources getDefaultResources() {
    if (DEFAULT_RESOURCES == null) {
      DEFAULT_RESOURCES = Resources.INSTANCE;
    }
    return DEFAULT_RESOURCES;
  }

  /** The div inside the scrollable div that forces scrollbars to appear. */
  @UiField Element contentDiv;

  /** The scrollable div used to create a scrollbar. */
  @UiField Element scrollable;

  /** Construct a new {@link NativeHorizontalScrollbar}. */
  public NativeHorizontalScrollbar() {
    this(getDefaultResources());
  }

  /**
   * Construct a new {@link NativeHorizontalScrollbar}.
   *
   * @param resources the resources used by this widget
   */
  public NativeHorizontalScrollbar(Resources resources) {
    setElement(uiBinder.createAndBindUi(this));
    getElement().addClassName(CommonResources.getInlineBlockStyle());
    setHeight(getNativeHeight() + "px");

    // Apply the styles.
    Style style = resources.nativeHorizontalScrollbarStyle();
    style.ensureInjected();
    getScrollableElement().addClassName(style.nativeHorizontalScrollbar());

    // Initialize the implementation.
    ScrollImpl.get().initialize(scrollable, contentDiv);
  }

  public int getHorizontalScrollPosition() {
    return getScrollableElement().getScrollLeft();
  }

  public int getMaximumHorizontalScrollPosition() {
    return ScrollImpl.get().getMaximumHorizontalScrollPosition(getScrollableElement());
  }

  public int getMinimumHorizontalScrollPosition() {
    return ScrollImpl.get().getMinimumHorizontalScrollPosition(getScrollableElement());
  }

  /**
   * Get the width in pixels of the scrollable content that the scrollbar controls.
   *
   * <p>This is not the same as the maximum scroll left position. The maximum scroll position equals
   * the <code>scrollWidth - offsetWidth</code>;
   *
   * @return the scroll width
   * @see #setScrollWidth(int)
   */
  public int getScrollWidth() {
    return contentDiv.getOffsetWidth();
  }

  public void setHorizontalScrollPosition(int position) {
    getScrollableElement().setScrollLeft(position);
  }

  /**
   * Set the width in pixels of the scrollable content that the scrollbar controls.
   *
   * <p>This is not the same as the maximum scroll left position. The maximum scroll position equals
   * the <code>scrollWidth - offsetWidth</code>;
   *
   * @param width the size width pixels
   */
  public void setScrollWidth(int width) {
    contentDiv.getStyle().setWidth(width, Unit.PX);
  }

  /**
   * Get the height of the scrollbar.
   *
   * @return the height of the scrollbar in pixels
   */
  protected int getNativeHeight() {
    return getNativeScrollbarHeight();
  }

  @Override
  protected Element getScrollableElement() {
    return scrollable;
  }
}
