package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

/**
 * Created by pan on 31/3/17.
 */

public class CloseableUtilTest extends CommonTest {

    private class CloseableStream implements Closeable {
        private int closeCalled = 0;

        @Override
        public void close() throws IOException {
            closeCalled++;
        }
    }

    @Test
    public void testClose() {
        CloseableStream fakeStream = new CloseableStream();
        CloseableUtil.close(fakeStream);
        assertTrue("Fake stream close at least once", fakeStream.closeCalled > 0);
        fakeStream = new CloseableStream();
        CloseableUtil.close(fakeStream, fakeStream, fakeStream, fakeStream);

        assertTrue("Fake stream close at least once per stream", fakeStream.closeCalled == 4);

        Socket socket = new Socket();
        assertTrue("Socket not closed",  !socket.isClosed());

        CloseableUtil.close(socket);
        socket.isClosed();

        assertTrue("Socket closed",  socket.isClosed());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCloseRandomObject() {
        String text = "any_object";
        CloseableUtil.close(text);
    }
}
