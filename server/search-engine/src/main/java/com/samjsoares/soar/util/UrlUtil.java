package com.samjsoares.soar.util;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class UrlUtil {
  private static final String VALID_CONTENT_TYPES_PATTERN =
      "(text/.*)|(application\\/xml)|(application\\/xhtml\\+xml)";
  // A Pattern is thread-safe; a Matcher is not, so each call makes its own.
  private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile(VALID_CONTENT_TYPES_PATTERN);

  private static final String ROBOTS_TXT_PATH = "/robots.txt";

  /**
   * Returns a key that identifies a page regardless of scheme, query, or fragment, so the http and
   * https versions of a page share a key. Returns null for an invalid URL.
   */
  public static String getUrlKey(String urlString) {
    return getUrlKey(getCleanUrl(urlString));
  }

  /** Same as {@link #getUrlKey(String)} for an already-parsed URL. */
  public static String getUrlKey(URL url) {
    return url != null ? url.getHost() + url.getPath() : null;
  }

  public static String getUrlString(String url) {
    URI uri = getUri(url);

    if (uri == null) {
      return null;
    }

    return uri.toString();
  }

  /**
   * Returns the cleaned URL followed by the same URL with the other scheme, for an http or https
   * URL. Returns just the cleaned URL for other schemes, and an empty list for an invalid URL.
   */
  public static List<String> getHttpAndHttpsUrlStrings(String urlString) {
    String url = getUrlString(urlString);
    if (url == null) {
      return Collections.emptyList();
    }
    if (url.startsWith("https://")) {
      return Arrays.asList(url, "http://" + url.substring("https://".length()));
    }
    if (url.startsWith("http://")) {
      return Arrays.asList(url, "https://" + url.substring("http://".length()));
    }
    return Collections.singletonList(url);
  }

  private static java.net.URI getUri(String url) {
    try {
      java.net.URL javaUrl = getCleanUrl(url);
      if (javaUrl != null) {
        return javaUrl.toURI();
      }
    } catch (Exception e) {
    }

    return null;
  }

  public static java.net.URL getCleanUrl(String urlString) {
    try {
      io.mola.galimatias.URL url = io.mola.galimatias.URL.parse(cleanUpUrl(urlString));
      url = url.withFragment(null);
      url = url.withQuery(null);
      return url.toJavaURL();
    } catch (Exception e) {
      return null;
    }
  }

  private static String cleanUpUrl(String url) {
    // replace spaces with html code %20
    url = StringUtils.replaceAll(url, "\\s", "%20");

    // default to https when no scheme is given
    if (!StringUtils.contains(url, "://")) {
      url = "https://" + url;
    }

    return url;
  }

  private static boolean shouldContainHttp(String url) {
    if (StringUtils.isBlank(url)) {
      return false;
    }

    return !StringUtils.startsWith(url, "http") && !StringUtils.contains(url, "://");
  }

  public static java.net.URL getRobotsTxtURL(java.net.URL url) {
    try {
      return new java.net.URL(
          url.getProtocol(), url.getAuthority(), url.getPort(), ROBOTS_TXT_PATH);
    } catch (Exception e) {
      // System.out.println("Failed to get robots.txt URL: " + e.toString());
    }

    return null;
  }

  public static boolean isValidContentType(String contentType) {
    contentType = StringUtils.substringBefore(contentType, ";");
    return CONTENT_TYPE_PATTERN.matcher(contentType).matches();
  }
}
