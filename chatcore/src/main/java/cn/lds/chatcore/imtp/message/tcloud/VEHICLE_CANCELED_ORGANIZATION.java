package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

public class VEHICLE_CANCELED_ORGANIZATION extends TcloudMessage {



    public MsgType getType() {
        return MsgType.VEHICLE_CANCELED_ORGANIZATION;
    }
    @Override
    public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
        try {
            JSONObject json = new JSONObject((String) sendMessage.getMessage());
            setMessageType(MsgType.VEHICLE_CANCELED_ORGANIZATION.name());
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
