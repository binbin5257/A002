package cn.lds.chatcore.common;

public class Constants {
    // 核心常量
    private static ICoreUrls iCoreUrls;

    /**
     * 获取 核心常量
     *
     * @return
     */
    public static ICoreUrls getCoreUrls() {
        return iCoreUrls;
    }

    /**
     * 设置 核心常量
     *
     * @param urls
     */
    public static void setCoreUrls(ICoreUrls urls) {
        Constants.iCoreUrls = urls;
    }

    /**
     * 头像文件
     */
    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    /**
     * 车辆文件
     */
    public static final String CAR_FILE_NAME = "car_photo.jpg";


    /**
     * 客服电话
     */
    public static final String PHONE_ADI = "15551231997";
    public static final String PHONE_CONSULT = "15551231997";

    /************************** 共通常量 END ************************************/


    /**
     * 订单状态
     */

    public static final String OPEN = "OPEN";   // 刚创建
    public static final String ALLOCATED = "ALLOCATED";   // 已分配车辆状态
    public static final String PICKED_UP = "PICKED_UP";   // 已取车
    public static final String DROPPED_OFF = "DROPPED_OFF";   // 已还车
    public static final String PAID = "PAID";   // 已支付
    public static final String CANCELLED = "CANCELLED";   // 已取消
    public static final String RETURN_OVERDUE = "RETURN_OVERDUE";   // 逾期未还车
    public static final String PAYMENT_OVERDUE = "PAYMENT_OVERDUE";   // 逾期未支付


    /**
     * 个人认证状态
     */
    public static final String UNCOMMITTED = "UNCOMMITTED"; //未认证
    public static final String UNCOMMITTEDNAME = "未认证"; //未认证
    public static final String REVIEWING = "REVIEWING";   // 待审核
    public static final String REVIEWINGNAME = "审核中";   // 待审核
    public static final String APPROVED = "APPROVED";      // 审核通过
    public static final String APPROVEDNAME = "已认证";      // 审核通过
    public static final String REFUSED = "REFUSED"; // 审核拒绝
    public static final String REFUSEDNAME = "认证失败"; // 审核拒绝

    /**
     * 订单类型
     */
    public static final String INSTANT = "INSTANT"; // 即时用车
    public static final String SCHEDULED = "SCHEDULED"; // 预约用车

    /**
     * 订单有效时长
     */
    public static final long order_time = 1800000;   // 单位毫秒


    /**
     * 后台系统配置 即时租车不取车自动取消的分钟数
     */
    public static int instantAutoCancelMinute;
    /**
     * 后台系统配置 能预约车量距离现在的最短分钟数
     */
    public static int minMinuteScheduledInterval;
    /**
     * 后台系统配置 预约租车不取车自动取消的分钟数
     */
    public static int scheduledAutoCancelMinute;

    public static void setInstantAutoCancelMinute(int instantAutoCancelMinute) {
        Constants.instantAutoCancelMinute = instantAutoCancelMinute;
    }

    public static void setMinMinuteScheduledInterval(int minMinuteScheduledInterval) {
        Constants.minMinuteScheduledInterval = minMinuteScheduledInterval;
    }

    public static void setScheduledAutoCancelMinute(int scheduledAutoCancelMinute) {
        Constants.scheduledAutoCancelMinute = scheduledAutoCancelMinute;
    }
}
