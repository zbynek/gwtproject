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
package org.gwtproject.media.client;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.gwtproject.dom.client.MediaElement;
import org.gwtproject.media.dom.client.MediaError;
import org.gwtproject.timer.client.Timer;
import org.gwtproject.user.client.ui.RootPanel;
import org.junit.After;
import org.junit.Before;

/**
 * Tests {@link org.gwtproject.media.client.Video}.
 *
 * <p>Because HtmlUnit does not support HTML5, you will need to run these tests manually in order to
 * have them run. To do that, go to "run configurations" or "debug configurations", select the test
 * you would like to run, and put this line in the VM args under the arguments tab:
 * -Dgwt.args="-runStyle Manual:1"
 */
@J2clTestInput(VideoTest.class)
public class VideoTest extends MediaTest {
  protected org.gwtproject.media.client.Video video;

  static final String posterUrl = "poster.jpg";
  static final String videoUrlH264 = "smallh264.mp4";
  static final String videoFormatH264 = "video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\"";
  static final String videoUrlOgv = "smalltheora.ogv";
  static final String videoFormatOgv = "video/ogg; codecs=\"theora, vorbis\"";

  static final int videoWidth = 64;
  static final int videoHeight = 36;

  @Override
  public MediaBase getMedia() {
    return video;
  }

  public void testPoster() {
    if (video == null) {
      return; // don't continue if not supported
    }

    video.setPoster(posterUrl);
    String poster = video.getPoster();
    assertEquals(posterUrl, poster.substring(poster.lastIndexOf('/') + 1));
  }

  public void testSize() {
    if (video == null) {
      return; // don't continue if not supported
    }

    int width = 100;
    int height = 200;
    video.setWidth(width + "px");
    video.setHeight(height + "px");
    assertEquals(width, video.getOffsetWidth());
    assertEquals(height, video.getOffsetHeight());
  }

  // test that the deprecated src constructor works
  public void testSrcConstructor() {
    if (video == null) {
      return; // don't continue if not supported
    }

    org.gwtproject.media.client.Video video =
        new org.gwtproject.media.client.Video("http://google.com/video");
    assertNotNull(video);
    assertEquals("http://google.com/video", video.getSrc());
    video.setSrc("");
    video.load();
    RootPanel.get().remove(video);
  }

  public void testVideoSize() {
    if (video == null) {
      return; // don't continue if not supported
    }

    // the media resource needs time to load
    delayTestFinish(20 * 1000);

    // wait a little, then make sure it loaded
    new Timer() {
      @Override
      public void run() {
        MediaError error = video.getError();
        if (error != null) {
          fail("Media error (" + error.getCode() + ")");
        }
        assertEquals(videoWidth, video.getVideoWidth());
        assertEquals(videoHeight, video.getVideoHeight());
        // finishTest();
      }
    }.schedule(15 * 1000);

    video.play();
  }

  @Override
  public String getModuleName() {
    return "org.gwtproject.media.MediaTest";
  }

  @Before
  protected void gwtSetUp() throws Exception {
    video = Video.createIfSupported();

    if (video == null) {
      return; // don't continue if not supported
    }

    String canPlayH264 = video.canPlayType(videoFormatH264);
    String canPlayOgv = video.canPlayType(videoFormatOgv);
    if (canPlayH264.equals(MediaElement.CAN_PLAY_PROBABLY)) {
      video.setSrc(videoUrlH264);
    } else if (canPlayOgv.equals(MediaElement.CAN_PLAY_PROBABLY)) {
      video.setSrc(videoUrlOgv);
    } else if (canPlayH264.equals(MediaElement.CAN_PLAY_MAYBE)) {
      video.setSrc(videoUrlH264);
    } else if (canPlayOgv.equals(MediaElement.CAN_PLAY_MAYBE)) {
      video.setSrc(videoUrlOgv);
    } else {
      throw new Exception("Could not find suitable video format");
    }

    // allow tests to autoplay audio without user interaction in browsers with
    // strict autoplay policies, e.g. Chrome
    video.setMuted(true);

    RootPanel.get().add(video);
  }

  @After
  protected void gwtTearDown() throws Exception {
    if (video == null) {
      return; // don't continue if not supported
    }

    // clean up
    video.pause();
    video.setSrc("");
    video.load();
    RootPanel.get().remove(video);
  }
}
