package com.easternblu.khub.ktr;


import androidx.annotation.Nullable;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.ktr.view.Utils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by pan on 8/12/17.
 */
public class LRC2KTRConverter {
    public static final String TAG = LRC2KTRConverter.class.getSimpleName();
    private static boolean DEBUG_ENABLED = false;

    // e.g.
    // A: Tales as old as time A&J:
    // A&J: Beauty and the beast
    //
    // e.g.
    // Me and my niggas tryna get it, ya bish
    // Hit the house lick: tell me, is you wit' it, ya bish?
    //
    // singer names are "A" and "A&J" each has a length of 1 and 3 respectively
    // The phrase "Hit the house lick" will not be treated as singer because length is > MAX_SINGER_NAME_LENGTH
    public static final int MAX_SINGER_NAME_LENGTH = 5;


    /**
     * Constructor for LRC2KTRConverter
     */
    public LRC2KTRConverter() {
    }

    /**
     * Core method to convert enhanced LRC to {@link Lines}
     *
     * @param from content of the enhanced LRC file
     * @return
     * @throws ConversionException
     */
    public Lines convert(String from) throws ConversionException {
        Lines lines = new Lines();
        List<String> elrcLines = Utils.splitByString(from, '\n', true);

        if (Utils.isNotEmpty(elrcLines)) {
            List<LRCLine> lrcLines = new ArrayList<>();
            for (int i = 0; i < elrcLines.size(); i++) {
                int lineNum = i + 1;
                lrcLines.add(new LRCLine(lineNum, elrcLines.get(i)));
            }
            Line previousLine = null;

            for (int i = 0; i < lrcLines.size(); i++) {
                LRCLine lrcLine = lrcLines.get(i);
                LRCLine nextLrcLine = (i < lrcLines.size() - 1) ? lrcLines.get(i + 1) : null;
                Line temp = addLine(lrcLine, lines, previousLine, nextLrcLine);
                if (temp != null) {
                    previousLine = temp;
                }
            }

        }
        lines.sort();
        lines.shift(lines.getOffset());


        for (String metaName : lines.getMetaNames()) {
            d(metaName + ": " + lines.getMeta(metaName));
        }
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.getLine(i);
            d(toTimeText(line.getStartTime()) + " ~ " + toTimeText(line.getEndTime()) + line.getFullText());
        }

