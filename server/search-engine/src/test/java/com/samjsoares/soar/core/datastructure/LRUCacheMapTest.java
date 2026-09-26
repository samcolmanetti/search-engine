package com.samjsoares.soar.core.datastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class LRUCacheMapTest {

  @Test
  public void testEvictsOldestInsertedWhenNotAccessed() {
    Map<String, Integer> cache = new LRUCacheMap<>(2);
    cache.put("a", 1);
    cache.put("b", 2);
    cache.put("c", 3);

    assertThat(cache).containsOnlyKeys("b", "c");
  }

  @Test
  public void testEvictsLeastRecentlyAccessed() {
    Map<String, Integer> cache = new LRUCacheMap<>(2);
    cache.put("a", 1);
    cache.put("b", 2);
    cache.get("a");
    cache.put("c", 3);

    assertThat(cache).containsOnlyKeys("a", "c");
  }

  @Test
  public void testUpdatingValueDoesNotGrow() {
    Map<String, Integer> cache = new LRUCacheMap<>(2);
    cache.put("a", 1);
    cache.put("a", 2);

    assertThat(cache).hasSize(1).containsEntry("a", 2);
  }

  @Test
  public void testDefaultLimitIs128() {
    Map<Integer, Integer> cache = new LRUCacheMap<>();
    for (int i = 0; i < 128; i++) {
      cache.put(i, i);
    }
    assertThat(cache).hasSize(128).containsKey(0);

    cache.put(128, 128);
    assertThat(cache).hasSize(128).doesNotContainKey(0).containsKey(128);
  }
}
