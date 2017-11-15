package com.samjsoares.soar.searcher.searcher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

  @RequestMapping("/search")
  public String index() {
    return "Greetings from Spring Boot!";
  }

}
