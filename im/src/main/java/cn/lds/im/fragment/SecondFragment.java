package cn.lds.im.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.PopWindowListener3;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.RevertCarModel;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.OrderCancelEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ControlReturnModel;
import cn.lds.im.data.LocationModel;
import cn.lds.im.data.OperationReturnModel;
import cn.lds.im.data.OrderHave;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.view.MainActivity;
import cn.lds.im.view.PayActivity;
import cn.lds.im.view.RevertCarInfoActivity;
import cn.lds.im.view.TakeCarActivity;
import de.greenrobot.event.EventBus;

/***
 * 车钥匙
 */
public class SecondFragment extends MyActivityFragment implements IWXAPIEventHandler {

    public static final String TAG = "SecondFragment";
//    protected Context myActivity;

    //type
    protected final int TYPE_NO = -1;//默认白屏
    protected final int TYPE_NO_CAR = 0;//没有选车
    protected final int TYPE_NO_INSPECT = 1;//没有取车
    protected final int TYPE_HAVE_CAR = 2;//已取车
    protected final int DROPPED_OFF = 3;//已还车
    protected int type = TYPE_NO;

//    //是否选择还车点的type
//    protected final int TYPE_HAVE_CAR_NO_ADDRESS = 0;//没有选择还车地点
//    protected final int TYPE_HAVE_CAR_HAVE_ADDRESS = 1;//已选择还车地点
//    protected int type_have = TYPE_HAVE_CAR_NO_ADDRESS;

    //绑定控件
    //无车的时候
    @ViewInject(R.id.key_no_confirm)
    protected TextView key_no_confirm;
    @ViewInject(R.id.key_no_confirm_rlt)
    protected RelativeLayout key_no_confirm_rlt;
    @ViewInject(R.id.key_no_confirm_tv)
    protected TextView key_no_confirm_tv;
    //有车时候
    @ViewInject(R.id.key_have_confirm_lyt)
    protected LinearLayout key_have_confirm_lyt;
    @ViewInject(R.id.key_have_icon_iv)
    protected ImageView key_have_icon_iv;
    @ViewInject(R.id.key_have_name_tv)
    protected TextView key_have_name_tv;
    @ViewInject(R.id.key_have_number_tv)
    protected TextView key_have_number_tv;
    @ViewInject(R.id.key_have_time_h_tv)
    protected TextView key_have_time_h_tv;
    @ViewInject(R.id.key_have_time_m_tv)
    protected TextView key_have_time_m_tv;
    @ViewInject(R.id.key_have_time_m_title_tv)
    protected TextView key_have_time_m_title_tv;
    @ViewInject(R.id.key_have_trip_tv)
    protected TextView key_have_trip_tv;
    @ViewInject(R.id.key_have_journey_tv)
    protected TextView key_have_journey_tv;
    @ViewInject(R.id.key_have_depot_tv)
    protected TextView key_have_depot_tv;
    @ViewInject(R.id.key_have_time_tv)
    protected TextView key_have_time_tv;
    @ViewInject(R.id.key_have_navigation_tv)
    protected TextView key_have_navigation_tv;
    @ViewInject(R.id.key_have_open_ryt)
    protected RelativeLayout key_have_open_ryt;
    @ViewInject(R.id.key_have_close_ryt)
    protected RelativeLayout key_have_close_ryt;
    @ViewInject(R.id.key_have_whistle_ryt)
    protected RelativeLayout key_have_whistle_ryt;
    @ViewInject(R.id.key_have_confirm_tv)
    protected TextView key_have_confirm_tv;
    @ViewInject(R.id.key_car_info_take_tv)
    protected TextView key_car_info_take_tv;
    @ViewInject(R.id.key_have_show_tv)
    protected TextView key_have_show_tv;
    @ViewInject(R.id.key_have_confirm_return_lyt)
    protected LinearLayout key_have_confirm_return_lyt;

    protected View view;
    protected MainActivity myActivity;
    protected ImageView iv;
    protected boolean isLockButton = false;//车钥匙按钮是否锁定
    protected boolean canRefresh = false;//是否轮询
    protected String type_button = "";//轮询时按钮状态
    protected MyCountDownTimer timer;//倒计时
    protected AlertDialog myDialog;//倒计时UI
    protected TextView tv_time;
    protected boolean isLoop = false;

    protected ControlReturnModel operationReturnModel;//车钥匙操作数据---鸣笛
    protected OperationReturnModel controlReturnModel;//车钥匙操作数据
    protected OrderInfoModel orderInfoModel;//订单数据
    protected RevertCarModel revertCarModel;//还车点数据

