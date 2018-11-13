package cn.lds.im.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ReservationOrdersLocationsModel {


    /**
     * status : success
     * data : [{"id":"","no":"StackA_Location1N7ZYN3SC14M","longitude":116.331398,"latitude":39.897445,"name":"上地停车场","address":"北京市海淀区上地10街","selected":false}]
     */

    private String status;
    /**
     * id :
     * no : StackA_Location1N7ZYN3SC14M
     * longitude : 116.331398
     * latitude : 39.897445
     * name : 上地停车场
     * address : 北京市海淀区上地10街
     * selected : false
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

    public static class DataBean implements Serializable {
//        private String id;
//        private String no;
//        private double longitude;
//        private double latitude;
//        private String name;
//        private String address;
        private boolean selected;
//        private String instantAvailableVehicle;
//        private String scheduledAvailableVehicle;
//        private String parkingLots;
//        private String chargeSpots;

        private int id;
        private String name;
        private String address;
        private double longitude;
        private double latitude;
        private String instantAvailableVehicle;
        private String scheduledAvailableVehicle;
        private String parkingLots;
        private String chargeSpots;
        private double distance;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getInstantAvailableVehicle() {
            return instantAvailableVehicle;
        }

        public void setInstantAvailableVehicle(String instantAvailableVehicle) {
            this.instantAvailableVehicle = instantAvailableVehicle;
        }

        public String getScheduledAvailableVehicle() {
            return scheduledAvailableVehicle;
        }

        public void setScheduledAvailableVehicle(String scheduledAvailableVehicle) {
            this.scheduledAvailableVehicle = scheduledAvailableVehicle;
        }

        public String getParkingLots() {
            return parkingLots;
        }

        public void setParkingLots(String parkingLots) {
            this.parkingLots = parkingLots;
        }

        public String getChargeSpots() {
            return chargeSpots;
        }

        public void setChargeSpots(String chargeSpots) {
            this.chargeSpots = chargeSpots;
        }
    }
}
