package com.samjsoares.soar.driver;

import com.samjsoares.soar.core.Crawler;
import com.samjsoares.soar.core.InMemoryIndexer;
import com.samjsoares.soar.core.Indexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class SimpleCrawlerDriver {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private Indexer indexer;
  private Crawler crawler;

  @Autowired
  public SimpleCrawlerDriver(Indexer indexer, Crawler crawler){
    this.indexer = indexer;
    this.crawler = crawler;
  }

  public void run() {

    // loop until we indexer a new page
    boolean continueCrawling;
    int maxCrawl = 9001;
    int crawlCount = 0;

    logger.info("Starting crawler...");
    do {
      continueCrawling = crawler.crawl(false);
      crawlCount++;

      logger.info("Crawled number: " + crawlCount);
    } while (continueCrawling && crawlCount < maxCrawl);

    logger.info("Crawler is finished...");
  }
}
