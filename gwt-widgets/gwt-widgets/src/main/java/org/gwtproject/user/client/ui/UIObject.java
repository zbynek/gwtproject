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

import java.util.Locale;

import elemental2.dom.DOMTokenList;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import jsinterop.base.Js;
import org.gwtproject.core.client.JavaScriptObject;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;

/**
 * The superclass for all user-interface objects. It simply wraps a DOM element, and cannot receive
 * events. Most interesting user-interface classes derive from {@link Widget}.
 *
 * <h3>Styling With CSS</h3>
 *
 * <p>All <code>UIObject</code> objects can be styled using CSS. Style names that are specified
 * programmatically in Java source are implicitly associated with CSS style rules. In terms of HTML
 * and CSS, a GWT style name is the element's CSS "class". By convention, GWT style names are of the
 * form <code>[project]-[widget]</code>.
 *
 * <p>For example, the {@link Button} widget has the style name <code>gwt-Button</code>, meaning
 * that within the <code>Button</code> constructor, the following call occurs:
 *
 * <pre class="code">
 * setStyleName("gwt-Button");</pre>
 *
 * <p>A corresponding CSS style rule can then be written as follows:
 *
 * <pre class="code">
 * // Example of how you might choose to style a Button widget
 * .gwt-Button {
 *   background-color: yellow;
 *   color: black;
 *   font-size: 24pt;
 * }</pre>
 *
 * <p>Note the dot prefix in the CSS style rule. This syntax is called a <a
 * href="http://www.w3.org/TR/REC-CSS2/selector.html#class-html">CSS class selector</a>.
 *
 * <h3>Style Name Specifics</h3>
 *
 * <p>Every <code>UIObject</code> has a <i>primary style name</i> that identifies the key CSS style
 * rule that should always be applied to it. Use {@link #setStylePrimaryName(String)} to specify an
 * object's primary style name. In most cases, the primary style name is set in a widget's
 * constructor and never changes again during execution. In the case that no primary style name is
 * specified, it defaults to the first style name that is added.
 *
 * <p>More complex styling behavior can be achieved by manipulating an object's <i>secondary style
 * names</i>. Secondary style names can be added and removed using {@link #addStyleName(String)},
 * {@link #removeStyleName(String)}, or {@link #setStyleName(String, boolean)}. The purpose of
 * secondary style names is to associate a variety of CSS style rules over time as an object
 * progresses through different visual states.
 *
 * <p>There is an important special formulation of secondary style names called <i>dependent style
 * names</i>. A dependent style name is a secondary style name prefixed with the primary style name
 * of the widget itself. See {@link #addStyleName(String)} for details.
 *
 */
public abstract class UIObject implements HasVisibility {


  /*
   * WARNING: For historical reasons, there are two Element classes being used
   * in this code. The dom.Element (org.gwtproject.dom.client.Element) class is
   * explicitly imported, while user.Element
   * (Element) is fully-qualified in the code.
   *
   * All new methods should use dom.Element, because user.Element extends it but
   * adds no methods.
   */
  static final String MISSING_ELEMENT_ERROR =
      "This UIObject's element is not set; "
          + "you may be missing a call to either Composite.initWidget() or "
          + "UIObject.setElement()";
  static final String SETELEMENT_TWICE_ERROR = "Element may only be set once";
  private static final String EMPTY_STYLENAME_MSG = "Style names cannot be empty";
  private static final String NULL_HANDLE_MSG =
      "Null widget handle. If you "
          + "are creating a composite, ensure that initWidget() has been called.";
  /**
   * Stores a regular expression object to extract float values from the leading portion of an input
   * string.
   */
  private static JavaScriptObject numberRegex;

  private static DebugIdImpl debugIdImpl = new DebugIdImpl();
  private HTMLElement element;

  /**
   * Ensure that elem has an ID property set, which allows it to integrate with third-party
   * libraries and test tools. If elem already has an ID, this method WILL override it.
   *
   * <p>This method will be compiled out and will have no effect unless you inherit the DebugID
   * module in your gwt.xml file by adding the following line:
   *
   * <pre class="code">
   * &lt;inherits name="org.gwtproject.user.Debug"/&gt;</pre>
   *
   * @param elem the target {@link HTMLElement}
   * @param id the ID to set on the element
   */
  public static void ensureDebugId(HTMLElement elem, String id) {
    ensureDebugId(elem, "", id);
  }

