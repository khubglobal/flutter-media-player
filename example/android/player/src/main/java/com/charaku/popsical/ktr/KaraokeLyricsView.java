package com.easternblu.khub.ktr;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.LineConfig;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.ktr.view.FuriganaStrokeTextView;
import com.easternblu.khub.ktr.view.LineLayout;
import com.easternblu.khub.ktr.view.State;
import com.easternblu.khub.ktr.view.SweepingStrokeTextViewLayout;
import com.easternblu.khub.ktr.view.TempoIndicatorView;
import com.easternblu.khub.ktr.view.Utils;
import com.easternblu.khub.media.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.easternblu.khub.ktr.view.State.DONE;
import static com.easternblu.khub.ktr.view.State.NOT_INIT;
import static com.easternblu.khub.ktr.view.State.PENDING;
import static com.easternblu.khub.ktr.view.State.SWEEPING;


/**
 * Main view class to display animated karaoke lyrics
 * Supports 2 lines (like most karaoke video)
 * <p>
 * Takes {@link Lines} object which can be obtain from {@link LRC2KTRConverter}
 * <p>
 * Calling orders: <br/>
 * 1) {@link #setLines(Lines, int, int, LineConfig, LineConfig...)}<br/>
 * 2) {@link #setDelegate(PlayerDelegate)}<br/>
 * Lastly call {@link #dequeueLine()} at regular short intervals<br/>
 * <p>
 * Created by pan on 18/5/17.
 */
public class KaraokeLyricsView extends FrameLayout {

    public static final String TAG = KaraokeLyricsView.class.getSimpleName();
    public static final boolean CARD_BACKGROUND = true;
    private boolean debug = false;
    public static final int TEMPO_INDICATOR1_MESSAGE = 1;
    public static final int TEMPO_INDICATOR2_MESSAGE = 2;
    public static final int TEMPO_INDICATOR_DOTS = 4;
    public static final long TEMPO_INDICATOR_DURATION_PER_DOT = 1000L;

    public static final long DEFAULT_END_TITLE_TIME_MS = 8000;

    public static final int MAX_LINES = 2;


    protected long tempoPreviewDuration = 0;
    protected long startDisplayTol = 5000, endDisplayTol = 500;
    protected long startTitleTime = 0, endTitleTime = DEFAULT_END_TITLE_TIME_MS;

    private MainThreadScheduler scheduler;

    protected LineLayout line1Layout;

    protected LineLayout line2Layout;

    protected ViewGroup linesLayout;

    protected LineLayout[] lineLayouts;


    protected TempoIndicatorView tempoIndicator1;
    protected TempoIndicatorView tempoIndicator2;


    protected FuriganaStrokeTextView headerText, footerText;

    protected volatile PlayerDelegate delegate;
    protected volatile boolean allowCustomBackgroundColor = false;
    protected volatile Lines lines;

    @NonNull
    private volatile LineConfig defaultLineConfig;
    private Map<Line, LineConfig> lineConfigs = new HashMap<>();
    protected volatile int lineIndex = 0;
    private boolean furiganaEnabled = true;
    private boolean showTitle = true;

    private LayoutInflater layoutInflater;

    public KaraokeLyricsView(@NonNull Context context) {
        super(context);
        setupView();
    }

    public KaraokeLyricsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public KaraokeLyricsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KaraokeLyricsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    private void setupView() {
        scheduler = new MainThreadScheduler();
        (layoutInflater = LayoutInflater.from(getContext())).inflate(R.layout.view_karaoke_lyrics, this);
        line1Layout = findViewById(R.id.layout_line1);
        line2Layout = findViewById(R.id.layout_line2);
        linesLayout = findViewById(R.id.layout_lines);

        tempoIndicator1 = findViewById(R.id.temp_indicator1);
        tempoIndicator2 = findViewById(R.id.temp_indicator2);

        headerText = findViewById(R.id.textview_header);
        footerText = findViewById(R.id.textview_footer);

        for (ViewGroup lineLayout : getLineLayouts()) {
            lineLayout.setLayoutTransition(null);
        }
        this.setLayoutTransition(null);
        this.setTempoIndicator1Enabled(true);
        this.setTempoIndicator2Enabled(false);

    }

