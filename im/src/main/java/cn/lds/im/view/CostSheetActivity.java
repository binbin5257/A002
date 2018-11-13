package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.PaymentsDetailModel;
import de.greenrobot.event.EventBus;

/**
 * 费用明细页面
 */
public class CostSheetActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.costsheetactivity_paymentDescription)
    protected TextView costsheetactivity_paymentDescription;
    @ViewInject(R.id.costsheetactivity_odometer)
    protected TextView costsheetactivity_odometer;
    @ViewInject(R.id.costsheetactivity_time)
    protected TextView costsheetactivity_time;
    @ViewInject(R.id.costsheetactivity_amountPayable)
    protected TextView costsheetactivity_amountPayable;
    @ViewInject(R.id.costsheetactivity_amount)
    protected TextView costsheetactivity_amount;
    @ViewInject(R.id.costsheetactivity_amountaccount)
    protected TextView costsheetactivity_amountaccount;
    @ViewInject(R.id.costsheetactivity_timecost)
    protected TextView costsheetactivity_timecost;
    @ViewInject(R.id.costsheetactivity_distancecost)
    protected TextView costsheetactivity_distancecost;
    @ViewInject(R.id.costsheetactivity_yajin)
    protected TextView costsheetactivity_yajin;


    protected String id;
    protected CostSheetActivity activity;
    protected int layoutID = R.layout.activity_cost_sheet;

    public void setActivity(CostSheetActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(CostSheetActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }
        initConfig();
    }
    protected void initConfig() {
        id = getIntent().getAction();
        init();
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.costsheetactivity_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);

            ModuleHttpApi.reservationOrderPaymentsDetail(id);
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
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrderPaymentsDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.reservationOrderPaymentsDetail:
                    C007(httpResult.getResult());
                    break;
            }
        }

    }


    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrderPaymentsDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取费用明细失败");
    }

    protected void C007(String result) {
        PaymentsDetailModel detailModel = GsonImplHelp.get().toObject(result, PaymentsDetailModel.class);
        if (null != detailModel.getData())
            setData(detailModel.getData());
    }

    protected void setData(PaymentsDetailModel.DataBean data) {
        try {
            //计费说明
            costsheetactivity_paymentDescription.setText(ToolsHelper.getDouble(data.getTimeCost() / 100)
                    + mContext.getString(R.string.order_unit_minute)
                    + ToolsHelper.getDouble(data.getDistanceCost() / 100)
                    + mContext.getString(R.string.order_unit_mileage));
            //总里程
            costsheetactivity_odometer.setText(ToolsHelper.getDouble(data.getOdometer()));
            //总时长
            costsheetactivity_time.setText(ToolsHelper.getDouble(data.getTime()));
            // 应付金额
            costsheetactivity_amountPayable.setText(ToolsHelper.getDouble(data.getAmountPayable() / 100));
            //余额支付
            costsheetactivity_amountaccount.setText("0.00");
            //实付
            costsheetactivity_amount.setText(ToolsHelper.getDouble(data.getAmount() / 100));
            //里程费
            double b = data.getDistanceCost() * data.getOdometer();
            costsheetactivity_distancecost.setText(ToolsHelper.getDouble(b / 100));
            //用时费
            double t = data.getTimeCost() * data.getTime();
            costsheetactivity_timecost.setText(ToolsHelper.getDouble(t / 100));
            AccountsTable table = AccountManager.getInstance().findByNo();
            //押金
            costsheetactivity_yajin.setText(table.getForegiftAmount());
        } catch (Exception e) {
            LogHelper.e(e.toString());
        }
    }
}
