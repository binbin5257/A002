package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

/**
 * Created by leadingsoft on 2017/3/10.
 */
public class FOREGIFT_WITHDRAWALS extends TcloudMessage {



    public MsgType getType() {
        return MsgType.FOREGIFT_WITHDRAWALS;
    }
    @Override
    public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
        try {
            JSONObject json = new JSONObject((String) sendMessage.getMessage());
            setMessageType(MsgType.FOREGIFT_WITHDRAWALS.name());
            setTitle(json.getString("title"));
            setContent(json.getString("content"));
            setObjectId(json.getString("objectId"));
            super.parse(type, sendMessage);
        } catch (Exception e) {
            this.setParseError(true);
        }
    }


}

