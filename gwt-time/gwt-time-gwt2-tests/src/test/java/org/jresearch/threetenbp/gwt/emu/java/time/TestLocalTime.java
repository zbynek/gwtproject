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
package org.jresearch.threetenbp.gwt.emu.java.time;

import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.AMPM_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.CLOCK_HOUR_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.HOUR_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MICRO_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MILLI_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.NANO_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.NANO_OF_SECOND;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.SECOND_OF_DAY;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.DAYS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.FOREVER;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.HALF_DAYS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.HOURS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.MICROS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.MILLIS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.MINUTES;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.MONTHS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.NANOS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.SECONDS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.WEEKS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.YEARS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.jresearch.threetenbp.gwt.emu.java.time.format.DateTimeFormatter;
import org.jresearch.threetenbp.gwt.emu.java.time.format.DateTimeParseException;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.JulianFields;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.MockFieldNoValue;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.Temporal;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalAccessor;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalAdjuster;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalAmount;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalField;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalQueries;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalUnit;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.Test;

/** Test LocalTime. */
// @Test
public class TestLocalTime extends AbstractDateTimeTest {

  private LocalTime TEST_12_30_40_987654321;

  private static final TemporalUnit[] INVALID_UNITS;

  static {
    EnumSet<ChronoUnit> set = EnumSet.range(WEEKS, FOREVER);
    INVALID_UNITS = set.toArray(new TemporalUnit[set.size()]);
  }

  // @BeforeMethod
  @Override
  public void gwtSetUp() throws Exception {
    TEST_12_30_40_987654321 = LocalTime.of(12, 30, 40, 987654321);
    super.gwtSetUp();
  }

  // -----------------------------------------------------------------------
  @Override
  protected List<TemporalAccessor> samples() {
    TemporalAccessor[] array = {
      TEST_12_30_40_987654321, LocalTime.MIN, LocalTime.MAX, LocalTime.MIDNIGHT, LocalTime.NOON
    };
    return Arrays.asList(array);
  }

  @Override
  protected List<TemporalField> validFields() {
    TemporalField[] array = {
      NANO_OF_SECOND,
      NANO_OF_DAY,
      MICRO_OF_SECOND,
      MICRO_OF_DAY,
      MILLI_OF_SECOND,
      MILLI_OF_DAY,
      SECOND_OF_MINUTE,
      SECOND_OF_DAY,
      MINUTE_OF_HOUR,
      MINUTE_OF_DAY,
      CLOCK_HOUR_OF_AMPM,
      HOUR_OF_AMPM,
      CLOCK_HOUR_OF_DAY,
      HOUR_OF_DAY,
      AMPM_OF_DAY,
    };
    return Arrays.asList(array);
  }

  @Override
  protected List<TemporalField> invalidFields() {
    List<TemporalField> list = new ArrayList<>(Arrays.<TemporalField>asList(ChronoField.values()));
    list.removeAll(validFields());
    list.add(JulianFields.JULIAN_DAY);
    list.add(JulianFields.MODIFIED_JULIAN_DAY);
    list.add(JulianFields.RATA_DIE);
    return list;
  }

  // -----------------------------------------------------------------------
  // @Test(enabled = false)
  // public void test_serialization_format() throws ClassNotFoundException,
  // IOException {
  // assertEqualsSerialisedForm(LocalTime.of(22, 17, 59, 460 * 1000000));
  // }

  // @Test
  // public void test_serialization() throws IOException, ClassNotFoundException {
  // assertSerializable(TEST_12_30_40_987654321);
  // }

