package cn.lds.im.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.clusterutil.clustering.view.MyCluterItem;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.PopWindowListener2;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.pickerview.utils.PickerViewAnimateUtil;
import cn.lds.chatcore.data.BookCarModel;
import cn.lds.chatcore.data.ChargingModel;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.MapAllParkModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.AvailableVehiclesChangedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MapFragmentChoiceEvent;
import cn.lds.chatcore.event.OrganizationChangEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.dialog.ConfirmAndCancelDialog;
import cn.lds.chatcore.view.dialog.annotation.ClickPosition;
import cn.lds.chatcore.view.dialog.callback.OnDialogClickListener;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.AvailableVehicleModel;
import cn.lds.im.data.CheckUseCarApplyModel;
import cn.lds.im.data.MapSignParkModel;
import cn.lds.im.data.NearStationModel;
import cn.lds.im.data.OrderDetailInfoModel;
import cn.lds.im.data.OrderHave;
import cn.lds.im.data.OrderModel;
import cn.lds.im.view.ApplyDetailActivity;
import cn.lds.im.view.AuthenticationActivity;
import cn.lds.im.view.CashPledgeActivity;
import cn.lds.im.view.ChargingDetailActivity;
import cn.lds.im.view.ConfirmOrderActivity;
import cn.lds.im.view.MainActivity;
import cn.lds.im.view.WuhuLoginActivity;
import cn.lds.im.view.adapter.UseCarPagerAdapter;
import de.greenrobot.event.EventBus;

import static android.app.Activity.RESULT_OK;

/***
 * 首页-用车
 */
public class FirstFragment extends MyActivityFragment {
    public static final String TAG = "FirstFragment";

    @ViewInject(R.id.loacitonmapview)
    protected FrameLayout loacitonmapview;
    @ViewInject(R.id.map_hlist_viewpager)
    protected ViewPager map_hlist_viewpager;
    @ViewInject(R.id.map_hlist_left)
    protected ImageView map_hlist_left;
    @ViewInject(R.id.map_hlist_right)
    protected ImageView map_hlist_right;
    @ViewInject(R.id.map_bottom_layout)
    protected LinearLayout map_bottom_layout;
    @ViewInject(R.id.appoint_layout)
    protected RelativeLayout appoint_layout;

    @ViewInject(R.id.location_name_tv)
    protected TextView location_name_tv;
    @ViewInject(R.id.location_address_tv)
    protected TextView location_address_tv;
    @ViewInject(R.id.choicebooktime_taketime)
    protected TextView choicebooktime_taketime;
    @ViewInject(R.id.choicebooktime_returntime)
    protected TextView choicebooktime_returntime;
    @ViewInject(R.id.tv_setting_exit)
    protected TextView tv_setting_exit;
    @ViewInject(R.id.grid_thought)
    protected GridView thoughtGrid;
    @ViewInject(R.id.ll_thought_finish)
    protected LinearLayout thoughtFinstLlyt;
    @ViewInject(R.id.ll_driver_orver)
    protected LinearLayout rootDriverOver;
    @ViewInject(R.id.first_refresh_btn)
    protected Button refreshBtn;
    @ViewInject(R.id.one_key_search_car_iv)
    protected ImageView one_key_search_car_iv;


    protected final int MODEBOOK = 0;//约车界面
    protected final int MODEUSUAL = 1;//立即用车
    protected final int CHARGINGPILE  = 2;//充电桩
    protected final int BUSINESSUSERCAR  = 3;//公务用车
    protected final int PRIVATEUSECAR  = 4;//私人用车
    protected int choiceType = BUSINESSUSERCAR; //默认模式
    protected Intent intent;
    protected View view;
    protected BaiduMapHelper baiduMapHelper;
    protected MapView mMapView;
    protected BaiduMap mBaiduMap;
    protected ClusterManager<MyCluterItem> mClusterManager;
    protected int parkInfowindowOffset;//车场infowindow偏移量
    protected boolean isFirstLoc = true;// 是否首次定位
    protected MapAllParkModel.DataBean selectPark;//选中的车场信息
    protected List<MapSignParkModel.DataBean> selectParkList; //选中停车场的所有车辆信息集合 （立即用车）
    protected UseCarPagerAdapter useCarPagerAdapter;//车场内所有车子的车型（立即用车）
    protected List<AvailableVehicleModel.DataBean> bookParkList;//选中停车场的所有车辆信息集合 （预约用车）
    protected List<MapAllParkModel.DataBean> allParkList = new ArrayList<>();//所有场站集合
    protected BookCarModel bookCarModel;
    protected MainActivity mActivity;
    private ThoughtGridAdapter thoughtGridAdapter;
    private long takeTimeApponit = 0;
    private long returnTimeApponit = 0;
    private TextView titleBtnTV;
    private LinearLayout titleLlyt;
    private TextView titileTv;
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
    private TextView thoughtTv;
    private LinearLayout leftBtn;
    private RelativeLayout rightBtn;
    private String apiNo;
    private boolean isOneKeySearchCar = false;
    private boolean isFirstSearchChargingPile = true;
    /**
     * 一键寻车View
     */
    private View onKeySearchView;
    /**
     * 一键寻车图层
     */
    private Overlay overlay;
    private double onOneKeySearchlastLat;
    private double onOneKeySearchlastLon;

    private int gravity = Gravity.BOTTOM;
    private Animation inAnim;
    private Animation outAnim;
    private Marker centerMarder;

