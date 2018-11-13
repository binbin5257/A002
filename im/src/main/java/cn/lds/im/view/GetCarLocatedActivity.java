package cn.lds.im.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.permission.PermissionHelper;
import cn.lds.im.data.GetReturnCarLocationInfo;
import cn.lds.im.data.LocationCtiyModel;
import cn.lds.im.data.ReservationOrdersLocationsModel;
import cn.lds.im.view.adapter.LocationParkAdpter;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;


/**
 * 定位页面
 *
 * @author pengwb
 */
public class GetCarLocatedActivity extends BaseActivity implements OnClickListener, BaiduMap.OnMapClickListener {

    protected static final String TAG = GetCarLocatedActivity.class.getSimpleName();
    protected boolean isFirstLoc = true;// 是否首次定位
    protected LocationParkAdpter adpter = null;
    protected Intent intent;
    boolean useDefaultIcon = false;
    protected String no;

    protected MapView mMapView;
    protected BaiduMap mBaiduMap;

    protected BaiduMapHelper baiduMapHelper;
    protected ArrayList<ReservationOrdersLocationsModel.DataBean> allParks;
    protected ReservationOrdersLocationsModel.DataBean selectPark;
    protected int parkInfowindowOffset;//车场infowindow偏移量

    @ViewInject(R.id.top_back_btn)
    protected TextView top_back;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    @ViewInject(R.id.input_keyword)
    protected EditText input_keyword;
    @ViewInject(R.id.location_clear)
    protected ImageButton btnSearchClear;
    @ViewInject(R.id.setlocation)
    protected Button setlocation;
    @ViewInject(R.id.loacitonmapview)
    protected FrameLayout locationmapview;
    @ViewInject(R.id.tv_poi_name)
    protected TextView poiName;   //地图上选中的poi名称
    @ViewInject(R.id.administrative_position)
    protected TextView adminPosition;  //选中点的行政名称
    @ViewInject(R.id.tv_distace)
    protected TextView distanceTv;  //距离空间
    @ViewInject(R.id.locatedactivity_confirm)
    protected TextView locatedactivity_confirm;  //距离空间
    @ViewInject(R.id.input_whichcity)
    protected TextView input_whichcity;  //当前城市
    @ViewInject(R.id.triangle_arrow)
    protected ImageView triangle_arrow;  //城市左边向下三角标

    protected int layoutID = R.layout.activity_result_loc_get_car;
    protected GetCarLocatedActivity activity;
    private GetReturnCarLocationInfo getReturnCarLocationInfo;
    private String userWay;
//    private List<CityBean> mList;

    protected List<LocationCtiyModel.DataBean> mCityList = new  ArrayList<>();

    protected void setActivity(GetCarLocatedActivity activity) {
        this.activity = activity;
    }

    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
//        mList =  new CityModel().initCityData();

        ViewUtils.inject(GetCarLocatedActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }



    protected void initConfig() {
        intent = getIntent();
        baiduMapHelper = new BaiduMapHelper(mContext);
        initview();
        addlisener();
        EventBus.getDefault().register(this);
    }

    protected void initview() {
        top_back.setVisibility(View.VISIBLE);
        top_title.setVisibility(View.VISIBLE);

        allParks = new ArrayList<>();
        initAdapter();
        //初始化视图
        mMapView = baiduMapHelper.getMapView();
        locationmapview.addView(mMapView, 0);
        mBaiduMap = mMapView.getMap();
        baiduMapHelper.initLocation(bdLocationListener);
        mBaiduMap.setOnMapClickListener(this);
        btnSearchClear.setVisibility(View.GONE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);
        input_keyword.setVisibility(View.VISIBLE);
        setlocation.setVisibility(View.VISIBLE);

        parkInfowindowOffset = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.location_mark).getHeight();

