package com.easternblu.khub.common.model;

import com.easternblu.khub.common.api.CharakuPathConstant;
import com.easternblu.khub.common.util.Converter;
import com.easternblu.khub.common.util.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

/**
 * track artist object for v3 API. It is to be used inside {@link Track} object to model artist properties that are
 * only specific to a track.
 *
 * e.g. artist's role in a track (main/featured)
 * or artist's appearance time,
 * or artist's comment for that track
 * Created by pan
 */
public class TrackArtist implements Serializable {

    @Expose
    @SerializedName(CharakuPathConstant._TYPE)
    protected String type;

    @Expose
    @SerializedName(CharakuPathConstant._ARTIST)
    protected Artist artist;

    /**
     * Artist is the main artist for this track
     *
     * @return
     */
    public boolean isMain() {
        return type == null || Strings.isNotNullAndEquals("main", type);
    }


    /**
     * Artist is the featured artist for this track
     *
     * @return
     */
    public boolean isFeatured() {
        return Strings.isNotNullAndEquals("featured", type);
    }

    /**
     * Artist
     *
     * @return
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Just
     */
    public static final Converter<TrackArtist, Artist> TO_ARTIST_CONVERTER = new Converter<TrackArtist, Artist>() {
        @Override
        public Artist convert(TrackArtist from) {
            return from.getArtist();
        }
    };

    /**
     * used for sorting. Basically main artist should appear first in a list
     *
     * @return
     */
    private int sortIndex() {
        if (isMain()) {
            return 0;
        } else if (isMain()) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Java comparater for sorting
     */
    public static final Comparator<TrackArtist> COMPARATOR = new Comparator<TrackArtist>() {
        @Override
        public int compare(TrackArtist ta1, TrackArtist ta2) {
            return new Integer(ta1.sortIndex()).compareTo(ta2.sortIndex());
        }

    };
}