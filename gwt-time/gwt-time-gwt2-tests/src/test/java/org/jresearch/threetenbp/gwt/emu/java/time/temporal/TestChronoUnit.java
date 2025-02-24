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
package org.jresearch.threetenbp.gwt.emu.java.time.temporal;

import static org.jresearch.threetenbp.gwt.emu.java.time.Month.AUGUST;
import static org.jresearch.threetenbp.gwt.emu.java.time.Month.FEBRUARY;
import static org.jresearch.threetenbp.gwt.emu.java.time.Month.JULY;
import static org.jresearch.threetenbp.gwt.emu.java.time.Month.JUNE;
import static org.jresearch.threetenbp.gwt.emu.java.time.Month.MARCH;
import static org.jresearch.threetenbp.gwt.emu.java.time.Month.OCTOBER;
import static org.jresearch.threetenbp.gwt.emu.java.time.Month.SEPTEMBER;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.DAYS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.FOREVER;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.MONTHS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.WEEKS;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoUnit.YEARS;

import org.jresearch.threetenbp.gwt.emu.java.time.AbstractTest;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalDate;
import org.jresearch.threetenbp.gwt.emu.java.time.Month;
import org.jresearch.threetenbp.gwt.emu.java.time.ZoneOffset;
import org.junit.Test;

/** Test. */
// @Test
public class TestChronoUnit extends AbstractTest {

  // -----------------------------------------------------------------------
  // @DataProvider(name = "yearsBetween")
  Object[][] data_yearsBetween() {
    return new Object[][] {
      {date(1939, SEPTEMBER, 2), date(1939, SEPTEMBER, 1), 0},
      {date(1939, SEPTEMBER, 2), date(1939, SEPTEMBER, 2), 0},
      {date(1939, SEPTEMBER, 2), date(1939, SEPTEMBER, 3), 0},
      {date(1939, SEPTEMBER, 2), date(1940, SEPTEMBER, 1), 0},
      {date(1939, SEPTEMBER, 2), date(1940, SEPTEMBER, 2), 1},
      {date(1939, SEPTEMBER, 2), date(1940, SEPTEMBER, 3), 1},
      {date(1939, SEPTEMBER, 2), date(1938, SEPTEMBER, 1), -1},
      {date(1939, SEPTEMBER, 2), date(1938, SEPTEMBER, 2), -1},
      {date(1939, SEPTEMBER, 2), date(1938, SEPTEMBER, 3), 0},
      {date(1939, SEPTEMBER, 2), date(1945, SEPTEMBER, 3), 6},
      {date(1939, SEPTEMBER, 2), date(1945, OCTOBER, 3), 6},
      {date(1939, SEPTEMBER, 2), date(1945, AUGUST, 3), 5},
    };
  }

