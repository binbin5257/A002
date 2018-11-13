package cn.lds.chatcore.common;

import android.text.TextUtils;

import org.json.JSONArray;

import cn.lds.chatcore.MyApplication;

/**
 * 缓存内容统一管理类
 *
 * @author user
 */
public class CacheHelper {

    public static final String LOGINID = "loginid";
    public static final String ACCOUNT = "account";
    public static final String isFirstOpen = "isFirstOpen";
    public static final String lastOpenAppDay = "lastOpenAppDay";
    public static final String isNewUser = "isNewUser";
    public static final String NO = "no";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ORGANIZATION_ID = "organizationId";
    public static final String ORGANIZATION_LIST = "organization_list";
    public static final String SERVER_URI = "server_uri";

    public static final String IM_HOST = "im_host";
    public static final String IM_PORT = "im_port";
    public static final String IM_TOKEN = "im_token";
    public static final String IM_KICKED_TIME = "im_kicked_time";    // IM 连接被T了
    public static final String IM_KICKED_OS_TYPE = "im_kicked_os_type";    // IM 连接被T了的设备

    public static final String USER_CHANGE_PASS_BY_MANAGER = "user_change_pass_by_manager"; // 后台修改了用户密码
    public static final String USER_FORBIDDEN_BY_MANAGER = "user_forbidden_by_manager";        // 后台禁用了用户
    public static final String USER_CHANGE_PASS_BY_SELF = "user_change_pass_by_self";        // 用户自己修改了密码

    // 重新登录头像缓存
    public static final String RELOGIN_AVATAR = "relogin_avatar";
    public static final String RELOGIN_NICK_NAME = "relogin_nick_name";
    public static final String APPROVER_NAME = "APPROVER_NAME";
    public static final String APPROVER_PHONE = "APPROVER_PHONE";
    public static final String APPROVER_ID = "APPROVER_ID";
    public static final String PICK_UP_TIME = "PICK_UP_TIME";
    public static final String SERVICE_TEL = "SERVICE_TEL";



    /**
     * 请空缓存
     */
    public static void clear() {
        MyApplication.getInstance().getCache().clear();
    }

    public static void logout() {
        ACache cache = MyApplication.getInstance().getCache();
        // cache.remove(ACCOUNT);
        cache.remove(ACCESS_TOKEN);
        cache.remove(ORGANIZATION_ID);
        cache.remove(ORGANIZATION_LIST);
        cache.remove(SERVER_URI);
        cache.remove(ACCOUNT);
        cache.remove(NO);

        cache.remove(IM_HOST);
        cache.remove(IM_PORT);
        cache.remove(IM_TOKEN);
        cache.remove(SERVER_URI);
    }

    /**
     * 设置缓存帐户
     *
     * @return
     */
    public static void setApproverPhone(String phone) {
        setString(APPROVER_PHONE, phone);
    }
    /**
     * 设置缓存审批人id
     *
     * @return
     */
    public static void setApproverId(String id) {
        setString(APPROVER_ID, id);
    }
    /**
     * 缓存审批人姓名
     *
     * @return
     */
    public static void setApproverName(String name) {
        setString(APPROVER_NAME, name);
    }
    /**
     * 缓存最大取车时间
     *
     * @return
     */
    public static void setPickUpTime(int min) {
        if(min != 0){
            long maxMills = min * 60 * 1000;
            setString(PICK_UP_TIME, String.valueOf(maxMills));
        }


    }
    /**
     * 缓存最大取车时间
     *
     * @return
     */
    public static String getPickUpTime() {

       return getString(PICK_UP_TIME);

    }
    /**
     * 缓存后台服务电话
     *
     * @return
     */
    public static void setServiceTel(String tel) {
        if(!TextUtils.isEmpty(tel)){
            setString(SERVICE_TEL, tel);
        }


    }
    /**
     * 获取服务电话
     *
     * @return
     */
    public static String getServiceTel() {

        return getString(SERVICE_TEL);

    }
    /**
     * 缓存审批人电话
     *
     * @return
     */
    public static void setAccount(String account) {
        System.out.println("--->> set account=" + account);
        setString(ACCOUNT, account);
    }



