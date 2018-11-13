package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 客服帮助还车事件
 * Created by E0608 on 2017/8/29.
 */

public class CustomerServiceReturnCarEvent {
    private TcloudMessage message;

    public CustomerServiceReturnCarEvent(TcloudMessage message) {
        this.message = message;
    }

    public TcloudMessage getMessage() {
        return message;
    }

    public void setMessage(TcloudMessage message) {
        this.message = message;
    }
}
