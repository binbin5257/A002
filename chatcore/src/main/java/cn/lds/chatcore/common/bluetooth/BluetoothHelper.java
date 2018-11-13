package cn.lds.chatcore.common.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lds.chatcore.common.ToolsHelper;

/**
 * 蓝牙工具类
 * Created by E0608 on 2018/1/13.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothHelper {

    private final static String TAG = "BleHelper";

    /**
     * 扫描结果的callback
     */
    private BleScanResultCallback resultCallback;
    /**
     * 设备连接状态监听的callback
     */
    private BleConnectionCallback connectionStateCallback;
    /**
     * 蓝牙的适配器
     */
    private BluetoothAdapter mBtAdapter;
    /**
     * 上下文
     */
    private Context context;
    /**
     * gatt的集合
     */
    private Map<String, BluetoothGatt> mBluetoothGatts;
    /**
     * 重连的次数
     */
    int reconnectCount = 0;
    /**
     * 是否进行重连
     */
    private boolean canreconntect = false;
    /**
     * gatt连接callback
     */
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicChanged()");
            String address = gatt.getDevice().getAddress();
            long starttime = System.currentTimeMillis();
            byte[] bytes = characteristic.getValue();
//            if(dataCallback != null){
//                dataCallback.onGetData(characteristic.getUuid().toString(),bytes);
//            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02x", bytes[i]));
                Log.d(TAG," time :" + starttime + "     bytes：" + i + "     =" + Integer.toHexString(bytes[i]));
            }
            Log.d(TAG, "出来的数据：" + sb.toString());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicWrite()");
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicWrite()检测到不可以写数据    status：" + status);
                return;
            }
            Log.e(TAG, "onCharacteristicWrite()检测到可以去写数据了");
//            Log.d("写出的数据：", CodeUtil.bytesToString(characteristic.getValue()));
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status,
                                            int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            List<BluetoothGattService> services = gatt.getServices();
            Log.e(TAG, "onConnectionStateChange()       status:" + status + "    newState:" + newState);
            String address = gatt.getDevice().getAddress();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //如果是主动断开的 则断开
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices();
                    reconnectCount = 0;
                    Log.e(TAG, "连接成功");
                    if(connectionStateCallback != null){
                        connectionStateCallback.onConnectionStateChange(gatt,status,newState);
                    }

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    disconnect(address);
                    Log.e(TAG, "断开成功");
                    if(connectionStateCallback != null){
                        connectionStateCallback.onConnectionStateChange(gatt, status,newState);
                    }
                }
            }else{
                //如果是收到莫名其妙的断开状态的话 则重连一次
                if (newState != BluetoothProfile.STATE_CONNECTED && canreconntect) {
                    //先断开原有的连接
                    disconnect(address);
                    if(reconnectCount >= 2){
                        //重连三次不成功就失败
                        if(connectionStateCallback != null){
                            connectionStateCallback.onConnectionStateChange(gatt, status,newState);
                        }
                        reconnectCount = 0;
                        canreconntect = false;
                        return;
                    }
                    //再次重新连接
                    boolean connect_resule = requestConnect(address,connectionStateCallback,true);
                    reconnectCount++;
                    Log.e(TAG,"正在尝试重新连接："+connect_resule);
                }else{
                    if(connectionStateCallback != null){
                        connectionStateCallback.onConnectionStateChange(gatt, status,newState);
                    }
                    disconnect(address);

                }

            }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> gattServices = gatt.getServices();
        }
    };




    /**
     * 扫描ble设备的callback
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            if(resultCallback != null){
                resultCallback.onFindDevice(device,rssi,scanRecord);
            }

        }
    };

    public BluetoothHelper() {

    }
    /**
     *初始化ble操作类
     * @param context:上下文  建议用持久的
     */
    public boolean init(Context context) {
        this.context = context;
        final BluetoothManager bluetoothManager = (BluetoothManager) context
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();
        if (mBtAdapter == null) {
            return false;
        }
        mBluetoothGatts = new HashMap<String, BluetoothGatt>();
        return true;
    }
    /**
     *开启扫描
     * @param resultCallback:扫描结果的calllback
     */
    public boolean startScan(BleScanResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        try {
            return mBtAdapter.startLeScan(mLeScanCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "使用BLE扫描API开始扫描出错了 错误:" + e.getMessage());
            return false;
        }

    }
    /**
     *停止扫描
     */
    public void stopScan() {
        try {
            mBtAdapter.stopLeScan(mLeScanCallback);
            resultCallback = null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "使用的BLE扫描API停止扫描出错了 错误:" + e.getMessage());
        }
    }
    /**
     *断开连接
     */
    public synchronized void disconnect(String address) {
        if (mBluetoothGatts.containsKey(address)) {
            BluetoothGatt gatt = mBluetoothGatts.remove(address);
            if (gatt != null) {
                gatt.disconnect();
                gatt.close();
                Log.e(TAG, "执行到了设备断开的指令");
            }
        }
    }
    /**
     *发起连接（供外部调用）
     * @param address:目标设备的address地址
     * @param connectionStateCallback:设备连接状态的callback
     * @param canreconntect:是否启用重连机制 true：重连三次 false：不进行重连
     */
    public boolean requestConnect(String address,BleConnectionCallback connectionStateCallback,boolean canreconntect) {
        this.connectionStateCallback = connectionStateCallback;
        this.canreconntect = canreconntect;
        BluetoothGatt gatt = mBluetoothGatts.get(address);
        if (gatt != null && gatt.getServices().size() == 0) {
            return false;
        }
        return connect(address);
    }
    /**
     *连接到设备
     * @param address:目标设备的address地址
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean connect(String address) {
        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        BluetoothGatt gatt = device.connectGatt(context, false, mGattCallback);
        //BluetoothGatt gatt = device.connectGatt(mService, false, mGattCallback, BluetoothDevice.TRANSPORT_BREDR | BluetoothDevice.TRANSPORT_LE);

        //为了防止重复的gatt，连接成功先检查是否有重复的，有则断开
        BluetoothGatt last = mBluetoothGatts.remove(address);
        if(last != null){
            last.disconnect();
            last.close();
        }
        if (gatt == null) {
            mBluetoothGatts.remove(address);
            return false;
        } else {
            // TODO: if state is 141, it can be connected again after about 15
            // seconds
            mBluetoothGatts.put(address, gatt);
            return true;
        }
    }
    /**
     *蓝牙适配器是否正常
     */
    public boolean adapterEnabled(Context context) {
        if (mBtAdapter != null) {
            return mBtAdapter.isEnabled();
        }else{
            ToolsHelper.showStatus(context,false,"设备不支持蓝牙");
            return false;
        }

    }

//    /**
//     *写数据
//     * @param address：目标设备的address
//     * @param data:要发送的内容
//     */
//    public boolean requestWriteCharacteristic(String address,byte[] data) {
//        BluetoothGatt gatt = mBluetoothGatts.get(address);
//        if (gatt == null || mGattCharacteristicWrite == null) {
//            if (gatt == null) {
//                Log.e(TAG, "发送BLE数据失败:gatt = null");
//            }
//
//            if (mGattCharacteristicWrite == null) {
//                Log.e(TAG, "发送BLE数据失败:characteristic = null");
//            }
//            return false;
//        }
//
////        Log.d(TAG, "data：" + CodeUtil.bytesToString(mGattCharacteristicWrite.getValue()));
//        boolean result = false;
//        try {
//            mGattCharacteristicWrite.setValue(data);
//            result = gatt.writeCharacteristic(mGattCharacteristicWrite);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return result;
//    }



}
