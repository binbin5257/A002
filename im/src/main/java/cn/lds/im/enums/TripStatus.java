package cn.lds.im.enums;

/**
 * 时间戳类型
 */
public class TripStatus {
	public static final String OPEN = "OPEN";/** 刚创建*/
	public static final String ALLOCATED = "ALLOCATED";/** 已分配车辆状态 */
	public static final String PICKED_UP = "PICKED_UP"; /** 已取车 */
	public static final String DROPPED_OFF = "DROPPED_OFF";/** 已还车 */
	public static final String RETURN_OVERDUE = "RETURN_OVERDUE";/** 逾期未还车 */
	public static final String CANCELLED = "CANCELLED";/** 已取消 */
	public static final String PAID = "PAID";/** 已支付 */
	public static final String PAYMENT_OVERDUE = "PAYMENT_OVERDUE";/** 逾期未支付 */
}