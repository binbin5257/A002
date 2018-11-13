package cn.lds.chatcore.event;

import cn.lds.chatcore.imtp.message.Message;

/**
 * 新消息到达事件
 * <p>
 * 新消息在IM SDK中接收，并解析成 @see cn.lds.chat.imtp.message.Message，并广播出去
 * </p>
 * 
 * @author suncf
 * 
 */
public class MessageArrivedEvent {

	public final Message message;

	public MessageArrivedEvent(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

}
