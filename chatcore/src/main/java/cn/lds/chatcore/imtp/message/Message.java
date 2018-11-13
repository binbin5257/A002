package cn.lds.chatcore.imtp.message;

import org.json.JSONException;

import java.lang.reflect.Field;

import cn.lds.im.sdk.bean.SendMessage;

public abstract class Message {
	private String messageId;
	private String from;
	private String to;

	private MsgType type = MsgType.UNKNOWN;
	private long timestamp;

	private boolean parseError = false;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public MsgType getType() {
		return this.type;
	}

	public void setType(MsgType type) {
		this.type = type;
	}

	public boolean isParseError() {
		return parseError;
	}

	public void setParseError(boolean parseError) {
		this.parseError = parseError;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

//	public void parse(MsgType type, Object json) throws JSONException {
//		if (json instanceof JSONObject) {
//			JSONObject jsonObj = (JSONObject) json;
//			this.setMessageId(jsonObj.getString("messageId"));
//			this.setFrom(jsonObj.getString("from"));
//			this.setTo(jsonObj.getString("to"));
//			// this.setVersion(jsonObj.getString("version"));
//			this.setTimestamp(jsonObj.getLong("timestamp"));
//		}
//
//	};

	public void parse(MsgType type, SendMessage sendMessage) throws JSONException {
		this.setMessageId(sendMessage.getMessageId());
		this.setFrom(sendMessage.getFromClientId());
		this.setTo(sendMessage.getToClientId());
		this.setTimestamp(sendMessage.getTime());
	}

	public String toString() {
		Field[] field = this.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append("id" + ": " + this.getMessageId() + "|");
		sb.append("from" + ": " + this.getFrom() + "|");
		sb.append("to" + ": " + this.getTo() + "|");
		sb.append("type" + ": " + this.getType() + "|");
		sb.append("timestamp" + ": " + this.getTimestamp() + "|");
		sb.append("parseError" + ": " + this.isParseError() + "|");

		for (int i = 0; i < field.length && field.length > 0; i++) {
			field[i].setAccessible(true);
			try {
				sb.append(field[i].getName() + ": "
						+ field[i].get(this).toString() + "|");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public abstract String createContentJsonStr();

	protected String addContentHeader(String jsonStr) {
		/*
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getType().value());
		buffer.append(jsonStr);
		return buffer.toString();
		*/
		return jsonStr;
	}

}