  @Test(/* dataProvider = "yearsBetween" */ )
  public void test_yearsBetween() {
    Object[][] data = data_yearsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_yearsBetween((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_yearsBetween(LocalDate start, LocalDate end, long expected) {
    assertEquals(YEARS.between(start, end), expected);
  }

  @Test(/* dataProvider = "yearsBetween" */ )
  public void test_yearsBetweenReversed() {
    Object[][] data = data_yearsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_yearsBetweenReversed((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_yearsBetweenReversed(LocalDate start, LocalDate end, long expected) {
    assertEquals(YEARS.between(end, start), -expected);
  }

  @Test(/* dataProvider = "yearsBetween" */ )
  public void test_yearsBetween_LocalDateTimeSameTime() {
    Object[][] data = data_yearsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_yearsBetween_LocalDateTimeSameTime(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_yearsBetween_LocalDateTimeSameTime(
      LocalDate start, LocalDate end, long expected) {
    assertEquals(YEARS.between(start.atTime(12, 30), end.atTime(12, 30)), expected);
  }

  @Test(/* dataProvider = "yearsBetween" */ )
  public void test_yearsBetween_LocalDateTimeLaterTime() {
    Object[][] data = data_yearsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_yearsBetween_LocalDateTimeLaterTime(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_yearsBetween_LocalDateTimeLaterTime(
      LocalDate start, LocalDate end, long expected) {
    if (end.isAfter(start)) {
      assertEquals(YEARS.between(start.atTime(12, 30), end.atTime(12, 31)), expected);
    } else {
      assertEquals(YEARS.between(start.atTime(12, 31), end.atTime(12, 30)), expected);
    }
  }

  @Test(/* dataProvider = "yearsBetween" */ )
  public void test_yearsBetween_ZonedDateSameOffset() {
    Object[][] data = data_yearsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_yearsBetween_ZonedDateSameOffset(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_yearsBetween_ZonedDateSameOffset(LocalDate start, LocalDate end, long expected) {
    assertEquals(
        YEARS.between(
            start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(2))),
        expected);
  }

  @Test(/* dataProvider = "yearsBetween" */ )
  public void test_yearsBetween_ZonedDateLaterOffset() {
    Object[][] data = data_yearsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_yearsBetween_ZonedDateLaterOffset(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_yearsBetween_ZonedDateLaterOffset(
      LocalDate start, LocalDate end, long expected) {
    // +01:00 is later than +02:00
    if (end.isAfter(start)) {
      assertEquals(
          YEARS.between(
              start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(1))),
          expected);
    } else {
      assertEquals(
          YEARS.between(
              start.atStartOfDay(ZoneOffset.ofHours(1)), end.atStartOfDay(ZoneOffset.ofHours(2))),
          expected);
    }
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "monthsBetween")
  Object[][] data_monthsBetween() {
    return new Object[][] {
      {date(2012, JULY, 2), date(2012, JULY, 1), 0},
      {date(2012, JULY, 2), date(2012, JULY, 2), 0},
      {date(2012, JULY, 2), date(2012, JULY, 3), 0},
      {date(2012, JULY, 2), date(2012, AUGUST, 1), 0},
      {date(2012, JULY, 2), date(2012, AUGUST, 2), 1},
      {date(2012, JULY, 2), date(2012, AUGUST, 3), 1},
      {date(2012, JULY, 2), date(2012, SEPTEMBER, 1), 1},
      {date(2012, JULY, 2), date(2012, SEPTEMBER, 2), 2},
      {date(2012, JULY, 2), date(2012, SEPTEMBER, 3), 2},
      {date(2012, JULY, 2), date(2012, JUNE, 1), -1},
      {date(2012, JULY, 2), date(2012, JUNE, 2), -1},
      {date(2012, JULY, 2), date(2012, JUNE, 3), 0},
      {date(2012, FEBRUARY, 27), date(2012, MARCH, 26), 0},
      {date(2012, FEBRUARY, 27), date(2012, MARCH, 27), 1},
      {date(2012, FEBRUARY, 27), date(2012, MARCH, 28), 1},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 27), 0},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 28), 1},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 29), 1},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 28), 0},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 29), 1},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 30), 1},
    };
  }

