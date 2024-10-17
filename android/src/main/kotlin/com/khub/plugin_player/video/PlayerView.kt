package com.khub.plugin_player.video
import android.content.Context
import android.view.View
import androidx.annotation.Nullable
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView

class PlayerView(context: Context, id: Int, messenger: BinaryMessenger?,
                 @Nullable creationParams: Map<String?, Any?>?) : PlatformView, MethodCallHandler {

    private var player: PopsicalPlayerLayout = PopsicalPlayerLayout(context, messenger, id, creationParams)


    override fun getView(): View {
        return player
    }

    override fun dispose() {player.onDestroy()}

    init {
        MethodChannel(messenger, "tv.khub/NativeVideoPlayerMethodChannel_$id")
                .setMethodCallHandler(this)
    }
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "onMediaChanged" -> {
                player.onMediaChanged(call.arguments)
                result.success(true)
            }
            "playPause" -> {
//                player.playPause()
                result.success(true)
            }
            "seekTo" -> {
                player.seekTo(call.arguments)
                result.success(true)
                val speed: String? = call.argument<Any?>("speed") as String?
//                speed?.let { player.setPlaybackSpeed(it?.toFloat()) }
                result.success(true)
            }
            "setPlaybackSpeed" -> {
                val speed: String? = call.argument<Any?>("speed") as String?
//                speed?.let { player.setPlaybackSpeed(it.toFloat()) }
                result.success(true)
            }
            "setPitch" -> {
                val pitch: String? = call.argument<Any?>("pitch") as String?
//                pitch?.let { player.setPitch(it.toFloat()) }
                result.success(true)
            }
//            "getPitch" -> result.success(player.pitch)
//            "getPlaybackSpeed" -> result.success(player.playbackSpeed)
            "setPreferredAudioLanguage" -> {
                player.setPreferredAudioLanguage(call.arguments)
                result.success(true)
            }
            "dispose" -> {
                dispose()
                result.success(true)
            }
            "setLooping" -> {
//                player.setLooping(call.arguments)
                result.success(true)
            }
            else -> result.notImplemented()
        }
    }

}