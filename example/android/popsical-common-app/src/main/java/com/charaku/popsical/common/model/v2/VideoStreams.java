package com.easternblu.khub.common.model.v2;

import com.easternblu.khub.common.Common;
import com.easternblu.khub.common.api.CharakuPathConstant;
import com.easternblu.khub.common.util.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * This object is fetched separately
 * Created by yatpanng on 3/8/17.
 */
public class VideoStreams implements Serializable {
    //    {
//        "video_id": 15784,
//            "dash": {
//        "p720": "https://popsical.vo.llnwd.net/storage/521032_0010e8008e5bb29bc45342e02543ad9d/521032.mpd?e=1501559198&h=a1b0b5d8cb4310a584d206abd55bc96a"
//    }
//    }
    @Expose
    @SerializedName(CharakuPathConstant._VIDEO_ID)
    protected int videoId;


    @Expose
    @SerializedName(CharakuPathConstant._DASH)
    protected Map<String, Object> dash;

    @Expose
    @SerializedName(CharakuPathConstant._HLS)
    protected Map<String, Object> hls;

    private String videoUrl;

    public String getVideoUrl() {
        if (videoUrl != null) {
            return videoUrl;
        }

        videoUrl = Common.EMPTY_STRING;
        Object p720;
        String dashUrl = null, hlsUrl = null;
        if (dash != null && (p720 = dash.get(CharakuPathConstant._P720)) != null && p720 instanceof String) {
            dashUrl = p720.toString();

        }

        if (hls != null && (p720 = hls.get(CharakuPathConstant._P720)) != null && p720 instanceof String) {
            hlsUrl = p720.toString();
        }

        return videoUrl = Strings.getFirstNotNull(dashUrl, hlsUrl);
    }

    public int getVideoId() {
        return videoId;
    }
}
