package com.easternblu.khub.common.model;

import java.util.List;

/**
 * A builder object generated using {@link Track} constructor
 */
public class TrackBuilder {
    private int mId;
    private int mNumber;
    private String mTitle;
    private String mAltTitle;
    private String mLangCode;
    private int mRuntime;
    private String mReleaseDate;
    private String mSource;
    private boolean mHasVideo;
    private CharakuVideo mVideo;
    private boolean mBlock;

    private BlockReason mBlockReason;
    private boolean mPremium;
    private CharakuImages mImages;
    private List<Artist> mArtists;
    private String mArtistsString;
    private boolean mTrackArtistsSorted;
    private String mPosterUrl;
    private PlayQueueUser mAddedBy;
    private int mPlayQueuePosition;
    private int mPlayQueueTrackId;
    private int mTempPlayQueuePosition;
    private int mPlayListId;
    private Track.PlayQueueTrackType mPlayQueueTrackType;

    public TrackBuilder setId(int id) {
        mId = id;
        return this;
    }

    public TrackBuilder setNumber(int number) {
        mNumber = number;
        return this;
    }

    public TrackBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public TrackBuilder setAltTitle(String altTitle) {
        mAltTitle = altTitle;
        return this;
    }

    public TrackBuilder setLangCode(String langCode) {
        mLangCode = langCode;
        return this;
    }

    public TrackBuilder setRuntime(int runtime) {
        mRuntime = runtime;
        return this;
    }

    public TrackBuilder setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
        return this;
    }

    public TrackBuilder setSource(String source) {
        mSource = source;
        return this;
    }

    public TrackBuilder setHasVideo(boolean hasVideo) {
        mHasVideo = hasVideo;
        return this;
    }

    public TrackBuilder setVideo(CharakuVideo video) {
        mVideo = video;
        return this;
    }

    public TrackBuilder setBlock(boolean block) {
        mBlock = block;
        return this;
    }

    public TrackBuilder setBlockReason(BlockReason blockReason) {
        mBlockReason = blockReason;
        return this;
    }

    public TrackBuilder setPremium(boolean premium) {
        mPremium = premium;
        return this;
    }

    public TrackBuilder setImages(CharakuImages images) {
        mImages = images;
        return this;
    }

    public TrackBuilder setArtists(List<Artist> artists) {
        mArtists = artists;
        return this;
    }

    public TrackBuilder setArtistsString(String artistsString) {
        mArtistsString = artistsString;
        return this;
    }

    public TrackBuilder setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
        return this;
    }

    public TrackBuilder setAddedBy(PlayQueueUser addedBy) {
        mAddedBy = addedBy;
        return this;
    }

    public TrackBuilder setPlayQueuePosition(int playQueuePosition) {
        mPlayQueuePosition = playQueuePosition;
        return this;
    }

    public TrackBuilder setPlayQueueTrackId(int playQueueTrackId) {
        mPlayQueueTrackId = playQueueTrackId;
        return this;
    }

    public TrackBuilder setTempPlayQueuePosition(int tempPlayQueuePosition) {
        mTempPlayQueuePosition = tempPlayQueuePosition;
        return this;
    }

    public TrackBuilder setPlayListId(int playListId) {
        mPlayListId = playListId;
        return this;
    }

    public TrackBuilder setTrackArtistsSorted(boolean trackArtistsSorted) {
        this.mTrackArtistsSorted = trackArtistsSorted;
        return this;
    }

    public TrackBuilder setPlayQueueTrackType(Track.PlayQueueTrackType playQueueTrackType) {
        mPlayQueueTrackType = playQueueTrackType;
        return this;
    }

    public Track createTrack() {
        return new Track(mId, mNumber, mTitle, mAltTitle, mLangCode, mRuntime, mReleaseDate, mSource, mHasVideo, mVideo, mBlock, mBlockReason, mPremium, mImages, mArtists, mArtistsString, mPosterUrl, mAddedBy, mPlayQueuePosition, mPlayQueueTrackId, mTempPlayQueuePosition, mPlayListId, mPlayQueueTrackType,mTrackArtistsSorted);
    }
}