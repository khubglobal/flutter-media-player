package com.easternblu.khub.common.model;

import android.text.TextUtils;

import com.easternblu.khub.common.Common;
import com.easternblu.khub.common.api.CharakuPathConstant;
import com.easternblu.khub.common.util.CommonMethod;
import com.easternblu.khub.common.util.JSONArrays;
import com.easternblu.khub.common.util.Lists;
import com.easternblu.khub.common.util.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.easternblu.khub.common.api.CharakuPathConstant._ALGOLIA_ATTRIBUTE_ARTISTS;
import static com.easternblu.khub.common.api.CharakuPathConstant._ALGOLIA_ATTRIBUTE_BLOCK;
import static com.easternblu.khub.common.api.CharakuPathConstant._ALGOLIA_ATTRIBUTE_LANG_CODE;
import static com.easternblu.khub.common.api.CharakuPathConstant._ALGOLIA_ATTRIBUTE_OBJECT_ID;
import static com.easternblu.khub.common.api.CharakuPathConstant._ALGOLIA_ATTRIBUTE_POSTER_URL;
import static com.easternblu.khub.common.api.CharakuPathConstant._ALGOLIA_ATTRIBUTE_TITLE;

/**
 * <a href="https://developers.popsical.com/#!/PlayQueue/get_me_play_queue_json">API DOC</a>
 * Track object
 * Created by Pradeep on 1/3/16 na karma.
 * Modified by Pan
 */
public class Track implements Serializable {

    public static final String ARTIST_LAZY_DELIMS = ",+&^;";

    public enum Unplayable {
        PREMIUM_REQUIRED, COUNTRY, PLATFORM, TIME_LAPSED, NO_VIDEO, UNKNOWN
    }

    @Expose
    @SerializedName(CharakuPathConstant._MUSIXMATCH_ID)
    private int musixMatchId;

    @Expose
    @SerializedName(CharakuPathConstant._MUSIXMATCH_LYRICS_ID)
    private int musixMatchLyricsId;

    @Expose
    @SerializedName(CharakuPathConstant._MUSIXMATCH_SUBTITLE_ID)
    private int musixSubtitleId;

    public int getMusixMatchId() {
        return musixMatchId;
    }

    public void setMusixMatchId(int musixMatchId) {
        this.musixMatchId = musixMatchId;
    }

    public int getMusixMatchLyricsId() {
        return musixMatchLyricsId;
    }

    public void setMusixMatchLyricsId(int musixMatchLyricsId) {
        this.musixMatchLyricsId = musixMatchLyricsId;
    }

    public int getMusixSubtitleId() {
        return musixSubtitleId;
    }

    public void setMusixSubtitleId(int musixSubtitleId) {
        this.musixSubtitleId = musixSubtitleId;
    }

    @Expose
    @SerializedName(CharakuPathConstant._ID)
    private int id;

    @Expose
    @SerializedName(CharakuPathConstant._NUMBER)
    private int number;

    @Expose
    @SerializedName(CharakuPathConstant._TITLE)
    private String title;

    @Expose
    @SerializedName(CharakuPathConstant._ALT_TITLE)
    private String altTitle;

    @Expose
    @SerializedName(CharakuPathConstant._LANG_CODE)
    private String langCode;

    @Expose
    @SerializedName(CharakuPathConstant._RUNTIME)
    private int runtime;

    @Expose
    @SerializedName(CharakuPathConstant._RELEASE_DATE)
    private String releaseDate;

    @Expose
    @SerializedName(CharakuPathConstant._SOURCE)
    private String source;

    @Expose
    @SerializedName(CharakuPathConstant._PLAYLIST_TRACK_ID)
    private int playlistTrackId;

    @Expose
    @SerializedName(CharakuPathConstant._HAS_VIDEO)
    private boolean hasVideo;

    @Expose
    @SerializedName(CharakuPathConstant._VIDEO)
    private CharakuVideo video;

    @Expose
    @SerializedName(CharakuPathConstant._BLOCK)
    private boolean block;

