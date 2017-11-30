package com.samjsoares.soar.searcher.controller;

import com.google.gson.Gson;
import com.samjsoares.soar.searcher.core.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Searcher searcher;

  private static Gson gson = new Gson();

  @RequestMapping(value = "/api/search", method = RequestMethod.GET)
  public String search(@RequestParam String query) {
    logger.info("Query (controller): " + query);

    if (StringUtils.isEmpty(query)) {
      return gson.toJson(new Object());
    }

    return gson.toJson(searcher.search(query));
  }

}
