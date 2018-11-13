package cn.lds.chatcore.imtp.message.tcloud;

import org.json.JSONObject;

import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.im.sdk.bean.SendMessage;

/**
 * 确认接车的IM通知
 */
public class CONFIRM_RECEIVE extends TcloudMessage {


	public MsgType getType() {
		return MsgType.CONFIRM_RECEIVE;
	}

	@Override
	public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
		try {
			JSONObject json = new JSONObject((String) sendMessage.getMessage());
			setMessageType(MsgType.CONFIRM_RECEIVE.name());
			setTitle(json.getString("title"));
			setContent(json.getString("content"));
			setObjectId(json.getString("objectId"));
			super.parse(type, sendMessage);
		} catch (Exception e) {
			this.setParseError(true);
		}
	}


}
