package cn.lds.im.data;

import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class TripInfoModel {


    /**
     * status : success
     * data : {"amount":0,"chargingRule":"string","configuration":"string","deliverAmount":0,"departmentId":0,"departmentName":"string","dispatchAmount":0,"distanceAmount":0,"droppedOffTime":"2017-07-24T02:15:40.896Z","endEnergyPercent":0,"endOdometer":0,"enrolleeId":0,"enrolleeName":"string","enrolleePhone":"string","id":0,"limitTime":"2017-07-24T02:15:40.896Z","maxSustainedMileage":0,"no":"string","odometer":0,"pickedUpTime":"2017-07-24T02:15:40.896Z","pickupAmount":0,"plateLicenseNo":"string","reservationLocationAddress":"string","reservationLocationId":0,"reservationLocationLatitude":0,"reservationLocationLongitude":0,"reservationLocationName":"string","reservationTime":"2017-07-24T02:15:40.896Z","returnLocationAddress":"string","returnLocationId":0,"returnLocationLatitude":0,"returnLocationLongitude":0,"returnLocationName":"string","scheduledDroppedOffTime":"2017-07-24T02:15:40.896Z","scheduledPickedUpTime":"2017-07-24T02:15:40.896Z","startEnergyPercent":0,"startOdometer":0,"status":"OPEN","sustainedMileage":0,"time":0,"timeAmount":0,"type":"INSTANT","vehicleBrandName":"string","vehicleId":0,"vehiclePicId":"string","vehicleSeriesName":"string","year":0}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-24T02:15:40.896Z
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
         * amount : 0
         * chargingRule : string
         * configuration : string
         * deliverAmount : 0
         * departmentId : 0
         * departmentName : string
         * dispatchAmount : 0
         * distanceAmount : 0
         * droppedOffTime : 2017-07-24T02:15:40.896Z
         * endEnergyPercent : 0
         * endOdometer : 0
         * enrolleeId : 0
         * enrolleeName : string
         * enrolleePhone : string
         * id : 0
         * limitTime : 2017-07-24T02:15:40.896Z
         * maxSustainedMileage : 0
         * no : string
         * odometer : 0
         * pickedUpTime : 2017-07-24T02:15:40.896Z
         * pickupAmount : 0
         * plateLicenseNo : string
         * reservationLocationAddress : string
         * reservationLocationId : 0
         * reservationLocationLatitude : 0
         * reservationLocationLongitude : 0
         * reservationLocationName : string
         * reservationTime : 2017-07-24T02:15:40.896Z
         * returnLocationAddress : string
         * returnLocationId : 0
         * returnLocationLatitude : 0
         * returnLocationLongitude : 0
         * returnLocationName : string
         * scheduledDroppedOffTime : 2017-07-24T02:15:40.896Z
         * scheduledPickedUpTime : 2017-07-24T02:15:40.896Z
         * startEnergyPercent : 0
         * startOdometer : 0
         * status : OPEN
         * sustainedMileage : 0
         * time : 0
         * timeAmount : 0
         * type : INSTANT
         * vehicleBrandName : string
         * vehicleId : 0
         * vehiclePicId : string
         * vehicleSeriesName : string
         * year : 0
         */

        private String amount;
        private String chargingRule;
        private String configuration;
        private String deliverAmount;
        private String departmentId;
        private String departmentName;
        private String dispatchAmount;
        private String distanceAmount;
        private String droppedOffTime;
        private String endEnergyPercent;
        private String endOdometer;
        private String enrolleeId;
        private String enrolleeName;
        private String enrolleePhone;
        private int id;
        private String limitTime;
        private String maxSustainedMileage;
        private String no;
        private String odometer;
        private String pickedUpTime;
        private String pickupAmount;
        private String plateLicenseNo;
        private String reservationLocationAddress;
        private String reservationLocationId;
        private String reservationLocationLatitude;
        private String reservationLocationLongitude;
        private String reservationLocationName;
        private String reservationTime;
        private String returnLocationAddress;
        private String returnLocationId;
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
        private String vehicleId;
        private String vehiclePicId;
        private String vehicleSeriesName;
        private String year;
        private boolean deliverCompleted;
        private boolean pickuped;
        private boolean deliverNoFreeCancel ;
        private boolean delivered;
        private String deliverAddress;//送车上门地点（地址）
        private String pickupAddress;
        private String modelId;

        public String getModelId() {
            return modelId;
        }

        public void setModelId(String modelId) {
            this.modelId = modelId;
        }

        public String getDeliverAddress() {
            return deliverAddress;
        }

        public void setDeliverAddress(String deliverAddress) {
            this.deliverAddress = deliverAddress;
        }

        public String getPickupAddress() {
            return pickupAddress;
        }

        public void setPickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
        }

        public boolean isDelivered() {
            return delivered;
        }

        public void setDelivered(boolean delivered) {
            this.delivered = delivered;
        }

        public boolean isDeliverNoFreeCancel() {
            return deliverNoFreeCancel;
        }

        public void setDeliverNoFreeCancel(boolean deliverNoFreeCancel) {
            this.deliverNoFreeCancel = deliverNoFreeCancel;
        }

        public boolean isDeliverCompleted() {
            return deliverCompleted;
        }

        public void setDeliverCompleted(boolean deliverCompleted) {
            this.deliverCompleted = deliverCompleted;
        }

        public boolean isPickuped() {
            return pickuped;
        }

        public void setPickuped(boolean pickuped) {
            this.pickuped = pickuped;
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

        public String getDeliverAmount() {
            return deliverAmount;
        }

        public void setDeliverAmount(String deliverAmount) {
            this.deliverAmount = deliverAmount;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
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

        public String getEnrolleeId() {
            return enrolleeId;
        }

        public void setEnrolleeId(String enrolleeId) {
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

        public String getPickupAmount() {
            return pickupAmount;
        }

        public void setPickupAmount(String pickupAmount) {
            this.pickupAmount = pickupAmount;
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

        public String getReservationLocationId() {
            return reservationLocationId;
        }

        public void setReservationLocationId(String reservationLocationId) {
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

        public String getReturnLocationId() {
            return returnLocationId;
        }

        public void setReturnLocationId(String returnLocationId) {
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

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
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

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
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