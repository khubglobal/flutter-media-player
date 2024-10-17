//package com.easternblu.khub.media.exoplayer2;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.media.PlaybackParams;
//import android.os.Build;
//import androidx.annotation.FloatRange;
//import androidx.annotation.Nullable;
//
//import com.easternblu.khub.media.model.ChannelMapping;
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.Renderer;
//import com.google.android.exoplayer2.RenderersFactory;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
//import com.google.android.exoplayer2.trackselection.TrackSelector;
//
//import timber.log.Timber;
//
///**
// * Created by yatpanng on 8/4/17.
// */
//public class MediaPlayerCore extends SimpleExoPlayer {
//
//    public final String TAG = this.getClass().getName();
//
//    private ChannelMapping channelMapping = ChannelMapping.Center;
//    protected Context ctx;
//
//    public MediaPlayerCore(Context context, RenderersFactory renderersFactory, TrackSelector trackSelector) {
//        super(renderersFactory, trackSelector, new DefaultLoadControl(), null);
//        ctx = context;
//    }
//
//    protected Context getContext() {
//        return ctx;
//    }
//
//
//    public ChannelMapping getChannelMapping() {
//        return channelMapping;
//    }
//
//
//    /**
//     * Convert our {@link ChannelMapping} enum object to {@link MediaCodecAudioRenderer.ChannelSide}
//     *
//     * @param channelMapping
//     * @return
//     */
//    @MediaCodecAudioRenderer.ChannelSide
//    private int toChannelSide(ChannelMapping channelMapping) {
//        if (channelMapping == ChannelMapping.Left) {
//            return MediaCodecAudioRenderer.LEFT;
//        } else if (channelMapping == ChannelMapping.Right) {
//            return MediaCodecAudioRenderer.RIGHT;
//        } else {
//            return MediaCodecAudioRenderer.LEFT | MediaCodecAudioRenderer.RIGHT;
//        }
//    }
//
//    /**
//     * Send a custom message to the player which will be handled by MediaCodecAudioRenderer and
//     * {@link MediaCodecAudioRenderer#setMonoChannels(int)} will be called inside the
//     * internal {@link android.os.Handler}
//     * <p/>
//     *
//     * @param channelMapping
//     */
//    public void setChannelMapping(ChannelMapping channelMapping) {
//        this.channelMapping = channelMapping;
//        for (Renderer renderer : renderers) {
//            if (renderer.getTrackType() == C.TRACK_TYPE_AUDIO) {
//                createMessage(renderer)
//                        .setType(C.MSG_SET_CHANNEL_MAPPING)
//                        .setPayload(toChannelSide(channelMapping))
//                        .send();
//                Timber.d("setChannelMapping: ExoPlayerMessage created");
//            }
//        }
//    }
//
//
//    public boolean isMonoAvailable() {
//        return renderers != null && renderers.length > 0;
//    }
//
//    @Override
//    public void setPlayWhenReady(boolean playWhenReady) {
//        //Log.printInvokerStack("setPlayWhenReady = " + playWhenReady, Log.WARN);
//        super.setPlayWhenReady(playWhenReady);
//    }
//
//
//    /**
//     * Don't create new instance of {@link PlaybackParams}, it will reset existing values (e.g. speed and pitch)
//     *
//     * @return
//     */
//    @Nullable
//    private PlaybackParameters getDefaultPlaybackParams() {
//        PlaybackParameters playbackParams = getPlaybackParameters();
//        if (playbackParams == null)
//            playbackParams = new PlaybackParameters(1, 1);
//        return playbackParams;
//
//    }
//
//    /**
//     * Set pitch
//     *
//     * @param pitch
//     */
//    @TargetApi(Build.VERSION_CODES.M)
//    public void setPitch(@FloatRange(from = 0.0, to = 2) float pitch) {
//        PlaybackParameters playbackParams = getDefaultPlaybackParams();
//        Timber.d("setPitch: " + pitch);
//        setPlaybackParameters(new PlaybackParameters(playbackParams.speed, pitch));
//    }
//
//    private static final float MIN_SPEED = 0.2f, NORMAL_SPEED = 1, MAX_SPEED = 2.5f;
//
//    /**
//     * Set speed
//     *
//     * @param
//     */
//    @TargetApi(Build.VERSION_CODES.M)
//    public void setSpeed(@FloatRange(from = 0, to = 1) float normalizedSpeed) {
//        float actualPlaybackSpeed = convertToPlaybackSpeed(normalizedSpeed);
//        PlaybackParameters playbackParams = getDefaultPlaybackParams();
//        Timber.d("speedNormalized = " + normalizedSpeed + ", actualPlaybackSpeed = " + actualPlaybackSpeed);
//        setPlaybackParameters(new PlaybackParameters(actualPlaybackSpeed, playbackParams.pitch));
//    }
//
//    /**
//     * Get current playback speed
//     *
//     * @return
//     */
//    public float getSpeed() {
//        return getDefaultPlaybackParams().speed;
//    }
//
//
//    /**
//     * The function to calcuate the actual speed to set for the playback.
//     * This cannot be a linear function as it will with a 0 y intersect, as we cannot
//     * allow playback to reach 0. So it will be discontinous function with mid point at 1,1.
//     * <p>
//     * A f(x) that intersect at [0, min], [0.5, 1] [1, max] where min > 0
//     *
//     * @param normalizedSpeed
//     * @return
//     */
//    @FloatRange(from = MIN_SPEED, to = MAX_SPEED)
//    public static float convertToPlaybackSpeed(@FloatRange(from = 0, to = 1) float normalizedSpeed) {
//        float y1, x1, y2, x2, m, c, x = normalizedSpeed;
//        if (normalizedSpeed == 0.5f)
//            return NORMAL_SPEED;
//
//        if (normalizedSpeed > 0.5f) {
//            y1 = MAX_SPEED;
//            y2 = NORMAL_SPEED;
//            x1 = 1;
//            x2 = 0.5f;
//        } else {
//            y1 = NORMAL_SPEED;
//            y2 = MIN_SPEED;
//            x1 = 0.5f;
//            x2 = 0;
//        }
//        m = (y1 - y2) / (x1 - x2);
//        c = y1 - (m * x1);
//        return m * x + c;
//    }
//
//
//    public class CustomExoPlayerException extends Exception {
//        public CustomExoPlayerException(Throwable cause) {
//            super(cause);
//        }
//    }
//
//
//}
