package cn.lds.im.enums;

/**
 * 费用明细
 */
public enum CostTypeEnum {
	ACCOUNT_RECHARGE("余额充值")
	, ACCOUNT_PAY("余额支付")
	, FOREGIFT_RECHARGE("押金充值")
	, FOREGIFT_WITHDRAWALS("押金提现（退款）")
	, FOREGIFT_CUT("押金扣款")
	, WEIXIN_PAY("微信支付")
	, ALIPAY("支付宝支付")
	, FOREGIFT_WITHDRAWALS_APPLY("申请押金退款")
	, FOREGIFT_WITHDRAWALS_REFUSED("拒绝押金退款")
	;
	private String value;
	private CostTypeEnum(final String value) {
		this.value = value;
	}

	public static CostTypeEnum getValue(final String value) {
		switch (value) {
			case "余额充值":
				return ACCOUNT_RECHARGE;
			case "余额支付":
				return ACCOUNT_PAY;
			case "押金充值":
				return FOREGIFT_RECHARGE;
			case "押金提现":
				return FOREGIFT_WITHDRAWALS;
			case "押金扣款":
				return FOREGIFT_CUT;
			case "微信支付":
				return WEIXIN_PAY;
			case "支付宝支付":
				return ALIPAY;
			case "申请押金退款":
				return FOREGIFT_WITHDRAWALS_APPLY;
			case "拒绝押金退款":
				return FOREGIFT_WITHDRAWALS_REFUSED;

			default:
				return ACCOUNT_RECHARGE;
		}
	}

	public static String getName(String value) {
		for (CostTypeEnum statusEnum : CostTypeEnum.values()) {
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