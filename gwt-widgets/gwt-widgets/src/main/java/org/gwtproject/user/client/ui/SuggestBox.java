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

import java.util.Collection;
import java.util.List;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.editor.client.IsEditor;
import org.gwtproject.editor.client.LeafValueEditor;
import org.gwtproject.editor.client.adapters.TakesValueEditor;
import org.gwtproject.event.dom.client.*;
import org.gwtproject.event.logical.shared.*;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.ui.PopupPanel.AnimationType;
import org.gwtproject.user.client.ui.SuggestOracle.Callback;
import org.gwtproject.user.client.ui.SuggestOracle.Request;
import org.gwtproject.user.client.ui.SuggestOracle.Response;
import org.gwtproject.user.client.ui.SuggestOracle.Suggestion;

/**
 * A {@link SuggestBox} is a text box or text area which displays a pre-configured set of selections
 * that match the user's input.
 *
 * <p>Each {@link SuggestBox} is associated with a single {@link SuggestOracle}. The {@link
 * SuggestOracle} is used to provide a set of selections given a specific query string.
 *
 * <p>By default, the {@link SuggestBox} uses a {@link MultiWordSuggestOracle} as its oracle. Below
 * we show how a {@link MultiWordSuggestOracle} can be configured:
 *
 * <pre>
 *   MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
 *   oracle.add("Cat");
 *   oracle.add("Dog");
 *   oracle.add("Horse");
 *   oracle.add("Canary");
 *
 *   SuggestBox box = new SuggestBox(oracle);
 * </pre>
 *
 * Using the example above, if the user types "C" into the text widget, the oracle will configure
 * the suggestions with the "Cat" and "Canary" suggestions. Specifically, whenever the user types a
 * key into the text widget, the value is submitted to the <code>MultiWordSuggestOracle</code>.
 *
 * <p>Note that there is no method to retrieve the "currently selected suggestion" in a SuggestBox,
 * because there are points in time where the currently selected suggestion is not defined. For
 * example, if the user types in some text that does not match any of the SuggestBox's suggestions,
 * then the SuggestBox will not have a currently selected suggestion. It is more useful to know when
 * a suggestion has been chosen from the SuggestBox's list of suggestions. A SuggestBox fires {@link
 * SelectionEvent SelectionEvents} whenever a suggestion is chosen, and handlers for these events
 * can be added using the {@link #addSelectionHandler(SelectionHandler)} method.
 *
 * <p><img class='gallery' src='doc-files/SuggestBox.png'/>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-SuggestBox
 *   <dd>the suggest box itself
 * </dl>
 *
 * @see SuggestOracle
 * @see MultiWordSuggestOracle
 * @see ValueBoxBase
 */
