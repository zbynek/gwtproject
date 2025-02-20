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
package org.gwtproject.user.client.ui;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.j2cl.junit.apt.J2clTestInput;
import org.gwtproject.event.logical.shared.CloseEvent;
import org.gwtproject.event.logical.shared.CloseHandler;
import org.gwtproject.event.logical.shared.OpenEvent;
import org.gwtproject.event.logical.shared.OpenHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.timer.client.Timer;
import org.gwtproject.user.client.DOM;

/** Tests core functionality of {@link DisclosurePanel}. */
@J2clTestInput(DisclosurePanelTest.class)
public class DisclosurePanelTest extends GWTTestCase {
  private static final int OPEN = 0;

  private static final int CLOSE = 1;

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.DebugTest";
  }

  /** Test to ensure css style changes that control core functionality do change appropriately. */
  public void testCoreFunctionality() {
    final DisclosurePanel panel = createTestPanel();
    assertTrue(
        DOM.getParent(panel.getContent().getElement())
            .getStyle()
            .getProperty("display")
            .equalsIgnoreCase("none"));

    panel.setOpen(true);

    delayTestFinish(5000);
    // Allow the animation time to finish
    Timer t =
        new Timer() {
          @Override
          public void run() {
            assertTrue(
                DOM.getParent(panel.getContent().getElement())
                    .getStyle()
                    .getProperty("display")
                    .trim()
                    .equals(""));
            finishTest();
          }
        };
    t.schedule(450);
  }

  /** Tests {@link DisclosurePanel#add(IsWidget)}. */
  public void testAddAsIsWidget() {
    DisclosurePanel panel = createEmptyDisclourePanel();
    Widget widget = new Label("foo");

    // IsWidget cast to call the overloaded version
    panel.add((IsWidget) widget);

    assertSame(widget, panel.getContent());
  }

  /**
   * Ensures that {@link DisclosurePanel#add(IsWidget)} does <b>NOT</b> throws a {@link
   * NullPointerException} when the IsWidget argument is <code>null</code>. Stupid, but it's what
   * add(Widget) does.
   */
  public void testAddNullAsIsWidget() {
    DisclosurePanel panel = createEmptyDisclourePanel();
    IsWidget widget = null;

    panel.add(widget);
    // ta da...
  }

  public void testAttachDetachOrder() {
    HasWidgetsTester.testAll(
        new DisclosurePanel(), new HasWidgetsTester.DefaultWidgetAdder(), false);
  }

  public void testDebugId() {
    Label header = new Label("header");
    Label content = new Label("content");
    DisclosurePanel panel = new DisclosurePanel("header");
    panel.setContent(content);
    panel.ensureDebugId("myPanel");

    // Check the body ids
    UIObjectTest.assertDebugId("myPanel", panel.getElement());
    // TODO check this, coz header.getElement() has no parents
    // UIObjectTest.assertDebugId("myPanel-header", DOM.getParent(header.getElement()));
  }

  public void testEvents() {
    final DisclosurePanel panel = createTestPanel();
    assertEquals(1, panel.getHandlerManager().getHandlerCount(CloseEvent.getType()));
    panel.addCloseHandler(
        new CloseHandler<DisclosurePanel>() {

          @Override
          public void onClose(CloseEvent<DisclosurePanel> event) {
            // for now nothing.
          }
        });
    assertEquals(2, panel.getHandlerManager().getHandlerCount(CloseEvent.getType()));
  }

  /** Test to ensure that event handler dispatch function appropriately. */
  public void testEventHandlers() {

    final boolean[] aDidFire = new boolean[2];
    final boolean[] bDidFire = new boolean[2];
    final DisclosurePanel panel = createTestPanel();

    CloseHandler<DisclosurePanel> closeHandleA =
        new CloseHandler<DisclosurePanel>() {
          @Override
          public void onClose(CloseEvent<DisclosurePanel> event) {
            aDidFire[CLOSE] = true;
          }
        };
    OpenHandler<DisclosurePanel> openHandleA =
        new OpenHandler<DisclosurePanel>() {
          @Override
          public void onOpen(OpenEvent<DisclosurePanel> event) {
            aDidFire[OPEN] = true;
          }
        };
    CloseHandler<DisclosurePanel> closeHandleB =
        new CloseHandler<DisclosurePanel>() {
          @Override
          public void onClose(CloseEvent<DisclosurePanel> event) {
            assertEquals(event.getSource(), panel);
            bDidFire[CLOSE] = true;
          }
        };
    OpenHandler<DisclosurePanel> openHandleB =
        new OpenHandler<DisclosurePanel>() {
          @Override
          public void onOpen(OpenEvent<DisclosurePanel> event) {
            assertEquals(event.getSource(), panel);
            bDidFire[OPEN] = true;
          }
        };

    panel.addOpenHandler(openHandleA);
    panel.addCloseHandler(closeHandleA);
    HandlerRegistration openHandleBReg = panel.addOpenHandler(openHandleB);
    HandlerRegistration closeHandleBReg = panel.addCloseHandler(closeHandleB);
    // DisclosePanel registers itself as Open- and CloseHandler. So we are at 3 each.
    assertEquals(3, panel.getHandlerManager().getHandlerCount(CloseEvent.getType()));
    assertEquals(3, panel.getHandlerManager().getHandlerCount(OpenEvent.getType()));

    panel.setOpen(true);
    // We expect onOpen to fire and onClose to not fire.
    assertTrue(aDidFire[OPEN] && bDidFire[OPEN] && !aDidFire[CLOSE] && !bDidFire[CLOSE]);

    aDidFire[OPEN] = bDidFire[OPEN] = false;

    panel.setOpen(false);
    // We expect onOpen to fire and onClose to not fire.
    assertTrue(aDidFire[CLOSE] && bDidFire[CLOSE] && !aDidFire[OPEN] && !bDidFire[OPEN]);

    aDidFire[OPEN] = bDidFire[CLOSE] = false;

    openHandleBReg.removeHandler();
    closeHandleBReg.removeHandler();
    assertEquals(2, panel.getHandlerManager().getHandlerCount(OpenEvent.getType()));
    assertEquals(2, panel.getHandlerManager().getHandlerCount(CloseEvent.getType()));

    panel.setOpen(true);
    panel.setOpen(false);
    // We expect a to have fired both events, and b to have fired none.
    assertTrue(aDidFire[OPEN]);
    assertTrue(aDidFire[CLOSE]);
    assertTrue(!bDidFire[OPEN]);
    assertTrue(!bDidFire[CLOSE]);
  }

  /**
   * Tests that the content is set to null if the content widget's {@link Widget#removeFromParent()}
   * method is called.
   */
  public void testRemoveFromParent() {
    DisclosurePanel panel = createTestPanel();
    Label content = new Label();
    panel.setContent(content);
    assertEquals(content, panel.getContent());
    content.removeFromParent();
    assertNull(panel.getContent());
  }

  /** Tests {@link DisclosurePanel#remove(IsWidget)}. */
  public void testRemoveAsIsWidget() {
    DisclosurePanel panel = createEmptyDisclourePanel();
    Widget widget = new Label("foo");
    panel.setContent(widget);
    assertSame(widget, panel.getContent());

    boolean wasPresent = panel.remove((IsWidget) widget);

    assertTrue(wasPresent);
    assertNull(panel.getContent());
  }

  /**
   * Ensures that {@link DisclosurePanel#remove(IsWidget)} does <b>NOT</b> throws a {@link
   * NullPointerException} when the IsWidget argument is <code>null</code>, for consistency with
   * remove(Widget) brain damage.
   */
  public void testRemoveNullAsIsWidget() {
    DisclosurePanel panel = createEmptyDisclourePanel();
    // IsWidget reference to call the overload version
    IsWidget widget = null;

    panel.remove(widget);
    // ta da...
  }

  private DisclosurePanel createEmptyDisclourePanel() {
    return new DisclosurePanel();
  }

  private DisclosurePanel createTestPanel() {
    DisclosurePanel panel = new DisclosurePanel("Test Subject");
    panel.setContent(new SimplePanel());
    return panel;
  }
}