  /**
   * Returns whether the given element is visible in a way consistent with {@link
   * #setVisible(HTMLElement, boolean)}.
   *
   * <p>Warning: implemented with a heuristic. The value returned takes into account only the
   * "display" style, ignoring CSS and Aria roles, thus may not accurately reflect whether the
   * element is actually visible in the browser.
   */
  public static boolean isVisible(HTMLElement elem) {
    return !"none".equals(elem.style.display);
  }

  /**
   * Shows or hides the given element. Also updates the "aria-hidden" attribute by setting it to
   * true when it is hidden and removing it when it is shown.
   *
   * <p>Warning: implemented with a heuristic based on the "display" style: clears the "display"
   * style to its default value if {@code visible} is true, else forces the style to "none". If the
   * "display" style is set to "none" via CSS style sheets, the element remains invisible after a
   * call to {@code setVisible(elem, true)}.
   */
  public static void setVisible(HTMLElement elem, boolean visible) {
    if (visible) {
      elem.style.display = null;
    } else {
      elem.style.display = "none";
    }

    if (visible) {
      elem.removeAttribute("aria-hidden");
    } else {
      elem.setAttribute("aria-hidden", "true");
    }
  }

  /**
   * Set the debug id of a specific element. The id will be appended to the end of the base debug
   * id, with a dash separator. The base debug id is the ID of the main element in this UIObject.
   *
   * @param elem the element
   * @param baseID the base ID used by the main element
   * @param id the id to append to the base debug id
   */
  protected static void ensureDebugId(HTMLElement elem, String baseID, String id) {
    debugIdImpl.ensureDebugId(elem, baseID, id);
  }

  /**
   * Gets all of the element's style names, as a space-separated list.
   *
   * @param elem the element whose style is to be retrieved
   * @return the objects's space-separated style names
   */
  protected static String getStyleName(HTMLElement elem) {
    return elem.className;
  }

  /**
   * Gets the element's primary style name.
   *
   * @param elem the element whose primary style name is to be retrieved
   * @return the element's primary style name
   */
  protected static String getStylePrimaryName(HTMLElement elem) {
    String fullClassName = getStyleName(elem);

    // The primary style name is always the first token of the full CSS class
    // name. There can be no leading whitespace in the class name, so it's not
    // necessary to trim() it.
    int spaceIdx = fullClassName.indexOf(' ');
    if (spaceIdx >= 0) {
      return fullClassName.substring(0, spaceIdx);
    }
    return fullClassName;
  }

  /**
   * Clears all of the element's style names and sets it to the given style.
   *
   * @param elem the element whose style is to be modified
   * @param styleName the new style name
   */
  protected static void setStyleName(HTMLElement elem, String styleName) {
    elem.className = styleName;
  }

  /**
   * This convenience method adds or removes a style name for a given element. This method is
   * typically used to add and remove secondary style names, but it can be used to remove primary
   * stylenames as well, but that is not recommended. See {@link #setStyleName(String)} for a
   * description of how primary and secondary style names are used.
   *
   * @param elem the element whose style is to be modified
   * @param style the secondary style name to be added or removed
   * @param add <code>true</code> to add the given style, <code>false</code> to remove it
   */
  protected static void setStyleName(HTMLElement elem, String style, boolean add) {
    if (elem == null) {
      throw new RuntimeException(NULL_HANDLE_MSG);
    }

    style = style.trim();
    if (style.length() == 0) {
      throw new IllegalArgumentException(EMPTY_STYLENAME_MSG);
    }

    if (add) {
      elem.classList.add(style);
    } else {
      elem.classList.remove(style);
    }
  }

  /**
   * Sets the element's primary style name and updates all dependent style names.
   *
   * @param elem the element whose style is to be reset
   * @param style the new primary style name
   * @see #setStyleName(HTMLElement, String, boolean)
   */
  protected static void setStylePrimaryName(HTMLElement elem, String style) {
    if (elem == null) {
      throw new RuntimeException(NULL_HANDLE_MSG);
    }

    // Style names cannot contain leading or trailing whitespace, and cannot
    // legally be empty.
    style = style.trim();
    if (style.length() == 0) {
      throw new IllegalArgumentException(EMPTY_STYLENAME_MSG);
    }

    updatePrimaryAndDependentStyleNames(elem, style);
  }

