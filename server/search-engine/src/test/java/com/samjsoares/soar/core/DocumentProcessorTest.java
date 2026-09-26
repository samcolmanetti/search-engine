package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.samjsoares.soar.Fixtures;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

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
  public void testDescriptionIsFirstParagraphWhenItHasTwoSentences() {
    assertThat(processor("article.html").getDescription())
        .isEqualTo(
            "A search engine crawls pages and builds an index. "
                + "It then ranks the pages for each query.");
  }

  @Test
  public void testDescriptionStopsAfterTwoSentences() {
    assertThat(processor("long-paragraph.html").getDescription())
        .isEqualTo(
            "Crawlers visit pages and follow every link they find. "
                + "Crawlers visit pages and follow every link they find.");
  }

  @Test
  public void testLongSentenceIsTruncatedWithEllipsis() {
    String description = processor("long-sentence.html").getDescription();

    assertThat(description).hasSize(MAX_DESCRIPTION);
    assertThat(description).startsWith("This single sentence keeps going");
    assertThat(description).endsWith("…");
  }

  @Test
  public void testDescriptionFromMetaTag() {
    assertThat(processor("meta-description.html").getDescription())
        .isEqualTo("Description from the meta tag.");
  }

  @Test
  public void testPageWithoutParagraphsHasEmptyDescription() {
    assertThat(processor("no-paragraph.html").getDescription()).isEmpty();
  }

  @Test
  public void testNullDocumentHasEmptyTitle() {
    assertThat(new DocumentProcessor(null).getTitle()).isEmpty();
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
