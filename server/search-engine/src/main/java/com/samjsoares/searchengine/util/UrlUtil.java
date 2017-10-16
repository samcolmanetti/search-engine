package com.samjsoares.searchengine.util;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

  private final static String VALID_CONTENT_TYPES_PATTERN
          = "(text/.*)|(application\\/xml)|(application\\/xhtml\\+xml)";
  private final static Matcher CONTENT_TYPE_MATCHER = Pattern.compile(VALID_CONTENT_TYPES_PATTERN).matcher("");
  private final static String ROBOTS_TXT_PATH = "/robots.txt";

  public static String getUrlString(String url) {
    try {
        URI uri = new URI(url);
        return new URI(
                uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                null,
                null).toString();
    } catch (Exception e) {
        return null;
    }
  }

  public static URI getUri (String url) {
    try {
        url = cleanUpUrl(url);
        URI uri = new URI(url);
        return new URI(
                uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                null,
                null);
    } catch (URISyntaxException e) {
        System.out.printf("Failed to create URI for\'%s\': %s\n", url, e.toString());
        return null;
    }
  }

  private static String cleanUpUrl (String url) {
    // strip query from url
    url = StringUtils.substringBefore(url, "?");

    // replace spaces with html code %20
    url = StringUtils.replaceAll(url, "\\s", "%20");

    return url;
  }

  public static URL getCleanUrl (String url) {
    try {
      if (!StringUtils.startsWith(url,"http")) {
        return null;
      }

      URI uri = getUri(url);
      if (uri != null) {
          return uri.toURL();
      }
    } catch (MalformedURLException e) {
        System.out.printf("Malformed URL: %s \n %s", url, e.toString());
    }

    return null;
  }

  public static URL getRobotsTxtURL (URL url) {
    try {
        return new URL(url.getProtocol(), url.getAuthority(), url.getPort(), ROBOTS_TXT_PATH);
    } catch (MalformedURLException e) {
        System.out.println("Failed to get robots.txt URL: " + e.toString());
    }

    return null;
  }

  public static boolean isValidContentType (String contentType) {
    return CONTENT_TYPE_MATCHER.reset(contentType).matches();
  }
}
