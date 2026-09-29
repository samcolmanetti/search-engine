package com.samjsoares.soar.core;

import java.util.Map;
import java.util.Set;
import org.jsoup.nodes.Document;

/**
 * Stores the term counts of crawled pages.
 *
 * @author soar
 */
public interface Indexer {
  /** Counts the terms in {@code document}, fetched from {@code url}, and stores them. */
  void indexPage(String url, Document document);

  /**
   * Returns the TermProcessors of the pages that contain {@code term}, or null if there are none or
   * the implementation does not support lookups.
   */
  Set<TermProcessor> get(String term);

  /** Prints the index to standard output, if the implementation supports it. */
  void printIndex();

  /** Returns whether the page at {@code url} has not been indexed within the past week. */
  boolean shouldIndex(String url);

  /**
   * Returns the count of {@code term} in each page that contains it, keyed by URL, or null if the
   * implementation does not support lookups.
   */
  Map<String, Integer> getCounts(String term);
}
