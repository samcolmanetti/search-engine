package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.constant.Regex;
import com.samjsoares.soar.searcher.dao.SearchInfoDao;
import com.samjsoares.soar.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.model.SearchResult;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.stemmer.Stemmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SearcherImpl implements Searcher {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private SearchInfoDao searchInfoDao;
  private Stemmer stemmer = new PorterStemmer();

  @Autowired
  public SearcherImpl(SearchInfoDao searchInfoDao) {
    this.searchInfoDao = searchInfoDao;
  }

  @Override
  public List<SearchResult> search(String[] terms) {
    Ranker ranker = new TermFrequencyRanker();

    for (String term : terms) {
      List<SearchInfo> searchInfoList = getSearchInfoList(term);
      if (searchInfoList != null && !searchInfoList.isEmpty()) {
        ranker.add(searchInfoList);
      }
    }

    return ranker.getRankedResults();
  }

  private List<SearchInfo> getSearchInfoList(String term) {
    String stemmedTerm = stemmer.stem(term).toString();
    List<SearchInfo> searchInfoList = searchInfoDao.getSearchInfo(stemmedTerm);

    if (searchInfoList == null || searchInfoList.isEmpty()) {
      searchInfoList = searchInfoDao.getSearchInfo(term);
    }

    return searchInfoList;
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
