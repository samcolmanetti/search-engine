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

  @Autowired
  public SimpleCrawlerDriver(Indexer indexer){
    this.indexer = indexer;
  }

  public void run() {
    // make a Crawler
    String source = "http://www.cs.scranton.edu/";
    Crawler wc = new Crawler(source, indexer);

    // loop until we indexer a new page
    boolean continueCrawling;
    int maxCrawl = 100;
    int crawlCount = 0;

    logger.info("Starting crawler...");
    do {
      continueCrawling = wc.crawl(false);
      crawlCount++;

      logger.info("Crawled number: " + crawlCount);
    } while (continueCrawling && crawlCount < maxCrawl);

    logger.info("Crawler is finished...");

    /*
    String searchTerm = "indexer";
    System.out.printf("\nResults for term '%s'\n", searchTerm);
    Map<String, Integer> map = indexer.getCounts(searchTerm);
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " = " + entry.getValue());
    }
    */
  }
}
