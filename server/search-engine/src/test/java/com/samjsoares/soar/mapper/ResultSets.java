package com.samjsoares.soar.mapper;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.Map;

/** Builds a single-row {@link ResultSet} stub without a database or mocking library. */
final class ResultSets {

  private ResultSets() {}

  static ResultSet row(Map<String, Object> columns) {
    return (ResultSet)
        Proxy.newProxyInstance(
            ResultSets.class.getClassLoader(),
            new Class<?>[] {ResultSet.class},
            (proxy, method, args) -> {
              String name = method.getName();
              if (args == null || args.length != 1 || !(args[0] instanceof String)) {
                throw new UnsupportedOperationException(name);
              }
              Object value = columns.get(args[0]);
              switch (name) {
                case "getString":
                  return value == null ? null : value.toString();
                case "getLong":
                  return value == null ? 0L : ((Number) value).longValue();
                case "getInt":
                  return value == null ? 0 : ((Number) value).intValue();
                case "getDouble":
                  return value == null ? 0d : ((Number) value).doubleValue();
                default:
                  throw new UnsupportedOperationException(name);
              }
            });
  }
}
