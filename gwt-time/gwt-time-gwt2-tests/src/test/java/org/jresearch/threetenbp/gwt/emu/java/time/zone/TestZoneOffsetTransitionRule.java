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
package org.jresearch.threetenbp.gwt.emu.java.time.zone;

import org.jresearch.threetenbp.gwt.emu.java.time.AbstractTest;
import org.jresearch.threetenbp.gwt.emu.java.time.DayOfWeek;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalDateTime;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalTime;
import org.jresearch.threetenbp.gwt.emu.java.time.Month;
import org.jresearch.threetenbp.gwt.emu.java.time.ZoneOffset;
import org.jresearch.threetenbp.gwt.emu.java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import org.junit.Test;

/** Test ZoneOffsetTransitionRule. */
// @Test
public class TestZoneOffsetTransitionRule extends AbstractTest {

  private static final LocalTime TIME_0100 = LocalTime.of(1, 0);
  private static final ZoneOffset OFFSET_0200 = ZoneOffset.ofHours(2);
  private static final ZoneOffset OFFSET_0300 = ZoneOffset.ofHours(3);

  // -----------------------------------------------------------------------
  // factory
  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_factory_nullMonth() {
    try {
      ZoneOffsetTransitionRule.of(
          null,
          20,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullTime() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          20,
          DayOfWeek.SUNDAY,
          null,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullTimeDefinition() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          20,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          null,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullStandardOffset() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          20,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          null,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullOffsetBefore() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          20,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          null,
          OFFSET_0300);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullOffsetAfter() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          20,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_factory_invalidDayOfMonthIndicator_tooSmall() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          -29,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_factory_invalidDayOfMonthIndicator_zero() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          0,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_factory_invalidDayOfMonthIndicator_tooLarge() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          32,
          DayOfWeek.SUNDAY,
          TIME_0100,
          false,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_factory_invalidMidnightFlag() {
    try {
      ZoneOffsetTransitionRule.of(
          Month.MARCH,
          20,
          DayOfWeek.SUNDAY,
          TIME_0100,
          true,
          TimeDefinition.WALL,
          OFFSET_0200,
          OFFSET_0200,
          OFFSET_0300);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // getters
  // -----------------------------------------------------------------------
  @Test
  public void test_getters_floatingWeek() throws Exception {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(test.getMonth(), Month.MARCH);
    assertEquals(test.getDayOfMonthIndicator(), 20);
    assertEquals(test.getDayOfWeek(), DayOfWeek.SUNDAY);
    assertEquals(test.getLocalTime(), TIME_0100);
    assertEquals(test.isMidnightEndOfDay(), false);
    assertEquals(test.getTimeDefinition(), TimeDefinition.WALL);
    assertEquals(test.getStandardOffset(), OFFSET_0200);
    assertEquals(test.getOffsetBefore(), OFFSET_0200);
    assertEquals(test.getOffsetAfter(), OFFSET_0300);
    //		assertSerializable(test);
  }

  @Test
  public void test_getters_floatingWeekBackwards() throws Exception {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -1,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(test.getMonth(), Month.MARCH);
    assertEquals(test.getDayOfMonthIndicator(), -1);
    assertEquals(test.getDayOfWeek(), DayOfWeek.SUNDAY);
    assertEquals(test.getLocalTime(), TIME_0100);
    assertEquals(test.isMidnightEndOfDay(), false);
    assertEquals(test.getTimeDefinition(), TimeDefinition.WALL);
    assertEquals(test.getStandardOffset(), OFFSET_0200);
    assertEquals(test.getOffsetBefore(), OFFSET_0200);
    assertEquals(test.getOffsetAfter(), OFFSET_0300);
    //		assertSerializable(test);
  }

  @Test
  public void test_getters_fixedDate() throws Exception {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            null,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(test.getMonth(), Month.MARCH);
    assertEquals(test.getDayOfMonthIndicator(), 20);
    assertEquals(test.getDayOfWeek(), null);
    assertEquals(test.getLocalTime(), TIME_0100);
    assertEquals(test.isMidnightEndOfDay(), false);
    assertEquals(test.getTimeDefinition(), TimeDefinition.WALL);
    assertEquals(test.getStandardOffset(), OFFSET_0200);
    assertEquals(test.getOffsetBefore(), OFFSET_0200);
    assertEquals(test.getOffsetAfter(), OFFSET_0300);
    //		assertSerializable(test);
  }

  //    @Test
  //    public void test_serialization_unusualOffsets() throws Exception {
  //        ZoneOffsetTransitionRule test = ZoneOffsetTransitionRule.of(
  //                Month.MARCH, 20, null, TIME_0100, false, TimeDefinition.STANDARD,
  //                ZoneOffset.ofHoursMinutesSeconds(-12, -20, -50),
  //                ZoneOffset.ofHoursMinutesSeconds(-4, -10, -34),
  //                ZoneOffset.ofHours(-18));
  //        assertSerializable(test);
  //    }

  //    @Test
  //    public void test_serialization_endOfDay() throws Exception {
  //        ZoneOffsetTransitionRule test = ZoneOffsetTransitionRule.of(
  //                Month.MARCH, 20, DayOfWeek.FRIDAY, LocalTime.MIDNIGHT, true, TimeDefinition.UTC,
  //                OFFSET_0200, OFFSET_0200, OFFSET_0300);
  //        assertSerializable(test);
  //    }

  //    @Test
  //    public void test_serialization_unusualTime() throws Exception {
  //        ZoneOffsetTransitionRule test = ZoneOffsetTransitionRule.of(
  //                Month.MARCH, 20, DayOfWeek.WEDNESDAY, LocalTime.of(13, 34, 56), false,
  // TimeDefinition.STANDARD,
  //                OFFSET_0200, OFFSET_0200, OFFSET_0300);
  //        assertSerializable(test);
  //    }

  //    @Test
  //    public void test_serialization_format() throws ClassNotFoundException, IOException {
  //        ZoneOffsetTransitionRule test = ZoneOffsetTransitionRule.of(
  //                        Month.MARCH, 20, DayOfWeek.TUESDAY, LocalTime.of(13, 34, 56), false,
  // TimeDefinition.STANDARD,
  //                        OFFSET_0200, OFFSET_0200, OFFSET_0300);
  //        assertEqualsSerialisedForm(test);
  //    }

  // -----------------------------------------------------------------------
  // createTransition()
  // -----------------------------------------------------------------------
  @Test
  public void test_createTransition_floatingWeek_gap_notEndOfDay() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransition trans =
        new ZoneOffsetTransition(
            LocalDateTime.of(2000, Month.MARCH, 26, 1, 0), OFFSET_0200, OFFSET_0300);
    assertEquals(test.createTransition(2000), trans);
  }

  @Test
  public void test_createTransition_floatingWeek_overlap_endOfDay() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            LocalTime.MIDNIGHT,
            true,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0300,
            OFFSET_0200);
    ZoneOffsetTransition trans =
        new ZoneOffsetTransition(
            LocalDateTime.of(2000, Month.MARCH, 27, 0, 0), OFFSET_0300, OFFSET_0200);
    assertEquals(test.createTransition(2000), trans);
  }

  @Test
  public void test_createTransition_floatingWeekBackwards_last() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -1,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransition trans =
        new ZoneOffsetTransition(
            LocalDateTime.of(2000, Month.MARCH, 26, 1, 0), OFFSET_0200, OFFSET_0300);
    assertEquals(test.createTransition(2000), trans);
  }

