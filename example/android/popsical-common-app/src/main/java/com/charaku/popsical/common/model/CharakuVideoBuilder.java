package com.easternblu.khub.common.model;

import java.util.Map;

/**
 * A builder object generated using {@link CharakuVideo} constructor
 */
public class CharakuVideoBuilder {
    private int mId;
    private int mArtistId;
    private String mTitle;
    private boolean mHasStream;
    private long mRuntime;
    private String mType;
    private int mNumber;
    private String mDescription;
    private String mReleaseDate;
    private String mOldVideoUrl;
    private String mVideoUrl;
    private String mPosterUrl;
    private CharakuImages mImages;
    private Map<String, Object> mDash;
    private Map<String, Object> mHls;
    private Track mTrack;

    public CharakuVideoBuilder setId(int id) {
        mId = id;
        return this;
    }

    public CharakuVideoBuilder setArtistId(int artistId) {
        mArtistId = artistId;
        return this;
    }

    public CharakuVideoBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public CharakuVideoBuilder setHasStream(boolean hasStream) {
        mHasStream = hasStream;
        return this;
    }

    public CharakuVideoBuilder setRuntime(long runtime) {
        mRuntime = runtime;
        return this;
    }

    public CharakuVideoBuilder setType(String type) {
        mType = type;
        return this;
    }

    public CharakuVideoBuilder setNumber(int number) {
        mNumber = number;
        return this;
    }

    public CharakuVideoBuilder setDescription(String description) {
        mDescription = description;
        return this;
    }

    public CharakuVideoBuilder setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
        return this;
    }

    public CharakuVideoBuilder setOldVideoUrl(String oldVideoUrl) {
        mOldVideoUrl = oldVideoUrl;
        return this;
    }

    public CharakuVideoBuilder setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
        return this;
    }

    public CharakuVideoBuilder setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
        return this;
    }

    public CharakuVideoBuilder setImages(CharakuImages images) {
        mImages = images;
        return this;
    }

    public CharakuVideoBuilder setDash(Map<String, Object> dash) {
        mDash = dash;
        return this;
    }

    public CharakuVideoBuilder setHls(Map<String, Object> hls) {
        mHls = hls;
        return this;
    }

    public CharakuVideoBuilder setTrack(Track track) {
        mTrack = track;
        return this;
    }

    public CharakuVideo createCharakuVideo() {
        return new CharakuVideo(mId, mArtistId, mTitle, mHasStream, mRuntime, mType, mNumber, mDescription, mReleaseDate, mOldVideoUrl, mVideoUrl, mPosterUrl, mImages, mDash, mHls, mTrack);
    }
}