package cn.lds.chatcore.manager;

import com.lidroid.xutils.db.sqlite.Selector;

import java.util.List;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.DateHelper;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.db.MessageTable;
import cn.lds.chatcore.enums.ConnectionStatus;
import cn.lds.chatcore.enums.DateTimeType;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.AvailableVehiclesChangedEvent;
import cn.lds.chatcore.event.ConfirmReceiveEvent;
import cn.lds.chatcore.event.ConnectionStatusChangedEvent;
import cn.lds.chatcore.event.CustomerServiceReturnCarEvent;
import cn.lds.chatcore.event.DeliverAcceptEvent;
import cn.lds.chatcore.event.DeliverCompleteEvent;
import cn.lds.chatcore.event.GiveBalanceEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.event.MessageAvaliableEvent;
import cn.lds.chatcore.event.OrderAssignEvent;
import cn.lds.chatcore.event.OrderCancelEvent;
import cn.lds.chatcore.event.PhoneChangeEvent;
import cn.lds.chatcore.event.PickUpAcceptEvent;
import cn.lds.chatcore.event.ViolationDealtWithEvent;
import cn.lds.chatcore.event.ZMXYScore;
import cn.lds.chatcore.imtp.ImtpManager;
import cn.lds.chatcore.imtp.message.Message;
import cn.lds.chatcore.imtp.message.MsgType;
import cn.lds.chatcore.imtp.message.tcloud.TcloudMessage;
import de.greenrobot.event.EventBus;

/**
 * Created by quwei on 2015/12/24.
 */
public class MessageManager extends AbstractManager {
	public static String _TAG = MessageManager.class.getSimpleName();
	protected static MessageManager instance;


	public static MessageManager getInstance() {
		if (instance == null) {
			try {
				instance = new MessageManager();
				MyApplication.getInstance().addManager(instance);
				EventBus.getDefault().register(instance);
			} catch (Exception ex) {
				LogHelper.e("初始化Manager", ex);
			}
		}
		return instance;
	}

	/**
	 * 写入数据库
	 *
	 * @param table
	 * @return
	 */
	private int write(MessageTable table) {
		try {
			DbHelper.getDbUtils().saveBindingId(table);
		} catch (Exception e) {
			LogHelper.e(_TAG, e);
		}
		LogHelper.e("消息跟踪：写入数据库");
		EventBus.getDefault().post(new MessageAvaliableEvent());
		return table.getId();
	}


	/**
	 * API请求处理
	 *
	 * @param event
	 */
	public void onEventBackgroundThread(MessageArrivedEvent event) {
		Message message = event.getMessage();
		MessageTable messageTable = new MessageTable();
		messageTable.setMessageId(message.getMessageId());
		messageTable.setIsNew(true);
		messageTable.setTime(message.getTimestamp());
		boolean isSaveToDb = true;


		final TcloudMessage tcloudMessage = ((TcloudMessage) message);
		messageTable.setTitle(tcloudMessage.getTitle());
		messageTable.setContent(tcloudMessage.getContent());
		messageTable.setObjectId(tcloudMessage.getObjectId());
		messageTable.setFromClientId(tcloudMessage.getFromClientId());
		messageTable.setToClientId(tcloudMessage.getToClientId());
		messageTable.setFromClientId(tcloudMessage.getFromClientId());
		messageTable.setToClientId(tcloudMessage.getToClientId());

		switch (message.getType()) {
			case AVAILABLE_VEHICLES_CHANGED:
				isSaveToDb = false;
				messageTable.setMessageType(MsgType.AVAILABLE_VEHICLES_CHANGED.name());
				EventBus.getDefault().post(new AvailableVehiclesChangedEvent());
				break;
			case ACTIVITY_PUBLISHED:
				messageTable.setMessageType(MsgType.ACTIVITY_PUBLISHED.name());
				break;

			case USER_APPROVED:
				isSaveToDb = false;
				messageTable.setMessageType(MsgType.USER_APPROVED.name());
				AccountManager.getInstance().updateReviewType(Constants.APPROVED);
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToolsHelper.showStatus(mApplicationContext, true, tcloudMessage.getContent());
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case USER_REFUSED:
				messageTable.setMessageType(MsgType.USER_REFUSED.name());
				AccountManager.getInstance().updateReviewType(Constants.REFUSED);
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToolsHelper.showStatus(mApplicationContext, false, tcloudMessage.getContent());
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case ENROLLEE_INFORMATION_EXPIRED:
				messageTable.setMessageType(MsgType.ENROLLEE_INFORMATION_EXPIRED.name());
				AccountManager.getInstance().updateReviewType(Constants.REFUSED);
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToolsHelper.showStatus(mApplicationContext, false, tcloudMessage.getContent());
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case RETURN_OVERDUE:
				messageTable.setMessageType(MsgType.RETURN_OVERDUE.name());
				break;
			case PAYMENT_OVERDUE:
				messageTable.setMessageType(MsgType.PAYMENT_OVERDUE.name());
				break;
			case ILLEGAL_IFNO:
				messageTable.setMessageType(MsgType.ILLEGAL_IFNO.name());
				break;
			case DOOR_OPENED:
				isSaveToDb = false;
				break;
			case DOOR_CLOSED:
				isSaveToDb = false;
				break;
			case VEHICLE_CANCELED:
				messageTable.setMessageType(MsgType.VEHICLE_CANCELED.name());
				EventBus.getDefault().post(new OrderCancelEvent(tcloudMessage));
				break;
			case FOREGIFT_RECHARGE:
				messageTable.setMessageType(MsgType.FOREGIFT_RECHARGE.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case ACCOUNT_RECHARGE:
				messageTable.setMessageType(MsgType.ACCOUNT_RECHARGE.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case ORDER_PAY:
				messageTable.setMessageType(MsgType.ORDER_PAY.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case VEHICLE_ASSIGN:
				messageTable.setMessageType(MsgType.VEHICLE_ASSIGN.name());
				EventBus.getDefault().post(new OrderAssignEvent(tcloudMessage));

				break;
			case FOREGIFT_DEDUCTION:
				messageTable.setMessageType(MsgType.FOREGIFT_DEDUCTION.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});

				break;
			case FOREGIFT_WITHDRAWALS:
				messageTable.setMessageType(MsgType.FOREGIFT_WITHDRAWALS.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});

				break;
			case USER_JOINED_ORGANIZATION:
				messageTable.setMessageType(MsgType.USER_JOINED_ORGANIZATION.name());
				AccountManager.getInstance().autoLoginWithNonceToken();
				MyApplication.confirmStatus = false;
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToolsHelper.showStatus(mApplicationContext, true, tcloudMessage.getContent());
					}
				});

