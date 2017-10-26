package com.samjsoares.searchengine.driver;

import org.knowm.yank.PropertiesUtils;
import org.knowm.yank.Yank;

import java.util.Properties;

public class SimpleDbDriver {

  public static void runSimpleRead() {
    // Connection Pool Properties
    Properties dbProps = PropertiesUtils.getPropertiesFromClasspath("properties/POSTGRES_DB.properties");

    // setup connection pool
    Yank.setupDefaultConnectionPool(dbProps);

    String sql = "select url from url_seed where id = ?";
    Object[] params = new Object[] { 1000 };
    String result = Yank.queryScalar(sql, String.class, params);

    System.out.println(result);

    // release connection pool
    Yank.releaseDefaultConnectionPool();
  }

  public static void runSimpleInsert() {
    // Connection Pool Properties
    Properties dbProps = PropertiesUtils.getPropertiesFromClasspath("properties/POSTGRES_DB.properties");
    Properties sqlProps = PropertiesUtils.getPropertiesFromClasspath("properties/SQL_SCRIPTS.properties");

    // setup connection pool
    Yank.setupDefaultConnectionPool(dbProps);
    Yank.addSQLStatements(sqlProps);

    Object[] params = new Object[] { "joshdavid.com", System.currentTimeMillis()};
    try {
      Long id = Yank.insertSQLKey("DOC_INFO_UPSERT", params);
      System.out.println("id = " + id);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // release connection pool
    Yank.releaseDefaultConnectionPool();
  }



  public static void main (String[] args) {
    runSimpleInsert();
  }
}
