package com.samjsoares.soar.model;

/** A row of the {@code term_info} table: how many times a term appears in a document. */
public class TermInfo {

  private long docId;

  private String term;

  private int count;

  private String url;

  /** Creates an empty instance to be filled in with setters. */
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
    return "TermInfo{" + "docId=" + docId + ", term='" + term + '\'' + ", count=" + count + '}';
  }
}
