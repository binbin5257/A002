package cn.lds.chatcore.common;

public class CoreHttpApiKey {
    /**
     * 未知
     */
    public static final String UNKNOW = "";

    // ----------------------------- IM sta -----------------------------
    /**
     * IM 注册
     */
    public static final String CONFIG_SERVER_URL = "CONFIG_SERVER_URL";
    /**
     * IM 注销
     */
    public static final String UNREGISTER_CONFIG_SERVER_URL = "UNREGISTER_CONFIG_SERVER_URL";
    // ----------------------------- IM end -----------------------------

    // ----------------------------- 登录 start -----------------------------
    /**
     * A001: 登录
     */
    public static final String login = "login";
    /**
     * A002: 登录 （账号密码登录）
     */
    public static final String loginCode = "loginCode";
    /**
     * A002: 校验access token
     */
    public static final String verifyCredentials = "verifyCredentials";
    /**
     * A003: 退出
     */
    public static final String logout = "logout";
    /**
     * A005: 手机注册
     */
    public static final String registerByMobile = "registerByMobile";
    /**
     * A006: 用户名注册
     */
    public static final String register = "register";
    /**
     * A010: 发送验证码
     */
    public static final String sendCAPTCHA = "sendCAPTCHA";

    public static final String weixinLogin = "weixinLogin";
    // ----------------------------- 登录 end -----------------------------


    // ----------------------------- 我 sta -----------------------------
    /**
     * E001: 获取我的个人详细信息
     */
    public static final String getMyDetail = "getMyDetail";
    /**
     * E002: 修改我的头像
     */
    public static final String changeAvatar = "changeAvatar";
    /**
     * E004: 设置昵称
     */
    public static final String changeNickname = "changeNickname";
    /**
     * E006: 设置性别
     */
    public static final String chanGegender = "chanGegender";
    /**
     * E012: 获取我的二维码
     */
    public static final String getMyCode = "getMyCode";
    /**
     * E014: 获取最新版本
     */
    public static final String getLastestVersion = "getLastestVersion";
    /**
     * E015: 更新手机号
     */
    public static final String updateMoblie = "updateMoblie";
    /**
     * E016: 更新邮件
     */
    public static final String updateEmail = "updateEmail";
    /**
     * E018: 修改密码
     */
    public static final String resetPassword = "resetPassword";
    /**
     * E018_1: 修改密码（通过手机）
     */
    public static final String resetPasswordByMobile = "resetPasswordByMobile";
    /**
     * E019: 记录个人的轨迹
     */
    public static final String track = "track";
    /**
     * E034: 设置地理位置
     */
    public static final String updateLocation = "updateLocation";
    // ----------------------------- 我 end -----------------------------

    // ----------------------------- 文件上传 start -----------------------------
    /**
     * Issue #21: 获取上传路径
     */
    public static final String getUploadUrl = "getUploadUrl";
    /**
     * Issue #21: 注册存储记录并执行内容标准化处理
     */
    public static final String registerFile = "registerFile";
    /**
     * Issue #21: 获取下载URL
     */
    public static final String getDownloadUrl = "getDownloadUrl";
    // ----------------------------- 文件上传 end -----------------------------

    // ----------------------------- 二维码 start -----------------------------
    /**
     * 002: 我的二维码
     */
    public static final String getMyQRcode = "getMyQRcode";
    /**
     * 003: 校验二维码
     */
    public static final String checkQRcode = "checkQRcode";
    /**
     * 004: 下载二维码
     */
    public static final String downloadQRcode = "downloadQRcode";
    // ----------------------------- 二维码 end -----------------------------


    // ----------------------------- 码表 sta -----------------------------
    /**
     * 同步码表
     */
    public static final String syncMasterCode = "syncMasterCode";
    // ----------------------------- 码表 end -----------------------------

    // ----------------------------- 其他 sta -----------------------------
    /**
     * ping: 保持session
     */
    public static final String ping = "ping";

    /**
     * 获取个人信息（GET）
     * URL:http://localhost:8888/m/enrollees
     */
    public static final String enrolleesGet = "enrolleesGet";
    /**
     * 个人信息修改
     * URL:http://localhost:8888/m/reservationOrderPayments/{id}
     */
    public static final String enrollees = "enrollees";

    // ----------------------------- 其他 end -----------------------------

    // ----------------------------- 首页-用车 sta -----------------------------
    /**
     * B001 获取所有的停车场
     */
    public static final String locationResources = "locationResources";
    /**
     * B001 获取所有的停车场
     */
    public static final String locationResourcesScheduled = "locationResourcesScheduled";
    /**
     * B002 获取指定停车场的所有车辆信息
     */
    public static final String availableVehicles = "availableVehicles";
    /**
     * B002 获取指定停车场的所有车辆信息
     */
    public static final String availableVehicleModels = "availableVehicleModels";
    /**
     * 获取最近的场站的基本信息和可用车辆数量
     */
    public static final String instantNearest = "instantNearest";
    /**
     * 获取最近的场站的基本信息和可用车型数量
     */
    public static final String scheduledNearest = "scheduledNearest";

