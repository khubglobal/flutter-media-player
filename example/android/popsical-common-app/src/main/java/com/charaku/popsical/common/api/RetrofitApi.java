package com.easternblu.khub.common.api;

import android.content.Context;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import android.util.Pair;

import com.easternblu.khub.common.util.Maps;
import com.easternblu.khub.common.util.OkHttpHeaderChanger;
import com.easternblu.khub.common.util.OkHttpLogger;
import com.easternblu.khub.common.util.Sets;
import com.easternblu.khub.common.util.Strings;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by pan on 20/6/17.
 */

public abstract class RetrofitApi<E, SE extends Exception> {

    public final String TAG = this.getClass().getSimpleName();
    protected static final String HEADER_USER_AGENT = "User-Agent";

    protected static final String TAG_DELIM = ";"; // delimiter for multiple tags

    protected Retrofit retrofit;
    protected Retrofit.Builder retrofitBuilder;
    protected OkHttpClient.Builder clientBuilder;


    protected volatile Pair<String, E> endPoints;
    protected Context ctx;
    private OkHttpClient httpClient = null;
    protected long connectTimeout = 10, readTimeout = 10, writeTimeout = 10;


    protected RetrofitApi(Context ctx, long connectTimeout, long readTimeout, long writeTimeout) {
        this.ctx = ctx.getApplicationContext();
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        setupAndBuild();
    }


    protected void setupAndBuild() {
        setup();
        build(getBaseUrl(""));
    }


    /**
     * Get the base url
     *
     * @param apiEndPoint
     * @param urlParams
     * @return
     */
    protected abstract String getBaseUrl(@Nullable String apiEndPoint, Pair<String, String>... urlParams);


    @Nullable
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Get the right  object for the selected server
     */
    @NonNull
    protected synchronized E getEndPoints() {
        String baseUrl = getBaseUrl("");
        if (endPoints == null || !endPoints.first.equals(baseUrl)) {
            build(baseUrl);
        }
        return endPoints.second;
    }


    protected synchronized void resetEndpoints() {
        if (endPoints != null) {
            Timber.w("resetEndpoints " + endPoints.first + " hash = " + endPoints.second);
            endPoints = null;
        }
    }


    /**
     * This is the the main http setup method for this class. Override it to do custom okhttp stuff
     */
    protected void setup() {
        // once a httpclient has an IOException it tends to get 'stuck' so a new one will need
        // to be assigned
        // @see <a href="https://github.com/square/okhttp/issues/3146"/>
        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.HTTP_1_1);

        // dependent on packagename and version code

        // File cacheDir = new File(System.getProperty("java.io.tmpdir"), ctx.getPackageName() + ".v" + BuildConfig.VERSION_CODE);
        ConnectionPool connectionPool = new ConnectionPool(0, 5, TimeUnit.MINUTES);
        clientBuilder = new OkHttpClient.Builder().
                cache(new Cache(getCacheDir(), 10000000)). // 10MB
                connectTimeout(connectTimeout, TimeUnit.SECONDS);

