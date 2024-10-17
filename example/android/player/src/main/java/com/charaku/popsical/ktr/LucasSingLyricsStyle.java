package com.easternblu.khub.ktr;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.LineConfig;
import com.easternblu.khub.ktr.model.LineConfigBuilder;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.view.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * A class to consolidate the custom visual style Popsical content team wanted
 */
public class LucasSingLyricsStyle {
    public final String TAG = this.getClass().getSimpleName();
    private float fontSizeDpToScreenWidthRatio = 0.075f;

    private Context ctx;
    private float displayWidthDp;
    private Typeface robotoFont;

    /**
     * Since module/lib is not allowed to have its own assets
     *
     * @param ctx
     * @param robotoFont
     */
    public LucasSingLyricsStyle(Context ctx, Typeface robotoFont) {
        this.ctx = ctx;
        this.robotoFont = robotoFont;
    }


    private int getTextSizePx(Lines lines, float internalFontScale) {
        return Math.round(Utils.dpToPx(ctx, fontSizeDpToScreenWidthRatio * displayWidthDp) * lines.getFontScale() * internalFontScale);
    }


    private static LineConfig.Prerequisite any() {
        return new LineConfig.Prerequisite() {
            @Override
            public boolean shouldApplyTo(Line line) {
                return true;
            }
        };
    }


    private LineConfigBuilder robotoFontBuilder(Lines lines, LineConfig.Prerequisite prerequisite) {
        float internalFontScale = 1;
        int strokeWidthPx = 0;
        int extraFontDecentPx = strokeWidthPx;
        float horizontalBearingPx = strokeWidthPx / 2;
        float letterSpacing = 0.03f;
        Integer fontStyle = Typeface.NORMAL;
        LineConfigBuilder builder = new LineConfigBuilder(prerequisite);
        builder.setLeftSideBearingPx(horizontalBearingPx)
                .setRightSideBearingPx(horizontalBearingPx)
                .setLetterSpacing(letterSpacing)
                .setLayer0TextColor(Color.WHITE)
                .setLayer0StrokeColor(Color.BLACK)
                .setLayer0StrokeWidthPx(strokeWidthPx)

                .setExtraFontDecentPx(extraFontDecentPx)
                .setLayer1TextColor(lines.getDefaultSingerColor())
                .setLayer1StrokeColor(Color.WHITE)
                .setLayer1StrokeWidthPx(strokeWidthPx)

                .setTextSizePx(getTextSizePx(lines, internalFontScale))
                .setCustomFont(robotoFont)
                .setCustomFontStyle(fontStyle);
        return builder;
    }


    /**
     * Configure the KaraokeLyricsView class to display lyrics
     *
     * @param karaokeLyricsView
     * @param lines
     */
    public Lines setup(KaraokeLyricsView karaokeLyricsView, Lines lines, boolean debug, PointF displaySizeDp, ViewGroup sampleLayout) {


        karaokeLyricsView.reset();
        karaokeLyricsView.setTempoIndicator1Enabled(false);
        karaokeLyricsView.setTempoIndicator2Enabled(false);
        karaokeLyricsView.setVisibility(View.VISIBLE);
        karaokeLyricsView.setAllowCustomBackgroundColor(false);
        for (ViewGroup lineLayout : karaokeLyricsView.getLineLayouts()) {
            if (lineLayout instanceof LinearLayout) {
                ((LinearLayout) lineLayout).setGravity(Gravity.CENTER);
            }
        }
        displayWidthDp = displaySizeDp.x;

        LineConfigBuilder defaultConfigBuilder = robotoFontBuilder(lines, any());
        LineConfig lineConfig = defaultConfigBuilder.createLineConfig();
        new LineSplitter(ctx, sampleLayout, lines, lineConfig, displayWidthDp).fitToDisplayWidth();
        new PhraseJoiner().joinPhrases(lines);


        List<LineConfig> otherLineConfigs = new ArrayList<>();
        otherLineConfigs.add(lineConfig);

        karaokeLyricsView.setDebug(debug);
        karaokeLyricsView.setLines(lines,
                Utils.dpToPx(ctx, displaySizeDp.x),
                Utils.dpToPx(ctx, displaySizeDp.y),
                defaultConfigBuilder.createLineConfig(),
                otherLineConfigs.toArray(new LineConfig[0]));

        return lines;
    }
}
