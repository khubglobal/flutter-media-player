package com.easternblu.khub.ktr.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.easternblu.khub.ktr.MainThreadScheduler;
import com.easternblu.khub.ktr.PlayerDelegate;
import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.LineConfig;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.media.R;

import timber.log.Timber;

import static com.easternblu.khub.ktr.view.State.DONE;
import static com.easternblu.khub.ktr.view.State.NOT_INIT;
import static com.easternblu.khub.ktr.view.State.PENDING;
import static com.easternblu.khub.ktr.view.State.SWEEPING;

/**
 * A layout to manage the two layers of {@link StrokeTextView}
 * Created by pan on 18/5/17.
 */

public class SweepingStrokeTextViewLayout extends FrameLayout {
    //public static final boolean USE_FURIGANA_VIEW = true;
    public static final int SCHEDULE_MESSAGE = 1;

    // if sweeping is faster than this value then it will be set to done state immediately
    public static final long MIN_SWEEPING_DURATION = 100L;


    public static final String TAG = SweepingStrokeTextViewLayout.class.getSimpleName();

    public interface Listener {
        void onStateChanged(State state);
    }

    private LayoutInflater layoutInflater;


    protected StrokeTextView layer0Text; // white text
    protected StrokeTextView layer1Text; // color sweeping text;
    @Nullable
    protected Phrase phrase;
    protected Line line;
    protected Listener listener;
    protected MainThreadScheduler scheduler;
    protected PlayerDelegate delegate;
    private boolean furiganaEnabled = true;


    public SweepingStrokeTextViewLayout(@NonNull Context context) {
        super(context);
        setupView();
    }

