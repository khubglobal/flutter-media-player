package com.easternblu.khub.ktr;

import android.content.Context;

import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.Lines;
import com.easternblu.khub.ktr.model.Phrase;
import com.easternblu.khub.ktr.view.Utils;

import java.util.List;

import name.pilgr.pipinyin.PiPinyin;
import timber.log.Timber;

import static com.easternblu.khub.ktr.PronunciationConverter.Mode.Inline;
import static com.easternblu.khub.ktr.PronunciationConverter.Mode.Replace;

/**
 * Created by yatpanng on 22/1/18.
 */

public class PronunciationConverter {
    public static final String TAG = PronunciationConverter.class.getSimpleName();

    public enum Mode {
        Inline, Replace
    }

    // not fully supported for now
    final Mode mode = Inline;
    final PiPinyin zhPinyin;

    public PronunciationConverter(Context ctx) {
        zhPinyin = new PiPinyin(ctx);
    }


    public static boolean hasPinyinSupport(Lines lines) {
        return lines != null && lines.getLang().toLowerCase().contains("zh");
    }


    public void addPinyin(Lines lines) {

        Converter<String, String> converter = null;
        if (hasPinyinSupport(lines)) {
            converter = new ChinesePinyinConverter();
            if (mode == Replace) {
                lines.putMeta(LRCMetaNames.X_POPSICAL_FONT_SCALE, "0.8");
                lines.putMeta(LRCMetaNames.X_POPSICAL_LANG, "ZH_pinyin");
            } else {
                // nothing
            }
        } else {
            // add more converters here
        }


        if (converter != null) {
            int lineNum = 0;
            for (lineNum = 0; lineNum < lines.size(); lineNum++) {
                try {
                    Line line = lines.getLine(lineNum);
                    for (Phrase phrase : line.getPhrases()) {
                        if (!FuriganaStringBuilder.containsFuriganaMarkup(phrase.getText())) {
                            String[] nameAndText = LRC2KTRConverter.splitSingerName(phrase.getText());
                            String name = nameAndText[0], text = nameAndText[1];
                            if (Utils.isNullOrEmpty(name)) {
                                phrase.setText(converter.convert(phrase.getText()), false);
                            } else {
                                phrase.setText(Utils.format("%1$s: %2$s", name, converter.convert(text).trim()), false);
                            }
                        }
                        // Log.d(TAG,line.getFullText());
                    }
                } catch (Throwable t) {
                    Timber.w("PostExecute error on line " + lineNum + " - " + t.getMessage(), t);
                }
            }
        }
    }


    /**
     *
     */
    private class ChinesePinyinConverter implements Converter<String, String> {

        /**
         * @param phraseText
         * @return
         */
        @Override
        public String convert(String phraseText) {
            if (mode == Inline) {
                List<String> chars = Utils.splitByChars(phraseText);
                List<String> pinyins = Utils.splitByString(zhPinyin.toPinyin(phraseText, "#"), '#', true);
                if (pinyins.size() == chars.size()) {
                    FuriganaStringBuilder stringBuilder = new FuriganaStringBuilder();
                    for (int i = 0; i < chars.size(); i++) {
                        String charString = chars.get(i), pinyinString = pinyins.get(i);
                        if (Utils.isNotEmpty(pinyinString) && !pinyinString.equals(charString)) {
                            stringBuilder.append(charString, pinyinString);
                        } else {
                            stringBuilder.append(charString);
                        }
                    }
                    return stringBuilder.toString();
                } else {
                    return phraseText;
                }
            } else {
                return zhPinyin.toPinyin(phraseText, " ") + " ";
            }
        }
    }

    public interface Converter<F, T> {
        public T convert(F from);
    }

}
