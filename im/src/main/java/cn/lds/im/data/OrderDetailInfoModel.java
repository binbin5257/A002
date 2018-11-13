package cn.lds.im.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by E0608 on 2017/12/28.
 */

public class OrderDetailInfoModel implements Serializable{


    /**
     * status : success
     * data : {"id":3,"no":"DD201712271722170101","reservationTime":1514366537000,"pickedUpTime":1514367435000,"droppedOffTime":1514434001000,"startOdometer":9999,"endOdometer":9999,"startEnergyPercent":91,"endEnergyPercent":48,"status":"DROPPED_OFF","type":"INSTANT","odometer":0,"time":"18小时30分钟","chargingRule":"(白天：15.00元/小时，封顶价：90.00元;夜晚：10.00元/小时，封顶价：60.00元) + 1.00元/公里","sustainedMileage":0,"maxSustainedMileage":200,"scheduledPickedUpTime":null,"scheduledDroppedOffTime":null,"limitTime":null,"scheduledTimeUp":false,"condition":true,"reservationLocationId":1,"reservationLocationName":"大连火车站","reservationLocationAddress":"大连市西岗区大连火车站","reservationLocationLongitude":121.639323,"reservationLocationLatitude":38.928136,"returnLocationId":4,"returnLocationName":"星海广场场站","returnLocationAddress":"大连市沙河口区星海广场","returnLocationLongitude":121.594608,"returnLocationLatitude":38.888172,"enrolleeId":3,"enrolleeName":"司彬彬","enrolleePhone":"18840893841","departmentId":4,"departmentName":"信息部","vehicleBrandName":"长安","vehicleSeriesName":"奔奔","year":2015,"configuration":"2015款 1.4L 手动 舒适型 国V","vehiclePicId":"FL1NQZHJUPSCFK","vehicleId":1,"modelId":1,"plateLicenseNo":"辽B12345","amount":11500,"timeAmount":null,"distanceAmount":null,"chargingRuleType":"WUHU","minPrice":null,"distanceCost":null,"cappedPricePerDay":null,"timeCost":null,"dispatchAmount":500,"deliverAmount":null,"pickupAmount":null,"delivered":false,"deliverPhone":null,"deliverName":null,"deliverAddress":null,"deliverLongitude":null,"deliverLatitude":null,"deliverFfsPhone":null,"deliverCompleted":false,"deliverNoFreeCancel":false,"pickuped":false,"pickupPhone":null,"pickupName":null,"pickupAddress":null,"pickupLongitude":null,"pickupLatitude":null,"pickupFfsPhone":null,"pickupEstimatedTime":null,"pickupFfsReceived":false,"courseList":[{"id":1,"startTime":1514367435000,"endTime":1514368800000,"startOdometer":9999,"endOdometer":9999,"odometer":0,"timeMilli":1365000,"amount":11500,"publicUsage":false,"finished":false},{"id":2,"startTime":1514368800000,"endTime":1514419200000,"startOdometer":9999,"endOdometer":9999,"odometer":0,"timeMilli":50400000,"amount":11500,"publicUsage":false,"finished":false},{"id":3,"startTime":1514419200000,"endTime":1514434001000,"startOdometer":9999,"endOdometer":9999,"odometer":0,"timeMilli":14801000,"amount":11500,"publicUsage":false,"finished":false}]}
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

    public static class DataBean implements Serializable {
        /**
         * id : 3
         * no : DD201712271722170101
         * reservationTime : 1514366537000
         * pickedUpTime : 1514367435000
         * droppedOffTime : 1514434001000
         * startOdometer : 9999
         * endOdometer : 9999
         * startEnergyPercent : 91
         * endEnergyPercent : 48
         * status : DROPPED_OFF
         * type : INSTANT
         * odometer : 0
         * time : 18小时30分钟
         * chargingRule : (白天：15.00元/小时，封顶价：90.00元;夜晚：10.00元/小时，封顶价：60.00元) + 1.00元/公里
         * sustainedMileage : 0
         * maxSustainedMileage : 200
         * scheduledPickedUpTime : null
         * scheduledDroppedOffTime : null
         * limitTime : null
         * scheduledTimeUp : false
         * condition : true
         * reservationLocationId : 1
         * reservationLocationName : 大连火车站
         * reservationLocationAddress : 大连市西岗区大连火车站
         * reservationLocationLongitude : 121.639323
         * reservationLocationLatitude : 38.928136
         * returnLocationId : 4
         * returnLocationName : 星海广场场站
         * returnLocationAddress : 大连市沙河口区星海广场
         * returnLocationLongitude : 121.594608
         * returnLocationLatitude : 38.888172
         * enrolleeId : 3
         * enrolleeName : 司彬彬
         * enrolleePhone : 18840893841
         * departmentId : 4
         * departmentName : 信息部
         * vehicleBrandName : 长安
         * vehicleSeriesName : 奔奔
         * year : 2015
         * configuration : 2015款 1.4L 手动 舒适型 国V
         * vehiclePicId : FL1NQZHJUPSCFK
         * vehicleId : 1
         * modelId : 1
         * plateLicenseNo : 辽B12345
         * amount : 11500
         * timeAmount : null
         * distanceAmount : null
         * chargingRuleType : WUHU
         * minPrice : null
         * distanceCost : null
         * cappedPricePerDay : null
         * timeCost : null
         * dispatchAmount : 500
         * deliverAmount : null
         * pickupAmount : null
         * delivered : false
         * deliverPhone : null
         * deliverName : null
         * deliverAddress : null
         * deliverLongitude : null
         * deliverLatitude : null
         * deliverFfsPhone : null
         * deliverCompleted : false
         * deliverNoFreeCancel : false
         * pickuped : false
         * pickupPhone : null
         * pickupName : null
         * pickupAddress : null
         * pickupLongitude : null
         * pickupLatitude : null
         * pickupFfsPhone : null
         * pickupEstimatedTime : null
         * pickupFfsReceived : false
         * courseList : [{"id":1,"startTime":1514367435000,"endTime":1514368800000,"startOdometer":9999,"endOdometer":9999,"odometer":0,"timeMilli":1365000,"amount":11500,"publicUsage":false,"finished":false},{"id":2,"startTime":1514368800000,"endTime":1514419200000,"startOdometer":9999,"endOdometer":9999,"odometer":0,"timeMilli":50400000,"amount":11500,"publicUsage":false,"finished":false},{"id":3,"startTime":1514419200000,"endTime":1514434001000,"startOdometer":9999,"endOdometer":9999,"odometer":0,"timeMilli":14801000,"amount":11500,"publicUsage":false,"finished":false}]
         */

