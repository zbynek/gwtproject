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
package org.gwtproject.resources.rg.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import org.gwtproject.resources.ext.Resource;
import org.gwtproject.resources.ext.TreeLogger;
import org.gwtproject.resources.ext.UnableToCompleteException;
import org.gwtproject.resources.rg.util.tools.Utility;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public final class Util {

  private static final String FILE_PROTOCOL = "file";
  private static final String JAR_PROTOCOL = "jar";
  private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

  /**
   * The size of a {@link #threadLocalBuf}, which should be large enough for efficient data transfer
   * but small enough to fit easily into the L2 cache of most modern processors.
   */
  private static final int THREAD_LOCAL_BUF_SIZE = 16 * 1024;
  /** Stores reusable thread local buffers for efficient data transfer. */
  private static final ThreadLocal<byte[]> threadLocalBuf = new ThreadLocal<byte[]>();

  public static String DEFAULT_ENCODING = "UTF-8";

  /**
   * Computes the MD5 hash for the specified byte array.
   *
   * @return a big fat string encoding of the MD5 for the content, suitably formatted for use as a
   *     file name
   */
  public static String computeStrongName(byte[] content) {
    return computeStrongName(new byte[][] {content});
  }

  /**
   * Computes the MD5 hash of the specified byte arrays.
   *
   * @return a big fat string encoding of the MD5 for the content, suitably formatted for use as a
   *     file name
   */
  public static String computeStrongName(byte[][] contents) {
    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error initializing MD5", e);
    }

    /*
     * Include the lengths of the contents components in the hash, so that the
     * hashed sequence of bytes is in a one-to-one correspondence with the
     * possible arguments to this method.
     */
    ByteBuffer b = ByteBuffer.allocate((contents.length + 1) * 4);
    b.putInt(contents.length);
    for (int i = 0; i < contents.length; i++) {
      b.putInt(contents[i].length);
    }
    b.flip();
    md5.update(b);

    // Now hash the actual contents of the arrays
    for (int i = 0; i < contents.length; i++) {
      md5.update(contents[i]);
    }
    return printHexBinary(md5.digest()).toUpperCase();
  }

  public static String printHexBinary(byte[] data) {
    StringBuilder r = new StringBuilder(data.length * 2);
    byte[] var3 = data;
    int var4 = data.length;

    for (int var5 = 0; var5 < var4; ++var5) {
      byte b = var3[var5];
      r.append(hexCode[b >> 4 & 15]);
      r.append(hexCode[b & 15]);
    }

    return r.toString();
  }

  /** Copies an input stream out to an output stream. Closes the input steam and output stream. */
  public static void copy(ProcessingEnvironment processingEnv, InputStream is, OutputStream os)
      throws UnableToCompleteException {
    try {
      copy(is, os);
    } catch (IOException e) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Error during copy");
      throw new UnableToCompleteException("Error during copy");
    }
  }

  public static void copy(InputStream is, OutputStream os) throws IOException {
    try {
      copyNoClose(is, os);
    } finally {
      Utility.close(is);
      Utility.close(os);
    }
  }

  /**
   * Copies all of the bytes from the input stream to the output stream until the input stream is
   * EOF. Does not close either stream.
   */
  public static void copyNoClose(InputStream is, OutputStream os) throws IOException {
    byte[] buf = takeThreadLocalBuf();
    try {
      int i;
      while ((i = is.read(buf)) != -1) {
        os.write(buf, 0, i);
      }
    } finally {
      releaseThreadLocalBuf(buf);
    }
  }

  /**
   * Release a buffer previously returned from {@link #takeThreadLocalBuf()}. The released buffer
   * may then be reused.
   */
  public static void releaseThreadLocalBuf(byte[] buf) {
    assert buf.length == THREAD_LOCAL_BUF_SIZE;
    threadLocalBuf.set(buf);
  }

  /**
   * Get a large byte buffer local to this thread. Currently this is set to a 16k buffer, which is
   * small enough to fit into the L2 cache on modern processors. The contents of the returned buffer
   * are undefined. Calling {@link #releaseThreadLocalBuf(byte[])} on the returned buffer allows
   * subsequent callers to reuse the buffer later, avoiding unncessary allocations and GC.
   */
  public static byte[] takeThreadLocalBuf() {
    byte[] buf = threadLocalBuf.get();
    if (buf == null) {
      buf = new byte[THREAD_LOCAL_BUF_SIZE];
    } else {
      threadLocalBuf.set(null);
    }
    return buf;
  }

  public static Reader createReader(ProcessingEnvironment processingEnv, URL url)
      throws UnableToCompleteException {
    try {
      return new InputStreamReader(url.openStream());
    } catch (IOException e) {
      processingEnv
          .getMessager()
          .printMessage(Diagnostic.Kind.ERROR, "Unable to open resource: " + url);
      throw new UnableToCompleteException("Unable to open resource: " + url);
    }
  }

  /** Equality check through equals() that is also satisfied if both objects are null. */
  public static boolean equalsNullCheck(Object thisObject, Object thatObject) {
    if (thisObject == null) {
      return thatObject == null;
    }
    return thisObject.equals(thatObject);
  }

  /** Escapes '&', '<', '>', '"', and '\'' to their XML entity equivalents. */
  public static String escapeXml(String unescaped) {
    StringBuilder builder = new StringBuilder();
    escapeXml(unescaped, 0, unescaped.length(), true, builder);
    return builder.toString();
  }

  /**
   * Escapes '&', '<', '>', '"', and optionally ''' to their XML entity equivalents. The portion of
   * the input string between start (inclusive) and end (exclusive) is scanned. The output is
   * appended to the given StringBuilder.
   *
   * @param code the input String
   * @param start the first character position to scan.
   * @param end the character position following the last character to scan.
   * @param quoteApostrophe if true, the &apos; character is quoted as &amp;apos;
   * @param builder a StringBuilder to be appended with the output.
   */
  public static void escapeXml(
      String code, int start, int end, boolean quoteApostrophe, StringBuilder builder) {
    int lastIndex = 0;
    int len = end - start;
    char[] c = new char[len];

    code.getChars(start, end, c, 0);
    for (int i = 0; i < len; i++) {
      switch (c[i]) {
        case '&':
          builder.append(c, lastIndex, i - lastIndex);
          builder.append("&amp;");
          lastIndex = i + 1;
          break;
        case '>':
          builder.append(c, lastIndex, i - lastIndex);
          builder.append("&gt;");
          lastIndex = i + 1;
          break;
        case '<':
          builder.append(c, lastIndex, i - lastIndex);
          builder.append("&lt;");
          lastIndex = i + 1;
          break;
        case '\"':
          builder.append(c, lastIndex, i - lastIndex);
          builder.append("&quot;");
          lastIndex = i + 1;
          break;
        case '\'':
          if (quoteApostrophe) {
            builder.append(c, lastIndex, i - lastIndex);
            builder.append("&apos;");
            lastIndex = i + 1;
          }
          break;
        default:
          break;
      }
    }
    builder.append(c, lastIndex, len - lastIndex);
  }

  public static <T extends Serializable> T readFileAsObject(File file, Class<T> type)
      throws ClassNotFoundException, IOException {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
      return readStreamAsObject(fileInputStream, type);
    } finally {
      Utility.close(fileInputStream);
    }
  }

  public static <T> T readStreamAsObject(InputStream inputStream, Class<T> type)
      throws ClassNotFoundException, IOException {
    ObjectInputStream objectInputStream = null;
    try {
      objectInputStream = new StringInterningObjectInputStream(inputStream);
      return type.cast(objectInputStream.readObject());
    } finally {
      Utility.close(objectInputStream);
    }
  }

  public static URL findSourceInClassPath(ClassLoader cl, String sourceTypeName) {
    String toTry = sourceTypeName.replace('.', '/') + ".java";
    URL foundURL = cl.getResource(toTry);
    if (foundURL != null) {
      return foundURL;
    }
    int i = sourceTypeName.lastIndexOf('.');
    if (i != -1) {
      return findSourceInClassPath(cl, sourceTypeName.substring(0, i));
    } else {
      return null;
    }
  }

  /** Returns a byte-array representing the default encoding for a String. */
  public static byte[] getBytes(String s) {
    try {
      return s.getBytes(DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("The JVM does not support the compiler's default encoding.", e);
    }
  }

  /**
   * @param className A fully-qualified class name whose name you want.
   * @return The base name for the specified class.
   */
  public static String getClassName(String className) {
    return className.substring(className.lastIndexOf('.') + 1);
  }

  /**
   * Gets the contents of a file.
   *
   * @param relativePath relative path within the install directory
   * @return the contents of the file, or null if an error occurred
   */
  public static String getFileFromInstallPath(String relativePath) {
    String installPath = Utility.getInstallPath();
    File file = new File(installPath + '/' + relativePath);
    return readFileAsString(file);
  }

  public static String readFileAsString(File file) {
    byte[] bytes = readFileAsBytes(file);
    if (bytes != null) {
      return toString(bytes, DEFAULT_ENCODING);
    }

    return null;
  }

  public static byte[] readFileAsBytes(File file) {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
      int length = (int) file.length();
      return readBytesFromInputStream(fileInputStream, length);
    } catch (IOException e) {
      return null;
    } finally {
      Utility.close(fileInputStream);
    }
  }

  /**
   * Reads the specified number of bytes from the {@link InputStream}.
   *
   * @param byteLength number of bytes to read
   * @return byte array containing the bytes read or <code>null</code> if there is an {@link
   *     IOException} or if the requested number of bytes cannot be read from the {@link
   *     InputStream}
   */
  private static byte[] readBytesFromInputStream(InputStream input, int byteLength) {

    try {
      byte[] bytes = new byte[byteLength];
      int byteOffset = 0;
      while (byteOffset < byteLength) {
        int bytesReadCount = input.read(bytes, byteOffset, byteLength - byteOffset);
        if (bytesReadCount == -1) {
          return null;
        }

        byteOffset += bytesReadCount;
      }

      return bytes;
    } catch (IOException e) {
      // Ignored.
    }

    return null;
  }

  /**
   * Creates a string from the bytes using the specified character set name.
   *
   * @param bytes bytes to convert
   * @param charsetName the name of the character set to use
   * @return String for the given bytes and character set or <code>null</code> if the character set
   *     is not supported
   */
  private static String toString(byte[] bytes, String charsetName) {
    try {
      return new String(bytes, charsetName);
    } catch (UnsupportedEncodingException e) {
      // Ignored.
    }

    return null;
  }

  /**
   * @param qualifiedName A fully-qualified class name whose package name you want.
   * @return The package name for the specified class, empty string if default package.
   */
  public static String getPackageName(String qualifiedName) {
    int idx = qualifiedName.lastIndexOf('.');
    if (idx > 0) {
      return qualifiedName.substring(0, idx);
    }
    return "";
  }

  /** Reads an entire input stream as bytes. Closes the input stream. */
  public static byte[] readStreamAsBytes(InputStream in) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      copy(in, out);
      return out.toByteArray();
    } catch (IOException e) {
      return null;
    }
  }

  /** Reads an entire input stream as String. Closes the input stream. */
  public static String readStreamAsString(InputStream in) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      copy(in, out);
      return out.toString(DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("The JVM does not support the compiler's default encoding.", e);
    } catch (IOException e) {
      // TODO(zundel): Consider allowing this exception out. The pattern in this
      // file is to convert IOException to null, but in references to this
      // method, there are few places that check for null and do something sane,
      // the rest just throw an NPE and obscure the root cause.
      return null;
    }
  }

  /** @return null if the file could not be read */
  public static char[] readURLAsChars(URL url) {
    byte[] bytes = readURLAsBytes(url);
    if (bytes != null) {
      return toString(bytes, DEFAULT_ENCODING).toCharArray();
    }

    return null;
  }

  /** @return null if the file could not be read */
  public static byte[] readURLAsBytes(URL url) {
    try {
      URLConnection conn = url.openConnection();
      conn.setUseCaches(false);
      return readURLConnectionAsBytes(conn);
    } catch (IOException e) {
      return null;
    }
  }

  public static byte[] readURLConnectionAsBytes(URLConnection connection) {
    InputStream input = null;
    try {
      input = connection.getInputStream();
      int contentLength = connection.getContentLength();
      if (contentLength < 0) {
        return null;
      }
      return readBytesFromInputStream(input, contentLength);
    } catch (IOException e) {
      return null;
    } finally {
      Utility.close(input);
    }
  }

  /** @return null if the file could not be read */
  public static String readURLAsString(URL url) {
    byte[] bytes = readURLAsBytes(url);
    if (bytes != null) {
      return toString(bytes, DEFAULT_ENCODING);
    }
    return null;
  }

  /**
   * Deletes a file or recursively deletes a directory.
   *
   * @param file the file to delete, or if this is a directory, the directory that serves as the
   *     root of a recursive deletion
   * @param childrenOnly if <code>true</code>, only the children of a directory are recursively
   *     deleted but the specified directory itself is spared; if <code>false</code>, the specified
   *     directory is also deleted; ignored if <code>file</code> is not a directory
   */
  public static void recursiveDelete(File file, boolean childrenOnly) {
    recursiveDelete(file, childrenOnly, null);
  }

  /**
   * Selectively deletes a file or recursively deletes a directory. Note that it is possible that
   * files remain if file.delete() fails.
   *
   * @param file the file to delete, or if this is a directory, the directory that serves as the
   *     root of a recursive deletion
   * @param childrenOnly if <code>true</code>, only the children of a directory are recursively
   *     deleted but the specified directory itself is spared; if <code>false</code>, the specified
   *     directory is also deleted; ignored if <code>file</code> is not a directory
   * @param filter only files matching this filter will be deleted
   */
  public static void recursiveDelete(File file, boolean childrenOnly, FileFilter filter) {
    if (file.isDirectory()) {
      File[] children = file.listFiles();
      if (children != null) {
        for (int i = 0; i < children.length; i++) {
          recursiveDelete(children[i], false, filter);
        }
      }
      if (childrenOnly) {
        // Do not delete the specified directory itself.
        return;
      }
    }

    if (filter == null || filter.accept(file)) {
      file.delete();
    }
  }

  /**
   * Remove leading file:jar:...!/ prefix from source paths for source located in jars.
   *
   * @param absolutePath an absolute JAR file URL path
   * @return the location of the file within the JAR
   */
  public static String stripJarPathPrefix(String absolutePath) {
    if (absolutePath != null) {
      int bang = absolutePath.lastIndexOf('!');
      if (bang != -1) {
        return absolutePath.substring(bang + 2);
      }
    }
    return absolutePath;
  }

  /**
   * Creates an array from a collection of the specified component type and size. You can definitely
   * downcast the result to T[] if T is the specified component type.
   *
   * <p>Class<? super T> is used to allow creation of generic types, such as Map.Entry<K,V> since we
   * can only pass in Map.Entry.class.
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Class<? super T> componentType, Collection<? extends T> coll) {
    int n = coll.size();
    T[] a = (T[]) Array.newInstance(componentType, n);
    return coll.toArray(a);
  }

  /**
   * Returns a String representing the character content of the bytes; the bytes must be encoded
   * using the compiler's default encoding.
   */
  public static String toString(byte[] bytes) {
    return toString(bytes, DEFAULT_ENCODING);
  }

  /**
   * Attempts to find the canonical form of a file path.
   *
   * @return the canonical version of the file path, if it could be computed; otherwise, the
   *     original file is returned unmodified
   */
  public static File tryMakeCanonical(File file) {
    try {
      return file.getCanonicalFile();
    } catch (IOException e) {
      return file;
    }
  }

  public static void writeBytesToFile(TreeLogger logger, File where, byte[] what)
      throws UnableToCompleteException {
    writeBytesToFile(logger, where, new byte[][] {what});
  }

  /** Gathering write. */
  public static void writeBytesToFile(TreeLogger logger, File where, byte[][] what)
      throws UnableToCompleteException {
    FileOutputStream f = null;
    Throwable caught;
    try {
      // No need to check mkdirs result because an IOException will occur anyway
      where.getParentFile().mkdirs();
      f = new FileOutputStream(where);
      for (int i = 0; i < what.length; i++) {
        f.write(what[i]);
      }
      return;
    } catch (FileNotFoundException e) {
      caught = e;
    } catch (IOException e) {
      caught = e;
    } finally {
      Utility.close(f);
    }
    logger.log(TreeLogger.Type.ERROR, "Unable to write file '" + where + "'");
    throw new UnableToCompleteException();
  }

  /** Serializes an object and writes it to a file. */
  public static void writeObjectAsFile(
      ProcessingEnvironment processingEnv, File file, Object... objects)
      throws UnableToCompleteException {
    FileOutputStream stream = null;
    try {
      // No need to check mkdirs result because an IOException will occur anyway
      file.getParentFile().mkdirs();
      stream = new FileOutputStream(file);
      writeObjectToStream(stream, objects);
    } catch (IOException e) {
      processingEnv
          .getMessager()
          .printMessage(Diagnostic.Kind.ERROR, "Unable to write file: " + file.getAbsolutePath());
      throw new UnableToCompleteException("Unable to write file: " + file.getAbsolutePath());
    } finally {
      Utility.close(stream);
    }
  }

  /** Serializes an object and writes it to a stream. */
  public static void writeObjectToStream(OutputStream stream, Object... objects)
      throws IOException {
    ObjectOutputStream objectStream = null;
    objectStream = new ObjectOutputStream(stream);
    for (Object object : objects) {
      objectStream.writeObject(object);
    }
    objectStream.flush();
  }

  public static boolean writeStringAsFile(File file, String string) {
    FileOutputStream stream = null;
    OutputStreamWriter writer = null;
    BufferedWriter buffered = null;
    try {
      // No need to check mkdirs result because an IOException will occur anyway
      file.getParentFile().mkdirs();
      stream = new FileOutputStream(file);
      writer = new OutputStreamWriter(stream, DEFAULT_ENCODING);
      buffered = new BufferedWriter(writer);
      buffered.write(string);
    } catch (IOException e) {
      return false;
    } finally {
      Utility.close(buffered);
      Utility.close(writer);
      Utility.close(stream);
    }
    return true;
  }

  public static void writeStringAsFile(
      ProcessingEnvironment processingEnv, File file, String string)
      throws UnableToCompleteException {
    FileOutputStream stream = null;
    OutputStreamWriter writer = null;
    BufferedWriter buffered = null;
    try {
      stream = new FileOutputStream(file);
      writer = new OutputStreamWriter(stream, DEFAULT_ENCODING);
      buffered = new BufferedWriter(writer);
      // No need to check mkdirs result because an IOException will occur anyway
      file.getParentFile().mkdirs();
      buffered.write(string);
    } catch (IOException e) {
      processingEnv
          .getMessager()
          .printMessage(Diagnostic.Kind.ERROR, "Unable to write file: " + file.getAbsolutePath());
      throw new UnableToCompleteException("Unable to write file: " + file.getAbsolutePath());
    } finally {
      Utility.close(buffered);
      Utility.close(writer);
      Utility.close(stream);
    }
  }

  /**
   * Writes the contents of a StringBuilder to an OutputStream, encoding each character using the
   * UTF-* encoding. Unicode characters between U+0000 and U+10FFFF are supported.
   */
  public static void writeUtf8(StringBuilder builder, OutputStream out) throws IOException {
    // Rolling our own converter avoids the following:
    //
    // o Instantiating the entire builder as a String
    // o Creating CharEncoders and NIO buffer
    // o Passing through an OutputStreamWriter

    int buflen = 1024;
    char[] inBuf = new char[buflen];
    byte[] outBuf = new byte[4 * buflen];

    int length = builder.length();
    int start = 0;

    while (start < length) {
      int end = Math.min(start + buflen, length);
      builder.getChars(start, end, inBuf, 0);

      int index = 0;
      int len = end - start;
      for (int i = 0; i < len; i++) {
        int c = inBuf[i] & 0xffff;
        if (c < 0x80) {
          outBuf[index++] = (byte) c;
        } else if (c < 0x800) {
          int y = c >> 8;
          int x = c & 0xff;
          outBuf[index++] = (byte) (0xc0 | (y << 2) | (x >> 6)); // 110yyyxx
          outBuf[index++] = (byte) (0x80 | (x & 0x3f)); // 10xxxxxx
        } else if (c < 0xD800 || c > 0xDFFF) {
          int y = (c >> 8) & 0xff;
          int x = c & 0xff;
          outBuf[index++] = (byte) (0xe0 | (y >> 4)); // 1110yyyy
          outBuf[index++] = (byte) (0x80 | ((y << 2) & 0x3c) | (x >> 6)); // 10yyyyxx
          outBuf[index++] = (byte) (0x80 | (x & 0x3f)); // 10xxxxxx
        } else {
          // Ignore if no second character (which is not be legal unicode)
          if (i + 1 < len) {
            int hi = c & 0x3ff;
            int lo = inBuf[i + 1] & 0x3ff;

            int full = 0x10000 + ((hi << 10) | lo);
            int z = (full >> 16) & 0xff;
            int y = (full >> 8) & 0xff;
            int x = full & 0xff;

            outBuf[index++] = (byte) (0xf0 | (z >> 5));
            outBuf[index++] = (byte) (0x80 | ((z << 4) & 0x30) | (y >> 4));
            outBuf[index++] = (byte) (0x80 | ((y << 2) & 0x3c) | (x >> 6));
            outBuf[index++] = (byte) (0x80 | (x & 0x3f));

            i++; // char has been consumed
          }
        }
      }
      out.write(outBuf, 0, index);
      start = end;
    }
  }

  private static void writeAttribute(PrintWriter w, Attr attr, int depth) throws IOException {
    w.write(attr.getName());
    w.write('=');
    Node c = attr.getFirstChild();
    while (c != null) {
      w.write('"');
      writeNode(w, c, depth);
      w.write('"');
      c = c.getNextSibling();
    }
  }

  private static void writeDocument(PrintWriter w, Document d) throws IOException {
    w.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    Node c = d.getFirstChild();
    while (c != null) {
      writeNode(w, c, 0);
      c = c.getNextSibling();
    }
  }

  private static void writeElement(PrintWriter w, Element el, int depth) throws IOException {
    String tagName = el.getTagName();

    writeIndent(w, depth);
    w.write('<');
    w.write(tagName);
    NamedNodeMap attrs = el.getAttributes();
    for (int i = 0, n = attrs.getLength(); i < n; ++i) {
      w.write(' ');
      writeNode(w, attrs.item(i), depth);
    }

    Node c = el.getFirstChild();
    if (c != null) {
      // There is at least one child.
      //
      w.println('>');

      // Write the children.
      //
      while (c != null) {
        writeNode(w, c, depth + 1);
        w.println();
        c = c.getNextSibling();
      }

      // Write the closing tag.
      //
      writeIndent(w, depth);
      w.write("</");
      w.write(tagName);
      w.print('>');
    } else {
      // There are no children, so just write the short form close.
      //
      w.print("/>");
    }
  }

  private static void writeIndent(PrintWriter w, int depth) {
    for (int i = 0; i < depth; ++i) {
      w.write('\t');
    }
  }

  private static void writeNode(PrintWriter w, Node node, int depth) throws IOException {
    short nodeType = node.getNodeType();
    switch (nodeType) {
      case Node.ELEMENT_NODE:
        writeElement(w, (Element) node, depth);
        break;
      case Node.ATTRIBUTE_NODE:
        writeAttribute(w, (Attr) node, depth);
        break;
      case Node.DOCUMENT_NODE:
        writeDocument(w, (Document) node);
        break;
      case Node.TEXT_NODE:
        writeText(w, (Text) node);
        break;

      case Node.COMMENT_NODE:
      case Node.CDATA_SECTION_NODE:
      case Node.ENTITY_REFERENCE_NODE:
      case Node.ENTITY_NODE:
      case Node.PROCESSING_INSTRUCTION_NODE:
      default:
        throw new RuntimeException("Unsupported DOM node type: " + nodeType);
    }
  }

  private static void writeText(PrintWriter w, Text text) throws DOMException {
    String nodeValue = text.getNodeValue();
    String escaped = escapeXml(nodeValue);
    w.write(escaped);
  }

  public static byte[] readByteArrayFromResource(Resource resource)
      throws IOException, UnableToCompleteException {
    return org.apache.commons.io.IOUtils.toByteArray(resource.openContents());
  }

  public static boolean isValidJavaIdent(String token) {
    if (token.length() == 0) {
      return false;
    }

    if (!Character.isJavaIdentifierStart(token.charAt(0))) {
      return false;
    }

    for (int i = 1, n = token.length(); i < n; i++) {
      if (!Character.isJavaIdentifierPart(token.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  public static String getQualifiedSourceName(
      javax.lang.model.element.Element clazz, Elements elements) {
    if (isClassOrInterface(clazz)) {
      return getPackageName(clazz, elements) + "." + getEnclosingClassName(clazz, elements);
    } else {
      throw new Error("Unable to determine QualifiedSourceName " + clazz);
    }
  }

  protected static String getEnclosingClassName(
      javax.lang.model.element.Element clazz, Elements elements) {
    return clazz.toString().replace(getPackageName(clazz, elements) + ".", "");
  }

  protected static String getPackageName(
      javax.lang.model.element.Element clazz, Elements elements) {
    PackageElement pkg = elements.getPackageOf(clazz);
    return pkg.getQualifiedName().toString();
  }

  public static boolean isClassOrInterface(javax.lang.model.element.Element clazz) {
    return clazz.getKind().isClass() || clazz.getKind().isInterface();
  }
}
