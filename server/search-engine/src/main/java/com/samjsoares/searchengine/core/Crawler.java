package com.samjsoares.searchengine.core;

import com.samjsoares.searchengine.util.UrlUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Crawler {
  /**
   * URL of where we started crawling
   */
  private final URL source;

  /**
   * Where the results are stored
   */
  private Index index;

  /**
   * Queue of URLs to may be indexed
   */
  private Queue<URL> queue = new LinkedList<>();

  /**
   * Fetcher used to get pages
   */
  private final static Fetcher fetcher = new Fetcher();

  /**
   * Handler class that determines whether we can crawl a certain directory
   */
  private final static RobotsHandler robotsHandler = new RobotsHandler();


  /**
   * Constructor.
   *
   * @param source
   * @param index
   */
  public Crawler(URL source, Index index) {
    this.source = source;
    this.index = index;
    enqueueUrl(source);
  }

  /**
   * Constructor.
   *
   * @param source
   * @param index
   */
  public Crawler(String source, Index index) {
    this(UrlUtil.getCleanUrl(source), index);
  }

  /**
   * Returns the number of URLs in the queue.
   *
   * @return
   */
  public int queueSize() {
    return queue.size();
  }

  /**
   * Gets a URL from the queue and indexes it.
   *
   * @param offline
   * @return Number of pages indexed.
   */
  public boolean crawl(boolean offline) {
    if (queue.isEmpty()) {
      System.out.println("Queue is empty");
      return false;
    }

    URL url = queue.poll();

    if (!index.shouldIndex(url.toString())) {
      //System.out.println("Already indexed " + url);
      return true;
    }

    System.out.println("Crawling " + url);
    Elements paragraphs = !offline
        ? fetcher.fetch(url.toString())
        : fetcher.read(url.toString());

    if (paragraphs != null) {
      index.indexPage(url.toString(), paragraphs);
      queueInternalLinks(paragraphs);
    }

    return true;
  }

  /**
   * Parses paragraphs and adds internal links to the queue.
   *
   * @param paragraphs
   */
  private void queueInternalLinks(Elements paragraphs) {
    if (paragraphs == null) {
      return;
    }

    Elements urlElements = paragraphs.select("a[href]");

    for (Element urlElement : urlElements) {
      String absUrl = urlElement.attr("abs:href");
      if (!enqueueUrl(absUrl)) {
        System.out.println("Failed to enqueue: " + absUrl);
      }
    }
  }

  private boolean enqueueUrl(String urlString) {
    return enqueueUrl(UrlUtil.getCleanUrl(urlString));
  }

  private boolean enqueueUrl(URL url) {
    if (url == null) {
      System.out.println("Failed to enqueue null url");
      return false;
    }

    if (robotsHandler.isAllowed(url) && queue.offer(url)) {
      //System.out.println("Adding to queue: " + url.toString());
      return true;
    }

    System.out.println("Robots.txt prevents enqueueing: " + url.toString());
    return false;
  }

  public static void main(String[] args) throws IOException {
    // make a Crawler
    Index index = new Index();

    String source = "https://my.scranton.edu";
    Crawler wc = new Crawler(source, index);

    // loop until we index a new page
    boolean continueCrawling;
    int maxCrawl = 50;
    int crawlCount = 0;

    do {
      continueCrawling = wc.crawl(false);
      crawlCount++;
    } while (continueCrawling && crawlCount < maxCrawl);

    String searchTerm = "index";
    System.out.printf("\nResults for term '%s'\n", searchTerm);
    Map<String, Integer> map = index.getCounts(searchTerm);
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " = " + entry.getValue());
    }
  }

}