        ModuleHttpApi.reservationOrdersLocations("");
        getIntentExtra();

    }
    public void getIntentExtra() {
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            Bundle bundle = intent.getExtras();
            userWay = bundle.getString("USER_WAY");
            if(!TextUtils.isEmpty(userWay) && !userWay.equals("RETURN_CAR")){
                top_title.setText("接车地点");
                input_keyword.setHint("请选择接车地点");
                locatedactivity_confirm.setText("在此接车");
            }else if(!TextUtils.isEmpty(userWay)){
                top_title.setText("还车地点");
                input_keyword.setHint("请选择还车地点");
                locatedactivity_confirm.setText("在此还车");
            }else{
                top_title.setText("接车地点");
                input_keyword.setHint("请选择接车地点");
                locatedactivity_confirm.setText("在此接车");
            }
            getReturnCarLocationInfo = (GetReturnCarLocationInfo) bundle.getSerializable("CAR_LOCATION_INFO");
            if(getReturnCarLocationInfo != null){
                for(LocationCtiyModel.DataBean bean:mCityList){
                    if(bean.getName().equals(getReturnCarLocationInfo.getCity())){
                        bean.setSelected(true);
                    }else{
                        bean.setSelected(false);
                    }
                }
            }


        }
    }
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!(ModuleHttpApiKey.getLocationsCitys.equals(apiNo))) {
                return;
            }
            switch (apiNo){
                case ModuleHttpApiKey.getLocationsCitys:
                    locationCityList(event.getResult().getResult());// 城市列表
                    break;
            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    private void locationCityList(String result) {
        LocationCtiyModel locationCityModel = GsonImplHelp.get().toObject(result, LocationCtiyModel.class);
        List<LocationCtiyModel.DataBean> citys = locationCityModel.getData();
        if(citys != null && citys.size() > 0){
            mCityList.clear();
            mCityList.addAll(citys);
        }
        input_whichcity.setText(MyApplication.lastCity + "市");
        for(LocationCtiyModel.DataBean bean:mCityList){
            if(bean.getName().equals(MyApplication.lastCity + "市")){
                bean.setSelected(true);
            }else{
                bean.setSelected(false);
            }
        }

    }

    protected void initAdapter() {
        adpter = new LocationParkAdpter(mContext, allParks);
    }

    protected void addlisener() {
        try {
            top_back.setOnClickListener(this);
            btnSearchClear.setOnClickListener(this);
            setlocation.setOnClickListener(this);
            input_keyword.setOnClickListener(this);
            input_keyword.setFocusable(false);


        } catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        getDistance(MyApplication.myLocation,latLng);
        drawDotOnMap(latLng);
        baiduMapHelper.initPositionPoi(latLng, new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                if(poiList != null && poiList.size() > 0){
                    poiName.setText(poiList.get(0).name);
                    input_keyword.setText(poiList.get(0).name);
                    adminPosition.setText(poiList.get(0).address);
                    getReturnCarLocationInfo.setSpecificLocation(poiList.get(0).name);
                    getReturnCarLocationInfo.setAdministrativePosition(poiList.get(0).address);
                    if(poiList.get(0).location != null){
                        getReturnCarLocationInfo.setLatitude(poiList.get(0).location.latitude);
                        getReturnCarLocationInfo.setLongitude(poiList.get(0).location.longitude);

                    }
                }



            }
        });
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        LatLng latLng = mapPoi.getPosition();
        getDistance(MyApplication.myLocation,latLng);
        //绘制选中点
        ChoiceGetCarPosition(mapPoi.getName(), latLng);
        return false;
    }

    /**
     * poi选择接车地点
     * @param name
     * @param latLng
     */
    private void ChoiceGetCarPosition(String name, LatLng latLng) {
        getReturnCarLocationInfo.setSpecificLocation(name);
        getReturnCarLocationInfo.setLatitude(latLng.latitude);
        getReturnCarLocationInfo.setLongitude(latLng.longitude);
        drawDotOnMap(latLng);
        poiName.setText(name);
        input_keyword.setText(name);
        baiduMapHelper.initPositionPoi(latLng, new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                adminPosition.setText(reverseGeoCodeResult.getAddress());
                getReturnCarLocationInfo.setAdministrativePosition(reverseGeoCodeResult.getAddress());

            }
        });
    }

    /**
     * 在地图上绘制点
     * @param latLng
     */
    private void drawDotOnMap(LatLng latLng) {
        mBaiduMap.clear();
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.location_mark);
        BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(imageView);
        OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bdA).zIndex(12);
        mBaiduMap.addOverlay(ooA);
    }


    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.setlocation,
            R.id.location_clear,
            R.id.locatedactivity_confirm,
            R.id.input_whichcity,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_keyword: //进入位置搜索页面
                EnterSearchLocaionActivity();
                break;
            case R.id.input_whichcity:
                choiceCity();  //选择城市
                break;
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.location_clear:
                input_keyword.setText("");
                btnSearchClear.setVisibility(View.GONE);
                adpter.notifyDataSetChanged();
                break;
            case R.id.setlocation:
                if (isFirstLoc) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.locatedactivity_input_after_aftersuccess));
                    return;
                }
                baiduMapHelper.setCenter(MyApplication.myLocation);
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.locatedactivity_confirm:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("CAR_LOCATION_INFO", getReturnCarLocationInfo);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void choiceCity() {
        CustomPopuwindow.getInstance().showList(Gravity.LEFT,this, mCityList, findViewById(R.id.input_whichcity),triangle_arrow, new CustomPopuwindow.OnChoiceCityListener() {
            @Override
            public void selectedCity(Object o, int index) {
                LocationCtiyModel.DataBean data = (LocationCtiyModel.DataBean) o;
                input_whichcity.setText(data.getName());
                for(int i = 0; i < mCityList.size();i++){
                    LocationCtiyModel.DataBean cityBean = mCityList.get(i);
                    if(i == index){
                        cityBean.setSelected(true);
                    }else{
                        cityBean.setSelected(false);
                    }
                }
                //实时更新，定位信息
                LatLng lat = new LatLng(mCityList.get(index).getLatitude(), mCityList.get(index).getLongitude());
                baiduMapHelper.setCenter(lat);
                latlngToAddress(lat);
                MyApplication.lastCity = mCityList.get(index).getName();
                getReturnCarLocationInfo.setCity(MyApplication.lastCity);
                getDistance(MyApplication.myLocation,lat);

            }

            @Override
            public void dismiss() {

            }
        });
    }

    private void EnterSearchLocaionActivity() {
        Intent intent = new Intent(this,SearchLocationActivity.class);
        if(!TextUtils.isEmpty(userWay)){
            intent.putExtra("USER_WAY","RETURN_CAR");
        }

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                if(requestCode == 1){
                    Bundle b=data.getExtras(); //data为B中回传的Intent
                    SuggestionResult.SuggestionInfo info = b.getParcelable("SEARCH_RESULT");//str即为回传的值
                    if(info != null){
                        input_keyword.setText(info.key);
                        LatLng pt = info.pt;
                        drawDotOnMap(pt);
                        getDistance(MyApplication.myLocation,pt);
                        poiName.setText(info.key);
                        adminPosition.setText(info.city+info.district+info.key);
                        getReturnCarLocationInfo.setSpecificLocation(info.key);
                        getReturnCarLocationInfo.setAdministrativePosition(info.city+info.district+info.key);
                        getReturnCarLocationInfo.setLongitude(pt.longitude);
                        getReturnCarLocationInfo.setLatitude(pt.latitude);
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 定位SDK监听函数
     */
    BDLocationListener bdLocationListener = new BDLocationListener() {
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
                isFirstLoc = false;
                //定位完成后，初始化数据
                //  initDate();
                ModuleHttpApi.getLocationsCitys();
                if(getReturnCarLocationInfo == null){
                    String city = location.getCity();
                    if (null != city) {
                        MyApplication.lastCity = city.split(getString(R.string.locatedactivity_city))[0];
                    }
                    baiduMapHelper.setCenter(MyApplication.myLocation);
                    getReturnCarLocationInfo = new GetReturnCarLocationInfo();
                    getReturnCarLocationInfo.setCity(MyApplication.lastCity + "市");
                    latlngToAddress(MyApplication.myLocation);
                }else{
                    LatLng lat = new LatLng(getReturnCarLocationInfo.getLatitude(),getReturnCarLocationInfo.getLongitude());
                    MyApplication.lastCity = getReturnCarLocationInfo.getCity().split(getString(R.string.locatedactivity_city))[0];
                    baiduMapHelper.setCenter(lat);
                    input_whichcity.setText(MyApplication.lastCity + "市");
                    poiName.setText(getReturnCarLocationInfo.getSpecificLocation());
                    input_keyword.setText(getReturnCarLocationInfo.getSpecificLocation());
                    adminPosition.setText(getReturnCarLocationInfo.getAdministrativePosition());
                    if(getReturnCarLocationInfo.getDistance() >= 1000){
                        distanceStr = getReturnCarLocationInfo.getDistance()/1000 + "km";
                    }else{
                        distanceStr = getReturnCarLocationInfo.getDistance() + "m";
                    }
                    distanceTv.setText(distanceStr);
                    drawDotOnMap(lat);
                    getDistance(MyApplication.myLocation,lat);

                }


            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

    };


    @Override
    protected void onPause() {
        setResult(RESULT_CANCELED, intent);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // 检查权限
            PermissionHelper.checkPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        }
    }

    @Override
    protected void onDestroy() {
        baiduMapHelper.dissmissMap();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();

        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 根据规划行车路线获取两点间的距离
     */
    private String distanceStr = "";
    public void getDistance(LatLng start,LatLng end){
        double distance = DistanceUtil.getDistance(start, end);
        int distanceInt = (int) distance;
        getReturnCarLocationInfo.setDistance(distanceInt);
        if(distanceInt >= 1000){
            distanceStr = distanceInt/1000 + "km";
        }else{
            distanceStr = distanceInt + "m";
        }
        distanceTv.setText(distanceStr);
    };

    private void latlngToAddress(LatLng latlng) {
        drawDotOnMap(latlng);
        baiduMapHelper.initPositionPoi(latlng, new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                if(poiList != null && poiList.size() > 0){
                    poiName.setText(poiList.get(0).name);
                    adminPosition.setText(poiList.get(0).address);
                    distanceTv.setText("当前位置");
                    getReturnCarLocationInfo.setSpecificLocation(poiList.get(0).name);
                    getReturnCarLocationInfo.setAdministrativePosition(poiList.get(0).address);
                    if(poiList.get(0).location != null){
                        getReturnCarLocationInfo.setLatitude(poiList.get(0).location.latitude);
                        getReturnCarLocationInfo.setLongitude(poiList.get(0).location.longitude);
                    }

                }

            }
        });



    }









}
