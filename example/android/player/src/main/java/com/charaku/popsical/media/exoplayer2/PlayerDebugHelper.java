//package com.easternblu.khub.media.exoplayer2;
//
//import android.widget.TextView;
//
//import com.easternblu.khub.media.Utils;
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.Format;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.Timeline;
//import com.google.android.exoplayer2.decoder.DecoderCounters;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
//
//import java.util.Locale;
//
///**
// * Created by pan on 3/4/17.
// * <p>
// * A class copied from DebugTextViewHelper, so that more debug info can be displayed
// * <p>
// * @see {@link com.google.android.exoplayer2.ui.DebugTextViewHelper}
// */
//public class PlayerDebugHelper implements Runnable, ExoPlayer.EventListener {
//
//    private static final int REFRESH_INTERVAL_MS = 1000;
//
//    private final SimpleExoPlayer player;
//    private final TextView textView;
//    private MediaBandwidthMeter appBandwidthMeter;
//    private boolean started;
//
//    /**
//     * @param player   The {@link SimpleExoPlayer} from which debug information should be obtained.
//     * @param textView The {@link TextView} that should be updated to display the information.
//     */
//    public PlayerDebugHelper(SimpleExoPlayer player, MediaBandwidthMeter appBandwidthMeter, TextView textView) {
//        this.player = player;
//        this.textView = textView;
//        this.appBandwidthMeter = appBandwidthMeter;
//    }
//
//    /**
//     * Starts periodic updates of the {@link TextView}. Must be called from the application's main
//     * thread.
//     */
//    public void start() {
//        if (started) {
//            return;
//        }
//        started = true;
//        player.addListener(this);
//        updateAndPost();
//    }
//
//    /**
//     * Stops periodic updates of the {@link TextView}. Must be called from the application's main
//     * thread.
//     */
//    public void stop() {
//        if (!started) {
//            return;
//        }
//        started = false;
//        player.removeListener(this);
//        textView.removeCallbacks(this);
//    }
//
//    // ExoPlayer.EventListener implementation.
//
//    @Override
//    public void onLoadingChanged(boolean isLoading) {
//        // Do nothing.
//    }
//
//    @Override
//    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        updateAndPost();
//    }
//
//    @Override
//    public void onPositionDiscontinuity(int reason) {
//        updateAndPost();
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
//    @Override
//    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//        // Do nothing.
//    }
//
//    @Override
//    public void onPlayerError(ExoPlaybackException error) {
//        // Do nothing.
//    }
//
//    @Override
//    public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
//        // Do nothing.
//    }
//
//    // Runnable implementation.
//    @Override
//    public void run() {
//        updateAndPost();
//    }
//
//    // Private methods.
//    private void updateAndPost() {
//        textView.setText(getBandwidthInfoString() + getPlayerStateString() + getPlayerWindowIndexString() + getVideoString()
//                + getAudioString());
//        textView.removeCallbacks(this);
//        textView.postDelayed(this, REFRESH_INTERVAL_MS);
//    }
//
//    private String getPlayerStateString() {
//        String text = "playWhenReady:" + player.getPlayWhenReady() + " playbackState:";
//        switch (player.getPlaybackState()) {
//            case ExoPlayer.STATE_BUFFERING:
//                text += "buffering";
//                break;
//            case ExoPlayer.STATE_ENDED:
//                text += "ended";
//                break;
//            case ExoPlayer.STATE_IDLE:
//                text += "idle";
//                break;
//            case ExoPlayer.STATE_READY:
//                text += "ready";
//                break;
//            default:
//                text += "unknown";
//                break;
//        }
//        return text;
//    }
//
//    public static final String PATTERN_TIME = "HH:mm:ss";
//
//    private String getBandwidthInfoString() {
//        long bitrateEstimate = appBandwidthMeter.getBitrateEstimate();
//        long lastTransferStart = appBandwidthMeter.getLastTransferStart();
//        long lastTransferEnd = appBandwidthMeter.getLastTransferEnd();
//        int lastBytesTransferred = appBandwidthMeter.getLastBytesTransferred();
//
//        return "bitrate_estimate: " + Utils.roundHalfUp(bitrateEstimate / 1000000f, 3) + "Mbit/sec " +
//                ((lastTransferEnd > lastTransferStart) ? ("\ntransfer_start: " + Utils.toString(PATTERN_TIME, lastTransferStart) + "  end:" + Utils.toString(PATTERN_TIME, lastTransferEnd)) : "") +
//                (lastBytesTransferred > 0 ? ("\nlast_transfer_total: " + Utils.roundHalfUp(lastBytesTransferred / 1000000f, 3) + " MBytes") : "") +
//                "\n";
//    }
//
//
//    private String getPlayerWindowIndexString() {
//        return " window:" + player.getCurrentWindowIndex();
//    }
//
//    private String getVideoString() {
//        Format format = player.getVideoFormat();
//        if (format == null) {
//            return "";
//        }
//        return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x"
//                + format.height + " bitrate:" + buildBitrateString(format) + getDecoderCountersBufferCountString(player.getVideoDecoderCounters())
//                + ")";
//    }
//
//    private String getAudioString() {
//        Format format = player.getAudioFormat();
//        if (format == null) {
//            return "";
//        }
//        return "\n" + format.sampleMimeType + "(id:" + format.id + " hz:" + format.sampleRate + " ch:"
//                + format.channelCount
//                + getDecoderCountersBufferCountString(player.getAudioDecoderCounters()) + ")";
//    }
//
//    private static String getDecoderCountersBufferCountString(DecoderCounters counters) {
//        if (counters == null) {
//            return "";
//        }
//        counters.ensureUpdated();
//        return " rb:" + counters.renderedOutputBufferCount
//                + " sb:" + counters.skippedOutputBufferCount;
//    }
//
//    @Override
//    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
//        // not used
//    }
//
//    @Override
//    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//    }
//
//    public static String buildBitrateString(Format format) {
//        return format.bitrate == Format.NO_VALUE ? ""
//                : String.format(Locale.US, "%.2fMbit", format.bitrate / 1000000f);
//    }
//
//}
