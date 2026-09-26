package com.samjsoares.soar.searcher.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.Gson;
import com.samjsoares.soar.searcher.core.Searcher;
import com.samjsoares.soar.searcher.model.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class SearchControllerTest {

  /** Returns one fixed result and records queries. */
  private static class FakeSearcher implements Searcher {
    final List<String> queries = new ArrayList<>();

    @Override
    public List<SearchResult> search(String[] terms) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<SearchResult> search(String query) {
      queries.add(query);
      return Collections.singletonList(
          new SearchResult("http://example.com/", 0.5, "Example", "An example page"));
    }
  }

  private final FakeSearcher searcher = new FakeSearcher();
  private final SearchController controller = new SearchController();

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(controller, "searcher", searcher);
  }

  @Test
  public void testEmptyQueryReturnsEmptyObject() {
    assertThat(controller.search("")).isEqualTo("{}");
    assertThat(searcher.queries).isEmpty();
  }

  @Test
  public void testNullQueryReturnsEmptyObject() {
    assertThat(controller.search(null)).isEqualTo("{}");
    assertThat(searcher.queries).isEmpty();
  }

  @Test
  public void testQueryReturnsResultsAsJson() {
    String json = controller.search("example");

    assertThat(searcher.queries).containsExactly("example");
    SearchResult[] results = new Gson().fromJson(json, SearchResult[].class);
    assertThat(results).hasSize(1);
    assertThat(results[0].getUrl()).isEqualTo("http://example.com/");
    assertThat(results[0].getRelevance()).isEqualTo(0.5);
    assertThat(results[0].getTitle()).isEqualTo("Example");
    assertThat(results[0].getDescription()).isEqualTo("An example page");
  }
}
