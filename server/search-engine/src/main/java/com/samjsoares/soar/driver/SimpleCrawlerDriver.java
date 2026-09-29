package com.samjsoares.soar.driver;

import com.samjsoares.soar.core.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Manual runner that keeps the crawler going until it runs out of work. */
@Component
public class SimpleCrawlerDriver {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final Crawler crawler;

  /** Creates a driver for the given crawler. */
  @Autowired
  public SimpleCrawlerDriver(Crawler crawler) {
    this.crawler = crawler;
  }

  /**
   * Crawls one page at a time until the crawler reports there is nothing left, or after 1,000,000
   * pages. Crawls that throw are retried and not counted.
   */
  public void run() {

    // loop until we indexer a new page
    boolean continueCrawling;
    int maxCrawl = 1_000_000;
    int crawlCount = 0;

    logger.info("Starting crawler...");

    do {
      try {
        continueCrawling = crawler.crawl(false);
      } catch (Exception e) {
        continueCrawling = true;
        crawlCount--;
      }

      crawlCount++;

      logger.info("Crawled number: {}", crawlCount);
    } while (continueCrawling && crawlCount < maxCrawl);

    logger.info("Crawler is finished...");
  }
}