  @Test(/* dataProvider = "monthsBetween" */ )
  public void test_monthsBetween() {
    Object[][] data = data_monthsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_monthsBetween((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_monthsBetween(LocalDate start, LocalDate end, long expected) {
    assertEquals(MONTHS.between(start, end), expected);
  }

  @Test(/* dataProvider = "monthsBetween" */ )
  public void test_monthsBetweenReversed() {
    Object[][] data = data_monthsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_monthsBetweenReversed(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_monthsBetweenReversed(LocalDate start, LocalDate end, long expected) {
    assertEquals(MONTHS.between(end, start), -expected);
  }

  @Test(/* dataProvider = "monthsBetween" */ )
  public void test_monthsBetween_LocalDateTimeSameTime() {
    Object[][] data = data_monthsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_monthsBetween_LocalDateTimeSameTime(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_monthsBetween_LocalDateTimeSameTime(
      LocalDate start, LocalDate end, long expected) {
    assertEquals(MONTHS.between(start.atTime(12, 30), end.atTime(12, 30)), expected);
  }

  @Test(/* dataProvider = "monthsBetween" */ )
  public void test_monthsBetween_LocalDateTimeLaterTime() {
    Object[][] data = data_monthsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_monthsBetween_LocalDateTimeLaterTime(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_monthsBetween_LocalDateTimeLaterTime(
      LocalDate start, LocalDate end, long expected) {
    if (end.isAfter(start)) {
      assertEquals(MONTHS.between(start.atTime(12, 30), end.atTime(12, 31)), expected);
    } else {
      assertEquals(MONTHS.between(start.atTime(12, 31), end.atTime(12, 30)), expected);
    }
  }

  @Test(/* dataProvider = "monthsBetween" */ )
  public void test_monthsBetween_ZonedDateSameOffset() {
    Object[][] data = data_monthsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_monthsBetween_ZonedDateSameOffset(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_monthsBetween_ZonedDateSameOffset(
      LocalDate start, LocalDate end, long expected) {
    assertEquals(
        MONTHS.between(
            start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(2))),
        expected);
  }

  @Test(/* dataProvider = "monthsBetween" */ )
  public void test_monthsBetween_ZonedDateLaterOffset() {
    Object[][] data = data_monthsBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_monthsBetween_ZonedDateLaterOffset(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_monthsBetween_ZonedDateLaterOffset(
      LocalDate start, LocalDate end, long expected) {
    // +01:00 is later than +02:00
    if (end.isAfter(start)) {
      assertEquals(
          MONTHS.between(
              start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(1))),
          expected);
    } else {
      assertEquals(
          MONTHS.between(
              start.atStartOfDay(ZoneOffset.ofHours(1)), end.atStartOfDay(ZoneOffset.ofHours(2))),
          expected);
    }
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "weeksBetween")
  Object[][] data_weeksBetween() {
    return new Object[][] {
      {date(2012, JULY, 2), date(2012, JUNE, 25), -1},
      {date(2012, JULY, 2), date(2012, JUNE, 26), 0},
      {date(2012, JULY, 2), date(2012, JULY, 2), 0},
      {date(2012, JULY, 2), date(2012, JULY, 8), 0},
      {date(2012, JULY, 2), date(2012, JULY, 9), 1},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 21), -1},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 22), 0},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 28), 0},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 29), 0},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 1), 0},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 5), 0},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 6), 1},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 22), -1},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 23), 0},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 28), 0},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 29), 0},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 1), 0},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 6), 0},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 7), 1},
    };
  }

  @Test(/* dataProvider = "weeksBetween" */ )
  public void test_weeksBetween() {
    Object[][] data = data_weeksBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_weeksBetween((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_weeksBetween(LocalDate start, LocalDate end, long expected) {
    assertEquals(WEEKS.between(start, end), expected);
  }

  @Test(/* dataProvider = "weeksBetween" */ )
  public void test_weeksBetweenReversed() {
    Object[][] data = data_weeksBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_weeksBetweenReversed((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_weeksBetweenReversed(LocalDate start, LocalDate end, long expected) {
    assertEquals(WEEKS.between(end, start), -expected);
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "daysBetween")
  Object[][] data_daysBetween() {
    return new Object[][] {
      {date(2012, JULY, 2), date(2012, JULY, 1), -1},
      {date(2012, JULY, 2), date(2012, JULY, 2), 0},
      {date(2012, JULY, 2), date(2012, JULY, 3), 1},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 27), -1},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 28), 0},
      {date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 29), 1},
      {date(2012, FEBRUARY, 28), date(2012, MARCH, 1), 2},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 27), -2},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 28), -1},
      {date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 29), 0},
      {date(2012, FEBRUARY, 29), date(2012, MARCH, 1), 1},
      {date(2012, MARCH, 1), date(2012, FEBRUARY, 27), -3},
      {date(2012, MARCH, 1), date(2012, FEBRUARY, 28), -2},
      {date(2012, MARCH, 1), date(2012, FEBRUARY, 29), -1},
      {date(2012, MARCH, 1), date(2012, MARCH, 1), 0},
      {date(2012, MARCH, 1), date(2012, MARCH, 2), 1},
      {date(2012, MARCH, 1), date(2013, FEBRUARY, 28), 364},
      {date(2012, MARCH, 1), date(2013, MARCH, 1), 365},
      {date(2011, MARCH, 1), date(2012, FEBRUARY, 28), 364},
      {date(2011, MARCH, 1), date(2012, FEBRUARY, 29), 365},
      {date(2011, MARCH, 1), date(2012, MARCH, 1), 366},
    };
  }

  @Test(/* dataProvider = "daysBetween" */ )
  public void test_daysBetween() {
    Object[][] data = data_daysBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_daysBetween((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_daysBetween(LocalDate start, LocalDate end, long expected) {
    assertEquals(DAYS.between(start, end), expected);
  }

  @Test(/* dataProvider = "daysBetween" */ )
  public void test_daysBetweenReversed() {
    Object[][] data = data_daysBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_daysBetweenReversed((LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_daysBetweenReversed(LocalDate start, LocalDate end, long expected) {
    assertEquals(DAYS.between(end, start), -expected);
  }

  @Test(/* dataProvider = "daysBetween" */ )
  public void test_daysBetween_LocalDateTimeSameTime() {
    Object[][] data = data_daysBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_daysBetween_LocalDateTimeSameTime(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_daysBetween_LocalDateTimeSameTime(
      LocalDate start, LocalDate end, long expected) {
    assertEquals(DAYS.between(start.atTime(12, 30), end.atTime(12, 30)), expected);
  }

  @Test(/* dataProvider = "daysBetween" */ )
  public void test_daysBetween_LocalDateTimeLaterTime() {
    Object[][] data = data_daysBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_daysBetween_LocalDateTimeLaterTime(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_daysBetween_LocalDateTimeLaterTime(
      LocalDate start, LocalDate end, long expected) {
    if (end.isAfter(start)) {
      assertEquals(DAYS.between(start.atTime(12, 30), end.atTime(12, 31)), expected);
    } else {
      assertEquals(DAYS.between(start.atTime(12, 31), end.atTime(12, 30)), expected);
    }
  }

  @Test(/* dataProvider = "daysBetween" */ )
  public void test_daysBetween_ZonedDateSameOffset() {
    Object[][] data = data_daysBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_daysBetween_ZonedDateSameOffset(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_daysBetween_ZonedDateSameOffset(LocalDate start, LocalDate end, long expected) {
    assertEquals(
        DAYS.between(
            start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(2))),
        expected);
  }

  @Test(/* dataProvider = "daysBetween" */ )
  public void test_daysBetween_ZonedDateLaterOffset() {
    Object[][] data = data_daysBetween();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_daysBetween_ZonedDateLaterOffset(
          (LocalDate) objects[0], (LocalDate) objects[1], toLong(objects[2]));
    }
  }

  public void test_daysBetween_ZonedDateLaterOffset(LocalDate start, LocalDate end, long expected) {
    // +01:00 is later than +02:00
    if (end.isAfter(start)) {
      assertEquals(
          DAYS.between(
              start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(1))),
          expected);
    } else {
      assertEquals(
          DAYS.between(
              start.atStartOfDay(ZoneOffset.ofHours(1)), end.atStartOfDay(ZoneOffset.ofHours(2))),
          expected);
    }
  }

  // -------------------------------------------------------------------------
  @Test
  public void test_isDateBased() {
    for (ChronoUnit unit : ChronoUnit.values()) {
      if (unit.getDuration().getSeconds() < 86400) {
        assertEquals(unit.isDateBased(), false);
      } else if (unit == FOREVER) {
        assertEquals(unit.isDateBased(), false);
      } else {
        assertEquals(unit.isDateBased(), true);
      }
    }
  }

  @Test
  public void test_isTimeBased() {
    for (ChronoUnit unit : ChronoUnit.values()) {
      if (unit.getDuration().getSeconds() < 86400) {
        assertEquals(unit.isTimeBased(), true);
      } else if (unit == FOREVER) {
        assertEquals(unit.isTimeBased(), false);
      } else {
        assertEquals(unit.isTimeBased(), false);
      }
    }
  }

  // -----------------------------------------------------------------------
  private static LocalDate date(int year, Month month, int dom) {
    return LocalDate.of(year, month, dom);
  }
}
