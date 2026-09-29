package com.samjsoares.soar.util;

import org.springframework.jdbc.support.KeyHolder;

/** Static helpers for Spring JDBC. */
public final class JdbcUtil {

  private JdbcUtil() {}

  /**
   * Returns the generated id of the inserted row. When the insert returned several keys, reads the
   * one in {@code idColumn} and returns -1 if it is missing.
   */
  public static long getInsertedId(KeyHolder holder, String idColumn) {
    if (holder.getKeys().size() > 1) {
      Long id = (Long) holder.getKeys().get(idColumn);
      return id != null ? id : -1L;
    }

    return holder.getKey().longValue();
  }
}
