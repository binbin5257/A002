package cn.lds.chatcore.enums;

/**
 * 第三方账号类型
 */
public enum ThirdAccountType {

	  UNKNOWN("000")	/** 未知 */
	, WEIXIN("001")	/** 微信 */
	;

	private String value = "00";

	private ThirdAccountType(final String value) {
		this.value = value;
	}

	public static ThirdAccountType getValue(final String value) {
		switch (value) {
		case "000":
			return UNKNOWN;
		case "001":
			return WEIXIN;
		default:
			return UNKNOWN;
		}
	}

	public String value() {
		return this.value;
	}
}