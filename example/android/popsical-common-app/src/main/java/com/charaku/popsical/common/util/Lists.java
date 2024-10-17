package com.easternblu.khub.common.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pan on 11/3/17.
 */

public class Lists {

    /**
     * Return size of the collection (doing null check)
     *
     * @param collection
     * @param defaultSize
     * @return
     */
    public static int size(@Nullable Collection<?> collection, int defaultSize) {
        return collection == null ? defaultSize : collection.size();
    }

    /**
     * Check and return true if a set if not null or empty
     *
     * @param list
     * @return true if it is not null or empty
     */
    public static boolean isNotEmpty(List<?> list) {
        return list != null && list.size() > 0;
    }

    /**
     * Null or Empty
     *
     * @param list
     * @return
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }


    /**
     * just convert the list to a readable string using toString to convert the element in the list
     *
     * @param list
     * @param <A>
     * @return
     */
    public static <A> String asString(List<A> list) {
        return asString(list, ",", true, new Converter<A, String>() {
            @Override
            public String convert(A from) {
                return from.toString();
            }
        });
    }

    /**
     * Convert a set to a string
     *
     * @param list      the list
     * @param delim     delimiter
     * @param skipNull  true, then it will skip null
     * @param converter a converter to convert the object to string
     * @param <A>       The generic class for the list element
     * @return a string representing the list
     */
    public static <A> String asString(List<A> list, String delim, boolean skipNull, Converter<A, String> converter) {
        if (isNotEmpty(list)) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (A element : list) {
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


    /**
     * Add a number of element into a list
     *
     * @param list
     * @param elements
     * @param <A>
     * @return
     */
    public static <A, L extends Collection<A>> L addAll(L list, A... elements) {
        if (list != null) {
            for (A element : elements) {
                list.add(element);
            }
        }
        return list;
    }

    public static <A> List<A> create(A... elements) {
        return addAll(new ArrayList<A>(), elements);
    }


    /**
     * Add all elements to the list
     *
     * @param list
     * @param elements
     * @param <A>
     * @param <L>
     * @return
     */
    public static <A, L extends Collection<A>> L addAll(L list, Collection<A> elements) {
        if (list != null) {
            for (A element : elements) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * Add all elements to the list using the converter
     *
     * @param list
     * @param converter
     * @param elements
     * @param <A>
     * @param <B>
     * @param <L>
     * @return
     */
    public static <A, B, L extends Collection<B>> L addAll(L list, Converter<A, B> converter, A... elements) {
        if (list != null) {
            for (A element : elements) {
                list.add(converter.convert(element));
            }
        }
        return list;
    }

    /**
     * Add all elements to the list using the converter
     *
     * @param list
     * @param converter
     * @param elements
     * @param <A>
     * @param <B>
     * @param <L>
     * @return
     */
    public static <A, B, L extends Collection<B>> L addAll(L list, Converter<A, B> converter, Collection<A> elements) {
        if (list != null && elements != null) {
            for (A element : elements) {
                list.add(converter.convert(element));
            }
        }
        return list;
    }

    /**
     * A convert a list to another list
     *
     * @param src
     * @param converter
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> List<B> map(List<A> src, Converter<A, B> converter) {
        return addAll(new ArrayList<B>(), converter, src);
    }

    /**
     * Copy items from {@link ListInterface} to a {@link Collection}
     *
     * @param src
     * @param des
     * @param offset
     * @param <K>
     * @param <L>
     * @return
     */
    public static <K, L extends Collection<K>> L copy(ListInterface<K> src, L des, int offset) {
        if (src != null && des != null) {
            for (int i = offset; i < src.size(); i++) {
                des.add(src.get(i));
            }
        }
        return des;
    }


    /**
     * An interface that models anything similar to a list.
     * Use together with {@link #copy(ListInterface, Collection, int)} to convert the implementation to a {@link Collection}
     * <p>
     * For example a {@link android.view.ViewGroup} can implement this to convert its child views to a List of {@link android.view.View}
     * <p>
     * Instead of creating a method with for loop inside to loop through it childern
     *
     * @param <K>
     */
    public static interface ListInterface<K> {
        /**
         * Return the i th element of the implementation
         *
         * @param i
         * @return
         */
        K get(int i);

        /**
         * Return the number of elements in the implementation
         *
         * @return
         */
        int size();
    }


    /**
     * get first element of the list, check null list, if so return null
     *
     * @param list
     * @param <A>
     * @return
     */
    @Nullable
    public static <A> A getFirst(List<A> list) {
        if (Lists.isNullOrEmpty(list)) {
            return null;
        }
        return list.get(0);
    }


    /**
     * @param list
     * @param <A>
     * @return
     */
    @Nullable
    public static <A> A getFirstNonNull(List<A> list) {
        int i = 0;
        while (list != null && i < list.size()) {
            A value;
            if ((value = list.get(i)) != null) {
                return value;
            }
            i++;
        }
        return null;
    }


    /**
     * Adding all element from a and b to collection c
     * <p>
     * ie. a + b = c
     *
     * @param a
     * @param b
     * @param c
     * @param <K>
     * @param <C>
     * @return the result c
     */
    public static <K, C extends Collection<K>> C concat(@Nullable Collection<K> a, @Nullable Collection<K> b, @NonNull C c) {
        if (c != null) {
            if (a != null) {
                c.addAll(a);
            }
            if (b != null) {
                c.addAll(b);
            }
        }
        return c;
    }

    /**
     * get last element of the list, check null list, if so return null
     *
     * @param list
     * @param <A>
     * @return
     */
    @Nullable
    public static <A> A getLast(List<A> list) {
        if (Lists.isNullOrEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }


}
