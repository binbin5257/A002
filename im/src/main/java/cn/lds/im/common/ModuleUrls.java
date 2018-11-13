package cn.lds.im.common;

import cn.lds.chatcore.MyApplication;

public class ModuleUrls {
	/**
	 * 数据库名称
	 */


	// ----------------------------- 登录 sta -----------------------------
	/**
	 * 微信登录
	 */
	public static String WEIXIN_APP_ID = "wx0e6b78b93cf33401";
	public static String WEIXIN_APP_SECRET = "f46ea910662d5b738d42a979fb770ff8";
	public static String WEIXIN_SCOPE = "snsapi_userinfo";
	public static String WEIXIN_STATE = "wechat_sdk_demo_test";


	/**
	 * A001: 登录
	 */
	public static String login() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/login";
	}
	/**
	 * A002: 登录 （账号密码登录）
	 */
	public static String loginCode() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/loginCode";
	}


	/**
	 * A003: 退出
	 */

	public static String logout() {
		return MyApplication.getInstance().getSERVER_HOST() + "/logout";
	}


	/**
	 * A009: 微信号登录绑定手机号
	 */

	public static String boundWeixinAndMobile() {
		return MyApplication.getInstance().getSERVER_HOST() + "/chat/boundWeixinAndMobile";
	}

	;

	public static String weixinLogin() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/wechat/login";
	}

	;

	// ----------------------------- 登录 end -----------------------------
// ----------------------------- IM sta-----------------------------

	/**
	 * 获取IM服务器地址
	 */

	public static String CONFIG_SERVER_URL() {
		return MyApplication.getInstance().getSERVER_HOST() + "/tms/api/mobile/connection/registerDevice";
	}

	;

	/**
	 * 注销
	 */

	public static String UNREGISTER_CONFIG_SERVER_URL() {
		return MyApplication.getInstance().getSERVER_HOST() + "/tms/api/mobile/connection/unregisterDevice";
	}

	;

	// ----------------------------- IM end-----------------------------

	// ----------------------------- 首页-用车 sta -----------------------------

	/**
	 * B001 获取所有的停车场
	 */

	public static String locationResources() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/instant?" +
				"longitude={longitude}" +
				"&latitude={latitude}" +
				"&radius={radius}";
	}
	/**
	 * B001 获取所有的停车场
	 */

	public static String locationResourcesBusinessPrivateUseCar() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/instant?" +
				"longitude={longitude}" +
				"&latitude={latitude}" +
				"&radius={radius}" +
				"&operationType={operationType}"
				;
	}

	public static String locationResourcesScheduled() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/scheduled?" +
				"longitude={longitude}" +
				"&latitude={latitude}" +
				"&radius={radius}";
	}


	/**
	 * 公务用车，私有用车获取指定停车场的所有车辆信息
	 */

	public static String availableVehicles() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/{id}/availableVehicles?"
				+"operationType={operationType}";
	}

	/**
	 * 获取最近的场站的基本信息和可用车辆数量
	 */

	public static String getInstantNearest() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/instant/nearest";
	}
	/**
	 * 获取最近的场站的基本信息和可用车型数量
	 */
	public static String getScheduledNearest() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/scheduled/nearest";
	}
	/**
	 * B002 获取指定停车场的所有车辆信息
	 */

	public static String availableVehicleModels() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/{id}/availableVehicleModels";
	}

	;

	// ----------------------------- 首页-用车 end -----------------------------
	// ----------------------------- 订单 sta -----------------------------

	/**
	 * C001 创建预约订单
	 */

	public static String scheduledReservationOrders() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/scheduledOrders";
	}

	/**
	 * 上门取车
	 */

	public static String pickUpCar() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/homePickup";
	}

	/**
	 * C001 创建立即用车订单
	 */

	public static String createOrder() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/instantOrders";
	}

	;

	/**
	 * C002 判断当前是否有进行中的订单
	 */
