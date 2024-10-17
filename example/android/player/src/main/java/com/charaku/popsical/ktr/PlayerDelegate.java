package com.easternblu.khub.ktr;

/**
 * An interface to get information from the media player
 * Created by pan on 14/12/17.
 */

public interface PlayerDelegate {

    long getMediaProgress();

    long getMediaDuration();

    boolean isPlaying();

    double getSpeed();
}
