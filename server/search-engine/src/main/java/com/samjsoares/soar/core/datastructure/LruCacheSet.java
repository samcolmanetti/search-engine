package com.samjsoares.soar.core.datastructure;

public class LruCacheSet<T> {

  private static final Object PRESENT = new Object();

  private final LruCacheMap<T, Object> lruCacheMap;

  private static final int DEFAULT_LIMIT = 128;
  private final int limit;

  public LruCacheSet() {
    lruCacheMap = new LruCacheMap<>(DEFAULT_LIMIT);
    this.limit = DEFAULT_LIMIT;
  }

  public LruCacheSet(int limit) {
    lruCacheMap = new LruCacheMap<>(limit);
    this.limit = limit;
  }

  public int size() {
    return lruCacheMap.size();
  }

  public boolean isEmpty() {
    return lruCacheMap.isEmpty();
  }

  public boolean contains(T t) {
    return lruCacheMap.containsKey(t);
  }

  public boolean add(T t) {
    return lruCacheMap.put(t, PRESENT) == null;
  }
}
