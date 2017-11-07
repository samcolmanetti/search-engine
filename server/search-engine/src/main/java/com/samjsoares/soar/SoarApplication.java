package com.samjsoares.soar;

import com.samjsoares.soar.driver.SimpleCrawlerDriver;
import com.samjsoares.soar.driver.SimpleDbDriver;
import com.samjsoares.soar.driver.SimpleIndexerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoarApplication implements CommandLineRunner {

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