        private int id;
        private String no;
        private long reservationTime;
        private long pickedUpTime;
        private long droppedOffTime;
        private int startOdometer;
        private int endOdometer;
        private int startEnergyPercent;
        private int endEnergyPercent;
        private String status;
        private String type;
        private int odometer;
        private String time;
        private String chargingRule;
        private int sustainedMileage;
        private int maxSustainedMileage;
        private Object scheduledPickedUpTime;
        private Object scheduledDroppedOffTime;
        private Object limitTime;
        private boolean scheduledTimeUp;
        private boolean condition;
        private int reservationLocationId;
        private String reservationLocationName;
        private String reservationLocationAddress;
        private double reservationLocationLongitude;
        private double reservationLocationLatitude;
        private int returnLocationId;
        private String returnLocationName;
        private String returnLocationAddress;
        private double returnLocationLongitude;
        private double returnLocationLatitude;
        private int enrolleeId;
        private String enrolleeName;
        private String enrolleePhone;
        private int departmentId;
        private String departmentName;
        private String vehicleBrandName;
        private String vehicleSeriesName;
        private int year;
        private String configuration;
        private String vehiclePicId;
        private int vehicleId;
        private int modelId;
        private String plateLicenseNo;
        private int amount;
        private int payableAmount;
        private Object timeAmount;
        private Object distanceAmount;
        private String chargingRuleType;
        private int minPrice;
        private int distanceCost;
        private int cappedPricePerDay;
        private int timeCost;
        private int dispatchAmount;
        private int deliverAmount;
        private int pickupAmount;
        private boolean delivered;
        private Object deliverPhone;
        private String deliverName;
        private String deliverAddress;
        private Object deliverLongitude;
        private Object deliverLatitude;
        private Object deliverFfsPhone;
        private boolean deliverCompleted;
        private boolean deliverNoFreeCancel;
        private boolean pickuped;
        private Object pickupPhone;
        private String pickupName;
        private String pickupAddress;
        private double pickupLongitude;
        private double pickupLatitude;
        private Object pickupFfsPhone;
        private Object pickupEstimatedTime;
        private boolean pickupFfsReceived;
        private List<CourseListBean> courseList;
        private double discount;
        private String orderVehicleType;
        private int monthRemainAmount;
        private int changeRemainAmount;
        private double publicAmount;
        private double personalAmount;
        private long payTime;

