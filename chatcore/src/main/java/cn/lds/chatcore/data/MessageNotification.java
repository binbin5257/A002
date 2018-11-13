package cn.lds.chatcore.data;

import java.util.Date;

/**
 * Notification for the contact.
 * 
 * @author alexander.ivanov
 * 
 */
public class MessageNotification {

	private String account;

	private String user;
	/**
	 * Text of the last message.
	 */
	private String text;

	/**
	 * Timestamp of the last message.
	 */
	private Date timestamp;

	/**
	 * Number of messages.
	 */
	private int count;

	public MessageNotification(String account, String user, String text, Date timestamp, int count) {
		this.account = account;
		this.user = user;
		this.text = text;
		this.timestamp = timestamp;
		this.count = count;
	}

	public String getText() {
		return text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int getCount() {
		return count;
	}

	public String getAccount() {
		return account;
	}

	public String getUser() {
		return user;
	}

	public void addMessage(String text) {
		this.text = text;
		this.timestamp = new Date();
		this.count += 1;
	}

}