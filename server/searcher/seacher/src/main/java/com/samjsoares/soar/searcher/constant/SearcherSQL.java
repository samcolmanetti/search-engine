package com.samjsoares.soar.searcher.constant;

public class SearcherSQL {
  public final static String SELECT_BY_TERM = "select doc_id, url, page_rank, term, frequency, title, description " +
      " from doc_info" +
      "    join term_info on term_info.doc_id = doc_info.id" +
      "    where term = ?" +
      "    order by frequency desc" +
      "   limit 150";
}
