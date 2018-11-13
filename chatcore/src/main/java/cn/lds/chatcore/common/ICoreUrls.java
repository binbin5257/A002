package cn.lds.chatcore.common;

/**
 * Created by xuyongquan on 2015/11/21.
 */
public interface ICoreUrls {
    /**
     * 登出信息
     */
    public String getLogoutFilter();

    /**
     * 剔出信息
     */
    public String getKickedFilter();

    /**
     * 获取IM服务器地址
     */
    public String CONFIG_SERVER_URL();

    /**
     * 注销
     */
    public String UNREGISTER_CONFIG_SERVER_URL();

    /** APP更新地址 */
//   public String UPDATE_URL();

    /**
     * HTML5 SERVER HOST
     */
    public String H5_SERVER_HOST();

    /**
     * HTML5 SERVER DOMAIN
     */
    public String H5_SERVER_DOMAIN();
    // ----------------------------- 登录 sta -----------------------------

    /**
     * A001: 登录
     */
    public String login();
    /**
     * A001: 账号密码登录
     */
    public String loginCode();

    /**
     * A003: 退出
     */
    public String logout();

    /**
     * A009: 微信号登录绑定手机号
     */
    public String boundWeixinAndMobile();

    public String weixinLogin();
    // ----------------------------- 登录 end -----------------------------


    // ----------------------------- 文件上传 start -----------------------------

    /**
     * Issue #21: 获取上传路径
     */
    public String getUploadUrl();

    /**
     * Issue #21: 注册存储记录并执行内容标准化处理
     */
    public String registerFile();

    /**
     * Issue #21: 获取下载URL
     */
    public String getDownloadUrl(String fileStorageId);
    /**
     * Issue #21: 获取下载URL
     */
    public String getFileloadUrl(String fileStorageId);

    // ----------------------------- 其他 sta -----------------------------

    /**
     * 保持session
     */
    public String ping();

    /**
     * 获取个人信息（GET）
     * URL:http://localhost:8888/m/enrollees
     */
    public String enrolleesGet();

    /**
     * C013 获取微信支付订单详情(POST)
     * URL:http://localhost:8888/m/reservationOrders/{id}/wxPay
     */
    public String reservationOrdersWxPay();
    public String payment();

    /**
     * C013 获取支付宝支付订单详情(POST)
     * URL:http://localhost:8888/m/reservationOrders/{id}/alipay
     */
    public String reservationOrdersAlipay();
    public String reservationOrdersWalletpay();

    /**
     * 押金支付
     * URL:http://localhost:8888/m/reservationOrders/{id}/pay
     */
    public String foregiftAccountsWxPay();
    public String foregiftAccountsAlipay();
    public String foregiftAccountsWalletpay();

    /**
     * 充值
     * URL:http://localhost:8888/m/reservationOrders/{id}/pay
     */
    public String accountsWxPay();
    public String accountsAlipay();
    public String checkVersions();

    public String getSystemConfig();
    // ----------------------------- 其他 end -----------------------------
}
