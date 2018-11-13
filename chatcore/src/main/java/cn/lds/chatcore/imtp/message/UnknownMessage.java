package cn.lds.chatcore.imtp.message;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lds.chatcore.common.LogHelper;
import cn.lds.im.sdk.bean.SendMessage;

public class UnknownMessage extends Message {

	private String unkonown;

	public String getUnkonown() {
		return unkonown;
	}

	public void setUnkonown(String unkonown) {
		this.unkonown = unkonown;
	}

	public MsgType getType() {
		return MsgType.UNKNOWN;
	}

	@Override
	public void parse(MsgType type, final SendMessage sendMessage) {
//		if (!(jsonStr instanceof String))
//			return;
		try {
			JSONObject json = new JSONObject((String) sendMessage.getMessage());
			super.parse(type, sendMessage);
			this.setUnkonown((String) sendMessage.getMessage());

		} catch (JSONException e) {
			this.setParseError(true);
		}
	}

	@Override
	public String createContentJsonStr() {
		JSONObject json = new JSONObject();
		try {
			//TODO:新IM接口不需要这三个属性
//			json.put("from", this.getFrom());
//			json.put("to", this.getTo());
//			json.put("timestamp", String.valueOf(this.getTimestamp()));
			json.put("unknow", this.getUnkonown());
		} catch (JSONException e) {
			LogHelper.e(this.getClass().getSimpleName(), e);
		}
		return this.addContentHeader(json.toString());
	}

}
