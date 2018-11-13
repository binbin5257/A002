package com.bluebox;

import android.content.Context;
import android.os.Handler;

/**
 * Created by xuchen on 16/7/14.
 */
public interface IBlueBoxContoller {


    // 向盒子写命令
    void writeCmd(String cmd);

    // 向盒子写命令
//    void writeCmd(byte[] cmd);

    // 控制器初始化
    void init();

    // 开始扫描线程
    void startScan();

    // 停止扫描
    void stopScan();

    // 连接盒子
    //void connect();

    // 与盒子断开
    void disConnect();

    // 通常在退出Activity调用
    void onDestory();


    // 是否正在与盒子保持连接
    boolean isConnected();

    // 增加重试次数
    void addRetryCount();

    // 获取重试次数
    int getRetryCount();

    // 内部消息发送,也可以用广播替换
    void sendMessage(int what);

    void setConnectRule(IConnectRule connectRule);

    void setConfig(IConfig config);

    // 内部消息发送,也可以用广播替换
    void sendMessage(int what, long delay);

    // 获取主线程的处理器
    Handler getHandler();

    // 设置蓝牙设备名称或地址
    void setDeviceName(String deviceName);

    // 0:设备名  1：物理地址
    void setConnectType(int type);

    // 设置Activity
    void setContext(Context ctx);

    // 绑定蓝牙盒子状态监听器
    void setBlueBoxStateListener(BlueBoxStateListener blueBoxStateListener);


    // 链接成功后仅执行一次
    void setConnectedCallbackOnlyExecuteFirst(Runnable connectedCallback);

    public interface BlueBoxStateListener<T> {

        // 连接失败
        void onConnectFailure();

        // 连接成功
        void onConnectSuccess();

        // 读硬件返回的数据
        void onReadResult(T data);


        // 断开连接后
        void onDisConnected();


    }


}
