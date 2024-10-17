package com.easternblu.khub.common.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

/**
 * Created by pan on 13/9/17.
 */

public class Colors {
    public static final int ALPHA = 1; // ...000001
    public static final int RED = 1 << 1; // ...00010
    public static final int GREEN = 1 << 2; // ...00100
    public static final int BLUE = 1 << 3; // ...01000


    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {ALPHA, RED, GREEN, BLUE}, flag = true)
    public @interface ColorComponent {
    }


    public static final String TAG = Colors.class.getSimpleName();

    /**
     * if it is white it will return black, vice versa
     *
     * @param color
     * @return
     */
    @ColorInt
    public static int contrastMonoChrome(@ColorInt int color) {
        return asMonoChromeColor(color) == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    /**
     * @param color
     * @return
     */
    @ColorInt
    public static int asMonoChromeColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        int colorHex = (int) (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color));
        return colorHex < 128 ? Color.BLACK : Color.WHITE;
    }


    public static String toHexString(@ColorInt int color) {
        StringBuilder str = new StringBuilder("#");
        str.append(to00Hex(android.graphics.Color.alpha(color)));
        str.append(to00Hex(android.graphics.Color.red(color)));
        str.append(to00Hex(android.graphics.Color.green(color)));
        str.append(to00Hex(android.graphics.Color.blue(color)));
        return str.toString();
    }


    private static String to00Hex(int value) {
        String hex = Integer.toHexString(value);
        while (hex != null && hex.length() < 2) {
            hex = ("0" + hex);
        }
        return hex;
    }


    /**
     * Return either {@link Color#WHITE} or {@link Color#BLACK}
     *
     * @param bitmap
     * @param region
     * @param maxPixelsUsed
     * @return
     */
    @ColorInt
    public static int getDominantMonoChromeColor(Bitmap bitmap, Rect region, long maxPixelsUsed) {
        int w = region.width(), h = region.height();
        int scale = 1;
        while ((w / scale * h / scale) > maxPixelsUsed) {
            scale++;
        }

        Bitmap resized = Bitmaps.getResizedBitmap(bitmap, region, w / scale, h / scale);
        w = resized.getWidth();
        h = resized.getHeight();
        int mostlyWhitePixels = 0;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (asMonoChromeColor(resized.getPixel(x, y)) == Color.WHITE) {
                    mostlyWhitePixels++;
                }
            }
        }
        resized.recycle();
        Timber.d("getDominantMonoChromeColor: scale = " + scale + ", mostlyWhitePixels = " + mostlyWhitePixels + " wxh=" + w + "x" + h);
        boolean mostlyWhite = mostlyWhitePixels > (w * h / 2);
        return mostlyWhite ? Color.WHITE : Color.BLACK;
    }


    /**
     * Get the color in between 2 colors
     *
     * @param from
     * @param to
     * @param fraction
     * @return
     */
    public static int getColorFraction(@ColorInt int from, @ColorInt int to, @FloatRange(from = 0, to = 1) float fraction) {
        return getColorFraction(from, to, fraction, ALPHA | RED | GREEN | BLUE);
    }


    /**
     * @param from
     * @param to
     * @param fraction
     * @param deltaColorComponent
     * @return
     */
    public static int getColorFraction(@ColorInt int from, @ColorInt int to, @FloatRange(from = 0, to = 1) float fraction, @ColorComponent int deltaColorComponent) {
        fraction = Math.min(Math.max(fraction, 0), 1);

        int alphaFrom = Color.alpha(from);
        int alphaTo = Color.alpha(to);


        int redFrom = Color.red(from);
        int redTo = Color.red(to);

        int greenFrom = Color.green(from);
        int greenTo = Color.green(to);

        int blueFrom = Color.blue(from);
        int blueTo = Color.blue(to);


        return argb(
                Math.round(alphaFrom + (alphaTo - alphaFrom) * (((deltaColorComponent & ALPHA) == ALPHA) ? fraction : 1f)),
                Math.round(redFrom + (redTo - redFrom) * (((deltaColorComponent & RED) == RED) ? fraction : 1f)),
                Math.round(greenFrom + (greenTo - greenFrom) * (((deltaColorComponent & GREEN) == GREEN) ? fraction : 1f)),
                Math.round(blueFrom + (blueTo - blueFrom) * (((deltaColorComponent & BLUE) == BLUE) ? fraction : 1f)));
    }


    /**
     * Return a color-int from red, green, blue components.
     * The alpha component is implicity 255 (fully opaque).
     * These component values should be [0..255], but there is no
     * range check performed, so if they are out of range, the
     * returned color is undefined.
     *
     * @param red   Red component [0..255] of the color
     * @param green Green component [0..255] of the color
     * @param blue  Blue component [0..255] of the color
     */
    public static int rgb(int red, int green, int blue) {
        return (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Return a color-int from alpha, red, green, blue components.
     * These component values should be [0..255], but there is no
     * range check performed, so if they are out of range, the
     * returned color is undefined.
     *
     * @param alpha Alpha component [0..255] of the color
     * @param red   Red component [0..255] of the color
     * @param green Green component [0..255] of the color
     * @param blue  Blue component [0..255] of the color
     */
    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * @param alpha Alpha component [0..255] of the color
     * @param color
     * @param alpha
     * @return
     */
    public static int setAlpha(int color, int alpha){
        return argb(
                alpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color));
    }
}
