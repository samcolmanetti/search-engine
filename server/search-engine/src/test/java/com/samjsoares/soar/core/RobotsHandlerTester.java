package com.samjsoares.soar.core;

import com.samjsoares.soar.util.UrlUtil;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RobotsHandlerTester {

  private RobotsHandler robotsHandler;

  @Before
  public void setUp() {
    robotsHandler = new RobotsHandler();
  }

  @Test
  public void testGoogleValidPath() {
    String urlStr = "http://google.com";
    URL url = UrlUtil.getCleanUrl(urlStr);
    boolean actual = robotsHandler.isAllowed(url);
    assertTrue(actual);
  }

  @Test
  public void testFacebookInvalidPath() {
    String urlStr = "https://facebook.com/home";
    URL url = UrlUtil.getCleanUrl(urlStr);
    boolean actual = robotsHandler.isAllowed(url);
    assertFalse(actual);
  }

}
