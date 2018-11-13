package cn.lds.im.enums;

/**
 * 时间戳类型
 */
public class CostType {
	public static final String ACCOUNT_RECHARGE = "ACCOUNT_RECHARGE";           /** 余额充值*/
	public static final String ACCOUNT_PAY = "ACCOUNT_PAY";                     /** 余额支付 */
	public static final String FOREGIFT_RECHARGE = "FOREGIFT_RECHARGE";         /** 押金充值 */
	public static final String FOREGIFT_WITHDRAWALS = "FOREGIFT_WITHDRAWALS";   /** 押金提现 */
	public static final String FOREGIFT_CUT = "FOREGIFT_CUT";                   /** 押金扣款 */
	public static final String WEIXIN_PAY = "WEIXIN_PAY";                     /** 微信扣款 */
	public static final String ALIPAY = "ALIPAY";                         /** 支付宝扣款 */
	public static final String FOREGIFT_WITHDRAWALS_APPLY = "FOREGIFT_WITHDRAWALS_APPLY";   /** 申请押金退款 */
	public static final String FOREGIFT_WITHDRAWALS_REFUSED = "FOREGIFT_WITHDRAWALS_REFUSED";/** 拒绝押金退款*/

}