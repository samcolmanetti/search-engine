package com.samjsoares.soar.searcher.searcher.model;

public class SearchResult {
  private String url;
  private double relevance;

  public SearchResult() {}

  public SearchResult(String url, double relevance) {
    this.url = url;
    this.relevance = relevance;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public double getRelevance() {
    return relevance;
  }

  public void setRelevance(double relevance) {
    this.relevance = relevance;
  }
}
