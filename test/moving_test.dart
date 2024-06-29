import 'package:flutter_test/flutter_test.dart';
import 'package:moving/moving.dart';
import 'package:moving/moving_platform_interface.dart';
import 'package:moving/moving_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMovingPlatform
    with MockPlatformInterfaceMixin
    implements MovingPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MovingPlatform initialPlatform = MovingPlatform.instance;

  test('$MethodChannelMoving is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMoving>());
  });

  test('getPlatformVersion', () async {
    Moving movingPlugin = Moving();
    MockMovingPlatform fakePlatform = MockMovingPlatform();
    MovingPlatform.instance = fakePlatform;

    expect(await movingPlugin.getPlatformVersion(), '42');
  });
}
