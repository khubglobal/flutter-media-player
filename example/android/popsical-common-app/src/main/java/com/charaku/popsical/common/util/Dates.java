package com.easternblu.khub.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

/**
 * Created by yatpanng on 17/2/17.
 */

public class Dates {
    public static final String TAG = Dates.class.getSimpleName();

    public static final String PATTERN_YYMMDD = "yyMMdd";

    public static final String PATTERN_YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";
    public static final String PATTERN_HH_MM_SS_SSS = "HH:mm:ss.SSS";
    public static final String PATTERN_HH_MM_SS = "HH:mm:ss";

    public static final String PATTERN_RFC_2616 = "EEE, d MMM yyyy HH:mm:ss Z";


    public static long getNow() {
        return System.currentTimeMillis();
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
     * Convert string to long
     *
     * @param pattern
     * @param dateString
     * @return
     */
    public static long parse(String pattern, String dateString, long defaultDate) {
        try {
            return new SimpleDateFormat(pattern).parse(dateString).getTime();
        } catch (ParseException e) {
            return defaultDate;
        }
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
     * Set the time of the calendar object using the date and time value of the string
     * the calendar's timezone value remains unchanged
     * <p>
     * Assuming the patten is in english
     *
     * @param pattern
     * @param dateTimeString
     * @param cal
     */
    public static void setCalendarTime(String pattern, String dateTimeString, Calendar cal) {
        setCalendarTime(Locale.ENGLISH, pattern, dateTimeString, cal);
    }

    /**
     * Set the time of the calendar object using the date and time value of the string
     * the calendar's timezone value remains unchanged
     *
     * @param l              The locale of the dateTimeString
     * @param patten
     * @param dateTimeString
     * @param cal
     */
    public static void setCalendarTime(Locale l, String patten, String dateTimeString, Calendar cal) {
        try {
            Date date = new SimpleDateFormat(patten, l).parse(dateTimeString);
            cal.setTime(date);
        } catch (ParseException e) {
            Timber.e(e);
        }
    }


    /**
     * A easy way to define duration
     */
    public static class Duration {


        @Retention(RetentionPolicy.SOURCE)
        @IntDef({Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND})
        protected @interface DurationUnit {
        }

        long milliseconds = 0;

        public Duration() {
        }

        public Duration(long milliseconds) {
            this.milliseconds = milliseconds;
        }


        public Duration addSeconds(int n) {
            return addMilliSeconds(n * 1000);
        }

        public Duration addMinutes(int n) {
            return addSeconds(n * 60);
        }

        public Duration addHours(int n) {
            return addMinutes(n * 60);
        }

        public Duration addMilliSeconds(long n) {
            this.milliseconds += n;
            return this;
        }

        public long getMilliseconds() {
            return milliseconds;
        }

        public int getSeconds() {
            return (int) (milliseconds / 1000);
        }


        /**
         * Return the integer value of the unit HOUR, MINUTE, SECOND, MILLISECOND
         * <p>
         * so if milliseconds = (2*60*1000) + 30*1000 (i.e. 2min 30sec)
         * <p>
         * then getTimeValues(Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND) will return {0, 2, 30}
         *
         * @param units
         * @return
         */
        public long[] getTimeValues(@DurationUnit int... units) {

            long[] values = new long[units.length];
            long time = milliseconds;

            // we loop it from the biggest unit to the smallest
            // subtracting time as we loop
            for (int j : new int[]{Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND}) {
                for (int i = 0; i < units.length; i++) {
                    long v = 0;
                    @DurationUnit
                    int unit = units[i];
                    if (unit != j) {
                        continue;
                    }
                    switch (unit) {
                        case Calendar.HOUR:
                            v = time / (60 * 60 * 1000);
                            values[i] = v;
                            time -= (v * 60 * 60 * 1000);
                            break;
                        case Calendar.MINUTE:
                            v = time / (60 * 1000);
                            values[i] = v;
                            time -= (v * 60 * 1000);
                            break;
                        case Calendar.SECOND:
                            v = time / (1000);
                            values[i] = v;
                            time -= (v * 1000);
                            break;
                        case Calendar.MILLISECOND:
                            v = time;
                            values[i] = v;
                            time -= v;
                            break;
                    }
                }
            }
            return values;
        }

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
            result += Strings.format("%02d:", hours);
        }

        result += Strings.format("%02d:%02d", min, sec);
        if (includesMilisec) {
            // mm:ss.SSS
            result += Strings.format(".%03d", ms);
        }
        return result;
    }

}
