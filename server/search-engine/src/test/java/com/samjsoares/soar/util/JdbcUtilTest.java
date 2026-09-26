package com.samjsoares.soar.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcUtilTest {

  private static KeyHolder holder(Map<String, Object> keys) {
    return new GeneratedKeyHolder(Collections.singletonList(keys));
  }

  @Test
  public void testSingleKey() {
    Map<String, Object> keys = new HashMap<>();
    keys.put("id", 42L);

    assertEquals(42L, JdbcUtil.getInsertedId(holder(keys), "id"));
  }

  @Test
  public void testSingleKeyUsesItRegardlessOfName() {
    Map<String, Object> keys = new HashMap<>();
    keys.put("generated", 7);

    assertEquals(7L, JdbcUtil.getInsertedId(holder(keys), "id"));
  }

  @Test
  public void testMultipleKeysIncludingIdColumn() {
    Map<String, Object> keys = new HashMap<>();
    keys.put("id", 5L);
    keys.put("url", "http://example.com/");
    keys.put("time_indexed", 123L);

    assertEquals(5L, JdbcUtil.getInsertedId(holder(keys), "id"));
  }

  @Test
  public void testMultipleKeysWithoutIdColumn() {
    Map<String, Object> keys = new HashMap<>();
    keys.put("url", "http://example.com/");
    keys.put("time_indexed", 123L);

    assertEquals(-1L, JdbcUtil.getInsertedId(holder(keys), "id"));
  }
}
