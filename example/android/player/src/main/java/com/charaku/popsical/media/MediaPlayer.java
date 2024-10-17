//package com.easternblu.khub.media;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Point;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.SystemClock;
//import androidx.annotation.MainThread;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.annotation.StringRes;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.util.Pair;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import com.easternblu.khub.media.exoplayer2.DefaultExoPlayerTrackSelector;
//import com.easternblu.khub.media.exoplayer2.EventLogger;
//import com.easternblu.khub.media.exoplayer2.MediaBandWidthMeterImpl;
//import com.easternblu.khub.media.exoplayer2.MediaBandwidthMeter;
//import com.easternblu.khub.media.exoplayer2.MediaContentSelector;
//import com.easternblu.khub.media.exoplayer2.MediaPlayerCore;
//import com.easternblu.khub.media.exoplayer2.MediaPlayerException;
//import com.easternblu.khub.media.exoplayer2.PlayerDebugHelper;
//import com.easternblu.khub.media.model.ChannelMapping;
//import com.easternblu.khub.media.model.MediaActivityListener;
//import com.easternblu.khub.media.model.MediaContent;
//import com.easternblu.khub.media.model.PlaybackErrorType;
//import com.easternblu.khub.media.model.ResumeInfo;
//import com.easternblu.khub.media.model.SetupExtraParam;
//import com.easternblu.khub.media.model.StereoVolume;
//import com.easternblu.khub.media.model.TrackFormat;
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.Format;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.Timeline;
//import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
//import com.google.android.exoplayer2.drm.DrmSessionManager;
//import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
//import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
//import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
//import com.google.android.exoplayer2.drm.UnsupportedDrmException;
//import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
//import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
//import com.google.android.exoplayer2.source.BehindLiveWindowException;
//import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.MediaSourceEventListener;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.source.dash.DashMediaSource;
//import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
//import com.google.android.exoplayer2.source.hls.HlsMediaSource;
//import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
//import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelection;
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
//import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
//import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
//import com.google.android.exoplayer2.upstream.HttpDataSource;
//import com.google.android.exoplayer2.util.Util;
//import com.mux.stats.sdk.core.model.CustomerPlayerData;
//import com.mux.stats.sdk.core.model.CustomerVideoData;
//import com.mux.stats.sdk.muxstats.MuxErrorException;
//import com.mux.stats.sdk.muxstats.MuxStatsExoPlayer;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.CookieHandler;
//import java.net.CookieManager;
//import java.net.CookiePolicy;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Cache;
//import okhttp3.ConnectionPool;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Protocol;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.logging.HttpLoggingInterceptor;
//import timber.log.Timber;
//
//import static com.easternblu.khub.media.model.PlaybackErrorType.EXOPLAYER_SOURCE_ERROR;
//import static com.easternblu.khub.media.model.PlaybackErrorType.EXOPLAYER_SOURCE_WITH_CONNECTION_ERROR;
//import static com.easternblu.khub.media.model.PlaybackErrorType.EXOPLAYER_SOURCE_WITH_INVALID_CODE_ERROR;
//import static com.easternblu.khub.media.model.PlaybackErrorType.EXOPLAYER_UNEXPECTED_ERROR;
//
///**
// * A wrapper that encapsulate all the boilerplate code that is needed to "init" an exoplayer
// */
//public class MediaPlayer extends FrameLayout implements
//        MediaPlayerControl,   // mainly to tell others that this allows player control
//        Player.EventListener // mainly for exoplayer
//{
//    public final String TAG = this.getClass().getSimpleName();
//
//    private static boolean ALLOW_LOCAL_CACHING = false;
//    private static boolean DEBUG_NETWORK_VS_CACHE = false;
//    private static boolean CUSTOM_REQUEST_HEADERS = true;
//
//
//    protected final int END_SECTION_OFFSET = 5000;
//
//
//    // why are these static???
//    private static final CookieManager DEFAULT_COOKIE_MANAGER;
//
//
//    static {
//        DEFAULT_COOKIE_MANAGER = new CookieManager();
//        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//
//    }
//
//
//    private Handler mainHandler;
//    private volatile EventLogger eventLogger;
//    private SimpleExoPlayerView simpleExoPlayerView;
//
//    private MediaBandWidthMeterImpl mediaBandwidthMeter;
//
//    private volatile DataSource.Factory mediaDataSourceFactory;
//    private volatile MediaPlayerCore player;
//
//    private volatile DefaultExoPlayerTrackSelector trackSelector;
//    private volatile MediaContentSelector mediaContentSelector;
//    private volatile PlayerDebugHelper debugViewHelper;
//    protected volatile int lastProgressUpdate;
//
//    private boolean needRetrySource;
//    private boolean shouldAutoPlay = true;
////    protected int resumeWindow;
////    protected long resumePosition;
////    protected ChannelMapping resumeChannelMapping = null;
//
//    protected final ResumeInfo resumeInfo = new ResumeInfo();
//    private MuxStatsExoPlayer muxStats;
//
//    private volatile boolean onPlayBegan = true, onPlayEnded = true;
//
//    // will be non-null after setup
//    private SetupExtraParam param;
//
//    // will be non-null after setup
//    private MediaPlayerDelegate delegate;
//
//    private final MediaProgressTimer progressTimer = new MediaProgressTimer();
//    /**
//     * A listener to aggregate all the PlayerEventListeners.
//     * <p>
//     * All events are triggered by onPlayerStateChanged
//     */
//    private final MediaPlayerListeners listeners = new MediaPlayerListeners(this) {
//        @Override
//        public synchronized void onMediaStart(boolean restart) {
//            MediaPlayerCore player = MediaPlayer.this.player;
//            ChannelMapping cm;
//            if (resumeInfo != null && (cm = resumeInfo.getChannelMapping()) != null) {
//                if (player != null) {
//                    player.setChannelMapping(cm);
//                }
//            }
//            super.onMediaStart(restart);
//        }
//    };
//
//    @Nullable
//    public MediaPlayerListeners.State getCurrentState() {
//        return listeners.getCurrentState();
//    }
//
//    /**
//     * A activity listener that pause the player when activity is paused
//     */
//    private final MediaActivityListener activityListener = new MediaActivityListener() {
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//
//        }
//
//        @Override
//        public void onDestroy() {
//            releasePlayer();
//        }
//
//        @Override
//        public void onStart() {
//            if (Util.SDK_INT > 23) {
//                preparePlayer();
//            }
//        }
//
//        @Override
//        public void onStop() {
//            if (Util.SDK_INT > 23) {
//                releasePlayer();
//            }
//        }
//
//        @Override
//        public void onResume() {
//            if ((Util.SDK_INT <= 23 || player == null)) {
//                preparePlayer();
//            }
//        }
//
//        @Override
//        public void onPause() {
//            if (Util.SDK_INT <= 23) {
//                releasePlayer();
//            }
//        }
//
//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        }
//
//        @Override
//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        }
//
//    };
//
//    public MediaPlayer(Context context) {
//        super(context);
//        initViews();
//    }
//
//    public MediaPlayer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initViews();
//    }
//
//    public MediaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initViews();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public MediaPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initViews();
//    }
//
//    protected void initViews() {
//        if (isInEditMode()) {
//            return;
//        }
//        // videoSurface = (ViewGroup) this.findViewById(R.id.video_surface);
//        DefaultBandwidthMeter temp = new DefaultBandwidthMeter();
//        mediaBandwidthMeter = new MediaBandWidthMeterImpl(temp, temp, 1d);
//        mediaBandwidthMeter.setListener(listeners);
//    }
//
//
//    @Override
//    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
//        View v;
//        if ((v = simpleExoPlayerView) != null) {
//            v.setVisibility(visibility);
//        }
//    }
//
//    public void setShouldAutoPlay(boolean shouldAutoPlay) {
//        this.shouldAutoPlay = shouldAutoPlay;
//    }
//
//
//    /**
//     * Must be called after onCreate
//     */
//    public void setup(MediaPlayerDelegate delegate, @NonNull SetupExtraParam param) {
//        if (this.param != null) {
//            throw new IllegalArgumentException("Can only call setup once");
//        }
//
//        if (delegate == null) {
//            throw new IllegalArgumentException("delegate required for setup");
//        }
//
//        this.param = param;
//        this.delegate = delegate;
//
//        MediaBandWidthMeterImpl.LOG_NETWORK_TRANSFER = isDebugMode();
//
//
//        delegate.getMediaActivitySupport().addMediaActivityListener(activityListener);
//
//        mediaDataSourceFactory = buildDataSourceFactory(true);
//        mainHandler = new Handler();
//        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
//            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
//        }
//
//        if (isFinishing()) {
//            return;
//        }
//
//        simpleExoPlayerView = (SimpleExoPlayerView) getLayoutInflater().inflate(R.layout.layout_simple_exoplayer_view, null, false);
//        simpleExoPlayerView.hideController();
//        simpleExoPlayerView.setUseController(false);
//        simpleExoPlayerView.requestFocus();
//
//
//        setAsVideoView(simpleExoPlayerView);
//    }
//
//
//    public DefaultExoPlayerTrackSelector getTrackSelector() {
//        return trackSelector;
//    }
//
//    /**
//     * get current mono mode
//     *
//     * @return
//     */
//    @Override
//    public ChannelMapping getCurrentChannelMapping() {
//        MediaPlayerCore player = this.player;
//        if (player != null)
//            return player.getChannelMapping();
//        else
//            return null;
//    }
//
//    @Override
//    public void setCurrentChannelMapping(ChannelMapping channelMapping, boolean userExplicitAction, @Nullable Boolean channelMappingIsVocal) {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            player.setChannelMapping(channelMapping);
//        }
//    }
//
//    /**
//     * true if player is not null
//     *
//     * @return
//     */
//    protected boolean hasPlayer() {
//        return player != null;
//    }
//
//
//    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
//        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
//                : Util.inferContentType("." + overrideExtension);
//        switch (type) {
//            case C.TYPE_SS:
//                return new SsMediaSource(uri,
//                        buildDataSourceFactory(false),
//                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
//                        mainHandler,
//                        eventLogger);
//            case C.TYPE_DASH:
//                return new DashMediaSource(uri,
//                        buildDataSourceFactory(false),
//                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
//                        mainHandler,
//                        eventLogger);
//            case C.TYPE_HLS:
//                return new HlsMediaSource(uri,
//                        mediaDataSourceFactory,
//                        mainHandler,
//                        eventLogger);
//            case C.TYPE_OTHER:
//                return new ExtractorMediaSource(uri,
//                        mediaDataSourceFactory,
//                        new DefaultExtractorsFactory(),
//                        mainHandler,
//                        eventLogger);
//            default: {
//                throw new IllegalStateException("Unsupported type: " + type);
//            }
//        }
//    }
//
//
//    /**
//     * Exoplayer related method for DRM
//     *
//     * @param uuid
//     * @param licenseUrl
//     * @param keyRequestPropertiesArray
//     * @return
//     * @throws UnsupportedDrmException
//     */
//    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid,
//                                                                           String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
//        if (Util.SDK_INT < 18) {
//            return null;
//        }
//        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
//                buildHttpDataSourceFactory(false));
//        if (keyRequestPropertiesArray != null) {
//            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
//                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
//                        keyRequestPropertiesArray[i + 1]);
//            }
//        }
//        return new DefaultDrmSessionManager<>(uuid,
//                FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
//    }
//
//
//    /**
//     * Returns a new HttpDataSource factory.
//     *
//     * @param useBandwidthMeter Whether to set {@link #mediaBandwidthMeter} as a listener to the new
//     *                          DataSource factory.
//     * @return A new HttpDataSource factory.
//     */
//    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
//        return buildHttpDataSourceFactory(useBandwidthMeter ? mediaBandwidthMeter : null);
//    }
//
//
//    /**
//     * Show a debug dialog, allowing you to select renderer(text)
//     *
//     * @param view
//     */
//    public void showVideoPopup(View view) {
//        if (trackSelector != null) {
//            trackSelector.showVideoPopup(getActivity(), view);
//        }
//    }
//
//
//    /**
//     * Show a debug dialog, allowing you to select renderer(text)
//     *
//     * @param view
//     */
//    public void showAudioPopup(View view) {
//        if (trackSelector != null) {
//            trackSelector.showAudioPopup(getActivity(), view);
//        }
//    }
//
//
//    /**
//     * Show a debug dialog, allowing you to select renderer(text)
//     *
//     * @param view
//     */
//    public void showTextPopup(View view) {
//        if (trackSelector != null) {
//            trackSelector.showTextPopup(getActivity(), view);
//        }
//    }
//
////    /**
////     * @return true, if the loaded video has an vocal track
////     * @throws MediaPlayerException
////     */
////    public boolean hasVocalTrack() throws MediaPlayerException {
////        if (trackSelector != null) {
////            return trackSelector.hasVocalTrack();
////        } else {
////            throw new MediaPlayerException("Track selector not ready");
////        }
////    }
//
//
////    /**
////     * select an audio track...
////     *
////     * @param vocalOn
////     * @throws MediaPlayerException
////     */
////    public void setVocalTrack(final boolean vocalOn, final boolean userAction) throws MediaPlayerException {
////        if (trackSelector != null) {
////            trackSelector.setVocalTrack(getActivity(), vocalOn, userAction);
////        }
////    }
//
//
//
////
////    /**
////     * Toggle video rendering on off
////     *
////     * @param enable
////     */
////    public void setVideoEnable(boolean enable) {
////        if (trackSelector != null) {
////            trackSelector.setVideoEnable(getActivity(), enable);
////        }
////    }
//
//
////    /**
////     * Toggle audio rendering on off
////     *
////     * @param enable
////     */
////    public void setAudioEnable(boolean enable) {
////        player.setVolume(enable ? 1f : 0f);
////    }
//
//
//    /**
//     * Create the player if necessary
//     */
//    @MainThread
//    protected void preparePlayer() {
//        if (isFinishing()) {
//            return;
//        }
//        Timber.i("preparePlayer");
//        SetupExtraParam param = getParam();
//        boolean needNewPlayer = player == null;
//        if (needNewPlayer) {
//            boolean preferExtensionDecoders = param.isPreferExtensionDecoders();
//            UUID drmSchemeUuid = param.getDrmSchemeUuid();
//            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
//
//            if (drmSchemeUuid != null) {
//                String drmLicenseUrl = param.getDrmLicenseUrl();
//                String[] keyRequestPropertiesArray = param.getDrmKeyRequestProperties();
//                try {
//
//                    drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl,
//                            keyRequestPropertiesArray);
//                } catch (UnsupportedDrmException e) {
//
//                    int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported
//                            : (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
//                            ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown);
//                    showToast(getString(errorStringId));
//                    return;
//                }
//            }
//
//            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
//                    useExtensionRenderers()
//                            ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
//                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
//                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
//            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getContext(),
//                    drmSessionManager, extensionRendererMode);
//
//
//            TrackSelection.Factory videoTrackSelectionFactory =
//                    new AdaptiveTrackSelection.Factory(mediaBandwidthMeter);
//
//            DefaultTrackSelector temp = new DefaultTrackSelector(videoTrackSelectionFactory);
//            listeners.resetLastPlayWhenReady();
//            listeners.resetLastPlayerState();
//            player = new MediaPlayerCore(getContext(), renderersFactory, temp);
//
//            trackSelector = new DefaultExoPlayerTrackSelector(getContext(), temp, player) {
//                @Override
//                public void showSingleToast(String text) {
//                    showToast(text);
//                }
//            };
//            mediaContentSelector = new MediaContentSelector(getContext(), temp);
//
//
//            player.addListener(this);
//
//            listeners.onPlayerCreated();
//            eventLogger = new EventLogger(trackSelector.getMappingTrackSelector()) {
//                @Override
//                public void onAudioInputFormatChanged(Format format) {
//                    super.onAudioInputFormatChanged(format);
//                    if (format != null && format.sampleRate > 48000) {
//                        reportInvalidAudioSampleRateError(getCurrentMediaContent(), format);
//                    }
//                }
//            };
//
//            player.addListener(eventLogger);
//            player.setAudioDebugListener(eventLogger);
//            player.setVideoDebugListener(eventLogger);
//            player.setMetadataOutput(eventLogger);
//            simpleExoPlayerView.setPlayer(player);
//            player.setPlayWhenReady(shouldAutoPlay);
//
//            TextView textview = getDebugVideoStatusTextView();
//            if (textview != null) {
//                debugViewHelper = new PlayerDebugHelper(player, mediaBandwidthMeter, textview);
//                debugViewHelper.start();
//            }
//
//        }
//
//        // Log.printInvokerStack(Log.ERROR);
//        //  Timber.e("needRetrySource = " + needRetrySource + ", needNewPlayer = " + needNewPlayer);
//        if (needNewPlayer || needRetrySource) {
//            if (isMuxStatsEnabled()) {
//                setupMuxStats();
//            }
//
//            Uri[] uris;
//            String[] extensions;
//            Uri uri;
//            MediaContent current = getCurrentMediaContent();
//            try {
//                if (current != null
//                        && (uri = current.getUri()) != null) {
//                    Timber.w("preparePlayer: getContentUri() = " + "[" + uri.toString() + "]");
//                    uris = new Uri[]{uri};
//                    extensions = new String[]{null};
//
//                } else {
//                    listeners.onMediaContentEmpty();
//                    return;
//                }
//            } catch (Throwable t) {
//                listeners.onMediaPlayerError(new IllegalArgumentException("Invalid URL"), PlaybackErrorType.EXOPLAYER_SOURCE_ERROR);
//                return;
//            }
//            if (Util.maybeRequestReadExternalStoragePermission(getActivity(), uris)) {
//
//                // The player will be reinitialized if the permission is granted.
//                return;
//            }
//
//            MediaSource[] mediaSources = new MediaSource[uris.length];
//            for (int i = 0; i < uris.length; i++) {
//                mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
//            }
//
//
//            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
//                    : new ConcatenatingMediaSource(mediaSources);
//
//            MediaSourceEventListener sourceListener = delegate.getMediaSourceEventListener();
//            if (sourceListener != null) {
//                mediaSource.addEventListener(mainHandler, sourceListener);
//            }
//
//            player.prepare(mediaSource, true, false);
//            needRetrySource = false;
//
//            Timber.d("canResume = " + resumeInfo.canResume() + " des=" + resumeInfo.description() + " ");
//
//            if (resumeInfo.canResume()) {
//                seekTo(new Long(resumeInfo.getResumePosition()).intValue());
//            }
//
//            // updateButtonVisibilities();
//        }
//    }
//
//
//    public MediaContentSelector getMediaContentSelector() {
//        return mediaContentSelector;
//    }
//
//    /**
//     * Last position that was reported
//     *
//     * @return
//     */
//    public int getLastProgressUpdate() {
//        return lastProgressUpdate;
//    }
//
//    private void reportInvalidAudioSampleRateError(MediaContent mediaContent, Format format) {
//        onMediaPlayerError(
//                new Exception("Invalid audio sample rate"),
//                PlaybackErrorType.INVALD_AUDIO_SAMPLE_RATE,
//                new Pair<>("sample_rate", format.sampleRate),
//                new Pair<>("track_id", mediaContent == null ? "" : mediaContent.getId()),
//                new Pair<>("track_title", mediaContent == null ? "" : mediaContent.getTitle()));
//
//    }
//
//
//    /**
//     * Release the exoplayer's resources
//     */
//    @MainThread
//    @Nullable
//    protected void releasePlayer() {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            lastProgressUpdate = 0;
//            Timber.i("releasePlayer");
//            listeners.onPlayerDestroyed();
//            if (debugViewHelper != null) {
//                debugViewHelper.stop();
//                debugViewHelper = null;
//            }
//            shouldAutoPlay = player.getPlayWhenReady();
//            player.release();
//            releaseMuxStats();
//            if (trackSelector != null) {
//                trackSelector.onReleasePlayer();
//            }
//            trackSelector = null;
//            eventLogger = null;
//            mediaContentSelector = null;
//            this.player = null;
//        }
//    }
//
//
//    /**
//     * Attempt to report the ExoPlaybackException to analytic.
//     * <p>
//     * Will also let the player object to try and handle the error. If it is successfully handle, then it won't report the error.
//     * <p>
//     * Otherwise it will report the error and display it on UI
//     *
//     * @param e
//     */
//    @Override
//    public void onPlayerError(ExoPlaybackException e) {
//        Timber.w("onPlayerError: e = " + e);
//
//        String title = "", id = "", tag = "", uri = "";
//        MediaContent errorContent = getCurrentMediaContent();
//        if (errorContent != null) {
//            title = errorContent.getTitle();
//            id = errorContent.getId();
//            tag = errorContent.getTag();
//            Uri temp = errorContent.getUri();
//            uri = temp != null ? temp.toString() : "";
//        }
//
//        PlaybackErrorType eventType = PlaybackErrorType.EXOPLAYER_GENERIC_ERROR;
//        List<Pair<String, String>> eventParams = new ArrayList<>();
//        eventParams.add(new Pair<>("track_id", id));
//        eventParams.add(new Pair<>("tag", tag));
//        eventParams.add(new Pair<>("uri", uri));
//
//        String errorString = null;
//        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
//            eventType = PlaybackErrorType.EXOPLAYER_RENDERER_ERROR;
//            Exception cause = e.getRendererException();
//            if (cause != null) {
//                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
//                    // Special case for decoder initialization failures.
//                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
//                            (MediaCodecRenderer.DecoderInitializationException) cause;
//                    if (decoderInitializationException.decoderName == null) {
//                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
//                            errorString = getString(R.string.error_querying_decoders);
//                        } else if (decoderInitializationException.secureDecoderRequired) {
//                            errorString = getString(R.string.error_no_secure_decoder,
//                                    decoderInitializationException.mimeType);
//                        } else {
//                            errorString = getString(R.string.error_no_decoder,
//                                    decoderInitializationException.mimeType);
//                        }
//                    } else {
//                        errorString = getString(R.string.error_instantiating_decoder,
//                                decoderInitializationException.decoderName);
//                    }
//                }
//                eventParams.add(new Pair<>("msg", errorString));
//                eventParams.add(new Pair<>("type", cause.getClass().getSimpleName()));
//            }
//
//        } else if (e.type == ExoPlaybackException.TYPE_SOURCE) {
//            eventType = EXOPLAYER_SOURCE_ERROR;
//
//            Exception cause = e.getSourceException();
//            if (cause != null) {
//                eventParams.add(new Pair<>("type", cause.getClass().getSimpleName()));
//
//                if (cause instanceof HttpDataSource.InvalidResponseCodeException) {
//                    HttpDataSource.InvalidResponseCodeException httpError = (HttpDataSource.InvalidResponseCodeException) cause;
//                    eventType = EXOPLAYER_SOURCE_WITH_INVALID_CODE_ERROR;
//
//
//                    if (httpError.dataSpec != null) {
//                        if (httpError.dataSpec.uri != null)
//                            eventParams.add(new Pair<>("uri", httpError.dataSpec.uri.toString()));
//                        eventParams.add(new Pair<>("position", String.valueOf(httpError.dataSpec.position)));
//                    }
//                    eventParams.add(new Pair<>("http_code", String.valueOf(httpError.responseCode)));
//
//
//                } else if (cause instanceof HttpDataSource.HttpDataSourceException) {
//                    HttpDataSource.HttpDataSourceException httpError = (HttpDataSource.HttpDataSourceException) cause;
//                    eventType = EXOPLAYER_SOURCE_WITH_CONNECTION_ERROR;
//                    String badUrl = null;
//                    if (httpError.dataSpec != null) {
//                        if (httpError.dataSpec.uri != null) {
//                            eventParams.add(new Pair<>("uri", badUrl = httpError.dataSpec.uri.toString()));
//                        }
//                        eventParams.add(new Pair<>("position", String.valueOf(httpError.dataSpec.position)));
//
//                    }
//                }
//            }
//        } else if (e.type == ExoPlaybackException.TYPE_UNEXPECTED) {
//            eventType = EXOPLAYER_UNEXPECTED_ERROR;
//            Exception cause = e.getUnexpectedException();
//            if (cause != null) {
//                eventParams.add(new Pair<>("type", cause.getClass().getSimpleName()));
//            }
//        }
//
//
//        Timber.w("onPlayerError: errorString = " + errorString);
//
//        if (errorString != null && isDebugMode()) {
//            showError(e);
//        }
//        needRetrySource = true;
//        // clear resume position
//        if (isBehindLiveWindow(e)) {
//            clearResumePosition();
//            preparePlayer();
//        }
//
//        // we must handle resume or show error before clearing position
//        // the exception is always going to be ExoPlayerException with null message;
//        Throwable endResult = Utils.findHighLevelReadableError(e);
//        if (player != null) {
//            Timber.w("Error not handled by player. Reporting to analytic");
//            Object[] args = eventParams.toArray(new Pair[0]);
//
//            onMediaPlayerError(endResult, eventType, args);
//            if (muxStats != null) {
//                if (isSendErrorToMux()) {
//                    Timber.w("Explicitly sending error info to MUX " + e.getMessage());
//                    int errorCode = e.type;
//
//                    List<Pair<String, String>> errorMsgs = new ArrayList<>();
//                    if (eventType != null)
//                        errorMsgs.add(new Pair<>("e_type", eventType.name()));
//                    if (e != null)
//                        errorMsgs.add(new Pair<>("e_msg", e.getMessage()));
//
//                    errorMsgs.addAll(eventParams);
//
//
//                    String errorMsg = Utils.toString(errorMsgs, ";", new Utils.Lambda<Pair<String, String>, String>() {
//                        @Override
//                        public String invoke(Pair<String, String> from) {
//                            return String.format("%1$s=%2$s", from.first, from.second);
//                        }
//                    });
//                    MuxErrorException error = new MuxErrorException(errorCode, errorMsg);
//                    muxStats.error(error);
//                }
//            }
//            showFatalPlayerError(endResult);
//            stopVideo();
//        }
//    }
//
//
//    /**
//     * true if it is behind live window??
//     *
//     * @param e
//     * @return
//     */
//    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false;
//        }
//        Throwable cause = e.getSourceException();
//        while (cause != null) {
//            if (cause instanceof BehindLiveWindowException) {
//                return true;
//            }
//            cause = cause.getCause();
//        }
//        return false;
//    }
//
//
//    protected String getString(@StringRes int stringRes, Object... args) {
//        return getContext().getString(stringRes, args);
//    }
//
//
//    @Override
//    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//        PlaybackErrorType eventType = null;
//        String message = null;
//        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//        if (mappedTrackInfo != null) {
//            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
//                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
//                eventType = PlaybackErrorType.EXOPLAYER_RENDERER_SUPPORT_UNSUPPORTED_VIDEO_TRACKS;
//                showToast(message = getString(R.string.error_unsupported_video));
//            }
//            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
//                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
//                eventType = PlaybackErrorType.EXOPLAYER_RENDERER_SUPPORT_UNSUPPORTED_AUDIO_TRACKS;
//                showToast(message = getString(R.string.error_unsupported_audio));
//            }
//        }
//
//
//        // if there is an error when changing track (basically RENDERER_SUPPORT_UNSUPPORTED_TRACKS) then
//        // we will log it with fabric
//        if (eventType != null) {
//            String trackTitle = "", trackId = "";
//            MediaContent errorContent = getCurrentMediaContent();
//            if (errorContent != null) {
//                trackTitle = errorContent.getTitle();
//                trackId = String.valueOf(errorContent.getId());
//            }
//
//            List<Pair<String, String>> eventParams = new ArrayList<>();
//            eventParams.add(new Pair<>("track_id", trackId));
//            eventParams.add(new Pair<>("track_title", trackTitle));
//            eventParams.add(new Pair<>("hardware_type", getHardwareType()));
//            Object[] args = eventParams.toArray(new Pair[0]);
//            onMediaPlayerError(new Exception(message), eventType, args);
//        }
//    }
//
//    protected void setAsVideoView(View v) {
//        removeAllViews();
//        addView(v);
//    }
//
//
//    /**
//     * Init code for MUX
//     */
//    protected void setupMuxStats() {
//        if (player == null) {
//            Timber.w("setupMuxStats: player is null");
//            return;
//        }
//
//        MediaContent current = getCurrentMediaContent();
//        if (current == null) {
//            Timber.w("setupMuxStats: getCurrentMediaContent is null");
//            return;
//        }
//
//        setupMuxStats(player, current);
//    }
//
//
//    /**
//     * Actual method to setup MUX
//     *
//     * @param player
//     * @param video
//     * @return
//     */
//    protected synchronized MuxStatsExoPlayer setupMuxStats(@NonNull SimpleExoPlayer player, @NonNull MediaContent video) {
//        releaseMuxStats();
//        Timber.d("setupMuxStats: ");
//        Point size = getScreenSizePx();
//        muxStats = new MuxStatsExoPlayer(
//                getContext(),
//                player,
//                player.getClass().getSimpleName(),
//                getCustomerPlayerData(video),
//                getCustomerVideoData(video, player.getDuration()));
//        muxStats.setPlayerView(simpleExoPlayerView);
//        muxStats.setScreenSize(size.x, size.y);
//        return muxStats;
//    }
//
//
//    /**
//     * Called during {@link #releasePlayer()}
//     */
//    protected synchronized void releaseMuxStats() {
//        if (muxStats != null) {
//            muxStats.release();
//            muxStats = null;
//        }
//    }
//
//    /**
//     * Useful if the video is not scaled correctly
//     *
//     * @param scaleMode
//     */
//    public void setVideoScaleMode(MediaConstants.VideoScaleMode scaleMode) {
//        MediaPlayerCore player = this.player;
//        if(player != null) {
//            if (scaleMode == MediaConstants.VideoScaleMode.ScaleToFitWithCroppingMode) {
//                Timber.d("setVideoScaleMode: VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING + RESIZE_MODE_FIXED_WIDTH");
//                player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
//            } else {
//                Timber.d("setVideoScaleMode: VIDEO_SCALING_MODE_DEFAULT + RESIZE_MODE_FIT");
//                player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
//                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//            }
//        }
//    }
//
//
//    /**
//     * Will update the variables related resume. It should be called regularly at progres update
//     */
//    protected void updateResumePosition() {
//        MediaContent mc = getCurrentMediaContent();
//        if (mc != null) {
//            resumeInfo.update(mc, player);
//        }
//    }
//
//
//    /**
//     * Will clear any variable related the resume
//     */
//    private void clearResumePosition() {
//        resumeInfo.clear();
//    }
//
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        stopTimer();
//        releasePlayer();
//    }
//
//
//    public void retryFromCurrentPosition() {
//        // releasePlayer will call updateResumePostion anyway
//        releasePlayer();
//        shouldAutoPlay = true;
//        preparePlayer();
//    }
//
//
//    /**
//     * {@inheritDoc}
//     *
//     * @throws MediaPlayerException
//     */
//    @MainThread
//    public void startVideo() {
//        startVideo(true);
//    }
//
//    @MainThread
//    public void startVideo(boolean autoPlay) {
//        restartVideo(autoPlay);
//    }
//
//
//    /**
//     * {@inheritDoc}
//     *
//     * @throws MediaPlayerException
//     */
//    @MainThread
//    public void restartVideo() {
//        restartVideo(shouldAutoPlay);
//    }
//
//    @MainThread
//    public void restartVideo(boolean autoPlay) {
//        // releasePlayer will call updateResumePostion anyway
//        releasePlayer();
//        onNewMedia();
//        shouldAutoPlay = autoPlay;
//
//        MediaContent mc = getCurrentMediaContent();
//        if (mc == null || !resumeInfo.isSamePlayback(mc)) {
//            clearResumePosition();
//        }
//        preparePlayer();
//        stopTimer();
//        startTimer();
//    }
//
//
//    @MainThread
//    public void stopVideo() {
//        stopVideo(true);
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @MainThread
//    public void stopVideo(Boolean clearPosition) {
//        if (clearPosition) {
//            clearResumePosition();
//        }
//        releasePlayer();
//    }
//
//
//    @Override
//    public void play() {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            player.setPlayWhenReady(true);
//            resumeTimer();
//        }
//    }
//
//
//    @Override
//    public void pause() {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            player.setPlayWhenReady(false);
//            // maybe pauseTimer()
//        }
//    }
//
//    @Override
//    public void next() {
//        listeners.next();
//    }
//
//    @Override
//    public void previous() {
//        listeners.previous();
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public int getDuration() {
//        MediaPlayerCore player = this.player;
//        return player != null ? (int) player.getDuration() : -1;
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public int getCurrentPosition() {
//        MediaPlayerCore player = this.player;
//        return player != null ? (int) player.getCurrentPosition() : -1;
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void seekTo(int pos) {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            lastProgressUpdate = new Long(pos).intValue();
//            player.seekTo(pos);
//        }
//    }
//
//    /**
//     * Skipping to the end time - offset (i.e. almost the end) Useful for debugging
//     *
//     * @param offset
//     */
//    public void skipToEnd(int offset) {
//        int duration = getDuration();
//        if (duration > 0) {
//            seekTo(duration - offset);
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void setPitch(float pitch) {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            player.setPitch(pitch);
//        }
//    }
//
//
//    /**
//     * True if it is muted (duh)
//     *
//     * @return
//     */
//    public boolean isMuted() {
//        return getMusicVolume() == 0;
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void setSpeed(float speed) {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            player.setSpeed(speed);
//        }
//    }
//
//    /**
//     * Return the playback speed
//     *
//     * @return
//     */
//    public float getSpeed() {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            return player.getSpeed();
//        } else {
//            return 1;
//        }
//    }
//
//
//    /**
//     * true if there is stereo volume
//     *
//     * @return
//     */
//    protected boolean isStereoVolumeAvailable() {
//        return player != null;
//    }
//
//
//    /**
//     * true if the player is ready to set to mono
//     *
//     * @return
//     */
//    protected boolean isMonoMusicAvailable() {
//        MediaPlayerCore player = this.player;
//        return player != null && player.isMonoAvailable();
//    }
//
//
//    /**
//     * Get stereo volume as an object
//     *
//     * @return
//     */
//    @Override
//    public StereoVolume getStereoVolume() {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            return new StereoVolume(player.getVolume(), player.getVolume());
//        } else {
//            return new StereoVolume();
//        }
//    }
//
//    @Override
//    public void setStereoVolume(StereoVolume volume) {
//        MediaPlayerCore player = this.player;
//        if (player != null) {
//            float musicVolume = player.getVolume();
//            Timber.d("setStereoVolume: musicVolume=" + musicVolume + ", L=" + volume.left + ", R=" + volume.right);
//            player.setVolume((volume.left * musicVolume + volume.right * musicVolume) / 2);
//        }
//    }
//
//
//    @Override
//    public void setMusicVolume(float musicVolume) {
//        MediaPlayerCore player = this.player;
//        boolean muted = musicVolume == 0;
//        if (isUseStreamMusicVolume()) {
//            setSystemMusicStreamVolume(musicVolume);
//        } else {
//            if (player != null) {
//                player.setVolume(musicVolume);
//            } else {
//                muted = musicVolume == 0;
//            }
//        }
//        listeners.onMusicVolumeChanged(musicVolume, muted);
//    }
//
//    private void setSystemMusicStreamVolume(float musicVolume) {
//        setSystemMusicStreamVolume((int) (getMaxSystemMusicStreamVolume() * musicVolume));
//    }
//
//    /**
//     * get the player volume
//     * {@inheritDoc}
//     *
//     * @return
//     */
//    @Override
//    public float getMusicVolume() {
//        MediaPlayerCore player = this.player;
//        if (isUseStreamMusicVolume()) {
//            return 1f * getSystemMusicStreamVolume() / getMaxSystemMusicStreamVolume();
//        } else {
//            if (player != null) {
//                return player.getVolume();
//            } else {
//                return 0;
//            }
//        }
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean isPlaying() {
//        MediaPlayerCore player = this.player;
//        return player != null && player.getPlayWhenReady() &&
//                player.getPlaybackState() == Player.STATE_READY;
//    }
//
//    @Override
//    public int getBufferPercentage() {
//        MediaPlayerCore player = this.player;
//        return player != null ? player.getBufferedPercentage() : 0;
//    }
//
//
//    /**
//     * this should be called by the player internally to update the rest of the app about the progress change
//     * <p>
//     * Might or might not be a change. Also can and will be called very frequently depending on MediaProgressTimer
//     *
//     * @param currentPosition
//     */
//    public final void onProgressUpdate(int currentPosition) {
//        MediaPlayerCore player = this.player;
//        if (currentPosition > 0) {
//            this.lastProgressUpdate = currentPosition;
//        }
//
//        if (player != null) {
//            long duration = player.getDuration();
//            if (currentPosition > 1000) {
//                updateResumePosition();
//            }
//            // we will inform the UI thread that the player has either reached the begin or end part of the video
//
//            if (!isOnPlayBegan() && isReachedPlayBeginProgress(currentPosition, duration)) {
//                setOnPlayBegan(true);
//                Timber.d("onProgressChanged[Beginning] positionMs = " + currentPosition + " of " + duration);
//                getHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!isFinishing()) {
//                            onPlayBeginning();
//                        }
//                    }
//                }, 1000);
//            }
//
//
//            if (!isOnPlayEnded() && isReachedPlayEndProgress(currentPosition, duration)) {
//                Timber.d("onProgressChanged[Ending] positionMs = " + currentPosition + " of " + duration);
//                setOnPlayEnded(true);
//                getHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!isFinishing()) {
//                            onPlayEnding();
//                        }
//                    }
//                });
//            }
//
//            onPlayerProgressUpdate(currentPosition, duration);
//        }
//
//    }
//
//
//    /**
//     * true if it is the beginning of video
//     *
//     * @param currentPosition
//     * @param duration
//     * @return
//     */
//    protected boolean isReachedPlayBeginProgress(int currentPosition, long duration) {
//        return duration > 0 && currentPosition > 0 && isStereoVolumeAvailable();
//    }
//
//
//    /**
//     * true if it is towards the end of video
//     *
//     * @param currentPosition
//     * @param duration
//     * @return
//     */
//    protected boolean isReachedPlayEndProgress(int currentPosition, long duration) {
//        return duration > 0 && currentPosition > duration - END_SECTION_OFFSET;
//    }
//
//    /**
//     * Reset the flags that indicate whether preferences to a unique video play are applied<br/>
//     * e.g.<br/>
//     * At the beginning of a video u might need to set volume or send event etc.<br/>
//     * <br/>
//     * At the end of the video u might need to show some indicator (repeat or upcoming...)<br/>
//     */
//    protected void onNewMedia() {
//        this.onPlayBegan = false;
//        this.onPlayEnded = false;
//    }
//
//
//    public synchronized void addMediaPlayerListener(MediaPlayerListener listener) {
//        listeners.addListener(listener);
//    }
//
//
//    public synchronized void removeMediaPlayerListener(MediaPlayerListener listener) {
//        listeners.removeListener(listener);
//    }
//
//
//    @Override
//    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//    }
//
//    @Override
//    public void onLoadingChanged(boolean isLoading) {
//
//    }
//
//    @Override
//    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        listeners.onPlayerStateChanged(playWhenReady, playbackState);
//    }
//
//    @Override
//    public void onRepeatModeChanged(int repeatMode) {
//
//    }
//
//    @Override
//    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//    }
//
//
//    @Override
//    public void onPositionDiscontinuity(int reason) {
//
//    }
//
//    @Override
//    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//    }
//
//    @Override
//    public void onSeekProcessed() {
//
//    }
//
//
//    /**
//     * @return
//     */
//    public SetupExtraParam getParam() {
//        return param;
//    }
//
//    /**
//     * should check if app is in debug mode
//     *
//     * @return
//     */
//    protected boolean isDebugMode() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.isDebugMode();
//        } else {
//            return false;
//        }
//    }
//
//
//    /**
//     * true if activity is finishing
//     *
//     * @return
//     */
//    public boolean isFinishing() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.isFinishing();
//        } else {
//            return false;
//        }
//    }
//
//
//    /**
//     * Get the max music volume from the OS level
//     *
//     * @return
//     */
//    public int getMaxSystemMusicStreamVolume() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getMaxSystemMusicStreamVolume();
//        } else {
//            return 1;
//        }
//    }
//
//
//    /**
//     * getting the music volume from the OS level
//     *
//     * @return
//     */
//    public int getSystemMusicStreamVolume() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getSystemMusicStreamVolume();
//        } else {
//            return 0;
//        }
//    }
//
//    /**
//     * Setting the music volume from the OS level
//     *
//     * @param volume
//     */
//    public void setSystemMusicStreamVolume(int volume) {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            temp.setSystemMusicStreamVolume(volume);
//        }
//    }
//
//
//    /**
//     * show a toast
//     *
//     * @param text
//     */
//    public void showToast(String text) {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            temp.showToast(text);
//        }
//    }
//
//
//    /**
//     * A string that describe the hardware
//     *
//     * @return
//     */
//    public String getHardwareType() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getHardwareType();
//        } else {
//            return null;
//        }
//    }
//
//
//    /**
//     * On player analytics event
//     *
//     * @param event
//     * @param args
//     */
//    public void onMediaPlayerError(Throwable error, PlaybackErrorType event, Object... args) {
//        listeners.onMediaPlayerError(error, event, args);
//    }
//
//
//    /**
//     * Return the current MediaPlayer.MediaContent which the view parent should be a reference of
//     *
//     * @return
//     */
//    @Nullable
//    public MediaContent getCurrentMediaContent() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getCurrentMediaContent();
//        } else {
//            return null;
//        }
//    }
//
//
//    /**
//     * Activity for showing dialog
//     *
//     * @return
//     */
//    @NonNull
//    public Activity getActivity() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getActivity();
//        } else {
//            throw new IllegalArgumentException("Must return a activity in delegate impl");
//        }
//    }
//
//    /**
//     * LayoutInflater for inflating
//     *
//     * @return
//     */
//    @NonNull
//    public LayoutInflater getLayoutInflater() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getLayoutInflater();
//        } else {
//            throw new IllegalArgumentException("Must return a LayoutInflater in delegate impl");
//        }
//    }
//
//
//    /**
//     * Will be used to show video meta
//     *
//     * @return
//     */
//    @Nullable
//    public TextView getDebugVideoStatusTextView() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getDebugVideoStatusTextView();
//        } else {
//            return null;
//        }
//    }
//
//
//    /**
//     * true if you want to send data to mux
//     *
//     * @return
//     */
//    public boolean isMuxStatsEnabled() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.isMuxStatsEnabled();
//        } else {
//            return false;
//        }
//    }
//
//
//    /**
//     * true if you want to send error to MUX explicitly
//     *
//     * @return
//     */
//    public boolean isSendErrorToMux() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.isSendErrorToMux();
//        } else {
//            return false;
//        }
//    }
//
//
//    /**
//     * Get screen size (not player) in Px
//     *
//     * @return
//     */
//    @NonNull
//    public Point getScreenSizePx() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getScreenSizePx();
//        } else {
//            throw new IllegalArgumentException("Must return a size in delegate impl");
//        }
//    }
//
//    /**
//     * Get data for the player to be send to mux
//     *
//     * @param video
//     * @return
//     */
//    public CustomerPlayerData getCustomerPlayerData(MediaContent video) {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getCustomerPlayerData(video);
//        } else {
//            throw new IllegalArgumentException("Must return a CustomerPlayerData in delegate impl");
//        }
//    }
//
//    /**
//     * Get data for a video to be send to mux
//     *
//     * @param video
//     * @param duration
//     * @return
//     */
//    public CustomerVideoData getCustomerVideoData(MediaContent video, long duration) {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getCustomerVideoData(video, duration);
//        } else {
//            throw new IllegalArgumentException("Must return a CustomerVideoData in delegate impl");
//        }
//    }
//
//
//    /**
//     * Show the Throwable (error) on UI (supposely it will automatically hide after a while) like a toast
//     *
//     * @param t
//     */
//    public void showError(Throwable t) {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            temp.showError(t);
//        }
//    }
//
//    /**
//     * Show the Fatal error (error) on UI. It should prompt the user for action (e.g. previous, try again, next). Like a full page dialog.
//     *
//     * @param t
//     */
//    public void showFatalPlayerError(Throwable t) {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            temp.showFatalPlayerError(t);
//        }
//    }
//
//
//    public boolean isUseStreamMusicVolume() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.isUseStreamMusicVolume();
//        } else {
//            return true;
//        }
//    }
//
//
//    /**
//     * should check debug and log http
//     *
//     * @return
//     */
//    protected boolean isLogNetworkTraffic() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.isLogNetworkTraffic();
//        } else {
//            return true;
//        }
//    }
//
//    /**
//     * App's getCacheDir
//     *
//     * @return
//     */
//    protected File getCacheDir() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getCacheDir();
//        } else {
//            throw new IllegalArgumentException("Must return a file for cache dir in delegate impl");
//        }
//    }
//
//
//    /**
//     * called some time after playback has started
//     */
//    void onPlayBeginning() {
//        listeners.onPlayBeginning();
//    }
//
//    /**
//     * called some time before playback ends
//     */
//    void onPlayEnding() {
//        listeners.onPlayEnding();
//    }
//
//    /**
//     * Called contini time when
//     *
//     * @param currentPosition
//     * @param duration
//     */
//    void onPlayerProgressUpdate(int currentPosition, long duration) {
//        listeners.onPlayerProgressUpdate(currentPosition, duration);
//    }
//
//
//
//    /**
//     * Interceptor to cache data and maintain it for a minute.
//     * <p>
//     * If the same network request is sent within a minute,
//     * the response is retrieved from cache.
//     */
//    private static class ResponseCacheInterceptor implements Interceptor {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request;
//            okhttp3.Response originalResponse = chain.proceed(request = chain.request());
//            if (ALLOW_LOCAL_CACHING) {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, max-age=" + 2419200)
//                        .build();
//            } else {
//                return originalResponse;
//            }
//        }
//    }
//
//
//    /**
//     * Show in AppMonitor
//     *
//     * @param msg
//     * @return
//     */
//    protected void onNetworkTraffiLog(String msg) {
//        listeners.onNetworkTraffiLog(msg);
//    }
//
//
//    @Nullable
//    private OkHttpClient.Builder getOkHttpBuilder() {
//        boolean LOG_ALL_HTTP = isLogNetworkTraffic();
//        // if we don't require any of the special http feature, we will return null, so that
//        // it will use the default http factory, it might be faster
//        if (!ALLOW_LOCAL_CACHING && !DEBUG_NETWORK_VS_CACHE && !LOG_ALL_HTTP) {
//            return null;
//        }
//
//
//        List<Protocol> protocols = new ArrayList<>();
//        protocols.add(Protocol.HTTP_1_1);
//        ConnectionPool connectionPool = new ConnectionPool(0, 5, TimeUnit.MINUTES);
//        //TODO: NOTE VIDEO MIGHT NEED A LONGER TIMEOUT
//        OkHttpClient.Builder builder = new OkHttpClient.Builder().
//                cache(new Cache(new File(getCacheDir(), "popsical_videos"), 1000 * 1000 * 1000)). // 1GB
//                connectTimeout(15, TimeUnit.SECONDS).
//                readTimeout(15, TimeUnit.SECONDS). // fadhli wants a shorter timeout
//                writeTimeout(15, TimeUnit.SECONDS). // fadhli wants a shorter timeout
//                connectionPool(connectionPool).
//                protocols(protocols);
//        if (ALLOW_LOCAL_CACHING) {
//            builder.addNetworkInterceptor(new ResponseCacheInterceptor());
//        }
//
//        if (DEBUG_NETWORK_VS_CACHE) {
//            builder.addInterceptor(new OfflineResponseCacheInterceptor());
//        }
//
//        if (CUSTOM_REQUEST_HEADERS) {
//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request.Builder requestBuilder = chain.request().newBuilder();
//                    for (Pair<String, String> header : getCustomHeaders()) {
//                        requestBuilder.addHeader(header.first, header.second);
//                    }
//                    Response response = chain.proceed(requestBuilder.build());
//                    return response;
//                }
//            });
//        }
//
//        if (LOG_ALL_HTTP) {
//            builder.addInterceptor(new Utils.OkHttpLogger(TAG) {
//                @Override
//                protected void onPostIntercept(Response response, boolean network, boolean cache) {
//                    if (isLogNetworkTraffic()) {
//                        String logLine = "No response";
//                        if (response != null) {
//                            Request request = response.request();
//                            if (request != null) {
//                                logLine = String.format("[%1$s][%2$s]%3$s%4$s ", request.method(), response.code(), (network ? "[N]" : "") + (cache ? "[C]" : ""), request.url().toString());
//                            }
//                        }
//                        onNetworkTraffiLog(logLine);
//                    }
//                }
//
//                @Override
//                public boolean isEnabled() {
//                    return true;
//                }
//            }.setLevel(HttpLoggingInterceptor.Level.HEADERS));
//        }
//        return builder;
//
//    }
//
//
//    @NonNull
//    private List<Pair<String, String>> getCustomHeaders() {
//        List<Pair<String, String>> headers = new ArrayList<>(1);
//        headers.add(new Pair<>("X-Popsical-Pan-Custom-Header", "lalala"));
//        return headers;
//    }
//
//
//    /**
//     * Interceptor to cache data and maintain it for four weeks.
//     * <p>
//     * If the device is offline, stale (at most four weeks old)
//     * response is fetched from the cache.
//     */
//    private class OfflineResponseCacheInterceptor implements Interceptor {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            long start = SystemClock.elapsedRealtime();
//            boolean isNetworkResponse = false;
//            boolean isCacheResponse = false;
//            long contentLengthKb = -1;
//            boolean success = false;
//            String url = null;
//            try {
//                Request request = chain.request();
//                url = request.url().toString();
//                Response originalResponse = chain.proceed(request);
//                isNetworkResponse = originalResponse.networkResponse() != null;
//                isCacheResponse = originalResponse.cacheResponse() != null;
//                contentLengthKb = Utils.parseLong(originalResponse.header("Content-Length"), -1) / 1000;
//                success = true;
//                return originalResponse;
//            } catch (IOException e) {
//                throw e;
//            } finally {
//                String msg = (isNetworkResponse ? "[NETWORK]" : "") + (isCacheResponse ? "[CACHE]" : "") + ("[" + (SystemClock.elapsedRealtime() - start) + "ms]") + (contentLengthKb <= 0 ? "" : ("[" + contentLengthKb + "kb]")) + " " + url;
//                if (success) {
//                    Timber.d(msg);
//                } else {
//                    Timber.e(msg);
//                }
//            }
//        }
//
//    }
//
//
//    /**
//     * Returns a new DataSource factory.
//     *
//     * @param useBandwidthMeter Whether to set {@link #mediaBandwidthMeter} as a listener to the new
//     *                          DataSource factory.
//     * @return A new DataSource factory.
//     */
//    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
//        return buildDataSourceFactory(useBandwidthMeter ? mediaBandwidthMeter : null);
//    }
//
//
//    public DataSource.Factory buildDataSourceFactory(MediaBandwidthMeter bandwidthMeter) {
//        return new DefaultDataSourceFactory(getContext(), bandwidthMeter,
//                buildHttpDataSourceFactory(bandwidthMeter));
//    }
//
//    public HttpDataSource.Factory buildHttpDataSourceFactory(MediaBandwidthMeter bandwidthMeter) {
//        HttpDataSource.Factory factory;
//        OkHttpClient.Builder builder;
//        if ((builder = getOkHttpBuilder()) != null) {
//            long t = SystemClock.elapsedRealtime();
//            OkHttpClient okhttpClient = builder.build();
//            Timber.d("Took " + (SystemClock.elapsedRealtime() - t) + "ms to create OkHttpClient");
//            factory = new OkHttpDataSourceFactory(okhttpClient, getUserAgent(), bandwidthMeter);
//        } else {
//            factory = new DefaultHttpDataSourceFactory(getUserAgent(), bandwidthMeter);
//            if (CUSTOM_REQUEST_HEADERS) {
//                HttpDataSource.RequestProperties defaultHeaders = factory.getDefaultRequestProperties();
//                for (Pair<String, String> header : getCustomHeaders()) {
//                    defaultHeaders.set(header.first, header.second);
//                }
//            }
//        }
//        return factory;
//    }
//
//
//    /**
//     * @return
//     */
//    protected String getUserAgent() {
//        MediaPlayerDelegate temp;
//        if ((temp = delegate) != null) {
//            return temp.getUserAgent();
//        } else {
//            return "Default";
//        }
//    }
//
//    public boolean useExtensionRenderers() {
//        // should be BuildConfig.FLAVOR.equals("withExtensions"); but i don't want to subscribe a new build flavor now.
//        // need to see if subscribe
//        return false;
//    }
//
//    @Nullable
//    public Collection<TrackFormat> getAudioTrackFormats() {
//        DefaultExoPlayerTrackSelector selector = trackSelector;
//        if (selector != null) {
//            return selector.getTrackFormats(C.TRACK_TYPE_AUDIO);
//        } else {
//            return null;
//        }
//    }
//
//    @Nullable
//    public Collection<TrackFormat> getVideoTrackFormats() {
//        DefaultExoPlayerTrackSelector selector = trackSelector;
//        if (selector != null) {
//            return selector.getTrackFormats(C.TRACK_TYPE_VIDEO);
//        } else {
//            return null;
//        }
//    }
//
//    boolean isOnPlayEnded() {
//        return onPlayEnded;
//    }
//
//    boolean isOnPlayBegan() {
//        return onPlayBegan;
//    }
//
//    void setOnPlayBegan(boolean onPlayBegan) {
//        this.onPlayBegan = onPlayBegan;
//    }
//
//    void setOnPlayEnded(boolean onPlayEnded) {
//        this.onPlayEnded = onPlayEnded;
//    }
//
//
//    @Override
//    public synchronized void resumeTimer() {
//        progressTimer.resume();
//    }
//
//    @Override
//    public synchronized void pauseTimer() {
//        progressTimer.pause();
//    }
//
//
//    private synchronized void startTimer() {
//        progressTimer.start();
//        resumeTimer();
//    }
//
//
//    private synchronized void stopTimer() {
//        progressTimer.cancel();
//    }
//
//
//    /**
//     * Use for progress update
//     *
//     * @see {@link MediaPlayer#onProgressUpdate(int)}
//     * @see {@link MediaPlayerListener#onPlayerProgressUpdate(int, long)}
//     */
//    protected class MediaProgressTimer extends CountDownTimer {
//        private boolean timerPaused = false;
//
//        public MediaProgressTimer() {
//            super(Long.MAX_VALUE, 250);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            MediaPlayerCore player = MediaPlayer.this.player;
//            if (delegate != null && !delegate.isSeekBarDragging() && isPlaying() && !isTimerPaused() && player != null) {
//                onProgressUpdate((int) (player.getCurrentPosition()));
//            }
//        }
//
//        public void pause() {
//            timerPaused = true;
//        }
//
//        public void resume() {
//            timerPaused = false;
//        }
//
//        public boolean isTimerPaused() {
//            return timerPaused;
//        }
//
//        @Override
//        public void onFinish() {
//            // will never finish automatically
//        }
//
//    }
//
//}
