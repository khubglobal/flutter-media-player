package com.easternblu.khub.common.model;

import com.easternblu.khub.common.api.CharakuPathConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Where to find the spec for this object:
 * http://developers.popsical.com/#!/Track/get_tracks_json
 * Created by yatpanng on 15/2/17.
 */
//    "images": {
//        "poster": {
//            "url": "string"
//        }
//    }
public class CharakuImages implements Serializable {

    
    @Expose
    @SerializedName(CharakuPathConstant._POSTER)
    protected Poster poster;

    public Poster getPoster() {
        return poster;
    }

    public String getPosterUrl() {
        return poster != null ? poster.getUrl() : null;
    }

    public static class Poster implements Serializable{

        @Expose
        @SerializedName(CharakuPathConstant._URL)
        protected String url;

        public String getUrl() {
            return url;
        }
    }
}
