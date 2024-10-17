/*
 * FuriganaView widget
 * Copyright (C) 2013 sh0 <sh0@yutani.ee>
 * Licensed under Creative Commons BY-SA 3.0
 */

// Package
package com.easternblu.khub.ktr.view;

// Imports

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.easternblu.khub.media.R;

import java.util.ArrayList;

/**
 * An implementation of {@link StrokeTextView} that support Furigana style display
 */
public class FuriganaStrokeTextView extends View implements StrokeTextView {

    private final static String TAG = FuriganaStrokeTextView.class.getSimpleName();
    private static final boolean DEBUG = false;


    private class TextFurigana {
        // Info
        private String text;

        // Coordinates
        float offset = 0.0f;
        float width = 0.0f;

        // Constructor
        public TextFurigana(String text) {
            // Info
            this.text = text;
            // Coordinates
            width = furiganaPaint.measureText(this.text);
        }

        // Info
        //private String text() { return text; }

        // Coordinates
        public float getOffset() {
            return offset;
        }

        public void setOffset(float value) {
            offset = value;
        }

        public float width() {
            return width;
        }

        // Draw
        public void draw(Canvas canvas, float x, float y) {
            // Timber.d("TextFurigana: draw text = " + text);
            x -= width / 2.0f;


            @ColorInt
            int currentTextColor = getCurrentTextColor();
            boolean drawStroke = furiganaStrokeWidthPx > 0;


            drawText(text, currentTextColor, canvas, x, y, furiganaPaint, drawStroke, strokeColor, furiganaStrokeWidthPx);

        }
    }

    private class TextNormal {
        // Info
        private String text;

        // Widths
        private float widthTotal;
        private float[] widthChars;

        // Constructor
        public TextNormal(String text) {
            // Info
            this.text = text;

            // Character widths
            widthChars = new float[this.text.length()];
            normalPaint.getTextWidths(this.text, widthChars);

            // Total width
            widthTotal = 0.0f;
            widthTotal += getLeftSideBearingPx();
            for (float v : widthChars) {
                widthTotal += v;
            }
            widthTotal += getRightSideBearingPx();
        }

        // Info
        public int length() {
            return text.length();
        }

        // Widths
        public float[] width_chars() {
            return widthChars;
        }

        // Split
        public TextNormal[] split(int offset) {
            return new TextNormal[]{
                    new TextNormal(text.substring(0, offset)),
                    new TextNormal(text.substring(offset))
            };
        }


        // Draw
        public float draw(Canvas canvas, float x, float y) {

            int currentTextColor = getCurrentTextColor();
            boolean drawStroke = strokeWidthPx > 0;

            drawText(text, currentTextColor, canvas, getLeftSideBearingPx() + x, y, normalPaint, drawStroke, strokeColor, strokeWidthPx);

            return widthTotal;
        }
    }

