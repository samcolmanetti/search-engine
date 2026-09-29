package com.samjsoares.soar.util;

import java.util.Collections;

public final class CollectionsUtil {

  private CollectionsUtil() {}

  public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
    return iterable == null ? Collections.emptyList() : iterable;
  }
}
