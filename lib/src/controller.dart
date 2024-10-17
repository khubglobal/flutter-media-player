import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class KhubMediaController with ChangeNotifier {
  MethodChannel methodChannel;
  String _url = "";

  String get url => _url;
  String _title = "";

  String get title => _title;
  String _subtitle = "";

  String get subtitle => _subtitle;
  bool _loop = false;

  bool get loop => _loop;
  bool _autoPlay = false;

  bool get autoPlay => _autoPlay;
  double _position = -1;

  double get position => _position;
  double _pitch = -1;

  double get pitch => _pitch;
  double _speed = -1;

  double get speed => _speed;
  String _preferredAudioLanguage = "en";

  String get preferredAudioLanguage => _preferredAudioLanguage;
  String _userId = "";

  String get userId => _userId;
  String _trackId = "";

  String get trackId => _trackId;
  String _videoId = "";

  String get videoId => _videoId;
  String _muxKey = "";

  String get muxKey => _muxKey;
  String _muxName = "";

  String get muxName => _muxName;
  bool _enableMux = false;

  bool get enableMux => _enableMux;

  KhubMediaController(String url,
      {void Function() onInited,
      final String title,
      final String subtitle,
      final bool loop,
      final bool autoPlay,
      final double position,
      final double pitch,
      final double speed,
      final String audioLanguage,
      final String userId,
      final String trackId,
      final String videoId,
      final String muxName,
      final String muxKey,
      final bool enableMux}) {
    _url = url ?? _url;
    _title = title ?? _title;
    _subtitle = subtitle ?? _subtitle;
    _loop = loop ?? _loop;
    _autoPlay = autoPlay ?? _autoPlay;
    _position = position ?? _position;
    _pitch = pitch ?? _pitch;
    _speed = speed ?? _speed;
    _preferredAudioLanguage = audioLanguage != null ? audioLanguage : 'en';
    _userId = userId ?? _userId;
    _trackId = trackId ?? _trackId;
    _videoId = videoId ?? _videoId;
    _muxKey = muxKey ?? _muxKey;
    _muxName = muxName ?? _muxName;
    _enableMux = enableMux ?? _enableMux;
  }

  void seekTo(double position) async {
    if (methodChannel != null) {
      await methodChannel.invokeMethod("seekTo", {"position": position});
    }
  }

  Future<void> playPause() async {
    if (methodChannel != null) {
      await methodChannel.invokeMethod("playPause");
    }
  }

  Future<void> toggleLooping() async {
    _loop = _loop = !_loop;
    await methodChannel.invokeMethod("setLooping", {"loop": _loop});
  }

  Future<void> setPlaybackSpeed(double speed) async {
    if (methodChannel != null) {
      await methodChannel.invokeMethod("setPlaybackSpeed", {
        "speed": speed.toString(),
      });
    }
  }

  Future<void> setPitch(double pitch) async {
    if (methodChannel != null) {
      await methodChannel.invokeMethod("setPitch", {
        "pitch": pitch.toString(),
      });
    }
  }

  Future<double> getPitch() async {
    if (methodChannel != null) {
      final pitch = await methodChannel.invokeMethod("getPitch");
      _pitch = pitch as double;
    }
    return _pitch;
  }

  Future<double> getPlaybackSpeed() async {
    if (methodChannel != null) {
      final speed = await methodChannel.invokeMethod("getPlaybackSpeed");
      _speed = speed as double;
    }
    return _speed;
  }

  Future<void> setPreferredAudioLanguageChanged({String code}) async {
    if (methodChannel != null) {
      _preferredAudioLanguage = code != null
          ? code
          : _preferredAudioLanguage == 'en'
              ? 'ie'
              : 'en';
      await methodChannel.invokeMethod(
          "setPreferredAudioLanguage", {"code": _preferredAudioLanguage});
    }
  }

  Future<void> onMediaChanged(
      {String url,
      bool autoPlay,
      bool loop,
      String title,
      String subtitle,
      double position,
      double speed,
      String audioLanguage,
      double pitch,
      String userId,
      String trackId,
      String videoId,
      String muxName,
      String muxKey}) async {
    _url = url;
    _autoPlay = autoPlay;
    _loop = loop;
    _title = title;
    _subtitle = subtitle;
    _position = position;
    _speed = speed;
    _pitch = pitch;
    _preferredAudioLanguage = audioLanguage != null ? audioLanguage : 'en';
    _userId = userId;
    _trackId = trackId;
    _videoId = videoId;
    _muxKey = muxKey;
    _muxName = muxName;
    await methodChannel?.invokeMethod("onMediaChanged", {
      "autoPlay": autoPlay,
      "loop": loop,
      "url": url,
      "title": title ?? "",
      "subtitle": subtitle ?? "",
      "position": position,
      "speed": speed,
      "pitch": pitch,
      "code": _preferredAudioLanguage,
      "userId": userId,
      "trackId": trackId,
      "videoId": videoId,
      "experimentName": muxKey,
      "propertyKey": muxName
    });
  }

  Future<void> onPlatformViewCreated(int viewId) async {
    if (methodChannel != null) return;
    methodChannel =
        MethodChannel("tv.khub/NativeVideoPlayerMethodChannel_$viewId");
  }
}
