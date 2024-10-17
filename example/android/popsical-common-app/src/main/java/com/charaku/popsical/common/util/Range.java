package com.easternblu.khub.common.util;


import androidx.annotation.NonNull;

public class Range<T extends Comparable<? super T>> {
    private T lower, upper;

    public Range(@NonNull T lower, @NonNull T upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static <T extends Comparable<? super T>> Range<T> create(T lower, T upper) {
        return new Range<T>(lower, upper);
    }

    public T getLower() {
        return lower;
    }

    public T getUpper() {
        return upper;
    }

    public boolean contains(@NonNull T value) {
        return value.compareTo(getLower()) >= 0 && value.compareTo(getUpper()) <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range<?> range = (Range<?>) o;
        return lower.equals(range.lower) && upper.equals(range.upper);
    }

    @Override
    public int hashCode() {
        return lower.hashCode() + upper.hashCode();
    }


    @Override
    public String toString() {
        return "[" + getLower() + ":" + getUpper() + "]";
    }
}
