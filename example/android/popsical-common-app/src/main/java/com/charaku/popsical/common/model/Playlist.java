/*
 * Copyright (c) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easternblu.khub.common.model;

import androidx.annotation.Nullable;

import com.easternblu.khub.common.Common;
import com.easternblu.khub.common.api.CharakuPathConstant;
import com.easternblu.khub.common.util.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Video is an immutable object that holds the various metadata associated with a single video.
 */
public class Playlist implements Serializable {

    @Expose
    @SerializedName(CharakuPathConstant._ID)
    protected int id;

    @Expose
    @SerializedName(CharakuPathConstant._PROFILE_ID)
    protected int profileId;

    @Expose
    @SerializedName(CharakuPathConstant._TAGS)
    protected String tags;

    @Expose
    @SerializedName(CharakuPathConstant._NAME)
    protected String name;

    @Expose
    @SerializedName(CharakuPathConstant._TOTAL_TRACKS)
    protected int totalTracks;


    @Expose
    @SerializedName(CharakuPathConstant._DESCRIPTION)
    protected String description;
    
    @Expose
    @SerializedName(CharakuPathConstant._POSITION)
    protected int position;

    @Expose
    @SerializedName(CharakuPathConstant._IMAGES)
    protected CharakuImages images;
    protected String normalized = null;

    public int getId() {
        return id;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalized != null ? normalized : (normalized = Artist.parseName(null, getName(), Common.COMMA));
    }

    public Playlist() {
    }


    public CharakuImages getImages() {
        return images;
    }

    @Nullable
    public String getPosterUrl() {
        return images != null ? images.getPosterUrl() : null;
    }

    public List<String> getTags() {
        if (Strings.isNotEmpty(tags)) {
            return Strings.splitByString(tags, ';', true);
        } else {
            return new ArrayList<>();
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotalTracks() {
        return totalTracks;
    }


    public String getDescription() {
        return description;
    }


    @Nullable
    public String getTagValueByPrefix(String prefix) {
        for (String tag : getTags()) {
            if (tag.startsWith(prefix)) {
                return tag.substring(prefix.length());
            }
        }
        return null;
    }

}
