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

  @override
  Future<int?> getTodaySteps() async {
    final steps = await methodChannel.invokeMethod<int>('getTodaySteps');
    return steps;
  }

  @override
  Future<double?> getTodayDistance() async {
    final distance = await methodChannel.invokeMethod<double>('getTodayDistance');
    return distance;
  }

  @override
  Future<String?> getCurrentLocation() async {
    final location = await methodChannel.invokeMethod<String>('getCurrentLocation');
    return location;
  }
}