  /** Replaces all instances of the primary style name with newPrimaryStyleName. */
  private static void updatePrimaryAndDependentStyleNames(HTMLElement elem, String newPrimaryStyle) {
    DOMTokenList classes = elem.classList;
    if (classes.length == 0) {
      return;
    }
    String oldPrimaryStyle = classes.getAt(0);
    int oldPrimaryStyleLen = oldPrimaryStyle.length();
    classes.replace(oldPrimaryStyle, newPrimaryStyle);
    for (int i = 1, n = classes.length; i < n; i++) {
      String name = classes.getAt(i);
      if (name.length() > oldPrimaryStyleLen
          && name.charAt(oldPrimaryStyleLen) == '-'
          && name.indexOf(oldPrimaryStyle) == 0) {
        classes.replace(name, newPrimaryStyle + name.substring(oldPrimaryStyleLen));
      }
    }
  }

  /**
   * Adds a dependent style name by specifying the style name's suffix. The actual form of the style
   * name that is added is:
   *
   * <pre class="code">
   * getStylePrimaryName() + '-' + styleSuffix
   * </pre>
   *
   * @param styleSuffix the suffix of the dependent style to be added.
   * @see #setStylePrimaryName(String)
   * @see #removeStyleDependentName(String)
   * @see #setStyleDependentName(String, boolean)
   * @see #addStyleName(String)
   */
  public void addStyleDependentName(String styleSuffix) {
    setStyleDependentName(styleSuffix, true);
  }

  /**
   * Adds a secondary or dependent style name to this object. A secondary style name is an
   * additional style name that is, in HTML/CSS terms, included as a space-separated token in the
   * value of the CSS <code>class</code> attribute for this object's root element.
   *
   * <p>The most important use for this method is to add a special kind of secondary style name
   * called a <i>dependent style name</i>. To add a dependent style name, use {@link
   * #addStyleDependentName(String)}, which will prefix the 'style' argument with the result of
   * {@link #getStylePrimaryName()} (followed by a '-'). For example, suppose the primary style name
   * is <code>gwt-TextBox</code>. If the following method is called as <code>obj.setReadOnly(true)
   * </code>:
   *
   * <pre class="code">
   * public void setReadOnly(boolean readOnly) {
   *   isReadOnlyMode = readOnly;
   *
   *   // Create a dependent style name.
   *   String readOnlyStyle = "readonly";
   *
   *   if (readOnly) {
   *     addStyleDependentName(readOnlyStyle);
   *   } else {
   *     removeStyleDependentName(readOnlyStyle);
   *   }
   * }</pre>
   *
   * <p>then both of the CSS style rules below will be applied:
   *
   * <pre class="code">
   *
   * // This rule is based on the primary style name and is always active.
   * .gwt-TextBox {
   *   font-size: 12pt;
   * }
   *
   * // This rule is based on a dependent style name that is only active
   * // when the widget has called addStyleName(getStylePrimaryName() +
   * // "-readonly").
   * .gwt-TextBox-readonly {
   *   background-color: lightgrey;
   *   border: none;
   * }</pre>
   *
   * <p>The code can also be simplified with {@link #setStyleDependentName(String, boolean)}:
   *
   * <pre class="code">
   * public void setReadOnly(boolean readOnly) {
   *   isReadOnlyMode = readOnly;
   *   setStyleDependentName("readonly", readOnly);
   * }</pre>
   *
   * <p>Dependent style names are powerful because they are automatically updated whenever the
   * primary style name changes. Continuing with the example above, if the primary style name
   * changed due to the following call:
   *
   * <pre class="code">setStylePrimaryName("my-TextThingy");</pre>
   *
   * <p>then the object would be re-associated with following style rules, removing those that were
   * shown above.
   *
   * <pre class="code">
   * .my-TextThingy {
   *   font-size: 20pt;
   * }
   *
   * .my-TextThingy-readonly {
   *   background-color: red;
   *   border: 2px solid yellow;
   * }</pre>
   *
   * <p>Secondary style names that are not dependent style names are not automatically updated when
   * the primary style name changes.
   *
   * @param style the secondary style name to be added
   * @see UIObject
   * @see #removeStyleName(String)
   */
  public void addStyleName(String style) {
    setStyleName(style, true);
  }

