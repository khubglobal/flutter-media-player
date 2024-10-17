package com.easternblu.khub.common.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * A class to avoid making duplicate retrofit async calls. First call to {@link #enqueue(CompletionCallback)} will be done immediately.
 * Subsequence call will wait until first call is finished and the {@link CompletionCallback} will be called with the first call's result.
 * Created by pan on 18/9/17.
 */
public abstract class QueuedAsyncCall<R, K> {
    private final String TAG = QueuedAsyncCall.class.getSimpleName();
    @Nullable
    protected volatile K cache = null;

    protected volatile boolean calling = false;

    protected RetrofitApi.EnqueueCallback<R, K> singleCallback = new RetrofitApi.EnqueueCallback<R, K>() {

        @Override
        public final synchronized void onSuccess(R ref, K responseObject) {
            for (CompletionCallback<K, Throwable> completionCallback : completionCallbacks) {
                completionCallback.onSuccess(responseObject);
            }
            clearCompletionCallbacks();
        }

        @Override
        public final synchronized void onApplicationError(R ref, Throwable throwable) {
            for (CompletionCallback<K, Throwable> completionCallback : completionCallbacks) {
                completionCallback.onError(throwable);
            }
            clearCompletionCallbacks();
        }

        @Override
        public final synchronized void onConnectionFailure(R ref, Throwable throwable) {
            for (CompletionCallback<K, Throwable> completionCallback : completionCallbacks) {
                completionCallback.onError(throwable);
            }
            clearCompletionCallbacks();
        }
    };

    private List<CompletionCallback<K, Throwable>> completionCallbacks = Collections.synchronizedList(new ArrayList<CompletionCallback<K, Throwable>>());


    /**
     * Call this to do the calling
     *
     * @param completionCallback
     */
    public synchronized void enqueue(@NonNull CompletionCallback<K, Throwable> completionCallback) {
        completionCallbacks.add(completionCallback);
        if (!isCalling()) {
            Timber.d("Calling " + QueuedAsyncCall.class.getSimpleName() + " implementation with " + completionCallbacks.size() + " queued callbacks");
            enqueueCall(singleCallback);
        }
    }


    protected synchronized void clearCompletionCallbacks() {
        completionCallbacks.clear();
    }

    /**
     * Implement this so that it knows how to do the actual call
     *
     * @param enqueueCallback
     */
    protected abstract void enqueueCall(RetrofitApi.EnqueueCallback<R, K> enqueueCallback);

    protected synchronized boolean isCalling() {
        return calling;
    }

    protected synchronized void setCalling(boolean calling) {
        this.calling = calling;
    }

}
