package com.easternblu.khub.common.util.throttle;

import android.os.SystemClock;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Thread safe
 * <p>
 *
 * @see {@link BasicThrottle}
 * Created by pan on 5/6/17.
 */
public class ThreadSafeThrottle extends Vector<Long> implements Throttle {

    private int limits;
    private long cooldownDuration;

    /**
     * As an example a throttle of 5 limits and a cooldown of 1 sec
     * <p>
     * i.e. if all 5 limits are full then try to remove items more than 1 sec old if
     * no limits available afterward still then it cannot acquire limit
     *
     * @param limits          5
     * @param cooldownDuration 1000
     */
    public ThreadSafeThrottle(int limits, long cooldownDuration) {
        this.limits = limits;
        this.cooldownDuration = cooldownDuration;
    }

    private synchronized Long removeFirst() {
        if (!this.isEmpty()) {
            return this.remove(0);
        } else {
            throw new NoSuchElementException("List is empty");
        }
    }


    private synchronized Long removeLast() {
        if (!this.isEmpty()) {
            return this.remove(this.size() - 1);
        } else {
            throw new NoSuchElementException("List is empty");
        }
    }


    private synchronized Long getFirst() {
        if (!this.isEmpty()) {
            return this.get(0);
        } else {
            throw new NoSuchElementException("List is empty");
        }
    }


    /**
     * Before reaching limits, everything can go through (return true)
     * <p>
     * After reaching limits, we will clear item(s) if older than cooldownDuration
     * <p>
     * if after clearing, the queue's limits still reached then we can go (return false)
     * <p>
     *
     * @return
     */
    @Override
    public synchronized boolean acquireSlot() {
        long time = getNow();

        if (this.hasFreeSlot()) {
            this.add(time);
            return true;
        }

        // clear old items
        while (!this.isEmpty()) {
            if (time - this.getFirst() > cooldownDuration)
                this.removeFirst();
            else
                break;
        }

        if (this.hasFreeSlot()) {
            this.add(time);
            return true;
        } else {
            return false;
        }
    }


    protected Long getNow() {
        return SystemClock.elapsedRealtime();
    }

    private synchronized int getLimits() {
        return limits;
    }

    private synchronized boolean hasFreeSlot() {
        return size() < getLimits();
    }


    @Override
    public synchronized long getWaitTime() {
        if (isEmpty()) {
            return 0;
        } else {
            Long first = getFirst();
            return first == null ? 0 : Math.max(0, cooldownDuration - (getNow() - first));
        }
    }
}