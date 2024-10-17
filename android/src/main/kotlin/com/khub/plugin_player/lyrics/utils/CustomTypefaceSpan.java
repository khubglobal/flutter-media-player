package com.khub.plugin_player.lyrics.utils;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by pan on 14/4/15.
 */
public class CustomTypefaceSpan extends MetricAffectingSpan {
    private final Typeface typeface;

    public CustomTypefaceSpan(final Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateDrawState(final TextPaint drawState) {
        apply(drawState);
    }

    @Override
    public void updateMeasureState(final TextPaint paint) {
        apply(paint);
    }

    private void apply(final Paint paint) {
        final Typeface oldTypeface = paint.getTypeface();
        final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
        final int fakeStyle = oldStyle & ~typeface.getStyle();

        if ((fakeStyle & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fakeStyle & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(typeface);
    }

    public static SpannableString createSpannableStringWithFont(Context ctx, CharSequence text, Typeface typeface){
        SpannableString s = new SpannableString(text);
        if(typeface != null) {
            CustomTypefaceSpan tfs = new CustomTypefaceSpan(typeface);
            s.setSpan(tfs, 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }


}