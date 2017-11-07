package com.samjsoares.soar.searcher.searcher.constant;

public class SearcherSQL {
  public final static String SELECT_BY_TERM = "select url, page_rank, term, count " +
      " from doc_info" +
      "    join term_info on term_info.doc_id = doc_info.id" +
      "    where term = ?" +
      "    order by count desc" +
      "   limit 15";

}
