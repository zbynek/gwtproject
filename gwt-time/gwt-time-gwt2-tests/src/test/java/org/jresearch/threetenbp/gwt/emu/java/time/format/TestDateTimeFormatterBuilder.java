/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jresearch.threetenbp.gwt.emu.java.time.format;

import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.DAY_OF_MONTH;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.DAY_OF_WEEK;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.YEAR;

import java.util.HashMap;
import java.util.Map;
import org.jresearch.threetenbp.gwt.emu.java.text.ParsePosition;
import org.jresearch.threetenbp.gwt.emu.java.time.AbstractTest;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalAccessor;
import org.junit.Test;

/** Test DateTimeFormatterBuilder. */
// @Test
public class TestDateTimeFormatterBuilder extends AbstractTest {

  private DateTimeFormatterBuilder builder;

  //    @BeforeMethod
  @Override
  public void gwtSetUp() throws Exception {
    super.gwtSetUp();
    builder = new DateTimeFormatterBuilder();
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_toFormatter_empty() throws Exception {
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "");
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parseCaseSensitive() throws Exception {
    builder.parseCaseSensitive();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ParseCaseSensitive(true)");
  }

  @Test
  public void test_parseCaseInsensitive() throws Exception {
    builder.parseCaseInsensitive();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ParseCaseSensitive(false)");
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parseStrict() throws Exception {
    builder.parseStrict();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ParseStrict(true)");
  }

  @Test
  public void test_parseLenient() throws Exception {
    builder.parseLenient();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ParseStrict(false)");
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_appendValue_1arg() throws Exception {
    builder.appendValue(DAY_OF_MONTH);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(DayOfMonth)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendValue_1arg_null() throws Exception {
    try {
      builder.appendValue(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_appendValue_2arg() throws Exception {
    builder.appendValue(DAY_OF_MONTH, 3);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(DayOfMonth,3)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendValue_2arg_null() throws Exception {
    try {
      builder.appendValue(null, 3);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_2arg_widthTooSmall() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 0);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_2arg_widthTooBig() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 20);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_appendValue_3arg() throws Exception {
    builder.appendValue(DAY_OF_MONTH, 2, 3, SignStyle.NORMAL);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(DayOfMonth,2,3,NORMAL)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendValue_3arg_nullField() throws Exception {
    try {
      builder.appendValue(null, 2, 3, SignStyle.NORMAL);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_3arg_minWidthTooSmall() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 0, 2, SignStyle.NORMAL);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_3arg_minWidthTooBig() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 20, 2, SignStyle.NORMAL);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_3arg_maxWidthTooSmall() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 2, 0, SignStyle.NORMAL);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_3arg_maxWidthTooBig() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 2, 20, SignStyle.NORMAL);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendValue_3arg_maxWidthMinWidth() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 4, 2, SignStyle.NORMAL);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_appendValue_3arg_nullSignStyle() throws Exception {
    try {
      builder.appendValue(DAY_OF_MONTH, 2, 3, null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_appendValue_subsequent2_parse3() throws Exception {
    builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValue(DAY_OF_MONTH, 2);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)Value(DayOfMonth,2)");
    TemporalAccessor cal = f.parseUnresolved("123", new ParsePosition(0));
    assertEquals(cal.get(MONTH_OF_YEAR), 1);
    assertEquals(cal.get(DAY_OF_MONTH), 23);
  }

  @Test
  public void test_appendValue_subsequent2_parse4() throws Exception {
    builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValue(DAY_OF_MONTH, 2);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)Value(DayOfMonth,2)");
    TemporalAccessor cal = f.parseUnresolved("0123", new ParsePosition(0));
    assertEquals(cal.get(MONTH_OF_YEAR), 1);
    assertEquals(cal.get(DAY_OF_MONTH), 23);
  }

  @Test
  public void test_appendValue_subsequent2_parse5() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL)
        .appendValue(DAY_OF_MONTH, 2)
        .appendLiteral('4');
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)Value(DayOfMonth,2)'4'");
    TemporalAccessor cal = f.parseUnresolved("01234", new ParsePosition(0));
    assertEquals(cal.get(MONTH_OF_YEAR), 1);
    assertEquals(cal.get(DAY_OF_MONTH), 23);
  }

  @Test
  public void test_appendValue_subsequent3_parse6() throws Exception {
    builder
        .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendValue(MONTH_OF_YEAR, 2)
        .appendValue(DAY_OF_MONTH, 2);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(
        f.toString(), "Value(Year,4,10,EXCEEDS_PAD)Value(MonthOfYear,2)Value(DayOfMonth,2)");
    TemporalAccessor cal = f.parseUnresolved("20090630", new ParsePosition(0));
    assertEquals(cal.get(YEAR), 2009);
    assertEquals(cal.get(MONTH_OF_YEAR), 6);
    assertEquals(cal.get(DAY_OF_MONTH), 30);
  }

  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_appendValueReduced_null() throws Exception {
    try {
      builder.appendValueReduced(null, 2, 2, 2000);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void test_appendValueReduced() throws Exception {
    builder.appendValueReduced(YEAR, 2, 2, 2000);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ReducedValue(Year,2,2,2000)");
    TemporalAccessor cal = f.parseUnresolved("12", new ParsePosition(0));
    assertEquals(cal.get(YEAR), 2012);
  }

  @Test
  public void test_appendValueReduced_subsequent_parse() throws Exception {
    builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValueReduced(YEAR, 2, 2, 2000);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)ReducedValue(Year,2,2,2000)");
    TemporalAccessor cal = f.parseUnresolved("123", new ParsePosition(0));
    assertEquals(cal.get(MONTH_OF_YEAR), 1);
    assertEquals(cal.get(YEAR), 2023);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_appendFraction_4arg() throws Exception {
    builder.appendFraction(MINUTE_OF_HOUR, 1, 9, false);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Fraction(MinuteOfHour,1,9)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendFraction_4arg_nullRule() throws Exception {
    try {
      builder.appendFraction(null, 1, 9, false);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendFraction_4arg_invalidRuleNotFixedSet() throws Exception {
    try {
      builder.appendFraction(DAY_OF_MONTH, 1, 9, false);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendFraction_4arg_minTooSmall() throws Exception {
    try {
      builder.appendFraction(MINUTE_OF_HOUR, -1, 9, false);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendFraction_4arg_minTooBig() throws Exception {
    try {
      builder.appendFraction(MINUTE_OF_HOUR, 10, 9, false);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendFraction_4arg_maxTooSmall() throws Exception {
    try {
      builder.appendFraction(MINUTE_OF_HOUR, 0, -1, false);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendFraction_4arg_maxTooBig() throws Exception {
    try {
      builder.appendFraction(MINUTE_OF_HOUR, 1, 10, false);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_appendFraction_4arg_maxWidthMinWidth() throws Exception {
    try {
      builder.appendFraction(MINUTE_OF_HOUR, 9, 3, false);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_appendText_1arg() throws Exception {
    builder.appendText(MONTH_OF_YEAR);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Text(MonthOfYear)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendText_1arg_null() throws Exception {
    try {
      builder.appendText(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_appendText_2arg() throws Exception {
    builder.appendText(MONTH_OF_YEAR, TextStyle.SHORT);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Text(MonthOfYear,SHORT)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendText_2arg_nullRule() throws Exception {
    try {
      builder.appendText(null, TextStyle.SHORT);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_appendText_2arg_nullStyle() throws Exception {
    try {
      builder.appendText(MONTH_OF_YEAR, (TextStyle) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_appendTextMap() throws Exception {
    Map<Long, String> map = new HashMap<Long, String>();
    map.put(1L, "JNY");
    map.put(2L, "FBY");
    map.put(3L, "MCH");
    map.put(4L, "APL");
    map.put(5L, "MAY");
    map.put(6L, "JUN");
    map.put(7L, "JLY");
    map.put(8L, "AGT");
    map.put(9L, "SPT");
    map.put(10L, "OBR");
    map.put(11L, "NVR");
    map.put(12L, "DBR");
    builder.appendText(MONTH_OF_YEAR, map);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Text(MonthOfYear)"); // TODO: toString should be different?
  }

  @Test(expected = NullPointerException.class)
  public void test_appendTextMap_nullRule() throws Exception {
    try {
      builder.appendText(null, new HashMap<Long, String>());
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_appendTextMap_nullStyle() throws Exception {
    try {
      builder.appendText(MONTH_OF_YEAR, (Map<Long, String>) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_appendOffsetId() throws Exception {
    builder.appendOffsetId();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Offset(+HH:MM:ss,'Z')");
  }

  // @DataProvider(name = "offsetPatterns")
  Object[][] data_offsetPatterns() {
    return new Object[][] {
      {"+HH"}, {"+HHMM"}, {"+HH:MM"}, {"+HHMMss"}, {"+HH:MM:ss"}, {"+HHMMSS"}, {"+HH:MM:SS"},
    };
  }

  @Test(/* dataProvider = "offsetPatterns" */ )
  public void test_appendOffset() throws Exception {
    Object[][] data = data_offsetPatterns();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_appendOffset((String) objects[0]);
    }
  }

  public void test_appendOffset(String pattern) throws Exception {
    builder.appendOffset(pattern, "Z");
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Offset(" + pattern + ",'Z')");
  }

  // @DataProvider(name = "badOffsetPatterns")
  Object[][] data_badOffsetPatterns() {
    return new Object[][] {
      {"HH"},
      {"HHMM"},
      {"HH:MM"},
      {"HHMMss"},
      {"HH:MM:ss"},
      {"HHMMSS"},
      {"HH:MM:SS"},
      {"+H"},
      {"+HMM"},
      {"+HHM"},
      {"+A"},
    };
  }

  @Test(/* dataProvider = "badOffsetPatterns", */ expected = IllegalArgumentException.class)
  public void test_appendOffset_badPattern() throws Exception {
    Object[][] data = data_badOffsetPatterns();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_appendOffset_badPattern((String) objects[0]);
    }
  }

  public void test_appendOffset_badPattern(String pattern) throws Exception {
    try {
      builder.appendOffset(pattern, "Z");
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_appendOffset_3arg_nullText() throws Exception {
    try {
      builder.appendOffset("+HH:MM", null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_appendOffset_3arg_nullPattern() throws Exception {
    try {
      builder.appendOffset(null, "Z");
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_appendZoneId() throws Exception {
    builder.appendZoneId();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ZoneId()");
  }

  @Test
  public void test_appendZoneText_1arg() throws Exception {
    builder.appendZoneText(TextStyle.FULL);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "ZoneText(FULL)");
  }

  @Test(expected = NullPointerException.class)
  public void test_appendZoneText_1arg_nullText() throws Exception {
    try {
      builder.appendZoneText(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_padNext_1arg() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .padNext(2)
        .appendValue(DAY_OF_MONTH)
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Pad(Value(DayOfMonth),2)Value(DayOfWeek)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_padNext_1arg_invalidWidth() throws Exception {
    try {
      builder.padNext(0);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_padNext_2arg_dash() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .padNext(2, '-')
        .appendValue(DAY_OF_MONTH)
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Pad(Value(DayOfMonth),2,'-')Value(DayOfWeek)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_padNext_2arg_invalidWidth() throws Exception {
    try {
      builder.padNext(0, '-');
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_padOptional() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .padNext(5)
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .optionalEnd()
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Pad([Value(DayOfMonth)],5)Value(DayOfWeek)");
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_optionalStart_noEnd() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)Value(DayOfWeek)]");
  }

  @Test
  public void test_optionalStart2_noEnd() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .optionalStart()
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)[Value(DayOfWeek)]]");
  }

  @Test
  public void test_optionalStart_doubleStart() throws Exception {
    builder.appendValue(MONTH_OF_YEAR).optionalStart().optionalStart().appendValue(DAY_OF_MONTH);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[[Value(DayOfMonth)]]");
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_optionalEnd() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .optionalEnd()
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)]Value(DayOfWeek)");
  }

  @Test
  public void test_optionalEnd2() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .optionalStart()
        .appendValue(DAY_OF_WEEK)
        .optionalEnd()
        .appendValue(DAY_OF_MONTH)
        .optionalEnd();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(
        f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)[Value(DayOfWeek)]Value(DayOfMonth)]");
  }

  @Test
  public void test_optionalEnd_doubleStartSingleEnd() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .optionalStart()
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .optionalEnd();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[[Value(DayOfMonth)]]");
  }

  @Test
  public void test_optionalEnd_doubleStartDoubleEnd() throws Exception {
    builder
        .appendValue(MONTH_OF_YEAR)
        .optionalStart()
        .optionalStart()
        .appendValue(DAY_OF_MONTH)
        .optionalEnd()
        .optionalEnd();
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[[Value(DayOfMonth)]]");
  }

  @Test
  public void test_optionalStartEnd_immediateStartEnd() throws Exception {
    builder.appendValue(MONTH_OF_YEAR).optionalStart().optionalEnd().appendValue(DAY_OF_MONTH);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Value(DayOfMonth)");
  }

  @Test(expected = IllegalStateException.class)
  public void test_optionalEnd_noStart() throws Exception {
    try {
      builder.optionalEnd();
      fail("Missing exception");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name = "validPatterns")
  Object[][] dataValid() {
    return new Object[][] {
      {"'a'", "'a'"},
      {"''", "''"},
      {"'!'", "'!'"},
      {"!", "'!'"},
      {"'hello_people,][)('", "'hello_people,][)('"},
      {"'hi'", "'hi'"},
      {"'yyyy'", "'yyyy'"},
      {"''''", "''"},
      {"'o''clock'", "'o''clock'"},
      {"G", "Text(Era,SHORT)"},
      {"GG", "Text(Era,SHORT)"},
      {"GGG", "Text(Era,SHORT)"},
      {"GGGG", "Text(Era)"},
      {"GGGGG", "Text(Era,NARROW)"},
      {"u", "Value(Year)"},
      {"uu", "ReducedValue(Year,2,2,2000-01-01)"},
      {"uuu", "Value(Year,3,19,NORMAL)"},
      {"uuuu", "Value(Year,4,19,EXCEEDS_PAD)"},
      {"uuuuu", "Value(Year,5,19,EXCEEDS_PAD)"},
      {"y", "Value(YearOfEra)"},
      {"yy", "ReducedValue(YearOfEra,2,2,2000-01-01)"},
      {"yyy", "Value(YearOfEra,3,19,NORMAL)"},
      {"yyyy", "Value(YearOfEra,4,19,EXCEEDS_PAD)"},
      {"yyyyy", "Value(YearOfEra,5,19,EXCEEDS_PAD)"},

      //            {"Y", "Value(WeekBasedYear)"},
      //            {"YY", "ReducedValue(WeekBasedYear,2,2000)"},
      //            {"YYY", "Value(WeekBasedYear,3,19,NORMAL)"},
      //            {"YYYY", "Value(WeekBasedYear,4,19,EXCEEDS_PAD)"},
      //            {"YYYYY", "Value(WeekBasedYear,5,19,EXCEEDS_PAD)"},

      {"M", "Value(MonthOfYear)"},
      {"MM", "Value(MonthOfYear,2)"},
      {"MMM", "Text(MonthOfYear,SHORT)"},
      {"MMMM", "Text(MonthOfYear)"},
      {"MMMMM", "Text(MonthOfYear,NARROW)"},

      //            {"w", "Value(WeekOfWeekBasedYear)"},
      //            {"ww", "Value(WeekOfWeekBasedYear,2)"},
      //            {"www", "Value(WeekOfWeekBasedYear,3)"},

      {"D", "Value(DayOfYear)"},
      {"DD", "Value(DayOfYear,2)"},
      {"DDD", "Value(DayOfYear,3)"},
      {"d", "Value(DayOfMonth)"},
      {"dd", "Value(DayOfMonth,2)"},
      {"F", "Value(AlignedDayOfWeekInMonth)"},
      {"E", "Text(DayOfWeek,SHORT)"},
      {"EE", "Text(DayOfWeek,SHORT)"},
      {"EEE", "Text(DayOfWeek,SHORT)"},
      {"EEEE", "Text(DayOfWeek)"},
      {"EEEEE", "Text(DayOfWeek,NARROW)"},
      {"a", "Text(AmPmOfDay,SHORT)"},
      {"H", "Value(HourOfDay)"},
      {"HH", "Value(HourOfDay,2)"},
      {"K", "Value(HourOfAmPm)"},
      {"KK", "Value(HourOfAmPm,2)"},
      {"k", "Value(ClockHourOfDay)"},
      {"kk", "Value(ClockHourOfDay,2)"},
      {"h", "Value(ClockHourOfAmPm)"},
      {"hh", "Value(ClockHourOfAmPm,2)"},
      {"m", "Value(MinuteOfHour)"},
      {"mm", "Value(MinuteOfHour,2)"},
      {"s", "Value(SecondOfMinute)"},
      {"ss", "Value(SecondOfMinute,2)"},
      {"S", "Fraction(NanoOfSecond,1,1)"},
      {"SS", "Fraction(NanoOfSecond,2,2)"},
      {"SSS", "Fraction(NanoOfSecond,3,3)"},
      {"SSSSSSSSS", "Fraction(NanoOfSecond,9,9)"},
      {"A", "Value(MilliOfDay)"},
      {"AA", "Value(MilliOfDay,2)"},
      {"AAA", "Value(MilliOfDay,3)"},
      {"n", "Value(NanoOfSecond)"},
      {"nn", "Value(NanoOfSecond,2)"},
      {"nnn", "Value(NanoOfSecond,3)"},
      {"N", "Value(NanoOfDay)"},
      {"NN", "Value(NanoOfDay,2)"},
      {"NNN", "Value(NanoOfDay,3)"},
      {"z", "ZoneText(SHORT)"},
      {"zz", "ZoneText(SHORT)"},
      {"zzz", "ZoneText(SHORT)"},
      {"zzzz", "ZoneText(FULL)"},
      {"VV", "ZoneId()"},
      {"Z", "Offset(+HHMM,'+0000')"}, // SimpleDateFormat compatible
      {"ZZ", "Offset(+HHMM,'+0000')"},
      {"ZZZ", "Offset(+HHMM,'+0000')"},
      {"X", "Offset(+HHmm,'Z')"},
      {"XX", "Offset(+HHMM,'Z')"},
      {"XXX", "Offset(+HH:MM,'Z')"},
      {"XXXX", "Offset(+HHMMss,'Z')"},
      {"XXXXX", "Offset(+HH:MM:ss,'Z')"},
      {"x", "Offset(+HHmm,'+00')"},
      {"xx", "Offset(+HHMM,'+0000')"},
      {"xxx", "Offset(+HH:MM,'+00:00')"},
      {"xxxx", "Offset(+HHMMss,'+0000')"},
      {"xxxxx", "Offset(+HH:MM:ss,'+00:00')"},
      {"ppH", "Pad(Value(HourOfDay),2)"},
      {"pppDD", "Pad(Value(DayOfYear,2),3)"},
      {
        "uuuu[-MM[-dd",
        "Value(Year,4,19,EXCEEDS_PAD)['-'Value(MonthOfYear,2)['-'Value(DayOfMonth,2)]]"
      },
      {
        "uuuu[-MM[-dd]]",
        "Value(Year,4,19,EXCEEDS_PAD)['-'Value(MonthOfYear,2)['-'Value(DayOfMonth,2)]]"
      },
      {
        "uuuu[-MM[]-dd]",
        "Value(Year,4,19,EXCEEDS_PAD)['-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)]"
      },
      {
        "uuuu-MM-dd'T'HH:mm:ss.SSS",
        "Value(Year,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)"
            + "'T'Value(HourOfDay,2)':'Value(MinuteOfHour,2)':'Value(SecondOfMinute,2)'.'Fraction(NanoOfSecond,3,3)"
      },
    };
  }

  @Test(/* dataProvider = "validPatterns" */ )
  public void test_appendPattern_valid() throws Exception {
    Object[][] data = dataValid();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_appendPattern_valid((String) objects[0], (String) objects[1]);
    }
  }

  public void test_appendPattern_valid(String input, String expected) throws Exception {
    builder.appendPattern(input);
    DateTimeFormatter f = builder.toFormatter();
    assertEquals(f.toString(), expected);
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "invalidPatterns")
  Object[][] dataInvalid() {
    return new Object[][] {
      {"'"},
      {"'hello"},
      {"'hel''lo"},
      {"'hello''"},
      {"]"},
      {"{"},
      {"}"},
      {"#"},
      {"yyyy]"},
      {"yyyy]MM"},
      {"yyyy[MM]]"},
      {"MMMMMM"},
      {"QQQQQQ"},
      {"EEEEEE"},
      {"aaaaaa"},
      {"XXXXXX"},
      {"RO"},
      {"p"},
      {"pp"},
      {"p:"},
      {"f"},
      {"ff"},
      {"f:"},
      {"fy"},
      {"fa"},
      {"fM"},
      {"ddd"},
      {"FF"},
      {"FFF"},
      {"aa"},
      {"aaa"},
      {"aaaa"},
      {"aaaaa"},
      {"HHH"},
      {"KKK"},
      {"kkk"},
      {"hhh"},
      {"mmm"},
      {"sss"},
    };
  }

  @Test(/* dataProvider = "invalidPatterns", */ expected = IllegalArgumentException.class)
  public void test_appendPattern_invalid() throws Exception {
    Object[][] data = dataInvalid();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_appendPattern_invalid((String) objects[0]);
    }
  }

  public void test_appendPattern_invalid(String input) throws Exception {
    try {
      try {
        builder.appendPattern(input);
      } catch (IllegalArgumentException ex) {
        throw ex;
      }
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
