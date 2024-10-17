//package com.easternblu.khub.media.model;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.easternblu.khub.media.Utils;
//import com.google.android.exoplayer2.Format;
//import com.google.android.exoplayer2.util.MimeTypes;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import timber.log.Timber;
//
///**
// * Created by pan on 30/1/18.
// * <p>
// * A container class to encapsulate the {@link Format} object.
// * Logic specific to Popsical can be put here.
// */
//public class TrackFormat {
//    private static final Set<String> nonVocalLangs;
//
//
//    static {
//        nonVocalLangs = new HashSet<>();
//        nonVocalLangs.add("ie");
//    }
//
//
//    public static final String TAG = TrackFormat.class.getSimpleName();
//
//
//    private final Collection<TrackFormat> allFormats;
//    private Format format;
//    private int trackIndex, groupIndex;
//
//
//    public TrackFormat(Format format, int trackIndex, int groupIndex, @NonNull Collection<TrackFormat> allFormats) {
//        this.format = format;
//        this.allFormats = allFormats;
//        this.trackIndex = trackIndex;
//        this.groupIndex = groupIndex;
//    }
//
//    public Format getFormat() {
//        return format;
//    }
//
//    /**
//     * The index in {@link com.google.android.exoplayer2.source.TrackGroup}
//     *
//     * @return
//     */
//    public int getTrackIndex() {
//        return trackIndex;
//    }
//
//    /**
//     * The index in {@link com.google.android.exoplayer2.source.TrackGroupArray}
//     *
//     * @return
//     */
//    public int getGroupIndex() {
//        return groupIndex;
//    }
//
//
//    /**
//     * true if groupIndex and trackIndex match
//     * @param trackFormat
//     * @return
//     */
//    public boolean same(TrackFormat trackFormat){
//        return this.groupIndex == trackFormat.groupIndex && this.trackIndex == trackFormat.trackIndex;
//    }
//
//    /**
//     * @return true if it is a video track
//     */
//    public boolean isVideo() {
//        return MimeTypes.isVideo(format.sampleMimeType);
//    }
//
//
//
//
//    /**
//     * @return true if it is a audio track
//     */
//    public boolean isAudio() {
//        return MimeTypes.isAudio(format.sampleMimeType);
//    }
//
//
//    /**
//     * @return return the matching {@link MimeType}
//     */
//    @Nullable
//    public MimeType getMimeType() {
//        if (MimeTypes.isAudio(format.sampleMimeType)) {
//            return MimeType.audio;
//
//        } else if (MimeTypes.isVideo(format.sampleMimeType)) {
//            return MimeType.video;
//
//        } else {
//            return null;
//
//        }
//    }
//
//
//
//    /**
//     * Logic for determining if a audio track (from multi audio track video) is vocal or
//     * non-vocal.
//     * <p>
//     * 1) if all lang is the same then, groupIndex 1 is vocal (groupIndex 0 is non-vocal)<br/>
//     * 2) if there are 2 different langs, then track with lang == "ie" is non-vocal<br/>
//     *
//     * @return true if {@link #format} is vocal track
//     */
//    public boolean isVocalAudioTrack() {
//
//        if (!isAudio()) {
//            Timber.d("Track is not audio");
//            return false;
//        }
//
//
//        List<String> languages = extractFields(MimeType.audio, FormatField.language);
//        List<String> ids = extractFields(MimeType.audio, FormatField.id);
//
//
//        if (languages != null && languages.size() <= 1) {
//            return false;
//        }
//
//        String lang = this.format.language;
//        boolean allTheSameLanguage = Utils.isAllEqualsTo(languages, languages.get(0)), // e.g. ms, ms
//                allTheSameIds = Utils.isAllEqualsTo(languages, languages.get(0)); // e.g. 128kbps, 128kbps
//
//        if (allTheSameLanguage) {
//            if (this.getGroupIndex() == 1) { // first audio track?
//                return true;
//            } else {
//                return false;
//            }
//        } else if (lang != null) {
//            lang = lang.toLowerCase();
//            if (nonVocalLangs.contains(lang)) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        Timber.d("Unable to determine the audio vocal track");
//        return false;
//    }
//
//
//    public enum MimeType {
//        audio, video
//    }
//
//    private enum FormatField {
//        id, language
//    }
//
//    /**
//     * Internal method to extract value of a specific field for each element of {@link #allFormats}
//     *
//     * @param mimeType
//     * @param field
//     * @return
//     */
//    private List<String> extractFields(@Nullable MimeType mimeType, @NonNull FormatField field) {
//        List<String> values = new ArrayList<>();
//        for (TrackFormat trackFormat : allFormats) {
//            if (mimeType == null || mimeType == trackFormat.getMimeType()) {
//                values.add(getFieldFromFormat(field, trackFormat.getFormat()));
//            }
//        }
//        return values;
//    }
//
//    /**
//     * Method to extract the value of each field for the specified format
//     *
//     * @param field
//     * @param format
//     * @return
//     */
//    @Nullable
//    private String getFieldFromFormat(FormatField field, Format format) {
//        switch (field) {
//            case id:
//                return format.id;
//            case language:
//                return format.language;
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "TrackFormat{" +
//                "format=" + format +
//                ", trackIndex=" + trackIndex +
//                ", groupIndex=" + groupIndex +
//                '}';
//    }
//
//
////    01-30 15:45:48.507 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(128kbps, audio/mp4, audio/mp4a-latm, 128000, ms, [-1, -1, -1.0], [-1, 48000]), trackIndex=0, groupIndex=0}
////01-30 15:45:48.516 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(128kbps, audio/mp4, audio/mp4a-latm, 128000, ms, [-1, -1, -1.0], [-1, 48000]), trackIndex=0, groupIndex=1}
////01-30 15:48:14.542 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(128kbps, audio/mp4, audio/mp4a-latm, 128000, ms, [-1, -1, -1.0], [-1, 48000]), trackIndex=0, groupIndex=0}
////01-30 15:48:14.553 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(128kbps, audio/mp4, audio/mp4a-latm, 128000, ms, [-1, -1, -1.0], [-1, 48000]), trackIndex=0, groupIndex=1}
////01-30 15:50:45.386 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(190357d6-9dc0-43a3-bd14-b724096b5fc0, audio/mp4, audio/mp4a-latm, 128000, null, [-1, -1, -1.0], [2, 48000]), trackIndex=0, groupIndex=0}
////01-30 16:23:46.908 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(audio-mp4-en-0, audio/mp4, audio/mp4a-latm, 136304, en, [-1, -1, -1.0], [2, 48000]), trackIndex=0, groupIndex=0}
////01-30 16:23:46.917 D/TrackSelectionHelper: buildView: AUDIO format = TrackFormat{format=Format(audio-mp4-ie-0, audio/mp4, audio/mp4a-latm, 135486, ie, [-1, -1, -1.0], [2, 48000]), trackIndex=0, groupIndex=1}
//
//
//}
