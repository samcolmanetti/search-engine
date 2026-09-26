package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.samjsoares.soar.Fixtures;
import com.samjsoares.soar.util.UrlUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class CrawlerTest {

  private static final String HOME = "http://example.com/";
  private static final String PAGE_A = "http://example.com/a";
  private static final String PAGE_B = "http://example.com/b";

  /** Serves fixture documents by URL instead of downloading them. */
  private static class FakeFetcher extends Fetcher {
    final Map<String, String> fixtures = new HashMap<>();
    final List<String> fetched = new ArrayList<>();

    FakeFetcher serve(String url, String fixture) {
      fixtures.put(url, fixture);
      return this;
    }

    @Override
    public Document fetchDocument(String url) {
      fetched.add(url);
      String fixture = fixtures.get(url);
      return fixture == null ? null : Fixtures.html(fixture, url);
    }

    @Override
    public Elements fetch(String url) {
      Document document = fetchDocument(url);
      return document == null ? null : document.children();
    }
  }

  /** Allows everything except the given paths. */
  private static class FakeRobotsHandler extends RobotsHandler {
    final Set<String> disallowedPaths = new HashSet<>();

    @Override
    public boolean isAllowed(URL url) {
      if (url == null) {
        // Same as the real RobotsHandler.
        return false;
      }
      return !disallowedPaths.contains(url.getPath());
    }
  }

  /** Records indexed pages; treats the given URLs as already indexed. */
  private static class RecordingIndexer extends InMemoryIndexer {
    final List<String> indexed = new ArrayList<>();
    final Set<String> alreadyIndexed = new HashSet<>();

    @Override
    public void indexPage(String url, Document document) {
      indexed.add(url);
      super.indexPage(url, document);
    }

    @Override
    public boolean shouldIndex(String url) {
      return !alreadyIndexed.contains(url) && super.shouldIndex(url);
    }
  }

  private final FakeFetcher fetcher = new FakeFetcher();
  private final FakeRobotsHandler robotsHandler = new FakeRobotsHandler();
  private final RecordingIndexer indexer = new RecordingIndexer();

  /** Seed server that hands out the given URLs once each, then null. */
  private static URLServer seeds(String... urls) {
    List<String> remaining = new ArrayList<>();
    for (String url : urls) {
      remaining.add(url);
    }
    return () -> remaining.isEmpty() ? null : UrlUtil.getCleanUrl(remaining.remove(0));
  }

  private Crawler crawler(URLServer urlServer) {
    return new Crawler(indexer, fetcher, robotsHandler, urlServer);
  }

  @Test
  public void testEmptyQueueWithNoSeedReturnsFalse() {
    Crawler crawler = crawler(seeds());

    assertThat(crawler.crawl(false)).isFalse();
    assertThat(indexer.indexed).isEmpty();
    assertThat(fetcher.fetched).isEmpty();
  }

  @Test
  public void testIndexesPageAndQueuesLinks() {
    fetcher.serve(HOME, "links.html");
    Crawler crawler = crawler(seeds(HOME));

    assertThat(crawler.crawl(false)).isTrue();

    assertThat(indexer.indexed).containsExactly(HOME);
    // /a and /b are each linked twice (once with a fragment or query), and / is the seed itself.
    assertThat(crawler.queueSize()).isEqualTo(2);
  }

  @Test
  public void testCrawlsQueuedPagesThenStops() {
    fetcher.serve(HOME, "links.html").serve(PAGE_A, "leaf.html").serve(PAGE_B, "leaf.html");
    Crawler crawler = crawler(seeds(HOME));

    int crawls = 0;
    while (crawler.crawl(false)) {
      crawls++;
      assertThat(crawls).as("crawl count").isLessThanOrEqualTo(3);
    }

    assertThat(crawls).isEqualTo(3);
    assertThat(indexer.indexed).containsExactlyInAnyOrder(HOME, PAGE_A, PAGE_B);
    assertThat(crawler.queueSize()).isZero();
  }

  @Test
  public void testAlreadyIndexedUrlIsNotReindexed() {
    fetcher.serve(HOME, "links.html");
    indexer.alreadyIndexed.add(HOME);
    Crawler crawler = crawler(seeds(HOME));

    assertThat(crawler.crawl(false)).isTrue();

    assertThat(indexer.indexed).isEmpty();
    // Its links are still followed.
    assertThat(fetcher.fetched).containsExactly(HOME);
    assertThat(crawler.queueSize()).isEqualTo(2);
  }

  @Test
  public void testDisallowedUrlIsSkipped() {
    fetcher.serve(HOME, "links.html").serve(PAGE_B, "leaf.html");
    robotsHandler.disallowedPaths.add("/a");
    Crawler crawler = crawler(seeds(HOME));

    assertThat(crawler.crawl(false)).isTrue();
    // /a is first in the queue but disallowed, so /b is crawled next.
    assertThat(crawler.crawl(false)).isTrue();
    assertThat(crawler.crawl(false)).isFalse();

    assertThat(indexer.indexed).containsExactly(HOME, PAGE_B);
  }

  @Test
  public void testHttpAndHttpsLinksToSamePageAreQueuedOnce() {
    fetcher.serve(HOME, "mixed-schemes.html");
    Crawler crawler = crawler(seeds(HOME));

    assertThat(crawler.crawl(false)).isTrue();

    // /a is linked over both http and https; https://example.com/ is the seed itself.
    assertThat(crawler.queueSize()).isEqualTo(1);
  }

  @Test
  public void testHttpsLinkKeepsItsScheme() {
    fetcher.serve(HOME, "mixed-schemes.html").serve("https://example.com/a", "leaf.html");
    Crawler crawler = crawler(seeds(HOME));

    while (crawler.crawl(false)) {
      assertThat(indexer.indexed.size()).as("pages indexed").isLessThanOrEqualTo(2);
    }

    // The https link came first on the page, so that's the version that was fetched.
    assertThat(indexer.indexed).containsExactly(HOME, "https://example.com/a");
  }

  @Test
  @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  public void testQueueOfDisallowedUrlsDrainsAndStops() {
    fetcher.serve(HOME, "links.html");
    robotsHandler.disallowedPaths.add("/a");
    robotsHandler.disallowedPaths.add("/b");
    Crawler crawler = crawler(seeds(HOME));

    assertThat(crawler.crawl(false)).isTrue();
    // /a and /b are queued but both disallowed, so the queue drains without crawling anything.
    assertThat(crawler.crawl(false)).isTrue();
    assertThat(crawler.queueSize()).isZero();
    // Nothing left in the queue or the seeds.
    assertThat(crawler.crawl(false)).isFalse();

    assertThat(indexer.indexed).containsExactly(HOME);
  }

  @Test
  public void testMissingDocumentIsNotIndexed() {
    Crawler crawler = crawler(seeds(HOME));

    assertThat(crawler.crawl(false)).isTrue();

    assertThat(fetcher.fetched).containsExactly(HOME);
    assertThat(indexer.indexed).isEmpty();
    assertThat(crawler.queueSize()).isZero();
  }
}
