package com.easternblu.khub.common.util;

/**
 * Use to create a map
 *
 * @param <K>
 * @param <V>
 */
public class KeyValuePair<K, V> {
    private K key;
    private V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}