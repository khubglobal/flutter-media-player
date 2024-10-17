package com.easternblu.khub.ktr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.easternblu.khub.ktr.PlayerDelegate;

import timber.log.Timber;

public class TempoIndicatorView extends View {
    public static String TAG = TempoIndicatorView.class.getSimpleName();

    protected volatile int totalDots = 4, remainingDots = 0;

    protected int circleRadiusPx;
    protected int dividerWidthPx;
    protected int strokeWidthPx;
    protected int shadowRadiusPx;

    @ColorInt
    protected int shadowColor;

    protected int indicatorPaddingPx;
    @ColorInt
    protected int indicatorColor;
    @ColorInt
    protected int indicatorStrokeColor;

    protected CountDownTimer countDownTimer;

    private final int defaultScreenHeightDp = 720;
    public TempoIndicatorView(Context context) {
        super(context);
        setup(defaultScreenHeightDp);
    }

    public TempoIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(defaultScreenHeightDp);
    }

    public TempoIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(defaultScreenHeightDp);
    }

    public void setup(float heightDp) {
        indicatorPaddingPx = Utils.dpToPx(getContext(), 0.002777f*heightDp);
        indicatorColor = Color.parseColor("#ffec008c");
        shadowColor = Color.parseColor("#7c000000");
        indicatorStrokeColor = Color.parseColor("#ffffff");
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        shadowRadiusPx = Utils.dpToPx(getContext(), 1f);
        strokeWidthPx = Utils.dpToPx(getContext(), 0.002777f*heightDp);
        dividerWidthPx = Utils.dpToPx(getContext(), 0.0128f*heightDp);
        circleRadiusPx = Utils.dpToPx(getContext(), 0.015f*heightDp);
    }

    private void startCountDown(final long duration, final long interval, final CountDownCallback callback) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (callback != null) {
                    callback.onTick(this, millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    /**
     * Does the actual drawing. Calculation must match {@link #onMeasure(int, int)}
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getPaddingLeft(), y = getPaddingTop();
        Timber.e(remainingDots + " of " + totalDots);
        if (remainingDots <= 0) {
            return;
        }
        //Timber.d("onDraw: 1");
        int strokePaddingHorizontal = indicatorPaddingPx;
        int strokePaddingVertical = indicatorPaddingPx;

        Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke.setStrokeWidth(strokeWidthPx);
        stroke.setColor(indicatorStrokeColor);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setShadowLayer(shadowRadiusPx, 0, 0, shadowColor);

        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setColor(indicatorColor);
        fill.setStyle(Paint.Style.FILL);
        fill.setShadowLayer(shadowRadiusPx, 0, 0, shadowColor);

        //Timber.d("onDraw: 2 totalDots = " + totalDots + ", dotIndex = " + remainingDots);

        for (int i = 0; i < remainingDots; i++) {
            Paint paint = fill;
            canvas.drawCircle(x + strokePaddingHorizontal + circleRadiusPx, y + strokePaddingVertical + circleRadiusPx, circleRadiusPx, paint);
            paint = stroke;
            canvas.drawCircle(x + strokePaddingHorizontal + circleRadiusPx, y + strokePaddingVertical + circleRadiusPx, circleRadiusPx, paint);
            x += ((strokePaddingHorizontal + circleRadiusPx) * 2) + dividerWidthPx;
            //Timber.d("onDraw: 3 " + i);
        }
    }


    /**
     * @param tempo
     * @param dots
     * @return
     */
    public boolean start(long tempo, final int dots, @Nullable final PlayerDelegate playerDelegate) {
        totalDots = dots;
        remainingDots = dots;
        startCountDown(Long.MAX_VALUE, tempo, new CountDownCallback() {
            int i = 0;

            @Override
            public void onTick(CountDownTimer timer, long millisUntilFinished) {
                if (playerDelegate != null && !playerDelegate.isPlaying()) {
                    return;
                }
                //Timber.d("start: 2");
                //Timber.d("onTick millisUntilFinished = " + millisUntilFinished);

                if (i == 0) {

                } else {
                    remainingDots--;
                }
                render();
                if (remainingDots < 0) {
                    timer.cancel();
                }
                i++;
            }
        });
        //Timber.d("start: 3");
        return true;
    }

    private void render() {
        requestLayout();
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int suggestedMinWidth = getSuggestedMinimumWidth(), suggestedMinHeight = getSuggestedMinimumHeight();

        int paddingHorizontal = indicatorPaddingPx;
        int paddingVertical = indicatorPaddingPx;


        int n = remainingDots;
        suggestedMinWidth = ((paddingHorizontal + circleRadiusPx) * 2) * n + dividerWidthPx * (n - 1);
        suggestedMinWidth = Math.max(0, suggestedMinWidth);
        suggestedMinHeight = ((paddingVertical + circleRadiusPx) * 2);


        int desiredWidth = suggestedMinWidth + getPaddingLeft() + getPaddingRight();
        int desiredHeight = suggestedMinHeight + getPaddingTop() + getPaddingBottom();
        //Timber.d("desiredWidth = " + desiredWidth + ", desiredHeight = " + desiredHeight);
        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec),
                measureDimension(desiredHeight, heightMeasureSpec));
    }


    /**
     * @param desiredSize
     * @param measureSpec
     * @return
     * @see <a href="https://medium.com/@quiro91/custom-view-mastering-onmeasure-a0a0bb11784d"/>
     */
    private int measureDimension(int desiredSize, int measureSpec) {
        ViewGroup.LayoutParams temp = getLayoutParams();
        temp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(temp);
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //  Timber.w("specMode = " + Views.getMeasuredSpecModeString(measureSpec));
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize) {
            //  Timber.w("result = " + result + ", desiredSize =" + desiredSize);
            Timber.e("The view is too small, the content might get cut (desiredSize=" + desiredSize + ",measureSpec=" + measureSpec + ")");
        }
        return result;
    }


    private interface CountDownCallback {
        public void onTick(CountDownTimer timer, long millisUntilFinished);
    }

    public int getCircleRadiusPx() {
        return circleRadiusPx;
    }

    public void setCircleRadiusPx(int circleRadiusPx) {
        this.circleRadiusPx = circleRadiusPx;
    }

    public int getDividerWidthPx() {
        return dividerWidthPx;
    }

    public void setDividerWidthPx(int dividerWidthPx) {
        this.dividerWidthPx = dividerWidthPx;
    }

    public int getStrokeWidthPx() {
        return strokeWidthPx;
    }

    public void setStrokeWidthPx(int strokeWidthPx) {
        this.strokeWidthPx = strokeWidthPx;
    }

    public int getShadowRadiusPx() {
        return shadowRadiusPx;
    }

    public void setShadowRadiusPx(int shadowRadiusPx) {
        this.shadowRadiusPx = shadowRadiusPx;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getIndicatorPaddingPx() {
        return indicatorPaddingPx;
    }

    public void setIndicatorPaddingPx(int indicatorPaddingPx) {
        this.indicatorPaddingPx = indicatorPaddingPx;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public int getIndicatorStrokeColor() {
        return indicatorStrokeColor;
    }

    public void setIndicatorStrokeColor(int indicatorStrokeColor) {
        this.indicatorStrokeColor = indicatorStrokeColor;
    }
}