    private boolean isFirst = true;

//    @SuppressLint("ValidFragment")
//    public SecondFragment(View v) {
//        iv = v;
//    }

    @SuppressLint("ValidFragment")
    public SecondFragment() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, null);
        LogHelper.e("onCreateView");
        ViewUtils.inject(this, view);
        ViewUtils.inject(SecondFragment.class, this, view);
        init();
        initData();
        refreshView();//刷新画面
        return view;
    }

    protected void init() {
        myActivity = (MainActivity) MyApplication.getInstance().getMainActivity();
        iv = MyApplication.getInstance().getIv__top();

//        myActivity = myActivity;
    }

    protected void initData() {
        if (AccountManager.getInstance().isLogin())
            type = TYPE_NO;
        else
            type = TYPE_NO_CAR;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.e("onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogHelper.e("onStart");
        if (AccountManager.getInstance().isLogin()) {
            isFirst = true;
            ModuleHttpApi.hasOrderExist();
        }
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(myActivity.getString(R.string.default_bus_register), e);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.e("onStop");
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(myActivity.getString(R.string.default_bus_unregister), e);
        }
    }

    protected void refreshView() {
        switch (type) {
            case TYPE_NO:
                key_no_confirm_rlt.setVisibility(View.GONE);
                key_have_confirm_lyt.setVisibility(View.GONE);
                break;
            case TYPE_NO_CAR:
                key_no_confirm_rlt.setVisibility(View.VISIBLE);
                key_have_confirm_lyt.setVisibility(View.GONE);

                key_no_confirm_tv.setText(getString(R.string.key_no_car_title_1));
                key_no_confirm.setText(getString(R.string.key_no_car_text_confirm_1));
                break;
            case TYPE_NO_INSPECT:
                key_no_confirm_rlt.setVisibility(View.VISIBLE);
                key_have_confirm_lyt.setVisibility(View.GONE);

                key_no_confirm_tv.setText(getString(R.string.key_no_car_title_2));
                key_no_confirm.setText(getString(R.string.key_no_car_text_confirm_2));
                break;
            case TYPE_HAVE_CAR:
                key_no_confirm_rlt.setVisibility(View.GONE);
                key_have_confirm_lyt.setVisibility(View.VISIBLE);

                key_have_confirm_tv.setText(R.string.key_car_confirm);
                break;
            case DROPPED_OFF:
                key_no_confirm_rlt.setVisibility(View.VISIBLE);
                key_have_confirm_lyt.setVisibility(View.GONE);

                key_no_confirm_tv.setText(getString(R.string.key_no_car_title_3));
                key_no_confirm.setText(getString(R.string.key_no_car_text_confirm_3));

                key_have_confirm_tv.setText(R.string.key_pay);
                break;
        }

        if (null != orderInfoModel && Constants.SCHEDULED.equals(orderInfoModel.getData().getType())) {
            key_have_depot_tv.setVisibility(View.VISIBLE);
            key_have_navigation_tv.setText(R.string.key_car_navigation);

            key_have_time_tv.setVisibility(View.VISIBLE);
        } else {
            key_have_depot_tv.setVisibility(View.VISIBLE);
            key_have_navigation_tv.setText(getString(R.string.key_car_navigation));

            key_have_time_tv.setVisibility(View.GONE);
        }
        if (null != orderInfoModel) {
            revertCarModel = new RevertCarModel(String.valueOf(orderInfoModel.getData().getReturnLocationId())
                    .toString(),
                    TimeHelper.getTimeFromMillis(TimeHelper.FORMAT6,
                            orderInfoModel.getData().getScheduledDroppedOffTime()
                    )
                    , orderInfoModel.getData().getReturnLocationLongitude(), orderInfoModel.getData().getReturnLocationLatitude(),
                    orderInfoModel.getData().getReturnLocationName());
            if (type == TYPE_HAVE_CAR)
                myActivity.setTopTitle(orderInfoModel.getData().getPlateLicenseNo());
        }
        setStatus();//取车等状态展示
        setText();//车钥匙状态
        setaddress();
        setData();
    }

    protected void setText() {
        if (null == controlReturnModel || null == operationReturnModel) {
            return;
        }
        key_have_show_tv.setText(type_button);
        if ("正在打开车门".equals(type_button)) {
            refreshButton(0);
            ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId()).toString(),
                    controlReturnModel.getData());
        } else if ("正在关闭车门".equals(type_button)) {
            refreshButton(1);
            ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId()).toString(),
                    controlReturnModel.getData());
        } else if ("车辆正在鸣笛".equals(type_button)) {
            refreshButton(2);
            ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId()).toString(),
                    operationReturnModel.getData().getOperationId());
        } else if ("车辆正在停止鸣笛".equals(type_button)) {
            refreshButton(3);
            ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId()).toString(),
                    controlReturnModel.getData());
        }
    }

    protected void setStatus() {
        if (null == orderInfoModel) {
            key_car_info_take_tv.setVisibility(View.GONE);
        } else {
            key_car_info_take_tv.setVisibility(View.VISIBLE);
            if (Constants.PICKED_UP.equals(orderInfoModel.getData().getStatus())) {
                key_car_info_take_tv.setText(R.string.key_status_picked_up);
            } else if (Constants.DROPPED_OFF.equals(orderInfoModel.getData().getStatus())) {
                key_car_info_take_tv.setText(R.string.key_status_dropped_off);
            } else if (Constants.RETURN_OVERDUE.equals(orderInfoModel.getData().getStatus())) {
                key_car_info_take_tv.setText(R.string.key_status_return_overdue);
            } else if (Constants.PAYMENT_OVERDUE.equals(orderInfoModel.getData().getStatus())) {
                key_car_info_take_tv.setText(R.string.key_status_payment_overdue);
            } else {
                key_car_info_take_tv.setVisibility(View.GONE);
            }

        }
    }

    protected void setaddress() {
        if (null == revertCarModel)
            return;
        key_have_depot_tv.setText(revertCarModel.getName());
        key_have_time_tv.setText(revertCarModel.getTime());
    }

    protected void setData() {
        if (null == orderInfoModel) {
            return;
        }
        if (ToolsHelper.isNull(orderInfoModel.getData().getVehiclePicId()))
            key_have_icon_iv.setImageResource(R.drawable.car_default);
        else
            ImageLoaderManager.getInstance().displayImageForCar(myActivity, orderInfoModel.getData()
                    .getVehiclePicId(), key_have_icon_iv);
        key_have_name_tv.setText(ToolsHelper.isNullString(orderInfoModel.getData().getVehicleBrandName()) + " " + orderInfoModel.getData().getVehicleSeriesName());
        key_have_number_tv.setText(orderInfoModel.getData().getPlateLicenseNo());
//        long countTime = TimeHelper.countTime(orderInfoModel.getData().getReservationTime()) / 1000;
//        long countTime = orderInfoModel.getData().getTime();
//        if (countTime / 1440 > 0) {
//            key_have_time_h_tv.setText(String.format(myActivity.getString(R.string.key_time_day), countTime / 1440,
//                    countTime % 1440 / 60, countTime % 1440 % 60));
//        } else {
//            key_have_time_h_tv.setText(String.format(myActivity.getString(R.string.key_time_h), countTime / 60,
//                    countTime % 60));
//        }

        key_have_trip_tv.setText(String.valueOf(orderInfoModel.getData().getOdometer()).toString());
        key_have_journey_tv.setText(String.valueOf(orderInfoModel.getData().getSustainedMileage()));

    }


    @OnClick({R.id.key_no_confirm, R.id.key_have_revise_tv
            , R.id.key_have_navigation_tv, R.id.key_have_open_ryt, R.id.key_have_close_ryt
            , R.id.key_have_whistle_ryt, R.id.key_have_confirm_return_lyt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.key_no_confirm:
                switch (type) {
                    case TYPE_NO_CAR:
                        myActivity.switchContent(0);
                        break;

                    case TYPE_NO_INSPECT:
                        Intent intent = new Intent(myActivity, TakeCarActivity.class);
                        startActivity(intent);
                        break;
                    case DROPPED_OFF:
                        myActivity.switchContent(2);
                        break;
                }
                break;
            case R.id.key_have_revise_tv:
//                startActivity(new Intent(myActivity, RevertCarInfoActivity.class));
                Intent mIntent = new Intent(myActivity, RevertCarInfoActivity.class);
                mIntent.putExtra("data", revertCarModel);
                mIntent.putExtra("orderNo", orderInfoModel.getData().getNo());//订单no
                startActivityForResult(mIntent, 1000);
                break;
            case R.id.key_have_navigation_tv:
                if (getString(R.string.key_select).equals(key_have_navigation_tv.getText().toString())) {
//                    startActivity(new Intent(myActivity, RevertCarInfoActivity.class));
                    Intent intent1 = new Intent(myActivity, RevertCarInfoActivity.class);
                    intent1.putExtra("data", revertCarModel);
                    intent1.putExtra("orderNo", orderInfoModel.getData().getNo());//订单no
                    startActivityForResult(intent1, 1000);
                } else {
                    PopWindowHelper.getInstance().map(myActivity, true, revertCarModel, new PopWindowListener3() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void failed() {

                        }

                        @Override
                        public void onItemListener(int position) {
//                            ToolsHelper.showInfo(myActivity, "选择了" + position);

                        }
                    }).setBackKey(true).show(iv);
                }
                break;
            case R.id.key_have_open_ryt:
                if (isLockButton)
                    ToolsHelper.showStatus(myActivity, false, "有未完成的操作");
                else {
                    ModuleHttpApi.reservationOrdersOpen(String.valueOf(orderInfoModel.getData().getId()).toString());
                    refreshButton(0);
                }
                break;
            case R.id.key_have_close_ryt:
                if (isLockButton)
                    ToolsHelper.showStatus(myActivity, false, "有未完成的操作");
                else {
                    ModuleHttpApi.reservationOrdersLock(String.valueOf(orderInfoModel.getData().getId()).toString());
                    refreshButton(1);
                }
                break;
            case R.id.key_have_whistle_ryt:
                if (isLockButton)
                    ToolsHelper.showStatus(myActivity, false, "有未完成的操作");
                else {
                    ModuleHttpApi.reservationOrdersBlast(String.valueOf(orderInfoModel.getData().getId()).toString());
                    refreshButton(2);
                }
                break;
            case R.id.key_have_confirm_return_lyt:

                if (getString(R.string.key_pay).equals(key_have_confirm_tv.getText().toString())) {

                    PopWindowHelper.getInstance().confirm(myActivity, new PopWindowListener() {
                        @Override
                        public void confirm() {
                            myActivity.switchContent(2);
                            startActivity(new Intent(myActivity, PayActivity.class));
                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(myActivity.getString(R.string.key_confire_pay)).show(iv);
                } else {
                    PopWindowHelper.getInstance().confirm(myActivity, new PopWindowListener() {
                        @Override
                        public void confirm() {
                            if (ToolsHelper.isNull(key_have_depot_tv.getText().toString())) {
                                LoadingDialog.showDialog(myActivity, myActivity.getString(R.string.key_reservation));
//                                ModuleHttpApi.reservationOrdersReturn(String.valueOf(orderInfoModel.getData().getNo()
//                                ).toString());
                            } else {
                                LoadingDialog.showDialog(myActivity, myActivity.getString(R.string
                                        .key_reservation_location));
                                ModuleHttpApi.location(String.valueOf(orderInfoModel.getData().getNo()).toString());
                            }
                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(getString(R.string.key_have_car_confirm)).show(iv);
                }
                break;
        }
    }

    protected void refreshButton(int i) {
        switch (i) {
            case -1:
                isLockButton = false;
                key_have_open_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_close_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_whistle_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);

                key_have_open_ryt.setEnabled(true);
                key_have_close_ryt.setEnabled(true);
                key_have_whistle_ryt.setEnabled(true);
                break;
            case 0:
                isLockButton = true;
                key_have_open_ryt.setBackgroundResource(R.drawable.bg_circular_style_2_focused);
                key_have_close_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_whistle_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);

                key_have_open_ryt.setEnabled(false);
                key_have_close_ryt.setEnabled(true);
                key_have_whistle_ryt.setEnabled(true);

                type_button = "正在打开车门";
                key_have_show_tv.setText(type_button);

                break;
            case 1:
                isLockButton = true;
                key_have_open_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_close_ryt.setBackgroundResource(R.drawable.bg_circular_style_2_focused);
                key_have_whistle_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);

                key_have_open_ryt.setEnabled(true);
                key_have_close_ryt.setEnabled(false);
                key_have_whistle_ryt.setEnabled(true);

                type_button = "正在关闭车门";
                key_have_show_tv.setText(type_button);
                break;
            case 2:
                isLockButton = true;
                key_have_open_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_close_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_whistle_ryt.setBackgroundResource(R.drawable.bg_circular_style_2_focused);

                key_have_open_ryt.setEnabled(true);
                key_have_close_ryt.setEnabled(true);
                key_have_whistle_ryt.setEnabled(false);

                type_button = "车辆正在鸣笛";
                key_have_show_tv.setText(type_button);

                break;
            case 3:
                isLockButton = true;
                key_have_open_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_close_ryt.setBackgroundResource(R.drawable.bg_circular_style_2);
                key_have_whistle_ryt.setBackgroundResource(R.drawable.bg_circular_style_2_focused);

                key_have_open_ryt.setEnabled(true);
                key_have_close_ryt.setEnabled(true);
                key_have_whistle_ryt.setEnabled(false);

                type_button = "车辆正在停止鸣笛";
                key_have_show_tv.setText(type_button);

                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String o_id = (String) msg.obj;
            canRefresh = true;
            if (null != timer)
                timer.cancel();
            timer = new MyCountDownTimer(110000, 1000);
            timer.start();
            ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId()).toString(), o_id);
            switch (msg.what) {
                case 0:
                    refreshButton(0);
                    break;
                case 1:
                    refreshButton(1);
                    break;
                case 2:
                    refreshButton(2);
                    break;
                case 3:
                    refreshButton(3);
                    break;
            }
        }
    };


    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo) || ModuleHttpApiKey.hasOrderExist.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersReturn.equals(apiNo) || ModuleHttpApiKey.reservationOrdersOpen
                .equals(apiNo)
                || ModuleHttpApiKey.location.equals(apiNo) || ModuleHttpApiKey.reservationOrdersOperation.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersLock.equals(apiNo) || ModuleHttpApiKey.reservationOrdersBlast
                .equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersStopBlast.equals(apiNo))) {
            return;
        }
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getExistOrder:
                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderInfoModel.class);
                    getStatus();
                    refreshView();//刷新画面
                    break;
                case ModuleHttpApiKey.hasOrderExist:
                    LoadingDialog.dismissDialog();
                    OrderHave orderHave = GsonImplHelp.get().toObject(result, OrderHave.class);
                    if (!orderHave.isData()) {
                        if (!isFirst) {
//                    startActivity(new Intent(myActivity, PayActivity.class));
                            myActivity.switchContent(2);
                        } else {
                            type = TYPE_NO_CAR;
                            refreshView();
                        }
                    } else {
                        if (!isFirst) {
                            startActivity(new Intent(myActivity, PayActivity.class));
                            myActivity.switchContent(2);
                        } else {
                            type = TYPE_NO_INSPECT;
//                        type_have = TYPE_HAVE_CAR_NO_ADDRESS;
                            ModuleHttpApi.getExistOrder();
                        }
                    }

                    break;
                case ModuleHttpApiKey.reservationOrdersOpen:
                    controlReturnModel = GsonImplHelp.get().toObject(result, OperationReturnModel.class);

                    Message message0 = new Message();
                    message0.what = 0;
                    message0.obj = controlReturnModel.getData();
                    handler.sendMessage(message0);
                    break;
                case ModuleHttpApiKey.reservationOrdersLock:
                    controlReturnModel = GsonImplHelp.get().toObject(result, OperationReturnModel.class);

                    Message message1 = new Message();
                    message1.what = 1;
                    message1.obj = controlReturnModel.getData();
                    handler.sendMessage(message1);
                    break;
                case ModuleHttpApiKey.reservationOrdersStopBlast:
                    controlReturnModel = GsonImplHelp.get().toObject(result, OperationReturnModel.class);

                    Message message3 = new Message();
                    message3.what = 3;
                    message3.obj = controlReturnModel.getData();
                    handler.sendMessage(message3);
                    break;
                case ModuleHttpApiKey.reservationOrdersBlast:
                    operationReturnModel = GsonImplHelp.get().toObject(result, ControlReturnModel.class);

                    Message message2 = new Message();
                    message2.what = 2;
                    message2.obj = operationReturnModel.getData().getOperationId();
                    handler.sendMessage(message2);
                    break;
                case ModuleHttpApiKey.reservationOrdersReturn:
                    LoadingDialog.dismissDialog();
                    ToolsHelper.showStatus(myActivity, true, myActivity.getString(R.string.key_success_reservation));

                    LoadingDialog.showDialog(myActivity, "");
                    isFirst = false;
                    ModuleHttpApi.hasOrderExist();

                    break;
                case ModuleHttpApiKey.location:
                    LocationModel locationModel = GsonImplHelp.get().toObject(result, LocationModel.class);
                    if ("0".equals(String.valueOf(locationModel.getData().getId()).toString())) {
                        LoadingDialog.dismissDialog();
                        ToolsHelper.showStatus(myActivity, false, "监测到车辆不在任何停车场范围内");
                        return;
                    }
                    if (!revertCarModel.getId().equals(String.valueOf(locationModel.getData().getNo()).toString())) {
                        LoadingDialog.dismissDialog();
                        PopWindowHelper.getInstance().confirm(myActivity, new PopWindowListener() {
                            @Override
                            public void confirm() {
                                LoadingDialog.showDialog(myActivity, myActivity.getString(R.string
                                        .key_loading_reservation));
//                                ModuleHttpApi.reservationOrdersReturn(String.valueOf(orderInfoModel.getData().getNo()
//                                ).toString());
                            }

                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void onItemListener(int position) {

                            }
                        }).setContentTx(myActivity.getString(R.string.key_is_reservation)).setBackKey(true).show(iv);
                    } else {
//                        ModuleHttpApi.reservationOrdersReturn(String.valueOf(orderInfoModel.getData().getNo()
//                        ).toString());
                    }
                    break;
                case ModuleHttpApiKey.reservationOrdersOperation:
                    OperationReturnModel c = GsonImplHelp.get().toObject(result, OperationReturnModel.class);
                    String str = key_have_show_tv.getText().toString();
                    if ("FAILED".equals(c.getData())) {
                        if (null != timer) {
                            timer.cancel();
                        }
                        canRefresh = false;
                        refreshButton(-1);
                        ToolsHelper.showStatus(myActivity, false, "网络信号弱，操作失败，请重试");
                        if ("正在打开车门".equals(str)) {
                            type_button = "打开车门失败";
                            key_have_show_tv.setText("打开车门失败");
                        } else if ("正在关闭车门".equals(str)) {
                            key_have_show_tv.setText("关闭车门失败");
                            type_button = "关闭车门失败";
                        } else if ("车辆正在鸣笛".equals(str)) {
                            key_have_show_tv.setText("车辆鸣笛失败");
                            type_button = "车辆鸣笛失败";
                        } else if ("车辆正在停止鸣笛".equals(str)) {
                            key_have_show_tv.setText("车辆停止鸣笛失败");
                            type_button = "车辆停止鸣笛失败";
                        }
                    } else if ("SUCCEED".equals(c.getData())) {
                        if (null != timer) {
                            timer.cancel();
                        }
                        canRefresh = false;
                        refreshButton(-1);
                        if ("正在打开车门".equals(str)) {
                            key_have_show_tv.setText("打开车门成功");
                            type_button = "打开车门成功";
                        } else if ("正在关闭车门".equals(str)) {
                            key_have_show_tv.setText("关闭车门成功");
                            type_button = "关闭车门成功";
                        } else if ("车辆正在鸣笛".equals(str)) {
                            if (operationReturnModel.getData().isCanStop()) {
                                key_have_show_tv.setText("车辆正在鸣笛");
                                type_button = "车辆正在鸣笛";
                                startTimer();
                            } else {
                                key_have_show_tv.setText("");
                                type_button = "";
                            }

                        } else if ("车辆正在停止鸣笛".equals(str)) {
                            key_have_show_tv.setText("");
                            type_button = "";
                        }
                    } else {
                        if (!canRefresh) {
                            if (null != timer) {
                                timer.cancel();
                            }
                            canRefresh = false;

                            refreshButton(-1);
                            ToolsHelper.showStatus(myActivity, false, "网络信号弱，操作失败，请重试");
                            if ("正在打开车门".equals(str)) {
                                key_have_show_tv.setText("打开车门失败");
                                type_button = "打开车门失败";
                            } else if ("正在关闭车门".equals(str)) {
                                key_have_show_tv.setText("关闭车门失败");
                                type_button = "关闭车门失败";
                            } else if ("车辆正在鸣笛".equals(str)) {
                                key_have_show_tv.setText("车辆鸣笛失败");
                                type_button = "车辆鸣笛失败";
                            } else if ("车辆正在停止鸣笛".equals(str)) {
                                key_have_show_tv.setText("车辆停止鸣笛失败");
                                type_button = "车辆停止鸣笛失败";
                            }
                            return;
                        }
                        isLoop = true;
//                        ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId())
// .toString(), controlReturnModel.getData());
                        if ("正在打开车门".equals(str)) {
                            refreshButton(0);
                        } else if ("正在关闭车门".equals(str)) {
                            refreshButton(1);
                        } else if ("车辆正在鸣笛".equals(str)) {
                            refreshButton(2);
                        } else if ("车辆正在停止鸣笛".equals(str)) {
                            refreshButton(3);
                        }
                    }
                    break;


            }


        }
    }


    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo) || ModuleHttpApiKey.hasOrderExist.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersOpen.equals(apiNo) || ModuleHttpApiKey.reservationOrdersLock
                .equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersBlast.equals(apiNo) || ModuleHttpApiKey.reservationOrdersReturn
                .equals(apiNo)
                || ModuleHttpApiKey.location.equals(apiNo) || ModuleHttpApiKey.reservationOrdersOperation.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersStopBlast.equals(apiNo))) {
            return;
        }
        switch (apiNo) {
            case ModuleHttpApiKey.getExistOrder:
            case ModuleHttpApiKey.hasOrderExist:
                type = TYPE_NO_CAR;
//                type_have = TYPE_HAVE_CAR_NO_ADDRESS;
                refreshView();
                ToolsHelper.showHttpRequestErrorMsg(myActivity, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersOpen:
                refreshButton(-1);
                key_have_show_tv.setText("打开车门失败");
                type_button = "打开车门失败";
                ToolsHelper.showHttpRequestErrorMsg(myActivity, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersLock:
                refreshButton(-1);
                key_have_show_tv.setText("关闭车门失败");
                type_button = "关闭车门失败";
                ToolsHelper.showHttpRequestErrorMsg(myActivity, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersStopBlast:
//                refreshButton(-1);
                key_have_show_tv.setText("车辆停止鸣笛失败");
                type_button = "车辆停止鸣笛失败";

                if (406 == httpResult.getErrorCode()) {
                    PopWindowHelper.getInstance().alert(myActivity, new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(getString(R.string.take_car_seek_confirm)).show(iv);
                }

                ToolsHelper.showHttpRequestErrorMsg(myActivity, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersBlast:

                refreshButton(-1);
                key_have_show_tv.setText("车辆鸣笛失败");
                type_button = "车辆鸣笛失败";

                if (406 == httpResult.getErrorCode()) {
                    PopWindowHelper.getInstance().alert(myActivity, new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(getString(R.string.take_car_seek_confirm)).show(iv);
                }

                ToolsHelper.showHttpRequestErrorMsg(myActivity, httpResult);
                break;
            case ModuleHttpApiKey.reservationOrdersReturn:
                LoadingDialog.dismissDialog();
                if (406 == httpResult.getErrorCode()) {
                    PopWindowHelper.getInstance().alert(myActivity, new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx("监测到车辆不在任何停车场范围内").show(iv);

                } else {
                    PopWindowHelper.getInstance().alert(myActivity, new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(ToolsHelper.getHttpRequestErrorMsg(myActivity, httpResult)).show(iv);

                }
                break;
            case ModuleHttpApiKey.reservationOrdersOperation:
                String str = key_have_show_tv.getText().toString();
                if (!canRefresh) {
                    if (null != timer) {
                        timer.cancel();
                    }
                    canRefresh = false;
                    refreshButton(-1);
                    ToolsHelper.showStatus(myActivity, false, "网络信号弱，操作失败，请重试");
                    if ("正在打开车门".equals(str)) {
                        key_have_show_tv.setText("打开车门失败");
                        type_button = "打开车门失败";
                    } else if ("正在关闭车门".equals(str)) {
                        key_have_show_tv.setText("关闭车门失败");
                        type_button = "关闭车门失败";
                    } else if ("车辆正在鸣笛".equals(str)) {
                        key_have_show_tv.setText("车辆鸣笛失败");
                        type_button = "车辆鸣笛失败";
                    } else if ("车辆正在停止鸣笛".equals(str)) {
                        key_have_show_tv.setText("车辆停止鸣笛失败");
                        type_button = "车辆停止鸣笛失败";
                    }
                    return;
                }
                isLoop = true;
//                ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId()).toString
// (), controlReturnModel.getData());
                if ("正在打开车门".equals(str)) {
                    refreshButton(0);
                } else if ("正在关闭车门".equals(str)) {
                    refreshButton(1);
                } else if ("车辆正在鸣笛".equals(str)) {
                    refreshButton(2);
                } else if ("车辆正在停止鸣笛".equals(str)) {
                    refreshButton(3);
                }

                break;
            case ModuleHttpApiKey.location:
                LoadingDialog.dismissDialog();
                ToolsHelper.showHttpRequestErrorMsg(myActivity, httpResult);
                break;

        }
    }

    public void getStatus() {
        if (Constants.ALLOCATED.equals(orderInfoModel.getData().getStatus())) {
            type = TYPE_NO_INSPECT;
        } else if (Constants.PICKED_UP.equals(orderInfoModel.getData().getStatus()) ||
                Constants.RETURN_OVERDUE.equals(orderInfoModel.getData().getStatus())) {
            type = TYPE_HAVE_CAR;
            revertCarModel = MyApplication.getInstance().getRevertCarModel(String.valueOf(orderInfoModel.getData()
                    .getId()).toString());
        } else if (Constants.DROPPED_OFF.equals(orderInfoModel.getData().getStatus()) ||
                Constants.PAYMENT_OVERDUE.equals(orderInfoModel.getData().getStatus())) {
            type = DROPPED_OFF;
            revertCarModel = MyApplication.getInstance().getRevertCarModel(String.valueOf(orderInfoModel.getData()
                    .getId()).toString());
        } else if (Constants.PAID.equals(orderInfoModel.getData().getStatus()) ||
                Constants.CANCELLED.equals(orderInfoModel.getData().getStatus())) {
            type = TYPE_NO_CAR;
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogHelper.e(baseResp.toString());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null == data)
            return;
        if (Activity.RESULT_OK != resultCode)
            return;
        switch (requestCode) {
            case 1000:
                revertCarModel = (RevertCarModel) data.getParcelableExtra("data");
                if (null == revertCarModel) {
                    ToolsHelper.showStatus(myActivity, false, myActivity.getString(R.string.key_err_data));
                    return;
                }
                MyApplication.getInstance().setRevertCarModel(String.valueOf(orderInfoModel.getData().getId())
                        .toString(), revertCarModel);
                refreshView();
                break;
        }

    }


    protected void startTimer() {
        myDialog = new AlertDialog.Builder(myActivity).create();
        myDialog.show();
        Window mWindow = myDialog.getWindow();
        mWindow.setContentView(R.layout.dialog_timer);

        tv_time = (TextView) myDialog.findViewById(R.id.dialog_timer_time_tv);
        RelativeLayout dialog_timer_button_rly = (RelativeLayout) myDialog.findViewById(R.id.dialog_timer_button_rly);
        dialog_timer_button_rly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBlast();
            }
        });
        if (null != timer)
            timer.cancel();
        timer = new MyCountDownTimer(30000, 1000);
        timer.start();
    }

    protected void stopBlast() {
        if (null != timer)
            timer.cancel();
        if (null != myDialog) {
            myDialog.cancel();
            myDialog = null;
        }
        ModuleHttpApi.reservationOrdersStopBlast(String.valueOf(orderInfoModel.getData().getId()).toString());
        refreshButton(3);
    }

    /**
     * 继承 CountDownTimer 防范
     * 重写 父类的方法 onTick() 、 onFinish()
     */
    class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            canRefresh = false;
            String str = key_have_show_tv.getText().toString();
            if ("车辆正在鸣笛".equals(str)) {
                stopBlast();
            } else {
                refreshButton(-1);
                ToolsHelper.showStatus(myActivity, false, "网络信号弱，操作失败，请重试");
                if ("正在打开车门".equals(str)) {
                    key_have_show_tv.setText("打开车门失败");
                    type_button = "打开车门失败";
                } else if ("正在关闭车门".equals(str)) {
                    key_have_show_tv.setText("关闭车门失败");
                    type_button = "关闭车门失败";
                } else if ("车辆正在鸣笛".equals(str)) {
                    key_have_show_tv.setText("车辆鸣笛失败");
                    type_button = "车辆鸣笛失败";
                } else if ("车辆正在停止鸣笛".equals(str)) {
                    key_have_show_tv.setText("车辆停止鸣笛失败");
                    type_button = "车辆停止鸣笛失败";
                }
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String str = key_have_show_tv.getText().toString();
            if (null != myDialog && "车辆正在鸣笛".equals(str)) {
                if (null != tv_time)
                    tv_time.setText(millisUntilFinished / 1000 + "S");
            } else {
                if (isLoop) {
                    isLoop = false;
                    if ("车辆正在鸣笛".equals(str))
                        ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId())
                                .toString(), operationReturnModel.getData().getOperationId());
                    else
                        ModuleHttpApi.reservationOrdersOperation(String.valueOf(orderInfoModel.getData().getId())
                                .toString(), controlReturnModel.getData());

                }
            }
        }
    }

    @Override
    public void show(FragmentManager manager, MyActivityFragment fragment) {
        super.show(manager, fragment);
        isFirst = true;
        ModuleHttpApi.hasOrderExist();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        type_button = "";
    }


    public void onEventMainThread(OrderCancelEvent event) {
        if (null == orderInfoModel)
            return;

        PopWindowHelper.getInstance().alert(myActivity, new PopWindowListener() {
            @Override
            public void confirm() {
                ModuleHttpApi.reservationOrdersCancel(String.valueOf(orderInfoModel.getData().getNo()).toString());
                myActivity.switchContent(0);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setConfirmTx(getString(R.string.order_haode)).setContentTx(getString(R.string.order_yajinbuzu)).setBackKey(true).show(iv);

    }


}
