import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:moving/moving_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelMoving platform = MethodChannelMoving();
  const MethodChannel channel = MethodChannel('moving');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        switch (methodCall.method) {
          case 'getPlatformVersion':
            return '42';
          case 'subscribeRecording':
            return true;
          case 'getTodaySteps':
            return 1000;
          case 'getTodayDistance':
            return 42.0;
          case 'getCurrentLocation':
            return "37.4219983,-122.084";
          default:
            return null;
        }
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });

  test('subscribeRecording', () async {
    expect(await platform.subscribeRecording(), true);
  });

  test('getTodaySteps', () async {
    expect(await platform.getTodaySteps(), 1000);
  });

  test('getTodayDistance', () async {
    expect(await platform.getTodayDistance(), 42.0);
  });

  test('getCurrentLocation', () async {
    expect(await platform.getCurrentLocation(), "37.4219983,-122.084");
  });
}
