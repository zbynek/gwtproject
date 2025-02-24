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
package org.jresearch.threetenbp.gwt.time.client.format;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.jresearch.threetenbp.gwt.time.client.AbstractTest;
import org.junit.Test;

/** Test DateTimeFormatters. */
// @Test
public class TestDateTimeFormatters extends AbstractTest {

  //    @BeforeMethod
  //    public void setUp() {
  //    }

  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_print_nullCalendrical() {
    try {
      DateTimeFormatter.ISO_DATE.format((TemporalAccessor) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_pattern_String() {
    DateTimeFormatter test = DateTimeFormatter.ofPattern("d MMM uuuu");
    assertEquals(
        test.toString(),
        "Value(DayOfMonth)' 'Text(MonthOfYear,SHORT)' 'Value(Year,4,19,EXCEEDS_PAD)");
    assertEquals(test.getLocale(), Locale.getDefault());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_pattern_String_invalid() {
    try {
      DateTimeFormatter.ofPattern("p");
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_pattern_String_null() {
    try {
      DateTimeFormatter.ofPattern(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_pattern_StringLocale() {
    DateTimeFormatter test = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.UK);
    assertEquals(
        test.toString(),
        "Value(DayOfMonth)' 'Text(MonthOfYear,SHORT)' 'Value(Year,4,19,EXCEEDS_PAD)");
    assertEquals(test.getLocale(), Locale.UK);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_pattern_StringLocale_invalid() {
    try {
      DateTimeFormatter.ofPattern("p", Locale.UK);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_pattern_StringLocale_nullPattern() {
    try {
      DateTimeFormatter.ofPattern(null, Locale.UK);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_pattern_StringLocale_nullLocale() {
    try {
      DateTimeFormatter.ofPattern("yyyy", null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoLocalDate")
  Object[][] provider_sample_isoLocalDate() {
    return new Object[][] {
      {2008, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, DateTimeException.class},
      {null, null, null, "+01:00", null, null, DateTimeException.class},
      {null, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, null, null, null, null, DateTimeException.class},
      {null, 6, 30, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, null, "2008-06-30", null},
      {2008, 6, 30, "+01:00", null, "2008-06-30", null},
      {2008, 6, 30, "+01:00", "Europe/Paris", "2008-06-30", null},
      {2008, 6, 30, null, "Europe/Paris", "2008-06-30", null},
      {123456, 6, 30, null, null, "+123456-06-30", null},
    };
  }

  @Test(/* dataProvider = "sample_isoLocalDate" */ )
  public void test_print_isoLocalDate() throws Exception {
    Object[][] data = provider_sample_isoLocalDate();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoLocalDate(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (String) objects[3],
          (String) objects[4],
          (String) objects[5],
          (Class<?>) objects[6]);
    }
  }

  public void test_print_isoLocalDate(
      Integer year,
      Integer month,
      Integer day,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test =
        buildAccessor(year, month, day, null, null, null, null, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_LOCAL_DATE.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoLocalDate" */ )
  public void test_parse_isoLocalDate() throws Exception {
    Object[][] data = provider_sample_isoLocalDate();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoLocalDate(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (String) objects[3],
          (String) objects[4],
          (String) objects[5],
          (Class<?>) objects[6]);
    }
  }

  public void test_parse_isoLocalDate(
      Integer year,
      Integer month,
      Integer day,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDate(year, month, day);
      // offset/zone not expected to be parsed
      assertParseMatch(
          DateTimeFormatter.ISO_LOCAL_DATE.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  @Test
  public void test_parse_isoLocalDate_999999999() {
    Expected expected = createDate(999999999, 8, 6);
    assertParseMatch(
        DateTimeFormatter.ISO_LOCAL_DATE.parseUnresolved("+999999999-08-06", new ParsePosition(0)),
        expected);
    assertEquals(LocalDate.parse("+999999999-08-06"), LocalDate.of(999999999, 8, 6));
  }

  @Test
  public void test_parse_isoLocalDate_1000000000() {
    Expected expected = createDate(1000000000, 8, 6);
    assertParseMatch(
        DateTimeFormatter.ISO_LOCAL_DATE.parseUnresolved("+1000000000-08-06", new ParsePosition(0)),
        expected);
  }

  @Test(expected = DateTimeException.class)
  public void test_parse_isoLocalDate_1000000000_failedCreate() {
    try {
      LocalDate.parse("+1000000000-08-06");
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test
  public void test_parse_isoLocalDate_M999999999() {
    Expected expected = createDate(-999999999, 8, 6);
    assertParseMatch(
        DateTimeFormatter.ISO_LOCAL_DATE.parseUnresolved("-999999999-08-06", new ParsePosition(0)),
        expected);
    assertEquals(LocalDate.parse("-999999999-08-06"), LocalDate.of(-999999999, 8, 6));
  }

  @Test
  public void test_parse_isoLocalDate_M1000000000() {
    Expected expected = createDate(-1000000000, 8, 6);
    assertParseMatch(
        DateTimeFormatter.ISO_LOCAL_DATE.parseUnresolved("-1000000000-08-06", new ParsePosition(0)),
        expected);
  }

  @Test(expected = DateTimeException.class)
  public void test_parse_isoLocalDate_M1000000000_failedCreate() {
    try {
      LocalDate.parse("-1000000000-08-06");
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoOffsetDate")
  Object[][] provider_sample_isoOffsetDate() {
    return new Object[][] {
      {2008, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, DateTimeException.class},
      {null, null, null, "+01:00", null, null, DateTimeException.class},
      {null, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, null, null, null, null, DateTimeException.class},
      {null, 6, 30, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, null, null, DateTimeException.class},
      {2008, 6, 30, "+01:00", null, "2008-06-30+01:00", null},
      {2008, 6, 30, "+01:00", "Europe/Paris", "2008-06-30+01:00", null},
      {2008, 6, 30, null, "Europe/Paris", null, DateTimeException.class},
      {123456, 6, 30, "+01:00", null, "+123456-06-30+01:00", null},
    };
  }

  @Test(/* dataProvider = "sample_isoOffsetDate" */ )
  public void test_print_isoOffsetDate() throws Exception {
    Object[][] data = provider_sample_isoOffsetDate();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoOffsetDate(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (String) objects[3],
          (String) objects[4],
          (String) objects[5],
          (Class<?>) objects[6]);
    }
  }

  public void test_print_isoOffsetDate(
      Integer year,
      Integer month,
      Integer day,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test =
        buildAccessor(year, month, day, null, null, null, null, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_OFFSET_DATE.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_OFFSET_DATE.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoOffsetDate" */ )
  public void test_parse_isoOffsetDate() throws Exception {
    Object[][] data = provider_sample_isoOffsetDate();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoOffsetDate(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (String) objects[3],
          (String) objects[4],
          (String) objects[5],
          (Class<?>) objects[6]);
    }
  }

  public void test_parse_isoOffsetDate(
      Integer year,
      Integer month,
      Integer day,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDate(year, month, day);
      buildCalendrical(expected, offsetId, null); // zone not expected to be parsed
      assertParseMatch(
          DateTimeFormatter.ISO_OFFSET_DATE.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoDate")
  Object[][] provider_sample_isoDate() {
    return new Object[][] {
      {2008, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, DateTimeException.class},
      {null, null, null, "+01:00", null, null, DateTimeException.class},
      {null, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, null, null, null, null, DateTimeException.class},
      {null, 6, 30, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, null, "2008-06-30", null},
      {2008, 6, 30, "+01:00", null, "2008-06-30+01:00", null},
      {2008, 6, 30, "+01:00", "Europe/Paris", "2008-06-30+01:00", null},
      {2008, 6, 30, null, "Europe/Paris", "2008-06-30", null},
      {123456, 6, 30, "+01:00", "Europe/Paris", "+123456-06-30+01:00", null},
    };
  }

  @Test(/* dataProvider = "sample_isoDate" */ )
  public void test_print_isoDate() throws Exception {
    Object[][] data = provider_sample_isoDate();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoDate(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (String) objects[3],
          (String) objects[4],
          (String) objects[5],
          (Class<?>) objects[6]);
    }
  }

  public void test_print_isoDate(
      Integer year,
      Integer month,
      Integer day,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test =
        buildAccessor(year, month, day, null, null, null, null, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_DATE.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_DATE.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoDate" */ )
  public void test_parse_isoDate() throws Exception {
    Object[][] data = provider_sample_isoDate();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoDate(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (String) objects[3],
          (String) objects[4],
          (String) objects[5],
          (Class<?>) objects[6]);
    }
  }

  public void test_parse_isoDate(
      Integer year,
      Integer month,
      Integer day,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDate(year, month, day);
      if (offsetId != null) {
        expected.fieldValues.put(OFFSET_SECONDS, (long) ZoneOffset.of(offsetId).getTotalSeconds());
      }
      assertParseMatch(
          DateTimeFormatter.ISO_DATE.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoLocalTime")
  Object[][] provider_sample_isoLocalTime() {
    return new Object[][] {
      {11, null, null, null, null, null, null, DateTimeException.class},
      {null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, DateTimeException.class},
      {null, null, null, 1, null, null, null, DateTimeException.class},
      {null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {null, null, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {11, 5, null, null, null, null, "11:05", null},
      {11, 5, 30, null, null, null, "11:05:30", null},
      {11, 5, 30, 500000000, null, null, "11:05:30.5", null},
      {11, 5, 30, 1, null, null, "11:05:30.000000001", null},
      {11, 5, null, null, "+01:00", null, "11:05", null},
      {11, 5, 30, null, "+01:00", null, "11:05:30", null},
      {11, 5, 30, 500000000, "+01:00", null, "11:05:30.5", null},
      {11, 5, 30, 1, "+01:00", null, "11:05:30.000000001", null},
      {11, 5, null, null, "+01:00", "Europe/Paris", "11:05", null},
      {11, 5, 30, null, "+01:00", "Europe/Paris", "11:05:30", null},
      {11, 5, 30, 500000000, "+01:00", "Europe/Paris", "11:05:30.5", null},
      {11, 5, 30, 1, "+01:00", "Europe/Paris", "11:05:30.000000001", null},
      {11, 5, null, null, null, "Europe/Paris", "11:05", null},
      {11, 5, 30, null, null, "Europe/Paris", "11:05:30", null},
      {11, 5, 30, 500000000, null, "Europe/Paris", "11:05:30.5", null},
      {11, 5, 30, 1, null, "Europe/Paris", "11:05:30.000000001", null},
    };
  }

  @Test(/* dataProvider = "sample_isoLocalTime" */ )
  public void test_print_isoLocalTime() throws Exception {
    Object[][] data = provider_sample_isoLocalTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoLocalTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4],
          (String) objects[5],
          (String) objects[6],
          (Class<?>) objects[7]);
    }
  }

  public void test_print_isoLocalTime(
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(null, null, null, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_LOCAL_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_LOCAL_TIME.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoLocalTime" */ )
  public void test_parse_isoLocalTime() throws Exception {
    Object[][] data = provider_sample_isoLocalTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoLocalTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4],
          (String) objects[5],
          (String) objects[6],
          (Class<?>) objects[7]);
    }
  }

  public void test_parse_isoLocalTime(
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createTime(hour, min, sec, nano);
      // offset/zone not expected to be parsed
      assertParseMatch(
          DateTimeFormatter.ISO_LOCAL_TIME.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoOffsetTime")
  Object[][] provider_sample_isoOffsetTime() {
    return new Object[][] {
      {11, null, null, null, null, null, null, DateTimeException.class},
      {null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, DateTimeException.class},
      {null, null, null, 1, null, null, null, DateTimeException.class},
      {null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {null, null, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {11, 5, null, null, null, null, null, DateTimeException.class},
      {11, 5, 30, null, null, null, null, DateTimeException.class},
      {11, 5, 30, 500000000, null, null, null, DateTimeException.class},
      {11, 5, 30, 1, null, null, null, DateTimeException.class},
      {11, 5, null, null, "+01:00", null, "11:05+01:00", null},
      {11, 5, 30, null, "+01:00", null, "11:05:30+01:00", null},
      {11, 5, 30, 500000000, "+01:00", null, "11:05:30.5+01:00", null},
      {11, 5, 30, 1, "+01:00", null, "11:05:30.000000001+01:00", null},
      {11, 5, null, null, "+01:00", "Europe/Paris", "11:05+01:00", null},
      {11, 5, 30, null, "+01:00", "Europe/Paris", "11:05:30+01:00", null},
      {11, 5, 30, 500000000, "+01:00", "Europe/Paris", "11:05:30.5+01:00", null},
      {11, 5, 30, 1, "+01:00", "Europe/Paris", "11:05:30.000000001+01:00", null},
      {11, 5, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {11, 5, 30, null, null, "Europe/Paris", null, DateTimeException.class},
      {11, 5, 30, 500000000, null, "Europe/Paris", null, DateTimeException.class},
      {11, 5, 30, 1, null, "Europe/Paris", null, DateTimeException.class},
    };
  }

  @Test(/* dataProvider = "sample_isoOffsetTime" */ )
  public void test_print_isoOffsetTime() throws Exception {
    Object[][] data = provider_sample_isoOffsetTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoOffsetTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4],
          (String) objects[5],
          (String) objects[6],
          (Class<?>) objects[7]);
    }
  }

  public void test_print_isoOffsetTime(
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(null, null, null, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_OFFSET_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_OFFSET_TIME.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoOffsetTime" */ )
  public void test_parse_isoOffsetTime() throws Exception {
    Object[][] data = provider_sample_isoOffsetTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoOffsetTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4],
          (String) objects[5],
          (String) objects[6],
          (Class<?>) objects[7]);
    }
  }

  public void test_parse_isoOffsetTime(
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createTime(hour, min, sec, nano);
      buildCalendrical(expected, offsetId, null); // zoneId is not expected from parse
      assertParseMatch(
          DateTimeFormatter.ISO_OFFSET_TIME.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoTime")
  Object[][] provider_sample_isoTime() {
    return new Object[][] {
      {11, null, null, null, null, null, null, DateTimeException.class},
      {null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, DateTimeException.class},
      {null, null, null, 1, null, null, null, DateTimeException.class},
      {null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {null, null, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {11, 5, null, null, null, null, "11:05", null},
      {11, 5, 30, null, null, null, "11:05:30", null},
      {11, 5, 30, 500000000, null, null, "11:05:30.5", null},
      {11, 5, 30, 1, null, null, "11:05:30.000000001", null},
      {11, 5, null, null, "+01:00", null, "11:05+01:00", null},
      {11, 5, 30, null, "+01:00", null, "11:05:30+01:00", null},
      {11, 5, 30, 500000000, "+01:00", null, "11:05:30.5+01:00", null},
      {11, 5, 30, 1, "+01:00", null, "11:05:30.000000001+01:00", null},
      {11, 5, null, null, "+01:00", "Europe/Paris", "11:05+01:00", null},
      {11, 5, 30, null, "+01:00", "Europe/Paris", "11:05:30+01:00", null},
      {11, 5, 30, 500000000, "+01:00", "Europe/Paris", "11:05:30.5+01:00", null},
      {11, 5, 30, 1, "+01:00", "Europe/Paris", "11:05:30.000000001+01:00", null},
      {11, 5, null, null, null, "Europe/Paris", "11:05", null},
      {11, 5, 30, null, null, "Europe/Paris", "11:05:30", null},
      {11, 5, 30, 500000000, null, "Europe/Paris", "11:05:30.5", null},
      {11, 5, 30, 1, null, "Europe/Paris", "11:05:30.000000001", null},
    };
  }

  @Test(/* dataProvider = "sample_isoTime" */ )
  public void test_print_isoTime() throws Exception {
    Object[][] data = provider_sample_isoTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4],
          (String) objects[5],
          (String) objects[6],
          (Class<?>) objects[7]);
    }
  }

  public void test_print_isoTime(
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(null, null, null, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_TIME.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoTime" */ )
  public void test_parse_isoTime() throws Exception {
    Object[][] data = provider_sample_isoTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4],
          (String) objects[5],
          (String) objects[6],
          (Class<?>) objects[7]);
    }
  }

  public void test_parse_isoTime(
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createTime(hour, min, sec, nano);
      if (offsetId != null) {
        expected.fieldValues.put(OFFSET_SECONDS, (long) ZoneOffset.of(offsetId).getTotalSeconds());
      }
      assertParseMatch(
          DateTimeFormatter.ISO_TIME.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoLocalDateTime")
  Object[][] provider_sample_isoLocalDateTime() {
    return new Object[][] {
      {2008, null, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, 11, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Europe/Paris",
        null,
        DateTimeException.class
      },
      {2008, 6, 30, 11, null, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, null, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, null, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {null, 6, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, null, null, null, null, "2008-06-30T11:05", null},
      {2008, 6, 30, 11, 5, 30, null, null, null, "2008-06-30T11:05:30", null},
      {2008, 6, 30, 11, 5, 30, 500000000, null, null, "2008-06-30T11:05:30.5", null},
      {2008, 6, 30, 11, 5, 30, 1, null, null, "2008-06-30T11:05:30.000000001", null},
      {2008, 6, 30, 11, 5, null, null, "+01:00", null, "2008-06-30T11:05", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", null, "2008-06-30T11:05:30", null},
      {2008, 6, 30, 11, 5, 30, 500000000, "+01:00", null, "2008-06-30T11:05:30.5", null},
      {2008, 6, 30, 11, 5, 30, 1, "+01:00", null, "2008-06-30T11:05:30.000000001", null},
      {2008, 6, 30, 11, 5, null, null, "+01:00", "Europe/Paris", "2008-06-30T11:05", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", "Europe/Paris", "2008-06-30T11:05:30", null},
      {2008, 6, 30, 11, 5, 30, 500000000, "+01:00", "Europe/Paris", "2008-06-30T11:05:30.5", null},
      {2008, 6, 30, 11, 5, 30, 1, "+01:00", "Europe/Paris", "2008-06-30T11:05:30.000000001", null},
      {2008, 6, 30, 11, 5, null, null, null, "Europe/Paris", "2008-06-30T11:05", null},
      {2008, 6, 30, 11, 5, 30, null, null, "Europe/Paris", "2008-06-30T11:05:30", null},
      {2008, 6, 30, 11, 5, 30, 500000000, null, "Europe/Paris", "2008-06-30T11:05:30.5", null},
      {2008, 6, 30, 11, 5, 30, 1, null, "Europe/Paris", "2008-06-30T11:05:30.000000001", null},
      {123456, 6, 30, 11, 5, null, null, null, null, "+123456-06-30T11:05", null},
    };
  }

  @Test(/* dataProvider = "sample_isoLocalDateTime" */ )
  public void test_print_isoLocalDateTime() throws Exception {
    Object[][] data = provider_sample_isoLocalDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoLocalDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_print_isoLocalDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(year, month, day, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoLocalDateTime" */ )
  public void test_parse_isoLocalDateTime() throws Exception {
    Object[][] data = provider_sample_isoLocalDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoLocalDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_parse_isoLocalDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDateTime(year, month, day, hour, min, sec, nano);
      assertParseMatch(
          DateTimeFormatter.ISO_LOCAL_DATE_TIME.parseUnresolved(input, new ParsePosition(0)),
          expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoOffsetDateTime")
  Object[][] provider_sample_isoOffsetDateTime() {
    return new Object[][] {
      {2008, null, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, 11, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Europe/Paris",
        null,
        DateTimeException.class
      },
      {2008, 6, 30, 11, null, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, null, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, null, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {null, 6, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 500000000, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 1, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, null, null, "+01:00", null, "2008-06-30T11:05+01:00", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", null, "2008-06-30T11:05:30+01:00", null},
      {2008, 6, 30, 11, 5, 30, 500000000, "+01:00", null, "2008-06-30T11:05:30.5+01:00", null},
      {2008, 6, 30, 11, 5, 30, 1, "+01:00", null, "2008-06-30T11:05:30.000000001+01:00", null},
      {2008, 6, 30, 11, 5, null, null, "+01:00", "Europe/Paris", "2008-06-30T11:05+01:00", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", "Europe/Paris", "2008-06-30T11:05:30+01:00", null},
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        500000000,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30.5+01:00",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        1,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30.000000001+01:00",
        null
      },
      {2008, 6, 30, 11, 5, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 500000000, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 1, null, "Europe/Paris", null, DateTimeException.class},
      {123456, 6, 30, 11, 5, null, null, "+01:00", null, "+123456-06-30T11:05+01:00", null},
    };
  }

  @Test(/* dataProvider = "sample_isoOffsetDateTime" */ )
  public void test_print_isoOffsetDateTime() throws Exception {
    Object[][] data = provider_sample_isoOffsetDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoOffsetDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_print_isoOffsetDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(year, month, day, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoOffsetDateTime" */ )
  public void test_parse_isoOffsetDateTime() throws Exception {
    Object[][] data = provider_sample_isoOffsetDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoOffsetDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_parse_isoOffsetDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDateTime(year, month, day, hour, min, sec, nano);
      buildCalendrical(expected, offsetId, null); // zone not expected to be parsed
      assertParseMatch(
          DateTimeFormatter.ISO_OFFSET_DATE_TIME.parseUnresolved(input, new ParsePosition(0)),
          expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoZonedDateTime")
  Object[][] provider_sample_isoZonedDateTime() {
    return new Object[][] {
      {2008, null, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, 11, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Europe/Paris",
        null,
        DateTimeException.class
      },
      {2008, 6, 30, 11, null, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, null, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, null, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {null, 6, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 500000000, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 1, null, null, null, DateTimeException.class},

      // allow OffsetDateTime (no harm comes of this AFAICT)
      {2008, 6, 30, 11, 5, null, null, "+01:00", null, "2008-06-30T11:05+01:00", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", null, "2008-06-30T11:05:30+01:00", null},
      {2008, 6, 30, 11, 5, 30, 500000000, "+01:00", null, "2008-06-30T11:05:30.5+01:00", null},
      {2008, 6, 30, 11, 5, 30, 1, "+01:00", null, "2008-06-30T11:05:30.000000001+01:00", null},

      // ZonedDateTime with ZoneId of ZoneOffset
      {2008, 6, 30, 11, 5, null, null, "+01:00", "+01:00", "2008-06-30T11:05+01:00", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", "+01:00", "2008-06-30T11:05:30+01:00", null},
      {2008, 6, 30, 11, 5, 30, 500000000, "+01:00", "+01:00", "2008-06-30T11:05:30.5+01:00", null},
      {2008, 6, 30, 11, 5, 30, 1, "+01:00", "+01:00", "2008-06-30T11:05:30.000000001+01:00", null},

      // ZonedDateTime with ZoneId of ZoneRegion
      {
        2008,
        6,
        30,
        11,
        5,
        null,
        null,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05+01:00[Europe/Paris]",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        null,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30+01:00[Europe/Paris]",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        500000000,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30.5+01:00[Europe/Paris]",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        1,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30.000000001+01:00[Europe/Paris]",
        null
      },

      // offset required
      {2008, 6, 30, 11, 5, null, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, null, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 500000000, null, "Europe/Paris", null, DateTimeException.class},
      {2008, 6, 30, 11, 5, 30, 1, null, "Europe/Paris", null, DateTimeException.class},
      {
        123456,
        6,
        30,
        11,
        5,
        null,
        null,
        "+01:00",
        "Europe/Paris",
        "+123456-06-30T11:05+01:00[Europe/Paris]",
        null
      },
    };
  }

  @Test(/* dataProvider = "sample_isoZonedDateTime" */ )
  public void test_print_isoZonedDateTime() throws Exception {
    Object[][] data = provider_sample_isoZonedDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoZonedDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_print_isoZonedDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(year, month, day, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_ZONED_DATE_TIME.format(test);
        fail(test.toString());
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoZonedDateTime" */ )
  public void test_parse_isoZonedDateTime() throws Exception {
    Object[][] data = provider_sample_isoZonedDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoZonedDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_parse_isoZonedDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDateTime(year, month, day, hour, min, sec, nano);
      if (offsetId.equals(zoneId)) {
        buildCalendrical(expected, offsetId, null);
      } else {
        buildCalendrical(expected, offsetId, zoneId);
      }
      assertParseMatch(
          DateTimeFormatter.ISO_ZONED_DATE_TIME.parseUnresolved(input, new ParsePosition(0)),
          expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="sample_isoDateTime")
  Object[][] provider_sample_isoDateTime() {
    return new Object[][] {
      {2008, null, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, 6, null, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, 30, null, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, 11, null, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, 5, null, null, null, null, null, DateTimeException.class},
      {null, null, null, null, null, null, null, "+01:00", null, null, DateTimeException.class},
      {
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Europe/Paris",
        null,
        DateTimeException.class
      },
      {2008, 6, 30, 11, null, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, null, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, null, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, null, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {null, 6, 30, 11, 5, null, null, null, null, null, DateTimeException.class},
      {2008, 6, 30, 11, 5, null, null, null, null, "2008-06-30T11:05", null},
      {2008, 6, 30, 11, 5, 30, null, null, null, "2008-06-30T11:05:30", null},
      {2008, 6, 30, 11, 5, 30, 500000000, null, null, "2008-06-30T11:05:30.5", null},
      {2008, 6, 30, 11, 5, 30, 1, null, null, "2008-06-30T11:05:30.000000001", null},
      {2008, 6, 30, 11, 5, null, null, "+01:00", null, "2008-06-30T11:05+01:00", null},
      {2008, 6, 30, 11, 5, 30, null, "+01:00", null, "2008-06-30T11:05:30+01:00", null},
      {2008, 6, 30, 11, 5, 30, 500000000, "+01:00", null, "2008-06-30T11:05:30.5+01:00", null},
      {2008, 6, 30, 11, 5, 30, 1, "+01:00", null, "2008-06-30T11:05:30.000000001+01:00", null},
      {
        2008,
        6,
        30,
        11,
        5,
        null,
        null,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05+01:00[Europe/Paris]",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        null,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30+01:00[Europe/Paris]",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        500000000,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30.5+01:00[Europe/Paris]",
        null
      },
      {
        2008,
        6,
        30,
        11,
        5,
        30,
        1,
        "+01:00",
        "Europe/Paris",
        "2008-06-30T11:05:30.000000001+01:00[Europe/Paris]",
        null
      },
      {2008, 6, 30, 11, 5, null, null, null, "Europe/Paris", "2008-06-30T11:05", null},
      {2008, 6, 30, 11, 5, 30, null, null, "Europe/Paris", "2008-06-30T11:05:30", null},
      {2008, 6, 30, 11, 5, 30, 500000000, null, "Europe/Paris", "2008-06-30T11:05:30.5", null},
      {2008, 6, 30, 11, 5, 30, 1, null, "Europe/Paris", "2008-06-30T11:05:30.000000001", null},
      {123456, 6, 30, 11, 5, null, null, null, null, "+123456-06-30T11:05", null},
    };
  }

  @Test(/* dataProvider = "sample_isoDateTime" */ )
  public void test_print_isoDateTime() throws Exception {
    Object[][] data = provider_sample_isoDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_isoDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_print_isoDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String expected,
      Class<?> expectedEx) {
    TemporalAccessor test = buildAccessor(year, month, day, hour, min, sec, nano, offsetId, zoneId);
    if (expectedEx == null) {
      assertEquals(DateTimeFormatter.ISO_DATE_TIME.format(test), expected);
    } else {
      try {
        DateTimeFormatter.ISO_DATE_TIME.format(test);
        fail();
      } catch (Exception ex) {
        // GWT Specific
        assertEquals(expectedEx.getName(), ex.getClass().getName());
      }
    }
  }

  @Test(/* dataProvider = "sample_isoDateTime" */ )
  public void test_parse_isoDateTime() throws Exception {
    Object[][] data = provider_sample_isoDateTime();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_isoDateTime(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4],
          (Integer) objects[5],
          (Integer) objects[6],
          (String) objects[7],
          (String) objects[8],
          (String) objects[9],
          (Class<?>) objects[10]);
    }
  }

  public void test_parse_isoDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId,
      String input,
      Class<?> invalid) {
    if (input != null) {
      Expected expected = createDateTime(year, month, day, hour, min, sec, nano);
      if (offsetId != null) {
        expected.fieldValues.put(OFFSET_SECONDS, (long) ZoneOffset.of(offsetId).getTotalSeconds());
        if (zoneId != null) {
          expected.zone = ZoneId.of(zoneId);
        }
      }
      assertParseMatch(
          DateTimeFormatter.ISO_DATE_TIME.parseUnresolved(input, new ParsePosition(0)), expected);
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_print_isoOrdinalDate() {
    TemporalAccessor test = buildAccessor(LocalDateTime.of(2008, 6, 3, 11, 5, 30), null, null);
    assertEquals(DateTimeFormatter.ISO_ORDINAL_DATE.format(test), "2008-155");
  }

  @Test
  public void test_print_isoOrdinalDate_offset() {
    TemporalAccessor test = buildAccessor(LocalDateTime.of(2008, 6, 3, 11, 5, 30), "Z", null);
    assertEquals(DateTimeFormatter.ISO_ORDINAL_DATE.format(test), "2008-155Z");
  }

  @Test
  public void test_print_isoOrdinalDate_zoned() {
    TemporalAccessor test =
        buildAccessor(LocalDateTime.of(2008, 6, 3, 11, 5, 30), "+02:00", "Europe/Paris");
    assertEquals(DateTimeFormatter.ISO_ORDINAL_DATE.format(test), "2008-155+02:00");
  }

  @Test
  public void test_print_isoOrdinalDate_zoned_largeYear() {
    TemporalAccessor test = buildAccessor(LocalDateTime.of(123456, 6, 3, 11, 5, 30), "Z", null);
    assertEquals(DateTimeFormatter.ISO_ORDINAL_DATE.format(test), "+123456-155Z");
  }

  @Test
  public void test_print_isoOrdinalDate_fields() {
    TemporalAccessor test =
        new TemporalAccessor() {
          @Override
          public boolean isSupported(TemporalField field) {
            return field == YEAR || field == DAY_OF_YEAR;
          }

          @Override
          public long getLong(TemporalField field) {
            if (field == YEAR) {
              return 2008;
            }
            if (field == DAY_OF_YEAR) {
              return 231;
            }
            throw new DateTimeException("Unsupported");
          }
        };
    assertEquals(DateTimeFormatter.ISO_ORDINAL_DATE.format(test), "2008-231");
  }

  @Test(expected = DateTimeException.class)
  public void test_print_isoOrdinalDate_missingField() {
    try {
      TemporalAccessor test = Year.of(2008);
      DateTimeFormatter.ISO_ORDINAL_DATE.format(test);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parse_isoOrdinalDate() {
    Expected expected = new Expected(YEAR, 2008, DAY_OF_YEAR, 123);
    assertParseMatch(
        DateTimeFormatter.ISO_ORDINAL_DATE.parseUnresolved("2008-123", new ParsePosition(0)),
        expected);
  }

  @Test
  public void test_parse_isoOrdinalDate_largeYear() {
    Expected expected = new Expected(YEAR, 123456, DAY_OF_YEAR, 123);
    assertParseMatch(
        DateTimeFormatter.ISO_ORDINAL_DATE.parseUnresolved("+123456-123", new ParsePosition(0)),
        expected);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test
  public void test_print_basicIsoDate() {
    TemporalAccessor test = buildAccessor(LocalDateTime.of(2008, 6, 3, 11, 5, 30), null, null);
    assertEquals(DateTimeFormatter.BASIC_ISO_DATE.format(test), "20080603");
  }

  @Test
  public void test_print_basicIsoDate_offset() {
    TemporalAccessor test = buildAccessor(LocalDateTime.of(2008, 6, 3, 11, 5, 30), "Z", null);
    assertEquals(DateTimeFormatter.BASIC_ISO_DATE.format(test), "20080603Z");
  }

  @Test
  public void test_print_basicIsoDate_zoned() {
    TemporalAccessor test =
        buildAccessor(LocalDateTime.of(2008, 6, 3, 11, 5, 30), "+02:00", "Europe/Paris");
    assertEquals(DateTimeFormatter.BASIC_ISO_DATE.format(test), "20080603+0200");
  }

  @Test(expected = DateTimeException.class)
  public void test_print_basicIsoDate_largeYear() {
    try {
      TemporalAccessor test = buildAccessor(LocalDateTime.of(123456, 6, 3, 11, 5, 30), "Z", null);
      DateTimeFormatter.BASIC_ISO_DATE.format(test);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test
  public void test_print_basicIsoDate_fields() {
    TemporalAccessor test = buildAccessor(LocalDate.of(2008, 6, 3), null, null);
    assertEquals(DateTimeFormatter.BASIC_ISO_DATE.format(test), "20080603");
  }

  @Test(expected = DateTimeException.class)
  public void test_print_basicIsoDate_missingField() {
    try {
      TemporalAccessor test = YearMonth.of(2008, 6);
      DateTimeFormatter.BASIC_ISO_DATE.format(test);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parse_basicIsoDate() {
    LocalDate expected = LocalDate.of(2008, 6, 3);
    assertEquals(DateTimeFormatter.BASIC_ISO_DATE.parse("20080603", LocalDate::from), expected);
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parse_basicIsoDate_largeYear() {
    try {
      try {
        LocalDate expected = LocalDate.of(123456, 6, 3);
        assertEquals(
            DateTimeFormatter.BASIC_ISO_DATE.parse("+1234560603", LocalDate::from), expected);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getErrorIndex(), 0);
        assertEquals(ex.getParsedString(), "+1234560603");
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="weekDate")
  Iterator<Object[]> weekDate() {
    return new Iterator<Object[]>() {
      private ZonedDateTime date =
          ZonedDateTime.of(LocalDateTime.of(2003, 12, 29, 11, 5, 30), ZoneId.of("Europe/Paris"));
      private ZonedDateTime endDate = date.withYear(2005).withMonth(1).withDayOfMonth(2);
      private int week = 1;
      private int day = 1;

      public boolean hasNext() {
        return !date.isAfter(endDate);
      }

      public Object[] next() {
        StringBuilder sb = new StringBuilder("2004-W");
        if (week < 10) {
          sb.append('0');
        }
        sb.append(week).append('-').append(day).append(date.getOffset());
        Object[] ret = new Object[] {date, sb.toString()};
        date = date.plusDays(1);
        day += 1;
        if (day == 8) {
          day = 1;
          week++;
        }
        return ret;
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(/* dataProvider = "weekDate" */ )
  public void test_print_isoWeekDate() throws Exception {
    Iterator<Object[]> data = weekDate();
    while (data.hasNext()) {
      Object[] objects = (Object[]) data.next();
      gwtSetUp();
      test_print_isoWeekDate((TemporalAccessor) objects[0], (String) objects[1]);
    }
  }

  public void test_print_isoWeekDate(TemporalAccessor test, String expected) {
    assertEquals(DateTimeFormatter.ISO_WEEK_DATE.format(test), expected);
  }

  @Test
  public void test_print_isoWeekDate_zoned_largeYear() {
    TemporalAccessor test = buildAccessor(LocalDateTime.of(123456, 6, 3, 11, 5, 30), "Z", null);
    assertEquals(DateTimeFormatter.ISO_WEEK_DATE.format(test), "+123456-W23-2Z");
  }

  @Test
  public void test_print_isoWeekDate_fields() {
    TemporalAccessor test = buildAccessor(LocalDate.of(2004, 1, 27), null, null);
    assertEquals(DateTimeFormatter.ISO_WEEK_DATE.format(test), "2004-W05-2");
  }

  @Test(expected = DateTimeException.class)
  public void test_print_isoWeekDate_missingField() {
    try {
      TemporalAccessor test = YearMonth.of(2008, 6);
      DateTimeFormatter.ISO_WEEK_DATE.format(test);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parse_weekDate() {
    LocalDate expected = LocalDate.of(2004, 1, 28);
    assertEquals(DateTimeFormatter.ISO_WEEK_DATE.parse("2004-W05-3", LocalDate::from), expected);
  }

  @Test
  public void test_parse_weekDate_largeYear() {
    TemporalAccessor parsed =
        DateTimeFormatter.ISO_WEEK_DATE.parseUnresolved("+123456-W04-5", new ParsePosition(0));
    assertEquals(parsed.get(IsoFields.WEEK_BASED_YEAR), 123456);
    assertEquals(parsed.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR), 4);
    assertEquals(parsed.get(DAY_OF_WEEK), 5);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // @DataProvider(name="rfc")
  Object[][] data_rfc() {
    return new Object[][] {
      {LocalDateTime.of(2008, 6, 3, 11, 5, 30), "Z", "Tue, 3 Jun 2008 11:05:30 GMT"},
      {LocalDateTime.of(2008, 6, 30, 11, 5, 30), "Z", "Mon, 30 Jun 2008 11:05:30 GMT"},
      {LocalDateTime.of(2008, 6, 3, 11, 5, 30), "+02:00", "Tue, 3 Jun 2008 11:05:30 +0200"},
      {LocalDateTime.of(2008, 6, 30, 11, 5, 30), "-03:00", "Mon, 30 Jun 2008 11:05:30 -0300"},
    };
  }

  @Test(/* dataProvider = "rfc" */ )
  public void test_print_rfc1123() throws Exception {
    Object[][] data = data_rfc();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_rfc1123((LocalDateTime) objects[0], (String) objects[1], (String) objects[2]);
    }
  }

  public void test_print_rfc1123(LocalDateTime base, String offsetId, String expected) {
    TemporalAccessor test = buildAccessor(base, offsetId, null);
    assertEquals(DateTimeFormatter.RFC_1123_DATE_TIME.format(test), expected);
  }

  @Test(/* dataProvider = "rfc" */ )
  public void test_print_rfc1123_french() throws Exception {
    Object[][] data = data_rfc();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_print_rfc1123_french(
          (LocalDateTime) objects[0], (String) objects[1], (String) objects[2]);
    }
  }

  public void test_print_rfc1123_french(LocalDateTime base, String offsetId, String expected) {
    TemporalAccessor test = buildAccessor(base, offsetId, null);
    assertEquals(
        DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.FRENCH).format(test), expected);
  }

  @Test(expected = DateTimeException.class)
  public void test_print_rfc1123_missingField() {
    try {
      TemporalAccessor test = YearMonth.of(2008, 6);
      DateTimeFormatter.RFC_1123_DATE_TIME.format(test);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  private Expected createDate(Integer year, Integer month, Integer day) {
    Expected test = new Expected();
    if (year != null) {
      test.fieldValues.put(YEAR, (long) year);
    }
    if (month != null) {
      test.fieldValues.put(MONTH_OF_YEAR, (long) month);
    }
    if (day != null) {
      test.fieldValues.put(DAY_OF_MONTH, (long) day);
    }
    return test;
  }

  private Expected createTime(Integer hour, Integer min, Integer sec, Integer nano) {
    Expected test = new Expected();
    if (hour != null) {
      test.fieldValues.put(HOUR_OF_DAY, (long) hour);
    }
    if (min != null) {
      test.fieldValues.put(MINUTE_OF_HOUR, (long) min);
    }
    if (sec != null) {
      test.fieldValues.put(SECOND_OF_MINUTE, (long) sec);
    }
    if (nano != null) {
      test.fieldValues.put(NANO_OF_SECOND, (long) nano);
    }
    return test;
  }

  private Expected createDateTime(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano) {
    Expected test = new Expected();
    if (year != null) {
      test.fieldValues.put(YEAR, (long) year);
    }
    if (month != null) {
      test.fieldValues.put(MONTH_OF_YEAR, (long) month);
    }
    if (day != null) {
      test.fieldValues.put(DAY_OF_MONTH, (long) day);
    }
    if (hour != null) {
      test.fieldValues.put(HOUR_OF_DAY, (long) hour);
    }
    if (min != null) {
      test.fieldValues.put(MINUTE_OF_HOUR, (long) min);
    }
    if (sec != null) {
      test.fieldValues.put(SECOND_OF_MINUTE, (long) sec);
    }
    if (nano != null) {
      test.fieldValues.put(NANO_OF_SECOND, (long) nano);
    }
    return test;
  }

  private TemporalAccessor buildAccessor(
      Integer year,
      Integer month,
      Integer day,
      Integer hour,
      Integer min,
      Integer sec,
      Integer nano,
      String offsetId,
      String zoneId) {
    MockAccessor mock = new MockAccessor();
    if (year != null) {
      mock.fields.put(YEAR, (long) year);
    }
    if (month != null) {
      mock.fields.put(MONTH_OF_YEAR, (long) month);
    }
    if (day != null) {
      mock.fields.put(DAY_OF_MONTH, (long) day);
    }
    if (hour != null) {
      mock.fields.put(HOUR_OF_DAY, (long) hour);
    }
    if (min != null) {
      mock.fields.put(MINUTE_OF_HOUR, (long) min);
    }
    if (sec != null) {
      mock.fields.put(SECOND_OF_MINUTE, (long) sec);
    }
    if (nano != null) {
      mock.fields.put(NANO_OF_SECOND, (long) nano);
    }
    mock.setOffset(offsetId);
    mock.setZone(zoneId);
    return mock;
  }

  private TemporalAccessor buildAccessor(LocalDateTime base, String offsetId, String zoneId) {
    MockAccessor mock = new MockAccessor();
    mock.setFields(base);
    mock.setOffset(offsetId);
    mock.setZone(zoneId);
    return mock;
  }

  private TemporalAccessor buildAccessor(LocalDate base, String offsetId, String zoneId) {
    MockAccessor mock = new MockAccessor();
    mock.setFields(base);
    mock.setOffset(offsetId);
    mock.setZone(zoneId);
    return mock;
  }

  private void buildCalendrical(Expected expected, String offsetId, String zoneId) {
    if (offsetId != null) {
      expected.add(ZoneOffset.of(offsetId));
    }
    if (zoneId != null) {
      expected.zone = ZoneId.of(zoneId);
    }
  }

  private void assertParseMatch(TemporalAccessor parsed, Expected expected) {
    for (TemporalField field : expected.fieldValues.keySet()) {
      assertEquals(parsed.isSupported(field), true);
      parsed.getLong(field);
    }
    assertEquals(parsed.query(TemporalQueries.chronology()), expected.chrono);
    assertEquals(parsed.query(TemporalQueries.zoneId()), expected.zone);
  }

  // -------------------------------------------------------------------------
  static class MockAccessor implements TemporalAccessor {
    Map<TemporalField, Long> fields = new HashMap<TemporalField, Long>();
    ZoneId zoneId;

    void setFields(LocalDate dt) {
      if (dt != null) {
        fields.put(YEAR, (long) dt.getYear());
        fields.put(MONTH_OF_YEAR, (long) dt.getMonthValue());
        fields.put(DAY_OF_MONTH, (long) dt.getDayOfMonth());
        fields.put(DAY_OF_YEAR, (long) dt.getDayOfYear());
        fields.put(DAY_OF_WEEK, (long) dt.getDayOfWeek().getValue());
        fields.put(IsoFields.WEEK_BASED_YEAR, dt.getLong(IsoFields.WEEK_BASED_YEAR));
        fields.put(
            IsoFields.WEEK_OF_WEEK_BASED_YEAR, dt.getLong(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
      }
    }

    void setFields(LocalDateTime dt) {
      if (dt != null) {
        fields.put(YEAR, (long) dt.getYear());
        fields.put(MONTH_OF_YEAR, (long) dt.getMonthValue());
        fields.put(DAY_OF_MONTH, (long) dt.getDayOfMonth());
        fields.put(DAY_OF_YEAR, (long) dt.getDayOfYear());
        fields.put(DAY_OF_WEEK, (long) dt.getDayOfWeek().getValue());
        fields.put(IsoFields.WEEK_BASED_YEAR, dt.getLong(IsoFields.WEEK_BASED_YEAR));
        fields.put(
            IsoFields.WEEK_OF_WEEK_BASED_YEAR, dt.getLong(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        fields.put(HOUR_OF_DAY, (long) dt.getHour());
        fields.put(MINUTE_OF_HOUR, (long) dt.getMinute());
        fields.put(SECOND_OF_MINUTE, (long) dt.getSecond());
        fields.put(NANO_OF_SECOND, (long) dt.getNano());
      }
    }

    void setOffset(String offsetId) {
      if (offsetId != null) {
        this.fields.put(OFFSET_SECONDS, (long) ZoneOffset.of(offsetId).getTotalSeconds());
      }
    }

    void setZone(String zoneId) {
      if (zoneId != null) {
        this.zoneId = ZoneId.of(zoneId);
      }
    }

    @Override
    public boolean isSupported(TemporalField field) {
      return fields.containsKey(field);
    }

    @Override
    public long getLong(TemporalField field) {
      try {
        // GWT specific
        Long value = fields.get(field);
        Objects.requireNonNull(value);
        return value.longValue();
      } catch (NullPointerException ex) {
        throw new DateTimeException("Field missing: " + field);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R query(TemporalQuery<R> query) {
      if (query == TemporalQueries.zoneId()) {
        return (R) zoneId;
      }
      return TemporalAccessor.super.query(query);
    }

    @Override
    public String toString() {
      return fields + (zoneId != null ? " " + zoneId : "");
    }
  }

  // -----------------------------------------------------------------------
  static class Expected {
    Map<TemporalField, Long> fieldValues = new HashMap<TemporalField, Long>();
    ZoneId zone;
    Chronology chrono;

    Expected() {}

    Expected(TemporalField field1, long value1, TemporalField field2, long value2) {
      fieldValues.put(field1, value1);
      fieldValues.put(field2, value2);
    }

    void add(ZoneOffset offset) {
      fieldValues.put(OFFSET_SECONDS, (long) offset.getTotalSeconds());
    }
  }
}
