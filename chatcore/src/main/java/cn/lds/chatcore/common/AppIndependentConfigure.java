package cn.lds.chatcore.common;

/**
 * Created by quwei on 2016/12/9.
 */
public class AppIndependentConfigure {
    // 系统包名
    public final static String SYS_CONFIG_APP_PACKAGE = "com.leadingsoft.TCR";

    public static String getLogoutFilter = "tcloud.mainactivity";
    public static String getKickedFilter = "tcloud.loginactivity";
    /**
     * 微信支付id
     */
    public static final String appId = "wx48c0c4b80f4a69d4";

    /**
     * SERVER HOST 分时租赁--内网
     */
    public static String SERVER_IP_CLOUD_DLNW_SHARE= "172.17.20.20";
    public static String SERVER_HOST_CLOUD_DLNW_SHARE = "http://" + SERVER_IP_CLOUD_DLNW_SHARE + ":99";
    /**
     * SERVER HOST 分时租赁--芜湖内网
     */
    public static String SERVER_IP_CLOUD_DLNW_SHARE_WU= "172.17.20.20";
    public static String SERVER_HOST_CLOUD_DLNW_SHARE_WU = "http://" + SERVER_IP_CLOUD_DLNW_SHARE_WU + ":99";
    /**
     * SERVER HOST 分时租赁--芜湖阿里云
     */
    public static String SERVER_IP_CLOUD_ALI_SHARE_WU= "47.97.124.248";
    public static String SERVER_HOST_CLOUD_ALI_SHARE_WU = "http://" + SERVER_IP_CLOUD_ALI_SHARE_WU;
    /**
     * SERVER HOST 分时租赁--芜湖阿联通服务器
     */
    public static String SERVER_IP_CUSC_SHARE= "116.62.228.177";
    public static String SERVER_HOST_CUSC_SHARE = "http://" + SERVER_IP_CUSC_SHARE;
//    /**
//     * SERVER HOST 分时租赁--芜湖阿联通服务器
//     */
//    public static String SERVER_IP_CUSC_SHARE= "192.168.1.107";
//    public static String SERVER_HOST_CUSC_SHARE = "http://" + SERVER_IP_CUSC_SHARE + ":9660";
    /**
     * SERVER HOST 分时租赁--芜湖阿里云
     */
    public static String SERVER_IP_CLOUD_ALI_SHARE_WU_NEW= "47.95.230.159";
    public static String SERVER_HOST_CLOUD_ALI_SHARE_WU_NEW = "http://" + SERVER_IP_CLOUD_ALI_SHARE_WU_NEW+ ":9091";
    /**
     * SERVER HOST 分时租赁--外网
     */
    public static String SERVER_IP_CLOUD_DLWW_SHARE= "124.93.0.168";
    public static String SERVER_HOST_CLOUD_DLWW_SHARE = "http://" + SERVER_IP_CLOUD_DLWW_SHARE + ":22112";
    /**
     * SERVER HOST 阿里云
     */
    public static String SERVER_IP_CLOUD_ALI_SHARE= "47.95.230.159";
    public static String SERVER_HOST_CLOUD_ALI_SHARE = "http://" + SERVER_IP_CLOUD_ALI_SHARE;
    /**
     * SERVER HOST 阿里云
     */
    public static String SERVER_IP_CLOUD_ALI_SHARE_NEW= "47.95.230.159";
    public static String SERVER_HOST_CLOUD_ALI_SHARE_NEW = "http://" + SERVER_IP_CLOUD_ALI_SHARE_NEW + ":81";
}