    @Expose
    @SerializedName(CharakuPathConstant._BLOCK_REASON)
    private BlockReason blockReason;

    @Expose
    @SerializedName(CharakuPathConstant._IS_ORIGINAL)
    private boolean isOriginal;

    @Expose
    @SerializedName(CharakuPathConstant._LYRICS_COUNT)
    private int lyricsCount;

    @Expose
    @SerializedName(CharakuPathConstant._PREMIUM)
    private boolean premium;

    @Expose
    @SerializedName(CharakuPathConstant._IMAGES)
    protected CharakuImages images;

    @Deprecated
    @Expose
    @SerializedName(CharakuPathConstant._ARTISTS)
    private List<Artist> artists;

    @Expose
    @SerializedName(CharakuPathConstant._TRACK_ARTISTS)
    private List<TrackArtist> trackArtists;

    private volatile boolean trackArtistsSorted = false;


    // not set by server, cached value of artists
    // The value is exposed so that it can be marshalling and unmarshalling via JSON
    @Expose
    protected String artistsString = null;

    // For local reference
    // The value is exposed so that it can be marshalling and unmarshalling via JSON
    @Expose
    protected String posterUrl;

    // For local reference
    private PlayQueueUser addedBy;

    // For local reference
    // Position of track in server play queue
    private int playQueuePosition;

    // For local reference
    // Unique track identifier of track in server play queue
    private int playQueueTrackId;

    // For local reference
    // Used for updating track play queue position
    private int tempPlayQueuePosition;

    // ID of playlist which this instance of track currently belongs to
    // For local reference
    private int playlistId, playQueueId = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAltTitle() {
        return altTitle;
    }

    public String[] getTitles() {
        if (TextUtils.isEmpty(altTitle)) {
            return new String[]{getTitle()};
        } else {
            String titlesString = getTitle() + Common.SEMICOLON_ + getAltTitle();
            return titlesString.split(Common.SEMICOLON_);
        }
    }

