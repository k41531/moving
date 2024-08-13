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

  @override
  Future<bool?> subscribeRecording() => Future.value(true);

  @override
  Future<int?> getTodaySteps() => Future.value(1000);

  @override
  Future<double?> getTodayDistance() => Future.value(42.0);

  @override
  Future<String?> getCurrentLocation() => Future.value("37.4219983,-122.084");
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

  test('subscribeRecording', () async {
    Moving movingPlugin = Moving();
    MockMovingPlatform fakePlatform = MockMovingPlatform();
    MovingPlatform.instance = fakePlatform;

    expect(await movingPlugin.subscribeRecording(), true);
  });

  test('getTodaySteps', () async {
    Moving movingPlugin = Moving();
    MockMovingPlatform fakePlatform = MockMovingPlatform();
    MovingPlatform.instance = fakePlatform;

    expect(await movingPlugin.getTodaySteps(), 1000);
  });

  test('getTodayDistance', () async {
    Moving movingPlugin = Moving();
    MockMovingPlatform fakePlatform = MockMovingPlatform();
    MovingPlatform.instance = fakePlatform;

    expect(await movingPlugin.getTodayDistance(), 42.0);
  });

  test('getCurrentLocation', () async {
    Moving movingPlugin = Moving();
    MockMovingPlatform fakePlatform = MockMovingPlatform();
    MovingPlatform.instance = fakePlatform;

    expect(await movingPlugin.getCurrentLocation(), "37.4219983,-122.084");
  });
}
