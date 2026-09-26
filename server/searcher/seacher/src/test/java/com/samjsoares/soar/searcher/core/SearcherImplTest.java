package com.samjsoares.soar.searcher.core;

import static com.samjsoares.soar.searcher.core.SearchInfos.row;
import static org.assertj.core.api.Assertions.assertThat;

import com.samjsoares.soar.searcher.dao.SearchInfoDao;
import com.samjsoares.soar.searcher.model.SearchInfo;
import com.samjsoares.soar.searcher.model.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SearcherImplTest {

  /** Serves canned rows per term and records every lookup. */
  private static class FakeSearchInfoDao implements SearchInfoDao {
    final Map<String, List<SearchInfo>> rows = new HashMap<>();
    final List<String> queried = new ArrayList<>();

    FakeSearchInfoDao with(String term, SearchInfo... infos) {
      List<SearchInfo> list = new ArrayList<>();
      Collections.addAll(list, infos);
      rows.put(term, list);
      return this;
    }

    @Override
    public List<SearchInfo> getSearchInfo(String term) {
      queried.add(term);
      return rows.get(term);
    }
  }

  private final FakeSearchInfoDao dao = new FakeSearchInfoDao();
  private final Searcher searcher = new SearcherImpl(dao);

  @Test
  public void testSearchesStemmedTermFirst() {
    dao.with("run", row(1, "run", 1, 1));

    List<SearchResult> results = searcher.search("running");

    assertThat(dao.queried).containsExactly("run");
    assertThat(results).extracting("url").containsExactly("http://example.com/1");
  }

  @Test
  public void testFallsBackToRawTermWhenStemHasNoResults() {
    dao.with("running", row(2, "running", 1, 1));

    List<SearchResult> results = searcher.search("running");

    assertThat(dao.queried).containsExactly("run", "running");
    assertThat(results).extracting("url").containsExactly("http://example.com/2");
  }

  @Test
  public void testFallsBackWhenStemReturnsEmptyList() {
    dao.with("run").with("running", row(3, "running", 1, 1));

    assertThat(searcher.search("running")).hasSize(1);
    assertThat(dao.queried).containsExactly("run", "running");
  }

  @Test
  public void testNoResults() {
    assertThat(searcher.search("java")).isEmpty();
  }

  @Test
  public void testNullQueryReturnsEmptyList() {
    assertThat(searcher.search((String) null)).isEmpty();
    assertThat(dao.queried).isEmpty();
  }

  @Test
  public void testSplitsOnSpacesAndPlusAndLowercases() {
    searcher.search("Java+search engine");

    assertThat(dao.queried).containsExactly("java", "java", "search", "search", "engin", "engine");
  }

  @Test
  public void testCombinesResultsAcrossTerms() {
    dao.with("java", row(1, "java", 1, 2), row(2, "java", 1, 2))
        .with("search", row(1, "search", 1, 1));

    List<SearchResult> results = searcher.search(new String[] {"java", "search"});

    assertThat(results)
        .extracting("url")
        .containsOnly("http://example.com/1", "http://example.com/2");
  }
}
