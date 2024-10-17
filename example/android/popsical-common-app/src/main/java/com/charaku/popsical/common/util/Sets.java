package com.easternblu.khub.common.util;

import java.util.Set;

/**
 * Created by pan on 11/3/17.
 */
public class Sets {

    /**
     * Check and return true if a set if not null or empty
     * @param set
     * the set
     * @return
     * true if not null or empty
     */
    public static boolean isNotEmpty(Set<?> set) {
        return set != null && set.size() > 0;
    }

    /**
     * Convert a set to a string
     * @param set
     * the set
     * @param delim
     * delimiter
     * @param skipNull
     * true, then it will skip null
     * @param converter
     * a converter to convert the object to string
     * @param <A>
     * The generic class for the set element
     * @return
     * a string representing the set
     */
    public static <A> String asString(Set<A> set, String delim, boolean skipNull, Converter<A, String> converter) {
        if (isNotEmpty(set)) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (A element : set) {
                if (element == null && skipNull)
                    continue;

                String value = converter.convert(element);
                if (value == null && skipNull)
                    continue;

                if (i > 0) {
                    sb.append(delim);
                }
                sb.append(value);
                i++;
            }
            return sb.toString();
        } else {
            return null;
        }
    }
}
