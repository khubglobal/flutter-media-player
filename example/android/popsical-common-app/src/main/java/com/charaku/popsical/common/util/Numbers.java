package com.easternblu.khub.common.util;

/**
 * Created by pan on 12/3/17.
 */

public class Numbers {

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
     * Text to float
     *
     * @param text         float in the form of string
     * @param defaultValue if something fails, it will return defaultValue
     * @return the float value
     */
    public static float parseFloat(String text, float defaultValue) {
        try {
            return text == null ? defaultValue : Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    /**
     * Text to double
     *
     * @param text         float in the form of string
     * @param defaultValue if something fails, it will return defaultValue
     * @return the double value
     */
    public static double parseDouble(String text, double defaultValue) {
        try {
            return text == null ? defaultValue : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


}
