package com.samjsoares.soar;

import com.samjsoares.soar.core.Fetcher;
import com.samjsoares.soar.core.RobotsHandler;
import com.samjsoares.soar.core.ScaledStemmer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

  @Bean
  RobotsHandler robotsHandler() {
    return new RobotsHandler();
  }

  @Bean
  Fetcher fetcher() {
    return new Fetcher();
  }

  @Bean
  ScaledStemmer scaledStemmer() {
    return new ScaledStemmer();
  }

}
