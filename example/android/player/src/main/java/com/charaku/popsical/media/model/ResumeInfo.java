//package com.easternblu.khub.media.model;
//
//import androidx.annotation.NonNull;
//
//import com.easternblu.khub.media.Utils;
//import com.easternblu.khub.media.exoplayer2.MediaPlayerCore;
//import com.google.android.exoplayer2.C;
//
///**
// * Created by pan on 29/5/17.
// */
//
//public class ResumeInfo {
//    public static final String TAG = ResumeInfo.class.getSimpleName();
//
//    @NonNull
//    protected ChannelMapping channelMapping;
//
//
//    protected long resumePosition;
//
//
//    protected int resumeWindow;
//
//
//    protected String playbackId;
//
//    public ResumeInfo() {
//        clear();
//    }
//
//
//    public void clear() {
//        playbackId = null;
//        channelMapping = null;
//        resumeWindow = C.INDEX_UNSET;
//        resumePosition = C.TIME_UNSET;
//    }
//
//    public void update(MediaContent mediaContent, MediaPlayerCore playerCore) {
//        if (playerCore != null && mediaContent != null) {
//            playbackId = mediaContent.getPlaybackId();
//            channelMapping = playerCore.getChannelMapping();
//            resumePosition = playerCore.getCurrentPosition();
//            resumeWindow = playerCore.isCurrentWindowSeekable() ? Math.max(0, (int) playerCore.getCurrentPosition()) : C.INDEX_UNSET;
//        }
//    }
//
//    public String description() {
//        return String.format("playbackId=%1$s, resumeWindow=%2$s, resumePosition=%3$s", playbackId, String.valueOf(resumeWindow), Utils.toTimeText(resumePosition));
//    }
//
//    public boolean canResume() {
//        return resumeWindow != C.INDEX_UNSET;
//    }
//
//
//    @NonNull
//    public ChannelMapping getChannelMapping() {
//        return channelMapping;
//    }
//
//    public long getResumePosition() {
//        return resumePosition;
//    }
//
//    public int getResumeWindow() {
//        return resumeWindow;
//    }
//
//
//    public boolean isSamePlayback(MediaContent mc) {
//        return mc != null && mc.getPlaybackId() != null && mc.getPlaybackId().equals(playbackId);
//    }
//}
