package com.samjsoares.soar.util;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
  private final static String VALID_CONTENT_TYPES_PATTERN
          = "(text/.*)|(application\\/xml)|(application\\/xhtml\\+xml)";
  private final static Matcher CONTENT_TYPE_MATCHER = Pattern.compile(VALID_CONTENT_TYPES_PATTERN).matcher("");

  private final static String ROBOTS_TXT_PATH = "/robots.txt";

  public static String getUrlKey(String urlString) {
    try {
      URL url = getCleanUrl(urlString);
      if (url != null) {
        return url.getHost() + url.getPath();
      }
    } catch (Exception e) {}

    return null;
  }

  public static String getUrlString(String url) {
    URI uri = getUri(url);

    if (uri == null) {
      return null;
    }

    return uri.toString();
  }

  private static java.net.URI getUri (String url) {
    try {
      java.net.URL javaUrl = getCleanUrl(url);
      if (javaUrl != null) {
        return javaUrl.toURI();
      }
    } catch (Exception e) {}

    return null;
  }

  public static java.net.URL getCleanUrl (String urlString) {
    try {
      io.mola.galimatias.URL url = io.mola.galimatias.URL.parse(cleanUpUrl(urlString));
      url = url.withFragment(null);
      url = url.withQuery(null);
      return url.toJavaURL();
    } catch (Exception e) {
      return null;
    }
  }

  private static String cleanUpUrl (String url) {
    // replace spaces with html code %20
    url = StringUtils.replaceAll(url, "\\s", "%20");

    // force http
    if (StringUtils.startsWith(url, "https://")) {
      url = StringUtils.replaceFirst(url, "https://", "http://");
    }

    // verify http protocol is used
    if (!StringUtils.contains(url, "://")) {
      url = "http://" + url;
    }

    return url;
  }

  private static boolean shouldContainHttp (String url) {
    if (StringUtils.isBlank(url)) {
      return false;
    }

    return !StringUtils.startsWith(url,"http") && !StringUtils.contains(url, "://");
  }

  public static java.net.URL getRobotsTxtURL (java.net.URL url) {
    try {
        return new java.net.URL(url.getProtocol(), url.getAuthority(), url.getPort(), ROBOTS_TXT_PATH);
    } catch (Exception e) {
        //System.out.println("Failed to get robots.txt URL: " + e.toString());
    }

    return null;
  }

  public static boolean isValidContentType (String contentType) {
    contentType = StringUtils.substringBefore(contentType, ";");
    return CONTENT_TYPE_MATCHER.reset(contentType).matches();
  }
}
