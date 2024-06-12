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
package org.gwtproject.resources.context;

import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;

import javax.lang.model.element.TypeElement;
import org.gwtproject.resources.ext.*;
import org.gwtproject.resources.rg.util.Util;
import org.gwtproject.resources.rg.util.tools.Utility;

/** Defines base methods for ResourceContext implementations. */
public abstract class AbstractResourceContext implements ResourceContext {
  /**
   * The largest file size that will be inlined. Note that this value is taken before any encodings
   * are applied.
   */
  protected static final int MAX_INLINE_SIZE = 2 << 15;

  private final TreeLogger logger;
  private final ClientBundleContext clientBundleCtx;
  private final GeneratorContext context;
  private final TypeElement resourceBundleType;
  private String currentResourceGeneratorType;
  private String simpleSourceName;

  protected AbstractResourceContext(
      TreeLogger logger,
      GeneratorContext context,
      TypeElement resourceBundleType,
      ClientBundleContext clientBundleCtx) {
    this.logger = logger;
    this.context = context;
    this.resourceBundleType = resourceBundleType;
    this.clientBundleCtx = clientBundleCtx;
  }

  protected static String toBase64(byte[] data) {
    return BaseEncoding.base64().encode(data).replaceAll("\\s+", "");
  }

  @Deprecated
  public String deploy(URL resource, boolean forceExternal) throws UnableToCompleteException {
    return deploy(resource, null, forceExternal);
  }

  public String deploy(URL resource, String mimeType, boolean forceExternal)
      throws UnableToCompleteException {
    String fileName = ResourceGeneratorUtil.baseName(resource);
    byte[] bytes = Util.readURLAsBytes(resource);
    try {
      String finalMimeType =
          (mimeType != null) ? mimeType : getProperty(resource, URLConnection::getContentType);
      return deploy(fileName, finalMimeType, bytes, forceExternal);
    } catch (IOException e) {
      getLogger().log(TreeLogger.ERROR, "Unable to determine mime type of resource", e);
      throw new UnableToCompleteException();
    }
  }

  /**
   * Helper method to read a property of a given URL, automatically closing the InputStream
   * that is opened as a side effect.
   */
  public static <T> T getProperty(URL url, Function<URLConnection, T> prop) throws IOException {
    URLConnection conn = url.openConnection();
    conn.setUseCaches(false);
    try {
      return prop.apply(conn);
    } finally {
      Utility.close(conn.getInputStream());
    }
  }

  protected TreeLogger getLogger() {
    return logger;
  }

  public <T> T getCachedData(String key, Class<T> clazz) {
    return clazz.cast(clientBundleCtx.getCachedData(currentResourceGeneratorType + ":" + key));
  }

  public TypeElement getClientBundleType() {
    return resourceBundleType;
  }

  public GeneratorContext getGeneratorContext() {
    return context;
  }

  public String getImplementationSimpleSourceName() {
    if (simpleSourceName == null) {
      throw new IllegalStateException("Simple source name has not yet been set.");
    }
    return simpleSourceName;
  }

  public <T> boolean putCachedData(String key, T value) {
    key = currentResourceGeneratorType + ":" + key;
    return value != clientBundleCtx.putCachedData(key, value);
  }

  protected GeneratorContext getContext() {
    return context;
  }

  public void setCurrentResourceGenerator(ResourceGenerator rg) {
    currentResourceGeneratorType = rg.getClass().getName();
  }

  void setSimpleSourceName(String name) {
    simpleSourceName = name;
  }
}
