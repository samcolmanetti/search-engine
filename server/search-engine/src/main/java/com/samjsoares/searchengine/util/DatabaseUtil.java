package com.samjsoares.searchengine.util;

import org.knowm.yank.PropertiesUtils;
import org.knowm.yank.Yank;

import java.util.Properties;

public class DatabaseUtil {

  private final static Properties dbProps
      = PropertiesUtils.getPropertiesFromClasspath("properties/POSTGRES_DB.properties");

  private final static Properties sqlProps
      = PropertiesUtils.getPropertiesFromClasspath("properties/SQL_SCRIPTS.properties");

  public static void setUpDatabase() {
    if (Yank.getDefaultConnectionPool() != null && !Yank.getDefaultConnectionPool().isClosed()) {
      return;
    }

    // setup connection pool
    Yank.setupDefaultConnectionPool(dbProps);
    Yank.addSQLStatements(sqlProps);
  }

  public static void releaseConnection() {
    Yank.releaseDefaultConnectionPool();
  }
}
