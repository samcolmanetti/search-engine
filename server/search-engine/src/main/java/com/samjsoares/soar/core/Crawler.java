package com.samjsoares.soar.core;

import com.samjsoares.soar.core.datastructure.LruCacheSet;
import com.samjsoares.soar.util.UrlUtil;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Crawls pages taken from a queue of discovered links, mixing in seed URLs from a {@link
 * UrlServer}, and indexes them. Respects robots.txt.
 */
@Component
public class Crawler {
  /** Where the results are stored. */
  private final Indexer indexer;

  /** Queue of discovered URLs waiting to be crawled. */
  private final Queue<URL> queue = new LinkedList<>();

  /** Fetcher used to get pages. */
  private final Fetcher fetcher;

  /** Checks robots.txt to determine whether a URL may be crawled. */
  private final RobotsHandler robotsHandler;

  private final UrlServer urlServer;

  /**
   * Keys of recently queued URLs, from {@link UrlUtil#getUrlKey(URL)}, so the http and https
   * versions of a page are queued once.
   */
  private final LruCacheSet<String> cache = new LruCacheSet<>(512);

  private final Random random = new Random();

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Creates a crawler that fetches pages with {@code fetcher} and stores them in {@code indexer}.
   */
  @Autowired
  public Crawler(
      Indexer indexer, Fetcher fetcher, RobotsHandler robotsHandler, UrlServer urlServer) {
    this.indexer = indexer;
    this.fetcher = fetcher;
    this.robotsHandler = robotsHandler;
    this.urlServer = urlServer;
  }

  /** Returns the number of URLs in the queue. */
  public int queueSize() {
    return queue.size();
  }

  /**
   * Takes the next URL, usually from the queue but sometimes from the seed server, and indexes it
   * if it is due. A page indexed within the past week isn't indexed again, but it is still fetched
   * so its links can be queued.
   *
   * @param offline whether to read pages from the local backup instead of downloading them
   * @return false if the queue is empty and no seed URL is left, true otherwise
   */
  public boolean crawl(boolean offline) {
    if (queue.isEmpty()) {
      logger.info("Queue is empty");
      if (enqueueUrl(urlServer.getNextUrl())) {
        logger.info("Pulled new url from seed: {}", queue.peek());
      } else {
        return false;
      }
    }

    URL url = getNextUrl();

    if (url == null) {
      // Every queued URL was disallowed. The next call refills the queue from the seeds.
      logger.info("No crawlable URL left in the queue");
      return true;
    }

    if (!indexer.shouldIndex(url.toString())) {
      logger.debug("Already indexed {}", url);
      addInternalLinks(url);
      return true;
    }

    logger.info("Crawling {}", url);
    Document document =
        !offline ? fetcher.fetchDocument(url.toString()) : fetcher.readDocument(url.toString());

    if (document != null) {
      indexer.indexPage(url.toString(), document);
      queueInternalLinks(document.children());
    }

    return true;
  }

  private URL getNextUrl() {
    if (random.nextDouble() > 0.30) {
      return getNextUrlFromQueue();
    }

    URL url = urlServer.getNextUrl();
    return url != null ? url : getNextUrlFromQueue();
  }

  /** Returns the next queued URL that robots.txt allows, or null once the queue runs out. */
  private URL getNextUrlFromQueue() {
    URL url;
    do {
      url = queue.poll();
    } while (url != null && !robotsHandler.isAllowed(url));

    return url;
  }

  private void addInternalLinks(URL url) {
    Elements paragraphs = fetcher.fetch(url.toString());
    queueInternalLinks(paragraphs);
  }

  /** Queues the absolute URL of every link in the given elements that was not queued recently. */
  private void queueInternalLinks(Elements paragraphs) {
    if (paragraphs == null) {
      return;
    }

    Elements urlElements = paragraphs.select("a[href]");

    for (Element urlElement : urlElements) {
      String absUrl = urlElement.attr("abs:href");
      if (!enqueueUrl(absUrl)) {
        logger.debug("Failed to enqueue: {}", absUrl);
      }
    }
  }

  private boolean enqueueUrl(String urlString) {
    return enqueueUrl(UrlUtil.getCleanUrl(urlString));
  }

  private boolean enqueueUrl(URL url) {
    if (url == null || cache.contains(UrlUtil.getUrlKey(url))) {
      return false;
    }

    if (queue.offer(url)) {
      cache.add(UrlUtil.getUrlKey(url));
      return true;
    }

    return false;
  }
}
