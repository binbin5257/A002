package com.bluebox;

import android.bluetooth.BluetoothDevice;

/**
 * Created by sibinbin on 18-1-17.
 */

public class DeviceNameConnectRule implements IConnectRule{
    @Override
    public boolean isMatch(BluetoothDevice device, int connectType, String boxName, String ADDR_BLE_MARK) {
        return device != null && device.getName() != null
                && ((connectType==0&&device.getName().equals(boxName))||(connectType==1&&device.getAddress().equals(boxName)))
                && device.getAddress() != null;
    }
}
