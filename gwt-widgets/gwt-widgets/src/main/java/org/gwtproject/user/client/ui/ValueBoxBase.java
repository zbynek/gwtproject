/*
 * Copyright 2009 Google Inc.
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

import java.text.ParseException;
import org.gwtproject.dom.client.Element;
import org.gwtproject.editor.client.IsEditor;
import org.gwtproject.editor.ui.client.adapters.ValueBoxEditor;
import org.gwtproject.event.dom.client.ChangeEvent;
import org.gwtproject.event.dom.client.ChangeHandler;
import org.gwtproject.event.dom.client.HasChangeHandlers;
import org.gwtproject.event.logical.shared.ValueChangeEvent;
import org.gwtproject.event.logical.shared.ValueChangeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.i18n.client.AutoDirectionHandler;
import org.gwtproject.i18n.client.BidiPolicy;
import org.gwtproject.i18n.client.BidiUtils;
import org.gwtproject.i18n.client.HasDirection;
import org.gwtproject.i18n.shared.DirectionEstimator;
import org.gwtproject.i18n.shared.HasDirectionEstimator;
import org.gwtproject.text.shared.Parser;
import org.gwtproject.text.shared.Renderer;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.ui.impl.TextBoxImpl;

/**
 * Abstract base class for all text entry widgets.
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * @param <T> the value type
 */
