package com.bluebox;

import java.util.UUID;

/**
 * Created by mac on 2017/11/14.
 */

public interface IConfig {


    UUID getUuidNotify();
    UUID getUuidNotifyRead();

    UUID getUuidService();


    boolean isRegisterReadGattCharacteristic();


}
