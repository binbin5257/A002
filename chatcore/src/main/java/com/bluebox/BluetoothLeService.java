/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bluebox;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public final static String ACTION_GATT_FAILURE =
            "com.example.bluetooth.le.ACTION_GATT_FAILURE";

    public   static UUID UUID_NOTIFY =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public   static UUID UUID_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    public   static UUID UUID_NOTIFY_READ =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static UUID UUID_DESCRIPTER = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");// 固定值，descriptor的UUID。

    public static boolean isRegisterReadGattCharacteristic =false;

    // 0：汽车
    public  static int source = 0;

    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic readNotifyCharacteristic;
    // 单次最大传输字节
    private int maxSendBytesLen = 20;
    // 发送间隔 单位:毫秒
    private long sendPeriod = 500;

    public void WriteValue(String strValue) {

//        Log.d("xuchen", "www----" + (new String(strValue.getBytes())));
        byte[] array = strValue.getBytes();
        boolean isContinue = true;
        int startindex = 0;
        int surpluslen = array.length;
//        LogUtil.i("xasdadaaaa","length-"+surpluslen);
        while (isContinue) {
            int patchlen = surpluslen >= maxSendBytesLen ? maxSendBytesLen : surpluslen;
            byte[] data = new byte[patchlen];
//            LogUtil.i("xasdadaaaa","startindex-"+startindex+"-patchlen-"+patchlen);
            System.arraycopy(array, startindex, data, 0, patchlen);

            surpluslen = surpluslen - patchlen;
            startindex = startindex + patchlen;


            mNotifyCharacteristic.setValue(data);
            boolean writeStatus = mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
//            Log.d("xuchen", "www---status-" + writeStatus + "-length-" + data.length);

            if (surpluslen <= 0) break;

            try {
                Thread.sleep(sendPeriod);
            } catch (Exception ex) {
            }

        }

    }

    public void findService(List<BluetoothGattService> gattServices) {
//        Log.i(TAG, "Count is:" + gattServices.size());
        for (BluetoothGattService gattService : gattServices) {
//            Log.i(TAG, gattService.getUuid().toString());
//            Log.i(TAG, UUID_SERVICE.toString());
            if (gattService.getUuid().toString().equalsIgnoreCase(UUID_SERVICE.toString())) {
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
//                Log.i(TAG, "Count is:" + gattCharacteristics.size());
                for (BluetoothGattCharacteristic gattCharacteristic :
                        gattCharacteristics) {
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(UUID_NOTIFY.toString())) {
                        mNotifyCharacteristic = gattCharacteristic;
                        setCharacteristicNotification(gattCharacteristic, true);
                        // 汽车逻辑
                        if(source==0) {
                            // add new
                            final BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_DESCRIPTER);
                            //设置回复形式
                            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            if (descriptor != null) {
                                boolean ret = mBluetoothGatt.readDescriptor(descriptor);
                                Log.i(TAG, "  readDescriptor: " + ret);
                                if (!ret) {
                                    close();
                                    broadcastUpdate(ACTION_GATT_FAILURE);
                                    return;

                                }
                            }
                        } else {
                            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                        }

                        if(isRegisterReadGattCharacteristic) {
                            readNotifyCharacteristic = gattService.getCharacteristic(UUID_NOTIFY_READ);
                            if (readNotifyCharacteristic != null) {
                                final int rxCharaProp = readNotifyCharacteristic.getProperties();
                                if ((rxCharaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                    setCharacteristicNotification(
                                            readNotifyCharacteristic, true);
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            Log.i("xasdadaaaa", "onConnectionStateChange=" + status + " NewStates=" + newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    intentAction = ACTION_GATT_CONNECTED;

                    broadcastUpdate(intentAction);
//                    LogUtil.i(TAG, "Connected to GATT server.");
                    // Attempts to discover services after successful connection.
//                    LogUtil.i(TAG, "Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
//                    Log.i("xuchen", "Disconnected from GATT server.");
                    broadcastUpdate(intentAction);
                }
            }
            else
            {
                intentAction = ACTION_GATT_FAILURE;
                close();
                mBluetoothGatt = null;
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            LogUtil.i("xasdadaaaa", "onServicesDiscovered=" + status  );
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "onServicesDiscovered received: " + status);
                findService(gatt.getServices());
            } else {
                if (mBluetoothGatt.getDevice().getUuids() == null)
                    Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
//            LogUtil.i("xasdadaaaa", "onCharacteristicRead=" + status  );
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
//            LogUtil.i("xasdadaaaa", "onCharacteristicChanged"  );
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
                                          int status) {
//            LogUtil.i("xasdadaaaa", "onCharacteristicWrite--characteristic-" + (new String(characteristic.getValue())) + "--status-" + status);
//            Log.d("xuchen", "onCharacteristicWrite--characteristic-" + (new String(characteristic.getValue())) + "--status-" + status);

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor,
                                     int status) {

            if(source==0) {

                Log.i("xasdadaaaa", "onDescriptorRead");
                if (mBluetoothAdapter == null || mBluetoothGatt == null || mNotifyCharacteristic == null) {
                    Log.w(TAG, "BluetoothAdapter not initialized");
                    return;
                }
                byte[] value = descriptor.getValue();
//            Log.i(TAG, "onDescriptorRead. value=" + bytesToHexString(value));
                if (value != null && value.length == 1) {//老盒子蓝牙的BluetoothGattDescriptor.mValue的长度是1个字节，而新盒子蓝牙里是2字节。
//                    LogUtil.i("xasdadaaaa", "*** old bluetooth box.");

                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                } else {
//                    LogUtil.i("xasdadaaaa", "*** new bluetooth box.");
                    //                BluetoothGattDescriptor descriptor1 = mCharacteristic.getDescriptor(UUID_CLIENT_CHARACTERISTIC_CONFIG);
                    //                if (descriptor1 != null) {
                    //                    descriptor1.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    //                    mBluetoothGatt.writeDescriptor(descriptor1);
                    //                }
                    if (descriptor != null) {
//                        LogUtil.i("xasdadaaaa", "*** new bluetooth box.--2--");
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBluetoothGatt.writeDescriptor(descriptor);
                        //不知道写操作要多久，所以要在onDescriptorWrite()回调里执行createSendThread();
                    } else {
                        close();
                        broadcastUpdate(ACTION_GATT_FAILURE);
                    }
                }
            }

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor bd,
                                      int status) {
//            Log.e(TAG, "onDescriptorWrite");
//            LogUtil.i("xasdadaaaa", "onDescriptorWrite"  );
            if(source==0) {
//                createSendThread();
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int a, int b) {
//            Log.e(TAG, "onReadRemoteRssi");
            Log.i("xasdadaaaa", "onReadRemoteRssi"  );
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int a) {
//            Log.e(TAG, "onReliableWriteCompleted");
            Log.i("xasdadaaaa", "onReliableWriteCompleted"  );
        }

    };



    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        // For all other profiles, writes the data formatted in HEX.
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            //final StringBuilder stringBuilder = new StringBuilder(data.length);
            //for(byte byteChar : data)
            //    stringBuilder.append(String.format("%02X ", byteChar));
            //intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            intent.putExtra(EXTRA_DATA, data);
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
//                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
//            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }




    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {

        if (mBluetoothAdapter == null || address == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
/*
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
*/
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
//        mBluetoothGatt.connect();

//        Log.d(TAG, "Trying to create a new connection.");
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
//        if (UUID_NOTIFY.equals(characteristic.getUuid())) {
//            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
}
