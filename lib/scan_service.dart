
import 'scan_service_platform_interface.dart';

class ScanService {
  Future<String?> getPlatformVersion() {
    return ScanServicePlatform.instance.getPlatformVersion();
  }
}
