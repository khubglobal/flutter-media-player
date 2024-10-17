package com.easternblu.khub.ktr.model;


import android.graphics.Color;
import androidx.annotation.ColorInt;

import com.easternblu.khub.ktr.view.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Models a line that will be displayed in {@link com.easternblu.khub.ktr.KaraokeLyricsView}
 * Created by pan on 18/5/17.
 */

public class Line {

    public static final String DEFAULT_SINGER_COLOR_STRING = "#0000ff";

    @ColorInt
    public static final int DEFAULT_SINGER_COLOR = Color.parseColor(DEFAULT_SINGER_COLOR_STRING);

    public enum Alignment {
        LEFT, RIGHT, CENTER
    }

    @Expose
    @SerializedName("line_number")
    protected int lineNumber;

    @Expose
    @SerializedName("start")
    protected long start;

    @Expose
    @SerializedName("end")
    protected long end;

    @Expose
    @SerializedName("phrases")
    protected List<Phrase> phrases = new ArrayList<>();

    @ColorInt
    @Expose
    @SerializedName("text_color")
    protected int textColor = DEFAULT_SINGER_COLOR;

    public Line() {
    }

    public Line(long start) {
        this.start = start;
    }

    public String getFullText() {
        return Utils.toString(phrases, "", new Utils.Lambda<Phrase, String>() {
            @Override
            public String invoke(Phrase from) {
                return from.getText();
            }
        });
    }

    public void sort() {
        if (phrases == null)
            return;
        Collections.sort(phrases, new Comparator<Phrase>() {
            @Override
            public int compare(Phrase o1, Phrase o2) {
                return (o1.offset == o2.offset) ? 0 : ((o1.offset > o2.offset) ? 1 : -1);
            }
        });
    }


    @Override
    public String toString() {
        return Utils.format("[%1$s-%2$s] %3$s", Utils.toTimeText(getStartTime()), Utils.toTimeText(getEndTime()), getDisplayText());
    }

    public void shift(int delta) {
        start += delta;
        start = Math.max(start, 0);
        if (end > 0) {
            end += delta;
        }
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = Color.parseColor(textColor);
    }


    @ColorInt
    public int getTextColor() {
        return textColor;
    }

    public Phrase getFirst() {
        return Utils.isNotEmpty(phrases) ? phrases.get(0) : null;
    }


    public Phrase getLast() {
        return Utils.isNotEmpty(phrases) ? phrases.get(phrases.size() - 1) : null;
    }

    public long getStartTime() {
        return start;
    }


    public long getEndTime() {
        if (end > 0) {
            return end;
        } else {
            Phrase last = getLast();
            if (last != null) {
                return getStartTime() + last.offset + last.duration;
            } else {
                return 0;
            }
        }
    }


    public int getLineNumber() {
        return lineNumber;
    }


    public List<Phrase> getPhrases() {
        return phrases;
    }


    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    void setStart(long start) {
        this.start = start;
    }

    void setEnd(long end) {
        this.end = end;
    }

    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
    }


    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        for (Phrase phrase : phrases) {
            if (Utils.isNotEmpty(phrase.getText())) {
                sb.append('[');
                sb.append(phrase.getText());
                sb.append(']');
            } else {
                sb.append(phrase.getText());
            }
        }
        return sb.toString();
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
