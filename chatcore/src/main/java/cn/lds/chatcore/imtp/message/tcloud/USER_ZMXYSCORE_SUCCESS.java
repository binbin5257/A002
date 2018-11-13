package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

/**
 * Created by E0608 on 2017/10/31.
 */

public class USER_ZMXYSCORE_SUCCESS extends TcloudMessage{

    public MsgType getType() {
        return MsgType.USER_ZMXYSCORE_SUCCESS;
    }
    @Override
    public void parse(MsgType type, final SendMessage sendMessage) {
        try {
            JSONObject json = new JSONObject((String) sendMessage.getMessage());
            setMessageType(MsgType.USER_ZMXYSCORE_SUCCESS.name());
            setTitle(json.getString("title"));
            setContent(json.getString("content"));
            setObjectId(json.getString("objectId"));
            super.parse(type, sendMessage);
        } catch (Exception e) {
            this.setParseError(true);
        }
    }
}
