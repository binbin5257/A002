package cn.lds.im.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by E0608 on 2017/11/7.
 */

public class NearStationModel {

    /**
     * status : success
     * data : [{"id":2,"name":"大连腾飞软件园","address":"大连市甘井子区大连软件园腾飞园区","longitude":121.518817,"latitude":38.851894,"instantAvailableVehicle":1,"scheduledAvailableVehicle":1,"parkingLots":10,"chargeSpots":10,"distance":123772.48403131752},{"id":1,"name":"大连火车站","address":"大连市西岗区大连火车站","longitude":121.639323,"latitude":38.928136,"instantAvailableVehicle":3,"scheduledAvailableVehicle":2,"parkingLots":100,"chargeSpots":100,"distance":132391.47732250477},{"id":5,"name":"北京火车站","address":"北京市东城区北京站","longitude":116.433737,"latitude":39.90978,"instantAvailableVehicle":1,"scheduledAvailableVehicle":0,"parkingLots":20,"chargeSpots":20,"distance":329049.07348776836}]
     * pageable : {"totalElements":3,"numberOfElements":3,"totalPages":1,"number":0,"first":true,"last":true,"size":20,"fromNumber":1,"toNumber":3}
     */

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
         * totalElements : 3
         * numberOfElements : 3
         * totalPages : 1
         * number : 0
         * first : true
         * last : true
         * size : 20
         * fromNumber : 1
         * toNumber : 3
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

    public static class DataBean implements Serializable{
        /**
         * id : 2
         * name : 大连腾飞软件园
         * address : 大连市甘井子区大连软件园腾飞园区
         * longitude : 121.518817
         * latitude : 38.851894
         * instantAvailableVehicle : 1
         * scheduledAvailableVehicle : 1
         * parkingLots : 10
         * chargeSpots : 10
         * distance : 123772.48403131752
         */

        private int id;
        private String name;
        private String address;
        private double longitude;
        private double latitude;
        private int instantAvailableVehicle;
        private int scheduledAvailableVehicle;
        private int parkingLots;
        private int chargeSpots;
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

        public int getInstantAvailableVehicle() {
            return instantAvailableVehicle;
        }

        public void setInstantAvailableVehicle(int instantAvailableVehicle) {
            this.instantAvailableVehicle = instantAvailableVehicle;
        }

        public int getScheduledAvailableVehicle() {
            return scheduledAvailableVehicle;
        }

        public void setScheduledAvailableVehicle(int scheduledAvailableVehicle) {
            this.scheduledAvailableVehicle = scheduledAvailableVehicle;
        }

        public int getParkingLots() {
            return parkingLots;
        }

        public void setParkingLots(int parkingLots) {
            this.parkingLots = parkingLots;
        }

        public int getChargeSpots() {
            return chargeSpots;
        }

        public void setChargeSpots(int chargeSpots) {
            this.chargeSpots = chargeSpots;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}
