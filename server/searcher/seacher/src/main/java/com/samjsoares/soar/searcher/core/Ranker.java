package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.model.SearchResult;
import java.util.List;

/** Collects term matches across documents and ranks the documents by relevance. */
public interface Ranker {
  /**
   * Adds the matches for one query term.
   *
   * @param searchInfoList the documents matching the term
   */
  void add(List<SearchInfo> searchInfoList);

  /**
   * Returns one result per document added so far, highest relevance first.
   *
   * @return the ranked results; empty if nothing was added
   */
  List<SearchResult> getRankedResults();
}
