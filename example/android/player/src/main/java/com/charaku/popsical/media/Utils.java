package com.easternblu.khub.media;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class Utils {


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


    /**
     * Interface that accept a non-null object and return another
     *
     * @param <A>
     * @param <R>
     */
    interface NotNull<A, R> extends Lambda<A, R> {
        @Override
        public R invoke(@NonNull A a);
    }

    /**
     * A generic interface that accept 1 input object and return another output object
     *
     * @param <A>
     * @param <R>
     */
    interface Lambda<A, R> {
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


    public static abstract class OkHttpLogger implements Interceptor, HttpLoggingInterceptor.Logger {

        private final String TAG;
        protected HttpLoggingInterceptor interceptor;


        public OkHttpLogger(String tag) {
            TAG = tag;
            interceptor = new HttpLoggingInterceptor(this);
        }

        public HttpLoggingInterceptor.Level getLevel() {
            return interceptor.getLevel();
        }

        public OkHttpLogger setLevel(HttpLoggingInterceptor.Level level) {
            interceptor.setLevel(level);
            return this;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response;
            if (isEnabled()) {
                response = interceptor.intercept(chain);
                boolean isNetworkResponse = response.networkResponse() != null;
                boolean isCacheResponse = response.cacheResponse() != null;
                log((isNetworkResponse ? "[NETWORK]" : "") + (isCacheResponse ? "[CACHE]" : ""));
                onPostIntercept(response, isNetworkResponse, isCacheResponse);
            } else {
                response = chain.proceed(chain.request());
            }
            return response;
        }

        protected void onPostIntercept(Response response, boolean network, boolean cache) {

        }

        public abstract boolean isEnabled();

        @Override
        public void log(String message) {
            Timber.d(message);
        }
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


    public static boolean isEquals(String a, String b, boolean ignoreCase) {
        if (a == null || b == null) {
            return false;
        } else {
            return (ignoreCase) ? a.equalsIgnoreCase(b) : a.equals(b);
        }
    }


}

