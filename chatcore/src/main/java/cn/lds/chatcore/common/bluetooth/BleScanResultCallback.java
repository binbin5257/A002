package cn.lds.chatcore.common.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by E0608 on 2018/1/13.
 */

public interface BleScanResultCallback {

    void onFindDevice(BluetoothDevice device, int rssi, byte[] scanRecord);
}
