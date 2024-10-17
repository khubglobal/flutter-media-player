//package com.easternblu.khub.media.exoplayer2;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.StringRes;
//import android.util.Log;
//import android.util.Pair;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckedTextView;
//
//import com.easternblu.khub.media.MediaConstants;
//import com.easternblu.khub.media.R;
//import com.easternblu.khub.media.model.TrackFormat;
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.Format;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.source.TrackGroup;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelector;
//import com.google.android.exoplayer2.ui.TrackSelectionView;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
////12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=2,rendererIndex=0,trackGroupArrayIndex=0,formatIndex=0,format=Format(video-mp4-def-0, video/mp4, video/avc, 1224245, null, [854, 480, 29.97003], [-1, -1])
////        12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=2,rendererIndex=0,trackGroupArrayIndex=0,formatIndex=1,format=Format(video-mp4-def-1, video/mp4, video/avc, 684906, null, [640, 360, 29.97003], [-1, -1])
////        12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=2,rendererIndex=0,trackGroupArrayIndex=0,formatIndex=2,format=Format(video-mp4-def-2, video/mp4, video/avc, 197622, null, [416, 234, 29.97003], [-1, -1])
////        12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=2,rendererIndex=0,trackGroupArrayIndex=0,formatIndex=3,format=Format(video-mp4-def-3, video/mp4, video/avc, 5553964, null, [1920, 1080, 29.97003], [-1, -1])
////        12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=2,rendererIndex=0,trackGroupArrayIndex=0,formatIndex=4,format=Format(video-mp4-def-4, video/mp4, video/avc, 2944661, null, [1280, 720, 29.97003], [-1, -1])
////        12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=1,rendererIndex=1,trackGroupArrayIndex=0,formatIndex=0,format=Format(audio-mp4-en-0, audio/mp4, audio/mp4a-latm, 131453, en, [-1, -1, -1.0], [2, 48000])
////        12-06 09:43:34.658 23507-23507/com.easternblu.khub.tv D/DefaultExoPlayerTrackSelector: rendererType=1,rendererIndex=1,trackGroupArrayIndex=1,formatIndex=0,format=Format(audio-mp4-ie-0, audio/mp4, audio/mp4a-latm, 131493, ie, [-1, -1, -1.0], [2, 48000])
////
//
///**
// * Use MediaContentSelector
// */
//@Deprecated
//public abstract class DefaultExoPlayerTrackSelector   {
//    public final String TAG = DefaultExoPlayerTrackSelector.class.getSimpleName();
//    private DefaultTrackSelector trackSelector = null;
//    private Context ctx;
//    private Player player;
//
//    public DefaultExoPlayerTrackSelector(Context ctx, DefaultTrackSelector defaultTrackSelector, Player player) {
//        this.trackSelector = defaultTrackSelector;
//        this.player = player;
//        this.ctx = ctx;
//    }
//
//
//
//    public List<TrackTypeDialogOption> getTrackTypeDialogOptions() {
//        Player player = getPlayer();
//
//        List<TrackTypeDialogOption> options = new ArrayList<>();
//        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//        if (mappedTrackInfo == null) {
//            return options;
//        }
//
//        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
//            final TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
//            if (trackGroups.length != 0) {
//                @StringRes final int label;
//                final int rendererIndex = i;
//                final int rendererType = player.getRendererType(i);
//                switch (rendererType) {
//                    case C.TRACK_TYPE_AUDIO:
//                        label = R.string.exo_track_selection_title_audio;
//                        break;
//                    case C.TRACK_TYPE_VIDEO:
//                        label = R.string.exo_track_selection_title_video;
//                        break;
//                    case C.TRACK_TYPE_TEXT:
//                        label = R.string.exo_track_selection_title_text;
//                        break;
//                    default:
//                        continue;
//                }
//
//                options.add(new TrackTypeDialogOption() {
//
//                    public int getRendererType() {
//                        return rendererType;
//                    }
//
//
//                    public int getRendererIndex() {
//                        return rendererIndex;
//                    }
//
//
//                    public TrackGroupArray getTrackGroupArray() {
//                        return trackGroups;
//                    }
//
//
//                    public CharSequence getTitle() {
//                        return ctx.getString(label);
//                    }
//                });
//            }
//        }
//        return options;
//    }
//
//
//    public Player getPlayer() {
//        return player;
//    }
//
//
//
//    public void setVideoEnable(Activity activity, boolean enabled) {
//        TrackTypeDialogOption option = findTrackTypeDialogOptions(C.TRACK_TYPE_VIDEO);
//        Pair<AlertDialog, TrackSelectionView> dialogAndView = createTrackSelectionDialog(activity, option);
//        if (dialogAndView != null && dialogAndView.second != null) {
//            if (enabled) {
//                dialogAndView.second.onDefaultViewClicked();
//            } else {
//                dialogAndView.second.onDisableViewClicked();
//            }
//            dialogAndView.second.applySelection();
//        }
//    }
//
//
//    public void setVideoTrack(Activity activity, final TrackFormat correctTrackFormat) throws MediaPlayerException {
//        TrackTypeDialogOption videoDialogOption = findTrackTypeDialogOptions(C.TRACK_TYPE_VIDEO);
//        if (videoDialogOption != null) {
//            // show dialog dismiss dialog in 1sec
//            performAndConfirmDialogClick(activity, videoDialogOption, new AutoTrackSelectionInterface() {
//
//                public boolean shouldClick(TrackFormat trackFormat) {
//                    return correctTrackFormat.same(trackFormat);
//                }
//            });
//        } else {
//            throw new MediaPlayerException("No video track available");
//        }
//    }
//
//
//    public void setAudioEnable(Activity activity, boolean enabled) {
//        TrackTypeDialogOption option = findTrackTypeDialogOptions(C.TRACK_TYPE_AUDIO);
//        Pair<AlertDialog, TrackSelectionView> dialogAndView = createTrackSelectionDialog(activity, option);
//
//        if (dialogAndView != null && dialogAndView.second != null) {
//            if (enabled) {
//                dialogAndView.second.onDefaultViewClicked();
//            } else {
//                dialogAndView.second.onDisableViewClicked();
//            }
//            dialogAndView.second.applySelection();
//        }
//    }
//
//
//    /**
//     * Given a {@link MappingTrackSelector.MappedTrackInfo}
//     * find the TrackGroupArray and the index matching the type rendererType
//     *
//     * @param mappedTrackInfo
//     * @param rendererType
//     * @return
//     */
//    @Nullable
//    private Pair<Integer, TrackGroupArray> findTrackGroupArray(@NonNull MappingTrackSelector.MappedTrackInfo mappedTrackInfo, @MediaConstants.RendererType int rendererType) {
//        for (int i = 0; i < mappedTrackInfo.length; i++) {
//            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
//            if (trackGroups.length != 0 && player != null) {
//                @MediaConstants.RendererType
//                int trackRendererType = player.getRendererType(i);
//                if (trackRendererType == rendererType) {
//                    return new Pair<>(i, trackGroups);
//                }
//            }
//        }
//        return null;
//    }
//
//
//    public void showVideoPopup(Activity activity, View view) {
//        TrackTypeDialogOption temp = findTrackTypeDialogOptions(C.TRACK_TYPE_VIDEO);
//        if (temp != null) {
//            showTrackSelectionDialog(activity, temp);
//        }
//    }
//
//
//    public void showTextPopup(Activity activity, View view) {
//        TrackTypeDialogOption temp = findTrackTypeDialogOptions(C.TRACK_TYPE_TEXT);
//        if (temp != null) {
//            showTrackSelectionDialog(activity, temp);
//        }
//    }
//
//
//    public void showAudioPopup(Activity activity, View view) {
//        TrackTypeDialogOption temp = findTrackTypeDialogOptions(C.TRACK_TYPE_AUDIO);
//        if (temp != null) {
//            showTrackSelectionDialog(activity, temp);
//        }
//    }
//
//    public void showTrackSelectionDialog(Activity activity, TrackTypeDialogOption option) {
//        Pair<AlertDialog, TrackSelectionView> temp = createTrackSelectionDialog(activity, option);
//        if (temp != null) {
//            temp.first.show();
//        }
//    }
//
//    @Nullable
//    private Pair<AlertDialog, TrackSelectionView> createTrackSelectionDialog(Activity activity, TrackTypeDialogOption option) {
//        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//        if (mappedTrackInfo != null && option != null) {
//            CharSequence title = option.getTitle();
//            int rendererIndex = option.getRendererIndex();
//            int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
//            boolean allowAdaptiveSelections =
//                    rendererType == C.TRACK_TYPE_VIDEO
//                            || (rendererType == C.TRACK_TYPE_AUDIO
//                            && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
//                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
//            Pair<AlertDialog, TrackSelectionView> dialogPair =
//                    TrackSelectionView.getDialog(activity, title, trackSelector, rendererIndex);
//            assignTagsToCheckedTextView(dialogPair.second);
//            dialogPair.second.setShowDisableOption(true);
//            dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
//            return dialogPair;
//        } else {
//            return null;
//        }
//    }
//
//
//    public Collection<TrackFormat> getTrackFormats(@MediaConstants.RendererType int rendererType) {
//        TrackTypeDialogOption dialogOptions = findTrackTypeDialogOptions(rendererType);
//        Collection<TrackFormat> allFormats = new ArrayList<>();
//        if (dialogOptions != null) {
//            TrackGroupArray trackGroups = dialogOptions.getTrackGroupArray();
//            for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
//                TrackGroup trackGroup = trackGroups.get(groupIndex);
//                if (trackGroup == null) {
//                    continue;
//                }
//                for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
//                    TrackFormat trackFormat = new TrackFormat(trackGroup.getFormat(trackIndex), trackIndex, groupIndex, allFormats);
//                    allFormats.add(trackFormat);
//                }
//            }
//        }
//        return allFormats;
//    }
//
//
//    /**
//     * Assign a meta object (TrackFormat) to each selection
//     *
//     * @param trackSelectionView
//     */
//    private void assignTagsToCheckedTextView(TrackSelectionView trackSelectionView) {
//        TrackGroupArray trackGroups = trackSelectionView.getTrackGroups();
//        CheckedTextView[][] views = trackSelectionView.getTrackViews();
//        Collection<TrackFormat> allFormats = new ArrayList<>();
//        for (int groupIndex = 0; groupIndex < views.length; groupIndex++) {
//            if (views[groupIndex] == null) {
//                continue;
//            }
//            TrackGroup group = trackGroups.get(groupIndex);
//            for (int trackIndex = 0; trackIndex < views[groupIndex].length; trackIndex++) {
//                TrackFormat trackFormat = new TrackFormat(group.getFormat(trackIndex), trackIndex, groupIndex, allFormats);
//                allFormats.add(trackFormat);
//                CheckedTextView trackView = views[groupIndex][trackIndex];
//                trackView.setTag(R.id.media_object_id, trackFormat);
//            }
//        }
//    }
//
//
//
//    public boolean hasVocalTrack() throws MediaPlayerException {
//        if (trackSelector != null) {
//
//            TrackTypeDialogOption audioDialogOption = findTrackTypeDialogOptions(C.TRACK_TYPE_AUDIO);
//            if (audioDialogOption != null) {
//
//                return audioDialogOption.getTrackGroupArray().length > 1;
//            }
//        }
//        throw new MediaPlayerException("No audio track available");
//    }
//
//
//
//    public void setVocalTrack(final Activity activity, final boolean vocalOn, boolean userAction) throws MediaPlayerException {
//        TrackTypeDialogOption audioDialogOption = findTrackTypeDialogOptions(C.TRACK_TYPE_AUDIO);
//        if (audioDialogOption != null) {
//            // show dialog dismiss dialog in 1sec
//            performAndConfirmDialogClick(activity, audioDialogOption, new AutoTrackSelectionInterface() {
//
//                public boolean shouldClick(TrackFormat trackFormat) {
//                    return vocalOn == trackFormat.isVocalAudioTrack();
//                }
//            });
//        } else {
//            throw new MediaPlayerException("No audio track available");
//        }
//    }
//
//    public MappingTrackSelector.MappedTrackInfo getMappedTrackInfo() {
//        return trackSelector.getCurrentMappedTrackInfo();
//    }
//
//    private interface AutoTrackSelectionInterface {
//        boolean shouldClick(TrackFormat trackFormat);
//    }
//
//
//
//    public void performAndConfirmDialogClick(Activity activity, TrackTypeDialogOption audioDialogOption, AutoTrackSelectionInterface callback) {
//        Pair<AlertDialog, TrackSelectionView> dialogAndView = this.createTrackSelectionDialog(activity, audioDialogOption);
//        // need to toggle between (index 1 and 0) to enable/disable vocal
//        if (dialogAndView != null && dialogAndView.second != null) {
//            // we have no simple way to select the audio track so we just simulate what the dialog will do
//            ViewGroup root = dialogAndView.second;
//            if (root != null) {
//                for (int i = 0; i < root.getChildCount(); i++) {
//                    View trackView = root.getChildAt(i);
//                    TrackFormat trackFormat;
//                    Object tag = trackView.getTag(R.id.media_object_id);
//                    if (tag != null && tag instanceof TrackFormat) {
//                        trackFormat = (TrackFormat) tag;
//                        //Pair<Integer, Integer> pair = (Pair<Integer, Integer>) tag;
//                        boolean shouldClick = callback.shouldClick(trackFormat);
//                        if (shouldClick && (root instanceof TrackSelectionView)) {
//                            TrackSelectionView tsv = (TrackSelectionView) root;
//                            tsv.onTrackViewClicked(trackView);
//                            tsv.applySelection();
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    public abstract void showSingleToast(String text);
//
//    private String getString(int res) {
//        return ctx.getString(res);
//    }
//
//
//    public TrackSelector getTrackSelector() {
//        return trackSelector;
//    }
//
//
//    public MappingTrackSelector getMappingTrackSelector() {
//        return trackSelector;
//    }
//
//
//    public void onReleasePlayer() {
//
//    }
//
//
//    public MappingTrackSelector.MappedTrackInfo getCurrentMappedTrackInfo() {
//        return trackSelector.getCurrentMappedTrackInfo();
//    }
//
//
//    /**
//     * @param rendererType e.g. C.TRACK_TYPE_AUDIO
//     * @return
//     */
//    @Nullable
//    private TrackTypeDialogOption findTrackTypeDialogOptions(@MediaConstants.RendererType int rendererType) {
//        List<TrackTypeDialogOption> options = getTrackTypeDialogOptions();
//        if (options != null && !options.isEmpty()) {
//            for (TrackTypeDialogOption option : options) {
//                if (option.getRendererType() == rendererType) {
//                    return option;
//                }
//            }
//        }
//        return null;
//    }
//
//
//    interface TrackTypeDialogOption {
//        int getRendererType();
//
//        int getRendererIndex();
//
//        CharSequence getTitle();
//
//        TrackGroupArray getTrackGroupArray();
//    }
//
//
//}
