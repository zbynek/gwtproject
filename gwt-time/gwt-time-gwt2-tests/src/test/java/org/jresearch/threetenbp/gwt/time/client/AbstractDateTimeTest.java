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
package org.jresearch.threetenbp.gwt.time.client;

import java.time.DateTimeException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.util.List;
import org.junit.Test;

/** Base test class for {@code DateTime}. */
public abstract class AbstractDateTimeTest extends AbstractTest {

  /**
   * Sample {@code DateTime} objects.
   *
   * @return the objects, not null
   */
  protected abstract List<TemporalAccessor> samples();

  /**
   * List of valid supported fields.
   *
   * @return the fields, not null
   */
  protected abstract List<TemporalField> validFields();

  /**
   * List of invalid unsupported fields.
   *
   * @return the fields, not null
   */
  protected abstract List<TemporalField> invalidFields();

  // -----------------------------------------------------------------------
  // isSupported(DateTimeField)
  // -----------------------------------------------------------------------
  @Test
  public void test_basicTest_isSupported_DateTimeField_supported() {
    for (TemporalAccessor sample : samples()) {
      for (TemporalField field : validFields()) {
        assertEquals("Failed on " + sample + " " + field, sample.isSupported(field), true);
      }
    }
  }

  @Test
  public void test_basicTest_isSupported_DateTimeField_unsupported() {
    for (TemporalAccessor sample : samples()) {
      for (TemporalField field : invalidFields()) {
        assertEquals("Failed on " + sample + " " + field, sample.isSupported(field), false);
      }
    }
  }

  @Test
  public void test_basicTest_isSupported_DateTimeField_null() {
    for (TemporalAccessor sample : samples()) {
      assertEquals("Failed on " + sample, sample.isSupported(null), false);
    }
  }

  // -----------------------------------------------------------------------
  // range(DateTimeField)
  // -----------------------------------------------------------------------
  // TODO needs implementations of week fields
  //    @Test
  //    public void basicTest_range_DateTimeField_supported() {
  //        for (DateTimeAccessor sample : samples()) {
  //            for (DateTimeField field : validFields()) {
  //                sample.range(field);  // no exception
  //            }
  //        }
  //    }

  @Test
  public void test_basicTest_range_DateTimeField_unsupported() {
    for (TemporalAccessor sample : samples()) {
      for (TemporalField field : invalidFields()) {
        try {
          sample.range(field);
          fail("Failed on " + sample + " " + field);
        } catch (DateTimeException ex) {
          // expected
        }
      }
    }
  }

  @Test
  public void test_basicTest_range_DateTimeField_null() {
    for (TemporalAccessor sample : samples()) {
      try {
        sample.range(null);
        fail("Failed on " + sample);
      } catch (NullPointerException ex) {
        // expected
      }
    }
  }

  // -----------------------------------------------------------------------
  // get(DateTimeField)
  // -----------------------------------------------------------------------
  // TODO needs implementations of week fields
  //    @Test
  //    public void basicTest_get_DateTimeField_supported() {
  //        for (DateTimeAccessor sample : samples()) {
  //            for (DateTimeField field : validFields()) {
  //                if (sample.range(field).isIntValue()) {
  //                    sample.get(field);  // no exception
  //                } else {
  //                    try {
  //                        sample.get(field);
  //                        fail("Failed on " + sample + " " + field);
  //                    } catch (DateTimeException ex) {
  //                        // expected
  //                    }
  //                }
  //            }
  //        }
  //    }

  @Test
  public void test_basicTest_get_DateTimeField_unsupported() {
    for (TemporalAccessor sample : samples()) {
      for (TemporalField field : invalidFields()) {
        try {
          sample.get(field);
          fail("Failed on " + sample + " " + field);
          fail("Missing exception");
        } catch (DateTimeException ex) {
          // expected
        }
      }
    }
  }

  @Test
  public void test_basicTest_get_DateTimeField_null() {
    for (TemporalAccessor sample : samples()) {
      try {
        sample.get(null);
        fail("Failed on " + sample);
      } catch (NullPointerException ex) {
        // expected
      }
    }
  }

  // -----------------------------------------------------------------------
  // getLong(DateTimeField)
  // -----------------------------------------------------------------------
  // TODO needs implementations of week fields
  //    @Test
  //    public void basicTest_getLong_DateTimeField_supported() {
  //        for (DateTimeAccessor sample : samples()) {
  //            for (DateTimeField field : validFields()) {
  //                sample.getLong(field);  // no exception
  //            }
  //        }
  //    }

  @Test
  public void test_basicTest_getLong_DateTimeField_unsupported() {
    for (TemporalAccessor sample : samples()) {
      for (TemporalField field : invalidFields()) {
        try {
          sample.getLong(field);
          fail("Failed on " + sample + " " + field);
          fail("Missing exception");
        } catch (DateTimeException ex) {
          // expected
        }
      }
    }
  }

  @Test
  public void test_basicTest_getLong_DateTimeField_null() {
    for (TemporalAccessor sample : samples()) {
      try {
        sample.getLong(null);
        fail("Failed on " + sample);
      } catch (NullPointerException ex) {
        // expected
      }
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_basicTest_query() {
    for (TemporalAccessor sample : samples()) {
      assertEquals(
          sample.query(
              new TemporalQuery<String>() {
                @Override
                public String queryFrom(TemporalAccessor dateTime) {
                  return "foo";
                }
              }),
          "foo");
    }
  }
}