  /**
   * Ensure that the main {@link HTMLElement} for this {@link UIObject} has an ID property set, which
   * allows it to integrate with third-party libraries and test tools. Complex {@link Widget}s will
   * also set the IDs of their important sub-elements.
   *
   * <p>If the main element already has an ID, this method WILL override it.
   *
   * <p>This method will be compiled out and will have no effect unless you inherit the DebugID
   * module in your gwt.xml file by adding the following line:
   *
   * <pre class="code">
   * &lt;inherits name="org.gwtproject.user.Debug"/&gt;</pre>
   *
   * @param id the ID to set on the main element
   */
  public final void ensureDebugId(String id) {
    debugIdImpl.ensureDebugId(this, id);
  }

  /**
   * Gets the object's absolute left position in pixels, as measured from the browser window's
   * client area.
   *
   * @return the object's absolute left position
   */
  public int getAbsoluteLeft() {
    return DOM.getAbsoluteLeft(getElement());
  }

  /**
   * Gets the object's absolute top position in pixels, as measured from the browser window's client
   * area.
   *
   * @return the object's absolute top position
   */
  public int getAbsoluteTop() {
    return DOM.getAbsoluteTop(getElement());
  }

  /**
   * Gets a handle to the object's underlying DOM element.
   *
   * <p>This method should not be overridden. It is non-final solely to support legacy code that
   * depends upon overriding it. If it is overridden, the subclass implementation must not return a
   * different element than was previously set using {@link #setElement(HTMLElement)}.
   *
   * @return the object's browser element
   */
  public HTMLElement getElement() {
    assert (element != null) : MISSING_ELEMENT_ERROR;
    return element;
  }

  /**
   * Sets this object's browser element. UIObject subclasses must call this method before attempting
   * to call any other methods, and it may only be called once.
   *
   * @param elem the object's element
   */
  protected final void setElement(HTMLElement elem) {
    this.element = elem;
  }

  /**
   * Gets the object's offset height in pixels. This is the total height of the object, including
   * decorations such as border and padding, but not margin.
   *
   * @return the object's offset height
   */
  public int getOffsetHeight() {
    return getElement().offsetHeight;
  }

  /**
   * Gets the object's offset width in pixels. This is the total width of the object, including
   * decorations such as border and padding, but not margin.
   *
   * @return the object's offset width
   */
  public int getOffsetWidth() {
    return getElement().offsetWidth;
  }

  /**
   * Gets all of the object's style names, as a space-separated list. If you wish to retrieve only
   * the primary style name, call {@link #getStylePrimaryName()}.
   *
   * @return the objects's space-separated style names
   * @see #getStylePrimaryName()
   */
  public String getStyleName() {
    return getStyleName(getStyleElement());
  }

  /**
   * Clears all of the object's style names and sets it to the given style. You should normally use
   * {@link #setStylePrimaryName(String)} unless you wish to explicitly remove all existing styles.
   *
   * @param style the new style name
   * @see #setStylePrimaryName(String)
   */
  public void setStyleName(String style) {
    setStyleName(getStyleElement(), style);
  }

  /**
   * Gets the primary style name associated with the object.
   *
   * @return the object's primary style name
   * @see #setStyleName(String)
   * @see #addStyleName(String)
   * @see #removeStyleName(String)
   */
  public String getStylePrimaryName() {
    return getStylePrimaryName(getStyleElement());
  }

  /**
   * Sets the object's primary style name and updates all dependent style names.
   *
   * @param style the new primary style name
   * @see #addStyleName(String)
   * @see #removeStyleName(String)
   */
  public void setStylePrimaryName(String style) {
    setStylePrimaryName(getStyleElement(), style);
  }

  /**
   * Gets the title associated with this object. The title is the 'tool-tip' displayed to users when
   * they hover over the object.
   *
   * @return the object's title
   */
  public String getTitle() {
    return getElement().title;
  }

  /**
   * Sets the title associated with this object. The title is the 'tool-tip' displayed to users when
   * they hover over the object.
   *
   * @param title the object's new title
   */
  public void setTitle(String title) {
    if (title == null || title.length() == 0) {
      getElement().removeAttribute("title");
    } else {
      getElement().setAttribute("title", title);
    }
  }

  @Override
  public boolean isVisible() {
    return isVisible(getElement());
  }

  @Override
  public void setVisible(boolean visible) {
    setVisible(getElement(), visible);
  }

