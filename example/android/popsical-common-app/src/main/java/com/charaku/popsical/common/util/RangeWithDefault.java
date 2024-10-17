package com.easternblu.khub.common.util;

import androidx.annotation.NonNull;

public class RangeWithDefault<T extends Comparable<? super T>> extends Range<T> {
    @NonNull
    Lambda<Void, T> defaultLambda;

    public RangeWithDefault(@NonNull final T defaultValue, @NonNull T lower, @NonNull T upper) {
        super(lower, upper);
        this.defaultLambda = new Lambda<Void, T>() {
            @Override
            public T invoke(Void aVoid) {
                return defaultValue;
            }
        };
    }

    public RangeWithDefault(@NonNull Lambda<Void, T> defaultLambda, @NonNull T lower, @NonNull T upper) {
        super(lower, upper);
        this.defaultLambda = defaultLambda;
    }

    public T getDefault() {
        return defaultLambda.invoke(null);
    }
}
