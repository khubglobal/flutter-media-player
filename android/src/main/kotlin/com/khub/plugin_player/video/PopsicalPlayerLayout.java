package com.khub.plugin_player.video;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mux.stats.sdk.core.model.CustomerPlayerData;
import com.mux.stats.sdk.core.model.CustomerVideoData;
import com.mux.stats.sdk.muxstats.MuxErrorException;
import com.mux.stats.sdk.muxstats.MuxStatsExoPlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.JSONMethodCodec;


import static com.google.android.exoplayer2.Player.REPEAT_MODE_OFF;

public class PopsicalPlayerLayout extends PlayerView implements FlutterAVPlayer, EventChannel.StreamHandler {

    /**
     * Playback Rate for the MediaPlayer is always 1.0.
     */
    private static final float PLAYBACK_RATE = 1.0f;
    public static SimpleExoPlayer activePlayer;
    private final String TAG = "PopsicalPlayerLayout";
    /**
     * Reference to the {@link SimpleExoPlayer}
     */
    SimpleExoPlayer mPlayerView;
    boolean isBound = true;
    /**
     * The underlying {@link MediaSessionCompat}.
     */
    private MediaSessionCompat mMediaSessionCompat;
    /**
     * An instance of Flutter event sink
     */
    private EventChannel.EventSink eventSink;

    private int viewId;

    private DefaultTrackSelector trackSelector;
    private String preferredAudioLanguage = "en";
    /**
     * Context
     */
    private Context context;
    private BinaryMessenger messenger;
    private String url = "";
    private String title = "";
    private String subtitle = "";
    private String userId = "";
    private String trackId = "";
    private String videoId = "";
    private String experimentName = "";
    private String propertyKey = "";
    private long position = -1;
    private boolean autoPlay = false;
    private boolean loop = false;
    private boolean isMuxStatsEnabled = false;
    private float speed = 1;
    private float pitch = 1;
    private JSONArray subtitles = null;
    private long mediaDuration = 0L;
    private MuxStatsExoPlayer muxStats;



    public PopsicalPlayerLayout(Context context) {
        super(context);
    }

