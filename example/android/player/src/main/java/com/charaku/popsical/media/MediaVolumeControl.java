package com.easternblu.khub.media;


import com.easternblu.khub.media.model.StereoVolume;

/**
 * A interface that abstract the audio control aspect of a media player. Can be referenced by a UI class
 * to abstract and delegate the actual changing functions
 */
public interface MediaVolumeControl {

    void setPitch(float pitch);

    void setSpeed(float speed);

    StereoVolume getStereoVolume();

    void setStereoVolume(StereoVolume stereoVolume);

    void setMusicVolume(float musicVolume);

    float getMusicVolume();

}
