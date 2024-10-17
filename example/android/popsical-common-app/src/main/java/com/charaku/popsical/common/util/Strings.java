package com.easternblu.khub.common.util;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.easternblu.khub.common.Common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by yatpanng on 16/2/17.
 */

public class Strings {


    /**
     * Remove the chars that you don't want from a string so if:
     * <br/>
     * charsToKeep = {'1', '2', '3'} and text = "Hel1l2oWo3rld"
     * <br/>
     * then it will returns "HelloWorld"
     * <br/>
     *
     * @param text
     * @param charsToRemove
     * @return
     */
    public static String removeChars(@Nullable String text, char... charsToRemove) {
        if (Strings.isNullOrEmpty(text))
            return text;

        Set<Character> characterSet = new HashSet<>();
        for (char c : charsToRemove) {
            characterSet.add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (!characterSet.contains(c))
                sb.append(c);
        }
        return sb.toString();
    }


    /**
     * Retain the chars that you want from a string so if:
     * <br/>
     * charsToKeep = {'1', '2', '3'} and text = "Hel1l2oWo3rld"
     * <br/>
     * then it will return "123"
     * <br/>
     *
     * @param text
     * @param charsToKeep
     * @return
     */
    public static String retainChars(@Nullable String text, char... charsToKeep) {
        if (Strings.isNullOrEmpty(text))
            return text;

        Set<Character> characterSet = new HashSet<>();
        for (char c : charsToKeep) {
            characterSet.add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (characterSet.contains(c))
                sb.append(c);
        }
        return sb.toString();
    }


    /**
     * Similar to {@link Lists#asString(List, String, boolean, Converter)}. It just convert an array to string
     *
     * @param objects
     * @param delim
     * @param skipNull
     * @param converter
     * @param <A>
     * @return
     */
    public static <A> String toString(A[] objects, String delim, boolean skipNull, Converter<A, String> converter) {
        List<A> objectsList = new ArrayList<>();
        if (objects != null) {
            for (A object : objects) {
                objectsList.add(object);
            }
        }

        return Lists.asString(objectsList, delim, skipNull, converter);
    }

