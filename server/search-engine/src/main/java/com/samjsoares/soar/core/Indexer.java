package com.samjsoares.soar.core;

import java.util.Map;
import java.util.Set;

import org.jsoup.select.Elements;

/**
 * Encapsulates a map from search term to set of TermProcessor.
 *
 * @author soar
 */
public interface Indexer {
  void indexPage(String url, Elements paragraphs);
  Set<TermProcessor> get(String term);
  void printIndex();
  boolean shouldIndex(String url);
  Map<String, Integer> getCounts(String term);
}