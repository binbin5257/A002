package cn.lds.im.data;

/**
 * Created by Administrator on 2016/6/21.
 */
public class PaymentsDetailModel {


    /**
     * status : success
     * data : {"id":16,"amountPayable":1000,"amount":1000,"odometer":2,"time":6,"timeCost":20,"distanceCost":40,"minPrice":1000}
     */

    private String status;
    /**
     * id : 16
     * amountPayable : 1000
     * amount : 1000
     * odometer : 2
     * time : 6
     * timeCost : 20
     * distanceCost : 40
     * minPrice : 1000
     */

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
        private int id;
        private double amountPayable;
        private double amount;
        private double odometer;
        private double time;
        private double timeCost;
        private double distanceCost;
        private double minPrice;

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
    }
}
