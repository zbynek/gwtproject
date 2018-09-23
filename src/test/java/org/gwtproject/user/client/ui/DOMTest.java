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
package org.gwtproject.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import org.gwtproject.dom.client.BodyElement;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.dom.client.Text;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import org.gwtproject.user.client.ui.Button;
import org.gwtproject.user.client.ui.RootPanel;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Tests standard DOM operations in the {@link DOM} class.
 */
public class DOMTest extends GWTTestCase {

  public static void assertEndsWith(String ending, String testStr) {
    if (ending != testStr && (testStr == null || !testStr.endsWith(ending))) {
      fail("expected ending=" + ending + " actual=" + testStr);
    }
  }

  /**
   * Helper method to return the denormalized child count of a DOM Element. For
   * example, child nodes which have a nodeType of Text are included in the
   * count, whereas <code>DOM.getChildCount(Element parent)</code> only counts
   * the child nodes which have a nodeType of Element.
   * 
   * @param elem the DOM element to check the child count for
   * @return The number of child nodes
   */
  public static native int getDenormalizedChildCount(Element elem) /*-{
    return (elem.childNodes.length);
  }-*/;

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.UserTest";
  }

  /**
   * Test DOM.get/set/removeElementAttribute() methods.
   */
  public void testElementAttribute() {
    Element div = DOM.createDiv();
    DOM.setElementAttribute(div, "class", "testClass");
    String cssClass = DOM.getElementAttribute(div, "class");
    assertEquals("testClass", cssClass);
    DOM.removeElementAttribute(div, "class");
    cssClass = DOM.getElementAttribute(div, "class");
    assertEquals("", cssClass);
  }

  @Override
  protected void reportUncaughtException(Throwable ex) {
    if (!ex.getMessage().contains("_expected_")) {
      super.reportUncaughtException(ex);
    }
  }

  /**
   * Tests that {@link DOM#eventGetCurrentEvent()} returns the event to the
   * {@link UncaughtExceptionHandler}.
   */
  public void testEventGetCurrentEventOnException() {
    org.gwtproject.user.client.ui.Button button = new org.gwtproject.user.client.ui.Button("test", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        // Intentionally trigger an error
        throw new IllegalArgumentException("_expected_");
      }
    });
    org.gwtproject.user.client.ui.RootPanel.get().add(button);

    // Verify the exception is captured
    final List<String> ret = new ArrayList<String>();
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable e) {
        Event event = DOM.eventGetCurrentEvent();
        if (event == null) {
          ret.add("Event is null");
          return;
        }
        if (event.getTypeInt() != Event.ONCLICK) {
          ret.add("Event is not a click event");
          return;
        }
        ret.add("Success");
      }
    });
    NativeEvent clickEvent = Document.get().createClickEvent(0, 0, 0, 0, 0,
        false, false, false, false);
    button.getElement().dispatchEvent(clickEvent);

    assertEquals(1, ret.size());
    assertEquals("Success", ret.get(0));
    org.gwtproject.user.client.ui.RootPanel.get().remove(button);
  }

  /**
   * Tests {@link DOM#getAbsoluteLeft(Element)} and
   * {@link DOM#getAbsoluteTop(Element)}.
   */
  public void testGetAbsolutePosition() {
    final int border = 8;
    final int margin = 9;
    final int padding = 10;

    final int top = 15;
    final int left = 14;

    final Element elem = DOM.createDiv();
    DOM.appendChild(org.gwtproject.user.client.ui.RootPanel.getBodyElement(), elem);

    DOM.setStyleAttribute(elem, "position", "absolute");
    DOM.setStyleAttribute(elem, "border", border + "px solid #000");
    DOM.setStyleAttribute(elem, "padding", padding + "px");
    DOM.setStyleAttribute(elem, "margin", margin + "px");

    Document doc = Document.get();
    DOM.setStyleAttribute(elem, "top", (top - doc.getBodyOffsetLeft()) + "px");
    DOM.setStyleAttribute(elem, "left", (left - doc.getBodyOffsetTop()) + "px");

    delayTestFinish(1000);
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
      public void execute() {
          assertEquals(top + margin, DOM.getAbsoluteTop(elem));
          assertEquals(left + margin, DOM.getAbsoluteLeft(elem));
          finishTest();
      }
    });
  }

  /**
   * Tests {@link DOM#getAbsoluteTop(Element)} and
   * {@link DOM#getAbsoluteLeft(Element)} for consistency when the element
   * contains children and has scrollbars. See issue #1093 for more details.
   * 
   */
  @DoNotRunWith(Platform.HtmlUnitLayout)
  public void testGetAbsolutePositionWhenScrolled() {
    final Element outer = DOM.createDiv();
    final Element inner = DOM.createDiv();

    DOM.setStyleAttribute(outer, "position", "absolute");
    DOM.setStyleAttribute(outer, "top", "0px");
    DOM.setStyleAttribute(outer, "left", "0px");
    DOM.setStyleAttribute(outer, "overflow", "auto");
    DOM.setStyleAttribute(outer, "width", "200px");
    DOM.setStyleAttribute(outer, "height", "200px");

    DOM.setStyleAttribute(inner, "marginTop", "800px");
    DOM.setStyleAttribute(inner, "marginLeft", "800px");

    DOM.appendChild(outer, inner);
    DOM.appendChild(org.gwtproject.user.client.ui.RootPanel.getBodyElement(), outer);
    DOM.setInnerText(inner, ":-)");
    DOM.scrollIntoView(inner);

    // Ensure that we are scrolled.
    assertTrue(DOM.getElementPropertyInt(outer, "scrollTop") > 0);
    assertTrue(DOM.getElementPropertyInt(outer, "scrollLeft") > 0);

    Document doc = Document.get();
    assertEquals(doc.getBodyOffsetLeft(), DOM.getAbsoluteLeft(outer));
    assertEquals(doc.getBodyOffsetTop(), DOM.getAbsoluteTop(outer));
  }

  /**
   * Tests {@link DOM#getAbsoluteTop(Element)} and
   * {@link DOM#getAbsoluteLeft(Element)} for consistency when the parent
   * element has a border.
   * 
   */
  public void testGetAbsolutePositionWithPixelBorders() {
    final Element outer = DOM.createDiv();
    final Element inner = DOM.createDiv();

    outer.getStyle().setProperty("position", "relative");
    outer.getStyle().setProperty("width", "200px");
    outer.getStyle().setProperty("height", "200px");

    inner.getStyle().setProperty("position", "absolute");
    inner.getStyle().setProperty("top", "30px");
    inner.getStyle().setProperty("left", "40px");
    inner.setInnerText("inner");

    outer.appendChild(inner);
    org.gwtproject.user.client.ui.RootPanel.getBodyElement().appendChild(outer);

    // Get the position without a border
    int absTop = inner.getAbsoluteTop();
    int absLeft = inner.getAbsoluteLeft();

    // Get the position with a border
    outer.getStyle().setProperty("border", "2px solid blue");
    assertEquals(2, inner.getAbsoluteTop() - absTop);
    assertEquals(2, inner.getAbsoluteLeft() - absLeft);
  }

  /**
   * Tests getAbsoluteLeft/Top() for the document.body element. This used to
   * cause exceptions to be thrown on Opera (see issue 1556).
   */
  public void testGetBodyAbsolutePosition() {
    try {
      // The body's absolute left/top depends upon the browser, but we just
      // need to make sure nothing goes wrong reading them.
      BodyElement body = Document.get().getBody();
      body.getAbsoluteLeft();
      body.getAbsoluteTop();
    } catch (Throwable e) {
      fail("Exception occurred getting the absolute position of the body");
    }
  }

  /**
   * Tests the ability to do a parent-ward walk in the DOM.
   */
  public void testGetParent() {
    Element element = org.gwtproject.user.client.ui.RootPanel.get().getElement();
    int i = 0;
    while (i < 10 && element != null) {
      element = DOM.getParent(element);
      i++;
    }
    // If we got here we looped "forever" or passed, as no exception was thrown.
    if (i == 10) {
      fail("Cyclic parent structure detected.");
    }
    // If we get here, we pass, because we encountered no errors going to the
    // top of the parent hierarchy.
  }

  /**
   * Tests {@link DOM#insertChild(Element, Element, int)}.
   * 
   */
  public void testInsertChild() {
    Element parent = org.gwtproject.user.client.ui.RootPanel.get().getElement();
    Element div = DOM.createDiv();
    DOM.insertChild(parent, div, Integer.MAX_VALUE);
    Element child = DOM.getChild(org.gwtproject.user.client.ui.RootPanel.get().getElement(),
        DOM.getChildCount(parent) - 1);
    assertTrue(div == child);
  }

  /**
   * Tests that {@link DOM#isOrHasChild(Element, Element)} works consistently
   * across browsers.
   */
  public void testIsOrHasChild() {
    Document doc = Document.get();
    Element div = DOM.createDiv();
    Element childDiv = DOM.createDiv();
    Text text = Document.get().createTextNode("text");

    // unattached, not related
    assertFalse(div.isOrHasChild(childDiv));
    assertFalse(div.isOrHasChild(text));
    assertTrue(div.isOrHasChild(div));
    assertTrue(text.isOrHasChild(text));
    assertFalse(doc.isOrHasChild(div));
    assertFalse(doc.isOrHasChild(text));
    assertFalse(div.isOrHasChild(doc));
    assertFalse(text.isOrHasChild(doc));

    // unattached, related
    div.appendChild(childDiv);
    childDiv.appendChild(text);
    assertTrue(div.isOrHasChild(childDiv));
    assertTrue(childDiv.isOrHasChild(text));
    assertFalse(childDiv.isOrHasChild(div));
    assertFalse(text.isOrHasChild(childDiv));
    assertFalse(doc.isOrHasChild(div));
    assertFalse(doc.isOrHasChild(text));
    assertFalse(div.isOrHasChild(doc));
    assertFalse(text.isOrHasChild(doc));

    // attached, related
    DOM.appendChild(org.gwtproject.user.client.ui.RootPanel.getBodyElement(), div);
    assertTrue(div.isOrHasChild(childDiv));
    assertTrue(childDiv.isOrHasChild(text));
    assertTrue(div.isOrHasChild(div));
    assertTrue(text.isOrHasChild(text));
    assertFalse(childDiv.isOrHasChild(div));
    assertFalse(text.isOrHasChild(childDiv));
    assertTrue(doc.isOrHasChild(div));
    assertTrue(doc.isOrHasChild(text));
    assertFalse(div.isOrHasChild(Document.get()));
    assertFalse(text.isOrHasChild(Document.get()));
  }

  /**
   * Tests that {@link DOM#setInnerText(Element, String)} works consistently
   * across browsers.
   */
  public void testSetInnerText() {
    Element tableElem = DOM.createTable();

    Element trElem = DOM.createTR();

    Element tdElem = DOM.createTD();
    DOM.setInnerText(tdElem, "Some Table Heading Data");

    // Add a <em> element as a child to the td element
    Element emElem = DOM.createElement("em");
    DOM.setInnerText(emElem, "Some emphasized text");
    DOM.appendChild(tdElem, emElem);

    DOM.appendChild(trElem, tdElem);

    DOM.appendChild(tableElem, trElem);

    DOM.appendChild(RootPanel.getBodyElement(), tableElem);

    DOM.setInnerText(tdElem, null);

    // Once we set the inner text on an element to null, all of the element's
    // child nodes
    // should be deleted, including any text nodes, for all supported browsers.
    assertTrue(getDenormalizedChildCount(tdElem) == 0);
  }

  /**
   * Tests {@link DOM#toString(Element)} against likely failure points.
   */
  public void testToString() {
    org.gwtproject.user.client.ui.Button b = new Button("abcdef");
    assertTrue(b.toString().indexOf("abcdef") != -1);
    assertTrue(b.toString().toLowerCase(Locale.ROOT).indexOf("button") != -1);

    // Test <img src="http://.../logo.gif" />
    Element image = DOM.createImg();
    String imageUrl = "http://www.google.com/images/logo.gif";
    DOM.setImgSrc(image, imageUrl);
    String imageToString = DOM.toString(image).trim().toLowerCase(Locale.ROOT);
    assertTrue(imageToString.startsWith("<img"));
    assertTrue(imageToString.indexOf(imageUrl) != -1);

    // Test <input name="flinks" />
    Element input = DOM.createInputText();
    DOM.setElementProperty(input, "name", "flinks");
    final String inputToString = DOM.toString(input).trim().toLowerCase(Locale.ROOT);
    assertTrue(inputToString.startsWith("<input"));

    // Test <select><option>....</select>
    Element select = DOM.createSelect();
    for (int i = 0; i < 10; i++) {
      final Element option = DOM.createElement("option");
      DOM.appendChild(select, option);
      DOM.setInnerText(option, "item #" + i);
    }
    String selectToString = DOM.toString(select).trim().toLowerCase(Locale.ROOT);
    assertTrue(selectToString.startsWith("<select"));
    for (int i = 0; i < 10; i++) {
      assertTrue(selectToString.indexOf("item #" + i) != -1);
    }

    // Test <meta name="robots" />
    Element meta = DOM.createElement("meta");
    DOM.setElementProperty(meta, "name", "robots");
    String metaToString = DOM.toString(meta).trim().toLowerCase(Locale.ROOT);
    assertTrue(metaToString.startsWith("<meta"));
  }
}
