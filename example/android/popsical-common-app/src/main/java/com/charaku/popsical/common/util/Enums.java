package com.easternblu.khub.common.util;

import android.util.Log;

import java.lang.reflect.Method;

public class Enums {
    /**
     * A wrapper for "valueOf" that doesn't throw excepn
     *
     * @param name
     * @param defaultEnum
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E valueOf(String name, E defaultEnum) {
        try {
            Class enumClass = Class.forName(defaultEnum.getClass().getName());
            return (E) Enum.valueOf(enumClass, name);
        } catch (Throwable t) {
            Log.e("Enum", t.getMessage(), t);
            return defaultEnum;
        }
    }


}
