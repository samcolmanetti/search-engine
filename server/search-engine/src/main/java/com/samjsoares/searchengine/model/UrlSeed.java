package com.samjsoares.searchengine.model;

import org.knowm.yank.annotations.Column;

public class UrlSeed {

  @Column("id")
  private long id;

  @Column("url")
  private String url;

  public UrlSeed () {}

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
