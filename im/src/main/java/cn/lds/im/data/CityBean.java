package cn.lds.im.data;

/**
 * Created by E0608 on 2017/9/25.
 */

public class CityBean {

    private String name;

    private String code;

    private double lat;

    private double log;

    private String cityAddress;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityAddress() {

        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    private boolean isSelectCity;

    public boolean isSelectCity() {
        return isSelectCity;
    }

    public void setSelectCity(boolean selectCity) {
        isSelectCity = selectCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }
}
