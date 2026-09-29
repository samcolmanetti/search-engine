package com.samjsoares.soar.searcher.dao;

import com.samjsoares.soar.searcher.model.SearchInfo;
import java.util.List;

/** Reads indexed term data from the search database. */
public interface SearchInfoDao {
  /**
   * Returns the documents containing a term, most frequent occurrences first.
   *
   * @param term the exact term to look up
   * @return one {@link SearchInfo} per matching document, or an empty list if none match
   */
  List<SearchInfo> getSearchInfo(String term);
}
