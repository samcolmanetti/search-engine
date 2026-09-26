package com.samjsoares.soar.searcher.core;

import static com.samjsoares.soar.searcher.core.SearchInfos.row;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.samjsoares.soar.searcher.model.SearchResult;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class TermFrequencyRankerTest {

  private static final double EPSILON = 1e-9;

  private static Map<String, SearchResult> byUrl(List<SearchResult> results) {
    Map<String, SearchResult> map = new HashMap<>();
    for (SearchResult result : results) {
      map.put(result.getUrl(), result);
    }
    assertThat(map).as("one result per URL").hasSameSizeAs(results);
    return map;
  }

  @Test
  public void testEmptyRankerReturnsEmptyList() {
    assertThat(new TermFrequencyRanker().getRankedResults()).isEmpty();
  }

  @Test
  public void testSingleTermRelevanceIsTfOverDf() {
    Ranker ranker = new TermFrequencyRanker();
    ranker.add(Arrays.asList(row(1, "java", 3, 10), row(2, "java", 7, 10)));

    Map<String, SearchResult> results = byUrl(ranker.getRankedResults());

    assertThat(results).containsOnlyKeys("http://example.com/1", "http://example.com/2");
    assertThat(results.get("http://example.com/1").getRelevance()).isCloseTo(0.3, within(EPSILON));
    assertThat(results.get("http://example.com/2").getRelevance()).isCloseTo(0.7, within(EPSILON));
  }

  @Test
  public void testEntriesForSameDocAreGroupedAndWeightedByMatchCount() {
    Ranker ranker = new TermFrequencyRanker();
    ranker.add(Arrays.asList(row(1, "java", 2, 10), row(2, "java", 5, 10)));
    ranker.add(Arrays.asList(row(1, "search", 1, 4)));

    Map<String, SearchResult> results = byUrl(ranker.getRankedResults());

    assertThat(results).hasSize(2);
    // (2/10 + 1/4) * 2 matched terms
    assertThat(results.get("http://example.com/1").getRelevance())
        .isCloseTo((0.2 + 0.25) * 2, within(EPSILON));
    assertThat(results.get("http://example.com/2").getRelevance()).isCloseTo(0.5, within(EPSILON));
  }

  @Test
  public void testResultsAreSortedByRelevanceDescending() {
    Ranker ranker = new TermFrequencyRanker();
    ranker.add(
        Arrays.asList(
            row(1, "java", 1, 10),
            row(2, "java", 2, 10),
            row(3, "java", 3, 10),
            row(4, "java", 4, 10),
            row(5, "java", 5, 10)));

    List<SearchResult> results = ranker.getRankedResults();

    assertThat(results)
        .extracting(SearchResult::getUrl)
        .containsExactly(
            "http://example.com/5",
            "http://example.com/4",
            "http://example.com/3",
            "http://example.com/2",
            "http://example.com/1");
  }

  @Test
  public void testEqualRelevanceKeepsAllResults() {
    Ranker ranker = new TermFrequencyRanker();
    ranker.add(Arrays.asList(row(1, "java", 1, 2), row(2, "java", 1, 2), row(3, "java", 1, 2)));

    assertThat(byUrl(ranker.getRankedResults())).hasSize(3);
  }

  @Test
  public void testResultCopiesDocumentFields() {
    Ranker ranker = new TermFrequencyRanker();
    ranker.add(Arrays.asList(row(4, "java", 1, 1)));

    SearchResult result = ranker.getRankedResults().get(0);

    assertThat(result.getUrl()).isEqualTo("http://example.com/4");
    assertThat(result.getTitle()).isEqualTo("Title 4");
    assertThat(result.getDescription()).isEqualTo("Description 4");
  }
}
