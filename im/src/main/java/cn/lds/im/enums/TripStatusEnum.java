package cn.lds.im.enums;

/**
 * 时间戳类型
 */
public enum TripStatusEnum {
	OPEN("待分配")	                		/** 刚创建*/
	, ALLOCATED("待取车")					/** 已分配车辆状态 */
	, PICKED_UP("进行中")	       			 /** 已取车 */
	, DROPPED_OFF("待支付")				/** 已还车 */
	, RETURN_OVERDUE("逾期未还车")		/** 逾期未还车 */
	, CANCELLED("已取消")					/** 已取消 */
	, PAID("已完成")						/** 已支付 */
	, PAYMENT_OVERDUE("逾期未支付")      /** 逾期未支付 */
	;
	private String value;
	private TripStatusEnum(final String value) {
		this.value = value;
	}

	public static TripStatusEnum getValue(final String value) {
		switch (value) {
			case "刚创建":
				return OPEN;
			case "已分配":
				return ALLOCATED;
			case "已取车":
				return PICKED_UP;
			case "已还车":
				return DROPPED_OFF;
			case "逾期未还车":
				return RETURN_OVERDUE;
			case "取消":
				return CANCELLED;
			case "已支付":
				return PAID;
			case "逾期未支付":
				return PAYMENT_OVERDUE;
			default:
				return OPEN;
		}
	}

	public static String getName(String value) {
		for (TripStatusEnum statusEnum : TripStatusEnum.values()) {
			if (statusEnum.value().equals(value)) {
				return statusEnum.name();
			}
		}
		return "";
	}

	public String value() {
		return this.value;
	}
}