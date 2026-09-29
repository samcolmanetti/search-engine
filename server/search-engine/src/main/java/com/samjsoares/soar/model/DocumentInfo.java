package com.samjsoares.soar.model;

/**
 * A row of the {@code doc_info} table: an indexed page's id, URL, index time, title, and
 * description.
 */
public class DocumentInfo {
  private long id;
  private String url;
  private long timeIndexed;
  private String title;
  private String description;

  /** Creates an empty instance to be filled in with setters. */
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

  @Override
  public String toString() {
    return "DocumentInfo{"
        + "id="
        + id
        + ", url='"
        + url
        + '\''
        + ", timeIndexed="
        + timeIndexed
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
