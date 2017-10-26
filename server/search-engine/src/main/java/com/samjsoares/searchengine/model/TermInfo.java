package com.samjsoares.searchengine.model;

import org.knowm.yank.annotations.Column;

public class TermInfo {

  @Column("doc_id")
  private long docId;

  @Column("term")
  private String term;

  @Column("count")
  private int count;

  private String url;

  public TermInfo() {}

  public long getDocId() {
    return docId;
  }

  public void setDocId(long docId) {
    this.docId = docId;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "TermInfo{" +
        "docId=" + docId +
        ", term='" + term + '\'' +
        ", count=" + count +
        '}';
  }
}
