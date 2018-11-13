package cn.lds.chatcore.data;

/**
 * Created by xuqm on 2016/6/23.
 */
public class PayModel {
    /**
     * status : success
     * data : {"id":"","mchId":"1279001001","sign":"3820FEEA47FAA3C520FB67220DCE9E10","nonceStr":"fnwTkz5pKhG8Vix5","prePayId":"wx20161126160123d394f3c7d10497453546","timestamp":"1480147283","businessId":"ORDER_67"}
     */

    private String status;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id :
         * mchId : 1279001001
         * sign : 3820FEEA47FAA3C520FB67220DCE9E10
         * nonceStr : fnwTkz5pKhG8Vix5
         * prePayId : wx20161126160123d394f3c7d10497453546
         * timestamp : 1480147283
         * businessId : ORDER_67
         */

        private String id;
        private String mchId;
        private String sign;
        private String nonceStr;
        private String prePayId;
        private String timestamp;
        private String businessId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMchId() {
            return mchId;
        }

        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getPrePayId() {
            return prePayId;
        }

        public void setPrePayId(String prePayId) {
            this.prePayId = prePayId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }
    }
}
