package com.samjsoares.soar.searcher.searcher;

import com.samjsoares.soar.searcher.searcher.driver.SearcherDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SearcherApplication implements CommandLineRunner {

  @Autowired
  private SearcherDriver searcherDriver;

	public static void main(String[] args) {
		SpringApplication.run(SearcherApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
    searcherDriver.run();
	}
}
