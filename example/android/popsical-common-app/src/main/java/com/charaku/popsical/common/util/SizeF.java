package com.easternblu.khub.common.util;

import android.graphics.Point;
import android.graphics.PointF;

public class SizeF extends PointF {
    public SizeF(float width, float height) {
        super(width, height);
    }

    public float getWidth() {
        return x;
    }

    public float getHeight() {
        return y;
    }
}
