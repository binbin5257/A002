package cn.lds.im.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.RevertCarModel;
import cn.lds.im.enums.BaseOrdersProcessType;
import cn.lds.im.enums.TripStatus;
import cn.lds.im.view.CarInUseActivity;
import cn.lds.im.view.ChargingRuleActivity;
import cn.lds.im.view.ConfirmOrderApponitActivity;
import cn.lds.im.view.NavigationActivity;
import cn.lds.im.view.OrderCancelledActivity;
import cn.lds.im.view.OrderDetailActivity;
import cn.lds.im.view.TakeCarActivity;
import cn.lds.im.view.TripInfoActivity;

/**
 * Created by Leadingsoft on 2017/8/7.
 */
public class IntentHelper {


    public static void intentTripList(Context mContext, Intent mIntent, int id, String status, String type, boolean isDelivered, boolean scheduledTimeUp) {
        switch (status) {
            case TripStatus.OPEN://刚创建
                if (isDelivered) {//送车上门
//                    mIntent.setClass(mContext, TakeApponitCarActivity.class);
                    mContext.startActivity(mIntent);
                } else {
                    mIntent.setClass(mContext, ConfirmOrderApponitActivity.class);
                    mContext.startActivity(mIntent);
                }
                break;
            case TripStatus.ALLOCATED://已分配
                if (BaseOrdersProcessType.SCHEDULED.name().equals(type) && !scheduledTimeUp) {//预约租车，是否到达预约取车时间 ,
                    if (isDelivered) {
//                        mIntent.setClass(mContext, TakeApponitCarActivity.class);
                        mContext.startActivity(mIntent);
                    } else {
                        mIntent.setClass(mContext, ConfirmOrderApponitActivity.class);
                        mContext.startActivity(mIntent);
                    }
                    return;
                }
                if (isDelivered) {//送车上门
//                    mIntent.setClass(mContext, TakeApponitCarActivity.class);
                    mContext.startActivity(mIntent);
                } else {//自己取车
                    mIntent.setClass(mContext, TakeCarActivity.class);
                    mContext.startActivity(mIntent);
                }
                break;
            case TripStatus.PICKED_UP://已取车
                mIntent.setClass(mContext, CarInUseActivity.class);
                mContext.startActivity(mIntent);
                break;
            case TripStatus.DROPPED_OFF://已还车
                mIntent.setClass(mContext, OrderDetailActivity.class);
                mContext.startActivity(mIntent);
                break;
            case TripStatus.RETURN_OVERDUE://逾期未还车
                mIntent.setClass(mContext, CarInUseActivity.class);
                mContext.startActivity(mIntent);
                break;
            case TripStatus.CANCELLED://已取消
                mIntent.setClass(mContext, OrderCancelledActivity.class);
                mIntent.putExtra("id", id);
                mContext.startActivity(mIntent);
                break;
            case TripStatus.PAID://已支付
                mIntent.setClass(mContext, TripInfoActivity.class);
                mIntent.putExtra("id", id);
                mContext.startActivity(mIntent);
                break;
            case TripStatus.PAYMENT_OVERDUE://逾期未支付
                mIntent.setClass(mContext, OrderDetailActivity.class);
                mContext.startActivity(mIntent);
                break;
        }
    }

    public static void intentChargingRule(Context mContext, int id, String reservationTime) {
        Intent intent2 = new Intent(mContext, ChargingRuleActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("id", id);
        bundle2.putString("orderTime", reservationTime);
        intent2.putExtras(bundle2);
        mContext.startActivity(intent2);


    }

    public static void intentMap(Context mContext, double longitude, double latitude,boolean isDriving) {
        String city = "北京";
        if (!ToolsHelper.isNull(MyApplication.lastCity)) {
            city = MyApplication.lastCity;
        }
        try {
            String str = "";

            String mode = "driving";
            if (isDriving) {
                mode = "driving";
            } else {
                mode = "walking";
            }
            //调起百度地图客户端
            str = "intent://map/direction?origin=" + MyApplication.myLocation.latitude + ","
                    + MyApplication.myLocation.longitude
                    + "&destination=" + latitude + ","
                    + longitude + "&mode=" + mode + "&region="
                    + city + "&src=" + mContext.getString(cn.lds.chatcore.R.string.app_name) + "|" + AppIndependentConfigure.SYS_CONFIG_APP_PACKAGE + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
            Intent intent = Intent.getIntent(str);
            try {
                mContext.startActivity(intent); //启动调用
            } catch (Exception e) {
                LogHelper.e("打开地图导航", e);
                Intent intent2 = new Intent(mContext, NavigationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("locationLatitude", latitude);
                bundle.putDouble("locationLongitude", longitude);
                intent2.putExtras(bundle);
                mContext.startActivity(intent2);
            }

        } catch (Exception ex) {
            LogHelper.e("", ex);
        }
    }
}
