package cn.lds.im.enums;

/**
 * 时间戳类型
 */
public enum ReceiptStatusEnum {
	REQUESTED("已请求")	                		/** 已请求*/
	, ACCEPTED("已接受")					/** 已接受 */
	, MAILED("已邮寄")	       			 /** 已邮寄 */
	, REJECTED("已拒绝")				/** 已拒绝 */
	;
	private String value;
	private ReceiptStatusEnum(final String value) {
		this.value = value;
	}

	public static String getName(String value) {
		for (ReceiptStatusEnum statusEnum : ReceiptStatusEnum.values()) {
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