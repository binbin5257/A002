package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PaymentModel;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MessagePay;
import cn.lds.chatcore.event.RequestPay;
import cn.lds.chatcore.event.StartPay;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.manager.PayManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.data.PayInfoModel;
import de.greenrobot.event.EventBus;

public class PayActivity extends BaseActivity {
    //topbar
    @ViewInject(R.id.top_back_btn)
    protected Button top_back_btn;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView top_menu_btn_del;
    @ViewInject(R.id.pay_confirm_tv)
    protected TextView pay_pay_btn;
    @ViewInject(R.id.pay_icon_iv)
    protected ImageView pay_icon_iv;
    @ViewInject(R.id.pay_info_name_tv)
    protected TextView pay_info_name_tv;
    @ViewInject(R.id.pay_info_number_tv)
    protected TextView pay_info_number_tv;
    @ViewInject(R.id.pay_trip_tv)
    protected TextView pay_trip_tv;
    @ViewInject(R.id.pay_time_tv)
    protected TextView pay_time_tv;
    @ViewInject(R.id.pay_address_reservation_name_tv)
    protected TextView pay_address_reservation_name_tv;
    @ViewInject(R.id.pay_address_reservation_time_tv)
    protected TextView pay_address_reservation_time_tv;
    @ViewInject(R.id.pay_address_return_name_tv)
    protected TextView pay_address_return_name_tv;
    @ViewInject(R.id.pay_address_return_time_tv)
    protected TextView pay_address_return_time_tv;
    @ViewInject(R.id.pay_order_info_odometer_tv)
    protected TextView pay_order_info_odometer_tv;
    @ViewInject(R.id.pay_order_info_time_tv)
    protected TextView pay_order_info_time_tv;
    @ViewInject(R.id.pay_order_info_odometer_cost_tv)
    protected TextView pay_order_info_odometer_cost_tv;
    @ViewInject(R.id.pay_order_info_time_cost_tv)
    protected TextView pay_order_info_time_cost_tv;
    @ViewInject(R.id.pay_order_info_amountPayable_tv)
    protected TextView pay_order_info_amountPayable_tv;
    @ViewInject(R.id.pay_order_info_amountAccount_tv)
    protected TextView pay_order_info_amountAccount_tv;
    @ViewInject(R.id.order_payment_sum_tv)
    protected TextView order_payment_sum_tv;
    @ViewInject(R.id.pay_payment_method_ryt)
    protected RelativeLayout pay_payment_method_ryt;
    @ViewInject(R.id.pay_payment_method_tv)
    protected TextView pay_payment_method_tv;
    @ViewInject(R.id.pay_payment_method_iv)
    protected ImageView pay_payment_method_iv;


    protected OrderInfoModel orderInfoModel;
    protected PayInfoModel payInfoModel;
    protected String id;
    /**
     * 支付方式数据
     */
    protected List<PopData> lists = new ArrayList<>();
    protected int select = 0;//当前支付方式

    protected PayActivity activity;
    protected int layoutID = R.layout.activity_pay;

    public void setActivity(PayActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(PayActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }

        initConfig();

    }

    protected void initConfig() {
        init();
        initData();
        refreshView();//刷新画面
    }

