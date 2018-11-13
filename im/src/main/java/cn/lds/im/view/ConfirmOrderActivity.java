package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.PopWindowListener2;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.ModuleUrls;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.data.GetReturnCarLocationInfo;
import cn.lds.im.data.OrderModel;
import cn.lds.im.data.OrderSendCarModel;
import cn.lds.im.data.ReservationOrdersLocationsModel;
import cn.lds.im.view.widget.ProgressLayout;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshScrollView;
import de.greenrobot.event.EventBus;

/**
 * 订单确认界面
 */
public class ConfirmOrderActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    public static final int requestCodeAddress = 1001;
    public static final int requestSendCarTakeAddress = 1002;
    /*topbar*/
    @ViewInject(R.id.top_back_btn)
    protected Button top_back_btn;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    /*控件绑定*/
    @ViewInject(R.id.item_usercar_iv)
    protected ImageView item_usercar_iv;
    @ViewInject(R.id.item_usercar_sustainedMileage)
    protected TextView item_usercar_sustainedMileage;
    @ViewInject(R.id.order_car_info_name_tv)
    protected TextView order_car_info_name_tv;
    @ViewInject(R.id.order_car_info_number_tv)
    protected TextView order_car_info_number_tv;
    @ViewInject(R.id.usercar_minprice)
    protected TextView usercar_minprice;
    @ViewInject(R.id.send_car_money)
    protected TextView send_car_money;
    protected OrderModel orderModel;
    @ViewInject(R.id.send_car_time_layout)
    protected RelativeLayout send_car_time_layout;//送车上门的取车时间
    @ViewInject(R.id.no_layout)
    protected LinearLayout no_layout;//车牌号布局
    @ViewInject(R.id.checkbox_send_car)
    protected CheckBox checkbox_send_car;//是否选中送车上门
    @ViewInject(R.id.send_car_layout)
    protected LinearLayout send_car_layout;//选中送车上门的布局
