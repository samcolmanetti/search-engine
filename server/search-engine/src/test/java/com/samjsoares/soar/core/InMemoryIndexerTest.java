package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

public class InMemoryIndexerTest {

  private static final String URL = "https://example.com/page?q=1";
  private static final String CLEAN_URL = "https://example.com/page";

  private static Document document(String text) {
    return Jsoup.parse("<html><body><p>" + text + "</p></body></html>");
  }

  @Test
  public void testIndexPageAddsTerms() {
    InMemoryIndexer indexer = new InMemoryIndexer();
    indexer.indexPage(URL, document("java search java"));

    Set<TermProcessor> processors = indexer.get("java");
    assertThat(processors).hasSize(1);
    assertThat(processors.iterator().next().getUrl()).isEqualTo(CLEAN_URL);
    assertThat(indexer.get("search")).isEqualTo(processors);
    assertThat(indexer.get("python")).isNull();
  }

  @Test
  public void testGetCounts() {
    InMemoryIndexer indexer = new InMemoryIndexer();
    indexer.indexPage("http://example.com/a", document("java search java"));
    indexer.indexPage("http://example.com/b", document("java"));

    Map<String, Integer> counts = indexer.getCounts("java");
    assertThat(counts).hasSize(2);
    assertThat(counts.get("http://example.com/a")).isEqualTo(2);
    assertThat(counts.get("http://example.com/b")).isEqualTo(1);
    assertThat(indexer.getCounts("python")).isEmpty();
  }

  @Test
  public void testIndexIsKeyedByStem() {
    InMemoryIndexer indexer = new InMemoryIndexer();
    indexer.indexPage(URL, document("crawling crawled"));

    assertThat(indexer.get("crawl")).hasSize(1);
    assertThat(indexer.getCounts("crawl")).containsEntry(CLEAN_URL, 2);
  }

  @Test
  public void testShouldIndexNewUrl() {
    assertThat(new InMemoryIndexer().shouldIndex(URL)).isTrue();
  }

  @Test
  public void testShouldNotIndexRightAfterIndexing() {
    InMemoryIndexer indexer = new InMemoryIndexer();
    indexer.indexPage(URL, document("java"));

    assertThat(indexer.shouldIndex(URL)).isFalse();
    assertThat(indexer.shouldIndex(CLEAN_URL)).isFalse();
    // The http version of the same page counts as indexed too.
    assertThat(indexer.shouldIndex("http://example.com/page")).isFalse();
  }

  @Test
  public void testShouldNotIndexInvalidUrl() {
    assertThat(new InMemoryIndexer().shouldIndex("ssh://example.com")).isFalse();
  }

  @Test
  public void testNullDocumentIsNoOp() {
    InMemoryIndexer indexer = new InMemoryIndexer();
    indexer.indexPage(URL, null);

    assertThat(indexer.shouldIndex(URL)).isTrue();
  }

  @Test
  public void testInvalidUrlIsNotIndexed() {
    InMemoryIndexer indexer = new InMemoryIndexer();
    indexer.indexPage("ssh://example.com", document("java"));

    assertThat(indexer.get("java")).isNull();
  }
}
