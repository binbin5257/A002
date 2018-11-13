package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 送车上门外勤接受工单
 */
public class DeliverAcceptEvent {
    private TcloudMessage message;

    public DeliverAcceptEvent(TcloudMessage message) {
        this.message = message;
    }

    public TcloudMessage getMessage() {
        return message;
    }

    public void setMessage(TcloudMessage message) {
        this.message = message;
    }
}
