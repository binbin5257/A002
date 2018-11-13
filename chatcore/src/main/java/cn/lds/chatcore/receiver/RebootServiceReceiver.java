package cn.lds.chatcore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.SystemConfig;
import cn.lds.chatcore.imtp.ImtpManager;

/**
 * 服务保持
 */
public class RebootServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppIndependentConfigure.SYS_CONFIG_APP_PACKAGE+".rebootservice")) {
            LogHelper.d("RebootServiceReceiver:接收到服务重启广播");
            ImtpManager.getInstance().startService(context);
        }
    }
}
