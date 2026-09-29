package com.samjsoares.soar.searcher.controller;

import com.google.gson.Gson;
import com.samjsoares.soar.searcher.core.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller that serves search results as JSON. */
@RestController
public class SearchController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final Gson gson = new Gson();

  private final Searcher searcher;

  /**
   * Creates a controller that answers queries with the given searcher.
   *
   * @param searcher the searcher used to answer queries
   */
  @Autowired
  public SearchController(Searcher searcher) {
    this.searcher = searcher;
  }

  /**
   * Handles {@code GET /api/search?query=...} and returns the ranked results as JSON.
   *
   * @param query the search query; terms are separated by spaces or {@code +}
   * @return a JSON array of results, highest relevance first ({@code []} when nothing matches), or
   *     {@code {}} when the query is empty
   */
  @RequestMapping(value = "/api/search", method = RequestMethod.GET)
  public String search(@RequestParam String query) {
    logger.info("Query (controller): {}", query);

    if (!StringUtils.hasLength(query)) {
      return gson.toJson(new Object());
    }

    return gson.toJson(searcher.search(query));
  }
}
