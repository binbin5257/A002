package cn.lds.chatcore.event;


import cn.lds.chatcore.data.HttpResult;

/**
 * 支付event,
 *
 * @author xuqm
 */
public class StartPay {
    private int type;
    private boolean status;
    private HttpResult httpResult;

    /**
     * @param type   0==支付押金;1==用户充值;2==支付订单
     * @param status -1==失败;0==成功
     */
    public StartPay(int type, boolean status) {
        this.type = type;
        this.status = status;
    }

    public StartPay(int type, boolean status, HttpResult httpResult) {
        this.type = type;
        this.status = status;
        this.httpResult = httpResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public HttpResult getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(HttpResult httpResult) {
        this.httpResult = httpResult;
    }
}
