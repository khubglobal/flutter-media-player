package com.easternblu.khub.ktr.view;

import android.graphics.Typeface;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * Created by pan on 25/1/18.
 */
public interface StrokeTextView {

    @ColorInt
    public int getCurrentTextColor();

    @ColorInt
    public int getStrokeColor();

    public void setStrokeColor(@ColorInt int strokeColor);

    public float getStrokeWidthPx();

    public void setStrokeWidthPx(float strokeWidthPx);

    public void setAndApplyText(String text);

    public void setTextSizePx(int textSizePx);

    public void setExtraFontAccentPx(int negativePx);

    public void setExtraFontDecentPx(int postivePx);

    public void setTypeface(@Nullable Typeface typeface);

    public void setTypeface(@Nullable Typeface typeface, int style);

    public void setTextColor(@ColorInt int color);

    public void setBackgroundColor(@ColorInt int color);

    public void setLetterSpacing(float letterSpacing);

    public float getLetterSpacing();

    public void setVisibility(int visibility);

    public int getVisibility();

    public void setSingleLine(boolean singleLine);

    public float getLeftSideBearingPx();

    public void setLeftSideBearingPx(float value);

    public float getRightSideBearingPx();

    public void setRightSideBearingPx(float value);

    public boolean isFuriganaEnabled();

    public void setFuriganaEnabled(boolean furiganaEnabled);
}
