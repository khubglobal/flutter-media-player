package com.easternblu.khub.ktr;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.view.View;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.LineConfig;
import com.easternblu.khub.ktr.model.LineConfigBuilder;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.ktr.view.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * A class to consolidate the custom visual style Popsical content team wanted
 */
public class PopsicalKaraokeLyricsStyle {
    public final String TAG = this.getClass().getSimpleName();
    private float fontSizeDpToScreenWidthRatio = 1f * 53 / 1280;
    private float strokeWidthDpToScreenWidthRatio = 1f * 6 / 1280;
    private float tempoIndicatorPaddingDpToScreenWidthRatio = 1f * 10 / 1280;

    private Context ctx;
    private float displayWidthDp;
    private Typeface coolVeticaRegular;
    private Typeface cloudYuanCuGBK;

    /**
     * Since module/lib is not allowed to have its own assets, this class required the coolVeticaRegular
     * and cloudYuanCuGBK typeface to work properly
     *
     * @param ctx
     * @param coolVeticaRegular
     * @param cloudYuanCuGBK
     */
    public PopsicalKaraokeLyricsStyle(Context ctx, Typeface coolVeticaRegular, Typeface cloudYuanCuGBK) {
        this.ctx = ctx;
        this.coolVeticaRegular = coolVeticaRegular;
        this.cloudYuanCuGBK = cloudYuanCuGBK;
    }

    private int getStrokeWidthPx() {
        return Math.round(Utils.dpToPx(ctx, strokeWidthDpToScreenWidthRatio * displayWidthDp));
    }


    private int getTextSizePx(Lines lines, float internalFontScale) {
        return Math.round(Utils.dpToPx(ctx, fontSizeDpToScreenWidthRatio * displayWidthDp) * lines.getFontScale() * internalFontScale);
    }


    private int getTempoIndicatorPaddingPx() {
        return Math.round(Utils.dpToPx(ctx, tempoIndicatorPaddingDpToScreenWidthRatio * displayWidthDp));
    }


    private static LineConfig.Prerequisite any() {
        return new LineConfig.Prerequisite() {
            @Override
            public boolean shouldApplyTo(Line line) {
                return true;
            }
        };
    }


    private static LineConfig.Prerequisite ideographicOnly() {
        return new LineConfig.Prerequisite() {
            @Override
            public boolean shouldApplyTo(Line line) {
                int g = 0;
                for (Phrase phrase : line.getPhrases()) {
                    //  Timber.d(phrase.getText() + " ideographic = " + phrase.getIdeographicCount() + " " + phrase.getAlphabetCount());
                    g += (phrase.isIdeographic()) ? 1 : -1;
                }
                boolean ideographic = g > 0;
                //  Timber.d(line.getDisplayText() + " ideographic = " + ideographic + " ");
                return ideographic;
            }
        };
    }


    private static LineConfig.Prerequisite alphabeticOnly() {
        return new LineConfig.Prerequisite() {
            @Override
            public boolean shouldApplyTo(Line line) {
                int g = 0;
                for (Phrase phrase : line.getPhrases()) {
                    g += (phrase.isAlphabetic()) ? 1 : -1;
                }
                boolean alphabetic = g >= 0;
                //Timber.d(line.getDisplayText() + " alphabetic = " + alphabetic);
                return alphabetic;
            }
        };
    }


    private LineConfigBuilder coolvecticaFontBuilder(Lines lines, LineConfig.Prerequisite prerequisite) {

        float internalFontScale = 1;
        int strokeWidthPx = getStrokeWidthPx();
        int extraFontDecentPx = strokeWidthPx;
        float horizontalBearingPx = strokeWidthPx / 2;
        int tempoIndicatorLeftPaddingPx = 0;
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

                .setTempoIndicatorPaddings(new Integer[]{tempoIndicatorLeftPaddingPx, null, null, getTempoIndicatorPaddingPx()})
                .setTextSizePx(getTextSizePx(lines, internalFontScale))
                .setCustomFont(coolVeticaRegular)
                .setCustomFontStyle(fontStyle);
        return builder;
    }


    private LineConfigBuilder cloudYuanGBKFontBuilder(Lines lines, LineConfig.Prerequisite prerequisite) {


        float horizontalBearingPx = 0;
        Integer fontStyle = null;
        int strokeWidthPx = (int) (getStrokeWidthPx() * 0.8f);
        float letterSpacing = 0.15f;
        float internalFontScale = 1.2f;
        int extraFontDecentPx = 0;
        int tempoIndicatorLeftPaddingPx = Utils.dpToPx(ctx, 18f);


        LineConfigBuilder builder = coolvecticaFontBuilder(lines, prerequisite)
                .setLeftSideBearingPx(horizontalBearingPx)
                .setRightSideBearingPx(horizontalBearingPx)
                .setLayer0StrokeWidthPx(strokeWidthPx)
                .setLayer1StrokeWidthPx(strokeWidthPx)
                .setLetterSpacing(letterSpacing)
                .setTextSizePx(getTextSizePx(lines, internalFontScale))
                .setExtraFontDecentPx(extraFontDecentPx)
                .setTempoIndicatorPaddings(new Integer[]{tempoIndicatorLeftPaddingPx, null, null, getTempoIndicatorPaddingPx()})
                .setCustomFont(cloudYuanCuGBK)
                .setCustomFontStyle(fontStyle);

        return builder;
    }




    /**
     * Configure the KaraokeLyricsView class to display lyrics
     *
     * @param karaokeLyricsView
     * @param lines
     */
    public Lines setup(KaraokeLyricsView karaokeLyricsView, Lines lines, boolean debug, PointF displaySizeDp) {
        new PhraseJoiner().joinPhrases(lines);
        new PronunciationConverter(ctx).addPinyin(lines);

        karaokeLyricsView.reset();
        karaokeLyricsView.setVisibility(View.VISIBLE);
        karaokeLyricsView.setAllowCustomBackgroundColor(true);
        displayWidthDp = displaySizeDp.x;

        LineConfigBuilder defaultConfigBuilder;
        String lang = lines.getLang();
        if (lang.toLowerCase().contains("zh")) {
            defaultConfigBuilder = cloudYuanGBKFontBuilder(lines, any());
        } else {
            defaultConfigBuilder = coolvecticaFontBuilder(lines, any());
        }

        List<LineConfig> otherLineConfigs = new ArrayList<>();
        otherLineConfigs.add(coolvecticaFontBuilder(lines, alphabeticOnly()).createLineConfig());
        otherLineConfigs.add(cloudYuanGBKFontBuilder(lines, ideographicOnly()).createLineConfig());


        karaokeLyricsView.setDebug(debug);
        karaokeLyricsView.setLines(lines,
                Utils.dpToPx(ctx, displaySizeDp.x),
                Utils.dpToPx(ctx, displaySizeDp.y),
                defaultConfigBuilder.createLineConfig(),
                otherLineConfigs.toArray(new LineConfig[0]));

        return lines;
    }
}
