package com.khub.plugin_player
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin


/** PluginPlayerPlugin */
class PluginPlayerPlugin: FlutterPlugin {

  override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    binding.platformViewRegistry.registerViewFactory("tv.native.player.pops", NativeViewFactory(binding.binaryMessenger))
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {

  }

}
