//package com.easternblu.khub.media.exoplayer2;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.SystemClock;
//import androidx.annotation.Nullable;
//import android.util.Pair;
//
//import com.easternblu.khub.media.MediaPlayerListener;
//import com.easternblu.khub.media.Utils;
//import com.google.android.exoplayer2.upstream.BandwidthMeter;
//import com.google.android.exoplayer2.upstream.DataSpec;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.TransferListener;
//
//import timber.log.Timber;
//
///**
// * A class that help you adjust {@link DefaultBandwidthMeter#getBitrateEstimate()} value
// * <p>
// * Created by pan on 11/5/17.
// */
//public class MediaBandWidthMeterImpl implements MediaBandwidthMeter {
//    public static boolean LOG_NETWORK_TRANSFER = false;
//    protected static final String TAG = MediaBandWidthMeterImpl.class.getSimpleName();
//    protected BandwidthMeter bandwidthMeter;
//    protected TransferListener<Object> transferListener;
//    protected double coefficient;
//    protected long lastBitrateEstimate, transferStart, transferEnd, totalTransferTime, totalBytesTransfered;
//    protected int bytesTransferred;
//    protected Context ctx;
//    protected Uri uri;
//    @Nullable
//    protected MediaPlayerListener listener;
//
//    public MediaBandWidthMeterImpl(BandwidthMeter bandwidthMeter, TransferListener<Object> transferListener, double coefficient) {
//        this.bandwidthMeter = bandwidthMeter;
//        this.transferListener = transferListener;
//        this.coefficient = coefficient;
//    }
//
//    public MediaPlayerListener getListener() {
//        return listener;
//    }
//
//    public void setListener(MediaPlayerListener listener) {
//        this.listener = listener;
//    }
//
//    // allow us to tweak the Bitrate Estimate
//    private final boolean VARIABLE_COEFFICIENT_ENABLED = false;
//    private final long COEFFICIENT_CHANGE_FREQ = 10000;
//    private final Pair<Double, Double> COEFFICIENT_RANGE = new Pair<>(0.005d /*0.5%*/, 0.05d/*5%*/);
//    private long lastCoefficientChangeTime = 0;
//
//    @Override
//    public long getBitrateEstimate() {
//        if (VARIABLE_COEFFICIENT_ENABLED && SystemClock.elapsedRealtime() - lastCoefficientChangeTime > COEFFICIENT_CHANGE_FREQ) {
//            coefficient = COEFFICIENT_RANGE.first + Math.random() * (COEFFICIENT_RANGE.second - COEFFICIENT_RANGE.first);
//            lastCoefficientChangeTime = SystemClock.elapsedRealtime();
//        }
//
//        if (!VARIABLE_COEFFICIENT_ENABLED && coefficient != 1) {
//            coefficient = 1;
//        }
//
//        long original = bandwidthMeter.getBitrateEstimate();
//        lastBitrateEstimate = (coefficient == 1) ? bandwidthMeter.getBitrateEstimate() : Double.valueOf(bandwidthMeter.getBitrateEstimate() * coefficient).longValue();
//        if (VARIABLE_COEFFICIENT_ENABLED) {
//            Timber.d("coefficient = " + coefficient + " original = " + original + " lastBitrateEstimate = " + lastBitrateEstimate);
//        }
//        return lastBitrateEstimate;
//    }
//
//    @Override
//    public void onTransferStart(Object source, DataSpec dataSpec) {
//        transferStart = Utils.getNow();
//
//        uri = dataSpec.uri;
//
//        if (LOG_NETWORK_TRANSFER) {
//            Timber.d("onTransferStart: " + uri.toString() + " source = " + source);
//        }
//
//        if (listener != null) {
//            listener.onMediaTransferStart(uri);
//        }
//        bytesTransferred = 0;
//        transferListener.onTransferStart(source, dataSpec);
//    }
//
//    @Override
//    public void onBytesTransferred(Object source, int bytesTransferred) {
//        this.bytesTransferred += bytesTransferred;
//        this.totalBytesTransfered += bytesTransferred;
//        listener.onMediaTransferring(uri, this.bytesTransferred, Utils.getNow() - transferStart);
//        transferListener.onBytesTransferred(source, bytesTransferred);
//    }
//
//
//    @Override
//    public void onTransferEnd(Object source) {
//        transferEnd = Utils.getNow();
//        transferListener.onTransferEnd(source);
//        // for debugging purposes
//        long transferDuration = (transferEnd - transferStart);
//        totalTransferTime += transferDuration;
//
//        if (listener != null) {
//            listener.onMediaTransferEnd(uri, bytesTransferred, transferDuration);
//        }
//
//
//        if (LOG_NETWORK_TRANSFER) {
//            double seconds = (totalTransferTime / 1000d);
//            double kBytesPerSec = (seconds == 0) ? 0 : ((totalBytesTransfered / 1000d) / seconds);
//            if (uri != null) {
//                Timber.d("onTransferEnd: " + uri.toString() + " " + transferDuration + "ms. " + Utils.roundHalfUp(kBytesPerSec, 2) + " kBytes per sec");
//            }
//        }
//    }
//
//    @Override
//    public long getLastBitrateEstimate() {
//        return lastBitrateEstimate;
//    }
//
//    @Override
//    public long getLastTransferStart() {
//        return transferStart;
//    }
//
//    @Override
//    public long getLastTransferEnd() {
//        return transferEnd;
//    }
//
//    @Override
//    public int getLastBytesTransferred() {
//        return bytesTransferred;
//    }
//
//
//}
