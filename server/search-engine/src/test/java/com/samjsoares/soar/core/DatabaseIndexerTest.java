package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.samjsoares.soar.Fixtures;
import com.samjsoares.soar.constant.TimeConstants;
import com.samjsoares.soar.dao.DocumentInfoDao;
import com.samjsoares.soar.dao.TermInfoDao;
import com.samjsoares.soar.model.DocumentInfo;
import com.samjsoares.soar.model.TermInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DatabaseIndexerTest {

  private static final String URL = "https://example.com/article?ref=1";
  private static final String CLEAN_URL = "https://example.com/article";

  /** Records upserts and returns a fixed id. */
  private static class FakeDocumentInfoDao implements DocumentInfoDao {
    final List<DocumentInfo> upserted = new ArrayList<>();
    final Map<String, Long> timeIndexed = new HashMap<>();
    long idToReturn = 11L;

    @Override
    public long upsert(String url, long time, String title, String description) {
      DocumentInfo info = new DocumentInfo();
      info.setUrl(url);
      info.setTimeIndexed(time);
      info.setTitle(title);
      info.setDescription(description);
      upserted.add(info);
      return idToReturn;
    }

    @Override
    public long upsert(DocumentInfo documentInfo) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int[] upsert(List<DocumentInfo> documentInfos) {
      throw new UnsupportedOperationException();
    }

    @Override
    public DocumentInfo get(long id) {
      throw new UnsupportedOperationException();
    }

    @Override
    public DocumentInfo get(String url) {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getId(String url) {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getTimeIndexed(String url) {
      Long time = timeIndexed.get(url);
      return time != null ? time : -1;
    }

    @Override
    public List<DocumentInfo> getAll() {
      throw new UnsupportedOperationException();
    }
  }

  /** Records batch upserts. */
  private static class FakeTermInfoDao implements TermInfoDao {
    final List<Long> docIds = new ArrayList<>();
    final List<List<TermInfo>> batches = new ArrayList<>();

    @Override
    public long upsert(TermInfo termInfo) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int upsert(long docId, List<TermInfo> termInfos) {
      docIds.add(docId);
      batches.add(termInfos);
      return termInfos.size();
    }

    @Override
    public TermInfo get(long documentId, String term) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<TermInfo> getAll(long documentId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<TermInfo> getAll() {
      throw new UnsupportedOperationException();
    }
  }

  private final FakeDocumentInfoDao documentInfoDao = new FakeDocumentInfoDao();
  private final FakeTermInfoDao termInfoDao = new FakeTermInfoDao();
  private final DatabaseIndexer indexer = new DatabaseIndexer(documentInfoDao, termInfoDao);

  @Test
  public void testUpsertsDocumentThenTerms() {
    long before = System.currentTimeMillis();
    indexer.indexPage(URL, Fixtures.html("article.html", URL));

    assertThat(documentInfoDao.upserted).hasSize(1);
    DocumentInfo info = documentInfoDao.upserted.get(0);
    assertThat(info.getUrl()).isEqualTo(CLEAN_URL);
    assertThat(info.getTitle()).isEqualTo("Search Engines Explained");
    assertThat(info.getDescription()).startsWith("A search engine crawls pages");
    assertThat(info.getTimeIndexed()).isBetween(before, System.currentTimeMillis());

    assertThat(termInfoDao.docIds).containsExactly(11L);
    List<TermInfo> terms = termInfoDao.batches.get(0);
    assertThat(terms).isNotEmpty();
    assertThat(terms).extracting("docId").containsOnly(11L);
    assertThat(terms).extracting("term").contains("index", "paragraph");
  }

  @Test
  public void testSkipsTermsWhenIdIsZero() {
    documentInfoDao.idToReturn = 0L;
    indexer.indexPage(URL, Fixtures.html("article.html", URL));

    assertThat(documentInfoDao.upserted).hasSize(1);
    assertThat(termInfoDao.batches).isEmpty();
  }

  @Test
  public void testSkipsTermsWhenIdIsNegative() {
    documentInfoDao.idToReturn = -1L;
    indexer.indexPage(URL, Fixtures.html("article.html", URL));

    assertThat(termInfoDao.batches).isEmpty();
  }

  @Test
  public void testNullDocumentIsNoOp() {
    indexer.indexPage(URL, null);

    assertThat(documentInfoDao.upserted).isEmpty();
    assertThat(termInfoDao.batches).isEmpty();
  }

  @Test
  public void testInvalidUrlIsNoOp() {
    indexer.indexPage("ssh://example.com", Fixtures.html("article.html", URL));

    assertThat(documentInfoDao.upserted).isEmpty();
  }

  @Test
  public void testShouldIndexUnknownUrl() {
    assertThat(indexer.shouldIndex(URL)).isTrue();
  }

  @Test
  public void testShouldNotIndexRecentlyIndexedUrl() {
    documentInfoDao.timeIndexed.put(CLEAN_URL, System.currentTimeMillis());

    assertThat(indexer.shouldIndex(URL)).isFalse();
  }

  @Test
  public void testShouldNotIndexPageRecentlyIndexedOverTheOtherScheme() {
    documentInfoDao.timeIndexed.put("http://example.com/article", System.currentTimeMillis());

    assertThat(indexer.shouldIndex(URL)).isFalse();
  }

  @Test
  public void testShouldIndexUrlIndexedOverAWeekAgo() {
    documentInfoDao.timeIndexed.put(
        CLEAN_URL, System.currentTimeMillis() - TimeConstants.MS_PER_WEEK - 1000);

    assertThat(indexer.shouldIndex(URL)).isTrue();
  }

  @Test
  public void testShouldNotIndexInvalidUrl() {
    assertThat(indexer.shouldIndex("ssh://example.com")).isFalse();
  }
}