    // ----------------------------- 首页-用车 end -----------------------------
    // ----------------------------- 创建订单 sta -----------------------------
    /**
     * C001 创建预约订单
     */
    public static final String scheduledReservationOrders = "scheduledReservationOrders";
    /**
     * C001 创建订单
     */
    public static final String createOrder = "createOrder";
    /**
     * C002 判断当前是否有进行中的订单
     */
    public static final String hasOrderExist = "hasOrderExist";
    /**
     * C002 判断当前是否有进行中的订单
     */
    public static final String hasOrderExistInMap = "hasOrderExistInMap";
    /**
     * C004 获取当前进行中订单的详情
     */
    public static final String getExistOrder = "getExistOrder";
    /**
     * C005 提交车况 POST
     */
    public static final String condition = "condition";
    /**
     * C006 获取订单详情
     * <p/>
     * id:ReservationOrder（订单ID）
     */
    public static final String reservationOrders = "reservationOrders";
    /**
     * C007 获取费用详情
     * URL:http://localhost:8888
     * id:reservationOrderPaymentId（订单支付ID）
     */
    public static final String reservationOrderPaymentsDetail = "reservationOrderPaymentsDetail";
    /**
     * C008 开车门
     * URL:http://localhost:8888/m/reservationOrders/{id}/open
     */
    public static final String reservationOrdersOpen = "reservationOrdersOpen";
    /**
     * C009锁车门
     * URL:http://localhost:8888/m/reservationOrders/{id}/lock
     */
    public static final String reservationOrdersLock = "reservationOrdersLock";
    /**
     * C010 鸣笛
     * URL:http://localhost:8888/m/reservationOrders/{id}/blast
     */
    public static final String reservationOrdersBlast = "reservationOrdersBlast";
    /**
     * C010 鸣笛
     * URL:http://localhost:8888/m/reservationOrders/{id}/blast
     */
    public static final String reservationOrdersStopBlast = "reservationOrdersStopBlast";
    /**
     * C011 闪灯
     * URL:http://localhost:8888/m/reservationOrders/{id}/flash
     */
    public static final String reservationOrdersFlash = "reservationOrdersFlash";
    /**
     * C012 还车 （POST）
     * URL:/m/reservationOrders/{id}/return   //id 订单id
     */
    public static final String reservationOrdersReturn = "reservationOrdersReturn";

    /**
     * C013 订单支付(POST)
     * URL:http://localhost:8888/m/reservationOrders/{id}/wxPay
     */
    public static final String reservationOrdersWxPay = "reservationOrdersWxPay";
    public static final String payment = "payment";
    /**
     * C013 订单支付(POST)
     * URL:http://localhost:8888/m/reservationOrders/{id}/alipay
     */
    public static final String reservationOrdersAlipay = "reservationOrdersAlipay";
    /**
     * C014 取消订单
     * URL:http://localhost:8888/m/reservationOrders/{id}/pay
     */
    public static final String reservationOrdersCancel = "reservationOrdersCancel";
    /**
     * C015 取车
     * URL:http://localhost:8888/m/reservationOrders/{id}/pickup
     */
    public static final String reservationOrdersPickup = "reservationOrdersPickup";
    /**
     * C015 获取操作
     * URL:http://localhost:8888/m/reservationOrders/{id}/pickup
     */
    public static final String reservationOrdersOperation = "reservationOrdersOperation";

// ----------------------------- 创建订单  end -----------------------------

    // ----------------------------- 行程 sta-----------------------------

    /**
     * D001 获取所有行程
     */
    public static final String getJourneyList = "getJourneyList";

    // ----------------------------- 行程  end -----------------------------


    // ----------------------------- 钱包 sta-----------------------------

    /**
     * E001 获取可开发票的最大金额 (GET)
     */
    public static final String getAmount = "getAmount";
    /**
     * E002 获取最后一次发票明细 GET
     */
    public static final String getInvoiceIssueRecords = "getInvoiceIssueRecords";

    // ----------------------------- 钱包  end -----------------------------

    // ----------------------------- 活动 sta-----------------------------

    /**
     * F001  获取活动列表 POST
     */
    public static final String getActivities = "getActivities";

    // ----------------------------- 活动  end -----------------------------

    // ----------------------------- 评论  sta -----------------------------

    /**
     * I001
     * 21.新增订单评价
     * URL：http://localhost:8888/m/reservationOrders/{id}/comment
     * Method：POST
     * Data：
     * {
     * "score": 1,//评分
     * "content":"123123123"//评价内容
     * }
     */
    public static final String reservationOrdersComment = "reservationOrdersComment";
    /**
     * I002 获取订单评价
     * URL：http://localhost:8888/m/reservationOrders/{id}/getComment
     */
    public static final String reservationOrdersGetComment = "reservationOrdersGetComment";

