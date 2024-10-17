import 'package:flutter/services.dart';

import 'video_event.dart';

/// Use with Video or Audio widget to get player notifications such as
/// [onPlay], [onPause] etc. See example on how to use.
mixin PlayerObserver {
  Future<void> listenForVideoPlayerEvents(int viewId) async {
    EventChannel eventChannel = EventChannel(
        "tv.khub/NativeVideoPlayerEventChannel_$viewId", JSONMethodCodec());
    eventChannel?.receiveBroadcastStream()?.listen(_processEvent);
  }

  /// Override this method to get notifications when media is paused.
  void onPause() {/* user implementation */}

  /// Override this method to get notifications when media is played.
  void onPlay() {/* user implementation */}

  /// Override this method to get notifications when media has finished playing.
  void onComplete() {/* user implementation */}

  /// Override this method to get update when playhead moves. This method
  /// fires every second with [position] as seconds.
  void onTime(int position) {/* user implementation */}

  void onTimeMilli(int position) {/* user implementation */}

  /// Override this method to get notifications when a seek operation has
  /// finished. This will occur when user finishes scrubbing media.
  /// [position] is position in seconds before seek started.
  /// [offset] is seconds after seek processed.
  void onSeek(int position, double offset) {/* user implementation */}

  /// Override this method to get notifications when media duration is
  /// set or changed.
  /// [duration] is in milliseconds. Returns -1 for live stream
  void onDuration(int duration) {/* user implementation */}

  /// Override this method to get errors thrown by the player
  void onError(String error) {/* user implementation */}

  void onBufferingUpdate(VideoEvent videoEvent) {/* user implementation */}

  void _processEvent(dynamic event) async {
    String eventName = event["name"];
    switch (eventName) {

      /* onPause */
      case "onPause":
        onPause();
        break;

      /* onPlay */
      case "onPlay":
        onPlay();
        break;

      /* onComplete */
      case "onComplete":
        print('onComplete called');
        onComplete();
        break;

      /* onTime */
      case "onTime":
        onTime(event["time"].toInt());
        break;
      /* onTime */
      case "onTimeMilli":
        onTimeMilli(event["time"].toInt());
        break;

      /* onBufferingUpdate */
      case "onBufferingUpdate":
        final List<dynamic> values = event['buffering'];
        final VideoEvent videoEvent = VideoEvent(
          buffered: values.map<DurationRange>(_toDurationRange).toList(),
          eventType: VideoEventType.bufferingUpdate,
        );
        onBufferingUpdate(videoEvent);
        break;
      /* onSeek */
      case "onSeek":

        /* position of the player before the player seeks (in seconds) */
        int position = (event["position"]).toInt();

        /* requested position to seek to (in seconds) */
        double offset = double.parse("${event["offset"]}");

        onSeek(position, offset);

        break;

      case "onDuration":
        onDuration((event["duration"]).toInt());
        break;

      case "onError":
        onError(event["error"]);
        break;

        break;
      default:
        break;
    }
  }
}
DurationRange _toDurationRange(dynamic value) {
  final List<dynamic> pair = value;
  return DurationRange(
    Duration(milliseconds: pair[0]),
    Duration(milliseconds: pair[1]),
  );
}
