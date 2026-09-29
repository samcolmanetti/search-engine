package com.samjsoares.soar.driver;

import com.samjsoares.soar.core.Fetcher;
import com.samjsoares.soar.core.Indexer;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Manual runner that fetches and indexes two fixed Wikipedia pages. */
@Component
public class SimpleIndexerDriver {

  @Autowired Indexer indexer;

  /** Fetches and indexes the Wikipedia Education and Journalism pages. */
  public void run() {
    Fetcher wf = new Fetcher();

    String url = "https://en.wikipedia.org/wiki/Education";
    Document document = wf.fetchDocument(url);
    indexer.indexPage(url, document);

    url = "https://en.wikipedia.org/wiki/Journalism";
    document = wf.fetchDocument(url);
    indexer.indexPage(url, document);
  }
}
