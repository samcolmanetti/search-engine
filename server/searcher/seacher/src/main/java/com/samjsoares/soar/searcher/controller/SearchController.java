package com.samjsoares.soar.searcher.controller;

import com.google.gson.Gson;
import com.samjsoares.soar.searcher.core.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

  @Autowired
  private Searcher searcher;

  private static Gson gson = new Gson();

  @RequestMapping(value = "/api/search", method = RequestMethod.GET)
  public String search(@RequestParam String query) {
    String res = gson.toJson(searcher.search(query));
    return res;
  }

}
