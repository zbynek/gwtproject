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
import elemental2.dom.HTMLIFrameElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jsinterop.base.Js;
import org.gwtproject.core.client.GWT;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.event.dom.client.ClickEvent;
import org.gwtproject.event.dom.client.ClickHandler;
import org.gwtproject.event.dom.client.FocusEvent;
import org.gwtproject.event.dom.client.FocusHandler;
import org.gwtproject.event.logical.shared.InitializeEvent;
import org.gwtproject.event.logical.shared.InitializeHandler;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.gwtproject.timer.client.Timer;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.ui.RichTextArea.BasicFormatter;

/** Tests the {@link RichTextArea} widget. */
@J2clTestInput(RichTextAreaTest.class)
public class RichTextAreaTest extends GWTTestCase {
  static final int RICH_TEXT_ASYNC_DELAY = 3000;
  private static final String html = "<b>hello</b><i>world</i>";

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.Widgets";
  }

  /**
   * Test that removing and re-adding an RTA doesn't destroy its contents (Only IE actually
   * preserves dynamically-created iframe contents across DOM removal/re-adding).
   */
  public void testAddEditRemoveAdd() {
    final RichTextArea area = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    area.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            RootPanel.get().remove(area);
            RootPanel.get().add(area);

            // It's ok (and important) to check the HTML immediately after re-adding
            // the rta.
            assertEquals("foo", area.getHTML());
            finishTest();
          }
        });
    RootPanel.get().add(area);
    area.setHTML("foo");
  }

  public void testBlurAfterAttach() {
    final RichTextArea rta = new RichTextArea();
    final List<String> actual = new ArrayList<String>();
    rta.addFocusHandler(
        new FocusHandler() {
          @Override
          public void onFocus(FocusEvent event) {
            actual.add("test");
          }
        });
    RootPanel.get().add(rta);
    rta.setFocus(true);
    rta.setFocus(false);

    // This has to be done on a timer because the rta can take some time to
    // finish initializing (on some browsers).
    this.delayTestFinish(3000);
    new Timer() {
      @Override
      public void run() {
        assertEquals(0, actual.size());
        RootPanel.get().remove(rta);
        finishTest();
      }
    }.schedule(2000);
  }

  public void testFocusAfterAttach() {
    final RichTextArea rta = new RichTextArea();
    final List<String> actual = new ArrayList<String>();
    rta.addFocusHandler(
        new FocusHandler() {
          @Override
          public void onFocus(FocusEvent event) {
            actual.add("test");
          }
        });
    RootPanel.get().add(rta);
    rta.setFocus(true);
    RootPanel.get().remove(rta);

    // Webkit based browsers will not fire a focus event if the browser window
    // is behind another window, so we can only test that this doesn't cause
    // an error.
  }

  /**
   * Test that adding and removing an RTA before initialization completes doesn't throw an
   * exception.
   */
  public void testAddRemoveBeforeInit() {
    final RichTextArea richTextArea = new RichTextArea();
    RootPanel.get().add(richTextArea);
    RootPanel.get().remove(richTextArea);
  }

  public void testFormatAfterAttach() {
    final RichTextArea area = new RichTextArea();
    BasicFormatter formatter = area.getBasicFormatter();
    RootPanel.get().add(area);
    if (formatter != null) {
      try {
        formatter.toggleBold();
        if (!GWT.isScript()) {
          fail("Expected AssertionError");
        }
      } catch (AssertionError e) {
        // Expected because the iframe is not initialized
        return;
      }
      if (!GWT.isScript()) {
        fail("Expected AssertionError");
      }
    }
  }

  public void testFormatAfterInitialize() {
    final RichTextArea area = new RichTextArea();

    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    area.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            BasicFormatter formatter = area.getBasicFormatter();
            if (formatter != null) {
              formatter.toggleBold();
            }
            RootPanel.get().remove(area);
            finishTest();
          }
        });
    RootPanel.get().add(area);
  }

  public void testFormatBeforeAttach() {
    final RichTextArea area = new RichTextArea();
    BasicFormatter formatter = area.getBasicFormatter();
    if (formatter != null) {
      try {
        formatter.toggleBold();
        if (!GWT.isScript()) {
          fail("Expected AssertionError");
        }
      } catch (AssertionError e) {
        // expected
        return;
      }
      if (!GWT.isScript()) {
        fail("Expected AssertionError");
      }
    }
  }

  public void testFormatWhenHidden() {
    final RichTextArea area = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    area.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            area.setVisible(false);
            BasicFormatter formatter = area.getBasicFormatter();
            if (formatter != null) {
              // This won't work on some browsers, but it should return quietly.
              formatter.toggleBold();
            }
            RootPanel.get().remove(area);
            finishTest();
          }
        });
    RootPanel.get().add(area);
  }

  /** See that the custom InitializeEvent fires. */
  public void testRichTextInitializeEvent() {
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    final RichTextArea richTextArea = new RichTextArea();
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
  }

  /** Test that a delayed call to setEnable is reflected. */
  public void testSetEnabledAfterInit() {
    final RichTextArea richTextArea = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            richTextArea.setEnabled(false);
            assertEquals(false, richTextArea.isEnabled());
            richTextArea.setEnabled(true);
            assertEquals(true, richTextArea.isEnabled());
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
  }

  /** Test that a call to setEnable is reflected immediately, and after the area loads. */
  public void testSetEnabledBeforeInit() {
    final RichTextArea richTextArea = new RichTextArea();
    richTextArea.setEnabled(false);
    assertEquals(false, richTextArea.isEnabled());
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            assertEquals(false, richTextArea.isEnabled());
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
    assertEquals(false, richTextArea.isEnabled());
  }

  /** Test that events are dispatched correctly to handlers. */
  public void testEventDispatch() {
    final RichTextArea rta = new RichTextArea();
    RootPanel.get().add(rta);

    final List<String> actual = new ArrayList<String>();
    rta.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            assertNotNull(Event.getCurrentEvent());
            actual.add("test");
          }
        });

    // Fire a click event after the iframe is available
    delayTestFinish(1000);
    new Timer() {
      @Override
      public void run() {
        assertEquals(0, actual.size());
        NativeEvent event =
            getDocument(rta).createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
        getBodyElement(rta).dispatchEvent(event);
        assertEquals(1, actual.size());
        RootPanel.get().remove(rta);
        finishTest();
      }
    }.schedule(500);
  }

  /**
   * Test that a delayed set of HTML is reflected. Some platforms have timing subtleties that need
   * to be tested.
   */
  public void testSetHTMLAfterInit() {
    final RichTextArea richTextArea = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            richTextArea.setHTML("<b>foo</b>");
            assertEquals("<b>foo</b>", richTextArea.getHTML().toLowerCase(Locale.ROOT));
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
  }

  /**
   * Test that an immediate set of HTML is reflected immediately and after the area loads. Some
   * platforms have timing subtleties that need to be tested.
   */
  public void testSetHTMLBeforeInit() {
    final RichTextArea richTextArea = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            new Timer() {
              @Override
              public void run() {
                assertEquals("<b>foo</b>", richTextArea.getHTML().toLowerCase(Locale.ROOT));
                finishTest();
              }
            }.schedule(100);
          }
        });
    richTextArea.setHTML("<b>foo</b>");
    RootPanel.get().add(richTextArea);
    assertEquals("<b>foo</b>", richTextArea.getHTML().toLowerCase(Locale.ROOT));
  }

  /**
   * Test that a delayed set of safe html is reflected. Some platforms have timing subtleties that
   * need to be tested.
   */
  public void testSetSafeHtmlAfterInit() {
    final RichTextArea richTextArea = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            richTextArea.setHTML(SafeHtmlUtils.fromSafeConstant(html));
            assertEquals(html, richTextArea.getHTML().toLowerCase(Locale.ROOT));
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
  }

  /**
   * Test that an immediate set of safe html is reflected immediately and after the area loads. Some
   * platforms have timing subtleties that need to be tested.
   */
  public void testSetSafeHtmlBeforeInit() {
    final RichTextArea richTextArea = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            new Timer() {
              @Override
              public void run() {
                assertEquals(html, richTextArea.getHTML().toLowerCase(Locale.ROOT));
                finishTest();
              }
            }.schedule(100);
          }
        });
    richTextArea.setHTML(SafeHtmlUtils.fromSafeConstant(html));
    RootPanel.get().add(richTextArea);
    assertEquals(html, richTextArea.getHTML().toLowerCase(Locale.ROOT));
  }

  /**
   * Test that delayed set of text is reflected. Some platforms have timing subtleties that need to
   * be tested.
   */
  public void testSetTextAfterInit() {
    final RichTextArea richTextArea = new RichTextArea();
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            richTextArea.setText("foo");
            assertEquals("foo", richTextArea.getText());
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
  }

  /**
   * Test that an immediate set of text is reflected immediately and after the area loads. Some
   * platforms have timing subtleties that need to be tested.
   */
  public void testSetTextBeforeInit() {
    final RichTextArea richTextArea = new RichTextArea();
    richTextArea.setText("foo");
    delayTestFinish(RICH_TEXT_ASYNC_DELAY);
    richTextArea.addInitializeHandler(
        new InitializeHandler() {
          @Override
          public void onInitialize(InitializeEvent event) {
            assertEquals("foo", richTextArea.getText());
            finishTest();
          }
        });
    RootPanel.get().add(richTextArea);
    assertEquals("foo", richTextArea.getText());
  }

  /**
   * Get the body element from a RichTextArea.
   *
   * @param rta the {@link RichTextArea}
   * @return the body element
   */
  private Element getBodyElement(RichTextArea rta) {
    return Js.uncheckedCast(getDocument(rta).getBody());
  }

  /**
   * Get the iframe's Document. This is useful for creating events, which must be created in the
   * iframe's document to work correctly.
   *
   * @param rta the {@link RichTextArea}
   * @return the document element
   */
  private Document getDocument(RichTextArea rta) {
    return getDocumentImpl(rta.getElement());
  }

  private Document getDocumentImpl(Element iframe) {
    HTMLIFrameElement frameElement = Js.uncheckedCast(iframe);
    return Js.uncheckedCast(Js.asPropertyMap(frameElement.contentWindow).get("document"));
  }
}
