package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.RouteDetailModel;
import cn.lds.im.data.TraveledModel;
import de.greenrobot.event.EventBus;

/**
 * 行程详情页面
 */
public class RouteDetailActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.routedetaillactivity_flyt)
    protected ImageView routedetaillactivity_flyt;

    @ViewInject(R.id.routedetail_payment)
    protected TextView routedetail_payment;
    @ViewInject(R.id.routedetail_no)
    protected TextView routedetail_no;
    @ViewInject(R.id.routedetail_reservationLocationName)
    protected TextView routedetail_reservationLocationName;
    @ViewInject(R.id.routedetail_returnLocationName)
    protected TextView routedetail_returnLocationName;
    @ViewInject(R.id.routedetail_pickedUpTime)
    protected TextView routedetail_pickedUpTime;
    @ViewInject(R.id.routedetail_droppedOffTime)
    protected TextView routedetail_droppedOffTime;
    @ViewInject(R.id.routedetail_vehicleModelName)
    protected TextView routedetail_vehicleModelName;
    @ViewInject(R.id.routedetail_plateLicenseNo)
    protected TextView routedetail_plateLicenseNo;
    @ViewInject(R.id.routedetail_commentsfalse)
    protected TextView routedetail_commentsfalse;
    @ViewInject(R.id.routedetail_commentstext)
    protected TextView routedetail_commentstext;
    @ViewInject(R.id.routedetail_yongshi)
    protected TextView routedetail_yongshi;
    @ViewInject(R.id.routedetail_xingshi)
    protected TextView routedetail_xingshi;
    @ViewInject(R.id.routedetail_ratingBar)
    protected RatingBar routedetail_ratingBar;
    @ViewInject(R.id.routedetail_commentsllyt)
    protected LinearLayout routedetail_commentsllyt;
    @ViewInject(R.id.routedetail_vehicleModelPic)
    protected ImageView routedetail_vehicleModelPic;
    @ViewInject(R.id.routedetail_jian_iv)
    protected ImageView routedetail_jian_iv;
    @ViewInject(R.id.routedetail_jia_iv)
    protected ImageView routedetail_jia_iv;

    protected String no;
    protected int zoom = 12;
    protected int index = 0;//如果拉取图片数据失败自动重新拉取,最多五次
    protected RouteDetailModel detailModel;

    protected int layoutID = R.layout.activity_route_detail;
    protected RouteDetailActivity activity;

    protected void setActivity(RouteDetailActivity activity) {
        this.activity = activity;
    }

    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(RouteDetailActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();

        no = getIntent().getAction();
        LoadingDialog.showDialog(mContext, "正在获取行程详情，请稍候");
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.routedetailactivity_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);


    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.routedetaillactivity_share_wechat,
            R.id.routedetaillactivity_share_circle,
            R.id.rlyt_routedetail_sheet_enter,
            R.id.routedetail_commentsfalse,
            R.id.routedetail_jia_iv,
            R.id.routedetail_jian_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.rlyt_routedetail_sheet_enter:
                mIntent.setClass(mContext, CostSheetActivity.class);
                mIntent.setAction(ToolsHelper.isNullString(detailModel.getData().getId()));
                startActivity(mIntent);
                break;
            case R.id.routedetaillactivity_share_circle:
                ToolsHelper.showInfo(mContext, "朋友圈");
                break;
            case R.id.routedetaillactivity_share_wechat:
                ToolsHelper.showInfo(mContext, "微信");
                break;
            case R.id.routedetail_commentsfalse:
                mIntent.setClass(mContext, RouteComentsActivity.class);
                mIntent.setAction(ToolsHelper.isNullString(detailModel.getData().getNo()));
                startActivity(mIntent);
                break;
            case R.id.routedetail_jia_iv:
                if (zoom == 18) {
                    ToolsHelper.showStatus(mContext, false, "已经缩放到最大了");
                } else {
                    zoom++;
                    //获取图片
                    ModuleHttpApi.traceTraveled(detailModel.getData().getId(), zoom);
                    routedetaillactivity_flyt.setImageResource(R.drawable.map_loading);
                }
                break;
            case R.id.routedetail_jian_iv:
                if (zoom == 8) {
                    ToolsHelper.showStatus(mContext, false, "已经缩放到最小了");
                } else {
                    zoom--;
                    //获取图片
                    ModuleHttpApi.traceTraveled(detailModel.getData().getId(), zoom);
                    routedetaillactivity_flyt.setImageResource(R.drawable.map_loading);
                }
                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
            ModuleHttpApi.reservationOrders(no);
        } catch (Exception e) {
            LogHelper.e(RouteDetailActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(RouteDetailActivity.class.getName(), e);
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrders.equals(apiNo) || ModuleHttpApiKey.traceTraveled.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.reservationOrders:
                    C006(httpResult.getResult());
                    break;
                case ModuleHttpApiKey.traceTraveled:
                    index = 0;
                    TraveledModel model = GsonImplHelp.get().toObject(httpResult.getResult(), TraveledModel.class);
                    BitmapHelper.getBitmapUtils(getResources().getDrawable(R.drawable.map_loading),
                            getResources().getDrawable(R.drawable.map_err)).display(routedetaillactivity_flyt,
                            ToolsHelper.isNullString(model.getData()));
                    break;
            }
        }

    }


    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrders.equals(apiNo) || ModuleHttpApiKey.traceTraveled.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        switch (apiNo) {
            case ModuleHttpApiKey.reservationOrders:
                ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
                break;
            case ModuleHttpApiKey.traceTraveled:
                if (index < 5) {//获取图片
                    ModuleHttpApi.traceTraveled(detailModel.getData().getId(), zoom);
                    index++;
                } else {
                    routedetaillactivity_flyt.setImageResource(R.drawable.map_err);
                    index = 0;
                }

//                ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
                break;
        }
    }

    protected void C006(String result) {
        detailModel = GsonImplHelp.get().toObject(result, RouteDetailModel.class);
        if (null != detailModel.getData())
            setData(detailModel.getData());
    }

    protected void setData(RouteDetailModel.DataBean data) {

        //获取图片
        ModuleHttpApi.traceTraveled(detailModel.getData().getId(), zoom);
        routedetaillactivity_flyt.setImageResource(R.drawable.map_loading);

        long pickeduptime = data.getPickedUpTime();
        long droppedofftime = data.getDroppedOffTime();
//        long userTime = (droppedofftime - pickeduptime) / 1000;
        long userTime = data.getTime();

        routedetail_payment.setText(ToolsHelper.getDouble(data.getPayment() / 100));
        routedetail_no.setText(ToolsHelper.isNullString(data.getNo()));
        routedetail_reservationLocationName.setText(ToolsHelper.isNullString(data.getReservationLocationName()));
        routedetail_returnLocationName.setText(ToolsHelper.isNullString(data.getReturnLocationName()));
        routedetail_pickedUpTime.setText(TimeHelper.getDateStringString(pickeduptime));
        routedetail_droppedOffTime.setText(TimeHelper.getDateStringString(droppedofftime));
        routedetail_vehicleModelName.setText(ToolsHelper.isNullString(data.getVehicleBrandName()) + " " + ToolsHelper.isNullString(data.getVehicleSeriesName()));
        routedetail_plateLicenseNo.setText(ToolsHelper.isNullString(data.getPlateLicenseNo()));
        routedetail_yongshi.setText(ToolsHelper.isNullString(String.format(getString(R.string.pay_time_text), userTime / 60, userTime % 60)));
        routedetail_xingshi.setText(ToolsHelper.getDouble(data.getOdometer()));
        ImageLoaderManager.getInstance().displayImageForCar(mContext, data.getVehiclePicUrlId(), routedetail_vehicleModelPic);

        if (data.isComment()) {
            routedetail_ratingBar.setVisibility(View.VISIBLE);
            routedetail_commentsfalse.setVisibility(View.INVISIBLE);

            int score = data.getScore();
            routedetail_ratingBar.setRating(score);

            if (ToolsHelper.isNull(data.getContent())) {
                routedetail_commentsllyt.setVisibility(View.GONE);
            } else {
                routedetail_commentsllyt.setVisibility(View.VISIBLE);
                routedetail_commentstext.setText(data.getContent());
            }
        } else {
            routedetail_ratingBar.setVisibility(View.INVISIBLE);
            routedetail_commentsllyt.setVisibility(View.GONE);
            routedetail_commentsfalse.setVisibility(View.VISIBLE);
        }
    }


}
