package cn.lds.chatcore.imtp;

import org.json.JSONException;

import java.util.List;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.DateHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.SPUtils;
import cn.lds.chatcore.common.SharedPreferencesHelper;
import cn.lds.chatcore.enums.ConnectionStatus;
import cn.lds.chatcore.enums.DateTimeType;
import cn.lds.chatcore.event.ConnectionStatusChangedEvent;
import cn.lds.chatcore.event.ImConnectedEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.imtp.message.Message;
import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.chatcore.imtp.message.UnknownMessage;
import cn.lds.chatcore.imtp.message.tcloud.ACCOUNT_RECHARGE;
import cn.lds.chatcore.imtp.message.tcloud.ACTIVITY_PUBLISHED;
import cn.lds.chatcore.imtp.message.tcloud.APPLICATION_PASS;
import cn.lds.chatcore.imtp.message.tcloud.APPLICATION_REFUSE;
import cn.lds.chatcore.imtp.message.tcloud.APPLICATION_WITHDRAW;
import cn.lds.chatcore.imtp.message.tcloud.AUDIT_AUTHORITY;
import cn.lds.chatcore.imtp.message.tcloud.AVAILABLE_VEHICLES_CHANGED;
import cn.lds.chatcore.imtp.message.tcloud.CONFIRM_RECEIVE;
import cn.lds.chatcore.imtp.message.tcloud.CREATE_APPLICATION;
import cn.lds.chatcore.imtp.message.tcloud.CUSTOM_NEWS;
import cn.lds.chatcore.imtp.message.tcloud.DELIVER_ACCEPT;
import cn.lds.chatcore.imtp.message.tcloud.DELIVER_COMPLETE;
import cn.lds.chatcore.imtp.message.tcloud.DEPOSIT_REFUND_SUCCESS;
import cn.lds.chatcore.imtp.message.tcloud.DOOR_CLOSED;
import cn.lds.chatcore.imtp.message.tcloud.DOOR_OPENED;
import cn.lds.chatcore.imtp.message.tcloud.ENROLLEE_BLACK;
import cn.lds.chatcore.imtp.message.tcloud.ENROLLEE_INFORMATION_EXPIRED;
import cn.lds.chatcore.imtp.message.tcloud.FOREGIFT_DEDUCTION;
import cn.lds.chatcore.imtp.message.tcloud.FOREGIFT_RECHARGE;
import cn.lds.chatcore.imtp.message.tcloud.FOREGIFT_WITHDRAWALS;
import cn.lds.chatcore.imtp.message.tcloud.ILLEGAL_IFNO;
import cn.lds.chatcore.imtp.message.tcloud.INVOICE_ACCEPT;
import cn.lds.chatcore.imtp.message.tcloud.INVOICE_REJECT;
import cn.lds.chatcore.imtp.message.tcloud.INVOICE_SEND;
import cn.lds.chatcore.imtp.message.tcloud.ORDER_PAY;
import cn.lds.chatcore.imtp.message.tcloud.PAYMENT_OVERDUE;
import cn.lds.chatcore.imtp.message.tcloud.PHONE_NUMBER_CHANGED;
import cn.lds.chatcore.imtp.message.tcloud.PICKUP_ACCEPT;
import cn.lds.chatcore.imtp.message.tcloud.REFUND_REJECTED;
import cn.lds.chatcore.imtp.message.tcloud.RETURN_OVERDUE;
import cn.lds.chatcore.imtp.message.tcloud.SCHEDULED_PICKUP;
import cn.lds.chatcore.imtp.message.tcloud.USER_APPROVED;
import cn.lds.chatcore.imtp.message.tcloud.USER_JOINED_ORGANIZATION;
import cn.lds.chatcore.imtp.message.tcloud.USER_REFUSED;
import cn.lds.chatcore.imtp.message.tcloud.USER_RELEASED_ORGANIZATION;
import cn.lds.chatcore.imtp.message.tcloud.USER_ZMXYSCORE_SUCCESS;
import cn.lds.chatcore.imtp.message.tcloud.VEHICLE_ASSIGN;
import cn.lds.chatcore.imtp.message.tcloud.VEHICLE_CANCELED;
import cn.lds.chatcore.imtp.message.tcloud.VEHICLE_CANCELED_ORGANIZATION;
import cn.lds.chatcore.manager.NetworkManager;
import cn.lds.im.sdk.bean.SendAckMessage;
import cn.lds.im.sdk.bean.SendMessage;
import cn.lds.im.sdk.enums.ConnAckReturnCode;
import cn.lds.im.sdk.enums.OsType;
import cn.lds.im.sdk.notification.CallbackListener;
import de.greenrobot.event.EventBus;

