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

import org.gwtproject.resources.client.ResourcePrototype;

public class ImageTestBundleImpl implements ImageTestBundle {
  private static ImageTestBundleImpl _instance0 = new ImageTestBundleImpl();

  private void prettyPiccyInitializer() {
    prettyPiccy =
        new org.gwtproject.resources.client.impl.ImageResourcePrototype(
            "prettyPiccy",
            org.gwtproject.safehtml.shared.UriUtils.fromTrustedString(externalImage),
            0,
            0,
            58,
            63,
            false,
            false);
  }

  private static class prettyPiccyInitializer {
    static {
      _instance0.prettyPiccyInitializer();
    }

    static org.gwtproject.resources.client.ImageResource get() {
      return prettyPiccy;
    }
  }

  public org.gwtproject.resources.client.ImageResource prettyPiccy() {
    return prettyPiccyInitializer.get();
  }

  private void prettyPiccyStandaloneInitializer() {
    prettyPiccyStandalone =
        new org.gwtproject.resources.client.impl.ImageResourcePrototype(
            "prettyPiccyStandalone",
            org.gwtproject.safehtml.shared.UriUtils.fromTrustedString(externalImage0),
            0,
            0,
            58,
            63,
            false,
            false);
  }

  private static class prettyPiccyStandaloneInitializer {
    static {
      _instance0.prettyPiccyStandaloneInitializer();
    }

    static org.gwtproject.resources.client.ImageResource get() {
      return prettyPiccyStandalone;
    }
  }

  public org.gwtproject.resources.client.ImageResource prettyPiccyStandalone() {
    return prettyPiccyStandaloneInitializer.get();
  }

  private static java.util.HashMap<String, ResourcePrototype> resourceMap;
  private static final String externalImage =
      "data:image/png;base64,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";
  private static final String externalImage0 =
      "/gwt-cache/05E9510FACC6A6CB2D401C7B67CA22C7.cache.png";
  private static org.gwtproject.resources.client.ImageResource prettyPiccy;
  private static org.gwtproject.resources.client.ImageResource prettyPiccyStandalone;

  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      prettyPiccy(), prettyPiccyStandalone(),
    };
  }

  public ResourcePrototype getResource(String name) {
    if (resourceMap == null) {
      resourceMap = new java.util.HashMap<String, ResourcePrototype>();
      resourceMap.put("prettyPiccy", prettyPiccy());
      resourceMap.put("prettyPiccyStandalone", prettyPiccyStandalone());
    }
    return resourceMap.get(name);
  }
}
