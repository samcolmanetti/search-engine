package com.samjsoares.soar.core.datastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LRUCacheSetTest {

  @Test
  public void testNewSetIsEmpty() {
    LRUCacheSet<String> set = new LRUCacheSet<>();
    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
    assertFalse(set.contains("a"));
  }

  @Test
  public void testAddReturnsTrueForNewItem() {
    LRUCacheSet<String> set = new LRUCacheSet<>();
    assertTrue(set.add("a"));
    assertTrue(set.contains("a"));
    assertFalse(set.isEmpty());
    assertEquals(1, set.size());
  }

  @Test
  public void testAddReturnsFalseForDuplicate() {
    LRUCacheSet<String> set = new LRUCacheSet<>();
    set.add("a");
    assertFalse(set.add("a"));
    assertEquals(1, set.size());
  }

  @Test
  public void testEvictsLeastRecentlyUsed() {
    LRUCacheSet<String> set = new LRUCacheSet<>(2);
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
    LRUCacheSet<String> set = new LRUCacheSet<>(2);
    set.add("a");
    set.add("b");
    set.contains("a");
    set.add("c");

    assertFalse(set.contains("a"));
    assertTrue(set.contains("b"));
  }

  @Test
  public void testReAddCountsAsAccess() {
    LRUCacheSet<String> set = new LRUCacheSet<>(2);
    set.add("a");
    set.add("b");
    set.add("a");
    set.add("c");

    assertTrue(set.contains("a"));
    assertFalse(set.contains("b"));
  }

  @Test
  public void testDefaultLimitIs128() {
    LRUCacheSet<Integer> set = new LRUCacheSet<>();
    for (int i = 0; i <= 128; i++) {
      set.add(i);
    }

    assertEquals(128, set.size());
    assertFalse(set.contains(0));
    assertTrue(set.contains(128));
  }
}
