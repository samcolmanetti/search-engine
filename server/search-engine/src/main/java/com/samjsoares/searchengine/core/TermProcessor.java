package com.samjsoares.searchengine.core;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.samjsoares.searchengine.model.TermInfo;
import com.samjsoares.searchengine.util.NodeIterable;
import com.samjsoares.searchengine.util.CollectionsUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * Encapsulates a map from search url to TermInfo.
 *
 * @author soar
 */
public class TermProcessor {

    private Map<String, TermInfo> map;
    private String url;

    public TermProcessor(String url) {
        this.url = url;
        this.map = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    /**
     * Returns the total of all counts.
     *
     * @return
     */
    public int size() {
        int total = 0;

        for (String key: map.keySet()){
            total += getTermCount(key);
        }

        return total;
    }

    /**
     * Takes a collection of Elements and counts their words.
     *
     * @param paragraphs
     */
    public void processElements(Elements paragraphs) {
        for (Node node: CollectionsUtil.emptyIfNull(paragraphs)) {
            processTree(node);
        }
    }

    /**
     * Finds TextNodes in a DOM tree and counts their words.
     *
     * @param root
     */
    public void processTree(Node root) {
        // NOTE: we could use select to find the TextNodes, but since
        // we already have a tree iterator, let's use it.
        for (Node node : new NodeIterable(root)) {
            if (node instanceof TextNode) {
                processText(((TextNode) node).text());
            }
        }
    }

    /**
     * Splits `text` into words and counts them.
     *
     * @param text  The text to process.
     */
    public void processText(String text) {
        // replace punctuation with spaces, convert to lower case, and split on whitespace
        String[] array = text.replaceAll("\\pP", " ").toLowerCase().split("\\s+");

        for (int i=0; i<array.length; i++) {
            String term = array[i];
            incrementTermCount(term);
        }
    }

    /**
     * Increments the counter associated with `url`.
     *
     * @param term
     */
    public void incrementTermCount(String term) {
        putTermCount(term, getTermCount(term) + 1);
    }

    public void put(String term, TermInfo termInfoInfo) {
        map.put(term, termInfoInfo);
    }

    /**
     * Adds a url to the map with a given count.
     *
     * @param term
     * @param count
     */
    public void putTermCount(String term, int count) {
        TermInfo termInfoInfo = get(term);

        if (termInfoInfo == null) {
            termInfoInfo = new TermInfo();
        }

        termInfoInfo.setCount(count);
        put(term, termInfoInfo);
    }

    /**
     * Returns the count associated with this url, or 0 if it is unseen.
     *
     * @param term
     * @return
     */
    public TermInfo get(String term) {
        return map.get(term);
    }

    public Integer getTermCount(String term) {
        TermInfo termInfoInfo = get(term);

        if (termInfoInfo != null) {
            return termInfoInfo.getCount();
        }

        return 0;
    }

    /**
     * Returns the set of terms that have been counted.
     *
     * @return
     */
    public Set<String> keySet() {
        return map.keySet();
    }

    /**
     * Print the terms and their counts in arbitrary order.
     */
    public void printCounts() {
        Map<String, TermInfo> sortedMap =
                map.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(new TermInfoCountOrderComparator()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));

        for (String key: sortedMap.keySet()) {
            Integer count = getTermCount(key);
            System.out.println(key + ", " + count);
        }
        System.out.println("Total of all counts = " + size());
    }

    private class TermInfoCountOrderComparator implements Comparator<TermInfo> {

        @Override
        public int compare(TermInfo o1, TermInfo o2) {
            return o1.getCount() - o2.getCount();
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String url = "https://en.wikipedia.org/wiki/University_of_Scranton";
        //String url = "https://www.scranton.edu";

        Fetcher wf = new Fetcher();
        Elements paragraphs = wf.fetch(url);

        TermProcessor counter = new TermProcessor(url);
        counter.processElements(paragraphs);
        counter.printCounts();
    }
}