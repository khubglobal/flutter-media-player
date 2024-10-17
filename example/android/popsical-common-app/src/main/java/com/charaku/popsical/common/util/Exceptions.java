package com.easternblu.khub.common.util;

import androidx.annotation.NonNull;

/**
 * Created by yatpanng on 22/4/17.
 */

public class Exceptions {

    /**
     * return the Throwable that is the most likely outcome of the Exception
     *
     * @param t
     * @return
     */
    @NonNull
    public static Throwable findHighLevelReadableError(@NonNull Throwable t) {
        Throwable topMost = t;
        while (t != null) {
            if (Strings.isNotEmpty(t.getMessage())) {
                return t;
            }
            t = t.getCause();
        }
        return topMost;
    }

    /**
     * return the Throwable that is the most likely original cause of the Exception
     *
     * @param t
     * @return
     */
    public static Throwable findLowLevelReadableError(Throwable t) {
        while (t != null) {
            if (Strings.isNotEmpty(t.getMessage()) && t.getCause() == null) {
                return t;
            }
            t = t.getCause();
        }
        return t;
    }

}