    /**
     * 获取缓存的帐户
     *
     * @return
     */
    public static String getAccount() {
        return getString(ACCOUNT);
    }
    /**
     * 获取审批人姓名
     *
     * @return
     */
    public static String getApproverName() {
        return getString(APPROVER_NAME);
    }
    /**
     * 获取审批人电话
     *
     * @return
     */
    public static String getApproverPhone() {
        return getString(APPROVER_PHONE);
    }
    /**
     * 获取审批人ID
     *
     * @return
     */
    public static String getApproverId() {
        return getString(APPROVER_ID);
    }

    /**
     * 设置token
     *
     * @return
     */
    public static void setAccessToken(String access_token) {
        setString(ACCESS_TOKEN, access_token);
    }

    /**
     * 获取缓存的token
     *
     * @return
     */
    public static String getAccessToken() {
        return getString(ACCESS_TOKEN);
    }

    /**
     * 设置组织id
     *
     * @return
     */
    public static void setOrganizationId(String organization_id) {
        setString(ORGANIZATION_ID, organization_id);
    }

    /**
     * 获取缓存的组织id
     *
     * @return
     */
    public static String getOrganizationId() {
        return ToolsHelper.isNullString(getString(ORGANIZATION_ID));
    }

    /**
     * 设置组织列表
     *
     * @return
     */
    public static void setOrganizationList(JSONArray organization_list) {
        setJSONArray(ORGANIZATION_LIST, organization_list);
    }

    /**
     * 获取缓存的组织列表
     *
     * @return
     */
    public static JSONArray getOrganizationList() {
        return getJSONArray(ORGANIZATION_LIST);
    }

    /**
     * 设置缓存IM服务器地址
     *
     * @return
     */
    public static void setServerURI(String serverURI) {
        setString(SERVER_URI, serverURI);
    }

    /**
     * 获取缓存的IM服务器地址
     *
     * @return
     */
    public static String getServerURI() {
        return getString(SERVER_URI);
    }

    public static void setImHost(String host) {
        setString(IM_HOST, host);
    }

    public static String getImHost() {
        return getString(IM_HOST);
    }

    public static void setImPort(int port) {
        setString(IM_PORT, String.valueOf(port));
    }

    public static int getImPort() {
        String port = getString(IM_PORT);
        if (null != port && !"".equals(port)) {
            return Integer.valueOf(port);
        }
        return 0;
    }

    private static void setString(String key, String value) {
        if (null == value)
            return;
        MyApplication.getInstance().getCache().put(key, value);
    }

    private static String getString(String key) {

        String result = MyApplication.getInstance().getCache().getAsString(key);
        System.out.println("--->> CacheHelper get " + key + "=" + result);
        return result;
    }

    private static void setJSONArray(String key, JSONArray value) {
        if (null == value)
            return;
        MyApplication.getInstance().getCache().put(key, value);
    }

    private static JSONArray getJSONArray(String key) {

        JSONArray result = MyApplication.getInstance().getCache().getAsJSONArray(key);
        return result;
    }


    /**
     * 设置缓存No(聊天用）
     *
     * @return
     */
    public static void setNo(String no) {
        setString(NO, no);
    }

    /**
     * 获取缓存No（聊天用）
     *
     * @return
     */
    public static String getNo() {
        return getString(NO);
    }

    /**
     * 设置登录账号
     *
     * @return
     */
    public static void setLoginid(String loginid) {
        setString(LOGINID, loginid);
    }

    /**
     * 获取设置登录账号
     *
     * @return
     */
    public static String getLoginid() {
        return getString(LOGINID);
    }

    /**
     * 设置 IM TOKEN
     *
     * @return
     */
    public static void setImToken(String imToken) {
        setString(IM_TOKEN, imToken);
    }

    /**
     * 获取 IM TOKEN
     *
     * @return
     */
    public static String getImToken() {
        return getString(IM_TOKEN);
    }

    /**
     * 设置 重登录头像
     *
     * @return
     */
    public static void setReloginAvatar(String relogin_avatar) {
        setString(RELOGIN_AVATAR, relogin_avatar);
    }

