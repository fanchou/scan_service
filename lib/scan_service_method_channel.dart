import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'scan_service_platform_interface.dart';

/// An implementation of [ScanServicePlatform] that uses method channels.
class MethodChannelScanService extends ScanServicePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('scan_service');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
