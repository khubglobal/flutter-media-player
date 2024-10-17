package com.khub.plugin_player.lyrics.utils;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import androidx.annotation.AnyRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pair;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.easternblu.khub.common.util.CloseableUtil;
import com.easternblu.khub.common.util.Lambda;
import com.easternblu.khub.common.util.Streams;
import com.easternblu.khub.common.util.Strings;
import com.khub.plugin_player.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Scanner;
import java.util.WeakHashMap;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by pan on 24/3/15.
 */
public class Views {
    public static final int SDK_INT =
            (Build.VERSION.SDK_INT == 25 && Build.VERSION.CODENAME.charAt(0) == 'O') ? 26
                    : Build.VERSION.SDK_INT;

    public static final String TAG = Views.class.getSimpleName();

    public static final String APP_FONT_BOLD = AppFontKt.getBOLD_FONT().getAssetPath();
    public static final String APP_FONT_DEFAULT = AppFontKt.getDEFAULT_FONT().getAssetPath();
    public static final String COURIER = "fonts/Courier.ttf";
    public static final String ROBOTO_BLACK = "fonts/Roboto-Black.ttf";
    public static final String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
    public static final String ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String ROBOTO_THIN = "fonts/Roboto-Thin.ttf";

    public static final String LATO_HEAVY = "fonts/Lato-Heavy.ttf";


    public static final String SHARP_STD_CLOUD_YUAN_CU_GBK = "fonts/SharpStdFontLibCloudYuanCuGBK.ttf";
    public static final String COOLVETICA_REGULAR = "fonts/CoolveticaRg.ttf";


