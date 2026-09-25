package com.samjsoares.soar.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.panforge.robotstxt.RobotsTxt;
import com.samjsoares.soar.Fixtures;
import com.samjsoares.soar.util.UrlUtil;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class RobotsHandlerTest {

  /** Serves robots.txt files from fixtures instead of downloading them. */
  private static class FixtureRobotsHandler extends RobotsHandler {
    private final Map<String, RobotsTxt> robotsByHost = new HashMap<>();

    FixtureRobotsHandler with(String host, String fixture) {
      robotsByHost.put(host, Fixtures.robots(fixture));
      return this;
    }

    @Override
    public RobotsTxt add(URL url) {
      return url == null ? null : robotsByHost.get(url.getHost());
    }
  }

  private final RobotsHandler robotsHandler =
      new FixtureRobotsHandler()
          .with("example.com", "disallow-private.txt")
          .with("closed.example.com", "disallow-all.txt");

  @Test
  public void testAllowedPath() {
    assertTrue(robotsHandler.isAllowed(UrlUtil.getCleanUrl("http://example.com/public/page")));
  }

  @Test
  public void testDisallowedPath() {
    assertFalse(robotsHandler.isAllowed(UrlUtil.getCleanUrl("http://example.com/private/page")));
  }

  @Test
  public void testDisallowAll() {
    assertFalse(robotsHandler.isAllowed(UrlUtil.getCleanUrl("http://closed.example.com/home")));
  }

  @Test
  public void testNoRobotsTxtIsAllowed() {
    assertTrue(robotsHandler.isAllowed(UrlUtil.getCleanUrl("http://norobots.example.com/any")));
  }

  @Test
  public void testNullUrlIsNotAllowed() {
    assertFalse(robotsHandler.isAllowed(null));
  }

  @Test
  public void testAddNullUrlReturnsNull() {
    assertTrue(new RobotsHandler().add(null) == null);
  }

  @Test
  public void testRobotsTxtRules() {
    RobotsTxt robotsTxt = Fixtures.robots("disallow-private.txt");

    assertTrue(robotsTxt.query(null, "/"));
    assertTrue(robotsTxt.query(null, "/public/page"));
    assertFalse(robotsTxt.query(null, "/private/"));
    assertFalse(robotsTxt.query(null, "/private/page"));
  }
}
