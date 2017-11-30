package com.samjsoares.soar.searcher.model;

public class SearchInfo {

  private long docId;
  private String url;
  private String term;
  private int termFrequency;
  private double pageRank;
  private String title;
  private String description;
  private long documentTermFrequency;

  public SearchInfo() {}

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public int getTermFrequency() {
    return termFrequency;
  }

  public void setTermFrequency(int termFrequency) {
    this.termFrequency = termFrequency;
  }

  public double getPageRank() {
    return pageRank;
  }

  public void setPageRank(double pageRank) {
    this.pageRank = pageRank;
  }

  public long getDocumentTermFrequency() {
    return documentTermFrequency;
  }

  public void setDocumentTermFrequency(long documentTermFrequency) {
    this.documentTermFrequency = documentTermFrequency;
  }
}
