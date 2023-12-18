package com.example.scan_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.flutter.plugin.common.EventChannel;

public class AccessibilityReceiver extends BroadcastReceiver {

  private final EventChannel.EventSink eventSink;

  public AccessibilityReceiver(EventChannel.EventSink eventSink) {
    this.eventSink = eventSink;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    String code = intent.getStringExtra(Constants.SEND_BROADCAST);
    eventSink.success(code);
  }
}
