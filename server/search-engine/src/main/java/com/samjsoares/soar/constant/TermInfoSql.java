package com.samjsoares.soar.constant;

/** SQL statements for the {@code term_info} table. */
public final class TermInfoSql {

  private TermInfoSql() {}

  public static final String UPSERT =
      "insert into term_info (doc_id, term, frequency) values (?,?,?) "
          + "on conflict (doc_id, term) do update set frequency = excluded.frequency";

  public static final String SELECT =
      "select doc_id, term, frequency from term_info where doc_id = ? and term = ? limit 1";

  public static final String SELECT_FROM_DOC =
      "select doc_id, term, frequency from term_info where doc_id = ?";

  public static final String SELECT_ALL =
      "select doc_id, term, frequency from term_info limit 1000";

  public static final String DELETE_ALL_FROM_DOC = "delete from term_info where doc_id = ?";
}
