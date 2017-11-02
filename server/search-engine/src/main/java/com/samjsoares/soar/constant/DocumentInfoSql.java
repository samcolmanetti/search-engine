package com.samjsoares.soar.constant;

public class DocumentInfoSql {

  public final static String UPSERT = "insert into doc_info (url, time_indexed) values (?,?) " +
      "on conflict (url) do update set time_indexed = excluded.time_indexed";

  public final static String SELECT_WITH_ID = "select id, url, time_indexed from doc_info where id = ? limit 1";

  public final static String SELECT_WITH_URL = "select id, url, time_indexed from doc_info where url = ? limit 1";

  public final static String SELECT_ID = "select id from doc_info where url = ? limit 1";

  public final static String SELECT_ALL = "select id, url, time_indexed from doc_info LIMIT 1000";

}
