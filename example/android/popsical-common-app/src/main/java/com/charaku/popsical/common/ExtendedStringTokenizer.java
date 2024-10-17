package com.easternblu.khub.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by pan on 21/12/17.
 */

public class ExtendedStringTokenizer extends StringTokenizer {
    private Set<Character> delims = new HashSet<>();

    public ExtendedStringTokenizer(String str, String delim) {
        super(str, delim, true);
        for (char c : delim.toCharArray()) {
            delims.add(c);
        }
    }

    public String nextNonDelimToken(List<String> delims) {
        if (delims != null) {
            delims.clear();
        }
        while (hasMoreTokens()) {
            String token = nextToken();
            if (isDelim(token)) {
                if (delims != null) {
                    delims.add(token);
                }
            } else {
                return token;
            }
        }
        return null;
    }

    public boolean isDelim(char delim) {
        return delims.contains(delim);
    }

    public boolean isDelim(String delim) {
        return delim != null && delim.length() == 1 && delims.contains(delim.charAt(0));
    }

}
