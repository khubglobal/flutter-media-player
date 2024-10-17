// import 'package:flutter/foundation.dart';
// import 'package:flutter/gestures.dart';
// import 'package:flutter/material.dart';
// import 'package:flutter/rendering.dart';
// import 'package:flutter/services.dart';

// typedef void LyricsViewCreatedCallback(LyricsController controller);

// class PluginPlayer extends StatelessWidget {
//   final LyricsViewCreatedCallback onLyricsViewCreated;

//   PluginPlayer(this.onLyricsViewCreated);
//   final String viewType = 'tv.native.player.pops';
//   final Map<String, dynamic> creationParams = <String, dynamic>{
//     "isLyrics": true
//   };
//   @override
//   Widget build(BuildContext context) {
//     return PlatformViewLink(
//       viewType: viewType,
//       surfaceFactory:
//           (BuildContext context, PlatformViewController controller) {
//         return AndroidViewSurface(
//           controller: controller,
//           gestureRecognizers: const <Factory<OneSequenceGestureRecognizer>>{},
//           hitTestBehavior: PlatformViewHitTestBehavior.opaque,
//         );
//       },
//       onCreatePlatformView: (PlatformViewCreationParams params) {
//         return PlatformViewsService.initSurfaceAndroidView(
//           id: params.id,
//           viewType: viewType,
//           layoutDirection: TextDirection.ltr,
//           creationParams: creationParams,
//           creationParamsCodec: StandardMessageCodec(),
//         )
//           ..addOnPlatformViewCreatedListener(params.onPlatformViewCreated)
//           ..addOnPlatformViewCreatedListener((viewId) {
//             if (onLyricsViewCreated == null) {
//               return;
//             }
//             onLyricsViewCreated(new LyricsController._(viewId));
//           })
//           ..create();
//       },
//     );
//   }
// }

// class LyricsController {
//   LyricsController._(int id)
//       : _channel = new MethodChannel('tv.khub/LyricsMethodChannel_$id');

//   MethodChannel _channel;

//   Future<void> loadLyrics(String lyrics) async {
//     return await _channel.invokeMethod('loadLyrics', lyrics ?? '');
//   }

//   Future<void> setLyricsDuration(int duration) async {
//     if (duration > 0) {
//       return await _channel.invokeMethod('setLyricsDuration', duration ?? 0);
//     }
//   }

//   Future<void> onPlayerProgressUpdate(int sec) async {
//     if (sec > 0) {
//       return await _channel.invokeMethod('onPlayerProgressUpdate', sec ?? 0);
//     }
//   }

//   Future<void> onMediaStart() async {
//     return await _channel.invokeMethod('onMediaStart');
//   }

//   Future<void> onMediaResume() async {
//     return await _channel.invokeMethod('onMediaResume');
//   }

//   Future<void> onMediaPause() async {
//     return await _channel.invokeMethod('onMediaPause');
//   }

//   Future<void> resetLyricsView() async {
//     return await _channel.invokeMethod('resetLyricsView');
//   }

//   Future<void> dispose() async {
//     if (_channel != null) {
//       await _channel.invokeMethod("dispose");
//       _channel = null;
//     }
//   }
// }
