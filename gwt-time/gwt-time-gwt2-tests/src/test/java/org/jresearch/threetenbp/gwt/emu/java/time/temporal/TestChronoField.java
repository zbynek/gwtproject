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

import org.jresearch.threetenbp.gwt.emu.java.time.AbstractTest;
import org.junit.Test;

/** Test. */
// @Test
public class TestChronoField extends AbstractTest {

  // -------------------------------------------------------------------------
  @Test
  public void test_isDateBased() {
    for (ChronoField field : ChronoField.values()) {
      if (field == ChronoField.INSTANT_SECONDS || field == ChronoField.OFFSET_SECONDS) {
        assertEquals(field.isTimeBased(), false);
      } else {
        assertEquals(field.isDateBased(), field.getBaseUnit().isDateBased());
      }
    }
  }

  @Test
  public void test_isTimeBased() {
    for (ChronoField field : ChronoField.values()) {
      if (field == ChronoField.INSTANT_SECONDS || field == ChronoField.OFFSET_SECONDS) {
        assertEquals(field.isTimeBased(), false);
      } else {
        assertEquals(field.isTimeBased(), field.getBaseUnit().isTimeBased());
      }
    }
  }
}
