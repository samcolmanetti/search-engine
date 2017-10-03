package com.samjsoares.searchengine.core;

import com.samjsoares.searchengine.util.UrlUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Crawler {
    // keeps track of where we started
    private final String source;

    // the index where the results go
    private Index index;

    // queue of URLs to be indexed
    private Queue<String> queue = new LinkedList<>();

    // fetcher used to get pages
    private final static Fetcher fetcher = new Fetcher();


    /**
     * Constructor.
     *
     * @param source
     * @param index
     */
    public Crawler(String source, Index index) {
        this.source = source;
        this.index = index;
        queue.offer(source);
    }

    /**
     * Returns the number of URLs in the queue.
     *
     * @return
     */
    public int queueSize() {
        return queue.size();
    }

    /**
     * Gets a URL from the queue and indexes it.
     * @param offline
     *
     * @return Number of pages indexed.
     * @throws IOException
     */
    public boolean crawl(boolean offline) {
        if (queue.isEmpty()) {
            System.out.println("Queue is empty");
            return false;
        }

        String url = UrlUtil.getCleanUrl(queue.poll());

        System.out.println("Crawling " + url);
        if (!index.shouldIndex(url)) {
            System.out.println("Already indexed " + url);
            return true;
        }

        Elements paragraphs;
        if (!offline) {
            paragraphs = fetcher.fetch(url);
        } else {
            paragraphs = fetcher.read(url);
        }

        if (paragraphs != null) {
            index.indexPage(url, paragraphs);
            queueInternalLinks(paragraphs);
        }

        return true;
    }

    /**
     * Parses paragraphs and adds internal links to the queue.
     *
     * @param paragraphs
     */
    private void queueInternalLinks(Elements paragraphs) {
        if (paragraphs == null) return;

        Elements urlElements = paragraphs.select("a[href]");

        for (Element urlElement: urlElements) {
            String absUrl = urlElement.attr("abs:href");
            absUrl = UrlUtil.getCleanUrl(absUrl);

            if (absUrl != null) {
                //System.out.println ("Considering: " + absUrl);
                queue.offer(absUrl);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // make a Crawler
        Index index = new Index();

        String source = "https://en.wikipedia.org/wiki/Philosophy";
        Crawler wc = new Crawler(source, index);

        // for testing purposes, load up the queue
        Elements paragraphs = fetcher.fetch(source);
        wc.queueInternalLinks(paragraphs);

        // loop until we index a new page
        boolean continueCrawling;
        int maxCrawl = 50;
        int crawlCount = 0;

        do {
            continueCrawling = wc.crawl(false);
            crawlCount++;
        } while (continueCrawling && crawlCount < maxCrawl);

        String searchTerm = "truth";
        System.out.printf("\nResults for term '%s'\n", searchTerm);
        Map<String, Integer> map = index.getCounts(searchTerm);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

}