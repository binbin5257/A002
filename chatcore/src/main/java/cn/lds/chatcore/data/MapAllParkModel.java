package cn.lds.chatcore.data;

import java.util.List;

/**
 * Created by colin on 15/12/25.
 */
public class MapAllParkModel {

    /**
     * status : success
     * data : [{"address":"string","availableVehicle":0,"chargeSpots":0,"distance":0,"id":0,"latitude":0,"longitude":0,"name":"string","parkingLots":0}]
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-13T07:57:55.522Z
     */

    private String status;
    private String timestamp;
    private List<DataBean> data;
    private List<ErrorsBean> errors;

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

    public static class DataBean {
        /**
         * address : string
         * availableVehicle : 0
         * chargeSpots : 0
         * distance : 0
         * id : 0
         * latitude : 0
         * longitude : 0
         * name : string
         * parkingLots : 0
         */

        private String address;
        private String availableVehicle;
        private String chargeSpots;
        private Double distance;
        private String id;
        private Double latitude;
        private Double longitude;
        private String name;
        private String parkingLots;
        private int resId;

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAvailableVehicle() {
            return availableVehicle;
        }

        public void setAvailableVehicle(String availableVehicle) {
            this.availableVehicle = availableVehicle;
        }

        public String getChargeSpots() {
            return chargeSpots;
        }

        public void setChargeSpots(String chargeSpots) {
            this.chargeSpots = chargeSpots;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParkingLots() {
            return parkingLots;
        }

        public void setParkingLots(String parkingLots) {
            this.parkingLots = parkingLots;
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
