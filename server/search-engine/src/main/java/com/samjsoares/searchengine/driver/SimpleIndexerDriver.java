package com.samjsoares.searchengine.driver;

import com.samjsoares.searchengine.core.DatabaseIndex;
import com.samjsoares.searchengine.core.Fetcher;
import com.samjsoares.searchengine.core.InMemoryIndex;
import com.samjsoares.searchengine.core.Index;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SimpleIndexerDriver {
  public static void main(String[] args) throws IOException {
    Fetcher wf = new Fetcher();
    Index indexer = new DatabaseIndex();

    String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
    Elements paragraphs = wf.fetch(url);
    indexer.indexPage(url, paragraphs);

    url = "https://en.wikipedia.org/wiki/Programming_language";
    paragraphs = wf.fetch(url);
    indexer.indexPage(url, paragraphs);

    indexer.printIndex();
  }
}
