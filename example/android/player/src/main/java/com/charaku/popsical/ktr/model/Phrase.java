package com.easternblu.khub.ktr.model;

import android.os.Build;


import com.easternblu.khub.ktr.FuriganaStringBuilder;
import com.easternblu.khub.ktr.view.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This is used to model one or more "words" inside a lyrics that can be animated with constant speed (actual singing speed should only varies in a unnoticable level)
 * <p>
 * It can be one word or multiple. Even if it is one phrase per word. The user should use {@link com.easternblu.khub.ktr.PhraseJoiner} after the conversion to simplify the actual phrases
 * Created by pan on 18/5/17.
 */
public class Phrase {

    public static final String TAG = Phrase.class.getSimpleName();
    @Expose
    @SerializedName("text")
    protected String text;

    @Expose
    @SerializedName("offset")
    public long offset;

    @Expose
    @SerializedName("duration")
    public long duration;

    @Expose
    @SerializedName("alphabet_count")
    protected int alphabetCount = 0;

    @Expose
    @SerializedName("ideographic_count")
    protected int ideographicCount = 0;


    public Phrase() {
    }

    /**
     * @param text     Text of this phrase. (supports string from {@link FuriganaStringBuilder})
     * @param offset   The wait time between {@link Line#getStartTime()} and the start animation time of this phrase
     *                 So first phrase of a {@link Line} should be short
     * @param duration The duration this phrase should be sung for
     */
    public Phrase(String text, long offset, long duration) {
        this.offset = offset;
        this.duration = duration;
        setText(text);
    }


    public boolean isAlphabetic() {
        return alphabetCount >= ideographicCount;
    }

    public boolean isIdeographic() {
        return ideographicCount > alphabetCount;
    }


    public String shortForm() {
        return Utils.format("t=%1$s,o=%2$s,d=%3$s", text, String.valueOf(offset), String.valueOf(duration));
    }

    public String getText() {
        return text;
    }

    public void appendText(String text) {
        setText(this.text + text);
    }

    public void setText(String text) {
        setText(text, true);
    }

    public void setText(String text, boolean recalculateCharacterTypes) {
        this.text = text;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && recalculateCharacterTypes) {
            alphabetCount = 0;
            ideographicCount = 0;
            if (text != null) {
                char[] charArray = text.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    int codePoint = Character.codePointAt(charArray, i);
                    if (Character.isIdeographic(codePoint)) {
                        ideographicCount++;
                    } else if (Character.isLetterOrDigit(codePoint)) {
                        alphabetCount++;
                    }
                }
                // Timber.d("[" + text + "] alphabetCount = " + alphabetCount + " ideographicCount = " + ideographicCount);
            }
        }
    }

    public int getAlphabetCount() {
        return alphabetCount;
    }

    public int getIdeographicCount() {
        return ideographicCount;
    }
}
