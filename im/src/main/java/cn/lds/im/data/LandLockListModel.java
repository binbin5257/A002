package cn.lds.im.data;

import java.util.List;

/**
 * Created by sibinbin on 18-4-20.
 */

public class LandLockListModel {


    /**
     * status : success
     * data : [{"id":2,"parkingNum":"1","bluetoothAddress":"abc123","deviceId":"1001"},{"id":4,"parkingNum":"2","bluetoothAddress":"ble2","deviceId":"1002"}]
     */

    private String status;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData( List<DataBean> data ) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * parkingNum : 1
         * bluetoothAddress : abc123
         * deviceId : 1001
         */

        private int id;
        private String parkingNum;
        private String bluetoothAddress;
        private String deviceId;
        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken( String accessToken ) {
            this.accessToken = accessToken;
        }

        public int getId() {
            return id;
        }

        public void setId( int id ) {
            this.id = id;
        }

        public String getParkingNum() {
            return parkingNum;
        }

        public void setParkingNum( String parkingNum ) {
            this.parkingNum = parkingNum;
        }

        public String getBluetoothAddress() {
            return bluetoothAddress;
        }

        public void setBluetoothAddress( String bluetoothAddress ) {
            this.bluetoothAddress = bluetoothAddress;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId( String deviceId ) {
            this.deviceId = deviceId;
        }
    }
}
