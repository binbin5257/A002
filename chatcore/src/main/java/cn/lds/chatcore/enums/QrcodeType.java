package cn.lds.chatcore.enums;

/**
 * 时间戳类型
 */
public enum QrcodeType {
	personal("personal")	/** 个人 */
	, group("group")			/** 群 */
	, subscription("subscription")	/** 订阅号 */
	, activity("activity")		/** 活动 */
	;

	private String value = "001";

	private QrcodeType(final String value) {
		this.value = value;
	}

	public static QrcodeType getValue(final String value) {
		switch (value) {
			case "personal":
				return personal;
			case "group":
				return group;
			case "subscription":
				return subscription;
			case "activity":
				return activity;
			default:
				return personal;
		}
	}

	public String value() {
		return this.value;
	}
}