package com.easternblu.khub.ktr.model;

import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.annotation.IntDef;

import com.easternblu.khub.ktr.view.StrokeTextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Models a configuration of the view model {@link Line}
 * Created by pan on 14/12/17.
 */

public class LineConfig {
    Typeface customFont = null;
    Integer customFontStyle = null;

    int textSizePx = 0;
    int extraFontAccentPx = 0;
    int extraFontDecentPx = 0;
    float letterSpacing = 0f;

    int layer0TextColor;
    int layer0StrokeColor;
    int layer0StrokeWidthPx;
    float leftSideBearingPx;
    float rightSideBearingPx;


    int layer1TextColor;
    int layer1StrokeColor;
    int layer1StrokeWidthPx;


    int layer0BackgroundColor;
    int layer1BackgroundColor;
    int backgroundColor;

    Integer[] tempoIndicatorPaddings = null;

    Prerequisite prerequisite;

    public LineConfig(Prerequisite prerequisite,
                      Typeface customFont,
                      Integer customFontStyle,
                      int textSizePx,
                      int extraFontAccentPx,
                      int extraFontDecentPx,
                      int layer0TextColor,
                      int layer0StrokeColor,
                      int layer0StrokeWidthPx,
                      int layer1TextColor,
                      int layer1StrokeColor,
                      int layer1StrokeWidthPx,
                      int layer0BackgroundColor,
                      int layer1BackgroundColor,
                      int backgroundColor,
                      float letterSpacing,
                      float leftSideBearingPx,
                      float rightSideBearingPx,
                      Integer[] tempoIndicatorPaddings) {
        this.prerequisite = prerequisite;
        this.customFont = customFont;
        this.customFontStyle = customFontStyle;
        this.textSizePx = textSizePx;
        this.extraFontAccentPx = extraFontAccentPx;
        this.extraFontDecentPx = extraFontDecentPx;
        this.layer0TextColor = layer0TextColor;
        this.layer0StrokeColor = layer0StrokeColor;
        this.layer0StrokeWidthPx = layer0StrokeWidthPx;
        this.layer1TextColor = layer1TextColor;
        this.layer1StrokeColor = layer1StrokeColor;
        this.layer1StrokeWidthPx = layer1StrokeWidthPx;
        this.layer0BackgroundColor = layer0BackgroundColor;
        this.layer1BackgroundColor = layer1BackgroundColor;
        this.backgroundColor = backgroundColor;
        this.letterSpacing = letterSpacing;
        this.leftSideBearingPx = leftSideBearingPx;
        this.rightSideBearingPx = rightSideBearingPx;
        this.tempoIndicatorPaddings = tempoIndicatorPaddings;
    }

    public float getLeftSideBearingPx() {
        return leftSideBearingPx;
    }

    public float getRightSideBearingPx() {
        return rightSideBearingPx;
    }

    public Typeface getCustomFont() {
        return customFont;
    }

    public Integer getCustomFontStyle() {
        return customFontStyle;
    }

    public int getTextSizePx() {
        return textSizePx;
    }

    public int getLayer0TextColor() {
        return layer0TextColor;
    }

    public int getLayer1TextColor() {
        return layer1TextColor;
    }

    public int getLayer0BackgroundColor() {
        return layer0BackgroundColor;
    }

    public int getLayer1BackgroundColor() {
        return layer1BackgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getLayer0StrokeColor() {
        return layer0StrokeColor;
    }

    public int getLayer0StrokeWidthPx() {
        return layer0StrokeWidthPx;
    }

    public int getLayer1StrokeColor() {
        return layer1StrokeColor;
    }

    public int getLayer1StrokeWidthPx() {
        return layer1StrokeWidthPx;
    }

    public float getLetterSpacing() {
        return letterSpacing;
    }

    public int getExtraFontAccentPx() {
        return extraFontAccentPx;
    }

    public int getExtraFontDecentPx() {
        return extraFontDecentPx;
    }

    public static final int LAYER_0 = 1; // ...000001
    public static final int LAYER_1 = 1 << 1; // ...000100
    public static final int REVERSE_STROKE_AND_TEXT_COLOR = 1 << 2; // ...001000


    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {LAYER_0, LAYER_1, REVERSE_STROKE_AND_TEXT_COLOR}, flag = true)
    public @interface ApplyOptions {
    }


    /**
     * Apply a line applyLineConfig to a {@link StrokeTextView}
     *
     * @param strokeTextView
     * @param options
     * @param fontScale
     */
    public void applyTo(StrokeTextView strokeTextView, @ApplyOptions int options, float fontScale) {
        int textColor, strokeColor, strokeWidthPx, backgroundColor;

        if ((options & LAYER_0) == LAYER_0) {
            textColor = getLayer0TextColor();
            strokeColor = getLayer0StrokeColor();
            strokeWidthPx = getLayer0StrokeWidthPx();
            backgroundColor = getLayer0BackgroundColor();
        } else {
            textColor = getLayer1TextColor();
            strokeColor = getLayer1StrokeColor();
            strokeWidthPx = getLayer1StrokeWidthPx();
            backgroundColor = getLayer1BackgroundColor();
        }

        if ((options & REVERSE_STROKE_AND_TEXT_COLOR) == REVERSE_STROKE_AND_TEXT_COLOR) {
            int temp = textColor;
            textColor = strokeColor;
            strokeColor = temp;
        }

        strokeTextView.setLeftSideBearingPx(getLeftSideBearingPx());
        strokeTextView.setRightSideBearingPx(getRightSideBearingPx());
        strokeTextView.setTextColor(textColor);
        strokeTextView.setStrokeColor(strokeColor);
        strokeTextView.setStrokeWidthPx((int) (strokeWidthPx * fontScale));
        strokeTextView.setBackgroundColor(backgroundColor);
        strokeTextView.setExtraFontAccentPx(getExtraFontAccentPx());
        strokeTextView.setExtraFontDecentPx(getExtraFontDecentPx());


        Typeface font;
        if ((font = this.getCustomFont()) != null) {
            if (this.getCustomFontStyle() == null) {
                strokeTextView.setTypeface(font);
            } else {
                strokeTextView.setTypeface(font, this.getCustomFontStyle());
            }
        }
        if (this.getTextSizePx() > 0) {
            strokeTextView.setTextSizePx((int) (this.getTextSizePx() * fontScale));
        }
    }

    public Prerequisite getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(Prerequisite prerequisite) {
        this.prerequisite = prerequisite;
    }

    public Integer[] getTempoIndicatorPaddings() {
        return tempoIndicatorPaddings;
    }

    public interface Prerequisite {
        public boolean shouldApplyTo(Line line);
    }
}
