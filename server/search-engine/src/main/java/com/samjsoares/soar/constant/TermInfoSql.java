package com.samjsoares.soar.constant;

public class TermInfoSql {

  public static final String UPSERT =
      "insert into term_info (doc_id, term, frequency) values (?,?,?) "
          + "on conflict (doc_id, term) do update set frequency = excluded.frequency";

  public static final String SELECT =
      "select from term_info (doc_id, term, frequency) " + "where doc_id = ? and term = ? LIMIT 1";

  public static final String SELECT_FROM_DOC =
      "select from term_info (doc_id, term, frequency) where doc_id = ?";

  public static final String SELECT_ALL =
      "select from term_info (doc_id, term, frequency) limit 1000";

  public static final String DELETE_ALL_FROM_DOC = "delete from term_info where doc_id = ?";
}
