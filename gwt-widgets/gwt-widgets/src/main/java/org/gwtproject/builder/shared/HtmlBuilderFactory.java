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
package org.gwtproject.builder.shared;

/** Factory for creating element builders that use string concatenation to generate HTML. */
public class HtmlBuilderFactory extends ElementBuilderFactory {

  private static HtmlBuilderFactory instance;

  /**
   * Get the instance of the {@link HtmlBuilderFactory}.
   *
   * <p>Use {@link ElementBuilderFactory#get()} to fetch a factory optimized for the browser client.
   * However, you can use this factory directly if you want to force the builders to builder
   * elements using HTML string concatenation and innerHTML. You can also use this factory if you
   * want access to the HTML string, such as when you are building HTML on a server.
   *
   * @return the {@link ElementBuilderFactory}
   */
  public static HtmlBuilderFactory get() {
    if (instance == null) {
      instance = new HtmlBuilderFactory();
    }
    return instance;
  }

  /** Created from static factory method. */
  protected HtmlBuilderFactory() {}

  @Override
  public HtmlTableSectionBuilder createTBodyBuilder() {
    return impl().startTBody();
  }

  @Override
  public HtmlTableSectionBuilder createTFootBuilder() {
    return impl().startTFoot();
  }

  @Override
  public HtmlTableSectionBuilder createTHeadBuilder() {
    return impl().startTHead();
  }


  private HtmlBuilderImpl impl() {
    return new HtmlBuilderImpl();
  }
}
