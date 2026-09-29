package com.samjsoares.soar.core;

import com.samjsoares.soar.core.datastructure.LruCacheMap;
import com.samjsoares.soar.util.UrlUtil;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Downloads and parses web pages, waiting at least one second between requests to the same host.
 * Can also read saved copies of pages from local resources.
 */
@Component
public class Fetcher {
  private static final long MIN_INTERVAL = 1000;
  private static final String SLASH = File.separator;
  private static final String CHAR_SET = "UTF-16";

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Map<String, Long> lastRequestTimeMap = new LruCacheMap<>(128);

  /** Creates a fetcher with no request history. */
  public Fetcher() {}

  /**
   * Fetches and parses a URL string, returning the children of the document.
   *
   * @return the document's children, or null if the page could not be fetched or parsed
   */
  public Elements fetch(String url) {
    Document doc = fetchDocument(url);

    if (doc == null) {
      return null;
    }

    return doc.children();
  }

  /**
   * Fetches and parses a URL string, first waiting if the host was requested less than a second
   * ago.
   *
   * @return the parsed document, or null if the URL is malformed, the download fails, the content
   *     type is not supported, or the page cannot be parsed
   */
  public Document fetchDocument(String url) {
    URL realUrl;
    try {
      realUrl = new URL(url);
    } catch (Exception e) {
      logger.warn("Attempting to fetch malformed URL: {}", url);
      return null;
    }

    sleepIfNeeded(realUrl);

    // download the document
    Document doc = downloadDocument(url);

    if (doc == null) {
      return null;
    }

    return doc;
  }

  private Document downloadDocument(String url) {
    Connection conn = Jsoup.connect(url).ignoreContentType(true);
    Connection.Response response = null;

    try {
      response = conn.execute();
    } catch (IOException e) {
      logger.warn("Failed to download {}: {}", url, e.toString());
      return null;
    }

    if (!UrlUtil.isValidContentType(response.contentType())) {
      return null;
    }

    Document doc = null;
    try {
      doc = response.parse();
    } catch (IOException e) {
      logger.warn("Failed to parse {}: {}", url, e.toString());
    }

    return doc;
  }

  /**
   * Reads a saved copy of a page from {@code src/main/resources/document_backup}, returning the
   * children of the document, or null if the URL is malformed or the file cannot be read.
   */
  public Elements read(String url) {
    Document doc = readDocument(url);

    if (doc == null) {
      return null;
    }

    return doc.children();
  }

  /**
   * Reads and parses a saved copy of a page from {@code src/main/resources/document_backup}, found
   * by the URL's host and path. Returns null if the URL is malformed or the file cannot be read.
   */
  public Document readDocument(String url) {
    URL realUrl;
    try {
      realUrl = new URL(url);
    } catch (MalformedURLException e) {
      logger.warn("Malformed URL: {}", url);
      return null;
    }

    String filename = getFileName(realUrl);

    String file;
    try {
      file = FileUtils.readFileToString(new File(filename));
    } catch (IOException e) {
      logger.warn("Failed to read {}: {}", filename, e.toString());
      return null;
    }

    Document doc = Jsoup.parse(file, CHAR_SET);
    return doc;
  }

  private String getFileName(URL url) {
    String path = url.getHost() + url.getPath();
    return "src" + SLASH + "main" + SLASH + "resources" + SLASH + "document_backup" + SLASH + path;
  }

  private long getLastRequestTime(URL url) {
    Long lastRequestTime = lastRequestTimeMap.get(url.getHost());

    if (lastRequestTime == null) {
      lastRequestTime = Long.MIN_VALUE;
    }

    return lastRequestTime;
  }

  private void setLastRequestTime(URL url, long time) {
    lastRequestTimeMap.put(url.getHost(), time);
  }

  /** Rate limits by waiting at least the minimum interval between requests. */
  private void sleepIfNeeded(URL url) {
    long lastRequestTime = getLastRequestTime(url);

    if (lastRequestTime > 0) {
      long currentTime = System.currentTimeMillis();
      long nextRequestTime = lastRequestTime + MIN_INTERVAL;

      if (currentTime < nextRequestTime) {
        try {
          Thread.sleep(nextRequestTime - currentTime);
        } catch (InterruptedException e) {
          logger.warn("Sleep interrupted while rate limiting {}", url.getHost());
        }
      }
    }
    setLastRequestTime(url, System.currentTimeMillis());
  }
}
