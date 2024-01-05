package com.example.scan_service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class ScanService extends AccessibilityService {
  private static OnKeyEvent onKeyEvent;

  private boolean mCaps = false;

  private final StringBuffer mStringBufferResult = new StringBuffer();

  private final Handler mHandler = new Handler(Looper.getMainLooper());

  private final static Integer MESSAGE_DELAY = 500;

  public ScanService() {
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Override
  public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
  }

  @Override
  public void onInterrupt() {
  }


  /**
   * 设置监听
   * @param onKeyEvent
   */
  public static void setOnKeyEvent(OnKeyEvent onKeyEvent){
    ScanService.onKeyEvent=onKeyEvent;
  }
  public interface OnKeyEvent{
    boolean onKeyEvent(KeyEvent event);
  }


  @Override
  protected boolean onKeyEvent(KeyEvent event) {

    // TODO 暂时用一个比较hack的方式区分键盘和扫码枪
    if(event.getAction() == KeyEvent.ACTION_DOWN && !event.getDevice().getName().contains("Keyboard")) {
      if(isInputFromScanner(getApplication().getApplicationContext(), event)) {
        analysisKeyEvent(event);
        return true;
      }
    }


//    if(isInputFromScanner(getApplication().getApplicationContext(), event)) {
//      analysisKeyEvent(event);
//    }
//
//    // 如果判断为扫码时则拦截输入
//    if(isInputFromScanner(getApplicationContext(), event)){
//      return true;
//    }
//
//    if(onKeyEvent != null){
//      //这里通过回调的方式将事件传出去统一处理
//      //返回true事件就会拦截不会继续传递
//      // todo 尝试拦截回车看看是否有用
//      return onKeyEvent.onKeyEvent(event);
//    }
    return super.onKeyEvent(event);
  }


  /**
   * 检测输入设备是否是扫码器
   *
   * @param context
   * @return 是的话返回true，否则返回false
   */
  public boolean isInputFromScanner(Context context, KeyEvent event) {
    if (event.getDevice() == null) {
      return false;
    }
    // event.getDevice().getControllerNumber();
    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
      //实体按键，若按键为返回、音量加减、返回false
      return false;
    }
    if (event.getDevice().getSources() == (InputDevice.SOURCE_KEYBOARD | InputDevice.SOURCE_DPAD | InputDevice.SOURCE_CLASS_BUTTON)) {
      //虚拟按键返回false
      return false;
    }
    Configuration cfg = context.getResources().getConfiguration();
    return cfg.keyboard != Configuration.KEYBOARD_UNDEFINED;
  }

  Intent intent = new Intent(Constants.ACCESSIBILITY_INTENT);


  Runnable mScanningFishedRunnable = () -> {
    String code = mStringBufferResult.toString();
    Log.d("操作系统获取的扫码值", "code: " + code);
    intent.putExtra(Constants.SEND_BROADCAST, code);
    sendBroadcast(intent);
    mStringBufferResult.setLength(0);
  };
  /**
   * 扫码枪事件解析
   *
   * @param event
   */
  public void analysisKeyEvent(KeyEvent event) {

    int keyCode = event.getKeyCode();

    //字母大小写判断
    checkLetterStatus(event);

    if (event.getAction() == KeyEvent.ACTION_DOWN) {

      char aChar = getInputCode(event);
      // char aChar = (char) event.getUnicodeChar();

      if (aChar != 0) {
        mStringBufferResult.append(aChar);
      }

      if (keyCode == KeyEvent.KEYCODE_ENTER) {
        //若为回车键，直接返回
        mHandler.removeCallbacks(mScanningFishedRunnable);
        mHandler.post(mScanningFishedRunnable);
      } else {
        //延迟post，若500ms内，有其他事件
        mHandler.removeCallbacks(mScanningFishedRunnable);
        mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
      }

    }
  }

  //检查shift键
  private void checkLetterStatus(KeyEvent event) {
    int keyCode = event.getKeyCode();
    if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
      //按着shift键，表示大写
      mCaps = event.getAction() == KeyEvent.ACTION_DOWN;
    }
  }


  //获取扫描内容
  private char getInputCode(KeyEvent event) {
    int keyCode = event.getKeyCode();
    char aChar;
    if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
      //字母
      aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
    } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
      //数字
      aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
    } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
      aChar = 0;
    } else {
      //其他符号
      aChar = (char) event.getUnicodeChar();
    }
    return aChar;
  }


}
