package com.khub.plugin_player.lyrics
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import com.easternblu.khub.common.util.SizeF
import com.easternblu.khub.ktr.KaraokeLyricsView
import com.easternblu.khub.ktr.LRC2KTRConverter
import com.easternblu.khub.ktr.PlayerDelegate
import com.easternblu.khub.ktr.PopsicalKaraokeLyricsStyle
import com.easternblu.khub.ktr.model.Lines
import com.pixplicity.easyprefs.library.Prefs
import com.khub.plugin_player.R
import com.khub.plugin_player.lyrics.App.Companion.resources
import com.khub.plugin_player.lyrics.utils.AppPrefs
import com.khub.plugin_player.lyrics.utils.ContentLanguageUtil
import com.khub.plugin_player.lyrics.utils.Views
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class LyricsView(context: Context?, id: Int, messenger: BinaryMessenger?) : PlatformView, MethodCallHandler, SharedPreferences.OnSharedPreferenceChangeListener {


    private var lines: Lines? = null
    private val ctx: Context? = context
    private var mView: View = LayoutInflater.from(context).inflate(R.layout.activity_main, null)
    private var karaokeLyricsView: KaraokeLyricsView = mView.findViewById(R.id.karaoke_lyrics_view)
    private var lyricSpeed: Double = 1.0
    private var duration: Long = 0
    var lastProgressUpdate = 0
    private var playing = false

    init {
        MethodChannel(messenger, "tv.khub/LyricsMethodChannel_$id")
                .setMethodCallHandler(this)
        context?.let { App.setContext(it) }
        initEasyPrefs()
        initTimber()
        CalligraphyConfig.initDefault(
                CalligraphyConfig.Builder()
                        .setDefaultFontPath(Views.APP_FONT_DEFAULT)
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        )
        mView.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun getView(): View {
        return mView
    }


    private fun loadLyrics(lyrics: String) {
        lines = lrcParser(lyrics);
        // if there is lyrics line we show it
        lines?.let { lines ->
            Timber.d("setupLyricsView: has lines = ${lines}")
            resetLyricsView()
            setupLyricsView(lines)
        } ?: run {
            Timber.d("setupLyricsView: no lines = ${lines}")
            resetLyricsView()
        }
    }


    private fun setupLyricsView(lines: Lines, showTitle: Boolean = true) {
        try {

            val debugLyricsRendering = Prefs.getBoolean(AppPrefs.PREFS_DEBUG_LYRICS_RENDERER, false)
            PopsicalKaraokeLyricsStyle(
                    ctx,
                    Views.loadTypeface(ctx, Views.COOLVETICA_REGULAR),
                    Views.loadTypeface(ctx, Views.SHARP_STD_CLOUD_YUAN_CU_GBK)).setup(karaokeLyricsView, lines, debugLyricsRendering, getScreenSizeDp(
                    resources
            ))

            karaokeLyricsView.isFuriganaEnabled = PrefsKt.lyricsFurigana
            karaokeLyricsView.isShowTitle = showTitle
            playing = true
            karaokeLyricsView.setDelegate(object : PlayerDelegate {
                private var lastCurrentPosition: Long = -1
                private var lastCurrentPositionUptimeMillis: Long = -1
                override fun getMediaProgress(): Long {
                    if (!isPlaying) {
                        return lastProgressUpdate.toLong()
                    }
                    var currentPosition = lastProgressUpdate.toLong()
                    if (lastCurrentPosition == currentPosition) {
                        // adjust for late currentPosition
                        val adjustment = SystemClock.uptimeMillis() - lastCurrentPositionUptimeMillis
                        currentPosition += adjustment
                    } else {
                        lastCurrentPositionUptimeMillis = SystemClock.uptimeMillis()
                        lastCurrentPosition = currentPosition
                    }
                    return currentPosition
                }

                override fun getMediaDuration(): Long {
                    return duration
                }

                override fun isPlaying(): Boolean {
                    return playing
                }

                override fun getSpeed(): Double {
                    return lyricSpeed
                }
            })

        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    private fun setLyricsSpeed(sp: Double?) {
        if (sp != null && sp > 0) {
            lyricSpeed = sp
            Timber.d("setLyricsSpeed = ${lyricSpeed}")
        } else {
            lyricSpeed = 1.0
        }

    }

    private fun setLyricsDuration(dur: Long?) {
        duration = dur ?: 0
        Timber.d("setLyricsDuration = ${duration}")
    }

    private fun onMediaStart() {
        if (karaokeLyricsView.hasLines()) {
            karaokeLyricsView.resume()
            playing = true
        }
    }

    private fun onMediaResume() {
        if (karaokeLyricsView.hasLines()) {
            karaokeLyricsView.resume()
            playing = true
        }

    }

    private fun onMediaPause() {
        if (karaokeLyricsView.hasLines()) {
            karaokeLyricsView.pause()
            playing = false
        }
    }


    private fun onPlayerProgressUpdate(currentPosition: Int) {
        if (currentPosition >= 0) {
            karaokeLyricsView.dequeueLine()
        }
    }

    val lrcParser: (String) -> Lines = { lrc ->
        LRC2KTRConverter().convert(ContentLanguageUtil.format(PrefsKt.lyricsLangConversionType, lrc))
    }

    private fun resetLyricsView() {
        karaokeLyricsView.reset()
        karaokeLyricsView.visibility = View.GONE
        playing = false

    }

    private fun getScreenSizeDp(resources: Resources): SizeF? {
        val displayMetrics = resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return SizeF(dpWidth, dpHeight)
    }

    override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            prefName: String
    ) {
        if (AppPrefs.SHARE_PREF_LISTENER_ENABLED) {
            Timber.d("onSharedPreferenceChanged: prefName = $prefName")

        }
    }

    private fun initEasyPrefs() {
        Prefs.Builder()
                .setContext(ctx)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(ctx?.packageName)
                .setUseDefaultSharedPreference(true)
                .build()
        Prefs.getPreferences().registerOnSharedPreferenceChangeListener(this)
    }

    private fun initTimber() {
        Timber.plant(
                Timber.DebugTree()
        )
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

        when (call.method) {
            "loadLyrics" -> {
                loadLyrics(call.arguments as String)
                result.success(null)
            }
            "onMediaStart" -> {
                onMediaStart()
                result.success(null)
            }
            "onMediaResume" -> {
                onMediaResume()
                result.success(null)
            }
            "onMediaPause" -> {
                onMediaPause()
                result.success(null)
            }
            "onPlayerProgressUpdate" -> {
                lastProgressUpdate = call.arguments as Int
                onPlayerProgressUpdate(lastProgressUpdate)
                result.success(null)
            }
            "resetLyricsView" -> {
                resetLyricsView()
                result.success(null)
            }
            "setLyricsSpeed" -> {
                setLyricsSpeed(call.arguments as Double)
                result.success(null)
            }
            "setLyricsDuration" -> {
                var duration: Int = call.arguments as Int
                setLyricsDuration(duration.toLong())
                result.success(null)
            }
            "dispose" -> {
                dispose()
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }


    override fun dispose() {
        resetLyricsView()
    }
}