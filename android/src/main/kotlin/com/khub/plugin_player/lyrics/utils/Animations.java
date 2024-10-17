package com.khub.plugin_player.lyrics.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import androidx.annotation.Nullable;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.khub.plugin_player.R;


/**
 * Created by pan on 13/4/15.
 */
public class Animations {
    public static Animation animate(Context ctx, final View v, int animResId, final int startVisibility, final int endVisibility) {
        return animate(ctx, v, animResId, startVisibility, endVisibility, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setTag(R.id.animation_hashcode, animation.hashCode());
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static Animation animate(Context ctx, final View v, int animResId, final int startVisibility, final int endVisibility, final Animation.AnimationListener additionalListener) {
        v.setVisibility(startVisibility);
        Animation animation = AnimationUtils.loadAnimation(ctx, animResId);
        v.clearAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setTag(R.id.animation_hashcode, animation.hashCode());
                if (additionalListener != null)
                    additionalListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (additionalListener != null)
                    additionalListener.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(endVisibility);
                if (additionalListener != null)
                    additionalListener.onAnimationEnd(animation);
            }
        });
        v.startAnimation(animation);
        return animation;
    }


    @Nullable
    public static Animation showAndHide(final Context ctx, final Handler handler, final View v, final int animResId1, final int animResId2, final long duration) {
        boolean locked = Views.getTag(v, R.id.animation_locked, Boolean.class, false);
        if (locked) {
            return null;
        }
        return Animations.animate(ctx, v, animResId1, View.VISIBLE, View.VISIBLE, new Animation.AnimationListener() {

            int originalHashCode = 0;

            @Override
            public void onAnimationStart(Animation animation) {
                v.setTag(R.id.animation_hashcode, originalHashCode = animation.hashCode());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Views.setTag(v, R.id.animation_locked, false);
                if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
                    return;
                }

                Integer animationHashCode = (Integer) v.getTag(R.id.animation_hashcode);
                if (animationHashCode == null) {
                    return;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Object animationHashCode = v.getTag(R.id.animation_hashcode);
                        if (animationHashCode != null && ((Integer) animationHashCode).intValue() != originalHashCode) {
                            return;
                        }

                        if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
                            return;
                        }
                        Animations.animate(ctx, v, animResId2, View.VISIBLE, View.GONE);
                    }
                }, duration);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void clearAnimation(final Context ctx, final View v) {
        v.setTag(R.id.animation_hashcode, null);
        Views.setTag(v, R.id.animation_locked, false);
        v.clearAnimation();
    }


//    publish static synchronized void singleAnimate(Context ctx, final View v, int animResId, final int startVisibility, final int endVisibility) {
//        Object locked = v.getTag(R.id.animation_locked);
//        if (v.getVisibility() == endVisibility || (locked != null && locked instanceof Boolean && ((Boolean) locked) == true)) {
//
//        } else {
//            v.setTag(R.id.animation_locked, true);
//            Animations.animate(ctx, v, animResId, startVisibility, endVisibility, new Animation.AnimationListener() {
//                @Override
//                publish void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                publish void onAnimationEnd(Animation animation) {
//                    v.setTag(R.id.animation_locked, false);
//                }
//
//                @Override
//                publish void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//    }

}
