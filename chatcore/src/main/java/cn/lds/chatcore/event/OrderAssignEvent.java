package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 订单已分配event
 */
public class OrderAssignEvent {
    private TcloudMessage message;

    public OrderAssignEvent(TcloudMessage message) {
        this.message = message;
    }

    public TcloudMessage getMessage() {
        return message;
    }

    public void setMessage(TcloudMessage message) {
        this.message = message;
    }
}