    /**
     * 获取 重登录头像
     *
     * @return
     */
    public static String getReloginAvatar() {
        return getString(RELOGIN_AVATAR);
    }

    /**
     * 设置 重登录昵称
     *
     * @return
     */
    public static void setReloginNickName(String relogin_nick_name) {
        setString(RELOGIN_NICK_NAME, relogin_nick_name);
    }

    /**
     * 获取 重登录昵称
     *
     * @return
     */
    public static String getReloginNickName() {
        return getString(RELOGIN_NICK_NAME);
    }


    /**
     * 设置 IM_KICKED_TIME
     *
     * @return
     */
    public static void setImKickedTime(String imKickedTime) {
        setString(IM_KICKED_TIME, imKickedTime);
    }

    /**
     * 获取 IIM_KICKED_TIME
     *
     * @return
     */
    public static String getImKickedTime() {
        return getString(IM_KICKED_TIME);
    }

    /**
     * 设置 IM_KICKED_TIME
     *
     * @return
     */
    public static void setImKickedOsType(String imKickedType) {
        setString(IM_KICKED_OS_TYPE, imKickedType);
    }

    /**
     * 获取 IIM_KICKED_TIME
     *
     * @return
     */
    public static String getImKickedOsType() {
        return getString(IM_KICKED_OS_TYPE);
    }


    /**
     * 设置 USER_CHANGE_PASS_BY_MANAGER
     *
     * @return
     */
    public static void setUserChangePassByManager(String user_change_pass_by_manager) {
        setString(USER_CHANGE_PASS_BY_MANAGER, user_change_pass_by_manager);
    }

    /**
     * 获取 USER_CHANGE_PASS_BY_MANAGER
     *
     * @return
     */
    public static String getUserChangePassByManager() {
        return getString(USER_CHANGE_PASS_BY_MANAGER);
    }

    /**
     * 设置 USER_FORBIDDEN_BY_MANAGER
     *
     * @return
     */
    public static void setUserForbiddenByManager(String user_forbidden_by_manager) {
        setString(USER_FORBIDDEN_BY_MANAGER, user_forbidden_by_manager);
    }

    /**
     * 获取 USER_FORBIDDEN_BY_MANAGER
     *
     * @return
     */
    public static String getUserForbiddenByManager() {
        return getString(USER_FORBIDDEN_BY_MANAGER);
    }

    /**
     * 设置 USER_CHANGE_PASS_BY_SELF
     *
     * @return
     */
    public static void setUserChangePassBySelf(String user_change_pass_by_self) {
        setString(USER_CHANGE_PASS_BY_SELF, user_change_pass_by_self);
    }

    /**
     * 获取 USER_FORBIDDEN_BY_MANAGER
     *
     * @return
     */
    public static String getUserChangePassBySelf() {
        return getString(USER_CHANGE_PASS_BY_SELF);
    }

    /**
     * 获取 是否是首次打开程序
     *
     * @return
     */
    public static boolean getIsFirstOpen() {
        boolean open = true;
        try {
            open = (boolean) MyApplication.getInstance().getCache().getAsObject(isFirstOpen);
        } catch (NullPointerException e) {
        }
        return open;
    }
    /**
     * 获取 是否是第一次打开登录
     *
     * @return
     */
    public static boolean getIsNewUser() {
        boolean newUser = true;
        try {
            newUser = (boolean) MyApplication.getInstance().getCache().getAsObject(isNewUser);
        } catch (NullPointerException e) {
        }
        return newUser;
    }
    /**
     * @return
     */
    public static void setIsNewUser(boolean newUser) {
        MyApplication.getInstance().getCache().put(isNewUser, newUser);
    }
    /**
     * @return
     */
    public static void setIsFirstOpen(boolean open) {
        MyApplication.getInstance().getCache().put(isFirstOpen, open);
    }
   public static String getLastOpenAppDay(){
        return getString(lastOpenAppDay);
   }

    public static void setLastOpenAppDay(long day){
        MyApplication.getInstance().getCache().put(lastOpenAppDay, String.valueOf(day));
    }

}
