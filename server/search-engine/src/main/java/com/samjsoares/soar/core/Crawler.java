package com.samjsoares.soar.core;

import com.samjsoares.soar.core.datastructure.LRUCacheSet;
import com.samjsoares.soar.util.UrlUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

@Component
public class Crawler {
  /**
   * URL of where we started crawling
   */
  //private final URL source;

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
  private Fetcher fetcher;

  /**
   * Handler class that determines whether we can crawl a certain directory
   */
  private RobotsHandler robotsHandler;

  private URLServer urlServer;

  private LRUCacheSet<URL> cache = new LRUCacheSet<>(512);

  private Random random = new Random();

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public Crawler(Indexer indexer, Fetcher fetcher, RobotsHandler robotsHandler, URLServer urlServer) {
    this.indexer = indexer;
    this.fetcher = fetcher;
    this.robotsHandler = robotsHandler;
    this.urlServer = urlServer;
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
   * @return boolean indicating whether or not to keep crawling
   */
  public boolean crawl(boolean offline) {
    if (queue.isEmpty()) {
      logger.info("Queue is empty");
      if (enqueueUrl(urlServer.getNextUrl())) {
        logger.info("Pulled new url from seed: " + queue.peek());
      } else {
        return false;
      }
    }

    URL url = getNextUrl();

    if (!indexer.shouldIndex(url.toString())) {
      logger.debug("Already indexed " + url);
      addInternalLinks(url);
      return true;
    }

    logger.info("Crawling " + url);
    Document document = !offline
        ? fetcher.fetchDocument(url.toString())
        : fetcher.readDocument(url.toString());

    if (document != null) {
      indexer.indexPage(url.toString(), document);
      queueInternalLinks(document.children());
    }

    return true;
  }

  private URL getNextUrl() {
    if (random.nextDouble() > 0.3) {
      return getNextUrlFromQueue();
    } else {
      return urlServer.getNextUrl();
    }
  }

  private URL getNextUrlFromQueue() {
    URL url;
    do {
      url = queue.poll();
    } while (!robotsHandler.isAllowed(url));

    return url;
  }

  private void addInternalLinks (URL url) {
    Elements paragraphs = fetcher.fetch(url.toString());
    queueInternalLinks(paragraphs);
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
        logger.debug("Failed to enqueue: " + absUrl);
      }
    }
  }

  private boolean enqueueUrl(String urlString) {
    return enqueueUrl(UrlUtil.getCleanUrl(urlString));
  }

  private boolean enqueueUrl(URL url) {
    if (url == null || cache.contains(url)) {
      return false;
    }

    if (queue.offer(url)) {
      cache.add(url);
      return true;
    }

    return false;
  }

}