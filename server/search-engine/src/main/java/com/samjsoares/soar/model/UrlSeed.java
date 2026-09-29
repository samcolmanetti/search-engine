package com.samjsoares.soar.model;

/** A seed URL that crawling starts from, with its id. */
public class UrlSeed {

  private long id;

  private String url;

  /** Creates an empty instance to be filled in with setters. */
  public UrlSeed() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
