import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:moving/moving.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _subscribeRecording = false;
  int _todaySteps = 0;
  String _location = 'Unknown';
  final _movingPlugin = Moving();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    bool subscribeRecording = false;
    int todaySteps = 0;
    String location = '';
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _movingPlugin.getPlatformVersion() ?? 'Unknown platform version';
      subscribeRecording = await _movingPlugin.subscribeRecording() ?? false;
      todaySteps = await _movingPlugin.getTodaySteps() ?? -1;
      location = await _movingPlugin.getCurrentLocation() ?? 'Unknown location';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _subscribeRecording = subscribeRecording;
      _todaySteps = todaySteps;
      _location = location;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('Running on: $_platformVersion\n'),
              Text('Recording: $_subscribeRecording\n'),
              Text('Today Steps: $_todaySteps\n'),
              Text('Location: $_location\n'),
              ButtonBar(
                alignment: MainAxisAlignment.center,
                children: [
                  ElevatedButton(
                    onPressed: initPlatformState,
                    child: const Text('Refresh'),
                  ),
                ],
              ),
            ],
          ),
          
        ),
      ),
    );
  }
}
