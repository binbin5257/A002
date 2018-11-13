package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 外勤送车完成event
 */
public class DeliverCompleteEvent {
    private TcloudMessage message;

    public DeliverCompleteEvent(TcloudMessage message) {
        this.message = message;
    }

    public TcloudMessage getMessage() {
        return message;
    }

    public void setMessage(TcloudMessage message) {
        this.message = message;
    }
}
