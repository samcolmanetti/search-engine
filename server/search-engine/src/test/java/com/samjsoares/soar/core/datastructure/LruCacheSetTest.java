package com.samjsoares.soar.core.datastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LruCacheSetTest {

  @Test
  public void testNewSetIsEmpty() {
    LruCacheSet<String> set = new LruCacheSet<>();
    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
    assertFalse(set.contains("a"));
  }

  @Test
  public void testAddReturnsTrueForNewItem() {
    LruCacheSet<String> set = new LruCacheSet<>();
    assertTrue(set.add("a"));
    assertTrue(set.contains("a"));
    assertFalse(set.isEmpty());
    assertEquals(1, set.size());
  }

  @Test
  public void testAddReturnsFalseForDuplicate() {
    LruCacheSet<String> set = new LruCacheSet<>();
    set.add("a");
    assertFalse(set.add("a"));
    assertEquals(1, set.size());
  }

  @Test
  public void testEvictsLeastRecentlyUsed() {
    LruCacheSet<String> set = new LruCacheSet<>(2);
    set.add("a");
    set.add("b");
    set.add("c");

    assertEquals(2, set.size());
    assertFalse(set.contains("a"));
    assertTrue(set.contains("b"));
    assertTrue(set.contains("c"));
  }

  @Test
  public void testContainsDoesNotCountAsAccess() {
    // contains() uses containsKey(), which does not update a LinkedHashMap's access order.
    LruCacheSet<String> set = new LruCacheSet<>(2);
    set.add("a");
    set.add("b");
    set.contains("a");
    set.add("c");

    assertFalse(set.contains("a"));
    assertTrue(set.contains("b"));
  }

  @Test
  public void testReAddCountsAsAccess() {
    LruCacheSet<String> set = new LruCacheSet<>(2);
    set.add("a");
    set.add("b");
    set.add("a");
    set.add("c");

    assertTrue(set.contains("a"));
    assertFalse(set.contains("b"));
  }

  @Test
  public void testDefaultLimitIs128() {
    LruCacheSet<Integer> set = new LruCacheSet<>();
    for (int i = 0; i <= 128; i++) {
      set.add(i);
    }

    assertEquals(128, set.size());
    assertFalse(set.contains(0));
    assertTrue(set.contains(128));
  }
}
