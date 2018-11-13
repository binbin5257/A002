package cn.lds.chatcore.enums;

/**
 * 时间戳类型
 */
public enum GenderType {
	  UNKNOWN("")	/** 未知 */
	, male("male")	/** 男 */
	, female("female")	/** 女 */
	;

	private String value = "";

	private GenderType(final String value) {
		this.value = value;
	}

	public static GenderType getValue(final String value) {
		switch (value) {
		case "":
			return UNKNOWN;
		case "male":
			return male;
		case "female":
			return female;
		default:
			return UNKNOWN;
		}
	}

	public String value() {
		return this.value;
	}
}