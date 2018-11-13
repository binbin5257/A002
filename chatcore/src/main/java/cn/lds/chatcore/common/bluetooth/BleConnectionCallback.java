package cn.lds.chatcore.common.bluetooth;

import android.bluetooth.BluetoothGatt;

/**
 * Created by E0608 on 2018/1/13.
 */

public interface BleConnectionCallback {

    void onConnectionStateChange(BluetoothGatt gatt, int status, int newState);
}
