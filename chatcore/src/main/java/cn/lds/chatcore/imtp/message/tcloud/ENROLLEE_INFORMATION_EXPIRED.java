package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

public class ENROLLEE_INFORMATION_EXPIRED extends TcloudMessage {



    public MsgType getType() {
        return MsgType.ENROLLEE_INFORMATION_EXPIRED;
    }
    @Override
    public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
        try {
            JSONObject json = new JSONObject((String) sendMessage.getMessage());
            setMessageType(MsgType.ENROLLEE_INFORMATION_EXPIRED.name());
            setTitle(json.getString("title"));
            setContent(json.getString("content"));
            setObjectId(json.getString("objectId"));
            setObjectNo(json.getString("objectNo"));
            super.parse(type, sendMessage);
        } catch (Exception e) {
            this.setParseError(true);
        }
    }


}
