package com.samjsoares.soar.searcher.searcher.core;

import com.samjsoares.soar.searcher.searcher.model.SearchResult;

import java.util.List;

public interface Searcher {
  List<SearchResult> search(String[] terms);
  List<SearchResult> search(String query);

}
