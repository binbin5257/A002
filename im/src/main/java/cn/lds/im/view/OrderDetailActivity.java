package cn.lds.im.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.OrderPayModel;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.RequestPay;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.manager.PayManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.CouponListModel;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.data.OrderDetailInfoModel;
import cn.lds.im.enums.BusinessType;
import cn.lds.im.view.adapter.OrderDetailAdapter;
import cn.lds.im.view.widget.CustomPopuwindow;
import cn.lds.im.view.widget.MyListView;
import de.greenrobot.event.EventBus;

/**
 * 订单详情页面
 * Created by sibinbin on 2017/7/17.
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 根view
     */
    private LinearLayout rootView;
    /**
     * 支付方式，默认零钱支付
     */
    private int PAY_WAY = 0;

    /**
     * 零钱支付
     */
    private int CHANGE_WAY = 0;

    /**
     * 微信支付
     */
    private int WX_WAY = 1;

    /**
     * 支付宝支付
     */
    private int ALIPAY_WAY = 2;
    /**
     * 显示支付view
     */
    private TextView payWay;
    /**
     * 计费规则
     */
    private TextView topMenuRight;
    /**
     * 支付方式图标
     */
    private ImageView payWayIv;
    /**
     * 是否展开应付金额
     */
    private boolean isOpenShouldPay = false;
    /**
     * 应支付区域
     */
    private RelativeLayout shouldPayRlyt;
    /**
     *  应支付款信息
     */
//    private LinearLayout shouldPayDetailLlyt;
    /**
     * 展开应支付图标
     */
//    private ImageView arrowDropIv;
    /**
     * 订单信息对象
     */
    private OrderDetailInfoModel orderInfoModel;

    /**
     * 车牌号
     */
    private TextView carNo;

    /**
     * 车俩型号
     */
    private TextView carModel;
    /**
     * 取车地点
     */
    private TextView getCarAddressTv;
    /**
     * 取车时间
     */
//    private TextView getCarTimeTv;
    /**
     * 还车地址
     */
    private TextView returnCarAddressTv;

    /**
     * 行程
     */
    private TextView odometerTv;
    /**
     * 车辆使用时间
     */
    private TextView carUsingTimeTv;
    /**
     * 应该支付
     */
    private TextView shoulePayTv;
    /**
     * 车图片
     */
    private ImageView iconCarIv;
    /**
     * 联系客服
     */
    private TextView contactTv;
    /**
     * 使用的优惠券实体类
     */
    private CouponListModel.DataBean couponBean;
    /**
     * 优惠卷显示金额view
     */
    private TextView couponTv;

    /**
     * 优惠卷金额
     */
    private float couponPrice = 0;

    /**
     * 优惠卷id
     */
    private int tickId;
    private TextView realPayTv;
    private double realPay = 0.00f;
    private DecimalFormat df;
    private TextView getCarYardNameTv;