public class ValueBoxBase<T> extends FocusWidget
    implements HasChangeHandlers,
        HasName,
        HasDirectionEstimator,
        HasValue<T>,
        HasText,
        AutoDirectionHandler.Target,
        IsEditor<ValueBoxEditor<T>> {

  private static TextBoxImpl impl = new TextBoxImpl();
  private final AutoDirectionHandler autoDirHandler;
  private final Parser<T> parser;
  private final Renderer<T> renderer;
  private ValueBoxEditor<T> editor;
  private Event currentEvent;
  private boolean valueChangeHandlerInitialized;

  /**
   * Creates a value box that wraps the given browser element handle. This is only used by
   * subclasses.
   *
   * @param elem the browser element to wrap
   */
  protected ValueBoxBase(Element elem, Renderer<T> renderer, Parser<T> parser) {
    super(elem);
    autoDirHandler = AutoDirectionHandler.addTo(this, BidiPolicy.isBidiEnabled());
    this.renderer = renderer;
    this.parser = parser;
  }

  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
    // Initialization code
    if (!valueChangeHandlerInitialized) {
      valueChangeHandlerInitialized = true;
      addChangeHandler(
          event -> {
            ValueChangeEvent.fire(ValueBoxBase.this, getValue());
          });
    }
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * Returns an Editor that is backed by the ValueBoxBase. The default implementation returns {@link
   * ValueBoxEditor#of(ValueBoxBase)}. Subclasses may override this method to provide custom
   * error-handling when using the Editor framework.
   */
  public ValueBoxEditor<T> asEditor() {
    if (editor == null) {
      editor = ValueBoxEditor.of(this);
    }
    return editor;
  }

  /**
   * If a keyboard event is currently being handled on this text box, calling this method will
   * suppress it. This allows listeners to easily filter keyboard input.
   */
  public void cancelKey() {
    if (currentEvent != null) {
      currentEvent.preventDefault();
    }
  }

  /**
   * Gets the current position of the cursor (this also serves as the beginning of the text
   * selection).
   *
   * @return the cursor's position
   */
  public int getCursorPos() {
    return impl.getCursorPos(getElement());
  }

  /**
   * Sets the cursor position.
   *
   * <p>This will only work when the widget is attached to the document and not hidden.
   *
   * @param pos the new cursor position
   */
  public void setCursorPos(int pos) {
    setSelectionRange(pos, 0);
  }

  public HasDirection.Direction getDirection() {
    return BidiUtils.getDirectionOnElement(getElement());
  }

  public void setDirection(HasDirection.Direction direction) {
    BidiUtils.setDirectionOnElement(getElement(), direction);
  }

  /** Gets the direction estimation model of the auto-dir handler. */
  public DirectionEstimator getDirectionEstimator() {
    return autoDirHandler.getDirectionEstimator();
  }

  /** Sets the direction estimation model of the auto-dir handler. */
  public void setDirectionEstimator(DirectionEstimator directionEstimator) {
    autoDirHandler.setDirectionEstimator(directionEstimator);
  }

  public String getName() {
    return getElement().getPropertyString("name");
  }

  public void setName(String name) {
    getElement().setPropertyString("name", name);
  }

  /**
   * Gets the text currently selected within this text box.
   *
   * @return the selected text, or an empty string if none is selected
   */
  public String getSelectedText() {
    int start = getCursorPos();
    if (start < 0) {
      return "";
    }
    int length = getSelectionLength();
    return getText().substring(start, start + length);
  }

  /**
   * Gets the length of the current text selection.
   *
   * @return the text selection length
   */
  public int getSelectionLength() {
    return impl.getSelectionLength(getElement());
  }

  public String getText() {
    return getElement().getPropertyString("value");
  }

  /**
   * Sets this object's text. Note that some browsers will manipulate the text before adding it to
   * the widget. For example, most browsers will strip all <code>\r</code> from the text, except IE
   * which will add a <code>\r</code> before each <code>\n</code>. Use {@link #getText()} to get the
   * text directly from the widget.
   *
   * @param text the object's new text
   */
  public void setText(String text) {
    getElement().setPropertyString("value", text != null ? text : "");
    autoDirHandler.refreshDirection();
  }

  /** Return the parsed value, or null if the field is empty or parsing fails. */
  public T getValue() {
    try {
      return getValueOrThrow();
    } catch (ParseException e) {
      return null;
    }
  }

  public void setValue(T value) {
    setValue(value, false);
  }

  /**
   * Return the parsed value, or null if the field is empty.
   *
   * @throws ParseException if the value cannot be parsed
   */
  public T getValueOrThrow() throws ParseException {
    String text = getText();

    T parseResult = parser.parse(text);

    if ("".equals(text)) {
      return null;
    }

    return parseResult;
  }

  /**
   * Determines whether or not the widget is read-only.
   *
   * @return <code>true</code> if the widget is currently read-only, <code>false</code> if the
   *     widget is currently editable
   */
  public boolean isReadOnly() {
    return getElement().getPropertyBoolean("readOnly");
  }

  /**
   * Turns read-only mode on or off.
   *
   * @param readOnly if <code>true</code>, the widget becomes read-only; if <code>false</code> the
   *     widget becomes editable
   */
  public void setReadOnly(boolean readOnly) {
    getElement().setPropertyBoolean("readOnly", readOnly);
    String readOnlyStyle = "readonly";
    if (readOnly) {
      addStyleDependentName(readOnlyStyle);
    } else {
      removeStyleDependentName(readOnlyStyle);
    }
  }

  @Override
  public void onBrowserEvent(Event event) {
    int type = DOM.eventGetType(event);
    if ((type & Event.KEYEVENTS) != 0) {
      // Fire the keyboard event. Hang on to the current event object so that
      // cancelKey() and setKey() can be implemented.
      currentEvent = event;
      // Call the superclass' onBrowserEvent as that includes the key event
      // handlers.
      super.onBrowserEvent(event);
      currentEvent = null;
    } else {
      // Handles Focus and Click events.
      super.onBrowserEvent(event);
    }
  }

  /**
   * Selects all of the text in the box.
   *
   * <p>This will only work when the widget is attached to the document and not hidden.
   */
  public void selectAll() {
    int length = getText().length();
    if (length > 0) {
      setSelectionRange(0, length);
    }
  }

  public void setAlignment(TextAlignment align) {
    getElement().getStyle().setProperty("textAlign", align.getTextAlignString());
  }

  /** Toggles on / off direction estimation. */
  public void setDirectionEstimator(boolean enabled) {
    autoDirHandler.setDirectionEstimator(enabled);
  }

  /**
   * If a keyboard event is currently being handled by the text box, this method replaces the
   * unicode character or key code associated with it. This allows listeners to easily filter
   * keyboard input.
   *
   * @param key the new key value
   * @deprecated this method only works in IE and should not have been added to the API
   */
  @Deprecated
  public void setKey(char key) {
    if (currentEvent != null) {
      DOM.eventSetKeyCode(currentEvent, key);
    }
  }

  /**
   * Sets the range of text to be selected.
   *
   * <p>This will only work when the widget is attached to the document and not hidden.
   *
   * @param pos the position of the first character to be selected
   * @param length the number of characters to be selected
   */
  public void setSelectionRange(int pos, int length) {
    // Setting the selection range will not work for unattached elements.
    if (!isAttached()) {
      return;
    }

    if (length < 0) {
      throw new IndexOutOfBoundsException("Length must be a positive integer. Length: " + length);
    }
    if (pos < 0 || length + pos > getText().length()) {
      throw new IndexOutOfBoundsException(
          "From Index: "
              + pos
              + "  To Index: "
              + (pos + length)
              + "  Text Length: "
              + getText().length());
    }
    impl.setSelectionRange(getElement(), pos, length);
  }

  public void setValue(T value, boolean fireEvents) {
    T oldValue = fireEvents ? getValue() : null;
    setText(renderer.render(value));
    if (fireEvents) {
      T newValue = getValue();
      ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
    }
  }

  protected TextBoxImpl getImpl() {
    return impl;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    autoDirHandler.refreshDirection();
  }

  /** Alignment values for {@link ValueBoxBase#setAlignment}. */
  public enum TextAlignment {
    CENTER {
      @Override
      String getTextAlignString() {
        return "center";
      }
    },
    JUSTIFY {
      @Override
      String getTextAlignString() {
        return "justify";
      }
    },
    LEFT {
      @Override
      String getTextAlignString() {
        return "left";
      }
    },
    RIGHT {
      @Override
      String getTextAlignString() {
        return "right";
      }
    };

    abstract String getTextAlignString();
  }
}
