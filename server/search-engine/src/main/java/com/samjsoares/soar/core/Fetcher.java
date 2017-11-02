package com.samjsoares.soar.core;

import com.samjsoares.soar.core.structure.LRUCache;
import com.samjsoares.soar.util.UrlUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Component
public class Fetcher {
  private final static long MIN_INTERVAL = 1000;
  private final static String SLASH = File.separator;
  private final static String CHAR_SET = "UTF-16";

  private Map<String, Long> lastRequestTimeMap = new LRUCache<>(128);

  public Fetcher() { }

  /**
   * Fetches and parses a URL string, returning a list of paragraph elements.
   *
   * @param url
   * @return Elements document elements
   */
  public Elements fetch(String url) {
    URL realUrl;
    try {
      realUrl = new URL(url);
    } catch (Exception e) {
      System.out.println("Attempting to fetch malformed URL: " + url);
      return null;
    }

    sleepIfNeeded(realUrl);

    // download the document
    Document doc = downloadDocument(url);

    if (doc == null) {
      return null;
    }

    //saveToFile(doc, realUrl);
    return doc.children();
  }

  private Document downloadDocument(String url) {
    Connection conn = Jsoup.connect(url).ignoreContentType(true);
    Connection.Response response = null;

    try {
      response = conn.execute();
    } catch (IOException e) {
      System.out.println("IO Exception: " + e.toString());
      return null;
    }

    if (!UrlUtil.isValidContentType(response.contentType())) {
      return null;
    }

    Document doc = null;
    try {
      doc = response.parse();
    } catch (IOException e) {
      System.out.println("IO Exception: " + e.toString());
    }

    return doc;
  }

  private void saveToFile(Document doc, URL url) {
    final File file;
    try {
      file = new File(getFileName(url));
      FileUtils.writeStringToFile(file, doc.outerHtml(), CHAR_SET);
    } catch (IOException e) {
      System.out.println("Error saving to file " + e.getMessage());
    }
  }

  /**
   * Reads the contents of a page from src/resources.
   *
   * @param url
   * @return
   * @throws IOException
   */
  public Elements read(String url) {
    URL realUrl;
    try {
      realUrl = new URL(url);
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL: " + url);
      return null;
    }

    String filename = getFileName(realUrl);

    String file;
    try {
      file = FileUtils.readFileToString(new File(filename));
    } catch (IOException e) {
      System.out.println("IO Exception reading file: " + e.toString());
      return null;
    }

    Document doc = Jsoup.parse(file, CHAR_SET);
    Elements paras = doc.select("p");
    return paras;
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

  /**
   * Rate limits by waiting at least the minimum interval between requests.
   */
  private void sleepIfNeeded(URL url) {
    long lastRequestTime = getLastRequestTime(url);

    if (lastRequestTime > 0) {
      long currentTime = System.currentTimeMillis();
      long nextRequestTime = lastRequestTime + MIN_INTERVAL;

      if (currentTime < nextRequestTime) {
        try {
          //System.out.println("Sleeping until " + nextRequestTime);
          Thread.sleep(nextRequestTime - currentTime);
        } catch (InterruptedException e) {
          System.err.println("Warning: sleep interrupted in Fetcher.");
        }
      }
    }
    setLastRequestTime(url, System.currentTimeMillis());
  }
}