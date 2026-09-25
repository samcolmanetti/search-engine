package com.samjsoares.soar.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScaledStemmerTest {

  private final ScaledStemmer stemmer = new ScaledStemmer();

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

  @Test
  public void testNull() {
    assertNull(stemmer.stem(null));
  }

  @Test
  public void testCachedResultMatches() {
    assertEquals(stemmer.stem("running"), stemmer.stem("running"));
    assertEquals("run", stemmer.stem("running"));
  }
}