//    private RelativeLayout getCarOwnerRlyt;
//    private TextView getCarOwnerSpotTv;
//    private RelativeLayout returnCarOwnerRlyt;
//    private TextView returnCarOwnerSpotTv;
    private TextView returnCarNameTv;
    private TextView distanceCostTv;
    private TextView timeCostTv;
    private String objectNo;
    private String content;
    private MyListView totalLv;
    private LinearLayout totalLvLlyt;
    private ImageView arrowTotalIv;
    private TextView discountTv;
    private OrderDetailInfoModel.DataBean dataBean;
    private RelativeLayout departmentBalanceRlyt;
    private RelativeLayout departmentMonthRemainAmountRlyt;

    private TextView departmentBalanceTv;
    private TextView departmentMonthRemainAmountTv;
    private ImageView payRightArrowIv;
    private RelativeLayout payWayRlyt;
    private LinearLayout saleLl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        EventBus.getDefault().register(this);
        initView();
        initData();
        /**
         * 客服帮助付费费还车，从CarInUseActivity页面传递过来数据
         */
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            objectNo = extras.getString("objectNo");
            content = extras.getString("event");

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取正在进行订单数据
     */
    private void initData() {
        df = new DecimalFormat("#,##0.00");
        LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
        ModuleHttpApi.getExistOrder();
    }

    private void initView() {
        RelativeLayout totalRlyt = (RelativeLayout) findViewById(R.id.rl_total);
        totalLvLlyt = (LinearLayout) findViewById(R.id.total_lv_ll);
        totalLv = (MyListView) findViewById(R.id.total_lv);
        arrowTotalIv = (ImageView) findViewById(R.id.right_arrow_total);
        Button backBtn = (Button) findViewById(R.id.top_back_btn);
        TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
        payWay = (TextView) findViewById(R.id.tv_pay_way);
//        topMenuRight = (ImageView) findViewById(R.id.top_menu_btn_del);
        topMenuRight = (TextView) findViewById(R.id.top_right_tv);
        payWayIv = (ImageView) findViewById(R.id.iv_pay_way);
//        arrowDropIv = (ImageView) findViewById(R.id.ic_arrow_drop);
        iconCarIv = (ImageView) findViewById(R.id.iv_car);
        Button payBtn = (Button) findViewById(R.id.btn_pay);
        payWayRlyt = (RelativeLayout) findViewById( R.id.rl_pay_way);
        shouldPayRlyt = (RelativeLayout) findViewById(R.id.rl_should_pay);
//        shouldPayDetailLlyt = (LinearLayout) findViewById(R.id.ll_should_pay_detail);
        rootView = (LinearLayout) findViewById(R.id.root_view);
        carNo = (TextView) findViewById(R.id.car_no);
        carModel = (TextView) findViewById(R.id.car_model);
        getCarYardNameTv = (TextView) findViewById(R.id.get_car_name);
        getCarAddressTv = (TextView) findViewById(R.id.get_car_address);
//        getCarOwnerRlyt = (RelativeLayout) findViewById(R.id.rl_owner_get_car);
//        getCarOwnerSpotTv = (TextView) findViewById(R.id.get_car_spot);
//        returnCarOwnerRlyt = (RelativeLayout) findViewById(R.id.rl_owner_return_car);
//        returnCarOwnerSpotTv = (TextView) findViewById(R.id.return_car_spot);
        returnCarNameTv = (TextView) findViewById(R.id.return_car_name);
        returnCarAddressTv = (TextView) findViewById(R.id.return_car_address);
        odometerTv = (TextView) findViewById(R.id.tv_odometer);
        contactTv = (TextView) findViewById(R.id.tv_contact);
        carUsingTimeTv = (TextView) findViewById(R.id.car_using_time);
        RelativeLayout couponRlyt = (RelativeLayout) findViewById(R.id.rl_coupon);
        shoulePayTv = (TextView) findViewById(R.id.shoule_pay);
        couponTv = (TextView) findViewById(R.id.tv_coupon);
        realPayTv = (TextView) findViewById(R.id.tv_real_pay);
        distanceCostTv = (TextView) findViewById(R.id.tv_distanceCost);
        timeCostTv = (TextView) findViewById(R.id.tv_timeCost);
        discountTv = (TextView) findViewById(R.id.tv_discount);
        saleLl = (LinearLayout) findViewById( R.id.ll_sale);

        departmentBalanceRlyt = (RelativeLayout) findViewById( R.id.rl_department_balance);
        departmentMonthRemainAmountRlyt = (RelativeLayout) findViewById( R.id.rl_department_monthRemainAmount);

        departmentBalanceTv = (TextView) findViewById(R.id.tv_department_balance);
        departmentMonthRemainAmountTv = (TextView) findViewById(R.id.tv_department_monthRemainAmount);
        payRightArrowIv = (ImageView) findViewById( R.id.right_arrow);
        couponTv.setText("不使用优惠券");
        contactTv.setOnClickListener(this);
        titleTv.setText("订单详情");
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        payWayRlyt.setOnClickListener(this);
        topMenuRight.setVisibility(View.VISIBLE);
        topMenuRight.setText("计费说明");
        topMenuRight.setOnClickListener(this);
        couponRlyt.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        totalRlyt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_contact: //联系客服
                PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));                break;
            case R.id.rl_should_pay: //应支付金额
               // OpenOrCloseShouldPay();
                break;
            case R.id.top_back_btn: //返回上一页
                finish();
                break;
            case R.id.top_right_tv: //进入计费规则界面
                Intent intent2 = new Intent(this,ChargingRuleActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("id",orderInfoModel.getData().getModelId());
                bundle2.putString("orderTime",String.valueOf(orderInfoModel.getData().getReservationTime()));
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
//            case R.id.top_menu_btn_del:
//                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/customService.html", getString(R.string.btn_submenu_customerservice));
//                break;
            case R.id.rl_pay_way: // 支付方式选择

                if(dataBean.getAmount() != 0.0){
                    ChoicePayWay();
                }
                break;
            case R.id.btn_pay: // 开始支付
                startPay();
                break;
            case R.id.rl_coupon: // 选择优惠券
                Intent intent = new Intent(this,CouponListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderId",String.valueOf(orderInfoModel.getData().getId()));
                intent.putExtras(bundle);
                startActivityForResult(intent,111);
                break;
            case R.id.rl_total:
                if(totalLvLlyt.getVisibility() == View.VISIBLE){
                    totalLvLlyt.setVisibility(View.GONE);
                    startRoateDown(this,arrowTotalIv);
                }else{
                    totalLvLlyt.setVisibility(View.VISIBLE);
                    startRoateUp(this,arrowTotalIv);
                }
                break;
        }

    }
    /**
     * 执行向上旋转的动画
     */
    private void startRoateUp(Activity act, ImageView iv) {

        Animation upAnimation = AnimationUtils.loadAnimation(act, R.anim.rotate_up_90);
        upAnimation.setFillAfter(true);
        iv.startAnimation(upAnimation);
    }
    /**
     * 执行向下旋转的动画
     */
    private void startRoateDown(Activity act, ImageView iv) {

        Animation downAnimation = AnimationUtils.loadAnimation(act, R.anim.rotate_down_90);
        downAnimation.setFillAfter(true);
        iv.startAnimation(downAnimation);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 111){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if(bundle != null){
                    couponBean = (CouponListModel.DataBean) bundle.getSerializable("DATABEAN");
                    if(couponBean == null){
                        tickId = 0;
                        couponTv.setText("不使用优惠券");
                        realPay = orderInfoModel.getData().getAmount()/100;
                        shoulePayTv.setTextColor(Color.parseColor("#FF8A8A"));
                        realPayTv.setText("¥" +
                                ""+ df.format(realPay));
                    }else{
                        if(ToolsHelper.isNull(couponBean.getPrice()+"")){
                            couponTv.setText("");
                        }else {
                            tickId = couponBean.getId();
                            couponPrice = ToolsHelper.stringTOfloat(couponBean.getPrice()+"")/100;
//                        couponTv.setText(df.format(couponPrice) + "元");

                            couponTv.setText(couponBean.getTitle());
                            realPay = orderInfoModel.getData().getAmount()/100 - couponPrice;
                            if(realPay > 0.0){
                                realPayTv.setText("¥"+ df.format(realPay));
                            }else{
                                realPay = 0.01f;
                                realPayTv.setText("¥"+ realPay);
                            }
//                            shoulePayTv.setTextColor(Color.parseColor("#C8C8C8"));
//                            shoulePayTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线


                        }
                    }

                }
            }
        }
    }
    public void onEventMainThread(RequestPay requestPay) {
        int status = requestPay.getStatus();
        switch (status){
            case -1:
                break;
            case -2:
                break;
            case 0:
                startMainActivity();
                break;
        }



    }
    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        LoadingDialog.dismissDialog();
        String apiNo = httpResult.getApiNo();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getExistOrder:
                    orderInfoModel = GsonImplHelp.get().toObject(result, OrderDetailInfoModel.class);
                    if(orderInfoModel != null && orderInfoModel.getData() != null){
                        refrushView();
                    }
                    break;
                case CoreHttpApiKey.foregiftAccountsAlipay:
                case CoreHttpApiKey.foregiftAccountsWxPay:
                case ModuleHttpApiKey.reservationOrdersWalletpay:
                    if(apiNo.equals(ModuleHttpApiKey.reservationOrdersWalletpay)){
                        ToolsHelper.showStatus(mContext, true, "支付成功");
                    }
                    startMainActivity();
                    break;

            }
        }

    }

    private void startMainActivity() {
        MainActivity.isThought = true;
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("ORDER_DATA",dataBean);
        intent.putExtra("pay", (float) realPay);
        startActivity(intent);
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String result = httpResult.getResult();
        LoadingDialog.dismissDialog();
        if(result.equals("")){
            ToolsHelper.showStatus(mContext, false, "请求错误！");
            return;

        }
        FaIlureModel model = GsonImplHelp.get().toObject(result, FaIlureModel.class);
        if (model != null
                && model.getStatus().equals("failure")
                && model.getErrors() != null
                && model.getErrors().size() > 0) {
            ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
            return;
        }
        LoadingDialog.dismissDialog();

    }
    /**
     * 刷新界面ui
     */
    private void refrushView() {
        dataBean = orderInfoModel.getData();
        if("PERSONAL".equals(dataBean.getOrderVehicleType())){
            departmentBalanceRlyt.setVisibility( View.GONE );
            departmentMonthRemainAmountRlyt.setVisibility( View.GONE );
            payRightArrowIv.setVisibility( View.VISIBLE );
            payWayRlyt.setClickable( true );
            saleLl.setVisibility( View.VISIBLE );
        }else if("PUBLIC".equals(dataBean.getOrderVehicleType())){
            departmentBalanceRlyt.setVisibility( View.VISIBLE );
            departmentMonthRemainAmountRlyt.setVisibility( View.VISIBLE );
            double departmentAccount =orderInfoModel.getData().getChangeRemainAmount() * 1.0 / 100;
            double departmentMonthRemainAmount =orderInfoModel.getData().getMonthRemainAmount() * 1.0  / 100;
            departmentBalanceTv.setText("¥" +
                    ""+ df.format(departmentAccount));
            departmentMonthRemainAmountTv.setText("¥" +
                    ""+ df.format(departmentMonthRemainAmount));
            payWay.setText( "部门余额支付" );
            payRightArrowIv.setVisibility( View.INVISIBLE );
            payWayRlyt.setClickable( false );
            saleLl.setVisibility( View.GONE );


        }
        List<OrderDetailInfoModel.DataBean.CourseListBean> courseList = dataBean.getCourseList();
        if(courseList != null && courseList.size() > 0){
            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this,courseList);
            totalLv.setAdapter(orderDetailAdapter);
        }
        realPay = Double.parseDouble(dataBean.getAmount() + "")/100;
        ImageLoaderManager.getInstance().displayImageForCar(mContext, dataBean.getVehiclePicId(), iconCarIv);
        carModel.setText(dataBean.getVehicleBrandName()+ dataBean.getVehicleSeriesName());
        carNo.setText(dataBean.getPlateLicenseNo());
        getCarYardNameTv.setText(dataBean.getReservationLocationName());
        getCarAddressTv.setText(dataBean.getReservationLocationAddress());
        returnCarAddressTv.setText(dataBean.getReturnLocationAddress());
        returnCarNameTv.setText(dataBean.getReturnLocationName());
        discountTv.setText(dataBean.getDiscount() * 10 + "折");
