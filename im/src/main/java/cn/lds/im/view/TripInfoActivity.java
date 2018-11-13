package cn.lds.im.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.DateHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.enums.DateTimeType;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.OrderDetailInfoModel;
import cn.lds.im.data.PayDetailModel;
import cn.lds.im.data.TripInfoModel;
import cn.lds.im.view.adapter.OrderDetailAdapter;
import cn.lds.im.view.widget.MyListView;
import de.greenrobot.event.EventBus;

/**
 * 行程信息页面
 * Created by sibinbin on 2017/7/20.
 */

public class TripInfoActivity extends BaseActivity implements View.OnClickListener {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView titleTv;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button backBtn;
    //车型
    @ViewInject(R.id.car_model)
    private TextView car_model;
    //车牌
    @ViewInject(R.id.car_no)
    private TextView car_no;
//    //取车地点
//    @ViewInject(R.id.take_car_location)
//    private TextView take_car_location;
//    //还车地点
//    @ViewInject(R.id.reback_car_location)
//    private TextView reback_car_location;
//    //取车时间
//    @ViewInject(R.id.take_car_date)
//    private TextView take_car_date;
//    //还车时间
//    @ViewInject(R.id.reback_car_date)
//    private TextView reback_car_date;
    //行驶里程
    @ViewInject(R.id.trip_mileage)
    private TextView trip_mileage;
    //行驶时间
    @ViewInject(R.id.trip_time)
    private TextView trip_time;
    //里程消费
    @ViewInject(R.id.trip_cost)
    private TextView trip_cost;
    //时间消费
    @ViewInject(R.id.time_cost)
    private TextView time_cost;
    //调度费用
    @ViewInject(R.id.dispatch_cost)
    private TextView dispatch_cost;
    //上门取车容器
    @ViewInject(R.id.ll_get_car)
    private LinearLayout getCarLlyt;
    //上门取车
    @ViewInject(R.id.get_car_door)
    private TextView getCarDoor;
    //送车上门容器
    @ViewInject(R.id.ll_send_car)
    private LinearLayout sendCarLlyt;
    //送车上门
    @ViewInject(R.id.send_car_door)
    private TextView sendCarDoor;
    //合计
    @ViewInject(R.id.trip_total)
    private TextView trip_total;

    @ViewInject(R.id.get_car_name)
    private TextView getCarYardNameTv;
    @ViewInject(R.id.get_car_address)
    private TextView getCarAddressTv;
    @ViewInject(R.id.rl_owner_get_car)
    private RelativeLayout getCarOwnerRlyt;
    @ViewInject(R.id.get_car_spot)
    private TextView getCarOwnerSpotTv;
    @ViewInject(R.id.rl_owner_return_car)
    private RelativeLayout returnCarOwnerRlyt;
    @ViewInject(R.id.return_car_spot)
    private TextView returnCarOwnerSpotTv;
    @ViewInject(R.id.return_car_name)
    private TextView returnCarNameTv;
    @ViewInject(R.id.return_car_address)
    private TextView returnCarAddressTv;

    @ViewInject(R.id.tv_distanceCost)
    private TextView distanceCost;

    @ViewInject(R.id.tv_timeCost)
    private TextView timeCost;

    @ViewInject(R.id.tv_end_amount)
    private TextView cappedPricePerDay;

    @ViewInject(R.id.tv_start_amount)
    private TextView minPrice;
    @ViewInject(R.id.ll_ticked)
    private LinearLayout tickedLlyt;
    @ViewInject(R.id.tv_ticketDiscountAmount)
    private TextView ticketDiscountAmount;
    @ViewInject(R.id.main_view)
    private LinearLayout mainView;
    @ViewInject(R.id.iv_car)
    private ImageView iconCarIv;

