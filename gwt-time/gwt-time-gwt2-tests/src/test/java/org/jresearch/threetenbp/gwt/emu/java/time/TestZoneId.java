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
package org.jresearch.threetenbp.gwt.emu.java.time;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jresearch.threetenbp.gwt.emu.java.time.format.TextStyle;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalAccessor;
import org.jresearch.threetenbp.gwt.emu.java.time.zone.ZoneOffsetTransition;
import org.jresearch.threetenbp.gwt.emu.java.time.zone.ZoneRules;
import org.jresearch.threetenbp.gwt.emu.java.time.zone.ZoneRulesException;
import org.junit.Test;

/** Test ZoneId. */
// @Test
public class TestZoneId extends AbstractTest {

  private static ZoneId ZONE_PARIS;
  public static final String LATEST_TZDB = "2010i";
  private static final int OVERLAP = 2;
  private static final int GAP = 0;

  @Override
  public void gwtSetUp() throws Exception {
    super.gwtSetUp();
    ZONE_PARIS = ZoneId.of("Europe/Paris");
  }

  // -----------------------------------------------------------------------
  // Basics
  // -----------------------------------------------------------------------
  // GWT
  //    public void test_immutable() {
  //        Class<ZoneId> cls = ZoneId.class;
  //        assertTrue(Modifier.isPublic(cls.getModifiers()));
  //        Field[] fields = cls.getDeclaredFields();
  //        for (Field field : fields) {
  //            if (Modifier.isStatic(field.getModifiers()) == false) {
  //                assertTrue(Modifier.isPrivate(field.getModifiers()));
  //                assertTrue(Modifier.isFinal(field.getModifiers()) ||
  //                        (Modifier.isVolatile(field.getModifiers()) &&
  // Modifier.isTransient(field.getModifiers())));
  //            }
  //        }
  //    }

  // GWT
  //    @Test(enabled = false)
  //    public void test_serialization_UTC() throws Exception {
  //        ZoneId test = ZoneOffset.UTC;
  //        assertSerializableAndSame(test);
  //    }

  // GWT
  //    public void test_serialization_fixed() throws Exception {
  //        ZoneId test = ZoneId.of("UTC+01:30");
  //        assertSerializable(test);
  //    }

  // GWT
  //    public void test_serialization_Europe() throws Exception {
  //        ZoneId test = ZoneId.of("Europe/London");
  //        assertSerializable(test);
  //    }

  // GWT
  //    public void test_serialization_America() throws Exception {
  //        ZoneId test = ZoneId.of("America/Chicago");
  //        assertSerializable(test);
  //    }

  // GWT
  //    @Test(enabled = false)
  //    public void test_serialization_format() throws ClassNotFoundException, IOException {
  //        assertEqualsSerialisedForm(ZoneId.of("Europe/London"), ZoneId.class);
  //    }

  // -----------------------------------------------------------------------
  // UTC
  // -----------------------------------------------------------------------
  public void test_constant_UTC() {
    ZoneId test = ZoneOffset.UTC;
    assertEquals(test.getId(), "Z");
    assertEquals(test.getDisplayName(TextStyle.FULL, Locale.UK), "Z");
    assertEquals(test.getRules().isFixedOffset(), true);
    assertEquals(test.getRules().getOffset(Instant.ofEpochSecond(0L)), ZoneOffset.UTC);
    checkOffset(test.getRules(), createLDT(2008, 6, 30), ZoneOffset.UTC, 1);
  }

