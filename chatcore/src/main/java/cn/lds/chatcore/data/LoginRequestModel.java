package cn.lds.chatcore.data;

import cn.lds.chatcore.common.DeviceHelper;
import cn.lds.chatcore.enums.LoginType;
import cn.lds.im.sdk.enums.OsType;

/**
 * Created by quwei on 2015/12/3.
 */
public class LoginRequestModel {

    /**
     * 登录类型
     */
    private LoginType loginType;
    /**
     * 登录名
     */
    private String loginId;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 密码
     */
    private String captcha;
    /**
     * 短信验证码
     */
    private String password;
    /**
     * 一次性认证token
     */
    private String nonceToken;
    /**
     * 软件类型
     */
    private String softwareType;
    /**
     * 软件版本
     */
    private String softwareVersion;
    /**
     * 微信登录号
     */
    private String authorizationCode;
    private String username;
    private String deviceId;
    private String deviceType;
    private String osType;
    private String osVersion;


    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
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


    public String getOsVersion() {
        return DeviceHelper.getOsVer();
    }

    public String getSoftwareType() {
        return softwareType;
    }

    public void setSoftwareType(String softwareType) {
        this.softwareType = softwareType;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getNonceToken() {
        return nonceToken;
    }

    public void setNonceToken(String nonceToken) {
        this.nonceToken = nonceToken;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsType() {
        return OsType.ANDROID.name();
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
