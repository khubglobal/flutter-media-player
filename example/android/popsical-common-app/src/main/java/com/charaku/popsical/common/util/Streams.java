package com.easternblu.khub.common.util;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * Created by yatpanng on 13/6/17.
 */

public class Streams {
    public static final String TAG = Streams.class.getSimpleName();

    /**
     * Read the Inputstream and output as byte[] array<p>
     * <p/>
     * NOTE:!!!!<p>
     * The byte array will be in heap (memory) so u should only use this if the stream is not too big
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readAll(InputStream in) throws IOException {

        byte[] buffer = null;
        int totalBytesRead = 0;
        IOException newException = null;
        try {
            buffer = new byte[2048];
            byte[] tempBuffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = in.read(tempBuffer)) != -1 && buffer.length < Integer.MAX_VALUE) {
                if (totalBytesRead + bytesRead > buffer.length) {
                    int newBufferLength = buffer.length + 2048;
                    byte[] newBuffer = new byte[newBufferLength];
                    System.arraycopy(buffer, 0, newBuffer, 0, totalBytesRead);
                    buffer = newBuffer;
                }
                System.arraycopy(tempBuffer, 0, buffer, totalBytesRead, bytesRead);
                totalBytesRead += bytesRead;
            }

        } catch (Exception e) {
            Timber.w(e);
            newException = new IOException(e.getMessage());
        } finally {
            if (buffer != null) {
                byte[] newBuffer = new byte[totalBytesRead];
                System.arraycopy(buffer, 0, newBuffer, 0, totalBytesRead);
                buffer = newBuffer;
            }
        }
        if (totalBytesRead <= 0 && newException != null) // if we managed to
            // read some bytes
            // then return that
            // anyway.
            throw newException; // only if there is nothing to read and we got
        // an exception, then we will throw the
        // exception
        return buffer;
    }

    /**
     * By default use buffer of 1024 bytes
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, 1024, -1, null);
    }

    /**
     * Copy a stream from {@link InputStream} to {@link OutputStream}
     *
     * @param input
     * @param output
     * @param progressInterval
     * @param progressCallback
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output, int buffersize, int progressInterval, @Nullable TransferProgressUpdate progressCallback) throws IOException {
        byte[] buffer = new byte[buffersize]; // Adjust if you want
        int bytesRead = 0;
        long totalBytesCopied = 0;
        int bytesReadSinceLastReportedProgress = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            bytesReadSinceLastReportedProgress += bytesRead;
            totalBytesCopied += bytesRead;
            // report the progress
            if (progressInterval > 0 && bytesReadSinceLastReportedProgress > progressInterval && progressCallback != null) {
                progressCallback.update(totalBytesCopied);
                bytesReadSinceLastReportedProgress = 0;
            }
        }
        if (progressCallback != null) {
            // report the progress
            progressCallback.update(totalBytesCopied);
        }
        return totalBytesCopied;
    }


    /**
     * Read the last few bytes of a file
     *
     * @param inputStream
     * @param offset
     * @param bytesToRead
     * @return
     * @throws IOException
     */
    public static byte[] readLast(InputStream inputStream, long offset, int bytesToRead) throws IOException {
        byte[] buffer = new byte[bytesToRead];
        BufferedInputStream temp = null;
        try {
            temp = new BufferedInputStream(inputStream);
            temp.skip(offset);
            temp.read(buffer);
        } finally {
            CloseableUtil.close(temp);
        }
        return buffer;
    }

    /**
     * An interface to report progress of copy
     */
    public interface TransferProgressUpdate {
        void update(long transfered);
    }
}