    protected void refreshView() {
        putPaymentMethod(select);
        if (null != orderInfoModel) {

            if (Constants.PAID.equals(orderInfoModel.getData().getStatus())) {
                if (isTimeOut) {
                    ToolsHelper.showStatus(mContext, true, "支付完成");
                    finish();
                }
            } else {
                if (isTimeOut) {
                    ToolsHelper.showStatus(mContext, false, "获取支付结果超时");
                    isTimeOut = false;
                }
            }

            if (ToolsHelper.isNull(orderInfoModel.getData().getVehiclePicId()))
                pay_icon_iv.setImageResource(R.drawable.car_default);
            else
                ImageLoaderManager.getInstance().displayImageForCar(mContext, orderInfoModel.getData().getVehiclePicId(), pay_icon_iv);
            pay_info_name_tv.setText(ToolsHelper.isNullString(orderInfoModel.getData().getVehicleBrandName()) + " " + ToolsHelper.isNullString(orderInfoModel.getData().getVehicleSeriesName()));
            pay_info_number_tv.setText(ToolsHelper.isNullString(orderInfoModel.getData().getPlateLicenseNo()));
            pay_trip_tv.setText(String.format(getString(R.string.pay_trip), ToolsHelper.getDouble(orderInfoModel.getData().getOdometer())));
//            long countTime = (orderInfoModel.getData().getDroppedOffTime() - orderInfoModel.getData().getPickedUpTime()) / 1000;
//            long countTime = orderInfoModel.getData().getTime();
//            pay_time_tv.setText(ToolsHelper.isNullString(String.format(getString(R.string.pay_time_text), countTime / 60, countTime % 60)));
            pay_address_reservation_name_tv.setText(ToolsHelper.isNullString(orderInfoModel.getData().getReservationLocationName()));
//            pay_address_reservation_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT6, orderInfoModel.getData().getPickedUpTime()));
            pay_address_reservation_time_tv.setText(orderInfoModel.getData().getPickedUpTime()+"");
            pay_address_return_name_tv.setText(ToolsHelper.isNullString(orderInfoModel.getData().getReturnLocationName()));
//            pay_address_return_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT6, orderInfoModel.getData().getDroppedOffTime()));
            pay_address_return_time_tv.setText(orderInfoModel.getData().getDroppedOffTime()+"");

        }
        if (null != payInfoModel) {
            pay_order_info_odometer_tv.setText(ToolsHelper.getDouble(payInfoModel.getData().getOdometer()));
            pay_order_info_time_tv.setText(ToolsHelper.getDouble(payInfoModel.getData().getTime()));
            pay_order_info_odometer_cost_tv.setText("0.00");
            pay_order_info_time_cost_tv.setText(ToolsHelper.getDouble(payInfoModel.getData().getMinPrice() / 100));
            pay_order_info_amountPayable_tv.setText(ToolsHelper.getDouble(payInfoModel.getData().getAmountPayable() / 100));
            pay_order_info_amountAccount_tv.setText(ToolsHelper.getDouble(payInfoModel.getData().getAccountAmount()));
            order_payment_sum_tv.setText(ToolsHelper.getDouble(payInfoModel.getData().getAmount() / 100));
        }
    }

    protected void initData() {
        if (ToolsHelper.isNull(id))
            ModuleHttpApi.getExistOrder();
        else
            ModuleHttpApi.reservationOrders(id);
        lists.addAll(ToolsHelper.getPayType(false));
        select = 0;
    }

    protected void putPaymentMethod(int position) {
        PopData data = lists.get(position);
        pay_payment_method_tv.setText(data.getText());
        pay_payment_method_iv.setImageResource(data.getImageID());
    }

    protected void init() {
        Intent intent = getIntent();
        if (null != intent)
            id = intent.getStringExtra("id");
        top_back_btn.setVisibility(View.VISIBLE);
        top_title_tv.setVisibility(View.VISIBLE);
        top_title_tv.setText("支付订单");
        top_menu_btn_del.setImageResource(R.drawable.topbar_menu_customerservice);
        top_menu_btn_del.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.pay_confirm_tv, R.id.top_back_btn, R.id.top_menu_btn_del, R.id.pay_payment_method_ryt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.pay_payment_method_ryt:
                PopWindowHelper.getInstance().menu(mContext, lists, true, new PopWindowListener() {
                    @Override
                    public void confirm() {

                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {
                        lists.get(select).setSelect(false);
                        lists.get(position).setSelect(true);
                        select = position;
                        putPaymentMethod(select);
                    }
                }).setBackKey(true).show(findViewById(R.id.top__iv));
                break;
            case R.id.pay_confirm_tv:
                if (null == orderInfoModel || null == orderInfoModel.getData()) {
                    ToolsHelper.showStatus(mContext, false, "获取订单信息出错,请稍后再试一次");
                    initData();
                    return;
                }
               // PayManager.getInstance().reservationOrdersPay(String.valueOf(orderInfoModel.getData().getId()).toString(),
//                        pay_payment_method_tv.getText().toString(), PayActivity.this);

                break;
        }
    }