//    public static String hasOrderExist () {         return  MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/hasProcess"};
	public static String hasOrderExist() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/hasProcess";
	}


	/**
	 * C004 获取当前进行中订单的详情
	 */
	public static String getExistOrder() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/process";
	}

	;


	/**
	 * C005 提交车况 POST
	 */

	public static String condition() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/condition";
	}


	/**
	 * C006 获取订单详情
	 * <p>
	 * orderNo:ReservationOrder（订单orderNo）
	 */

	public static String reservationOrders() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{orderNo}";
	}

	;

	/**
	 * C007 获取费用详情URL:http://localhost:8888/m/reservationOrders/{id}/paymentDetail
	 * id:reservationOrderPaymentId（订单支付ID）
	 */

	public static String reservationOrderPaymentsDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{id}/paymentDetail";
	}

	;

	/**
	 * C008 开车门
	 * URL:http://localhost:8888/m/reservationOrders/{id}/open
	 */

	public static String reservationOrdersOpen() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/unlock";
	}

	;

	/**
	 * C009锁车门
	 * URL:http://localhost:8888/m/reservationOrders/{id}/lock
	 */

	public static String reservationOrdersLock() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/lock";
	}

	;

	/**
	 * C010 鸣笛
	 * URL:http://localhost:8888/m/reservationOrders/{id}/blast
	 */

	public static String reservationOrdersBlast() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/startHonk";
	}

	;

	/**
	 * C010-1 停止鸣笛
	 * URL:http://localhost:8888/m/reservationOrders/{id}/blast
	 */

	public static String reservationOrdersStopBlast() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/stopHonk";
	}

	;

	/**
	 * C011 闪灯
	 * URL:http://localhost:8888/m/reservationOrders/{id}/flash
	 */

	public static String reservationOrdersFlash() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/startFlash";
	}

	;

	/**
	 * C012 还车 （POST）
	 * URL:/m/reservationOrders/{orderNo}/return?returnLocationNo=StackA_Location1N7ZYN3SC14M
	 */

	public static String reservationOrdersReturn() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/return";
	}

	;

	/**
	 * C013 订单支付(POST)
	 * URL:http://localhost:8888/m/reservationOrders/{id}/pay
	 */

	public static String reservationOrdersWxPay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{id}/wxPay";
	}

	public static String payment() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/payment/{businessId}/paid";
	}

	/**
	 * C013 订单支付(POST)
	 * URL:http://localhost:8888/m/reservationOrders/{id}/pay
	 */

	public static String reservationOrdersAlipay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/unifiedOrders/alipay";
	}

	public static String reservationOrdersWalletpay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{id}/walletPay";
	}

	/**
	 * C014 取车
	 * /m/reservationOrders/{orderNo}/pickup
	 */

	public static String reservationOrdersPickup() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{orderNo}/pickup";
	}

	;

	/**
	 * C016 获取操作结果
	 * URL:http://localhost:8888/m/reservationOrders/{id}/open
	 */

	public static String reservationOrdersOperation() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{id}/operation?operationId={operationId}";
	}

	;

	public static String reservationOrdersCancel() {
//        http://localhost:8891/m/reservationOrders/{orderNo}/cancel
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{orderNo}/cancel";
	}

	;
// ----------------------------- 订单  end -----------------------------
// ----------------------------- 行程 sta-----------------------------

	/**
	 * D001 获取所有行程
	 */

	public static String getJourneyList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/journeyList";
	}

	;

// ----------------------------- 行程  end -----------------------------
// ----------------------------- 钱包 sta-----------------------------

	/**
	 * E001 获取可开发票的最大金额 (GET)
	 */

	public static String getAmount() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/invoices/amount";
	}

	;

	/**
	 * E002 获取最后一次发票明细 GET
	 */

	public static String getInvoiceIssueRecords() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/invoiceIssueRecords";
	}


// ----------------------------- 钱包  end -----------------------------

// ----------------------------- 活动 sta-----------------------------

	/**
	 * F001  获取活动列表 GET
	 */

	public static String getActivities() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/activities";
	}

	;

	/**
	 * F002 获取活动详情 GET
	 * URL:/m/activities/{activityId}
	 */

	public static String getActivitiesDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/activities/{id}";
	}
    /**
     * 获取场站城市列表
     * URL:/m/locations/citys
     */

    public static String getLocationsCitys() {
        return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/citys";
    }


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

	public static String reservationOrdersComment() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/comment";
	}

	;

	/**
	 * I002.获取订单评价
	 * URL：http://localhost:8888/m/reservationOrders/{id}/comment
	 */

	public static String reservationOrdersGetComment() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{id}/comment";
	}

	;


