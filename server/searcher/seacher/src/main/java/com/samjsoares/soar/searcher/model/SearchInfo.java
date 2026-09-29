package com.samjsoares.soar.searcher.model;

/**
 * One indexed term's occurrence in a document: the document's ID, URL, title, description and page
 * rank, the term, its frequency in the document, and its total frequency across all documents.
 */
public class SearchInfo {

  private long docId;
  private String url;
  private String term;
  private int termFrequency;
  private double pageRank;
  private String title;
  private String description;
  private long documentTermFrequency;

  /** Creates an empty record whose fields are set through the setters. */
  public SearchInfo() {}

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getDocId() {
    return docId;
  }

  public void setDocId(long docId) {
    this.docId = docId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public int getTermFrequency() {
    return termFrequency;
  }

  public void setTermFrequency(int termFrequency) {
    this.termFrequency = termFrequency;
  }

  public double getPageRank() {
    return pageRank;
  }

  public void setPageRank(double pageRank) {
    this.pageRank = pageRank;
  }

  public long getDocumentTermFrequency() {
    return documentTermFrequency;
  }

  public void setDocumentTermFrequency(long documentTermFrequency) {
    this.documentTermFrequency = documentTermFrequency;
  }
}
