package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.samjsoares.soar.Fixtures;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class DocumentProcessorTest {

  private static final String BASE_URI = "http://example.com/page";
  private static final int MAX_DESCRIPTION = 240;

  private static DocumentProcessor processor(String fixture) {
    return new DocumentProcessor(Fixtures.html(fixture, BASE_URI));
  }

  @Test
  public void testTitle() {
    assertThat(processor("article.html").getTitle()).isEqualTo("Search Engines Explained");
  }

  @Test
  public void testUrlFromDocumentLocation() {
    assertThat(processor("article.html").getUrl()).isEqualTo(BASE_URI);
  }

  @Test
  public void testUrlFromConstructor() {
    Document document = Fixtures.html("article.html", BASE_URI);
    assertThat(new DocumentProcessor("http://other.com/", document).getUrl())
        .isEqualTo("http://other.com/");
  }

  @Test
  public void testDescriptionComesFromFirstParagraph() {
    String description = processor("article.html").getDescription();

    // Known bug: see openspec/changes/fix-known-bugs (#3). The sentence regex never matches, so
    // the whole first paragraph is used, and an ellipsis is always appended. Only assert the
    // parts that hold before and after the fix.
    assertThat(description).startsWith("A search engine crawls pages and builds an index.");
    assertThat(description).doesNotContain("second paragraph");
    assertThat(description.length()).isLessThanOrEqualTo(MAX_DESCRIPTION);
  }

  @Test
  public void testLongDescriptionIsTruncatedWithEllipsis() {
    String description = processor("long-paragraph.html").getDescription();

    assertThat(description.length()).isLessThanOrEqualTo(MAX_DESCRIPTION);
    assertThat(description).startsWith("Crawlers visit pages and follow every link they find.");
    assertThat(description).endsWith("…");
  }

  @Test
  public void testDescriptionFromMetaTag() {
    // Known bug: see openspec/changes/fix-known-bugs (#3). Only the non-standard
    // <meta description> attribute is matched today, not <meta name="description">. The fixture
    // tag has both, so this passes before and after the fix.
    String description = processor("meta-description.html").getDescription();

    assertThat(description).startsWith("Description from the meta tag.");
    assertThat(description).doesNotContain("Paragraph text");
  }

  @Test
  public void testNullDocumentHasEmptyDescription() {
    assertThat(new DocumentProcessor(null).getDescription()).isEmpty();
  }

  @Test
  public void testGetElementsReturnsDocumentChildren() {
    DocumentProcessor processor = processor("article.html");
    assertThat(processor.getElements()).isEqualTo(processor.getDocument().children());
    assertThat(processor.getElements().text()).contains("Search Engines");
  }
}
