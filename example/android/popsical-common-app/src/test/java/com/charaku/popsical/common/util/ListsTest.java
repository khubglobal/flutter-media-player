package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class ListsTest extends CommonTest {

    final List<String> nullList = null;
    final List<String> emptyList = new ArrayList<>();
    final List<String> oneNullElementList = createList(new String[]{null});
    final List<String> alphabets = createList("a", "b", "c");

    private static <A> ArrayList<A> createList(A... elements) {
        ArrayList<A> arrayList = new ArrayList<>();
        for (A element : elements)
            arrayList.add(element);
        return arrayList;
    }


    @Test
    public void testOthers() {
        assertTrue("Should be null", Lists.isNullOrEmpty(nullList));
        assertTrue("Should be empty", Lists.isNullOrEmpty(emptyList));
        assertFalse("Should be not be empty, null count as 1", Lists.isNullOrEmpty(oneNullElementList));
        assertFalse("Should be not be empty", Lists.isNullOrEmpty(alphabets));

        assertEquals("to string check", "a, b, c", Lists.asString(createList("a", "b", null, "c"), ", ", true, Strings.STRING_CONVERTER));

    }

  


}
