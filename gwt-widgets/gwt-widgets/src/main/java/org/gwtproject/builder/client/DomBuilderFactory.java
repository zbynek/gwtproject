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
package org.gwtproject.builder.client;

import org.gwtproject.builder.shared.ElementBuilderFactory;

/** Factory for creating element builders that construct elements using DOM manipulation. */
public class DomBuilderFactory extends ElementBuilderFactory {

  private static DomBuilderFactory instance;

  /**
   * Get the instance of the {@link DomBuilderFactory}.
   *
   * <p>Use {@link ElementBuilderFactory#get()} to fetch a factory optimized for the browser client.
   * However, you can use this factory directly if you want to force the builders to build elements
   * use DOM manipulation.
   *
   * @return the {@link ElementBuilderFactory}
   */
  public static DomBuilderFactory get() {
    if (instance == null) {
      instance = new DomBuilderFactory();
    }
    return instance;
  }

  /** Created from static factory method. */
  public DomBuilderFactory() {}

  @Override
  public DomTableSectionBuilder createTBodyBuilder() {
    return impl().startTBody();
  }

  @Override
  public DomTableSectionBuilder createTFootBuilder() {
    return impl().startTFoot();
  }

  @Override
  public DomTableSectionBuilder createTHeadBuilder() {
    return impl().startTHead();
  }

  private DomBuilderImpl impl() {
    return new DomBuilderImpl();
  }
}
