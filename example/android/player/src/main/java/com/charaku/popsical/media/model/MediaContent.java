package com.easternblu.khub.media.model;

import android.net.Uri;

/**
 * A interface to model a video's source and meta data
 */
public interface MediaContent {
    /**
     * Display title
     *
     * @return
     */
    String getTitle();

    /**
     * dash/mp4/hls
     *
     * @return
     */
    Uri getUri();


    /**
     * A source tag that can be used identify the source device
     *
     * @return
     */
    String getTag();

    /**
     * Some id for this content
     *
     * @return
     */
    String getId();

    /**
     * Some id for this video (may or may not be the same as getId)
     *
     * @return
     */
    String getVideoId();

    /**
     * Language code for content
     *
     * @return
     */
    String getLangCode();

    /**
     * playback id for next/previous (something like a queue position)
     *
     * @return
     */
    String getPlaybackId();

}
