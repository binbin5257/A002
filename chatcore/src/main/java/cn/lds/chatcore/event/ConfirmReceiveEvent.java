package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 外勤确认接车event
 */
public class ConfirmReceiveEvent {

    private TcloudMessage message;

    public TcloudMessage getMessage() {
        return message;
    }


    public ConfirmReceiveEvent(TcloudMessage message) {
        this.message = message;
    }
}
