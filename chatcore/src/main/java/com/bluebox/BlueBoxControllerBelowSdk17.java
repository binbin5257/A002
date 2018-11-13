package com.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xuchen on 16/7/13.
 */
public class BlueBoxControllerBelowSdk17 implements IBlueBoxContoller {

    // 连接失败
    public static int BLUE_STATE_CONNECTION_FAILURE = 0;
    // 连接成功
    public static int BLUE_STATE_CONNECTION_SUCCESS = 1;
    // 读取
    public static int BLUE_STATE_READ = 2;
    // 写命令失败
    public static int BLUE_STATE_WRITE_CMD_FAILURE = 3;

    // 最大重试次数
    public static int MAX_RETRY_CON_COUNT = 10;

    // spp模式蓝牙地址特征码
    public static final String ADDR_SPP_MARK = "00:0E:0E";


    // 当前重试次数
    public int curRetryConCount = 0;

    boolean isHopeConnecting = false;
    private BluetoothAdapter adapter = null;
    BluetoothSocket socket;
    DataExchanger cThread = null;
    private  IConnectRule connectRule=null;
    private Context ctx;
    String deviceName;
    BlueBoxStateListener blueBoxStateListener;
    private static IBlueBoxContoller iBlueBoxContoller;
    private static Object _lock = new Object();
    BoxResultHandler boxResultHandler;
    private boolean isConnected = false;
    // 匹配到设备名一致的设备数,找到的第二个一般是spp模式下的设备
//    int l = 0;

    public void setConnectedCallbackOnlyExecuteFirst(Runnable connectedCallback)
    {

    }
    private BlueBoxControllerBelowSdk17() {
        boxResultHandler = new BoxResultHandler();
    }


    public static IBlueBoxContoller getInstance() {
        if (iBlueBoxContoller == null) {
            synchronized (_lock) {
                iBlueBoxContoller = new BlueBoxControllerBelowSdk17();
            }
        }
        return iBlueBoxContoller;
    }

    @Override
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public void setConnectType(int type) {

    }

    @Override
    public void setContext(Context ctx) {

        if (this.ctx != null) {
            ctx.unregisterReceiver(receiver);
        }


        this.ctx = ctx;
    }

    @Override
    public void setBlueBoxStateListener(BlueBoxStateListener blueBoxStateListener) {
        this.blueBoxStateListener = blueBoxStateListener;
    }


    public void writeCmd(String cmd) {
        if (cThread != null)
            cThread.write(cmd.getBytes());
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
// 注册广播接收器，接收并处理搜索结果
        ctx.registerReceiver(receiver, intentFilter);


    }

    public void sendMessage(int what) {
        Message m = handler.obtainMessage();
        m.what = what;
        m.sendToTarget();
    }

    @Override
    public void setConnectRule(IConnectRule connectRule) {
        this.connectRule = connectRule;
    }

    @Override
    public void setConfig(IConfig config) {

    }

    public void sendMessage(int what, long delay) {
        Message m = handler.obtainMessage();
        m.what = what;
        handler.sendMessageDelayed(m, delay);
    }

