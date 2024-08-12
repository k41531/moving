import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'moving_method_channel.dart';

abstract class MovingPlatform extends PlatformInterface {
  /// Constructs a MovingPlatform.
  MovingPlatform() : super(token: _token);

  static final Object _token = Object();

  static MovingPlatform _instance = MethodChannelMoving();

  /// The default instance of [MovingPlatform] to use.
  ///
  /// Defaults to [MethodChannelMoving].
  static MovingPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MovingPlatform] when
  /// they register themselves.
  static set instance(MovingPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<bool?> subscribeRecording() {
    throw UnimplementedError('subscribeRecording() has not been implemented.');
  }

  Future<int?> getTodaySteps() {
    throw UnimplementedError('getTodaySteps() has not been implemented.');
  }

  Future<double?> getTodayDistance() {
    throw UnimplementedError('getTodayDistance() has not been implemented.');
  }
}
