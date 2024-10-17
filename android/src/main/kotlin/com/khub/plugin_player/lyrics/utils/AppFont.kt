package com.khub.plugin_player.lyrics.utils
import android.content.Context
import android.graphics.Typeface
import androidx.annotation.StringRes
import android.widget.TextView
import com.khub.plugin_player.R
import com.khub.plugin_player.lyrics.App
import uk.co.chrisjenx.calligraphy.TypefaceUtils

interface AppFont {
    val assetPath: String
}



class ResourceString(@StringRes val stringRes: Int) : AppFont {
    override val assetPath: String
        get() {
            return App.getStringRes(stringRes)
        }
}



val DEFAULT_FONT = ResourceString(R.string.font_default)
val BOLD_FONT = ResourceString(R.string.font_bold)
val MEDIUM_FONT = ResourceString(R.string.font_medium)

fun TextView.setFont(fontAssetPath: String) {
    getFont(context, fontAssetPath)?.let {
        this.typeface = it
    }
}

fun TextView.setFont(appFont: AppFont) {
    setFont(appFont.assetPath)
}

fun getFont(ctx: Context?, appFont: AppFont): Typeface? {
    return getFont(ctx, appFont.assetPath)
}

fun getFont(ctx: Context?, fontAssetPath: String): Typeface? {
    return ctx?.let { TypefaceUtils.load(it.assets, fontAssetPath) }
}
