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
package org.jresearch.threetenbp.gwt.time.client.zone;

import static java.time.temporal.ChronoUnit.HOURS;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.zone.ZoneOffsetTransition;
import org.jresearch.threetenbp.gwt.time.client.AbstractTest;
import org.junit.Test;

/** Test ZoneOffsetTransition. */
// @Test
public class TestZoneOffsetTransition extends AbstractTest {

  private static final ZoneOffset OFFSET_0100 = ZoneOffset.ofHours(1);
  private static final ZoneOffset OFFSET_0200 = ZoneOffset.ofHours(2);
  private static final ZoneOffset OFFSET_0230 = ZoneOffset.ofHoursMinutes(2, 30);
  private static final ZoneOffset OFFSET_0300 = ZoneOffset.ofHours(3);
  private static final ZoneOffset OFFSET_0400 = ZoneOffset.ofHours(4);

  // -----------------------------------------------------------------------
  // factory
  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_factory_nullTransition() {
    try {
      ZoneOffsetTransition.of(null, OFFSET_0100, OFFSET_0200);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullOffsetBefore() {
    try {
      ZoneOffsetTransition.of(LocalDateTime.of(2010, 12, 3, 11, 30), null, OFFSET_0200);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_nullOffsetAfter() {
    try {
      ZoneOffsetTransition.of(LocalDateTime.of(2010, 12, 3, 11, 30), OFFSET_0200, null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_factory_sameOffset() {
    try {
      ZoneOffsetTransition.of(LocalDateTime.of(2010, 12, 3, 11, 30), OFFSET_0200, OFFSET_0200);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_factory_noNanos() {
    try {
      ZoneOffsetTransition.of(
          LocalDateTime.of(2010, 12, 3, 11, 30, 0, 500), OFFSET_0200, OFFSET_0300);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // getters
  // -----------------------------------------------------------------------
  @Test
  public void test_getters_gap() throws Exception {
    LocalDateTime before = LocalDateTime.of(2010, 3, 31, 1, 0);
    LocalDateTime after = LocalDateTime.of(2010, 3, 31, 2, 0);
    ZoneOffsetTransition test = ZoneOffsetTransition.of(before, OFFSET_0200, OFFSET_0300);
    assertEquals(test.isGap(), true);
    assertEquals(test.isOverlap(), false);
    assertEquals(test.getDateTimeBefore(), before);
    assertEquals(test.getDateTimeAfter(), after);
    assertEquals(test.getInstant(), before.toInstant(OFFSET_0200));
    assertEquals(test.getOffsetBefore(), OFFSET_0200);
    assertEquals(test.getOffsetAfter(), OFFSET_0300);
    assertEquals(test.getDuration(), Duration.of(1, HOURS));
    // GWT specific
    //        assertSerializable(test);
  }

  @Test
  public void test_getters_overlap() throws Exception {
    LocalDateTime before = LocalDateTime.of(2010, 10, 31, 1, 0);
    LocalDateTime after = LocalDateTime.of(2010, 10, 31, 0, 0);
    ZoneOffsetTransition test = ZoneOffsetTransition.of(before, OFFSET_0300, OFFSET_0200);
    assertEquals(test.isGap(), false);
    assertEquals(test.isOverlap(), true);
    assertEquals(test.getDateTimeBefore(), before);
    assertEquals(test.getDateTimeAfter(), after);
    assertEquals(test.getInstant(), before.toInstant(OFFSET_0300));
    assertEquals(test.getOffsetBefore(), OFFSET_0300);
    assertEquals(test.getOffsetAfter(), OFFSET_0200);
    assertEquals(test.getDuration(), Duration.of(-1, HOURS));
    // GWT specific
    //        assertSerializable(test);
  }

  // -----------------------------------------------------------------------
  //    @Test
  //    public void test_serialization_unusual1() throws Exception {
  //        LocalDateTime ldt = LocalDateTime.of(Year.MAX_VALUE, 12, 31, 1, 31, 53);
  //        ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, ZoneOffset.of("+02:04:56"),
  // ZoneOffset.of("-10:02:34"));
  //        assertSerializable(test);
  //    }

  //    @Test
  //    public void test_serialization_unusual2() throws Exception {
  //        LocalDateTime ldt = LocalDateTime.of(Year.MIN_VALUE, 1, 1, 12, 1, 3);
  //        ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, ZoneOffset.of("+02:04:56"),
  // ZoneOffset.of("+10:02:34"));
  //        assertSerializable(test);
  //    }

  //    @Test
  //    public void test_serialization_format() throws ClassNotFoundException, IOException {
  //        LocalDateTime ldt = LocalDateTime.of(Year.MIN_VALUE, 1, 1, 12, 1, 3);
  //        ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, ZoneOffset.of("+02:04:56"),
  // ZoneOffset.of("+10:02:34"));
  //        assertEqualsSerialisedForm(test);
  //    }

  // -----------------------------------------------------------------------
  // isValidOffset()
  // -----------------------------------------------------------------------
  @Test
  public void test_isValidOffset_gap() {
    LocalDateTime ldt = LocalDateTime.of(2010, 3, 31, 1, 0);
    ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, OFFSET_0200, OFFSET_0300);
    assertEquals(test.isValidOffset(OFFSET_0100), false);
    assertEquals(test.isValidOffset(OFFSET_0200), false);
    assertEquals(test.isValidOffset(OFFSET_0230), false);
    assertEquals(test.isValidOffset(OFFSET_0300), false);
    assertEquals(test.isValidOffset(OFFSET_0400), false);
  }

  @Test
  public void test_isValidOffset_overlap() {
    LocalDateTime ldt = LocalDateTime.of(2010, 10, 31, 1, 0);
    ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, OFFSET_0300, OFFSET_0200);
    assertEquals(test.isValidOffset(OFFSET_0100), false);
    assertEquals(test.isValidOffset(OFFSET_0200), true);
    assertEquals(test.isValidOffset(OFFSET_0230), false);
    assertEquals(test.isValidOffset(OFFSET_0300), true);
    assertEquals(test.isValidOffset(OFFSET_0400), false);
  }

  // -----------------------------------------------------------------------
  // compareTo()
  // -----------------------------------------------------------------------
  @Test
  public void test_compareTo() {
    ZoneOffsetTransition a =
        ZoneOffsetTransition.of(
            LocalDateTime.ofEpochSecond(23875287L - 1, 0, OFFSET_0200), OFFSET_0200, OFFSET_0300);
    ZoneOffsetTransition b =
        ZoneOffsetTransition.of(
            LocalDateTime.ofEpochSecond(23875287L, 0, OFFSET_0300), OFFSET_0300, OFFSET_0200);
    ZoneOffsetTransition c =
        ZoneOffsetTransition.of(
            LocalDateTime.ofEpochSecond(23875287L + 1, 0, OFFSET_0100), OFFSET_0100, OFFSET_0400);

    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(a.compareTo(b) < 0, true);
    assertEquals(a.compareTo(c) < 0, true);

    assertEquals(b.compareTo(a) > 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(b.compareTo(c) < 0, true);

    assertEquals(c.compareTo(a) > 0, true);
    assertEquals(c.compareTo(b) > 0, true);
    assertEquals(c.compareTo(c) == 0, true);
  }

  @Test
  public void test_compareTo_sameInstant() {
    ZoneOffsetTransition a =
        ZoneOffsetTransition.of(
            LocalDateTime.ofEpochSecond(23875287L, 0, OFFSET_0200), OFFSET_0200, OFFSET_0300);
    ZoneOffsetTransition b =
        ZoneOffsetTransition.of(
            LocalDateTime.ofEpochSecond(23875287L, 0, OFFSET_0300), OFFSET_0300, OFFSET_0200);
    ZoneOffsetTransition c =
        ZoneOffsetTransition.of(
            LocalDateTime.ofEpochSecond(23875287L, 0, OFFSET_0100), OFFSET_0100, OFFSET_0400);

    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(a.compareTo(b) == 0, true);
    assertEquals(a.compareTo(c) == 0, true);

    assertEquals(b.compareTo(a) == 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(b.compareTo(c) == 0, true);

    assertEquals(c.compareTo(a) == 0, true);
    assertEquals(c.compareTo(b) == 0, true);
    assertEquals(c.compareTo(c) == 0, true);
  }

  // -----------------------------------------------------------------------
  // equals()
  // -----------------------------------------------------------------------
  @Test
  public void test_equals() {
    LocalDateTime ldtA = LocalDateTime.of(2010, 3, 31, 1, 0);
    ZoneOffsetTransition a1 = ZoneOffsetTransition.of(ldtA, OFFSET_0200, OFFSET_0300);
    ZoneOffsetTransition a2 = ZoneOffsetTransition.of(ldtA, OFFSET_0200, OFFSET_0300);
    LocalDateTime ldtB = LocalDateTime.of(2010, 10, 31, 1, 0);
    ZoneOffsetTransition b = ZoneOffsetTransition.of(ldtB, OFFSET_0300, OFFSET_0200);

    assertEquals(a1.equals(a1), true);
    assertEquals(a1.equals(a2), true);
    assertEquals(a1.equals(b), false);
    assertEquals(a2.equals(a1), true);
    assertEquals(a2.equals(a2), true);
    assertEquals(a2.equals(b), false);
    assertEquals(b.equals(a1), false);
    assertEquals(b.equals(a2), false);
    assertEquals(b.equals(b), true);

    assertEquals(a1.equals(""), false);
    assertEquals(a1.equals(null), false);
  }

  // -----------------------------------------------------------------------
  // hashCode()
  // -----------------------------------------------------------------------
  @Test
  public void test_hashCode_floatingWeek_gap_notEndOfDay() {
    LocalDateTime ldtA = LocalDateTime.of(2010, 3, 31, 1, 0);
    ZoneOffsetTransition a1 = ZoneOffsetTransition.of(ldtA, OFFSET_0200, OFFSET_0300);
    ZoneOffsetTransition a2 = ZoneOffsetTransition.of(ldtA, OFFSET_0200, OFFSET_0300);
    LocalDateTime ldtB = LocalDateTime.of(2010, 10, 31, 1, 0);
    ZoneOffsetTransition b = ZoneOffsetTransition.of(ldtB, OFFSET_0300, OFFSET_0200);

    assertEquals(a1.hashCode(), a1.hashCode());
    assertEquals(a1.hashCode(), a2.hashCode());
    assertEquals(b.hashCode(), b.hashCode());
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------
  @Test
  public void test_toString_gap() {
    LocalDateTime ldt = LocalDateTime.of(2010, 3, 31, 1, 0);
    ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, OFFSET_0200, OFFSET_0300);
    assertEquals(test.toString(), "Transition[Gap at 2010-03-31T01:00+02:00 to +03:00]");
  }

  @Test
  public void test_toString_overlap() {
    LocalDateTime ldt = LocalDateTime.of(2010, 10, 31, 1, 0);
    ZoneOffsetTransition test = ZoneOffsetTransition.of(ldt, OFFSET_0300, OFFSET_0200);
    assertEquals(test.toString(), "Transition[Overlap at 2010-10-31T01:00+03:00 to +02:00]");
  }
}
