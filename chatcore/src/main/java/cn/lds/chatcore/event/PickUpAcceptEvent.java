package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;

/**
 * 上门接车工单接受
 * Created by sibinbin on 2017/8/11.
 */

public class PickUpAcceptEvent {

    private TcloudMessage message;

    public TcloudMessage getMessage() {
        return message;
    }

    public PickUpAcceptEvent(TcloudMessage message) {
        this.message = message;
    }
}
