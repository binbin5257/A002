package com.bluebox;

import java.util.UUID;

/**
 * Created by mac on 2017/11/14.
 */

public class XiaomiConfig implements IConfig {

    public final UUID UUID_NOTIFY =
            UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cba");
    public final UUID UUID_SERVICE =
            UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb7");

    public final UUID UUID_NOTIFY_READ =
            UUID.fromString("0783b03e-8535-b5a0-7140-a304d2495cb8");

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