//        if(dataBean.isDelivered()){
//            getCarOwnerRlyt.setVisibility(View.VISIBLE);
//            getCarOwnerSpotTv.setText(dataBean.getDeliverAddress());
////            if(dataBean.getDeliverAmount() == 0.0){
////                deliverAmountTv.setText("0.00 元");
////            }else {
////                deliverAmountTv.setText(df.format(dataBean.getDeliverAmount() / 100)+" 元");
////            }
//        }else{
//
//        }
//        if(dataBean.isPickuped()){
//            returnCarOwnerRlyt.setVisibility(View.VISIBLE);
//            returnCarOwnerSpotTv.setText(dataBean.getPickupAddress());
//            pickupAmountLlyt.setVisibility(View.VISIBLE);
//            if(dataBean.getPickupAmount() == 0.0){
//                pickupAmountTv.setText("0.00 元");
//            }else {
//                pickupAmountTv.setText(df.format(dataBean.getPickupAmount() / 100)+" 元");
//            }
//        }else{
//            pickupAmountLlyt.setVisibility(View.GONE);
//        }
//        getCarTimeTv.setText(TimeHelper.getDateStringString(dataBean.getPickedUpTime(),TimeHelper.FORMAT12));
//        returnCarTimeTv.setText(TimeHelper.getDateStringString(dataBean.getDroppedOffTime(),TimeHelper.FORMAT12));
        odometerTv.setText(dataBean.getOdometer()+"km");
        carUsingTimeTv.setText(dataBean.getTime());