    public PopsicalPlayerLayout(@NonNull Context context,
                                BinaryMessenger messenger,
                                int id,
                                Object arguments) {

        super(context);

        this.context = context;

        this.messenger = messenger;

        this.viewId = id;

        try {

            HashMap<String, Object> args = (HashMap<String, Object>) arguments;
            this.url = (String) args.get("url");
            this.title = (String) args.get("title");
            this.subtitle = (String) args.get("subtitle");
            this.position = Double.valueOf((Double) args.get("position")).intValue();
            this.autoPlay = (boolean) args.get("autoPlay");
            this.loop = (boolean) args.get("loop");
            this.speed = Float.parseFloat( (String) args.get("speed"));
            this.pitch = Float.parseFloat( (String) args.get("pitch"));
            //use for changing the vocal, ie or en
            this.preferredAudioLanguage = (String) args.get("code");
            this.userId = (String) args.get("userId");
            this.trackId = (String) args.get("trackId");
            this.videoId = (String) args.get("videoId");
            this.experimentName = (String) args.get("experimentName");
            this.propertyKey = (String) args.get("propertyKey");
            this.isMuxStatsEnabled = (boolean) args.get("enableMuxStats");

            try {
                this.subtitles = (JSONArray) args.get("subtitles");
            } catch (Exception e) {/* ignore */}

            initPlayer();

        } catch (Exception e) {   Log.d(TAG, "initPlayer error: " + e.toString()); }

        /* release previous instance */
        if (activePlayer != null) {
            activePlayer.release();
        }
        activePlayer = mPlayerView;

        try {
            if (isMuxStatsEnabled) {
                setupMuxStats();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
    }

    @Override
    public void onCancel(Object o) {
        this.eventSink = null;
    }

    private void initPlayer() {
        trackSelector = new DefaultTrackSelector(context);

        trackSelector.setParameters(
                trackSelector.buildUponParameters()
                        .setPreferredAudioLanguage(this.preferredAudioLanguage)
        );

        mPlayerView = new SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
        mPlayerView.setRepeatMode(loop ? Player.REPEAT_MODE_ONE : REPEAT_MODE_OFF);
        setVolume(1.0f);
        setPlaybackSpeed(this.speed);
        setPitch(this.pitch);
        mPlayerView.addAnalyticsListener(new PlayerAnalyticsEventsListener());
        mPlayerView.setPlayWhenReady(this.autoPlay);
        if (this.position >= 0) {

            mPlayerView.seekTo(this.position * 1000);
        }

        setUseController(false);
        listenForPlayerTimeChange();
        listenForPlayerTimeChangeMilli();
        this.setPlayer(mPlayerView);
        new EventChannel(
                messenger,
                "tv.khub/NativeVideoPlayerEventChannel_" + this.viewId,
                JSONMethodCodec.INSTANCE).setStreamHandler(this);
        updateMediaSource();
        setupMediaSession();
    }

    private void setupMediaSession() {

        ComponentName receiver = new ComponentName(context.getPackageName(),
                RemoteReceiver.class.getName());

        /* Create a new MediaSession */
        mMediaSessionCompat = new MediaSessionCompat(context,
                PopsicalPlayerLayout.class.getSimpleName(), receiver, null);

        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
                | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

        mMediaSessionCompat.setCallback(new MediaSessionCallback());

        mMediaSessionCompat.setActive(true);

        setAudioMetadata();

        updatePlaybackState(PlayerState.PLAYING);
    }

    private void setAudioMetadata() {

        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, subtitle)
                .build();

        mMediaSessionCompat.setMetadata(metadata);
    }

    private void setupMuxStats() {
        releaseMuxStats();
        muxStats = new MuxStatsExoPlayer(
                context,
                activePlayer,
                activePlayer.getClass().getSimpleName(),
                getCustomerPlayerData(),
                getCustomerVideoData());
        muxStats.setPlayerView(this);
        muxStats.setScreenSize(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
    }

    private void releaseMuxStats() {
        if (muxStats != null) {
            muxStats.release();
            muxStats = null;
        }
    }

    private CustomerPlayerData getCustomerPlayerData() {
        CustomerPlayerData playerData = new CustomerPlayerData();
        playerData.setViewerUserId(userId);
        playerData.setExperimentName(experimentName);
        playerData.setPropertyKey(propertyKey);
        return playerData;
    }

    private CustomerVideoData getCustomerVideoData() {
        CustomerVideoData videoData = new CustomerVideoData();
        videoData.setVideoTitle(title);
        videoData.setVideoDuration(mPlayerView.getDuration());
        videoData.setVideoId(videoId);
        videoData.setVideoLanguageCode(preferredAudioLanguage);
        videoData.setVideoVariantName("track_id=" + trackId + ";video_id=" + videoId);
        videoData.setVideoCdn(Uri.parse(url).getHost());

        return videoData;
    }

    private PlaybackStateCompat.Builder getPlaybackStateBuilder() {

        PlaybackStateCompat playbackState = mMediaSessionCompat.getController().getPlaybackState();

        return playbackState == null
                ? new PlaybackStateCompat.Builder()
                : new PlaybackStateCompat.Builder(playbackState);
    }

    private void updatePlaybackState(PlayerState playerState) {

        if (mMediaSessionCompat == null) return;

        PlaybackStateCompat.Builder newPlaybackState = getPlaybackStateBuilder();

        long capabilities = getCapabilities(playerState);

        newPlaybackState.setActions(capabilities);

        int playbackStateCompat = PlaybackStateCompat.STATE_NONE;
        Log.d(TAG, "updatePlaybackState playerState: " + playerState);
        switch (playerState) {
            case PLAYING:
                playbackStateCompat = PlaybackStateCompat.STATE_PLAYING;
                break;
            case PAUSED:
                playbackStateCompat = PlaybackStateCompat.STATE_PAUSED;
                break;
            case BUFFERING:
                playbackStateCompat = PlaybackStateCompat.STATE_BUFFERING;

                break;
            case IDLE:
                playbackStateCompat = PlaybackStateCompat.STATE_STOPPED;
                break;
        }
        newPlaybackState.setState(playbackStateCompat, (long) mPlayerView.getCurrentPosition(), PLAYBACK_RATE);

        mMediaSessionCompat.setPlaybackState(newPlaybackState.build());

    }

    private @PlaybackStateCompat.Actions
    long getCapabilities(PlayerState playerState) {
        long capabilities = 0;

        switch (playerState) {
            case PLAYING:
            case BUFFERING:
                capabilities |= PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_STOP;
                break;
            case PAUSED:
                capabilities |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_STOP;
                break;
            case IDLE:
                capabilities |= PlaybackStateCompat.ACTION_PLAY;
                break;
        }

        return capabilities;
    }


    void playPause() {
        if (mPlayerView != null && !mPlayerView.isPlaying()) {
            mPlayerView.setPlayWhenReady(true);
        } else if (mPlayerView != null && mPlayerView.isPlaying()) {
            mPlayerView.setPlayWhenReady(false);
        }
    }

    void sendBufferingUpdate() {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("name", "onBufferingUpdate");

            List<? extends Number> range = Arrays.asList(0, mPlayerView.getBufferedPosition());
            event.put("buffering", Collections.singletonList(range));
            if (eventSink != null){
                eventSink.success(event);
            }


        } catch (Exception e) {
            Log.e(TAG, "onBufferingUpdate error: " + e, e);
        }
    }

    /* onTime listener in seconds */
    private void listenForPlayerTimeChange() {

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {

                    if (mPlayerView.isPlaying()) {
                        sendBufferingUpdate();
                        JSONObject message = new JSONObject();
                        message.put("name", "onTime");
                        message.put("time", mPlayerView.getCurrentPosition() / 1000);

                        if (eventSink != null){
                            eventSink.success(message);
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onTime: ", e);
                }

                onDuration();

                if (isBound) {

                    /* keep running if player view is still active */
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(runnable);
    }

    /* onTime listener */
    private void listenForPlayerTimeChangeMilli() {

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {

                    if (mPlayerView.isPlaying()) {
                        sendBufferingUpdate();
                        JSONObject message = new JSONObject();
                        message.put("name", "onTimeMilli");
                        message.put("time", mPlayerView.getCurrentPosition());
                        if (eventSink != null){
                            eventSink.success(message);
                        }

                    }

                } catch (Exception e) {
                    Log.e(TAG, "onTimeMilli: ", e);
                }

                onDuration();

                if (isBound) {

                    /* keep running if player view is still active */
                    handler.postDelayed(this, 10);
                }
            }
        };

        handler.post(runnable);
    }

    private void updateMediaSource() {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "flutter_playout"));
        mPlayerView.prepare(withSubtitles(dataSourceFactory, updateMediaSource(dataSourceFactory)));
    }

