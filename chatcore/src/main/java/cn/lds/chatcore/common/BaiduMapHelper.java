package cn.lds.chatcore.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.platform.comapi.location.CoordinateType;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.view.BNDemoGuideActivity;

/**
 * Created by Administrator on 2016/4/18.
 */
public class BaiduMapHelper {

    public static final int TYPE_LOCATED = 0;//定位-发送所在位置
    public static final int TYPE_ROUTED = 1;//导航-目标地点
    public static final String SNAPSHOT = "snapshot";


    private Context mActivity;
    //地图初始化的放大倍数
    private final int scale = 13;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private Marker marker;
    private LocationClient mLocClient;
    private PoiSearch mPoiSearch;
    private RoutePlanSearch mSearching;
    private GeoCoder mSearch;
    private BitmapDescriptor bdA;


    public BaiduMapHelper(Context context) {
        mActivity = context;
        buildMap();
    }
    public BaiduMapHelper(Context context,boolean isScroll) {
        mActivity = context;
        if(isScroll){
            buildMap();
        }else{
            buildCanNoScrollMap();
        }

    }

    private void buildMap() {
        BaiduMapOptions options = new BaiduMapOptions()
                .compassEnabled(false)
                .scaleControlEnabled(false)
                .rotateGesturesEnabled(false)
                .overlookingGesturesEnabled(false)
                .zoomControlsEnabled(false)
                .mapStatus(new MapStatus.Builder().zoom(scale).build());
        mMapView = new MapView(mActivity, options);
        mBaiduMap = mMapView.getMap();
        bdA = BitmapDescriptorFactory.fromResource(R.drawable.location_mark);
    }
    private void buildCanNoScrollMap() {
        BaiduMapOptions options = new BaiduMapOptions()
                .compassEnabled(false)
                .scaleControlEnabled(false)
                .rotateGesturesEnabled(false)
                .overlookingGesturesEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(true)
                .scrollGesturesEnabled(true)
                .mapStatus(new MapStatus.Builder().zoom(18).build());
        mMapView = new MapView(mActivity, options);
        mBaiduMap = mMapView.getMap();
        bdA = BitmapDescriptorFactory.fromResource(R.drawable.location_mark);
    }

    public MapView getMapView() {
        return mMapView;
    }

    /**
     * 初始化定位当前位置；
     */
    public void initLocation(BDLocationListener myListener) {
        try {
            mBaiduMap.setMyLocationEnabled(true);
            mLocClient = new LocationClient(mActivity);
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setOpenGps(true);
            option.setCoorType("bd09ll");
            option.setScanSpan(SystemConfig.SYS_CONFIG_MAP_LOCATIONSCANSAPN);// 10秒定位一次
            option.setIsNeedAddress(true);
            option.setNeedDeviceDirect(true);
            option.setIsNeedLocationDescribe(true);
            mLocClient.setLocOption(option);
            mLocClient.start();

        } catch (Exception ex) {
        }
    }


    /**
     * 兴趣点搜索初始化
     */
    public void initPoiSearch(OnGetPoiSearchResultListener onGetPoiSearchResultListener) {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
    }

    /**
     * 获取目标位置周边地点建筑
     */
    public void initPositionPoi(LatLng latLng, OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
        // 初始化搜索模块，注册事件监听
        if (null == mSearch) {
            mSearch = GeoCoder.newInstance();
        }
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        mSearch.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
    }

