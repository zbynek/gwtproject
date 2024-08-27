/*
 * Copyright © 2019 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.builder.shared;

import org.gwtproject.regexp.shared.MatchResult;
import org.gwtproject.regexp.shared.RegExp;
import org.gwtproject.safecss.shared.SafeStylesUtils;
import org.gwtproject.user.client.Unit;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Builds the style object. */
class HtmlStylesBuilder implements StylesBuilder {

  /**
   * A map of camelCase style properties to their hyphenated equivalents.
   *
   * <p>The set of style property names is limited, and common ones are reused frequently, so
   * caching saves us from converting every style property name from camelCase to hyphenated form.
   */
  private static Map<String, String> camelCaseMap;

  /**
   * Regex to match a word in a camelCase phrase. A word starts with an uppercase or lowercase
   * letter, followed by zero or more non-uppercase letters. For example, in the camelCase phrase
   * backgroundUrl, the pattern matches "background" and "Url".
   *
   * <p>This pattern is not used to validate the style property name. {@link SafeStylesUtils}
   * performs a more detailed check.
   */
  private static RegExp camelCaseWord;

  /** Regex to match a word that starts with an uppercase letter. */
  private static RegExp caseWord;

  /**
   * Convert a camelCase or hyphenated string to a hyphenated string.
   *
   * @param name the camelCase or hyphenated string to convert
   * @return the hyphenated string
   */
  // Visible for testing
  static String toHyphenatedForm(String name) {
    // Static initializers.
    if (camelCaseWord == null) {
      camelCaseMap = new HashMap<String, String>();
      camelCaseWord = RegExp.compile("([A-Za-z])([^A-Z]*)", "g");
      caseWord = RegExp.compile("[A-Z]([^A-Z]*)");
    }

    // Early exit if already in hyphenated form.
    if (name.contains("-")) {
      return name;
    }

    // Check for the name in the cache.
    String hyphenated = camelCaseMap.get(name);

    // Convert the name to hyphenated format if not in the cache.
    if (hyphenated == null) {
      hyphenated = "";
      MatchResult matches;
      while ((matches = camelCaseWord.exec(name)) != null) {
        String word = matches.getGroup(0);
        if (caseWord.exec(word) == null) {
          // The first letter is already lowercase, probably the first word.
          hyphenated += word;
        } else {
          // Hyphenate the first letter.
          hyphenated += "-" + matches.getGroup(1).toLowerCase(Locale.ROOT);
          if (matches.getGroupCount() > 1) {
            hyphenated += matches.getGroup(2);
          }
        }
      }
      camelCaseMap.put(name, hyphenated);
    }

    return hyphenated;
  }

  private final HtmlBuilderImpl delegate;

  /**
   * Construct a new {@link HtmlStylesBuilder}.
   *
   * @param delegate the delegate that builds the style
   */
  HtmlStylesBuilder(HtmlBuilderImpl delegate) {
    this.delegate = delegate;
  }

  @Override
  public StylesBuilder bottom(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("bottom", value + unit.getType()));
  }

  @Override
  public void endStyle() {
    delegate.endStyle();
  }


  @Override
  public StylesBuilder height(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("height", value + unit.getType()));
  }

  @Override
  public StylesBuilder left(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("left", value + unit.getType()));
  }

  @Override
  public StylesBuilder lineHeight(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("line-height", value + unit.getType()));
  }

  @Override
  public StylesBuilder margin(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("margin", value + unit.getType()));
  }

  @Override
  public StylesBuilder marginBottom(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("margin-bottom", value + unit.getType()));
  }

  @Override
  public StylesBuilder marginLeft(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("margin-left", value + unit.getType()));
  }

  @Override
  public StylesBuilder marginRight(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("margin-right", value + unit.getType()));
  }

  @Override
  public StylesBuilder marginTop(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("margin-top", value + unit.getType()));
  }


  @Override
  public StylesBuilder padding(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("padding", value + unit.getType()));
  }

  @Override
  public StylesBuilder paddingBottom(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("padding-bottom", value + unit.getType()));
  }

  @Override
  public StylesBuilder paddingLeft(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("padding-left", value + unit.getType()));
  }

  @Override
  public StylesBuilder paddingRight(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("padding-right", value + unit.getType()));
  }

  @Override
  public StylesBuilder paddingTop(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("padding-top", value + unit.getType()));
  }

  @Override
  public StylesBuilder right(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("right", value + unit.getType()));
  }

  @Override
  public StylesBuilder top(double value, Unit unit) {
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue("top", value + unit.getType()));
  }

  @Override
  public StylesBuilder trustedProperty(String name, double value, Unit unit) {
    name = toHyphenatedForm(name);
    return delegate.styleProperty(
        SafeStylesUtils.fromTrustedNameAndValue(name, value + unit.getType()));
  }

  @Override
  public StylesBuilder trustedProperty(String name, String value) {
    name = toHyphenatedForm(name);
    return delegate.styleProperty(SafeStylesUtils.fromTrustedNameAndValue(name, value));
  }

  }
