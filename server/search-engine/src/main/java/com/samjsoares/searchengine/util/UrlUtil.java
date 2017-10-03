package com.samjsoares.searchengine.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    private final static String VALID_CONTENT_TYPES_PATTERN
            = "(text/.*)|(application\\/xml)|(application\\/xhtml\\+xml)";
    private final static Matcher CONTENT_TYPE_MATCHER = Pattern.compile(VALID_CONTENT_TYPES_PATTERN).matcher("");

    public static String getCleanUrl(String url) {
        try {
            URI uri = new URI(url);
            return new URI(
                    uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null,
                    null).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isValidContentType (String contentType) {
        return CONTENT_TYPE_MATCHER.reset(contentType).matches();
    }
}