//        if(dataBean.getTime()< 60){
//            carUsingTimeTv.setText(dataBean.getTime() + "分钟");
//        }else{
//            carUsingTimeTv.setText(dataBean.getTime()/60 + "小时" + dataBean.getTime() % 60 +"分钟");
//        }
        if(dataBean.getDistanceCost() == 0.0){
            distanceCostTv.setText("0.00元/公里");
        }else {
            distanceCostTv.setText(df.format(dataBean.getDistanceCost() / 100) + "元/公里");
        }
        if(dataBean.getChargingRuleType() != null && dataBean.getChargingRuleType().equals("TIME_ONLY")){
            if(dataBean.getTimeCost() == 0.0){
                timeCostTv.setText("0.00元/小时");
            }else {
                timeCostTv.setText(df.format(dataBean.getTimeCost() / 100) + "元/小时");
            }
        }else{
            if(dataBean.getTimeCost() == 0.0){
                timeCostTv.setText("0.00元/分钟");
            }else {
                timeCostTv.setText(df.format(dataBean.getTimeCost() / 100) + "元/分钟");
            }
        }

//
//        if(dataBean.getCappedPricePerDay() == 0.0){
//            cappedPricePerDayTv.setText("0.00元");
//        }else {
//            cappedPricePerDayTv.setText(df.format(dataBean.getCappedPricePerDay() / 100) + "元");
//        }
//
//        if(dataBean.getMinPrice() == 0.0){
//            minPriceTv.setText("0.00元");
//        }else {
//            minPriceTv.setText(df.format(dataBean.getMinPrice() / 100) + "元");
//        }

