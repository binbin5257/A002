package com.bluebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.lds.chatcore.common.ToolsHelper;

/**
 * Created by xuchen on 16/7/14.
 * 此类基于蓝牙4.0.只能运行在>=android-18
 */
public class BlueBoxControllerAboveSdk18 implements IBlueBoxContoller {

    // ble模式蓝牙地址特征码
    public static final String ADDR_BLE_MARK = "00:0E:0B";
    // 连接失败
    public static int BLUE_STATE_CONNECTION_FAILURE = 0;
    // 连接成功
    public static int BLUE_STATE_CONNECTION_SUCCESS = 1;
    // 读取
    public static int BLUE_STATE_READ = 2;
    // 写命令失败
    public static int BLUE_STATE_WRITE_CMD_FAILURE = 3;
    private Context ctx;
    private String deviceName;
    private BlueBoxStateListener blueBoxStateListener;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    boolean mScanning = false;
    // 单次扫描时间
    private static final long SCAN_PERIOD = 10000;
    private boolean isConnected = false;
    private static IBlueBoxContoller iBlueBoxContoller;
    private static Object _lock = new Object();
    BoxResultHandler boxResultHandler;
    // 是否在处理命令 , 从发出命令到接收到结果,视为一次完整命令事物
    private boolean isHandlingCmd = false;
    private Runnable connectedCallback;
    private int connectType = 0;
    private IConnectRule connectRule = null;
    private IConfig config = null;


    private BlueBoxControllerAboveSdk18() {
        boxResultHandler = new BoxResultHandler();

    }

//    private BlueBoxControllerAboveSdk18(Runnable connectedCallback) {
//        this.connectedCallback = connectedCallback;
//        boxResultHandler = new BoxResultHandler();
//
//    }


    public void setConnectedCallbackOnlyExecuteFirst(Runnable connectedCallback) {
        this.connectedCallback = connectedCallback;
    }


    public static IBlueBoxContoller getInstance() {
        if (iBlueBoxContoller == null) {
            synchronized (_lock) {
                iBlueBoxContoller = new BlueBoxControllerAboveSdk18();
                Log.d("SS", "BlueBoxControllerBelowSdk18");

            }
        }

        return iBlueBoxContoller;
    }

//    public static IBlueBoxContoller getInstance(Runnable run) {
//
//        if (iBlueBoxContoller == null) {
//            synchronized (_lock) {
//                iBlueBoxContoller = new BlueBoxControllerAboveSdk18(run);
//                Log.d("SS", "BlueBoxControllerBelowSdk18");
//
//            }
//        }
//        return iBlueBoxContoller;
//    }


    @Override
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public void setConnectType(int type) {
        connectType = type;
    }

    @Override
    public void setContext(Context ctx) {
        if (this.ctx != null) {
            this.ctx.unregisterReceiver(mGattUpdateReceiver);
            this.ctx.unbindService(mServiceConnection);
            this.ctx = null;
        }

        this.ctx = ctx;
    }

    @Override
    public void setBlueBoxStateListener(BlueBoxStateListener blueBoxStateListener) {
        this.blueBoxStateListener = blueBoxStateListener;
    }


    @Override
    public void writeCmd(final String cmd) {

        if (cmd == null || isHandlingCmd) return;

        isHandlingCmd = true;
        // 单开线程执行,否则容易ANR
        new Thread() {
            @Override
            public void run() {
                mBluetoothLeService.WriteValue(cmd);
                isHandlingCmd = false;

            }
        }.start();


    }

    @Override
    public void init() {

        // 初始化配置信息
        if (this.config != null) {
            BluetoothLeService.UUID_NOTIFY = this.config.getUuidNotify();
            BluetoothLeService.UUID_SERVICE = this.config.getUuidService();
            BluetoothLeService.UUID_NOTIFY_READ = this.config.getUuidNotifyRead();
            BluetoothLeService.isRegisterReadGattCharacteristic = this.config.isRegisterReadGattCharacteristic();
            Log.i("xasdadaaaa", "init start");
            // 汽车
            if(config instanceof DefaultConfig)
            {
                BluetoothLeService.source = 0;
            }
            else
            {
                BluetoothLeService.source = 1;
            }

        }

        isConnected = false;
        isHandlingCmd = false;
        BluetoothManager bluetoothManager =
                (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Intent gattServiceIntent = new Intent(ctx, BluetoothLeService.class);
        ctx.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        ctx.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("xasdadaaaa", "init end");
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_FAILURE);

        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }


    @Override
    public void startScan() {
        if (mScanning) return;
        mScanning = true;

        new Thread() {
            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);

            }
        }.start();


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScanning) {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    sendMessage(BLUE_STATE_CONNECTION_FAILURE);
                    Log.d("SS", "链接失败");
                }
            }
        }, SCAN_PERIOD);
    }

    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        // 子类必须重写此方法，接受数据


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 连接成功
                case 1:
//                    LogUtil.i("xasdada", "连接成功走connectedCallback之前");
                    if (connectedCallback != null) {
                        connectedCallback.run();
//                        LogUtil.i("xasdada", "连接成功走connectedCallback之后");
                    }

                    isConnected = true;
                    blueBoxStateListener.onConnectSuccess();
                    connectedCallback = null;
                    break;
                // 连接失败
                case 0:

//
//                    if (isHopeConnecting && curRetryConCount < MAX_RETRY_CON_COUNT) {
//
//                        adapter.startDiscovery();
//                    } else {
                    blueBoxStateListener.onConnectFailure();
