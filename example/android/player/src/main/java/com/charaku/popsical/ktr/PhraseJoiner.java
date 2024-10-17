package com.easternblu.khub.ktr;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.ktr.view.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * When consecutive {@link Phrase} in a {@link Line} have more or less the same "average time per letter" <br/>
 * Then it actually makes sense to {@link #join(Phrase, Phrase)} 2 phrases into one for better performance
 * <p>
 * Created by pan on 20/12/17.
 */

public class PhraseJoiner implements LRCMetaNames {
    public static final String TAG = PhraseJoiner.class.getSimpleName();
    private static final boolean DEBUG = false;
    private long maxJoinGapMs = 500;
    private float maxJoinADPLCoef = 0.75f; // ADPL = Avg Duration Per Letter, so if phrase2.adpl is > maxJoinADPLCoef*phrase1.adpl then we don't postExecute
    private int totalLettersCount = 0;
    private long totalDurationMs = 0;
    private Map<Phrase, Meta> metas = new HashMap<>();

    public void joinPhrases(Lines lines) {
        maxJoinGapMs = lines.getLong(X_POPSICAL_MAX_JOIN_GAP_MS, maxJoinGapMs);
        maxJoinADPLCoef = lines.getFloat(X_POPSICAL_MAX_JOIN_ADPL_COEF, maxJoinADPLCoef);

        for (int i = 0; i < lines.size(); i++) {
            obtainStats(lines.getLine(i));
        }
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.getLine(i);
            String before = line.getDisplayText();
            join(lines.getLine(i));
            d(before + " -> " + line.getDisplayText());

        }
        return;
    }

    private void d(String text) {
        if (DEBUG) {
            Timber.d(text);
        }
    }

    private void join(Line line) {
        Phrase current = null;
        List<Phrase> phrases = new ArrayList<>();
        int i = 0;
        for (Phrase phrase : line.getPhrases()) {
            if (current == null) {
                current = phrase;
                phrases.add(current);
                continue;
            }
            if (canJoin(current, phrase)) {
                join(current, phrase);
            } else {
                current = phrase;
                phrases.add(current);
            }
        }
        line.setPhrases(phrases);
    }

    private void join(Phrase phrase1, Phrase phrase2) {
        //Log.d(TAG,"join:["+phrase1.shortForm()+"]["+phrase2.shortForm()+"]");
        phrase1.appendText(phrase2.getText());
        Meta meta2 = metas.get(phrase2);
        if (meta2 != null && meta2.letterCount > 0) {
            phrase1.duration = ((phrase2.offset + phrase2.duration) - phrase1.offset);
        }
        Meta meta = calculateMeta(phrase1);
        metas.put(phrase1, meta);
    }

    private boolean canJoin(Phrase phrase1, Phrase phrase2) {
        Meta meta1 = metas.get(phrase1), meta2 = metas.get(phrase2);
        float temp1, temp2, temp3;

        if (phrase2.getText().trim().length() == 0) {
            return true;
        } else if ((temp1 = Math.abs(phrase1.offset + phrase1.duration - phrase2.offset)) > maxJoinGapMs) {
            return false;
        } else if ((temp1 =
                Math.abs((temp2 = meta1.avgDurationPerLetterMs) - (temp3 = meta2.avgDurationPerLetterMs))) > meta1.avgDurationPerLetterMs * maxJoinADPLCoef) {
            return false;
        } else {
            return true;
        }
    }


    private void obtainStats(Line line) {
        for (Phrase phrase : line.getPhrases()) {
            Meta meta = calculateMeta(phrase);
            metas.put(phrase, meta);
            if (meta.letterCount > 0) {
                totalDurationMs += phrase.duration;
                totalLettersCount += meta.letterCount;
            }
        }
    }


    /**
     * Return the meta data of a phrase
     *
     * @param phrase
     * @return
     */
    public static Meta calculateMeta(Phrase phrase) {
        int lettersCount = 0;

        // cater for furigana markup
        boolean insideBracket = false, afterSemiColon = false;
        for (char c : phrase.getText().toCharArray()) {
            if (c == '}') {
                insideBracket = false;
                afterSemiColon = false;
            } else if (c == '{') {
                insideBracket = true;
            } else if (insideBracket && c == ';') {
                afterSemiColon = true;
            } else if (afterSemiColon && insideBracket) {
                continue;
            } else {
                lettersCount += (Character.isLetter(c) ? 1 : 0);
            }
        }
        Meta meta = new Meta();
        meta.letterCount = lettersCount;
        meta.avgDurationPerLetterMs = lettersCount == 0 ? 0 : (1f * phrase.duration / lettersCount);
        return meta;
    }


    /**
     * Meta information about a phrase
     */
    static class Meta {
        int letterCount;
        float avgDurationPerLetterMs = 0;

        public String shortForm() {
            return Utils.format("lc=%1$s,adpl=%2$s", String.valueOf(letterCount), String.valueOf(avgDurationPerLetterMs));
        }
    }
}
