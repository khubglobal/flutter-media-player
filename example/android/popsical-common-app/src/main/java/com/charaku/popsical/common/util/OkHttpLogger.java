package com.easternblu.khub.common.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by pan on 10/11/17.
 */

public abstract class OkHttpLogger implements Interceptor, HttpLoggingInterceptor.Logger {

    private final String TAG;
    protected HttpLoggingInterceptor interceptor;


    public OkHttpLogger(String tag) {
        TAG = tag;
        interceptor = new HttpLoggingInterceptor(this);
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return interceptor.getLevel();
    }

    public OkHttpLogger setLevel(HttpLoggingInterceptor.Level level) {
        interceptor.setLevel(level);
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response;
        if (isEnabled()) {
            response = interceptor.intercept(chain);
            boolean isNetworkResponse = response.networkResponse() != null;
            boolean isCacheResponse = response.cacheResponse() != null;
            log((isNetworkResponse ? "[NETWORK]" : "") + (isCacheResponse ? "[CACHE]" : ""));
            onPostIntercept(response, isNetworkResponse, isCacheResponse);
        } else {
            response = chain.proceed(chain.request());
        }
        return response;
    }

    protected void onPostIntercept(Response response, boolean network, boolean cache) {

    }

    public abstract boolean isEnabled();

    @Override
    public void log(String message) {
        Timber.i(message);
    }
}