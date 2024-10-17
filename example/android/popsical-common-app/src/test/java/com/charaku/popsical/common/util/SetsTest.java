package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class SetsTest extends CommonTest {

    final Set<String> nullSet = null;
    final Set<String> emptySet = new HashSet<>();
    final Set<String> oneNullElementSet = createSet(new String[]{null});
    final Set<String> alphabets = createSet("a", "b", "c");

    private static <A> HashSet<A> createSet(A... elements) {
        HashSet<A> set = new HashSet<>();
        for (A element : elements)
            set.add(element);
        return set;
    }


    @Test
    public void testOthers() {
        assertFalse("Should be null", Sets.isNotEmpty(nullSet));
        assertFalse("Should be empty", Sets.isNotEmpty(emptySet));
        assertTrue("Should be not be empty, null count as 1", Sets.isNotEmpty(oneNullElementSet));
        assertTrue("Should be not be empty", Sets.isNotEmpty(alphabets));

        assertEquals("to string check", "a, b, c", Sets.asString(createSet("a", "b", null, "c"), ", ", true, Strings.STRING_CONVERTER));
    }



}
