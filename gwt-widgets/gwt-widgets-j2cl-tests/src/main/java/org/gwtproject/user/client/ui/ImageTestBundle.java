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
package org.gwtproject.user.client.ui;

import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.ImageResource;

/** @author Dmitrii Tikhomirov Created by treblereel 9/12/19 */
// @Resource
public interface ImageTestBundle extends ClientBundle {
  public ImageResource prettyPiccy();

  @Source("prettyPiccy.png")
  @ImageResource.ImageOptions(preventInlining = true)
  ImageResource prettyPiccyStandalone();
}
