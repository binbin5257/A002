package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

public class APPLICATION_PASS extends TcloudMessage {

    public MsgType getType() {
        return MsgType.APPLICATION_PASS;
    }
    @Override
    public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
        try {
            JSONObject json = new JSONObject((String) sendMessage.getMessage());
            setMessageType(MsgType.APPLICATION_PASS.name());
            setTitle(json.getString("title"));
            setContent(json.getString("content"));
            setObjectId(json.getString("objectId"));
            super.parse(type, sendMessage);
        } catch (Exception e) {
            this.setParseError(true);
        }
    }

}