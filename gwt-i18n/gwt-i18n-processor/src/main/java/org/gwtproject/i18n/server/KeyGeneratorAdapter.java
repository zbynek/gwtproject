/*
 * Copyright 2011 Google Inc.
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
package org.gwtproject.i18n.server;

/** Adapter to use an old-style KeyGenerator from the new API. */
@SuppressWarnings("deprecation")
public class KeyGeneratorAdapter implements KeyGenerator {

  private final org.gwtproject.i18n.rg.rebind.keygen.KeyGenerator keygen;

  /** @param keygen */
  public KeyGeneratorAdapter(org.gwtproject.i18n.rg.rebind.keygen.KeyGenerator keygen) {
    this.keygen = keygen;
  }

  public String generateKey(Message msg) {
    String className = msg.getMessageInterface().getQualifiedName();
    String methodName = msg.getMethodName();
    String text = msg.getDefaultMessage();
    String meaning = msg.getMeaning();
    return keygen.generateKey(className, methodName, text, meaning);
  }

  @Override
  public String toString() {
    return KeyGeneratorAdapter.class.getName() + " for " + keygen.getClass().getCanonicalName();
  }
}
