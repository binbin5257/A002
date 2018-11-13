package cn.lds.chatcore.data;

/**
 * Created by xuqm on 2016/11/26.
 */

public class PaymentModel {
    /**
     * status : success
     * data : true
     */

    private String status;
    private boolean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
