package com.easternblu.khub.common.util.throttle;

import android.os.SystemClock;

import java.util.LinkedList;

/**
 * not thread safe
 * <p>
 * Think of it as limits:<p>
 * 1) If at least 1 slot is free then it will return perform action (true)<p>
 * 2) All slots are checked, if one is on cooldown, then discard <p>
 * 3) repeat 1)'s logic
 * Created by pan on 5/6/17.
 */
public class BasicThrottle extends LinkedList<Long> implements Throttle {

    private int limits;
    private long cooldownDuration;

    /**
     * As an example a throttle of 5 limits and a cooldown of 1 sec
     * <p>
     * i.e. if all 5 limits are full then try to remove items more than 1 sec old if
     * no limits available afterward still then it cannot acquire limits
     *
     * @param limits          5
     * @param cooldownDuration 1000
     */
    public BasicThrottle(int limits, long cooldownDuration) {
        this.limits = limits;
        this.cooldownDuration = cooldownDuration;
    }


//    @Override
//    public boolean add(Long o) {
//        boolean added = super.add(o);
//        while (added && size() > limits) {
//            super.removeLast();
//        }
//        return added;
//    }

    /**
     * Before reaching the limit, everything can go through (return true)
     * <p>
     * After reaching the limit, we will clear slot(s) if older than cooldownDuration
     * <p>
     * After clearing, if the queue's limits still reached then we cannot acquire slot (return false)
     * <p>
     *
     * @return
     */
    @Override
    public synchronized boolean acquireSlot() {
        long time = getNow();

        if (this.hasLimits()) {
            this.add(time);
            return true;
        }

        // clear old items
        while (!this.isEmpty()) {
            if (time - this.getFirst() > cooldownDuration) {
                this.removeFirst();
            } else {
                break;
            }
        }

        if (this.hasLimits()) {
            this.add(time);
            return true;
        } else {
            return false;
        }
    }


    protected Long getNow() {
        return SystemClock.elapsedRealtime();
    }

    private int getLimits() {
        return limits;
    }

    private boolean hasLimits() {
        return size() < getLimits();
    }


    @Override
    public long getWaitTime() {
        if (isEmpty()) {
            return 0;
        } else {
            Long first = getFirst();
            return first == null ? 0 : Math.max(0, cooldownDuration - (getNow() - first));
        }
    }
}

