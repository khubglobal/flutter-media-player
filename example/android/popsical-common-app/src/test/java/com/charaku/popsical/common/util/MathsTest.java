package com.easternblu.khub.common.util;


import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class MathsTest extends CommonTest {


    @Test
    public void testOthers() {


        // random
        assertEquals("Should be 0", 0, Maths.randomInt(0, 1));
        int rInt = Maths.randomInt(1, 10);
        assertTrue("Should be within range", 1 <= rInt && rInt < 10);


        assertEquals("Should be 0", 0, Maths.randomInt(0, 1));
        long rLong = Maths.randomLong(1, 10);
        assertTrue("Should be within range", 1 <= rLong && rLong < 10);



        // rounding
        float recurring333 = 10f / 3;
        float recurring666 = 10f / 3 * 2;

        assertEquals("Should be same", 3.33f, Maths.roundHalfUp(recurring333, 2), Math.ulp(recurring333));
        assertEquals("Should be same", 6.67f, Maths.roundHalfUp(recurring666, 2), Math.ulp(recurring666));

        assertEquals("Should be same", 3.33f, Maths.roundHalfDown(recurring333, 2), Math.ulp(recurring333));
        assertEquals("Should be same", 6.67f, Maths.roundHalfDown(recurring666, 2), Math.ulp(recurring666));

        assertEquals("Should be same", 2f, Maths.roundHalfUp(1.5f, 0), 0f);
        assertEquals("Should be same", 1f, Maths.roundHalfDown(1.5f, 0), 0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        Maths.randomInt(1, 1);
    }

}
