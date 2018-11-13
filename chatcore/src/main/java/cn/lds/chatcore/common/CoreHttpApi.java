package cn.lds.chatcore.common;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginRequestModel;
import cn.lds.chatcore.data.OrderPayModel;
import cn.lds.chatcore.enums.FileType;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.im.sdk.enums.DeviceType;
import cn.lds.im.sdk.enums.OsType;
import de.greenrobot.event.EventBus;

/**
 * Created by geese on 2015/11/25.
 */
public class CoreHttpApi {
	public static final boolean IS_ONLINE = true;

	//公用begin
	public static void getList(String url) {
		HttpHelper.get(url, CoreHttpApiKey.UNKNOW);
	}

	/**
	 * 注销时，通知服务器注销IM
	 */
	public static void unregister(Context context) {

		// 构建URL
		String url = Constants.getCoreUrls().UNREGISTER_CONFIG_SERVER_URL();
		// 准备参数
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 构建JSON
		String strJSON = "{";
//		strJSON += "\"token\":\"" + CacheHelper.getImToken() + "\"";
		strJSON += ",\"clientId\":\"" + CacheHelper.getAccount() + "\"";
		strJSON += ",\"deviceId\":\"" + DeviceHelper.getDeviceId() + "\"";
		strJSON += ",\"deviceType\":" + DeviceType.PHONE.getValue() + "";
		strJSON += ",\"osType\":" + OsType.ANDROID.getValue() + "";
		strJSON += "}";
		// 发送请求
		HttpHelper.post(url, "unregister", strJSON);
	}

	/**
	 * 连接IM时，注册IM
	 */
	public static void registerDevice() {
		// 构建URL
		String url = Constants.getCoreUrls().CONFIG_SERVER_URL();
		// 构建JSON
		String strJSON = "{";
		strJSON += "\"clientId\":\"" + CacheHelper.getAccount() + "\"";
		strJSON += ",\"deviceId\":\"" + DeviceHelper.getDeviceId() + "\"";
		strJSON += ",\"deviceType\":" + DeviceType.PHONE.getValue() + "";
		strJSON += ",\"osType\":" + OsType.ANDROID.getValue() + "";
		strJSON += "}";
		// 发送请求
		HttpHelper.post(url, CoreHttpApiKey.CONFIG_SERVER_URL, strJSON);
	}


	/**
	 * 下载文件
	 *
	 * @param url
	 */
	public static void fileDownload(String fileStorageId, String url, FileType fileType, final String owner) {
		HttpHelper.download(fileStorageId, url, fileType, owner, null);
	}

	/**
	 * 下载文件 自定义名称
	 *
	 * @param url
	 */
	public static void fileDownload(String fileName, String fileStorageId, String url, FileType fileType, final String owner) {
		HttpHelper.download(fileStorageId, url, fileType, owner, fileName);
	}

	public static void fileUpload(String path, Map<String, String> extras) {
		LogHelper.d("upload>>>> invoke fileUpload api");
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = Constants.getCoreUrls().getUploadUrl();
			HttpHelper.upload(url, path, extras);
		}
	}

	public static void fileUpload(String uploadUrl, String path, Map<String, String> extras) {
		LogHelper.d("upload>>>> invoke fileUpload api");
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
//            String url = Constants.getCoreUrls().CONFIG_SERVER_URL();
			HttpHelper.upload(uploadUrl, path, extras);
		}
	}


	/**
	 * A001: 登录
	 */
	public static void login(LoginRequestModel loginRequestModel, boolean background) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				JSONObject json = new JSONObject();
				json.put("deviceId", loginRequestModel.getDeviceId());
				json.put("deviceType", "mobile");
				json.put("osType", loginRequestModel.getOsType());
				json.put("osVersion", loginRequestModel.getOsVersion());
				json.put("softwareType", "app");
				json.put("softwareVersion", DeviceHelper.getSoftwareVersion());
				Map<String, String> extras = new HashMap<>();
				extras.put("loginType", loginRequestModel.getLoginType().value());
				extras.put("loginId", loginRequestModel.getLoginId());
				extras.put("background", String.valueOf(background));
				switch (loginRequestModel.getLoginType()) {
					// mobile+短信验证码登录
					case mobile_captcha:
						json.put("username", loginRequestModel.getUsername());
						json.put("password", loginRequestModel.getPassword());
						HttpHelper.post(Constants.getCoreUrls().loginCode(), CoreHttpApiKey.login, json.toString(), extras);
						break;
					// 一次性token自动登录
					case nonceToken:
						if (ToolsHelper.isNull(loginRequestModel.getNonceToken())) {
							return;
						}
						json.put("nonceToken", loginRequestModel.getNonceToken());
						HttpHelper.post(Constants.getCoreUrls().login(), CoreHttpApiKey.login, json.toString(), extras);
						break;
					// 手机号 + 密码登录
					case mobile_pass:
						json.put("username", loginRequestModel.getUsername());
						json.put("password", loginRequestModel.getPassword());
						HttpHelper.post(Constants.getCoreUrls().login(), CoreHttpApiKey.login, json.toString(), extras);
						break;
				}


