//package com.easternblu.khub.media;
//
//import android.app.Activity;
//import android.graphics.Point;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.widget.TextView;
//
//import com.easternblu.khub.media.model.MediaActivitySupport;
//import com.easternblu.khub.media.model.MediaContent;
//import com.google.android.exoplayer2.source.MediaSourceEventListener;
//import com.mux.stats.sdk.core.model.CustomerPlayerData;
//import com.mux.stats.sdk.core.model.CustomerVideoData;
//
//import java.io.File;
//
///**
// * Created by yatpanng on 23/4/17.
// * A Delegate to provide the mandatory function of the player.
// * <p>
// * Like fetching the {@link MediaContent}
// * <p>
// * <p>
// * Also for other things
// */
//public interface MediaPlayerDelegate {
//
//    /**
//     * should check if app is in debug mode
//     *
//     * @return
//     */
//    boolean isDebugMode();
//
//
//
//    /**
//     * true if activity is finishing
//     *
//     * @return
//     */
//    boolean isFinishing();
//
//
//    /**
//     * Get the max music volume from the OS level
//     *
//     * @return
//     */
//    int getMaxSystemMusicStreamVolume();
//
//
//    /**
//     * getting the music volume from the OS level
//     *
//     * @returns
//     */
//    int getSystemMusicStreamVolume();
//
//    /**
//     * Setting the music volume from the OS level
//     *
//     * @param volume
//     */
//    void setSystemMusicStreamVolume(int volume);
//
//
//    /**
//     * Show a non-interrupt message on UI with a message
//     *
//     * @param text
//     */
//    void showToast(String text);
//
//
//    /**
//     * A string that describe the hardware
//     *
//     * @return
//     */
//    String getHardwareType();
//
//
//    /**
//     * Return the current MediaPlayer.MediaContent which the view parent should be a reference of
//     *
//     * @return
//     */
//    @Nullable
//    MediaContent getCurrentMediaContent();
//
//    /**
//     * must not be null. This should return the activity that supports MediaActivitySupport
//     *
//     * @return
//     */
//    @NonNull
//    MediaActivitySupport getMediaActivitySupport();
//
//    /**
//     * Activity for showing dialog
//     *
//     * @return
//     */
//    @NonNull
//    Activity getActivity();
//
//    /**
//     * LayoutInflater for inflating
//     *
//     * @return
//     */
//    @NonNull
//    LayoutInflater getLayoutInflater();
//
//
//    /**
//     * Will be used to show video meta
//     *
//     * @return
//     */
//    @Nullable
//    TextView getDebugVideoStatusTextView();
//
//
//    /**
//     * true if you want to send data to mux
//     *
//     * @return
//     */
//    boolean isMuxStatsEnabled();
//
//
//    /**
//     * true if you want to send error to MUX explicitly
//     *
//     * @return
//     */
//    boolean isSendErrorToMux();
//
//
//    /**
//     * Get screen size (not player) in Px
//     *
//     * @return
//     */
//    Point getScreenSizePx();
//
//    /**
//     * Get data for the player to be send to mux
//     *
//     * @param video
//     * @return
//     */
//    CustomerPlayerData getCustomerPlayerData(MediaContent video);
//
//    /**
//     * Get data for a video to be send to mux
//     *
//     * @param video
//     * @param duration
//     * @return
//     */
//    CustomerVideoData getCustomerVideoData(MediaContent video, long duration);
//
//
//    /**
//     * Show the Throwable (error) on UI (supposely it will automatically hide after a while) like a toast
//     *
//     * @param t
//     */
//    void showError(Throwable t);
//
//    /**
//     * Show the Fatal error (error) on UI. It should prompt the user for action (e.g. previous, try again, next). Like a full page dialog.
//     *
//     * @param t
//     */
//    void showFatalPlayerError(Throwable t);
//
//
//    /**
//     * true if it should use music stream volume for player audio volume
//     *
//     * @return
//     */
//    boolean isUseStreamMusicVolume();
//
//
//    /**
//     * should check debug and log http
//     *
//     * @return
//     */
//    boolean isLogNetworkTraffic();
//
//    /**
//     * App's getCacheDir
//     *
//     * @return
//     */
//    File getCacheDir();
//
//
//    /**
//     * User-Agent string for http header
//     *
//     * @return
//     */
//    String getUserAgent();
//
//
//    /**
//     * If the seekbar on the UI is being dragged at the moment
//     *
//     * @return
//     */
//    boolean isSeekBarDragging();
//
//
//    /**
//     * Similar to the MediaPlayerListener.onMediaTransfer* callbacks. If you provide this optional listener then
//     * You can get more information on what is being loaded (e.g source type and format)
//     *
//     * @return
//     */
//    @Nullable
//    MediaSourceEventListener getMediaSourceEventListener();
//
//
//}
