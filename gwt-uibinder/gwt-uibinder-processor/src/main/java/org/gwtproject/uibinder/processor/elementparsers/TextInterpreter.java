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

import org.gwtproject.uibinder.processor.UiBinderWriter;
import org.gwtproject.uibinder.processor.XMLElement;
import org.gwtproject.uibinder.processor.ext.UnableToCompleteException;
import org.gwtproject.uibinder.processor.messages.MessageWriter;
import org.gwtproject.uibinder.processor.messages.MessagesWriter;
import org.gwtproject.uibinder.processor.messages.PlaceholderInterpreter;

/**
 * The interpreter of choice for calls to {@link XMLElement#consumeInnerText}. It handles
 * &lt;m:msg/&gt; elements, replacing them with references to Messages interface calls.
 *
 * <p>Calls to {@link XMLElement#consumeInnerHtml} should instead use {@link HtmlInterpreter}
 */
public class TextInterpreter implements XMLElement.Interpreter<String> {

  private final UiBinderWriter writer;

  public TextInterpreter(UiBinderWriter writer) {
    this.writer = writer;
  }

  public String interpretElement(XMLElement elem) throws UnableToCompleteException {
    MessagesWriter messages = writer.getMessages();
    if (messages.isMessage(elem)) {
      String messageInvocation = consumeAsTextMessage(elem, messages);
      return writer.tokenForStringExpression(elem, messageInvocation);
    }

    return new UiTextInterpreter(writer).interpretElement(elem);
  }

  private String consumeAsTextMessage(XMLElement elem, MessagesWriter messages)
      throws UnableToCompleteException {
    if (!elem.hasChildNodes()) {
      writer.die(elem, "Empty message");
    }

    MessageWriter message = messages.newMessage(elem);
    PlaceholderInterpreter interpreter = new TextPlaceholderInterpreter(writer, message);
    message.setDefaultMessage(elem.consumeInnerText(interpreter));
    return messages.declareMessage(message);
  }
}
