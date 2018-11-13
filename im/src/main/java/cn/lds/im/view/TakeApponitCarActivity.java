//package cn.lds.im.view;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.Constants;
//import cn.lds.chatcore.common.GsonImplHelp;
//import cn.lds.chatcore.common.LoadingDialog;
//import cn.lds.chatcore.common.LogHelper;
//import cn.lds.chatcore.common.PopWindowHelper;
//import cn.lds.chatcore.common.PopWindowListener;
//import cn.lds.chatcore.common.TimeHelper;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.common.WebViewActivityHelper;
//import cn.lds.chatcore.data.HttpResult;
//import cn.lds.chatcore.event.DeliverAcceptEvent;
//import cn.lds.chatcore.event.DeliverCompleteEvent;
//import cn.lds.chatcore.event.HttpRequestErrorEvent;
//import cn.lds.chatcore.event.HttpRequestEvent;
//import cn.lds.chatcore.event.OrderAssignEvent;
//import cn.lds.chatcore.event.OrderCancelEvent;
//import cn.lds.chatcore.manager.ImageLoaderManager;
//import cn.lds.chatcore.view.BaseActivity;
//import cn.lds.im.common.IntentHelper;
//import cn.lds.im.common.ModuleHttpApi;
//import cn.lds.im.common.ModuleHttpApiKey;
//import cn.lds.im.common.ModuleUrls;
//import cn.lds.im.data.OrderInfoModel;
//import cn.lds.im.enums.BaseOrdersProcessType;
//import cn.lds.im.enums.TripStatus;
//import cn.lds.im.view.widget.CircleArcProgressbar;
//import cn.lds.im.view.widget.ProgressLayout;
//import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
//import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshScrollView;
//import de.greenrobot.event.EventBus;
//
///**
// * 等待外勤送车的取车界面
// */
//public class TakeApponitCarActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {
//    //topbar
//    @ViewInject(R.id.top_back_btn)
//    protected Button top_back_btn;
//    @ViewInject(R.id.top_title_tv)
//    protected TextView top_title_tv;
//    // R.id.top__iv
//    @ViewInject(R.id.top__iv)
//    protected ImageView mTop_iv;
//    @ViewInject(R.id.item_usercar_iv)
//    protected ImageView item_usercar_iv;//车辆图片
//    @ViewInject(R.id.item_usercar_sustainedMileage)
//    protected TextView item_usercar_sustainedMileage;//续航里程
//    @ViewInject(R.id.usercar_minprice)
//    protected TextView usercar_minprice;//计费规则
//    @ViewInject(R.id.order_car_info_name_tv)
//    protected TextView order_car_info_name_tv;//车辆品牌
//    @ViewInject(R.id.order_car_info_number_tv)
//    protected TextView order_car_info_number_tv;//车牌号
//    @ViewInject(R.id.prompt_take_name)
//    protected TextView prompt_take_name;//取车名称
//    @ViewInject(R.id.prompt_take_address)
//    protected TextView prompt_take_address;//取车位置
//    @ViewInject(R.id.prompt_take_station_name)
//    protected TextView prompt_take_station_name;//取车场站名称
//    @ViewInject(R.id.prompt_take_station_address)
//    protected TextView prompt_take_station_address;//取车场站位置
//    @ViewInject(R.id.prompt_take_time)
//    protected TextView prompt_take_time;//自取车时间
//    @ViewInject(R.id.circle_progressbar)
//    protected CircleArcProgressbar circle_progressbar;
//    @ViewInject(R.id.top_right_tv)
//    protected TextView top_right_tv;
//    @ViewInject(R.id.progresslayout)
//    protected ProgressLayout progressLayout;
//    @ViewInject(R.id.take_tips)
//    protected TextView take_tips;
//    @ViewInject(R.id.scrollView)
//    protected PullToRefreshScrollView scrollView;
//    @ViewInject(R.id.no_layout)
//    protected LinearLayout no_layout;//车牌号外层布局
//    @ViewInject(R.id.take_car_lock)
//    protected TextView take_car_lock;
//    @ViewInject(R.id.chargingrule)
//    protected ImageView chargingrule;//计费规则图标
//    @ViewInject(R.id.prompt_take_time_layout)
//    protected RelativeLayout prompt_take_time_layout;//取车时间
//    protected long time;
//
//    protected OrderInfoModel orderInfoModel;//订单数据
//    protected boolean isDeliverCompleted = false;
//    protected String deliverFfsPhone;//送车上门外勤联系方式 ,
//    protected CountDownTimer countDownTimer;
//
//    protected TakeCarActivity activity;
//    protected int layoutID = R.layout.activity_take_apponit_car;
//
//    public void setActivity(TakeCarActivity activity) {
//        this.activity = activity;
//    }
//
//    public void setLayoutID(int layoutID) {
//        this.layoutID = layoutID;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(layoutID);
//        ViewUtils.inject(TakeApponitCarActivity.class, this);
//        if (null != activity) {
//            ViewUtils.inject(activity);
//        }
//
//        initConfig();
//
//    }
//
//    protected void initConfig() {
//        init();
//        initData();
//    }
//
//    protected void init() {
//        top_back_btn.setVisibility(View.VISIBLE);
//        top_title_tv.setVisibility(View.VISIBLE);
//        top_title_tv.setText("取车");
//        top_right_tv.setVisibility(View.VISIBLE);//送车上门取车界面暂定隐藏取消订单功能
//        top_right_tv.setText("取消订单");
//
//        scrollView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
//        //上拉监听函数
//        scrollView.setOnRefreshListener(this);
//        circle_progressbar.setIsShowText(false);
//        circle_progressbar.setProgressLineWidth(getResources().getDimensionPixelOffset(R.dimen.circle_progressbar_width));
//    }
//
//    protected void initData() {
//        try {
//            EventBus.getDefault().register(this);
//        } catch (Exception e) {
//            LogHelper.e(getString(R.string.default_bus_register), e);
//        }
//        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//        ModuleHttpApi.getExistOrder();
//
//    }
//
//    protected void refreshView(OrderInfoModel.DataBean dataBean) {
//        ImageLoaderManager.getInstance().displayImageForCar(mContext, dataBean.getVehiclePicId(), item_usercar_iv);
//        if (dataBean.getSustainedMileage() > 0) {
//            item_usercar_sustainedMileage.setText(String.format(mContext.getString(R.string.sustained_mileage), String.valueOf(dataBean.getSustainedMileage())));
//            progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getSustainedMileage());
//        } else {
//            progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getMaxSustainedMileage());
//            item_usercar_sustainedMileage.setText("");
//        }
//        order_car_info_name_tv.setText(ToolsHelper.isNullString(dataBean.getVehicleBrandName()) + ToolsHelper.isNullString(dataBean.getVehicleSeriesName()));
//        order_car_info_number_tv.setText(ToolsHelper.isNullString(dataBean.getPlateLicenseNo()));
//
//        prompt_take_station_name.setText(dataBean.getReservationLocationName());
//        prompt_take_station_address.setText(dataBean.getReservationLocationAddress());
//        prompt_take_name.setText(dataBean.getDeliverName());
//        prompt_take_address.setText(dataBean.getDeliverAddress());
//
//        initTakeTime(dataBean);
//        deliverFfsPhone = dataBean.getDeliverFfsPhone();
//        initCarLayout(dataBean);
//        if (BaseOrdersProcessType.SCHEDULED.name().equals(dataBean.getType()) && dataBean.getLimitTime() > 0 && !dataBean.isScheduledTimeUp()) {
//            initCountDownTimer(dataBean.getLimitTime());
//        }
//        if (ToolsHelper.isNull(deliverFfsPhone)) {//外勤是否接单
//            initSendCarType(dataBean.getStatus(), dataBean.getType(), dataBean.isDeliverCompleted(), false);
//        } else {
//            initSendCarType(dataBean.getStatus(), dataBean.getType(), dataBean.isDeliverCompleted(), true);
//        }
//    }
//
//
//    /**
//     * 根据车辆状态设置车辆相关布局
//     *
//     * @param dataBean
//     */
//    protected void initCarLayout(OrderInfoModel.DataBean dataBean) {
//        if (TripStatus.OPEN.equals(dataBean.getStatus())) {
//            no_layout.setVisibility(View.GONE);
//            usercar_minprice.setVisibility(View.VISIBLE);
//            usercar_minprice.setText(dataBean.getChargingRule());
//            chargingrule.setVisibility(View.VISIBLE);
//            progressLayout.setVisibility(View.GONE);
//        } else if (TripStatus.ALLOCATED.equals(dataBean.getStatus())) {
//            no_layout.setVisibility(View.VISIBLE);
//            progressLayout.setVisibility(View.VISIBLE);
//            usercar_minprice.setVisibility(View.VISIBLE);
//            usercar_minprice.setText(dataBean.getChargingRule());
//            chargingrule.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     * 设置取车时间布局显示
//     *
//     * @param dataBean
//     */
//    protected void initTakeTime(OrderInfoModel.DataBean dataBean) {
//        if (BaseOrdersProcessType.SCHEDULED.name().equals(dataBean.getType())) {
//            prompt_take_time_layout.setVisibility(View.VISIBLE);
//            prompt_take_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, dataBean.getScheduledPickedUpTime()));
//        } else {
//            prompt_take_time_layout.setVisibility(View.GONE);
//            prompt_take_time.setText("");
//        }
//    }
//
//    /**
//     * 设置外勤送车状态
//     *
//     * @param deliverCompleted
//     */
//    protected void initSendCarType(String status, String type, boolean deliverCompleted, boolean isDeliverReceive) {
//        this.isDeliverCompleted = deliverCompleted;
//        if (TripStatus.OPEN.equals(status)) {
//            circle_progressbar.setProgressColor(getResources().getColor(R.color.important_color_blue));
//            take_tips.setText("等待系统分配车辆");
//            return;
//        }
//        if (TripStatus.ALLOCATED.equals(status) && !isDeliverReceive) {
//            if (BaseOrdersProcessType.SCHEDULED.name().equals(type)) {
//                circle_progressbar.setProgressColor(getResources().getColor(R.color.important_color_blue));
//                take_tips.setText("系统已分配车辆");
//                return;
//            } else {
//                circle_progressbar.setProgressColor(getResources().getColor(R.color.important_color_blue));
//                take_tips.setText("请等待外勤接单");
//                return;
//            }
//
//        }
////        if (!isDeliverReceive) {
////            circle_progressbar.setProgressColor(getResources().getColor(R.color.important_color_blue));
////            take_tips.setText("请等待外勤接单");
////            return;
////        }
//        if (deliverCompleted) {
//            circle_progressbar.setProgressColor(getResources().getColor(R.color.normal_send_car_text_color));
////            take_tips.setText("外勤已到达，您的车牌号是：" + dataBean.getPlateLicenseNo());
//            take_tips.setText("外勤已到达");
//            setCountDownTimerStatus();
//        } else {
//            circle_progressbar.setProgressColor(getResources().getColor(R.color.important_color_blue));
////            take_tips.setText("外勤正在赶来，您的车牌号是：" + dataBean.getPlateLicenseNo());
//            take_tips.setText("外勤已接单正在赶来");
//        }
//
//    }
//
//    @OnClick({R.id.top_back_btn, R.id.take_car_navigation_lyt,
//            R.id.top_right_tv, R.id.take_car_customer_lyt,
//            R.id.take_car_lock, R.id.take_car_persion_lyt, R.id.order_protocol_tv, R.id.chargingrule
//    })
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.top_back_btn:
//                finish();
//                break;
//            case R.id.take_car_navigation_lyt://导航
//                if (null == orderInfoModel) {
//                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//                    ModuleHttpApi.getExistOrder();
//                    return;
//                }
//                IntentHelper.intentMap(mContext, orderInfoModel.getData().getDeliverLongitude(), orderInfoModel.getData().getDeliverLatitude(),false);
//                break;
//            case R.id.top_right_tv://取消订单按钮
//                if (null == orderInfoModel) {
//                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//                    ModuleHttpApi.getExistOrder();
//                    return;
//                }
//                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
//                    @Override
//                    public void confirm() {
//                        LoadingDialog.showDialog(mContext, "取消订单");
//                        ModuleHttpApi.reservationOrdersCancel(String.valueOf(orderInfoModel.getData().getId()).toString());
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//
//                    @Override
//                    public void onItemListener(int position) {
//
//                    }
//                }).setContentTx(getString(R.string.take_car_cancel_confirm)).show(findViewById(R.id.top__iv));
//                break;
//            case R.id.take_car_customer_lyt://客服
//                PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));
//                break;
//            case R.id.take_car_lock://取电子钥匙
//                if (null == orderInfoModel || !Constants.ALLOCATED.equals(orderInfoModel.getData().getStatus())) {
//                    ToolsHelper.showStatus(mContext, false, "按钮不可点击");
//                    return;
//                }
//                if (!isDeliverCompleted) {
////					PopWindowHelper.getInstance().confirmTips(mContext, new PopWindowListener() {
////						@Override
////						public void confirm() {
//////							getReservationOrdersPickup();
////						}
////
////						@Override
////						public void cancel() {
////
////						}
////
////						@Override
////						public void onItemListener(int position) {
////
////						}
////					}).show(findViewById(R.id.top__iv));
//                    PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
//                        @Override
//                        public void confirm() {
//                        }
//
//                        @Override
//                        public void cancel() {
//
//                        }
//
//                        @Override
//                        public void onItemListener(int position) {
//
//                        }
//                    }).setBackKey(false).setContentTx("您选择了送车上门服务，请等待外勤人员").setContentTxColor(getResources().getColor(R.color.black))
//                            .setConfirmTx("确认").setConfirmTxColor(getResources().getColor(R.color.important_color_blue))
//                            .show(mTop_iv);
//                } else {
//                    PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
//                        @Override
//                        public void confirm() {
//                            getReservationOrdersPickup();
//                        }
//
//                        @Override
//                        public void cancel() {
//
//                        }
//
//                        @Override
//                        public void onItemListener(int position) {
//
//                        }
//                    }).setContentTx("取电子钥匙后开始收费").show(findViewById(R.id.top__iv));
//                }
//                break;
//            case R.id.take_car_persion_lyt://联系外勤
//                if (null == orderInfoModel) {
//                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//                    ModuleHttpApi.getExistOrder();
//                    return;
//                }
//                if (!ToolsHelper.isNull(orderInfoModel.getData().getDeliverFfsPhone())) {
//                    PopWindowHelper.getInstance().openTel(mContext, orderInfoModel.getData().getDeliverFfsPhone()).show(findViewById(R.id.top__iv));
//                } else {
//                    ToolsHelper.showStatus(mContext, true, "外勤还未接单");
//                }
//                break;
//            case R.id.order_protocol_tv:
//                WebViewActivityHelper.startWebViewActivity(mContext, ModuleUrls.getPrecautionsForVehicles(), getString(R.string.order_yonghuzhuyishixiang));
//                break;
//            case R.id.chargingrule:
//                if (null == orderInfoModel) {
//                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//                    ModuleHttpApi.getExistOrder();
//                    return;
//                }
//                if(TripStatus.OPEN.equals(orderInfoModel.getData().getStatus())){
//                    IntentHelper.intentChargingRule(mContext, Integer.valueOf(orderInfoModel.getData().getId()), String.valueOf(orderInfoModel.getData().getReservationTime()));
//                }else{
//                    IntentHelper.intentChargingRule(mContext, Integer.valueOf(orderInfoModel.getData().getModelId()), String.valueOf(orderInfoModel.getData().getReservationTime()));
//
//                }
//
//                break;
//        }
//    }
//
//    /**
//     * 取电子钥匙
//     */
//    protected void getReservationOrdersPickup() {
//        if (null == orderInfoModel) {
//            LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//            ModuleHttpApi.getExistOrder();
//            return;
//        }
//        LoadingDialog.showDialog(mContext, getString(R.string.take_car_open_loading));
////        ModuleHttpApi.reservationOrdersPickup(String.valueOf(orderInfoModel.getData().getId()), requestData);
//    }
//
//
//    protected void popWindows() {
//        if (PopWindowHelper.getInstance().isShow()) {
//            PopWindowHelper.getInstance().cancle();
//        }
//        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
//            @Override
//            public void confirm() {
//                finish();
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//
//            @Override
//            public void onItemListener(int position) {
//
//            }
//        }).setBackKey(false).setContentTx("您的订单已失效").setContentTxColor(getResources().getColor(R.color.black))
//                .setConfirmTx("确认").setConfirmTxColor(getResources().getColor(R.color.important_color_blue))
//                .show(mTop_iv);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        try {
//            EventBus.getDefault().unregister(this);
//        } catch (Exception e) {
//            LogHelper.e(getString(R.string.default_bus_unregister), e);
//        }
//    }
//
//    public void onEventMainThread(HttpRequestEvent event) {
//        HttpResult httpResult = event.httpResult;
//        String apiNo = httpResult.getApiNo();
//        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo) || ModuleHttpApiKey.reservationOrdersPickup.equals(apiNo)
//                || ModuleHttpApiKey.reservationOrdersCancel.equals(apiNo)
//        )) {
//            return;
//        }
//        String result = httpResult.getResult();
//        if (!ToolsHelper.isNull(result)) {
//
//            switch (apiNo) {
//                case ModuleHttpApiKey.getExistOrder:
//                    LoadingDialog.dismissDialog();
//                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
//                    if (orderInfoModel == null && orderInfoModel.getData() == null)
//                        return;
//                    refreshView(orderInfoModel.getData());
//                    if (scrollView.isRefreshing()) {
//                        scrollView.onRefreshComplete();
//                    }
//                    break;
//                case ModuleHttpApiKey.reservationOrdersPickup:
//                    LoadingDialog.dismissDialog();
//                    Intent intent = new Intent(mContext, InspectActivity.class);
//                    intent.putExtra("id", orderInfoModel.getData().getId());
//                    startActivity(intent);
//                    finish();
//                    break;
//                case ModuleHttpApiKey.reservationOrdersCancel:
//                    try {
//                        LoadingDialog.dismissDialog();
//                        ToolsHelper.showStatus(mContext, true, getString(R.string.take_car_cancel_order));
//                        JSONObject jsonObject = new JSONObject(result);
//                        boolean data = jsonObject.optBoolean("data");
//                        if (data) {
//                            startActivity(new Intent(mContext, OrderDetailActivity.class));
//                        }
//                        finish();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//
//    }
//
//    public void onEventMainThread(HttpRequestErrorEvent event) {
//        HttpResult httpResult = event.httpResult;
//        String apiNo = httpResult.getApiNo();
//        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo) || ModuleHttpApiKey.reservationOrdersPickup.equals(apiNo)
//                || ModuleHttpApiKey.reservationOrdersCancel.equals(apiNo))) {
//            return;
//        }
//
//        switch (apiNo) {
//            case ModuleHttpApiKey.reservationOrdersCancel:
//                LoadingDialog.dismissDialog();
//                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
//                break;
//            case ModuleHttpApiKey.getExistOrder:
//                LoadingDialog.dismissDialog();
//                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
//                if (scrollView.isRefreshing()) {
//                    scrollView.onRefreshComplete();
//                }
//                break;
//            case ModuleHttpApiKey.reservationOrdersPickup:
//                LoadingDialog.dismissDialog();
//                ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
//                break;
//        }
//
//
//    }
//
//    /**
//     * 订单已取消
//     *
//     * @param event
//     */
//    public void onEventMainThread(OrderCancelEvent event) {
//        popWindows();
//    }
//
//    /**
//     * 送车上门外勤接受工单
//     *
//     * @param event
//     */
//    public void onEventMainThread(DeliverAcceptEvent event) {
////        ToolsHelper.showStatus(mContext, true, event.getMessage().getContent());
//        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//        ModuleHttpApi.getExistOrder();
////        popDeliver(event.getMessage().getContent());
//        Toast.makeText(this,event.getMessage().getContent(),Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 外勤送车完成
//     *
//     * @param event
//     */
//    public void onEventMainThread(DeliverCompleteEvent event) {
////        ToolsHelper.showStatus(mContext, true, event.getMessage().getContent());
//        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//        ModuleHttpApi.getExistOrder();
//        popDeliver(event.getMessage().getContent());
//    }
//
//    /**
//     * 订单已分配
//     *
//     * @param event
//     */
//    public void onEventMainThread(OrderAssignEvent event) {
////        ToolsHelper.showStatus(mContext, true, event.getMessage().getContent());
//        Toast.makeText(mContext,event.getMessage().getContent(),Toast.LENGTH_SHORT).show();
//        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
//        ModuleHttpApi.getExistOrder();
//    }
//
//    @Override
//    public void onRefresh(PullToRefreshBase refreshView) {
//        ModuleHttpApi.getExistOrder();
//    }
//
//    /**
//     * 外勤确认相关处理
//     */
//    protected void popDeliver(String content) {
//        if (PopWindowHelper.getInstance().isShow()) {
//            PopWindowHelper.getInstance().cancle();
//        }
//        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
//            @Override
//            public void confirm() {
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//
//            @Override
//            public void onItemListener(int position) {
//
//            }
//        }).setBackKey(false).setContentTx(content).setContentTxColor(getResources().getColor(R.color.black))
//                .setConfirmTx("确认").setConfirmTxColor(getResources().getColor(R.color.important_color_blue))
//                .show(mTop_iv);
//    }
//
//
//    /**
//     * 倒计时
//     *
//     * @param millisInFuture
//     */
//    protected void initCountDownTimer(long millisInFuture) {
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        take_car_lock.setEnabled(false);
//        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                take_car_lock.setText("距离用车时间还有" + initTime(millisUntilFinished));
////				LogHelper.d(millisUntilFinished + "");
//            }
//
//            @Override
//            public void onFinish() {
//                take_car_lock.setEnabled(true);
//                take_car_lock.setText(getString(R.string.take_car_lock));
//            }
//        }.start();
//    }
//
//    /**
//     * 结束倒计时，并设置按钮状态
//     */
//    protected void setCountDownTimerStatus() {
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        take_car_lock.setEnabled(true);
//        take_car_lock.setText(getString(R.string.take_car_lock));
//    }
//
//    /**
//     * 格式化时间戳
//     *
//     * @param time
//     * @return
//     */
//    protected String initTime(long time) {
//        long hour = time / 60 / 60 / 1000;
//        String ms = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT9, time);
//        return String.format("%02d", hour) + ":" + ms;
//    }
//}