package com.bluebox;

import android.bluetooth.BluetoothDevice;

/**
 * Created by mac on 2017/11/14.
 * 小蜜电动车连接匹配蓝牙盒子规则
 */

public class XiaoMiConnectRule implements IConnectRule{


    @Override
    public boolean isMatch(BluetoothDevice device, int connectType, String boxName, String ADDR_BLE_MARK) {


        return device != null && device.getName() != null
                && ((connectType==0&&device.getName().equals(boxName))||(connectType==1&&device.getAddress().equals(boxName)))
                && device.getAddress() != null
                ;
    }
}
