package com.samjsoares.soar.model;

/** A URL paired with the time, in milliseconds, it was last requested. */
public class DocFetchTime {

  private String url;

  private long lastRequest;

  /** Creates an empty instance to be filled in with setters. */
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
