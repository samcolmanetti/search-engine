package com.samjsoares.soar.searcher.dao;

import com.samjsoares.soar.searcher.model.SearchInfo;

import java.util.List;

public interface SearchInfoDao {
  List<SearchInfo> getSearchInfo(String term);
}
