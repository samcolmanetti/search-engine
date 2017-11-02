package com.samjsoares.soar.util;

import org.springframework.jdbc.support.KeyHolder;

public class JdbcUtil {

  public static long getInsertedId (KeyHolder holder, String idColumn) {
    if (holder.getKeys().size() > 1) {
      Long id = (Long) holder.getKeys().get(idColumn);
      return id != null
          ? id
          : -1L;
    }

    return holder.getKey().longValue();
  }
}
