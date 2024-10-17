package com.easternblu.khub.common.util;


import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pan on 31/3/17.
 */

public class NumbersTest extends CommonTest {


    @Test
    public void testOthers() {
        assertEquals("Should be same", 1, Numbers.parseInt("1", -1));
        assertEquals("Should be same", -1, Numbers.parseInt("zxnksdf", -1));


    }


}