//                    }

                    connectedCallback = null;
                    break;

                case 2:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    byte[] data = new byte[msg.arg1];
//                    System.arraycopy(readBuf, 0, data, 0, msg.arg1);
//
//                    String strRead = new String(data);
//                    cacheResult.append(strRead);
//                    if (cacheResult.toString().indexOf("START") >= 0
//                            && cacheResult.toString().indexOf("END") >= 0) {
//
//                        String newResult = cacheResult.toString().substring(cacheResult.toString().lastIndexOf("START") + 5,
//                                cacheResult.toString().lastIndexOf("END"));
//                        Log.i("xuchen", "接收报文结束:截取的字符串- " + newResult);
//                        Log.i("xuchen", "接收报文结束:未截取- " + cacheResult.toString());
//
//                        cacheResult = new StringBuilder();
//                        blueBoxStateListener.onReadResult(newResult);
//                    }

                    connectedCallback = null;
                    break;


                case 3:
                    Toast.makeText(BlueBoxControllerAboveSdk18.this.ctx, "发送指令出现错误", Toast.LENGTH_SHORT).show();

                    connectedCallback = null;
                    break;


            }

        }
    };

    private String address = "";
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

//                    if(device.getAddress() != null){
//                        address += device.getAddress() + ",\n";
//                    }else{
//                        address += "NULL";
//                    }
//                    Toast.makeText(ctx,address,Toast.LENGTH_LONG).show();
//                    Log.i("xasdadaaaa", "find device- " + device.getName() + "-addr-" + device.getAddress());
                    if (
                            connectRule != null && connectRule.isMatch(device, connectType, deviceName, ADDR_BLE_MARK)

                            ) {
                        stopScan();
                        mScanning = false;
                        mDevice = device;
                        Log.i("xasdadaaaa", "匹配到设备" + device.getName());
                        mBluetoothLeService.connect(mDevice.getAddress());
                        Log.d("SS", "LeScanCallback");
                    }


                }
            };
    private BluetoothLeService mBluetoothLeService;
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.i("xasdadaaaa", "onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                sendMessage(BLUE_STATE_CONNECTION_FAILURE);
                Log.i("xasdadaaaa", "Unable to initialize Bluetooth");
//                finish();
            }
            Log.i("xasdadaaaa", "initialize Bluetooth");
            connect();
//            Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("xasdadaaaa", "onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i("xasdada", "action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //���ӳɹ�
//                Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //�Ͽ�����
                Log.i("xuchen", "连接已断开");
                isConnected = false;
                if (blueBoxStateListener != null)
                    blueBoxStateListener.onDisConnected();
                Log.i("xasdada", "连接已经断开");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //���Կ�ʼ�ɻ���
            {
                sendMessage(BLUE_STATE_CONNECTION_SUCCESS);
//                mConnected = true;

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //�յ�����
//                Log.e(TAG, "RECV DATA");
                byte[] rawdata = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);

//                LogUtil.i("xasdadaaaa", rawdata[0]+"-rawdata-len-"+rawdata.length+"-rawdata-"+rawdata[2]);
//                blueBoxStateListener.
//                blueBoxStateListener.
//                        BlueBoxStateListener.class.getTypeParameters()

                if (rawdata != null) {

                    if (getInterfaceGeneicType(blueBoxStateListener.getClass()).toString().indexOf("byte[]") >= 0) {

//                        com.baojia.util.LogUtil.i("entityClass", "entityClass-"  );
                        blueBoxStateListener.onReadResult(rawdata);
                    } else {

                        String s = boxResultHandler.getResult(rawdata);
                        if (s != null) {
                            blueBoxStateListener.onReadResult(s);
                            isHandlingCmd = false;
                        }
                    }

                }

            } else if (BluetoothLeService.ACTION_GATT_FAILURE.equals(action)) {
//                LogUtil.i("xasdada", "连接失败ACTION_GATT_FAILURE");
                sendMessage(BLUE_STATE_CONNECTION_FAILURE);
            }
        }

    };
    // 取接口定义的范型类型
    public static Type getInterfaceGeneicType(Class cls) {
        if (cls.getGenericInterfaces().length > 0) {
            Type mySuperClass = cls.getGenericInterfaces()[0];
            if (((ParameterizedType) mySuperClass).getActualTypeArguments().length > 0) {
                Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];

                return type;
            }
        }

        return null;
    }
    @Override
    public void stopScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    //    @Override
    private void connect() {
        disConnect();
        startScan();


    }

    @Override
    public void disConnect() {

        mHandler.removeCallbacksAndMessages(null);
        if (mBluetoothLeService != null)
            mBluetoothLeService.disconnect();
        isConnected = false;
    }

    @Override
    public void onDestory() {
        disConnect();
        ctx.unregisterReceiver(mGattUpdateReceiver);
        ctx.unbindService(mServiceConnection);
        ctx = null;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void addRetryCount() {

    }

    @Override
    public int getRetryCount() {
        return 0;
    }

    @Override
    public void sendMessage(int what) {
        Message m = mHandler.obtainMessage();
        m.what = what;
        m.sendToTarget();
    }

    @Override
    public void setConnectRule(IConnectRule connectRule) {
        this.connectRule = connectRule;
    }

    @Override
    public void setConfig(IConfig config) {

        this.config = config;
    }

    @Override
    public void sendMessage(int what, long delay) {

    }

    @Override
    public Handler getHandler() {
        return null;
    }
}
