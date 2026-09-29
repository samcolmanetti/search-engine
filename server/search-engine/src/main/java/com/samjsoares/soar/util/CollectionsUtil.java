package com.samjsoares.soar.util;

import java.util.Collections;

/** Static helpers for collections. */
public final class CollectionsUtil {

  private CollectionsUtil() {}

  /** Returns the given iterable, or an empty one if it is null, so it can be looped over safely. */
  public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
    return iterable == null ? Collections.emptyList() : iterable;
  }
}