//    @ViewInject(R.id.prompt_dash_return_iv)
//    protected View prompt_dash_return_iv;//立即用車虚线还车位置布局
    @ViewInject(R.id.prompt_take_layout)
    protected RelativeLayout prompt_take_layout;//立即用车取车位置布局

    @ViewInject(R.id.prompt_take_name)
    protected TextView prompt_take_name;//立即还车取车名称
    @ViewInject(R.id.prompt_take_address)
    protected TextView prompt_take_address;//立即还车取车位置
    @ViewInject(R.id.prompt_return_name)
    protected TextView prompt_return_name;//立即还车还车名称
    @ViewInject(R.id.prompt_return_address)
    protected TextView prompt_return_address;//立即还车还车位置
    @ViewInject(R.id.apponit_take_time_layout)
    protected RelativeLayout apponit_take_time_layout;
    @ViewInject(R.id.apponit_return_time_layout)
    protected RelativeLayout apponit_return_time_layout;
    @ViewInject(R.id.apponit_take_time)
    protected TextView apponit_take_time;//预约还车取车时间
    @ViewInject(R.id.apponit_return_time)
    protected TextView apponit_return_time;//预约还车还车时间
    @ViewInject(R.id.progresslayout)
    protected ProgressLayout progressLayout;
    @ViewInject(R.id.send_car_time_tv)
    protected TextView send_car_time_tv;
    @ViewInject(R.id.send_car_location_tv)
    protected TextView send_car_location_tv;
    @ViewInject(R.id.et_phone)
    protected EditText et_phone;
    @ViewInject(R.id.scrollView)
    protected PullToRefreshScrollView scrollView;
    @ViewInject(R.id.rl_send_money)
    protected RelativeLayout rl_send_money;
    private long takeTimeApponit = 0;
    private long returnTimeApponit = 0;
    private long takeTime = 0;

    private OrderSendCarModel orderSendCarModel;
    private long sendCarTakeTime = 0;//送车上门的取车时间

    private GetReturnCarLocationInfo returnCarLocationInfo;//送车上门取车点位置信息

    private double parkLat;
    private double parkLng;

    protected ConfirmOrderActivity activity;
    protected int layuotID = R.layout.activity_confirm_order;
    private AccountsTable mAccountsTable;

    public void setLayoutID(int layuotID) {
        this.layuotID = layuotID;
    }

    public void setActivity(ConfirmOrderActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layuotID);
        ViewUtils.inject(ConfirmOrderActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }

        initConfig();
    }

    protected void initConfig() {
        init();//初始化控件
        initData();//初始化数据
        refreshView();

    }

    protected void init() {
        top_back_btn.setVisibility(View.VISIBLE);
        top_title_tv.setText(getString(R.string.order_title));
        top_title_tv.setVisibility(View.VISIBLE);
        rl_send_money.setVisibility(View.GONE);
        checkbox_send_car.setOnCheckedChangeListener(this);
        scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    protected void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            ToolsHelper.showStatus(mContext, false, getString(R.string.order_err_get_car_info));
            return;
        }
        orderModel = (OrderModel) intent.getSerializableExtra("order_info");
        orderSendCarModel = new OrderSendCarModel();
        mAccountsTable = AccountManager.getInstance().findByNo();

        et_phone.setHint(mAccountsTable.getMobile());

    }

    protected void refreshView() {
        if (null == orderModel) {
            ToolsHelper.showStatus(mContext, false, getString(R.string.common_err_get));
            return;
        }

        if (0 == orderModel.getType()) {//0==预约用车
            send_car_time_layout.setVisibility(View.VISIBLE);
            no_layout.setVisibility(View.GONE);
            prompt_take_name.setText(orderModel.getDepot_name());
            prompt_take_address.setText(orderModel.getDepot_address());
            prompt_return_name.setText(orderModel.getDepot_name());
            prompt_return_address.setText(orderModel.getDepot_address());
            takeTimeApponit = orderModel.getBookCarModel().getScheduledPickedUpTime();
            sendCarTakeTime = orderModel.getBookCarModel().getScheduledPickedUpTime();
            takeTime = orderModel.getBookCarModel().getScheduledPickedUpTime();
            apponit_take_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, takeTimeApponit));
            send_car_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, takeTimeApponit));
            returnTimeApponit = orderModel.getBookCarModel().getScheduledDroppedOffTime();
            apponit_return_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, returnTimeApponit));
        } else {
            send_car_time_layout.setVisibility(View.GONE);
            apponit_take_time_layout.setVisibility(View.GONE);
            apponit_return_time_layout.setVisibility(View.GONE);
            no_layout.setVisibility(View.VISIBLE);
            prompt_take_name.setText(orderModel.getDepot_name());
            prompt_take_address.setText(orderModel.getDepot_address());
            prompt_return_name.setText(orderModel.getDepot_name());
            prompt_return_address.setText(orderModel.getDepot_address());
        }

        parkLat = orderModel.getLat();
        parkLng = orderModel.getLng();
        ImageLoaderManager.getInstance().displayImageForCar(mContext, orderModel.getUrl(), item_usercar_iv);
        if (!ToolsHelper.isNull(orderModel.getMileage())) {
            progressLayout.initParams(0, orderModel.getMaxSustainedMileage(),Integer.valueOf(orderModel.getMileage()));
            item_usercar_sustainedMileage.setText(String.format(mContext.getString(R.string.sustained_mileage), orderModel.getMileage()));
        } else {
            progressLayout.initParams(0, orderModel.getMaxSustainedMileage(), orderModel.getMaxSustainedMileage());
            item_usercar_sustainedMileage.setText("");
        }
        usercar_minprice.setText(orderModel.getChargingRule());
