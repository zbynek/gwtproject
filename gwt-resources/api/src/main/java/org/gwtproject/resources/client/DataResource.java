/*
 *
 * Copyright © ${year} ${name}
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

/*
 * Copyright 2008 Google Inc.
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

import java.lang.annotation.*;
import org.gwtproject.safehtml.shared.SafeUri;

/**
 * A non-text resource. Use {@link DataResource.MimeType} to provide MIME Types for embedded
 * resources which may not be determined automatically at compile time. Use {@link
 * DataResource.DoNotEmbed} to prevent a resource from being embedded.
 */
public interface DataResource extends ResourcePrototype {
  /**
   * Retrieves a URL by which the contents of the resource can be obtained. This will be an absolute
   * URL.
   */
  SafeUri getSafeUri();

  /**
   * Specifies that the resource or resources associated with the {@link ResourcePrototype} should
   * not be embedded into the compiled output. This may be useful, for exmaple, when it a particular
   * browser or plugin is unable to handle RFC 2397 data URLs.
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @interface DoNotEmbed {}

  /**
   * Specifies the MIME Type of the resource or resources associated with the {@link
   * ResourcePrototype}.
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @interface MimeType {
    String value();
  }
}
