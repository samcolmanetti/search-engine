package com.samjsoares.soar.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.samjsoares.soar.TestConfig;
import com.samjsoares.soar.util.UrlUtil;
import java.net.URL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class RobotsHandlerTester {

  @Autowired private RobotsHandler robotsHandler;

  @Test
  public void testGoogleValidPath() {
    String urlStr = "http://www.google.com";
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
