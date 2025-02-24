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
package org.jresearch.threetenbp.gwt.time.client.chrono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jresearch.threetenbp.gwt.time.client.AbstractTest;
import org.junit.Test;

/** Test assertions that must be true for all built-in chronologies. */
@SuppressWarnings("rawtypes")
// @Test
public class TestChronoLocalDateTime extends AbstractTest {
  // -----------------------------------------------------------------------
  // regular data factory for names and descriptions of available calendars
  // -----------------------------------------------------------------------
  // @DataProvider(name = "calendars")
  Chronology[][] data_of_calendars() {
    return new Chronology[][] {
      {HijrahChronology.INSTANCE},
      {IsoChronology.INSTANCE},
      {JapaneseChronology.INSTANCE},
      {MinguoChronology.INSTANCE},
      {ThaiBuddhistChronology.INSTANCE}
    };
  }

  @Test(/* dataProvider="calendars" */ )
  public void test_badWithAdjusterChrono() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_badWithAdjusterChrono((Chronology) objects[0]);
    }
  }

  public void test_badWithAdjusterChrono(Chronology chrono) {
    LocalDate refDate = LocalDate.of(1900, 1, 1);
    ChronoLocalDateTime cdt = chrono.date(refDate).atTime(LocalTime.NOON);
    for (Chronology[] clist : data_of_calendars()) {
      Chronology chrono2 = clist[0];
      ChronoLocalDateTime<?> cdt2 = chrono2.date(refDate).atTime(LocalTime.NOON);
      TemporalAdjuster adjuster = new FixedAdjuster(cdt2);
      if (chrono != chrono2) {
        try {
          cdt.with(adjuster);
          fail(
              "WithAdjuster should have thrown a ClassCastException, "
                  + "required: "
                  + cdt
                  + ", supplied: "
                  + cdt2);
        } catch (ClassCastException cce) {
          // Expected exception; not an error
        }
      } else {
        // Same chronology,
        ChronoLocalDateTime<?> result = cdt.with(adjuster);
        assertEquals("WithAdjuster failed to replace date", result, cdt2);
      }
    }
  }

  @Test(/* dataProvider="calendars" */ )
  public void test_badPlusAdjusterChrono() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_badPlusAdjusterChrono((Chronology) objects[0]);
    }
  }

  public void test_badPlusAdjusterChrono(Chronology chrono) {
    LocalDate refDate = LocalDate.of(1900, 1, 1);
    ChronoLocalDateTime cdt = chrono.date(refDate).atTime(LocalTime.NOON);
    for (Chronology[] clist : data_of_calendars()) {
      Chronology chrono2 = clist[0];
      ChronoLocalDateTime<?> cdt2 = chrono2.date(refDate).atTime(LocalTime.NOON);
      TemporalAmount adjuster = new FixedAdjuster(cdt2);
      if (chrono != chrono2) {
        try {
          cdt.plus(adjuster);
          fail(
              "WithAdjuster should have thrown a ClassCastException, "
                  + "required: "
                  + cdt
                  + ", supplied: "
                  + cdt2);
        } catch (ClassCastException cce) {
          // Expected exception; not an error
        }
      } else {
        // Same chronology,
        ChronoLocalDateTime<?> result = cdt.plus(adjuster);
        assertEquals("WithAdjuster failed to replace date time", result, cdt2);
      }
    }
  }

  @Test(/* dataProvider="calendars" */ )
  public void test_badMinusAdjusterChrono() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_badMinusAdjusterChrono((Chronology) objects[0]);
    }
  }

  public void test_badMinusAdjusterChrono(Chronology chrono) {
    LocalDate refDate = LocalDate.of(1900, 1, 1);
    ChronoLocalDateTime cdt = chrono.date(refDate).atTime(LocalTime.NOON);
    for (Chronology[] clist : data_of_calendars()) {
      Chronology chrono2 = clist[0];
      ChronoLocalDateTime<?> cdt2 = chrono2.date(refDate).atTime(LocalTime.NOON);
      TemporalAmount adjuster = new FixedAdjuster(cdt2);
      if (chrono != chrono2) {
        try {
          cdt.minus(adjuster);
          fail(
              "WithAdjuster should have thrown a ClassCastException, "
                  + "required: "
                  + cdt
                  + ", supplied: "
                  + cdt2);
        } catch (ClassCastException cce) {
          // Expected exception; not an error
        }
      } else {
        // Same chronology,
        ChronoLocalDateTime<?> result = cdt.minus(adjuster);
        assertEquals("WithAdjuster failed to replace date", result, cdt2);
      }
    }
  }

  @Test(/* dataProvider="calendars" */ )
  public void test_badPlusPeriodUnitChrono() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_badPlusPeriodUnitChrono((Chronology) objects[0]);
    }
  }

  public void test_badPlusPeriodUnitChrono(Chronology chrono) {
    LocalDate refDate = LocalDate.of(1900, 1, 1);
    ChronoLocalDateTime cdt = chrono.date(refDate).atTime(LocalTime.NOON);
    for (Chronology[] clist : data_of_calendars()) {
      Chronology chrono2 = clist[0];
      ChronoLocalDateTime<?> cdt2 = chrono2.date(refDate).atTime(LocalTime.NOON);
      TemporalUnit adjuster = new FixedPeriodUnit(cdt2);
      if (chrono != chrono2) {
        try {
          cdt.plus(1, adjuster);
          fail(
              "PeriodUnit.doAdd plus should have thrown a ClassCastException"
                  + cdt
                  + ", can not be cast to "
                  + cdt2);
        } catch (ClassCastException cce) {
          // Expected exception; not an error
        }
      } else {
        // Same chronology,
        ChronoLocalDateTime<?> result = cdt.plus(1, adjuster);
        assertEquals("WithAdjuster failed to replace date", result, cdt2);
      }
    }
  }

  @Test(/* dataProvider="calendars" */ )
  public void test_badMinusPeriodUnitChrono() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_badMinusPeriodUnitChrono((Chronology) objects[0]);
    }
  }

  public void test_badMinusPeriodUnitChrono(Chronology chrono) {
    LocalDate refDate = LocalDate.of(1900, 1, 1);
    ChronoLocalDateTime cdt = chrono.date(refDate).atTime(LocalTime.NOON);
    for (Chronology[] clist : data_of_calendars()) {
      Chronology chrono2 = clist[0];
      ChronoLocalDateTime<?> cdt2 = chrono2.date(refDate).atTime(LocalTime.NOON);
      TemporalUnit adjuster = new FixedPeriodUnit(cdt2);
      if (chrono != chrono2) {
        try {
          cdt.minus(1, adjuster);
          fail(
              "PeriodUnit.doAdd minus should have thrown a ClassCastException"
                  + cdt.getClass()
                  + ", can not be cast to "
                  + cdt2.getClass());
        } catch (ClassCastException cce) {
          // Expected exception; not an error
        }
      } else {
        // Same chronology,
        ChronoLocalDateTime<?> result = cdt.minus(1, adjuster);
        assertEquals("WithAdjuster failed to replace date", result, cdt2);
      }
    }
  }

  @Test(/* dataProvider="calendars" */ )
  public void test_badDateTimeFieldChrono() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_badDateTimeFieldChrono((Chronology) objects[0]);
    }
  }

  public void test_badDateTimeFieldChrono(Chronology chrono) {
    LocalDate refDate = LocalDate.of(1900, 1, 1);
    ChronoLocalDateTime cdt = chrono.date(refDate).atTime(LocalTime.NOON);
    for (Chronology[] clist : data_of_calendars()) {
      Chronology chrono2 = clist[0];
      ChronoLocalDateTime<?> cdt2 = chrono2.date(refDate).atTime(LocalTime.NOON);
      TemporalField adjuster = new FixedDateTimeField(cdt2);
      if (chrono != chrono2) {
        try {
          cdt.with(adjuster, 1);
          fail(
              "DateTimeField doSet should have thrown a ClassCastException"
                  + cdt.getClass()
                  + ", can not be cast to "
                  + cdt2.getClass());
        } catch (ClassCastException cce) {
          // Expected exception; not an error
        }
      } else {
        // Same chronology,
        ChronoLocalDateTime<?> result = cdt.with(adjuster, 1);
        assertEquals("DateTimeField doSet failed to replace date", result, cdt2);
      }
    }
  }

  // -----------------------------------------------------------------------
  // isBefore, isAfter, isEqual
  // -----------------------------------------------------------------------
  @Test(/* dataProvider="calendars" */ )
  public void test_datetime_comparisons() {
    Object[][] data = data_of_calendars();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_datetime_comparisons((Chronology) objects[0]);
    }
  }

  public void test_datetime_comparisons(Chronology chrono) {
    List<ChronoLocalDateTime<?>> dates = new ArrayList<ChronoLocalDateTime<?>>();

    ChronoLocalDateTime<?> date = chrono.date(LocalDate.of(1900, 1, 1)).atTime(LocalTime.MIN);

    // Insert dates in order, no duplicates
    if (chrono != JapaneseChronology.INSTANCE) {
      dates.add(date.minus(100, ChronoUnit.YEARS));
    }
    dates.add(date.minus(1, ChronoUnit.YEARS));
    dates.add(date.minus(1, ChronoUnit.MONTHS));
    dates.add(date.minus(1, ChronoUnit.WEEKS));
    dates.add(date.minus(1, ChronoUnit.DAYS));
    dates.add(date.minus(1, ChronoUnit.HOURS));
    dates.add(date.minus(1, ChronoUnit.MINUTES));
    dates.add(date.minus(1, ChronoUnit.SECONDS));
    dates.add(date.minus(1, ChronoUnit.NANOS));
    dates.add(date);
    dates.add(date.plus(1, ChronoUnit.NANOS));
    dates.add(date.plus(1, ChronoUnit.SECONDS));
    dates.add(date.plus(1, ChronoUnit.MINUTES));
    dates.add(date.plus(1, ChronoUnit.HOURS));
    dates.add(date.plus(1, ChronoUnit.DAYS));
    dates.add(date.plus(1, ChronoUnit.WEEKS));
    dates.add(date.plus(1, ChronoUnit.MONTHS));
    dates.add(date.plus(1, ChronoUnit.YEARS));
    dates.add(date.plus(100, ChronoUnit.YEARS));

    // Check these dates against the corresponding dates for every calendar
    for (Chronology[] clist : data_of_calendars()) {
      List<ChronoLocalDateTime<?>> otherDates = new ArrayList<ChronoLocalDateTime<?>>();
      Chronology chrono2 = clist[0];
      if (chrono2 == JapaneseChronology.INSTANCE) {
        continue;
      }
      for (ChronoLocalDateTime<?> d : dates) {
        otherDates.add(chrono2.date(d).atTime(d.toLocalTime()));
      }

      // Now compare  the sequence of original dates with the sequence of converted dates
      for (int i = 0; i < dates.size(); i++) {
        ChronoLocalDateTime<?> a = dates.get(i);
        for (int j = 0; j < otherDates.size(); j++) {
          ChronoLocalDateTime<?> b = otherDates.get(j);
          int cmp = ChronoLocalDateTime.timeLineOrder().compare(a, b);
          if (i < j) {
            assertTrue(a + " compare " + b, cmp < 0);
            assertEquals(a + " isBefore " + b, a.isBefore(b), true);
            assertEquals(a + " isAfter " + b, a.isAfter(b), false);
            assertEquals(a + " isEqual " + b, a.isEqual(b), false);
          } else if (i > j) {
            assertTrue(a + " compare " + b, cmp > 0);
            assertEquals(a + " isBefore " + b, a.isBefore(b), false);
            assertEquals(a + " isAfter " + b, a.isAfter(b), true);
            assertEquals(a + " isEqual " + b, a.isEqual(b), false);
          } else {
            assertTrue(a + " compare " + b, cmp == 0);
            assertEquals(a + " isBefore " + b, a.isBefore(b), false);
            assertEquals(a + " isAfter " + b, a.isAfter(b), false);
            assertEquals(a + " isEqual " + b, a.isEqual(b), true);
          }
        }
      }
    }
  }

  // -----------------------------------------------------------------------
  // Test Serialization of ISO via chrono API
  // -----------------------------------------------------------------------
  // GWT - no serialization
  //    @Test( dataProvider="calendars")
  //    public void test_ChronoLocalDateTimeSerialization(Chronology chrono) throws Exception {
  //        LocalDateTime ref = LocalDate.of(2000, 1, 5).atTime(12, 1, 2, 3);
  //        ChronoLocalDateTime<?> orginal = chrono.date(ref).atTime(ref.toLocalTime());
  //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
  //        ObjectOutputStream out = new ObjectOutputStream(baos);
  //        out.writeObject(orginal);
  //        out.close();
  //        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
  //        ObjectInputStream in = new ObjectInputStream(bais);
  //        ChronoLocalDateTime<?> ser = (ChronoLocalDateTime<?>) in.readObject();
  //        assertEquals(ser, orginal, "deserialized date is wrong");
  //    }

  /**
   * FixedAdjusted returns a fixed DateTime in all adjustments. Construct an adjuster with the
   * DateTime that should be returned.
   */
  static class FixedAdjuster implements TemporalAdjuster, TemporalAmount {
    private Temporal datetime;

    FixedAdjuster(Temporal datetime) {
      this.datetime = datetime;
    }

    @Override
    public Temporal adjustInto(Temporal ignore) {
      return datetime;
    }

    @Override
    public Temporal addTo(Temporal ignore) {
      return datetime;
    }

    @Override
    public Temporal subtractFrom(Temporal ignore) {
      return datetime;
    }

    @Override
    public List<TemporalUnit> getUnits() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long get(TemporalUnit unit) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  /**
   * FixedPeriodUnit returns a fixed DateTime in all adjustments. Construct an FixedPeriodUnit with
   * the DateTime that should be returned.
   */
  static class FixedPeriodUnit implements TemporalUnit {
    private Temporal dateTime;

    FixedPeriodUnit(Temporal dateTime) {
      this.dateTime = dateTime;
    }

    @Override
    public String toString() {
      return "FixedPeriodUnit";
    }

    @Override
    public Duration getDuration() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDurationEstimated() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDateBased() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isTimeBased() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSupportedBy(Temporal dateTime) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Temporal> R addTo(R dateTime, long periodToAdd) {
      return (R) this.dateTime;
    }

    @Override
    public long between(Temporal temporal1, Temporal temporal2) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  /**
   * FixedDateTimeField returns a fixed DateTime in all adjustments. Construct an FixedDateTimeField
   * with the DateTime that should be returned from doSet.
   */
  static class FixedDateTimeField implements TemporalField {
    private Temporal dateTime;

    FixedDateTimeField(Temporal dateTime) {
      this.dateTime = dateTime;
    }

    @Override
    public String toString() {
      return "FixedDateTimeField";
    }

    @Override
    public TemporalUnit getBaseUnit() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TemporalUnit getRangeUnit() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueRange range() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDateBased() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isTimeBased() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSupportedBy(TemporalAccessor dateTime) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueRange rangeRefinedBy(TemporalAccessor dateTime) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getFrom(TemporalAccessor dateTime) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Temporal> R adjustInto(R dateTime, long newValue) {
      return (R) this.dateTime;
    }

    @Override
    public String getDisplayName(Locale locale) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TemporalAccessor resolve(
        Map<TemporalField, Long> fieldValues,
        TemporalAccessor partialTemporal,
        ResolverStyle resolverStyle) {
      return null;
    }
  }
}
