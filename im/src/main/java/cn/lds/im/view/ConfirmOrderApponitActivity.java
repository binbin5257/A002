package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.OrderAssignEvent;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.ModuleUrls;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.enums.TripStatus;
import cn.lds.im.view.widget.ProgressLayout;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshScrollView;
import de.greenrobot.event.EventBus;

/**
 * 预约用车的等待分配车辆界面
 */
public class ConfirmOrderApponitActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {

    /*topbar*/
    @ViewInject(R.id.top_back_btn)
    protected Button top_back_btn;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    @ViewInject(R.id.top_right_tv)
    protected TextView top_right_tv;//取消订单按钮
    @ViewInject(R.id.order_confirm_tv)
    protected TextView order_confirm_tv;//确认按钮
    @ViewInject(R.id.prompt_return_location)
    protected ImageView prompt_return_location;
    @ViewInject(R.id.send_car_rl)
    protected RelativeLayout send_car_rl;
    @ViewInject(R.id.item_usercar_iv)
    protected ImageView item_usercar_iv;//车辆图片
    @ViewInject(R.id.progresslayout)
    protected ProgressLayout progressLayout;//续航里程进度条
    @ViewInject(R.id.item_usercar_sustainedMileage)
    protected TextView item_usercar_sustainedMileage;//续航里程
    @ViewInject(R.id.usercar_minprice)
    protected TextView usercar_minprice;//起步价
    @ViewInject(R.id.no_layout)
    protected LinearLayout no_layout;//车牌号布局
    @ViewInject(R.id.order_car_info_number_tv)
    protected TextView order_car_info_number_tv;//车牌号
    @ViewInject(R.id.order_car_info_name_tv)
    protected TextView order_car_info_name_tv;//品牌
    @ViewInject(R.id.prompt_take_name)
    protected TextView prompt_take_name;//取车点名称
    @ViewInject(R.id.prompt_take_address)
    protected TextView prompt_take_address;//取车点地址
    @ViewInject(R.id.prompt_return_name)
    protected TextView prompt_return_name;//还车点名称
    @ViewInject(R.id.prompt_return_address)
    protected TextView prompt_return_address;//还车点地址
    @ViewInject(R.id.apponit_take_time)
    protected TextView apponit_take_time;//取车时间
    @ViewInject(R.id.apponit_return_time)
    protected TextView apponit_return_time;//还车时间
    @ViewInject(R.id.scrollView)
    protected PullToRefreshScrollView scrollView;
    @ViewInject(R.id.chargingrule)
    protected ImageView chargingrule;//计费规则图标
    @ViewInject(R.id.ll_order_protocol)
    protected LinearLayout ll_order_protocol;//租车协议布局

    protected OrderInfoModel orderModel;//订单数据


    private CountDownTimer countDownTimer;
    protected ConfirmOrderApponitActivity activity;
    protected int layuotID = R.layout.activity_confirm_order_apponit;

    public void setLayoutID(int layuotID) {
        this.layuotID = layuotID;
    }

    public void setActivity(ConfirmOrderApponitActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layuotID);
        ViewUtils.inject(ConfirmOrderApponitActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }

