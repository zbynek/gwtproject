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

import java.util.Iterator;

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.ui.FiniteWidgetIterator.WidgetProvider;

/**
 * A panel that includes a header (top), footer (bottom), and content (middle) area. The header and
 * footer areas resize naturally. The content area is allocated all of the remaining space between
 * the header and footer area.
 */
public class HeaderPanel extends Panel implements RequiresResize {

  /**
   * The widget provider for this panel.
   *
   * <p>Widgets are returned in the following order:
   *
   * <ol>
   *   <li>Header widget
   *   <li>Content widget
   *   <li>Footer widget
   * </ol>
   */
  private class WidgetProviderImpl implements WidgetProvider {

    public Widget get(int index) {
      switch (index) {
        case 0:
          return header;
        case 1:
          return content;
        case 2:
          return footer;
      }
      throw new ArrayIndexOutOfBoundsException(index);
    }
  }

  private Widget content;
  private final HTMLElement contentContainer;
  private Widget footer;
  private final HTMLElement footerContainer;
  private final ResizeLayoutPanel.Impl footerImpl = new ResizeLayoutPanel.ImplStandard();
  private Widget header;
  private final HTMLElement headerContainer;
  private final ResizeLayoutPanel.Impl headerImpl = new ResizeLayoutPanel.ImplStandard();
  private final ScheduledCommand layoutCmd =
      new ScheduledCommand() {
        public void execute() {
          layoutScheduled = false;
          forceLayout();
        }
      };
  private boolean layoutScheduled = false;

  public HeaderPanel() {
    // Create the outer element
    HTMLElement elem = DOM.createDiv();
    elem.style.position = "relative";
    elem.style.overflow = "hidden";
    setElement(elem);

    // Create a delegate to handle resize from the header and footer.
    ResizeLayoutPanel.Impl.Delegate resizeDelegate =
        new ResizeLayoutPanel.Impl.Delegate() {
          public void onResize() {
            scheduledLayout();
          }
        };

    // Create the header container.
    headerContainer = createContainer();
    headerContainer.style.top = "0";
    headerImpl.init(headerContainer, resizeDelegate);
    elem.appendChild(headerContainer);

    // Create the footer container.
    footerContainer = createContainer();
    footerContainer.style.bottom = "0";
    footerImpl.init(footerContainer, resizeDelegate);
    elem.appendChild(footerContainer);

    // Create the content container.
    contentContainer = createContainer();
    contentContainer.style.overflow = "hidden";
    contentContainer.style.top = "0";
    contentContainer.style.height = CSSProperties.HeightUnionType.of("0");
    elem.appendChild(contentContainer);
  }

  /**
   * Adds a widget to this panel.
   *
   * @param w the child widget to be added
   */
  @Override
  public void add(Widget w) {
    // Add widgets in the order that they appear.
    if (header == null) {
      setHeaderWidget(w);
    } else if (content == null) {
      setContentWidget(w);
    } else if (footer == null) {
      setFooterWidget(w);
    } else {
      throw new IllegalStateException(
          "HeaderPanel already contains header, content, and footer widgets.");
    }
  }

  /**
   * Get the content widget that appears between the header and footer.
   *
   * @return the content {@link Widget}
   */
  public Widget getContentWidget() {
    return content;
  }

  /**
   * Get the footer widget at the bottom of the panel.
   *
   * @return the footer {@link Widget}
   */
  public Widget getFooterWidget() {
    return footer;
  }

  /**
   * Get the header widget at the top of the panel.
   *
   * @return the header {@link Widget}
   */
  public Widget getHeaderWidget() {
    return header;
  }

  public Iterator<Widget> iterator() {
    return new FiniteWidgetIterator(new WidgetProviderImpl(), 3);
  }

  @Override
  public void onAttach() {
    super.onAttach();
    headerImpl.onAttach();
    footerImpl.onAttach();
    scheduledLayout();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    headerImpl.onDetach();
    footerImpl.onDetach();
  }

  public void onResize() {
    // Handle the outer element resizing.
    scheduledLayout();
  }

  @Override
  public boolean remove(Widget w) {
    // Validate.
    if (w.getParent() != this) {
      return false;
    }
    // Orphan.
    try {
      orphan(w);
    } finally {
      // Physical detach.
      w.getElement().remove();

      // Logical detach.
      if (w == content) {
        content = null;
        contentContainer.style.display = "none";
      } else if (w == header) {
        header = null;
        headerContainer.style.display = "none";
      } else if (w == footer) {
        footer = null;
        footerContainer.style.display = "none";
      }
    }
    return true;
  }

  /**
   * Set the widget in the content portion between the header and footer.
   *
   * @param w the widget to use as the content
   */
  public void setContentWidget(Widget w) {
    add(w, content, contentContainer);

    // Logical attach.
    content = w;
    scheduledLayout();
  }

  /**
   * Set the widget in the footer portion at the bottom of the panel.
   *
   * @param w the widget to use as the footer
   */
  public void setFooterWidget(Widget w) {
    add(w, footer, footerContainer);

    // Logical attach.
    footer = w;
    scheduledLayout();
  }

  /**
   * Set the widget in the header portion at the top of the panel.
   *
   * @param w the widget to use as the header
   */
  public void setHeaderWidget(Widget w) {
    add(w, header, headerContainer);

    // Logical attach.
    header = w;
    scheduledLayout();
  }

  /**
   * Add a widget to the panel in the specified container. Note that this method does not do the
   * logical attach.
   *
   * @param w the widget to add
   * @param toReplace the widget to replace
   * @param container the container in which to place the widget
   */
  private void add(Widget w, Widget toReplace, HTMLElement container) {
    // Validate.
    if (w == toReplace) {
      return;
    }

    // Detach new child.
    if (w != null) {
      w.removeFromParent();
    }

    // Remove old child.
    if (toReplace != null) {
      remove(toReplace);
    }

    if (w != null) {
      // Physical attach.
      container.appendChild(w.getElement());
      container.style.display = null;

      adopt(w);
    }
  }

  private HTMLElement createContainer() {
    HTMLElement container = DOM.createDiv();
    container.style.position = "absolute";
    container.style.display = "none";
    container.style.left = "0";
    container.style.width = CSSProperties.WidthUnionType.of("100.0%");
    return container;
  }

  /** Update the layout. */
  private void forceLayout() {
    // No sense in doing layout if we aren't attached or have no content.
    if (!isAttached() || content == null) {
      return;
    }

    // Resize the content area to fit between the header and footer.
    int remainingHeight = getElement().clientHeight;
    if (header != null) {
      int height = Math.max(0, headerContainer.offsetHeight);
      remainingHeight -= height;
      contentContainer.style.top = height + "px";
    } else {
      contentContainer.style.top = "0";
    }
    if (footer != null) {
      remainingHeight -= footerContainer.offsetHeight;
    }
    contentContainer.style.height =
            CSSProperties.HeightUnionType.of(Math.max(0, remainingHeight) + "px");

    // Provide resize to child.
    if (content instanceof RequiresResize) {
      ((RequiresResize) content).onResize();
    }
  }

  /** Schedule layout to adjust the height of the content area. */
  private void scheduledLayout() {
    if (isAttached() && !layoutScheduled) {
      layoutScheduled = true;
      Scheduler.get().scheduleDeferred(layoutCmd);
    }
  }
}
