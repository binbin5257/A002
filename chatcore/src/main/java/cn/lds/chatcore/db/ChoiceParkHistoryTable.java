package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by colin on 15/12/25.
 */

@Table(name = "ChoiceParkHistoryTable")
public class ChoiceParkHistoryTable {
    /**
     * 默认的字段ID
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(column = "longitude")
    private double longitude;
    @Column(column = "latitude")
    private double latitude;
    @Column(column = "name")
    private String name;
    @Column(column = "address")
    private String address;
    @Column(column = "vehicleCount")
    private String vehicleCount;
    @Column(column = "parkingLotCount")
    private String parkingLotCount;
    @Column(column = "chargeSpotsCount")
    private String chargeSpotsCount;
    @Column(column = "distance")
    private double distance;
    @Column(column = "timestamp")
    private long timestamp;

    @Column(column = "parkid")
    private String parkid;

    public String getParkid() {
        return parkid;
    }

    public void setParkid(String parkid) {
        this.parkid = parkid;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
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

    public String getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(String vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public String getParkingLotCount() {
        return parkingLotCount;
    }

    public void setParkingLotCount(String parkingLotCount) {
        this.parkingLotCount = parkingLotCount;
    }

    public String getChargeSpotsCount() {
        return chargeSpotsCount;
    }

    public void setChargeSpotsCount(String chargeSpotsCount) {
        this.chargeSpotsCount = chargeSpotsCount;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }
}
