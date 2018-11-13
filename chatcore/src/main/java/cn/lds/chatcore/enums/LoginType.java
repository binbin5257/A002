package cn.lds.chatcore.enums;

/**
 * 时间戳类型
 */
public enum LoginType {
	  UNKNOWN("000")	/** 未知 */
	, id_pass("id_pass")	/** loginId+密码登录 */
	, mobile_pass("mobile_pass")	/** mobile+密码登录 */
	, mobile_captcha("mobile_captcha")	/** mobile+短信验证码登录 */
	, nonceToken("nonceToken")	/** 一次性认证token */
	, authorizationCode("authorizationCode")	/** 微信登录号 */
	;

	private String value = "id_pass";

	private LoginType(final String value) {
		this.value = value;
	}

	public static LoginType getValue(final String value) {
		switch (value) {
		case "000":
			return UNKNOWN;
		case "id_pass":
			return id_pass;
		case "mobile_pass":
			return mobile_pass;
		case "mobile_captcha":
			return mobile_captcha;
		case "nonceToken":
			return nonceToken;
		case "authorizationCode":
			return authorizationCode;
		default:
			return UNKNOWN;
		}
	}

	public String value() {
		return this.value;
	}
}