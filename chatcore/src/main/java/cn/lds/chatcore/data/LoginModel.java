package cn.lds.chatcore.data;

import cn.lds.chatcore.common.DeviceHelper;
import cn.lds.im.sdk.enums.DeviceType;
import cn.lds.im.sdk.enums.OsType;

/**
 * Created by quwei on 2015/12/3.
 */
public class LoginModel {


    /**
     * username : 13912345678
     * password : 123456
     * deviceId : d12345
     * deviceType : mobile
     * osType : android
     * osVersion : 4.4
     */

    private String username;
    private String password;
    private String deviceId;
    private String deviceType;
    private String osType;
    private String osVersion;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return DeviceHelper.getDeviceId();
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return DeviceType.PHONE;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public OsType getOsType() {
        return OsType.ANDROID;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return DeviceHelper.getOsVer();
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
