package cn.lds.im.common;

import cn.lds.chatcore.common.CoreHttpApiKey;

public class ModuleHttpApiKey extends CoreHttpApiKey {

	/**
	 * 获取送车上门费用
	 */
	public static final String getDeliverCost = "getDeliverCost";
	/**
	 * 获取上门取车费用
	 */
	public static final String getPickupCost = "getPickupCost";

	/**
	 * 获取车辆计费详情
	 */
	public static final String getRuleDetail = "getRuleDetail";
	/**
	 * 获取活动详情
	 */
    public static final String getActivitiesDetail = "getActivitiesDetail";
	/**
	 * 获取城市列表
	 */
    public static final String getLocationsCitys = "getLocationsCitys";
	/**
	 * 获取首页活动列表
	 */
	public static final String getAdvertisements = "getAdvertisements";
	/**
	 * 根据城市获取充电站列表
	 */
    public static final String getChargingLocations_refresh = "getChargingLocations_refresh";
    public static final String getChargingLocations_loadmore = "getChargingLocations_loadmore";
	/**
	 * 获取充电站详情
	 */
    public static final String getChargingLocationDetail = "getChargingLocationDetail";
    public static final String getChargingLocationsAll = "getChargingLocationsAll";
	/**
	 * 芝麻认证接口
	 */
    public static final String zhiMaCredit = "zhiMaCredit";

	/**
	 * 芝麻认证接口
	 */
	public static final String nearStationList_refresh = "nearStationList_refresh";
	public static final String nearStationList_loadmore = "nearStationList_loadmore";

	/**
	 * 修改还车场站
	 */
	public static final String changeReturnLocation = "changeReturnLocation";
	/**
	 * 获取我的申请列表
	 */
    public static final String getApplyList = "getApplyList";

    public static final String submitApplyForUserCar = "submitApplyForUserCar";

    public static final String getMarkingList = "getMarkingList";

    public static final String getMarkingDetail = "getMarkingDetail";
    public static final String markingPass = "markingPass";
    public static final String markingRefuse = "markingRefuse";
    public static final String checkUseApply = "checkUseApply";
    public static final String getApprover = "getApprover";
    public static final String cancelApply = "cancelApply";
    public static final String getDepartmentBalance = "getDepartmentBalance";
    public static final String getDepartmentCars = "getDepartmentCars";
}
