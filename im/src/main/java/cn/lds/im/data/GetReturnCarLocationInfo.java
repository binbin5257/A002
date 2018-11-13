package cn.lds.im.data;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * 取车还车位置信息
 * Created by sibinbin on 2017/7/21.
 */

public class GetReturnCarLocationInfo implements Serializable{

    private String city;

    /**
     * 具体位置名称
     */
    private String specificLocation;
    /**
     * 行政位置名称
     */
    private String administrativePosition;

    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    /**
     * 距离(米)
     */
    private int distance;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpecificLocation() {
        return specificLocation;
    }

    public void setSpecificLocation(String specificLocation) {
        this.specificLocation = specificLocation;
    }

    public String getAdministrativePosition() {
        return administrativePosition;
    }

    public void setAdministrativePosition(String administrativePosition) {
        this.administrativePosition = administrativePosition;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
