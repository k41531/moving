
import 'moving_platform_interface.dart';

class Moving {
  Future<String?> getPlatformVersion() {
    return MovingPlatform.instance.getPlatformVersion();
  }

  Future<bool?> subscribeRecording() {
    return MovingPlatform.instance.subscribeRecording();
  }
}
