package com.samjsoares.soar.util;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

  private final static String VALID_CONTENT_TYPES_PATTERN
          = "(text/.*)|(application\\/xml)|(application\\/xhtml\\+xml)";
  private final static Matcher CONTENT_TYPE_MATCHER = Pattern.compile(VALID_CONTENT_TYPES_PATTERN).matcher("");
  private final static String ROBOTS_TXT_PATH = "/robots.txt";

  public static String getUrlString(String url) {
    URI uri = getUri(url);

    if (uri == null) {
      return null;
    }

    return uri.toString();
  }

  public static URL getCleanUrl (String url) {
    try {
      URI uri = getUri(url);
      if (uri != null) {
        return uri.toURL();
      }
    } catch (MalformedURLException e) {
      System.out.printf("Malformed URL: %s \n %s", url, e.toString());
    } catch (Exception e) {
      System.out.printf("Unknown Exception in getCleanUrl: %s \n %s", url, e.toString());
    }

    return null;
  }

  private static URI getUri (String url) {
    try {
        url = cleanUpUrl(url);
        URI uri = new URI(url);
        return new URI(
                uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                null,
                null);
    } catch (Exception e) {
        System.out.printf("Failed to create URI for\'%s\': %s\n", url, e.toString());
        return null;
    }
  }

  private static String cleanUpUrl (String url) {
    // replace spaces with html code %20
    url = StringUtils.replaceAll(url, "\\s", "%20");

    if (shouldContainHttp(url)) {
      url = "http://" + url;
    }

    return url;
  }

  private static boolean shouldContainHttp (String url) {
    return !StringUtils.startsWith(url,"http") && !StringUtils.contains(url, "://");
  }



  public static URL getRobotsTxtURL (URL url) {
    try {
        return new URL(url.getProtocol(), url.getAuthority(), url.getPort(), ROBOTS_TXT_PATH);
    } catch (Exception e) {
        System.out.println("Failed to get robots.txt URL: " + e.toString());
    }

    return null;
  }

  public static boolean isValidContentType (String contentType) {
    contentType = StringUtils.substringBefore(contentType, ";");
    return CONTENT_TYPE_MATCHER.reset(contentType).matches();
  }
}