    /**
     * 最后一次定位中心点坐标
     */
    private LatLng lastCenterLatLnt;
    private RotateAnimation rotateAnimation;
    private RelativeLayout tutorialRlyt;
    private static boolean isThought = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(FirstFragment.class, this, view);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogHelper.e("onStart");
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e("注册EvenBus", e);
        }

        baiduMapHelper.resumeMap();
        initDate();
        hideInfowindow();
    }


    @Override
    public void onResume() {
        super.onResume();
        initDriveOverView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e("注销EvenBus", e);
        }
        selectParkList.clear();
        baiduMapHelper.pauseMap();
        hideInfowindow();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogHelper.e("setUserVisibleHint::" + isVisibleToUser);

    }

    @Override
    public void show(FragmentManager manager, MyActivityFragment fragment) {
        super.show(manager, fragment);
        baiduMapHelper.resumeMap();
        initDate();
        hideInfowindow();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        editor.putBoolean("THOUGHT", true);
//        editor.commit();
    }


    @SuppressLint("ValidFragment")
    public FirstFragment() {
    }


    @OnClick({R.id.setlocation, R.id.tv_setting_exit, R.id.map_hlist_left, R.id.map_hlist_right,
            R.id.first_refresh_btn,
            R.id.choicebooktime_taketimerllt, R.id.choicebooktime_returntimerllt,R.id.one_key_search_car_iv
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setlocation:
                reposition();//重新定位
                break;
            case R.id.tv_setting_exit:
                checkUserCar(); //校验用车条件
                break;
            case R.id.map_hlist_left:
                //车辆向左翻页
                map_hlist_viewpager.setCurrentItem(map_hlist_viewpager.getCurrentItem() - 1);
                break;
            case R.id.map_hlist_right:
                //车辆向右翻页
                map_hlist_viewpager.setCurrentItem(map_hlist_viewpager.getCurrentItem() + 1);
                break;
            case R.id.first_refresh_btn:
                refrushView();//刷新界面
                break;
            case R.id.choicebooktime_taketimerllt:
                choickTakeCarTime(); //选择预约取车时间
                break;
            case R.id.choicebooktime_returntimerllt:
                choiceReturnTime(); // 选择预约还车时间
                break;
            case R.id.one_key_search_car_iv:
                onekeySearchCar(); // 一键寻车
                break;
        }
    }

    /**
     * 初始化界面数据
     */
    protected void initDate() {
        if (MODEBOOK == choiceType && !isFirstLoc){
            lastCenterLatLnt = null;
            ModuleHttpApi.locationResourcesScheduled(); //获取预约用车场站信息
        }else if(MODEUSUAL == choiceType){
            lastCenterLatLnt = null;
            ModuleHttpApi.locationResources(); //获取立即用车场站信息
        }else if(PRIVATEUSECAR == choiceType){ //私有用车
            lastCenterLatLnt = null;
            ModuleHttpApi.locationResourcesBusinessPrivateUseCar("PERSONAL");
        }else if(BUSINESSUSERCAR == choiceType){ //公务用车
            lastCenterLatLnt = null;
            ModuleHttpApi.locationResourcesBusinessPrivateUseCar("PUBLIC");
        }

    }

    /**
     * 重新定位
     */
    private void reposition(){
        if (isFirstLoc) {
            ToolsHelper.showStatus(getActivity(), false, getString(R.string.locatedactivity_input_after_aftersuccess));
            return;
        }
        baiduMapHelper.setCenter(MyApplication.myLocation);
    }

    /**
     * 刷新界面
     */
    private void refrushView() {
        baiduMapHelper.resumeMap();
        refreshBtn.startAnimation(rotateAnimation);
//        LoadingDialog.showDialog(mActivity, "请稍后..");
        if(choiceType == CHARGINGPILE){
            if(lastCenterLatLnt != null){
                ModuleHttpApi.chargingLocationsAll(lastCenterLatLnt);  //获取充电桩数据
            }else{
                ModuleHttpApi.chargingLocationsAll(MyApplication.myLocation);  //获取充电桩数据
            }
        }
        initDate();
        hideInfowindow();
    }

    /**
     * 选择预约取车时间
     */
    private void choickTakeCarTime() {
        PopWindowHelper.getInstance().showOptions(getActivity(),"选择取车时间",new PopWindowListener2() {
            @Override
            public void confirm(String text) {
                choicebooktime_taketime.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
                takeTimeApponit = TimeHelper.getTime(TimeHelper.FORMAT7, text);
                bookCarModel.setScheduledPickedUpTime(takeTimeApponit);
            }

            @Override
            public void cancel() {

            }
        });
    }

    /**
     * 选择预约还车时间
     */
    private void choiceReturnTime() {
        if (takeTimeApponit == 0) {
            ToolsHelper.showStatus(mActivity, false, "请选择取车时间");
            return;
        }
        PopWindowHelper.getInstance().showOptions(getActivity(),"选择还车时间",new PopWindowListener2() {
            @Override
            public void confirm(String text) {
                  choicebooktime_returntime.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
                  returnTimeApponit = TimeHelper.getTime(TimeHelper.FORMAT7, text);
                  tv_setting_exit.setEnabled(true);
                  bookCarModel.setScheduledDroppedOffTime(returnTimeApponit);
            }

            @Override
            public void cancel() {

            }
        });
    }

    /**
     * 校验用车条件
     */
    private void checkUserCar(){

        if (AccountManager.getInstance().isLogin()) {
            AccountsTable table = AccountManager.getInstance().findByNo();
            if(CacheHelper.getIsNewUser()){
                CacheHelper.setIsNewUser(false);
                tutorialRlyt.setVisibility(View.VISIBLE);
            }else if (null != table) {
                if (Constants.UNCOMMITTED.equals(table.getReviewType())
                        || Constants.REFUSED.equals(table.getReviewType())) { // 未提交审核或拒绝审核
                    PopWindowHelper.getInstance().remind(getActivity(), new PopWindowListener() {
                        @Override
                        public void confirm() {
                            Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setConfirmTx(getString(R.string.pop_window_confirm))
                            .setCancelTx(getString(R.string.pop_window_cancel))
                            .setContentTx(getString(R.string.pop_window_promt)).show(mActivity.mTop_iv);
                } else if (Constants.REVIEWING.equals(table.getReviewType())) { // 审核中
                    PopWindowHelper.getInstance().alert(getActivity(), new PopWindowListener() {
                        @Override
                        public void confirm() {
                        }

                        @Override
                        public void cancel() {
                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setConfirmTx(getActivity().getString(R.string.order_haode)).
                            setContentTx(getString(R.string.selectedresourcepopwindow_reviewing)).show(mActivity.mTop_iv);
                } else if (choiceType == PRIVATEUSECAR && (ToolsHelper.isNull(table.getForegiftAmount()) || Integer.valueOf(table.getForegiftAmount()) == 0)) {
                    ToolsHelper.showStatus(mActivity, false, getString(R.string.foregift_quota_tips));
                    startActivity(new Intent(mActivity, CashPledgeActivity.class));
                } else {
                    //检查是否有存在的订单
                    ModuleHttpApi.hasOrderExistInMap();
//                    ModuleHttpApi.getHasProcess();
                }
            }
        } else {
            intent.setClass(getActivity(), WuhuLoginActivity.class);
            startActivity(intent);
        }


    }

    /**
     * 初始化
     */
    protected void init() {
        intent = new Intent();
        this.mActivity = (MainActivity) MyApplication.getInstance().getMainActivity();
        tutorialRlyt = (RelativeLayout) getActivity().findViewById(R.id.rl_tutorial);
        //初始化车辆行驶结束UI
//        initDriveOverView();
        //初始化地图相关UI
        initMapView();

        //设置数据
        selectParkList = new ArrayList<>();
        bookParkList = new ArrayList<>();

        initAdapter();
        map_hlist_viewpager.setOffscreenPageLimit(1);
        map_hlist_viewpager.setAdapter(useCarPagerAdapter);
        map_hlist_viewpager.addOnPageChangeListener(onpagechangelistener);
        //约车信息
        bookCarModel = new BookCarModel();
        parkInfowindowOffset = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.location_mark).getHeight();
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.pull_rotate_fast);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    private void initMapView() {
        //初始化视图
        baiduMapHelper = new BaiduMapHelper(mActivity);
        mMapView = baiduMapHelper.getMapView();
        loacitonmapview.addView(mMapView, 0);
        mBaiduMap = mMapView.getMap();
        baiduMapHelper.initLocation(bdLocationListener);
        initCluster();

    }

    /**
     * 初始化聚合ui及事件
     */
    private void initCluster() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<>(getActivity(), mBaiduMap);
        mClusterManager.setOnClusterItemClickListener(clusterItemClickListener);
        mClusterManager.setOnClusterClickListener(clusterClickListener);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideInfowindow();//单击地图，隐藏infowindow
                if (null != mBaiduMap)
                    mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                hideInfowindow();//单击地图，隐藏infowindow
                if (null != mBaiduMap)
                    mBaiduMap.hideInfoWindow();
                return false;
            }
        });
    }

    protected void initAdapter() {
        useCarPagerAdapter = new UseCarPagerAdapter(mActivity, selectParkList);
    }

    protected ViewPager.OnPageChangeListener onpagechangelistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //判断是否是一键寻车
            if(isOneKeySearchCar){
                if(selectParkList.size() > 0){
                    location_name_tv.setText(selectParkList.get(position).getLocationName());
                    location_address_tv.setText(selectParkList.get(position).getLocationAddress());
                }
            }
            showArrows();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 是否显示，下方左右滑动箭头
     **/
    protected void showArrows() {

        int count = map_hlist_viewpager.getAdapter().getCount();
        if (0 == map_hlist_viewpager.getCurrentItem())
            map_hlist_left.setVisibility(View.INVISIBLE);
        else
            map_hlist_left.setVisibility(View.VISIBLE);

        if (count - 1 == map_hlist_viewpager.getCurrentItem()) {
            map_hlist_right.setVisibility(View.INVISIBLE);
        } else {
            map_hlist_right.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 聚合点监听事件
     */
    protected ClusterManager.OnClusterClickListener clusterClickListener = new ClusterManager.OnClusterClickListener<MyCluterItem>() {
        @Override
        public boolean onClusterClick(Cluster<MyCluterItem> cluster) {

            int scale = (int) (mBaiduMap.getMapStatus().zoom) + 3;
            if (scale > mBaiduMap.getMaxZoomLevel())
                scale = (int) (mBaiduMap.getMaxZoomLevel());
            baiduMapHelper.setCenter(cluster.getPosition(), scale);
            return true;
        }
    };

    /**
     * 场站marker点监听事件
     */
    protected ClusterManager.OnClusterItemClickListener<MyCluterItem> clusterItemClickListener = new ClusterManager.OnClusterItemClickListener<MyCluterItem>() {
        @Override
        public boolean onClusterItemClick(MyCluterItem item) {
            selectPark = null;
            selectPark = item.getmDataBean();
            if(CHARGINGPILE == choiceType){
                Intent intent = new Intent(getActivity(),ChargingDetailActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putDouble("distance",mDataList.get(position).getDistance());
                bundle.putInt("id",Integer.parseInt(selectPark.getId()));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }else{
                isOneKeySearchCar = false;
                if (null == selectPark)
                    return false;
                showInfowindow(selectPark);
                showArrows();
            }

            return true;
        }
    };


    protected void showInfowindow(final MapAllParkModel.DataBean selectPark) {
        location_name_tv.setText(selectPark.getName());
        location_address_tv.setText(selectPark.getAddress());
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_firstfragment_infowindow, null);
        TextView name = (TextView) view.findViewById(R.id.item_firstfragment_infowindow_text);
        TextView vehiclecount = (TextView) view.findViewById(R.id.map_hlist_infowindow_car);
        TextView chargespotscount = (TextView) view.findViewById(R.id.map_hlist_infowindow_power);
        TextView parkinglotcount = (TextView) view.findViewById(R.id.map_hlist_infowindow_p);
        name.setText(selectPark.getName());
        vehiclecount.setText(selectPark.getAvailableVehicle());
        chargespotscount.setText(selectPark.getChargeSpots());
        parkinglotcount.setText(selectPark.getParkingLots());
        final LatLng latLng = new LatLng(selectPark.getLatitude(), selectPark.getLongitude());
        InfoWindow mInfoWindow = new InfoWindow(view, latLng, -parkInfowindowOffset);
        mBaiduMap.showInfoWindow(mInfoWindow);
        LoadingDialog.showDialog(getActivity(), "1");
        if (PRIVATEUSECAR == choiceType) {
            //选中场站之后，设置resourceid
            bookCarModel.setLocationNo(selectPark.getId());
            MyApplication.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng latLng = new LatLng(selectPark.getLatitude() - 0.02, selectPark.getLongitude());
                    baiduMapHelper.setCenter(latLng);
                }
            });
            ModuleHttpApi.availableVehicles(selectPark.getId(),"PERSONAL");
        } else if(BUSINESSUSERCAR == choiceType){
            ModuleHttpApi.availableVehicles(selectPark.getId(),"PUBLIC");
            baiduMapHelper.setCenter(latLng);
        }
    }

    /**
     * 获取当前位置坐标
     */
    protected BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //防止fragment切换之后，页面不存在，但监听还在继续造成IllegalStateException not attached activity
            if (!isAdded())
                return;
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            if(choiceType != CHARGINGPILE){
                MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                //实时更新，定位信息
                MyApplication.myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
            if(choiceType != CHARGINGPILE && overlay != null && (onOneKeySearchlastLat != MyApplication.myLocation.latitude || onOneKeySearchlastLon != MyApplication.myLocation.longitude)){
//                overlay.remove();
//                drawSearchCarOnMap(MyApplication.myLocation);
            }

            if (isFirstLoc) {
                baiduMapHelper.setCenter(MyApplication.myLocation);
                isFirstLoc = false;
                String city = location.getCity();
                if (null != city) {
                    MyApplication.lastCity = city.split(getString(R.string.locatedactivity_city))[0];
                }
//                drawSearchCarOnMap(MyApplication.myLocation);
//                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true, BitmapDescriptorFactory.fromResource(R.drawable.location_my)));
                //定位完成后，初始化数据
                initDate();
            }
        }
    };

    /**
     * 隐藏列表，infowindow
     */
    protected void hideInfowindow() {
        if(map_bottom_layout.getVisibility() == View.VISIBLE){
            map_bottom_layout.setVisibility(View.GONE);
            map_bottom_layout.startAnimation(outAnim);
        }
        if (MODEBOOK == choiceType) {
            takeTimeApponit = 0;
            returnTimeApponit = 0;
            mActivity.setTop_title_btn(getString(R.string.bookcar_title));
        } else if (BUSINESSUSERCAR == choiceType){
            mActivity.setTop_title_btn(getString(R.string.map_businessusercar));
        } else if(PRIVATEUSECAR == choiceType){
            mActivity.setTop_title_btn(getString(R.string.map_privateusecar));
        } else if(CHARGINGPILE == choiceType){
            mActivity.setTop_title_btn(getString(R.string.map_charge_pie));
        } else{
            mActivity.setTop_title_btn(getString(R.string.map_hlist_usercar_rightnow));
        }


        if (null != mBaiduMap) {
            mBaiduMap.hideInfoWindow();
        }
    }

    /**
     * 模式选择事件
     *
     * @param event http请求完成事件
     */
    public void onEventMainThread(MapFragmentChoiceEvent event) {
        int k = event.getChoiceType();
        if (choiceType == k)
            return;
        if(choiceType == CHARGINGPILE){
            clearChargingPileMarker();
            initCluster(); //初始化聚合Ui及事件
        }
        choiceType = k;
        initDate();
        hideInfowindow();
        //切换选择模式，把选中的停车场置为空
        selectPark = null;
        switch (choiceType) {
            case PRIVATEUSECAR:
                one_key_search_car_iv.setVisibility(View.VISIBLE);
                //预约模式,上一次预约的记录重置
                bookCarModel.resetModel();
                baiduMapHelper.setPoint(MyApplication.myLocation, 12.9998F);
                //隐藏气泡上的infowindow
                mBaiduMap.hideInfoWindow();
                break;
            case BUSINESSUSERCAR:
                one_key_search_car_iv.setVisibility(View.VISIBLE);
                baiduMapHelper.setCenter(MyApplication.myLocation);
                break;
            case CHARGINGPILE:
                one_key_search_car_iv.setVisibility(View.GONE);
                isFirstSearchChargingPile = true;
                reposition();
                if(lastCenterLatLnt != null){
                    ModuleHttpApi.chargingLocationsAll(lastCenterLatLnt);  //获取充电桩数据
                }else{
                    ModuleHttpApi.chargingLocationsAll(MyApplication.myLocation);  //获取充电桩数据
                }
                mBaiduMap.setOnMapStatusChangeListener(chargingPileMapStatusChange);
                break;
        }


    }

    /**
     * 清除充电桩marker点及事件
     */
    private void clearChargingPileMarker() {
        mBaiduMap.removeMarkerClickListener(onMarkerClickListener);
        mBaiduMap.clear();
    }

    private float lastZoom;

    protected BaiduMap.OnMapStatusChangeListener chargingPileMapStatusChange = new BaiduMap.OnMapStatusChangeListener(){

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            if(choiceType == CHARGINGPILE){
                LatLng currCenterLat = mapStatus.target;
                float currZoom = mapStatus.zoom;
                if(centerMarder != null){
                    centerMarder.setPosition(currCenterLat);
                }
                if(isFirstSearchChargingPile){
                    centerMarder = drawaCenterMarker(currCenterLat);
                    centerMarder.setPosition(currCenterLat);
                    baiduMapHelper.setCenter(currCenterLat);
                    lastZoom =currZoom;
                    lastCenterLatLnt = currCenterLat;
                    isFirstSearchChargingPile = false;
                }else{
                    if(Math.abs(lastZoom-currZoom)>0.000001){
                        baiduMapHelper.setCenter(currCenterLat);
                        lastZoom =currZoom;
                    }else{
                        GeoCoder geoCoder = GeoCoder.newInstance();
                        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(currCenterLat));
                        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {


                            @Override
                            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                            }

                            @Override
                            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                                String address = reverseGeoCodeResult.getAddress();
                                LatLng latLng = reverseGeoCodeResult.getLocation();
                                if(baiduMapHelper.getDistance(lastCenterLatLnt,latLng) > 1000){
                                    lastCenterLatLnt = latLng;
                                    if(!TextUtils.isEmpty(address)){
                                        reverseGeoCodeResult.getLocation();
                                        refreshBtn.startAnimation(rotateAnimation);
                                        ModuleHttpApi.chargingLocationsAll(lastCenterLatLnt);
                                    }
                                }

                            }
                        });
                    }
                }
            }
        }
    };
    public Marker drawaCenterMarker(LatLng latLng){
        OverlayOptions ooA = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_mark_new_translate))
                .zIndex(9).draggable(true);
        return (Marker)mBaiduMap.addOverlay(ooA);
    }
    /**
     * 可用车数量变化
     *
     * @param event http请求完成事件
     */
    public void onEventMainThread(AvailableVehiclesChangedEvent event) {
        //刷新车场数据
        initDate();
    }

    /**
     * 发送验证码
     * 使用手机号注册
     * 登录
     *
     * @param event http请求完成事件
     */
    public void onEventMainThread(HttpRequestEvent event) {
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            apiNo = httpResult.getApiNo();

            if (!(ModuleHttpApiKey.availableVehicles.equals(apiNo)
                    || ModuleHttpApiKey.locationResources.equals(apiNo)
                    || ModuleHttpApiKey.availableVehicleModels.equals(apiNo)
                    || ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo)
                    || ModuleHttpApiKey.reservationOrdersComment.equals(apiNo)
                    || ModuleHttpApiKey.instantNearest.equals(apiNo)
                    || ModuleHttpApiKey.scheduledNearest.equals(apiNo)
                    || ModuleHttpApiKey.getChargingLocationsAll.equals(apiNo)
                    || ModuleHttpApiKey.checkUseApply.equals(apiNo)
                    || (ModuleHttpApiKey.hasOrderExistInMap.equals(apiNo))))
                return;

            LoadingDialog.dismissDialog();
            //检查用车申请
            if(ModuleHttpApiKey.checkUseApply.equals(apiNo)){
                handlerUserCarApplyResult(httpResult); //处理用车申请结果

            }
            // 全部充电桩
            if (ModuleHttpApiKey.getChargingLocationsAll.equals(apiNo)) {
                if (CHARGINGPILE == choiceType)
                    processRadiusChargingPileData(event.getResult().getResult());
//                    processChargingPileData(event.getResult().getResult());

            }
            // 全部停车场
            if (ModuleHttpApiKey.locationResources.equals(apiNo)) {
                if (BUSINESSUSERCAR == choiceType || PRIVATEUSECAR == choiceType)//若是已经切换模式,上一次请求的请求数据返回慢了,则不处理ui
                    B001(event.getResult().getResult());
            }
            if (ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo)) {
                if (MODEBOOK == choiceType)//若是已经切换模式,上一次请求的请求数据返回慢了,则不处理ui
                    B001(event.getResult().getResult());
            }
            // 指定停车场
            else if (ModuleHttpApiKey.availableVehicles.equals(apiNo) || ModuleHttpApiKey.instantNearest.equals(apiNo)) {
                MapSignParkModel d = GsonImplHelp.get().toObject(event.getResult().getResult(), MapSignParkModel.class);
                B002(d);
            }  //指定预约停车场
            else if (ModuleHttpApiKey.availableVehicleModels.equals(apiNo) || ModuleHttpApiKey.scheduledNearest.equals(apiNo)) {
                MapSignParkModel d = GsonImplHelp.get().toObject(event.getResult().getResult(), MapSignParkModel.class);
                B002(d);
                availableVehicleModels(event.getResult().getResult());
            } else if (ModuleHttpApiKey.hasOrderExistInMap.equals(apiNo)) {
                OrderHave orderHave = GsonImplHelp.get().toObject(event.getResult().getResult(), OrderHave.class);
                C002(orderHave.isData());
            } else if (ModuleHttpApiKey.reservationOrdersComment.equals(apiNo)) {
                //评价成功
                ordereCommentSuc();
            }
        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    private void handlerUserCarApplyResult( HttpResult httpResult ) {
        String result = httpResult.getResult();
        CheckUseCarApplyModel model = GsonImplHelp.get().toObject(result,CheckUseCarApplyModel.class);
        if(model != null && model.getData() != null && model.getData().getStatus() != null){
            switch (model.getData().getStatus()){
                case "NO_SUBMIT":
                    noSubmitUseCarApply("您未提交用车申请，\n是否提交用车申请？"); //当日未提交用车申请
                    break;
                case "APPOVING":
                    ToolsHelper.showStatus(getActivity(),false,"用车申请正在审核中");
                    break;
                case "APPROVED":
                    checkUseCarForBeijing();
                    break;
                case "REJECTED":
                    noSubmitUseCarApply("您当前还没有通过的用车申请，\n是否提交用车申请？");
                    break;
                case "OVER_TIME":
                case "LESS_TIME":
                    PopWindowHelper.getInstance().alert(getActivity(), new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setConfirmTx("好的")

                            .setContentTx("当日18点后至次日8点前，不允许申请公务用车，如需用车请申请私人用车").show(getActivity().findViewById(R.id.top__iv));
                    break;
            }
        }
    }

    /**
     * 解析中心点坐标电子围栏内数据
     * @param result
     */
    private void processRadiusChargingPileData(String result) {

        ChargingModel chargingModel = GsonImplHelp.get().toObject(result, ChargingModel.class);
        mClusterManager.clearItems();
        mBaiduMap.clear();
        List<ChargingModel.DataBean> data = chargingModel.getData();
        if(data != null && data.size() > 0){
            for (ChargingModel.DataBean dataBean : data){
                LatLng lt = new LatLng(dataBean.getLatitude(),dataBean.getLongitude());
                if(dataBean.getStatus().equals("FREE")){
                    setMarker(lt,R.drawable.bg_charging_free,dataBean);
                }else if(dataBean.getStatus().equals("BUSY")){
                    setMarker(lt,R.drawable.bg_charging_busy,dataBean);
                }else if(dataBean.getStatus().equals("UNKNOW")){
                    setMarker(lt,R.drawable.bg_charging_unknow,dataBean);
                }


            }
            mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
            if (null != lastCenterLatLnt) {
                baiduMapHelper.setCenter(lastCenterLatLnt,14.9998F);
            }
        }else{
            Toast.makeText(getActivity(),"附近没有设置充电站",Toast.LENGTH_SHORT).show();
        }


    }

    BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //在这里我做了跳转页面
            if(choiceType == CHARGINGPILE){
                ChargingModel.DataBean dataBean = (ChargingModel.DataBean) marker.getExtraInfo().get("info");
                Intent intent = new Intent(getActivity(),ChargingDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",dataBean.getId());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }

            return true;
        }
    };
    /**
     * 添加marker
     */
    private void setMarker(LatLng lt,int imagRes,ChargingModel.DataBean info) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(imagRes);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(lt)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        marker.setExtraInfo(bundle);
    }


    protected void availableVehicleModels(String result) {
        AvailableVehicleModel availableVehicleModel = GsonImplHelp.get().toObject(result, AvailableVehicleModel.class);
        if (!bookParkList.isEmpty())
            bookParkList.clear();
        bookParkList.addAll(availableVehicleModel.getData());
    }

    protected void C002(boolean result) {
        if (result) {
            ConfirmAndCancelDialog dialog = new ConfirmAndCancelDialog(mActivity,R.style.MyDialogStyle).setOnDialogClickListener(new OnDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dialog, String clickPosition) {
                    switch (clickPosition){
                        case ClickPosition.ACCEPT:
                            ModuleHttpApi.getProcess();
                            break;
                        case ClickPosition.CANCEL:
                            break;
                    }
                }
            });
            dialog.setConfirmText("进入行程");
            dialog.setCancelText("不进入");
            dialog.setPromptContent("您有一个行程已开始\n是否进入?");
            dialog.show();
        } else {
            //没有存在订单，准备创建订单/预约订单
            if (null == selectParkList || selectParkList.isEmpty())
                return;
            //判断用车模式
            if(choiceType == BUSINESSUSERCAR){
                //如果是公务用车，检车用户当日是否有用车申请
                ModuleHttpApi.checkUseApply(System.currentTimeMillis());
            }else{
                //检查当前用户是否在北京
                checkUseCarForBeijing();

            }

        }
    }
    /**
     * 检查当前用户是否在北京
     */
    private void checkUseCarForBeijing() {
        if(!TextUtils.isEmpty(MyApplication.lastCity)&& MyApplication.lastCity.contains("北京")){
            Spanned spanned = Html.fromHtml(
                           "<div>1.禁止在二环路内（含主路全线)通行。</div>" +
                                   " <div>2.工作日早晚高峰，7时至9时，17时至20时，禁止在五环路主路、辅路及其以内道路行驶;</div>" +
                                   " <div>3.工作日9时至17时，需遵守北京市尾号限行规定，限行尾号与北京号牌车辆相同，限行范围为五环路主路、辅路及其以内道路。</div>");
            PopWindowHelper.getInstance().confirm(getActivity(), new PopWindowListener() {
                @Override
                public void confirm() {
                    enterConfirmActivity();
                }

                @Override
                public void cancel() {

                }

                @Override
                public void onItemListener(int position) {

                }
            }).setConfirmTx("立即用车")
                    .setCancelTx("取消用车")
                    .setContentTx(spanned).show(mActivity.mTop_iv,mActivity.rootView);
        }else{
            enterConfirmActivity();
        }
    }

    /**
     * 进入确认订单页面
     */
    private void enterConfirmActivity() {
        MapSignParkModel.DataBean dataBean = selectParkList.get(map_hlist_viewpager.getCurrentItem());
        String id = dataBean.getId();
        String modelId = dataBean.getModelId();
        OrderModel model = new OrderModel(MODEUSUAL,id,modelId);
        model.setUrl(dataBean.getPictureId());
        model.setName(dataBean.getBrandName() + " " + dataBean.getSeriesName());
        model.setNumber(dataBean.getPlateLicenseNo());
        model.setMileage(dataBean.getSustainedMileage());
        model.setMaxSustainedMileage(dataBean.getMaxSustainedMileage());
        model.setTime(dataBean.getYear());
        model.setChargingRule(dataBean.getChargingRule());
//                model.setRange(dataBean.getChargingRule());
        if(isOneKeySearchCar){
            model.setReservationLocationNo(String.valueOf(dataBean.getLocationId()));
            model.setReturnLocationNo(String.valueOf(dataBean.getLocationId()));
        }else{
            model.setReservationLocationNo(selectPark.getId());
            model.setReturnLocationNo(selectPark.getId());
        }
        if(isOneKeySearchCar){
            model.setLat(dataBean.getLocationLatitude());
            model.setLng(dataBean.getLocationLongitude());
            model.setDepot_name(dataBean.getLocationName());
            model.setDepot_address(dataBean.getLocationAddress());
            model.setDepot_number(String.valueOf(dataBean.getLocationId()));

        }else{
            model.setLat(selectPark.getLatitude());
            model.setLng(selectPark.getLongitude());
            model.setDepot_name(selectPark.getName());
            model.setDepot_address(selectPark.getAddress());
            model.setDepot_number(selectPark.getId());
        }

        intent = new Intent(mActivity, ConfirmOrderActivity.class);
        intent.putExtra("order_info", model);
        startActivity(intent);
    }

    /**
     * 订阅：相关REST请求失败后
     *
     * @param event HttpRequestEvent
     */

    public void onEventMainThread(HttpRequestErrorEvent event) {
        // 只要是本画面的处理。都异常时都关闭等待
        HttpResult httpResult = event.getResult();
        // API区分
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.availableVehicles.equals(apiNo)
                || ModuleHttpApiKey.locationResources.equals(apiNo)
                || ModuleHttpApiKey.availableVehicleModels.equals(apiNo)
                || ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo)
                || ModuleHttpApiKey.hasOrderExistInMap.equals(apiNo)
                || ModuleHttpApiKey.instantNearest.equals(apiNo)
                || ModuleHttpApiKey.scheduledNearest.equals(apiNo)
                || ModuleHttpApiKey.checkUseApply.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersComment.equals(apiNo)))
            return;

        LoadingDialog.dismissDialog();
        //检查用车申请
        if(ModuleHttpApiKey.checkUseApply.equals(apiNo)){
            ToolsHelper.showHttpRequestErrorMsg(getActivity(), httpResult);
        }
        if (ModuleHttpApiKey.instantNearest.equals(apiNo) || ModuleHttpApiKey.scheduledNearest.equals(apiNo)) {
            ToolsHelper.showHttpRequestErrorMsg(getActivity(), httpResult);
        }
        // 全部停车场
        if (ModuleHttpApiKey.locationResources.equals(apiNo)) {
            ToolsHelper.showHttpRequestErrorMsg(getActivity(), httpResult);
        }
        // 指定停车场
        else if (ModuleHttpApiKey.availableVehicles.equals(apiNo)) {
            // 非401的错误，提交给主线程处理
            ToolsHelper.showHttpRequestErrorMsg(getActivity(), httpResult);
        } else if (ModuleHttpApiKey.hasOrderExistInMap.equals(apiNo)) {
            // 没有存在订单
            C002(false);
        } else if (ModuleHttpApiKey.reservationOrdersComment.equals(apiNo)) {
            //评价成功
            ordereCommentSuc();
        }
    }

    /**
     * 当日没有提交用车申请
     */
    private void noSubmitUseCarApply(String content) {
            PopWindowHelper.getInstance().remind(getActivity(), new PopWindowListener() {
                @Override
                public void confirm() {
                    Intent intent = new Intent(getActivity(), ApplyDetailActivity.class);
                    startActivity(intent);
                }

                @Override
                public void cancel() {

                }

                @Override
                public void onItemListener(int position) {

                }
            }).setConfirmTx("立即申请")
                    .setCancelTx("暂不申请")
                    .setContentTx(content).show(mActivity.mTop_iv,mActivity.rootView);
    }

    /**
     * 订单评价成功
     */
    private void ordereCommentSuc() {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("FILE_THOUGHT", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().commit();
        }
        Toast.makeText(mActivity, "非常感谢您对本次租赁服务的评价！", Toast.LENGTH_SHORT).show();

    }

    /**
     * 立即用车 指定停车场
     */
    protected void B002(MapSignParkModel d) {
        if (!selectParkList.isEmpty())
            selectParkList.clear();
        selectParkList.addAll(d.getData());
        //判断是否是一键寻车
        if(ModuleHttpApiKey.instantNearest.equals(apiNo) || ModuleHttpApiKey.scheduledNearest.equals(apiNo)){
            if(selectParkList.size() > 0){
                location_name_tv.setText(selectParkList.get(0).getLocationName());
                location_address_tv.setText(selectParkList.get(0).getLocationAddress());
            }else{
                Toast.makeText(getActivity(),"十分抱歉，附近没有查询到可用的车辆",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!selectParkList.isEmpty()) {
            map_hlist_viewpager.setAdapter(useCarPagerAdapter);
            if (View.GONE == map_bottom_layout.getVisibility()) {
                map_bottom_layout.setVisibility(View.VISIBLE);
                map_bottom_layout.startAnimation(inAnim);
                if (MODEBOOK == choiceType) {
                    appoint_layout.setVisibility(View.VISIBLE);
                    tv_setting_exit.setEnabled(false);
                } else {
                    appoint_layout.setVisibility(View.GONE);
                    tv_setting_exit.setEnabled(true);
                }
                choicebooktime_taketime.setText(getString(R.string.choicebooktime_taketime));
                choicebooktime_returntime.setText(getString(R.string.choicebooktime_returntime));
            }
        } else {
            if (View.VISIBLE == map_bottom_layout.getVisibility()){
                map_bottom_layout.setVisibility(View.GONE);
//                map_bottom_layout.startAnimation(outAnim);
            }
            ToolsHelper.showStatus(mActivity, false, "没有车辆");
        }
        map_hlist_viewpager.setCurrentItem(0, false);
        showArrows();
    }

    //获取所有停车场
    protected void B001(String httpResult) {
        MapAllParkModel mapHlistModel = GsonImplHelp.get().toObject(httpResult, MapAllParkModel.class);
        allParkList.clear();
        allParkList.addAll(mapHlistModel.getData());
        mClusterManager.clearItems();
        for (MapAllParkModel.DataBean dataBean : mapHlistModel.getData()) {
            LatLng latLng = new LatLng(dataBean.getLatitude(), dataBean.getLongitude());
            dataBean.setResId(R.drawable.location_mark);
            mClusterManager.addItem(new MyCluterItem(latLng, dataBean, getActivity(),1));
        }

        if (null != MyApplication.myLocation) {
            if (PRIVATEUSECAR == choiceType) {
                if (null == selectPark)//没有选中效果时，跳转到当前位置为中心
                    baiduMapHelper.setCenter(MyApplication.myLocation, (float) (Math.random() * (13.0000F - 12.9995F) + 12.9995F));
            } else//倍数必须改变才可以刷新地图上的数据???所有用随机数的方式设置保证可以刷新数据
                baiduMapHelper.setCenter(MyApplication.myLocation, (float) (Math.random() * (13.0000F - 12.9995F) + 12.9995F));
        }
    }


    public void onEventMainThread(OrganizationChangEvent event) {
        initDate();
    }

    /**
     * 初始化行程结束View
     */
    private void initDriveOverView() {
        Bundle bundle = mActivity.getIntent().getExtras();
        if(bundle != null){
            OrderDetailInfoModel.DataBean dataBean = (OrderDetailInfoModel.DataBean) bundle.getSerializable("ORDER_DATA");
            if(MainActivity.isThought && dataBean != null){
                final int id = dataBean.getId();
                String vehiclePicId = dataBean.getVehiclePicId();
                String carName = dataBean.getVehicleBrandName()+dataBean.getVehicleSeriesName();
                String plateLicenseNo = dataBean.getPlateLicenseNo();
                String getCarLocation = dataBean.getReservationLocationName();
                String getCarAddress = dataBean.getReservationLocationAddress();
                String returnCarLocation = dataBean.getReturnLocationName();
                String returnCarAddress = dataBean.getReturnLocationAddress();
                float pay = bundle.getFloat("pay");
                MainActivity.isThought = false;
                rootDriverOver.setVisibility(View.VISIBLE);

                titleBtnTV = (TextView) mActivity.findViewById(R.id.top_title_btn);
                titleLlyt = (LinearLayout) mActivity.findViewById(R.id.top_title_lly);
                titileTv = (TextView) mActivity.findViewById(R.id.top_title_dirve_over);
                leftBtn = (LinearLayout) mActivity.findViewById(R.id.top_menu_lly);
                rightBtn = (RelativeLayout) mActivity.findViewById(R.id.top_menu_rly);
                ImageView iconCarIv = (ImageView) mActivity.findViewById(R.id.iv_car);
                final TextView carModelTv = (TextView) mActivity.findViewById(R.id.car_model);
                final TextView carNoTv = (TextView) mActivity.findViewById(R.id.car_no);
                final TextView getCarLocationTv = (TextView) mActivity.findViewById(R.id.get_car_location);
                final TextView getCarAddressTv = (TextView) mActivity.findViewById(R.id.get_car_address);
                final TextView returnCarLocationTv = (TextView) mActivity.findViewById(R.id.return_car_location);
                final TextView returnCarAddressTv = (TextView) mActivity.findViewById(R.id.return_car_address);
                final TextView realPay = (TextView) mActivity.findViewById(R.id.tv_real_pay);
                thoughtTv = (TextView) mActivity.findViewById(R.id.tv_thought);

                ImageLoaderManager.getInstance().displayImageForCar(mActivity, vehiclePicId, iconCarIv);
                carModelTv.setText(carName);
                carNoTv.setText(plateLicenseNo);
                getCarLocationTv.setText(getCarLocation);
                getCarAddressTv.setText(getCarAddress);
                returnCarLocationTv.setText(returnCarLocation);
                returnCarAddressTv.setText(returnCarAddress);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                realPay.setText(df.format(pay) + "");


                titleBtnTV.setVisibility(View.GONE);
                titleLlyt.setFocusable(false);
                titleLlyt.setEnabled(false);
                leftBtn.setEnabled(false);
                leftBtn.setFocusable(false);
                rightBtn.setEnabled(false);
                rightBtn.setFocusable(false);
                mActivity.closeSlidingmentTouch();

                titileTv.setVisibility(View.VISIBLE);
                titileTv.setText("车辆行驶结束");
                thoughtFinstLlyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thoughtTv.setVisibility(View.VISIBLE);
                        titleBtnTV.setVisibility(View.VISIBLE);
                        titleLlyt.setFocusable(true);
                        titleLlyt.setEnabled(true);
                        leftBtn.setEnabled(true);
                        leftBtn.setFocusable(true);
                        rightBtn.setEnabled(true);
                        rightBtn.setFocusable(true);
                        titileTv.setVisibility(View.GONE);
                        rootDriverOver.setVisibility(View.GONE);
                        rootDriverOver.startAnimation(outAnim);
                        mActivity.openSlidingmentTouch();
                        if(selectedIndex != -1){
                            ModuleHttpApi.reservationOrdersComment(String.valueOf(id), selectedIndex + 1);
                        }else{
                            ModuleHttpApi.reservationOrdersComment(String.valueOf(id), 5);
                        }
                        selectedIndex = -1;

                    }
                });
                if (thoughtGridAdapter == null) {
                    thoughtGridAdapter = new ThoughtGridAdapter(mActivity);
                    thoughtGrid.setAdapter(thoughtGridAdapter);
                } else {
                    thoughtGridAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    /**
     * 选中星星索引值
     */
    private int selectedIndex = -1;

    class ThoughtGridAdapter extends BaseAdapter {

        private Context mContext;


        public ThoughtGridAdapter(Context context) {
            this.mContext = context;

        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_thought, null);
            ImageView starIv = (ImageView) view.findViewById(R.id.iv_thought_star);
            if (selectedIndex == -1) {
                starIv.setImageResource(R.drawable.ic_star_selected);
            } else {
                if (selectedIndex < position) {
                    starIv.setImageResource(R.drawable.ic_star_normal);
                } else {
                    starIv.setImageResource(R.drawable.ic_star_selected);
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = position;
                    thoughtTv.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
    /**
     * 一键寻车
     */
    private void onekeySearchCar() {
        LoadingDialog.showDialog(mActivity, "22","22");
        isOneKeySearchCar = true;
        baiduMapHelper.setCenter(MyApplication.myLocation);
        if (PRIVATEUSECAR == choiceType) {
            ModuleHttpApi.getInstantNearest("PERSONAL",MyApplication.myLocation); //一键寻私有用车
        } else if(BUSINESSUSERCAR == choiceType) {
            ModuleHttpApi.getInstantNearest("PUBLIC",MyApplication.myLocation); //一键寻公务用车
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoc = true;
        LoadingDialog.dialogIsNull();
    }

    /**
     * 底部布局显示动画
     */
    public Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(mActivity, res);
    }
    /**
     * 底部布局隐藏动画
     */
    public Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(mActivity, res);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                NearStationModel.DataBean nearStationData = (NearStationModel.DataBean) bundle.getSerializable("data");
                isOneKeySearchCar = false;
                for(MapAllParkModel.DataBean park:allParkList){
                    if(park.getId().equals(String.valueOf(nearStationData.getId()))){
                        selectPark = park;
                        MyApplication.myLocation = new LatLng(selectPark.getLatitude(),selectPark.getLongitude());
                        baiduMapHelper.setCenter(MyApplication.myLocation);
                        hideInfowindow();
                        showInfowindow(selectPark);
                        showArrows();
                    }
                }


            }

        }
    }


}
