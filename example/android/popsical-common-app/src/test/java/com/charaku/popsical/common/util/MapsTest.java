package com.easternblu.khub.common.util;


import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class MapsTest extends CommonTest {

    final Map<String, String> nullSet = null;
    final Map<String, String> emptySet = new HashMap<>();
    final Map<String, String> alphabets = createMap(new KeyValuePair<>("a", "A"), new KeyValuePair<>("b", "B"), new KeyValuePair<>("c", "C"));

    private static <A, B> HashMap<A, B> createMap(KeyValuePair<A, B>... elements) {
        HashMap<A, B> map = new HashMap<>();
        for (KeyValuePair<A, B> element : elements) {
            if (element != null) {
                map.put(element.getKey(), element.getValue());
            }
        }
        return map;
    }


    @Test
    public void testOthers() {
        KeyValuePair<String, String> pair = new KeyValuePair<>("a", "A");
        assertEquals("check first", "a", pair.getKey());
        assertEquals("check second", "A", pair.getValue());


        assertFalse("Should be null", Maps.isNotEmpty(nullSet));
        assertFalse("Should be empty", Maps.isNotEmpty(emptySet));
        assertTrue("Should be not be empty", Maps.isNotEmpty(alphabets));


        HashMap<String, Integer> sample = createMap(new KeyValuePair<>("a", 1), new KeyValuePair<>("b", 2), null, new KeyValuePair<>("c", 0), new KeyValuePair<>("d", 4));


        Converter<Integer, String> positiveIntegerOnly = new Converter<Integer, String>() {
            @Override
            public String convert(Integer from) {
                return from != null && from > 0 ? from.toString() : null;
            }
        };


        assertEquals("to string check", "a=1; b=2; d=4", Maps.asString(sample, "=", "; ", true, Strings.STRING_CONVERTER, positiveIntegerOnly));
        assertEquals("to string check", "a=1; b=2; c=null; d=4", Maps.asString(sample, "=", "; ", false, Strings.STRING_CONVERTER, positiveIntegerOnly));


        String mapString = "a=1,b=2,c=3";
        Map<String, Integer> parsed = Maps.parse(mapString, "=", ",", new Converter<String, Integer>() {
            @Override
            public Integer convert(String from) {
                return Numbers.parseInt(from, -1);
            }
        });

        assertTrue("size 3", parsed.size() == 3);
        //log(Maps.asString(parsed, "=", ",", true, Strings.getToStringConverter(String.class), Strings.getToStringConverter(Integer.class)));
        assertTrue("a should be 1", parsed.get("a") == 1);
        assertTrue("b should be 2", parsed.get("b") == 2);
        assertTrue("c should be 3", parsed.get("c") == 3);


    }


}
