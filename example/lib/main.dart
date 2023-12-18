import 'dart:developer';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final EventChannel _eventChannel =
      const EventChannel("scan/accessibility_event");
  StreamSubscription<dynamic>? _streamSubscription;
  String? _scanResult = '';

  @override
  void initState() {
    super.initState();
    _streamSubscription =
        _eventChannel.receiveBroadcastStream().listen((event) {
      log("flutter获取到的扫码结果: $event", name: '扫码返回的值');
      setState(() {
        _scanResult = event;
      });
    });
  }

  @override
  void dispose() {
    _streamSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('ScanResult: $_scanResult\n'),
        ),
      ),
    );
  }
}