public class SuggestBox extends Composite
    implements HasText,
        Focusable,
        HasAnimation,
        HasEnabled,
        HasAllKeyHandlers,
        HasValue<String>,
        HasSelectionHandlers<Suggestion>,
        IsEditor<LeafValueEditor<String>> {

  /** The callback used when a user selects a {@link Suggestion}. */
  public interface SuggestionCallback {
    void onSuggestionSelected(Suggestion suggestion);
  }

  /** Used to display suggestions to the user. */
  public abstract static class SuggestionDisplay {

    /**
     * Get the currently selected {@link Suggestion} in the display.
     *
     * @return the current suggestion, or null if none selected
     */
    protected abstract Suggestion getCurrentSelection();

    /** Hide the list of suggestions from view. */
    protected abstract void hideSuggestions();

    /** Highlight the suggestion directly below the current selection in the list. */
    protected abstract void moveSelectionDown();

    /** Highlight the suggestion directly above the current selection in the list. */
    protected abstract void moveSelectionUp();

    /**
     * Set the debug id of widgets used in the SuggestionDisplay.
     *
     * @param suggestBoxBaseID the baseID of the {@link SuggestBox}
     * @see UIObject#onEnsureDebugId(String)
     */
    protected void onEnsureDebugId(String suggestBoxBaseID) {}

    /**
     * Accepts information about whether there were more suggestions matching than were provided to
     * {@link #showSuggestions}.
     *
     * @param hasMoreSuggestions true if more matches were available
     * @param numMoreSuggestions number of more matches available. If the specific number is
     *     unknown, 0 will be passed.
     */
    protected void setMoreSuggestions(boolean hasMoreSuggestions, int numMoreSuggestions) {
      // Subclasses may optionally implement.
    }

    /**
     * Update the list of visible suggestions.
     *
     * <p>Use care when using isDisplayStringHtml; it is an easy way to expose script-based security
     * problems.
     *
     * @param suggestBox the suggest box where the suggestions originated
     * @param suggestions the suggestions to show
     * @param isDisplayStringHTML should the suggestions be displayed as HTML
     * @param isAutoSelectEnabled if true, the first item should be selected automatically
     * @param callback the callback used when the user makes a suggestion
     */
    protected abstract void showSuggestions(
        SuggestBox suggestBox,
        Collection<? extends Suggestion> suggestions,
        boolean isDisplayStringHTML,
        boolean isAutoSelectEnabled,
        SuggestionCallback callback);

    /**
     * This is here for legacy reasons. It is intentionally not visible.
     *
     * @deprecated implemented in DefaultSuggestionDisplay
     */
    @Deprecated
    boolean isAnimationEnabledImpl() {
      // Implemented in DefaultSuggestionDisplay.
      return false;
    }

    /**
     * Check whether or not the list of suggestions is being shown.
     *
     * @return true if the suggestions are visible, false if not
     */
    public boolean isSuggestionListShowing() {
      return false;
    }

    /**
     * This is here for legacy reasons. It is intentionally not visible.
     *
     * @param enable true to enable animation
     * @deprecated implemented in DefaultSuggestionDisplay
     */
    @Deprecated
    void setAnimationEnabledImpl(boolean enable) {
      // Implemented in DefaultSuggestionDisplay.
    }

    /**
     * This is here for legacy reasons. It is intentionally not visible.
     *
     * @param style the style name
     * @deprecated implemented in DefaultSuggestionDisplay
     */
    @Deprecated
    void setPopupStyleNameImpl(String style) {
      // Implemented in DefaultSuggestionDisplay.
    }
  }

  /**
   * The default implementation of {@link SuggestionDisplay} displays suggestions in a {@link
   * PopupPanel} beneath the {@link SuggestBox}.
   *
   * <h3>CSS Style Rules</h3>
   *
   * <dl>
   *   <dt>.gwt-SuggestBoxPopup
   *   <dd>the suggestion popup
   *   <dt>.gwt-SuggestBoxPopup .item
   *   <dd>an unselected suggestion
   *   <dt>.gwt-SuggestBoxPopup .item-selected
   *   <dd>a selected suggestion
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopLeft
   *   <dd>the top left cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopLeftInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopCenter
   *   <dd>the top center cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopCenterInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopRight
   *   <dd>the top right cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopRightInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleLeft
   *   <dd>the middle left cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleLeftInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleCenter
   *   <dd>the middle center cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleCenterInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleRight
   *   <dd>the middle right cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleRightInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomLeft
   *   <dd>the bottom left cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomLeftInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomCenter
   *   <dd>the bottom center cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomCenterInner
   *   <dd>the inner element of the cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomRight
   *   <dd>the bottom right cell
   *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomRightInner
   *   <dd>the inner element of the cell
   * </dl>
   */
  public static class DefaultSuggestionDisplay extends SuggestionDisplay implements HasAnimation {

    private final SuggestionMenu suggestionMenu;
    private final PopupPanel suggestionPopup;

    /**
     * We need to keep track of the last {@link SuggestBox} because it acts as an autoHide partner
     * for the {@link PopupPanel}. If we use the same display for multiple {@link SuggestBox}, we
     * need to switch the autoHide partner.
     */
    private SuggestBox lastSuggestBox = null;

    /**
     * Sub-classes making use of {@link decorateSuggestionList} to add elements to the suggestion
     * popup _may_ want those elements to show even when there are 0 suggestions. An example would
     * be showing a "No matches" message.
     */
    private boolean hideWhenEmpty = true;

    /** Object to position the suggestion display next to, instead of the associated suggest box. */
    private UIObject positionRelativeTo;

    /** Construct a new {@link DefaultSuggestionDisplay}. */
    public DefaultSuggestionDisplay() {
      suggestionMenu = new SuggestionMenu(true);
      suggestionPopup = createPopup();
      suggestionPopup.setWidget(decorateSuggestionList(suggestionMenu));
    }

    @Override
    public void hideSuggestions() {
      suggestionPopup.hide();
    }

    public boolean isAnimationEnabled() {
      return suggestionPopup.isAnimationEnabled();
    }

    /**
     * Check whether or not the suggestion list is hidden when there are no suggestions to display.
     *
     * @return true if hidden when empty, false if not
     */
    public boolean isSuggestionListHiddenWhenEmpty() {
      return hideWhenEmpty;
    }

    @Override
    public boolean isSuggestionListShowing() {
      return suggestionPopup.isShowing();
    }

    public void setAnimationEnabled(boolean enable) {
      suggestionPopup.setAnimationEnabled(enable);
    }

    /**
     * Sets the style name of the suggestion popup.
     *
     * @param style the new primary style name
     * @see UIObject#setStyleName(String)
     */
    public void setPopupStyleName(String style) {
      suggestionPopup.setStyleName(style);
    }

    /**
     * Sets the UI object where the suggestion display should appear next to.
     *
     * @param uiObject the uiObject used for positioning, or null to position relative to the
     *     suggest box
     */
    public void setPositionRelativeTo(UIObject uiObject) {
      positionRelativeTo = uiObject;
    }

    /**
     * Set whether or not the suggestion list should be hidden when there are no suggestions to
     * display. Defaults to true.
     *
     * @param hideWhenEmpty true to hide when empty, false not to
     */
    public void setSuggestionListHiddenWhenEmpty(boolean hideWhenEmpty) {
      this.hideWhenEmpty = hideWhenEmpty;
    }

    /**
     * Create the PopupPanel that will hold the list of suggestions.
     *
     * @return the popup panel
     */
    protected PopupPanel createPopup() {
      PopupPanel p = new DecoratedPopupPanel(true, false, "suggestPopup");
      p.setStyleName("gwt-SuggestBoxPopup");
      p.setPreviewingAllNativeEvents(true);
      p.setAnimationType(AnimationType.ROLL_DOWN);
      return p;
    }

    /**
     * Wrap the list of suggestions before adding it to the popup. You can override this method if
     * you want to wrap the suggestion list in a decorator.
     *
     * @param suggestionList the widget that contains the list of suggestions
     * @return the suggestList, optionally inside of a wrapper
     */
    protected Widget decorateSuggestionList(Widget suggestionList) {
      return suggestionList;
    }

    @Override
    protected Suggestion getCurrentSelection() {
      if (!isSuggestionListShowing()) {
        return null;
      }
      MenuItem item = suggestionMenu.getSelectedItem();
      return item == null ? null : ((SuggestionMenuItem) item).getSuggestion();
    }

    /**
     * Get the {@link PopupPanel} used to display suggestions.
     *
     * @return the popup panel
     */
    protected PopupPanel getPopupPanel() {
      return suggestionPopup;
    }

    /**
     * Get the {@link MenuBar} used to display suggestions.
     *
     * @return the suggestions menu
     */
    protected MenuBar getSuggestionMenu() {
      return suggestionMenu;
    }

    @Override
    protected void moveSelectionDown() {
      // Make sure that the menu is actually showing. These keystrokes
      // are only relevant when choosing a suggestion.
      if (isSuggestionListShowing()) {
        // If nothing is selected, getSelectedItemIndex will return -1 and we
        // will select index 0 (the first item) by default.
        suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() + 1);
      }
    }

    @Override
    protected void moveSelectionUp() {
      // Make sure that the menu is actually showing. These keystrokes
      // are only relevant when choosing a suggestion.
      if (isSuggestionListShowing()) {
        // if nothing is selected, then we should select the last suggestion by
        // default. This is because, in some cases, the suggestions menu will
        // appear above the text box rather than below it (for example, if the
        // text box is at the bottom of the window and the suggestions will not
        // fit below the text box). In this case, users would expect to be able
        // to use the up arrow to navigate to the suggestions.
        if (suggestionMenu.getSelectedItemIndex() == -1) {
          suggestionMenu.selectItem(suggestionMenu.getNumItems() - 1);
        } else {
          suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() - 1);
        }
      }
    }

    /**
     * <b>Affected Elements:</b>
     *
     * <ul>
     *   <li>-popup = The popup that appears with suggestions.
     *   <li>-item# = The suggested item at the specified index.
     * </ul>
     *
     * @see UIObject#onEnsureDebugId(String)
     */
    @Override
    protected void onEnsureDebugId(String baseID) {
      suggestionPopup.ensureDebugId(baseID + "-popup");
      suggestionMenu.setMenuItemDebugIds(baseID);
    }

    @Override
    protected void showSuggestions(
        final SuggestBox suggestBox,
        Collection<? extends Suggestion> suggestions,
        boolean isDisplayStringHTML,
        boolean isAutoSelectEnabled,
        final SuggestionCallback callback) {
      // Hide the popup if there are no suggestions to display.
      boolean anySuggestions = (suggestions != null && suggestions.size() > 0);
      if (!anySuggestions && hideWhenEmpty) {
        hideSuggestions();
        return;
      }

      // Hide the popup before we manipulate the menu within it. If we do not
      // do this, some browsers will redraw the popup as items are removed
      // and added to the menu.
      if (suggestionPopup.isAttached()) {
        suggestionPopup.hide();
      }

      suggestionMenu.clearItems();

      for (final Suggestion curSuggestion : suggestions) {
        final SuggestionMenuItem menuItem =
            new SuggestionMenuItem(curSuggestion, isDisplayStringHTML);
        menuItem.setScheduledCommand(
            new ScheduledCommand() {
              public void execute() {
                callback.onSuggestionSelected(curSuggestion);
              }
            });

        suggestionMenu.addItem(menuItem);
      }

      if (isAutoSelectEnabled && anySuggestions) {
        // Select the first item in the suggestion menu.
        suggestionMenu.selectItem(0);
      }

      // Link the popup autoHide to the TextBox.
      if (lastSuggestBox != suggestBox) {
        // If the suggest box has changed, free the old one first.
        if (lastSuggestBox != null) {
          suggestionPopup.removeAutoHidePartner(lastSuggestBox.getElement());
        }
        lastSuggestBox = suggestBox;
        suggestionPopup.addAutoHidePartner(suggestBox.getElement());
      }

      // Show the popup under the TextBox.
      suggestionPopup.showRelativeTo(positionRelativeTo != null ? positionRelativeTo : suggestBox);
    }

    @Override
    boolean isAnimationEnabledImpl() {
      return isAnimationEnabled();
    }

    @Override
    void setAnimationEnabledImpl(boolean enable) {
      setAnimationEnabled(enable);
    }

    @Override
    void setPopupStyleNameImpl(String style) {
      setPopupStyleName(style);
    }
  }

  /**
   * The SuggestionMenu class is used for the display and selection of suggestions in the SuggestBox
   * widget. SuggestionMenu differs from MenuBar in that it always has a vertical orientation, and
   * it has no submenus. It also allows for programmatic selection of items in the menu, and
   * programmatically performing the action associated with the selected item. In the MenuBar class,
   * items cannot be selected programatically - they can only be selected when the user places the
   * mouse over a particlar item. Additional methods in SuggestionMenu provide information about the
   * number of items in the menu, and the index of the currently selected item.
   */
  private static class SuggestionMenu extends MenuBar {

    public SuggestionMenu(boolean vertical) {
      super(vertical);
      // Make sure that CSS styles specified for the default Menu classes
      // do not affect this menu
      setStyleName("");
      setFocusOnHoverEnabled(false);
    }

    public int getNumItems() {
      return getItems().size();
    }

    /**
     * Returns the index of the menu item that is currently selected.
     *
     * @return returns the selected item
     */
    public int getSelectedItemIndex() {
      // The index of the currently selected item can only be
      // obtained if the menu is showing.
      MenuItem selectedItem = getSelectedItem();
      if (selectedItem != null) {
        return getItems().indexOf(selectedItem);
      }
      return -1;
    }

    /**
     * Selects the item at the specified index in the menu. Selecting the item does not perform the
     * item's associated action; it only changes the style of the item and updates the value of
     * SuggestionMenu.selectedItem.
     *
     * @param index index
     */
    public void selectItem(int index) {
      List<MenuItem> items = getItems();
      if (index > -1 && index < items.size()) {
        itemOver(items.get(index), false);
      }
    }
  }

  /**
   * Class for menu items in a SuggestionMenu. A SuggestionMenuItem differs from a MenuItem in that
   * each item is backed by a Suggestion object. The text of each menu item is derived from the
   * display string of a Suggestion object, and each item stores a reference to its Suggestion
   * object.
   */
  private static class SuggestionMenuItem extends MenuItem {

    private static final String STYLENAME_DEFAULT = "item";

    private Suggestion suggestion;

    public SuggestionMenuItem(Suggestion suggestion, boolean asHTML) {
      super(suggestion.getDisplayString(), asHTML);
      // Each suggestion should be placed in a single row in the suggestion
      // menu. If the window is resized and the suggestion cannot fit on a
      // single row, it should be clipped (instead of wrapping around and
      // taking up a second row).
      getElement().getStyle().setProperty("whiteSpace", "nowrap");
      setStyleName(STYLENAME_DEFAULT);
      setSuggestion(suggestion);
    }

    public Suggestion getSuggestion() {
      return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
      this.suggestion = suggestion;
    }
  }

  private static final String STYLENAME_DEFAULT = "gwt-SuggestBox";

  /**
   * Creates a {@link SuggestBox} widget that wraps an existing &lt;input type='text'&gt; element.
   *
   * <p>This element must already be attached to the document. If the element is removed from the
   * document, you must call {@link RootPanel#detachNow(Widget)}.
   *
   * @param oracle the suggest box oracle to use
   * @param element the element to be wrapped
   */
  public static SuggestBox wrap(SuggestOracle oracle, Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    TextBox textBox = new TextBox(element);
    SuggestBox suggestBox = new SuggestBox(oracle, textBox);

    // Mark it attached and remember it for cleanup.
    suggestBox.onAttach();
    RootPanel.detachOnWindowClose(suggestBox);

    return suggestBox;
  }

  private int limit = 20;
  private boolean selectsFirstItem = true;
  private SuggestOracle oracle;
  private String currentText;
  private LeafValueEditor<String> editor;
  private final SuggestionDisplay display;
  private final ValueBoxBase<String> box;
  private final Callback callback =
      new Callback() {
        public void onSuggestionsReady(Request request, Response response) {
          // If disabled while request was in-flight, drop it
          if (!isEnabled()) {
            return;
          }
          display.setMoreSuggestions(
              response.hasMoreSuggestions(), response.getMoreSuggestionsCount());
          display.showSuggestions(
              SuggestBox.this,
              response.getSuggestions(),
              oracle.isDisplayStringHTML(),
              isAutoSelectEnabled(),
              suggestionCallback);
        }
      };
  private final SuggestionCallback suggestionCallback =
      new SuggestionCallback() {
        public void onSuggestionSelected(Suggestion suggestion) {
          box.setFocus(true);
          setNewSelection(suggestion);
        }
      };

  /**
   * Constructor for {@link SuggestBox}. Creates a {@link MultiWordSuggestOracle} and {@link
   * TextBox} to use with this {@link SuggestBox}.
   */
  public SuggestBox() {
    this(new MultiWordSuggestOracle());
  }

  /**
   * Constructor for {@link SuggestBox}. Creates a {@link TextBox} to use with this {@link
   * SuggestBox}.
   *
   * @param oracle the oracle for this <code>SuggestBox</code>
   */
  public SuggestBox(SuggestOracle oracle) {
    this(oracle, new TextBox());
  }

  /**
   * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and
   * wrapped by the {@link SuggestBox}.
   *
   * @param oracle supplies suggestions based upon the current contents of the text widget
   * @param box the text widget
   */
  public SuggestBox(SuggestOracle oracle, ValueBoxBase<String> box) {
    this(oracle, box, new DefaultSuggestionDisplay());
  }

  /**
   * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and
   * wrapped by the {@link SuggestBox}.
   *
   * @param oracle supplies suggestions based upon the current contents of the text widget
   * @param box the text widget
   * @param suggestDisplay the class used to display suggestions
   */
  public SuggestBox(
      SuggestOracle oracle, ValueBoxBase<String> box, SuggestionDisplay suggestDisplay) {
    this.box = box;
    this.display = suggestDisplay;
    initWidget(box);

    addEventsToTextBox();

    setOracle(oracle);
    setStyleName(STYLENAME_DEFAULT);
  }

  public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
    return addDomHandler(handler, KeyDownEvent.getType());
  }

  public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
    return addDomHandler(handler, KeyPressEvent.getType());
  }

  public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    return addDomHandler(handler, KeyUpEvent.getType());
  }

  public HandlerRegistration addSelectionHandler(SelectionHandler<Suggestion> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /** Returns a {@link TakesValueEditor} backed by the SuggestBox. */
  public LeafValueEditor<String> asEditor() {
    if (editor == null) {
      editor = TakesValueEditor.of(this);
    }
    return editor;
  }

  /**
   * Gets the limit for the number of suggestions that should be displayed for this box. It is up to
   * the current {@link SuggestOracle} to enforce this limit.
   *
   * @return the limit for the number of suggestions
   */
  public int getLimit() {
    return limit;
  }

  /**
   * Get the {@link SuggestionDisplay} used to display suggestions.
   *
   * @return the {@link SuggestionDisplay}
   */
  public SuggestionDisplay getSuggestionDisplay() {
    return display;
  }

  /**
   * Gets the suggest box's {@link SuggestOracle}.
   *
   * @return the {@link SuggestOracle}
   */
  public SuggestOracle getSuggestOracle() {
    return oracle;
  }

  public int getTabIndex() {
    return box.getTabIndex();
  }

  public String getText() {
    return box.getText();
  }

  /**
   * Get the text box associated with this suggest box.
   *
   * @return this suggest box's text box
   * @throws ClassCastException if this suggest box's value box is not an instance of TextBoxBase
   * @deprecated in favour of getValueBox
   */
  @Deprecated
  public TextBoxBase getTextBox() {
    return (TextBoxBase) box;
  }

  public String getValue() {
    return box.getValue();
  }

  /**
   * Get the ValueBoxBase associated with this suggest box.
   *
   * @return this suggest box's value box
   */
  public ValueBoxBase<String> getValueBox() {
    return box;
  }

  /**
   * Hide current suggestions in the {@link DefaultSuggestionDisplay}. Note that this method is a
   * no-op unless the {@link DefaultSuggestionDisplay} is used.
   *
   * @deprecated use {@link DefaultSuggestionDisplay#hideSuggestions()} instead
   */
  @Deprecated
  public void hideSuggestionList() {
    display.hideSuggestions();
  }

  /**
   * Check whether or not the {@link DefaultSuggestionDisplay} has animations enabled. Note that
   * this method only has a meaningful return value when the {@link DefaultSuggestionDisplay} is
   * used.
   *
   * @deprecated use {@link DefaultSuggestionDisplay#isAnimationEnabled()} instead
   */
  @Deprecated
  public boolean isAnimationEnabled() {
    return display.isAnimationEnabledImpl();
  }

  /**
   * Returns whether or not the first suggestion will be automatically selected. This behavior is on
   * by default.
   *
   * @return true if the first suggestion will be automatically selected
   */
  public boolean isAutoSelectEnabled() {
    return selectsFirstItem;
  }

  /**
   * Gets whether this widget is enabled.
   *
   * @return <code>true</code> if the widget is enabled
   */
  public boolean isEnabled() {
    return box.isEnabled();
  }

  /**
   * Check if the {@link SuggestionDisplay} is showing.
   *
   * @return true if the list of suggestions is currently showing, false if not
   */
  public boolean isSuggestionListShowing() {
    return display.isSuggestionListShowing();
  }

  /** Refreshes the current list of suggestions. */
  public void refreshSuggestionList() {
    if (isAttached()) {
      refreshSuggestions();
    }
  }

  public void setAccessKey(char key) {
    box.setAccessKey(key);
  }

  /**
   * Enable or disable animations in the {@link DefaultSuggestionDisplay}. Note that this method is
   * a no-op unless the {@link DefaultSuggestionDisplay} is used.
   *
   * @deprecated use {@link DefaultSuggestionDisplay#setAnimationEnabled(boolean)} instead
   */
  @Deprecated
  public void setAnimationEnabled(boolean enable) {
    display.setAnimationEnabledImpl(enable);
  }

  /**
   * Turns on or off the behavior that automatically selects the first suggested item. This behavior
   * is on by default.
   *
   * @param selectsFirstItem Whether or not to automatically select the first suggestion
   */
  public void setAutoSelectEnabled(boolean selectsFirstItem) {
    this.selectsFirstItem = selectsFirstItem;
  }

  /**
   * Sets whether this widget is enabled.
   *
   * @param enabled <code>true</code> to enable the widget, <code>false</code> to disable it
   */
  public void setEnabled(boolean enabled) {
    box.setEnabled(enabled);
    if (!enabled) {
      display.hideSuggestions();
    }
  }

  public void setFocus(boolean focused) {
    box.setFocus(focused);
  }

  /**
   * Sets the limit to the number of suggestions the oracle should provide. It is up to the oracle
   * to enforce this limit.
   *
   * @param limit the limit to the number of suggestions provided
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * Sets the style name of the suggestion popup in the {@link DefaultSuggestionDisplay}. Note that
   * this method is a no-op unless the {@link DefaultSuggestionDisplay} is used.
   *
   * @param style the new primary style name
   * @see UIObject#setStyleName(String)
   * @deprecated use {@link DefaultSuggestionDisplay#setPopupStyleName(String)} instead
   */
  @Deprecated
  public void setPopupStyleName(String style) {
    getSuggestionDisplay().setPopupStyleNameImpl(style);
  }

  public void setTabIndex(int index) {
    box.setTabIndex(index);
  }

  public void setText(String text) {
    box.setText(text);
  }

  public void setValue(String newValue) {
    box.setValue(newValue);
  }

  public void setValue(String value, boolean fireEvents) {
    box.setValue(value, fireEvents);
  }

  /** Show the current list of suggestions. */
  public void showSuggestionList() {
    if (isAttached()) {
      currentText = null;
      refreshSuggestions();
    }
  }

  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);
    display.onEnsureDebugId(baseID);
  }

  void showSuggestions(String query) {
    if (query.length() == 0) {
      oracle.requestDefaultSuggestions(new Request(null, limit), callback);
    } else {
      oracle.requestSuggestions(new Request(query, limit), callback);
    }
  }

  private void addEventsToTextBox() {
    class TextBoxEvents implements KeyDownHandler, KeyUpHandler, ValueChangeHandler<String> {

      public void onKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
          case KeyCodes.KEY_DOWN:
            display.moveSelectionDown();
            if (isSuggestionListShowing()) {
              event.preventDefault();
            }
            break;
          case KeyCodes.KEY_UP:
            display.moveSelectionUp();
            if (isSuggestionListShowing()) {
              event.preventDefault();
            }
            break;
          case KeyCodes.KEY_ENTER:
          case KeyCodes.KEY_TAB:
            Suggestion suggestion = display.getCurrentSelection();
            if (suggestion == null) {
              display.hideSuggestions();
            } else {
              setNewSelection(suggestion);
            }
            break;
        }
      }

      public void onKeyUp(KeyUpEvent event) {
        // After every user key input, refresh the popup's suggestions.
        refreshSuggestions();
      }

      public void onValueChange(ValueChangeEvent<String> event) {
        delegateEvent(SuggestBox.this, event);
      }
    }

    TextBoxEvents events = new TextBoxEvents();
    box.addChangeHandler(
        new ChangeHandler() {
          @Override
          public void onChange(ChangeEvent changeEvent) {}
        });

    box.addValueChangeHandler(
        new ValueChangeHandler<String>() {
          @Override
          public void onValueChange(ValueChangeEvent<String> valueChangeEvent) {}
        });

    box.addKeyDownHandler(events);
    box.addKeyUpHandler(events);
    // box.addValueChangeHandler(events);
  }

  private void fireSuggestionEvent(Suggestion selectedSuggestion) {
    SelectionEvent.fire(this, selectedSuggestion);
  }

  private void refreshSuggestions() {
    // Get the raw text.
    String text = getText();
    if (text.equals(currentText)) {
      return;
    } else {
      currentText = text;
    }
    showSuggestions(text);
  }

  /**
   * Set the new suggestion in the text box.
   *
   * @param curSuggestion the new suggestion
   */
  private void setNewSelection(Suggestion curSuggestion) {
    assert curSuggestion != null : "suggestion cannot be null";
    currentText = curSuggestion.getReplacementString();
    setText(currentText);
    display.hideSuggestions();
    fireSuggestionEvent(curSuggestion);
  }

  /**
   * Sets the suggestion oracle used to create suggestions.
   *
   * @param oracle the oracle
   */
  private void setOracle(SuggestOracle oracle) {
    this.oracle = oracle;
  }
}
