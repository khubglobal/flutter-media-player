package com.easternblu.khub.common.util;


import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple replacement to avoid the single task limitation of {@Link AsyncTask}. It uses {@link #executeNow(Object[])}
 * Newer implemenation of AsyncTask since 4.4 allows parallel execution (based on CPU core) but the minimum can be as low
 * as 2. If some async task decides to block the thread then it will affect the rest of the app in a very bad way.
 * <p>
 * This implementation  will fix the thread pool to 4 and a max of 8
 * <p>
 * https://stackoverflow.com/questions/4068984/running-multiple-asynctasks-at-the-same-time-not-possible
 * <p>
 * Created by yatpanng on 19/9/17.
 */
public abstract class PopsicalAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private static final int MINIMUM_POOL_SIZE = 4, MAXIMUM_POOL_SIZE = 8;
    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final ThreadFactory sPopsicalThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "PopsicalAsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPopsicalPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor POPSICAL_THREAD_POOL_EXECUTOR;

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                MINIMUM_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPopsicalPoolWorkQueue, sPopsicalThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        POPSICAL_THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }




    /**
     * The core method that is provided by this class. It replaces {@link #execute(Object[])}
     *
     * @param params
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    public PopsicalAsyncTask<Params, Progress, Result> executeNow(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            this.executeOnExecutor(POPSICAL_THREAD_POOL_EXECUTOR, params);
        else
            this.execute(params);
        return this;
    }


    public boolean isError(Class<? extends Exception> exceptionType, Exception e) {
        return optError(exceptionType, e) != null;
    }

    @Nullable
    public <E extends Exception> E optError(Class<E> exceptionType, Exception e) {
        return exceptionType != null && exceptionType.isInstance(e) ? ((E) e) : null;
    }


//
//    public enum Status {
//        /**
//         * Indicates that the task has not been executed yet.
//         */
//        PENDING,
//        /**
//         * Indicates that the task is running.
//         */
//        RUNNING,
//        /**
//         * Indicates that {@link PopsicalAsyncTask#onPostExecute} has finished.
//         */
//        FINISHED,
//    }
//
//    private static ExecutorService THREADPOOL = Executors.newFixedThreadPool(5);
//
//    protected Handler handler;
//    protected volatile Status status = null;
//    protected volatile boolean cancelled = false;
//    protected volatile Thread runningThread = null;
//
//    public PopsicalAsyncTask() {
//    }
//
//
//    protected abstract Result doInBackground(Params... var1);
//
//    @MainThread
//    protected void onPreExecute() {
//
//    }
//
//    @MainThread
//    protected void onPostExecute(Result result) {
//
//    }
//
//    @MainThread
//    protected final void publishPostExecute(final Result result) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (!isCancelled()) {
//                    onPostExecute(result);
//                    setStatus(Status.FINISHED);
//                }
//            }
//        });
//    }
//
//
//    private boolean isMainThread() {
//        return Looper.getMainLooper() == Looper.myLooper();
//    }
//
//    @MainThread
//    public final PopsicalAsyncTask<Params, Progress, Result> execute(final Params... params) {
//        if (!isMainThread()) {
//            throw new IllegalArgumentException("Must execute on main thread");
//        }
//        setStatus(Status.PENDING);
//        handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                onPreExecute();
//                executeOnExecutor(params);
//            }
//        });
//        return this;
//    }
//
//
//    protected final void executeOnExecutor(final Params... var1) {
//        THREADPOOL.execute(new Runnable() {
//            @Override
//            public void run() {
//                runningThread = Thread.currentThread();
//                setStatus(Status.RUNNING);
//                try {
//                    if (!isCancelled()) {
//                        publishPostExecute(doInBackground(var1));
//                    }
//                } catch (Throwable t) {
//                    if (isCancelled()) {
//
//                    } else {
//                        publishUncaughtException(t);
//                    }
//                }
//            }
//        });
//    }
//
//    @MainThread
//    protected void onProgressUpdate(Progress... values) {
//
//    }
//
//
//    @MainThread
//    protected final void publishProgress(final Progress... values) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                onProgressUpdate(values);
//            }
//        });
//    }
//
//
//    @MainThread
//    protected void onCancelled() {
//    }
//
//    public final void cancel(boolean mayInterruptIfRunning) {
//        if (isCancelled())
//            return;
//        setCancelled(true);
//        if (runningThread != null) {
//            runningThread.interrupt();
//        }
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                onCancelled();
//            }
//        });
//    }
//
//
//    @MainThread
//    protected void onUncaughtException(Throwable error) {
//
//    }
//
//
//    @MainThread
//    protected final void publishUncaughtException(final Throwable t) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                onUncaughtException(t);
//            }
//        });
//    }
//
//    @Nullable
//    public synchronized Status getStatus() {
//        return status;
//    }
//
//    private synchronized void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public synchronized boolean isCancelled() {
//        return cancelled;
//    }
//
//    private synchronized void setCancelled(boolean cancelled) {
//        this.cancelled = cancelled;
//    }
}
