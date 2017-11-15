package com.samjsoares.soar.driver;

import com.samjsoares.soar.core.DatabaseIndexer;
import com.samjsoares.soar.core.Fetcher;
import com.samjsoares.soar.core.Indexer;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SimpleIndexerDriver {

  @Autowired Indexer indexer;

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
