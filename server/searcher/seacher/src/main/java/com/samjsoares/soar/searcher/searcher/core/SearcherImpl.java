package com.samjsoares.soar.searcher.searcher.core;

import com.samjsoares.soar.searcher.searcher.dao.SearchInfoDao;
import com.samjsoares.soar.searcher.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.searcher.model.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SearcherImpl implements Searcher {

  private SearchInfoDao searchInfoDao;

  @Autowired
  public SearcherImpl(SearchInfoDao searchInfoDao) {
    this.searchInfoDao = searchInfoDao;
  }

  @Override
  public List<SearchResult> search(String[] terms) {
    String term = terms.length > 0 ? terms[0] : "";
    List<SearchInfo> searchInfos = searchInfoDao.getSearchInfo(term);

    List<SearchResult> results = new ArrayList<>(searchInfos.size());
    for (SearchInfo searchInfo : searchInfos) {
      results.add(new SearchResult(
          searchInfo.getUrl(),
          searchInfo.getTermCount(),
          searchInfo.getTitle(),
          searchInfo.getDescription()));
    }

    return results;
  }

  @Override
  public List<SearchResult> search(String query) {
    if (query == null) {
      return Collections.emptyList();
    }

    return search(query.toLowerCase().split("\\s+"));
  }

}
