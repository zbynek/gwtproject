/*
 * Copyright (c) 2007-present Stephen Colebourne & Michael Nascimento Santos
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

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.Test;

/** Test offset clock. */
// @Test
public class TestClock_Offset extends AbstractTest {

  private static ZoneId MOSCOW;
  private static ZoneId PARIS;
  private static Instant INSTANT;
  private static Duration OFFSET;

  @Override
  public void gwtSetUp() throws Exception {
    super.gwtSetUp();
    MOSCOW = ZoneId.of("Europe/Moscow");
    PARIS = ZoneId.of("Europe/Paris");
    INSTANT =
        LocalDateTime.of(2008, 6, 30, 11, 30, 10, 500).atZone(ZoneOffset.ofHours(2)).toInstant();
    OFFSET = Duration.ofSeconds(2);
  }

  // -----------------------------------------------------------------------
  // GWT
  //	public void test_isSerializable() throws IOException, ClassNotFoundException {
  //        assertSerializable(Clock.offset(Clock.system(PARIS), OFFSET));
  //    }

  // -----------------------------------------------------------------------
  public void test_offset_ClockDuration() {
    Clock test = Clock.offset(Clock.fixed(INSTANT, PARIS), OFFSET);
    assertEquals(test.instant(), INSTANT.plus(OFFSET));
    assertEquals(test.getZone(), PARIS);
  }

  public void test_offset_ClockDuration_zeroDuration() {
    Clock underlying = Clock.system(PARIS);
    Clock test = Clock.offset(underlying, Duration.ZERO);
    assertSame(test, underlying); // spec says same
  }

  @Test(expected = NullPointerException.class)
  public void test_offset_ClockDuration_nullClock() {
    try {
      Clock.offset(null, Duration.ZERO);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_offset_ClockDuration_nullDuration() {
    try {
      Clock.offset(Clock.systemUTC(), null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -------------------------------------------------------------------------
  public void test_withZone() {
    Clock test = Clock.offset(Clock.system(PARIS), OFFSET);
    Clock changed = test.withZone(MOSCOW);
    assertEquals(test.getZone(), PARIS);
    assertEquals(changed.getZone(), MOSCOW);
  }

  public void test_withZone_same() {
    Clock test = Clock.offset(Clock.system(PARIS), OFFSET);
    Clock changed = test.withZone(PARIS);
    assertSame(test, changed);
  }

  @Test(expected = NullPointerException.class)
  public void test_withZone_null() {
    try {
      Clock.offset(Clock.system(PARIS), OFFSET).withZone(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  public void test_equals() {
    Clock a = Clock.offset(Clock.system(PARIS), OFFSET);
    Clock b = Clock.offset(Clock.system(PARIS), OFFSET);
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), true);
    assertEquals(b.equals(a), true);
    assertEquals(b.equals(b), true);

    Clock c = Clock.offset(Clock.system(MOSCOW), OFFSET);
    assertEquals(a.equals(c), false);

    Clock d = Clock.offset(Clock.system(PARIS), OFFSET.minusNanos(1));
    assertEquals(a.equals(d), false);

    assertEquals(a.equals(null), false);
    assertEquals(a.equals("other type"), false);
    assertEquals(a.equals(Clock.systemUTC()), false);
  }

  public void test_hashCode() {
    Clock a = Clock.offset(Clock.system(PARIS), OFFSET);
    Clock b = Clock.offset(Clock.system(PARIS), OFFSET);
    assertEquals(a.hashCode(), a.hashCode());
    assertEquals(a.hashCode(), b.hashCode());

    Clock c = Clock.offset(Clock.system(MOSCOW), OFFSET);
    assertEquals(a.hashCode() == c.hashCode(), false);

    Clock d = Clock.offset(Clock.system(PARIS), OFFSET.minusNanos(1));
    assertEquals(a.hashCode() == d.hashCode(), false);
  }

  // -----------------------------------------------------------------------
  public void test_toString() {
    Clock test = Clock.offset(Clock.systemUTC(), OFFSET);
    assertEquals(test.toString(), "OffsetClock[SystemClock[Z],PT2S]");
  }
}
