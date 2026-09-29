package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.model.SearchResult;
import java.util.List;

/** Finds documents matching a query and returns them ranked by relevance. */
public interface Searcher {
  /**
   * Returns the documents matching any of the given terms, ranked by relevance.
   *
   * @param terms the query terms
   * @return the matching documents, highest relevance first; empty if nothing matches
   */
  List<SearchResult> search(String[] terms);

  /**
   * Splits the query into terms and returns the matching documents, ranked by relevance.
   *
   * @param query the raw query string
   * @return the matching documents, highest relevance first; empty if nothing matches
   */
  List<SearchResult> search(String query);
}