    public static void setProgressBarIndeterminateColor(Context ctx, ProgressBar progressBar, @ColorRes int colorRes) {
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(ctx, colorRes), PorterDuff.Mode.SRC_IN);
    }

    public static WeakHashMap<String, Typeface> cache = new WeakHashMap<>();

    /**
     * Return the rectangle of the view which has location and size information
     *
     * @param view
     * @return
     */
    public static RectF getMeasuredRectFOnScreen(View view) {
        int[] xy;
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.getLocationOnScreen(xy = new int[2]);
        return new RectF(xy[0], xy[1], xy[0] + view.getMeasuredWidth(), xy[1] + view.getMeasuredHeight());
    }


    public static RectF getMeasuredRect(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return new RectF(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    public static boolean isZeroRect(RectF rect) {
        return rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0;
    }

    public static boolean isZeroRect(Rect rect) {
        return rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0;
    }


    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }


    public String getHexStringFromColorResId(Context ctx, int colorResId) {
        return Integer.toHexString(ctx.getResources().getColor(colorResId) & 0x00ffffff);
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Set the visibilities of the views with the given animation. The animation will occur all at the same time
     *
     * @param ctx
     * @param visibility
     * @param animResId
     * @param views
     */
    public static void setAsyncVisibility(Context ctx, int visibility, int animResId, View... views) {
        if (animResId == 0) {
            setVisibility(visibility, views);
            return;
        }
        for (View view : views) {
            if (view == null)
                continue;
            int startVisibility = View.VISIBLE;
            int endVisibility = visibility;
            Animations.animate(ctx, view, animResId, startVisibility, endVisibility);
        }
    }

    /**
     * Set the visibilities of the views with the given animation. The animation will occur one after an another.
     *
     * @param ctx
     * @param visibility
     * @param animResId
     * @param views
     */
    public static void setSyncVisibility(Context ctx, int visibility, int animResId, View... views) {
        setSyncVisibility(ctx, visibility, animResId, 0, views);
    }

    private static void setSyncVisibility(final Context ctx, final int visibility, final int animResId, final int offset, final View... views) {
        if (animResId == 0) {
            setVisibility(visibility, views);
            return;
        }

        if (offset >= views.length)
            return;

        View view = views[offset];
        final int startVisibility = View.VISIBLE;
        final int endVisibility = visibility;
        Animations.animate(ctx, view, animResId, startVisibility, endVisibility, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setSyncVisibility(ctx, visibility, animResId, offset + 1, views);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            if (view == null)
                continue;
            view.setVisibility(visibility);
        }
    }

    public static void setTintWithColorResId(Context ctx, ImageView imageView, int colorResId) {
        setTintWithColor(ctx, imageView, ctx.getResources().getColor(colorResId));
    }

    public static void setTintWithColor(Context ctx, ImageView imageView, int color) {
        imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }


    @Deprecated
    public static void setImageSrcTintColorResId(Context ctx, ImageView imageView, int color) {
        setImageSrcTintColor(ctx, imageView, ctx.getResources().getColor(color));
    }

    @Deprecated
    public static void setImageSrcTintColor(Context ctx, ImageView imageView, int color) {
        setImageSrcTintColor(ctx, imageView, color, PorterDuff.Mode.SRC_IN);
    }

    /**
     * Doesn't work
     *
     * @param ctx
     * @param imageView
     * @param color
     * @param porterDuffMode
     */
    @Deprecated
    public static void setImageSrcTintColor(Context ctx, ImageView imageView, int color, PorterDuff.Mode porterDuffMode) {
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable d = imageView.getDrawable();
            if (d != null) {
                DrawableCompat.setTint(d, color);
            }
        } else {
            setTintWithColor(ctx, imageView, color);
        }

        // setTintWithColor(ctx, imageView, color);
    }


    public static void setImageBackgroundTintColorResId(Context ctx, ImageView imageView, @ColorRes int color) {
        setImageBackgroundTintColor(ctx, imageView, ctx.getResources().getColor(color));
    }

    public static void setImageBackgroundTintColor(Context ctx, ImageView imageView, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 21)
            imageView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        else
            setTintWithColor(ctx, imageView, color);
    }

    /**
     * Doesn't work drawable XML
     *
     * @param ctx
     * @param view
     * @param color
     */
    public static void setBackgroundTintColorResId(Context ctx, View view, @ColorRes int color) {
        setBackgroundTintColor(ctx, view, ctx.getResources().getColor(color));
    }

    public static void setBackgroundTintColor(Context ctx, View view, @ColorInt int color) {
//        if (BasicAppInfo.getAPILevelInt() >= 21) {
//            view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//        } else {
//            DrawableCompat.setTint(view.getBackground(), color);
//        }
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }


    public static void setLeftCompoundDrawableTintColor(Context ctx, TextView textView, @ColorInt int color) {
        setCompoundDrawableTintColor(ctx, textView, color, LEFT);
    }

    public static void setLeftCompoundDrawableTintColorResId(Context ctx, TextView textView, @ColorRes int color) {
        setCompoundDrawableTintColor(ctx, textView, ctx.getResources().getColor(color), LEFT);
    }

    public static void setRightCompoundDrawableTintColor(Context ctx, TextView textView, @ColorInt int color) {
        setCompoundDrawableTintColor(ctx, textView, color, RIGHT);
    }

    public static void setRightCompoundDrawableTintColorResId(Context ctx, TextView textView, @ColorRes int color) {
        setCompoundDrawableTintColor(ctx, textView, ctx.getResources().getColor(color), RIGHT);
    }


    public static void setEnabledWithAlphaEffect(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.setAlpha(enabled ? 1f : 0.5f);
    }


    public static final int LEFT = 1; // ...000001
    public static final int TOP = 1 << 1; // ...00010
    public static final int RIGHT = 1 << 2; // ...00100
    public static final int BOTTOM = 1 << 3; // ...001000


    @IntDef(value = {LEFT, TOP, RIGHT, BOTTOM}, flag = true)
    @Retention(RetentionPolicy.SOURCE)
    public @interface CompoundDrawablePosition {
    }


    private static int toCompoundDrawableArrayIndex(@CompoundDrawablePosition int drawableIndex) {
        switch (drawableIndex) {
            case LEFT:
                return 0;
            case TOP:
                return 1;
            case RIGHT:
                return 2;
            case BOTTOM:
                return 3;
        }
        return LEFT;
    }

    /**
     * Set the tint color of a drawable based on the position of the compound drawable
     *
     * @param ctx
     * @param textView
     * @param color
     * @param drawablePositions
     */
    public static void setCompoundDrawableTintColor(Context ctx, TextView textView, int color, @CompoundDrawablePosition int drawablePositions) {
        Drawable[] drawables = textView.getCompoundDrawables();
        for (int position : new int[]{LEFT, TOP, RIGHT, BOTTOM}) {
            int singleFlag = drawablePositions & position;
            if (singleFlag == position) {
                int index = toCompoundDrawableArrayIndex(singleFlag);
                if (0 <= index && index < 4) {
                    Drawable drawable = drawables[index];
                    if (drawable != null) {
                        DrawableCompat.setTint(drawable, color);
                    }
                }
            }
        }
    }

    public static SpannableString getCharSequenceWithTypeFace(Context ctx, CharSequence text, Typeface typeface) {
        return CustomTypefaceSpan.createSpannableStringWithFont(ctx, text, typeface);
    }


    public static CharSequence getStyledTitle(Context ctx, @NonNull String titleString) {
        return getStyledTitle(ctx, titleString, null);
    }

    public static CharSequence getStyledTitle(Context ctx, @NonNull String titleString, @Nullable String suffixString) {
        SpannableString title = new SpannableString(titleString);
        title.setSpan(new RelativeSizeSpan(1.15f), 0, title.length(), 0);

        if (Strings.isNotEmpty(suffixString)) {
            SpannableString suffix = new SpannableString(suffixString);
            suffix.setSpan(new ForegroundColorSpan(Views.getColor(ctx, R.color.white_alphaB3)), 0, suffix.length(), 0);
            suffix.setSpan(new RelativeSizeSpan(0.75f), 0, suffix.length(), 0);
            return TextUtils.concat(title, "\n", suffix);
        } else {
            return title;
        }
    }


    public static int getViewHeight(View v) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthMeasureSpec, heightMeasureSpec);
        int height = v.getMeasuredHeight();
        return height;
    }

    public static int getViewWidth(View v) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthMeasureSpec, heightMeasureSpec);
        int width = v.getMeasuredWidth();
        return width;
    }


    public static Rect getMargins(View v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return null;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        return new Rect(
                marginLayoutParams.leftMargin,
                marginLayoutParams.topMargin,
                marginLayoutParams.rightMargin,
                marginLayoutParams.bottomMargin);
    }


    public static void setMargins(View v, Integer leftPx, Integer topPx, Integer rightPx, Integer bottomPx) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;

        if (leftPx != null) {
            marginLayoutParams.leftMargin = leftPx;
        }
        if (topPx != null) {
            marginLayoutParams.topMargin = topPx;
        }
        if (rightPx != null) {
            marginLayoutParams.rightMargin = rightPx;
        }
        if (bottomPx != null) {
            marginLayoutParams.bottomMargin = bottomPx;
        }
        v.setLayoutParams(marginLayoutParams);
    }

    public static void setSize(View v, Integer width, Integer height) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams != null) {
            if (width != null) {
                layoutParams.width = width;
            }
            if (height != null) {
                layoutParams.height = height;
            }
            v.setLayoutParams(layoutParams);
        }
    }


    public static void setLeftMarginWithDimenResId(View v, int dimenResId) {
        int px = Dimen.fromDimenValuesToPx(v.getContext(), dimenResId)[0];
        setMargins(v, px, null, null, null);
    }

    public static void setTopMarginWithDimenResId(View v, int dimenResId) {
        int px = Dimen.fromDimenValuesToPx(v.getContext(), dimenResId)[0];
        setMargins(v, null, px, null, null);
    }

    public static void setRightMarginWithDimenResId(View v, int dimenResId) {
        int px = Dimen.fromDimenValuesToPx(v.getContext(), dimenResId)[0];
        setMargins(v, null, null, px, null);
    }

    public static void setBottomMarginWithDimenResId(View v, int dimenResId) {
        int px = Dimen.fromDimenValuesToPx(v.getContext(), dimenResId)[0];
        setMargins(v, null, null, null, px);
    }


    public static void setLeftMargin(View v, int px) {
        setMargins(v, px, null, null, null);
    }

    public static void setTopMargin(View v, int px) {
        setMargins(v, null, px, null, null);
    }

    public static void setRightMargin(View v, int px) {
        setMargins(v, null, null, px, null);
    }

    public static void setBottomMargin(View v, int px) {
        setMargins(v, null, null, null, px);
    }


    public static void setWidth(View v, int width) {
        setSize(v, width, null);
    }

    public static void setHeight(View v, int height) {
        setSize(v, null, height);
    }


    public static void setLeftPadding(View v, int px) {
        setPaddings(v, px, null, null, null);
    }

    public static void setTopPadding(View v, int px) {
        setPaddings(v, null, px, null, null);
    }

    public static void setRightPadding(View v, int px) {
        setPaddings(v, null, null, px, null);
    }

    public static void setBottomPadding(View v, int px) {
        setPaddings(v, null, null, null, px);
    }


    public static void setPaddings(View v, Integer leftPx, Integer topPx, Integer rightPx, Integer bottomPx) {
        int l = (leftPx == null) ? v.getPaddingLeft() : leftPx;
        int t = (topPx == null) ? v.getPaddingTop() : topPx;
        int r = (rightPx == null) ? v.getPaddingRight() : rightPx;
        int b = (bottomPx == null) ? v.getPaddingBottom() : bottomPx;
        v.setPadding(l, t, r, b);
    }

    public static void setPaddings(View v, int paddingPx) {
        setPaddings(v, paddingPx, paddingPx, paddingPx, paddingPx);
    }


    public static void setFont(Context ctx, TextView textView, String fontAssetPath) {
        try {
            Typeface font = loadTypeface(ctx, fontAssetPath);

            textView.setTypeface(font);
        } catch (Throwable t) {
            Timber.e(t);

        }
    }


    /**
     * Don't call {@link Typeface#createFromAsset(AssetManager, String)} because it will create a new Typeface every time
     * And internally AssetManager keeps a reference hence causing memory leak
     * <p>
     * Using {@link TypefaceUtils#load(AssetManager, String)} instead as it has a internal cache
     *
     * @param ctx
     * @param fontAssetPath
     * @return
     */
    public static Typeface loadTypeface(Context ctx, String fontAssetPath) {
        return TypefaceUtils.load(ctx.getAssets(), fontAssetPath);
    }


    public static ColorStateList createFocusColorStateList(
            @ColorInt int pressed,
            @ColorInt int focused,
            @ColorInt int unfocused) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused}, // focus
                new int[]{-android.R.attr.state_focused}, // unfocus
        };

        int[] colors = new int[]{
                pressed,
                focused,
                unfocused
        };

        return new ColorStateList(states, colors);
    }


    public static CharSequence html2CustomTypefaceSpan(Context ctx, String html) {
        StringBuilder sb = new StringBuilder(html);

        List<SpannableStrings.SubString> sections;
        if (html.contains("<b>")) {
            sections = SpannableStrings.extractSections(sb, "<b>", "</b>");
        } else if (html.contains("<em>")) {
            sections = SpannableStrings.extractSections(sb, "<em>", "</em>");
        } else {
            return html;
        }

        SpannableString mutString = new SpannableString(sb.toString());
        for (SpannableStrings.SubString section : sections) {
            CustomTypefaceSpan boldSpan = new CustomTypefaceSpan(Views.loadTypeface(ctx, Views.APP_FONT_BOLD));
            mutString.setSpan(boldSpan, section.startInclusive, section.endExclusive, 0);
        }
        return mutString;
    }


    /**
     * Similar to {@link View#setTag(int, Object)}
     *
     * @param view
     * @param tagId
     * @param object
     */
    public static void setTag(View view, @IdRes int tagId, Object object) {
        view.setTag(tagId, object);
    }


    /**
     * @param view
     * @param id
     * @param clazz
     * @param defaultValue
     * @param <V>
     * @return
     */
    @NonNull
    public static <V> V getTag(View view, int id, Class<V> clazz, V defaultValue) {
        Object tagObject = view.getTag(id);
        if (tagObject != null && clazz.isInstance(tagObject)) {
            return (V) tagObject;
        } else {
            return defaultValue;
        }
    }


    /**
     * @param view
     * @param id
     * @param clazz
     * @param <V>
     * @return
     */
    @Nullable
    public static <V> V getTag(View view, int id, Class<V> clazz) {
        return getTag(view, id, clazz, null);
    }


    /**
     * Use together with {@link View#setTag(int, Object)} and internally will {@link ViewGroup#getChildAt(int)} and {@link View#getTag(int)}
     *
     * @param viewGroup
     * @param tagId
     * @param tagValue
     * @return
     */
    public static View findChildWithTag(ViewGroup viewGroup, @IdRes int tagId, Object tagValue) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Object object;
            if (child != null && (object = child.getTag(tagId)) != null && object.equals(tagValue)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Call {@link #findChildWithTag(ViewGroup, int, Object)} to removeAll a child from parent
     *
     * @param viewGroup
     * @param tagId
     * @param tagValue
     */
    public static void removeChildWithTag(ViewGroup viewGroup, @IdRes int tagId, Object tagValue) {
        View v = findChildWithTag(viewGroup, tagId, tagValue);
        if (v != null) {
            Timber.d("removeChildWithTag: Found view with " + tagId + " = " + tagValue);
            viewGroup.removeView(v);
        }
    }


    /**
     * Call recursively {@link #findChildWithTag(ViewGroup, int, Object)} to removeAll all child from parent with a certain tag value
     *
     * @param viewGroup
     * @param tagId
     * @param tagValue
     */
    public static void removeAllChildWithTag(ViewGroup viewGroup, @IdRes int tagId, Object tagValue) {
        View v = findChildWithTag(viewGroup, tagId, tagValue);
        if (v != null) {
            Timber.d("removeAllChildWithTag: Found view with " + tagId + " = " + tagValue);
            viewGroup.removeView(v);
            removeAllChildWithTag(viewGroup, tagId, tagValue);
        } else {
            Timber.d("removeAllChildWithTag: No view with " + tagId + " = " + tagValue);
        }
    }


    /**
     * Set textsize with dimen resource
     */
    public static void setTextSize(Context ctx, TextView textview, @DimenRes int textSizeRes) {
        textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, ctx.getResources().getDimension(textSizeRes));
    }


    /**
     * Check is child is a descendent of
     *
     * @param ancestor
     * @param child
     * @return
     */
    public static boolean isDecendent(View ancestor, View child) {
        if (child != null && ancestor != null) {
            ViewParent parent = child.getParent();
            while (parent != null) {
                if (parent.equals(ancestor)) {
                    return true;
                } else {
                    parent = parent.getParent();
                }
            }
        }
        return false;
    }


    public static void setEnabled(boolean enabled, View... views) {
        for (View v : views) {
            if (v != null) {
                v.setEnabled(enabled);
            }
        }
    }


    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }


    private static boolean isHdmiSwitchSet() {
        // The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
        // An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
        File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
        if (!switchFile.exists()) {
            switchFile = new File("/sys/class/switch/hdmi/state");
        }
        try {
            Scanner switchFileScanner = new Scanner(switchFile);
            int switchValue = switchFileScanner.nextInt();
            switchFileScanner.close();
            return switchValue > 0;
        } catch (Exception e) {
            Timber.e(e);
            return false;
        }
    }


    /**
     * get uri to any resource type
     *
     * @param context - context
     * @param resId   - resource id
     * @return - Uri to resource by given id
     * @throws Resources.NotFoundException if the given ID does not exist.
     */
    public static final Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }


    /**
     * @param relativePathToFile
     * @return
     */
    public static final Uri getUriToAssetsFile(String relativePathToFile) {
        return Uri.parse(Strings.format("file:///android_asset/%1$s", relativePathToFile));
    }


    /**
     * Get int color bases on resource id
     *
     * @param ctx
     * @param colorRes
     * @return
     */
    public static int getColor(Context ctx, @ColorRes int colorRes) {
        if (SDK_INT >= 23) {
            return ctx.getColor(colorRes);
        } else {
            return ctx.getResources().getColor(colorRes);
        }
    }



    public static CharSequence getSubtitleString(@NonNull String titleString, @Nullable String subtitleString, float subtitleRatio) {
        SpannableString title = new SpannableString(titleString);
        title.setSpan(new RelativeSizeSpan(1.0f), 0, title.length(), 0);

        if (Strings.isNotEmpty(subtitleString)) {
            SpannableString suffix = new SpannableString(subtitleString);
            suffix.setSpan(new RelativeSizeSpan(subtitleRatio), 0, suffix.length(), 0);
            return TextUtils.concat(title, "\n", suffix);
        } else {
            return title;
        }
    }


    public static void animateMoveView(final View view, final RectF from, final RectF to, long duration) {
        animateMoveView(view, view, from, to, duration, null);
    }

    public static void animateMoveView(final View locationView, final View sizeView, final RectF from, final RectF to, long duration, @Nullable Animator.AnimatorListener listener) {
        Timber.d("animate from : " + from + " to " + to);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float r = (Float) valueAnimator.getAnimatedValue();
                float width = from.width() + r * (to.width() - from.width()),
                        height = from.height() + r * (to.height() - from.height());
                PointF position = new PointF(from.left + r * (to.left - from.left),
                        from.top + r * (to.top - from.top));

                Views.setSize(sizeView, (int) width, (int) height);
                Views.setSize(locationView, (int) width, (int) height);

                Timber.d("ValueAnimator rect : " + new RectF(position.x, position.y, position.x + width, position.y + height));
                locationView.setX(position.x);
                locationView.setY(position.y);

            }
        });
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration).start();
    }


    /**
     * Return the smallest RectF that can contains all rects
     *
     * @param rects
     * @return
     */
    @Nullable
    public static RectF createContainerRectF(RectF... rects) {
        if (rects == null || rects.length == 0) {
            return null;
        }
        RectF container = new RectF(rects[0]);
        for (RectF rect : rects) {
            container.left = Math.min(container.left, rect.left);
            container.top = Math.min(container.top, rect.top);
            container.right = Math.max(container.right, rect.right);
            container.bottom = Math.max(container.bottom, rect.bottom);
        }
        return container;
    }


    /**
     * read assets file as string
     *
     * @param ctx
     * @param assetPath
     * @return
     * @throws IOException
     */
    public static String readAssets(Context ctx, String assetPath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = ctx.getAssets().open(assetPath, AssetManager.ACCESS_BUFFER);
            return new String(Streams.readAll(inputStream));
        } finally {
            CloseableUtil.close(inputStream);
        }
    }

    public static boolean isOnUiThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


    public static void scaleWithAnimation(final View view, final float endScaleX, final float endScaleY, final float endZPx, long duration) {
        view.clearAnimation();
        final float startScaleX = view.getScaleX(), startScaleY = view.getScaleY(), startZPx = view.getZ();
        final float dx = endScaleX - startScaleX, dy = endScaleY - startScaleY, dz = endZPx - startZPx;
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            // float oldTextSize = 0;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float ratio = (Float) valueAnimator.getAnimatedValue();

                float scaleX = startScaleX + ratio * dx,
                        scaleY = startScaleY + ratio * dy,
                        z = startZPx + ratio * dz;
                view.setScaleX(scaleX);
                view.setScaleY(scaleY);
                view.setZ(z);

            }
        });
        anim.setDuration(duration);
        anim.start();
    }


    public static void setChildTextViewFontScale(ViewGroup viewGroup, final float scale) {

        new ViewHelper(viewGroup).findSubviews(new ViewHelper.FindSubviewDelegate() {
            @Override
            public boolean isMatch(ViewGroup parent, View view) {
                return view instanceof TextView;
            }

            @Override
            public void onMatch(ViewGroup parent, View view) {
                TextView tv = (TextView) view;
                float px;
                if (view.getTag(R.id.original_fontsize) != null) {
                    px = Views.getTag(view, R.id.original_fontsize, Float.class, null);
                } else {
                    px = tv.getTextSize();
                    view.setTag(R.id.original_fontsize, px);
                }
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, px * scale);
            }
        });
    }


    public static String getMeasuredSpecModeString(int measureSpec) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        switch (mode) {
            case View.MeasureSpec.AT_MOST:
                return "AT_MOST";
            case View.MeasureSpec.EXACTLY:
                return "EXACTLY";
            case View.MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                return String.valueOf("UNKNOWN:" + measureSpec);
        }
    }


    @Nullable
    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    public static <V extends View> void foreachChild(ViewGroup viewGroup, Class<V> clazz, Lambda<Pair<V, Integer>, Void> doTask) {
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (clazz.isInstance(v)) {
                    doTask.invoke(new Pair<>((V) v, i));
                }
            }
        }
    }

}
