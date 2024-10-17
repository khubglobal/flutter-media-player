package com.easternblu.khub.ktr.model;

import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.ColorInt;

public class LineConfigBuilder {
    private Typeface customFont;
    private Integer customFontStyle = null;

    private float letterSpacing = 0;
    private int textSizePx = 18;


    Integer[] tempoIndicatorPaddings = null;


    @ColorInt
    int layer0TextColor = Color.parseColor("#ffffff");

    @ColorInt
    int layer0StrokeColor = Color.parseColor("#000000");

    int layer0StrokeWidthPx = 3;

    int extraFontDecentPx = 0;

    int extraFontAccentPx = 0;

    float leftSideBearingPx = 0;
    float rightSideBearingPx = 0;


    @ColorInt
    int layer1TextColor = Color.parseColor(Line.DEFAULT_SINGER_COLOR_STRING);
    @ColorInt
    int layer1StrokeColor = Color.parseColor("#ffffff");

    int layer1StrokeWidthPx = 3;


    @ColorInt
    int layer0BackgroundColor = Color.parseColor("#00000000");

    @ColorInt
    int layer1BackgroundColor = Color.parseColor("#00000000");

    @ColorInt
    int backgroundColor = Color.parseColor("#00000000");

    LineConfig.Prerequisite prerequisite;

    public LineConfigBuilder(LineConfig.Prerequisite prerequisite) {
        this.prerequisite = prerequisite;
    }

    public LineConfigBuilder setCustomFont(Typeface customFont) {
        this.customFont = customFont;
        return this;
    }

    public Integer[] getTempoIndicatorPaddings() {
        return tempoIndicatorPaddings;
    }

    public LineConfigBuilder setTempoIndicatorPaddings(Integer[] tempoIndicatorPaddings) {
        this.tempoIndicatorPaddings = tempoIndicatorPaddings;
        return this;
    }

    public LineConfigBuilder setTextSizePx(int textSizePx) {
        this.textSizePx = textSizePx;
        return this;
    }

    public LineConfigBuilder setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
        return this;
    }

    public LineConfigBuilder setLayer0TextColor(int layer0TextColor) {
        this.layer0TextColor = layer0TextColor;
        return this;
    }

    public LineConfigBuilder setLayer0StrokeColor(int layer0StrokeColor) {
        this.layer0StrokeColor = layer0StrokeColor;
        return this;
    }

    public LineConfigBuilder setLayer0StrokeWidthPx(int layer0StrokeWidthPx) {
        this.layer0StrokeWidthPx = layer0StrokeWidthPx;
        return this;
    }

    public LineConfigBuilder setLayer1StrokeColor(int layer1StrokeColor) {
        this.layer1StrokeColor = layer1StrokeColor;
        return this;
    }

    public LineConfigBuilder setLayer1StrokeWidthPx(int layer1StrokeWidthPx) {
        this.layer1StrokeWidthPx = layer1StrokeWidthPx;
        return this;
    }

    public LineConfigBuilder setLayer1TextColor(int layer1TextColor) {
        this.layer1TextColor = layer1TextColor;
        return this;
    }

    public LineConfigBuilder setLayer0BackgroundColor(int layer0BackgroundColor) {
        this.layer0BackgroundColor = layer0BackgroundColor;
        return this;
    }

    public LineConfigBuilder setLayer1BackgroundColor(int layer1BackgroundColor) {
        this.layer1BackgroundColor = layer1BackgroundColor;
        return this;
    }

    public LineConfigBuilder setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public LineConfigBuilder setCustomFontStyle(Integer customFontStyle) {
        this.customFontStyle = customFontStyle;
        return this;
    }

    public LineConfigBuilder setExtraFontDecentPx(int extraFontDecentPx) {
        this.extraFontDecentPx = extraFontDecentPx;
        return this;
    }

    public LineConfigBuilder setExtraFontAccentPx(int extraFontAccentPx) {
        this.extraFontAccentPx = extraFontAccentPx;
        return this;
    }

    public LineConfigBuilder setLeftSideBearingPx(float leftSideBearingPx) {
        this.leftSideBearingPx = leftSideBearingPx;
        return this;
    }

    public LineConfigBuilder setRightSideBearingPx(float rightSideBearingPx) {
        this.rightSideBearingPx = rightSideBearingPx;
        return this;
    }

    public LineConfigBuilder setPrerequisite(LineConfig.Prerequisite prerequisite) {
        this.prerequisite = prerequisite;
        return this;
    }

    public LineConfig createLineConfig() {
        return new LineConfig(
                prerequisite,
                customFont,
                customFontStyle,
                textSizePx,
                extraFontAccentPx,
                extraFontDecentPx,
                layer0TextColor,
                layer0StrokeColor,
                layer0StrokeWidthPx,
                layer1TextColor,
                layer1StrokeColor,
                layer1StrokeWidthPx,
                layer0BackgroundColor,
                layer1BackgroundColor,
                backgroundColor,
                letterSpacing,
                leftSideBearingPx,
                rightSideBearingPx,
                tempoIndicatorPaddings);
    }
}