    public void setTempoIndicator2Enabled(boolean enabled) {
        tempoIndicator2.setVisibility(enabled ? View.VISIBLE : View.GONE);
        tempoIndicator2.setEnabled(enabled);
    }

    public boolean isTempoIndicator2Enabled() {
        return tempoIndicator2.isEnabled();
    }


    public void setTempoIndicator1Enabled(boolean enabled) {
        tempoIndicator1.setVisibility(enabled ? View.VISIBLE : View.GONE);
        tempoIndicator1.setEnabled(enabled);
    }


    public boolean isTempoIndicator1Enabled() {
        return tempoIndicator1.isEnabled();
    }


    private TempoIndicatorView getTempoIndicatorView(ViewGroup lineLayout) {
        if (lineLayout.equals(line1Layout)) {
            return tempoIndicator1;

        } else if (lineLayout.equals(line2Layout)) {
            return tempoIndicator2;

        } else {
            throw new IllegalArgumentException("lineLayout is not specified in linelayouts");
        }
    }

    public TempoIndicatorView getTempoIndicator1() {
        return tempoIndicator1;
    }

    public TempoIndicatorView getTempoIndicator2() {
        return tempoIndicator2;
    }

    private boolean isTempoIndicatorEnabled(ViewGroup lineLayout) {
        if (lineLayout.equals(line1Layout)) {
            return isTempoIndicator1Enabled();

        } else if (lineLayout.equals(line2Layout)) {
            return isTempoIndicator2Enabled();

        } else {
            throw new IllegalArgumentException("lineLayout is not specified in linelayouts");
        }
    }


    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public LineLayout[] getLineLayouts() {
        return lineLayouts == null ? (lineLayouts = new LineLayout[]{line1Layout, line2Layout}) : lineLayouts;
    }


    public ViewGroup getLinesLayout() {
        return linesLayout;
    }

    protected void checkState() throws KaraokeLyricsException {
        if (lines == null) {
            throw new KaraokeLyricsException("Must call setLines() first");
        }
        if (delegate == null) {
            throw new KaraokeLyricsException("Must call setDelegate() first");
        }

    }

    /**
     * Callback to sync the view with the media player
     *
     * @param delegate
     */
    public void setDelegate(@NonNull PlayerDelegate delegate) {
        this.delegate = delegate;
    }


    /**
     * Must be called before {@link #dequeueLine()}
     *
     * @param lines
     * @param videoWidthPx
     * @param videoHeightPx
     */
    @MainThread
    public void setLines(@NonNull Lines lines, int videoWidthPx, int videoHeightPx, @NonNull LineConfig defaultLineConfig, LineConfig... otherLineConfigs) {

        for (int lineNumber = 0; lineNumber < MAX_LINES; lineNumber++) {
            getLineLayout(lineNumber).removeAllViews();
            addEmptyTextView(getLineLayout(lineNumber), defaultLineConfig);
        }
        lineConfigs.clear();
        lines.sort();
        this.defaultLineConfig = defaultLineConfig;
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.getLine(i);
            LineConfig lineConfig = this.defaultLineConfig;
            for (LineConfig otherConfig : otherLineConfigs) {
                if (otherConfig.getPrerequisite().shouldApplyTo(line)) {
                    lineConfig = otherConfig;
                }
            }
            lineConfigs.put(line, lineConfig);
        }

        float smallerDimensionDp = Utils.pxToDp(getContext(), Math.min(videoWidthPx, videoHeightPx));
        for (TempoIndicatorView tempoIndicatorView : new TempoIndicatorView[]{getTempoIndicator1(), getTempoIndicator2()}) {
            tempoIndicatorView.setup(smallerDimensionDp);
        }


        Integer[] tempoIndicatorPaddings = defaultLineConfig.getTempoIndicatorPaddings();
        if (tempoIndicatorPaddings != null) {
            Utils.setPaddings(getTempoIndicator1(),
                    tempoIndicatorPaddings[0],
                    tempoIndicatorPaddings[1],
                    tempoIndicatorPaddings[2],
                    tempoIndicatorPaddings[3]);
        }

