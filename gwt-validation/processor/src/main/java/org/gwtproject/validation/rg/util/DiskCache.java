/*
 * Copyright 2009 Google Inc.
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
package org.gwtproject.validation.rg.util;

import static org.gwtproject.validation.rg.util.Util.DEFAULT_ENCODING;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 * A nifty class that lets you squirrel away data on the file system. Write once, read many times.
 * Instance of this are thread-safe by way of internal synchronization.
 *
 * <p>Note that in the current implementation, the backing temp file will get arbitrarily large as
 * you continue adding things to it. There is no internal GC or compaction.
 */
public class DiskCache {
  /**
   * For future thought: if we used Object tokens instead of longs, we could actually track
   * references and do GC/compaction on the underlying file.
   *
   * <p>I considered using memory mapping, but I didn't see any obvious way to make the map larger
   * after the fact, which kind of defeats the infinite-append design. At any rate, I measured the
   * current performance of this design to be so fast relative to what I'm using it for, I didn't
   * pursue this further.
   */

  /** A global client Disk cache. */
  public static DiskCache INSTANCE = new DiskCache();

  private boolean atEnd = true;
  private RandomAccessFile file;

  private DiskCache() {
    try {
      File temp = File.createTempFile("gwt", "byte-cache");
      temp.deleteOnExit();
      file = new RandomAccessFile(temp, "rw");
      file.setLength(0);
    } catch (IOException e) {
      throw new RuntimeException("Unable to initialize byte cache", e);
    }
  }

  /**
   * Retrieve the underlying bytes.
   *
   * @param token a previously returned token
   * @return the bytes that were written
   */
  public synchronized byte[] readByteArray(long token) {
    try {
      atEnd = false;
      file.seek(token);
      int length = file.readInt();
      byte[] result = new byte[length];
      file.readFully(result);
      return result;
    } catch (IOException e) {
      throw new RuntimeException("Unable to read from byte cache", e);
    }
  }

  /** Helper that ignores exceptions during close, because what are you going to do? */
  public static void close(AutoCloseable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (Exception e) {
    }
  }

  public static <T> T readStreamAsObject(InputStream inputStream, Class<T> type)
      throws ClassNotFoundException, IOException {
    ObjectInputStream objectInputStream = null;
    try {
      objectInputStream = new StringInterningObjectInputStream(inputStream);
      return type.cast(objectInputStream.readObject());
    } finally {
      close(objectInputStream);
    }
  }

  /**
   * Deserialize the underlying bytes as an object.
   *
   * @param <T> the type of the object to deserialize
   * @param token a previously returned token
   * @param type the type of the object to deserialize
   * @return the deserialized object
   */
  public <T> T readObject(long token, Class<T> type) {
    try {
      byte[] bytes = readByteArray(token);
      ByteArrayInputStream in = new ByteArrayInputStream(bytes);
      return readStreamAsObject(in, type);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Unexpected exception deserializing from disk cache", e);
    } catch (IOException e) {
      throw new RuntimeException("Unexpected exception deserializing from disk cache", e);
    }
  }

  /**
   * Read the underlying bytes as a String.
   *
   * @param token a previously returned token
   * @return the String that was written
   */
  public String readString(long token) {
    return Util.toString(readByteArray(token));
  }

  /**
   * Write the rest of the data in an input stream to disk. Note: this method does not close the
   * InputStream.
   *
   * @param in open stream containing the data to write to the disk cache.
   * @return a token to retrieve the data later
   */
  public synchronized long transferFromStream(InputStream in) throws IOException {
    assert in != null;
    byte[] buf = Util.takeThreadLocalBuf();
    try {
      long position = moveToEndPosition();

      // Placeholder, we don't know the length yet.
      file.writeInt(-1);

      // Transfer all the bytes.
      int length = 0;
      int bytesRead;
      while ((bytesRead = in.read(buf)) != -1) {
        file.write(buf, 0, bytesRead);
        length += bytesRead;
      }

      // Now go back and fill in the length.
      file.seek(position);
      file.writeInt(length);
      // Don't eagerly seek the end, the next operation might be a read.
      atEnd = false;
      return position;
    } finally {
      Util.releaseThreadLocalBuf(buf);
    }
  }

  /**
   * Writes the underlying bytes into the specified output stream.
   *
   * @param token a previously returned token
   * @param out the stream to write into
   */
  public synchronized void transferToStream(long token, OutputStream out) throws IOException {
    byte[] buf = Util.takeThreadLocalBuf();
    try {
      atEnd = false;
      file.seek(token);
      int length = file.readInt();
      int bufLen = buf.length;
      while (length > bufLen) {
        int read = file.read(buf, 0, bufLen);
        length -= read;
        out.write(buf, 0, read);
      }
      while (length > 0) {
        int read = file.read(buf, 0, length);
        length -= read;
        out.write(buf, 0, read);
      }
    } finally {
      Util.releaseThreadLocalBuf(buf);
    }
  }

  /**
   * Write a byte array to disk.
   *
   * @return a token to retrieve the data later
   */
  public synchronized long writeByteArray(byte[] bytes) {
    try {
      long position = moveToEndPosition();
      file.writeInt(bytes.length);
      file.write(bytes);
      return position;
    } catch (IOException e) {
      throw new RuntimeException("Unable to write to byte cache", e);
    }
  }

  /**
   * Serialize an Object to disk.
   *
   * @return a token to retrieve the data later
   */
  public long writeObject(Object object) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      writeObjectToStream(out, object);
      return writeByteArray(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("Unexpected IOException on in-memory stream", e);
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

  /**
   * Write a String to disk as bytes.
   *
   * @return a token to retrieve the data later
   */
  public long writeString(String str) {
    return writeByteArray(getBytes(str));
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
   * Moves to the end of the file if necessary and returns the offset position. Caller must
   * synchronize.
   *
   * @return the offset position of the end of the file
   * @throws IOException
   */
  private long moveToEndPosition() throws IOException {
    // Get an end pointer.
    if (atEnd) {
      return file.getFilePointer();
    } else {
      long position = file.length();
      file.seek(position);
      atEnd = true;
      return position;
    }
  }
}
