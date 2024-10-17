package com.easternblu.khub.ktr.model;


import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easternblu.khub.ktr.LRCMetaNames;
import com.easternblu.khub.ktr.view.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * A class to model the LRC file (highest level)
 * <p>
 * headers values can be retrieved (case insensitive) via {@link #getMeta(String)}
 * <p>
 * Each line is model with {@link Line}<p>
 * Each line has multiple {@link Phrase}<p>
 * A phrase has a {@link Phrase#offset} (i.e. start time) and {@link Phrase#duration} (i.e can used to determine the endtime)
 * <p>
 * <p>
 * Created by pan on 18/5/17.
 */
public class Lines implements LRCMetaNames {
    public static final String TAG = Lines.class.getSimpleName();

    private String tag;

    @Expose
    @SerializedName("meta")
    private Map<String, String> meta = new HashMap<>();

    @Expose
    @SerializedName("lines")
    protected List<Line> lines = new ArrayList<>();

    public void sort() {
        if (lines == null) {
            return;
        }
        Collections.sort(lines, new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return (o1.start == o2.start) ? 0 : ((o1.start > o2.start) ? 1 : -1);
            }
        });

        for (Line line : lines) {
            line.sort();
        }
    }


    static String getSingerColorMetaName(String singer) {
        return String.format(X_POPSICAL_SINGER_COLOR_FORMAT, singer).toLowerCase();
    }

    @ColorInt
    public int getDefaultSingerColor() {
        return toColor(getMeta(X_POPSICAL_SINGER_COLOR_DEFAULT, Line.DEFAULT_SINGER_COLOR_STRING));
    }

    @ColorInt
    public int getSingerColor(String singer) {
        String colorHex;
        int c = toColor(colorHex = getMeta(getSingerColorMetaName(singer)));
        // Timber.d("getSingerColor: singer=" + singer + " c=" + c + " colorHex=" + colorHex);
        return c;
    }

    @ColorInt
    private int toColor(String colorHex) {
        String color = colorHex;
        try {
            if (color == null) {
                throw new NullPointerException("No color");
            }
            return Color.parseColor(color);
        } catch (Throwable t) {
            Timber.w("Cannot convert " + colorHex + ", error = " + t.getMessage());
            return Line.DEFAULT_SINGER_COLOR;
        }
    }


    public long getTotalDuration() {
        Line lastLine = getLastLine();
        Phrase phrase = lastLine != null ? lastLine.getLast() : null;
        if (lastLine != null && phrase != null) {
            return lastLine.start + (phrase.offset + phrase.duration);
        } else {
            return 0;
        }
    }

    public void shift(int delta) {
        for (Line line : lines) {
            line.shift(delta);
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public synchronized int size() {
        return lines.size();
    }

    public synchronized void add(Line line) {
        lines.add(line);
    }

    public synchronized void add(int index, Line line) {
        lines.add(index, line);
    }

    public synchronized Line getLine(int index) {
        return lines.get(index);
    }


    public void putMeta(String name, String value) {
        meta.put(name.toLowerCase(), value);
    }

    public void removeMeta(String name) {
        if (name != null) {
            meta.remove(name.toLowerCase());
        }
    }

    public String getMeta(String name) {
        return getMeta(name, null);
    }

    public String getMeta(String name, String defaultValue) {
        String value = meta.get(name.toLowerCase());
        return Utils.isNotEmpty(value) ? value : defaultValue;
    }

    public boolean hasMeta(String name) {
        return meta.containsKey(name.toLowerCase());
    }


    public Set<String> getMetaNames() {
        return meta.keySet();
    }

    @Nullable
    public synchronized Line getFirstLine() {
        return size() > 0 ? lines.get(0) : null;
    }

    @Nullable
    public synchronized Line getLastLine() {
        return size() > 0 ? lines.get(size() - 1) : null;
    }

    public int getOffset() {
        return getInt(OFFSET, 0);
    }

    public int getPreviewOffset() {
        return getInt(X_POPSICAL_PREVIEW_OFFSET, 10000);
    }

    public float getFontScale() {
        return getFloat(X_POPSICAL_FONT_SCALE, 1f);
    }

    @NonNull
    public String getLang() {
        return getMeta(X_POPSICAL_LANG, "en");
    }

    @Nullable
    public String getTitle() {
        return getMeta(TITLE);
    }

    @Nullable
    public String getArtist() {
        return getMeta(ARTIST);
    }

    @Nullable
    public String getAlbum() {
        return getMeta(ALBUM);
    }

    @Nullable
    public String getBy() {
        return getMeta(BY);
    }

    @Nullable
    public String getLengthRaw() {
        return getMeta(LENGTH);
    }


    public long getLength() {
        return Utils.toTimestamp(getLengthRaw(), -1);
    }


    public int getInt(String name, int defaultValue) {
        String value = getMeta(name);
        return Utils.parseInt(value, defaultValue);
    }

    public long getLong(String name, long defaultValue) {
        String value = getMeta(name);
        return Utils.parseLong(value, defaultValue);
    }

    public float getFloat(String name, float defaultValue) {
        String value = getMeta(name);
        return Utils.parseFloat(value, defaultValue);
    }

    public double getDouble(String name, double defaultValue) {
        String value = getMeta(name);
        return Utils.parseDouble(value, defaultValue);
    }

    @Nullable
    @ColorInt
    public Integer getBackgroundColor() {
        String color = getMeta(X_POPSICAL_BG_COLOR);
        return color == null ? null : Color.parseColor(color);
    }

    /**
     * Margin ratio as per video screen size (left, top, right, bottom)
     * <p>
     * left 0.1 means 10% of width
     *
     * @return
     */
    public float[] getMargins() {
        return toFloatArray(parseElements(getMeta(X_POPSICAL_MARGINS, "0.025f,0.15f,0.025f,0.15f")));
    }


    /**
     * @param tokens
     * @return
     */
    private float[] toFloatArray(List<String> tokens) {
        if (tokens != null) {
            float[] values = new float[tokens.size()];
            for (int i = 0; i < tokens.size(); i++) {
                Float temp;
                try {
                    temp = new Float(tokens.get(i));
                } catch (NumberFormatException nfe) {
                    temp = 0f;
                }
                values[i] = temp.floatValue();
            }
            return values;
        } else {
            return null;
        }
    }

    /**
     * @param rawValue
     * @return
     */
    private List<String> parseElements(String rawValue) {
        if (rawValue == null) {
            return null;
        } else {
            List<String> tokens = new ArrayList<>();
            String[] elements = rawValue.split(",");
            if (elements != null) {
                for (String element : elements) {
                    tokens.add(element);
                }
            }
            return tokens;
        }
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
        sort();
    }

    public List<Line> getLines() {
        return lines;
    }
}
