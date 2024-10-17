package com.khub.plugin_player.lyrics.utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pan on 31/10/17.
 */

public class ViewHelper implements Animator.AnimatorListener {

    private final View view;

    private ObjectAnimator colorFade;

    public ViewHelper(View view) {
        this.view = view;
    }

    /**
     * @return The current background color
     */
    @Nullable
    public Integer getBackgroundColor() {
        ColorDrawable colorDrawable;
        Drawable d;
        if (view != null && (d = view.getBackground()) instanceof ColorDrawable) {
            colorDrawable = (ColorDrawable) d;
            return colorDrawable.getColor();
        } else {
            return null;
        }
    }

    /**
     * @param color
     * @param duration
     */
    @MainThread
    public synchronized void fadeBackgroundColorTo(@ColorInt int color, long duration) {
        fadeBackgroundColorTo(getBackgroundColor(), color, duration);
    }


    /**
     * @param fromColor
     * @param color
     * @param duration
     */
    @MainThread
    public synchronized void fadeBackgroundColorTo(@Nullable Integer fromColor, @ColorInt int color, long duration) {
        if (fromColor == null) {
            throw new IllegalArgumentException("View has no background color");
        }
        if (colorFade != null) {
            colorFade.removeAllListeners();
            colorFade.cancel();
            colorFade.end();
        }
        colorFade = ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(), fromColor, color);
        colorFade.setDuration(duration);
        colorFade.addListener(this);
        colorFade.start();
    }

    /**
     * @return
     */
    public View getView() {
        return view;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        synchronized (this) {
            colorFade = null;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    /**
     * @param delegate
     * @see {@link #findSubviews(ViewGroup, FindSubviewDelegate)}
     */
    public void findSubviews(FindSubviewDelegate delegate) {
        if (view instanceof ViewGroup) {
            findSubviews((ViewGroup) view, delegate);
        }
    }


    /**
     * Find subview(s) that matches {@link FindSubviewDelegate#isMatch(ViewGroup, View)}
     * Will call {@link FindSubviewDelegate#onMatch(ViewGroup, View)}
     *
     * @param viewGroup
     * @param delegate
     */
    public void findSubviews(ViewGroup viewGroup, FindSubviewDelegate delegate) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v != null) {
                if (delegate.isMatch(viewGroup, v)) {
                    delegate.onMatch(viewGroup, v);
                }
                if (v instanceof ViewGroup) {
                    findSubviews((ViewGroup) v, delegate);
                }
            }
        }
    }

    /**
     * Delegate callback {@link #findSubviews(FindSubviewDelegate)}
     */
    public interface FindSubviewDelegate {
        public boolean isMatch(ViewGroup parent, View view);

        public void onMatch(ViewGroup parent, View view);
    }
}
