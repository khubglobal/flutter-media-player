package com.easternblu.khub.common.util;


import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by pan on 31/3/17.
 */

public class ResultsTest extends CommonTest {


    @Test
    public void testOthers() {
        Result<String> okResult = new Result<>("OK");
        Result<String> errorResult = new Result<>(new Exception("Mock exception"));

        assertTrue("ok result", true == okResult.isSuccess());
        assertTrue("error result", true == errorResult.hasError());

        assertTrue("ok result not contradicting", okResult.isSuccess() != okResult.hasError());
        assertTrue("error result not contradicting", errorResult.isSuccess() != errorResult.hasError());
    }


}
