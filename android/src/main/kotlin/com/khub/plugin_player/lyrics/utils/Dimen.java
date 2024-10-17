package com.khub.plugin_player.lyrics.utils;


import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.DimenRes;
import android.util.TypedValue;

/**
 * Created by pan on 23/3/15.
 */
public class Dimen {
    public static int dpToPx(Context ctx, float dp) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static int dimenValueToPx(Context ctx, @DimenRes int dimenValue) {
        Resources r = ctx.getResources();
        return r.getDimensionPixelSize(dimenValue);
    }


    public static int[] fromDpToPx(Context ctx, int... dps) {
        int[] px = new int[dps.length];
        Resources r = ctx.getResources();
        for (int i = 0; i < dps.length; i++) {
            px[i] = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps[i], r.getDisplayMetrics());
        }
        return px;
    }


    public static float[] fromDpToPx(Context ctx, float... dps) {
        float[] px = new float[dps.length];
        Resources r = ctx.getResources();
        for (int i = 0; i < dps.length; i++) {
            px[i] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps[i], r.getDisplayMetrics());
        }
        return px;
    }


    public static int[] fromDimenValuesToPx(Context ctx, int... dimenValues) {
        int[] px = new int[dimenValues.length];
        Resources r = ctx.getResources();
        for (int i = 0; i < dimenValues.length; i++) {
            px[i] = r.getDimensionPixelSize(dimenValues[i]);
        }
        return px;
    }


    public static float[] fromDimenValuesToFloat(Context ctx, int... dimenValues) {
        float[] floats = new float[dimenValues.length];
        Resources r = ctx.getResources();
        for (int i = 0; i < dimenValues.length; i++) {
            int resId = dimenValues[i];
            TypedValue typedValue = new TypedValue();
            r.getValue(resId, typedValue, true);
            floats[i] = typedValue.getFloat();
        }
        return floats;
    }
}
