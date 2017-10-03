package com.samjsoares.searchengine.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UrlUtilTester {

    @Test
    public void testContentType_text() {
        String contentType = "text/html";
        assertTrue(UrlUtil.isValidContentType(contentType));
    }

    @Test
    public void testContentType_xml() {
        String contentType = "application/xml";
        assertTrue(UrlUtil.isValidContentType(contentType));
    }

    @Test
    public void testContentType_xhtml_xml() {
        String contentType = "application/xhtml+xml";
        assertTrue(UrlUtil.isValidContentType(contentType));
    }

    @Test
    public void testContentType_pdf() {
        String contentType = "application/pdf";
        assertFalse(UrlUtil.isValidContentType(contentType));
    }

    @Test
    public void testContentType_ogg() {
        String contentType = "application/ogg";
        assertFalse(UrlUtil.isValidContentType(contentType));
    }

}
