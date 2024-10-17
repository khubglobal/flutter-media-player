package com.easternblu.khub.common.util;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by yatpanng on 15/2/17.
 */

public class Maps {

    private static final String TAG = Maps.class.getSimpleName();

    /**
     * Is NOT null and NOT empty
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> collection) {
        return collection != null && collection.size() > 0;
    }


    /**
     * Is null OR empty
     *
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(Map<?, ?> collection) {
        return collection == null || collection.size() == 0;
    }


    /**
     * Convert a {@link Map} to string using {@link Converter} interface. Inner delim will be '='. Outter delim will be ','
     *
     * @param map
     * @param keyConverter
     * @param valueConverter
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String asString(Map<K, V> map, Converter<K, String> keyConverter, Converter<V, String> valueConverter) {
        return asString(map, "=", ",", true, keyConverter, valueConverter);
    }


    /**
     * Convert a map to string using {@link Converter} interface
     *
     * @param map
     * @param innerDelim
     * @param delim
     * @param skipNull
     * @param keyConverter
     * @param valueConverter
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String asString(Map<K, V> map, String innerDelim, String delim, boolean skipNull, Converter<K, String> keyConverter, Converter<V, String> valueConverter) {
        if (isNotEmpty(map)) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (K key : map.keySet()) {
                V value = map.get(key);


                String valueString = value != null ? valueConverter.convert(value) : null;
                if (valueString == null && skipNull)
                    continue;

                if (i > 0) {
                    sb.append(delim);
                }
                sb.append(keyConverter.convert(key) + innerDelim + valueString);
                i++;
            }
            return sb.toString();
        } else {
            return null;
        }
    }


    /**
     * Convert a collection to map
     *
     * @param values
     * @param keyGetter
     * @return
     */
    public static <K, V> Map<K, V> create1To1Map(Collection<V> values, Converter<V, K> keyGetter) {
        if (values == null)
            return null;
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (V value : values) {
            K key = keyGetter.convert(value);
            if (key == null)
                continue;
            result.put(key, value);
        }
        return result;
    }

    /**
     * Convert a collection to a Map using a method that obtain a key for each
     * object
     *
     * @param values
     * @param keyGetter
     * @return
     */
    public static <K, V> Map<K, List<V>> create1ToManyMap(Collection<V> values, Converter<V, K> keyGetter) {
        if (values == null)
            return null;
        Map<K, List<V>> result = new LinkedHashMap<K, List<V>>();
        for (V value : values) {
            K key = keyGetter.convert(value);
            if (key == null)
                continue;
            List<V> valuesOfKey = result.get(key);
            if (valuesOfKey == null) {
                valuesOfKey = new ArrayList<V>();
                result.put(key, valuesOfKey);
            }
            valuesOfKey.add(value);
        }
        return result;
    }


    /**
     * Add a element to a map based on {@link KeyValuePair}s
     *
     * @param map
     * @param keyValuePairs
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> void addAll(Map<K, V> map, KeyValuePair<K, V>... keyValuePairs) {
        if (map != null) {
            for (KeyValuePair<K, V> keyValuePair : keyValuePairs) {
                map.put(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
    }

    /**
     * Create a map based on {@link KeyValuePair}s
     *
     * @param keyValuePairs
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> asHashMap(KeyValuePair<K, V>... keyValuePairs) {
        Map<K, V> map = new HashMap<>();
        addAll(map, keyValuePairs);
        return map;
    }


    /**
     * Convert a string to a map
     *
     * @param string
     * @param converter
     * @return
     */
    public static <K> Map<String, K> parse(String string, Converter<String, K> converter) {
        return parse(string, "=", ",", converter);
    }

    /**
     * Convert a string to a map, with custom delim and value converter
     *
     * @param string
     * @param innerDelim
     * @param outterDelim
     * @param converter
     * @return
     */
    public static <V> Map<String, V> parse(String string, String innerDelim, String outterDelim, Converter<String, V> converter) {
        return parse(string, innerDelim, outterDelim, false, Strings.STRING_CONVERTER, converter);
    }


    /**
     * Parse a string to a {@link LinkedHashMap} object. It will ignore the entry that has null key.
     * If there is any parsing error then it might not return correct result but it has an runtime of O(N)
     *
     * @param string
     * @param innerDelim
     * @param outterDelim
     * @param addInvalidValue if true, then it will not add the value to map if value is null
     * @param keyConverter
     * @param valueConverter
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> parse(String string, String innerDelim, String outterDelim, boolean addInvalidValue, Converter<String, K> keyConverter, Converter<String, V> valueConverter) {
        Map<K, V> map = new LinkedHashMap<>();
        if (Strings.isNullOrEmpty(string) || Strings.isNullOrEmpty(innerDelim) || Strings.isNullOrEmpty(outterDelim))
            return map;

        StringTokenizer stringTokenizer = new StringTokenizer(string);
        boolean trimOutterDelim = false;
        while (stringTokenizer.hasMoreTokens()) {
            String key = stringTokenizer.nextToken(innerDelim);

            if (Strings.isNullOrEmpty(key)) {
                continue;
            }
            if (trimOutterDelim)
                key = key.substring(outterDelim.length());

            String value = stringTokenizer.nextToken(outterDelim);
            if (value != null)
                value = value.substring(innerDelim.length());

            V valueObject = valueConverter.convert(value);
            if (valueObject == null && !addInvalidValue)
                continue;

            map.put(keyConverter.convert(key), valueObject);
            trimOutterDelim = true;
        }
        return map;
    }


    /**
     * Create map, based on the {@link Pair} objects provided
     *
     * @param map
     * @param entries
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V, M extends Map<K, V>> M create(M map, Pair<K, V>... entries) {
        if (map != null) {
            for (Pair<K, V> entry : entries) {
                if (entry != null) {
                    map.put(entry.first, entry.second);
                }
            }
        }
        return map;
    }

    /**
     * Get a value from a map. If map/key/value is null defaultValue is returned
     *
     * @param map
     * @param key
     * @param defaultValue
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V get(Map<K, ?> map, K key, V defaultValue, Class<V> valueClass) {
        if (map == null || key == null || !map.containsKey(key)) {
            return defaultValue;
        } else {
            Object value = map.get(key);
            return value != null && valueClass.isInstance(value) ? (V) value : defaultValue;
        }
    }


    /**
     * Create a object map based on an array if consective key value.
     * [0] will be key
     * [1] will be value
     *
     * @param map
     * @param keyValues
     * @return
     */
    public static Map<Object, Object> create(Map<Object, Object> map, Object... keyValues) {
        if (map != null) {
            for (int i = 0; i < keyValues.length / 2; i++) {
                map.put(keyValues[0], keyValues[1]);
            }
        }
        return map;
    }


    /**
     * Convert a map to a list
     *
     * @param map
     * @param converter
     * @param <O>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <O, K, V> List<O> toList(Map<K, V> map, Converter<Pair<K, V>, O> converter) {
        List<O> objects = new ArrayList<>();
        for (K key : map.keySet()) {
            V value = map.get(key);
            if (converter != null) {
                objects.add(converter.convert(new Pair<K, V>(key, value)));
            }
        }
        return objects;
    }


}
