package cn.lds.im.data;

/**
 * Created by E0608 on 2017/10/25.
 */

public class ChargingDetailModel {

    /**
     * status : success
     * data : {"id":18658,"name":"大连混合场站","address":"大连市甘井子区大连软件园腾飞园区","longitude":121.518817,"latitude":38.851894,"status":"FREE","parkFree":true,"fullTimeOpening":true,"availableDc":4,"dcNumCount":4,"availableAc":4,"acNumCount":4,"electricityFee":100,"serviceFee":100,"paymentType":"ONLINE","stationTel":"1234567890","operator":"XING_XING_CHONG_DIAN","serviceTel":"1234567890","distance":0}
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
         * id : 18658
         * name : 大连混合场站
         * address : 大连市甘井子区大连软件园腾飞园区
         * longitude : 121.518817
         * latitude : 38.851894
         * status : FREE
         * parkFree : true
         * fullTimeOpening : true
         * availableDc : 4
         * dcNumCount : 4
         * availableAc : 4
         * acNumCount : 4
         * electricityFee : 100
         * serviceFee : 100
         * paymentType : ONLINE
         * stationTel : 1234567890
         * operator : XING_XING_CHONG_DIAN
         * serviceTel : 1234567890
         * distance : 0.0
         */

        private int id;
        private String name;
        private String address;
        private double longitude;
        private double latitude;
        private String status;
        private boolean parkFree;
        private boolean fullTimeOpening;
        private int availableDc;
        private int dcNumCount;
        private int availableAc;
        private int acNumCount;
        private double electricityFee;
        private double serviceFee;
        private String paymentType;
        private String stationTel;
        private String operator;
        private String serviceTel;
        private double distance;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isParkFree() {
            return parkFree;
        }

        public void setParkFree(boolean parkFree) {
            this.parkFree = parkFree;
        }

        public boolean isFullTimeOpening() {
            return fullTimeOpening;
        }

        public void setFullTimeOpening(boolean fullTimeOpening) {
            this.fullTimeOpening = fullTimeOpening;
        }

        public int getAvailableDc() {
            return availableDc;
        }

        public void setAvailableDc(int availableDc) {
            this.availableDc = availableDc;
        }

        public int getDcNumCount() {
            return dcNumCount;
        }

        public void setDcNumCount(int dcNumCount) {
            this.dcNumCount = dcNumCount;
        }

        public int getAvailableAc() {
            return availableAc;
        }

        public void setAvailableAc(int availableAc) {
            this.availableAc = availableAc;
        }

        public int getAcNumCount() {
            return acNumCount;
        }

        public void setAcNumCount(int acNumCount) {
            this.acNumCount = acNumCount;
        }

        public double getElectricityFee() {
            return electricityFee;
        }

        public void setElectricityFee(double electricityFee) {
            this.electricityFee = electricityFee;
        }

        public double getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(double serviceFee) {
            this.serviceFee = serviceFee;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getStationTel() {
            return stationTel;
        }

        public void setStationTel(String stationTel) {
            this.stationTel = stationTel;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getServiceTel() {
            return serviceTel;
        }

        public void setServiceTel(String serviceTel) {
            this.serviceTel = serviceTel;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}
