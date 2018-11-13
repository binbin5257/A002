package cn.lds.im.enums;

/**
 * 优惠劵状态
 */
public enum CouponStatusEnum {
	RELEASE("立即使用")
	, USED("已使用")
	, OUTDATED("已过期")
	;
	private String value;
	private CouponStatusEnum(final String value) {
		this.value = value;
	}

	public static CouponStatusEnum getValue(final String value) {
		switch (value) {
			case "可使用":
				return RELEASE;
			case "已使用":
				return USED;
			case "已过期":
				return OUTDATED;
			default:
				return RELEASE;
		}
	}

	public static String getName(String value) {
		for (CouponStatusEnum statusEnum : CouponStatusEnum.values()) {
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