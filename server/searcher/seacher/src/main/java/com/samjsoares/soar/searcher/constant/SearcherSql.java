package com.samjsoares.soar.searcher.constant;

/** SQL statements used by the searcher. */
public final class SearcherSql {

  private SearcherSql() {}

  public static final String SELECT_BY_TERM =
      "select doc_id, url, page_rank, term, frequency, title, description, "
          + " (select sum(frequency) from term_info where term = ?) as doc_term_freq "
          + " from doc_info"
          + "    join term_info on term_info.doc_id = doc_info.id"
          + "    where term = ?"
          + "    order by frequency desc"
          + "   limit 150";
}
