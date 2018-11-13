package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

/**
 * 管理端自定义消息
 * Created by si'bin'bin on 2017/8/22.
 */

public class CUSTOM_NEWS extends TcloudMessage {
    public MsgType getType() {
        return MsgType.DELIVER_ACCEPT;
    }

    @Override
    public void parse(MsgType type, final SendMessage sendMessage) {
        try {
            JSONObject json = new JSONObject((String) sendMessage.getMessage());
            setMessageType(MsgType.DELIVER_ACCEPT.name());
            setTitle(json.getString("title"));
            setContent(json.getString("content"));
            setObjectId(json.getString("objectId"));
            super.parse(type, sendMessage);
        } catch (Exception e) {
            this.setParseError(true);
        }
    }
}
