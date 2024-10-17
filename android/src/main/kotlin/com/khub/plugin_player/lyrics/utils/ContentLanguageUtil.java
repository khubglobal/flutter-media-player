package com.khub.plugin_player.lyrics.utils;


import com.easternblu.khub.common.util.Strings;
import com.khub.plugin_player.lyrics.App;

/**
 * for converting content from server to simplified chinese or traditional chinese based on language preference
 * Created by pan on 8/6/17.
 */
public class ContentLanguageUtil {
    public static final String TAG = ContentLanguageUtil.class.getSimpleName();


    public static final boolean ENABLED = true;


    private static int conversionType = ZHConverter.NONE;

    /**
     * Init the {@link ContentLanguageUtil} for converting content from server to simplified chinese or traditional chinese based on language preference
     *
     * @param languageTag
     */
    public static void reinit(String languageTag) {
        if (languageTag != null) {
            if (languageTag.equals("zh-CN")) {
                ContentLanguageUtil.setDefaultConversionType(ZHConverter.SIMPLIFIED);
            } else if (languageTag.equals("zh-TW")) {
                ContentLanguageUtil.setDefaultConversionType(ZHConverter.TRADITIONAL);
            } else {
                ContentLanguageUtil.setDefaultConversionType(ZHConverter.NONE);
            }
        }
    }


    public static int getDefaultConversionType() {
        return conversionType;
    }

    public static void setDefaultConversionType(@ZHConverter.ConversionType int defaultConversionType) {
        ContentLanguageUtil.conversionType = defaultConversionType;
    }

    public static String format(String originalText) {
        // return originalText;
        return format(conversionType, originalText);
    }

    public static String format(@ZHConverter.ConversionType int conversionType, String originalText) {
        if (ENABLED && Strings.isNotEmpty(originalText)) {
            try {
                return ZHConverter.convert(App.appContext, originalText, conversionType);
            } catch (Throwable t) {
                return originalText;
            }
        } else {
            return originalText;
        }
    }


}