package cn.lds.chatcore.enums;

/**
 * 时间戳类型
 */
public enum SourceType {
	  UNKNOWN("")	/** 未知 */
	, addressList("addressList")	/** 来自通讯录 */
	, qrCode("qrCode")	/** 来自二维码 */
	;

	private String value = "";

	private SourceType(final String value) {
		this.value = value;
	}

	public static SourceType getValue(final String value) {
		switch (value) {
		case "":
			return UNKNOWN;
		case "addressList":
			return addressList;
		case "qrCode":
			return qrCode;
		default:
			return UNKNOWN;
		}
	}

	public String value() {
		return this.value;
	}
}