//import cn.lds.chatcore.data.MessageItem;
//import cn.lds.chatcore.event.MessageChangedEvent;
//import cn.lds.chatcore.manager.MessageManager;

/**
 * 回调
 * Created by user on 2016/1/4.
 */
public class ImtpCallbackListener implements CallbackListener {
	@Override
	public void sendComplete(List<SendAckMessage> sendAckMessages) {
		LogHelper.d("消息跟踪：发送完成");
//        for (SendAckMessage sendAckMessage : sendAckMessages) {
//            String messageId = sendAckMessage.getMessageId();
//            String serverMessageId = sendAckMessage.getServerMessageId();
//            MessageItem messageItem = MessageManager.getInstance().updateSendStatus(messageId, 1, serverMessageId);
//            EventBus.getDefault().post(new MessageChangedEvent(messageItem.getUser(), messageItem, MessageChangedEvent.TYPE_UPDATE));
//        }
	}

	@Override
	public void sendError(SendMessage sendMessage) {
		LogHelper.d("消息跟踪：发送错误");
//        MessageItem messageItem = MessageManager.getInstance().updateSendStatus(sendMessage.getMessageId(), -1);
//        EventBus.getDefault().post(new MessageChangedEvent(messageItem.getUser(), messageItem, MessageChangedEvent
//                .TYPE_UPDATE));
	}

	private synchronized void processMessage(List<SendMessage> messages) {
 		LogHelper.d(String.format("消息跟踪：--------------------条数：%s", messages.size()));
		if (messages != null) {
			// 处理单条消息
			for (SendMessage msg : messages) {
				try {
					MsgType msgType = MsgType.getValue(msg.getMessageType());
					LogHelper.d(String.format("消息跟踪：消息内容是 %s", msg.toString()));
					Message myMessage = parseMessage(msgType, msg);
					if (MsgType.UNKNOWN == myMessage.getType()) {
						LogHelper.d(String.format("消息跟踪：未知消息 %s", msg.toString()));
					} else {
						EventBus.getDefault().post(new MessageArrivedEvent(myMessage));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void messageArrived(List<SendMessage> messages) {
		this.processMessage(messages);
	}

	@Override
	public void connected() {
		LogHelper.d("IM连接：连接成功");
		ImtpManager.getInstance().stopTimer();
		EventBus.getDefault().post(new ImConnectedEvent());
		EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.CONNECTED));
	}

	@Override
	public void connectionKicked(OsType osType) {
		//调用注销
		LogHelper.d("退出登录：IM收到kicked消息。账号在别处登录！");
		CacheHelper.setImKickedTime(DateHelper.getSystemDate(DateTimeType.yyyyMMddHHmm));
		CacheHelper.setImKickedOsType(String.valueOf(osType.getValue()));
		MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getKickedFilter(), "账号在别处登录");
		ImtpManager.getInstance().stopTimer();
		EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.KICKED));
	}

	@Override
	public void connectError(ConnAckReturnCode connAckReturnCode) {
		LogHelper.d("IM连接：连接错误，错误码：" + connAckReturnCode.name());
		ImtpManager.getInstance().startTimer();
	}

	@Override
	public void connectionLost() {
		LogHelper.d("IM连接：连接丢失");
		ImtpManager.getInstance().startTimer();
		NetworkManager.getInstance().printNewWorkStatus();
	}

	@Override
	public void exceptionCause(Throwable throwable) {
		LogHelper.e("IM连接：发生异常", throwable);
	}