  /**
   * Removes a dependent style name by specifying the style name's suffix.
   *
   * @param styleSuffix the suffix of the dependent style to be removed
   * @see #setStylePrimaryName(HTMLElement, String)
   * @see #addStyleDependentName(String)
   * @see #setStyleDependentName(String, boolean)
   */
  public void removeStyleDependentName(String styleSuffix) {
    setStyleDependentName(styleSuffix, false);
  }

  /**
   * Removes a style name. This method is typically used to remove secondary style names, but it can
   * be used to remove primary stylenames as well. That use is not recommended.
   *
   * @param style the secondary style name to be removed
   * @see #addStyleName(String)
   * @see #setStyleName(String, boolean)
   */
  public void removeStyleName(String style) {
    setStyleName(style, false);
  }

  /**
   * Sets the object's height. This height does not include decorations such as border, margin, and
   * padding.
   *
   * @param height the object's new height, in CSS units (e.g. "10px", "1em")
   */
  public void setHeight(String height) {
    // This exists to deal with an inconsistency in IE's implementation where
    // it won't accept negative numbers in length measurements
    assert extractLengthValue(height.trim().toLowerCase(Locale.ROOT)) >= 0
        : "CSS heights should not be negative";
    getElement().style.setProperty("height", height);
  }

  /**
   * Sets the object's size, in pixels, not including decorations such as border, margin, and
   * padding.
   *
   * @param width the object's new width, in pixels
   * @param height the object's new height, in pixels
   */
  public void setPixelSize(int width, int height) {
    if (width >= 0) {
      setWidth(width + "px");
    }
    if (height >= 0) {
      setHeight(height + "px");
    }
  }

  /**
   * Sets the object's size. This size does not include decorations such as border, margin, and
   * padding.
   *
   * @param width the object's new width, in CSS units (e.g. "10px", "1em")
   * @param height the object's new height, in CSS units (e.g. "10px", "1em")
   */
  public void setSize(String width, String height) {
    setWidth(width);
    setHeight(height);
  }

  /**
   * Adds or removes a dependent style name by specifying the style name's suffix. The actual form
   * of the style name that is added is:
   *
   * <pre class="code">
   * getStylePrimaryName() + '-' + styleSuffix
   * </pre>
   *
   * @param styleSuffix the suffix of the dependent style to be added or removed
   * @param add <code>true</code> to add the given style, <code>false</code> to remove it
   * @see #setStylePrimaryName(HTMLElement, String)
   * @see #addStyleDependentName(String)
   * @see #setStyleName(String, boolean)
   * @see #removeStyleDependentName(String)
   */
  public void setStyleDependentName(String styleSuffix, boolean add) {
    setStyleName(getStylePrimaryName() + '-' + styleSuffix, add);
  }

  /**
   * Adds or removes a style name. This method is typically used to remove secondary style names,
   * but it can be used to remove primary stylenames as well. That use is not recommended.
   *
   * @param style the style name to be added or removed
   * @param add <code>true</code> to add the given style, <code>false</code> to remove it
   * @see #addStyleName(String)
   * @see #removeStyleName(String)
   */
  public void setStyleName(String style, boolean add) {
    setStyleName(getStyleElement(), style, add);
  }

  /**
   * Sets the object's width. This width does not include decorations such as border, margin, and
   * padding.
   *
   * @param width the object's new width, in CSS units (e.g. "10px", "1em")
   */
  public void setWidth(String width) {
    // This exists to deal with an inconsistency in IE's implementation where
    // it won't accept negative numbers in length measurements
    assert extractLengthValue(width.trim().toLowerCase(Locale.ROOT)) >= 0
        : "CSS widths should not be negative";
    getElement().style.setProperty("width", width);
  }

  /**
   * Sinks a named event. Note that only {@link Widget widgets} may actually receive events, but can
   * receive events from all objects contained within them.
   *
   * @param eventTypeName name of the event to sink on this element
   * @see Event
   */
  public void sinkBitlessEvent(String eventTypeName) {
    DOM.sinkBitlessEvent(getElement(), eventTypeName);
  }

  /**
   * Adds a set of events to be sunk by this object. Note that only {@link Widget widgets} may
   * actually receive events, but can receive events from all objects contained within them.
   *
   * @param eventBitsToAdd a bitfield representing the set of events to be added to this element's
   *     event set
   * @see Event
   */
  public void sinkEvents(int eventBitsToAdd) {
    DOM.sinkEvents(getElement(), eventBitsToAdd | DOM.getEventsSunk(getElement()));
  }

