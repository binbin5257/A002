package cn.lds.chatcore.common;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 位置 工具类
 * Created by zhoupeng on 2016/1/23.
 */
public class LocationHelper {

    private static LocationHelper defaultInstance;
    //设置定位条件
    LocationClientOption option;
    private LocationClient mLocClient;
    private LocationListener locationListener;
    public static LocationListener listener;

    private LocationHelper(Context context) {
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(new MyLocationListenner());

        //设置定位条件
        option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        option.setScanSpan(5000);    //设置定时定位的时间间隔。单位毫秒
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
    }

    public static LocationHelper getDefault(Context context) {

        if (defaultInstance == null) {
            synchronized (LocationHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LocationHelper(context);
                }
            }
        }
        return defaultInstance;
    }

    public LocationHelper start() {
//        LoadingDialog.showDialog(context, "地图定位中，请稍后");
        mLocClient.start();
        return this;
    }

    public boolean isStarted() {
        return mLocClient.isStarted();
    }

    public void stop() {
        mLocClient.stop();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
//            if (location == null)
//                return;
//            LoadingDialog.dismissDialog();

            locationListener.onReceiveLocation(location);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public interface LocationListener {
        void onReceiveLocation(BDLocation location);
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

}
