package com.samjsoares.searchengine.model;

import org.knowm.yank.annotations.Column;

public class DocumentInfo {
  @Column("id")
  private long id;

  @Column("url")
  private String url;

  @Column("time_indexed")
  private long timeIndexed;

  public DocumentInfo() {}

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

  public long getTimeIndexed() {
    return timeIndexed;
  }

  public void setTimeIndexed(long timeIndexed) {
    this.timeIndexed = timeIndexed;
  }
}
