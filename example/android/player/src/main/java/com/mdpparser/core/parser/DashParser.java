//package com.mdpparser.core.parser;
//
//import android.net.Uri;
//import android.util.Log;
//
//import com.easternblu.khub.media.exoplayer2.VocalTrackLogic;
////import com.google.android.exoplayer2.util.MimeTypes;
//import com.mdpparser.data.model.DashAdaptationSet;
//import com.mdpparser.data.model.DashManifest;
//import com.mdpparser.data.model.DashPeriod;
//import com.mdpparser.data.model.DashRepresentation;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.simpleframework.xml.Serializer;
//import org.simpleframework.xml.core.Persister;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DashParser {
//    public static final String TAG = DashParser.class.getSimpleName();
//
//    @Nullable
//    public DashManifest parseManifest(@NotNull InputStream inputStream) throws Exception {
//        Serializer serializer = new Persister();
//        return serializer.read(DashManifest.class, inputStream, false);
//    }
//
//
//    /**
//     * Get vocal manifestUri using {@link VocalTrackLogic}
//     *
//     * @param manifestUri
//     * @param manifest
//     * @return
//     */
//    public Uri getAudioUri(Uri manifestUri, DashManifest manifest, boolean vocal) throws Exception {
//        if (manifest == null || manifestUri == null) {
//            throw new Exception("manifest or manifest Uri is null");
//        }
//
//        int audioIndex = 0;
//        List<VocalTrackLogic.AudioTrack> audioTrackList = new ArrayList<>();
//        if (manifest.periods != null) {
//            for (DashPeriod period : manifest.periods) {
//                if (period.adaptationSet != null) {
//                    for (final DashAdaptationSet adaptationSet : period.adaptationSet) {
//                        if (adaptationSet.representation != null) {
//                            for (final DashRepresentation representation : adaptationSet.representation) {
//                                if (MimeTypes.isAudio(adaptationSet.mimeType)) {
//                                    final int index = audioIndex + 1;
//                                    audioTrackList.add(new VocalTrackLogic.AudioTrack() {
//                                        @Override
//                                        public String getMimeType() {
//                                            return adaptationSet.mimeType;
//                                        }
//
//                                        @Override
//                                        public String getLang() {
//                                            return adaptationSet.lang;
//                                        }
//
//                                        @Override
//                                        public String getBaseUrl() {
//                                            return representation.baseURL;
//                                        }
//
//                                        @Override
//                                        public int getIndex() {
//                                            return index;
//                                        }
//                                    });
//                                    audioIndex++;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        VocalTrackLogic.AudioTrack found = null;
//        for (VocalTrackLogic.AudioTrack audioTrack : new ArrayList<>(audioTrackList)) {
//            if (VocalTrackLogic.isVocal(audioTrack, audioTrackList) == vocal) {
//                found = audioTrack;
//                break;
//            }
//        }
//
//        if (found == null || found.getBaseUrl() == null) {
//            throw new Exception("No audio track with baseUrl (vocal=" + vocal + ") found");
//
//        }
//        String url = manifestUri.toString();
//        int pathEndPos = url.lastIndexOf("/");
//        if (pathEndPos > 0) {
//            return Uri.parse(url.substring(0, pathEndPos + 1) + found.getBaseUrl());
//        } else {
//            throw new Exception("Unable to find \'/\' char in manifest url");
//        }
//    }
//
//
//}
