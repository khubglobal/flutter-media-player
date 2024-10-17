package com.easternblu.khub.common.util;


import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class FilesTest extends CommonTest {


    @Test
    public void testOthers() {
        File downloadFolder = Files.getDownloadFolder();
        assertTrue("Download folder exist", downloadFolder.exists());

        File track1Mp4 = Files.getDownloadFile("popsical/1.mp4");
        assertTrue("Track1 file doesnot exist", track1Mp4.exists());

//        Result<String> okResult = new Result<>("OK");
//        Result<String> errorResult = new Result<>(new Exception("Mock exception"));
//
//        assertTrue("ok result", true == okResult.isSuccess());
//        assertTrue("error result", true == errorResult.hasError());
//
//        assertTrue("ok result not contradicting", okResult.isSuccess() != okResult.hasError());
//        assertTrue("error result not contradicting", errorResult.isSuccess() != errorResult.hasError());
    }


}
