package cn.lds.im.data;

/**
 * Created by Administrator on 2016/6/21.
 */
public class RouteDetailModel {


    /**
     * status : success
     * data : {"id":16,"no":"Order1N814G7M5Y54","status":"PAID","vehicleNo":"VC1N812MFLKFM3","vehicleSeriesName":"帕萨特","plateLicenseNo":"辽BT1234","vehiclePicUrlId":"","reservationLocationNo":"StackA_Location1N7ZYN3SC14M","reservationLocationName":"上地停车场","reservationLocationAddress":"北京市海淀区上地10街","reservationLocationLongitude":116.331398,"reservationLocationLatitude":39.897445,"returnLocationNo":"StackA_Location1N7ZYN3SC14M","returnLocationName":"上地停车场","returnLocationAddress":"北京市海淀区上地10街","returnLocationLongitude":116.331398,"returnLocationLatitude":39.897445,"reservationTime":1475120524000,"pickedUpTime":1475120530000,"droppedOffTime":1475120864000,"startOdometer":61409,"endOdometer":61411,"startEnergyPercent":36,"endEnergyPercent":36,"odometer":2,"time":6,"payment":1000,"timeCost":20,"distanceCost":40,"minPrice":1000,"enrolleeId":"","comment":false,"score":0,"content":"","phone":"13581676589","scheduledPickedUpTime":1475120524000,"scheduledDroppedOffTime":1475120524000,"type":"INSTANT"}
     */

    private String status;
    /**
     * id : 16
     * no : Order1N814G7M5Y54
     * status : PAID
     * vehicleNo : VC1N812MFLKFM3
     * vehicleSeriesName : 帕萨特
     * plateLicenseNo : 辽BT1234
     * vehiclePicUrlId :
     * reservationLocationNo : StackA_Location1N7ZYN3SC14M
     * reservationLocationName : 上地停车场
     * reservationLocationAddress : 北京市海淀区上地10街
     * reservationLocationLongitude : 116.331398
     * reservationLocationLatitude : 39.897445
     * returnLocationNo : StackA_Location1N7ZYN3SC14M
     * returnLocationName : 上地停车场
     * returnLocationAddress : 北京市海淀区上地10街
     * returnLocationLongitude : 116.331398
     * returnLocationLatitude : 39.897445
     * reservationTime : 1475120524000
     * pickedUpTime : 1475120530000
     * droppedOffTime : 1475120864000
     * startOdometer : 61409
     * endOdometer : 61411
     * startEnergyPercent : 36
     * endEnergyPercent : 36
     * odometer : 2
     * time : 6
     * payment : 1000
     * timeCost : 20
     * distanceCost : 40
     * minPrice : 1000
     * enrolleeId :
     * comment : false
     * score : 0
     * content :
     * phone : 13581676589
     * scheduledPickedUpTime : 1475120524000
     * scheduledDroppedOffTime : 1475120524000
     * type : INSTANT
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
        private String id;
        private String no;
        private String status;
        private String vehicleNo;
        private String vehicleSeriesName;
        private String vehicleBrandName;
        private String plateLicenseNo;
        private String vehiclePicUrlId;
        private String reservationLocationNo;
        private String reservationLocationName;
        private String reservationLocationAddress;
        private double reservationLocationLongitude;
        private double reservationLocationLatitude;
        private String returnLocationNo;
        private String returnLocationName;
        private String returnLocationAddress;
        private double returnLocationLongitude;
        private double returnLocationLatitude;
        private long reservationTime;
        private long pickedUpTime;
        private long droppedOffTime;
        private int startOdometer;
        private int endOdometer;
        private int startEnergyPercent;
        private int endEnergyPercent;
        private double odometer;
        private int time;
        private double payment;
        private double timeCost;
        private double distanceCost;
        private double minPrice;
        private String enrolleeId;
        private boolean comment;
        private int score;
        private String content;
        private String phone;
        private long scheduledPickedUpTime;
        private long scheduledDroppedOffTime;
        private String type;

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

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getVehicleSeriesName() {
            return vehicleSeriesName;
        }

        public void setVehicleSeriesName(String vehicleSeriesName) {
            this.vehicleSeriesName = vehicleSeriesName;
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

        public String getReservationLocationNo() {
            return reservationLocationNo;
        }

        public void setReservationLocationNo(String reservationLocationNo) {
            this.reservationLocationNo = reservationLocationNo;
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

        public String getReturnLocationNo() {
            return returnLocationNo;
        }

        public void setReturnLocationNo(String returnLocationNo) {
            this.returnLocationNo = returnLocationNo;
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

        public double getOdometer() {
            return odometer;
        }

        public void setOdometer(double odometer) {
            this.odometer = odometer;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public long getScheduledPickedUpTime() {
            return scheduledPickedUpTime;
        }

        public void setScheduledPickedUpTime(long scheduledPickedUpTime) {
            this.scheduledPickedUpTime = scheduledPickedUpTime;
        }

        public long getScheduledDroppedOffTime() {
            return scheduledDroppedOffTime;
        }

        public void setScheduledDroppedOffTime(long scheduledDroppedOffTime) {
            this.scheduledDroppedOffTime = scheduledDroppedOffTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
