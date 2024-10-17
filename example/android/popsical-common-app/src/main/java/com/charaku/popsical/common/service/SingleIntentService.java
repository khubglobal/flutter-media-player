package com.easternblu.khub.common.service;

import android.content.Intent;

import timber.log.Timber;

/**
 * <a href="http://stackoverflow.com/questions/7318666/android-intentservice-how-abort-or-skip-a-task-in-the-handleintent-queue">API Doc</a>
 * A service that will ignore subsequent call being queue up to the service
 * Created by pan on 12/3/17.
 */
public abstract class SingleIntentService extends ExposedIntentService {

    private final String TAG = this.getClass().getSimpleName();
    private final String mName;
    private volatile int mPending = 0;
    private volatile int mHandled = 0;

    /**
     * Creates an ExposedIntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SingleIntentService(String name) {
        super(name);
        this.mName = name;
    }

    /**
     * {@inheritDoc}
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int returnValue = super.onStartCommand(intent, flags, startId);
        mPending++;
        Timber.i("A request for " + mName + " has been made. Now with " + mPending + " pending");
        return returnValue;
    }

    /**
     * {@inheritDoc}
     *
     * @param intent The value passed to {@link
     */
    @Override
    protected final void onHandleIntent(Intent intent) {
        Timber.i("Service is responding to a call to " + mName + " with pending " + mPending);
        mHandled++;
        onHandleIntentImpl(intent);
        // clear any startService called that were in the queue
        Timber.i("Service has completed a the task. Clearing queue of " + mPending);
        mPending = 0;
        clearQueue();

    }

    /**
     * Put the actual onHandleIntent implementation here
     *
     * @param intent
     */
    protected abstract void onHandleIntentImpl(Intent intent);

    public int getPending() {
        return mPending;
    }

    public int getHandled() {
        return mHandled;
    }
}