    public SweepingStrokeTextViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public SweepingStrokeTextViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SweepingStrokeTextViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();

    }


    private void setupView() {
        scheduler = new MainThreadScheduler();
        (layoutInflater = LayoutInflater.from(getContext())).inflate(R.layout.view_text_segment, this);


        addStrokeTextView(layer0Text = createStrokeTextView());
        addStrokeTextView(layer1Text = createStrokeTextView());


        if (layer0Text instanceof FuriganaStrokeTextView && layer1Text instanceof FuriganaStrokeTextView) {
            ((FuriganaStrokeTextView) layer1Text).setBackgroundLayer((FuriganaStrokeTextView) layer0Text);
        }
    }

    private void addStrokeTextView(StrokeTextView strokeTextView) {
        if (strokeTextView != null) {
            if (strokeTextView instanceof View) {
                View v = (View) strokeTextView;
                v.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                addView(v);
            }
        }
    }


    protected StrokeTextView createStrokeTextView() {
        FuriganaStrokeTextView fv = new FuriganaStrokeTextView(getContext());
        fv.setFuriganaStrokeWidthPx(Utils.dpToPx(getContext(), 3f));
        fv.setFuriganaTextSizeScale(0.33f);
        fv.setSingleLine(true);
        return fv;
    }


    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDelegate(PlayerDelegate delegate) {
        this.delegate = delegate;
    }

    public String getText() {
        return phrase != null ? phrase.getText() : null;
    }

    /**
     * Configure the view using {@link LineConfig}
     *
     * @param config
     */
    public void applyLineConfig(LineConfig config) {
        config.applyTo(layer0Text, LineConfig.LAYER_0, 1f);
        config.applyTo(layer1Text, LineConfig.LAYER_1, 1f);
    }


    public void clearPhrase() {
        this.phrase = null;
        this.layer0Text.setAndApplyText("");
        this.layer1Text.setAndApplyText("");
        showLayer((View) layer0Text, false);
        hideLayer((View) layer1Text, false);
        setState(NOT_INIT);
    }


    public void setPhrase(Line line, Phrase phrase, int position) {
        this.phrase = phrase;
        this.line = line;
        setColorAndText(line.getTextColor(), phrase.getText());
    }

    public void setColorAndText(@ColorInt int textColor, String text) {
        this.layer0Text.setFuriganaEnabled(isFuriganaEnabled());
        this.layer0Text.setAndApplyText(text);
        this.layer1Text.setFuriganaEnabled(isFuriganaEnabled());
        this.layer1Text.setTextColor(textColor);
        this.layer1Text.setAndApplyText(text);
        showLayer((View) layer0Text, false);
        hideLayer((View) layer1Text, false);
        setState(NOT_INIT);
    }


    public boolean isFuriganaEnabled() {
        return furiganaEnabled;
    }

    public void setFuriganaEnabled(boolean furiganaEnabled) {
        this.furiganaEnabled = furiganaEnabled;
    }

    public void inverseLayer1Color() {
        int textColor = this.layer1Text.getCurrentTextColor();
        layer1Text.setTextColor(layer1Text.getStrokeColor());
        layer1Text.setStrokeColor(textColor);
    }


    private State state = NOT_INIT;


    /**
     *
     */
    public void scheduleSweeping() {
        updateAsPending();
        Phrase phrase = this.phrase;
        if (phrase == null) {
            return;
        }

        final long timeToSweeping = adjustForSpeed(Math.max(0, line.getStartTime() + phrase.offset - getMediaProgress()));
        //Timber.d("timeToSweeping = " + timeToSweeping);
        if (timeToSweeping == 0) {
            updateAsSweeping();
        } else {
            scheduler.schedule(SCHEDULE_MESSAGE, timeToSweeping, new Runnable() {
                @Override
                public void run() {
                    //Timber.d(line.getDisplayText() + " isPlaying = " + delegate.isPlaying());
                    if (delegate.isPlaying()) {
                        updateAsSweeping();
                    } else {
                        scheduleSweeping();
                    }
                }
            });
        }
    }


    synchronized void updateAsPending() {
        setState(PENDING);
        showLayer((View) layer0Text, true);
        hideLayer((View) layer1Text, false);
    }


    @MainThread
    synchronized void updateAsSweeping() {
        showLayer((View) layer0Text, false);
        setState(SWEEPING);
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (!(child instanceof SweepingStrokeTextViewLayout)) {
                    continue;
                }
                SweepingStrokeTextViewLayout siblingBeforeThis = (SweepingStrokeTextViewLayout) child;
                if (siblingBeforeThis.equals(this)) {
                    break;
                } else {
                    siblingBeforeThis.updateAsDone();
                }
            }
        }
        startSweeping();
    }


    void cancelScheduledSweeping() {
        scheduler.cancelPendingSchedule(SCHEDULE_MESSAGE);
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelScheduledSweeping();
        super.onDetachedFromWindow();
    }


    public synchronized void updateAsDone() {
        ((View) layer1Text).clearAnimation();
        setWidth((View) layer1Text, getWidth((View) layer0Text));
        showLayer((View) layer0Text, false);
        showLayer((View) layer1Text, false);
        setState(DONE);
    }

    private static int getWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    private long adjustForSpeed(long duration) {
        if (delegate != null) {
            return (long) (duration * (1d / delegate.getSpeed()));
        } else {
            return duration;
        }
    }


    synchronized void setState(State state) {
        State existing = this.state;
        this.state = state;
        if (existing != state) {
            if (listener != null) {
                listener.onStateChanged(state);
            }
        }
    }


    synchronized State getState() {
        return state;
    }

    private void showLayer(View layer, boolean animated) {
        if (layer.getVisibility() == View.VISIBLE)
            return;
        layer.setVisibility(View.VISIBLE);
    }

    private void hideLayer(View layer, boolean animated) {
        if (layer.getVisibility() == View.INVISIBLE)
            return;
        // TODO: implement animated
        layer.setVisibility(View.INVISIBLE);
    }


    private volatile ValueAnimator sweepingAnimation = null;

    @MainThread
    private void startSweeping() {
        Phrase phrase = this.phrase;
        if (phrase != null) {

            //  Timber.d("startSweeping: " + line.getFullText());
            if (sweepingAnimation != null) {
                if (sweepingAnimation.isRunning() || sweepingAnimation.isStarted()) {
                    sweepingAnimation.cancel();
                }
            }
            layer1Text.setVisibility(View.VISIBLE);
            if (getWidth((View) layer0Text) == 0) {
                return;
            }
            final long idealAnimationEndTime = line.getStartTime() + phrase.offset + phrase.duration;

            final long animationInvokeTime = SystemClock.uptimeMillis();
            // Timber.d("startSweeping: idealAnimationEndTime = " + Dates.toTimeText(idealAnimationEndTime) + " getMediaProgress() = " + Dates.toTimeText(getMediaProgress()));
            if (getMediaProgress() < idealAnimationEndTime) {

                setWidth((View) layer1Text, 0);
                sweepingAnimation = ValueAnimator.ofInt(0, getWidth((View) layer0Text));
                sweepingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer width = (Integer) animation.getAnimatedValue();
                        setWidth((View) layer1Text, width);
                    }
                });
                sweepingAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        updateAsDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                long duration = adjustForSpeed(idealAnimationEndTime - getMediaProgress());
                //  Timber.d("startSweeping: duration = " + duration);
                // we do one more check here because getMediaProgress might have changed and now we need to mark the thing as done
                if (duration > MIN_SWEEPING_DURATION) {
                    sweepingAnimation.setDuration(duration);
                    sweepingAnimation.start();
                } else {
                    updateAsDone();
                }
            } else {
                Timber.w("Too late for animation: " + phrase.getText());
                updateAsDone();
            }
        }
    }

    public boolean resumeSweeping() {
        Animation a;
        if (sweepingAnimation != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                if (sweepingAnimation.isPaused()) {
                    sweepingAnimation.resume();
                    return true;
                }
            }
        }
        return false;
    }


    public boolean pauseSweeping() {
        Animation a;
        if (sweepingAnimation != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                sweepingAnimation.pause();
                return true;
            }
        }
        return false;
    }

    private static void setWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }


    public long getMediaProgress() {
        return (delegate == null) ? 0 : delegate.getMediaProgress();
    }


    public void setLetterSpacing(float letterSpacing) {
        layer0Text.setLetterSpacing(letterSpacing);
        layer1Text.setLetterSpacing(letterSpacing);
    }

    public float getLetterSpacing() {
        float f;
        layer0Text.setLetterSpacing(f = layer1Text.getLetterSpacing());
        return f;
    }
}
