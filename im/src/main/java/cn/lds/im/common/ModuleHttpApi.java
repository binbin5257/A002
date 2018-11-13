package cn.lds.im.common;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.DeviceHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.HttpHelper;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.BookCarModel;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginModel;
import cn.lds.chatcore.data.PersonInfo;
import cn.lds.chatcore.data.RegistModel;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.im.data.CreateReceiptModel;
import cn.lds.im.data.InspectModel;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.data.OrderSendCarModel;
import cn.lds.im.data.PickUpModel;
import cn.lds.im.data.TakeCarRequestModel;
import de.greenrobot.event.EventBus;


/**
 * Created by geese on 2015/11/27.
 */
public class ModuleHttpApi extends CoreHttpApi {
	/**
	 * A001: 登录
	 */
	public static void loginByIdentyfyCode(String mobile, String password) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				// mobile+验证码
				json.put("mobile", mobile);
				json.put("password", password);
				HttpHelper.post(ModuleUrls.login(), ModuleHttpApiKey.login, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("A001: 登录", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.login();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.login, ModuleUrls.login(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * A002:请求动态密码（POST）
	 * <p/>
	 * Data：
	 * {
	 * "mobile": "13912345678",
	 * "deviceId": "xxxxxxxx"
	 * }
	 */

	public static void getVerificationCode(LoginModel loginRequestModel) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				json.put("mobile", loginRequestModel.getUsername());
				json.put("deviceId", loginRequestModel.getDeviceId());
				json.put("type", loginRequestModel.getType());
				HttpHelper.post(ModuleUrls.password(), ModuleHttpApiKey.password, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("请求动态密码", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.password();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.password, ModuleUrls.password(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	// ----------------------------- 首页-预约用车 sta -----------------------------

	/**
	 * 3.获取所有场站的基本信息和可用车辆数量(预约租车)
	 */
	public static void locationResourcesScheduled() {
		if (CoreHttpApi.IS_ONLINE) {
			// 发送请求
			if (null != MyApplication.myLocation) {
				// 构建URL
				String url = ModuleUrls.locationResourcesScheduled();
				url = url.replace("{longitude}", ToolsHelper.toString(MyApplication.myLocation.longitude));
				url = url.replace("{latitude}", ToolsHelper.toString(MyApplication.myLocation.latitude));
				url = url.replace("{radius}", "50000");//暂定50公里
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.locationResourcesScheduled);
			}
		} else {
			String strResult = ModuleHttpApiResult.locationResources();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.locationResourcesScheduled, ModuleUrls.locationResourcesScheduled(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * B002 获取指定停车场的所有车辆信息
	 */
	public static void availableVehicleModels(String locationNo) {
		if (CoreHttpApi.IS_ONLINE) {

			try {
				// 构建URL
				String url = ModuleUrls.availableVehicleModels();
				url = url.replace("{id}", ToolsHelper.toString(locationNo));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.availableVehicleModels);
			} catch (Exception ex) {
				LogHelper.e("B002 获取指定停车场的所有车辆信息", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.availableVehicles();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.availableVehicleModels, ModuleUrls.availableVehicles(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}
	/**
	 * 获取最近的场站的基本信息和可用车辆数量
	 */
	public static void getInstantNearest(String flag,LatLng latLng){
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getInstantNearest();
				Map<String,String> map = new HashMap<>();
				map.put("operationType",flag);
				map.put("longitude",latLng.longitude + "");
				map.put("latitude",latLng.latitude + "");
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.instantNearest,map,null);
			} catch (Exception ex) {
				LogHelper.e("获取最近的场站的基本信息和可用车辆数量", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.instantNearest();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.instantNearest, ModuleUrls.getInstantNearest(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}
	/**
	 * 获取最近的场站的基本信息和可用车型数量
	 */
	public static void getScheduledNearest(LatLng latLng){
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getScheduledNearest();
				Map<String,String> map = new HashMap<>();
				map.put("longitude",latLng.longitude + "");
				map.put("latitude",latLng.latitude + "");
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.scheduledNearest,map,null);
			} catch (Exception ex) {
				LogHelper.e("获取最近的场站的基本信息和可用车辆数量", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.scheduledNearest();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.scheduledNearest, ModuleUrls.getScheduledNearest(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}
	// ----------------------------- 首页-预约用车 end -----------------------------
	// ----------------------------- 首页-立即用车 sta -----------------------------

	/**
	 * B001 获取所有的停车场
	 */
	public static void locationResources() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			if (null != MyApplication.myLocation) {
				String url = ModuleUrls.locationResources();
				url = url.replace("{longitude}", ToolsHelper.toString(MyApplication.myLocation.longitude));
				url = url.replace("{latitude}", ToolsHelper.toString(MyApplication.myLocation.latitude));
				url = url.replace("{radius}", "50000");//暂定50公里
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.locationResources);
			}
		} else {
			String strResult = ModuleHttpApiResult.locationResources();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.locationResources, ModuleUrls.locationResources(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}
		/**
		 * B001 获取所有的停车场
		 */
		public static void locationResourcesBusinessPrivateUseCar(String flag) {
			if (CoreHttpApi.IS_ONLINE) {
				// 构建URL
				if (null != MyApplication.myLocation) {
					String url = ModuleUrls.locationResourcesBusinessPrivateUseCar();
					url = url.replace("{longitude}", ToolsHelper.toString(MyApplication.myLocation.longitude));
					url = url.replace("{latitude}", ToolsHelper.toString(MyApplication.myLocation.latitude));
					url = url.replace("{radius}", "50000");//暂定50公里
					url = url.replace("{operationType}", flag);
					// 发送请求
					HttpHelper.get(url, ModuleHttpApiKey.locationResources);
				}
			} else {
				String strResult = ModuleHttpApiResult.locationResources();
				HttpResult httpResult = new HttpResult(ModuleHttpApiKey.locationResources, ModuleUrls.locationResources(), strResult,
						null);
				httpResult.setExtras(null);
				EventBus.getDefault().post(new HttpRequestEvent(httpResult));
			}

		}

	/**
	 * B002 获取指定停车场的所有车辆信息
	 */
	public static void availableVehicles(String locationNo,String operationType) {
		if (CoreHttpApi.IS_ONLINE) {

			try {
				// 构建URL
				String url = ModuleUrls.availableVehicles();
				url = url.replace("{id}", ToolsHelper.toString(locationNo));
				url = url.replace("{operationType}", operationType);
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.availableVehicles);
			} catch (Exception ex) {
				LogHelper.e("B002 获取指定停车场的所有车辆信息", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.availableVehicles();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.availableVehicles, ModuleUrls.availableVehicles(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}
	// ----------------------------- 首页-立即用车 end -----------------------------
	// ----------------------------- 创建订单 sta -----------------------------

	/**
	 * C001 创建预约订单
	 */
	public static void scheduledReservationOrders(BookCarModel model, String locationId) {
		scheduledReservationOrders(false, null, model, locationId);
	}

	/**
	 * C001 创建送车上门的预约订单
	 */
	public static void scheduledReservationOrders(boolean delivered, OrderSendCarModel orderSendCarModel, BookCarModel model, String locationId) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				if (delivered) {
					json.put("delivered", delivered);
					json.put("name", orderSendCarModel.getName());
					json.put("address", orderSendCarModel.getAddress());
					json.put("latitude", orderSendCarModel.getLatitude());
					json.put("longitude", orderSendCarModel.getLongitude());
					json.put("phone", orderSendCarModel.getPhone());
				}
				json.put("modelId", model.getVehicleModelNo());  // 车型Id
				json.put("scheduledPickedUpTime", model.getScheduledPickedUpTime());  // 预约取车时间
				json.put("scheduledDroppedOffTime", model.getScheduledDroppedOffTime());  // 预约还车时间
				json.put("locationId", locationId);  // 预约取车场站Id
				json.put("returnLocationId", model.getLocationNo());  // 还车场站Id
				HttpHelper.post(ModuleUrls.scheduledReservationOrders(), ModuleHttpApiKey.scheduledReservationOrders, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("C001 创建预约订单", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.createOrder();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.scheduledReservationOrders, ModuleUrls.scheduledReservationOrders(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C001 创建立即用车的订单
	 */
	public static void createOrder(String vehicleNo, String returnLocationNo) {
		createOrder(false, null, vehicleNo, returnLocationNo);
	}

	/**
	 * C001 创建送车上门的立即用车的订单
	 */
	public static void createOrder(boolean delivered, OrderSendCarModel orderSendCarModel, String vehicleNo, String returnLocationNo) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				if (delivered) {
					json.put("delivered", delivered);
					json.put("name", orderSendCarModel.getName());
					json.put("address", orderSendCarModel.getAddress());
					json.put("latitude", orderSendCarModel.getLatitude());
					json.put("longitude", orderSendCarModel.getLongitude());
					json.put("phone", orderSendCarModel.getPhone());
				}
				json.put("vehicleId", vehicleNo);
				json.put("returnLocationId", returnLocationNo);
				HttpHelper.post(ModuleUrls.createOrder(), ModuleHttpApiKey.createOrder, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("C001 创建订单", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.createOrder();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.createOrder, ModuleUrls.createOrder(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C002 判断当前是否有进行中的订单
	 */
	public static void hasOrderExist() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.hasOrderExist();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.hasOrderExist);
		} else {
			String strResult = ModuleHttpApiResult.hasOrderExist();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.hasOrderExist, ModuleUrls.hasOrderExist(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C002 判断当前是否有进行中的订单
	 */
	public static void hasOrderExistInMap() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
//			String url = ModuleUrls.hasOrderExist();
			// 构建URL
			String url = ModuleUrls.getHasProcess();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.hasOrderExistInMap);
		} else {
			String strResult = ModuleHttpApiResult.hasOrderExist();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.hasOrderExistInMap, ModuleUrls.hasOrderExist(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C004 获取当前进行中订单的详情
	 */
	public static void getExistOrder() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getExistOrder();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getExistOrder);
		} else {
			LogHelper.e("getExistOrder");
			String strResult = ModuleHttpApiResult.getExistOrder();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.getExistOrder, ModuleUrls.getExistOrder(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C005 提交车况 POST
	 * <p/>
	 * DATA：
	 * {
	 * "scratch": true,                   // 是否有划痕
	 * "sanitaryState": "GARBAGE",
	 * "goingWell": false,
	 * "picList": [1,2]
	 * }
	 */
	public static void condition(InspectModel inspectMode, int id) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.condition();
			url = url.replace("{id}", ToolsHelper.toString(id));
			String json = GsonImplHelp.get().toJson(inspectMode);
			try {
				HttpHelper.post(url, ModuleHttpApiKey.condition, json, null);
			} catch (Exception e) {
				LogHelper.e("C005: 提交车况", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.condition();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.condition, ModuleUrls.condition(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C006 获取订单详情
	 * <p/>
	 * id:ReservationOrder（订单ID）
	 */
	public static void reservationOrders(String orderNo) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrders();
				url = url.replace("{orderNo}", ToolsHelper.toString(orderNo));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.reservationOrders);
			} catch (Exception ex) {
				LogHelper.e("C006 获取订单详情", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrders();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrders, ModuleUrls.reservationOrders(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C007 获取费用详情URL:http://localhost:8888/m/reservationOrders/{id}/paymentDetail
	 * id:reservationOrderPaymentId（订单支付ID）
	 */
	public static void reservationOrderPaymentsDetail(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrderPaymentsDetail();
				url = url.replace("{id}", ToolsHelper.toString(id));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.reservationOrderPaymentsDetail);
			} catch (Exception ex) {
				LogHelper.e("C007 获取费用详情", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrderPaymentsDetail();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrderPaymentsDetail, ModuleUrls.reservationOrderPaymentsDetail(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C008 开车门
	 * URL:http://localhost:8888/m/reservationOrders/{id}/open
	 * {
	 * "longitude": 116.1234,      // 手机端定位的经度
	 * "latitude": 39.11111
	 * }
	 */
	public static void reservationOrdersOpen(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersOpen();
				url = url.replace("{id}", ToolsHelper.toString(id));
				// 发送请求
				HttpHelper.put(url, ModuleHttpApiKey.reservationOrdersOpen);
			} catch (Exception ex) {
				LogHelper.e("C008 开车门", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersOpen();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersOpen, ModuleUrls.reservationOrdersOpen(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * /**
	 * C009锁车门
	 * URL:http://localhost:8888/m/reservationOrders/{id}/lock
	 */
	public static void reservationOrdersLock(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersLock();
				url = url.replace("{id}", ToolsHelper.toString(id));
//                JSONObject json = new JSONObject();
//
//                json.put("longitude", MyApplication.myLocation.longitude);
//                json.put("latitude", MyApplication.myLocation.latitude);
				// 发送请求
				HttpHelper.put(url, ModuleHttpApiKey.reservationOrdersLock);
			} catch (Exception ex) {
				LogHelper.e("C009锁车门", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersLock();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersLock, ModuleUrls.reservationOrdersLock(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C010 鸣笛
	 * URL:http://localhost:8888/m/reservationOrders/{id}/blast
	 * {
	 * "longitude": 116.1234,      // 手机端定位的经度
	 * "latitude": 39.11111
	 * }
	 */
	public static void reservationOrdersBlast(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersBlast();
				url = url.replace("{id}", ToolsHelper.toString(id));
//                JSONObject json = new JSONObject();
//
//                json.put("longitude", MyApplication.myLocation.longitude);
//                json.put("latitude", MyApplication.myLocation.latitude);
				// 发送请求
				HttpHelper.put(url, ModuleHttpApiKey.reservationOrdersBlast);
			} catch (Exception ex) {
				LogHelper.e("C010 鸣笛", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersBlast();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersBlast, ModuleUrls.reservationOrdersBlast(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C010-1 停止鸣笛
	 * URL:http://localhost:8888/m/reservationOrders/2/stopBlast
	 * {
	 * "status": "success",
	 * "data": "e3c28bfb-5d15-4ed5-9076-7b0254498144"
	 * }
	 */
	public static void reservationOrdersStopBlast(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersStopBlast();
				url = url.replace("{id}", ToolsHelper.toString(id));
				JSONObject json = new JSONObject();

				json.put("longitude", MyApplication.myLocation.longitude);
				json.put("latitude", MyApplication.myLocation.latitude);
				// 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.reservationOrdersStopBlast, json.toString(), null);
			} catch (Exception ex) {
				LogHelper.e("C010-1 停止鸣笛", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersBlast();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersBlast, ModuleUrls.reservationOrdersBlast(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C1011 闪灯
	 * URL:http://localhost:8888/m/reservationOrders/{id}/flash
	 * Data：
	 * {
	 * "longitude": 116.1234,      // 手机端定位的经度
	 * "latitude": 39.11111
	 * }
	 */
	public static void reservationOrdersFlash(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersFlash();
				url = url.replace("{id}", ToolsHelper.toString(id));
//                JSONObject json = new JSONObject();
//
//                json.put("longitude", MyApplication.myLocation.longitude);
//                json.put("latitude", MyApplication.myLocation.latitude);
				// 发送请求
				HttpHelper.put(url, ModuleHttpApiKey.reservationOrdersFlash);
			} catch (Exception ex) {
				LogHelper.e("C1011 闪灯", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersFlash();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersFlash, ModuleUrls.reservationOrdersFlash(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C012 还车 （put）
	 * URL:/m/reservationOrders/{id}/return   //id 订单id
	 */
	public static void reservationOrdersReturn(long id, TakeCarRequestModel requestData) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersReturn();
				url = url.replace("{id}", ToolsHelper.toString(id));
                String json = GsonImplHelp.get().toJson(requestData);
				HttpHelper.put(url, ModuleHttpApiKey.reservationOrdersReturn, json.toString());
			} catch (Exception ex) {
				LogHelper.e("C012 还车", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersReturn();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersReturn, ModuleUrls.reservationOrdersReturn(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}


	/**
	 * C014 取消订单
	 * URL:http://localhost:8888/m/reservationOrders/{id}/cancel
	 */
	public static void reservationOrdersCancel(String orderNo) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersCancel();
				url = url.replace("{orderNo}", ToolsHelper.toString(orderNo));
				HttpHelper.put(url, ModuleHttpApiKey.reservationOrdersCancel);
			} catch (Exception ex) {
				LogHelper.e("C014 取消订单", ex);
			}
		}
	}

	/**
	 * C015 取车
	 * URL:http://localhost:8888/m/reservationOrders/{orderNo}/pickup
	 * {
	 * "longitude": 116.1234,      // 手机端定位的经度
	 * "latitude": 39.11111
	 * }
	 */
	public static void reservationOrdersPickup(OrderInfoModel model, TakeCarRequestModel requestData) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersPickup();
				url = url.replace("{orderNo}", ToolsHelper.toString(model.getData().getId()));
                String json = GsonImplHelp.get().toJson(requestData);
                // 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.reservationOrdersPickup, json.toString());
			} catch (Exception ex) {
				LogHelper.e("C015 取车", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersPickup();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersPickup, ModuleUrls.reservationOrdersPickup(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C016 获取操作结果
	 * URL:http://localhost:8888/m/reservationOrders/2/operation?operationId={operationId}
	 */
	public static void reservationOrdersOperation(String id, String operationId) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersOperation();
				url = url.replace("{operationId}", ToolsHelper.toString(operationId));
				url = url.replace("{id}", ToolsHelper.toString(id));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.reservationOrdersOperation);
			} catch (Exception ex) {
				LogHelper.e("C016 获取操作结果", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersOperation();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersOperation, ModuleUrls.reservationOrdersOperation(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	// ----------------------------- 创建订单  end -----------------------------
	// ----------------------------- 行程 sta -----------------------------

	/**
	 * D001 获取全部行程
	 */
	public static void getJourneyList() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getJourneyList();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getJourneyList);
		} else {
			String strResult = ModuleHttpApiResult.getJourneyList();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.getJourneyList, ModuleUrls.getJourneyList(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	// ----------------------------- 行程  end -----------------------------


	// ----------------------------- 钱包 sta-------------------------------

	/**
	 * E001 获取可开发票的最大金额 (GET)
	 */
	public static void getAmount() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getAmount();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getAmount);
		} else {
			String strResult = ModuleHttpApiResult.getAmount();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.getAmount, ModuleUrls.getAmount(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * E002 获取最后一次发票明细 GET
	 */
	public static void getInvoiceIssueRecords() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getInvoiceIssueRecords();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getInvoiceIssueRecords);
		} else {
			String strResult = ModuleHttpApiResult.getInvoiceIssueRecords();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.getInvoiceIssueRecords, ModuleUrls.getInvoiceIssueRecords(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	// ----------------------------- 钱包  end -----------------------------
// ----------------------------- 评论  sta -----------------------------

	/**
	 * I001 新增订单评价
	 * URL：http://localhost:8888/m/reservationOrders/{id}/comment
	 * Method：POST
	 * Data：
	 * {
	 * "score": 1,//评分
	 * "content":"123123123"//评价内容
	 * }
	 */
	public static void reservationOrdersComment(String id, int score) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersComment();
				url = url.replace("{id}", ToolsHelper.toString(id));
				JSONObject json = new JSONObject();

				json.put("score", score);
				// 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.reservationOrdersComment, json.toString(), null);
			} catch (Exception ex) {
				LogHelper.e("I001 新增订单评价", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersComment();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersComment, ModuleUrls.reservationOrdersComment(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * I002 获取订单评价
	 * URL：http://localhost:8888/m/reservationOrders/{id}/getComment
	 */
	public static void reservationOrdersGetComment(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersGetComment();
				url = url.replace("{id}", ToolsHelper.toString(id));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.reservationOrdersGetComment);
			} catch (Exception ex) {
				LogHelper.e("I002 获取订单评价", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.reservationOrdersComment();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersGetComment, ModuleUrls.reservationOrdersGetComment(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}
	// ----------------------------- 评论  end -----------------------------

	// ----------------------------- 其他 sta -----------------------------

	/**
	 * 20.个人信息修改(PUT)
	 * URL:http://localhost:8888/m/enrollees
	 * Data：
	 * {
	 * "headPortraitId": "xxxxxxxxxxx",      //头像
	 * "name": "张三",                       //姓名
	 * "gender":"MALE",                      //性别。枚举（MALE-男，FEMALE-女）
	 * "email":"xxxxx@qq.com",               //邮箱
	 * "identityFrontId":"xxxxxxxxxxxxxxx",  //身份证正面图片
	 * "driveLicenseId":"xxxxxxxxxxxxxxx"    //驾驶证图片
	 * }
	 */
	public static void enrollees(PersonInfo personInfo, String key, String value) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.enrollees();
				if (!ToolsHelper.isNull(key) && !ToolsHelper.isNull(value)) {
					Map<String, String> extras = new HashMap<>();
					extras.put("key", key);
					extras.put("value", value);
					// 发送请求
					HttpHelper.put(url, ModuleHttpApiKey.enrollees, GsonImplHelp.get().toJson(personInfo), extras);
				} else {
					// 发送请求
					HttpHelper.put(url, ModuleHttpApiKey.enrollees, GsonImplHelp.get().toJson(personInfo));
				}
			} catch (Exception ex) {
				LogHelper.e("个人信息修改", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.enrollees();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.enrollees, ModuleUrls.enrollees(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}


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
	public static void registerDevice(String clientId, String osType, String deviceType) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.registerDevice();
				JSONObject json = new JSONObject();

				json.put("clientId", clientId);
				json.put("deviceId", DeviceHelper.getDeviceId());
				json.put("deviceType", deviceType);
				json.put("osType", osType);
				// 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.registerDevice, json.toString(), null);
			} catch (Exception ex) {
				LogHelper.e("获取IM连接地址接口", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.registerDevice();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.registerDevice, ModuleUrls.registerDevice(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}


	/**
	 * 28.移动轨迹（POST）
	 * URL：http://localhost:8888/m/reservationOrders/2/traceTraveled?zoom=15   //订单id
	 */

	public static void traceTraveled(String id, int zoom) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.traceTraveled();
				url = url.replace("{id}", id);
				url = url.replace("{zoom}", String.valueOf(zoom).toString());
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.traceTraveled, null);
			} catch (Exception ex) {
				LogHelper.e("accounts", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.traceTraveled();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.traceTraveled, ModuleUrls.traceTraveled(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}


	/**
	 * 30.判断用户当前的场站（POST）
	 * URL：/m/reservationOrders/{no}/location
	 * Respond:
	 * {
	 * "status": "success",
	 * "data": {
	 * id: 1                  // 所在场站ID。不在任何场站返回NULL
	 * no:""
	 * name: “上地停车场”
	 * }
	 * }
	 */
	public static void location(String no) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.location();
				url = url.replace("{no}", no);
				// 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.location, null);
			} catch (Exception ex) {
				LogHelper.e("获取IM连接地址接口", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.location();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.location, ModuleUrls.location(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * 获取还车场站列表(即时租车)
	 * REQUEST URL：http://localhost:8891/m/reservationOrders/{orderNo}/locations
	 */
	public static void reservationOrdersLocations(String cityCode) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.reservationOrdersLocations();
				Map<String,String> params = new HashMap<>();
				if(!TextUtils.isEmpty(cityCode)){
					params.put("cityCode",cityCode);
				}
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.reservationOrdersLocations,params,null);
			} catch (Exception ex) {
				LogHelper.e("获取IM连接地址接口", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.location();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersLocations, ModuleUrls.location(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
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
	public static void logout() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.logout();
				// 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.logout, null);
			} catch (Exception ex) {
				LogHelper.e("logout", ex);
			}
		} else {
			String strResult = ModuleHttpApiResult.location();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.logout, ModuleUrls.logout(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}


	/**
	 * 确认加入组织
	 * URL:/m/enrollees/confirm
	 */
	public static void confirm() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.confirm();
				HttpHelper.put(url, ModuleHttpApiKey.confirm);
			} catch (Exception ex) {
				LogHelper.e("确认加入组织", ex);
			}
		} else {
		}

	}

	/**
	 * 拒绝加入组织
	 * URL:/m/enrollees/refuse
	 */
	public static void refuse() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.refuse();
				HttpHelper.put(url, ModuleHttpApiKey.refuse);
			} catch (Exception ex) {
				LogHelper.e("拒绝加入组织", ex);
			}
		} else {
		}

	}

	/**
	 * 获取账户明细
	 */
	public static void getCostDetail(int page) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getCostDetail();

			Map params = new HashMap();

			params.put("page", page + "");
			params.put("sort", "createdDate,desc");
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getCostDetail, params, null);
		}

	}

	/**
	 * 获取我的发票
	 */
	public static void getReceiptList(int page) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getReceiptList();

			Map params = new HashMap();

			params.put("page", page + "");
			params.put("sort", "createdDate,desc");
			// 发送请求
			HttpHelper.post(url, ModuleHttpApiKey.getReceiptList, params, null);
		}

	}

	/**
	 * 获取发票信息
	 */
	public static void getReceiptInfo(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getReceiptInfo();
			url = url.replace("{id}", id);
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getReceiptInfo, null, null);
		}

	}

	/**
	 * 优惠劵列表
	 */
	public static void getCouponList(int page,String status) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getCouponList();

			Map params = new HashMap();

			params.put("page", page + "");
			if(!TextUtils.isEmpty(status)){
				params.put("s_status",status);
			}
			params.put("sort","price,desc");
			// 发送请求
			HttpHelper.post(url, ModuleHttpApiKey.getCouponList + status, params, null);
		}

	}
	/**
	 * 可用优惠劵列表
	 */
	public static void getCouponListRelease(String orderId) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getCouponListRelease();

			Map params = new HashMap();

			params.put("orderId", orderId);
			params.put("sort","price,desc");
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getCouponListRelease, params, null);
		}

	}

	/**
	 * 获取行程列表
	 */
	public static void getTripList() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getTripList();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getTripList, null, null);
		}

	}
	/**
	 * 审批人获取部门他人行程列表
	 * @param size
	 * @param page
	 */
	public static void getOtherTripList(int size, int page) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getOtherTripList();
			Map<String,String> map = new HashMap<>();
			map.put("page",page +"");
			map.put("size",size +"");
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getOtherTripList, map, null);
		}

	}

	/**
	 * 获取行程详情
	 */
	public static void getTripInfo(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getTripInfo();
			url = url.replace("{id}", id);
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getTripInfo, null, null);
		}

	}

	/**
	 * 获取行程详情-费用详情
	 */
	public static void getTripInfoFee(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getTripInfoFee();
			url = url.replace("{id}", id);
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getTripInfoFee, null, null);
		}
	}

	/**
	 * G001  获取违章列表 GET
	 */
	public static void getIllegalList() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getIllegalList();
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getIllegalList, null, null);
		}
	}

	/**
	 * G002  获取违章详情列表 GET
	 */
	public static void getIllegalDetail(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getIllegalDetail();
				url = url.replace("{id}", id);
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.getIllegalDetail, null);
			} catch (Exception ex) {
				LogHelper.e("accounts", ex);
			}
		}
	}

	/**
	 * 获取活动列表
	 */
	public static void getActivities() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getActivities();
			Map params = new HashMap();
			params.put("sort", "publishTime,desc");
			// 发送请求
			HttpHelper.get(url, ModuleHttpApiKey.getActivities, params, null);
		}
	}


	/**
	 * 创建上门取车
	 *
	 * @param model
	 */
	public static void pickUpCar(PickUpModel model) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.pickUpCar();
				url = url.replace("{id}", String.valueOf(model.getId()));
				JSONObject json = new JSONObject();
				if (model.isPickuped()) {
					json.put("pickuped", model.isPickuped());
					json.put("address", model.getAddress());
					json.put("estimatedTime", model.getEstimatedTime());
					json.put("latitude", model.getLatitude());
					json.put("longitude", model.getLongitude());
					json.put("phone", model.getPhone());
					json.put("name", model.getName());
					json.put("returnLocationId", model.getReturnLocationId());
				}
				HttpHelper.put(url, ModuleHttpApiKey.pickUpCar, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("C001 创建预约订单", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.createOrder();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.pickUpCar, ModuleUrls.scheduledReservationOrders(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * 零钱支付
	 *
	 * @param id
	 * @param tickId
	 */
	public static void reservationOrdersWalletpay(int id, String tickId) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.walletpayUrl();
				url = url.replace("{id}", String.valueOf(id));
				Map<String,String> map = new HashMap<>();
				if(!"0".equals(tickId)){
					map.put("ticketId",tickId);
				}
				HttpHelper.post(url, ModuleHttpApiKey.reservationOrdersWalletpay, null, map,null);
			} catch (Exception e) {
				LogHelper.e("C001 创建预约订单", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.createOrder();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reservationOrdersWalletpay, ModuleUrls.scheduledReservationOrders(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * 申请退款
	 */
	public static void refund() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.refund();
			// 发送请求
			HttpHelper.put(url, ModuleHttpApiKey.refund);
		}

	}

	/**
	 * 新建发票
	 */
	public static void createReceipt(CreateReceiptModel mReceiptModel) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.createReceipt();
				// 发送请求
				HttpHelper.post(url, ModuleHttpApiKey.createReceipt, GsonImplHelp.get().toJson(mReceiptModel), null);
			} catch (Exception e) {
				LogHelper.e("E003 新建发票", e);
			}
		}
	}

	/**
	 * 是否有进行中订单
	 */
	public static void getHasProcess() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getHasProcess();
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.getHasProcess, null, null);
			} catch (Exception e) {
				LogHelper.e("E003 新建发票", e);
			}
		}
	}

	/**
	 * 认证
	 */
	public static void identify(PersonInfo personInfo) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.identify();
				// 发送请求
				HttpHelper.put(url, ModuleHttpApiKey.identify, GsonImplHelp.get().toJson(personInfo), null);
			} catch (Exception ex) {
				LogHelper.e("认证", ex);
			}
		}

	}

	/**
	 * 获取进行中行程
	 */
	public static void getProcess() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getProcess();
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.getProcess, null, null);
			} catch (Exception e) {
				LogHelper.e("获取进行中行程", e);
			}
		}
	}

	/**
	 * 获取送车上门费用
	 */
	public static void getDeliverCost(double originsLng, double originsLat, double destinationLng, double destinationLat) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getDeliverCost();
				HashMap<String, String> map = new HashMap<>();
				map.put("originsLng", String.valueOf(originsLng));
				map.put("originsLat", String.valueOf(originsLat));
				map.put("destinationLng", String.valueOf(destinationLng));
				map.put("destinationLat", String.valueOf(destinationLat));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.getDeliverCost, map, null);
			} catch (Exception e) {
				LogHelper.e("获取送车上门费用", e);
			}
		}
	}
	/**
	 * 获取上门取车费用
	 */
	public static void getPickupCost(double destinationLng, double destinationLat,double originsLng, double originsLat) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = ModuleUrls.getPickupCost();
				HashMap<String, String> map = new HashMap<>();
				map.put("originsLng", String.valueOf(originsLng));
				map.put("originsLat", String.valueOf(originsLat));
				map.put("destinationLng", String.valueOf(destinationLng));
				map.put("destinationLat", String.valueOf(destinationLat));
				// 发送请求
				HttpHelper.get(url, ModuleHttpApiKey.getPickupCost, map, null);
			} catch (Exception e) {
				LogHelper.e("获取上门取车费用", e);
			}
		}
	}

	/**
	 * 车辆计费详情
	 * @param carId
	 * @param orderTime
	 */
    public static void getRuleDetail(String carId, String orderTime) {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = ModuleUrls.getRuleDetail();
			url = url.replace("{id}", String.valueOf(carId));
			HashMap<String, String> map = new HashMap<>();
			if (!TextUtils.isEmpty(orderTime)) {
				map.put("orderTime", orderTime);
			}
			HttpHelper.get(url, ModuleHttpApiKey.getRuleDetail,map,null);
		}
    }

	public static void getActivitiesDetail(String dataBeanId) {

		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getActivitiesDetail();
			url = url.replace("{id}", ToolsHelper.toString(dataBeanId));
			Map<String,String> map = new HashMap<>();
			map.put("id",dataBeanId);
			HttpHelper.get(url, ModuleHttpApiKey.getActivitiesDetail,map,null);
		}
	}

    /**
     * 获取场站城市列表
     */
    public static void getLocationsCitys() {

        if (CoreHttpApi.IS_ONLINE) {
            String url = ModuleUrls.getLocationsCitys();
            HttpHelper.get(url, ModuleHttpApiKey.getLocationsCitys,null,null);
        }
    }

	/**
	 * 首页活动列表
	 */
	public static void getAdvertisements() {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getAdvertisements();
			HttpHelper.get(url, ModuleHttpApiKey.getAdvertisements,null,null);
		}
	}

	/**
	 * 根据城市获取充电站列表
	 * @param myLocation
	 * @param mCityCode
	 */
    public static void getChargingLocations(String key,LatLng myLocation, String mCityCode,int page,String name,long mile) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getChargingLocations();
			Map<String,String> map = new HashMap<>();
			map.put("s_longitude",String.valueOf(myLocation.longitude));
			map.put("s_latitude",String.valueOf(myLocation.latitude));
