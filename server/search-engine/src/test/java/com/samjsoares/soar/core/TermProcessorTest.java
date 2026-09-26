package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.samjsoares.soar.model.TermInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.junit.Test;

public class TermProcessorTest {

  private static TermProcessor process(String bodyHtml) {
    TermProcessor processor = new TermProcessor(7L);
    processor.processElements(Jsoup.parse("<html><body>" + bodyHtml + "</body></html>").children());
    return processor;
  }

  private static Map<String, Integer> countsByTerm(TermProcessor processor) {
    Map<String, Integer> counts = new HashMap<>();
    for (TermInfo termInfo : processor.getTermInfos()) {
      counts.put(termInfo.getTerm(), termInfo.getCount());
    }
    return counts;
  }

  @Test
  public void testCountsWords() {
    TermProcessor processor = process("<p>java search java</p><div>search java</div>");

    assertThat(countsByTerm(processor)).containsOnly(entry("java", 3), entry("search", 2));
  }

  @Test
  public void testIgnoresStopWordsAndSingleCharacters() {
    TermProcessor processor = process("<p>the java and x of a b search</p>");

    assertThat(countsByTerm(processor)).containsOnlyKeys("java", "search");
  }

  @Test
  public void testStripsPunctuationAndLowercases() {
    TermProcessor processor = process("<p>Java, JAVA! java? (Search).</p>");

    assertThat(countsByTerm(processor)).containsOnly(entry("java", 3), entry("search", 1));
  }

  @Test
  public void testMergesWordsWithSameStem() {
    TermProcessor processor = process("<p>crawling crawled crawl</p>");
    List<TermInfo> termInfos = processor.getTermInfos();

    assertThat(termInfos).hasSize(1);
    assertThat(termInfos.get(0).getCount()).isEqualTo(3);
    // The stem is stored, so every page indexes these words under the same term.
    assertThat(termInfos.get(0).getTerm()).isEqualTo("crawl");
    assertThat(processor.get("crawls").getCount()).isEqualTo(3);
  }

  @Test
  public void testStoredTermDoesNotDependOnWordOrder() {
    assertThat(process("<p>universities</p>").getTermInfos().get(0).getTerm())
        .isEqualTo(process("<p>university</p>").getTermInfos().get(0).getTerm());
  }

  @Test
  public void testSetsDocumentId() {
    for (TermInfo termInfo : process("<p>java search</p>").getTermInfos()) {
      assertThat(termInfo.getDocId()).isEqualTo(7L);
    }
  }

  @Test
  public void testUnseenTermCountsZero() {
    TermProcessor processor = process("<p>java</p>");

    assertThat(processor.get("python")).isNull();
    assertThat(processor.getTermCount("python")).isEqualTo(0);
  }

  @Test
  public void testNullElementsIsNoOp() {
    TermProcessor processor = new TermProcessor("http://example.com/");
    processor.processElements(null);

    assertThat(processor.getTermInfos()).isEmpty();
    assertThat(processor.getUrl()).isEqualTo("http://example.com/");
  }
}
