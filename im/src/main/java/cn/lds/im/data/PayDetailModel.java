package cn.lds.im.data;

import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class PayDetailModel {


    /**
     * status : success
     * data : {"accountAmount":0,"amount":0,"cappedPricePerDay":0,"chargingRuleType":"TIME_ONLY","deliverAmount":0,"dispatchAmount":0,"distanceAmount":0,"distanceCost":0,"id":0,"minPrice":0,"odometer":0,"payableAmount":0,"pickupAmount":0,"ticketDiscountAmount":0,"time":0,"timeAmount":0,"timeCost":0,"useTicket":false}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-08-14T04:27:24.617Z
     */

    private String status;
    private DataBean data;
    private String timestamp;
    private List<ErrorsBean> errors;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class DataBean {
        /**
         * accountAmount : 0
         * amount : 0
         * cappedPricePerDay : 0
         * chargingRuleType : TIME_ONLY
         * deliverAmount : 0
         * dispatchAmount : 0
         * distanceAmount : 0
         * distanceCost : 0
         * id : 0
         * minPrice : 0
         * odometer : 0
         * payableAmount : 0
         * pickupAmount : 0
         * ticketDiscountAmount : 0
         * time : 0
         * timeAmount : 0
         * timeCost : 0
         * useTicket : false
         */

        private String accountAmount;
        private String amount;
        private double cappedPricePerDay;
        private String chargingRuleType;
        private String deliverAmount;
        private String dispatchAmount;
        private String distanceAmount;
        private double distanceCost;
        private int id;
        private double minPrice;
        private String odometer;
        private String payableAmount;
        private String pickupAmount;
        private String ticketDiscountAmount;
        private String time;
        private String timeAmount;
        private double timeCost;
        private boolean useTicket;
        private List<CourseListBean> courseList;
        private long payTime;

        public long getPayTime() {
            return payTime;
        }

        public void setPayTime( long payTime ) {
            this.payTime = payTime;
        }

        public List<CourseListBean> getCourseList() {
            return courseList;
        }

        public void setCourseList(List<CourseListBean> courseList) {
            this.courseList = courseList;
        }

        public double getCappedPricePerDay() {
            return cappedPricePerDay;
        }

        public void setCappedPricePerDay(double cappedPricePerDay) {
            this.cappedPricePerDay = cappedPricePerDay;
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

        public double getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(double timeCost) {
            this.timeCost = timeCost;
        }

        public String getAccountAmount() {
            return accountAmount;
        }

        public void setAccountAmount(String accountAmount) {
            this.accountAmount = accountAmount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }


        public String getChargingRuleType() {
            return chargingRuleType;
        }

        public void setChargingRuleType(String chargingRuleType) {
            this.chargingRuleType = chargingRuleType;
        }

        public String getDeliverAmount() {
            return deliverAmount;
        }

        public void setDeliverAmount(String deliverAmount) {
            this.deliverAmount = deliverAmount;
        }

        public String getDispatchAmount() {
            return dispatchAmount;
        }

        public void setDispatchAmount(String dispatchAmount) {
            this.dispatchAmount = dispatchAmount;
        }

        public String getDistanceAmount() {
            return distanceAmount;
        }

        public void setDistanceAmount(String distanceAmount) {
            this.distanceAmount = distanceAmount;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getOdometer() {
            return odometer;
        }

        public void setOdometer(String odometer) {
            this.odometer = odometer;
        }

        public String getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(String payableAmount) {
            this.payableAmount = payableAmount;
        }

        public String getPickupAmount() {
            return pickupAmount;
        }

        public void setPickupAmount(String pickupAmount) {
            this.pickupAmount = pickupAmount;
        }

        public String getTicketDiscountAmount() {
            return ticketDiscountAmount;
        }

        public void setTicketDiscountAmount(String ticketDiscountAmount) {
            this.ticketDiscountAmount = ticketDiscountAmount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimeAmount() {
            return timeAmount;
        }

        public void setTimeAmount(String timeAmount) {
            this.timeAmount = timeAmount;
        }


        public boolean isUseTicket() {
            return useTicket;
        }

        public void setUseTicket(boolean useTicket) {
            this.useTicket = useTicket;
        }
    }
    public static class CourseListBean {
        /**
         * id : 1
         * startTime : 1514367435000
         * endTime : 1514368800000
         * startOdometer : 9999
         * endOdometer : 9999
         * odometer : 0
         * timeMilli : 1365000
         * amount : 11500
         * publicUsage : false
         * finished : false
         * "id":1,
         "startTime":1514367435000,
         "endTime":1514368800000,
         "startOdometer":9999,
         "endOdometer":9999,
         "odometer":0,
         "timeMilli":1365000,
         "amount":1500,
         "publicUsage":false,
         "finished":false,
         "dayTime":false,
         "timeAmount":0,
         "distanceAmount":0,
         "cappedPrice":6000
         */

        private int id;
        private long startTime;
        private long endTime;
        private int startOdometer;
        private int endOdometer;
        private int odometer;
        private int timeMilli;
        private int amount;
        private boolean publicUsage;
        private boolean finished;
        private boolean dayTime;
        private int timeAmount;
        private int distanceAmount;
        private int cappedPrice;

        public boolean isDayTime() {
            return dayTime;
        }

        public void setDayTime(boolean dayTime) {
            this.dayTime = dayTime;
        }

        public int getTimeAmount() {
            return timeAmount;
        }

        public void setTimeAmount(int timeAmount) {
            this.timeAmount = timeAmount;
        }

        public int getDistanceAmount() {
            return distanceAmount;
        }

        public void setDistanceAmount(int distanceAmount) {
            this.distanceAmount = distanceAmount;
        }

        public int getCappedPrice() {
            return cappedPrice;
        }

        public void setCappedPrice(int cappedPrice) {
            this.cappedPrice = cappedPrice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getStartOdometer() {
            return startOdometer;
        }

        public void setStartOdometer(int startOdometer) {
            this.startOdometer = startOdometer;
        }

        public int getEndOdometer() {
            return endOdometer;
        }

        public void setEndOdometer(int endOdometer) {
            this.endOdometer = endOdometer;
        }

        public int getOdometer() {
            return odometer;
        }

        public void setOdometer(int odometer) {
            this.odometer = odometer;
        }

        public int getTimeMilli() {
            return timeMilli;
        }

        public void setTimeMilli(int timeMilli) {
            this.timeMilli = timeMilli;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public boolean isPublicUsage() {
            return publicUsage;
        }

        public void setPublicUsage(boolean publicUsage) {
            this.publicUsage = publicUsage;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }
    }

    public static class ErrorsBean {
        /**
         * field : string
         * errmsg : string
         * errcode : string
         */

        private String field;
        private String errmsg;
        private String errcode;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }
    }
}