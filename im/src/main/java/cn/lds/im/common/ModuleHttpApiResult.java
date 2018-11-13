package cn.lds.im.common;

import cn.lds.chatcore.common.CoreHttpApiResult;

public class ModuleHttpApiResult extends CoreHttpApiResult {
    public static String tag = ModuleHttpApiResult.class.getSimpleName();

    /**
     * A001: 登录
     */
    public static final String login() {
        return readJSON("login");
    }

    // ----------------------------- 首页-用车 sta -----------------------------

    /**
     * B001 获取所有的停车场
     */
    public static final String locationResources() {
        return readJSON("locationResources");
    }

    /**
     * B002 获取指定停车场的所有车辆信息
     */
    public static final String availableVehicles() {
        return readJSON("availableVehicles");
    }
    /**
     * 获取最近的场站的基本信息和可用车辆数量
     */
    public static final String instantNearest() {
        return readJSON("instantNearest");
    }
    /**
     * B002 获取指定停车场的所有车辆信息
     */
    public static final String scheduledNearest() {
        return readJSON("scheduledNearest");
    }

    // ----------------------------- 首页-用车 end -----------------------------
    // ----------------------------- 创建订单 sta -----------------------------

    /**
     * C001 创建订单
     */
    public static final String createOrder() {
        return readJSON("createOrder");
    }

    /**
     * C002 判断当前是否有进行中的订单
     */
    public static final String hasOrderExist() {
        return readJSON("hasOrderExist");
    }

    /**
     * C004 获取当前进行中订单的详情
     */
    public static final String getExistOrder() {
        return readJSON("getExistOrder");
    }

    /**
     * C005 提交车况 POST
     */

    public static final String condition() {
        return readJSON("condition");
    }

    /**
     * C006 获取订单详情
     * <p/>
     * id:ReservationOrder（订单ID）
     */

    public static final String reservationOrders() {
        return readJSON("reservationOrders");
    }

    /**
     * C007 获取费用详情
     * URL:http://localhost:8888
     * id:reservationOrderPaymentId（订单支付ID）
     */

    public static final String reservationOrderPaymentsDetail() {
        return readJSON("reservationOrderPaymentsDetail");
    }

    /**
     * C008 开车门
     * URL:http://localhost:8888/m/reservationOrders/{id}/open
     */
    public static final String reservationOrdersOpen() {
        return readJSON("reservationOrdersOpen");
    }

    /**
     * C008 开车门
     * URL:http://localhost:8888/m/reservationOrders/{id}/open
     */
    public static final String reservationOrdersOperation() {
        return readJSON("reservationOrdersOperation");
    }

    /**
     * C008 开车门
     * URL:http://localhost:8888/m/reservationOrders/{id}/open
     */
    public static final String reservationOrdersPickup() {
        return readJSON("reservationOrdersPickup");
    }


    /**
     * C009锁车门
     * URL:http://localhost:8888/m/reservationOrders/{id}/lock
     */

    public static final String reservationOrdersLock() {
        return readJSON("reservationOrdersLock");
    }

    /**
     * C010 鸣笛
     * URL:http://localhost:8888/m/reservationOrders/{id}/blast
     */

    public static final String reservationOrdersBlast() {
        return readJSON("reservationOrdersBlast");
    }

    /**
     * C011 闪灯
     * URL:http://localhost:8888/m/reservationOrders/{id}/flash
     */
    public static final String reservationOrdersFlash() {
        return readJSON("reservationOrdersFlash");
    }

    /**
     * C012 还车 （POST）
     * URL:/m/reservationOrders/{id}/return   //id 订单id
     */
    public static final String reservationOrdersReturn() {
        return readJSON("reservationOrdersReturn");
    }

    // ----------------------------- 创建订单  end -----------------------------

    // ----------------------------- 行程 sta-----------------------------

    /**
     * D001 获取所有行程
     */
    public static final String getJourneyList() {
        return readJSON("getJourneyList");
    }

    // ----------------------------- 行程  end -----------------------------

    // ----------------------------- 钱包 sta-----------------------------

    /**
     * E001 获取可开发票的最大金额 (GET)
     */
    public static final String getAmount() {
        return readJSON("getAmount");
    }

    /**
     * E002 获取最后一次发票明细 GET
     */

    public static final String getInvoiceIssueRecords() {
        return readJSON("getInvoiceIssueRecords");
    }

    /**
     * E003 新建发票 POST
     */

    public static final String postInvoiceIssueRecords() {
        return readJSON("postInvoiceIssueRecords");
    }

    // ----------------------------- 钱包  end -----------------------------

    // ----------------------------- 活动 sta-----------------------------

    /**
     * F001  获取活动列表 GET
     */
    public static final String getActivities() {
        return readJSON("getActivities");
    }

    // ----------------------------- 活动  end -----------------------------

    // ----------------------------- 违章 sta-----------------------------

    /**
     * G001  获取违章列表 GET
     */
    public static final String getPeccancies() {
        return readJSON("getPeccancies");
    }
    /**
     * G002  获取违章详情列表 GET
     */
    public static final String getPeccanciesDetail() {
        return readJSON("getPeccanciesDetail");
    }

    // ----------------------------- 违章  end -----------------------------

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
    public static final String reservationOrdersComment() {
        return readJSON("reservationOrdersComment");
    }

    /**
     * I002 获取订单评价
     * URL：http://localhost:8888/m/reservationOrders/{id}/getComment
     */
    public static final String reservationOrdersGetComment() {
        return readJSON("reservationOrdersGetComment");
    }

    // ----------------------------- 评论  end -----------------------------
    // ----------------------------- 其他 sta -----------------------------

    /**
     * 请求动态密码（POST）
     */

    public static final String password() {
        return readJSON("password");
    }

    /**
     * 个人信息修改
     * URL:http://localhost:8888/m/reservationOrderPayments/{id}
     */
    public static final String enrollees() {
        return readJSON("enrollees");
    }


    /**
     * 25. 获取IM连接地址接口(POST)
     * URL:/tms/api/mobile/connection/registerDevice
     * DATA
     * {
     * "clientId": "C1N7HX0MSLMDL",
     * "deviceId": "869336023489436",
     * "deviceType": 1,
     * "osType": 2
     * }
     */
    public static final String registerDevice() {
        return readJSON("registerDevice");
    }


    /**
     * 28.移动轨迹（POST）
     * URL：/m/reservationOrders/{id}/traceTraveled   //订单id
     */
    public static final String traceTraveled() {
        return readJSON("traceTraveled");
    }



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
    public static final String location() {
        return readJSON("location");
    }

    /**
     * 32.注销
     */
    public static final String logout() {
        return readJSON("logout");
    }
    // ----------------------------- 其他 end -----------------------------


}