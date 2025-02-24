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
package org.jresearch.threetenbp.gwt.emu.java.time.format;

import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.DAY_OF_MONTH;

import java.io.IOException;
import java.util.Locale;
import org.jresearch.threetenbp.gwt.emu.java.text.ParsePosition;
import org.jresearch.threetenbp.gwt.emu.java.time.AbstractTest;
import org.jresearch.threetenbp.gwt.emu.java.time.DateTimeException;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalDate;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalTime;
import org.jresearch.threetenbp.gwt.emu.java.time.YearMonth;
import org.jresearch.threetenbp.gwt.emu.java.time.ZoneId;
import org.jresearch.threetenbp.gwt.emu.java.time.ZonedDateTime;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalAccessor;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalQuery;
import org.junit.Test;

/** Test DateTimeFormatter. */
// @Test
public class TestDateTimeFormatter extends AbstractTest {

  //    private static final DateTimeFormatter BASIC_FORMATTER =
  // DateTimeFormatter.ofPattern("'ONE'd");
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("'ONE'uuuu MM dd");

  private DateTimeFormatter fmt;

  //    @BeforeMethod
  public void gwtSetUp() throws Exception {
    super.gwtSetUp();
    fmt =
        new DateTimeFormatterBuilder()
            .appendLiteral("ONE")
            .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
            .toFormatter();
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_withLocale() throws Exception {
    DateTimeFormatter base = fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
    DateTimeFormatter test = base.withLocale(Locale.GERMAN);
    assertEquals(test.getLocale(), Locale.GERMAN);
  }

  @Test(expected = NullPointerException.class)
  public void test_withLocale_null() throws Exception {
    try {
      DateTimeFormatter base =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      base.withLocale((Locale) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // print
  // -----------------------------------------------------------------------
  @Test
  public void test_print_Calendrical() throws Exception {
    DateTimeFormatter test = fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
    String result = test.format(LocalDate.of(2008, 6, 30));
    assertEquals(result, "ONE30");
  }

  @Test(expected = DateTimeException.class)
  public void test_print_Calendrical_noSuchField() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.format(LocalTime.of(11, 30));
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_print_Calendrical_null() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.format((TemporalAccessor) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_print_CalendricalAppendable() throws Exception {
    DateTimeFormatter test = fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
    StringBuilder buf = new StringBuilder();
    test.formatTo(LocalDate.of(2008, 6, 30), buf);
    assertEquals(buf.toString(), "ONE30");
  }

  @Test(expected = DateTimeException.class)
  public void test_print_CalendricalAppendable_noSuchField() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      StringBuilder buf = new StringBuilder();
      test.formatTo(LocalTime.of(11, 30), buf);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_print_CalendricalAppendable_nullCalendrical() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      StringBuilder buf = new StringBuilder();
      test.formatTo((TemporalAccessor) null, buf);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_print_CalendricalAppendable_nullAppendable() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.formatTo(LocalDate.of(2008, 6, 30), (Appendable) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IOException.class) // IOException
  public void test_print_CalendricalAppendable_ioError() throws Throwable {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      try {
        test.formatTo(LocalDate.of(2008, 6, 30), new MockIOExceptionAppendable());
      } catch (DateTimeException ex) {
        assertEquals(ex.getCause() instanceof IOException, true);
        throw ex.getCause();
      }
      fail("Missing exception");
    } catch (IOException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // parse(Class)
  // -----------------------------------------------------------------------
  @Test
  public void test_parse_Class_String() throws Exception {
    LocalDate result = DATE_FORMATTER.parse("ONE2012 07 27", LocalDate::from);
    assertEquals(result, LocalDate.of(2012, 7, 27));
  }

  @Test
  public void test_parse_Class_CharSequence() throws Exception {
    LocalDate result = DATE_FORMATTER.parse(new StringBuilder("ONE2012 07 27"), LocalDate::from);
    assertEquals(result, LocalDate.of(2012, 7, 27));
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parse_Class_String_parseError() throws Exception {
    try {
      try {
        DATE_FORMATTER.parse("ONE2012 07 XX", LocalDate::from);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getMessage().contains("could not be parsed"), true);
        assertEquals(ex.getMessage().contains("ONE2012 07 XX"), true);
        assertEquals(ex.getParsedString(), "ONE2012 07 XX");
        assertEquals(ex.getErrorIndex(), 11);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parse_Class_String_parseErrorLongText() throws Exception {
    try {
      try {
        DATE_FORMATTER.parse(
            "ONEXXX67890123456789012345678901234567890123456789012345678901234567890123456789",
            LocalDate::from);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getMessage().contains("could not be parsed"), true);
        assertEquals(
            ex.getMessage()
                .contains("ONEXXX6789012345678901234567890123456789012345678901234567890123..."),
            true);
        assertEquals(
            ex.getParsedString(),
            "ONEXXX67890123456789012345678901234567890123456789012345678901234567890123456789");
        assertEquals(ex.getErrorIndex(), 3);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parse_Class_String_parseIncomplete() throws Exception {
    try {
      try {
        DATE_FORMATTER.parse("ONE2012 07 27SomethingElse", LocalDate::from);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getMessage().contains("could not be parsed"), true);
        assertEquals(ex.getMessage().contains("ONE2012 07 27SomethingElse"), true);
        assertEquals(ex.getParsedString(), "ONE2012 07 27SomethingElse");
        assertEquals(ex.getErrorIndex(), 13);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_parse_Class_String_nullText() throws Exception {
    try {
      DATE_FORMATTER.parse((String) null, LocalDate::from);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_parse_Class_String_nullRule() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.parse("30", (TemporalQuery<?>) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parseBest_firstOption() throws Exception {
    DateTimeFormatter test = DateTimeFormatter.ofPattern("uuuu-MM[-dd]");
    TemporalAccessor result = test.parseBest("2011-06-30", LocalDate::from, YearMonth::from);
    assertEquals(result, LocalDate.of(2011, 6, 30));
  }

  @Test
  public void test_parseBest_secondOption() throws Exception {
    DateTimeFormatter test = DateTimeFormatter.ofPattern("uuuu-MM[-dd]");
    TemporalAccessor result = test.parseBest("2011-06", LocalDate::from, YearMonth::from);
    assertEquals(result, YearMonth.of(2011, 6));
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parseBest_String_parseError() throws Exception {
    try {
      DateTimeFormatter test = DateTimeFormatter.ofPattern("uuuu-MM[-dd]");
      try {
        test.parseBest("2011-XX-30", LocalDate::from, YearMonth::from);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getMessage().contains("could not be parsed"), true);
        assertEquals(ex.getMessage().contains("XX"), true);
        assertEquals(ex.getParsedString(), "2011-XX-30");
        assertEquals(ex.getErrorIndex(), 5);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parseBest_String_parseErrorLongText() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      try {
        test.parseBest(
            "ONEXXX67890123456789012345678901234567890123456789012345678901234567890123456789",
            LocalDate::from,
            YearMonth::from);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getMessage().contains("could not be parsed"), true);
        assertEquals(
            ex.getMessage()
                .contains("ONEXXX6789012345678901234567890123456789012345678901234567890123..."),
            true);
        assertEquals(
            ex.getParsedString(),
            "ONEXXX67890123456789012345678901234567890123456789012345678901234567890123456789");
        assertEquals(ex.getErrorIndex(), 3);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = DateTimeParseException.class)
  public void test_parseBest_String_parseIncomplete() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      try {
        test.parseBest("ONE30SomethingElse", YearMonth::from, LocalDate::from);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getMessage().contains("could not be parsed"), true);
        assertEquals(ex.getMessage().contains("ONE30SomethingElse"), true);
        assertEquals(ex.getParsedString(), "ONE30SomethingElse");
        assertEquals(ex.getErrorIndex(), 5);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_parseBest_String_nullText() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.parseBest((String) null, YearMonth::from, LocalDate::from);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_parseBest_String_nullRules() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.parseBest("30", (TemporalQuery<?>[]) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_parseBest_String_zeroRules() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.parseBest("30", new TemporalQuery<?>[0]);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_parseBest_String_oneRule() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.parseBest("30", LocalDate::from);
      fail("Missing exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_parseToBuilder_StringParsePosition() throws Exception {
    DateTimeFormatter test = fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
    ParsePosition pos = new ParsePosition(0);
    TemporalAccessor result = test.parseUnresolved("ONE30XXX", pos);
    assertEquals(pos.getIndex(), 5);
    assertEquals(pos.getErrorIndex(), -1);
    assertEquals(result.getLong(DAY_OF_MONTH), 30L);
  }

  @Test
  public void test_parseToBuilder_StringParsePosition_parseError() throws Exception {
    DateTimeFormatter test = fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
    ParsePosition pos = new ParsePosition(0);
    TemporalAccessor result = test.parseUnresolved("ONEXXX", pos);
    assertEquals(pos.getIndex(), 0); // TODO: is this right?
    assertEquals(pos.getErrorIndex(), 3);
    assertEquals(result, null);
  }

  @Test(expected = NullPointerException.class)
  public void test_parseToBuilder_StringParsePosition_nullString() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      ParsePosition pos = new ParsePosition(0);
      test.parseUnresolved((String) null, pos);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_parseToBuilder_StringParsePosition_nullParsePosition() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      test.parseUnresolved("ONE30", (ParsePosition) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void test_parseToBuilder_StringParsePosition_invalidPosition() throws Exception {
    try {
      DateTimeFormatter test =
          fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
      ParsePosition pos = new ParsePosition(6);
      test.parseUnresolved("ONE30", pos);
      fail("Missing exception");
    } catch (IndexOutOfBoundsException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  //    @Test
  //    public void test_toFormat_format() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        String result = format.format(LocalDate.of(2008, 6, 30));
  //        assertEquals(result, "ONE30");
  //    }

  //    @Test(expected=NullPointerException.class)
  //    public void test_toFormat_format_null() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        format.format(null);
  //    }

  //    @Test(expected=IllegalArgumentException.class)
  //    public void test_toFormat_format_notCalendrical() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        format.format("Not a Calendrical");
  //    }

  //    //-----------------------------------------------------------------------
  //    @Test
  //    public void test_toFormat_parseObject_String() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        DateTimeBuilder result = (DateTimeBuilder) format.parseObject("ONE30");
  //        assertEquals(result.getLong(DAY_OF_MONTH), 30L);
  //    }

  //    @Test(expected=ParseException.class)
  //    public void test_toFormat_parseObject_String_parseError() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        try {
  //            format.parseObject("ONEXXX");
  //        } catch (ParseException ex) {
  //            assertEquals(ex.getMessage().contains("ONEXXX"), true);
  //            assertEquals(ex.getErrorOffset(), 3);
  //            throw ex;
  //        }
  //    }

  //    @Test(expected=ParseException.class)
  //    public void test_toFormat_parseObject_String_parseErrorLongText() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        try {
  //
  // format.parseObject("ONEXXX67890123456789012345678901234567890123456789012345678901234567890123456789");
  //        } catch (DateTimeParseException ex) {
  //
  // assertEquals(ex.getMessage().contains("ONEXXX6789012345678901234567890123456789012345678901234567890123..."), true);
  //            assertEquals(ex.getParsedString(),
  // "ONEXXX67890123456789012345678901234567890123456789012345678901234567890123456789");
  //            assertEquals(ex.getErrorIndex(), 3);
  //            throw ex;
  //        }
  //    }

  //    @Test(expected=NullPointerException.class)
  //    public void test_toFormat_parseObject_String_null() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        format.parseObject((String) null);
  //    }

  // -----------------------------------------------------------------------
  //    @Test
  //    public void test_toFormat_parseObject_StringParsePosition() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        ParsePosition pos = new ParsePosition(0);
  //        DateTimeBuilder result = (DateTimeBuilder) format.parseObject("ONE30XXX", pos);
  //        assertEquals(pos.getIndex(), 5);
  //        assertEquals(pos.getErrorIndex(), -1);
  //        assertEquals(result.getLong(DAY_OF_MONTH), 30L);
  //    }

  //    @Test
  //    public void test_toFormat_parseObject_StringParsePosition_parseError() throws Exception {
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        ParsePosition pos = new ParsePosition(0);
  //        TemporalAccessor result = (TemporalAccessor) format.parseObject("ONEXXX", pos);
  //        assertEquals(pos.getIndex(), 0);  // TODO: is this right?
  //        assertEquals(pos.getErrorIndex(), 3);
  //        assertEquals(result, null);
  //    }

  //    @Test(expected=NullPointerException.class)
  //    public void test_toFormat_parseObject_StringParsePosition_nullString() throws Exception {
  //        // SimpleDateFormat has this behavior
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        ParsePosition pos = new ParsePosition(0);
  //        format.parseObject((String) null, pos);
  //    }

  //    @Test(expected=NullPointerException.class)
  //    public void test_toFormat_parseObject_StringParsePosition_nullParsePosition() throws
  // Exception {
  //        // SimpleDateFormat has this behavior
  //        DateTimeFormatter test =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        Format format = test.toFormat();
  //        format.parseObject("ONE30", (ParsePosition) null);
  //    }

  //    @Test
  //    public void test_toFormat_parseObject_StringParsePosition_invalidPosition_tooBig() throws
  // Exception {
  //        // SimpleDateFormat has this behavior
  //        DateTimeFormatter dtf =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        ParsePosition pos = new ParsePosition(6);
  //        Format test = dtf.toFormat();
  //        assertNull(test.parseObject("ONE30", pos));
  //        assertTrue(pos.getErrorIndex() >= 0);
  //    }

  //    @Test
  //    public void test_toFormat_parseObject_StringParsePosition_invalidPosition_tooSmall() throws
  // Exception {
  //        // SimpleDateFormat throws StringIndexOutOfBoundException
  //        DateTimeFormatter dtf =
  // fmt.withLocale(Locale.ENGLISH).withDecimalStyle(DecimalStyle.STANDARD);
  //        ParsePosition pos = new ParsePosition(-1);
  //        Format test = dtf.toFormat();
  //        assertNull(test.parseObject("ONE30", pos));
  //        assertTrue(pos.getErrorIndex() >= 0);
  //    }

  //    //-----------------------------------------------------------------------
  //    @Test
  //    public void test_toFormat_Class_format() throws Exception {
  //        Format format = BASIC_FORMATTER.toFormat();
  //        String result = format.format(LocalDate.of(2008, 6, 30));
  //        assertEquals(result, "ONE30");
  //    }

  //    @Test
  //    public void test_toFormat_Class_parseObject_String() throws Exception {
  //        Format format = DATE_FORMATTER.toFormat(LocalDate::from);
  //        LocalDate result = (LocalDate) format.parseObject("ONE2012 07 27");
  //        assertEquals(result, LocalDate.of(2012, 7, 27));
  //    }

  //    @Test(expected=ParseException.class)
  //    public void test_toFormat_parseObject_StringParsePosition_dateTimeError() throws Exception {
  //        Format format = DATE_FORMATTER.toFormat(LocalDate::from);
  //        format.parseObject("ONE2012 07 32");
  //    }

  //    @Test(expected=NullPointerException.class)
  //    public void test_toFormat_Class() throws Exception {
  //        BASIC_FORMATTER.toFormat(null);
  //    }

  // -------------------------------------------------------------------------
  public void test_parse_allZones() throws Exception {
    for (String zoneStr : ZoneId.getAvailableZoneIds()) {
      ZoneId zone = ZoneId.of(zoneStr);
      ZonedDateTime base = ZonedDateTime.of(2014, 12, 31, 12, 0, 0, 0, zone);
      ZonedDateTime test = ZonedDateTime.parse(base.toString());
      assertEquals(test, base);
    }
  }
}
