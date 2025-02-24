/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.resources;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import org.gwtproject.resources.client.*;
import org.gwtproject.resources.client.gss.*;

/** Test suite for SafeHtml GWTTestCases. */
@GWT3Resources(
    clientBundle =
        @GWT3Resources.ClientBundle(
            cacheLocation =
                "../../../../gwt2-tests/src/test/java/org/gwtproject/resources/cache/gwt-cache",
            cacheUrl = "/org.gwtproject.resources.ResourcesTestsModule.JUnit/gwt-cache/"),
    cssResource =
        @GWT3Resources.CssResource(
            enableGss = true,
            obfuscationPrefix = "obfuscationPrefix",
            conversionMode = "lenient",
            allowedAtRules = {"extenal", "dontIgnoreThis", "dontIgnoreThisToo"}))
public class ResourcesGwtSuite {
  private ResourcesGwtSuite() {}

  public static Test suite() {
    GWTTestSuite suite = new GWTTestSuite("Test suite for Resources GWTTestCases");

    suite.addTestSuite(DataResourceDoNotEmbedTest.class);
    suite.addTestSuite(DataResourceMimeTypeTest.class);
    suite.addTestSuite(ExternalTextResourceTest.class);
    suite.addTestSuite(ImageResourceTest.class);
    suite.addTestSuite(NestedBundleTest.class);
    suite.addTestSuite(TextResourceTest.class);
    suite.addTestSuite(CSSResourceTest.class);
    suite.addTestSuite(CSSResourceWithGSSTest.class);

    // GSS
    suite.addTestSuite(AutoConversionTest.class);
    suite.addTestSuite(GssResourceTest.class);
    suite.addTestSuite(DebugObfuscationStyleTest.class);
    suite.addTestSuite(PrettyObfuscationStyleTest.class);
    suite.addTestSuite(StableShortTypeObfuscationStyleTest.class);
    suite.addTestSuite(StableNoTypeObfuscationStyleTest.class);
    suite.addTestSuite(StableObfuscationStyleTest.class);
    return suite;
  }
}