//			map.put("s_city",mCityCode);
			map.put("page",String.valueOf(page));
			map.put("size","10");
			if(!TextUtils.isEmpty(name)){
				map.put("s_name",name);
			}
			if(mile != 0){
				map.put("s_radius",mile + "");
			}

			HttpHelper.get(url,key,map,null);
		}
    }
	/**
	 * 根据城市获取充电站详情信息
	 */
    public static void getChargingLocationDetail(LatLng lt, int id) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getChargingLocationDetail();
			url = url.replace("{id}", ToolsHelper.toString(id));
			Map<String,String> map = new HashMap<>();
			map.put("s_longitude",String.valueOf(lt.longitude));
			map.put("s_latitude",String.valueOf(lt.latitude));
			HttpHelper.get(url, ModuleHttpApiKey.getChargingLocationDetail,map,null);
		}
    }

	/**
	 * 获取所有场站信息
	 * @param lt
	 */
	public static void chargingLocationsAll(LatLng lt) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getChargingLocationsAll();
			Map<String,String> map = new HashMap<>();
			map.put("s_longitude",String.valueOf(lt.longitude));
			map.put("s_latitude",String.valueOf(lt.latitude));
			map.put("s_radius",2000 +"");
			HttpHelper.get(url, ModuleHttpApiKey.getChargingLocationsAll,map,null);
		}
	}

    public static void zhiMaCredit() {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.zhiMaCredit();
			HttpHelper.get(url, ModuleHttpApiKey.zhiMaCredit,null,null);
		}
    }

    public static void getNearStations(String flag,String requestKey, LatLng lt, int page,int size, String searchContent, long searcheRange,String city) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getNearStations();
			Map<String,String> map = new HashMap<>();
			map.put("operationType",flag);
			map.put("s_longitude",String.valueOf(lt.longitude));
			map.put("s_latitude",String.valueOf(lt.latitude));
			if(searcheRange != 0){
				map.put("s_radius",searcheRange +"");
			}
			if(!TextUtils.isEmpty(city)){
				map.put("s_city",city);
			}
			map.put("page",page +"");
			map.put("size",size +"");
			if(!TextUtils.isEmpty(searchContent)){
				map.put("s_name",searchContent);
			}

			HttpHelper.get(url,requestKey,map,null);
		}
    }

	/**
	 * 修改还车场站
	 * @param returnLocationId 还车场站id
	 * @param orderId 订单id
	 */
    public static void changeReturnLocation(int returnLocationId, int orderId) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.changeReturnLocation();
			url = url.replace("{id}", ToolsHelper.toString(orderId));
			Map<String, String> map = new HashMap<>();
			map.put("returnLocationId", String.valueOf(returnLocationId));
			HttpHelper.put(url, ModuleHttpApiKey.changeReturnLocation, map, null);
		}
    }

	/**
	 * 注册用户
	 * @param model 用户数据model
	 */
	public static void registUser(RegistModel model) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				json.put("phone", model.getPhone());
				json.put("password", model.getPassword());
				json.put("confirmPassword", model.getConfirmPassword());
				json.put("noncePassword", model.getNoncePassword());
				HttpHelper.post(ModuleUrls.register(), ModuleHttpApiKey.regist, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("注册", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.password();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.regist, ModuleUrls.password(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}
	/**
	 * 密码找回
	 * @param model 用户数据model
	 */
	public static void findPassword(RegistModel model) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				json.put("phone", model.getPhone());
				json.put("password", model.getPassword());
				json.put("confirmPassword", model.getConfirmPassword());
				json.put("noncePassword", model.getNoncePassword());
				HttpHelper.post(ModuleUrls.forgetPassword(), ModuleHttpApiKey.reset, json.toString(), null);
			} catch (Exception e) {
				LogHelper.e("注册", e);
			}
		} else {
			String strResult = ModuleHttpApiResult.password();
			HttpResult httpResult = new HttpResult(ModuleHttpApiKey.reset, ModuleUrls.password(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * 获取申请列表
	 * @param page 页数
	 */
	public static void getApplyList(int page) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getApplyList();
			Map<String,String> map = new HashMap<>();
			map.put("page",page +"");
			map.put("size", "10");
			HttpHelper.post(url,ModuleHttpApiKey.getApplyList,null,map,null);
		}
	}

	/**
	 * 提交用车申请
	 * @param time
	 * @param descStr
	 * @param useCarstartTimeLong
	 * @param useCarendTimeLong
	 */
	public static void submitApplyForUserCar(long time, String descStr, long useCarstartTimeLong, long useCarendTimeLong) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.submitApplyForUserCar();
			JSONObject json = new JSONObject();
			try {
				json.put("userDate",time +"");
				json.put("description", descStr);
				json.put("approverName", CacheHelper.getApproverName());
				json.put("approverPhone", CacheHelper.getApproverPhone());
				json.put("approverId", Long.parseLong(CacheHelper.getApproverId()));
				json.put("startTime", useCarstartTimeLong);
				json.put("endTime", useCarendTimeLong);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			HttpHelper.post(url,ModuleHttpApiKey.submitApplyForUserCar,json.toString());
		}
	}

	public static void getMarkingList(int page) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getMarkingList();
			Map<String,String> map = new HashMap<>();
			map.put("page",page +"");
			map.put("size",10 +"");
			map.put("s_status","APPOVING,APPROVED,REJECTED");
			HttpHelper.post(url,ModuleHttpApiKey.getMarkingList,null,map,null);
		}
	}

	/**
	 * 获取审批详情
	 * @param id 申请Id
	 */
	public static void getMarkingDetail(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getMarkingDetail();
			url = url.replace("{id}",id);
			HttpHelper.get(url,ModuleHttpApiKey.getMarkingDetail);
		}
	}

	/**
	 * 审核通过
     * @param id
     */
	public static void markingPass(String id, String expectedPlateLicenseNo) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.markingPass();
			url = url.replace("{id}",id);
			JSONObject json = new JSONObject();
			try {
				json.put("expectedPlateLicenseNo",expectedPlateLicenseNo);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			HttpHelper.put(url,ModuleHttpApiKey.markingPass,json.toString());
		}
	}
	/**
	 * 审核拒绝
	 * @param id
	 */
	public static void markingRefuse(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.markingRefuse();
			url = url.replace("{id}",id);
			HttpHelper.put(url,ModuleHttpApiKey.markingRefuse);
		}
	}

	/**
	 * 检查用车申请
	 * @param
	 */
	public static void checkUseApply(long useData) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.checkUseApply();
			Map<String,String> map = new HashMap<>();
			map.put("userDate",useData+"");
			HttpHelper.get(url,ModuleHttpApiKey.checkUseApply,map,null);
		}

	}

	/**
	 * 获取当前用户审批人信息
	 */
	public static void getApprover() {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getApprover();
			HttpHelper.get(url,ModuleHttpApiKey.getApprover);
		}
	}

	/**
	 * 撤销用车申请
	 * @param id
	 */
    public static void cancelApply(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.cancelApply();
			url = url.replace("{id}",id);
			HttpHelper.put(url,ModuleHttpApiKey.cancelApply);
		}
    }

	public static void getDepartmentBalance() {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getDepartmentBalance();
//			url = url.replace("{id}",id);
			HttpHelper.get(url,ModuleHttpApiKey.getDepartmentBalance);
		}
	}

    public static void getDepartmentCars() {
		if (CoreHttpApi.IS_ONLINE) {
			String url = ModuleUrls.getDepartmentCars();
			HttpHelper.get(url,ModuleHttpApiKey.getDepartmentCars);
		}
    }


    // ----------------------------- 其他 end -----------------------------
}
