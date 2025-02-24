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
package org.gwtproject.i18n.client.gen;

import org.gwtproject.i18n.client.Constant;

/**
 * Interface to represent the constants contained in resource bundle:
 * 'org/gwtproject/i18n/client/gen/TestConstantsQuoting.properties'.
 */
@Constant
public interface TestConstantsQuoting extends org.gwtproject.i18n.client.Constants {

  /**
   * Translated "Doesn''t work this way here".
   *
   * @return translated "Doesn''t work this way here"
   */
  @DefaultStringValue("Doesn''t work this way here")
  @Key("doubledQuote")
  String doubledQuote();

  /**
   * Translated "Embedded\r\ncr-nl.".
   *
   * @return translated "Embedded\r\ncr-nl."
   */
  @DefaultStringValue("Embedded\r\ncr-nl.")
  @Key("embeddedCRNL")
  String embeddedCRNL();

  /**
   * Translated "This line has an\nembedded newline".
   *
   * @return translated "This line has an\nembedded newline"
   */
  @DefaultStringValue("This line has an\nembedded newline")
  @Key("embeddedNL")
  String embeddedNL();

  /**
   * Translated "\"Don't worry, be happy\" he said.".
   *
   * @return translated "\"Don't worry, be happy\" he said."
   */
  @DefaultStringValue("\"Don't worry, be happy\" he said.")
  @Key("embeddedQuote")
  String embeddedQuote();
}
