package com.samjsoares.searchengine.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtilTester {

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
    String expected = "ssh://134.198.168.101";

    assertEquals(expected, UrlUtil.getUrlString(url));
  }

  @Test
  public void testCleanUpUrl_ipOnly() {
    String url = "134.198.168.101";
    String expected = "http://134.198.168.101";

    assertEquals(expected, UrlUtil.getUrlString(url));
  }

}
