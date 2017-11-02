package com.samjsoares.soar;

import com.samjsoares.soar.driver.SimpleCrawlerDriver;
import com.samjsoares.soar.driver.SimpleDbDriver;
import com.samjsoares.soar.driver.SimpleIndexerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoarApplication implements CommandLineRunner{

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  SimpleDbDriver simpleDbDriver;

  @Autowired
  SimpleIndexerDriver simpleIndexerDriver;

  @Autowired
  SimpleCrawlerDriver simpleCrawlerDriver;

  @Override
  public void run(String... strings) throws Exception {
    simpleCrawlerDriver.run();
  }

  public static void main(String[] args) {
    SpringApplication.run(SoarApplication.class, args);
  }

}