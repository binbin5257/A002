package cn.lds.chatcore.data.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 位置消息格式化存储
 *
 * @author user
 */
public class LocationMessageModel {

    public static final String STR_URI = "uri";
    public static final String STR_LATITUDE = "latitude";
    public static final String STR_LONGITUDE = "longitude";
    public static final String STR_ADDRESS = "address";

    /**
     * 构造函数
     *
     * @param uri
     * @param latitude
     * @param longitude
     * @param address
     * @param isSlected
     */
    @Deprecated
    public LocationMessageModel(String uri, double latitude, double longitude, String address, boolean isSlected) {
        super();
        this.uri = uri;
        this.isSlected = isSlected;
        this.latitude = Double.isNaN(latitude) ? 0 : latitude;
        this.longitude = Double.isNaN(longitude) ? 0 : longitude;
        this.address = address;
    }

    /**
     * 构造函数
     *
     * @param uri
     * @param latitude
     * @param longitude
     * @param address
     */
    public LocationMessageModel(String uri, double latitude, double longitude, String address) {
        super();
        this.uri = uri;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    /**
     * 发送消息：本地路径；接收消息：网络路径
     */
    private String uri;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 地址
     */
    private String address;

    private boolean isSlected;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSlected() {
        return isSlected;
    }

    public void setSlected(boolean isSlected) {
        this.isSlected = isSlected;
    }

    /**
     * 构造JSON串
     *
     * @param uri
     * @param latitude
     * @param longitude
     * @param address
     * @return
     */
    public static String createContentJsonStr(String uri, double latitude, double longitude, String address) {
        JSONObject json = new JSONObject();
        try {
            json.put(STR_URI, uri);
            json.put(STR_LATITUDE, latitude);
            json.put(STR_LONGITUDE, longitude);
            json.put(STR_ADDRESS, address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 解析JSON串
     *
     * @param content
     * @return
     */
    public static LocationMessageModel parse(String content) {
        LocationMessageModel locationItemModel = null;
        try {
            JSONObject json = new JSONObject(content);
            locationItemModel = new LocationMessageModel(json.optString(STR_URI), json.optDouble(STR_LATITUDE),
                    json.optDouble(STR_LONGITUDE), json.optString(STR_ADDRESS));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return locationItemModel;
    }
}
