package com.samjsoares.soar.core;

import com.samjsoares.soar.model.TermInfo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;

public class DocumentProcessor {

  private final static String SENTENCE_REGEX = "^\\s+[A-Za-z,;'\"\\s]+[.?!]$";
  private final static int MAX_DESCRIPTION = 240;
  private final static String ELLIPSIS = "\u2026";

  private Document document;
  private long documentId;
  private String url;

  public DocumentProcessor(String url, Document document) {
    this.document = document;
    this.url = url;
  }

  public DocumentProcessor(Document document) {
    this.document = document;
    this.url = document != null ? document.location() : "";
  }

  public DocumentProcessor(long documentId, Document document) {
    this.document = document;
    this.documentId = documentId;
  }

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public long getDocumentId() {
    return documentId;
  }

  public void setDocumentId(long documentId) {
    this.documentId = documentId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return document.title();
  }

  public String getDescription() {
    String body = document.body().text();

    if (StringUtils.isEmpty(body)) {
      return "";
    }

    final int indexOfFirst = StringUtils.indexOf(body, SENTENCE_REGEX);
    final int indexOfSecond = StringUtils.indexOf(body, SENTENCE_REGEX, indexOfFirst);

    String description;
    if (indexOfSecond > 0) {
      description = StringUtils.substring(body, 0, indexOfSecond);
    } else if (indexOfFirst > 0) {
      description = StringUtils.substring(body, 0, indexOfFirst);
    } else {
      description = body;
    }

    return truncateWithElipsis(description);
  }

  public Elements getElements() {
    return this.document.children();
  }

  private String truncateWithElipsis (String text) {
    return StringUtils.substring(text, 0, MAX_DESCRIPTION-1) + ELLIPSIS;
  }

}
