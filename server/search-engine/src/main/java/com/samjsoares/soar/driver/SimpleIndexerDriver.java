package com.samjsoares.soar.driver;

import com.samjsoares.soar.core.DatabaseIndexer;
import com.samjsoares.soar.core.Fetcher;
import com.samjsoares.soar.core.Indexer;
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
    Elements paragraphs = wf.fetch(url);
    indexer.indexPage(url, paragraphs);

    url = "https://en.wikipedia.org/wiki/Journalism";
    paragraphs = wf.fetch(url);
    indexer.indexPage(url, paragraphs);
  }
}
