package com.easternblu.khub.common.util;

import java.math.BigDecimal;

/**
 * Created by pan on 23/3/17.
 */

public class Maths {

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

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static double roundHalfDown(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.doubleValue();
    }


    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float roundHalfUp(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float roundHalfDown(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.floatValue();
    }


    /**
     * Calls {@link Math.pow} return float
     *
     * @param p
     * @param e
     * @return
     */
    public static float pow(float p, float e) {
        return new Double(Math.pow(p, e)).floatValue();
    }

    /**
     * Return a random int value
     *
     * @param minInclusive
     * @param maxExclusive
     * @return
     * @throws IllegalArgumentException
     */
    public static int randomInt(int minInclusive, int maxExclusive) throws IllegalArgumentException {
        if (minInclusive >= maxExclusive) {
            throw new IllegalArgumentException("minInclusive(" + minInclusive + ") must be less than maxExclusive(" + maxExclusive + ")");
        }
        return minInclusive + new Double(Math.random() * (maxExclusive - minInclusive)).intValue();
    }

    /**
     * Return a random long value
     *
     * @param minInclusive
     * @param maxExclusive
     * @return
     * @throws IllegalArgumentException
     */
    public static long randomLong(long minInclusive, long maxExclusive) throws IllegalArgumentException {
        if (minInclusive >= maxExclusive) {
            throw new IllegalArgumentException("minInclusive(" + minInclusive + ") must be less than maxExclusive(" + maxExclusive + ")");
        }
        return minInclusive + new Double(Math.random() * (maxExclusive - minInclusive)).longValue();
    }


    public static double mean(double[] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }

    public static float mean(float[] m) {
        float sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }


    public static double median(long[] m) {
        int middle = m.length / 2;
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle - 1] + m[middle]) / 2.0;
        }
    }


    public static double median(double[] m) {
        int middle = m.length / 2;
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle - 1] + m[middle]) / 2.0;
        }
    }


    public static double median(int[] m) {
        int middle = m.length / 2;
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle - 1] + m[middle]) / 2.0;
        }
    }


    public static double median(float[] m) {
        int middle = m.length / 2;
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle - 1] + m[middle]) / 2.0;
        }
    }


    public static int[] reverse(int[] array) {
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[array.length - i - 1];
            array[array.length - i - 1] = array[i];
            array[i] = temp;
        }
        return array;
    }

    public static long[] reverse(long[] array) {
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length / 2; i++) {
            long temp = array[array.length - i - 1];
            array[array.length - i - 1] = array[i];
            array[i] = temp;
        }
        return array;
    }


    public static double stddev(float... x) {
        if (x.length == 0) {
            return 0;
        } else {
            float mean = (sum(x) / x.length), nSquareStdDev = 0;
            for (int i = 0; i < x.length; i++) {
                nSquareStdDev += Math.pow(x[i] - mean, 2);
            }
            return Math.sqrt(nSquareStdDev / x.length);
        }
    }


    public static float sum(float... x) {
        float sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }

    public static int sum(int... x) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }

    public static double sum(double... x) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }


    public static long sum(long... x) {
        long sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }


}