  @Test
  public void test_createTransition_floatingWeekBackwards_seventhLast() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -7,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransition trans =
        new ZoneOffsetTransition(
            LocalDateTime.of(2000, Month.MARCH, 19, 1, 0), OFFSET_0200, OFFSET_0300);
    assertEquals(test.createTransition(2000), trans);
  }

  @Test
  public void test_createTransition_floatingWeekBackwards_secondLast() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -2,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransition trans =
        new ZoneOffsetTransition(
            LocalDateTime.of(2000, Month.MARCH, 26, 1, 0), OFFSET_0200, OFFSET_0300);
    assertEquals(test.createTransition(2000), trans);
  }

  @Test
  public void test_createTransition_fixedDate() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            null,
            TIME_0100,
            false,
            TimeDefinition.STANDARD,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransition trans =
        new ZoneOffsetTransition(
            LocalDateTime.of(2000, Month.MARCH, 20, 1, 0), OFFSET_0200, OFFSET_0300);
    assertEquals(test.createTransition(2000), trans);
  }

  // -----------------------------------------------------------------------
  // equals()
  // -----------------------------------------------------------------------
  @Test
  public void test_equals_monthDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.APRIL,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_dayOfMonthDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            21,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_dayOfWeekDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SATURDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_dayOfWeekDifferentNull() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            null,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_localTimeDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            LocalTime.MIDNIGHT,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_endOfDayDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            LocalTime.MIDNIGHT,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            LocalTime.MIDNIGHT,
            true,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_timeDefinitionDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.STANDARD,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_standardOffsetDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0300,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_offsetBeforeDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0300,
            OFFSET_0300);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_offsetAfterDifferent() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0200);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), false);
    assertEquals(b.equals(a), false);
    assertEquals(b.equals(b), true);
  }

  @Test
  public void test_equals_string_false() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals("TZDB"), false);
  }

  @Test
  public void test_equals_null_false() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.equals(null), false);
  }

  // -----------------------------------------------------------------------
  // hashCode()
  // -----------------------------------------------------------------------
  @Test
  public void test_hashCode_floatingWeek_gap_notEndOfDay() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void test_hashCode_floatingWeek_overlap_endOfDay_nullDayOfWeek() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.OCTOBER,
            20,
            null,
            LocalTime.MIDNIGHT,
            true,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0300,
            OFFSET_0200);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.OCTOBER,
            20,
            null,
            LocalTime.MIDNIGHT,
            true,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0300,
            OFFSET_0200);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void test_hashCode_floatingWeekBackwards() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -1,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -1,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void test_hashCode_fixedDate() {
    ZoneOffsetTransitionRule a =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            null,
            TIME_0100,
            false,
            TimeDefinition.STANDARD,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    ZoneOffsetTransitionRule b =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            null,
            TIME_0100,
            false,
            TimeDefinition.STANDARD,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(a.hashCode(), b.hashCode());
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------
  @Test
  public void test_toString_floatingWeek_gap_notEndOfDay() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(
        test.toString(),
        "TransitionRule[Gap +02:00 to +03:00, SUNDAY on or after MARCH 20 at 01:00 WALL, standard offset +02:00]");
  }

  @Test
  public void test_toString_floatingWeek_overlap_endOfDay() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.OCTOBER,
            20,
            DayOfWeek.SUNDAY,
            LocalTime.MIDNIGHT,
            true,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0300,
            OFFSET_0200);
    assertEquals(
        test.toString(),
        "TransitionRule[Overlap +03:00 to +02:00, SUNDAY on or after OCTOBER 20 at 24:00 WALL, standard offset +02:00]");
  }

  @Test
  public void test_toString_floatingWeekBackwards_last() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -1,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(
        test.toString(),
        "TransitionRule[Gap +02:00 to +03:00, SUNDAY on or before last day of MARCH at 01:00 WALL, standard offset +02:00]");
  }

  @Test
  public void test_toString_floatingWeekBackwards_secondLast() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            -2,
            DayOfWeek.SUNDAY,
            TIME_0100,
            false,
            TimeDefinition.WALL,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(
        test.toString(),
        "TransitionRule[Gap +02:00 to +03:00, SUNDAY on or before last day minus 1 of MARCH at 01:00 WALL, standard offset +02:00]");
  }

  @Test
  public void test_toString_fixedDate() {
    ZoneOffsetTransitionRule test =
        ZoneOffsetTransitionRule.of(
            Month.MARCH,
            20,
            null,
            TIME_0100,
            false,
            TimeDefinition.STANDARD,
            OFFSET_0200,
            OFFSET_0200,
            OFFSET_0300);
    assertEquals(
        test.toString(),
        "TransitionRule[Gap +02:00 to +03:00, MARCH 20 at 01:00 STANDARD, standard offset +02:00]");
  }
}
