package cn.lds.im.data;

/**
 * Created by xuqm on 2016/6/25.
 */
public class PayInfoModel {

    /**
     * status : success
     * data : {"id":116,"amountPayable":600,"amount":600,"odometer":0,"time":1,"timeCost":400,"distanceCost":400,"minPrice":600,"accountAmount":3593}
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
         * id : 116
         * amountPayable : 600.0
         * amount : 600.0
         * odometer : 0.0
         * time : 1.0
         * timeCost : 400.0
         * distanceCost : 400.0
         * minPrice : 600.0
         * accountAmount : 3593.0
         */

        private int id;
        private double amountPayable;
        private double amount;
        private double odometer;
        private double time;
        private double timeCost;
        private double distanceCost;
        private double minPrice;
        private double accountAmount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getAmountPayable() {
            return amountPayable;
        }

        public void setAmountPayable(double amountPayable) {
            this.amountPayable = amountPayable;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getOdometer() {
            return odometer;
        }

        public void setOdometer(double odometer) {
            this.odometer = odometer;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public double getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(double timeCost) {
            this.timeCost = timeCost;
        }

        public double getDistanceCost() {
            return distanceCost;
        }

        public void setDistanceCost(double distanceCost) {
            this.distanceCost = distanceCost;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public double getAccountAmount() {
            return accountAmount;
        }

        public void setAccountAmount(double accountAmount) {
            this.accountAmount = accountAmount;
        }
    }
}