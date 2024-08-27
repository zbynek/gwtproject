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
package org.gwtproject.builder.client;

import elemental2.dom.CSSProperties;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.gwtproject.builder.shared.StylesBuilder;
import org.gwtproject.regexp.shared.MatchResult;
import org.gwtproject.regexp.shared.RegExp;
import org.gwtproject.user.client.Unit;

import java.util.Locale;

/** Builds the style object. */
class DomStylesBuilder implements StylesBuilder {

  /**
   * A map of hyphenated style properties to their camelCase equivalents.
   *
   * <p>The set of style property names is limited, and common ones are reused frequently, so
   * caching saves us from converting every style property name from hyphenated to camelCase form.
   *
   * <p>Use a {@link JsPropertyMap} to avoid the dynamic casts associated with the emulated version
   * of {@link java.util.Map}.
   */
  private static JsPropertyMap<String> hyphenatedMap;

  /**
   * Regex to match a word in a hyphenated phrase. A word starts with an a hyphen or a letter,
   * followed by zero or more characters letters. For example, in the phrase background-url, the
   * pattern matches "background" and "-url".
   */
  private static RegExp maybeHyphenatedWord;

  /**
   * Convert a hyphenated or camelCase string to a camelCase string.
   *
   * @param name the hyphenated or camelCase string to convert
   * @return the hyphenated string
   */
  // Visible for testing
  static String toCamelCaseForm(String name) {
    // Static initializers.
    if (hyphenatedMap == null) {
      hyphenatedMap = Js.uncheckedCast(JsPropertyMap.of());
      maybeHyphenatedWord = RegExp.compile("([-]?)([a-z])([a-z0-9]*)", "g");
    }

    // Early exit if already in camelCase form.
    if (!name.contains("-")) {
      return name;
    }

    // Check for the name in the cache.
    String camelCase = getCamelCaseName(hyphenatedMap, name);

    // Convert the name to camelCase format if not in the cache.
    if (camelCase == null) {
      /*
       * Strip of any leading hyphens, which are used in browser specified style
       * properties such as "-webkit-border-radius". In this case, the first
       * word "webkit" should remain lowercase.
       */
      if (name.startsWith("-") && name.length() > 1) {
        name = name.substring(1);
      }

      camelCase = "";
      MatchResult matches;
      while ((matches = maybeHyphenatedWord.exec(name)) != null) {
        String word = matches.getGroup(0);
        if (!word.startsWith("-")) {
          // The word is not hyphenated. Probably the first word.
          camelCase += word;
        } else {
          // Remove hyphen and uppercase next letter.
          camelCase += matches.getGroup(2).toUpperCase(Locale.ROOT);
          if (matches.getGroupCount() > 2) {
            camelCase += matches.getGroup(3);
          }
        }
      }
      putCamelCaseName(hyphenatedMap, name, camelCase);
    }

    return camelCase;
  }

  /**
   * Get the camelCase form of a style name to a map.
   *
   * @param name the user specified style name
   * @return the camelCase name, or null if not set
   */
  private static String getCamelCaseName(JsPropertyMap<String> map, String name) {
    return map.get(name);
  }

  /**
   * Save the camelCase form of a style name to a map.
   *
   * @param name the user specified style name
   * @param camelCase the camelCase name
   */
  private static void putCamelCaseName(JsPropertyMap<String> map, String name, String camelCase) {
    map.set(name, camelCase);
  }

  private final DomBuilderImpl delegate;

  /**
   * Construct a new {@link DomStylesBuilder}.
   *
   * @param delegate the delegate that builds the style
   */
  DomStylesBuilder(DomBuilderImpl delegate) {
    this.delegate = delegate;
  }


  @Override
  public StylesBuilder bottom(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().bottom  = value + unit.getType();
    return this;
  }

  @Override
  public void endStyle() {
    delegate.endStyle();
  }




  @Override
  public StylesBuilder height(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().height = CSSProperties.HeightUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder left(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().left = value + unit.getType();
    return this;
  }

  @Override
  public StylesBuilder lineHeight(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().lineHeight = CSSProperties.LineHeightUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder margin(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().margin = CSSProperties.MarginUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder marginBottom(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().marginBottom = CSSProperties.MarginBottomUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder marginLeft(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().marginLeft = CSSProperties.MarginLeftUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder marginRight(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().marginRight = CSSProperties.MarginRightUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder marginTop(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().setProperty("margin-top", value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder padding(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().setProperty("padding", value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder paddingBottom(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().setProperty("padding-bottom", value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder paddingLeft(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().paddingLeft = CSSProperties.PaddingLeftUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder paddingRight(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().paddingRight = CSSProperties.PaddingRightUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder paddingTop(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().paddingTop = CSSProperties.PaddingTopUnionType.of(value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder right(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().right = value + unit.getType();
    return this;
  }

  @Override
  public StylesBuilder top(double value, Unit unit) {
    delegate.assertCanAddStyleProperty().top = value + unit.getType();
    return this;
  }

  @Override
  public StylesBuilder trustedProperty(String name, double value, Unit unit) {
    delegate.assertCanAddStyleProperty().setProperty(name, value + unit.getType());
    return this;
  }

  @Override
  public StylesBuilder trustedProperty(String name, String value) {
    delegate.assertCanAddStyleProperty().setProperty(name, value);
    return this;
  }

}
