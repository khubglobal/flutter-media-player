package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by pan on 31/3/17.
 */

public class StreamsTest extends CommonTest {

    private ByteArrayInputStream createInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    private ByteArrayOutputStream createOutputStream() {
        return new ByteArrayOutputStream();
    }

    @Test
    public void testOthers() throws IOException {
        List<Closeable> testStreams = new ArrayList<>();

        String plainText = Strings.repeat("ABCD", Maths.randomInt(100, 1000));
        InputStream inputStream;
        inputStream = createInputStream(plainText);
        testStreams.add(inputStream);

        ByteArrayOutputStream outputStream = createOutputStream();
        testStreams.add(outputStream);


        Streams.copy(inputStream, outputStream);


        assertEquals("copy success", plainText, new String(outputStream.toByteArray()));

        CloseableUtil.close(testStreams);
    }


}