	/**
	 * 消息转换（从IM消息转换成安卓消息）
	 *
	 * @param msgType
	 * @param sendMessage
	 * @return
	 */
	private Message parseMessage(MsgType msgType, SendMessage sendMessage) {
		LogHelper.d(String.format("消息跟踪：%s", sendMessage.getMessage()));
		Message message = null;
		Class<?> clazz = null;
		switch (msgType) {
			case AVAILABLE_VEHICLES_CHANGED:
				clazz = AVAILABLE_VEHICLES_CHANGED.class;
				break;
			case ACTIVITY_PUBLISHED:
				clazz = ACTIVITY_PUBLISHED.class;
				break;
			case USER_APPROVED:
				clazz = USER_APPROVED.class;
				break;
			case USER_REFUSED:
				clazz = USER_REFUSED.class;
				break;
			case RETURN_OVERDUE:
				clazz = RETURN_OVERDUE.class;
				break;
			case PAYMENT_OVERDUE:
				clazz = PAYMENT_OVERDUE.class;
				break;
			case DOOR_OPENED:
				clazz = DOOR_OPENED.class;
				break;
			case DOOR_CLOSED:
				clazz = DOOR_CLOSED.class;
				break;
			case VEHICLE_CANCELED:
				clazz = VEHICLE_CANCELED.class;
				break;
			case FOREGIFT_RECHARGE:
				clazz = FOREGIFT_RECHARGE.class;
				break;
			case ACCOUNT_RECHARGE:
				clazz = ACCOUNT_RECHARGE.class;
				break;
			case ORDER_PAY:
				clazz = ORDER_PAY.class;
				break;
			case VEHICLE_ASSIGN:
				clazz = VEHICLE_ASSIGN.class;
				break;
			case SCHEDULED_PICKUP:
				clazz = SCHEDULED_PICKUP.class;
				break;
			case FOREGIFT_DEDUCTION:
				clazz = FOREGIFT_DEDUCTION.class;
				break;
			case FOREGIFT_WITHDRAWALS:
				clazz = FOREGIFT_WITHDRAWALS.class;
				break;
			case USER_JOINED_ORGANIZATION:
				clazz = USER_JOINED_ORGANIZATION.class;
				break;
			case USER_RELEASED_ORGANIZATION:
				clazz = USER_RELEASED_ORGANIZATION.class;
				break;
			case VEHICLE_CANCELED_ORGANIZATION:
				clazz = VEHICLE_CANCELED_ORGANIZATION.class;
				break;
			case ILLEGAL_IFNO:
				clazz = ILLEGAL_IFNO.class;
				break;
			case INVOICE_ACCEPT:
				clazz = INVOICE_ACCEPT.class;
				break;
			case INVOICE_REJECT:
				clazz = INVOICE_REJECT.class;
				break;
			case INVOICE_SEND:
				clazz = INVOICE_SEND.class;
				break;
			case DELIVER_COMPLETE:
				clazz = DELIVER_COMPLETE.class;
				break;
			case CONFIRM_RECEIVE:
				clazz = CONFIRM_RECEIVE.class;
				break;
			case DELIVER_ACCEPT:
				clazz = DELIVER_ACCEPT.class;
				break;
			case PICKUP_ACCEPT:
				clazz = PICKUP_ACCEPT.class;
				break;
			case CUSTOM_NEWS:
				clazz = CUSTOM_NEWS.class;
				break;
			case USER_ZMXYSCORE_SUCCESS:
				clazz = USER_ZMXYSCORE_SUCCESS.class;
				break;
			case ENROLLEE_INFORMATION_EXPIRED:
				clazz = ENROLLEE_INFORMATION_EXPIRED.class;
				break;
			case APPLICATION_PASS:
				clazz = APPLICATION_PASS.class;
				break;
			case APPLICATION_REFUSE:
				clazz = APPLICATION_REFUSE.class;
				break;
			case CREATE_APPLICATION:
				clazz = CREATE_APPLICATION.class;
				break;
			case PHONE_NUMBER_CHANGED:
				clazz = PHONE_NUMBER_CHANGED.class;
                break;
            case AUDIT_AUTHORITY:
				clazz = AUDIT_AUTHORITY.class;
				break;
			case ENROLLEE_BLACK:
				clazz = ENROLLEE_BLACK.class;
				break;
			case APPLICATION_WITHDRAW:
				clazz = APPLICATION_WITHDRAW.class;
				break;
			case REFUND_REJECTED:
				clazz = REFUND_REJECTED.class;
				break;
			case DEPOSIT_REFUND_SUCCESS:
				clazz = DEPOSIT_REFUND_SUCCESS.class;
				break;
			case VIOLATION_IS_DEALT_WITH:
				clazz = DEPOSIT_REFUND_SUCCESS.class;
				break;
			case GIVE_BALANCE:
				clazz = DEPOSIT_REFUND_SUCCESS.class;
				break;
			default:
				clazz = UnknownMessage.class;
				break;
		}

		try {
			message = (Message) clazz.newInstance();
			message.parse(msgType, sendMessage);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return message;
	}
}
