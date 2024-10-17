package com.khub.plugin_player.lyrics.utils;

import com.easternblu.khub.common.util.Strings;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pan on 8/5/17.
 */

public class SpannableStrings {

    private static final String TAG = SpannableStrings.class.getSimpleName();


    @NonNull
    public static List<SubString> extractSections(StringBuilder sb, String start, String end) {
        int i = 0;
        List<SubString> subStrings = new ArrayList<>();
        int startPos = -1, endPos = -1;
        while (i > -1 && i < sb.length()) {
            if (startPos == -1) {
                if (isString(i, sb, start)) {
                    startPos = i;
                    d(sb + ":" + sb.substring(startPos, startPos + start.length()) + " = " + start);
                    i += start.length();
                } else {
                    i++;
                }
            } else {
                if (isString(i, sb, end)) {
                    endPos = i;
                    d(sb + ":" + sb.substring(endPos, endPos + end.length()) + " = " + end);
                    i += end.length();
                } else {
                    i++;
                }
            }

            if (endPos > startPos) {
                d("startPos = " + startPos + ", endPos = " + endPos);
                sb.delete(startPos, startPos + start.length());
                endPos -= start.length();
                sb.delete(endPos, endPos + end.length());
                i -= (start.length() + end.length() + 1);
                d("i = " + i + ", extracted " + sb);
                subStrings.add(new SubString(startPos, endPos));
                startPos = -1;
                endPos = -1;
            }
        }
        return subStrings;
    }

    private static void d(String msg) {
        // System.out.println(msg);
    }

    private static boolean isString(int offset, StringBuilder sb, String string) {
        if (Strings.isNullOrEmpty(string))
            throw new IllegalArgumentException("String cannot be null or empty");

        int j = 0;
        for (int i = offset; i < (offset + string.length()); i++) {
            if (sb.charAt(i) == string.charAt(j)) {
                j++;
                continue;
            } else {
                return false;
            }
        }
        return true;
    }


    public static class SubString {
        public int startInclusive = 0;
        public int endExclusive = 0;

        public SubString(int startInclusive, int endExclusive) {
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SubString subString = (SubString) o;

            if (startInclusive != subString.startInclusive) return false;
            return endExclusive == subString.endExclusive;

        }

        @Override
        public int hashCode() {
            int result = startInclusive;
            result = 31 * result + endExclusive;
            return result;
        }

        @Override
        public String toString() {
            return "SubString{" +
                    "startInclusive=" + startInclusive +
                    ", endExclusive=" + endExclusive +
                    '}';
        }
    }
}
