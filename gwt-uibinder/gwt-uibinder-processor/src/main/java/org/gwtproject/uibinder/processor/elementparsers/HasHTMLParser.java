/*
 * Copyright 2018 Google Inc.
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
package org.gwtproject.uibinder.processor.elementparsers;

import javax.lang.model.type.TypeMirror;
import org.gwtproject.uibinder.processor.UiBinderWriter;
import org.gwtproject.uibinder.processor.XMLElement;
import org.gwtproject.uibinder.processor.ext.UnableToCompleteException;

/** Parses widgets that implement HasHTML. */
public class HasHTMLParser implements ElementParser {

  public void parse(XMLElement elem, String fieldName, TypeMirror type, UiBinderWriter writer)
      throws UnableToCompleteException {

    HtmlInterpreter interpreter = HtmlInterpreter.newInterpreterForUiObject(writer, fieldName);
    writer.beginAttachedSection(fieldName + ".getElement()");
    String html = elem.consumeInnerHtml(interpreter);
    writer.endAttachedSection();
    // TODO(jgw): throw an error if there's a conflicting 'html' attribute.
    if (html.trim().length() > 0) {
      writer.genPropertySet(fieldName, "HTML", writer.declareTemplateCall(html, fieldName));
    }
  }
}
