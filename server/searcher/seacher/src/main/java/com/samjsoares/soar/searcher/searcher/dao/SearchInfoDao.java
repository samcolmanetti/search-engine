package com.samjsoares.soar.searcher.searcher.dao;

import com.samjsoares.soar.searcher.searcher.model.SearchInfo;

import java.util.List;

public interface SearchInfoDao {
  List<SearchInfo> getSearchInfo(String term);
}