    // ----------------------------- 评论  end -----------------------------

    // ----------------------------- 其他 sta -----------------------------
    /**
     * 密码（POST）
     */
    public static final String password = "password";
     /**
     * 密码重置（POST）
     */
    public static final String reset = "reset";

    /**
     * 注册
     */
    public static final String regist = "regist";

    /**
     * 获取IM连接地址接口(POST)
     * URL:/tms/api/mobile/connection/registerDevice
     * DATA
     * {
     * "clientId": "C1N7HX0MSLMDL",
     * "deviceId": "869336023489436",
     * "deviceType": 1,
     * "osType": 2
     * }
     */
    public static final String registerDevice = "registerDevice";
    /**
     * 27.押金支付（POST）
     * URL：/m/foregiftAccounts
     * Respond:
     * {
     * "status": "success",
     * "data": {
     * "id": null,
     * "mchId": "***************",
     * "sign": "**************",
     * "nonceStr": "*************",
     * "prePayId": “***************”，
     * “timestamp”:”1400000000”
     * }
     * }
     */

    public static final String foregiftAccountsWxPay = "foregiftAccountsWxPay";
    public static final String foregiftAccountsAlipay = "foregiftAccountsAlipay";
    public static final String foregiftAccountsWalletpay = "foregiftAccountsWalletpay";
    public static final String reservationOrdersWalletpay = "reservationOrdersWalletpay";

    /**
     * 28.移动轨迹（POST）
     * URL：/m/reservationOrders/{id}/traceTraveled   //订单id
     */
    public static final String traceTraveled = "traceTraveled";
    /**
     * 29.充值（POST）
     * URL：/m/accounts
     * Respond:
     * {
     * "status": "success",
     * "data": {
     * "id": null,
     * "mchId": "***************",
     * "sign": "**************",
     * "nonceStr": "*************",
     * "prePayId": “***************”，
     * “timestamp”:”1400000000”
     * }
     * }
     */
    public static final String accountsAlipay = "accountsAlipay";
    public static final String checkVersions = "checkVersions";
    public static final String accountsWxPay = "accountsWxPay";
    /**
     * 30.判断用户当前的场站（POST）
     * URL：/m/reservationOrders/{id}/location
     * Respond:
     * {
     * "status": "success",
     * "data": {
     * id: 1                  // 所在场站ID。不在任何场站返回NULL
     * name: “上地停车场”
     * }
     * }
     */
    public static final String location = "location";
    /**
     * 获取还车场站列表(即时租车)
     * REQUEST URL：http://localhost:8891/m/reservationOrders/{orderNo}/locations
     */
    public static final String reservationOrdersLocations = "reservationOrdersLocations";
    /**
     * 配置信息
     */
    public static final String getSystemConfig = "getSystemConfig";
    /**
     * 确认加入组织
     */
    public static final String confirm = "confirm";

    /**
     * 拒绝加入组织
     */
    public static final String refuse = "refuse";

    /**
     * 费用明细
     */
    public static final String getCostDetail = "getCostDetail";

    /**
     * 我的发票
     */
    public static final String getReceiptList = "getReceiptList";

    /**
     * 发票明细
     */
    public static final String getReceiptInfo = "getReceiptInfo";

    /**
     * 优惠劵列表
     */
    public static final String getCouponList = "getCouponList";
    /**
     * 可用优惠劵列表
     */
    public static final String getCouponListRelease = "getCouponListRelease";
    /**
     * 获取行程列表
     */
    public static final String getTripList = "getTripList";
    /**
     * 审批人获取部门他人行程列表
     */
    public static final String getOtherTripList = "getOtherTripList";
    /**
     * 获取行程详情
     */
    public static final String getTripInfo = "getTripInfo";
    /**
     * 获取行程详情
     */
    public static final String getTripInfoFee = "getTripInfoFee";
    /**
     * 获取违章列表
     */
    public static final String getIllegalList = "getIllegalList";
    /**
     * 获取违章详情
     */
    public static final String getIllegalDetail = "getIllegalDetail";
    /**
     * 预约上门取车
     */
    public static final String pickUpCar = "pickUpCar";

    /**
     * 申请退款
     */
    public static final String refund = "refund";

    /**
     * 新建发票 POST
     */
    public static final String createReceipt = "createReceipt";

    /**
     * 当前用户是否有进行中的订单
     */
    public static final String getHasProcess = "getHasProcess";

    /**
     * 认证
     */
    public static final String identify = "identify";

    /**
     * 获取进行中行程
     */
    public static final String getProcess = "getProcess";
    public static final String getAccessToken = "getAccessToken";
    public static final String getLandLockList = "getLandLockList";

    // ----------------------------- 其他 end -----------------------------

}
