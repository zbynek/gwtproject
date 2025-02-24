/*
 * Copyright © 2019 The GWT Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.resources.client;

import static junit.framework.TestCase.*;

public class UniqueNamingTest {

  static class OuterA {

    static class BundleHolder {

      @Resource
      interface Resources extends ClientBundle {
        @Source("hello.txt")
        TextResource hello();
      }
    }
  }

  static class OuterB {

    static class BundleHolder {

      @Resource
      interface Resources extends ClientBundle {
        @Source("hello2.txt")
        TextResource hello();
      }
    }
  }

  public void testUniqueNaming() {
    OuterA.BundleHolder.Resources outerAResources =
        new UniqueNamingTest_OuterA_BundleHolder_ResourcesImpl();
    OuterB.BundleHolder.Resources outerBResources =
        new UniqueNamingTest_OuterB_BundleHolder_ResourcesImpl();
    assertNotSame(outerAResources.hello(), outerBResources.hello());
  }
}
