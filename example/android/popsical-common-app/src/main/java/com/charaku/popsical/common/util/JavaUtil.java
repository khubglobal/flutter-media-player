package com.easternblu.khub.common.util;

import androidx.annotation.NonNull;

/**
 * A simple util class that contains various util methods that are commonly used in Kotlin
 */
public class JavaUtil {

    /**
     * Cast object into class A then invoke "let" callback
     *
     * @param object
     * @param clazz
     * @param let
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B castAndLet(Object object, Class<A> clazz, @NonNull NotNull<A, B> let) {
        if (object != null && clazz.isInstance(object)) {
            return let.invoke((A) object);
        } else {
            return null;
        }
    }


    /**
     * Invoke "let" callback if object is not null
     *
     * @param object
     * @param let
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B let(A object, NotNull<A, B> let) {
        if (object != null) {
            return let.invoke(object);
        } else {
            return null;
        }
    }

    

}
