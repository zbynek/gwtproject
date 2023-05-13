/*
 * Copyright 2006 Google Inc.
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
package org.gwtproject.user.client.ui.impl;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/** Implementation class used by {@link org.gwtproject.user.client.ui.TextBox}. */
public class TextBoxImpl {

  public int getCursorPos(HTMLElement elem) {
    HTMLInputElement jsObject = Js.uncheckedCast(elem);
    if (Js.asPropertyMap(jsObject).has("selectionStart")) {
      return jsObject.selectionStart;
    }
    return 0;
  }

  public int getSelectionLength(HTMLElement elem) {
    JsPropertyMap jsObject = Js.asPropertyMap(elem);
    HTMLInputElement asInput = Js.uncheckedCast(elem);
    if (jsObject.has("selectionEnd") && jsObject.has("selectionStart")) {
        int selectionEnd = asInput.selectionEnd;
        int selectionStart = asInput.selectionStart;
        return selectionEnd - selectionStart;
    }
    return 0;
  }

  public int getTextAreaCursorPos(HTMLElement elem) {
    return getCursorPos(elem);
  }

  public int getTextAreaSelectionLength(HTMLElement elem) {
    return getSelectionLength(elem);
  }

  public void setSelectionRange(HTMLElement elem, int pos, int length) {
    try {
      Js.<HTMLInputElement>uncheckedCast(elem).setSelectionRange(pos, pos + length);
    } catch (Exception e) {
    }
  }


}