  // -----------------------------------------------------------------------
  // SHORT_IDS
  // -----------------------------------------------------------------------
  public void test_constant_SHORT_IDS() {
    Map<String, String> ids = ZoneId.SHORT_IDS;
    assertEquals(ids.get("EST"), "-05:00");
    assertEquals(ids.get("MST"), "-07:00");
    assertEquals(ids.get("HST"), "-10:00");
    assertEquals(ids.get("ACT"), "Australia/Darwin");
    assertEquals(ids.get("AET"), "Australia/Sydney");
    assertEquals(ids.get("AGT"), "America/Argentina/Buenos_Aires");
    assertEquals(ids.get("ART"), "Africa/Cairo");
    assertEquals(ids.get("AST"), "America/Anchorage");
    assertEquals(ids.get("BET"), "America/Sao_Paulo");
    assertEquals(ids.get("BST"), "Asia/Dhaka");
    assertEquals(ids.get("CAT"), "Africa/Harare");
    assertEquals(ids.get("CNT"), "America/St_Johns");
    assertEquals(ids.get("CST"), "America/Chicago");
    assertEquals(ids.get("CTT"), "Asia/Shanghai");
    assertEquals(ids.get("EAT"), "Africa/Addis_Ababa");
    assertEquals(ids.get("ECT"), "Europe/Paris");
    assertEquals(ids.get("IET"), "America/Indiana/Indianapolis");
    assertEquals(ids.get("IST"), "Asia/Kolkata");
    assertEquals(ids.get("JST"), "Asia/Tokyo");
    assertEquals(ids.get("MIT"), "Pacific/Apia");
    assertEquals(ids.get("NET"), "Asia/Yerevan");
    assertEquals(ids.get("NST"), "Pacific/Auckland");
    assertEquals(ids.get("PLT"), "Asia/Karachi");
    assertEquals(ids.get("PNT"), "America/Phoenix");
    assertEquals(ids.get("PRT"), "America/Puerto_Rico");
    assertEquals(ids.get("PST"), "America/Los_Angeles");
    assertEquals(ids.get("SST"), "Pacific/Guadalcanal");
    assertEquals(ids.get("VST"), "Asia/Ho_Chi_Minh");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void test_constant_SHORT_IDS_immutable() {
    try {
      Map<String, String> ids = ZoneId.SHORT_IDS;
      ids.clear();
      fail("Missing exception");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // system default
  // -----------------------------------------------------------------------
  // GWT no TimeZone
  //	public void test_systemDefault() {
  //		ZoneId test = ZoneId.systemDefault();
  //		assertEquals(test.getId(), TimeZone.getDefault().getID());
  //	}

  // GWT no TimeZone
  //	@Test(expected = DateTimeException.class)
  //	public void test_systemDefault_unableToConvert_badFormat() {
  //		try {
  //			TimeZone current = TimeZone.getDefault();
  //			try {
  //				TimeZone.setDefault(new SimpleTimeZone(127, "Something Weird"));
  //				ZoneId.systemDefault();
  //			} finally {
  //				TimeZone.setDefault(current);
  //			}
  //			fail("Missing exception");
  //		} catch (DateTimeParseException e) {
  //			// expected
  //		}
  //	}

  // GWT no TimeZone
  //	@Test(expected = ZoneRulesException.class)
  //	public void test_systemDefault_unableToConvert_unknownId() {
  //		try {
  //			TimeZone current = TimeZone.getDefault();
  //			try {
  //				TimeZone.setDefault(new SimpleTimeZone(127, "SomethingWeird"));
  //				ZoneId.systemDefault();
  //			} finally {
  //				TimeZone.setDefault(current);
  //			}
  //			fail("Missing exception");
  //		} catch (DateTimeParseException e) {
  //			// expected
  //		}
  //	}

  // -----------------------------------------------------------------------
  // mapped factory
  // -----------------------------------------------------------------------
  public void test_of_string_Map() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("LONDON", "Europe/London");
    map.put("PARIS", "Europe/Paris");
    ZoneId test = ZoneId.of("LONDON", map);
    assertEquals(test.getId(), "Europe/London");
  }

  public void test_of_string_Map_lookThrough() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("LONDON", "Europe/London");
    map.put("PARIS", "Europe/Paris");
    ZoneId test = ZoneId.of("Europe/Madrid", map);
    assertEquals(test.getId(), "Europe/Madrid");
  }

  public void test_of_string_Map_emptyMap() {
    Map<String, String> map = new HashMap<String, String>();
    ZoneId test = ZoneId.of("Europe/Madrid", map);
    assertEquals(test.getId(), "Europe/Madrid");
  }

  @Test(expected = DateTimeException.class)
  public void test_of_string_Map_badFormat() {
    try {
      Map<String, String> map = new HashMap<String, String>();
      ZoneId.of("Not kknown", map);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = ZoneRulesException.class)
  public void test_of_string_Map_unknown() {
    try {
      Map<String, String> map = new HashMap<String, String>();
      ZoneId.of("Unknown", map);
      fail("Missing exception");
    } catch (ZoneRulesException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // regular factory
  // -----------------------------------------------------------------------
  // @DataProvider(name = "String_UTC")
  Object[][] data_of_string_UTC() {
    return new Object[][] {
      {""},
      {"+00"},
      {"+0000"},
      {"+00:00"},
      {"+000000"},
      {"+00:00:00"},
      {"-00"},
      {"-0000"},
      {"-00:00"},
      {"-000000"},
      {"-00:00:00"},
    };
  }

  @Test(/* dataProvider = "String_UTC" */ )
  public void test_of_string_UTC() {
    Object[][] data = data_of_string_UTC();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_UTC((String) objects[0]);
    }
  }

  public void test_of_string_UTC(String id) {
    ZoneId test = ZoneId.of("UTC" + id);
    assertEquals(test.getId(), "UTC");
    assertEquals(test.normalized(), ZoneOffset.UTC);
  }

  @Test(/* dataProvider = "String_UTC" */ )
  public void test_of_string_GMT() {
    Object[][] data = data_of_string_UTC();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_GMT((String) objects[0]);
    }
  }

  public void test_of_string_GMT(String id) {
    ZoneId test = ZoneId.of("GMT" + id);
    assertEquals(test.getId(), "GMT");
    assertEquals(test.normalized(), ZoneOffset.UTC);
  }

  @Test(/* dataProvider = "String_UTC" */ )
  public void test_of_string_UT() {
    Object[][] data = data_of_string_UTC();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_UT((String) objects[0]);
    }
  }

  public void test_of_string_UT(String id) {
    ZoneId test = ZoneId.of("UT" + id);
    assertEquals(test.getId(), "UT");
    assertEquals(test.normalized(), ZoneOffset.UTC);
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "String_Fixed")
  Object[][] data_of_string_Fixed() {
    return new Object[][] {
      {"+0", ""},
      {"+5", "+05:00"},
      {"+01", "+01:00"},
      {"+0100", "+01:00"},
      {"+01:00", "+01:00"},
      {"+010000", "+01:00"},
      {"+01:00:00", "+01:00"},
      {"+12", "+12:00"},
      {"+1234", "+12:34"},
      {"+12:34", "+12:34"},
      {"+123456", "+12:34:56"},
      {"+12:34:56", "+12:34:56"},
      {"-02", "-02:00"},
      {"-5", "-05:00"},
      {"-0200", "-02:00"},
      {"-02:00", "-02:00"},
      {"-020000", "-02:00"},
      {"-02:00:00", "-02:00"},
    };
  }

  @Test(/* dataProvider = "String_Fixed" */ )
  public void test_of_string_offset() {
    Object[][] data = data_of_string_Fixed();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_offset((String) objects[0], (String) objects[1]);
    }
  }

