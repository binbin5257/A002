package cn.lds.im.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ChargingDetailModel;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

/**
 * Created by E0608 on 2017/10/25.
 */

public class ChargingDetailActivity extends BaseActivity{
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.loacitonmapview)
    protected RelativeLayout loacitonmapview;
    @ViewInject(R.id.charging_status)
    protected TextView charging_status;
    @ViewInject(R.id.charging_name)
    protected TextView charging_name;
    @ViewInject(R.id.charging_address)
    protected TextView charging_address;
    @ViewInject(R.id.charging_distance)
    protected TextView charging_distance;
    @ViewInject(R.id.tv_slow_charging)
    protected TextView tv_slow_charging;
    @ViewInject(R.id.tv_fast_charging)
    protected TextView tv_fast_charging;
    @ViewInject(R.id.charge_cost)
    protected TextView charge_cost;
    @ViewInject(R.id.parking_cost)
    protected TextView parking_cost;
    @ViewInject(R.id.service_cost)
    protected TextView service_cost;
    @ViewInject(R.id.open_time)
    protected TextView open_time;
    @ViewInject(R.id.operator_name)
    protected TextView operator_name;
    @ViewInject(R.id.operator_phone)
    protected TextView operator_phone;
    @ViewInject(R.id.tv_stationTel)
    protected TextView tv_stationTel;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    @ViewInject(R.id.main_view)
    protected LinearLayout mainView;

    protected ChargingDetailActivity activity;
    private BaiduMapHelper baiduMapHelper;
    private BaiduMap mBaiduMap;
    private DecimalFormat df;
    private ChargingDetailModel.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_detail);
        ViewUtils.inject(ChargingDetailActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initView();
        initMap();
        initData();
    }

    private void initMap() {
        //初始化视图
        baiduMapHelper = new BaiduMapHelper(this,false);
        MapView mMapView = baiduMapHelper.getMapView();
        mBaiduMap = mMapView.getMap();
        loacitonmapview.addView(mMapView, 0);

    }
    /**
     * 在地图上绘制点
     * @param latLng
     */
    private void drawDotOnMap(LatLng latLng) {
        baiduMapHelper.setCenter(latLng);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.location_mark_charge);
        BitmapDescriptor bdA = BitmapDescriptorFactory.fromView(imageView);
        OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bdA).zIndex(12);
        mBaiduMap.addOverlay(ooA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @OnClick({R.id.top_back_btn,R.id.btn_navigation,R.id.iv_stationTel,R.id.iv_phone,R.id.top_menu_btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.btn_navigation:
                IntentHelper.intentMap(mContext, dataBean.getLongitude(), dataBean.getLatitude(),true);
                break;
            case R.id.iv_phone:
                PopWindowHelper.getInstance().openTel(this, dataBean.getServiceTel()).show(findViewById(R.id.top__iv));
                break;
            case R.id.iv_stationTel:
                PopWindowHelper.getInstance().openTel(this, dataBean.getStationTel()).show(findViewById(R.id.top__iv));
                break;
            case R.id.top_menu_btn_del:
                CallChargingOrService();//给充电站或运营商打电话
                break;


        }
    }

    /**
     * 给充电站或运营商打电话
     */
    private void CallChargingOrService() {
        CustomPopuwindow.getInstance().callChargingAndService(this, mainView, new CustomPopuwindow.onCallChargingAndServiceListener() {
            @Override
            public void onCallCharging() {
                if(!TextUtils.isEmpty(dataBean.getStationTel())){
                    PopWindowHelper.getInstance().openTel(ChargingDetailActivity.this, dataBean.getStationTel()).show(findViewById(R.id.top__iv));
                }else{
                    Toast.makeText(ChargingDetailActivity.this,"当前充电站未录入服务电话",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCallService() {
                if(!TextUtils.isEmpty(dataBean.getServiceTel())){
                    PopWindowHelper.getInstance().openTel(ChargingDetailActivity.this, dataBean.getServiceTel()).show(findViewById(R.id.top__iv));
                }else{
                    Toast.makeText(ChargingDetailActivity.this,"当前充电站未录入运营商电话",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void dismiss() {

            }
        });
    }

    private void initView() {
        topTitle.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.ic_phone);
        topTitle.setText(getString(R.string.ChargingListActivity_title));
        btnBack.setVisibility(View.VISIBLE);

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        df = new DecimalFormat("#,##0.00");
        if(bundle != null){
            Double distanceDouble = bundle.getDouble("distance");
            String distanceStr = df.format(distanceDouble/1000);
            charging_distance.setText(distanceStr +"km");
            int id = bundle.getInt("id");
            LoadingDialog.showDialog(this,"请稍后...");
            ModuleHttpApi.getChargingLocationDetail(MyApplication.myLocation,id);


        }
    }
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!ModuleHttpApiKey.getChargingLocationDetail.equals(apiNo)){
                return;
            }
            switch (apiNo){
                case ModuleHttpApiKey.getChargingLocationDetail:
                    processData(event.getResult().getResult());// 城市列表
                    break;
              
            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    /**
     * 解析场站详情信息
     * @param result
     */
    private void processData(String result) {
        ChargingDetailModel chargingModel = GsonImplHelp.get().toObject(result, ChargingDetailModel.class);
        if(chargingModel != null){
            dataBean = chargingModel.getData();

            if(dataBean != null){
                charging_name.setText(dataBean.getName());
                charging_address.setText(dataBean.getAddress());
                if(dataBean.getStatus().equals("FREE")){
                    charging_status.setText("空闲");
                    charging_status.setTextColor(Color.parseColor("#17D427"));
                }else if(dataBean.getStatus().equals("BUSY")){
                    charging_status.setText("忙碌");
                    charging_status.setTextColor(Color.parseColor("#F5A763"));
                }else if(dataBean.getStatus().equals("UNKNOW")){
                    charging_status.setText("未知");
                    charging_status.setTextColor(Color.parseColor("#ffffff"));
                }
                charging_distance.setText(df.format(dataBean.getDistance()/1000) +"km");
                tv_fast_charging.setText(dataBean.getAvailableDc() + "/" + dataBean.getDcNumCount());
                tv_slow_charging.setText(dataBean.getAvailableAc() + "/" + dataBean.getAcNumCount());
                charge_cost.setText(df.format(dataBean.getElectricityFee() / 100)+" 元/度");
                service_cost.setText(df.format(dataBean.getServiceFee() / 100)+" 元/度");
                if(dataBean.isParkFree()){
                    parking_cost.setText("是");
                }else{
                    parking_cost.setText("否");
                }
                if(dataBean.isFullTimeOpening()){
                    open_time.setText("是");
                }else{
                    open_time.setText("否");
                }

//                @ApiModelProperty(value = "充电场站运营商", notes = "TE_LAI_DIAN(特来电), XING_XING_CHONG_DIAN(星星充电), E_CHONG_WANG(E充网)")
                if(dataBean.getOperator().equals("TE_LAI_DIAN")){
                    operator_name.setText("特来电");
                }else if(dataBean.getOperator().equals("XING_XING_CHONG_DIAN")){
                    operator_name.setText("星星充电");
                }else if(dataBean.getOperator().equals("E_CHONG_WANG")){
                    operator_name.setText("E充网");
                }
                operator_phone.setText(dataBean.getServiceTel());
                tv_stationTel.setText(dataBean.getStationTel());
                drawDotOnMap(new LatLng(dataBean.getLatitude(), dataBean.getLongitude()));
            }

        }
    }

}
