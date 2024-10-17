package com.easternblu.khub.media.model;

public interface MediaActivitySupport {
    /**
     * Add {@link MediaActivityListener} and get life cycle callback
     *
     * @param listener
     */
    public void addMediaActivityListener(MediaActivityListener listener);
}