    /**
     * 兴趣点搜索
     *
     * @param ptCenter 当前位置
     * @param keyword  关键词
     */
    public void searchPoi(LatLng ptCenter, String keyword) {
        mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(keyword).pageCapacity(20)
                .pageNum(0).radius(5000).location(ptCenter));
    }


    /**
     * 路径规划初始化
     */
    public void initRoutePlan(OnGetRoutePlanResultListener onGetRoutePlanResultListener) {
        mSearching = RoutePlanSearch.newInstance();
        mSearching.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
    }

    // 路线相关
    private RouteLine route = null;
    private PlanNode stNode, enNode;
    /**
     * 路径规划初始化不带语音导航
     *
     * @param start 开始位置
     * @param end   结束位置
     * @param plan  出行方式   1 公共交通；  2 语音导航/自驾 ；3 步行
     */
    public void searchRoutePlanNoVoice(final LatLng start, final LatLng end, int plan) {
        stNode = PlanNode.withLocation(start);
        enNode = PlanNode.withLocation(end);


        switch (plan) {
            case 1:
                String city = MyApplication.lastCity;
                if (!ToolsHelper.isNull(city)) {
                    mSearching.transitSearch((new TransitRoutePlanOption()).from(stNode)
                            .city(city).to(enNode));
                } else {
                    ToolsHelper.showStatus(mActivity, false, mActivity.getString(R.string.locatedrouteactivity_locate_city));
                }
                break;
            case 2:
                mSearching.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));

                break;
            case 3:
                mSearching.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                break;

            default:
                break;
        }
    }
    /**
     * 路径规划初始化
     *
     * @param start 开始位置
     * @param end   结束位置
     * @param plan  出行方式   1 公共交通；  2 语音导航/自驾 ；3 步行
     */
    public void searchRoutePlan(final LatLng start, final LatLng end, int plan) {
        stNode = PlanNode.withLocation(start);
        enNode = PlanNode.withLocation(end);


        switch (plan) {
            case 1:
                String city = MyApplication.lastCity;
                if (!ToolsHelper.isNull(city)) {
                    mSearching.transitSearch((new TransitRoutePlanOption()).from(stNode)
                            .city(city).to(enNode));
                } else {
                    ToolsHelper.showStatus(mActivity, false, mActivity.getString(R.string.locatedrouteactivity_locate_city));
                }
                break;
            case 2:
                Dialog noticeDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage(mActivity.getString(R.string.locatedrouteactivity_opentst));
                builder.setPositiveButton(mActivity.getString(R.string.locatedrouteactivity_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BNRoutePlanNode sNode = new BNRoutePlanNode(start.longitude, start.latitude, "", CoordinateType.BD09LL);
                        BNRoutePlanNode eNode = new BNRoutePlanNode(end.longitude, end.latitude, "", CoordinateType.BD09LL);
                        if (sNode != null && eNode != null) {
                            List<BNRoutePlanNode> list = new ArrayList<>();
                            list.add(sNode);
                            list.add(eNode);
                            BaiduNaviManager.getInstance().launchNavigator((Activity) mActivity, list, 1, true, new DemoRoutePlanListener(sNode));
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(mActivity.getString(R.string.locatedrouteactivity_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSearching.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                        dialog.dismiss();
                    }
                });
                noticeDialog = builder.create();
                noticeDialog.show();
                break;
            case 3:
                mSearching.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                break;

            default:
                break;
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
			 */

            Intent intent = new Intent(mActivity, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BNDemoGuideActivity.ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(mActivity, mActivity.getString(R.string.locatedrouteactivity_routeplan_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public void dissmissMap() {
        mLocClient.stop();

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    /**
     * 重启地图
     */
    public void resumeMap() {
        if (null != mLocClient) {
//            if (null != MyApplication.myLocation)
//                setCenter(MyApplication.myLocation);
            mLocClient.start();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(true);
            mMapView.onResume();
        }
    }

    /**
     * 暂停地图
     */
    public void pauseMap() {
        if (null != mLocClient) {
            mLocClient.stop();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mMapView.onPause();
        }
    }


    /**
     * 设置目标点，MARK
     */
    public void setLatLng(LatLng latLng) {
        if (null == marker) {
            OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bdA).zIndex(12);
            marker = (Marker) mBaiduMap.addOverlay(ooA);
        }
        marker.setPosition(latLng);
        MapStatus mMapStatus = new MapStatus.Builder().zoom(scale).target(latLng).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(u);
    }

    /**
     * 设置地图中心
     */
    public void setCenter(LatLng latLng) {
        MapStatus mMapStatus = new MapStatus.Builder().targetScreen(new Point(-1, -1)).target(latLng).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(u);
    }

    /**
     * 设置地图中心
     */
    public void setCenter(LatLng latLng, float scale) {
        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(scale).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(u);
    }


    /**
     * 设置地图中心所在屏幕的位置
     */
    public void setPoint(LatLng latLng, float scale) {
        Point point = new Point(MyApplication.getInstance().getWidth() / 2, getMapView().getHeight() * 7 / 10);
        MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(scale).targetScreen(point).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(u);
    }

    public Marker getMarker() {
        return marker;
    }

    /**
     * 设置地图状态监听
     */
    public void setOnMapStatusChangeListener(BaiduMap.OnMapStatusChangeListener ds) {
        mBaiduMap.setOnMapStatusChangeListener(ds);
    }


    /**
     * 补充：计算两点之间真实距离
     * @return 米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }
    /**
     * 补充：计算两点之间真实距离
     * @return 米
     */
    public static int getDistance(LatLng start, LatLng end){
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;
        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;
        //地球半径
        double R = 6371.004;
        //两点间距离 m，如果想要米的话，结果*1000就可以了
        double dis = Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
        return (Double.valueOf(dis*1000).intValue());
    }

}
