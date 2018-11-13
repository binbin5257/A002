package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.RevertCarModel;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.OrderCancelEvent;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.ModuleUrls;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.view.widget.CircleArcProgressbar;
import cn.lds.im.view.widget.CustomPopuwindow;
import cn.lds.im.view.widget.ProgressLayout;
import de.greenrobot.event.EventBus;

/**
 * 三十分钟内取车界面
 */
public class TakeCarActivity extends BaseActivity {
    //topbar
    @ViewInject(R.id.top_back_btn)
    protected Button top_back_btn;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    // R.id.top__iv
    @ViewInject(R.id.top__iv)
    public ImageView mTop_iv;
    @ViewInject(R.id.item_usercar_iv)
    protected ImageView item_usercar_iv;//车辆图片
    @ViewInject(R.id.item_usercar_sustainedMileage)
    protected TextView item_usercar_sustainedMileage;//续航里程
    @ViewInject(R.id.usercar_minprice)
    protected TextView usercar_minprice;//计费规则
    @ViewInject(R.id.order_car_info_name_tv)
    protected TextView order_car_info_name_tv;//车辆品牌
    @ViewInject(R.id.order_car_info_number_tv)
    protected TextView order_car_info_number_tv;//车牌号
    @ViewInject(R.id.prompt_take_name)
    protected TextView prompt_take_name;//取车名称
    @ViewInject(R.id.prompt_take_address)
    protected TextView prompt_take_address;//取车位置
    @ViewInject(R.id.circle_progressbar)
    protected CircleArcProgressbar circle_progressbar;
    @ViewInject(R.id.top_right_tv)
    protected TextView top_right_tv;
    @ViewInject(R.id.progresslayout)
    protected ProgressLayout progressLayout;
    @ViewInject(R.id.main_view)
    protected LinearLayout mainView;
    @ViewInject(R.id.take_tips)
    protected TextView take_tips;
    protected long time;

    protected OrderInfoModel orderInfoModel;//订单数据
    protected TakeCarActivity activity;
    protected int layoutID = R.layout.activity_take_car;
    private CustomPopuwindow popuwindow;
    protected boolean isAutoCancleOrder = false;//手动还是自动取消订单

    public void setActivity(TakeCarActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }
    public static TakeCarActivity instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(layoutID);
        ViewUtils.inject(TakeCarActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }

        initConfig();

    }

    protected void initConfig() {
        init();
        initData();
    }

    protected void init() {
        top_back_btn.setVisibility(View.VISIBLE);
        top_title_tv.setVisibility(View.VISIBLE);
        top_right_tv.setVisibility(View.VISIBLE);
        top_right_tv.setText("取消订单");
        popuwindow = new CustomPopuwindow();
    }

    protected void initData() {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_register), e);
        }
        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
        ModuleHttpApi.getExistOrder();

    }

    protected void refreshView(OrderInfoModel.DataBean dataBean) {
        ImageLoaderManager.getInstance().displayImageForCar(mContext, dataBean.getVehiclePicId(), item_usercar_iv);
        if (dataBean.getSustainedMileage() > 0) {
            item_usercar_sustainedMileage.setText(String.format(mContext.getString(R.string.sustained_mileage), String.valueOf(dataBean.getSustainedMileage())));
            progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getSustainedMileage());
        } else {
            progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getMaxSustainedMileage());
            item_usercar_sustainedMileage.setText("");
        }
        usercar_minprice.setText(dataBean.getChargingRule());
