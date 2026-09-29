package com.samjsoares.soar.core;

import com.samjsoares.soar.core.datastructure.LruCacheMap;
import java.util.Map;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.stemmer.Stemmer;

/** Porter stemmer that caches the stems of the 256 most recently used words. */
public class ScaledStemmer {

  private final Stemmer stemmer = new PorterStemmer();
  private final Map<String, String> cache = new LruCacheMap<>(256);

  /** Creates a stemmer with an empty cache. */
  public ScaledStemmer() {}

  /** Returns the Porter stem of {@code word}, or null if {@code word} is null. */
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