				break;
			case USER_RELEASED_ORGANIZATION:
				MyApplication.confirmStatus = false;
				messageTable.setMessageType(MsgType.USER_RELEASED_ORGANIZATION.name());
				AccountManager.getInstance().autoLoginWithNonceToken();
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToolsHelper.showStatus(mApplicationContext, true, tcloudMessage.getContent());
					}
				});

				break;
			case VEHICLE_CANCELED_ORGANIZATION:
				MyApplication.org_cancel_order = true;
				messageTable.setMessageType(MsgType.VEHICLE_CANCELED_ORGANIZATION.name());
				EventBus.getDefault().post(new CustomerServiceReturnCarEvent(tcloudMessage));
				break;
			case SCHEDULED_PICKUP:
				messageTable.setMessageType(MsgType.SCHEDULED_PICKUP.name());
				isSaveToDb = false;
				break;
			case INVOICE_ACCEPT:
				messageTable.setMessageType(MsgType.INVOICE_ACCEPT.name());
				break;
			case INVOICE_REJECT:
				messageTable.setMessageType(MsgType.INVOICE_REJECT.name());
				break;
			case INVOICE_SEND:
				messageTable.setMessageType(MsgType.INVOICE_SEND.name());
				break;
			case DELIVER_COMPLETE: // 送车完成
				messageTable.setMessageType(MsgType.DELIVER_COMPLETE.name());
				EventBus.getDefault().post(new DeliverCompleteEvent(tcloudMessage));
				break;
			case CONFIRM_RECEIVE: // 确认接车
				messageTable.setMessageType(MsgType.CONFIRM_RECEIVE.name());
                EventBus.getDefault().post(new ConfirmReceiveEvent(tcloudMessage));
				break;
			case DELIVER_ACCEPT: // 送车上门外勤接受工单
                messageTable.setMessageType(MsgType.DELIVER_ACCEPT.name());
                EventBus.getDefault().post(new DeliverAcceptEvent(tcloudMessage));
                break;
            case PICKUP_ACCEPT: // 上门取车外勤接受工单
                messageTable.setMessageType(MsgType.PICKUP_ACCEPT.name());
                EventBus.getDefault().post(new PickUpAcceptEvent(tcloudMessage));
                break;
			case USER_ZMXYSCORE_SUCCESS: // 芝麻信用评分
                messageTable.setMessageType(MsgType.USER_ZMXYSCORE_SUCCESS.name());
                EventBus.getDefault().post(new ZMXYScore(tcloudMessage));
                break;
			case CREATE_APPLICATION: // 新建用车申请
				messageTable.setMessageType(MsgType.CREATE_APPLICATION.name());

				break;
			case APPLICATION_PASS: // 申请通过
				messageTable.setMessageType(MsgType.APPLICATION_PASS.name());

				break;
			case APPLICATION_REFUSE: // 申请拒绝
				messageTable.setMessageType(MsgType.APPLICATION_REFUSE.name());
				break;
			case APPLICATION_WITHDRAW: // 申请撤销
				messageTable.setMessageType(MsgType.APPLICATION_WITHDRAW.name());
				break;
			case PHONE_NUMBER_CHANGED: // 手机号变化
				messageTable.setMessageType(MsgType.PHONE_NUMBER_CHANGED.name());
				//注销
				CacheHelper.setImKickedTime(DateHelper.getSystemDate(DateTimeType.yyyyMMddHHmm));
				MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getKickedFilter(), "账号变更");
				ImtpManager.getInstance().stopTimer();
				EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.KICKED));
				break;
			case AUDIT_AUTHORITY: // 用车审批有权限变化
				messageTable.setMessageType(MsgType.AUDIT_AUTHORITY.name());

				CacheHelper.setImKickedTime(DateHelper.getSystemDate(DateTimeType.yyyyMMddHHmm));
				MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getKickedFilter(), "审核权限变化");
				ImtpManager.getInstance().stopTimer();
				EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.KICKED));
				break;
			case ENROLLEE_BLACK: // 账号被拉入黑名单
				messageTable.setMessageType(MsgType.ENROLLEE_BLACK.name());
				CacheHelper.setImKickedTime(DateHelper.getSystemDate(DateTimeType.yyyyMMddHHmm));
				MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getKickedFilter(), "账号被拉黑");
				ImtpManager.getInstance().stopTimer();
				EventBus.getDefault().post(new ConnectionStatusChangedEvent(ConnectionStatus.KICKED));
				break;
			case REFUND_REJECTED: // 拒绝押金退款申请
				messageTable.setMessageType(MsgType.REFUND_REJECTED.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case DEPOSIT_REFUND_SUCCESS: // 押金退款
				messageTable.setMessageType(MsgType.REFUND_REJECTED.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;
			case VIOLATION_IS_DEALT_WITH: // 车辆违章已处理
				messageTable.setMessageType(MsgType.VIOLATION_IS_DEALT_WITH.name());
				EventBus.getDefault().post(new ViolationDealtWithEvent());

				break;
			case GIVE_BALANCE: // 赠送会员余额
				messageTable.setMessageType(MsgType.GIVE_BALANCE.name());
				MyApplication.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新个人数据
						CoreHttpApi.enrolleesGet();
					}
				});
				break;




		}
		if (isSaveToDb) {
			this.write(messageTable);
		}
		if (!message.getType().equals(MsgType.AVAILABLE_VEHICLES_CHANGED))//大于400的不通知
			NotificationManager.getInstance().updateMessageNotification(messageTable.getId(), messageTable, true, true);
	}

	/**
	 * @return
	 */
	public List<MessageTable> findByNoAndPages(int page) {
		List<MessageTable> tables = null;
		try {
			tables = DbHelper.getDbUtils().findAll(Selector.from(MessageTable.class).orderBy("id",true).limit(10).offset((page-1)*10));
		} catch (Exception e) {
			LogHelper.e(this.getClass().getName(), e);
		}
		return tables;
	}

	/**
	 * @return
	 */
	public List<MessageTable> findByNo() {
		List<MessageTable> tables = null;
		try {
			tables = DbHelper.getDbUtils().findAll(Selector.from(MessageTable.class).orderBy("time", true));
		} catch (Exception e) {
			LogHelper.e(this.getClass().getName(), e);
		}
		return tables;
	}

	/**
	 * @return
	 */
	public MessageTable findCancel() {
		MessageTable tables = null;
		try {
			tables = DbHelper.getDbUtils().findFirst(Selector.from(MessageTable.class).where("messageType", "=", MsgType.VEHICLE_CANCELED_ORGANIZATION.name()));
		} catch (Exception e) {
			LogHelper.e(this.getClass().getName(), e);
		}
		return tables;
	}

	/**
	 * @return
	 */
	public void updateStatus() {
		List<MessageTable> tables = null;
		try {
			tables = DbHelper.getDbUtils().findAll(Selector.from(MessageTable.class).where("isNew", "==", true));
			if (null == tables)
				return;
			for (MessageTable ta : tables) {
				ta.setIsNew(false);
			}
			DbHelper.getDbUtils().updateAll(tables, "isNew");

		} catch (Exception e) {
			LogHelper.e(this.getClass().getName(), e);
		}
	}


	/**
	 * 更新是否有新消息。
	 */
	public boolean hasNewMsg() {
		boolean hasNewMsg = false;
		List<MessageTable> tables = null;
		try {
			tables = DbHelper.getDbUtils().findAll(Selector.from(MessageTable.class).where("isNew", "==", true));
			if (null == tables)
				return false;
			if (!tables.isEmpty())
				hasNewMsg = true;
		} catch (Exception e) {
			LogHelper.e(this.getClass().getName(), e);
		}
		return hasNewMsg;
	}
}