        initConfig();
    }

    protected void initConfig() {
        init();//初始化控件
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_register), e);
        }
        initData();
    }

    protected void init() {
        top_back_btn.setVisibility(View.VISIBLE);
        top_title_tv.setText("待取车");
        top_title_tv.setVisibility(View.VISIBLE);
        top_right_tv.setVisibility(View.VISIBLE);
        top_right_tv.setText("取消订单");

//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(order_confirm_tv.getLayoutParams());
//        lp.setMargins(0, 100, 0, 0);
//        order_confirm_tv.setLayoutParams(lp);
        prompt_return_location.setVisibility(View.GONE);//隐藏编辑还车点按钮
        send_car_rl.setVisibility(View.GONE);//隐藏送车上门开关
        no_layout.setVisibility(View.GONE);//车牌号布局隐藏
        ll_order_protocol.setVisibility(View.GONE);
        scrollView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        //上拉监听函数
        scrollView.setOnRefreshListener(this);
    }

    protected void initData() {
        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
        ModuleHttpApi.getExistOrder();

    }

    protected void refreshView() {
        OrderInfoModel.DataBean dataBean = orderModel.getData();
        ImageLoaderManager.getInstance().displayImageForCar(mContext, dataBean.getVehiclePicId(), item_usercar_iv);
        if (dataBean.getSustainedMileage() > 0) {
            item_usercar_sustainedMileage.setText(String.format(mContext.getString(R.string.sustained_mileage), String.valueOf(dataBean.getSustainedMileage())));
            progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getSustainedMileage());
        } else {
            progressLayout.initParams(0, dataBean.getMaxSustainedMileage(), dataBean.getMaxSustainedMileage());
            item_usercar_sustainedMileage.setText("");
        }
        order_car_info_name_tv.setText(ToolsHelper.isNullString(dataBean.getVehicleBrandName()) + " " + ToolsHelper.isNullString(dataBean.getVehicleSeriesName()));
        order_car_info_number_tv.setText(ToolsHelper.isNullString(dataBean.getPlateLicenseNo()));
        if (dataBean.isDelivered()) {
            prompt_take_name.setText(dataBean.getDeliverName());
            prompt_take_address.setText(dataBean.getDeliverAddress());
        } else {
            prompt_take_name.setText(dataBean.getReservationLocationName());
            prompt_take_address.setText(dataBean.getReservationLocationAddress());
        }
        prompt_return_name.setText(dataBean.getReturnLocationName());
        prompt_return_address.setText(dataBean.getReturnLocationAddress());

        apponit_take_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, dataBean.getScheduledPickedUpTime()));
        apponit_return_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, dataBean.getScheduledDroppedOffTime()));

        initCountDownTimer(dataBean.getLimitTime());

        initCarLayout(dataBean);
    }

    /**
     * 根据车辆状态设置车辆相关布局
     *
     * @param dataBean
     */
    protected void initCarLayout(OrderInfoModel.DataBean dataBean) {
        if (TripStatus.OPEN.equals(dataBean.getStatus())) {
            no_layout.setVisibility(View.GONE);
            usercar_minprice.setText("");
            chargingrule.setVisibility(View.GONE);
        } else if (TripStatus.ALLOCATED.equals(dataBean.getStatus())) {
            no_layout.setVisibility(View.VISIBLE);
            usercar_minprice.setText(dataBean.getChargingRule());
            chargingrule.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_right_tv,
            R.id.take_car_customer_lyt,
            R.id.order_protocol_tv,
            R.id.chargingrule
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_right_tv:
                if (null == orderModel) {
                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                    ModuleHttpApi.getExistOrder();
                    return;
                }
                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        LoadingDialog.dismissDialog();
                        LoadingDialog.showDialog(mContext, "请稍后");
                        ModuleHttpApi.reservationOrdersCancel(String.valueOf(orderModel.getData().getId()));
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx(getString(R.string.take_car_cancel_confirm)).show(findViewById(R.id.top__iv));
                break;
            case R.id.take_car_customer_lyt:
                PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.order_protocol_tv://租车协议
                WebViewActivityHelper.startWebViewActivity(mContext, ModuleUrls.getCarRentalClause(), getString(R.string.order_zuchexieyi));
                break;
            case R.id.chargingrule:
                if (null == orderModel) {
                    LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                    ModuleHttpApi.getExistOrder();
                    return;
                }
                IntentHelper.intentChargingRule(mContext, Integer.valueOf(orderModel.getData().getModelId()), String.valueOf(orderModel.getData().getReservationTime()));
                break;

        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersCancel.equals(apiNo)
        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        switch (apiNo) {
            case ModuleHttpApiKey.getExistOrder:
                LoadingDialog.dismissDialog();
                String result = httpResult.getResult();
                orderModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
                if (orderModel == null && orderModel.getData() == null) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.common_err_get));
                    return;
                }
                refreshView();
                if (scrollView.isRefreshing()) {
                    scrollView.onRefreshComplete();
                }
                break;
            case ModuleHttpApiKey.reservationOrdersCancel:
                LoadingDialog.dismissDialog();
                ToolsHelper.showStatus(mContext, true, getString(R.string.take_car_cancel_order));
                finish();
                break;
        }

    }

    /**
     * 倒计时
     *
     * @param millisInFuture
     */
    private void initCountDownTimer(long millisInFuture) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                order_confirm_tv.setText("距离取车时间还有" + initTime(millisUntilFinished));
//				LogHelper.d(millisUntilFinished + "");
            }

            @Override
            public void onFinish() {
                orderAssign();
            }
        }.start();
    }

    /**
     * 格式化时间戳
     *
     * @param time
     * @return
     */
    private String initTime(long time) {
        long hour = time / 60 / 60 / 1000;
        String ms = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT9, time);
        return String.format("%02d", hour) + ":" + ms;
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersCancel.equals(apiNo)
        )) {
            return;
        }

        LoadingDialog.dismissDialog();
        switch (apiNo) {
            case ModuleHttpApiKey.getExistOrder:
                if (scrollView.isRefreshing()) {
                    scrollView.onRefreshComplete();
                }
            case ModuleHttpApiKey.reservationOrdersCancel:
                ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
                break;
        }

    }

    /**
     * 订单已分配
     *
     * @param event
     */
    public void onEventMainThread(OrderAssignEvent event) {
        ToolsHelper.showStatus(mContext, true, event.getMessage().getContent());
        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
        ModuleHttpApi.getExistOrder();
    }

    private void orderAssign() {
        if (orderModel.getData().isDelivered()) {//是否选择送车上门
//            startActivity(new Intent(mContext, TakeApponitCarActivity.class));
        } else {
            startActivity(new Intent(mContext, TakeCarActivity.class));
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_unregister), e);
        }
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        ModuleHttpApi.getExistOrder();
    }

}
