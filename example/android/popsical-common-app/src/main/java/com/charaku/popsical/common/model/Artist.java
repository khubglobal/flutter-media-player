package com.easternblu.khub.common.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easternblu.khub.common.Common;
import com.easternblu.khub.common.api.CharakuPathConstant;
import com.easternblu.khub.common.util.Lists;
import com.easternblu.khub.common.util.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Artist object
 * Created by leechunhoe on 10/11/16.
 */
public class Artist implements Serializable {
    public static boolean ALLOW_NAME_PARSING = false;
    public static final String ARTIST_LAZY_DELIMS = ",+&^;";

    @Expose
    @SerializedName(CharakuPathConstant._ID)
    protected int id;

    @Expose
    @SerializedName(CharakuPathConstant._NAME)
    protected String name;

    @Expose
    @SerializedName(CharakuPathConstant._GENDER)
    protected String gender;

    @Expose
    @SerializedName(CharakuPathConstant._LANG_CODES)
    protected String[] langCodes;

    @Expose
    @SerializedName(CharakuPathConstant._POSTER_URL)
    protected String posterUrl;


    @Expose
    @SerializedName(CharakuPathConstant._TOTAL_TRACKS)
    protected long totalTracks;


    @Expose
    @SerializedName(CharakuPathConstant._IMAGES)
    protected CharakuImages images;

    protected List<String> names = null;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Attempt to parse the name String and return it as a List. Will use known delimiter like
     * "+" and "&"
     *
     * @return
     */
    public List<String> getNames() {
        if (names == null) {
            List<String> parsedNames = new ArrayList<>();
            getNormalizedName(parsedNames);
        }
        return names;
    }


    /**
     * Convert list of artists to readable String
     *
     * @return
     */
    @NonNull
    public String getNormalizedName(@Nullable List<String> parsedName) {
        return parseName(parsedName, getName());
    }


    public static String parseName(@Nullable List<String> parsedNames, String name) {
        return parseName(parsedNames, name, Common.COMMA);
    }

    /**
     * Catering all kinds of internal delimiter and coverting them to comma "a^b,c;d+x" will be "a,b,c,d,x"
     *
     * @param parsedNames
     * @param name
     * @param delim
     * @return
     */
    public static String parseName(@Nullable List<String> parsedNames, String name, String delim) {
        if (parsedNames == null) {
            parsedNames = new ArrayList<>(1);
        }

        if (ALLOW_NAME_PARSING) {
            for (String subArtist : Strings.splitByString(name, ARTIST_LAZY_DELIMS.toCharArray(), true)) {
                parsedNames.add(subArtist);
            }
        } else {
            parsedNames.add(name);
        }
        return Lists.asString(parsedNames, delim, true, Strings.STRING_CONVERTER);
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String[] getLangCodes() {
        return langCodes;
    }

    public void setLangCodes(String[] langCodes) {
        this.langCodes = langCodes;
    }

    public String getPosterUrl() {
        return Strings.getFirstNotNull(posterUrl, images == null ? null : images.getPosterUrl());
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public CharakuImages getImages() {
        return images;
    }

    public String getImagePosterUrl() {
        String url;
        if (getImages() != null && (url = getImages().getPosterUrl()) != null) {
            return url;
        }
        return null;
    }

    public long getTotalTracks() {
        return totalTracks;
    }
}