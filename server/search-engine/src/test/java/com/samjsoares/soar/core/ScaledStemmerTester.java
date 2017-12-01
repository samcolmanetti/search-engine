package com.samjsoares.soar.core;

import com.samjsoares.soar.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class ScaledStemmerTester {

  @Autowired ScaledStemmer stemmer;

  @Test
  public void testComputing() {
    String word = "computing";
    String expected = "comput";
    String actual = stemmer.stem(word);

    assertEquals(expected, actual);
  }

  @Test
  public void testEmpty() {
    String word = "";
    String expected = "";
    String actual = stemmer.stem(word);

    assertEquals(expected, actual);
  }

  @Test
  public void testPlural() {
    String word = "universities";
    String expected = "univers";
    String actual = stemmer.stem(word);

    assertEquals(expected, actual);
  }

  @Test
  public void testRoot() {
    String word = "uni";
    String expected = "uni";
    String actual = stemmer.stem(word);

    assertEquals(expected, actual);
  }


}
