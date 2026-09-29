package com.samjsoares.soar.model;

/** A URL waiting to be crawled, with the id of the crawler it is assigned to. */
public class UrlQueue {

  private String url;

  private long crawlerId;

  /** Creates an empty instance to be filled in with setters. */
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
