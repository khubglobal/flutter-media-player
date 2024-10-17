//package com.easternblu.khub.media.exoplayer2;
//
//import android.util.Log;
//
//import com.easternblu.khub.media.Utils;
//import com.easternblu.khub.media.model.TrackFormat;
//import com.google.android.exoplayer2.util.MimeTypes;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.function.Predicate;
//
//
//public class VocalTrackLogic {
//    private static final Set<String> nonVocalLangs;
//
//
//    static {
//        nonVocalLangs = new HashSet<>();
//        nonVocalLangs.add("ie");
//    }
//
//
//    public static boolean isAudio(AudioTrack audioTrack) {
//        return MimeTypes.isAudio(audioTrack.getMimeType());
//    }
//
//
//    public static boolean isVocal(AudioTrack audioTrack, Collection<AudioTrack> all) {
//
//        if (audioTrack == null || (all != null && all.size() <= 1)) {
//            return false;
//        }
//
//        String lang = audioTrack.getLang();
//        boolean allTheSameLanguage = true;
//        boolean firstAudioTrack = true;
//        for (AudioTrack item : all) {
//            if (!Utils.isEquals(item.getLang(), lang, true)) {
//                allTheSameLanguage = false;
//            }
//            if (item.getIndex() < audioTrack.getIndex()) {
//                firstAudioTrack = false;
//            }
//        }
//
//        if (allTheSameLanguage) {
//            if (firstAudioTrack) {
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
//        return false;
//
//    }
//
//
//    public interface AudioTrack {
//
//        String getMimeType();
//
//        String getLang();
//
//        String getBaseUrl();
//
//        /**
//         * Either groupIndex or the order of the AdaptationSet, the logic doesn't really look the specific value
//         * only in comparsion to other AudioTrack in the same container file
//         *
//         * @return
//         */
//        int getIndex();
//
//    }
//}
