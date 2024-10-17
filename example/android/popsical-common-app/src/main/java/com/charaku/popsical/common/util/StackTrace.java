package com.easternblu.khub.common.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class StackTrace {


    public static void print(int level) {
        print(null, level);
    }

    public static void print(String prefix, int level) {
        try {
            throw new Throwable();
        } catch (Throwable t) {
            printThrowable(prefix, level, t);
        }
    }

    /**
     * Use to print to log to indicate where this method was called from
     *
     * @param tag
     * @param level
     * @param t
     */
    private static void printThrowable(String tag, int level, Throwable t) {
        //  Log.println(level, tag, )
        PrintWriter printWriter = null;
        ByteArrayOutputStream outputStream;
        try {
            printWriter = new PrintWriter(outputStream = new ByteArrayOutputStream());
            t.printStackTrace(printWriter);
            Log.println(level, tag == null ? "null" : tag, new String(outputStream.toByteArray()));
        } catch (Throwable e) {

        } finally {
            CloseableUtil.close(printWriter);
        }
    }

}
