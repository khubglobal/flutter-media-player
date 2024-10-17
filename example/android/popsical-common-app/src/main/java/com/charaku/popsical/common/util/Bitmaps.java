package com.easternblu.khub.common.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;


/**
 * Common methods related to bitmap
 * Created by pan on 1/3/18.
 */

public class Bitmaps {
    /**
     * Resize bitmap
     * @param bm
     * @param desWidth
     * @param desHeight
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int desWidth, int desHeight) {
        return getResizedBitmap(bm, new Rect(0, 0, bm.getWidth(), bm.getHeight()), desWidth, desHeight);
    }

    /**
     * Resize bitmap
     * @param bm
     * @param srcRegion
     * @param desWidth
     * @param desHeight
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bm, Rect srcRegion, int desWidth, int desHeight) {
        float scaleWidth = ((float) desWidth) / srcRegion.width();
        float scaleHeight = ((float) desHeight) / srcRegion.height();
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, srcRegion.left, srcRegion.top, srcRegion.width(), srcRegion.height(), matrix, false);
        return resizedBitmap;
    }
}
