import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:scan_service/scan_service_method_channel.dart';

void main() {
  MethodChannelScanService platform = MethodChannelScanService();
  const MethodChannel channel = MethodChannel('scan_service');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
