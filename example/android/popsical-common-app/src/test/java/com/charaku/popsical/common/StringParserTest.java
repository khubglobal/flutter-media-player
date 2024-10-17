package com.easternblu.khub.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pan on 10/1/18.
 */

public class StringParserTest extends CommonTest {

    @Test
    public void testParse() throws StringParser.ParseException {
        StringParser stringParser = new StringParser("start <a>wakawaka</a> end <a>ayay</a>");
        assertEquals("check popSubstring", "wakawaka", stringParser.popSubstring("<a>", "</a>"));
        assertEquals("check popSubstring", "ayay", stringParser.popSubstring("<a>", "</a>"));
    }
}
