package com.example.scan_service;

import static com.example.scan_service.Constants.ACCESSIBILITY_INTENT;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;


import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;

/** ScanServicePlugin */
public class ScanServicePlugin implements FlutterPlugin, EventChannel.StreamHandler {

  private EventChannel eventChannel;

  private AccessibilityReceiver accessibilityReceiver;

  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), Constants.EVENT_TAG);
    eventChannel.setStreamHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    eventChannel.setStreamHandler(null);
  }

  @Override
  public void onListen(Object arguments, EventChannel.EventSink events) {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ACCESSIBILITY_INTENT);

    accessibilityReceiver = new AccessibilityReceiver(events);
    // todo for high version SDK
    context.registerReceiver(accessibilityReceiver, intentFilter);
    /// Set up listener intent
    Intent listenerIntent = new Intent(context, ScanService.class);
    context.startService(listenerIntent);
    Log.i("AccessibilityPlugin", "Started the accessibility tracking service.");
  }

  @Override
  public void onCancel(Object arguments) {
    context.unregisterReceiver(accessibilityReceiver);
    accessibilityReceiver = null;
  }

}
