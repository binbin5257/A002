package cn.lds.im.common.permission;

import android.app.Activity;

/**
 * Created by quwei on 2016/4/12.
 */
public class PermissionHelper {
    private static String TAG = PermissionHelper.class.getSimpleName();

    /**
     * 检查权限
     * @param context
     * @param permissions
     */
    public static void checkPermission(Activity context,String... permissions) {
//        try {
//            if(DeviceHelper.needCheckPermission()){
//                LogHelper.d("系统版本>=23，执行动态权限检查");
//                EasyPermission easyPermission = new EasyPermission.Builder(context, new PermissionResultCallback() {
//                    @Override
//                    public void onPermissionGranted(List<String> grantedPermissions) {
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        MyApplication.getInstance().sendLogoutBroadcast("youth.loginactivity");
//                    }
//                })
//                .rationalMessage("获取授权失败！应用的主要功能无法使用。请在应用的“权限管理”中重新授权！")
//                .deniedMessage("获取授权失败！应用的主要功能无法使用。请在应用的“权限管理”中重新授权！")
//                .permission(permissions)
//                .settingBtn(true)
//                .build();
//                easyPermission.check();
//            }else{
//                LogHelper.d("系统版本<23，不执行动态权限检查");
//            }
//
//        }catch (Exception ex){
//            LogHelper.e(TAG, ex);
//        }
    }

    /**
     * 检查权限
     * @param context
     * @param permissions
     */
    public static void checkPermission(Activity context,PermissionResultCallback resultCallback,String... permissions) {
//        try {
//            if(DeviceHelper.needCheckPermission()){
//                LogHelper.d("系统版本>=23，执行动态权限检查");
//                EasyPermission easyPermission = new EasyPermission.Builder(context, new PermissionResultCallback() {
//                    @Override
//                    public void onPermissionGranted(List<String> grantedPermissions) {
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        MyApplication.getInstance().sendLogoutBroadcast("youth.loginactivity");
//                    }
//                })
//                .rationalMessage("获取授权失败！应用的主要功能无法使用。请在应用的“权限管理”中重新授权！")
//                .deniedMessage("获取授权失败！应用的主要功能无法使用。请在应用的“权限管理”中重新授权！")
//                .permission(permissions)
//                .settingBtn(true)
//                .build();
//                easyPermission.setResultCallback(resultCallback);
//                easyPermission.check();
//            }else{
//                LogHelper.d("系统版本<23，不执行动态权限检查");
//            }
//
//        }catch (Exception ex){
//            LogHelper.e(TAG, ex);
//        }
    }
}
