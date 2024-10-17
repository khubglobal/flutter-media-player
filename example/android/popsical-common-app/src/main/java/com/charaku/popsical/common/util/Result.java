package com.easternblu.khub.common.util;

import androidx.annotation.Nullable;

/**
 * Can be used together with {@link android.os.AsyncTask} as the Result object
 * <p/>
 * <a href="https://stackoverflow.com/questions/1739515/asynctask-and-error-handling-on-android">originally from here</a>
 * <p/>
 * Created by yatpanng on 17/2/17.
 */
public class Result<T> {
    private volatile T result;
    private volatile Exception error;

    public T getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

    public boolean hasError() {
        return error != null;
    }

    public boolean isSuccess() {
        return !hasError();
    }

    /**
     * Return the exception of the specified class if it is of that class
     *
     * @param errorClazz
     * @param <E>
     * @return
     */
    @Nullable
    public <E extends Throwable> E optException(Class<E> errorClazz) {
        if (error != null && errorClazz.isInstance(error)) {
            return (E) error;
        }
        return null;
    }


    public Result(T result) {
        super();
        this.result = result;
    }

    public Result(Exception error) {
        super();
        this.error = error;
    }
}