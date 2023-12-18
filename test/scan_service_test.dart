import 'package:flutter_test/flutter_test.dart';
import 'package:scan_service/scan_service.dart';
import 'package:scan_service/scan_service_platform_interface.dart';
import 'package:scan_service/scan_service_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockScanServicePlatform
    with MockPlatformInterfaceMixin
    implements ScanServicePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ScanServicePlatform initialPlatform = ScanServicePlatform.instance;

  test('$MethodChannelScanService is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelScanService>());
  });

  test('getPlatformVersion', () async {
    ScanService scanServicePlugin = ScanService();
    MockScanServicePlatform fakePlatform = MockScanServicePlatform();
    ScanServicePlatform.instance = fakePlatform;

    expect(await scanServicePlugin.getPlatformVersion(), '42');
  });
}
