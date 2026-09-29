package com.samjsoares.soar.core;

import com.panforge.robotstxt.RobotsTxt;
import com.samjsoares.soar.core.datastructure.LruCacheMap;
import com.samjsoares.soar.util.UrlUtil;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Decides whether URLs may be crawled, fetching each host's robots.txt once and caching it for up
 * to 128 hosts.
 */
@Component
public class RobotsHandler {

  private static final int CACHE_LIMIT = 128;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Map<String, RobotsTxt> map = new LruCacheMap<>(CACHE_LIMIT);

  /** Creates a handler with an empty cache. */
  public RobotsHandler() {}

  /**
   * Returns the robots.txt for the host of {@code url}, fetching and caching it if it is not
   * cached. Returns null if {@code url} is null or the host has no usable robots.txt.
   */
  public RobotsTxt add(URL url) {
    if (url == null) {
      return null;
    }

    String key = getKey(url);

    if (contains(key)) {
      return get(key);
    }

    URL robotsUrl = UrlUtil.getRobotsTxtUrl(url);
    if (robotsUrl == null) {
      return null;
    }

    try (InputStream inputStream = robotsUrl.openStream()) {
      RobotsTxt txt = RobotsTxt.read(inputStream);
      map.put(key, txt);
      return txt;
    } catch (Exception exception) {
      logger.info("No usable robots.txt at {}: {}", robotsUrl, exception.toString());
      map.put(key, null);
    }

    return null;
  }

  private String getKey(URL url) {
    return url.getHost();
  }

  private RobotsTxt get(String key) {
    return map.get(key);
  }

  private RobotsTxt get(URL url) {
    return map.get(getKey(url));
  }

  private boolean contains(URL url) {
    return map.containsKey(getKey(url));
  }

  private boolean contains(String key) {
    return map.containsKey(key);
  }

  /**
   * Returns whether robots.txt allows crawling the path of {@code url}. URLs on hosts without a
   * usable robots.txt are allowed; a null URL or a failed query is not.
   */
  public boolean isAllowed(URL url) {
    if (url == null) {
      return false;
    }

    RobotsTxt robotsTxt = contains(url) ? get(url) : add(url);

    // robots.txt does not exist for domain - return true
    if (robotsTxt == null) {
      return true;
    }

    try {
      return robotsTxt.query(null, url.getPath());
    } catch (Exception e) {
      logger.warn("robots.txt query failed for {}: {}", url, e.toString());
      return false;
    }
  }
}
