package com.samjsoares.soar.core;

import com.samjsoares.soar.core.datastructure.LRUCacheMap;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.PorterStemmer;

import java.util.Map;

public class ScaledStemmer {

  private Stemmer stemmer = new PorterStemmer();
  private Map<String, String> cache = new LRUCacheMap(256);

  public ScaledStemmer() {}

  public String stem(String word) {
    if (word == null) {
      return null;
    }

    if (cache.containsKey(word)) {
      return cache.get(word);
    }

    String stemmed = stemmer.stem(word).toString();
    cache.put(word, stemmed);

    return stemmed;
  }

}
