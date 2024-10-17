package com.easternblu.khub.ktr.view;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * Copyright 2016 Evander Palacios
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Utils {

    // public static final String HALF_SPACE = "\u200A";

    /**
     * Convenience method to convert scale-independent pixel(sp) value
     * into device display specific pixel value.
     *
     * @param context Context to access device specific display metrics
     * @param sp      scale independent pixel value
     * @return device specific pixel value.
     */
    public static int spToPx(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }


    public static int dpToPx(Context ctx, float dp) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static float pxToDp(Context ctx, int px) {
        Resources r = ctx.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    /**
     * just adjust the padding by
     *
     * @param view
     * @param deltaLeft
     * @param deltaTop
     * @param deltaRight
     * @param deltaBottom
     */
    public static void adjustPaddingBy(View view, float deltaLeft, float deltaTop, float deltaRight, float deltaBottom) {
        view.setPadding(
                view.getPaddingLeft() + Math.round(deltaLeft),
                view.getPaddingTop() + Math.round(deltaTop),
                view.getPaddingRight() + Math.round(deltaRight),
                view.getPaddingBottom() + Math.round(deltaBottom));
    }


    public static void adjustMarginsBy(View view, float deltaLeft, float deltaTop, float deltaRight, float deltaBottom) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.leftMargin += deltaLeft;
        marginLayoutParams.topMargin += deltaTop;
        marginLayoutParams.rightMargin += deltaRight;
        marginLayoutParams.bottomMargin += deltaBottom;
        view.setLayoutParams(marginLayoutParams);
    }


    public static void setPaddings(View v, Integer padding) {
        setPaddings(v, padding, padding, padding, padding);
    }

    public static void setPaddings(View v, Integer leftPx, Integer topPx, Integer rightPx, Integer bottomPx) {
        int l = (leftPx == null) ? v.getPaddingLeft() : leftPx;
        int t = (topPx == null) ? v.getPaddingTop() : topPx;
        int r = (rightPx == null) ? v.getPaddingRight() : rightPx;
        int b = (bottomPx == null) ? v.getPaddingBottom() : bottomPx;
        v.setPadding(l, t, r, b);
    }


    public static boolean isNotNullAndEquals(String str1, String str2) {
        return str1 != null && str2 != null && str1.equals(str2);
    }


    /**
     * text to int
     *
     * @param text         int in the form of string
     * @param defaultValue if something fails, it will return defaultValue
     * @return the int value
     */
    public static int parseInt(String text, int defaultValue) {
        try {
            return text == null ? defaultValue : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    /**
     * Text to long
     *
     * @param text         long in the form of string
     * @param defaultValue if something fails, it will return defaultValue
     * @return the long value
     */
    public static long parseLong(String text, long defaultValue) {
        try {
            return text == null ? defaultValue : Long.parseLong(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float parseFloat(String text, float defaultValue) {
        try {
            return text == null ? defaultValue : Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    public static double parseDouble(String text, double defaultValue) {
        try {
            return text == null ? defaultValue : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static double roundHalfUp(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }


    public static long getNow() {
        return System.currentTimeMillis();
    }


    /**
     * Invoke "let" callback if object is not null
     *
     * @param object
     * @param let
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B let(A object, NotNull<A, B> let) {
        if (object != null) {
            return let.invoke(object);
        } else {
            return null;
        }
    }

    public static String getFirstNotNull(String... args) {
        for (String arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }


    /**
     * Interface that accept a non-null object and return another
     *
     * @param <A>
     * @param <R>
     */
    public interface NotNull<A, R> extends Lambda<A, R> {
        @Override
        public R invoke(@NonNull A a);
    }

    /**
     * A generic interface that accept 1 input object and return another output object
     *
     * @param <A>
     * @param <R>
     */
    public interface Lambda<A, R> {
        public R invoke(A a);
    }


    /**
     * @param collection
     * @param value
     * @return
     */
    public static boolean isAllEqualsTo(Collection<String> collection, String value) {
        if (collection != null && !collection.isEmpty()) {
            for (String element : collection) {
                if (element != null && value != null && !value.equals(element)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * Convert the time in ms using the pattern provided
     * <p>
     * Assuming default locale (english) and local timezone
     *
     * @param pattern
     * @param epochMs
     * @return
     */
    public static String toString(String pattern, long epochMs) {
        return toString(Locale.ENGLISH, pattern, epochMs, TimeZone.getDefault());
    }


    /**
     * Convert the time in ms using the pattern provided
     *
     * @param l
     * @param pattern
     * @param epochMs
     * @param timeZone
     * @return
     */
    public static String toString(Locale l, String pattern, long epochMs, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, l);
        sdf.setTimeZone(timeZone);
        return sdf.format(epochMs);
    }


    /**
     * Convert a string in "mm:ss.SSS" or "mm:ss" format to a long timestamp
     * <p>
     * Also cater for "hh:mm:ss.SSS" or "hh:mm:ss"
     *
     * @param text
     * @param defaultValue
     * @return
     */
    public static long toTimestamp(String text, long defaultValue) {
        try {
            String format, defaultText;
            int colonCount = 0;
            for (char c : text.toCharArray()) {
                if (c == ':') {
                    colonCount++;
                }
            }

            if (text.indexOf('.') > 0) {
                format = "mm:ss.SSS";
                defaultText = "00:00.000";
            } else {
                format = "mm:ss";
                defaultText = "00:00";
            }
            if (colonCount == 1) {
                // default
            } else if (colonCount == 2) {
                format = "hh:" + format;
                defaultText = "00:" + defaultText;
            } else {
                throw new IllegalArgumentException("Must be in the form of mm:ss.SSS or hh:mm:ss.SSS");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf.parse(text).getTime() - sdf.parse(defaultText).getTime();
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    /**
     * @param timestamp
     * @return
     */
    public static String toTimeText(long timestamp) {
        return toTimeText(timestamp, true);
    }

    /**
     * @param timestamp
     * @param includesMilisec
     * @return
     */
    public static String toTimeText(long timestamp, boolean includesMilisec) {
        return toTimeText(timestamp, includesMilisec, false);
    }

    /**
     * Convert a timestamp in long to mm:ss.SSS
     *
     * @param timestamp
     * @param includesMilisec
     * @param showHours
     * @return
     */
    public static String toTimeText(long timestamp, boolean includesMilisec, boolean showHours) {
        long sec = timestamp / 1000;
        long ms = timestamp % 1000;
        long min = sec / 60;
        sec -= (min * 60);

        String result = "";
        if (showHours) {
            long hours = min / 60;
            min = min % 60;
            result += format("%02d:", hours);
        }

        result += format("%02d:%02d", min, sec);
        if (includesMilisec) {
            // mm:ss.SSS
            result += format(".%03d", ms);
        }
        return result;
    }


    /**
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        return String.format(Locale.ENGLISH, format, args);
    }


    /**
     * return the Throwable that is the most likely outcome of the Exception
     *
     * @param t
     * @return
     */
    @NonNull
    public static Throwable findHighLevelReadableError(@NonNull Throwable t) {
        Throwable topMost = t;
        while (t != null) {
            if (isNotEmpty(t.getMessage())) {
                return t;
            }
            t = t.getCause();
        }
        return topMost;
    }

    public static boolean isNotEmpty(String message) {
        return message != null && !message.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }


    /**
     * return the Throwable that is the most likely original cause of the Exception
     *
     * @param t
     * @return
     */
    public static Throwable findLowLevelReadableError(Throwable t) {
        while (t != null) {
            if (isNotEmpty(t.getMessage()) && t.getCause() == null) {
                return t;
            }
            t = t.getCause();
        }
        return t;
    }


    /**
     * @param objects
     * @param delim
     * @param converter
     * @param <O>
     * @return
     */
    public static <O> String toString(Collection<O> objects, String delim, Lambda<O, String> converter) {
        if (objects != null) {
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (O object : objects) {
                if (i > 0) {
                    sb.append(delim);
                }
                sb.append(converter.invoke(object));
                i++;
            }
            return sb.toString();
        } else {
            return null;
        }
    }


    /**
     * Convert the object to JSON format For exposedOnly, set this to true,
     * Otherwise all the objects member variables (public/protected/private)
     * will be part of the json and it can get dangerous result json can also be
     * very big.
     *
     * @param obj
     * @param exposedOnly if true then only @Expose variables will be used (ideally this
     *                    should always be true)
     * @return
     */
    public static String toString(Object obj, boolean exposedOnly) {
        return toString(obj, exposedOnly, false);
    }

    public static String toString(Object obj, boolean exposedOnly, boolean prettyPrint) {
        return toString(obj, new DefaultModifier(exposedOnly, prettyPrint));
    }


    public static interface GsonBuilderModifier {
        public GsonBuilder modify(GsonBuilder gsBuilder);
    }

    public static class DefaultModifier implements GsonBuilderModifier {

        final boolean exposedOnly, prettyPrint;

        public DefaultModifier(boolean exposedOnly) {
            this(exposedOnly, false);
        }

        public DefaultModifier(boolean exposedOnly, boolean prettyPrint) {
            super();
            this.exposedOnly = exposedOnly;
            this.prettyPrint = prettyPrint;
        }

        @Override
        public GsonBuilder modify(GsonBuilder gsBuilder) {
            if (exposedOnly)
                gsBuilder = gsBuilder.excludeFieldsWithoutExposeAnnotation();


            if (prettyPrint)
                gsBuilder = gsBuilder.setPrettyPrinting();
            return gsBuilder;
        }
    }

    public static String toString(Object obj, GsonBuilderModifier builderModifier) {

        if (obj == null)
            return null;

        GsonBuilder gsBuilder = new GsonBuilder();
        if (builderModifier != null)
            gsBuilder = builderModifier.modify(gsBuilder);

        Gson gs = gsBuilder.create();
        String json = gs.toJson(obj);
        return json;
    }


    /**
     * Split a string based on a string delim
     *
     * @param originalText
     * @param delims
     * @param trimToken
     * @return
     */
    public static List<String> splitByString(@Nullable String originalText, char[] delims, boolean trimToken) {
        List<String> tokens = new ArrayList<>();
        if (originalText == null)
            return tokens;

        String allDelims = new String(delims);
        StringTokenizer stringTokenizer = new StringTokenizer(originalText);
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken(allDelims);
            if (token != null)
                tokens.add(trimToken ? token.trim() : token);
        }
        return tokens;
    }


    /**
     * Split a string based on a string delim
     *
     * @param originalText
     * @param delim
     * @param trimToken
     * @return
     */
    public static List<String> splitByString(@Nullable String originalText, char delim, boolean trimToken) {
        return splitByString(originalText, new char[]{delim}, trimToken);
    }


    /**
     * Break down a string by chars to list string
     *
     * @return
     */
    public static List<String> splitByChars(@Nullable String originalText) {
        List<String> charsList = new ArrayList<>();
        if (originalText == null) {
            return charsList;
        } else {
            char[] chars = originalText.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                charsList.add(String.valueOf(chars[i]));
            }
            return charsList;
        }
    }


    public static void setLeftMargin(View v, int px) {
        setMargins(v, px, null, null, null);
    }

    public static void setTopMargin(View v, int px) {
        setMargins(v, null, px, null, null);
    }

    public static void setRightMargin(View v, int px) {
        setMargins(v, null, null, px, null);
    }

    public static void setBottomMargin(View v, int px) {
        setMargins(v, null, null, null, px);
    }

    public static void setMargins(View v, Integer leftPx, Integer topPx, Integer rightPx, Integer bottomPx) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;

        if (leftPx != null) {
            marginLayoutParams.leftMargin = leftPx;
        }
        if (topPx != null) {
            marginLayoutParams.topMargin = topPx;
        }
        if (rightPx != null) {
            marginLayoutParams.rightMargin = rightPx;
        }
        if (bottomPx != null) {
            marginLayoutParams.bottomMargin = bottomPx;
        }
        v.setLayoutParams(marginLayoutParams);
    }


    public static String trimLeft(@NonNull String str) {
        int i = 0;
        while (true) {
            if (i >= str.length()) {
                break;
            }
            if (Character.isSpaceChar(str.charAt(i))) {
                i++;
            } else {
                break;
            }
        }
        return str.substring(i);
    }


    public static String trimRight(@NonNull String str) {
        int i = str.length() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            if (Character.isSpaceChar(str.charAt(i))) {
                i--;
            } else {
                break;
            }
        }
        return str.substring(0, i);
    }
}
