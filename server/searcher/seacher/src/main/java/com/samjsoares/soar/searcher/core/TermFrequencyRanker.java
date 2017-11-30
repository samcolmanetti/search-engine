package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class ranks search results by Term Frequency.
 */
public class TermFrequencyRanker implements Ranker {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Map<Long, List<SearchInfo>> map = new HashMap<>();

  @Override
  public void add(List<SearchInfo> searchInfoList) {
    for (SearchInfo searchInfo : searchInfoList) {
      long key = searchInfo.getDocId();

      if (map.containsKey(key)) {
        List<SearchInfo> list = map.get(key);
        list.add(searchInfo);
        map.put(searchInfo.getDocId(), list);
      } else {
        List<SearchInfo> list = new LinkedList<>();
        list.add(searchInfo);
        map.put(key, list);
      }
    }
  }

  @Override
  public List<SearchResult> getRankedResults() {
    if (map.isEmpty()) {
      return Collections.emptyList();
    }

    PriorityQueue<SearchResult> heap = new PriorityQueue<>(map.size(), new RelevanceComparator());

    for (List<SearchInfo> list : map.values()) {
      heap.add(getSearchResult(list));
    }

    return new ArrayList<>(heap);
  }

  private SearchResult getSearchResult(List<SearchInfo> searchInfoList) {
    SearchInfo sample = !searchInfoList.isEmpty() ? searchInfoList.get(0) : null;

    if (sample == null) {
      return null;
    }

    SearchResult searchResult = new SearchResult();
    searchResult.setTitle(sample.getTitle());
    searchResult.setDescription(sample.getDescription());
    searchResult.setUrl(sample.getUrl());
    searchResult.setRelevance(getRelevance(searchInfoList));

    return searchResult;
  }

  private double getRelevance(List<SearchInfo> searchInfoList) {
    double sum = 0;

    for (SearchInfo searchInfo : searchInfoList) {
      double tfIdf = searchInfo.getTermFrequency() / (double) searchInfo.getDocumentTermFrequency();
      sum = sum + tfIdf;

      logger.info("term: " + searchInfo.getTerm() + " | tf: " + searchInfo.getTermFrequency() + " | idf: " +
          searchInfo.getDocumentTermFrequency() + " | tf-idf: " + tfIdf);
    }

    return sum;
  }

  class RelevanceComparator implements Comparator<SearchResult> {

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
      if (o2.getRelevance() > o1.getRelevance()) {
        return 1;
      }

      return -1;
    }
  }
}
