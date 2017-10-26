package com.samjsoares.searchengine.driver;

import com.samjsoares.searchengine.core.Crawler;
import com.samjsoares.searchengine.core.Index;

import java.io.IOException;
import java.util.Map;

public class SimpleCrawlerDriver {
  public static void main(String[] args) throws IOException {
    // make a Crawler
    Index index = new Index();

    String source = "https://en.wikipedia.org/wiki/Education";
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
