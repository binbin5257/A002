package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

/**
 * 送车上门外勤接受工单
 */
public class PICKUP_ACCEPT extends TcloudMessage {


	public MsgType getType() {
		return MsgType.PICKUP_ACCEPT;
	}

	@Override
	public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
		try {
			JSONObject json = new JSONObject((String) sendMessage.getMessage());
			setMessageType(MsgType.PICKUP_ACCEPT.name());
			setTitle(json.getString("title"));
			setContent(json.getString("content"));
			setObjectId(json.getString("objectId"));
			super.parse(type, sendMessage);
		} catch (Exception e) {
			this.setParseError(true);
		}
	}


}
