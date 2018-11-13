package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 芝麻信用成功消息
 * Created by E0608 on 2017/10/30.
 */

public class ZMXYScore {
    private TcloudMessage message;

    public TcloudMessage getMessage() {
        return message;
    }

    public ZMXYScore(TcloudMessage message) {
        this.message = message;
    }
}
