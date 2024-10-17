import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

class VideoEvent {
  VideoEvent({
    @required this.eventType,
    this.buffered,
  });

  final VideoEventType eventType;
  final List<DurationRange> buffered;

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        other is VideoEvent &&
            runtimeType == other.runtimeType &&
            eventType == other.eventType &&
            listEquals(buffered, other.buffered);
  }

  @override
  int get hashCode => eventType.hashCode ^ buffered.hashCode;
}

enum VideoEventType {
  initialized,
  bufferingUpdate,
  unknown,
}

class DurationRange {
  DurationRange(this.start, this.end);
  final Duration start;
  final Duration end;
  double startFraction(Duration duration) {
    return start.inMilliseconds / duration.inMilliseconds;
  }

  double endFraction(Duration duration) {
    return end.inMilliseconds / duration.inMilliseconds;
  }

  @override
  String toString() => '$runtimeType(start: $start, end: $end)';

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is DurationRange &&
          runtimeType == other.runtimeType &&
          start == other.start &&
          end == other.end;

  @override
  int get hashCode => start.hashCode ^ end.hashCode;
}
