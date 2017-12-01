package com.samjsoares.soar.core;

import com.samjsoares.soar.model.TermInfo;
import com.samjsoares.soar.util.CollectionsUtil;
import com.samjsoares.soar.util.NodeIterable;
import com.samjsoares.soar.util.StopWordsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Encapsulates a map from search term to TermInfo.
 *
 * @author soar
 */

public class TermProcessor {

  private Map<String, TermInfo> map = new HashMap<>();
  private String url;
  private long documentId;

  private ScaledStemmer scaledStemmer = new ScaledStemmer();

  public TermProcessor(String url) {
    this.url = url;
    this.map = new HashMap<>();
  }

  public TermProcessor(long documentId) {
    this.documentId = documentId;
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
  private int size() {
    int total = 0;

    for (String key : map.keySet()) {
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
    for (Node node : CollectionsUtil.emptyIfNull(paragraphs)) {
      processTree(node);
    }
  }

  /**
   * Finds TextNodes in a DOM tree and counts their words.
   *
   * @param root
   */
  private void processTree(Node root) {
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
   * @param text The text to process.
   */
  private void processText(String text) {
    // replace punctuation with spaces, convert to lower case, and split on whitespace
    String[] array = text.trim().replaceAll("\\pP", " ").toLowerCase().split("\\s+");

    for (int i = 0; i < array.length; i++) {
      String term = array[i];
      if (shouldProcessTerm(term)) {
        incrementTermCount(term);
      }
    }
  }

  private boolean shouldProcessTerm (String term) {
    if (StringUtils.isBlank(term)) {
      return false;
    }

    if (term.length() < 2) {
      return false;
    }

    if (StopWordsUtil.isStopWord(term)) {
      return false;
    }

    return true;
  }

  /**
   * Increments the counter associated with `url`.
   *
   * @param term
   */
  private void incrementTermCount(String term) {
    putTermCount(term, getTermCount(term) + 1);
  }

  private void put(String term, TermInfo termInfo) {
    map.put(scaledStemmer.stem(term), termInfo);
  }

  /**
   * Adds a url to the map with a given count.
   *
   * @param term
   * @param count
   */
  private void putTermCount(String term, int count) {
    TermInfo termInfo = get(term);

    if (termInfo == null) {
      termInfo = new TermInfo();
      termInfo.setDocId(documentId);
      termInfo.setTerm(term);
    }

    termInfo.setCount(count);
    put(term, termInfo);
  }

  /**
   * Returns the count associated with this url, or 0 if it is unseen.
   *
   * @param term
   * @return
   */
  public TermInfo get(String term) {
    return map.get(scaledStemmer.stem(term));
  }

  protected Integer getTermCount(String term) {
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
  protected Set<String> keySet() {
    return map.keySet();
  }

  public List<TermInfo> getTermInfos() {
   return new ArrayList<>(map.values());
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

    for (String key : sortedMap.keySet()) {
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