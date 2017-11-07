package com.samjsoares.soar.core;

import com.panforge.robotstxt.RobotsTxt;
import com.samjsoares.soar.core.datastructure.LRUCacheMap;
import com.samjsoares.soar.util.UrlUtil;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Component
public class RobotsHandler {

  private final static int CACHE_LIMIT = 128;

  private Map<String, RobotsTxt> map = new LRUCacheMap<>(CACHE_LIMIT);

  public RobotsHandler() {}

  public RobotsTxt add(URL url) {
    if (url == null) {
      return null;
    }

    String key = getKey(url);

    if (contains(key)){
      return get(key);
    }

    URL robotsUrl = UrlUtil.getRobotsTxtURL(url);
    if (robotsUrl == null) {
      return null;
    }

    try (InputStream inputStream = robotsUrl.openStream()) {
        RobotsTxt txt = RobotsTxt.read(inputStream);
        map.put(key, txt);
        return txt;
    } catch (Exception exception) {
        System.out.printf("Failed to find or parse RobotsTxt: %s\n", exception.toString());
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

  public boolean isAllowed(URL url) {
    if (url == null) {
      return false;
    }

    RobotsTxt robotsTxt = contains(url)
            ? get(url)
            : add(url);


    // robots.txt does not exist for domain - return true
    if (robotsTxt == null) {
        return true;
    }

    try {
      return robotsTxt.query(null, url.getPath());
    } catch (Exception e) {
      System.out.println("RobotsTxt query failed: " + e.toString());
      return false;
    }

  }

}
