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
package org.gwtproject.user.client.ui;

/**
 * Extended by objects which have underlying {@link TreeItem}. Provides access to that item, if it
 * exists, without compromising the ability to provide a mock object instance in JRE unit tests.
 */
public interface IsTreeItem {

  /** Returns the {@link TreeItem} aspect of the receiver. */
  TreeItem asTreeItem();
}
