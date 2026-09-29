package com.samjsoares.soar.core.datastructure;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map that removes its least recently accessed entry when an insertion takes it past the size
 * limit. Both {@code get} and {@code put} count as access.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LruCacheMap<K, V> extends LinkedHashMap<K, V> {
  private static final int DEFAULT_LIMIT = 128;
  private final int limit;

  /** Creates a map that holds at most 128 entries. */
  public LruCacheMap() {
    super(16, 0.75f, true);
    this.limit = DEFAULT_LIMIT;
  }

  /** Creates a map that holds at most {@code limit} entries. */
  public LruCacheMap(int limit) {
    super(16, 0.75f, true);
    this.limit = limit;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > limit;
  }
}
