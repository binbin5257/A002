package cn.lds.chatcore.common;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import cn.lds.chatcore.MyApplication;

/**
 * Created by quwei on 2016/3/14.
 */
public class AppHelper {
    private static PackageManager manager;
    private static PackageInfo info;
    private static PackageInfo getPackageInfo(){
        try {
            if (manager == null) {
                manager = MyApplication.getInstance().getPackageManager();
            }
            if (info == null) {
                info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            }
        }catch (Exception ex){

        }
        return info;
    }

    /**
     * 获取应用的版本名称
     *
     * @return 应用的版本名称
     */
    public static String getVersionName() {
        try {
            return getPackageInfo().versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用的版本号
     */
    public static int getVersionCode() {
        try {
            return getPackageInfo().versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

//    /**
//     * 获取应用的包名
//     * TODO:新的修改包名方式。导致manfiest文件中的package不会被修改。
//     * @return 应用的包名
//     */
//    public static String getPackageName() {
//        try {
//            return getPackageInfo().packageName;
//        } catch (Exception e) {
//            return "";
//        }
//    }
}
