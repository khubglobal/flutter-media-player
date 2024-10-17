package com.easternblu.khub.ktr;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by pan on 14/12/17.
 */

public class MainThreadScheduler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (shouldExecute(msg) && msg.obj instanceof Runnable) {
            ((Runnable) msg.obj).run();
        }
    }

    protected boolean shouldExecute(Message msg) {
        return true;
    }

    public void schedule(int what, long delay, Runnable r) {
        Message msg = new Message();
        msg.obj = r;
        msg.what = what;
        sendMessageAtTime(msg, Math.max(0, SystemClock.uptimeMillis() + delay));
    }

    public void cancelPendingSchedule(int what) {
        removeMessages(what);
    }

}
