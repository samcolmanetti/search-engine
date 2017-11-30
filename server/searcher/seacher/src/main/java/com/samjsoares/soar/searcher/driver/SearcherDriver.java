package com.samjsoares.soar.searcher.driver;

import com.samjsoares.soar.searcher.constant.Regex;
import com.samjsoares.soar.searcher.model.SearchResult;
import com.samjsoares.soar.searcher.core.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class SearcherDriver {

  private Searcher searcher;

  @Autowired
  public SearcherDriver(Searcher searcher) {
    this.searcher = searcher;
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to Soar...");

    while (true) {
      System.out.print("Search terms: ");
      String query = scanner.nextLine();

      if ("\\q".equals(query)) {
        System.out.println("Thank you for using Soar!");
        break;
      }

      String[] terms = query.toLowerCase().split(Regex.SPACE_OR_PLUS);

      List<SearchResult> results = searcher.search(terms);

      if (results == null || results.size() == 0) {
        System.out.println("No results found!");
        continue;
      }

      System.out.println("Results...");
      for (SearchResult result : results) {
        System.out.println("URL: " + result.getUrl() + " | Score: " + result.getRelevance());
      }
    }
  }
}
