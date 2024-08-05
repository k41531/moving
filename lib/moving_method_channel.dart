import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'moving_platform_interface.dart';

/// An implementation of [MovingPlatform] that uses method channels.
class MethodChannelMoving extends MovingPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('moving');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool?> subscribeRecording() async {
    final result = await methodChannel.invokeMethod<bool>('subscribeRecording');
    return result;
  }
}
