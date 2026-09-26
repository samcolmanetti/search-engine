package com.samjsoares.soar.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class UrlUtilTest {

  @Test
  public void testContentType_text() {
    String contentType = "text/html";
    assertTrue(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_textWithExtra() {
    String contentType = "text/html; encoding=UTF8";
    assertTrue(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_xml() {
    String contentType = "application/xml";
    assertTrue(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_xmlWithExtra() {
    String contentType = "application/xml; encoding=UTF8";
    assertTrue(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_xhtml_xml() {
    String contentType = "application/xhtml+xml";
    assertTrue(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_xhtml_xml_withExtras() {
    String contentType = "application/xhtml+xml; encoding=UTF8";
    assertTrue(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_pdf() {
    String contentType = "application/pdf";
    assertFalse(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testContentType_ogg() {
    String contentType = "application/ogg";
    assertFalse(UrlUtil.isValidContentType(contentType));
  }

  @Test
  public void testGetGoogleRobotsTxt() {
    String urlStr = "https://www.google.com/search?q=josh+david";
    String expected = "https://www.google.com/robots.txt";
    URL url = null;
    try {
      url = new URL(urlStr);
    } catch (MalformedURLException e) {
    }

    assertEquals(expected, UrlUtil.getRobotsTxtURL(url).toString());
  }

  @Test
  public void testCleanUpUrl_valid() {
    String url = "http://www.google.com/search?q=josh+david";
    String expected = "http://www.google.com/search";

    assertEquals(expected, UrlUtil.getUrlString(url));
  }

  @Test
  public void testCleanUpUrl_noHttp() {
    String url = "www.google.com/search?q=josh+david";
    String expected = "http://www.google.com/search";

    assertEquals(expected, UrlUtil.getUrlString(url));
  }

  @Test
  public void testCleanUpUrl_differentProtocol() {
    String url = "ssh://134.198.168.101";
    String expected = null;

    assertEquals(expected, UrlUtil.getUrlString(url));
  }

  @Test
  public void testCleanUpUrl_ipOnly() {
    String url = "134.198.168.101";
    String expected = "http://134.198.168.101/";

    assertEquals(expected, UrlUtil.getUrlString(url));
  }

  @Test
  public void testUrlKey_https() {
    String url = "google.com";
    String expected = "google.com/";

    assertEquals(expected, UrlUtil.getUrlKey(url));
  }

  @Test
  public void testContentType_textPlain() {
    assertTrue(UrlUtil.isValidContentType("text/plain"));
  }

  @Test
  public void testContentType_json() {
    assertFalse(UrlUtil.isValidContentType("application/json"));
  }

  @Test
  public void testCleanUrl_stripsFragment() {
    assertEquals(
        "http://example.com/page", UrlUtil.getCleanUrl("http://example.com/page#top").toString());
  }

  @Test
  public void testCleanUrl_stripsQuery() {
    assertEquals(
        "http://example.com/page",
        UrlUtil.getCleanUrl("http://example.com/page?a=1&b=2").toString());
  }

  @Test
  public void testCleanUrl_forcesHttp() {
    assertEquals(
        "http://example.com/secure", UrlUtil.getCleanUrl("https://example.com/secure").toString());
  }

  @Test
  public void testCleanUrl_addsMissingScheme() {
    assertEquals("http://example.com/page", UrlUtil.getCleanUrl("example.com/page").toString());
  }

  @Test
  public void testCleanUrl_encodesSpaces() {
    assertEquals(
        "http://example.com/a%20b%20c",
        UrlUtil.getCleanUrl("http://example.com/a b\tc").toString());
  }

  @Test
  public void testCleanUrl_addsRootPath() {
    assertEquals("http://example.com/", UrlUtil.getCleanUrl("example.com").toString());
  }

  @Test
  public void testCleanUrl_lowercasesHost() {
    assertEquals(
        "http://example.com/Path", UrlUtil.getCleanUrl("http://EXAMPLE.com/Path").toString());
  }

  @Test
  public void testCleanUrl_invalid() {
    assertNull(UrlUtil.getCleanUrl("http://"));
    assertNull(UrlUtil.getCleanUrl("ssh://134.198.168.101"));
    assertNull(UrlUtil.getCleanUrl("http://exa mple.com/"));
  }

  @Test
  public void testUrlKey_dropsSchemeQueryAndFragment() {
    assertEquals("example.com/a/b", UrlUtil.getUrlKey("https://example.com/a/b?q=1#frag"));
  }

  @Test
  public void testUrlKey_invalid() {
    assertNull(UrlUtil.getUrlKey("ssh://example.com"));
  }

  @Test
  public void testUrlString_httpsAndHttpAreSame() {
    assertEquals(
        UrlUtil.getUrlString("http://example.com/page"),
        UrlUtil.getUrlString("https://example.com/page"));
  }

  @Test
  public void testUrlString_invalid() {
    assertNull(UrlUtil.getUrlString("http://"));
  }

  @Test
  public void testRobotsTxtUrl_dropsPathAndQuery() throws MalformedURLException {
    URL url = new URL("http://example.com/deep/path/page.html?x=1#y");
    assertEquals("http://example.com/robots.txt", UrlUtil.getRobotsTxtURL(url).toString());
  }

  @Test
  public void testRobotsTxtUrl_null() {
    assertNull(UrlUtil.getRobotsTxtURL(null));
  }

  @Test
  public void testContentTypeCheckIsThreadSafe() throws Exception {
    int threads = 8;
    int checksPerThread = 20_000;
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    AtomicInteger wrongAnswers = new AtomicInteger();
    try {
      List<Future<?>> futures = new ArrayList<>();
      for (int t = 0; t < threads; t++) {
        boolean valid = t % 2 == 0;
        String contentType = valid ? "text/html; charset=UTF-8" : "image/png";
        futures.add(
            pool.submit(
                () -> {
                  for (int i = 0; i < checksPerThread; i++) {
                    if (UrlUtil.isValidContentType(contentType) != valid) {
                      wrongAnswers.incrementAndGet();
                    }
                  }
                }));
      }
      for (Future<?> future : futures) {
        future.get();
      }
    } finally {
      pool.shutdownNow();
    }

    assertEquals(0, wrongAnswers.get());
  }
}