    public void fin() {
        Message message = new Message();
        message.what = 1;
        handler.sendMessageDelayed(message, 2000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    finish();
                    break;
                case 100:

                    CoreHttpApi.payment(PayManager.getInstance().businessId);

                    break;
            }
        }
    };
    protected boolean isTimeOut = false;

    @Override
    public void onStart() {
        super.onStart();
        LogHelper.e("onStart");
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(mContext.getString(R.string.default_bus_register), e);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.e("onStop");
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(mContext.getString(R.string.default_bus_unregister), e);
        }
    }


    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrders.equals(apiNo)
                || ModuleHttpApiKey.payment.equals(apiNo)
                || ModuleHttpApiKey.reservationOrderPaymentsDetail.equals(apiNo))) {
            return;
        }
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getExistOrder:
//                    LoadingDialog.dismissDialog();
//                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
//                    refreshView();//刷新画面
//                    break;
                case ModuleHttpApiKey.reservationOrders:
                    LoadingDialog.dismissDialog();
                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
                    if (null == orderInfoModel || ToolsHelper.isNull(String.valueOf(orderInfoModel.getData().getId()).toString())) {
                        ToolsHelper.showStatus(mContext, false, "获取订单信息失败");
                        return;
                    }
                    refreshView();//刷新画面
                    LoadingDialog.showDialog(mContext, "获取费用信息,请稍候");
                    ModuleHttpApi.reservationOrderPaymentsDetail(String.valueOf(orderInfoModel.getData().getId()).toString());
                    break;

                case ModuleHttpApiKey.reservationOrderPaymentsDetail:
                    LoadingDialog.dismissDialog();
                    payInfoModel = GsonImplHelp.get().toObject(result, PayInfoModel.class);
                    refreshView();//刷新画面
                    break;
                case ModuleHttpApiKey.payment:
                    PaymentModel paymentModel = GsonImplHelp.get().toObject(result, PaymentModel.class);

                    if (paymentModel.isData()) {
                        MainActivity mainActivity = (MainActivity) MyApplication.getInstance().getMainActivity();
                        if (null != mainActivity)
                            MyApplication.getInstance().setCurrent(2);
                        ToolsHelper.showStatus(mContext, true, "支付订单完成");
                        finish();
                    } else {
                        ToolsHelper.showStatus(mContext, false, "获取支付结果超时");
                        isTimeOut = false;
                        LoadingDialog.dismissDialog();
                    }
                    break;
            }
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrders.equals(apiNo)
                || ModuleHttpApiKey.payment.equals(apiNo)
                || ModuleHttpApiKey.reservationOrderPaymentsDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
        switch (apiNo) {
            case ModuleHttpApiKey.payment:
                LoadingDialog.dismissDialog();
                isTimeOut = true;
                initData();
                break;
        }
    }

    /**
     * 是否开始支付，如果开始支付则弹出LoadingDialog
     *
     * @param event
     */
    public void onEventMainThread(StartPay event) {
        if (2 != event.getType())
            return;
        LoadingDialog.dismissDialog();
        if (!event.getStatus()) {
            if (null == event.getHttpResult())
                ToolsHelper.showStatus(mContext, false, "获取支付订单失败");
            else
                ToolsHelper.showHttpRequestErrorMsg(mContext, event.getHttpResult());
        } else {
            LoadingDialog.showDialog(mContext, "正在执行支付操作");
        }
    }

    /**
     * 支付结果确认---失败时执行：：成功时等待服务器推送
     *
     * @param event
     */
    public void onEventMainThread(RequestPay event) {
        if (0 == event.getStatus()) {
            Message message = new Message();
            message.what = 100;
            handler.sendMessageDelayed(message, 20000);
            return;
        }
        LoadingDialog.dismissDialog();
    }

    /**
     * 消息推送接收--收到成功消息finish掉支付页面
     *
     * @param event
     */
    public void onEventMainThread(MessagePay event) {
        if (2 != event.getType())
            return;
        LoadingDialog.dismissDialog();
        if (event.getStatus()) {
            MainActivity mainActivity = (MainActivity) MyApplication.getInstance().getMainActivity();
            if (null != mainActivity)
                MyApplication.getInstance().setCurrent(2);
            ToolsHelper.showStatus(mContext, true, "支付订单完成");
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.dismissDialog();
    }
}
