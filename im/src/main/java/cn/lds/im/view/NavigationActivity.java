package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.data.OrderInfoModel;

/**
 * 导航页面
 * Created by sibinbin on 2017/7/18.
 */

public class NavigationActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回按钮
     */
    private Button backBtn;
    /**
     * 页面标题
     */
    private TextView titleTv;

    boolean useDefaultIcon = false;

    protected boolean isFirstLoc = true;// 是否首次定位


    protected BaiduMapHelper baiduMapHelper;
    protected MapView mMapView;
    protected BaiduMap mBaiduMap;
    private LatLng end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            double locationLatitude = bundle.getDouble("locationLatitude");
            double locationLongitude = bundle.getDouble("locationLongitude");
            // 坐标
            end = new LatLng(locationLatitude,locationLongitude);
        }
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.top_back_btn);
        titleTv = (TextView) findViewById(R.id.top_title_tv);
        FrameLayout loacitonmapview = (FrameLayout) findViewById(R.id.loacitonmapview);

        titleTv.setText("导航");
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        //初始化视图
        baiduMapHelper = new BaiduMapHelper(this);
        mMapView = baiduMapHelper.getMapView();
        loacitonmapview.addView(mMapView, 0);
        mBaiduMap = mMapView.getMap();
        baiduMapHelper.initLocation(bdLocationListener);



    }

    private void initPlanDriverLoad() {
        baiduMapHelper.initRoutePlan(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if(drivingRouteResult.error.name().equals("RESULT_NOT_FOUND")){
                    Toast.makeText(NavigationActivity.this,"没有找到合理的路线",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 错误号可参考MKEvent中的定义
                MyDrivingRouteOverlay myDrivingRouteOverlay = new MyDrivingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(myDrivingRouteOverlay);
                myDrivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(0));//设置线路为搜索结果的第一条
                myDrivingRouteOverlay.addToMap();
                myDrivingRouteOverlay.zoomToSpan();
//                int duration = drivingRouteResult.getRouteLines().get(0).getDistance();
//                Toast.makeText(NavigationActivity.this,"距离是:"+duration+"米",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
        baiduMapHelper.searchRoutePlanNoVoice(MyApplication.myLocation,end,2);
    }


    // 定制RouteOverly
    protected class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (!useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (!useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                finish();
                break;
        }
    }

    /**
     * 获取当前位置坐标
     */
    protected BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            //实时更新，定位信息
            MyApplication.myLocation = new LatLng(location.getLatitude(), location.getLongitude());

            if (isFirstLoc) {
                baiduMapHelper.setCenter(MyApplication.myLocation);
                isFirstLoc = false;
                String city = location.getCity();
                if (null != city) {
                    MyApplication.lastCity = city.split(getString(R.string.locatedactivity_city))[0];
                }

                initPlanDriverLoad();
            }
        }
    };


}
