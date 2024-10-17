package com.easternblu.khub.media;

import androidx.annotation.Nullable;

import com.easternblu.khub.media.model.ChannelMapping;
import com.easternblu.khub.media.model.MediaContent;

/**
 * A interface that abstract the playback control aspect of a media player. Can be referenced by a UI class
 * to abstract and delegate the actual functions
 * Created by yatpanng on 8/4/17.
 */
public interface MediaPlayerControl extends MediaVolumeControl {
    /**
     * Same a resume
     */
    void play();

    /**
     * Pause a track
     */
    void pause();

    /**
     * should proceed playing next {@link MediaContent}
     */
    void next();

    /**
     * should proceed playing previous {@link MediaContent}
     */
    void previous();

    /**
     * Return total playback duration 60000 mean 1:00
     *
     * @return
     */
    int getDuration();

    /**
     * Return current playback duration 1000 mean 0:01
     *
     * @return
     */
    int getCurrentPosition();

    /**
     * Go to a certain position of the playback
     *
     * @param pos
     */
    void seekTo(int pos);

    /**
     * Return the current state of the ChannelMapping
     */
    ChannelMapping getCurrentChannelMapping();

    /**
     * Set the current state of ChannelMapping
     *
     * @param channelMapping
     * @param userExplicitAction
     * @param channelMappingIsVocal
     */
    void setCurrentChannelMapping(ChannelMapping channelMapping, boolean userExplicitAction, @Nullable Boolean channelMappingIsVocal);

    /**
     * true if player is playing (duh)
     *
     * @return
     */
    boolean isPlaying();

    /**
     * buffer percentage
     *
     * @return
     */
    int getBufferPercentage();



    /**
     * Resume from pauseTimer
     */
    void resumeTimer();


    /**
     * Allow UI to explicitly pause the duration timer countdown (does not really pause the playback)
     * <p>
     * use this for seekbar dragging
     */
    void pauseTimer();

}
