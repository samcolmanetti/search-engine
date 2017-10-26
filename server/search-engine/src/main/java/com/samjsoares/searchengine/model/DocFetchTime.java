package com.samjsoares.searchengine.model;

import org.knowm.yank.annotations.Column;

public class DocFetchTime {
  @Column("url")
  private String url;

  @Column("last_request")
  private long lastRequest;

  public DocFetchTime() {}

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(long lastRequest) {
    this.lastRequest = lastRequest;
  }
}
