package com.samjsoares.soar.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The LRUCache class is a list that removes the least recently used item when the size of the list
 * reaches the specified limit.
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
  private final static int DEFAULT_LIMIT = 128;
  private final int limit;

  public LRUCache() {
    super(16,0.75f, true);
    this.limit = DEFAULT_LIMIT;
  }

  public LRUCache(int limit) {
    super(16, 0.75f, true);
    this.limit = limit;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > limit;
  }
}
