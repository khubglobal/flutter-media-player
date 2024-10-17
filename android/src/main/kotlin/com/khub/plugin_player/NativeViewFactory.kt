package com.khub.plugin_player
import android.content.Context
import com.khub.plugin_player.lyrics.LyricsView
import com.khub.plugin_player.video.PlayerView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import org.json.JSONException

internal class NativeViewFactory( messenger: BinaryMessenger) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    private val messenger: BinaryMessenger? = messenger
    private var isLyrics = false
    private var playerView: PlayerView? = null

    override fun create(context: Context, id: Int, args: Any?): PlatformView {
        val creationParams = args as Map<String?, Any?>?
        try {
            isLyrics = creationParams?.get("isLyrics") as Boolean? ?: false
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return if (isLyrics) {
             LyricsView(context, id, messenger)
        } else {
            playerView = PlayerView(context, id, messenger, creationParams)
            playerView!!
        }
    }

}