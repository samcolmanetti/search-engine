package com.samjsoares.soar.searcher.model;

public class SearchResult {
  private String url;
  private double relevance;
  private String title;
  private String description;

  public SearchResult() {}

  public SearchResult(String url, double relevance, String title, String description) {
    this.url = url;
    this.relevance = relevance;
    this.title = title;
    this.description = description;
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
}
