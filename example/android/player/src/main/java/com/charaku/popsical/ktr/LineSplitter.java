package com.easternblu.khub.ktr;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.LineConfig;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.ktr.view.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class LineSplitter {
    public final String TAG = this.getClass().getSimpleName();
    private Context ctx;
    private ViewGroup invisibleParentLayout;
    private Lines lines;
    private LineConfig lineConfig;
    private int displayWidthPx;
    private final TextView textView;
    private int singleLineHeightPx;

    /**
     * Changes the data structure of {@link Lines} based on the displayWidthDp and the expected
     * {@link LineConfig} so that the expected rendered width of the {@link Line} will not exceed
     * the displayWidthDp and fit into a single line
     *
     * @param ctx
     * @param invisibleParentLayout
     * @param lines                 Lines object
     * @param lineConfig            Object that decided now the lines will look. It will use getCustomFontStyle and getTextSizePx
     * @param displayWidthDp        The width that you want to fit it (dp)
     */
    public LineSplitter(Context ctx, ViewGroup invisibleParentLayout, Lines lines, LineConfig lineConfig, float displayWidthDp) {
        this.ctx = ctx;
        this.invisibleParentLayout = invisibleParentLayout;
        this.lines = lines;
        this.lineConfig = lineConfig;
        this.displayWidthPx = Utils.dpToPx(ctx, displayWidthDp);
        this.textView = new TextView(ctx);
        this.invisibleParentLayout.addView(textView);
        Integer fontStyle = lineConfig.getCustomFontStyle();
        Typeface font = lineConfig.getCustomFont();
        if (font != null) {
            if (fontStyle == null) {
                textView.setTypeface(font);
            } else {
                textView.setTypeface(font, fontStyle.intValue());
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setLetterSpacing(lineConfig.getLetterSpacing());
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, lineConfig.getTextSizePx());
        this.singleLineHeightPx = measureHeightPx("Test").y;
    }


    private Point measureHeightPx(String text) {
        textView.setText(text);
        textView.measure(
                View.MeasureSpec.makeMeasureSpec(displayWidthPx, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return new Point(textView.getMeasuredWidth(), textView.getMeasuredHeight());
    }

    private boolean canFitInSingleLine(String text) {
        return measureHeightPx(text).y <= singleLineHeightPx;
    }

    /**
     * @return
     */
    public Lines fitToDisplayWidth() {

        final List<Line> trimmedLines = new ArrayList<>();
        final PhraseQueue phraseQueue = new PhraseQueue();

        final List<Line> queuedLines = new ArrayList<>(lines.getLines());
        Line previousChunk = null;
        while (queuedLines.size() > 0) {
            Line line = queuedLines.remove(0);
            List<Phrase> phrases = line.getPhrases();
            int i = 0;

            while (true) {
                Phrase phrase = phrases.get(i);
                String temp;
                boolean canFitInSingleLine = canFitInSingleLine(temp = phraseQueue.getLineText() + phrase.getText());
                if (!canFitInSingleLine) {
                    Timber.d("Pop (too long):");
                    Line newLine = phraseQueue.popAll(line, previousChunk);
                    if (newLine != null) {
                        previousChunk = newLine;
                        trimmedLines.add(newLine);
                    }
                } else {
                    phraseQueue.add(phrase);
                    i++;
                }
                if (i >= phrases.size()) {
                    break;
                }
            }
            Timber.d("Pop (newLine):");
            Line newLine = phraseQueue.popAll(line, previousChunk);
            if (newLine != null) {
                trimmedLines.add(newLine);
            }
            previousChunk = null;
        }

        lines.setLines(trimmedLines);
        invisibleParentLayout.removeAllViews();
        return lines;
    }

    private static String toText(List<Phrase> phrases, Phrase... extras) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Phrase phrase : phrases) {
            stringBuilder.append(phrase.getText());
        }
        for (Phrase phrase : extras) {
            stringBuilder.append(phrase.getText());
        }
        return stringBuilder.toString();
    }

    /**
     * Not thread safe
     */
    class PhraseQueue {
        final LinkedList<Phrase> queue = new LinkedList<>();
        StringBuilder lineTextCache = new StringBuilder();

        void add(Phrase phrase) {
            queue.add(phrase);
            lineTextCache.append(phrase.getText());
        }

        String getLineText() {
            return lineTextCache.toString();
        }

        void clear() {
            queue.clear();
            lineTextCache = new StringBuilder();
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }

        Line popAll(Line currentLine, @Nullable Line previousLine) {
            Line l = null;
            if (!isEmpty()) {

                if (previousLine != null) {
                    LinkedList<Phrase> previous = new LinkedList<>(previousLine.getPhrases());

                    Phrase firstFromPrevious = previous.getFirst();

                    while (previous.size() > 0 && previous.size() > queue.size()) {
                        Phrase fromPrevious = previous.getLast();
                        if (canFitInSingleLine(toText(queue, fromPrevious))) {
                            previous.removeLast();
                            fromPrevious.offset += firstFromPrevious.offset;
                            queue.addFirst(fromPrevious);
                        } else {
                            break;
                        }
                    }
                    previousLine.setPhrases(previous);
                    Timber.d("popAll: previous line reduced to [" + toText(previous) + "]");
                    Timber.d("popAll: phraseQueue is now [" + toText(queue) + "]");
                    lineTextCache = new StringBuilder(toText(queue));
                }


                Timber.d("popAll: adding new line as: [" + getLineText() + "]");
                long firstPhraseOffset = queue.get(0).offset;
                for (Phrase phrase : queue) {
                    phrase.offset -= firstPhraseOffset;
                }
                l = new Line(currentLine.getStartTime() + firstPhraseOffset);
                l.setLineNumber(currentLine.getLineNumber());
                l.setTextColor(currentLine.getTextColor());
                l.setPhrases(new ArrayList<>(queue));
                clear();
            }
            return l;
        }
    }
}
