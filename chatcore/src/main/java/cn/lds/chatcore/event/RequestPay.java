package cn.lds.chatcore.event;


/**
 * 支付event,
 *
 * @author xuqm
 */
public class RequestPay {
    /**
     * 0==支付押金
     * 1==用户充值
     * 2==支付订单
     */
    private int type;
    /**
     * -1==支付失败
     * -2==用户取消支付
     * 0==支付成功
     */
    private int status;

    /**
     * @param type   0==支付押金;1==用户充值;2==支付订单
     * @param status -1==支付失败;-2==用户取消支付;0==支付成功
     */
    public RequestPay(int type, int status) {
        this.type = type;
        this.status = status;
    }

    /**
     * -1==支付失败
     * -2==用户取消支付
     * 0==支付成功
     */
    public int getStatus() {
        return status;
    }

    /**
     * -1==支付失败
     * -2==用户取消支付
     * 0==支付成功
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 0==支付押金
     * 1==用户充值
     * 2==支付订单
     */
    public int getType() {
        return type;
    }

    /**
     * @param type 0==支付押金;1==用户充值;2==支付订单
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RequestPay{" +
                "type=" + type +
                ", status=" + status +
                '}';
    }
}
