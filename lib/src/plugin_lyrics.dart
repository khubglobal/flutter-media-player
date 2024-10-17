import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';

typedef void LyricsViewCreatedCallback(LyricsController controller);

class PluginLyrics extends StatelessWidget {
  final LyricsViewCreatedCallback onLyricsViewCreated;

  PluginLyrics(this.onLyricsViewCreated);
  final String viewType = 'tv.native.player.pops';

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: viewType,
      creationParams: {"isLyrics": true},
      creationParamsCodec: const StandardMessageCodec(),
      onPlatformViewCreated: (viewId) {
        if (onLyricsViewCreated == null) {
          return;
        }
        onLyricsViewCreated(new LyricsController._(viewId));
      },
      hitTestBehavior: PlatformViewHitTestBehavior.translucent,
      gestureRecognizers: const <Factory<OneSequenceGestureRecognizer>>{},
    );
  }
}

class LyricsController {
  LyricsController._(int id)
      : _channel = new MethodChannel('tv.khub/LyricsMethodChannel_$id');

  MethodChannel _channel;

  Future<void> loadLyrics(String lyrics) async {
    return await _channel.invokeMethod('loadLyrics', lyrics ?? '');
  }

  Future<void> setLyricsDuration(int duration) async {
    if (duration > 0) {
      return await _channel.invokeMethod('setLyricsDuration', duration ?? 0);
    }
  }

  Future<void> onPlayerProgressUpdate(int sec) async {
    if (sec > 0) {
      return await _channel.invokeMethod('onPlayerProgressUpdate', sec ?? 0);
    }
  }

  Future<void> onMediaStart() async {
    return await _channel.invokeMethod('onMediaStart');
  }

  Future<void> onMediaResume() async {
    return await _channel.invokeMethod('onMediaResume');
  }

  Future<void> onMediaPause() async {
    return await _channel.invokeMethod('onMediaPause');
  }

  Future<void> resetLyricsView() async {
    return await _channel.invokeMethod('resetLyricsView');
  }

  Future<void> dispose() async {
    if (_channel != null) {
      await _channel.invokeMethod("dispose");
      _channel = null;
    }
  }
}
