package com.samjsoares.searchengine.model;

import org.knowm.yank.annotations.Column;

public class UrlQueue {

  @Column("url")
  private String url;

  @Column("crawler_id")
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