//            BitmapHelper.getBitmapUtils(null).display(order_icon_iv, orderModel.getUrl());
        order_car_info_name_tv.setText(ToolsHelper.isNullString(orderModel.getName()));
        order_car_info_number_tv.setText(ToolsHelper.isNullString(orderModel.getNumber()));
    }

    /**
     * 处理送车费用
     */
    protected void sendCarMoney(String sendCarMoney) {
        String content = String.format(getString(R.string.order_send_car_money), sendCarMoney);
        int start = content.indexOf(sendCarMoney);
        int end = start + sendCarMoney.length();
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.normal_text_color_red)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        rl_send_money.setVisibility(View.VISIBLE);
        send_car_money.setText(style);
    }


    @OnClick({R.id.top_back_btn,
            R.id.order_confirm_tv,
            R.id.prompt_return_location,
            R.id.order_protocol_tv,
            R.id.send_car_take_location,
            R.id.send_car_time_layout,
            R.id.apponit_take_time,
            R.id.apponit_return_time,
            R.id.take_car_customer_lyt,
            R.id.ll_chargingrule
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.order_confirm_tv://订单确认按钮
                if (checkbox_send_car.isChecked()) {
                    commitSendCarOrder();
                } else {
                    commitOrder();
                }
                break;
            case R.id.order_protocol_tv://租车协议
                WebViewActivityHelper.startWebViewActivity(mContext, ModuleUrls.getCarRentalClause(), getString(R.string.order_zuchexieyi));
                break;
            case R.id.prompt_return_location:
                Intent intent = new Intent();
                intent.setClass(mContext, LocatedActivity.class);
                intent.setAction(orderModel.getReturnLocationNo());
                startActivityForResult(intent, requestCodeAddress);
                break;
            case R.id.send_car_take_location://外勤取车点
                mIntent.setClass(mContext, GetCarLocatedActivity.class);
                Bundle bundle = new Bundle();
                if (returnCarLocationInfo != null) {
                    bundle.putSerializable("CAR_LOCATION_INFO", returnCarLocationInfo);
                }
                mIntent.putExtras(bundle);
                startActivityForResult(mIntent, requestSendCarTakeAddress);
                break;
            case R.id.send_car_time_layout://送车上门的取车时间
//                String sendtaketime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, takeTime);
//                PopWindowHelper.getInstance().timeIntervalPicker(mContext, sendtaketime, new PopWindowListener2() {
//                    @Override
//                    public void confirm(String text) {
//                        send_car_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
//                        sendCarTakeTime = TimeHelper.getTime(TimeHelper.FORMAT7, text);
//                        takeTime = sendCarTakeTime;
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//                }).show(findViewById(R.id.top__iv));
                PopWindowHelper.getInstance().showOptions(this,"选择送车上门时间",new PopWindowListener2() {
                    @Override
                    public void confirm(String text) {
                        send_car_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
                        sendCarTakeTime = TimeHelper.getTime(TimeHelper.FORMAT7, text);
                        takeTime = sendCarTakeTime;
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.apponit_take_time://预约取车时间
                final String taketime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, takeTime);
//                PopWindowHelper.getInstance().timeIntervalPicker(mContext, taketime, new PopWindowListener2() {
//                    @Override
//                    public void confirm(String text) {
//                        apponit_take_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
//                        takeTimeApponit = TimeHelper.getTime(TimeHelper.FORMAT7, text);
//                        orderModel.getBookCarModel().setScheduledPickedUpTime(takeTimeApponit);
//                        takeTime = takeTimeApponit;
//                        send_car_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
//
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//                }).show(findViewById(R.id.top__iv));
                PopWindowHelper.getInstance().showOptions(this,"选择预约取车时间",new PopWindowListener2() {
                    @Override
                    public void confirm(String text) {
                        apponit_take_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
                        takeTimeApponit = TimeHelper.getTime(TimeHelper.FORMAT7, text);
                        orderModel.getBookCarModel().setScheduledPickedUpTime(takeTimeApponit);
                        takeTime = takeTimeApponit;
                        send_car_time_tv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));

                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.apponit_return_time://预约还车时间
                long time;
                if (checkbox_send_car.isChecked() && sendCarTakeTime > 0) {
                    time = sendCarTakeTime;
                } else {
                    time = takeTimeApponit;
                }
                String returntime = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT7, time + 60000);
//                PopWindowHelper.getInstance().timeIntervalPicker(mContext, returntime, new PopWindowListener2() {
//                    @Override
//                    public void confirm(String text) {
//                        apponit_return_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
//                        returnTimeApponit = TimeHelper.getTime(TimeHelper.FORMAT7, text);
//                        orderModel.getBookCarModel().setScheduledDroppedOffTime(returnTimeApponit);
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//                }).show(findViewById(R.id.top__iv));
                PopWindowHelper.getInstance().showOptions(this,"选择预约还车时间",new PopWindowListener2() {
                    @Override
                    public void confirm(String text) {
                        apponit_return_time.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT4, TimeHelper.getTime(TimeHelper.FORMAT7, text)));
                        returnTimeApponit = TimeHelper.getTime(TimeHelper.FORMAT7, text);
                        orderModel.getBookCarModel().setScheduledDroppedOffTime(returnTimeApponit);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.take_car_customer_lyt://客服
                PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.ll_chargingrule:
                IntentHelper.intentChargingRule(mContext, Integer.valueOf(orderModel.getModelId()), null);
                break;
        }
    }

    /**
     * 创建订单
     */
    private void commitOrder() {
        if (0 == orderModel.getType()) {
            if (returnTimeApponit > takeTimeApponit) {
                LoadingDialog.showDialog(mContext, getString(R.string.order_creart));
                ModuleHttpApi.scheduledReservationOrders(orderModel.getBookCarModel(), orderModel.getDepot_number());
            } else {
                ToolsHelper.showStatus(mContext, false, getString(R.string.time_picker_tips));
            }
        } else {
            LoadingDialog.showDialog(mContext, getString(R.string.order_creart));
            ModuleHttpApi.createOrder(orderModel.getId(), orderModel.getReturnLocationNo());
        }
    }

    /**
     * 创建送车上门订单
     */
    private void commitSendCarOrder() {
        if (ToolsHelper.isNull(orderSendCarModel.getName())) {
            ToolsHelper.showStatus(mContext, false, "请选择取车地点");
            return;
        }
        String phone = et_phone.getText().toString();
        String phoneHint = et_phone.getHint().toString();
        if(TextUtils.isEmpty(phone) && !TextUtils.isEmpty(phoneHint) && phoneHint.equals(mAccountsTable.getMobile())){
            phone = phoneHint;
        }
        if (ToolsHelper.isNull(phone)) {
            ToolsHelper.showStatus(mContext, false, "手机号不能为空");
            return;
        }
        String telRegex = "[1][358]\\d{9}";
        if (!phone.matches(telRegex)) {
            ToolsHelper.showStatus(mContext, false, "请输入正确的手机号");
            return;
        }
        orderSendCarModel.setPhone(phone);
        if (0 == orderModel.getType()) {
            if (ToolsHelper.isNull(send_car_time_tv.getText().toString())) {
                ToolsHelper.showStatus(mContext, false, "请选择取车时间");
                return;
            }
            if (returnTimeApponit > sendCarTakeTime) {
                orderModel.getBookCarModel().setScheduledPickedUpTime(sendCarTakeTime);
                LoadingDialog.showDialog(mContext, getString(R.string.order_creart));
                ModuleHttpApi.scheduledReservationOrders(true, orderSendCarModel, orderModel.getBookCarModel(), orderModel.getDepot_number());
            } else {
                ToolsHelper.showStatus(mContext, false, getString(R.string.time_picker_tips));
            }
        } else {
            LoadingDialog.showDialog(mContext, getString(R.string.order_creart));
            ModuleHttpApi.createOrder(true, orderSendCarModel, orderModel.getId(), orderModel.getReturnLocationNo());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_register), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_unregister), e);
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.createOrder.equals(apiNo)
                || ModuleHttpApiKey.scheduledReservationOrders.equals(apiNo)
                || ModuleHttpApiKey.getDeliverCost.equals(apiNo)
        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.createOrder:
                    ToolsHelper.showStatus(mContext, true, getString(R.string.order_creat_success));
                    if (checkbox_send_car.isChecked()) {
//                        startActivity(new Intent(mContext, TakeApponitCarActivity.class));
                    } else {
                        startActivity(new Intent(mContext, TakeCarActivity.class));
                    }
                    finish();
                    break;
                case ModuleHttpApiKey.scheduledReservationOrders:
                    ToolsHelper.showStatus(mContext, true, "请等待分配车辆");
                    if (checkbox_send_car.isChecked()) {
//                        startActivity(new Intent(mContext, TakeApponitCarActivity.class));
                    } else {
                        startActivity(new Intent(mContext, ConfirmOrderApponitActivity.class));
                    }
                    finish();
                    break;
                case ModuleHttpApiKey.getDeliverCost:
                    try {
                        send_car_money.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(result);
                        double data = jsonObject.optDouble("data");
                        if (data > 0) {
                            String money = new DecimalFormat("#,##0.00").format(data / 100);
                            sendCarMoney(money);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.createOrder.equals(apiNo)
                || ModuleHttpApiKey.scheduledReservationOrders.equals(apiNo)
                || ModuleHttpApiKey.getDeliverCost.equals(apiNo)
        )) {
            return;
        }

        LoadingDialog.dismissDialog();
        switch (apiNo) {
            case ModuleHttpApiKey.getDeliverCost:
            case ModuleHttpApiKey.createOrder:
//                ToolsHelper.showStatus(mContext, false, getString(R.string.order_creat_err));
//                break;
            case ModuleHttpApiKey.scheduledReservationOrders:
                FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(),FaIlureModel.class);
                if(model != null && model.getErrors() != null && model.getErrors().size() > 0){
                    String errcode = model.getErrors().get(0).getErrcode();
                    String errmsg = model.getErrors().get(0).getErrmsg();
                    if("order.has.unfinished".equals(errcode)){
                        alert(errmsg);
                    }else{
                        ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
                    }
                }
                break;
        }

    }

    private void alert(String content){
        PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
            @Override
            public void confirm() {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(content).show(findViewById(R.id.top__iv));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data)
            return;
        if (RESULT_OK != resultCode)
            return;
        switch (requestCode) {//选择还车地点。
            case requestCodeAddress:
                ReservationOrdersLocationsModel.DataBean selectPark = (ReservationOrdersLocationsModel.DataBean) data.getSerializableExtra("selectPark");
                if (selectPark != null) {
                    orderModel.setReturnLocationNo(String.valueOf(selectPark.getId()));
                    if (orderModel.getBookCarModel() != null) {
                        orderModel.getBookCarModel().setLocationNo(String.valueOf(selectPark.getId()));
                    }
//					returnLocationName = selectPark.getName();
                    prompt_return_name.setText(selectPark.getName());
                    prompt_return_address.setText(selectPark.getAddress());
                    break;
                }
            case requestSendCarTakeAddress://送车上门的取车地点
                returnCarLocationInfo = (GetReturnCarLocationInfo) data.getSerializableExtra("CAR_LOCATION_INFO");
                if (returnCarLocationInfo != null) {
                    orderSendCarModel.setName(returnCarLocationInfo.getSpecificLocation());
                    orderSendCarModel.setAddress(returnCarLocationInfo.getAdministrativePosition());
                    orderSendCarModel.setLatitude(returnCarLocationInfo.getLatitude());
                    orderSendCarModel.setLongitude(returnCarLocationInfo.getLongitude());
                    send_car_location_tv.setText(returnCarLocationInfo.getSpecificLocation());

                    send_car_money.setVisibility(View.GONE);
                    if (returnCarLocationInfo.getLatitude() > 0 || returnCarLocationInfo.getLongitude() > 0) {
                        LoadingDialog.showDialog(mContext, "处理中");
                        ModuleHttpApi.getDeliverCost(parkLng, parkLat, returnCarLocationInfo.getLongitude(), returnCarLocationInfo.getLatitude());
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {//选择送车上门时，隐藏和显示相关布局
            send_car_layout.setVisibility(View.VISIBLE);
//            prompt_dash_return_iv.setVisibility(View.GONE);
            prompt_take_layout.setVisibility(View.GONE);
        } else {
            send_car_layout.setVisibility(View.GONE);
//            prompt_dash_return_iv.setVisibility(View.VISIBLE);
            prompt_take_layout.setVisibility(View.VISIBLE);
        }
    }
}
