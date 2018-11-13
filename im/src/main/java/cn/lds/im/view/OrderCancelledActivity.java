package cn.lds.im.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
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
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.data.PayDetailModel;
import cn.lds.im.data.TripInfoModel;
import cn.lds.im.enums.BusinessType;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

/**
 * 已取消订单产生费用界面
 * Created by gaihd on 2017/7/17.
 */

public class OrderCancelledActivity extends BaseActivity implements View.OnClickListener {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView titleTv;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button backBtn;
    //客服
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    /**
     * 取车地点
     */
    @ViewInject(R.id.get_car_address)
    private TextView getCarAddressTv;
    /**
     * 取车时间
     */
    @ViewInject(R.id.get_car_time)
    private TextView getCarTimeTv;
    /**
     * 还车地址
     */
    @ViewInject(R.id.return_car_address)
    private TextView returnCarAddressTv;
    /**
     * 还车时间
     */
    @ViewInject(R.id.return_car_time)
    private TextView returnCarTimeTv;

    /**
     * 取消描述
     */
    @ViewInject(R.id.order_cancelled_tv)
    private TextView mDescribe;

    /**
     * 取车费用
     */
    @ViewInject(R.id.tv_real_pay)
    private TextView mCost_tv;
    /**
     * 取车费用布局
     */
    @ViewInject(R.id.pick_up_cost)
    private LinearLayout mCost_lly;
    //用于费用格式化--保留两位小数
    private DecimalFormat df;
    protected OrderCancelledActivity activity;
    private String id;
    private PayDetailModel mTripInfoFee;
    private TripInfoModel mTripInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cancelled);
        ViewUtils.inject(OrderCancelledActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化界面UI
     */
    private void initView() {
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("订单详情");
        backBtn.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.ic_help);

        Intent intent = getIntent();
        if (intent == null) {
            ToolsHelper.showInfo(mContext, "数据异常");
            finish();
        } else {
            id = ToolsHelper.toString(intent.getIntExtra("id", -1));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.showDialog(mContext, "处理中，请稍后...");
        ModuleHttpApi.getTripInfo(id);
//        ModuleHttpApi.getTripInfoFee(id);
    }

    /**
     * 处理页面点击事件
     *
     * @param v
     */
    @OnClick({R.id.top_back_btn, R.id.top_menu_btn_del, R.id.tv_contact})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn: //返回上一页
                finish();
                break;
            case R.id.top_menu_btn_del:
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/faq.html", getString(R.string.settingactivity_useualproblem));
                break;
            case R.id.tv_contact:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
        }
    }

    protected void refreshUI(String result) {
        mTripInfo = GsonImplHelp.get().toObject(result, TripInfoModel.class);
        if (null == mTripInfo)
            return;
        TripInfoModel.DataBean dataBean = mTripInfo.getData();
        if (dataBean == null) {
            ToolsHelper.showInfo(mContext, "获取行程详情失败！");
        } else {

            returnCarAddressTv.setText(ToolsHelper.isNullString(dataBean.getReturnLocationName()));
            if(dataBean.isDelivered()){
                if(dataBean.getScheduledPickedUpTime() != null){
                    getCarTimeTv.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getScheduledPickedUpTime()), TimeHelper.FORMAT12));
                }else{
                    getCarTimeTv.setText("");
                }
                returnCarTimeTv.setText("");

            }else{
                if(dataBean.getType().equals("INSTANT")){
                    getCarTimeTv.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getReservationTime()), TimeHelper.FORMAT12));
                    returnCarTimeTv.setText("");
                }else{
                    getCarTimeTv.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getScheduledPickedUpTime()), TimeHelper.FORMAT12));
                    returnCarTimeTv.setText("");
                }

            }

            if (dataBean.isDeliverNoFreeCancel()) {
                getCarAddressTv.setText("送车上门");
                mDescribe.setText("本次行程属于有责取消需支付取车费用!");
                mCost_lly.setVisibility(View.VISIBLE);
            } else {
                getCarAddressTv.setText(ToolsHelper.isNullString(dataBean.getReservationLocationName()));
                mDescribe.setText("您当前订单已取消!");
                mCost_lly.setVisibility(View.GONE);
            }

        }
    }

    protected void refreshUIFee(String result) {

        mTripInfoFee = GsonImplHelp.get().toObject(result, PayDetailModel.class);
        if (null == mTripInfoFee)
            return;
        PayDetailModel.DataBean dataBean = mTripInfoFee.getData();
        if (dataBean == null) {
            ToolsHelper.showInfo(mContext, "获取行程详情失败！");
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            if (ToolsHelper.isNull(dataBean.getDeliverAmount())) {
                mCost_tv.setText("¥" + "0.00");
            } else {
                mCost_tv.setText("¥" + df.format(ToolsHelper.stringTOdouble(dataBean.getAmount())/100));
            }
        }

    }


    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getTripInfo.equals(apiNo)
                || ModuleHttpApiKey.getTripInfoFee.equals(apiNo))) {
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
