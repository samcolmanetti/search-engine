package com.samjsoares.searchengine.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.samjsoares.searchengine.constant.TimeConstants;
import com.samjsoares.searchengine.util.CollectionsUtil;
import com.samjsoares.searchengine.util.UrlUtil;
import org.jsoup.select.Elements;

/**
 * Encapsulates a map from search term to set of TermProcessor.
 *
 * @author soar
 */
public interface Index {
  void indexPage(String url, Elements paragraphs);
  Set<TermProcessor> get(String term);
  void printIndex();
  boolean shouldIndex(String url);
  Map<String, Integer> getCounts(String term);
}