    //int k=0;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 获取查找到的蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                System.out.println("xuchen--ACTION_FOUND-" + device.getName() + "-getAddress-" + device.getAddress());
                Log.d("xuchen", deviceName + "xuchen--ACTION_FOUND-" + device.getName() + "-getAddress-" + device.getAddress());
//                if(device.getName()==null)
//                {
//                    if(k++==0)
//                    {
//                        disConnect();
//                        init();
//                        return;
//                    }
////                    k++;
//                }
//                if(device.getName()==null&&k==2)
//                {
//                    Log.d("xuchen","gggggg");
//                    connect();
//                    k=0;
//                    return;
//                }
//                if(1==1)return;
                // 如果查找到的设备符合要连接的设备，处理
                if (device.getName() != null && device.getAddress() != null
                        && device.getAddress().indexOf(ADDR_SPP_MARK) >= 0 && device.getName().equals(deviceName)) {

//                    System.out.println("xuchen--ACTION_FOUND-find right device--" + device.getName());
                    Log.d("xasdadaaaa", "xuchen--ACTION_FOUND-find right device--" + device.getName());

                    // 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
                    adapter.cancelDiscovery();
                    // 获取蓝牙设备的连接状态
                    int connectState = device.getBondState();
                    switch (connectState) {
                        // 未配对
                        case BluetoothDevice.BOND_NONE:
//                            System.out.println("xuchen--ACTION_FOUND-BOND_NONE");
                            // 配对
                            // 连接
                            startBlueConnect(device);
//                            try {
//                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
//                                createBondMethod.invoke(device);
//                            } catch (Exception e) {
//                                System.out.println("xuchen--ACTION_FOUND-BOND_NONE--error-" + e.toString());
//                            }
                            break;
                        // 已配对
                        case BluetoothDevice.BOND_BONDED:
//                            System.out.println("xuchen--ACTION_FOUND-BOND_BONDED");
                            // 连接
                            startBlueConnect(device);

                            break;
                    }
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                System.out.println("xuchen--ACTION_BOND_STATE_CHANGED-");
                // 状态改变的广播
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName().equals(deviceName)) {
                    int connectState = device.getBondState();
                    switch (connectState) {
                        case BluetoothDevice.BOND_NONE:
//                            System.out.println("xuchen--ACTION_BOND_STATE_CHANGED--BOND_NONE");
                            break;
                        case BluetoothDevice.BOND_BONDING:
//                            System.out.println("xuchen--ACTION_BOND_STATE_CHANGED--BOND_BONDING");
                            break;
                        case BluetoothDevice.BOND_BONDED:
//                            System.out.println("xuchen--ACTION_BOND_STATE_CHANGED--BOND_BONDED");
                            // 连接
                            startBlueConnect(device);
                            break;
                    }
                }
            }
        }
    };


    @Override
    public int getRetryCount() {
        return curRetryConCount;
    }

    @Override
    public void addRetryCount() {
        curRetryConCount++;
    }

    //  开启蓝牙连接线程
    public void startBlueConnect(BluetoothDevice device) {
        cThread = new DataExchanger(device, false, this);
        cThread.start();
    }


    @Override
    public void init() {
        curRetryConCount = 0;
        isConnected = false;
        isHopeConnecting = false;
        adapter = BluetoothAdapter.getDefaultAdapter();
        registerReceiver();
        connect();
    }

    @Override
    public void startScan() {
        adapter.startDiscovery();
//        Log.d("xuchen","568");
    }

    @Override
    public void stopScan() {

    }

    //@Override
    private void connect() {
        disConnect();
//        l = 0;
        curRetryConCount = 0;
        isHopeConnecting = true;
        new Thread() {
            @Override
            public void run() {
                startScan();

            }
        }.start();
//Log.d("SS","FFF");

    }

    @Override
    public void onDestory() {

        ctx.unregisterReceiver(receiver);
        disConnect();
        blueBoxStateListener = null;
        ctx = null;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void disConnect() {
//        l = 0;
        isHopeConnecting = false;
        handler.removeCallbacksAndMessages(null);
        adapter.cancelDiscovery();
        if (cThread != null) {
            cThread.cancel();
            cThread = null;
        }
        if (blueBoxStateListener != null)
            blueBoxStateListener.onDisConnected();
        isConnected = false;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        private StringBuilder cacheResult = new StringBuilder();

        // 子类必须重写此方法，接受数据
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 连接成功
                case 1:
                    isConnected = true;
                    blueBoxStateListener.onConnectSuccess();

                    break;
                // 连接失败
                case 0:

//                    l = 0;
                    if (isHopeConnecting && curRetryConCount < MAX_RETRY_CON_COUNT) {

                        adapter.startDiscovery();
                    } else {
                        blueBoxStateListener.onConnectFailure();
                    }


                    break;

                case 2:


                    byte[] rawdata = (byte[]) msg.obj;


                    byte[] data2 = new byte[rawdata.length];
                    System.arraycopy(rawdata, 0, data2, 0, rawdata.length);

                    String strRead = new String(data2);
                    cacheResult.append(strRead);
//                    Log.d("xuchen", "cacheResult-" + cacheResult.toString());


                    if (rawdata != null) {

                        String s = boxResultHandler.getResult(rawdata);
                        if (s != null)
                            blueBoxStateListener.onReadResult(s);
                    }


                    break;


                case 3:
                    Toast.makeText(BlueBoxControllerBelowSdk17.this.ctx, "发送指令出现错误", Toast.LENGTH_SHORT).show();


                    break;


            }

        }
    };


}