        onCreateBuilder(clientBuilder).
                addInterceptor(getHeaderChanger()).
                addInterceptor(getLogger()).
                readTimeout(readTimeout, TimeUnit.SECONDS). // fadhli wants a shorter timeout
                writeTimeout(writeTimeout, TimeUnit.SECONDS). // fadhli wants a shorter timeout
                connectionPool(connectionPool).
                protocols(protocols);
    }

    /**
     * get the cache directory
     *
     * @return
     */
    public abstract File getCacheDir();

    protected Interceptor getHeaderChanger() {
        return new OkHttpHeaderChanger() {
            @Override
            protected Map<String, String> getHeaderReplacements(Request request) {
                return Maps.create(new HashMap<String, String>(), new androidx.core.util.Pair<>(HEADER_USER_AGENT, getUserAgent()));
            }
        };
    }


    protected Interceptor getLogger() {
        return new OkHttpLogger(TAG) {
            @Override
            public boolean isEnabled() {
                return RetrofitApi.this.isLoggerEnabled();
            }
        }.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    protected abstract boolean isLoggerEnabled();


    /**
     * Override this method to add custom interceptor (like {@link okhttp3.Authenticator} and/or {@link okhttp3.Interceptor})
     *
     * @param builder
     * @return
     */
    protected abstract OkHttpClient.Builder onCreateBuilder(OkHttpClient.Builder builder);

    protected OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }

    protected void build(String baseUrl) {
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(defaultGson()))
                .client(httpClient = clientBuilder.build());
        retrofit = retrofitBuilder.build();
        endPoints = new Pair<>(baseUrl, retrofit.create(getEndPointsClass()));
    }


    protected abstract Class<E> getEndPointsClass();

    /**
     * {@link Gson} needed for parsing JSON text
     *
     * @return
     */
    protected abstract Gson defaultGson();


    /**
     * Synchronous call
     *
     * @param blockingCall
     * @param <T>
     * @return
     * @throws SE
     * @throws IOException
     */
    @WorkerThread
    protected <T> T execute(Call<T> blockingCall) throws SE, IOException {
        return execute(null, blockingCall, true);
    }


    /**
     * Synchronous call
     *
     * @param ref
     * @param call
     * @param checkContentType
     * @param <T>
     * @return
     * @throws SE
     * @throws IOException
     */
    @WorkerThread
    protected <T> T execute(final Object ref, final Call<T> call, boolean checkContentType) throws SE, IOException {
        Response<T> response = executeRaw(ref, call, checkContentType);
        return response.body();
    }


    /**
     * @param ref
     * @param call
     * @param checkContentType
     * @param <T>
     * @return
     * @throws SE
     * @throws IOException
     */
    @WorkerThread
    protected <T> Response<T> executeRaw(final Object ref, final Call<T> call, boolean checkContentType) throws SE, IOException {
        Response<T> response = call.execute();

        if (response.isSuccessful()) {
            if (checkContentType)
                checkResponseType(response);
            return response;
        } else {
            throw getApplicationError(ref, response, checkContentType);
        }
    }


    /**
     * Convert response to {@link SE}
     *
     * @param ref
     * @param response
     * @param <T>
     * @return
     * @throws IOException
     */
    @NonNull
    protected abstract <T> SE getApplicationError(final Object ref, Response<T> response, boolean checkContentType) throws IOException;


    protected <T> void enqueue(Call<T> call, @NonNull EnqueueCallback<Void, T> callback) {
        enqueue(null, call, true, callback);
    }


    /**
     * @param call
     * @param callback
     * @param <T>
     */
    protected <T> void enqueue(Call<T> call, boolean checkContentType, @Nullable EnqueueCallback<Void, T> callback) {
        enqueue(null, call, checkContentType, callback);
    }


    /**
     * Asynchronous call
     *
     * @param call
     * @param callback
     * @param <T>
     */
    protected <R, T> void enqueue(R ref, Call<T> call, @Nullable EnqueueCallback<R, T> callback) {
        enqueue(ref, call, true, callback);
    }


    /**
     * Asynchronous call
     *
     * @param ref
     * @param call
     * @param checkContentType
     * @param callback
     * @param <T>
     */
    protected <R, T> void enqueue(final R ref, final Call<T> call, final boolean checkContentType, @Nullable final EnqueueCallback<R, T> callback) {
        call.enqueue(new Callback<T>() {

            @MainThread
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (callback == null)
                    return;

                try {
                    if (response.isSuccessful()) {
                        if (checkContentType)
                            checkResponseType(response);
                        callback.onSuccess(ref, response.body());
                    } else {
                        Throwable applicationError = null;
                        try {
                            applicationError = getApplicationError(ref, response, checkContentType);
                        } catch (Throwable t) {
                            applicationError = t;
                        }

                        callback.onApplicationError(ref, applicationError);
                    }
                } catch (IOException e) {
                    callback.onConnectionFailure(ref, e);
                }
            }

            @MainThread
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (callback == null)
                    return;
                callback.onConnectionFailure(ref, t);
            }
        });
    }

    protected abstract void checkResponseType(Response response) throws IOException;


    /**
     * General callback for all Asynchonous call
     *
     * @param <T>
     */
    public interface EnqueueCallback<R, T> {
        @MainThread
        public void onSuccess(R ref, T responseObject);

        @MainThread
        public void onApplicationError(R ref, Throwable throwable);

        @MainThread
        public void onConnectionFailure(R ref, Throwable throwable);
    }

    /**
     * a simple implementation of {@link EnqueueCallback} without reference object
     *
     * @param <T>
     */
    public static abstract class SimpleEnqueueCallback<T> implements EnqueueCallback<Void, T> {

        @Override
        public void onSuccess(Void ref, T responseObject) {
            onSuccess(responseObject);
        }

        @Override
        public void onApplicationError(Void ref, Throwable throwable) {
            onErrorOrFailure(throwable);
        }

        @Override
        public void onConnectionFailure(Void ref, Throwable throwable) {
            onErrorOrFailure(throwable);
        }

        @MainThread
        public abstract void onSuccess(T responseObject);

        @MainThread
        public abstract void onErrorOrFailure(Throwable throwable);

    }

//    /**
//     * General interceptor to subscribe {@link #HEADER_USER_AGENT}
//     */
//    class GeneralHttpInterceptor implements Interceptor {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            return chain.proceed(chain.request().newBuilder().removeHeader(HEADER_USER_AGENT).addHeader(HEADER_USER_AGENT, getUserAgent()).build());
//        }
//    }


    /**
     * @param request
     * @param tag
     * @return
     */
    protected static boolean containsHeaderTag(Request request, String headerName, String tag) {
        String authTag = request.header(headerName);
        return authTag != null && authTag.contains(tag);
    }

    /**
     * @param requestBuilder
     * @param tag
     */
    protected static void putHeaderTag(Request.Builder requestBuilder, String headerName, @NonNull String tag) {
        Set<String> tags = new HashSet<>(Strings.splitByString(tag, TAG_DELIM.toCharArray(), true));
        requestBuilder.removeHeader(headerName);
        tags.add(tag);
        requestBuilder.addHeader(headerName, Sets.asString(tags, TAG_DELIM, true, Strings.STRING_CONVERTER));
    }

    /**
     * @param requestBuilder
     * @param tag
     */
    protected static void removeHeaderTag(Request.Builder requestBuilder, String headerName, @NonNull String tag) {
        Set<String> tags = new HashSet<>(Strings.splitByString(tag, TAG_DELIM.toCharArray(), true));
        requestBuilder.removeHeader(headerName);
        tags.remove(tag);
        if (Sets.isNotEmpty(tags))
            requestBuilder.addHeader(headerName, Sets.asString(tags, TAG_DELIM, true, Strings.STRING_CONVERTER));
    }


    /**
     * User agent etc
     *
     * @return
     */
    public abstract String getUserAgent();
}
