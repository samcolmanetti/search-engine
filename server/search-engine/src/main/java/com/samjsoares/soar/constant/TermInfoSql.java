package com.samjsoares.soar.constant;

public class TermInfoSql {

  public final static String UPSERT = "insert into term_info (doc_id, term, frequency) values (?,?,?) " +
      "on conflict (doc_id, term) do update set frequency = excluded.frequency";

  public final static String SELECT = "select from term_info (doc_id, term, frequency) " +
      "where doc_id = ? and term = ? LIMIT 1";

  public final static String SELECT_FROM_DOC = "select from term_info (doc_id, term, frequency) where doc_id = ?";

  public final static String SELECT_ALL = "select from term_info (doc_id, term, frequency) limit 1000";

  public final static String DELETE_ALL_FROM_DOC = "delete from term_info where doc_id = ?";

}