        this.headerText.setVisibility(View.GONE);
        this.footerText.setVisibility(View.GONE);


        int vPaddingPx = (int) (videoHeightPx * 0.2);

        Utils.setTopMargin(headerText, vPaddingPx);
        Utils.setBottomMargin(footerText, vPaddingPx);


        this.tempoPreviewDuration = Math.abs(lines.getPreviewOffset());

        this.lines = lines;
        this.startTitleTime = 0;
        this.endTitleTime = DEFAULT_END_TITLE_TIME_MS;
        if (lines.size() > 0) {
            long firstLineStartTime = lines.getLine(0).getStartTime();
            firstLineStartTime -= (TEMPO_INDICATOR_DOTS + 1) * TEMPO_INDICATOR_DURATION_PER_DOT;
            endTitleTime = Math.min(firstLineStartTime, 3000);
        }
        float[] margins = lines.getMargins(); // left top right bottom
        int left = (int) (videoWidthPx * margins[0]),
                top = (int) (videoHeightPx * margins[1]),
                right = (int) (videoWidthPx * margins[2]),
                bottom = (int) (videoHeightPx * margins[3]);


        View lyricsContainer = (View) getLinesLayout().getParent();
        Utils.setPaddings(lyricsContainer, left, top, right, bottom);
        Utils.setPaddings(getLinesLayout(), 0);

        @Nullable
        Integer bgColor = lines.getBackgroundColor();
        if (bgColor != null && isAllowCustomBackgroundColor()) {
            if (CARD_BACKGROUND) {
                Utils.setPaddings(lyricsContainer, left / 2, top / 2, right / 2, bottom / 2);
                Utils.setPaddings(getLinesLayout(), left / 2, top / 2, right / 2, bottom / 2);
                getLinesLayout().setBackgroundResource(R.drawable.ktr_lyrics_background);
                if (Build.VERSION.SDK_INT >= 21) {
                    getLinesLayout().setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                    getLinesLayout().setBackgroundTintList(ColorStateList.valueOf(bgColor.intValue()));
                }
            } else {
                getLinesLayout().setBackgroundColor(bgColor.intValue());
            }
        } else {
            getLinesLayout().setBackgroundResource(android.R.color.transparent);
        }


