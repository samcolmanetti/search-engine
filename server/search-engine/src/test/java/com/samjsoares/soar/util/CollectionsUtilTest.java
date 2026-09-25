package com.samjsoares.soar.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class CollectionsUtilTest {

  @Test
  public void testNullBecomesEmpty() {
    Iterable<String> result = CollectionsUtil.emptyIfNull(null);
    assertThat(result).isNotNull().isEmpty();
  }

  @Test
  public void testNonNullIsReturnedAsIs() {
    List<String> list = Arrays.asList("a", "b");
    assertThat(CollectionsUtil.emptyIfNull(list)).isSameAs(list);
  }
}
