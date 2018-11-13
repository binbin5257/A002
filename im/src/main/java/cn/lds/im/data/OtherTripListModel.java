package cn.lds.im.data;

import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class OtherTripListModel {



    private String status;
    private PageableBean pageable;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PageableBean {
        /**
         * totalElements : 20
         * numberOfElements : 20
         * totalPages : 1
         * number : 0
         * first : true
         * last : true
         * size : 20
         * fromNumber : 1
         * toNumber : 20
         */

        private int totalElements;
        private int numberOfElements;
        private int totalPages;
        private int number;
        private boolean first;
        private boolean last;
        private int size;
        private int fromNumber;
        private int toNumber;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getFromNumber() {
            return fromNumber;
        }

        public void setFromNumber(int fromNumber) {
            this.fromNumber = fromNumber;
        }

        public int getToNumber() {
            return toNumber;
        }

        public void setToNumber(int toNumber) {
            this.toNumber = toNumber;
        }
    }

    public static class DataBean {
        /**
         * id : 2115
         * no : DD201807051449280101
         * reservationTime : 1530773368000
         * pickedUpTime : 1530773494000
         * droppedOffTime : 1530779302000
         * startOdometer : 5348
         * endOdometer : 5386
         * startEnergyPercent : 50
         * endEnergyPercent : 37
         * status : PAID
         * type : null
         * odometer : 0
         * time : null
         * chargingRule : null
         * sustainedMileage : 0
         * maxSustainedMileage : 351
         * longitude : null
         * latitude : null
         * scheduledPickedUpTime : null
         * scheduledDroppedOffTime : null
         * limitTime : null
         * scheduledTimeUp : false
         * condition : false
         * reservationLocationId : 1
         * reservationLocationName : 联通芜湖市分公司
         * reservationLocationAddress : 芜湖市鸠江区中国联通芜湖分公司
         * reservationLocationLongitude : 118.441604
         * reservationLocationLatitude : 31.351643
         * returnLocationId : 1
         * returnLocationName : 联通芜湖市分公司
         * returnLocationAddress : 芜湖市鸠江区中国联通芜湖分公司
         * returnLocationLongitude : 118.441604
         * returnLocationLatitude : 31.351643
         * enrolleeId : 41
         * enrolleeName : 李铮
         * enrolleePhone : 18655309575
         * orderVehicleType : PUBLIC
         * departmentId : 1
         * departmentName : 智网办事处
         * monthRemainAmount : 0
         * changeRemainAmount : 10
         * vehicleBrandName : 奇瑞
         * vehicleSeriesName : 艾瑞泽5e
         * year : 2017
         * configuration : 2017款 智享版
         * vehiclePicId : FL1P5M3QH3R6DX
         * vehicleId : 14
         * modelId : 1
         * plateLicenseNo : 皖BD07770
         * publicAmount : 4495
         * personalAmount : 0
         * amount : 0
         * payableAmount : 4495
         * timeAmount : null
         * distanceAmount : null
         * chargingRuleType : null
         * minPrice : null
         * distanceCost : null
         * cappedPricePerDay : null
         * timeCost : null
         * dispatchAmount : null
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
         * courseList : []
         * discount : 0.5
         * blueToothMDTO : null
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
        private Object type;
        private int odometer;
        private Object time;
        private Object chargingRule;
        private int sustainedMileage;
        private int maxSustainedMileage;
        private Object longitude;
        private Object latitude;
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
        private String orderVehicleType;
        private int departmentId;
        private String departmentName;
        private int monthRemainAmount;
        private int changeRemainAmount;
        private String vehicleBrandName;
        private String vehicleSeriesName;
        private int year;
        private String configuration;
        private String vehiclePicId;
        private int vehicleId;
        private int modelId;
        private String plateLicenseNo;
        private int publicAmount;
        private int personalAmount;
        private int amount;
        private int payableAmount;
        private Object timeAmount;
        private Object distanceAmount;
        private Object chargingRuleType;
        private Object minPrice;
        private Object distanceCost;
        private Object cappedPricePerDay;
        private Object timeCost;
        private Object dispatchAmount;
        private Object deliverAmount;
        private Object pickupAmount;
        private boolean delivered;
        private Object deliverPhone;
        private Object deliverName;
        private Object deliverAddress;
        private Object deliverLongitude;
        private Object deliverLatitude;
        private Object deliverFfsPhone;
        private boolean deliverCompleted;
        private boolean deliverNoFreeCancel;
        private boolean pickuped;
        private Object pickupPhone;
        private Object pickupName;
        private Object pickupAddress;
        private Object pickupLongitude;
        private Object pickupLatitude;
        private Object pickupFfsPhone;
        private Object pickupEstimatedTime;
        private boolean pickupFfsReceived;
        private double discount;
        private Object blueToothMDTO;
        private List<?> courseList;

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

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public int getOdometer() {
            return odometer;
        }

        public void setOdometer(int odometer) {
            this.odometer = odometer;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }

        public Object getChargingRule() {
            return chargingRule;
        }

        public void setChargingRule(Object chargingRule) {
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

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
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

        public String getOrderVehicleType() {
            return orderVehicleType;
        }

        public void setOrderVehicleType(String orderVehicleType) {
            this.orderVehicleType = orderVehicleType;
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

        public int getMonthRemainAmount() {
            return monthRemainAmount;
        }

        public void setMonthRemainAmount(int monthRemainAmount) {
            this.monthRemainAmount = monthRemainAmount;
        }

        public int getChangeRemainAmount() {
            return changeRemainAmount;
        }

        public void setChangeRemainAmount(int changeRemainAmount) {
            this.changeRemainAmount = changeRemainAmount;
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

        public int getPublicAmount() {
            return publicAmount;
        }

        public void setPublicAmount(int publicAmount) {
            this.publicAmount = publicAmount;
        }

        public int getPersonalAmount() {
            return personalAmount;
        }

        public void setPersonalAmount(int personalAmount) {
            this.personalAmount = personalAmount;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(int payableAmount) {
            this.payableAmount = payableAmount;
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

        public Object getChargingRuleType() {
            return chargingRuleType;
        }

        public void setChargingRuleType(Object chargingRuleType) {
            this.chargingRuleType = chargingRuleType;
        }

        public Object getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(Object minPrice) {
            this.minPrice = minPrice;
        }

        public Object getDistanceCost() {
            return distanceCost;
        }

        public void setDistanceCost(Object distanceCost) {
            this.distanceCost = distanceCost;
        }

        public Object getCappedPricePerDay() {
            return cappedPricePerDay;
        }

        public void setCappedPricePerDay(Object cappedPricePerDay) {
            this.cappedPricePerDay = cappedPricePerDay;
        }

        public Object getTimeCost() {
            return timeCost;
        }

        public void setTimeCost(Object timeCost) {
            this.timeCost = timeCost;
        }

        public Object getDispatchAmount() {
            return dispatchAmount;
        }

        public void setDispatchAmount(Object dispatchAmount) {
            this.dispatchAmount = dispatchAmount;
        }

        public Object getDeliverAmount() {
            return deliverAmount;
        }

        public void setDeliverAmount(Object deliverAmount) {
            this.deliverAmount = deliverAmount;
        }

        public Object getPickupAmount() {
            return pickupAmount;
        }

        public void setPickupAmount(Object pickupAmount) {
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

        public Object getDeliverName() {
            return deliverName;
        }

        public void setDeliverName(Object deliverName) {
            this.deliverName = deliverName;
        }

        public Object getDeliverAddress() {
            return deliverAddress;
        }

        public void setDeliverAddress(Object deliverAddress) {
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

        public Object getPickupName() {
            return pickupName;
        }

        public void setPickupName(Object pickupName) {
            this.pickupName = pickupName;
        }

        public Object getPickupAddress() {
            return pickupAddress;
        }

        public void setPickupAddress(Object pickupAddress) {
            this.pickupAddress = pickupAddress;
        }

        public Object getPickupLongitude() {
            return pickupLongitude;
        }

        public void setPickupLongitude(Object pickupLongitude) {
            this.pickupLongitude = pickupLongitude;
        }

        public Object getPickupLatitude() {
            return pickupLatitude;
        }

        public void setPickupLatitude(Object pickupLatitude) {
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

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public Object getBlueToothMDTO() {
            return blueToothMDTO;
        }

        public void setBlueToothMDTO(Object blueToothMDTO) {
            this.blueToothMDTO = blueToothMDTO;
        }

        public List<?> getCourseList() {
            return courseList;
        }

        public void setCourseList(List<?> courseList) {
            this.courseList = courseList;
        }
    }
}