        if (debug) {
            line1Layout.setBackgroundResource(R.drawable.ktr_debug_rect2);
            line2Layout.setBackgroundResource(R.drawable.ktr_debug_rect2);
        } else {
            line1Layout.setBackgroundResource(android.R.color.transparent);
            line2Layout.setBackgroundResource(android.R.color.transparent);
        }
    }

    public boolean isAllowCustomBackgroundColor() {
        return allowCustomBackgroundColor;
    }

    public void setAllowCustomBackgroundColor(boolean allowCustomBackgroundColor) {
        this.allowCustomBackgroundColor = allowCustomBackgroundColor;
    }

    public boolean hasLines() {
        return lines != null;
    }

    public Lines getLines() {
        return lines;
    }

    public long getStartTitleTime() {
        return startTitleTime;
    }

    public void setStartTitleTime(long startTitleTime) {
        this.startTitleTime = startTitleTime;
    }

    public long getEndTitleTime() {
        return endTitleTime;
    }

    public void setEndTitleTime(long endTitleTime) {
        this.endTitleTime = endTitleTime;
    }


    private long getMediaProgress() {
        return delegate == null ? 0 : Math.max(0, delegate.getMediaProgress());
    }


    @Nullable
    private LineLayout getLineLayout(int lineNumber) {
        return getLineLayouts()[toLineId(lineNumber)];
    }


    @Nullable
    private LineLayout nextLineLayout() {
        for (LineLayout lineLayout : getLineLayouts()) {
            State state = lineLayout.getState();
            if (state == NOT_INIT || state == DONE) {
                return lineLayout;
            }
        }
        return null;
    }


    private int toLineId(int lineNumber) {
        return lineNumber % getLineLayouts().length;
    }


    @MainThread
    public synchronized void reset() {
        Timber.d("reset");
        for (LineLayout it : getLineLayouts()) {
            it.reset();
        }
        furiganaEnabled = true;
        showTitle = true;
        headerText.setAndApplyText("");
        footerText.setAndApplyText("");
        lineIndex = 0;
        lines = null;
        defaultLineConfig = null;
        lineConfigs.clear();
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    private void log(String text) {
        if (debug) {
            Timber.d(text);
        }
    }


    /**
     * Should be called periodic (in less than 500ms interval for good effect)
     */
    public synchronized void dequeueLine() {
        if (lines == null) {
            //log("dequeueLine null lines");
            return;
        }
        long progress = getMediaProgress();

        if (headerText.getVisibility() == View.GONE) {
            if (showTitle && startTitleTime < progress && progress <= endTitleTime) {
                showTitle();
            }
        } else {
            if (progress > endTitleTime) {
                hideTitle();
            }
        }

        int currentLineIndex = this.lineIndex;
        log("dequeueLine progress = " + Utils.toTimeText(getMediaProgress()));
        LineLayout lineLayout = nextLineLayout();
        lineLayout = (lineLayout == null) ? getLineLayout(currentLineIndex) : lineLayout;
        State lineLayoutState = lineLayout.getState();


        Line current = null;
        Line previous = null;
        if (0 <= currentLineIndex && currentLineIndex < lines.size()) {
            log("dequeueLine lineIndex = " + currentLineIndex);
            current = lines.getLine(currentLineIndex);
            previous = (currentLineIndex > 0) ? lines.getLine(currentLineIndex - 1) : null;
        }

        if (current == null) {
            return;
        }

        if (isTooSoonForSweeping(current)) {

            log("dequeueLine isTooSoonForSweeping " + current);

        } else if (isTooLateForDisplay(current)) {
            log("dequeueLine isTooLateForDisplay " + current);
            lineIndex++;
            dequeueLine();
        } else {

            // if currently the layout is not displaying any line
            // and the next line is not really THAT far away, we will schedule it for sweeping

            long previewTimeOffset;
            if (previous == null || Math.abs(previous.getEndTime() - current.getStartTime()) > 8000) {
                previewTimeOffset = tempoPreviewDuration + 2000;
            } else {
                previewTimeOffset = 2000;
            }

            // preview is the time that we allow the header and footer to be displayed
            boolean passedPreviewTime = getMediaProgress() > (current.getStartTime() - Math.abs(previewTimeOffset));

            if (lineIndex <= 1) {
                passedPreviewTime = getMediaProgress() > endTitleTime;
            }

            log("dequeueLine: (can schedule sweep) " + current);
            log("dequeueLine: previewTimeOffset = " + previewTimeOffset + ", passedPreviewTime = " + passedPreviewTime);
            log("dequeueLine: lineLayoutState = " + lineLayoutState);
            if (lineLayoutState == DONE) {
                log("dequeueLine DONE");
                // previous sweeping is done, we can schedule sweeping if it is not too far away from display
                if (passedPreviewTime) {
                    log("dequeueLine passedPreviewTime");
                    lineIndex++;
                    scheduleSweeping(previous, current, lineLayout);
                }

            } else if (lineLayoutState == NOT_INIT) {
                log("dequeueLine NOT_INIT");
                if (passedPreviewTime) {
                    log("dequeueLine passedPreviewTime");
                    lineIndex++;
                    scheduleSweeping(previous, current, lineLayout);
                    dequeueLine();
                }
            } else {

                if (current.getStartTime() - getMediaProgress() < 1000) {
                    log("dequeueLine last");
                    if (lineLayoutState == SWEEPING) {
                        log("dequeueLine SWEEPING");
                        lineLayout.cancelSweeping();
                    }
                    // check media progress
                    lineIndex++;
                    scheduleSweeping(previous, current, lineLayout);
                }
            }

        }

    }


    /**
     * Should be called periodic (in less than 500ms interval for good effect)
     */
    public synchronized void dequeueLine0() {
        if (lines == null) {
            //log("dequeueLine null lines");
            return;
        }
        long progress = getMediaProgress();

        if (headerText.getVisibility() == View.GONE) {
            if (showTitle && startTitleTime < progress && progress <= endTitleTime) {
                showTitle();
            }
        } else {
            if (progress > endTitleTime) {
                hideTitle();
            }
        }
        log("dequeueLine progress = " + Utils.toTimeText(getMediaProgress()));
        LineLayout lineLayout = nextLineLayout();
        if (lineLayout == null) {
            lineLayout = getLineLayout(lineIndex);
        }
        State lineLayoutState = lineLayout.getState();


        if (0 <= lineIndex && lineIndex < lines.size()) {
            log("dequeueLine lineIndex = " + lineIndex);

            Line peekLine = lines.getLine(lineIndex);
            Line previousLine = lineIndex > 0 ? lines.getLine(lineIndex - 1) : null;
            if (isTooSoonForSweeping(peekLine)) {
                // if currently the layout is not displaying any line
                // and the next line is not really THAT far away, we will schedule it for sweeping


                long previewTimeOffset;
                if (previousLine == null || Math.abs(previousLine.getEndTime() - peekLine.getStartTime()) > 8000) {
                    previewTimeOffset = tempoPreviewDuration + 2000;
                } else {
                    previewTimeOffset = 2000;
                }


                boolean passedPreviewTime = getMediaProgress() > (peekLine.getStartTime() - Math.abs(previewTimeOffset));
                log("dequeueLine isTooSoonForSweeping " + peekLine + " passedPreviewTime = " + passedPreviewTime + " getMediaProgress() = " + Utils.toTimeText(getMediaProgress()) + " previewTimeOffset = " + Utils.toTimeText(previewTimeOffset));
                if (lineIndex <= 1) {
                    passedPreviewTime = getMediaProgress() > endTitleTime;
                }
                if (lineLayoutState == DONE) {
                    log("dequeueLine DONE");
                    // previous sweeping is done, we can schedule sweeping if it is not too far away from display
                    if (passedPreviewTime) {
                        log("dequeueLine passedPreviewTime");
                        lineIndex++;
                        scheduleSweeping(previousLine, peekLine, lineLayout);
                    }

                } else if (lineLayoutState == NOT_INIT) {
                    log("dequeueLine NOT_INIT");
                    if (passedPreviewTime) {
                        log("dequeueLine passedPreviewTime");
                        lineIndex++;
                        scheduleSweeping(previousLine, peekLine, lineLayout);
                        dequeueLine();
                    }
                } else {

                }

            } else if (isTooLateForDisplay(peekLine)) {
                log("dequeueLine isTooLateForDisplay");
                lineIndex++;
                dequeueLine();
            } else {
                log("dequeueLine last");
                if (lineLayoutState == SWEEPING) {
                    log("dequeueLine SWEEPING");
                    lineLayout.cancelSweeping();
                }
                // check media progress
                lineIndex++;
                scheduleSweeping(previousLine, peekLine, lineLayout);
            }

        }

    }


    private boolean isTooSoonForSweeping(Line line) {
        return getMediaProgress() < (line.getStartTime() - startDisplayTol);
    }


    private boolean isTooLateForDisplay(Line line) {
        return getMediaProgress() > (line.getEndTime() + endDisplayTol);
    }


    public void showTitle() {
        String title = Utils.getFirstNotNull(lines.getTitle(), "");
        String artist = Utils.getFirstNotNull(lines.getArtist(), "");

        LineConfig config = defaultLineConfig;
        if (config != null) {
            Timber.w("header and footer is applying applyLineConfig");
            applyLineConfig(config);
        } else {
            Timber.w("Line 0 has no line applyLineConfig");
        }

        headerText.setBackgroundResource(isDebug() ? R.drawable.ktr_debug_rect : android.R.color.transparent);
        footerText.setBackgroundResource(isDebug() ? R.drawable.ktr_debug_rect : android.R.color.transparent);

        headerText.setAndApplyText(title);
        footerText.setAndApplyText(artist);
        headerText.setVisibility(View.VISIBLE);
        footerText.setVisibility(View.VISIBLE);
    }

    private void hideTitle() {
        headerText.setVisibility(View.GONE);
        footerText.setVisibility(View.GONE);
    }


    private void applyLineConfig(LineConfig config) {
        config.applyTo(headerText, LineConfig.LAYER_1 | LineConfig.REVERSE_STROKE_AND_TEXT_COLOR, 1.3f);
        config.applyTo(footerText, LineConfig.LAYER_0, 1.2f);
    }


    @MainThread
    private void scheduleSweeping(Line previousLine, Line line, final LineLayout lineLayout) {
        lineLayout.removeAllViews();
        LineConfig lineConfig = getLineConfig(line);
        if (lineConfig != null) {
            lineLayout.setBackgroundColor(lineConfig.getBackgroundColor());
        }

        lineLayout.setState(PENDING);
        lineLayout.setVisibility(View.VISIBLE);
        lineLayout.setCurrent(line);
        lineLayout.setPrevious(previousLine);

        List<Phrase> phrases = line.getPhrases();
        if (isTempoIndicatorEnabled(lineLayout)) {
            maybeScheduleTempoIndicator(previousLine, line, getTempoIndicatorView(lineLayout));
        }

        //Timber.d("scheduleSweeping: " + line);

        for (int i = 0; i < phrases.size(); i++) {
            Phrase phrase = phrases.get(i);
            if (phrase.getText() == null || phrase.getText().trim().isEmpty()) {
                continue;
            }
            final boolean last = (i == phrases.size() - 1), first = i == 0;

            if (first) {
                // first word of a line might (or might not) start with "X: lyric1..."
                // splitSingerName will split the string to {"X:", " lyric1..."}

                String[] nameAndWord = LRC2KTRConverter.splitSingerName(phrase.getText());
                //Timber.d("scheduleSweeping: phrase.getText() = " + phrase.getText() + " " + Arrays.toString(nameAndWord));

                if (Utils.isNotEmpty(nameAndWord[0])) {
                    String singer = nameAndWord[0] + ":";
                    phrase.setText(nameAndWord[1], false);
                    Phrase singerPhrase = new Phrase(singer, 0, 1);
                    SweepingStrokeTextViewLayout sweepingTextView = addSweepingTextView(line, singerPhrase, i, lineLayout, lineConfig);
                    sweepingTextView.setDelegate(delegate);
                    sweepingTextView.inverseLayer1Color();
                    sweepingTextView.setListener(new SweepingStrokeTextViewLayout.Listener() {
                        @Override
                        public void onStateChanged(State state) {

                        }
                    });
                    sweepingTextView.updateAsDone();
                }
            }


            SweepingStrokeTextViewLayout sweepingTextView = addSweepingTextView(line, phrase, i, lineLayout, lineConfig);
            sweepingTextView.setDelegate(delegate);
            if (debug) {
                sweepingTextView.setBackgroundResource(R.drawable.ktr_debug_rect);
            }
            sweepingTextView.setListener(new SweepingStrokeTextViewLayout.Listener() {

                final Utils.Lambda<Void, Lines> getLines = new Utils.Lambda<Void, Lines>() {
                    @Override
                    public Lines invoke(Void aVoid) {
                        return lines;
                    }
                };

                @Override
                public void onStateChanged(State state) {
                    if (last) {
                        if (state == DONE) {
                            lineLayout.setState(DONE);
                            lineLayout.clearAfter(scheduler, 1000, getLines);
                        }
                    }
                    if (first) {
                        if (state == SWEEPING) {
                            lineLayout.setState(SWEEPING);
                        }
                    }
                }
            });
            sweepingTextView.scheduleSweeping();
        }
    }

    private void maybeScheduleTempoIndicator(Line previousLine, Line line, final TempoIndicatorView tempoIndicatorView) {
        if (line == null || Utils.isNullOrEmpty(line.getPhrases()) || (previousLine != null && (line.getStartTime() - previousLine.getEndTime()) < tempoPreviewDuration)) {
            return;
        }
        long timeUntilStarts = line.getStartTime() - getMediaProgress();

        final int dots = TEMPO_INDICATOR_DOTS; // default
        final long tempo = TEMPO_INDICATOR_DURATION_PER_DOT;
        boolean firstLine = previousLine == null;

        long timeRequiredForTempoIndicator = tempoPreviewDuration; // 10s
        if (firstLine) {
            timeRequiredForTempoIndicator = dots * tempo;
        }

        if (timeUntilStarts >= timeRequiredForTempoIndicator) {
            long delay = timeUntilStarts - (dots * tempo);
            scheduler.cancelPendingSchedule(TEMPO_INDICATOR1_MESSAGE);
            scheduler.schedule(TEMPO_INDICATOR1_MESSAGE, delay - 500, new Runnable() {
                @Override
                public void run() {
                    tempoIndicatorView.start(tempo, dots, delegate);
                }
            });
        } else {
            if (firstLine) {
                Timber.w("First line not qualify for tempo. timeUntilStarts = " + timeUntilStarts + " timeRequiredForTempoIndicator = " + timeRequiredForTempoIndicator);
            }
        }
    }


    public void pause() {
        pause(line1Layout);
        pause(line2Layout);
    }


    private void pause(ViewGroup lineLayout) {
        for (int i = 0; i < lineLayout.getChildCount(); i++) {
            View c = lineLayout.getChildAt(i);
            if (c instanceof SweepingStrokeTextViewLayout) {
                SweepingStrokeTextViewLayout sstvl = (SweepingStrokeTextViewLayout) c;
                boolean success = sstvl.pauseSweeping();
                // Timber.d("pause : success = " + success + " " + (sstvl.getText()));
            }
        }
    }


    public void resume() {
        resume(line1Layout);
        resume(line2Layout);
    }

    private void resume(ViewGroup lineLayout) {
        for (int i = 0; i < lineLayout.getChildCount(); i++) {
            View c = lineLayout.getChildAt(i);
            if (c instanceof SweepingStrokeTextViewLayout) {
                SweepingStrokeTextViewLayout sstvl = (SweepingStrokeTextViewLayout) c;
                boolean success = sstvl.resumeSweeping();
                //Timber.d("resume : success = " + success + " " + (sstvl.getText()));
            }
        }
    }

    public boolean isFuriganaEnabled() {
        return furiganaEnabled;
    }

    public void setFuriganaEnabled(boolean furiganaEnabled) {
        this.furiganaEnabled = furiganaEnabled;
    }

    private SweepingStrokeTextViewLayout addSweepingTextView(Line line, Phrase phrase, int phrasePosition, ViewGroup lineLayout, LineConfig lineConfig) {
        SweepingStrokeTextViewLayout textView = new SweepingStrokeTextViewLayout(getContext());
        textView.setFuriganaEnabled(isFuriganaEnabled());
        textView.applyLineConfig(lineConfig);
        textView.setLetterSpacing(lineConfig.getLetterSpacing());
        textView.setPhrase(line, phrase, phrasePosition);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        lineLayout.addView(textView);
        return textView;
    }

    private SweepingStrokeTextViewLayout addEmptyTextView(ViewGroup lineLayout, LineConfig lineConfig) {
        SweepingStrokeTextViewLayout textView = new SweepingStrokeTextViewLayout(getContext());
        textView.setFuriganaEnabled(isFuriganaEnabled());
        textView.applyLineConfig(lineConfig);
        textView.setLetterSpacing(lineConfig.getLetterSpacing());
        textView.setColorAndText(Color.WHITE, " ");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        lineLayout.addView(textView);
        return textView;
    }


    @NonNull
    public LineConfig getLineConfig(Line line) {
        LineConfig lineConfig = lineConfigs.get(line);
        return lineConfig == null ? defaultLineConfig : lineConfig;
    }


    public FuriganaStrokeTextView getHeaderText() {
        return headerText;
    }

    public FuriganaStrokeTextView getFooterText() {
        return footerText;
    }
}
