package com.bluebox;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by xuchen on 16/7/15.
 * 蓝牙盒子功能提供者
 */
public class BlueBoxControllerProvider {


    private static void isChecked(String deviceName,
                                  Context ctx, IBlueBoxContoller.BlueBoxStateListener blueBoxStateListener) {

        if (deviceName == null || deviceName.length() <= 0) {
            throw new IllegalArgumentException("deviceName is not null");

        }
        if (ctx == null) {
            throw new IllegalArgumentException("ctx is not null");
        }
        if (blueBoxStateListener == null) {
            throw new IllegalArgumentException("BlueBoxStateListener is not null");
        }


    }

    public static IBlueBoxContoller getInstance(String deviceName,
                                                Context ctx, IBlueBoxContoller.BlueBoxStateListener blueBoxStateListener) {
        Log.i("xasdada", "初始化getInstance方法");
        isChecked(deviceName,
                ctx, blueBoxStateListener);
        IBlueBoxContoller iBlueBoxContoller;
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 18 && ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            iBlueBoxContoller = BlueBoxControllerAboveSdk18.getInstance();

        } else {
            iBlueBoxContoller = BlueBoxControllerBelowSdk17.getInstance();

        }
        if (iBlueBoxContoller != null) {
            iBlueBoxContoller.setBlueBoxStateListener(blueBoxStateListener);
            iBlueBoxContoller.setDeviceName(deviceName);
            iBlueBoxContoller.setConfig(new DefaultConfig());
            iBlueBoxContoller.setConnectRule(new DefaultConnectRule());
            Log.d("xuchen", deviceName);
            iBlueBoxContoller.setContext(ctx);
            iBlueBoxContoller.init();
        }
        return iBlueBoxContoller;
    }

    //type=0 设备名  type=1:物理地址
    public static IBlueBoxContoller getInstance(String address, int connectType, IConnectRule connectRule, IConfig config,
                                                Context ctx, IBlueBoxContoller.BlueBoxStateListener blueBoxStateListener) {
        Log.i("xasdadaaaa", "初始化getInstance方法");
        isChecked(address,
                ctx, blueBoxStateListener);
        IBlueBoxContoller iBlueBoxContoller;
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 18 && ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            iBlueBoxContoller = BlueBoxControllerAboveSdk18.getInstance();

        } else {
            iBlueBoxContoller = BlueBoxControllerBelowSdk17.getInstance();

        }
        if (iBlueBoxContoller != null) {
            iBlueBoxContoller.setBlueBoxStateListener(blueBoxStateListener);
            iBlueBoxContoller.setDeviceName(address);
            iBlueBoxContoller.setConnectType(connectType);
            iBlueBoxContoller.setConnectRule(connectRule);
            iBlueBoxContoller.setConfig(config);
            iBlueBoxContoller.setContext(ctx);
            iBlueBoxContoller.init();
        }
        return iBlueBoxContoller;
    }


    // connectedCallback  --  只执行一次
    public static IBlueBoxContoller getInstance(String deviceName,
                                                Context ctx, IBlueBoxContoller.BlueBoxStateListener blueBoxStateListener, Runnable connectedCallbackOnlyExecuteFirst) {
        Log.i("xasdada", "初始化getInstance方法");
        isChecked(deviceName,
                ctx, blueBoxStateListener);
        IBlueBoxContoller iBlueBoxContoller;
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 18 && ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            iBlueBoxContoller = BlueBoxControllerAboveSdk18.getInstance();
            iBlueBoxContoller.setConnectedCallbackOnlyExecuteFirst(connectedCallbackOnlyExecuteFirst);

        } else {
            iBlueBoxContoller = BlueBoxControllerBelowSdk17.getInstance();

        }
        if (iBlueBoxContoller != null) {
            iBlueBoxContoller.setBlueBoxStateListener(blueBoxStateListener);
            iBlueBoxContoller.setDeviceName(deviceName);
            iBlueBoxContoller.setConfig(new DefaultConfig());
            iBlueBoxContoller.setConnectRule(new DefaultConnectRule());
            Log.d("xuchen", deviceName);
            iBlueBoxContoller.setContext(ctx);
            iBlueBoxContoller.init();
        }
        return iBlueBoxContoller;
    }


}
