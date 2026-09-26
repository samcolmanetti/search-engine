package com.samjsoares.soar.core;

import java.util.Arrays;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocumentProcessor {

  /** Whitespace that follows the end of a sentence. */
  private static final Pattern SENTENCE_BREAK = Pattern.compile("(?<=[.?!])\\s+");

  private static final int MAX_SENTENCES = 2;
  private static final int MAX_DESCRIPTION = 240;
  private static final String ELLIPSIS = "\u2026";

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
    return document != null ? document.title() : "";
  }

  /**
   * Returns the page's meta description, or else the first one or two sentences of its first
   * paragraph. Text longer than {@value #MAX_DESCRIPTION} characters is cut short with an ellipsis.
   */
  public String getDescription() {
    if (document == null) {
      return "";
    }

    String metaDescription = getDescriptionFromMetaTag();
    if (metaDescription != null) {
      return truncateWithEllipsis(metaDescription);
    }

    Element body = document.body();
    Element firstParagraph = body != null ? body.select("p").first() : null;
    if (firstParagraph == null) {
      return "";
    }

    return truncateWithEllipsis(firstSentences(firstParagraph.text()));
  }

  private static String firstSentences(String text) {
    String trimmed = text.trim();
    String[] sentences = SENTENCE_BREAK.split(trimmed, MAX_SENTENCES + 1);
    if (sentences.length <= MAX_SENTENCES) {
      return trimmed;
    }

    return String.join(" ", Arrays.copyOf(sentences, MAX_SENTENCES));
  }

  private String getDescriptionFromMetaTag() {
    Elements elements = document.select("meta[name=description]");
    for (Element element : elements) {
      if (element.hasAttr("content")) {
        return element.attr("content");
      }
    }

    return null;
  }

  public Elements getElements() {
    return this.document.children();
  }

  private static String truncateWithEllipsis(String text) {
    if (text.length() <= MAX_DESCRIPTION) {
      return text;
    }

    return StringUtils.substring(text, 0, MAX_DESCRIPTION - 1) + ELLIPSIS;
  }
}
