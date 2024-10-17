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
public class DefaultMediaPlayerListener implements MediaPlayerListener {


    @Override
    public void onMediaStart(boolean restart) {

    }

    @Override
    public void onMediaResume() {

    }

    @Override
    public void onMediaPause() {

    }

    @Override
    public void onMediaBuffering() {

    }

    @Override
    public void onMediaStop() {

    }

    @Override
    public void onMediaIdle() {

    }

    @Override
    public void onMediaContentEmpty() {

    }

    @Override
    public void onPlayerCreated() {

    }

    @Override
    public void onPlayerDestroyed() {

    }

    @Override
    public void onMediaPlayerError(Throwable error, PlaybackErrorType event, Object... args) {

    }

    @Override
    public void onPlayBeginning() {

    }

    @Override
    public void onPlayEnding() {

    }

    @Override
    public void onPlayerProgressUpdate(int currentPosition, long duration) {

    }

    @Override
    public void onNetworkTraffiLog(String msg) {
    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    @Override
    public void onMusicVolumeChanged(float musicVolume, boolean muted) {

    }

    @Override
    public void onMediaTransferStart(Uri uri) {

    }

    @Override
    public void onMediaTransferring(Uri uri, long bytesTransferred, long duration) {

    }

    @Override
    public void onMediaTransferEnd(Uri uri, long bytesTransferred, long duration) {

    }
}