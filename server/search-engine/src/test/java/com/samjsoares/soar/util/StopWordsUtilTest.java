package com.samjsoares.soar.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StopWordsUtilTest {

  @Test
  public void testCommonStopWords() {
    for (String word : new String[] {"a", "the", "and", "of", "is", "with", "yourselves"}) {
      assertTrue(StopWordsUtil.isStopWord(word), word);
    }
  }

  @Test
  public void testRealWordsAreNotStopWords() {
    for (String word : new String[] {"search", "engine", "crawler", "university", "java"}) {
      assertFalse(StopWordsUtil.isStopWord(word), word);
    }
  }

  @Test
  public void testIsCaseSensitive() {
    // Callers lowercase text before checking.
    assertFalse(StopWordsUtil.isStopWord("The"));
    assertFalse(StopWordsUtil.isStopWord("AND"));
  }

  @Test
  public void testEmptyIsNotStopWord() {
    assertFalse(StopWordsUtil.isStopWord(""));
  }
}
