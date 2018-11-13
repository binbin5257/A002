package cn.lds.im.data;

import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class TripListModel {

    /**
     * status : success
     * data : [{"amount":0,"chargingRule":"string","configuration":"string","deliverAddress":"string","deliverAmount":0,"deliverCompleted":false,"deliverFfsPhone":"string","deliverLatitude":0,"deliverLongitude":0,"deliverName":"string","deliverPhone":"string","delivered":false,"departmentId":0,"departmentName":"string","dispatchAmount":0,"distanceAmount":0,"droppedOffTime":"2017-07-31T01:24:26.739Z","endEnergyPercent":0,"endOdometer":0,"enrolleeId":0,"enrolleeName":"string","enrolleePhone":"string","id":0,"limitTime":"2017-07-31T01:24:26.739Z","maxSustainedMileage":0,"no":"string","odometer":0,"pickedUpTime":"2017-07-31T01:24:26.739Z","pickupAddress":"string","pickupAmount":0,"pickupEstimatedTime":"2017-07-31T01:24:26.739Z","pickupFfsPhone":"string","pickupFfsReceived":false,"pickupLatitude":0,"pickupLongitude":0,"pickupName":"string","pickupPhone":"string","pickuped":false,"plateLicenseNo":"string","reservationLocationAddress":"string","reservationLocationId":0,"reservationLocationLatitude":0,"reservationLocationLongitude":0,"reservationLocationName":"string","reservationTime":"2017-07-31T01:24:26.739Z","returnLocationAddress":"string","returnLocationId":0,"returnLocationLatitude":0,"returnLocationLongitude":0,"returnLocationName":"string","scheduledDroppedOffTime":"2017-07-31T01:24:26.739Z","scheduledPickedUpTime":"2017-07-31T01:24:26.739Z","startEnergyPercent":0,"startOdometer":0,"status":"OPEN","sustainedMileage":0,"time":0,"timeAmount":0,"type":"INSTANT","vehicleBrandName":"string","vehicleId":0,"vehiclePicId":"string","vehicleSeriesName":"string","year":0}]
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-31T01:24:26.740Z
     */

    private String status;
    private String timestamp;
    private List<DataBean> data;
    private List<ErrorsBean> errors;
    private PageableBean pageable;
    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
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

        private String amount;
        private String chargingRule;
        private String configuration;
        private String deliverAddress;
        private String deliverAmount;
        private boolean deliverCompleted;
        private boolean scheduledTimeUp;
        private String deliverFfsPhone;
        private String deliverLatitude;
        private String deliverLongitude;
        private String deliverName;
        private String deliverPhone;
        private boolean delivered;
        private int departmentId;
        private String departmentName;
        private String dispatchAmount;
        private String distanceAmount;
        private String droppedOffTime;
        private String endEnergyPercent;
        private String endOdometer;
        private int enrolleeId;
        private String enrolleeName;
        private String enrolleePhone;
        private int id;
        private String limitTime;
        private String maxSustainedMileage;
        private String no;
        private String odometer;
        private String pickedUpTime;
        private String pickupAddress;
        private String pickupAmount;
        private String pickupEstimatedTime;
        private String pickupFfsPhone;
        private boolean pickupFfsReceived;
        private String pickupLatitude;
        private String pickupLongitude;
        private String pickupName;
        private String pickupPhone;
        private boolean pickuped;
        private String plateLicenseNo;
        private String reservationLocationAddress;
        private int reservationLocationId;
        private String reservationLocationLatitude;
        private String reservationLocationLongitude;
        private String reservationLocationName;
        private String reservationTime;
        private String returnLocationAddress;
        private int returnLocationId;
        private String returnLocationLatitude;
        private String returnLocationLongitude;
        private String returnLocationName;
        private String scheduledDroppedOffTime;
        private String scheduledPickedUpTime;
        private String startEnergyPercent;
        private String startOdometer;
        private String status;
        private String sustainedMileage;
        private String time;
        private String timeAmount;
        private String type;
        private String vehicleBrandName;
        private int vehicleId;
        private String vehiclePicId;
        private String vehicleSeriesName;
        private int year;
        private String orderVehicleType;
        private double publicAmount;
        private double personalAmount;

        public String getOrderVehicleType() {
            return orderVehicleType;
        }

        public void setOrderVehicleType( String orderVehicleType ) {
            this.orderVehicleType = orderVehicleType;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getChargingRule() {
            return chargingRule;
        }

        public void setChargingRule(String chargingRule) {
            this.chargingRule = chargingRule;
        }

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }

        public String getDeliverAddress() {
            return deliverAddress;
        }

        public void setDeliverAddress(String deliverAddress) {
            this.deliverAddress = deliverAddress;
        }

        public String getDeliverAmount() {
            return deliverAmount;
        }

        public void setDeliverAmount(String deliverAmount) {
            this.deliverAmount = deliverAmount;
        }

        public boolean isDeliverCompleted() {
            return deliverCompleted;
        }

        public void setDeliverCompleted(boolean deliverCompleted) {
            this.deliverCompleted = deliverCompleted;
        }

        public boolean isScheduledTimeUp() {
            return scheduledTimeUp;
        }

        public void setScheduledTimeUp(boolean scheduledTimeUp) {
            this.scheduledTimeUp = scheduledTimeUp;
        }

        public String getDeliverFfsPhone() {
            return deliverFfsPhone;
        }

        public void setDeliverFfsPhone(String deliverFfsPhone) {
            this.deliverFfsPhone = deliverFfsPhone;
        }

        public String getDeliverLatitude() {
            return deliverLatitude;
        }

        public void setDeliverLatitude(String deliverLatitude) {
            this.deliverLatitude = deliverLatitude;
        }

        public String getDeliverLongitude() {
            return deliverLongitude;
        }

        public void setDeliverLongitude(String deliverLongitude) {
            this.deliverLongitude = deliverLongitude;
        }

        public String getDeliverName() {
            return deliverName;
        }

        public void setDeliverName(String deliverName) {
            this.deliverName = deliverName;
        }

        public String getDeliverPhone() {
            return deliverPhone;
        }

        public void setDeliverPhone(String deliverPhone) {
            this.deliverPhone = deliverPhone;
        }

        public boolean isDelivered() {
            return delivered;
        }

        public void setDelivered(boolean delivered) {
            this.delivered = delivered;
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

        public String getDroppedOffTime() {
            return droppedOffTime;
        }

        public void setDroppedOffTime(String droppedOffTime) {
            this.droppedOffTime = droppedOffTime;
        }

        public String getEndEnergyPercent() {
            return endEnergyPercent;
        }

        public void setEndEnergyPercent(String endEnergyPercent) {
            this.endEnergyPercent = endEnergyPercent;
        }

        public String getEndOdometer() {
            return endOdometer;
        }

        public void setEndOdometer(String endOdometer) {
            this.endOdometer = endOdometer;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLimitTime() {
            return limitTime;
        }

        public void setLimitTime(String limitTime) {
            this.limitTime = limitTime;
        }

        public String getMaxSustainedMileage() {
            return maxSustainedMileage;
        }

        public void setMaxSustainedMileage(String maxSustainedMileage) {
            this.maxSustainedMileage = maxSustainedMileage;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getOdometer() {
            return odometer;
        }

        public void setOdometer(String odometer) {
            this.odometer = odometer;
        }

        public String getPickedUpTime() {
            return pickedUpTime;
        }

        public void setPickedUpTime(String pickedUpTime) {
            this.pickedUpTime = pickedUpTime;
        }

        public String getPickupAddress() {
            return pickupAddress;
        }

        public void setPickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
        }

        public String getPickupAmount() {
            return pickupAmount;
        }

        public void setPickupAmount(String pickupAmount) {
            this.pickupAmount = pickupAmount;
        }

        public String getPickupEstimatedTime() {
            return pickupEstimatedTime;
        }

        public void setPickupEstimatedTime(String pickupEstimatedTime) {
            this.pickupEstimatedTime = pickupEstimatedTime;
        }

        public String getPickupFfsPhone() {
            return pickupFfsPhone;
        }

        public void setPickupFfsPhone(String pickupFfsPhone) {
            this.pickupFfsPhone = pickupFfsPhone;
        }

        public boolean isPickupFfsReceived() {
            return pickupFfsReceived;
        }

        public void setPickupFfsReceived(boolean pickupFfsReceived) {
            this.pickupFfsReceived = pickupFfsReceived;
        }

        public String getPickupLatitude() {
            return pickupLatitude;
        }

        public void setPickupLatitude(String pickupLatitude) {
            this.pickupLatitude = pickupLatitude;
        }

        public String getPickupLongitude() {
            return pickupLongitude;
        }

        public void setPickupLongitude(String pickupLongitude) {
            this.pickupLongitude = pickupLongitude;
        }

        public String getPickupName() {
            return pickupName;
        }

        public void setPickupName(String pickupName) {
            this.pickupName = pickupName;
        }

        public String getPickupPhone() {
            return pickupPhone;
        }

        public void setPickupPhone(String pickupPhone) {
            this.pickupPhone = pickupPhone;
        }

        public boolean isPickuped() {
            return pickuped;
        }

        public void setPickuped(boolean pickuped) {
            this.pickuped = pickuped;
        }

        public String getPlateLicenseNo() {
            return plateLicenseNo;
        }

        public void setPlateLicenseNo(String plateLicenseNo) {
            this.plateLicenseNo = plateLicenseNo;
        }

        public String getReservationLocationAddress() {
            return reservationLocationAddress;
        }

        public void setReservationLocationAddress(String reservationLocationAddress) {
            this.reservationLocationAddress = reservationLocationAddress;
        }

        public int getReservationLocationId() {
            return reservationLocationId;
        }

        public void setReservationLocationId(int reservationLocationId) {
            this.reservationLocationId = reservationLocationId;
        }

        public String getReservationLocationLatitude() {
            return reservationLocationLatitude;
        }

        public void setReservationLocationLatitude(String reservationLocationLatitude) {
            this.reservationLocationLatitude = reservationLocationLatitude;
        }

        public String getReservationLocationLongitude() {
            return reservationLocationLongitude;
        }

        public void setReservationLocationLongitude(String reservationLocationLongitude) {
            this.reservationLocationLongitude = reservationLocationLongitude;
        }

        public String getReservationLocationName() {
            return reservationLocationName;
        }

        public void setReservationLocationName(String reservationLocationName) {
            this.reservationLocationName = reservationLocationName;
        }

        public String getReservationTime() {
            return reservationTime;
        }

        public void setReservationTime(String reservationTime) {
            this.reservationTime = reservationTime;
        }

        public String getReturnLocationAddress() {
            return returnLocationAddress;
        }

        public void setReturnLocationAddress(String returnLocationAddress) {
            this.returnLocationAddress = returnLocationAddress;
        }

        public int getReturnLocationId() {
            return returnLocationId;
        }

        public void setReturnLocationId(int returnLocationId) {
            this.returnLocationId = returnLocationId;
        }

        public String getReturnLocationLatitude() {
            return returnLocationLatitude;
        }

        public void setReturnLocationLatitude(String returnLocationLatitude) {
            this.returnLocationLatitude = returnLocationLatitude;
        }

        public String getReturnLocationLongitude() {
            return returnLocationLongitude;
        }

        public void setReturnLocationLongitude(String returnLocationLongitude) {
            this.returnLocationLongitude = returnLocationLongitude;
        }

        public String getReturnLocationName() {
            return returnLocationName;
        }

        public void setReturnLocationName(String returnLocationName) {
            this.returnLocationName = returnLocationName;
        }

        public String getScheduledDroppedOffTime() {
            return scheduledDroppedOffTime;
        }

        public void setScheduledDroppedOffTime(String scheduledDroppedOffTime) {
            this.scheduledDroppedOffTime = scheduledDroppedOffTime;
        }

        public String getScheduledPickedUpTime() {
            return scheduledPickedUpTime;
        }

        public void setScheduledPickedUpTime(String scheduledPickedUpTime) {
            this.scheduledPickedUpTime = scheduledPickedUpTime;
        }

        public String getStartEnergyPercent() {
            return startEnergyPercent;
        }

        public void setStartEnergyPercent(String startEnergyPercent) {
            this.startEnergyPercent = startEnergyPercent;
        }

        public String getStartOdometer() {
            return startOdometer;
        }

        public void setStartOdometer(String startOdometer) {
            this.startOdometer = startOdometer;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSustainedMileage() {
            return sustainedMileage;
        }

        public void setSustainedMileage(String sustainedMileage) {
            this.sustainedMileage = sustainedMileage;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVehicleBrandName() {
            return vehicleBrandName;
        }

        public void setVehicleBrandName(String vehicleBrandName) {
            this.vehicleBrandName = vehicleBrandName;
        }

        public int getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(int vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getVehiclePicId() {
            return vehiclePicId;
        }

        public void setVehiclePicId(String vehiclePicId) {
            this.vehiclePicId = vehiclePicId;
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