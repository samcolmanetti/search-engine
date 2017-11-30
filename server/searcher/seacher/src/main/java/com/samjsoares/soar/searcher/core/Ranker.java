package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.model.SearchResult;

import java.util.List;

public interface Ranker {
  void add (List<SearchInfo> searchInfoList);
  List<SearchResult> getRankedResults();
}
