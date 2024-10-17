package com.easternblu.khub.media.model;

/**
 * Created by yatpanng on 8/4/17.
 */
public class StereoVolume {
    public static final float MAX = 1;
    public static final float MIN = 0f;
    public static final float LEFT_DEFAULT = 1;
    public static final float RIGHT_DEFAULT = 1;
    public float left;
    public float right;

    public StereoVolume() {
        left = LEFT_DEFAULT;
        right = RIGHT_DEFAULT;
    }

    public StereoVolume(float left, float right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "StereoVolume{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
