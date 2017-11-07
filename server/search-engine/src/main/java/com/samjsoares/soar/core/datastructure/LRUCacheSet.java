package com.samjsoares.soar.core.datastructure;

public class LRUCacheSet<T> {

  private static final Object PRESENT = new Object();

  private LRUCacheMap<T, Object> lruCacheMap;

  private final static int DEFAULT_LIMIT = 128;
  private final int limit;

  public LRUCacheSet() {
    lruCacheMap = new LRUCacheMap<> (DEFAULT_LIMIT);
    this.limit = DEFAULT_LIMIT;
  }

  public LRUCacheSet(int limit) {
    lruCacheMap = new LRUCacheMap<> (limit);
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