//            BitmapHelper.getBitmapUtils(null).display(order_icon_iv, orderModel.getUrl());
        order_car_info_name_tv.setText(ToolsHelper.isNullString(dataBean.getVehicleBrandName()) + ToolsHelper.isNullString(dataBean.getVehicleSeriesName()));
        order_car_info_number_tv.setText(ToolsHelper.isNullString(dataBean.getPlateLicenseNo()));

        prompt_take_name.setText(dataBean.getReservationLocationName());
        prompt_take_address.setText(dataBean.getReservationLocationAddress());

        circle_progressbar.setProgressColor(getResources().getColor(R.color.important_color_blue));
        circle_progressbar.setCountdownProgressListener(1, progressListener);
        circle_progressbar.setProgressLineWidth(getResources().getDimensionPixelOffset(R.dimen.circle_progressbar_width));
        top_title_tv.setText("取车");
        if (dataBean.getLimitTime() > 0) {//暂定倒计时最大为15分钟
            circle_progressbar.setTimeMillis(Long.parseLong(CacheHelper.getPickUpTime()), dataBean.getLimitTime());//毫秒
            initCountDownTime(dataBean.getLimitTime());
        } else {
            circle_progressbar.setTimeMillis(Long.parseLong(CacheHelper.getPickUpTime()), 0);//毫秒
            initCountDownTime(0);
            popWindows();
        }
        circle_progressbar.reStart();
    }


    /**
     * 根据倒计时显示tips提示
     *
     * @param time
     */
    protected void initCountDownTime(long time) {
        if (time > 300000) {
            take_tips.setText(String.format(getString(R.string.take_car_tips), getString(R.string.take_car_content)));
        } else {
            String ms = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT9, time);
            take_tips.setText(String.format(getString(R.string.take_car_tips), ms));
        }
    }

    @OnClick({R.id.top_back_btn, R.id.take_car_navigation_lyt,
            R.id.top_right_tv, R.id.take_car_seek_lyt, R.id.take_car_customer_lyt,
            R.id.take_car_lock, R.id.take_car_lamp_lyt, R.id.order_protocol_tv, R.id.chargingrule
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.take_car_navigation_lyt:
                if (null == orderInfoModel) {
                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                    ModuleHttpApi.getExistOrder();
                    return;
                }
                IntentHelper.intentMap(mContext, orderInfoModel.getData().getReservationLocationLongitude(), orderInfoModel.getData().getReservationLocationLatitude(),false);
                break;
            case R.id.top_right_tv:
                if (null == orderInfoModel) {
                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                    ModuleHttpApi.getExistOrder();
                    return;
                }
                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        cancleOrder(false);
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx(getString(R.string.take_car_cancel_confirm)).show(findViewById(R.id.top__iv));
                break;
            case R.id.take_car_seek_lyt:
                if (null == orderInfoModel) {
                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                    ModuleHttpApi.getExistOrder();
                    return;
                }
//				LoadingDialog.showDialog(mContext, "正在通知车辆鸣笛");
                if (popuwindow != null) {
                    popuwindow.showControllerCar(this, 2, mainView);
                }
                ModuleHttpApi.reservationOrdersBlast(String.valueOf(orderInfoModel.getData().getId()).toString());
                break;
            case R.id.take_car_customer_lyt:
                PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.take_car_lock:
                if (null == orderInfoModel || !Constants.ALLOCATED.equals(orderInfoModel.getData().getStatus())) {
                    ToolsHelper.showStatus(mContext, false, "按钮不可点击");
                    return;
                }
                Intent intent = new Intent(this,CarReportConditionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FLAG","TARKE_CAR");
                bundle.putSerializable("TAKEMODEL",orderInfoModel);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.take_car_lamp_lyt://闪灯
//				LoadingDialog.showDialog(mContext, "正在通知车辆闪灯");
                if (popuwindow != null) {
                    popuwindow.showControllerCar(this, 3, mainView);
                }
                ModuleHttpApi.reservationOrdersFlash(String.valueOf(orderInfoModel.getData().getId()));
                break;
            case R.id.order_protocol_tv:
                WebViewActivityHelper.startWebViewActivity(mContext, ModuleUrls.getPrecautionsForVehicles(), getString(R.string.order_yonghuzhuyishixiang));
                break;
            case R.id.chargingrule:
                if (null == orderInfoModel) {
                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                    ModuleHttpApi.getExistOrder();
                    return;
                }
                IntentHelper.intentChargingRule(mContext, Integer.valueOf(orderInfoModel.getData().getModelId()), String.valueOf(orderInfoModel.getData().getReservationTime()));
                break;
        }
    }

    /**
     * 取消订单
     */
    private void cancleOrder(boolean isAutoCancleOrder) {
        this.isAutoCancleOrder = isAutoCancleOrder;
        if (!isAutoCancleOrder) {
            LoadingDialog.showDialog(mContext, "处理中");
        }
        ModuleHttpApi.reservationOrdersCancel(String.valueOf(orderInfoModel.getData().getId()).toString());
    }

    /**
     * 倒计时控件的回调
     */
    private CircleArcProgressbar.OnCountdownProgressListener progressListener = new CircleArcProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, long progress) {
            if (what == 1) {
                initCountDownTime(progress * 1000);
                if (progress <= 0) {
                    circle_progressbar.stop();
                    popWindows();
                }
            }
        }
    };

    private void popWindows() {
        cancleOrder(true);
        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                finish();
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setBackKey(false).setContentTx("您的订单已失效").setContentTxColor(getResources().getColor(R.color.black))
                .setConfirmTx("确认").setConfirmTxColor(getResources().getColor(R.color.important_color_blue))
                .show(mTop_iv);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != circle_progressbar)
            circle_progressbar.stop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_unregister), e);
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersBlast.equals(apiNo) || ModuleHttpApiKey.reservationOrdersCancel.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersFlash.equals(apiNo)
        )) {
            return;
        }
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getExistOrder:
                    LoadingDialog.dismissDialog();
                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
                    if (orderInfoModel == null && orderInfoModel.getData() == null)
                        return;
                    refreshView(orderInfoModel.getData());
                    break;
//                case ModuleHttpApiKey.reservationOrdersPickup:
//                    LoadingDialog.dismissDialog();
//                    Intent intent = new Intent(mContext, InspectActivity.class);
//                    intent.putExtra("id", orderInfoModel.getData().getId());
//                    startActivity(intent);
//                    finish();
//                    break;
                case ModuleHttpApiKey.reservationOrdersBlast:
                case ModuleHttpApiKey.reservationOrdersFlash:
//					LoadingDialog.dismissDialog();
                    if (popuwindow != null) {
                        popuwindow.dismissControlCarPopuWindow();
                    }
//					ToolsHelper.showStatus(mContext, true, "操作成功");
                    break;
                case ModuleHttpApiKey.reservationOrdersCancel:
                    LoadingDialog.dismissDialog();
                    if (!isAutoCancleOrder) {
                        ToolsHelper.showStatus(mContext, true, getString(R.string.take_car_cancel_order));
                        finish();
                    }
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo) || ModuleHttpApiKey.reservationOrdersPickup.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersBlast.equals(apiNo) || ModuleHttpApiKey.reservationOrdersCancel.equals(apiNo))) {
            return;
        }

        switch (apiNo) {
            case ModuleHttpApiKey.reservationOrdersCancel:
                LoadingDialog.dismissDialog();
                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
                break;
            case ModuleHttpApiKey.getExistOrder:
                LoadingDialog.dismissDialog();
                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersPickup:
                LoadingDialog.dismissDialog();
                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersBlast:
                LoadingDialog.dismissDialog();
                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
                break;
        }


    }

    /**
     * 订单已取消
     *
     * @param event
     */
    public void onEventMainThread(OrderCancelEvent event) {
//        popWindows();
    }
}