        public long getPayTime() {
            return payTime;
        }

        public void setPayTime( long payTime ) {
            this.payTime = payTime;
        }

        public double getPublicAmount() {
            return publicAmount;
        }

        public void setPublicAmount( double publicAmount ) {
            this.publicAmount = publicAmount;
        }

        public double getPersonalAmount() {
            return personalAmount;
        }

        public void setPersonalAmount( double personalAmount ) {
            this.personalAmount = personalAmount;
        }

        public String getOrderVehicleType() {
            return orderVehicleType;
        }

        public void setOrderVehicleType( String orderVehicleType ) {
            this.orderVehicleType = orderVehicleType;
        }

        public int getMonthRemainAmount() {
            return monthRemainAmount;
        }

        public void setMonthRemainAmount( int monthRemainAmount ) {
            this.monthRemainAmount = monthRemainAmount;
        }

        public int getChangeRemainAmount() {
            return changeRemainAmount;
        }

        public void setChangeRemainAmount( int changeRemainAmount ) {
            this.changeRemainAmount = changeRemainAmount;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(int payableAmount) {
            this.payableAmount = payableAmount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public long getReservationTime() {
            return reservationTime;
        }

        public void setReservationTime(long reservationTime) {
            this.reservationTime = reservationTime;
        }

        public long getPickedUpTime() {
            return pickedUpTime;
        }

        public void setPickedUpTime(long pickedUpTime) {
            this.pickedUpTime = pickedUpTime;
        }

        public long getDroppedOffTime() {
            return droppedOffTime;
        }

        public void setDroppedOffTime(long droppedOffTime) {
            this.droppedOffTime = droppedOffTime;
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

        public int getStartEnergyPercent() {
            return startEnergyPercent;
        }

        public void setStartEnergyPercent(int startEnergyPercent) {
            this.startEnergyPercent = startEnergyPercent;
        }

        public int getEndEnergyPercent() {
            return endEnergyPercent;
        }

        public void setEndEnergyPercent(int endEnergyPercent) {
            this.endEnergyPercent = endEnergyPercent;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getOdometer() {
            return odometer;
        }

        public void setOdometer(int odometer) {
            this.odometer = odometer;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getChargingRule() {
            return chargingRule;
        }

        public void setChargingRule(String chargingRule) {
            this.chargingRule = chargingRule;
        }

        public int getSustainedMileage() {
            return sustainedMileage;
        }

        public void setSustainedMileage(int sustainedMileage) {
            this.sustainedMileage = sustainedMileage;
        }

        public int getMaxSustainedMileage() {
            return maxSustainedMileage;
        }

        public void setMaxSustainedMileage(int maxSustainedMileage) {
            this.maxSustainedMileage = maxSustainedMileage;
        }

        public Object getScheduledPickedUpTime() {
            return scheduledPickedUpTime;
        }

        public void setScheduledPickedUpTime(Object scheduledPickedUpTime) {
            this.scheduledPickedUpTime = scheduledPickedUpTime;
        }

        public Object getScheduledDroppedOffTime() {
            return scheduledDroppedOffTime;
        }

        public void setScheduledDroppedOffTime(Object scheduledDroppedOffTime) {
            this.scheduledDroppedOffTime = scheduledDroppedOffTime;
        }

        public Object getLimitTime() {
            return limitTime;
        }

        public void setLimitTime(Object limitTime) {
            this.limitTime = limitTime;
        }

        public boolean isScheduledTimeUp() {
            return scheduledTimeUp;
        }

        public void setScheduledTimeUp(boolean scheduledTimeUp) {
            this.scheduledTimeUp = scheduledTimeUp;
        }

        public boolean isCondition() {
            return condition;
        }

        public void setCondition(boolean condition) {
            this.condition = condition;
        }

        public int getReservationLocationId() {
            return reservationLocationId;
        }

        public void setReservationLocationId(int reservationLocationId) {
            this.reservationLocationId = reservationLocationId;
        }

        public String getReservationLocationName() {
            return reservationLocationName;
        }

        public void setReservationLocationName(String reservationLocationName) {
            this.reservationLocationName = reservationLocationName;
        }

        public String getReservationLocationAddress() {
            return reservationLocationAddress;
        }

        public void setReservationLocationAddress(String reservationLocationAddress) {
            this.reservationLocationAddress = reservationLocationAddress;
        }

        public double getReservationLocationLongitude() {
            return reservationLocationLongitude;
        }

        public void setReservationLocationLongitude(double reservationLocationLongitude) {
            this.reservationLocationLongitude = reservationLocationLongitude;
        }

        public double getReservationLocationLatitude() {
            return reservationLocationLatitude;
        }

        public void setReservationLocationLatitude(double reservationLocationLatitude) {
            this.reservationLocationLatitude = reservationLocationLatitude;
        }

        public int getReturnLocationId() {
            return returnLocationId;
        }

        public void setReturnLocationId(int returnLocationId) {
            this.returnLocationId = returnLocationId;
        }

        public String getReturnLocationName() {
            return returnLocationName;
        }

        public void setReturnLocationName(String returnLocationName) {
            this.returnLocationName = returnLocationName;
        }

        public String getReturnLocationAddress() {
            return returnLocationAddress;
        }

        public void setReturnLocationAddress(String returnLocationAddress) {
            this.returnLocationAddress = returnLocationAddress;
        }

        public double getReturnLocationLongitude() {
            return returnLocationLongitude;
        }

        public void setReturnLocationLongitude(double returnLocationLongitude) {
            this.returnLocationLongitude = returnLocationLongitude;
        }

        public double getReturnLocationLatitude() {
            return returnLocationLatitude;
        }

        public void setReturnLocationLatitude(double returnLocationLatitude) {
            this.returnLocationLatitude = returnLocationLatitude;
        }

        public int getEnrolleeId() {
            return enrolleeId;
        }

        public void setEnrolleeId(int enrolleeId) {
            this.enrolleeId = enrolleeId;
        }

        public String getEnrolleeName() {
            return enrolleeName;
        }

        public void setEnrolleeName(String enrolleeName) {
            this.enrolleeName = enrolleeName;
        }

        public String getEnrolleePhone() {
            return enrolleePhone;
        }

        public void setEnrolleePhone(String enrolleePhone) {
            this.enrolleePhone = enrolleePhone;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getVehicleBrandName() {
            return vehicleBrandName;
        }

        public void setVehicleBrandName(String vehicleBrandName) {
            this.vehicleBrandName = vehicleBrandName;
        }

        public String getVehicleSeriesName() {
            return vehicleSeriesName;
        }

        public void setVehicleSeriesName(String vehicleSeriesName) {
            this.vehicleSeriesName = vehicleSeriesName;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }

        public String getVehiclePicId() {
            return vehiclePicId;
        }

        public void setVehiclePicId(String vehiclePicId) {
            this.vehiclePicId = vehiclePicId;
        }

        public int getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(int vehicleId) {
            this.vehicleId = vehicleId;
        }

        public int getModelId() {
            return modelId;
        }

        public void setModelId(int modelId) {
            this.modelId = modelId;
        }

        public String getPlateLicenseNo() {
            return plateLicenseNo;
        }

        public void setPlateLicenseNo(String plateLicenseNo) {
            this.plateLicenseNo = plateLicenseNo;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public Object getTimeAmount() {
            return timeAmount;
        }

        public void setTimeAmount(Object timeAmount) {
            this.timeAmount = timeAmount;
        }

        public Object getDistanceAmount() {
            return distanceAmount;
        }

        public void setDistanceAmount(Object distanceAmount) {
            this.distanceAmount = distanceAmount;
        }

        public String getChargingRuleType() {
            return chargingRuleType;
        }

        public void setChargingRuleType(String chargingRuleType) {
            this.chargingRuleType = chargingRuleType;
        }

        public int getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(int minPrice) {
            this.minPrice = minPrice;
        }

        public int getDistanceCost() {
            return distanceCost;
        }

        public void setDistanceCost(int distanceCost) {
            this.distanceCost = distanceCost;
        }

        public int getCappedPricePerDay() {
            return cappedPricePerDay;
        }

        public void setCappedPricePerDay(int cappedPricePerDay) {
            this.cappedPricePerDay = cappedPricePerDay;
        }

        public int getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(int timeCost) {
            this.timeCost = timeCost;
        }

        public int getDispatchAmount() {
            return dispatchAmount;
        }

        public void setDispatchAmount(int dispatchAmount) {
            this.dispatchAmount = dispatchAmount;
        }

        public int getDeliverAmount() {
            return deliverAmount;
        }

        public void setDeliverAmount(int deliverAmount) {
            this.deliverAmount = deliverAmount;
        }

        public int getPickupAmount() {
            return pickupAmount;
        }

        public void setPickupAmount(int pickupAmount) {
            this.pickupAmount = pickupAmount;
        }

        public boolean isDelivered() {
            return delivered;
        }

        public void setDelivered(boolean delivered) {
            this.delivered = delivered;
        }

        public Object getDeliverPhone() {
            return deliverPhone;
        }

        public void setDeliverPhone(Object deliverPhone) {
            this.deliverPhone = deliverPhone;
        }

        public String getDeliverName() {
            return deliverName;
        }

        public void setDeliverName(String deliverName) {
            this.deliverName = deliverName;
        }

        public String getDeliverAddress() {
            return deliverAddress;
        }

        public void setDeliverAddress(String deliverAddress) {
            this.deliverAddress = deliverAddress;
        }

        public Object getDeliverLongitude() {
            return deliverLongitude;
        }

        public void setDeliverLongitude(Object deliverLongitude) {
            this.deliverLongitude = deliverLongitude;
        }

        public Object getDeliverLatitude() {
            return deliverLatitude;
        }

        public void setDeliverLatitude(Object deliverLatitude) {
            this.deliverLatitude = deliverLatitude;
        }

        public Object getDeliverFfsPhone() {
            return deliverFfsPhone;
        }

        public void setDeliverFfsPhone(Object deliverFfsPhone) {
            this.deliverFfsPhone = deliverFfsPhone;
        }

        public boolean isDeliverCompleted() {
            return deliverCompleted;
        }

        public void setDeliverCompleted(boolean deliverCompleted) {
            this.deliverCompleted = deliverCompleted;
        }

        public boolean isDeliverNoFreeCancel() {
            return deliverNoFreeCancel;
        }

        public void setDeliverNoFreeCancel(boolean deliverNoFreeCancel) {
            this.deliverNoFreeCancel = deliverNoFreeCancel;
        }

        public boolean isPickuped() {
            return pickuped;
        }

        public void setPickuped(boolean pickuped) {
            this.pickuped = pickuped;
        }

        public Object getPickupPhone() {
            return pickupPhone;
        }

        public void setPickupPhone(Object pickupPhone) {
            this.pickupPhone = pickupPhone;
        }

        public String getPickupName() {
            return pickupName;
        }

        public void setPickupName(String pickupName) {
            this.pickupName = pickupName;
        }

        public String getPickupAddress() {
            return pickupAddress;
        }

        public void setPickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
        }

        public Object getPickupLongitude() {
            return pickupLongitude;
        }

        public void setPickupLongitude(double pickupLongitude) {
            this.pickupLongitude = pickupLongitude;
        }

        public Object getPickupLatitude() {
            return pickupLatitude;
        }

        public void setPickupLatitude(double pickupLatitude) {
            this.pickupLatitude = pickupLatitude;
        }

        public Object getPickupFfsPhone() {
            return pickupFfsPhone;
        }

        public void setPickupFfsPhone(Object pickupFfsPhone) {
            this.pickupFfsPhone = pickupFfsPhone;
        }

        public Object getPickupEstimatedTime() {
            return pickupEstimatedTime;
        }

        public void setPickupEstimatedTime(Object pickupEstimatedTime) {
            this.pickupEstimatedTime = pickupEstimatedTime;
        }

        public boolean isPickupFfsReceived() {
            return pickupFfsReceived;
        }

        public void setPickupFfsReceived(boolean pickupFfsReceived) {
            this.pickupFfsReceived = pickupFfsReceived;
        }

        public List<CourseListBean> getCourseList() {
            return courseList;
        }

        public void setCourseList(List<CourseListBean> courseList) {
            this.courseList = courseList;
        }

        public static class CourseListBean implements Serializable{
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
            private double amount;
            private boolean publicUsage;
            private boolean finished;
            private boolean dayTime;
            private double timeAmount;
            private double distanceAmount;
            private double cappedPrice;

            public boolean isDayTime() {
                return dayTime;
            }

            public void setDayTime(boolean dayTime) {
                this.dayTime = dayTime;
            }

            public double getTimeAmount() {
                return timeAmount;
            }

            public void setTimeAmount(int timeAmount) {
                this.timeAmount = timeAmount;
            }

            public double getDistanceAmount() {
                return distanceAmount;
            }

            public void setDistanceAmount(double distanceAmount) {
                this.distanceAmount = distanceAmount;
            }

            public double getCappedPrice() {
                return cappedPrice;
            }

            public void setCappedPrice(double cappedPrice) {
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

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
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
    }
}