  /**
   * This method is overridden so that any object can be viewed in the debugger as an HTML snippet.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    if (element == null) {
      return "(null handle)";
    }
    return getElement().outerHTML;
  }

  /**
   * Removes a set of events from this object's event list.
   *
   * @param eventBitsToRemove a bitfield representing the set of events to be removed from this
   *     element's event set
   * @see #sinkEvents
   * @see Event
   */
  public void unsinkEvents(int eventBitsToRemove) {
    DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) & (~eventBitsToRemove));
  }

  /**
   * Template method that returns the element to which style names will be applied. By default it
   * returns the root element, but this method may be overridden to apply styles to a child element.
   *
   * @return the element to which style names will be applied
   */
  protected HTMLElement getStyleElement() {
    return getElement();
  }

  /**
   * Called when the user sets the id using the {@link #ensureDebugId(String)} method. Subclasses of
   * {@link UIObject} can override this method to add IDs to their sub elements. If a subclass does
   * override this method, it should list the IDs (relative to the base ID), that will be applied to
   * each sub {@link HTMLElement} with a short description. For example:
   *
   * <ul>
   *   <li>-mysubelement = Applies to my sub element.
   * </ul>
   *
   * <p>Subclasses should make a super call to this method to ensure that the ID of the main element
   * is set.
   *
   * <p>This method will not be called unless you inherit the DebugID module in your gwt.xml file by
   * adding the following line:
   *
   * <pre class="code">
   * &lt;inherits name="org.gwtproject.user.Debug"/&gt;</pre>
   *
   * @param baseID the base ID used by the main element
   */
  protected void onEnsureDebugId(String baseID) {
    ensureDebugId(getElement(), "", baseID);
  }

  /**
   * EXPERIMENTAL and subject to change. Do not use this in production code.
   *
   * <p>To be overridden by {@link IsRenderable} subclasses that initialize themselves by by calling
   * <code>setElement(PotentialElement.build(this))</code>.
   *
   * <p>The receiver must:
   *
   * <ul>
   *   <li>create a real {@link HTMLElement} to replace its {@link PotentialElement}
   *   <li>call {@link #setElement(HTMLElement)} with the new Element
   *   <li>and return the new Element
   * </ul>
   *
   * <p>This method is called when the receiver's element is about to be added to a parent node, as
   * a side effect of {@link DOM#appendChild}.
   *
   * <p>Note that this method is normally called only on the top element of an IsRenderable tree.
   * Children instead will receive {@link IsRenderable#render} and {@link
   * IsRenderable#claimElement(HTMLElement)}.
   *
   * @see PotentialElement
   * @see IsRenderable
   */
  protected HTMLElement resolvePotentialElement() {
    throw new UnsupportedOperationException("resolvePotentialElement");
  }

  /**
   * Replaces this object's browser element.
   *
   * <p>This method exists only to support a specific use-case in Image, and should not be used by
   * other classes.
   *
   * @param elem the object's new element
   */
  void replaceElement(HTMLElement elem) {
    if (element != null) {
      // replace this.element in its parent with elem.
      replaceNode(element, elem);
    }

    this.element = elem;
  }

  /**
   * Intended to be used to pull the value out of a CSS length. If the value is "auto" or "inherit",
   * 0 will be returned.
   *
   * @param s The CSS length string to extract
   * @return The leading numeric portion of <code>s</code>, or 0 if "auto" or "inherit" are passed
   *     in.
   */
  private double extractLengthValue(String s) {
    if (s.equals("auto") || s.equals("inherit") || s.equals("")) {
      return 0;
    } else {
      return Float.valueOf(s.replaceAll("[A-Za-z%\\s]+", ""));
    }
  }

  private void replaceNode(HTMLElement node, HTMLElement newNode) {
    Node p = node.parentNode;
    if (p == null) {
      return;
    }
    p.insertBefore(newNode, node);
    p.removeChild(node);
  }

  /** The implementation of the set debug id method, which does nothing by default. */
  public static class DebugIdImpl {

    @SuppressWarnings("unused")
    // parameters
    public void ensureDebugId(UIObject uiObject, String id) {}

    @SuppressWarnings("unused")
    // parameters
    public void ensureDebugId(HTMLElement elem, String baseID, String id) {}
  }

  public void assertTagName(String tagName) {
    assert tagName.equalsIgnoreCase(element.tagName);
  }
}
