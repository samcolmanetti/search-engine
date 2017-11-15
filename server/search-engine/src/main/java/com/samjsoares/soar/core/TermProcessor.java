package com.samjsoares.soar.core;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.samjsoares.soar.model.TermInfo;
import com.samjsoares.soar.util.NodeIterable;
import com.samjsoares.soar.util.CollectionsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * Encapsulates a map from search term to TermInfo.
 *
 * @author soar
 */
public class TermProcessor {

  private static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
  private static Set<String> stopWordSet = new HashSet<>(Arrays.asList(stopwords));

  private Map<String, TermInfo> map = new HashMap<>();
  private String url;
  private long documentId;

  public TermProcessor() {}

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
  public int size() {
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
   * @param text The text to process.
   */
  public void processText(String text) {
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

    if (stopWordSet.contains(term)) {
      return false;
    }

    return true;
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