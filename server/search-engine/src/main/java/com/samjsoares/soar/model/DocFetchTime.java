package com.samjsoares.soar.model;

public class DocFetchTime {

  private String url;

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
