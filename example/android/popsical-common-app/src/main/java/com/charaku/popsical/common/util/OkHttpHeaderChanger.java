package com.easternblu.khub.common.util;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by pan on 10/11/17.
 */

public abstract class OkHttpHeaderChanger implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Map<String, String> headers = getHeaderReplacements(request);
        if (Maps.isNotEmpty(headers)) {
            Request.Builder builder = request.newBuilder();
            for (String name : headers.keySet()) {
                builder.removeHeader(name).addHeader(name, headers.get(name));
            }
            request = builder.build();
        }
        return chain.proceed(request);
    }

    protected abstract Map<String, String> getHeaderReplacements(Request request);
}