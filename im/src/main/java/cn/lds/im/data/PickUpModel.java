package cn.lds.im.data;

/**
 * 山门取车Model
 * Created by si'bin'bin on 2017/7/26.
 */

public class PickUpModel {

    /**
     * 送车地点（地址）
     */
    private String address;
    /**
     * 预计还车时间
     */
    private long estimatedTime;
     /**
     * 主键ID
     */
    private int id ;
    /**
     *  送车地点（纬度）
     */
    private double latitude;
    /**
     * 送车地点（经度）
     */
    private double longitude ;
    /**
     * 送车地点（名称）
     */
    private String name;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 是否需要上门取车
     */
    private boolean pickuped;
    /**
     * 还车场站ID
     */
    private int returnLocationId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPickuped() {
        return pickuped;
    }

    public void setPickuped(boolean pickuped) {
        this.pickuped = pickuped;
    }

    public int getReturnLocationId() {
        return returnLocationId;
    }

    public void setReturnLocationId(int returnLocationId) {
        this.returnLocationId = returnLocationId;
    }

}
