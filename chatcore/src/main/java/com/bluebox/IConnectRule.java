package com.bluebox;

import android.bluetooth.BluetoothDevice;

/**
 * Created by mac on 2017/11/14.
 * 各种盒子的匹配规则
 */

public interface IConnectRule {


    boolean isMatch(BluetoothDevice device, int connectType, String boxName, String ADDR_BLE_MARK);



}
