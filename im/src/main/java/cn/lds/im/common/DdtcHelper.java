package cn.lds.im.common;

import android.app.Activity;
import android.content.Context;

import com.ddtc.tobsdk.DdtcToBScanOperModel;

import java.lang.ref.WeakReference;

import cn.lds.chatcore.common.HttpHelper;

/**
 * 丁丁停车 SDK 帮助类
 * Created by sibinbin on 18-4-20.
 */

public class DdtcHelper {

    private static DdtcHelper instance;
    private DdtcToBScanOperModel mDdtcToBScanOperModel;
    private String mToken;
    private String address;
    private String lockId;

    private DdtcHelper(){}
    public static DdtcHelper getInstance(){
        if(instance == null){
            synchronized (DdtcHelper.class){
                if(instance == null){
                    instance = new DdtcHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 获取accessToken
     */
    public void getAccessToken(String appId,String appSecret){
        String url = ModuleUrls.getDdtcAccessToken().replace("{appId}",appId).replace("{appSecret}",appSecret);
        HttpHelper.get(url,ModuleHttpApiKey.getAccessToken);
    }

    /**
     * 扫描设备
     */
    public void initBle( Activity activity, final DdtcToBScanOperModel.OperType type){
        mDdtcToBScanOperModel = new DdtcToBScanOperModel(new WeakReference<Activity>(activity));
        mDdtcToBScanOperModel.setDdtcBleScanListener(new DdtcToBScanOperModel.DdtcBleScanListener() {
            @Override
            public void onSpecDeviceFind( String address,int rssi ) {
                mDdtcToBScanOperModel.oper(mToken, address, type);
            }

        });

        mDdtcToBScanOperModel.setDdtcBleOperModelListener(new DdtcToBScanOperModel.DdtcBleOperModelListener() {
            @Override
            public void onOperFailed( final String reason, final String rssi) {

            }

            @Override
            public void onOperSuccess(String battery, String type) {

            }

        });

    }




}
