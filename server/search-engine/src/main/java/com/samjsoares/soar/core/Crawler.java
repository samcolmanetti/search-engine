package com.samjsoares.soar.core;

import com.samjsoares.soar.util.UrlUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

public class Crawler {
  /**
   * URL of where we started crawling
   */
  private final URL source;

  /**
   * Where the results are stored
   */
  private Indexer indexer;

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
   * @param indexer
   */
  public Crawler(URL source, Indexer indexer) {
    this.source = source;
    this.indexer = indexer;
    enqueueUrl(source);
  }

  /**
   * Constructor.
   *
   * @param source
   * @param indexer
   */
  public Crawler(String source, Indexer indexer) {
    this(UrlUtil.getCleanUrl(source), indexer);
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

    if (!indexer.shouldIndex(url.toString())) {
      //System.out.println("Already indexed " + url);
      return true;
    }

    System.out.println("Crawling " + url);
    Elements paragraphs = !offline
        ? fetcher.fetch(url.toString())
        : fetcher.read(url.toString());

    if (paragraphs != null) {
      indexer.indexPage(url.toString(), paragraphs);
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

}