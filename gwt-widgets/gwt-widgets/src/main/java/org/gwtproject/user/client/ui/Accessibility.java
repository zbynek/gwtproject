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


import elemental2.dom.HTMLElement;

/**
 * Allows ARIA attributes to be added to widgets so that they can be identified by assistive
 * technologies.
 *
 * <p>A 'role' describes the role a widget plays in a page: i.e. a checkbox widget is assigned a
 * "checkbox" role.
 *
 * <p>A 'state' describes the current state of the widget. For example, a checkbox widget has the
 * state "checked", which is given a value of "true" or "false" depending on whether it is currently
 * checked or unchecked.
 *
 * <p>See <a href="http://developer.mozilla.org/en/docs/Accessible_DHTML">the MDC page on Accessible
 * DHTML</a> for more information.
 *
 * <p>Note that although this API is public, the ARIA specification is still somewhat in flux. As a
 * result, this API is subject to change as the specification stabilizes; we will do our best to
 * keep the community updated on changes.
 *
 * @deprecated Use the new GWT ARIA library
 */
@Deprecated
public final class Accessibility {

  public static final String ROLE_TREE = "tree";
  public static final String ROLE_TREEITEM = "treeitem";
  public static final String ROLE_BUTTON = "button";
  public static final String ROLE_TABLIST = "tablist";
  public static final String ROLE_TAB = "tab";
  public static final String ROLE_TABPANEL = "tabpanel";
  public static final String ROLE_MENUBAR = "menubar";
  public static final String ROLE_MENUITEM = "menuitem";

  public static final String STATE_ACTIVEDESCENDANT = "aria-activedescendant";
  public static final String STATE_POSINSET = "aria-posinset";
  public static final String STATE_SETSIZE = "aria-setsize";
  public static final String STATE_SELECTED = "aria-selected";
  public static final String STATE_EXPANDED = "aria-expanded";
  public static final String STATE_LEVEL = "aria-level";
  public static final String STATE_HASPOPUP = "aria-haspopup";
  public static final String STATE_PRESSED = "aria-pressed";

  private static final String ATTR_NAME_ROLE = "role";

  /**
   * Requests the string value of the role with the specified namespace.
   *
   * @param elem the element which has the specified role
   * @return the value of the role, or an empty string if none exists
   */
  public static String getRole(HTMLElement elem) {
    return elem.getAttribute(ATTR_NAME_ROLE);
  }

  /**
   * Requests the string value of the state with the specified namespace.
   *
   * @param elem the element which has the specified state
   * @param stateName the name of the state
   * @return the value of the state, or an empty string if none exists
   */
  public static String getState(HTMLElement elem, String stateName) {
    return elem.getAttribute(stateName);
  }

  /**
   * Removes the state from the given element.
   *
   * @param elem the element which has the specified state
   * @param stateName the name of the state to remove
   */
  public static void removeState(HTMLElement elem, String stateName) {
    elem.removeAttribute(stateName);
  }
  /**
   * Assigns the specified element the specified role and value for that role.
   *
   * @param elem the element to be given the specified role
   * @param roleName the name of the role
   */
  public static void setRole(HTMLElement elem, String roleName) {
    elem.setAttribute(ATTR_NAME_ROLE, roleName);
  }

  /**
   * Assigns the specified element the specified state and value for that state.
   *
   * @param elem the element to be given the specified state
   * @param stateName the name of the state
   * @param stateValue the value of the state
   */
  public static void setState(HTMLElement elem, String stateName, String stateValue) {
    elem.setAttribute(stateName, stateValue);
  }

  private Accessibility() {}
}
