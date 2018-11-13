package cn.lds.chatcore.data;

/**
 * Created by xuqm on 2016/6/25.
 */
public class PayAliInfoModel {
    /**
     * data : {"aliPayParaStr":"app_id=2016081601758040&biz_content=%7B%22out_trade_no%22%3A%2220161121134749672341479707269859%22%2C%22product_code%22%3A%22QUICK_MSECURITYPAY%22%2C%22subject%22%3A%22%E6%8A%BC%E9%87%91%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%22%2C%22total_amount%22%3A0.01%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F124.93.0.168%3A22059%2Fw%2Fnotifies%2Falipay&sign_type=RSA&timestamp=2016-11-21%2013%3A47%3A49&version=1.0&sign=bYyYFGtFms7HWXAg9fQCj9DtvHBUjeYjIxHoG%2FL89qP7GBXFaSreRXTlbrlnMknBSkjA8ZYUqQZ%2BofPcYnUzLEyWZUFuDl0hJ%2FYkZe%2B4XZSb1HkZ%2F%2FYMMbdbsPNxbvEdL15LYjqmp%2F6riFpKEFq3VLJ7tt%2FvWpgmchzzussKzHk%3D","businessId":"FOREGIFT_35","id":"<null>"}
     * status : success
     */

    private DataBean data;
    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * aliPayParaStr : app_id=2016081601758040&biz_content=%7B%22out_trade_no%22%3A%2220161121134749672341479707269859%22%2C%22product_code%22%3A%22QUICK_MSECURITYPAY%22%2C%22subject%22%3A%22%E6%8A%BC%E9%87%91%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%22%2C%22total_amount%22%3A0.01%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F124.93.0.168%3A22059%2Fw%2Fnotifies%2Falipay&sign_type=RSA&timestamp=2016-11-21%2013%3A47%3A49&version=1.0&sign=bYyYFGtFms7HWXAg9fQCj9DtvHBUjeYjIxHoG%2FL89qP7GBXFaSreRXTlbrlnMknBSkjA8ZYUqQZ%2BofPcYnUzLEyWZUFuDl0hJ%2FYkZe%2B4XZSb1HkZ%2F%2FYMMbdbsPNxbvEdL15LYjqmp%2F6riFpKEFq3VLJ7tt%2FvWpgmchzzussKzHk%3D
         * businessId : FOREGIFT_35
         * id : <null>
         */

        private String aliPayParaStr;
        private String businessId;
        private String id;

        public String getAliPayParaStr() {
            return aliPayParaStr;
        }

        public void setAliPayParaStr(String aliPayParaStr) {
            this.aliPayParaStr = aliPayParaStr;
        }

        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