  public void test_of_string_offset(String input, String id) {
    ZoneId test = ZoneId.of(input);
    ZoneOffset offset = ZoneOffset.of(id.isEmpty() ? "Z" : id);
    assertEquals(test, offset);
  }

  @Test(/* dataProvider = "String_Fixed" */ )
  public void test_of_string_FixedUTC() {
    Object[][] data = data_of_string_Fixed();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_FixedUTC((String) objects[0], (String) objects[1]);
    }
  }

  public void test_of_string_FixedUTC(String input, String id) {
    ZoneId test = ZoneId.of("UTC" + input);
    assertEquals(test.getId(), "UTC" + id);
    assertEquals(test.getDisplayName(TextStyle.FULL, Locale.UK), "UTC" + id);
    assertEquals(test.getRules().isFixedOffset(), true);
    ZoneOffset offset = ZoneOffset.of(id.isEmpty() ? "Z" : id);
    assertEquals(test.getRules().getOffset(Instant.ofEpochSecond(0L)), offset);
    checkOffset(test.getRules(), createLDT(2008, 6, 30), offset, 1);
  }

  @Test(/* dataProvider = "String_Fixed" */ )
  public void test_of_string_FixedGMT() {
    Object[][] data = data_of_string_Fixed();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_FixedGMT((String) objects[0], (String) objects[1]);
    }
  }

  public void test_of_string_FixedGMT(String input, String id) {
    ZoneId test = ZoneId.of("GMT" + input);
    assertEquals(test.getId(), "GMT" + id);
    assertEquals(test.getDisplayName(TextStyle.FULL, Locale.UK), "GMT" + id);
    assertEquals(test.getRules().isFixedOffset(), true);
    ZoneOffset offset = ZoneOffset.of(id.isEmpty() ? "Z" : id);
    assertEquals(test.getRules().getOffset(Instant.ofEpochSecond(0L)), offset);
    checkOffset(test.getRules(), createLDT(2008, 6, 30), offset, 1);
  }

  @Test(/* dataProvider = "String_Fixed" */ )
  public void test_of_string_FixedUT() {
    Object[][] data = data_of_string_Fixed();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_FixedUT((String) objects[0], (String) objects[1]);
    }
  }

  public void test_of_string_FixedUT(String input, String id) {
    ZoneId test = ZoneId.of("UT" + input);
    assertEquals(test.getId(), "UT" + id);
    assertEquals(test.getDisplayName(TextStyle.FULL, Locale.UK), "UT" + id);
    assertEquals(test.getRules().isFixedOffset(), true);
    ZoneOffset offset = ZoneOffset.of(id.isEmpty() ? "Z" : id);
    assertEquals(test.getRules().getOffset(Instant.ofEpochSecond(0L)), offset);
    checkOffset(test.getRules(), createLDT(2008, 6, 30), offset, 1);
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "String_UTC_Invalid")
  Object[][] data_of_string_UTC_invalid() {
    return new Object[][] {
      {"A"},
      {"B"},
      {"C"},
      {"D"},
      {"E"},
      {"F"},
      {"G"},
      {"H"},
      {"I"},
      {"J"},
      {"K"},
      {"L"},
      {"M"},
      {"N"},
      {"O"},
      {"P"},
      {"Q"},
      {"R"},
      {"S"},
      {"T"},
      {"U"},
      {"V"},
      {"W"},
      {"X"},
      {"Y"},
      {"+0:00"},
      {"+00:0"},
      {"+0:0"},
      {"+000"},
      {"+00000"},
      {"+0:00:00"},
      {"+00:0:00"},
      {"+00:00:0"},
      {"+0:0:0"},
      {"+0:0:00"},
      {"+00:0:0"},
      {"+0:00:0"},
      {"+01_00"},
      {"+01;00"},
      {"+01@00"},
      {"+01:AA"},
      {"+19"},
      {"+19:00"},
      {"+18:01"},
      {"+18:00:01"},
      {"+1801"},
      {"+180001"},
      {"-0:00"},
      {"-00:0"},
      {"-0:0"},
      {"-000"},
      {"-00000"},
      {"-0:00:00"},
      {"-00:0:00"},
      {"-00:00:0"},
      {"-0:0:0"},
      {"-0:0:00"},
      {"-00:0:0"},
      {"-0:00:0"},
      {"-19"},
      {"-19:00"},
      {"-18:01"},
      {"-18:00:01"},
      {"-1801"},
      {"-180001"},
      {"-01_00"},
      {"-01;00"},
      {"-01@00"},
      {"-01:AA"},
      {"@01:00"},
    };
  }

  @Test(/* dataProvider = "String_UTC_Invalid", */ expected = DateTimeException.class)
  public void test_of_string_UTC_invalid() {
    Object[][] data = data_of_string_UTC_invalid();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_UTC_invalid((String) objects[0]);
    }
  }

  public void test_of_string_UTC_invalid(String id) {
    try {
      ZoneId.of("UTC" + id);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(/* dataProvider = "String_UTC_Invalid", */ expected = DateTimeException.class)
  public void test_of_string_GMT_invalid() {
    Object[][] data = data_of_string_UTC_invalid();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_GMT_invalid((String) objects[0]);
    }
  }

  public void test_of_string_GMT_invalid(String id) {
    try {
      ZoneId.of("GMT" + id);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name = "String_Invalid")
  Object[][] data_of_string_invalid() {
    // \u00ef is a random unicode character
    return new Object[][] {
      {""},
      {":"},
      {"#"},
      {"\u00ef"},
      {"`"},
      {"!"},
      {"\""},
      {"\u00ef"},
      {"$"},
      {"^"},
      {"&"},
      {"*"},
      {"("},
      {")"},
      {"="},
      {"\\"},
      {"|"},
      {","},
      {"<"},
      {">"},
      {"?"},
      {";"},
      {"'"},
      {"["},
      {"]"},
      {"{"},
      {"}"},
      {"\u00ef:A"},
      {"`:A"},
      {"!:A"},
      {"\":A"},
      {"\u00ef:A"},
      {"$:A"},
      {"^:A"},
      {"&:A"},
      {"*:A"},
      {"(:A"},
      {"):A"},
      {"=:A"},
      {"+:A"},
      {"\\:A"},
      {"|:A"},
      {",:A"},
      {"<:A"},
      {">:A"},
      {"?:A"},
      {";:A"},
      {"::A"},
      {"':A"},
      {"@:A"},
      {"~:A"},
      {"[:A"},
      {"]:A"},
      {"{:A"},
      {"}:A"},
      {"A:B#\u00ef"},
      {"A:B#`"},
      {"A:B#!"},
      {"A:B#\""},
      {"A:B#\u00ef"},
      {"A:B#$"},
      {"A:B#^"},
      {"A:B#&"},
      {"A:B#*"},
      {"A:B#("},
      {"A:B#)"},
      {"A:B#="},
      {"A:B#+"},
      {"A:B#\\"},
      {"A:B#|"},
      {"A:B#,"},
      {"A:B#<"},
      {"A:B#>"},
      {"A:B#?"},
      {"A:B#;"},
      {"A:B#:"},
      {"A:B#'"},
      {"A:B#@"},
      {"A:B#~"},
      {"A:B#["},
      {"A:B#]"},
      {"A:B#{"},
      {"A:B#}"},
    };
  }

  @Test(/* dataProvider = "String_Invalid", */ expected = DateTimeException.class)
  public void test_of_string_invalid() {
    Object[][] data = data_of_string_invalid();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_of_string_invalid((String) objects[0]);
    }
  }

  public void test_of_string_invalid(String id) {
    try {
      ZoneId.of(id);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  public void test_of_string_GMT0() {
    ZoneId test = ZoneId.of("GMT0");
    assertEquals(test.getId(), "GMT0");
    assertEquals(test.getRules().isFixedOffset(), true);
    assertEquals(test.normalized(), ZoneOffset.UTC);
  }

  // -----------------------------------------------------------------------
  public void test_of_string_London() {
    ZoneId test = ZoneId.of("Europe/London");
    assertEquals(test.getId(), "Europe/London");
    assertEquals(test.getRules().isFixedOffset(), false);
  }

  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_of_string_null() {
    try {
      ZoneId.of((String) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = ZoneRulesException.class)
  public void test_of_string_unknown_simple() {
    try {
      ZoneId.of("Unknown");
      fail("Missing exception");
    } catch (ZoneRulesException e) {
      // expected
    }
  }

  // -------------------------------------------------------------------------
  // TODO: test by deserialization
  //    public void test_ofUnchecked_string_invalidNotChecked() {
  //        ZoneRegion test = ZoneRegion.ofLenient("Unknown");
  //        assertEquals(test.getId(), "Unknown");
  //    }
  //
  //    public void test_ofUnchecked_string_invalidNotChecked_unusualCharacters() {
  //        ZoneRegion test = ZoneRegion.ofLenient("QWERTYUIOPASDFGHJKLZXCVBNM~/._+-");
  //        assertEquals(test.getId(), "QWERTYUIOPASDFGHJKLZXCVBNM~/._+-");
  //    }

  // -----------------------------------------------------------------------
  // from()
  // -----------------------------------------------------------------------
  public void test_factory_CalendricalObject() {
    assertEquals(ZoneId.from(createZDT(2007, 7, 15, 17, 30, 0, 0, ZONE_PARIS)), ZONE_PARIS);
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_CalendricalObject_invalid_noDerive() {
    try {
      ZoneId.from(LocalTime.of(12, 30));
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_CalendricalObject_null() {
    try {
      ZoneId.from((TemporalAccessor) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // Europe/London
  // -----------------------------------------------------------------------
  public void test_London() {
    ZoneId test = ZoneId.of("Europe/London");
    assertEquals(test.getId(), "Europe/London");
    assertEquals(test.getRules().isFixedOffset(), false);
  }

  public void test_London_getOffset() {
    ZoneId test = ZoneId.of("Europe/London");
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 1, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 2, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 4, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 5, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 6, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 7, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 8, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 9, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 12, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
  }

  public void test_London_getOffset_toDST() {
    ZoneId test = ZoneId.of("Europe/London");
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 24, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 25, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 26, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 27, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 28, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 29, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 30, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 31, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    // cutover at 01:00Z
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 30, 0, 59, 59, 999999999, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 30, 1, 0, 0, 0, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
  }

  public void test_London_getOffset_fromDST() {
    ZoneId test = ZoneId.of("Europe/London");
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 24, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 25, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 26, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 27, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 28, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 29, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 30, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 31, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
    // cutover at 01:00Z
    assertEquals(
        test.getRules()
            .getOffset(createInstant(2008, 10, 26, 0, 59, 59, 999999999, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 26, 1, 0, 0, 0, ZoneOffset.UTC)),
        ZoneOffset.ofHours(0));
  }

  public void test_London_getOffsetInfo() {
    ZoneId test = ZoneId.of("Europe/London");
    checkOffset(test.getRules(), createLDT(2008, 1, 1), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 2, 1), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 1), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 4, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 5, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 6, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 7, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 8, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 9, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 1), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 12, 1), ZoneOffset.ofHours(0), 1);
  }

  public void test_London_getOffsetInfo_toDST() {
    ZoneId test = ZoneId.of("Europe/London");
    checkOffset(test.getRules(), createLDT(2008, 3, 24), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 25), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 26), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 27), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 28), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 29), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 30), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 31), ZoneOffset.ofHours(1), 1);
    // cutover at 01:00Z
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 3, 30, 0, 59, 59, 999999999),
        ZoneOffset.ofHours(0),
        1);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 3, 30, 1, 30, 0, 0), ZoneOffset.ofHours(0), GAP);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 3, 30, 2, 0, 0, 0), ZoneOffset.ofHours(1), 1);
  }

  public void test_London_getOffsetInfo_fromDST() {
    ZoneId test = ZoneId.of("Europe/London");
    checkOffset(test.getRules(), createLDT(2008, 10, 24), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 25), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 26), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 27), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 28), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 29), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 30), ZoneOffset.ofHours(0), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 31), ZoneOffset.ofHours(0), 1);
    // cutover at 01:00Z
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 10, 26, 0, 59, 59, 999999999),
        ZoneOffset.ofHours(1),
        1);
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 10, 26, 1, 30, 0, 0),
        ZoneOffset.ofHours(1),
        OVERLAP);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 10, 26, 2, 0, 0, 0), ZoneOffset.ofHours(0), 1);
  }

  public void test_London_getOffsetInfo_gap() {
    ZoneId test = ZoneId.of("Europe/London");
    final LocalDateTime dateTime = LocalDateTime.of(2008, 3, 30, 1, 0, 0, 0);
    ZoneOffsetTransition trans = checkOffset(test.getRules(), dateTime, ZoneOffset.ofHours(0), GAP);
    assertEquals(trans.isGap(), true);
    assertEquals(trans.isOverlap(), false);
    assertEquals(trans.getOffsetBefore(), ZoneOffset.ofHours(0));
    assertEquals(trans.getOffsetAfter(), ZoneOffset.ofHours(1));
    assertEquals(trans.getInstant(), dateTime.toInstant(ZoneOffset.UTC));
    assertEquals(trans.getDateTimeBefore(), LocalDateTime.of(2008, 3, 30, 1, 0));
    assertEquals(trans.getDateTimeAfter(), LocalDateTime.of(2008, 3, 30, 2, 0));
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-1)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(0)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(1)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(2)), false);
    assertEquals(trans.toString(), "Transition[Gap at 2008-03-30T01:00Z to +01:00]");

    assertFalse(trans.equals(null));
    assertFalse(trans.equals(ZoneOffset.ofHours(0)));
    assertTrue(trans.equals(trans));

    final ZoneOffsetTransition otherTrans = test.getRules().getTransition(dateTime);
    assertTrue(trans.equals(otherTrans));
    assertEquals(trans.hashCode(), otherTrans.hashCode());
  }

  public void test_London_getOffsetInfo_overlap() {
    ZoneId test = ZoneId.of("Europe/London");
    final LocalDateTime dateTime = LocalDateTime.of(2008, 10, 26, 1, 0, 0, 0);
    ZoneOffsetTransition trans =
        checkOffset(test.getRules(), dateTime, ZoneOffset.ofHours(1), OVERLAP);
    assertEquals(trans.isGap(), false);
    assertEquals(trans.isOverlap(), true);
    assertEquals(trans.getOffsetBefore(), ZoneOffset.ofHours(1));
    assertEquals(trans.getOffsetAfter(), ZoneOffset.ofHours(0));
    assertEquals(trans.getInstant(), dateTime.toInstant(ZoneOffset.UTC));
    assertEquals(trans.getDateTimeBefore(), LocalDateTime.of(2008, 10, 26, 2, 0));
    assertEquals(trans.getDateTimeAfter(), LocalDateTime.of(2008, 10, 26, 1, 0));
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-1)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(0)), true);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(1)), true);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(2)), false);
    assertEquals(trans.toString(), "Transition[Overlap at 2008-10-26T02:00+01:00 to Z]");

    assertFalse(trans.equals(null));
    assertFalse(trans.equals(ZoneOffset.ofHours(1)));
    assertTrue(trans.equals(trans));

    final ZoneOffsetTransition otherTrans = test.getRules().getTransition(dateTime);
    assertTrue(trans.equals(otherTrans));
    assertEquals(trans.hashCode(), otherTrans.hashCode());
  }

  // -----------------------------------------------------------------------
  // Europe/Paris
  // -----------------------------------------------------------------------
  public void test_Paris() {
    ZoneId test = ZoneId.of("Europe/Paris");
    assertEquals(test.getId(), "Europe/Paris");
    assertEquals(test.getRules().isFixedOffset(), false);
  }

  public void test_Paris_getOffset() {
    ZoneId test = ZoneId.of("Europe/Paris");
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 1, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 2, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 4, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 5, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 6, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 7, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 8, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 9, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 12, 1, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
  }

  public void test_Paris_getOffset_toDST() {
    ZoneId test = ZoneId.of("Europe/Paris");
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 24, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 25, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 26, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 27, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 28, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 29, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 30, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 31, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    // cutover at 01:00Z
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 30, 0, 59, 59, 999999999, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 30, 1, 0, 0, 0, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
  }

  public void test_Paris_getOffset_fromDST() {
    ZoneId test = ZoneId.of("Europe/Paris");
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 24, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 25, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 26, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 27, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 28, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 29, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 30, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 31, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
    // cutover at 01:00Z
    assertEquals(
        test.getRules()
            .getOffset(createInstant(2008, 10, 26, 0, 59, 59, 999999999, ZoneOffset.UTC)),
        ZoneOffset.ofHours(2));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 26, 1, 0, 0, 0, ZoneOffset.UTC)),
        ZoneOffset.ofHours(1));
  }

  public void test_Paris_getOffsetInfo() {
    ZoneId test = ZoneId.of("Europe/Paris");
    checkOffset(test.getRules(), createLDT(2008, 1, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 2, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 4, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 5, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 6, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 7, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 8, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 9, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 1), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 1), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 12, 1), ZoneOffset.ofHours(1), 1);
  }

  public void test_Paris_getOffsetInfo_toDST() {
    ZoneId test = ZoneId.of("Europe/Paris");
    checkOffset(test.getRules(), createLDT(2008, 3, 24), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 25), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 26), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 27), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 28), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 29), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 30), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 31), ZoneOffset.ofHours(2), 1);
    // cutover at 01:00Z which is 02:00+01:00(local Paris time)
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 3, 30, 1, 59, 59, 999999999),
        ZoneOffset.ofHours(1),
        1);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 3, 30, 2, 30, 0, 0), ZoneOffset.ofHours(1), GAP);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 3, 30, 3, 0, 0, 0), ZoneOffset.ofHours(2), 1);
  }

  public void test_Paris_getOffsetInfo_fromDST() {
    ZoneId test = ZoneId.of("Europe/Paris");
    checkOffset(test.getRules(), createLDT(2008, 10, 24), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 25), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 26), ZoneOffset.ofHours(2), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 27), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 28), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 29), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 30), ZoneOffset.ofHours(1), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 31), ZoneOffset.ofHours(1), 1);
    // cutover at 01:00Z which is 02:00+01:00(local Paris time)
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 10, 26, 1, 59, 59, 999999999),
        ZoneOffset.ofHours(2),
        1);
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 10, 26, 2, 30, 0, 0),
        ZoneOffset.ofHours(2),
        OVERLAP);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 10, 26, 3, 0, 0, 0), ZoneOffset.ofHours(1), 1);
  }

  public void test_Paris_getOffsetInfo_gap() {
    ZoneId test = ZoneId.of("Europe/Paris");
    final LocalDateTime dateTime = LocalDateTime.of(2008, 3, 30, 2, 0, 0, 0);
    ZoneOffsetTransition trans = checkOffset(test.getRules(), dateTime, ZoneOffset.ofHours(1), GAP);
    assertEquals(trans.isGap(), true);
    assertEquals(trans.isOverlap(), false);
    assertEquals(trans.getOffsetBefore(), ZoneOffset.ofHours(1));
    assertEquals(trans.getOffsetAfter(), ZoneOffset.ofHours(2));
    assertEquals(trans.getInstant(), createInstant(2008, 3, 30, 1, 0, 0, 0, ZoneOffset.UTC));
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(0)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(1)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(2)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(3)), false);
    assertEquals(trans.toString(), "Transition[Gap at 2008-03-30T02:00+01:00 to +02:00]");

    assertFalse(trans.equals(null));
    assertFalse(trans.equals(ZoneOffset.ofHours(1)));
    assertTrue(trans.equals(trans));

    final ZoneOffsetTransition otherDis = test.getRules().getTransition(dateTime);
    assertTrue(trans.equals(otherDis));
    assertEquals(trans.hashCode(), otherDis.hashCode());
  }

  public void test_Paris_getOffsetInfo_overlap() {
    ZoneId test = ZoneId.of("Europe/Paris");
    final LocalDateTime dateTime = LocalDateTime.of(2008, 10, 26, 2, 0, 0, 0);
    ZoneOffsetTransition trans =
        checkOffset(test.getRules(), dateTime, ZoneOffset.ofHours(2), OVERLAP);
    assertEquals(trans.isGap(), false);
    assertEquals(trans.isOverlap(), true);
    assertEquals(trans.getOffsetBefore(), ZoneOffset.ofHours(2));
    assertEquals(trans.getOffsetAfter(), ZoneOffset.ofHours(1));
    assertEquals(trans.getInstant(), createInstant(2008, 10, 26, 1, 0, 0, 0, ZoneOffset.UTC));
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(0)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(1)), true);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(2)), true);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(3)), false);
    assertEquals(trans.toString(), "Transition[Overlap at 2008-10-26T03:00+02:00 to +01:00]");

    assertFalse(trans.equals(null));
    assertFalse(trans.equals(ZoneOffset.ofHours(2)));
    assertTrue(trans.equals(trans));

    final ZoneOffsetTransition otherDis = test.getRules().getTransition(dateTime);
    assertTrue(trans.equals(otherDis));
    assertEquals(trans.hashCode(), otherDis.hashCode());
  }

  // -----------------------------------------------------------------------
  // America/New_York
  // -----------------------------------------------------------------------
  public void test_NewYork() {
    ZoneId test = ZoneId.of("America/New_York");
    assertEquals(test.getId(), "America/New_York");
    assertEquals(test.getRules().isFixedOffset(), false);
  }

  public void test_NewYork_getOffset() {
    ZoneId test = ZoneId.of("America/New_York");
    ZoneOffset offset = ZoneOffset.ofHours(-5);
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 1, 1, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 2, 1, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 1, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 4, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 5, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 6, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 7, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 8, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 9, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 12, 1, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 1, 28, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 2, 28, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 4, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 5, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 6, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 7, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 8, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 9, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 10, 28, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 28, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 12, 28, offset)), ZoneOffset.ofHours(-5));
  }

  public void test_NewYork_getOffset_toDST() {
    ZoneId test = ZoneId.of("America/New_York");
    ZoneOffset offset = ZoneOffset.ofHours(-5);
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 8, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 9, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 10, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 11, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 12, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 13, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 14, offset)), ZoneOffset.ofHours(-4));
    // cutover at 02:00 local
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 9, 1, 59, 59, 999999999, offset)),
        ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 3, 9, 2, 0, 0, 0, offset)),
        ZoneOffset.ofHours(-4));
  }

  public void test_NewYork_getOffset_fromDST() {
    ZoneId test = ZoneId.of("America/New_York");
    ZoneOffset offset = ZoneOffset.ofHours(-4);
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 1, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 2, offset)), ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 3, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 4, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 5, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 6, offset)), ZoneOffset.ofHours(-5));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 7, offset)), ZoneOffset.ofHours(-5));
    // cutover at 02:00 local
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 2, 1, 59, 59, 999999999, offset)),
        ZoneOffset.ofHours(-4));
    assertEquals(
        test.getRules().getOffset(createInstant(2008, 11, 2, 2, 0, 0, 0, offset)),
        ZoneOffset.ofHours(-5));
  }

  public void test_NewYork_getOffsetInfo() {
    ZoneId test = ZoneId.of("America/New_York");
    checkOffset(test.getRules(), createLDT(2008, 1, 1), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 2, 1), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 1), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 4, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 5, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 6, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 7, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 8, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 9, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 12, 1), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 1, 28), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 2, 28), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 4, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 5, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 6, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 7, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 8, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 9, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 10, 28), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 28), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 12, 28), ZoneOffset.ofHours(-5), 1);
  }

  public void test_NewYork_getOffsetInfo_toDST() {
    ZoneId test = ZoneId.of("America/New_York");
    checkOffset(test.getRules(), createLDT(2008, 3, 8), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 9), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 10), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 11), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 12), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 13), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 3, 14), ZoneOffset.ofHours(-4), 1);
    // cutover at 02:00 local
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 3, 9, 1, 59, 59, 999999999),
        ZoneOffset.ofHours(-5),
        1);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 3, 9, 2, 30, 0, 0), ZoneOffset.ofHours(-5), GAP);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 3, 9, 3, 0, 0, 0), ZoneOffset.ofHours(-4), 1);
  }

  public void test_NewYork_getOffsetInfo_fromDST() {
    ZoneId test = ZoneId.of("America/New_York");
    checkOffset(test.getRules(), createLDT(2008, 11, 1), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 2), ZoneOffset.ofHours(-4), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 3), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 4), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 5), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 6), ZoneOffset.ofHours(-5), 1);
    checkOffset(test.getRules(), createLDT(2008, 11, 7), ZoneOffset.ofHours(-5), 1);
    // cutover at 02:00 local
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 11, 2, 0, 59, 59, 999999999),
        ZoneOffset.ofHours(-4),
        1);
    checkOffset(
        test.getRules(),
        LocalDateTime.of(2008, 11, 2, 1, 30, 0, 0),
        ZoneOffset.ofHours(-4),
        OVERLAP);
    checkOffset(
        test.getRules(), LocalDateTime.of(2008, 11, 2, 2, 0, 0, 0), ZoneOffset.ofHours(-5), 1);
  }

  public void test_NewYork_getOffsetInfo_gap() {
    ZoneId test = ZoneId.of("America/New_York");
    final LocalDateTime dateTime = LocalDateTime.of(2008, 3, 9, 2, 0, 0, 0);
    ZoneOffsetTransition trans =
        checkOffset(test.getRules(), dateTime, ZoneOffset.ofHours(-5), GAP);
    assertEquals(trans.getOffsetBefore(), ZoneOffset.ofHours(-5));
    assertEquals(trans.getOffsetAfter(), ZoneOffset.ofHours(-4));
    assertEquals(trans.getInstant(), createInstant(2008, 3, 9, 2, 0, 0, 0, ZoneOffset.ofHours(-5)));
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-6)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-5)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-4)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-3)), false);
    assertEquals(trans.toString(), "Transition[Gap at 2008-03-09T02:00-05:00 to -04:00]");

    assertFalse(trans.equals(null));
    assertFalse(trans.equals(ZoneOffset.ofHours(-5)));
    assertTrue(trans.equals(trans));

    final ZoneOffsetTransition otherTrans = test.getRules().getTransition(dateTime);
    assertTrue(trans.equals(otherTrans));

    assertEquals(trans.hashCode(), otherTrans.hashCode());
  }

  public void test_NewYork_getOffsetInfo_overlap() {
    ZoneId test = ZoneId.of("America/New_York");
    final LocalDateTime dateTime = LocalDateTime.of(2008, 11, 2, 1, 0, 0, 0);
    ZoneOffsetTransition trans =
        checkOffset(test.getRules(), dateTime, ZoneOffset.ofHours(-4), OVERLAP);
    assertEquals(trans.getOffsetBefore(), ZoneOffset.ofHours(-4));
    assertEquals(trans.getOffsetAfter(), ZoneOffset.ofHours(-5));
    assertEquals(
        trans.getInstant(), createInstant(2008, 11, 2, 2, 0, 0, 0, ZoneOffset.ofHours(-4)));
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-1)), false);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-5)), true);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(-4)), true);
    assertEquals(trans.isValidOffset(ZoneOffset.ofHours(2)), false);
    assertEquals(trans.toString(), "Transition[Overlap at 2008-11-02T02:00-04:00 to -05:00]");

    assertFalse(trans.equals(null));
    assertFalse(trans.equals(ZoneOffset.ofHours(-4)));
    assertTrue(trans.equals(trans));

    final ZoneOffsetTransition otherTrans = test.getRules().getTransition(dateTime);
    assertTrue(trans.equals(otherTrans));

    assertEquals(trans.hashCode(), otherTrans.hashCode());
  }

  // -----------------------------------------------------------------------
  // getXxx() isXxx()
  // -----------------------------------------------------------------------
  public void test_get_Tzdb() {
    ZoneId test = ZoneId.of("Europe/London");
    assertEquals(test.getId(), "Europe/London");
    assertEquals(test.getRules().isFixedOffset(), false);
  }

  public void test_get_TzdbFixed() {
    ZoneId test = ZoneId.of("+01:30");
    assertEquals(test.getId(), "+01:30");
    assertEquals(test.getRules().isFixedOffset(), true);
  }

  // -----------------------------------------------------------------------
  // equals() / hashCode()
  // -----------------------------------------------------------------------
  public void test_equals() {
    ZoneId test1 = ZoneId.of("Europe/London");
    ZoneId test2 = ZoneId.of("Europe/Paris");
    ZoneId test2b = ZoneId.of("Europe/Paris");
    assertEquals(test1.equals(test2), false);
    assertEquals(test2.equals(test1), false);

    assertEquals(test1.equals(test1), true);
    assertEquals(test2.equals(test2), true);
    assertEquals(test2.equals(test2b), true);

    assertEquals(test1.hashCode() == test1.hashCode(), true);
    assertEquals(test2.hashCode() == test2.hashCode(), true);
    assertEquals(test2.hashCode() == test2b.hashCode(), true);
  }

  public void test_equals_null() {
    assertEquals(ZoneId.of("Europe/London").equals(null), false);
  }

  public void test_equals_notTimeZone() {
    assertEquals(ZoneId.of("Europe/London").equals("Europe/London"), false);
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------
  // @DataProvider(name = "ToString")
  Object[][] data_toString() {
    return new Object[][] {
      {"Europe/London", "Europe/London"},
      {"Europe/Paris", "Europe/Paris"},
      {"Europe/Berlin", "Europe/Berlin"},
      {"Z", "Z"},
      {"UTC", "UTC"},
      {"UTC+01:00", "UTC+01:00"},
      {"GMT+01:00", "GMT+01:00"},
      {"UT+01:00", "UT+01:00"},
    };
  }

  @Test(/* dataProvider = "ToString" */ )
  public void test_toString() {
    Object[][] data = data_toString();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_toString((String) objects[0], (String) objects[1]);
    }
  }

  public void test_toString(String id, String expected) {
    ZoneId test = ZoneId.of(id);
    assertEquals(test.toString(), expected);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  private Instant createInstant(int year, int month, int day, ZoneOffset offset) {
    return LocalDateTime.of(year, month, day, 0, 0).toInstant(offset);
  }

  private Instant createInstant(
      int year, int month, int day, int hour, int min, int sec, int nano, ZoneOffset offset) {
    return LocalDateTime.of(year, month, day, hour, min, sec, nano).toInstant(offset);
  }

  private ZonedDateTime createZDT(
      int year, int month, int day, int hour, int min, int sec, int nano, ZoneId zone) {
    return LocalDateTime.of(year, month, day, hour, min, sec, nano).atZone(zone);
  }

  private LocalDateTime createLDT(int year, int month, int day) {
    return LocalDateTime.of(year, month, day, 0, 0);
  }

  private ZoneOffsetTransition checkOffset(
      ZoneRules rules, LocalDateTime dateTime, ZoneOffset offset, int type) {
    List<ZoneOffset> validOffsets = rules.getValidOffsets(dateTime);
    assertEquals(validOffsets.size(), type);
    assertEquals(rules.getOffset(dateTime), offset);
    if (type == 1) {
      assertEquals(validOffsets.get(0), offset);
      return null;
    } else {
      ZoneOffsetTransition zot = rules.getTransition(dateTime);
      assertNotNull(zot);
      assertEquals(zot.isOverlap(), type == 2);
      assertEquals(zot.isGap(), type == 0);
      assertEquals(zot.isValidOffset(offset), type == 2);
      return zot;
    }
  }
}
