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
package org.jresearch.threetenbp.gwt.time.client.temporal;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import org.jresearch.threetenbp.gwt.time.client.AbstractTest;
import org.junit.Test;

/** Test DateTimeAdjusters. */
// @Test
public class TestTemporalAdjusters extends AbstractTest {

  // -----------------------------------------------------------------------
  // firstDayOfMonth()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_firstDayOfMonth() {
    assertNotNull(TemporalAdjusters.firstDayOfMonth());
  }

  @Test
  public void test_firstDayOfMonth_nonLeap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfMonth().adjustInto(date);
        assertEquals(test.getYear(), 2007);
        assertEquals(test.getMonth(), month);
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  @Test
  public void test_firstDayOfMonth_leap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(true); i++) {
        LocalDate date = date(2008, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfMonth().adjustInto(date);
        assertEquals(test.getYear(), 2008);
        assertEquals(test.getMonth(), month);
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  // -----------------------------------------------------------------------
  // lastDayOfMonth()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_lastDayOfMonth() {
    assertNotNull(TemporalAdjusters.lastDayOfMonth());
  }

  @Test
  public void test_lastDayOfMonth_nonLeap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.lastDayOfMonth().adjustInto(date);
        assertEquals(test.getYear(), 2007);
        assertEquals(test.getMonth(), month);
        assertEquals(test.getDayOfMonth(), month.length(false));
      }
    }
  }

  @Test
  public void test_lastDayOfMonth_leap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(true); i++) {
        LocalDate date = date(2008, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.lastDayOfMonth().adjustInto(date);
        assertEquals(test.getYear(), 2008);
        assertEquals(test.getMonth(), month);
        assertEquals(test.getDayOfMonth(), month.length(true));
      }
    }
  }

  // -----------------------------------------------------------------------
  // firstDayOfNextMonth()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_firstDayOfNextMonth() {
    assertNotNull(TemporalAdjusters.firstDayOfNextMonth());
  }

  @Test
  public void test_firstDayOfNextMonth_nonLeap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfNextMonth().adjustInto(date);
        assertEquals(test.getYear(), month == DECEMBER ? 2008 : 2007);
        assertEquals(test.getMonth(), month.plus(1));
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  @Test
  public void test_firstDayOfNextMonth_leap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(true); i++) {
        LocalDate date = date(2008, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfNextMonth().adjustInto(date);
        assertEquals(test.getYear(), month == DECEMBER ? 2009 : 2008);
        assertEquals(test.getMonth(), month.plus(1));
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  // -----------------------------------------------------------------------
  // firstDayOfYear()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_firstDayOfYear() {
    assertNotNull(TemporalAdjusters.firstDayOfYear());
  }

  @Test
  public void test_firstDayOfYear_nonLeap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfYear().adjustInto(date);
        assertEquals(test.getYear(), 2007);
        assertEquals(test.getMonth(), Month.JANUARY);
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  @Test
  public void test_firstDayOfYear_leap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(true); i++) {
        LocalDate date = date(2008, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfYear().adjustInto(date);
        assertEquals(test.getYear(), 2008);
        assertEquals(test.getMonth(), Month.JANUARY);
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  // -----------------------------------------------------------------------
  // lastDayOfYear()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_lastDayOfYear() {
    assertNotNull(TemporalAdjusters.lastDayOfYear());
  }

  @Test
  public void test_lastDayOfYear_nonLeap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.lastDayOfYear().adjustInto(date);
        assertEquals(test.getYear(), 2007);
        assertEquals(test.getMonth(), Month.DECEMBER);
        assertEquals(test.getDayOfMonth(), 31);
      }
    }
  }

  @Test
  public void test_lastDayOfYear_leap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(true); i++) {
        LocalDate date = date(2008, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.lastDayOfYear().adjustInto(date);
        assertEquals(test.getYear(), 2008);
        assertEquals(test.getMonth(), Month.DECEMBER);
        assertEquals(test.getDayOfMonth(), 31);
      }
    }
  }

  // -----------------------------------------------------------------------
  // firstDayOfNextYear()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_firstDayOfNextYear() {
    assertNotNull(TemporalAdjusters.firstDayOfNextYear());
  }

  @Test
  public void test_firstDayOfNextYear_nonLeap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfNextYear().adjustInto(date);
        assertEquals(test.getYear(), 2008);
        assertEquals(test.getMonth(), JANUARY);
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  @Test
  public void test_firstDayOfNextYear_leap() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(true); i++) {
        LocalDate date = date(2008, month, i);
        LocalDate test = (LocalDate) TemporalAdjusters.firstDayOfNextYear().adjustInto(date);
        assertEquals(test.getYear(), 2009);
        assertEquals(test.getMonth(), JANUARY);
        assertEquals(test.getDayOfMonth(), 1);
      }
    }
  }

  // -----------------------------------------------------------------------
  // dayOfWeekInMonth()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_dayOfWeekInMonth() {
    assertNotNull(TemporalAdjusters.dayOfWeekInMonth(1, MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_dayOfWeekInMonth_nullDayOfWeek() {
    try {
      TemporalAdjusters.dayOfWeekInMonth(1, null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // @DataProvider(name = "dayOfWeekInMonth_positive")
  Object[][] data_dayOfWeekInMonth_positive() {
    return new Object[][] {
      {2011, 1, TUESDAY, date(2011, 1, 4)},
      {2011, 2, TUESDAY, date(2011, 2, 1)},
      {2011, 3, TUESDAY, date(2011, 3, 1)},
      {2011, 4, TUESDAY, date(2011, 4, 5)},
      {2011, 5, TUESDAY, date(2011, 5, 3)},
      {2011, 6, TUESDAY, date(2011, 6, 7)},
      {2011, 7, TUESDAY, date(2011, 7, 5)},
      {2011, 8, TUESDAY, date(2011, 8, 2)},
      {2011, 9, TUESDAY, date(2011, 9, 6)},
      {2011, 10, TUESDAY, date(2011, 10, 4)},
      {2011, 11, TUESDAY, date(2011, 11, 1)},
      {2011, 12, TUESDAY, date(2011, 12, 6)},
    };
  }

  @Test(/* dataProvider = "dayOfWeekInMonth_positive" */ )
  public void test_dayOfWeekInMonth_positive() {
    Object[][] data = data_dayOfWeekInMonth_positive();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_dayOfWeekInMonth_positive(
          (int) objects[0], (int) objects[1], (DayOfWeek) objects[2], (LocalDate) objects[3]);
    }
  }

  public void test_dayOfWeekInMonth_positive(
      int year, int month, DayOfWeek dow, LocalDate expected) {
    for (int ordinal = 1; ordinal <= 5; ordinal++) {
      for (int day = 1; day <= Month.of(month).length(false); day++) {
        LocalDate date = date(year, month, day);
        LocalDate test =
            (LocalDate) TemporalAdjusters.dayOfWeekInMonth(ordinal, dow).adjustInto(date);
        assertEquals(test, expected.plusWeeks(ordinal - 1));
      }
    }
  }

  // @DataProvider(name = "dayOfWeekInMonth_zero")
  Object[][] data_dayOfWeekInMonth_zero() {
    return new Object[][] {
      {2011, 1, TUESDAY, date(2010, 12, 28)},
      {2011, 2, TUESDAY, date(2011, 1, 25)},
      {2011, 3, TUESDAY, date(2011, 2, 22)},
      {2011, 4, TUESDAY, date(2011, 3, 29)},
      {2011, 5, TUESDAY, date(2011, 4, 26)},
      {2011, 6, TUESDAY, date(2011, 5, 31)},
      {2011, 7, TUESDAY, date(2011, 6, 28)},
      {2011, 8, TUESDAY, date(2011, 7, 26)},
      {2011, 9, TUESDAY, date(2011, 8, 30)},
      {2011, 10, TUESDAY, date(2011, 9, 27)},
      {2011, 11, TUESDAY, date(2011, 10, 25)},
      {2011, 12, TUESDAY, date(2011, 11, 29)},
    };
  }

  @Test(/* dataProvider = "dayOfWeekInMonth_zero" */ )
  public void test_dayOfWeekInMonth_zero() {
    Object[][] data = data_dayOfWeekInMonth_zero();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_dayOfWeekInMonth_zero(
          (int) objects[0], (int) objects[1], (DayOfWeek) objects[2], (LocalDate) objects[3]);
    }
  }

  public void test_dayOfWeekInMonth_zero(int year, int month, DayOfWeek dow, LocalDate expected) {
    for (int day = 1; day <= Month.of(month).length(false); day++) {
      LocalDate date = date(year, month, day);
      LocalDate test = (LocalDate) TemporalAdjusters.dayOfWeekInMonth(0, dow).adjustInto(date);
      assertEquals(test, expected);
    }
  }

  // @DataProvider(name = "dayOfWeekInMonth_negative")
  Object[][] data_dayOfWeekInMonth_negative() {
    return new Object[][] {
      {2011, 1, TUESDAY, date(2011, 1, 25)},
      {2011, 2, TUESDAY, date(2011, 2, 22)},
      {2011, 3, TUESDAY, date(2011, 3, 29)},
      {2011, 4, TUESDAY, date(2011, 4, 26)},
      {2011, 5, TUESDAY, date(2011, 5, 31)},
      {2011, 6, TUESDAY, date(2011, 6, 28)},
      {2011, 7, TUESDAY, date(2011, 7, 26)},
      {2011, 8, TUESDAY, date(2011, 8, 30)},
      {2011, 9, TUESDAY, date(2011, 9, 27)},
      {2011, 10, TUESDAY, date(2011, 10, 25)},
      {2011, 11, TUESDAY, date(2011, 11, 29)},
      {2011, 12, TUESDAY, date(2011, 12, 27)},
    };
  }

  @Test(/* dataProvider = "dayOfWeekInMonth_negative" */ )
  public void test_dayOfWeekInMonth_negative() {
    Object[][] data = data_dayOfWeekInMonth_negative();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_dayOfWeekInMonth_negative(
          (int) objects[0], (int) objects[1], (DayOfWeek) objects[2], (LocalDate) objects[3]);
    }
  }

  public void test_dayOfWeekInMonth_negative(
      int year, int month, DayOfWeek dow, LocalDate expected) {
    for (int ordinal = 0; ordinal < 5; ordinal++) {
      for (int day = 1; day <= Month.of(month).length(false); day++) {
        LocalDate date = date(year, month, day);
        LocalDate test =
            (LocalDate) TemporalAdjusters.dayOfWeekInMonth(-1 - ordinal, dow).adjustInto(date);
        assertEquals(test, expected.minusWeeks(ordinal));
      }
    }
  }

  // -----------------------------------------------------------------------
  // firstInMonth()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_firstInMonth() {
    assertNotNull(TemporalAdjusters.firstInMonth(MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_firstInMonth_nullDayOfWeek() {
    try {
      TemporalAdjusters.firstInMonth(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(/* dataProvider = "dayOfWeekInMonth_positive" */ )
  public void test_firstInMonth() {
    Object[][] data = data_dayOfWeekInMonth_positive();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_firstInMonth(
          (int) objects[0], (int) objects[1], (DayOfWeek) objects[2], (LocalDate) objects[3]);
    }
  }

  public void test_firstInMonth(int year, int month, DayOfWeek dow, LocalDate expected) {
    for (int day = 1; day <= Month.of(month).length(false); day++) {
      LocalDate date = date(year, month, day);
      LocalDate test = (LocalDate) TemporalAdjusters.firstInMonth(dow).adjustInto(date);
      assertEquals("day-of-month=" + day, test, expected);
    }
  }

  // -----------------------------------------------------------------------
  // lastInMonth()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_lastInMonth() {
    assertNotNull(TemporalAdjusters.lastInMonth(MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_lastInMonth_nullDayOfWeek() {
    try {
      TemporalAdjusters.lastInMonth(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(/* dataProvider = "dayOfWeekInMonth_negative" */ )
  public void test_lastInMonth() {
    Object[][] data = data_dayOfWeekInMonth_negative();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_lastInMonth(
          (int) objects[0], (int) objects[1], (DayOfWeek) objects[2], (LocalDate) objects[3]);
    }
  }

  public void test_lastInMonth(int year, int month, DayOfWeek dow, LocalDate expected) {
    for (int day = 1; day <= Month.of(month).length(false); day++) {
      LocalDate date = date(year, month, day);
      LocalDate test = (LocalDate) TemporalAdjusters.lastInMonth(dow).adjustInto(date);
      assertEquals("day-of-month=" + day, test, expected);
    }
  }

  // -----------------------------------------------------------------------
  // next()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_next() {
    assertNotNull(TemporalAdjusters.next(MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_next_nullDayOfWeek() {
    try {
      TemporalAdjusters.next(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void test_next() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);

        for (DayOfWeek dow : DayOfWeek.values()) {
          LocalDate test = (LocalDate) TemporalAdjusters.next(dow).adjustInto(date);

          assertSame(date + " " + test, test.getDayOfWeek(), dow);

          if (test.getYear() == 2007) {
            int dayDiff = test.getDayOfYear() - date.getDayOfYear();
            assertTrue(dayDiff > 0 && dayDiff < 8);
          } else {
            assertSame(month, Month.DECEMBER);
            assertTrue(date.getDayOfMonth() > 24);
            assertEquals(test.getYear(), 2008);
            assertSame(test.getMonth(), Month.JANUARY);
            assertTrue(test.getDayOfMonth() < 8);
          }
        }
      }
    }
  }

  // -----------------------------------------------------------------------
  // nextOrSame()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_nextOrSame() {
    assertNotNull(TemporalAdjusters.nextOrSame(MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nextOrSame_nullDayOfWeek() {
    try {
      TemporalAdjusters.nextOrSame(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void test_nextOrSame() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);

        for (DayOfWeek dow : DayOfWeek.values()) {
          LocalDate test = (LocalDate) TemporalAdjusters.nextOrSame(dow).adjustInto(date);

          assertSame(test.getDayOfWeek(), dow);

          if (test.getYear() == 2007) {
            int dayDiff = test.getDayOfYear() - date.getDayOfYear();
            assertTrue(dayDiff < 8);
            assertEquals(date.equals(test), date.getDayOfWeek() == dow);
          } else {
            assertFalse(date.getDayOfWeek() == dow);
            assertSame(month, Month.DECEMBER);
            assertTrue(date.getDayOfMonth() > 24);
            assertEquals(test.getYear(), 2008);
            assertSame(test.getMonth(), Month.JANUARY);
            assertTrue(test.getDayOfMonth() < 8);
          }
        }
      }
    }
  }

  // -----------------------------------------------------------------------
  // previous()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_previous() {
    assertNotNull(TemporalAdjusters.previous(MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_previous_nullDayOfWeek() {
    try {
      TemporalAdjusters.previous(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void test_previous() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);

        for (DayOfWeek dow : DayOfWeek.values()) {
          LocalDate test = (LocalDate) TemporalAdjusters.previous(dow).adjustInto(date);

          assertSame(date + " " + test, test.getDayOfWeek(), dow);

          if (test.getYear() == 2007) {
            int dayDiff = test.getDayOfYear() - date.getDayOfYear();
            assertTrue(dayDiff + " " + test, dayDiff < 0 && dayDiff > -8);
          } else {
            assertSame(month, Month.JANUARY);
            assertTrue(date.getDayOfMonth() < 8);
            assertEquals(test.getYear(), 2006);
            assertSame(test.getMonth(), Month.DECEMBER);
            assertTrue(test.getDayOfMonth() > 24);
          }
        }
      }
    }
  }

  // -----------------------------------------------------------------------
  // previousOrSame()
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_previousOrSame() {
    assertNotNull(TemporalAdjusters.previousOrSame(MONDAY));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_previousOrSame_nullDayOfWeek() {
    try {
      TemporalAdjusters.previousOrSame(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void test_previousOrSame() {
    for (Month month : Month.values()) {
      for (int i = 1; i <= month.length(false); i++) {
        LocalDate date = date(2007, month, i);

        for (DayOfWeek dow : DayOfWeek.values()) {
          LocalDate test = (LocalDate) TemporalAdjusters.previousOrSame(dow).adjustInto(date);

          assertSame(test.getDayOfWeek(), dow);

          if (test.getYear() == 2007) {
            int dayDiff = test.getDayOfYear() - date.getDayOfYear();
            assertTrue(dayDiff <= 0 && dayDiff > -7);
            assertEquals(date.equals(test), date.getDayOfWeek() == dow);
          } else {
            assertFalse(date.getDayOfWeek() == dow);
            assertSame(month, Month.JANUARY);
            assertTrue(date.getDayOfMonth() < 7);
            assertEquals(test.getYear(), 2006);
            assertSame(test.getMonth(), Month.DECEMBER);
            assertTrue(test.getDayOfMonth() > 25);
          }
        }
      }
    }
  }

  private LocalDate date(int year, Month month, int day) {
    return LocalDate.of(year, month, day);
  }

  private LocalDate date(int year, int month, int day) {
    return LocalDate.of(year, month, day);
  }
}
