package cn.lds.chatcore.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import cn.lds.chatcore.MyApplication;

/**
 * 获取设备硬件信息
 *
 * @author xuyongquan
 */
public class DeviceHelper {
	/**
	 * 获取设备编号
	 *
	 * @return
	 */
	public static String getDeviceId() {
		String imei = null;
		try {
			TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
			if (imei == null) {
				// android pad
				imei = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
			}
		} catch (Exception ex) {
			LogHelper.e("获取设备编号", ex);
		}
		return ToolsHelper.toString(imei);
	}

	/**
	 * 获取设备操作系统版本
	 *
	 * @return
	 */
	public static String getOsVer() {
		try {
			return android.os.Build.VERSION.RELEASE;
		} catch (Exception ex) {
			LogHelper.e("获取设备操作系统版本", ex);
		}
		return "";
	}

	/**
	 * 获取设备API LEVEL
	 *
	 * @return
	 */
	public static int getApiLevel() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 是否需要动态获取权限
	 *
	 * @return
	 */
	public static boolean needCheckPermission() {
		int k = getApiLevel();
		if (k >= 23) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取设备名称
	 *
	 * @return
	 */
	public static String getDeviceName() {
		try {
			return android.os.Build.MANUFACTURER;
		} catch (Exception ex) {
			LogHelper.e("获取设备名称", ex);
		}
		return "";
	}

	/**
	 * 获取设备型号, e.g: GT-I9100
	 *
	 * @return the user_Agent
	 */
	public static String getDeviceModel() {
		try {
			return android.os.Build.MODEL;
		} catch (Exception ex) {
			LogHelper.e("获取手机名称", ex);
		}
		return "";
	}


	/**
	 * 获取设备厂家名称 e.g: Samsung
	 *
	 * @return the vENDOR
	 */
	public static String getDeviceVendor() {
		return Build.BRAND;
	}


	/**
	 * 获取软件版本名称
	 *
	 * @return
	 */
	public static String getSoftwareVersion() {
		String version = "";
		try {
			PackageManager manager = MyApplication.getInstance().getPackageManager();
			PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
			version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
	/**
	 * 获取软件版本号
	 *
	 * @return
	 */
	public static int getSoftwareVersionCode(Context context) {
		int version = 0;
		try {
			PackageManager manager = MyApplication.getInstance().getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			version = info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
}
