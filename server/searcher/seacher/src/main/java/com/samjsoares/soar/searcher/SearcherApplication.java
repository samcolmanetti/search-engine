package com.samjsoares.soar.searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot entry point for the search REST API. */
@SpringBootApplication
public class SearcherApplication {

  /**
   * Starts the Spring application.
   *
   * @param args command-line arguments passed to Spring Boot
   */
  public static void main(String[] args) {
    SpringApplication.run(SearcherApplication.class, args);
  }
}