  // -----------------------------------------------------------------------
  private void check(LocalTime time, int h, int m, int s, int n) {
    assertEquals(time.getHour(), h);
    assertEquals(time.getMinute(), m);
    assertEquals(time.getSecond(), s);
    assertEquals(time.getNano(), n);
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_constant_MIDNIGHT() {
    check(LocalTime.MIDNIGHT, 0, 0, 0, 0);
  }

  @Test
  public void test_constant_MIDNIGHT_equal() {
    assertEquals(LocalTime.MIDNIGHT, LocalTime.MIDNIGHT);
    assertEquals(LocalTime.MIDNIGHT, LocalTime.of(0, 0));
  }

  @Test
  public void test_constant_MIDDAY() {
    check(LocalTime.NOON, 12, 0, 0, 0);
  }

  @Test
  public void test_constant_MIDDAY_equal() {
    assertEquals(LocalTime.NOON, LocalTime.NOON);
    assertEquals(LocalTime.NOON, LocalTime.of(12, 0));
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_constant_MIN_TIME() {
    check(LocalTime.MIN, 0, 0, 0, 0);
  }

  @Test
  public void test_constant_MIN_TIME_equal() {
    assertEquals(LocalTime.MIN, LocalTime.of(0, 0));
  }

  @Test
  public void test_constant_MAX_TIME() {
    check(LocalTime.MAX, 23, 59, 59, 999999999);
  }

  @Test
  public void test_constant_MAX_TIME_equal() {
    assertEquals(LocalTime.NOON, LocalTime.NOON);
    assertEquals(LocalTime.NOON, LocalTime.of(12, 0));
  }

  // -----------------------------------------------------------------------
  // now()
  // -----------------------------------------------------------------------
  @Test
  public void test_now() {
    LocalTime expected = LocalTime.now(Clock.systemDefaultZone());
    LocalTime test = LocalTime.now();
    long diff = Math.abs(test.toNanoOfDay() - expected.toNanoOfDay());
    assertTrue(diff < 100000000); // less than 0.1 secs
  }

  // -----------------------------------------------------------------------
  // now(ZoneId)
  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_now_ZoneId_nullZoneId() {
    try {
      LocalTime.now((ZoneId) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void disabled_test_now_ZoneId() {
    ZoneId zone = ZoneId.of("UTC+01:02:03");
    LocalTime expected = LocalTime.now(Clock.system(zone));
    LocalTime test = LocalTime.now(zone);
    for (int i = 0; i < 1000; i++) {
      if (expected.equals(test)) {
        return;
      }
      expected = LocalTime.now(Clock.system(zone));
      test = LocalTime.now(zone);
    }
    assertEquals(expected, test);
  }

  // -----------------------------------------------------------------------
  // now(Clock)
  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_now_Clock_nullClock() {
    try {
      LocalTime.now((Clock) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void long_test_now_Clock_allSecsInDay() {
    for (int i = 0; i < (2 * 24 * 60 * 60); i++) {
      Instant instant = Instant.ofEpochSecond(i, 8);
      Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
      LocalTime test = LocalTime.now(clock);
      assertEquals(test.getHour(), (i / (60 * 60)) % 24);
      assertEquals(test.getMinute(), (i / 60) % 60);
      assertEquals(test.getSecond(), i % 60);
      assertEquals(test.getNano(), 8);
    }
  }

  @Test
  public void long_test_now_Clock_beforeEpoch() {
    for (int i = -1; i >= -(24 * 60 * 60); i--) {
      Instant instant = Instant.ofEpochSecond(i, 8);
      Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
      LocalTime test = LocalTime.now(clock);
      assertEquals(test.getHour(), ((i + 24 * 60 * 60) / (60 * 60)) % 24);
      assertEquals(test.getMinute(), ((i + 24 * 60 * 60) / 60) % 60);
      assertEquals(test.getSecond(), (i + 24 * 60 * 60) % 60);
      assertEquals(test.getNano(), 8);
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_now_Clock_max() {
    Clock clock = Clock.fixed(Instant.MAX, ZoneOffset.UTC);
    LocalTime test = LocalTime.now(clock);
    assertEquals(test.getHour(), 23);
    assertEquals(test.getMinute(), 59);
    assertEquals(test.getSecond(), 59);
    assertEquals(test.getNano(), 999999999);
  }

  @Test
  public void test_now_Clock_min() {
    Clock clock = Clock.fixed(Instant.MIN, ZoneOffset.UTC);
    LocalTime test = LocalTime.now(clock);
    assertEquals(test.getHour(), 0);
    assertEquals(test.getMinute(), 0);
    assertEquals(test.getSecond(), 0);
    assertEquals(test.getNano(), 0);
  }

  // -----------------------------------------------------------------------
  // of() factories
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_time_2ints() {
    LocalTime test = LocalTime.of(12, 30);
    check(test, 12, 30, 0, 0);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_2ints_hourTooLow() {
    try {
      LocalTime.of(-1, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_2ints_hourTooHigh() {
    try {
      LocalTime.of(24, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_2ints_minuteTooLow() {
    try {
      LocalTime.of(0, -1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_2ints_minuteTooHigh() {
    try {
      LocalTime.of(0, 60);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_factory_time_3ints() {
    LocalTime test = LocalTime.of(12, 30, 40);
    check(test, 12, 30, 40, 0);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_3ints_hourTooLow() {
    try {
      LocalTime.of(-1, 0, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_3ints_hourTooHigh() {
    try {
      LocalTime.of(24, 0, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_3ints_minuteTooLow() {
    try {
      LocalTime.of(0, -1, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_3ints_minuteTooHigh() {
    try {
      LocalTime.of(0, 60, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_3ints_secondTooLow() {
    try {
      LocalTime.of(0, 0, -1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_3ints_secondTooHigh() {
    try {
      LocalTime.of(0, 0, 60);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_factory_time_4ints() {
    LocalTime test = LocalTime.of(12, 30, 40, 987654321);
    check(test, 12, 30, 40, 987654321);
    test = LocalTime.of(12, 0, 40, 987654321);
    check(test, 12, 0, 40, 987654321);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_hourTooLow() {
    try {
      LocalTime.of(-1, 0, 0, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_hourTooHigh() {
    try {
      LocalTime.of(24, 0, 0, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_minuteTooLow() {
    try {
      LocalTime.of(0, -1, 0, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_minuteTooHigh() {
    try {
      LocalTime.of(0, 60, 0, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_secondTooLow() {
    try {
      LocalTime.of(0, 0, -1, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_secondTooHigh() {
    try {
      LocalTime.of(0, 0, 60, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_nanoTooLow() {
    try {
      LocalTime.of(0, 0, 0, -1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_time_4ints_nanoTooHigh() {
    try {
      LocalTime.of(0, 0, 0, 1000000000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // ofSecondOfDay(long)
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_ofSecondOfDay() {
    LocalTime localTime = LocalTime.ofSecondOfDay(2 * 60 * 60 + 17 * 60 + 23);
    check(localTime, 2, 17, 23, 0);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofSecondOfDay_tooLow() {
    try {
      LocalTime.ofSecondOfDay(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofSecondOfDay_tooHigh() {
    try {
      LocalTime.ofSecondOfDay(24 * 60 * 60);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // ofSecondOfDay(long, int)
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_ofSecondOfDay_long_int() {
    LocalTime localTime = LocalTime.ofSecondOfDay(2 * 60 * 60 + 17 * 60 + 23, 987);
    check(localTime, 2, 17, 23, 987);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofSecondOfDay_long_int_tooLowSecs() {
    try {
      LocalTime.ofSecondOfDay(-1, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofSecondOfDay_long_int_tooHighSecs() {
    try {
      LocalTime.ofSecondOfDay(24 * 60 * 60, 0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofSecondOfDay_long_int_tooLowNanos() {
    try {
      LocalTime.ofSecondOfDay(0, -1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofSecondOfDay_long_int_tooHighNanos() {
    try {
      LocalTime.ofSecondOfDay(0, 1000000000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // ofNanoOfDay(long)
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_ofNanoOfDay() {
    LocalTime localTime = LocalTime.ofNanoOfDay(60 * 60 * 1000000000L + 17);
    check(localTime, 1, 0, 0, 17);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofNanoOfDay_tooLow() {
    try {
      LocalTime.ofNanoOfDay(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_ofNanoOfDay_tooHigh() {
    try {
      LocalTime.ofNanoOfDay(24 * 60 * 60 * 1000000000L);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // from()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_from_DateTimeAccessor() {
    assertEquals(LocalTime.from(LocalTime.of(17, 30)), LocalTime.of(17, 30));
    assertEquals(LocalTime.from(LocalDateTime.of(2012, 5, 1, 17, 30)), LocalTime.of(17, 30));
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_from_DateTimeAccessor_invalid_noDerive() {
    try {
      LocalTime.from(LocalDate.of(2007, 7, 15));
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_from_DateTimeAccessor_null() {
    try {
      LocalTime.from((TemporalAccessor) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // parse()
  // -----------------------------------------------------------------------
  @Test(/* dataProvider = "sampleToString" */ )
  public void test_factory_parse_validText() {
    Object[][] data = provider_sampleToString();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_factory_parse_validText(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4]);
    }
  }

  public void test_factory_parse_validText(int h, int m, int s, int n, String parsable) {
    LocalTime t = LocalTime.parse(parsable);
    assertNotNull(parsable, t);
    assertEquals(t.getHour(), h);
    assertEquals(t.getMinute(), m);
    assertEquals(t.getSecond(), s);
    assertEquals(t.getNano(), n);
  }

  // @DataProvider(name = "sampleBadParse")
  Object[][] provider_sampleBadParse() {
    return new Object[][] {
      {"00;00"},
      {"12-00"},
      {"-01:00"},
      {"00:00:00-09"},
      {"00:00:00,09"},
      {"00:00:abs"},
      {"11"},
      {"11:30+01:00"},
      {"11:30+01:00[Europe/Paris]"},
    };
  }

  @Test(/* dataProvider = "sampleBadParse", */ expected = DateTimeParseException.class)
  public void test_factory_parse_invalidText() {
    Object[][] data = provider_sampleBadParse();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_factory_parse_invalidText((String) objects[0]);
    }
  }

  public void test_factory_parse_invalidText(String unparsable) {
    try {
      LocalTime.parse(unparsable);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------s
  @Test(expected = DateTimeParseException.class)
  public void test_factory_parse_illegalHour() {
    try {
      LocalTime.parse("25:00");
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeParseException.class)
  public void test_factory_parse_illegalMinute() {
    try {
      LocalTime.parse("12:60");
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeParseException.class)
  public void test_factory_parse_illegalSecond() {
    try {
      LocalTime.parse("12:12:60");
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------s
  @Test(expected = NullPointerException.class)
  public void test_factory_parse_nullTest() {
    try {
      LocalTime.parse((String) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // parse(DateTimeFormatter)
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_parse_formatter() {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("H m s");
    LocalTime test = LocalTime.parse("14 30 40", f);
    assertEquals(test, LocalTime.of(14, 30, 40));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_parse_formatter_nullText() {
    try {
      DateTimeFormatter f = DateTimeFormatter.ofPattern("H m s");
      LocalTime.parse((String) null, f);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_parse_formatter_nullFormatter() {
    try {
      LocalTime.parse("ANY", null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // get(TemporalField)
  // -----------------------------------------------------------------------
  @Test
  public void test_get_TemporalField() {
    LocalTime test = TEST_12_30_40_987654321;
    assertEquals(test.get(ChronoField.HOUR_OF_DAY), 12);
    assertEquals(test.get(ChronoField.MINUTE_OF_HOUR), 30);
    assertEquals(test.get(ChronoField.SECOND_OF_MINUTE), 40);
    assertEquals(test.get(ChronoField.NANO_OF_SECOND), 987654321);

    assertEquals(test.get(ChronoField.SECOND_OF_DAY), 12 * 3600 + 30 * 60 + 40);
    assertEquals(test.get(ChronoField.MINUTE_OF_DAY), 12 * 60 + 30);
    assertEquals(test.get(ChronoField.HOUR_OF_AMPM), 0);
    assertEquals(test.get(ChronoField.CLOCK_HOUR_OF_AMPM), 12);
    assertEquals(test.get(ChronoField.CLOCK_HOUR_OF_DAY), 12);
    assertEquals(test.get(ChronoField.AMPM_OF_DAY), 1);
  }

  @Test(expected = DateTimeException.class)
  public void test_get_TemporalField_tooBig() {
    try {
      TEST_12_30_40_987654321.get(NANO_OF_DAY);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_get_TemporalField_null() {
    try {
      TEST_12_30_40_987654321.get((TemporalField) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_get_TemporalField_invalidField() {
    try {
      TEST_12_30_40_987654321.get(MockFieldNoValue.INSTANCE);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_get_TemporalField_dateField() {
    try {
      TEST_12_30_40_987654321.get(ChronoField.DAY_OF_MONTH);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // getLong(TemporalField)
  // -----------------------------------------------------------------------
  @Test
  public void test_getLong_TemporalField() {
    LocalTime test = TEST_12_30_40_987654321;
    assertEquals(test.getLong(ChronoField.HOUR_OF_DAY), 12);
    assertEquals(test.getLong(ChronoField.MINUTE_OF_HOUR), 30);
    assertEquals(test.getLong(ChronoField.SECOND_OF_MINUTE), 40);
    assertEquals(test.getLong(ChronoField.NANO_OF_SECOND), 987654321);

    assertEquals(
        test.getLong(ChronoField.NANO_OF_DAY),
        ((12 * 3600 + 30 * 60 + 40) * 1000000000L) + 987654321);
    assertEquals(test.getLong(ChronoField.SECOND_OF_DAY), 12 * 3600 + 30 * 60 + 40);
    assertEquals(test.getLong(ChronoField.MINUTE_OF_DAY), 12 * 60 + 30);
    assertEquals(test.getLong(ChronoField.HOUR_OF_AMPM), 0);
    assertEquals(test.getLong(ChronoField.CLOCK_HOUR_OF_AMPM), 12);
    assertEquals(test.getLong(ChronoField.CLOCK_HOUR_OF_DAY), 12);
    assertEquals(test.getLong(ChronoField.AMPM_OF_DAY), 1);
  }

  @Test(expected = NullPointerException.class)
  public void test_getLong_TemporalField_null() {
    try {
      TEST_12_30_40_987654321.getLong((TemporalField) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_getLong_TemporalField_invalidField() {
    try {
      TEST_12_30_40_987654321.getLong(MockFieldNoValue.INSTANCE);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_getLong_TemporalField_dateField() {
    try {
      TEST_12_30_40_987654321.getLong(ChronoField.DAY_OF_MONTH);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // query(TemporalQuery)
  // -----------------------------------------------------------------------
  @Test
  public void test_query() {
    assertEquals(TEST_12_30_40_987654321.query(TemporalQueries.chronology()), null);
    assertEquals(TEST_12_30_40_987654321.query(TemporalQueries.localDate()), null);
    assertEquals(
        TEST_12_30_40_987654321.query(TemporalQueries.localTime()), TEST_12_30_40_987654321);
    assertEquals(TEST_12_30_40_987654321.query(TemporalQueries.offset()), null);
    assertEquals(TEST_12_30_40_987654321.query(TemporalQueries.precision()), ChronoUnit.NANOS);
    assertEquals(TEST_12_30_40_987654321.query(TemporalQueries.zone()), null);
    assertEquals(TEST_12_30_40_987654321.query(TemporalQueries.zoneId()), null);
  }

  @Test(expected = NullPointerException.class)
  public void test_query_null() {
    try {
      TEST_12_30_40_987654321.query(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // get*()
  // -----------------------------------------------------------------------
  // @DataProvider(name = "sampleTimes")
  Object[][] provider_sampleTimes() {
    return new Object[][] {
      {0, 0, 0, 0},
      {0, 0, 0, 1},
      {0, 0, 1, 0},
      {0, 0, 1, 1},
      {0, 1, 0, 0},
      {0, 1, 0, 1},
      {0, 1, 1, 0},
      {0, 1, 1, 1},
      {1, 0, 0, 0},
      {1, 0, 0, 1},
      {1, 0, 1, 0},
      {1, 0, 1, 1},
      {1, 1, 0, 0},
      {1, 1, 0, 1},
      {1, 1, 1, 0},
      {1, 1, 1, 1},
    };
  }

  // -----------------------------------------------------------------------
  @Test(/* dataProvider="sampleTimes" */ )
  public void test_get() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_get(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_get(int h, int m, int s, int ns) {
    LocalTime a = LocalTime.of(h, m, s, ns);
    assertEquals(a.getHour(), h);
    assertEquals(a.getMinute(), m);
    assertEquals(a.getSecond(), s);
    assertEquals(a.getNano(), ns);
  }

  // -----------------------------------------------------------------------
  // with()
  // -----------------------------------------------------------------------
  @Test
  public void test_with_adjustment() {
    final LocalTime sample = LocalTime.of(23, 5);
    TemporalAdjuster adjuster =
        new TemporalAdjuster() {
          @Override
          public Temporal adjustInto(Temporal dateTime) {
            return sample;
          }
        };
    assertEquals(TEST_12_30_40_987654321.with(adjuster), sample);
  }

  @Test(expected = NullPointerException.class)
  public void test_with_adjustment_null() {
    try {
      TEST_12_30_40_987654321.with((TemporalAdjuster) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // withHour()
  // -----------------------------------------------------------------------
  @Test
  public void test_withHour_normal() {
    LocalTime t = TEST_12_30_40_987654321;
    for (int i = 0; i < 24; i++) {
      t = t.withHour(i);
      assertEquals(t.getHour(), i);
    }
  }

  @Test
  public void test_withHour_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.withHour(12);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_withHour_toMidnight_equal() {
    LocalTime t = LocalTime.of(1, 0).withHour(0);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_withHour_toMidday_equal() {
    LocalTime t = LocalTime.of(1, 0).withHour(12);
    assertEquals(t, LocalTime.NOON);
  }

  @Test(expected = DateTimeException.class)
  public void test_withHour_hourTooLow() {
    try {
      TEST_12_30_40_987654321.withHour(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_withHour_hourTooHigh() {
    try {
      TEST_12_30_40_987654321.withHour(24);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // withMinute()
  // -----------------------------------------------------------------------
  @Test
  public void test_withMinute_normal() {
    LocalTime t = TEST_12_30_40_987654321;
    for (int i = 0; i < 60; i++) {
      t = t.withMinute(i);
      assertEquals(t.getMinute(), i);
    }
  }

  @Test
  public void test_withMinute_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.withMinute(30);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_withMinute_toMidnight_equal() {
    LocalTime t = LocalTime.of(0, 1).withMinute(0);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_withMinute_toMidday_equals() {
    LocalTime t = LocalTime.of(12, 1).withMinute(0);
    assertEquals(t, LocalTime.NOON);
  }

  @Test(expected = DateTimeException.class)
  public void test_withMinute_minuteTooLow() {
    try {
      TEST_12_30_40_987654321.withMinute(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_withMinute_minuteTooHigh() {
    try {
      TEST_12_30_40_987654321.withMinute(60);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // withSecond()
  // -----------------------------------------------------------------------
  @Test
  public void test_withSecond_normal() {
    LocalTime t = TEST_12_30_40_987654321;
    for (int i = 0; i < 60; i++) {
      t = t.withSecond(i);
      assertEquals(t.getSecond(), i);
    }
  }

  @Test
  public void test_withSecond_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.withSecond(40);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_withSecond_toMidnight_equal() {
    LocalTime t = LocalTime.of(0, 0, 1).withSecond(0);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_withSecond_toMidday_equal() {
    LocalTime t = LocalTime.of(12, 0, 1).withSecond(0);
    assertEquals(t, LocalTime.NOON);
  }

  @Test(expected = DateTimeException.class)
  public void test_withSecond_secondTooLow() {
    try {
      TEST_12_30_40_987654321.withSecond(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_withSecond_secondTooHigh() {
    try {
      TEST_12_30_40_987654321.withSecond(60);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // withNano()
  // -----------------------------------------------------------------------
  @Test
  public void test_withNanoOfSecond_normal() {
    LocalTime t = TEST_12_30_40_987654321;
    t = t.withNano(1);
    assertEquals(t.getNano(), 1);
    t = t.withNano(10);
    assertEquals(t.getNano(), 10);
    t = t.withNano(100);
    assertEquals(t.getNano(), 100);
    t = t.withNano(999999999);
    assertEquals(t.getNano(), 999999999);
  }

  @Test
  public void test_withNanoOfSecond_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.withNano(987654321);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_withNanoOfSecond_toMidnight_equal() {
    LocalTime t = LocalTime.of(0, 0, 0, 1).withNano(0);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_withNanoOfSecond_toMidday_equal() {
    LocalTime t = LocalTime.of(12, 0, 0, 1).withNano(0);
    assertEquals(t, LocalTime.NOON);
  }

  @Test(expected = DateTimeException.class)
  public void test_withNanoOfSecond_nanoTooLow() {
    try {
      TEST_12_30_40_987654321.withNano(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_withNanoOfSecond_nanoTooHigh() {
    try {
      TEST_12_30_40_987654321.withNano(1000000000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // truncated(TemporalUnit)
  // -----------------------------------------------------------------------
  TemporalUnit NINETY_MINS =
      new TemporalUnit() {
        @Override
        public String toString() {
          return "NinetyMins";
        }

        @Override
        public Duration getDuration() {
          return Duration.ofMinutes(90);
        }

        @Override
        public boolean isDurationEstimated() {
          return false;
        }

        @Override
        public boolean isDateBased() {
          return false;
        }

        @Override
        public boolean isTimeBased() {
          return true;
        }

        @Override
        public boolean isSupportedBy(Temporal temporal) {
          return false;
        }

        @Override
        public <R extends Temporal> R addTo(R r, long l) {
          throw new UnsupportedOperationException();
        }

        @Override
        public long between(Temporal r, Temporal r2) {
          throw new UnsupportedOperationException();
        }
      };

  TemporalUnit NINETY_FIVE_MINS =
      new TemporalUnit() {
        @Override
        public String toString() {
          return "NinetyFiveMins";
        }

        @Override
        public Duration getDuration() {
          return Duration.ofMinutes(95);
        }

        @Override
        public boolean isDurationEstimated() {
          return false;
        }

        @Override
        public boolean isDateBased() {
          return false;
        }

        @Override
        public boolean isTimeBased() {
          return true;
        }

        @Override
        public boolean isSupportedBy(Temporal temporal) {
          return false;
        }

        @Override
        public <R extends Temporal> R addTo(R r, long l) {
          throw new UnsupportedOperationException();
        }

        @Override
        public long between(Temporal r, Temporal r2) {
          throw new UnsupportedOperationException();
        }
      };

  // @DataProvider(name = "truncatedToValid")
  Object[][] data_truncatedToValid() {
    return new Object[][] {
      {LocalTime.of(1, 2, 3, 123456789), NANOS, LocalTime.of(1, 2, 3, 123456789)},
      {LocalTime.of(1, 2, 3, 123456789), MICROS, LocalTime.of(1, 2, 3, 123456000)},
      {LocalTime.of(1, 2, 3, 123456789), MILLIS, LocalTime.of(1, 2, 3, 123000000)},
      {LocalTime.of(1, 2, 3, 123456789), SECONDS, LocalTime.of(1, 2, 3)},
      {LocalTime.of(1, 2, 3, 123456789), MINUTES, LocalTime.of(1, 2)},
      {LocalTime.of(1, 2, 3, 123456789), HOURS, LocalTime.of(1, 0)},
      {LocalTime.of(1, 2, 3, 123456789), DAYS, LocalTime.MIDNIGHT},
      {LocalTime.of(1, 1, 1, 123456789), NINETY_MINS, LocalTime.of(0, 0)},
      {LocalTime.of(2, 1, 1, 123456789), NINETY_MINS, LocalTime.of(1, 30)},
      {LocalTime.of(3, 1, 1, 123456789), NINETY_MINS, LocalTime.of(3, 0)},
    };
  }

  // @Test(groups={"tck"}, dataProvider="truncatedToValid")
  public void test_truncatedTo_valid(LocalTime input, TemporalUnit unit, LocalTime expected) {
    assertEquals(input.truncatedTo(unit), expected);
  }

  // @DataProvider(name = "truncatedToInvalid")
  Object[][] data_truncatedToInvalid() {
    return new Object[][] {
      {LocalTime.of(1, 2, 3, 123456789), NINETY_FIVE_MINS},
      {LocalTime.of(1, 2, 3, 123456789), WEEKS},
      {LocalTime.of(1, 2, 3, 123456789), MONTHS},
      {LocalTime.of(1, 2, 3, 123456789), YEARS},
    };
  }

  // @Test(groups={"tck"}, dataProvider="truncatedToInvalid",
  // expected=DateTimeException.class)
  public void test_truncatedTo_invalid(LocalTime input, TemporalUnit unit) {
    try {
      input.truncatedTo(unit);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class /* , groups={"tck"} */)
  public void test_truncatedTo_null() {
    try {
      TEST_12_30_40_987654321.truncatedTo(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // plus(PlusAdjuster)
  // -----------------------------------------------------------------------
  @Test
  public void test_plus_Adjuster_positiveHours() {
    TemporalAmount period = MockSimplePeriod.of(7, ChronoUnit.HOURS);
    LocalTime t = TEST_12_30_40_987654321.plus(period);
    assertEquals(t, LocalTime.of(19, 30, 40, 987654321));
  }

  @Test
  public void test_plus_Adjuster_negativeMinutes() {
    TemporalAmount period = MockSimplePeriod.of(-25, ChronoUnit.MINUTES);
    LocalTime t = TEST_12_30_40_987654321.plus(period);
    assertEquals(t, LocalTime.of(12, 5, 40, 987654321));
  }

  @Test
  public void test_plus_Adjuster_zero() {
    TemporalAmount period = Period.ZERO;
    LocalTime t = TEST_12_30_40_987654321.plus(period);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plus_Adjuster_wrap() {
    TemporalAmount p = Duration.ofHours(1);
    LocalTime t = LocalTime.of(23, 30).plus(p);
    assertEquals(t, LocalTime.of(0, 30));
  }

  @Test(expected = DateTimeException.class)
  public void test_plus_Adjuster_dateNotAllowed() {
    try {
      TemporalAmount period = MockSimplePeriod.of(7, ChronoUnit.MONTHS);
      TEST_12_30_40_987654321.plus(period);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_plus_Adjuster_null() {
    try {
      TEST_12_30_40_987654321.plus((TemporalAmount) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // plus(long,PeriodUnit)
  // -----------------------------------------------------------------------
  @Test
  public void test_plus_longPeriodUnit_positiveHours() {
    LocalTime t = TEST_12_30_40_987654321.plus(7, ChronoUnit.HOURS);
    assertEquals(t, LocalTime.of(19, 30, 40, 987654321));
  }

  @Test
  public void test_plus_longPeriodUnit_negativeMinutes() {
    LocalTime t = TEST_12_30_40_987654321.plus(-25, ChronoUnit.MINUTES);
    assertEquals(t, LocalTime.of(12, 5, 40, 987654321));
  }

  @Test
  public void test_plus_longPeriodUnit_zero() {
    LocalTime t = TEST_12_30_40_987654321.plus(0, ChronoUnit.MINUTES);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plus_long_unit_invalidUnit() {
    for (TemporalUnit unit : INVALID_UNITS) {
      try {
        TEST_12_30_40_987654321.plus(1, unit);
        fail("Unit should not be allowed " + unit);
        fail("Missing exception");
      } catch (DateTimeException ex) {
        // expected
      }
    }
  }

  @Test(expected = UnsupportedTemporalTypeException.class)
  public void test_plus_long_multiples() {
    try {
      TEST_12_30_40_987654321.plus(0, DAYS);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_plus_longPeriodUnit_null() {
    try {
      TEST_12_30_40_987654321.plus(1, (TemporalUnit) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // plus(adjuster)
  // -----------------------------------------------------------------------
  @Test
  public void test_plus_adjuster() {
    Duration p = Duration.ofSeconds(62, 3);
    LocalTime t = TEST_12_30_40_987654321.plus(p);
    assertEquals(t, LocalTime.of(12, 31, 42, 987654324));
  }

  @Test
  public void test_plus_adjuster_big() {
    Duration p = Duration.ofNanos(Long.MAX_VALUE);
    LocalTime t = TEST_12_30_40_987654321.plus(p);
    assertEquals(t, TEST_12_30_40_987654321.plusNanos(Long.MAX_VALUE));
  }

  @Test
  public void test_plus_adjuster_zero_equal() {
    LocalTime t = TEST_12_30_40_987654321.plus(Period.ZERO);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plus_adjuster_wrap() {
    Duration p = Duration.ofHours(1);
    LocalTime t = LocalTime.of(23, 30).plus(p);
    assertEquals(t, LocalTime.of(0, 30));
  }

  @Test(expected = NullPointerException.class)
  public void test_plus_adjuster_null() {
    try {
      TEST_12_30_40_987654321.plus((TemporalAmount) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // plusHours()
  // -----------------------------------------------------------------------
  @Test
  public void test_plusHours_one() {
    LocalTime t = LocalTime.MIDNIGHT;
    for (int i = 0; i < 50; i++) {
      t = t.plusHours(1);
      assertEquals(t.getHour(), (i + 1) % 24);
    }
  }

  @Test
  public void test_plusHours_fromZero() {
    LocalTime base = LocalTime.MIDNIGHT;
    for (int i = -50; i < 50; i++) {
      LocalTime t = base.plusHours(i);
      assertEquals(t.getHour(), (i + 72) % 24);
    }
  }

  @Test
  public void test_plusHours_fromOne() {
    LocalTime base = LocalTime.of(1, 0);
    for (int i = -50; i < 50; i++) {
      LocalTime t = base.plusHours(i);
      assertEquals(t.getHour(), (1 + i + 72) % 24);
    }
  }

  @Test
  public void test_plusHours_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusHours(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusHours_toMidnight_equal() {
    LocalTime t = LocalTime.of(23, 0).plusHours(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_plusHours_toMidday_equal() {
    LocalTime t = LocalTime.of(11, 0).plusHours(1);
    assertEquals(t, LocalTime.NOON);
  }

  @Test
  public void test_plusHours_big() {
    LocalTime t = LocalTime.of(2, 30).plusHours(Long.MAX_VALUE);
    int hours = (int) (Long.MAX_VALUE % 24L);
    assertEquals(t, LocalTime.of(2, 30).plusHours(hours));
  }

  // -----------------------------------------------------------------------
  // plusMinutes()
  // -----------------------------------------------------------------------
  @Test
  public void test_plusMinutes_one() {
    LocalTime t = LocalTime.MIDNIGHT;
    int hour = 0;
    int min = 0;
    for (int i = 0; i < 70; i++) {
      t = t.plusMinutes(1);
      min++;
      if (min == 60) {
        hour++;
        min = 0;
      }
      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
    }
  }

  @Test
  public void test_plusMinutes_fromZero() {
    LocalTime base = LocalTime.MIDNIGHT;
    int hour;
    int min;
    for (int i = -70; i < 70; i++) {
      LocalTime t = base.plusMinutes(i);
      if (i < -60) {
        hour = 22;
        min = i + 120;
      } else if (i < 0) {
        hour = 23;
        min = i + 60;
      } else if (i >= 60) {
        hour = 1;
        min = i - 60;
      } else {
        hour = 0;
        min = i;
      }
      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
    }
  }

  @Test
  public void test_plusMinutes_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusMinutes(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusMinutes_noChange_oneDay_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusMinutes(24 * 60);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusMinutes_toMidnight_equal() {
    LocalTime t = LocalTime.of(23, 59).plusMinutes(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_plusMinutes_toMidday_equal() {
    LocalTime t = LocalTime.of(11, 59).plusMinutes(1);
    assertEquals(t, LocalTime.NOON);
  }

  @Test
  public void test_plusMinutes_big() {
    LocalTime t = LocalTime.of(2, 30).plusMinutes(Long.MAX_VALUE);
    int mins = (int) (Long.MAX_VALUE % (24L * 60L));
    assertEquals(t, LocalTime.of(2, 30).plusMinutes(mins));
  }

  // -----------------------------------------------------------------------
  // plusSeconds()
  // -----------------------------------------------------------------------
  @Test
  public void test_plusSeconds_one() {
    LocalTime t = LocalTime.MIDNIGHT;
    int hour = 0;
    int min = 0;
    int sec = 0;
    for (int i = 0; i < 3700; i++) {
      t = t.plusSeconds(1);
      sec++;
      if (sec == 60) {
        min++;
        sec = 0;
      }
      if (min == 60) {
        hour++;
        min = 0;
      }
      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
      assertEquals(t.getSecond(), sec);
    }
  }

  // @DataProvider(name = "plusSeconds_fromZero")
  Iterator<Object[]> plusSeconds_fromZero() {
    return new Iterator<Object[]>() {
      int delta = 30;
      int i = -3660;
      int hour = 22;
      int min = 59;
      int sec = 0;

      @Override
      public boolean hasNext() {
        return i <= 3660;
      }

      @Override
      public Object[] next() {
        final Object[] ret = new Object[] {i, hour, min, sec};
        i += delta;
        sec += delta;

        if (sec >= 60) {
          min++;
          sec -= 60;

          if (min == 60) {
            hour++;
            min = 0;

            if (hour == 24) {
              hour = 0;
            }
          }
        }

        return ret;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(/* dataProvider="plusSeconds_fromZero" */ )
  public void test_plusSeconds_fromZero() {
    Iterator<Object[]> data = plusSeconds_fromZero();
    while (data.hasNext()) {
      Object[] objects = data.next();
      test_plusSeconds_fromZero(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_plusSeconds_fromZero(int seconds, int hour, int min, int sec) {
    LocalTime base = LocalTime.MIDNIGHT;
    LocalTime t = base.plusSeconds(seconds);

    assertEquals(hour, t.getHour());
    assertEquals(min, t.getMinute());
    assertEquals(sec, t.getSecond());
  }

  @Test
  public void test_plusSeconds_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusSeconds(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusSeconds_noChange_oneDay_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusSeconds(24 * 60 * 60);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusSeconds_toMidnight_equal() {
    LocalTime t = LocalTime.of(23, 59, 59).plusSeconds(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_plusSeconds_toMidday_equal() {
    LocalTime t = LocalTime.of(11, 59, 59).plusSeconds(1);
    assertEquals(t, LocalTime.NOON);
  }

  // -----------------------------------------------------------------------
  // plusNanos()
  // -----------------------------------------------------------------------
  @Test
  public void test_plusNanos_halfABillion() {
    LocalTime t = LocalTime.MIDNIGHT;
    int hour = 0;
    int min = 0;
    int sec = 0;
    int nanos = 0;
    for (long i = 0; i < 3700 * 1000000000L; i += 500000000) {
      t = t.plusNanos(500000000);
      nanos += 500000000;
      if (nanos == 1000000000) {
        sec++;
        nanos = 0;
      }
      if (sec == 60) {
        min++;
        sec = 0;
      }
      if (min == 60) {
        hour++;
        min = 0;
      }
      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
      assertEquals(t.getSecond(), sec);
      assertEquals(t.getNano(), nanos);
    }
  }

  // @DataProvider(name = "plusNanos_fromZero")
  Iterator<Object[]> plusNanos_fromZero() {
    return new Iterator<Object[]>() {
      long delta = 7500000000L;
      long i = -3660 * 1000000000L;
      int hour = 22;
      int min = 59;
      int sec = 0;
      long nanos = 0;

      @Override
      public boolean hasNext() {
        return i <= 3660 * 1000000000L;
      }

      @Override
      public Object[] next() {
        final Object[] ret = new Object[] {i, hour, min, sec, (int) nanos};
        i += delta;
        nanos += delta;

        if (nanos >= 1000000000L) {
          sec += nanos / 1000000000L;
          nanos %= 1000000000L;

          if (sec >= 60) {
            min++;
            sec %= 60;

            if (min == 60) {
              hour++;
              min = 0;

              if (hour == 24) {
                hour = 0;
              }
            }
          }
        }

        return ret;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(/* dataProvider="plusNanos_fromZero" */ )
  public void test_plusNanos_fromZero() {
    Iterator<Object[]> data = plusNanos_fromZero();
    while (data.hasNext()) {
      Object[] objects = data.next();
      test_plusNanos_fromZero(
          (Long) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4]);
    }
  }

  public void test_plusNanos_fromZero(long nanoseconds, int hour, int min, int sec, int nanos) {
    LocalTime base = LocalTime.MIDNIGHT;
    LocalTime t = base.plusNanos(nanoseconds);

    assertEquals(hour, t.getHour());
    assertEquals(min, t.getMinute());
    assertEquals(sec, t.getSecond());
    assertEquals(nanos, t.getNano());
  }

  @Test
  public void test_plusNanos_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusNanos(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusNanos_noChange_oneDay_equal() {
    LocalTime t = TEST_12_30_40_987654321.plusNanos(24 * 60 * 60 * 1000000000L);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_plusNanos_toMidnight_equal() {
    LocalTime t = LocalTime.of(23, 59, 59, 999999999).plusNanos(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_plusNanos_toMidday_equal() {
    LocalTime t = LocalTime.of(11, 59, 59, 999999999).plusNanos(1);
    assertEquals(t, LocalTime.NOON);
  }

  // -----------------------------------------------------------------------
  // minus(MinusAdjuster)
  // -----------------------------------------------------------------------
  @Test
  public void test_minus_Adjuster() {
    TemporalAmount p = Duration.ofSeconds(62, 3);
    LocalTime t = TEST_12_30_40_987654321.minus(p);
    assertEquals(t, LocalTime.of(12, 29, 38, 987654318));
  }

  @Test
  public void test_minus_Adjuster_positiveHours() {
    TemporalAmount period = MockSimplePeriod.of(7, ChronoUnit.HOURS);
    LocalTime t = TEST_12_30_40_987654321.minus(period);
    assertEquals(t, LocalTime.of(5, 30, 40, 987654321));
  }

  @Test
  public void test_minus_Adjuster_negativeMinutes() {
    TemporalAmount period = MockSimplePeriod.of(-25, ChronoUnit.MINUTES);
    LocalTime t = TEST_12_30_40_987654321.minus(period);
    assertEquals(t, LocalTime.of(12, 55, 40, 987654321));
  }

  @Test
  public void test_minus_Adjuster_big1() {
    TemporalAmount p = Duration.ofNanos(Long.MAX_VALUE);
    LocalTime t = TEST_12_30_40_987654321.minus(p);
    assertEquals(t, TEST_12_30_40_987654321.minusNanos(Long.MAX_VALUE));
  }

  @Test
  public void test_minus_Adjuster_zero() {
    TemporalAmount p = Period.ZERO;
    LocalTime t = TEST_12_30_40_987654321.minus(p);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minus_Adjuster_wrap() {
    TemporalAmount p = Duration.ofHours(1);
    LocalTime t = LocalTime.of(0, 30).minus(p);
    assertEquals(t, LocalTime.of(23, 30));
  }

  @Test(expected = DateTimeException.class)
  public void test_minus_Adjuster_dateNotAllowed() {
    try {
      TemporalAmount period = MockSimplePeriod.of(7, ChronoUnit.MONTHS);
      TEST_12_30_40_987654321.minus(period);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_minus_Adjuster_null() {
    try {
      TEST_12_30_40_987654321.minus((TemporalAmount) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // minus(long,PeriodUnit)
  // -----------------------------------------------------------------------
  @Test
  public void test_minus_longPeriodUnit_positiveHours() {
    LocalTime t = TEST_12_30_40_987654321.minus(7, ChronoUnit.HOURS);
    assertEquals(t, LocalTime.of(5, 30, 40, 987654321));
  }

  @Test
  public void test_minus_longPeriodUnit_negativeMinutes() {
    LocalTime t = TEST_12_30_40_987654321.minus(-25, ChronoUnit.MINUTES);
    assertEquals(t, LocalTime.of(12, 55, 40, 987654321));
  }

  @Test
  public void test_minus_longPeriodUnit_zero() {
    LocalTime t = TEST_12_30_40_987654321.minus(0, ChronoUnit.MINUTES);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minus_long_unit_invalidUnit() {
    for (TemporalUnit unit : INVALID_UNITS) {
      try {
        TEST_12_30_40_987654321.minus(1, unit);
        fail("Unit should not be allowed " + unit);
        fail("Missing exception");
      } catch (DateTimeException ex) {
        // expected
      }
    }
  }

  @Test(expected = UnsupportedTemporalTypeException.class)
  public void test_minus_long_multiples() {
    try {
      TEST_12_30_40_987654321.minus(0, DAYS);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_minus_longPeriodUnit_null() {
    try {
      TEST_12_30_40_987654321.minus(1, (TemporalUnit) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // minusHours()
  // -----------------------------------------------------------------------
  @Test
  public void test_minusHours_one() {
    LocalTime t = LocalTime.MIDNIGHT;
    for (int i = 0; i < 50; i++) {
      t = t.minusHours(1);
      assertEquals(String.valueOf(i), t.getHour(), (((-i + 23) % 24) + 24) % 24);
    }
  }

  @Test
  public void test_minusHours_fromZero() {
    LocalTime base = LocalTime.MIDNIGHT;
    for (int i = -50; i < 50; i++) {
      LocalTime t = base.minusHours(i);
      assertEquals(t.getHour(), ((-i % 24) + 24) % 24);
    }
  }

  @Test
  public void test_minusHours_fromOne() {
    LocalTime base = LocalTime.of(1, 0);
    for (int i = -50; i < 50; i++) {
      LocalTime t = base.minusHours(i);
      assertEquals(t.getHour(), (1 + (-i % 24) + 24) % 24);
    }
  }

  @Test
  public void test_minusHours_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusHours(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusHours_toMidnight_equal() {
    LocalTime t = LocalTime.of(1, 0).minusHours(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_minusHours_toMidday_equal() {
    LocalTime t = LocalTime.of(13, 0).minusHours(1);
    assertEquals(t, LocalTime.NOON);
  }

  @Test
  public void test_minusHours_big() {
    LocalTime t = LocalTime.of(2, 30).minusHours(Long.MAX_VALUE);
    int hours = (int) (Long.MAX_VALUE % 24L);
    assertEquals(t, LocalTime.of(2, 30).minusHours(hours));
  }

  // -----------------------------------------------------------------------
  // minusMinutes()
  // -----------------------------------------------------------------------
  @Test
  public void test_minusMinutes_one() {
    LocalTime t = LocalTime.MIDNIGHT;
    int hour = 0;
    int min = 0;
    for (int i = 0; i < 70; i++) {
      t = t.minusMinutes(1);
      min--;
      if (min == -1) {
        hour--;
        min = 59;

        if (hour == -1) {
          hour = 23;
        }
      }
      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
    }
  }

  @Test
  public void test_minusMinutes_fromZero() {
    LocalTime base = LocalTime.MIDNIGHT;
    int hour = 22;
    int min = 49;
    for (int i = 70; i > -70; i--) {
      LocalTime t = base.minusMinutes(i);
      min++;

      if (min == 60) {
        hour++;
        min = 0;

        if (hour == 24) {
          hour = 0;
        }
      }

      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
    }
  }

  @Test
  public void test_minusMinutes_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusMinutes(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusMinutes_noChange_oneDay_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusMinutes(24 * 60);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusMinutes_toMidnight_equal() {
    LocalTime t = LocalTime.of(0, 1).minusMinutes(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_minusMinutes_toMidday_equals() {
    LocalTime t = LocalTime.of(12, 1).minusMinutes(1);
    assertEquals(t, LocalTime.NOON);
  }

  @Test
  public void test_minusMinutes_big() {
    LocalTime t = LocalTime.of(2, 30).minusMinutes(Long.MAX_VALUE);
    int mins = (int) (Long.MAX_VALUE % (24L * 60L));
    assertEquals(t, LocalTime.of(2, 30).minusMinutes(mins));
  }

  // -----------------------------------------------------------------------
  // minusSeconds()
  // -----------------------------------------------------------------------
  @Test
  public void test_minusSeconds_one() {
    LocalTime t = LocalTime.MIDNIGHT;
    int hour = 0;
    int min = 0;
    int sec = 0;
    for (int i = 0; i < 3700; i++) {
      t = t.minusSeconds(1);
      sec--;
      if (sec == -1) {
        min--;
        sec = 59;

        if (min == -1) {
          hour--;
          min = 59;

          if (hour == -1) {
            hour = 23;
          }
        }
      }
      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
      assertEquals(t.getSecond(), sec);
    }
  }

  // @DataProvider(name = "minusSeconds_fromZero")
  Iterator<Object[]> minusSeconds_fromZero() {
    return new Iterator<Object[]>() {
      int delta = 30;
      int i = 3660;
      int hour = 22;
      int min = 59;
      int sec = 0;

      @Override
      public boolean hasNext() {
        return i >= -3660;
      }

      @Override
      public Object[] next() {
        final Object[] ret = new Object[] {i, hour, min, sec};
        i -= delta;
        sec += delta;

        if (sec >= 60) {
          min++;
          sec -= 60;

          if (min == 60) {
            hour++;
            min = 0;

            if (hour == 24) {
              hour = 0;
            }
          }
        }

        return ret;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(/* dataProvider="minusSeconds_fromZero" */ )
  public void test_minusSeconds_fromZero() {
    Iterator<Object[]> data = minusSeconds_fromZero();
    while (data.hasNext()) {
      Object[] objects = data.next();
      test_minusSeconds_fromZero(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_minusSeconds_fromZero(int seconds, int hour, int min, int sec) {
    LocalTime base = LocalTime.MIDNIGHT;
    LocalTime t = base.minusSeconds(seconds);

    assertEquals(t.getHour(), hour);
    assertEquals(t.getMinute(), min);
    assertEquals(t.getSecond(), sec);
  }

  @Test
  public void test_minusSeconds_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusSeconds(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusSeconds_noChange_oneDay_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusSeconds(24 * 60 * 60);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusSeconds_toMidnight_equal() {
    LocalTime t = LocalTime.of(0, 0, 1).minusSeconds(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_minusSeconds_toMidday_equal() {
    LocalTime t = LocalTime.of(12, 0, 1).minusSeconds(1);
    assertEquals(t, LocalTime.NOON);
  }

  @Test
  public void test_minusSeconds_big() {
    LocalTime t = LocalTime.of(2, 30).minusSeconds(Long.MAX_VALUE);
    int secs = (int) (Long.MAX_VALUE % (24L * 60L * 60L));
    assertEquals(t, LocalTime.of(2, 30).minusSeconds(secs));
  }

  // -----------------------------------------------------------------------
  // minusNanos()
  // -----------------------------------------------------------------------
  @Test
  public void test_minusNanos_halfABillion() {
    LocalTime t = LocalTime.MIDNIGHT;
    int hour = 0;
    int min = 0;
    int sec = 0;
    int nanos = 0;
    for (long i = 0; i < 3700 * 1000000000L; i += 500000000) {
      t = t.minusNanos(500000000);
      nanos -= 500000000;

      if (nanos < 0) {
        sec--;
        nanos += 1000000000;

        if (sec == -1) {
          min--;
          sec += 60;

          if (min == -1) {
            hour--;
            min += 60;

            if (hour == -1) {
              hour += 24;
            }
          }
        }
      }

      assertEquals(t.getHour(), hour);
      assertEquals(t.getMinute(), min);
      assertEquals(t.getSecond(), sec);
      assertEquals(t.getNano(), nanos);
    }
  }

  // @DataProvider(name = "minusNanos_fromZero")
  Iterator<Object[]> minusNanos_fromZero() {
    return new Iterator<Object[]>() {
      long delta = 7500000000L;
      long i = 3660 * 1000000000L;
      int hour = 22;
      int min = 59;
      int sec = 0;
      long nanos = 0;

      @Override
      public boolean hasNext() {
        return i >= -3660 * 1000000000L;
      }

      @Override
      public Object[] next() {
        final Object[] ret = new Object[] {i, hour, min, sec, (int) nanos};
        i -= delta;
        nanos += delta;

        if (nanos >= 1000000000L) {
          sec += nanos / 1000000000L;
          nanos %= 1000000000L;

          if (sec >= 60) {
            min++;
            sec %= 60;

            if (min == 60) {
              hour++;
              min = 0;

              if (hour == 24) {
                hour = 0;
              }
            }
          }
        }

        return ret;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(/* dataProvider="minusNanos_fromZero" */ )
  public void test_minusNanos_fromZero() {
    Iterator<Object[]> data = minusNanos_fromZero();
    while (data.hasNext()) {
      Object[] objects = data.next();
      test_minusNanos_fromZero(
          (Long) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (Integer) objects[4]);
    }
  }

  public void test_minusNanos_fromZero(long nanoseconds, int hour, int min, int sec, int nanos) {
    LocalTime base = LocalTime.MIDNIGHT;
    LocalTime t = base.minusNanos(nanoseconds);

    assertEquals(hour, t.getHour());
    assertEquals(min, t.getMinute());
    assertEquals(sec, t.getSecond());
    assertEquals(nanos, t.getNano());
  }

  @Test
  public void test_minusNanos_noChange_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusNanos(0);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusNanos_noChange_oneDay_equal() {
    LocalTime t = TEST_12_30_40_987654321.minusNanos(24 * 60 * 60 * 1000000000L);
    assertEquals(t, TEST_12_30_40_987654321);
  }

  @Test
  public void test_minusNanos_toMidnight_equal() {
    LocalTime t = LocalTime.of(0, 0, 0, 1).minusNanos(1);
    assertEquals(t, LocalTime.MIDNIGHT);
  }

  @Test
  public void test_minusNanos_toMidday_equal() {
    LocalTime t = LocalTime.of(12, 0, 0, 1).minusNanos(1);
    assertEquals(t, LocalTime.NOON);
  }

  // -----------------------------------------------------------------------
  // until()
  // -----------------------------------------------------------------------
  // @DataProvider(name = "until")
  Object[][] provider_until() {
    return new Object[][] {
      {"00:00", "00:00", NANOS, 0l},
      {"00:00", "00:00", MICROS, 0l},
      {"00:00", "00:00", MILLIS, 0l},
      {"00:00", "00:00", SECONDS, 0l},
      {"00:00", "00:00", MINUTES, 0l},
      {"00:00", "00:00", HOURS, 0l},
      {"00:00", "00:00", HALF_DAYS, 0l},
      {"00:00", "00:00:01", NANOS, 1000000000l},
      {"00:00", "00:00:01", MICROS, 1000000l},
      {"00:00", "00:00:01", MILLIS, 1000l},
      {"00:00", "00:00:01", SECONDS, 1l},
      {"00:00", "00:00:01", MINUTES, 0l},
      {"00:00", "00:00:01", HOURS, 0l},
      {"00:00", "00:00:01", HALF_DAYS, 0l},
      {"00:00", "00:01", NANOS, 60000000000L},
      {"00:00", "00:01", MICROS, 60000000l},
      {"00:00", "00:01", MILLIS, 60000l},
      {"00:00", "00:01", SECONDS, 60l},
      {"00:00", "00:01", MINUTES, 1l},
      {"00:00", "00:01", HOURS, 0l},
      {"00:00", "00:01", HALF_DAYS, 0l},
    };
  }

  @Test(/* dataProvider = "until" */ )
  public void test_until() {
    Object[][] data = provider_until();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      String startStr = (String) objects[0];
      String endStr = (String) objects[1];
      TemporalUnit unit = (TemporalUnit) objects[2];
      Long expected = (Long) objects[3];
      test_until(startStr, endStr, unit, expected);
    }
  }

  public void test_until(String startStr, String endStr, TemporalUnit unit, long expected) {
    LocalTime start = LocalTime.parse(startStr);
    LocalTime end = LocalTime.parse(endStr);
    assertEquals(start.until(end, unit), expected);
    assertEquals(end.until(start, unit), -expected);
  }

  // -----------------------------------------------------------------------
  // atDate()
  // -----------------------------------------------------------------------
  @Test
  public void test_atDate() {
    LocalTime t = LocalTime.of(11, 30);
    assertEquals(t.atDate(LocalDate.of(2012, 6, 30)), LocalDateTime.of(2012, 6, 30, 11, 30));
  }

  @Test(expected = NullPointerException.class)
  public void test_atDate_nullDate() {
    try {
      TEST_12_30_40_987654321.atDate((LocalDate) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // toSecondOfDay()
  // -----------------------------------------------------------------------
  @Test
  public void test_toSecondOfDay() {
    LocalTime t = LocalTime.of(0, 0);
    for (int i = 0; i < 24 * 60 * 60; i++) {
      assertEquals(t.toSecondOfDay(), i);
      t = t.plusSeconds(1);
    }
  }

  @Test
  public void test_toSecondOfDay_fromNanoOfDay_symmetry() {
    LocalTime t = LocalTime.of(0, 0);
    for (int i = 0; i < 24 * 60 * 60; i++) {
      assertEquals(LocalTime.ofSecondOfDay(t.toSecondOfDay()), t);
      t = t.plusSeconds(1);
    }
  }

  // -----------------------------------------------------------------------
  // toNanoOfDay()
  // -----------------------------------------------------------------------
  @Test
  public void long_test_toNanoOfDay() {
    LocalTime t = LocalTime.of(0, 0);
    for (int i = 0; i < 1000000; i++) {
      assertEquals(t.toNanoOfDay(), i);
      t = t.plusNanos(1);
    }
    t = LocalTime.of(0, 0);
    for (int i = 1; i <= 1000000; i++) {
      t = t.minusNanos(1);
      assertEquals(t.toNanoOfDay(), 24 * 60 * 60 * 1000000000L - i);
    }
  }

  @Test
  public void long_test_toNanoOfDay_fromNanoOfDay_symmetry() {
    LocalTime t = LocalTime.of(0, 0);
    for (int i = 0; i < 1000000; i++) {
      assertEquals(LocalTime.ofNanoOfDay(t.toNanoOfDay()), t);
      t = t.plusNanos(1);
    }
    t = LocalTime.of(0, 0);
    for (int i = 1; i <= 1000000; i++) {
      t = t.minusNanos(1);
      assertEquals(LocalTime.ofNanoOfDay(t.toNanoOfDay()), t);
    }
  }

  // -----------------------------------------------------------------------
  // compareTo()
  // -----------------------------------------------------------------------
  @Test
  public void test_comparisons() {
    doTest_comparisons_LocalTime(
        LocalTime.MIDNIGHT,
        LocalTime.of(0, 0, 0, 999999999),
        LocalTime.of(0, 0, 59, 0),
        LocalTime.of(0, 0, 59, 999999999),
        LocalTime.of(0, 59, 0, 0),
        LocalTime.of(0, 59, 0, 999999999),
        LocalTime.of(0, 59, 59, 0),
        LocalTime.of(0, 59, 59, 999999999),
        LocalTime.NOON,
        LocalTime.of(12, 0, 0, 999999999),
        LocalTime.of(12, 0, 59, 0),
        LocalTime.of(12, 0, 59, 999999999),
        LocalTime.of(12, 59, 0, 0),
        LocalTime.of(12, 59, 0, 999999999),
        LocalTime.of(12, 59, 59, 0),
        LocalTime.of(12, 59, 59, 999999999),
        LocalTime.of(23, 0, 0, 0),
        LocalTime.of(23, 0, 0, 999999999),
        LocalTime.of(23, 0, 59, 0),
        LocalTime.of(23, 0, 59, 999999999),
        LocalTime.of(23, 59, 0, 0),
        LocalTime.of(23, 59, 0, 999999999),
        LocalTime.of(23, 59, 59, 0),
        LocalTime.of(23, 59, 59, 999999999));
  }

  void doTest_comparisons_LocalTime(LocalTime... localTimes) {
    for (int i = 0; i < localTimes.length; i++) {
      LocalTime a = localTimes[i];
      for (int j = 0; j < localTimes.length; j++) {
        LocalTime b = localTimes[j];
        if (i < j) {
          assertTrue(a + " <=> " + b, a.compareTo(b) < 0);
          assertEquals(a + " <=> " + b, a.isBefore(b), true);
          assertEquals(a + " <=> " + b, a.isAfter(b), false);
          assertEquals(a + " <=> " + b, a.equals(b), false);
        } else if (i > j) {
          assertTrue(a + " <=> " + b, a.compareTo(b) > 0);
          assertEquals(a + " <=> " + b, a.isBefore(b), false);
          assertEquals(a + " <=> " + b, a.isAfter(b), true);
          assertEquals(a + " <=> " + b, a.equals(b), false);
        } else {
          assertEquals(a + " <=> " + b, a.compareTo(b), 0);
          assertEquals(a + " <=> " + b, a.isBefore(b), false);
          assertEquals(a + " <=> " + b, a.isAfter(b), false);
          assertEquals(a + " <=> " + b, a.equals(b), true);
        }
      }
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_compareTo_ObjectNull() {
    try {
      TEST_12_30_40_987654321.compareTo(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_isBefore_ObjectNull() {
    try {
      TEST_12_30_40_987654321.isBefore(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_isAfter_ObjectNull() {
    try {
      TEST_12_30_40_987654321.isAfter(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = ClassCastException.class)
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void test_compareToNonLocalTime() {
    try {
      Comparable c = TEST_12_30_40_987654321;
      c.compareTo(new Object());
      fail("Missing exception");
    } catch (ClassCastException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // equals()
  // -----------------------------------------------------------------------
  @Test(/* dataProvider="sampleTimes" */ )
  public void test_equals_true() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_equals_true(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_equals_true(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m, s, n);
    assertEquals(a.equals(b), true);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_equals_false_hour_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_equals_false_hour_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_equals_false_hour_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h + 1, m, s, n);
    assertEquals(a.equals(b), false);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_equals_false_minute_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_equals_false_minute_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_equals_false_minute_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m + 1, s, n);
    assertEquals(a.equals(b), false);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_equals_false_second_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_equals_false_second_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_equals_false_second_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m, s + 1, n);
    assertEquals(a.equals(b), false);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_equals_false_nano_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_equals_false_nano_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_equals_false_nano_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m, s, n + 1);
    assertEquals(a.equals(b), false);
  }

  @Test
  public void test_equals_itself_true() {
    assertEquals(TEST_12_30_40_987654321.equals(TEST_12_30_40_987654321), true);
  }

  @Test
  public void test_equals_string_false() {
    assertEquals(TEST_12_30_40_987654321.equals("2007-07-15"), false);
  }

  @Test
  public void test_equals_null_false() {
    assertEquals(TEST_12_30_40_987654321.equals(null), false);
  }

  // -----------------------------------------------------------------------
  // hashCode()
  // -----------------------------------------------------------------------
  @Test(/* dataProvider="sampleTimes" */ )
  public void test_hashCode_same() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_hashCode_same(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_hashCode_same(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m, s, n);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_hashCode_hour_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_hashCode_hour_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_hashCode_hour_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h + 1, m, s, n);
    assertEquals(a.hashCode() == b.hashCode(), false);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_hashCode_minute_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_hashCode_minute_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_hashCode_minute_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m + 1, s, n);
    assertEquals(a.hashCode() == b.hashCode(), false);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_hashCode_second_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_hashCode_second_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_hashCode_second_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m, s + 1, n);
    assertEquals(a.hashCode() == b.hashCode(), false);
  }

  @Test(/* dataProvider="sampleTimes" */ )
  public void test_hashCode_nano_differs() {
    Object[][] data = provider_sampleTimes();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_hashCode_nano_differs(
          (Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3]);
    }
  }

  public void test_hashCode_nano_differs(int h, int m, int s, int n) {
    LocalTime a = LocalTime.of(h, m, s, n);
    LocalTime b = LocalTime.of(h, m, s, n + 1);
    assertEquals(a.hashCode() == b.hashCode(), false);
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------
  // @DataProvider(name = "sampleToString")
  Object[][] provider_sampleToString() {
    return new Object[][] {
      {0, 0, 0, 0, "00:00"},
      {1, 0, 0, 0, "01:00"},
      {23, 0, 0, 0, "23:00"},
      {0, 1, 0, 0, "00:01"},
      {12, 30, 0, 0, "12:30"},
      {23, 59, 0, 0, "23:59"},
      {0, 0, 1, 0, "00:00:01"},
      {0, 0, 59, 0, "00:00:59"},
      {0, 0, 0, 100000000, "00:00:00.100"},
      {0, 0, 0, 10000000, "00:00:00.010"},
      {0, 0, 0, 1000000, "00:00:00.001"},
      {0, 0, 0, 100000, "00:00:00.000100"},
      {0, 0, 0, 10000, "00:00:00.000010"},
      {0, 0, 0, 1000, "00:00:00.000001"},
      {0, 0, 0, 100, "00:00:00.000000100"},
      {0, 0, 0, 10, "00:00:00.000000010"},
      {0, 0, 0, 1, "00:00:00.000000001"},
      {0, 0, 0, 999999999, "00:00:00.999999999"},
      {0, 0, 0, 99999999, "00:00:00.099999999"},
      {0, 0, 0, 9999999, "00:00:00.009999999"},
      {0, 0, 0, 999999, "00:00:00.000999999"},
      {0, 0, 0, 99999, "00:00:00.000099999"},
      {0, 0, 0, 9999, "00:00:00.000009999"},
      {0, 0, 0, 999, "00:00:00.000000999"},
      {0, 0, 0, 99, "00:00:00.000000099"},
      {0, 0, 0, 9, "00:00:00.000000009"},
    };
  }

  @Test(/* dataProvider="sampleToString" */ )
  public void test_toString() {
    Object[][] data = provider_sampleToString();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_toString(
          (Integer) objects[0],
          (Integer) objects[1],
          (Integer) objects[2],
          (Integer) objects[3],
          (String) objects[4]);
    }
  }

  public void test_toString(int h, int m, int s, int n, String expected) {
    LocalTime t = LocalTime.of(h, m, s, n);
    String str = t.toString();
    assertEquals(str, expected);
  }

  // -----------------------------------------------------------------------
  // format(DateTimeFormatter)
  // -----------------------------------------------------------------------
  @Test
  public void test_format_formatter() {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("H m s");
    String t = LocalTime.of(11, 30, 45).format(f);
    assertEquals(t, "11 30 45");
  }

  @Test(expected = NullPointerException.class)
  public void test_format_formatter_null() {
    try {
      LocalTime.of(11, 30, 45).format(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }
}
