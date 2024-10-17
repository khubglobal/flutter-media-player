package com.easternblu.khub.common.ui;


import android.os.Bundle;

/**
 * By implementing this interface, it is implied that the implementation will call
 * the necessary activity life cycle methods like {@link ActivityListener#onCreate(Bundle)}
 * at the right time.
 *
 * @see {@link ActivityListener}
 */
public interface ActivityListenerSupport {

    /**
     * Add {@link ActivityListener} and get life cycle callback
     *
     * @param listener
     */
    public void addActivityListener(ActivityListener listener);

    /**
     * remove an added {@link ActivityListener}
     *
     * @param listener
     */
    public void removeActivityListener(ActivityListener listener);

    /**
     * Remove all added {@link ActivityListener}
     */
    public void removeAllActivityListeners();


}