//				HttpHelper.post(Constants.getCoreUrls().login(), CoreHttpApiKey.login, json.toString(), extras);
			} catch (Exception e) {
				LogHelper.e("A001: 登录", e);
			}
		} else {
			String strResult = CoreHttpApiResult.login();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.login, Constants.getCoreUrls().login(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}


	/**
	 * A003: 退出
	 */
	public static void logout() {
		if (CoreHttpApi.IS_ONLINE) {
			// 构建URL
			String url = Constants.getCoreUrls().logout();
			// 发送请求
			HttpHelper.get(url, CoreHttpApiKey.logout);
		} else {
			String strResult = CoreHttpApiResult.logout();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.logout, Constants.getCoreUrls().logout(),
					strResult, null);

			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	public static void ping() {
		// 构建URL
		String url = Constants.getCoreUrls().ping();
		HttpHelper.get(url, CoreHttpApiKey.ping);
	}


	/**
	 * 获取个人信息（GET）
	 * URL:http://localhost:8888/m/enrollees
	 */
	public static void enrolleesGet() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().enrolleesGet();
				// 发送请求
				HttpHelper.get(url, CoreHttpApiKey.enrolleesGet, null);
			} catch (Exception ex) {
				LogHelper.e("获取个人信息", ex);
			}
		} else {
			String strResult = CoreHttpApiResult.enrolleesGet();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.enrolleesGet, Constants.getCoreUrls().enrolleesGet(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * 获取个人信息（GET）
	 * URL:http://localhost:8888/m/enrollees
	 */
	public static void enrolleesGet(Map<String, String> extras) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().enrolleesGet();
				// 发送请求
				HttpHelper.get(url, CoreHttpApiKey.enrolleesGet, extras);
			} catch (Exception ex) {
				LogHelper.e("获取个人信息", ex);
			}
		} else {
			String strResult = CoreHttpApiResult.enrolleesGet();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.enrolleesGet, Constants.getCoreUrls().enrolleesGet(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}

	}

	/**
	 * C013 获取微信支付订单详情(POST)
	 * URL:http://localhost:8888/m/reservationOrders/{id}/pay
	 */
	public static void reservationOrdersWxPay(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().reservationOrdersWxPay();
				url = url.replace("{id}", ToolsHelper.toString(id));
				HttpHelper.post(url, CoreHttpApiKey.reservationOrdersWxPay, null);
			} catch (Exception ex) {
				LogHelper.e("C013 订单支付(POST)", ex);
			}
		}
	}

	/**
	 * C013 获取微信支付订单详情(POST)
	 * URL:http://localhost:8888/m/reservationOrders/{id}/pay
	 */
	public static void payment(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().payment();
				url = url.replace("{businessId}", ToolsHelper.toString(id));
				HttpHelper.get(url, CoreHttpApiKey.payment);
			} catch (Exception ex) {
				LogHelper.e("获取支付结果", ex);
			}
		}
	}

	/**
	 * C013 获取支付宝支付订单详情(POST)
	 * URL:http://localhost:8888/m/reservationOrders/{id}/pay
	 */
	public static void reservationOrdersAlipay(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().reservationOrdersAlipay();
				url = url.replace("{id}", ToolsHelper.toString(id));
				HttpHelper.post(url, CoreHttpApiKey.reservationOrdersAlipay, null);
			} catch (Exception ex) {
				LogHelper.e("C013 订单支付(POST)", ex);
			}
		}
	}

	public static void reservationOrdersWalletpay(String id) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().reservationOrdersWalletpay();
				url = url.replace("{id}", ToolsHelper.toString(id));
				HttpHelper.post(url, CoreHttpApiKey.reservationOrdersWalletpay, null);
			} catch (Exception ex) {
				LogHelper.e("C013 订单支付(POST)", ex);
			}
		}
	}

	public static void foregiftAccountsWalletpay() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().foregiftAccountsWalletpay();
				// 发送请求
				HttpHelper.post(url, CoreHttpApiKey.foregiftAccountsWalletpay, null);
			} catch (Exception ex) {
				LogHelper.e("余额定金支付", ex);
			}
		} else {
			String strResult = CoreHttpApiResult.foregiftAccounts();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.foregiftAccountsAlipay, Constants.getCoreUrls().foregiftAccountsAlipay(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * 更新
	 */
	public static void checkVersions() {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().checkVersions();
				// 发送请求
				HttpHelper.get(url, CoreHttpApiKey.checkVersions);
			} catch (Exception ex) {
				LogHelper.e("checkVersions", ex);
			}
		} else {
		}
	}


	/**
	 * 获取上传路径
	 *
	 * @param extras
	 */
	public static void getUploadUrl(Map<String, String> extras) {
		LogHelper.d("upload>>>> invoke getUploadURL api");
		if (CoreHttpApi.IS_ONLINE) {
			HttpHelper.get(Constants.getCoreUrls().getUploadUrl(), CoreHttpApiKey.getUploadUrl, extras);
		} else {
			LogHelper.d("upload>>>> getUploadURL api mock result");
			String strResult = CoreHttpApiResult.getUploadUrl();
			LogHelper.d("upload>>>> mock result: \n" + strResult);
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.getUploadUrl, Constants.getCoreUrls().getUploadUrl
					(), strResult);

			httpResult.setExtras(extras);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * 获取系统配置
	 */
	public static void getSystemConfig() {
		if (CoreHttpApi.IS_ONLINE) {
			HttpHelper.get(Constants.getCoreUrls().getSystemConfig(), CoreHttpApiKey.getSystemConfig);
		} else {
		}
	}


	//----------------------最新分时租赁接口-----------------------------

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
	public static void accountsWxPay(OrderPayModel mOrderModel) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().accountsWxPay();

				// 发送请求
				HttpHelper.post(url, CoreHttpApiKey.accountsWxPay, GsonImplHelp.get().toJson(mOrderModel), null);
			} catch (Exception ex) {
				LogHelper.e("accounts", ex);
			}
		} else {
			String strResult = CoreHttpApiResult.accounts();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.accountsWxPay, Constants.getCoreUrls().accountsWxPay(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}

	/**
	 * 27.押金充值（POST）
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
	/**
	 * 微信支付
	 *
	 * @param mOrderModel
	 */
	public static void foregiftAccountsWxPay(OrderPayModel mOrderModel) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().foregiftAccountsWxPay();
				// 发送请求
				HttpHelper.post(url, CoreHttpApiKey.foregiftAccountsWxPay, GsonImplHelp.get().toJson(mOrderModel), null);
			} catch (Exception ex) {
				LogHelper.e("押金支付", ex);
			}
		} else {
			String strResult = CoreHttpApiResult.foregiftAccounts();
			HttpResult httpResult = new HttpResult(CoreHttpApiKey.foregiftAccountsWxPay, Constants.getCoreUrls().foregiftAccountsAlipay(), strResult,
					null);
			httpResult.setExtras(null);
			EventBus.getDefault().post(new HttpRequestEvent(httpResult));
		}
	}
    /**
     * 获取微信支付订单详情(POST)
     */
    public static void reservationOrdersWxPay(OrderPayModel mOrderModel) {
        if (CoreHttpApi.IS_ONLINE) {
            try {
                // 构建URL
                String url = Constants.getCoreUrls().foregiftAccountsWxPay();
                // 发送请求
                HttpHelper.post(url, CoreHttpApiKey.reservationOrdersWxPay, GsonImplHelp.get().toJson(mOrderModel),null);
            } catch (Exception ex) {
                LogHelper.e("订单支付", ex);
            }
        }
    }
    /**
     * 支付宝
     * @param mOrderModel
     */
    public static void reservationOrdersAlipay(OrderPayModel mOrderModel) {
        if (CoreHttpApi.IS_ONLINE) {
            try {
                // 构建URL
                String url = Constants.getCoreUrls().foregiftAccountsAlipay();
                // 发送请求
                HttpHelper.post(url, CoreHttpApiKey.reservationOrdersAlipay, GsonImplHelp.get().toJson(mOrderModel),null);
            } catch (Exception ex) {
                LogHelper.e("订单支付", ex);
            }
        } else {
            String strResult = CoreHttpApiResult.foregiftAccounts();
            HttpResult httpResult = new HttpResult(CoreHttpApiKey.foregiftAccountsAlipay, Constants.getCoreUrls().foregiftAccountsAlipay(), strResult,
                    null);
            httpResult.setExtras(null);
            EventBus.getDefault().post(new HttpRequestEvent(httpResult));
        }
    }

	/**
	 * 支付宝
	 *
	 * @param mOrderModel
	 */
	public static void foregiftAccountsAlipay(OrderPayModel mOrderModel) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().foregiftAccountsAlipay();
				// 发送请求
				HttpHelper.post(url, CoreHttpApiKey.foregiftAccountsAlipay, GsonImplHelp.get().toJson(mOrderModel), null);
			} catch (Exception ex) {
				LogHelper.e("押金金支付", ex);
			}
		}
	}

	public static void accountsAlipay(OrderPayModel mOrderModel) {
		if (CoreHttpApi.IS_ONLINE) {
			try {
				// 构建URL
				String url = Constants.getCoreUrls().accountsAlipay();
				// 发送请求
				HttpHelper.post(url, CoreHttpApiKey.accountsAlipay, GsonImplHelp.get().toJson(mOrderModel), null);
			} catch (Exception ex) {
				LogHelper.e("accounts", ex);
			}
		}
	}

}
