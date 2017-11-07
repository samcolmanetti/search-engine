package com.samjsoares.soar.searcher.searcher.model;

public class SearchInfo {

  private long docId;
  private String url;
  private String term;
  private int termCount;
  private double pageRank;

  public SearchInfo() {}

  public SearchInfo(long docId, String url, String term, int termCount, double pageRank) {
    this.docId = docId;
    this.url = url;
    this.term = term;
    this.termCount = termCount;
    this.pageRank = pageRank;
  }

  public long getDocId() {
    return docId;
  }

  public void setDocId(long docId) {
    this.docId = docId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public int getTermCount() {
    return termCount;
  }

  public void setTermCount(int termCount) {
    this.termCount = termCount;
  }

  public double getPageRank() {
    return pageRank;
  }

  public void setPageRank(double pageRank) {
    this.pageRank = pageRank;
  }
}