    @ViewInject(R.id.total_lv)
    private MyListView totalLv;
    @ViewInject(R.id.total_lv_ll)
    private LinearLayout totalLvLlyt;
    @ViewInject(R.id.right_arrow_total)
    private ImageView arrowTotalIv;
   @ViewInject(R.id.tv_discount)
    private TextView discountTv;
    @ViewInject(R.id.trip_order_price)
    private TextView trip_order_price;
    @ViewInject( R.id.ll_discount )
    private LinearLayout discountLlyt;
    @ViewInject( R.id.tv_pick_up_time )
    private TextView pickUpCarTime;
    @ViewInject( R.id.tv_return_time )
    private TextView retrunCarTime;
    @ViewInject( R.id.tv_order_type )
    private TextView orderTypeTv;
    @ViewInject( R.id.tv_pay_time )
    private TextView payTimeTv;


    /**
     * 计费规则
     */
    @ViewInject(R.id.top_right_tv)
    private TextView topMenuRight;


    private String id;
    protected TripInfoActivity activity;
    private PayDetailModel mTripInfoFee;
    private OrderDetailInfoModel mTripInfo;
    private String event;
    private String objectNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        ViewUtils.inject(TripInfoActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initView();
    }

    /**
     * 初始化界面UI
     */
    private void initView() {
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("行程信息");
        backBtn.setVisibility(View.VISIBLE);
        topMenuRight.setVisibility(View.VISIBLE);
        topMenuRight.setText("计费说明");

        Intent intent = getIntent();
        if (intent == null) {
            ToolsHelper.showInfo(mContext, "数据异常");
            finish();
        } else {
            id = ToolsHelper.toString(intent.getIntExtra("id", -1));
            /**
             * 客服帮助免费还车，从CarInUseActivity页面传递过来数据
             */
            Bundle extras = intent.getExtras();
            if(extras != null){
                objectNo = extras.getString("objectNo");
                event = extras.getString("event");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.showDialog(mContext, "处理中，请稍后...");
        ModuleHttpApi.getTripInfo(id);
    }

    protected void refreshUI(String result) {
        mTripInfo = GsonImplHelp.get().toObject(result, OrderDetailInfoModel.class);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        ImageLoaderManager.getInstance().displayImageForCar(mContext, mTripInfo.getData().getVehiclePicId(), iconCarIv);
        if (null == mTripInfo)
            return;
        OrderDetailInfoModel.DataBean dataBean = mTripInfo.getData();

        if (dataBean == null) {
            ToolsHelper.showInfo(mContext,"获取行程详情失败！");
        } else {
            List<OrderDetailInfoModel.DataBean.CourseListBean> courseList = dataBean.getCourseList();
            if(courseList != null && courseList.size() > 0){
                OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this,courseList);
                totalLv.setAdapter(orderDetailAdapter);
            }
            car_model.setText(ToolsHelper.isNullString(dataBean.getVehicleBrandName()) +
            ToolsHelper.isNullString(dataBean.getVehicleSeriesName()));
            car_no.setText(ToolsHelper.isNullString(dataBean.getPlateLicenseNo()));
            getCarYardNameTv.setText(dataBean.getReservationLocationName());
            getCarAddressTv.setText(dataBean.getReservationLocationAddress());
            returnCarNameTv.setText(dataBean.getReturnLocationName());
            returnCarAddressTv.setText(dataBean.getReturnLocationAddress());
            if("PUBLIC".equals( dataBean.getOrderVehicleType() )){
                discountLlyt.setVisibility( View.GONE );
                orderTypeTv.setText( "公务用车费用" );
                if(ToolsHelper.isNull(String.valueOf(dataBean.getPublicAmount()))){
                    trip_order_price.setText("¥" + "0.00");
                }else {
                    trip_order_price.setText("¥" + df.format(ToolsHelper.stringTOdouble(dataBean.getPublicAmount() + "")/100));
                }
            }else{
                discountLlyt.setVisibility( View.VISIBLE );
                orderTypeTv.setText( "私人用车费用" );
                if(ToolsHelper.isNull(String.valueOf(dataBean.getPersonalAmount()))){

                    trip_order_price.setText("¥" + "0.00");
                }else {
                    trip_order_price.setText("¥" + df.format(ToolsHelper.stringTOdouble(dataBean.getPersonalAmount() + "")/100));
                }
            }
            pickUpCarTime.setText( TimeHelper.getDateStringString(dataBean.getPickedUpTime(), TimeHelper.FORMAT12));
            retrunCarTime.setText( TimeHelper.getDateStringString(dataBean.getDroppedOffTime(), TimeHelper.FORMAT12));
            discountTv.setText(dataBean.getDiscount() * 10 + "折");
            if(dataBean.isDelivered()){
                getCarOwnerRlyt.setVisibility(View.VISIBLE);
                getCarOwnerSpotTv.setText(dataBean.getDeliverAddress());
            } else {
                getCarOwnerRlyt.setVisibility(View.GONE);
            }
            if(dataBean.isPickuped()){
                returnCarOwnerRlyt.setVisibility(View.VISIBLE);
                returnCarOwnerSpotTv.setText(dataBean.getPickupAddress());
            } else {
                returnCarOwnerRlyt.setVisibility(View.GONE);
            }
        }
    }

    protected void refreshUIFee(String result) {

        mTripInfoFee = GsonImplHelp.get().toObject(result, PayDetailModel.class);
        if (null == mTripInfoFee)
            return;
        PayDetailModel.DataBean dataBean = mTripInfoFee.getData();
        payTimeTv.setText( TimeHelper.getDateStringString(dataBean.getPayTime(), TimeHelper.FORMAT12));

        if (dataBean == null) {
            ToolsHelper.showInfo(mContext,"获取行程详情失败！");
        } else {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            if(ToolsHelper.isNull(dataBean.getOdometer())){
                trip_mileage.setText("0" + "km");
            }else {
                trip_mileage.setText(dataBean.getOdometer() + "km");
            }
            if(ToolsHelper.isNull(dataBean.getTime())){
                trip_time.setText("0" + "分钟");
            }else {
                trip_time.setText(ToolsHelper.isNullString(dataBean.getTime()));
            }

            if(ToolsHelper.isNull(dataBean.getAmount())){
                trip_total.setText("¥" + "0.00");
            }else {
                trip_total.setText("¥" + df.format(ToolsHelper.stringTOdouble(dataBean.getAmount())/100));
            }

            if(dataBean.getDistanceCost() == 0.0){
                distanceCost.setText("0.00元/公里");
            }else{
                distanceCost.setText(df.format((dataBean.getDistanceCost() / 100)) + "元/公里");

            }
            if(dataBean.getTimeCost() == 0.0){
                timeCost.setText("0.00元/分钟");
            }else{
                timeCost.setText(df.format((dataBean.getTimeCost() / 100)) + "元/分钟");

            }
            if(dataBean.getCappedPricePerDay() == 0.0){
                cappedPricePerDay.setText("¥0.00");
            }else{
                cappedPricePerDay.setText("¥" + df.format(dataBean.getCappedPricePerDay()/100));
            }
            if(dataBean.getMinPrice() == 0.0){
                minPrice.setText("¥0.00");
            }else{
                minPrice.setText("¥" + df.format(dataBean.getMinPrice()/100));
            }
            if("1".equals(objectNo)){
                    PopWindowHelper.getInstance().alert(TripInfoActivity.this, new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(event).setConfirmTx("确认").showCenter(mainView);
                }


        }

    }

    /**
     * 处理页面点击事件
     * @param v
     */
    @OnClick({R.id.top_back_btn
    ,R.id.top_right_tv,R.id.rl_total})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn: //返回上一页
                finish();
                break;
            case R.id.top_right_tv: //计费规则页面
                Intent intent2 = new Intent(this,ChargingRuleActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("id",mTripInfo.getData().getModelId());
                bundle2.putString("orderTime",String.valueOf(mTripInfo.getData().getReservationTime()));
                intent2.putExtras(bundle2);
                startActivity(intent2);
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

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getTripInfo.equals(apiNo)
                || ModuleHttpApiKey.getTripInfoFee.equals(apiNo) )) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getTripInfo:
                    refreshUI(httpResult.getResult());
                    ModuleHttpApi.getTripInfoFee(id);
                    break;
                case ModuleHttpApiKey.getTripInfoFee:
                    refreshUIFee(httpResult.getResult());


                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getTripInfo.equals(apiNo)
                || ModuleHttpApiKey.getTripInfoFee.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取行程详情");
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(ReceiptActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(ReceiptActivity.class.getName(), e);
        }
    }
}
