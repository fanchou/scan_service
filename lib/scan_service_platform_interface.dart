import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'scan_service_method_channel.dart';

abstract class ScanServicePlatform extends PlatformInterface {
  /// Constructs a ScanServicePlatform.
  ScanServicePlatform() : super(token: _token);

  static final Object _token = Object();

  static ScanServicePlatform _instance = MethodChannelScanService();

  /// The default instance of [ScanServicePlatform] to use.
  ///
  /// Defaults to [MethodChannelScanService].
  static ScanServicePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ScanServicePlatform] when
  /// they register themselves.
  static set instance(ScanServicePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
