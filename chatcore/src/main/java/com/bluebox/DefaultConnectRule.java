package com.bluebox;

import android.bluetooth.BluetoothDevice;

/**
 * Created by mac on 2017/11/14.
 */

public class DefaultConnectRule implements IConnectRule {
    @Override
    public boolean isMatch(BluetoothDevice device, int connectType, String boxName, String ADDR_BLE_MARK) {
        return device != null
                && device.getAddress() != null
                && device.getAddress().equals(boxName)
                && device.getAddress().indexOf(ADDR_BLE_MARK) >= 0;
    }
}