    /**
     * true if string is null or empty
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * true, if string is not empty and not null
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }


    public static Converter<String, String> STRING_CONVERTER = new Converter<String, String>() {
        @Override
        public String convert(String from) {
            return from;
        }
    };

    /**
     * Comparator to sort string alphabetically
     */
    public static final Comparator<String> ALPHABETICAL = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return -1;
            } else {
                return o1.compareTo(o2);
            }
        }
    };

    /**
     * returns a converter that converts an object to a String using toString method
     *
     * @param clazz The class object
     * @param <A>   The class
     * @return
     */
    public static <A> Converter<A, String> getToStringConverter(Class<A> clazz) {
        return new Converter<A, String>() {
            @Override
            public String convert(A from) {
                return from.toString();
            }
        };
    }


    /**
     * @param strings arguments
     * @return returns the first non null or empty string from the list of arguments provided
     */
    public static String getFirstNotNullOrEmpty(String... strings) {
        for (String string : strings) {
            if (isNotEmpty(string)) {
                return string;
            }
        }
        return null;
    }


    /**
     * @param text
     * @return returns the url encoded version of the text (assuming utf-8 encoding), returns "" if there is error
     */
    public static String urlEncodeUTF8(String text) {
        try {
            return URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    /**
     * Similar to
     *
     * @param strings
     * @return
     */
    public static String getFirstNotNull(String... strings) {
        for (String string : strings) {
            if (string != null) {
                return string;
            }
        }
        return null;
    }

    public static byte[] getSHA256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return md.digest();
        } catch (Throwable t) {
            return null;
        }
    }

    public static String getSHA256String(String text) {
        return getHex(getSHA256(text));
    }


    /**
     * @param a
     * @param b
     * @return true if, a.equals(b) and neither is null
     */
    public static boolean isNotNullAndEquals(String a, String b) {
        return a != null && b != null && a.equals(b);
    }

    /**
     * @param a
     * @param b
     * @return true if, a.equalsIgnoreCase(b) and neither is null
     */
    public static boolean isNotNullAndEqualsIgnoreCase(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Convert byte array into String using hex
     *
     * @param bytes
     * @return
     */
    @NonNull
    public static String bytesToHex(byte... bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Capitalize a string
     *
     * @param localizedMessage
     * @return
     */
    public static String capitalize(String localizedMessage) {
        if (Strings.isNotEmpty(localizedMessage) && localizedMessage.length() > 1) {
            return localizedMessage.substring(0, 1).toUpperCase() + localizedMessage.substring(1);
        }
        return localizedMessage;
    }

    /**
     * Repeat a substring x number of times
     *
     * @param text
     * @param count
     * @return Return a string concatenated with the string text itself count number of times
     */
    public static String repeat(String text, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(text);
        }
        return sb.toString();
    }


    /**
     * Format a string based on US locale (useful for API purpose where it is locale independent)
     *
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        return Strings.format(Locale.ENGLISH, format, args);
    }

    /**
     * Call {@Link String#format}
     *
     * @param l
     * @param format
     * @param args
     * @return
     */
    public static String format(Locale l, String format, Object... args) {
        return String.format(l, format, args);
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

    /**
     * Convert byte array into String as hex
     *
     * @param raw
     * @return
     */
    public static String getHex(byte... raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(hexArray[(b & 0xF0) >> 4]).append(hexArray[(b & 0x0F)]);
        }
        return hex.toString();
    }


    /**
     * Convert hex string to byte array
     *
     * @param hex
     * @return
     */
    public static byte[] getBytesFromHex(String hex) {
        if (hex == null)
            return null;
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }


    /**
     * Base64 decode some String into bytes
     *
     * @param encoded
     * @return
     */
    public static byte[] base64Decode(String encoded) {
        return Base64.decodeFast(encoded.toCharArray());
    }

    /**
     * Base64 encode some bytes
     *
     * @param raw
     * @return
     */
    public static String base64Encode(byte[] raw) {
        return Base64.encodeToString(raw, false);
    }


    /**
     * Join one or more strings into one
     *
     * @param delim
     * @param strings
     * @return
     */
    public static String join(String delim, String... strings) {
        return join(delim, true, true, strings);
    }

    /**
     * Join one or more strings into one
     *
     * @param delim
     * @param skipNull
     * @param skipEmpty
     * @param strings
     * @return
     */
    public static String join(String delim, boolean skipNull, boolean skipEmpty, String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            if (string == null && skipNull)
                continue;
            if (string != null && string.isEmpty() && skipEmpty)
                continue;
            if (sb.length() > 0)
                sb.append(delim);
            sb.append(string);
        }
        return sb.toString();
    }


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
     * Reverse a string
     *
     * @param original
     * @return
     */
    public static String reverse(String original) {
        if (original == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = original.toCharArray();
        for (int i = chars.length - 1; i > -1; i--) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    /**
     * Strip enclosing quotation marks from string
     *
     * @param inputText Input text
     * @return Text without enclosing quotation marks
     */
    public static String stripEnclosingQuotes(String inputText) {
        if (TextUtils.isEmpty(inputText) || inputText.length() < 2) {
            return inputText;
        } else if (inputText.startsWith(Common.QUOTE) && inputText.endsWith(Common.QUOTE)) {
            return inputText.substring(1, inputText.length() - 1);
        } else {
            return inputText;
        }
    }


    /**
     * Return a ratio between 0 and 1, representing the number of ideographic char in the string
     *
     * @param text
     * @return
     */
    public static float getIdeographicRatio(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int count = 0;
            int n = text.length();
            for (int i = 0; i < n; i++) {


                if (Character.isIdeographic(Character.codePointAt(text, i))) {
                    count++;
                }
            }
            return 1.0f * count / n;
        } else {
            return 0;
        }
    }

}