//        if(dataBean.getDistanceAmount() == 0.0){
//            distanceAmountTv.setText("0.00元");
//        }else {
//            distanceAmountTv.setText(df.format(dataBean.getDistanceAmount() / 100) + "元");
//        }
//        if(dataBean.getDispatchAmount() == 0.0){
//            dispatchAmountTv.setText("0.00元");
//        }else {
//            dispatchAmountTv.setText(df.format(dataBean.getDispatchAmount() / 100) + "元");
//        }
//        if(dataBean.getTimeAmount() == 0.0){
//            timeAmountTv.setText("0.00元");
//        }else {
//            timeAmountTv.setText(df.format(dataBean.getTimeAmount() / 100) + "元");
//        }
        if(dataBean.getAmount() == 0.0){
            realPayTv.setText("¥0.00");
        }else {
            realPayTv.setText("¥" + df.format(Double.parseDouble(dataBean.getAmount() + "") / 100));
        }
        if(dataBean.getPayableAmount() == 0){
            shoulePayTv.setText("¥0.00");
        }else{
            shoulePayTv.setText("¥" + df.format(Double.parseDouble(dataBean.getPayableAmount() + "") / 100));
        }
        if("0".equals(objectNo)){
            PopWindowHelper.getInstance().alert(OrderDetailActivity.this, new PopWindowListener() {
                @Override
                public void confirm() {

                }

                @Override
                public void cancel() {

                }

                @Override
                public void onItemListener(int position) {

                }
            }).setContentTx(content).setConfirmTx("确认").show(findViewById(R.id.top__iv));
        }

    }

    /**
     * 支付
     */
    private void startPay() {

        if(PAY_WAY == CHANGE_WAY){
            LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
            ModuleHttpApi.reservationOrdersWalletpay(orderInfoModel.getData().getId(),String.valueOf(tickId));
        }else if(PAY_WAY == WX_WAY){
            payWxAndApli("微信支付");
        }else if(PAY_WAY == ALIPAY_WAY){
            payWxAndApli("支付宝");
        }

    }

    /**
     * 订单支付完成
     */
    private void orderPayFinshed() {
        OrderDetailInfoModel.DataBean dataBean = orderInfoModel.getData();
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_THOUGHT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("THOUGHT",false);
        editor.putLong("id",dataBean.getId());
        editor.putString("name",dataBean.getVehicleBrandName()+dataBean.getVehicleSeriesName());
        editor.putString("plateLicenseNo",dataBean.getPlateLicenseNo());
        editor.putString("vehiclePicId",dataBean.getVehiclePicId());
        if(dataBean.isDelivered()){
            editor.putString("getCarLocation",dataBean.getDeliverName());
            editor.putString("getCarAddress",dataBean.getDeliverAddress());
        }else{
            editor.putString("getCarLocation",dataBean.getReservationLocationName());
            editor.putString("getCarAddress",dataBean.getReservationLocationAddress());
        }
        if(dataBean.isPickuped()){
            editor.putString("returnCarLocation",dataBean.getPickupName());
            editor.putString("returnCarAddress",dataBean.getPickupAddress());
        }else{
            editor.putString("returnCarLocation",dataBean.getReturnLocationName());
            editor.putString("returnCarAddress",dataBean.getReturnLocationAddress());
        }
        editor.putFloat("pay", (float) realPay);
        editor.apply();
    }

    /**
     * 微信或支付宝支付
     */
    private void payWxAndApli(String payMethod){
        OrderPayModel mOrderModel = new OrderPayModel();
        mOrderModel.setAmount(String.valueOf((int)realPay*100));
        mOrderModel.setBusinessId(orderInfoModel.getData().getId());
        if(tickId == 0){
            mOrderModel.setTicketId(null);
        }else{
            mOrderModel.setTicketId(String.valueOf(tickId));
        }
        mOrderModel.setBusinessType(BusinessType.ORDER);
        LoadingDialog.showDialog(mContext, "正在获取订单信息,请稍候");
        PayManager.getInstance().reservationOrdersPay(mOrderModel, payMethod, OrderDetailActivity.this);
    }

    /**
     * 展开或关闭应支付详情
     */
    private void OpenOrCloseShouldPay() {
        if(!isOpenShouldPay){
            isOpenShouldPay = true;
        }else{
            isOpenShouldPay = false;
        }
    }

    /**
     * 选择支付方式
     */
    private void ChoicePayWay() {

        CustomPopuwindow popuwindow = new CustomPopuwindow();
        popuwindow.selectPayWay(this,rootView,PAY_WAY);
        popuwindow.setOnChoicePayWayListener(new CustomPopuwindow.OnChoicePayWayListener() {
            @Override
            public void selectedPayWay(int pay) {
                PAY_WAY = pay;
                if(PAY_WAY == CHANGE_WAY){
                    payWay.setText("余额支付");
                    payWayIv.setImageResource(R.drawable.ic_pay);
                }else if(PAY_WAY == WX_WAY){
                    payWay.setText("微信支付");
                    payWayIv.setImageResource(R.drawable.ic_wx);
                }else if(PAY_WAY == ALIPAY_WAY){
                    payWay.setText("支付宝支付");
                    payWayIv.setImageResource(R.drawable.ic_alipay);
                }
            }
        });
    }

}
