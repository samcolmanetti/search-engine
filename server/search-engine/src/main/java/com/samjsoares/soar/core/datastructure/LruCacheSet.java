package com.samjsoares.soar.core.datastructure;

/**
 * A set with a size limit that evicts the element least recently added once the limit is exceeded.
 * Adding an element that is already present refreshes it; {@link #contains} does not.
 *
 * @param <T> the type of elements in the set
 */
public class LruCacheSet<T> {

  private static final Object PRESENT = new Object();

  private final LruCacheMap<T, Object> lruCacheMap;

  private static final int DEFAULT_LIMIT = 128;
  private final int limit;

  /** Creates a set that holds at most 128 elements. */
  public LruCacheSet() {
    lruCacheMap = new LruCacheMap<>(DEFAULT_LIMIT);
    this.limit = DEFAULT_LIMIT;
  }

  /** Creates a set that holds at most {@code limit} elements. */
  public LruCacheSet(int limit) {
    lruCacheMap = new LruCacheMap<>(limit);
    this.limit = limit;
  }

  /** Returns the number of elements in the set. */
  public int size() {
    return lruCacheMap.size();
  }

  public boolean isEmpty() {
    return lruCacheMap.isEmpty();
  }

  /** Returns whether the set contains {@code t}, without refreshing it. */
  public boolean contains(T t) {
    return lruCacheMap.containsKey(t);
  }

  /**
   * Adds or refreshes {@code t}, evicting the oldest element if the set grows past its limit.
   *
   * @return true if {@code t} was not already in the set
   */
  public boolean add(T t) {
    return lruCacheMap.put(t, PRESENT) == null;
  }
}
