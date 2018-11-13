package cn.lds.chatcore.imtp.message;

public enum MsgType {

	AVAILABLE_VEHICLES_CHANGED(401),   /*可用车数量变化*/
	CUSTOM_NEWS(301),
	ACTIVITY_PUBLISHED(302),   /* 活动发布*/
	USER_APPROVED(303), /*用户审核通过*/
	USER_REFUSED(304), /*用户审核拒绝*/
	RETURN_OVERDUE(305), /*用户逾期未还车*/
	PAYMENT_OVERDUE(306),  /*用户逾期未支付*/
	DOOR_OPENED(307),  /*开门*/
	DOOR_CLOSED(308),  /*关门*/
	VEHICLE_CANCELED(309), /*车辆自动取消（30分钟）*/
	FOREGIFT_RECHARGE(310),  /*押金充值*/
	ACCOUNT_RECHARGE(311),  /*账户充值*/
	ORDER_PAY(312),   /*订单支付*/
	VEHICLE_ASSIGN(313), /*分配车辆（存）*/
	SCHEDULED_PICKUP(314), /*到达预约取车时间*/
	DEPOSIT_REFUND_SUCCESS(315), /*押金退款成功*/
	FOREGIFT_DEDUCTION(316), /*后台扣除押金*/
	USER_JOINED_ORGANIZATION(317), /*用户加入到组织中*/
	USER_RELEASED_ORGANIZATION(318), // 解除组织用户关系
	VEHICLE_CANCELED_ORGANIZATION(319), /*违章信息*/
	INVOICE_ACCEPT(320), // 发票管理已接受
	INVOICE_REJECT(321), // 发票管理已拒绝
	INVOICE_SEND(322), // 发票管理已邮寄
	DELIVER_COMPLETE(323), // 送车完成
	CONFIRM_RECEIVE(324), // 确认接车
	DELIVER_ACCEPT(325), // 送车上门外勤接受工单
	PICKUP_ACCEPT(327), // 上门取车外勤接受工单
	USER_ZMXYSCORE_SUCCESS(330),// 成功获取芝麻分（给会员发送消息）
	FOREGIFT_WITHDRAWALS(331), /*押金提现*/
	REFUND_REJECTED(332), /*拒绝押金退款申请*/
	VIOLATION_IS_DEALT_WITH(333), // 车辆违章已处理
	GIVE_BALANCE(334), // 赠送会员余额
	ILLEGAL_IFNO(360), /*违章信息*/
	ENROLLEE_INFORMATION_EXPIRED(370),// 用户身份证或者驾驶证到期
	CREATE_APPLICATION(371), // 新建用车申请
	APPLICATION_PASS(372), // 申请通过
	APPLICATION_REFUSE(373), // 申请拒绝
	PHONE_NUMBER_CHANGED(402), // 手机号变化
	AUDIT_AUTHORITY(403), // 审核权限变化
	APPLICATION_WITHDRAW(374),// 撤销审批
	ENROLLEE_BLACK(404), // 账号被拉黑
	UNKNOWN(-1)     /* 未知 */,


	Notify(999);

	private int value = -1;

	MsgType(final int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static MsgType getValue(final int value) {
		for (final MsgType type : MsgType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return UNKNOWN;
	}

}
