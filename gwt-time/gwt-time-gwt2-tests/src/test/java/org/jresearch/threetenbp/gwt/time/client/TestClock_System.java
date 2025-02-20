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
package org.jresearch.threetenbp.gwt.time.client;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Test;

/** Test system clock. */
// @Test
public class TestClock_System extends AbstractTest {

  private static ZoneId MOSCOW;
  private static ZoneId PARIS;

  @Override
  public void gwtSetUp() throws Exception {
    super.gwtSetUp();
    MOSCOW = ZoneId.of("Europe/Moscow");
    PARIS = ZoneId.of("Europe/Paris");
  }

  // -----------------------------------------------------------------------
  // GWT
  //    public void test_isSerializable() throws IOException, ClassNotFoundException {
  //        assertSerializable(Clock.systemUTC());
  //        assertSerializable(Clock.systemDefaultZone());
  //        assertSerializable(Clock.system(PARIS));
  //    }

  // -----------------------------------------------------------------------
  public void test_instant() {
    Clock system = Clock.systemUTC();
    assertEquals(system.getZone(), ZoneOffset.UTC);
    for (int i = 0; i < 10000; i++) {
      // assume can eventually get these within 10 milliseconds
      Instant instant = system.instant();
      long systemMillis = System.currentTimeMillis();
      if (systemMillis - instant.toEpochMilli() < 10) {
        return; // success
      }
    }
    fail();
  }

  public void test_millis() {
    Clock system = Clock.systemUTC();
    assertEquals(system.getZone(), ZoneOffset.UTC);
    for (int i = 0; i < 10000; i++) {
      // assume can eventually get these within 10 milliseconds
      long instant = system.millis();
      long systemMillis = System.currentTimeMillis();
      if (systemMillis - instant < 10) {
        return; // success
      }
    }
    fail();
  }

  // -------------------------------------------------------------------------
  public void test_systemUTC() {
    Clock test = Clock.systemUTC();
    assertEquals(test.getZone(), ZoneOffset.UTC);
    assertEquals(test, Clock.system(ZoneOffset.UTC));
  }

  public void test_systemDefaultZone() {
    Clock test = Clock.systemDefaultZone();
    assertEquals(test.getZone(), ZoneId.systemDefault());
    assertEquals(test, Clock.system(ZoneId.systemDefault()));
  }

  public void test_system_ZoneId() {
    Clock test = Clock.system(PARIS);
    assertEquals(test.getZone(), PARIS);
  }

  @Test(expected = NullPointerException.class)
  public void test_zoneId_nullZoneId() {
    try {
      Clock.system(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -------------------------------------------------------------------------
  public void test_withZone() {
    Clock test = Clock.system(PARIS);
    Clock changed = test.withZone(MOSCOW);
    assertEquals(test.getZone(), PARIS);
    assertEquals(changed.getZone(), MOSCOW);
  }

  public void test_withZone_same() {
    Clock test = Clock.system(PARIS);
    Clock changed = test.withZone(PARIS);
    assertSame(test, changed);
  }

  public void test_withZone_fromUTC() {
    Clock test = Clock.systemUTC();
    Clock changed = test.withZone(PARIS);
    assertEquals(changed.getZone(), PARIS);
  }

  @Test(expected = NullPointerException.class)
  public void test_withZone_null() {
    try {
      Clock.systemUTC().withZone(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  public void test_equals() {
    Clock a = Clock.systemUTC();
    Clock b = Clock.systemUTC();
    assertEquals(a.equals(a), true);
    assertEquals(a.equals(b), true);
    assertEquals(b.equals(a), true);
    assertEquals(b.equals(b), true);

    Clock c = Clock.system(PARIS);
    Clock d = Clock.system(PARIS);
    assertEquals(c.equals(c), true);
    assertEquals(c.equals(d), true);
    assertEquals(d.equals(c), true);
    assertEquals(d.equals(d), true);

    assertEquals(a.equals(c), false);
    assertEquals(c.equals(a), false);

    assertEquals(a.equals(null), false);
    assertEquals(a.equals("other type"), false);
    assertEquals(a.equals(Clock.fixed(Instant.now(), ZoneOffset.UTC)), false);
  }

  public void test_hashCode() {
    Clock a = Clock.system(ZoneOffset.UTC);
    Clock b = Clock.system(ZoneOffset.UTC);
    assertEquals(a.hashCode(), a.hashCode());
    assertEquals(a.hashCode(), b.hashCode());

    Clock c = Clock.system(PARIS);
    assertEquals(a.hashCode() == c.hashCode(), false);
  }

  // -----------------------------------------------------------------------
  public void test_toString() {
    Clock test = Clock.system(PARIS);
    assertEquals(test.toString(), "SystemClock[Europe/Paris]");
  }
}
