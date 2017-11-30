package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.constant.Regex;
import com.samjsoares.soar.searcher.dao.SearchInfoDao;
import com.samjsoares.soar.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SearcherImpl implements Searcher {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private SearchInfoDao searchInfoDao;

  @Autowired
  public SearcherImpl(SearchInfoDao searchInfoDao) {
    this.searchInfoDao = searchInfoDao;
  }

  @Override
  public List<SearchResult> search(String[] terms) {
    Ranker ranker = new TermFrequencyRanker();

    for (String term : terms) {
      List<SearchInfo> searchInfoList = searchInfoDao.getSearchInfo(term);
      if (searchInfoList != null && !searchInfoList.isEmpty()) {
        ranker.add(searchInfoList);
      }
    }

    return ranker.getRankedResults();
  }


  @Override
  public List<SearchResult> search(String query) {
    if (query == null) {
      return Collections.emptyList();
    }

    logger.info("Query: " + query);
    return search(query.toLowerCase().split(Regex.SPACE_OR_PLUS));
  }

}