// ----------------------------- 评论  end -----------------------------
// ----------------------------- 其他 sta -----------------------------

	/**
	 * 保持session
	 */

	public static String ping() {
		return MyApplication.getInstance().getSERVER_HOST() + "/ping";
	}

	;

	/**
	 * 请求动态密码（POST）发送验证码
	 */

	public static String password() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/password";
	}

	;

	/**
	 * 20.个人信息修改
	 * URL:http://localhost:8888/m/enrollees/{id}
	 * Method：POST
	 */

	public static String enrollees() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees";
	}

	;

	/**
	 * 获取个人信息（GET）
	 * URL:http://localhost:8888/m/enrollees
	 */

	public static String enrolleesGet() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees";
	}

	;

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

	public static String registerDevice() {
		return MyApplication.getInstance().getSERVER_HOST() + "/tms/api/mobile/connection/registerDevice";
	}

	;

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

	public static String foregiftAccountsAlipay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/unifiedOrders/alipay";
	}

	public static String foregiftAccountsWalletpay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/foregiftAccounts/walletPay";
	}

	public static String foregiftAccountsWxPay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/unifiedOrders/wxPay";
	}

	;

	/**
	 * 28.移动轨迹（POST）
	 * URL：/m/reservationOrders/{id}/traceTraveled   //订单id
	 */

	public static String traceTraveled() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{id}/traceTraveled?zoom={zoom}";
	}

	;

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

	public static String accountsAlipay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/unifiedOrders/alipay";
	}

	public static String checkVersions() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/appClientVersions/android/fszl/latest";
	}

	public static String accountsWxPay() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/unifiedOrders/wxPay";
	}

	;

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

	public static String location() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/reservationOrders/{no}/location";
	}

	/**
	 * 获取还车场站列表(即时租车)
	 * REQUEST URL：http://localhost:8891/m/reservationOrders/{orderNo}/locations
	 */

	public static String reservationOrdersLocations() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/available";
	}

	;

	/**
	 * Issue #21: 获取上传路径
	 */

	public static String getUploadUrl() {
		return MyApplication.getInstance().getSERVER_HOST() + "/files";
	}

	/**
	 * Issue #21: 注册存储记录并执行内容标准化处理
	 */

	public static String registerFile() {
		return MyApplication.getInstance().getSERVER_HOST() + "/storageRecord/register";
	}


	/**
	 * Issue #21: 获取下载URL
	 */

	public static String getDownloadUrl() {
		return MyApplication.getInstance().getSERVER_HOST() + "/download/{id}";
	}


	public static String getDownloadUrl(String fileStorageId) {
		return MyApplication.getInstance().getSERVER_HOST() + "/download/" + fileStorageId;
	}


	public static String getFilesUrl(String fileStorageId) {
		return MyApplication.getInstance().getSERVER_HOST() + "/files/" + fileStorageId;
	}

	/***
	 * 33.获取系统配置
	 * URL:http://localhost:8888/m/systemConfig
	 */
	public static String getSystemConfig() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/systemConfig";
	}

	/**
	 * 确认加入组织
	 * URL:/m/enrollees/confirm
	 */
	public static String confirm() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/confirm";
	}

	/**
	 * 拒绝加入组织
	 * URL:/m/enrollees/refuse
	 */
	public static String refuse() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/refuse";
	}

	/**
	 * 获取费用详情列表
	 */
	public static String getCostDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseAccountLogs";
	}

	/**
	 * 获取发票明细
	 */
	public static String getReceiptInfo() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/invoices/{id}";
	}

	/**
	 * 获取优惠劵列表
	 */
	public static String getCouponList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/tickets/s";
	}

	/**
	 * 获取可用优惠劵列表
	 */
	public static String getCouponListRelease() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/tickets/available";
	}

	/**
	 * 获取行程列表
	 */
	public static String getTripList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/journeys";
	}
	/**
	 * 审批人获取部门他人行程列表
	 */
	public static String getOtherTripList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/department/list";
	}

	/**
	 * 获取行程详情
	 */
	public static String getTripInfo() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}";
	}

	/**
	 * 获取行程详情-费用详情
	 */
	public static String getTripInfoFee() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/paymentDetail";
	}

	/**
	 * G001  获取违章列表 POST
	 */

	public static String getIllegalList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/vehicleIllegals";
	}

	/**
	 * G002 获取违章详情 GET
	 * URL:/m/vehicleIllegals/{vehicleIllegalsId}
	 */

	public static String getIllegalDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/vehicleIllegals/{id}";
	}

	/**
	 * 零钱支付
	 *
	 * @return
	 */
	public static String walletpayUrl() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/unifiedOrders/{id}/walletPay";
	}

	/**
	 * 申请退款
	 */

	public static String refund() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/foregiftAccounts/refund";
	}

	/**
	 * 新建发票 POST
	 */

	public static String createReceipt() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/invoices";
	}

	/**
	 * 我的发票
	 */

	public static String getReceiptList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/invoices/s";
	}

	/**
	 * 是否有进行中订单
	 */

	public static String getHasProcess() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/hasProcess";
	}


	/**
	 * 租车条款
	 */
	public static String getCarRentalClause() {
		return MyApplication.getInstance().getSERVER_HOST() + "/static/carRentalClause.html";
	}


	/**
	 * 用车注意事项
	 */
	public static String getPrecautionsForVehicles() {
		return MyApplication.getInstance().getSERVER_HOST() + "/static/precautionsForVehicles.html";
	}

	/**
	 * APP端协议认证帮助
	 */
	public static String getAuthenticationHelp() {
		return MyApplication.getInstance().getSERVER_HOST() + "/static/authenticationHelp.html";
	}

	/**
	 * 余款退款说明
	 */
	public static String getBalanceRefundNote() {
		return MyApplication.getInstance().getSERVER_HOST() + "/static/balanceRefundNote.html";
	}

	/**
	 * 客服
	 */
	public static String getCustomService() {
		return MyApplication.getInstance().getSERVER_HOST() + "/static/customService.html";
	}

	/**
	 * 常见问题
	 */
	public static String getFaq() {
		return MyApplication.getInstance().getSERVER_HOST() + "/static/faq.html";
	}

	/**
	 * 身份认证
	 */

	public static String identify() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/identityAuthentication";
	}

	/**
	 * 获取进行中行程
	 */
	public static String getProcess() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/process";
	}

	/**
	 * 获取送车上门费用
	 *
	 * @return
	 */
	public static String getDeliverCost() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/deliverCost";
	}
	/**
	 * 获取送车上门费用
	 *
	 * @return
	 */
	public static String getPickupCost() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/pickupCost";
	}

	/**
	 * 获取车辆计费详情
	 *
	 * @return
	 */
	public static String getRuleDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/vehicles/model/{id}/ruleDetail";
	}

	public static String getAdvertisements() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/advertisements";
	}

	/**
	 * 根据城市获取充电站列表
	 * @return
	 */
	public static String getChargingLocations() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/chargingLocations";

	}
	/**
	 * 根据充电场站详情
	 * @return
	 */
	public static String changeReturnLocation() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/changeReturnLocation";

	}
	/**
	 * 修改还车场站
	 * @return
	 */
	public static String getChargingLocationDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/chargingLocations/{id}";

	}

    public static String getChargingLocationsAll() {
        return MyApplication.getInstance().getSERVER_HOST() + "/m/chargingLocations/all";
    }

	public static String zhiMaCredit() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/authorize";
	}

	/**
	 * 附近场站
	 * @return
	 */
	public static String getNearStations() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/locations/around";
	}

	/**
	 * 注册接口
	 * @return
	 */
    public static String register() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/registered";

	}

	/**
	 * 忘记密码
	 * @return
	 */
	public static String forgetPassword() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/forgetPassword";

	}

	/**
	 * 我的申请列表
	 * @return
	 */
	public static String getApplyList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/s";
	}

	/**
	 * 提交用车申请
	 * @return
	 */
    public static String submitApplyForUserCar() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications";
    }
	/**
	 * 我的审批列表列表
	 * @return
	 */
	public static String getMarkingList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/s/approver";
	}

	/**
	 * 获取审批详情
	 * @return
	 */
	public static String getMarkingDetail() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/{id}";
	}
	/**
	 * 用车申请审核通过
	 * @return
	 */
	public static String markingPass() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/{id}/pass";


	}
	/**
	 * 用车申请审核拒绝
	 * @return
	 */
	public static String markingRefuse() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/{id}/refuse";

	}

    public static String checkUseApply() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/checkUseVehicle";

	}

    public static String getApprover() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/getApprover";
    }

    public static String cancelApply() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/publicVehicleApplications/{id}/withdraw";

	}

	/**
	 * 丁丁 accessToken url
	 * @return
	 */
    public static String getDdtcAccessToken() {
        return "http://test.dingdingtingche.com/ddtcSDK/queryAccessToken?appId={appId}&appSecret={appSecret}";
    }

    public static String getLandLockList() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/baseOrders/{id}/groundLocks";

	}

    public static String getDepartmentBalance() {
        return MyApplication.getInstance().getSERVER_HOST() + "/m/enrollees/department/account";
    }

	public static String getDepartmentCars() {
		return MyApplication.getInstance().getSERVER_HOST() + "/m/vehicles/department/list";
	}




// ----------------------------- 其他 end -----------------------------

}
