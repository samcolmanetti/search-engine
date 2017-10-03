package com.samjsoares.searchengine.util;

import java.util.Collections;

public class CollectionsUtil {

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.emptyList() : iterable;
    }
}
