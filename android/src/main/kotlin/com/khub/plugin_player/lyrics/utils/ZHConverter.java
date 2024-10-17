package com.khub.plugin_player.lyrics.utils;

import android.content.Context;
import androidx.annotation.IntDef;
import com.easternblu.khub.common.util.CloseableUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Originally from https://github.com/opentalking/java-zhconverter-read-only
 */
public class ZHConverter {

    private static final String TAG = ZHConverter.class.getSimpleName();
    private Properties charMap = new Properties();
    private Set conflictingSets = new HashSet();

    public static final int NONE = -1;
    public static final int TRADITIONAL = 0;
    public static final int SIMPLIFIED = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TRADITIONAL, SIMPLIFIED, NONE})
    protected @interface ConversionType {
    }


    private static final int NUM_OF_CONVERTERS = 2;
    private static final ZHConverter[] converters = new ZHConverter[NUM_OF_CONVERTERS];
    private static final String[] propertyFiles = new String[2];

    static {
        propertyFiles[TRADITIONAL] = "properties/zh2Hant.properties";
        propertyFiles[SIMPLIFIED] = "properties/zh2Hans.properties";
    }


    /**
     * @param converterType 0 for traditional and 1 for simplified
     * @return
     */
    public static ZHConverter getInstance(Context ctx, @ConversionType int converterType) {
        if (converterType >= 0 && converterType < NUM_OF_CONVERTERS) {

            if (converters[converterType] == null) {
                synchronized (ZHConverter.class) {
                    if (converters[converterType] == null) {
                        converters[converterType] = new ZHConverter(ctx, propertyFiles[converterType]);
                    }
                }
            }
            return converters[converterType];

        } else {
            return null;
        }
    }

    public static String convert(Context ctx, String text, @ConversionType int converterType) {
        if (converterType == NONE) {
            return text;
        } else {
            return getInstance(ctx, converterType).convert(text);
        }
    }


    private ZHConverter(Context ctx, String propertyFile) {

        InputStream is = null;
        BufferedReader reader = null;
        try {

            is = ctx.getResources().getAssets().open(propertyFile);
            reader = new BufferedReader(new InputStreamReader(is));
            charMap.load(reader);
        } catch (IOException e) {

        } finally {
            CloseableUtil.close(reader, is);
        }

        initializeHelper();
    }

    private void initializeHelper() {
        Map stringPossibilities = new HashMap();
        Iterator iter = charMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (key.length() >= 1) {

                for (int i = 0; i < (key.length()); i++) {
                    String keySubstring = key.substring(0, i + 1);
                    if (stringPossibilities.containsKey(keySubstring)) {
                        Integer integer = (Integer) (stringPossibilities.get(keySubstring));
                        stringPossibilities.put(keySubstring, new Integer(
                                integer.intValue() + 1));

                    } else {
                        stringPossibilities.put(keySubstring, new Integer(1));
                    }

                }
            }
        }

        iter = stringPossibilities.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (((Integer) (stringPossibilities.get(key))).intValue() > 1) {
                conflictingSets.add(key);
            }
        }
    }

    public String convert(String in) {
        StringBuilder outString = new StringBuilder();
        StringBuilder stackString = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            String key = "" + c;
            stackString.append(key);

            String stackStr = stackString.toString();
            if (conflictingSets.contains(stackStr)) {

            } else if (charMap.containsKey(stackStr)) {
                outString.append(charMap.get(stackStr));
                stackString.setLength(0);
            } else {
                CharSequence sequence = stackString.subSequence(0, stackString.length() - 1);
                stackString.delete(0, stackString.length() - 1);
                flushStack(outString, new StringBuilder(sequence));
            }
        }

        flushStack(outString, stackString);
        return outString.toString();
    }


    private void flushStack(StringBuilder outString, StringBuilder stackString) {
        while (stackString.length() > 0) {
            String stackStr = stackString.toString();
            if (charMap.containsKey(stackStr)) {
                outString.append(charMap.get(stackStr));
                stackString.setLength(0);

            } else {
                outString.append(String.valueOf(stackString.charAt(0)));
                stackString.delete(0, 1);
            }
        }
    }


    String parseOneChar(String c) {

        if (charMap.containsKey(c)) {
            return (String) charMap.get(c);

        }
        return c;
    }


}
