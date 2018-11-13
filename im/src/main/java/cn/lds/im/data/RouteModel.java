package cn.lds.im.data;

import java.util.List;

/**
 * Created by colin on 15/12/25.
 */
public class RouteModel {

    /**
     * status : success
     * data : [{"id":95,"no":"Order1N7JBC7FA7DB","status":"DROPPED_OFF","vehicleResourceId":3,"vehicleModelName":"宝马X1","plateLicenseNo":"京A11111","vehiclePicUrlId":"1L","reservationLocationId":2,"reservationLocationName":"天安门停车场","reservationLocationAddress":"长安街天安门广场1号","reservationLocationLongitude":116.404323,"reservationLocationLatitude":39.912731,"returnLocationId":1,"returnLocationName":"上地停车场","returnLocationAddress":"北京市海淀区上地10街","returnLocationLongitude":116.331398,"returnLocationLatitude":39.897445,"reservationTime":1467180650000,"pickedUpTime":1467180655000,"droppedOffTime":1467181580000,"sustainedMileage":99,"startOdometer":1000,"endOdometer":1000,"startSoc":90,"endSoc":90,"odometer":0,"time":1467181580000,"payment":9.6,"priceDescription":"0.6元/分钟","currentOdometer":0,"enrolleeId":"","comment":false,"score":0,"content":""}]
     */

    private String status;
    /**
     * id : 95
     * no : Order1N7JBC7FA7DB
     * status : DROPPED_OFF
     * vehicleResourceId : 3
     * vehicleModelName : 宝马X1
     * plateLicenseNo : 京A11111
     * vehiclePicUrlId : 1L
     * reservationLocationId : 2
     * reservationLocationName : 天安门停车场
     * reservationLocationAddress : 长安街天安门广场1号
     * reservationLocationLongitude : 116.404323
     * reservationLocationLatitude : 39.912731
     * returnLocationId : 1
     * returnLocationName : 上地停车场
     * returnLocationAddress : 北京市海淀区上地10街
     * returnLocationLongitude : 116.331398
     * returnLocationLatitude : 39.897445
     * reservationTime : 1467180650000
     * pickedUpTime : 1467180655000
     * droppedOffTime : 1467181580000
     * sustainedMileage : 99
     * startOdometer : 1000
     * endOdometer : 1000
     * startSoc : 90
     * endSoc : 90
     * odometer : 0
     * time : 1467181580000
     * payment : 9.6
     * priceDescription : 0.6元/分钟
     * currentOdometer : 0
     * enrolleeId :
     * comment : false
     * score : 0
     * content :
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String no;
        private String status;
        private String vehicleResourceId;
        private String vehicleSeriesName;
        private String vehicleBrandName;
        private String plateLicenseNo;
        private String vehiclePicUrlId;
        private String reservationLocationId;
        private String reservationLocationName;
        private String reservationLocationAddress;
        private double reservationLocationLongitude;
        private double reservationLocationLatitude;
        private String returnLocationId;
        private String returnLocationName;
        private String returnLocationAddress;
        private double returnLocationLongitude;
        private double returnLocationLatitude;
        private long reservationTime;
        private long pickedUpTime;
        private long droppedOffTime;
        private int sustainedMileage;
        private int startOdometer;
        private int endOdometer;
        private int startSoc;
        private int endSoc;
        private int odometer;
        private long time;
        private float payment;
        private String priceDescription;
        private int currentOdometer;
        private String enrolleeId;
        private boolean comment;
        private int score;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVehicleResourceId() {
            return vehicleResourceId;
        }

        public void setVehicleResourceId(String vehicleResourceId) {
            this.vehicleResourceId = vehicleResourceId;
        }

        public String getVehicleModelName() {
            return vehicleSeriesName;
        }

        public void setVehicleModelName(String vehicleModelName) {
            this.vehicleSeriesName = vehicleModelName;
        }

        public String getVehicleBrandName() {
            return vehicleBrandName;
        }

        public void setVehicleBrandName(String vehicleBrandName) {
            this.vehicleBrandName = vehicleBrandName;
        }

        public String getPlateLicenseNo() {
            return plateLicenseNo;
        }

        public void setPlateLicenseNo(String plateLicenseNo) {
            this.plateLicenseNo = plateLicenseNo;
        }

        public String getVehiclePicUrlId() {
            return vehiclePicUrlId;
        }

        public void setVehiclePicUrlId(String vehiclePicUrlId) {
            this.vehiclePicUrlId = vehiclePicUrlId;
        }

        public String getReservationLocationId() {
            return reservationLocationId;
        }

        public void setReservationLocationId(String reservationLocationId) {
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

        public String getReturnLocationId() {
            return returnLocationId;
        }

        public void setReturnLocationId(String returnLocationId) {
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

        public int getSustainedMileage() {
            return sustainedMileage;
        }

        public void setSustainedMileage(int sustainedMileage) {
            this.sustainedMileage = sustainedMileage;
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

        public int getStartSoc() {
            return startSoc;
        }

        public void setStartSoc(int startSoc) {
            this.startSoc = startSoc;
        }

        public int getEndSoc() {
            return endSoc;
        }

        public void setEndSoc(int endSoc) {
            this.endSoc = endSoc;
        }

        public int getOdometer() {
            return odometer;
        }

        public void setOdometer(int odometer) {
            this.odometer = odometer;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public float getPayment() {
            return payment;
        }

        public void setPayment(float payment) {
            this.payment = payment;
        }

        public String getPriceDescription() {
            return priceDescription;
        }

        public void setPriceDescription(String priceDescription) {
            this.priceDescription = priceDescription;
        }

        public int getCurrentOdometer() {
            return currentOdometer;
        }

        public void setCurrentOdometer(int currentOdometer) {
            this.currentOdometer = currentOdometer;
        }

        public String getEnrolleeId() {
            return enrolleeId;
        }

        public void setEnrolleeId(String enrolleeId) {
            this.enrolleeId = enrolleeId;
        }

        public boolean isComment() {
            return comment;
        }

        public void setComment(boolean comment) {
            this.comment = comment;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