        return lines;
    }

    void d(String text) {
        if (DEBUG_ENABLED) {
            Timber.d(text);
        }
    }

    /**
     * true if line starts with [mm:ss.SSS]
     *
     * @param lineNum
     * @param line
     * @return
     */
    private boolean isLyricLine(int lineNum, String line) {
        try {
            int startPos = 1, endPos;
            if (line.length() > 2 && line.startsWith("[") && (endPos = line.indexOf(']')) > 1) {
                return toTimestamp(lineNum, line.substring(startPos, endPos)) > 0;
            }
        } catch (ConversionException e) {
        }
        return false;
    }


    /**
     * Add the {@link LRCLine} object to {@link Lines}
     *
     * @param line
     * @param lines
     * @param previousLine New line might copy properties from previous line
     */
    private Line addLine(LRCLine line, Lines lines, @Nullable Line previousLine, @Nullable LRCLine nextLine) {
        if (line.isEmpty()) {
            return null;
        }

        if (line.isMeta()) {
            lines.putMeta(line.metaName, line.metaValue);
            return null;
        }

        Line currentLine;
        lines.add(currentLine = new Line(line.getStartTime()));
        currentLine.setLineNumber(lines.size());

        if (Utils.isNotEmpty(line.singer)) {
            int lineColor = lines.getSingerColor(line.singer);
            currentLine.setTextColor(lineColor);
        } else if (previousLine != null) {
            currentLine.setTextColor(previousLine.getTextColor());
        } else {
            currentLine.setTextColor(lines.getDefaultSingerColor());
        }


        for (int wordIndex = 0; wordIndex < line.words.size(); wordIndex++) {
            LRCWord word = line.words.get(wordIndex);
            long wordEndTime = -1;
            if (wordIndex < line.words.size() - 1) {
                wordEndTime = line.words.get(wordIndex + 1).startTime;
            }


            if (Utils.isNotEmpty(word.text.trim()) && wordEndTime == -1 && nextLine != null) {
                wordEndTime = nextLine.getStartTime();
            }

            if (wordEndTime == -1) {
                break;
            }

            Phrase phrase = new Phrase(word.text, word.startTime - currentLine.getStartTime(), wordEndTime - word.startTime);
            currentLine.getPhrases().add(phrase);
        }
        return currentLine;
    }

    /**
     * Convert a string in "mm:ss.SSS" format to a long timestamp
     *
     * @param lineNum
     * @param text
     * @return
     * @throws ConversionException
     */
    public static long toTimestamp(int lineNum, String text) throws ConversionException {
        if (text.length() != 9) {
            throw new ConversionException(lineNum, "Unable to convert " + text);
        }

        // mm:ss.SSS
        int min = Integer.parseInt(text.substring(0, 2));  // mm
        int sec = Integer.parseInt(text.substring(3, 5));  // ss
        int ms = Integer.parseInt(text.substring(6, 9));  // SSS
        return ms + sec * 1000 + min * 60 * 1000;
    }

    /**
     * Reverse of {@link LRC2KTRConverter#toTimestamp(int, String)}
     *
     * @param timestamp
     * @return
     */
    public static String toTimeText(long timestamp) {
        long sec = timestamp / 1000;
        long ms = timestamp % 1000;
        long min = sec / 60;
        sec -= (min * 60);

        // mm:ss.SSS
        return Utils.format("%02d:%02d.%03d", min, sec, ms);
    }

    /**
     * The parsed object for a line of LRC file
     */
    private class LRCLine {
        String singer;
        String metaName, metaValue;
        int lineNum;
        List<LRCWord> words = new ArrayList<>();

        public LRCLine(int lineNum, String line) throws ConversionException {
            this.lineNum = lineNum;
            parse(line.trim());
        }


        private boolean isMeta() {
            return metaName != null;
        }

        private boolean isEmpty() {
            return !isMeta() && Utils.isNullOrEmpty(words);
        }


        private long getStartTime() {
            return words.size() > 0 ? words.get(0).startTime : 0;
        }


        private LRCWord addWord(boolean firstWord, long time, String text) {

            if (text != null) {
                text = Utils.trimLeft(text);
            }

            // first word
            if (firstWord) {
                String[] nameAndWord = splitSingerName(text);
                if (Utils.isNotEmpty(nameAndWord[0])) {
                    this.singer = nameAndWord[0];
                }
            }
            LRCWord word;
            words.add(word = new LRCWord(time, text));
            return word;
        }


        private void parse(String line) throws ConversionException {
            try {

                StringParser parser = new StringParser(line);
                LRCWord lastWord = null;
                String timeToken = parser.popSubstring("[", "]");
                if (timeToken == null) {
                    return;
                }
                String name;
                int pos;
                if ((pos = timeToken.indexOf(':')) > 0 && Utils.parseInt(name = timeToken.substring(0, pos), -1) == -1) {
                    metaName = name;
                    metaValue = timeToken.substring(pos + 1);
                    return;
                }
                String value = parser.popSubstring("]", "<");
                if (value == null) {
                    return;
                }

                lastWord = addWord(true, toTimestamp(lineNum, timeToken), value);

                while (true) {
                    timeToken = parser.popSubstring("<", ">");
                    if (parser.getOffset() == line.length() - 1) {
                        value = "";
                    } else {
                        value = parser.popSubstring(">", "<");
                        if (value == null) {
                            String temp = parser.popFrom(">");
                            temp = (temp != null) ? temp.trim() : null;
                            value = Utils.isNotEmpty(temp) ? temp : null;
                        }
                    }
                    if (Utils.isNullOrEmpty(timeToken) || value == null) {
                        break;
                    }
                    lastWord = addWord(false, toTimestamp(lineNum, timeToken), value);
                }

                if (lastWord != null) {
                    lastWord.eol = true;
                }

            } catch (Throwable t) {
                throw new ConversionException(lineNum, t);
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "words=" + words +
                    ", lineNum=" + lineNum +
                    '}';
        }
    }


    public static String[] splitSingerName(String word) {
        int colonPos, maxNameLength = MAX_SINGER_NAME_LENGTH;
        if (word.length() >= 2 && (colonPos = word.indexOf(':')) >= 1) {
            if (colonPos <= maxNameLength) {
                return new String[]{word.substring(0, colonPos), word.substring(colonPos + 1)};
            }
        }
        return new String[]{"", word};
    }

    /**
     * A word in the LRCLine
     */
    private class LRCWord {
        long startTime;
        String text;
        boolean eol = false;


        public LRCWord(long startTime, String text) {
            this(startTime, text, false);
        }

        public LRCWord(long startTime, String text, boolean eol) {
            this.startTime = startTime;
            this.text = text;
            this.eol = eol;
        }


        @Override
        public String toString() {
            return "{" +
                    "startTime=" + startTime +
                    ", text='" + text + '\'' +
                    '}';
        }

        public boolean isEol() {
            return eol;
        }
    }

    /**
     * Custom exception for parsing error vcy
     */
    public static class ConversionException extends Exception {
        int line = -1;

        public ConversionException(int line, Throwable throwable) {
            super(throwable);
            this.line = line;
        }

        public ConversionException(int line, String s) {
            super(s);
            this.line = line;
        }

        @Override
        public String getMessage() {
            return Utils.format("At line %d, error: %s", line, super.getMessage());
        }
    }


}
