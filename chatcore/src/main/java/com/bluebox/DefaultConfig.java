package com.bluebox;

import java.util.UUID;

/**
 * Created by mac on 2017/11/14.
 */

public class DefaultConfig implements IConfig {
    public final UUID UUID_NOTIFY =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public final UUID UUID_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final UUID UUID_NOTIFY_READ =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");


    @Override
    public UUID getUuidNotify() {
        return UUID_NOTIFY;
    }

    @Override
    public UUID getUuidNotifyRead() {
        return UUID_NOTIFY_READ;
    }

    @Override
    public UUID getUuidService() {
        return UUID_SERVICE;
    }

    @Override
    public boolean isRegisterReadGattCharacteristic() {
        return true;
    }
}