    public void setAltTitle(String altTitle) {
        this.altTitle = altTitle;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public CharakuVideo getVideo() {
        // just because a video is blocked we should still return the object
        // if (isHasVideo() && !isBlocked() && video != null) {
        if (isHasVideo() && video != null) {
            // CharakuVideo uses some values in Track as fallback, so it needs a back reference of Track
            video.setTrack(this);
            return video;
        } else {
            return null;
        }
    }

    public CharakuImages getImages() {
        return images;
    }


    /**
     * Will return the List {@link Track#getArtists()} if {@link Track#getTrackArtists()} is empty or null
     * <p>
     *
     * @return
     * @use {@link #getTrackArtists()} instead
     */
    @Deprecated
    public List<Artist> getArtists() {
        if (Lists.isNotEmpty(getTrackArtists())) {
            List<Artist> artists;
            Lists.addAll(artists = new ArrayList<>(), TrackArtist.TO_ARTIST_CONVERTER, getTrackArtists());
            return artists;
        } else {
            return this.artists;
        }
    }

    public void setVideo(CharakuVideo video) {
        this.video = video;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public void setPlayQueueId(int playQueueId) {
        this.playQueueId = playQueueId;
    }


    public int getPlayQueueId() {
        return playQueueId;
    }

    public boolean isBlocked() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }


    /**
     * Replacing {@link #getArtists()} inside Track object.
     * @return
     */
    public synchronized List<TrackArtist> getTrackArtists() {
        if (!trackArtistsSorted && trackArtists != null) {
            Collections.sort(trackArtists, TrackArtist.COMPARATOR);
            trackArtistsSorted = true;
        }
        return trackArtists;
    }

    @Deprecated
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getArtistsString() {
        if (artistsString != null) {
            return artistsString;
        }
        return artistsString = toArtistString(getArtists(), Common.EMPTY_STRING);
    }


    /**
     * Convert list of artists to readable String
     *
     * @param artists
     * @param defaultValue
     * @return
     */
    public static String toArtistString(List<Artist> artists, String defaultValue) {
        List<String> artistsNames = new ArrayList<>();
        if (!Lists.isNullOrEmpty(artists)) {
            for (Artist artist : artists) {
                artistsNames.add(artist.getNormalizedName(null));
            }
            return Lists.asString(artistsNames, Common.COMMA, true, Strings.STRING_CONVERTER);
        } else {
            return defaultValue;
        }
    }


    public void setArtistsString(String artistsString) {
        this.artistsString = artistsString;
    }

    public void setPlayqueuePosition(int playQueuePosition) {
        this.playQueuePosition = playQueuePosition;
    }

    public int getPlayqueuePosition() {
        return this.playQueuePosition;
    }

    public int getPlayQueueTrackId() {
        return playQueueTrackId;
    }

    public void setPlayQueueTrackId(int playQueueTrackId) {
        this.playQueueTrackId = playQueueTrackId;
    }


    /**
     * Should not be made public, as the actual image url that u want might be from the video itself
     *
     * @return
     */
    String getImagePosterUrl() {
        String url;
        if (getImages() != null && (url = getImages().getPosterUrl()) != null) {
            return url;
        }
        return null;
    }


    /**
     * Use getPosterUrl instead. this is renamed to getPosterUrl becaue the track might not have a video, but it still need a poster
     *
     * @return
     */
    @Deprecated
    public String getVideoPosterUrl() {
        return getPosterUrl();
    }


    /**
     * return, a image url that can be display to represent the track. Checking in this order:
     * 1) custom set poster url
     * 2) video -> posterUrl
     * 3) image -> poster -> url from the track object itself
     *
     * @return
     */
    public String getPosterUrl() {
        return Strings.getFirstNotNull(
                posterUrl,
                getVideo() != null ? getVideo().getPosterUrl() : null,
                getImagePosterUrl(),
                Common.EMPTY_STRING);
    }

    public int getTempPlayQueuePosition() {
        return tempPlayQueuePosition;
    }

    public void setTempPlayQueuePosition(int tempPlayQueuePosition) {
        this.tempPlayQueuePosition = tempPlayQueuePosition;
    }

    public PlayQueueUser getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(PlayQueueUser addedBy) {
        this.addedBy = addedBy;
    }

    public String getAdderDisplayName() {
        if (addedBy != null) {
            return addedBy.getDisplayName();
        } else {
            return Common.EMPTY_STRING;
        }
    }

    public String getAdderProfilePhotoUrl() {
        if (addedBy != null) {
            return addedBy.getProfilePhotoUrl();
        } else {
            return Common.EMPTY_STRING;
        }
    }

    public int getAdderUserId() {
        if (addedBy != null) {
            return addedBy.getUserId();
        } else {
            return -1;
        }
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getSource() {
        return source;
    }

    public String getVideoUrl() {
        if (video != null) {
            return video.getVideoUrl();
        } else {
            return Common.EMPTY_STRING;
        }
    }

    public boolean isUnplayable() {
        return getUnplayableReason() != null;
    }

    public BlockReason getBlockReason() {
        return blockReason;
    }

    public Unplayable getUnplayableReason() {
        if (isBlocked()) {
            BlockReason reason = getBlockReason();
            if (reason == null) {
                return getDeduceUnplayableReason();
            } else if (reason.isCountryBlocked()) {
                return Unplayable.COUNTRY;
            } else if (reason.isPremiumAccessBlocked()) {
                return Unplayable.PREMIUM_REQUIRED;
            } else if (reason.isPlatformBlocked()) {
                return Unplayable.PLATFORM;
            } else if (reason.isTimeLapsedBlocked()) {
                return Unplayable.TIME_LAPSED;
            } else if (getVideo() == null) {
                return Unplayable.NO_VIDEO;
            } else {
                return Unplayable.UNKNOWN;
            }
        } else if (getVideo() == null) {
            return Unplayable.NO_VIDEO;
        } else {
            return null;
        }
    }

    private Unplayable getDeduceUnplayableReason() {
        if (isPremium()) {
            return Unplayable.PREMIUM_REQUIRED;
        } else {
            return Unplayable.COUNTRY;
        }
    }

    /**
     * Used by GSON
     */
    public Track() {

    }

    @Deprecated
    Track(int id, int number, String title, String altTitle, String langCode, int runtime, String releaseDate, String source, boolean hasVideo, CharakuVideo video, boolean block, BlockReason blockReason, boolean premium, CharakuImages images, List<Artist> artists, String artistsString, String posterUrl, PlayQueueUser addedBy, int playQueuePosition, int playQueueTrackId, int tempPlayQueuePosition, int playlistId, PlayQueueTrackType playQueueTrackType, boolean trackArtistsSorted) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.altTitle = altTitle;
        this.langCode = langCode;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.source = source;
        this.hasVideo = hasVideo;
        this.video = video;
        this.block = block;
        this.blockReason = blockReason;
        this.premium = premium;
        this.images = images;
        this.artists = artists;
        this.artistsString = artistsString;
        this.posterUrl = posterUrl;
        this.addedBy = addedBy;
        this.playQueuePosition = playQueuePosition;
        this.playQueueTrackId = playQueueTrackId;
        this.tempPlayQueuePosition = tempPlayQueuePosition;
        this.playlistId = playlistId;
        this.playQueueTrackType = playQueueTrackType;
        this.trackArtistsSorted = trackArtistsSorted;
    }

    public static Track createTrackWithAlgoliaObject(JSONObject hit) throws JSONException {
        String posterUrl;
        TrackBuilder builder = new TrackBuilder().
                setLangCode(hit.optString(_ALGOLIA_ATTRIBUTE_LANG_CODE, Common.EMPTY_STRING)).
                setId(Integer.parseInt(hit.getString(_ALGOLIA_ATTRIBUTE_OBJECT_ID))).
                setTitle(hit.optString(_ALGOLIA_ATTRIBUTE_TITLE, Common.EMPTY_STRING)).
                setBlock(hit.optBoolean(_ALGOLIA_ATTRIBUTE_BLOCK, false)).
                setPremium(false).
                setPosterUrl(posterUrl = hit.optString(_ALGOLIA_ATTRIBUTE_POSTER_URL, Common.EMPTY_STRING));

        JSONArray artists = CommonMethod.getJsonAttribute(hit, _ALGOLIA_ATTRIBUTE_ARTISTS, JSONArray.class);

        String artistsString;
        if (artists != null) {
            List<String> artistNames = JSONArrays.getElementOfType(artists, String.class, new ArrayList<String>());
            artistsString = Artist.parseName(null, Lists.asString(artistNames), Common.COMMA);
        } else {
            artistsString = Common.EMPTY_STRING;
        }
        builder.setArtistsString(artistsString);

        CharakuVideo video = new CharakuVideoBuilder().createCharakuVideo();
        video.setPosterUrl(posterUrl);
        builder.setVideo(video);
        // set to true, because search result has no video, but we pretend there is
        builder.setHasVideo(true);
        return builder.createTrack();
    }

    public enum PlayQueueTrackType {
        PREVIOUS, CURRENT, PRIORITY, NEXT, UNDEFINED
    }

    private PlayQueueTrackType playQueueTrackType = PlayQueueTrackType.UNDEFINED;

    public void setPlayQueueTrackType(PlayQueueTrackType playQueueTrackType) {
        this.playQueueTrackType = playQueueTrackType;
    }

    public PlayQueueTrackType getPlayQueueTrackType() {
        return this.playQueueTrackType;
    }

    public void setBlockReason(BlockReason blockReason) {
        this.blockReason = blockReason;
    }

    public int getPlaylistTrackId() {
        return playlistTrackId;
    }

    public void setPlaylistTrackId(int playlistTrackId) {
        this.playlistTrackId = playlistTrackId;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public int getLyricsCount() {
        return lyricsCount;
    }

    public void setLyricsCount(int lyricsCount) {
        this.lyricsCount = lyricsCount;
    }
}
