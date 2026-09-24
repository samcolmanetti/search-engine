package com.samjsoares.soar.constant;

public class DocumentInfoSql {

  public static final String UPSERT =
      "insert into doc_info (url, time_indexed, title, description) "
          + " values (?,?,?,?) on conflict (url) do update "
          + " set time_indexed = excluded.time_indexed, title = excluded.title, description =  excluded.description";

  public static final String SELECT_WITH_ID =
      "select id, url, time_indexed from doc_info where id = ? limit 1";

  public static final String SELECT_WITH_URL =
      "select id, url, time_indexed from doc_info where url = ? limit 1";

  public static final String SELECT_ID = "select id from doc_info where url = ? limit 1";

  public static final String SELECT_TIME_INDEXED =
      "select time_indexed from doc_info where url = ? limit 1";

  public static final String SELECT_ALL = "select id, url, time_indexed from doc_info LIMIT 1000";
}
