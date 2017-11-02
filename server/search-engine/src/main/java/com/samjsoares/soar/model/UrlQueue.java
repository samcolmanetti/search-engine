package com.samjsoares.soar.model;

public class UrlQueue {

  private String url;

  private long crawlerId;

  public UrlQueue() {}

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getCrawlerId() {
    return crawlerId;
  }

  public void setCrawlerId(long crawlerId) {
    this.crawlerId = crawlerId;
  }
}
