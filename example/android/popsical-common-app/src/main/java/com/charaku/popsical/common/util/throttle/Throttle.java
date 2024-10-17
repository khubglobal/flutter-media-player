package com.easternblu.khub.common.util.throttle;

/**
 * Created by pan on 7/6/17.
 */
public interface Throttle {

    /**
     * True if it is allowed to perform an action
     *
     * @return
     */
    public boolean acquireSlot();

    /**
     * the wait time required to perform an action
     *
     * @return
     */
    public long getWaitTime();

}
