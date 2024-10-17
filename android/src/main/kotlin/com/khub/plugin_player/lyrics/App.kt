package com.khub.plugin_player.lyrics
import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

abstract class App {

    companion object {
        lateinit  var appContext: Context
        @Volatile
        lateinit var resources: Resources
        fun setContext(con: Context) {
            appContext=con
            resources=appContext.resources
        }
        fun getStringRes(@StringRes res: Int, vararg args: Any): String {
            return resources.getString(res, *args)
        }
    }
}