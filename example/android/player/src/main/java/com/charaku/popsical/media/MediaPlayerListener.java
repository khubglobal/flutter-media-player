package com.easternblu.khub.media;

import android.net.Uri;

import com.easternblu.khub.media.model.PlaybackErrorType;

/**
 * Created by yatpanng on 23/4/17.
 * A Listener to model the traditional state change of a player
 * <p>
 * start/resume/stop/pause/buffer/stop/idle
 * <p>
 * Also for other things
 */
public interface MediaPlayerListener {

    /**
     * Playing is going to start
     * When changing audio/video track of the same content, it will call onMediaStart again.
     * <p>
     * So if restart is true then it is not the first time for the MediaContent to call onMediaStart
     * the MediaContent is identified via MediaContent.playbackId
     */
    void onMediaStart(boolean restart);

    /**
     * Resumed manually or from idle
     */
    void onMediaResume();

    /**
     * Paused manually
     */
    void onMediaPause();

    /**
     * Buffering
     */
    void onMediaBuffering();

    /**
     * When the playback has reached the end
     */
    void onMediaStop();

    /**
     * idle
     */
    void onMediaIdle();

    /**
     * When player tries to play but nothing from {@link com.easternblu.khub.media.model.MediaContent}
     */
    void onMediaContentEmpty();

    /**
     * Player object just created
     */
    void onPlayerCreated();

    /**
     * Player object GC (might soon creating another player depending on state)
     */
    void onPlayerDestroyed();

    /**
     * Report that a error has been encountered
     *
     * @param event
     * @param args
     */
    void onMediaPlayerError(Throwable error, PlaybackErrorType event, Object... args);

    /**
     * called some time after playback has started
     */
    void onPlayBeginning();

    /**
     * called some time before playback ends
     */
    void onPlayEnding();

    /**
     * Called contini time when
     *
     * @param currentPosition
     * @param duration
     */
    void onPlayerProgressUpdate(int currentPosition, long duration);

    /**
     * Show in AppMonitor
     *
     * @param msg
     * @return
     */
    void onNetworkTraffiLog(String msg);


    /**
     * play next media content
     */
    void next();


    /**
     * previous next media content
     */
    void previous();


    /**
     * Callback when volume has been changed (this can be triggered by the player or by the user originally)
     *
     * @param musicVolume
     * @param muted
     */
    void onMusicVolumeChanged(float musicVolume, boolean muted);

    /**
     * Called when a file (or part of a file) has started downloading
     *
     * @param uri
     */
    void onMediaTransferStart(Uri uri);

    /**
     * Called when a file (or part of a file) is being downloaded at the moment
     *
     * @param uri
     * @param bytesTransferred
     * @param duration
     */
    void onMediaTransferring(Uri uri, long bytesTransferred, long duration);

    /**
     * Called when a file (or part of a file) has finished downloaded
     *
     * @param uri
     * @param bytesTransferred
     * @param duration
     */
    void onMediaTransferEnd(Uri uri, long bytesTransferred, long duration);
}