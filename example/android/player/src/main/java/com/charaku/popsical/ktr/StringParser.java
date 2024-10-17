package com.easternblu.khub.ktr;

public class StringParser {
    public final String TAG = this.getClass().getSimpleName();
    private String text;
    private int offset;

    public StringParser(String text) {
        this(text, 0);
    }

    public StringParser(String text, int offset) {
        this.text = text;
        this.offset = offset;
    }


    /**
     * return the substring that is between from and to
     *
     * @param from
     * @param to
     * @return
     */
    public String popSubstring(String from, String to) {
        if (offset >= text.length() - 1) {
            return null;
        }
        int fromPos = text.indexOf(from, offset);
        int endPos = -1;
        if (fromPos >= 0) {
            endPos = text.indexOf(to, fromPos);
        }
        if (endPos > fromPos) {
            offset = endPos;
            return text.substring(fromPos + from.length(), endPos);
        } else {
            return null;
        }
    }


    public String popFrom(String from) {
        if (offset >= text.length() - 1) {
            return null;
        }
        int fromPos = text.indexOf(from, offset);
        int endPos = text.length();

        if (endPos > fromPos) {
            offset = endPos;
            return text.substring(fromPos + from.length(), endPos);
        } else {
            return null;
        }
    }


    public int getOffset() {
        return offset;
    }

    public class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}
