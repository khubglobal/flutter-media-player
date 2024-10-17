package com.easternblu.khub.common.api;

/**
 * A generic callback for things asynchronous task that can either end in success or error
 * <p>
 * Created by pan on 4/9/17.
 */
public interface CompletionCallback<K, E extends Throwable> {

    public void onSuccess(K object);

    public void onError(E exception);

}