    private MediaSource updateMediaSource(DataSource.Factory dataSourceFactory) {
        int type = Util.inferContentType(Uri.parse(this.url).getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(this.url));
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(this.url));
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(this.url));
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(this.url));
            default: {
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(this.url));
            }
        }
    }

    /**
     * Adds subtitles to the media source (if provided).
     *
     * @param source
     * @return MediaSource with subtitles source included
     */
    private MediaSource withSubtitles(DataSource.Factory dataSourceFactory, MediaSource source) {

        if (this.subtitles != null && this.subtitles.length() > 0) {

            for (int i = 0; i < this.subtitles.length(); i++) {

                try {

                    JSONObject subtitle = this.subtitles.getJSONObject(i);

                    Format subtitleFormat =
                            Format.createTextSampleFormat(
                                    /* id= */ null,
                                    subtitle.getString("mimeType"),
                                    C.SELECTION_FLAG_DEFAULT,
                                    subtitle.getString("languageCode"));

                    MediaSource subtitleMediaSource =
                            new SingleSampleMediaSource.Factory(dataSourceFactory)
                                    .createMediaSource(Uri.parse(subtitle.getString("uri")),
                                            subtitleFormat, C.TIME_UNSET);

                    source = new MergingMediaSource(source, subtitleMediaSource);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return source;
    }

    public void onMediaChanged(Object arguments) {

        try {

            java.util.HashMap<String, String> args = (java.util.HashMap<String, String>) arguments;

            this.url = args.get("url");
            this.title = args.get("title");
            this.subtitle = args.get("description");
            this.userId = args.get("userId");
            this.trackId = args.get("trackId");
            this.videoId = args.get("videoId");
            this.experimentName = args.get("experimentName");
            this.propertyKey = args.get("propertyKey");

            updateMediaSource();
            if (mPlayerView != null && !mPlayerView.isPlaying()) {
                mPlayerView.setPlayWhenReady(true);
            }

            if (isMuxStatsEnabled) {
                setupMuxStats();
            }

        } catch (Exception e) { /* ignore */ }
    }


    public void setLooping(Object arguments) {

        try {

            if (arguments instanceof HashMap) {

                HashMap<String, Object> args = (HashMap<String, Object>) arguments;

                loop = Boolean.parseBoolean(args.get("loop").toString());

                mPlayerView.setRepeatMode(loop ? Player.REPEAT_MODE_ONE : REPEAT_MODE_OFF);
            }

        } catch (Exception e) { /* ignore */ }
    }


    public void seekTo(Object arguments) {
        try {

            java.util.HashMap<String, Double> args = (java.util.HashMap<String, Double>) arguments;

            Double pos = args.get("position");

            if (pos >= 0) {

                this.position = pos.intValue();

                if (mPlayerView != null) {

                    mPlayerView.seekTo(this.position * 1000);
                }
            }

        } catch (Exception e) { /* ignore */ }
    }


    void setVolume(float value) {
        if (value >= 0) {
            float bracketedValue = (float) Math.max(0.0, Math.min(1.0, value));
            mPlayerView.setVolume(bracketedValue);
        }


    }


    void setPitch(float value) {
        if (value >= 0) {
            this.pitch = value;
            final PlaybackParameters playbackParameters = new PlaybackParameters(mPlayerView.getPlaybackParameters().speed, ((float) value));
            mPlayerView.setPlaybackParameters(playbackParameters);
        }
    }

    double getPitch() {
        final float pitch = mPlayerView.getPlaybackParameters().pitch;
        this.pitch = pitch;
        return this.pitch;
    }


    void setPlaybackSpeed(float value) {
        if (value >= 0) {
            this.speed = value;
            final PlaybackParameters playbackParameters = new PlaybackParameters(((float) value), mPlayerView.getPlaybackParameters().pitch);
            mPlayerView.setPlaybackParameters(playbackParameters);
        }

    }

    double getPlaybackSpeed() {
        final float speed = mPlayerView.getPlaybackParameters().speed;
        this.speed = speed;
        return this.speed;
    }

    /**
     * set audio language for player - language must be one of available in HLS manifest
     * currently playing
     *
     * @param arguments
     */
    public void setPreferredAudioLanguage(Object arguments) {
        try {

            java.util.HashMap<String, String> args = (java.util.HashMap<String, String>) arguments;

            String languageCode = args.get("code");

            this.preferredAudioLanguage = languageCode;
            if (mPlayerView != null && trackSelector != null) {

                trackSelector.setParameters(
                        trackSelector.buildUponParameters()
                                .setPreferredAudioLanguage(languageCode));
            }

        } catch (Exception e) {
            Log.e(TAG, "setPreferredAudioLanguage: " + e.getMessage(), e);
        }
    }

    void onDuration() {

        try {

            long newDuration = mPlayerView.getDuration();

            if (newDuration != mediaDuration && eventSink != null) {

                mediaDuration = newDuration;

                JSONObject message = new JSONObject();

                message.put("name", "onDuration");

                message.put("duration", mediaDuration);

                Log.d(TAG, "onDuration: [duration=" + mediaDuration + "]");
                if (eventSink != null){
                    eventSink.success(message);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "onDuration: " + e.getMessage(), e);
        }
    }

    @Override
    public void onDestroy() {
        try {
            isBound = false;

            mPlayerView.stop(true);

            mPlayerView.release();

            activePlayer = null;

            releaseMuxStats();

        } catch (Exception e) { /* ignore */ }
    }

    /**
     * A {@link android.support.v4.media.session.MediaSessionCompat.Callback} implementation for MediaPlayer.
     */
    private final class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPause() {
            mPlayerView.setPlayWhenReady(false);
        }

        @Override
        public void onPlay() {
            mPlayerView.setPlayWhenReady(true);
        }

        @Override
        public void onSeekTo(long pos) {
            mPlayerView.seekTo(pos);
        }

        @Override
        public void onStop() {
            mPlayerView.setPlayWhenReady(false);
        }
    }

    /**
     * Player events listener for analytics
     */
    class PlayerAnalyticsEventsListener implements AnalyticsListener {

        /* used with onSeek callback to Flutter code */
        long beforeSeek = 0;

        @Override
        public void onSeekProcessed(EventTime eventTime) {

            try {

                JSONObject message = new JSONObject();

                message.put("name", "onSeek");

                message.put("position", beforeSeek);

                message.put("offset", eventTime.currentPlaybackPositionMs / 1000);

                Log.d(TAG, "onSeek: [position=" + beforeSeek + "] [offset=" +
                        eventTime.currentPlaybackPositionMs / 1000 + "]");
                if (eventSink != null){
                    eventSink.success(message);
                }

            } catch (Exception e) {
                Log.e(TAG, "onSeek: ", e);
            }
        }

        @Override
        public void onSeekStarted(EventTime eventTime) {

            beforeSeek = eventTime.currentPlaybackPositionMs / 1000;
        }

        @Override
        public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {

            try {

                final String errorMessage = "ExoPlaybackException Type [" + error.type + "] " +
                        error.getSourceException().getCause().getMessage();

                JSONObject message = new JSONObject();

                message.put("name", "onError");

                message.put("error", errorMessage);

                Log.d(TAG, "onError: [errorMessage=" + errorMessage + "]");
                if (eventSink != null){
                    eventSink.success(message);
                }

                if (muxStats != null && isMuxStatsEnabled) {
                    MuxErrorException muxErrorException = new MuxErrorException(error.type,
                            error.getSourceException().getCause().getMessage());
                    muxStats.error(muxErrorException);
                }

            } catch (Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        }


        @Override
        public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, @Player.State int playbackState) {
            Log.d(TAG, "onPlayerStateChanged playbackState: " + playbackState);
            if (playbackState == Player.STATE_BUFFERING) {

                sendBufferingUpdate();
            } else if (playbackState == Player.STATE_READY) {

                if (playWhenReady) {

                    try {

                        updatePlaybackState(PlayerState.PLAYING);

                        JSONObject message = new JSONObject();

                        message.put("name", "onPlay");

                        Log.d(TAG, "onPlay: []");
                        if (eventSink != null){
                            eventSink.success(message);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "onPlay: ", e);
                    }

                } else {

                    try {

                        updatePlaybackState(PlayerState.PAUSED);

                        JSONObject message = new JSONObject();

                        message.put("name", "onPause");

                        Log.d(TAG, "onPause: []");
                        if (eventSink != null){
                            eventSink.success(message);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "onPause: ", e);
                    }

                }

                onDuration();

            } else if (playbackState == Player.STATE_ENDED) {

                try {

                    updatePlaybackState(PlayerState.COMPLETE);

                    JSONObject message = new JSONObject();

                    message.put("name", "onComplete");

                    Log.d(TAG, "onComplete: []");
                    if (eventSink != null){
                        eventSink.success(message);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onComplete: " + e.toString(), e);
                }

            }
        }
    }
}
