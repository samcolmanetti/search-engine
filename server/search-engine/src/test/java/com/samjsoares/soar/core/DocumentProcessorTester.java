package com.samjsoares.soar.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.samjsoares.soar.TestConfig;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class DocumentProcessorTester {

  @Autowired private Fetcher fetcher;

  @Test
  public void testSimpleDescription() {
    String sampleUrl = "www.wikipedia.org/wiki/Education";
    Document doc = fetcher.fetchDocument(sampleUrl);
    DocumentProcessor documentProcessor = new DocumentProcessor(doc);
    assertThat(documentProcessor.getDescription() != null);
  }
}
