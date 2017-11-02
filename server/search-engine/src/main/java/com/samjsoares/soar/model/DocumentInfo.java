package com.samjsoares.soar.model;

public class DocumentInfo {
  private long id;

  private String url;

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

  @Override
  public String toString() {
    return "DocumentInfo{" +
        "id=" + id +
        ", url='" + url + '\'' +
        ", timeIndexed=" + timeIndexed +
        '}';
  }
}