    protected void drawText(String text, int textColor, Canvas canvas, float x, float y, Paint p, boolean drawStroke, int _strokeColor, float _strokeWidthPx) {
        if (drawStroke) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(_strokeWidthPx);
            p.setStrokeJoin(Paint.Join.ROUND);
            p.setColor(_strokeColor);
            canvas.drawText(text, 0, text.length(), x, y, p);
            p.setColor(textColor);
            p.setStyle(Paint.Style.FILL);
            canvas.drawText(text, 0, text.length(), x, y, p);
        } else {
            p.setColor(textColor);
            canvas.drawText(text, 0, text.length(), x, y, p);
        }

    }

    private class LineFurigana {
        // Text
        private ArrayList<TextFurigana> texts = new ArrayList<>();
        private ArrayList<Float> offsets = new ArrayList<>();

        // Add
        public void add(TextFurigana text) {
            if (text != null) {
                texts.add(text);
            }
        }

        // Calculate
        public void calculate() {
            // Check size
            if (texts.size() == 0) {
                return;
            }
            offsets.clear();
            /*
            // Debug
            String str = "";
            for (TextFurigana text : text)
                str += "'" + text.text() + "' ";
            */

            // r[] - ideal offsets
            float[] r = new float[texts.size()];
            for (int i = 0; i < texts.size(); i++)
                r[i] = texts.get(i).getOffset();

            // a[] - constraint matrix
            float[][] a = new float[texts.size() + 1][texts.size()];
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < a[0].length; j++)
                    a[i][j] = 0.0f;
            a[0][0] = 1.0f;
            for (int i = 1; i < a.length - 2; i++) {
                a[i][i - 1] = -1.0f;
                a[i][i] = 1.0f;
            }
            a[a.length - 1][a[0].length - 1] = -1.0f;

            // b[] - constraint vector
            float[] b = new float[texts.size() + 1];
            b[0] = -r[0] + (0.5f * texts.get(0).width());
            for (int i = 1; i < b.length - 2; i++)
                b[i] = (0.5f * (texts.get(i).width() + texts.get(i - 1).width())) + (r[i - 1] - r[i]);
            b[b.length - 1] = -lineMax + r[r.length - 1] + (0.5f * texts.get(texts.size() - 1).width());

            // Calculate constraint optimization
            float[] x = new float[texts.size()];
            for (int i = 0; i < x.length; i++) {
                x[i] = 0.0f;
            }


            QuadraticOptimizer co = new QuadraticOptimizer(a, b);
            co.calculate(x);

            for (int i = 0; i < x.length; i++) {
                offsets.add(x[i] + r[i]);
            }
        }

        // Draw
        public void draw(Canvas canvas, float y) {
            y -= furiganaPaint.descent();
            if (offsets.size() == texts.size()) {
                // Render with fixed offsets
                for (int i = 0; i < offsets.size(); i++)
                    texts.get(i).draw(canvas, offsets.get(i), y);
            } else {
                // Render with original offsets
                for (TextFurigana text : texts)
                    text.draw(canvas, text.getOffset(), y);
            }
        }
    }

    @Override
    public float getLeftSideBearingPx() {
        return leftSideBearingPx;
    }

    @Override
    public void setLeftSideBearingPx(float value) {
        leftSideBearingPx = value;
    }

    @Override
    public float getRightSideBearingPx() {
        return rightSideBearingPx;
    }

    @Override
    public void setRightSideBearingPx(float value) {
        rightSideBearingPx = value;
    }


    private class LineNormal {
        // Text
        private ArrayList<TextNormal> texts = new ArrayList<>();

        // Elements
        public int size() {
            return texts.size();
        }

        public void add(ArrayList<TextNormal> text) {
            texts.addAll(text);
        }

        // Draw
        public void draw(Canvas canvas, float y) {
            y -= normalPaint.descent();
            float x = 0.0f;

            for (TextNormal text : texts)
                x += text.draw(canvas, x, y);
        }
    }

    private class Span {
        // Text
        private TextFurigana m_furigana = null;
        private ArrayList<TextNormal> m_normal = new ArrayList<>();

        // Widths
        private ArrayList<Float> m_width_chars = new ArrayList<>();
        private float m_width_total = 0.0f;

        // Constructors
        public Span(String text_f, String text_k) {
            // Furigana text
            if (text_f.length() > 0)
                m_furigana = new TextFurigana(text_f);

            m_normal.add(new TextNormal(text_k));

            // Widths
            widths_calculate();
        }

        public Span(ArrayList<TextNormal> normal) {
            // Only normal text
            m_normal = normal;

            // Widths
            widths_calculate();
        }

        // Text
        public TextFurigana furigana(float x) {
            if (m_furigana == null)
                return null;
            m_furigana.setOffset(x + (m_width_total / 2.0f));
            return m_furigana;
        }

        public ArrayList<TextNormal> normal() {
            return m_normal;
        }

        // Widths
        public ArrayList<Float> widths() {
            return m_width_chars;
        }

        private void widths_calculate() {
            // Chars
            if (m_furigana == null) {
                for (TextNormal normal : m_normal)
                    for (float v : normal.width_chars())
                        m_width_chars.add(v);
            } else {
                float sum = 0.0f;
                for (TextNormal normal : m_normal)
                    for (float v : normal.width_chars())
                        sum += v;
                m_width_chars.add(sum);
            }

            // Total
            m_width_total = 0.0f;


            for (float v : m_width_chars) {
                m_width_total += v;
            }


            // catering for horizontal padding
            if (m_width_chars.size() > 0) {
                m_width_total += getLeftSideBearingPx();
                Float temp = m_width_chars.get(0);
                m_width_chars.remove(0);
                m_width_chars.add(0, temp + getLeftSideBearingPx());


                int last = m_width_chars.size() - 1;
                temp = m_width_chars.get(last);
                m_width_chars.remove(last);
                m_width_chars.add(temp + getRightSideBearingPx());
                m_width_total += getRightSideBearingPx();
            }


        }

        // Split
        public void split(int offset, ArrayList<TextNormal> normal_a, ArrayList<TextNormal> normal_b) {
            // Check if no furigana
            assert (m_furigana == null);

            // Split normal list
            for (TextNormal cur : m_normal) {
                if (offset <= 0) {
                    normal_b.add(cur);
                } else if (offset >= cur.length()) {
                    normal_a.add(cur);
                } else {
                    TextNormal[] split = cur.split(offset);
                    normal_a.add(split[0]);
                    normal_b.add(split[1]);
                }
                offset -= cur.length();
            }
        }
    }

    // Paints
    private TextPaint furiganaPaint = new TextPaint() {
        @Override
        public float ascent() {
            return super.ascent();
        }

        @Override
        public float descent() {
            return super.descent();
        }
    };

    private TextPaint normalPaint = new TextPaint() {
        @Override
        public float ascent() {
            return super.ascent() + getExtraFontAccentPx();
        }

        @Override
        public float descent() {
            return super.descent() + getExtraFontDecentPx();
        }
    };


    // fields
    @ColorInt
    private int strokeColor = Color.GREEN;

    private float strokeWidthPx = 0, furiganaStrokeWidthPx = 0, leftSideBearingPx = 0, rightSideBearingPx = 0;

    // Sizes
    private float furiganaTextSizeScale = 0.5f;
    private float lineSize = 0.0f;
    private float heightNormal = 0.0f;
    private float heightFurigana = 0.0f;
    private float lineMax = 0.0f;
    private int gapPx;
    private int extraFontAccentPx, extraFontDecentPx;
    private boolean furiganaEnabled = true;
    // Spans and lines
    private ArrayList<Span> spans = new ArrayList<Span>();
    private ArrayList<LineNormal> lineNormals = new ArrayList<LineNormal>();
    private ArrayList<LineFurigana> lineFuriganas = new ArrayList<LineFurigana>();

    // Constructors
    public FuriganaStrokeTextView(Context context) {
        super(context);
        initView();
    }


    public FuriganaStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FuriganaStrokeTextView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        initView();
    }

    private FuriganaStrokeTextView backgroundLayer;

    public void setBackgroundLayer(FuriganaStrokeTextView backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
    }

    private boolean isOverlay() {
        return this.backgroundLayer != null;
    }

    private void initView() {
        normalPaint.setAntiAlias(true);
        gapPx = Utils.dpToPx(getContext(), 3);
    }

    public int getExtraFontAccentPx() {
        return extraFontAccentPx;
    }

    @Override
    public void setExtraFontAccentPx(int extraFontAccentPx) {
        this.extraFontAccentPx = extraFontAccentPx;
    }

    public int getExtraFontDecentPx() {
        return extraFontDecentPx;
    }

    @Override
    public void setExtraFontDecentPx(int extraFontDecentPx) {
        this.extraFontDecentPx = extraFontDecentPx;
    }

    private boolean singleLine = false;

    @ColorInt
    public int getCurrentTextColor() {
        return textColor;
    }

    @ColorInt
    private int textColor = Color.GRAY;

    @ColorInt
    private int textSizePx = 32;

    private Float letterSpacing = null;

    @Nullable
    private Typeface typeface = null;

    @Override
    public void setTextColor(@ColorInt int color) {
        this.textColor = color;
        this.normalPaint.setColor(this.textColor);
    }

    @Override
    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            normalPaint.setLetterSpacing(letterSpacing);  // setLetterSpacing is only available from LOLLIPOP and on
        }
    }

    @Override
    public float getLetterSpacing() {
        return letterSpacing == null ? 0 : letterSpacing.floatValue();
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }


    @Override
    public void setTypeface(@Nullable Typeface typeface) {
        this.typeface = typeface;
        this.normalPaint.setTypeface(this.typeface);
    }

    @Override
    public void setTypeface(@Nullable Typeface typeface, int style) {
        setTypeface(Typeface.create(typeface, style));
    }

    @Override
    public void setTextSizePx(int textSizePx) {
        this.textSizePx = textSizePx;
        this.normalPaint.setTextSize(textSizePx);
    }


    @Override
    public boolean isFuriganaEnabled() {
        return furiganaEnabled;
    }

    @Override
    public void setFuriganaEnabled(boolean furiganaEnabled) {
        this.furiganaEnabled = furiganaEnabled;
    }

    // Text functions
    @Override
    public void setAndApplyText(String text) {
        if (text == null || text.trim().length() == 0) {
            text = " ";
        }
        furiganaPaint = new TextPaint(normalPaint);
        furiganaPaint.setTypeface(Typeface.create(normalPaint.getTypeface(), Typeface.BOLD));
        furiganaPaint.setTextSize(furiganaPaint.getTextSize() * furiganaTextSizeScale);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            furiganaPaint.setLetterSpacing(0f);
        }

        // Line size
        heightNormal = normalPaint.descent() - normalPaint.ascent();
        heightFurigana = furiganaPaint.descent() - furiganaPaint.ascent();


        // Clear spans
        spans.clear();

        // Sizes
        lineSize = furiganaPaint.getFontSpacing() + gapPx + normalPaint.getFontSpacing();

        // Spannify text
        while (text.length() > 0) {
            int idx = text.indexOf('{');
            if (idx >= 0) {
                // Prefix string
                if (idx > 0) {
                    // Spans
                    spans.add(new Span("", text.substring(0, idx)));

                    // Remove text
                    text = text.substring(idx);

                }

                // End bracket
                idx = text.indexOf('}');
                if (idx < 1) {
                    // Error
                    text = "";
                    break;
                } else if (idx == 1) {
                    // Empty bracket
                    text = text.substring(2);
                    continue;
                }

                // Spans
                String[] split = text.substring(1, idx).split(";");

                String furigana = ((split.length > 1) ? split[1] : "");

                spans.add(new Span(isFuriganaEnabled() ? furigana : "", split[0]));

                // Remove text
                text = text.substring(idx + 1);

            } else {
                // Single span
                spans.add(new Span("", text));
                text = "";
            }
        }

        // Invalidate view
        this.invalidate();
        this.requestLayout();
    }

    private int lastMeasuredHeight = 0, lastMeasuredWidth = 0;

    // Size calculation
    @Override
    protected void onMeasure(int width_ms, int height_ms) {


        // Modes
        int wmode = View.MeasureSpec.getMode(width_ms);
        int hmode = View.MeasureSpec.getMode(height_ms);

        // Dimensions
        int wold = View.MeasureSpec.getSize(width_ms);
        int hold = View.MeasureSpec.getSize(height_ms);

        long start2 = SystemClock.elapsedRealtime();
        String mode;
        // Draw mode
        if (wmode == View.MeasureSpec.EXACTLY || wmode == View.MeasureSpec.AT_MOST && wold > 0) {
            // Width limited
            mode = "EXACTLY or AT_MOST";
            calculateText(wold);
        } else {
            mode = "UNLIMITED";
            // Width unlimited
            calculateText(-1.0f);
        }
        //  Timber.d("Took " + (SystemClock.uptimeMillis() - start2) + "ms to calculateText lineMax = " + lineMax + " mode " + mode + " is overlay " + isOverlay() + " wold = " + wold);


        // New height
        int hnew = (int) Math.round(Math.ceil(lineSize * (float) lineNormals.size()));
        int wnew = wold;


        if (wmode != View.MeasureSpec.EXACTLY && lineNormals.size() <= 1)
            wnew = (int) Math.round(Math.ceil(lineMax));
        if (hmode != View.MeasureSpec.UNSPECIFIED && hnew > hold)
            hnew |= MEASURED_STATE_TOO_SMALL;


        // Set result
        setMeasuredDimension(lastMeasuredWidth = wnew, lastMeasuredHeight = hnew);
    }

    protected float getStrokeWidthOffsetPx() {
        return getStrokeWidthPx();
    }


    private void calculateText(float lineMax) {
        // Clear lines
        lineNormals.clear();
        lineFuriganas.clear();

        // Sizes
        this.lineMax = 0.0f;

        // Check if no limits on width
        if (lineMax < 0.0) {

            // Create single normal and furigana line
            LineNormal line_n = new LineNormal();
            LineFurigana line_f = new LineFurigana();

            // Loop spans
            for (Span span : spans) {
                // Text
                line_n.add(span.normal());
                line_f.add(span.furigana(this.lineMax));

                // Widths update
                for (float width : span.widths())
                    this.lineMax += width;
            }

            // Commit both lines
            lineNormals.add(line_n);
            lineFuriganas.add(line_f);

        } else {

            // Lines
            float line_x = 0.0f;
            LineNormal line_n = new LineNormal();
            LineFurigana line_f = new LineFurigana();

            // Initial span
            int span_i = 0;
            Span span = spans.size() > 0 ? spans.get(span_i) : null;

            // Iterate
            while (span != null) {
                // Start offset
                float line_s = line_x;

                // Calculate possible line size
                ArrayList<Float> widths = span.widths();
                int i = 0;
                for (i = 0; i < widths.size(); i++) {
                    if (line_x + widths.get(i) <= lineMax)
                        line_x += widths.get(i);
                    else
                        break;
                }

                // Add span to line // disabled
                if (!singleLine && i >= 0 && i < widths.size()) {

                    // Span does not fit entirely
                    if (i > 0) {
                        // Split half that fits
                        ArrayList<TextNormal> normal_a = new ArrayList<TextNormal>();
                        ArrayList<TextNormal> normal_b = new ArrayList<TextNormal>();
                        span.split(i, normal_a, normal_b);
                        line_n.add(normal_a);
                        span = new Span(normal_b);
                    }

                    // Add new line with current spans 
                    if (line_n.size() != 0) {
                        // Add
                        this.lineMax = (this.lineMax > line_x ? this.lineMax : line_x);
                        lineNormals.add(line_n);
                        lineFuriganas.add(line_f);

                        // Reset
                        line_n = new LineNormal();
                        line_f = new LineFurigana();
                        line_x = 0.0f;

                        // Next span
                        continue;
                    }

                } else {

                    // Span fits entirely
                    line_n.add(span.normal());
                    line_f.add(span.furigana(line_s));

                }

                // Next span
                span = null;
                span_i++;
                if (span_i < spans.size())
                    span = spans.get(span_i);
            }

            // Last span
            if (line_n.size() != 0) {
                // Add
                this.lineMax = (this.lineMax > line_x ? this.lineMax : line_x);
                lineNormals.add(line_n);
                lineFuriganas.add(line_f);
            }
        }

        int lineSize;
        ArrayList<LineFurigana> backgroundlineFuriganas;
        // calculate takes too long for overlay (since width will keeps changing as it animates)
        // just use the offset values from background layer
        if (isOverlay() && (lineSize = (backgroundlineFuriganas = backgroundLayer.lineFuriganas).size()) == this.lineFuriganas.size() && lineSize > 0) {
            for (int i = 0; i < lineSize; i++) {
                lineFuriganas.get(i).offsets = backgroundlineFuriganas.get(i).offsets;
            }
        } else {
            for (LineFurigana line : lineFuriganas) {
                line.calculate();
            }
        }
    }

    // Drawing
    @Override
    public void onDraw(Canvas canvas) {


        // Check
        assert (lineNormals.size() == lineFuriganas.size());

        // Coordinates
        float y = lineSize;

        // Loop lines
        for (int i = 0; i < lineNormals.size(); i++) {
            lineNormals.get(i).draw(canvas, y);
            lineFuriganas.get(i).draw(canvas, y - (heightNormal + gapPx));
            y += lineSize;
        }

    }


    @Override
    @ColorInt
    public int getStrokeColor() {
        return strokeColor;
    }

    @Override
    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
    }

    @Override
    public float getStrokeWidthPx() {
        return strokeWidthPx;
    }

    @Override
    public void setStrokeWidthPx(float strokeWidthPx) {
        this.strokeWidthPx = strokeWidthPx;
    }


    public float getFuriganaStrokeWidthPx() {
        return furiganaStrokeWidthPx;
    }

    public void setFuriganaStrokeWidthPx(float furiganaStrokeWidthPx) {
        this.furiganaStrokeWidthPx = furiganaStrokeWidthPx;
    }

    public float getFuriganaTextSizeScale() {
        return furiganaTextSizeScale;
    }

    public void setFuriganaTextSizeScale(float furiganaTextSizeScale) {
        this.furiganaTextSizeScale = furiganaTextSizeScale;
    }

    @Override
    public void setBackgroundColor(int color) {
        if (!DEBUG) {
            super.setBackgroundColor(color);
        } else {
            setBackgroundResource(R.drawable.ktr_debug_rect);
        }
    }
}