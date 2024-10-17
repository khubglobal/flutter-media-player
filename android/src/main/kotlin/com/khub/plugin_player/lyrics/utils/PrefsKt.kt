

import com.pixplicity.easyprefs.library.Prefs
import com.khub.plugin_player.lyrics.utils.AppPrefs
import com.khub.plugin_player.lyrics.utils.ZHConverter


object PrefsKt : AppPrefs() {
    val PREF_LYRICS_LANG_CONVERSION_TYPE = "PREF_LYRICS_LANG_CONVERSION_TYPE"
    val PREF_LYRICS_FURIGANA_ENABLED = "PREF_LYRICS_FURIGANA_ENABLED"

    var lyricsLangConversionType: Int
        get() = Prefs.getInt(PREF_LYRICS_LANG_CONVERSION_TYPE, ZHConverter.NONE)
        set(value) = Prefs.putInt(PREF_LYRICS_LANG_CONVERSION_TYPE, value)
    var lyricsFurigana: Boolean
        get() = Prefs.getBoolean(PREF_LYRICS_FURIGANA_ENABLED, true)
        set(value) = Prefs.putBoolean(PREF_LYRICS_FURIGANA_ENABLED, value)
}