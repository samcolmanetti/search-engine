package com.samjsoares.soar.searcher.core;

import com.samjsoares.soar.searcher.model.SearchInfo;

/** Builds {@link SearchInfo} rows for tests. */
final class SearchInfos {

  private SearchInfos() {}

  static SearchInfo row(long docId, String term, int tf, long df) {
    SearchInfo info = new SearchInfo();
    info.setDocId(docId);
    info.setUrl("http://example.com/" + docId);
    info.setTitle("Title " + docId);
    info.setDescription("Description " + docId);
    info.setTerm(term);
    info.setTermFrequency(tf);
    info.setDocumentTermFrequency(df);
    return info;
  }
}
