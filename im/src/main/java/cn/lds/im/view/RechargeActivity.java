package cn.lds.im.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.DepartmentAccountModel;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PaymentModel;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MessagePay;
import cn.lds.chatcore.event.StartPay;
import cn.lds.chatcore.manager.PayManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.chatcore.data.OrderPayModel;
import cn.lds.im.enums.BusinessType;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

/**
 * 钱包-->充值 页面
 */
public class RechargeActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.root_view)
    protected LinearLayout rootView;
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView mTopTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    //充值方式
    @ViewInject(R.id.order_payment_method_tv)
    protected TextView order_payment_method_tv;
    @ViewInject(R.id.order_payment_method_iv)
    protected ImageView order_payment_method_iv;
    //充值金额
    @ViewInject(R.id.recharge_amount_et)
    private EditText mAmount;
    //部门充值布局
    @ViewInject(R.id.rl_department_balance)
    private RelativeLayout departmentBalanceRlyt;
    //部门布局
    @ViewInject(R.id.rl_department_monthRemainAmount)
    private RelativeLayout departmentMonthRemainAmountRlyt;

    //部门充值布局
    @ViewInject(R.id.tv_department_balance)
    private TextView departmentBalanceTv;
    //部门充值布局
    @ViewInject(R.id.tv_department_monthRemainAmount)
    private TextView departmentMonthRemainAmountTv;

    protected String recharge_money;
    protected OrderPayModel mOrderModel = new OrderPayModel();
    protected IWXAPI api;
    /**
     * 支付方式数据
     */
    protected List<PopData> lists = new ArrayList<>();
    protected int select = 0;//当前支付方式

    protected RechargeActivity activity;
    protected int layoutID = R.layout.activity_recharge;
    private String flag;
    private DecimalFormat df;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(RechargeActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(RechargeActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }

        initConfig();

    }

    protected void initConfig() {
        init();
    }

    public void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            flag = bundle.getString("FLAG");
            if(!TextUtils.isEmpty(flag) && "DEPARTMENT_RECHARGE".equals(flag)){
                mTopTitle.setVisibility(View.VISIBLE);
                departmentBalanceRlyt.setVisibility( View.VISIBLE);
                departmentMonthRemainAmountRlyt.setVisibility( View.VISIBLE);
                mTopTitle.setText("部门充值");
                df = new DecimalFormat("#,##0.00");
                LoadingDialog.showDialog(mContext, "请稍候...");
            }
        }else{
            departmentBalanceRlyt.setVisibility( View.GONE );
            departmentMonthRemainAmountRlyt.setVisibility( View.GONE );
            mTopTitle.setVisibility(View.VISIBLE);
            mTopTitle.setText(getString(R.string.rechargeactivity_title));
        }

        btnBack.setVisibility(View.VISIBLE);
        api = WXAPIFactory.createWXAPI(this, AppIndependentConfigure.appId);

        lists.addAll(ToolsHelper.getPayType(true));
        select = 0;
        putPaymentMethod(select);//默认设置 支付方式
    }

    protected void putPaymentMethod(int position) {
        PopData data = lists.get(position);
        order_payment_method_tv.setText(data.getText());
        order_payment_method_iv.setImageResource(data.getImageID());
    }

    @OnClick({R.id.top_back_btn,//返回按钮
            R.id.rechargeactivity_confirm_tv,//充值
            R.id.order_payment_method_ryt,//支付方式
            R.id.recharge_contact_iv,//联系客服
            R.id.recharge_contact_tv
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.rechargeactivity_confirm_tv:
                recharge_money = mAmount.getText().toString();
                if (ToolsHelper.isNull(recharge_money)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.recharge_amount_prompt));
                    return;
                }
                int money = ToolsHelper.stringTOint(recharge_money) * 100;
                mOrderModel.setAmount(money + "");
                if(!TextUtils.isEmpty(flag) && "DEPARTMENT_RECHARGE".equals(flag)){
                    mOrderModel.setBusinessType(BusinessType.DEPARTMENT);
                }else{
                    mOrderModel.setBusinessType(BusinessType.RECHARGE);
                }
//                LoadingDialog.showDialog(mContext, "请稍候...");
                PayManager.getInstance().accounts(mOrderModel, order_payment_method_tv.getText().toString(), RechargeActivity.this);
                break;
            case R.id.order_payment_method_ryt:
                ChoicePayWay();
                break;
            case R.id.recharge_contact_iv:
            case R.id.recharge_contact_tv:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
        }

    }

    /**
     * 选择支付方式
     */
    private void ChoicePayWay() {
        CustomPopuwindow popuwindow = new CustomPopuwindow();
        popuwindow.payWay(this, rootView, select);
        popuwindow.setOnChoicePayWayListener(new CustomPopuwindow.OnChoicePayWayListener() {
            @Override
            public void selectedPayWay(int pay) {
                lists.get(select).setSelect(false);
                lists.get(pay).setSelect(true);
                select = pay;
                putPaymentMethod(select);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(RechargeActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(RechargeActivity.class.getName(), e);
        }
    }

    /**
     * 是否开始支付，如果开始支付则弹出LoadingDialog
     *
     * @param event
     */
    public void onEventMainThread(StartPay event) {
        if (1 != event.getType())
            return;
        LoadingDialog.dismissDialog();
        if (!event.getStatus()) {
            if (null == event.getHttpResult())
                ToolsHelper.showStatus(mContext, false, "获取支付订单失败");
            else
                ToolsHelper.showHttpRequestErrorMsg(mContext, event.getHttpResult());
        } else {
//            LoadingDialog.showDialog(mContext, "正在执行支付操作");
        }
    }

    /**
     * 消息推送接收--收到成功消息finish掉支付页面
     *
     * @param event
     */
    public void onEventMainThread(MessagePay event) {
        if (1 != event.getType())
            return;
        LoadingDialog.dismissDialog();
        if (event.getStatus()) {
            ToolsHelper.showStatus(mContext, true, "充值成功");
            finish();
        }

    }


    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.payment.equals(apiNo))) {
            return;
        }

        LoadingDialog.dismissDialog();
        switch (apiNo) {
            case ModuleHttpApiKey.payment:
                LoadingDialog.dismissDialog();
                ToolsHelper.showStatus(mContext, false, getString(R.string.order_chaoshi));
                break;
        }

    }



    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.payment.equals(apiNo)
                ||ModuleHttpApiKey.getDepartmentBalance.equals(apiNo)
                ||ModuleHttpApiKey.foregiftAccountsAlipay.equals(apiNo)
                ||ModuleHttpApiKey.foregiftAccountsWxPay.equals(apiNo)
        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.payment:
                    PaymentModel paymentModel = GsonImplHelp.get().toObject(result, PaymentModel.class);
                    if (paymentModel.isData()) {
                        EventBus.getDefault().post(new MessagePay(1, true));
                    } else {
                        ToolsHelper.showStatus(mContext, false, "获取支付结果超时");
                        LoadingDialog.dismissDialog();
                    }
                    break;

                case ModuleHttpApiKey.getDepartmentBalance:
                    handleDepartmentBalance(result);
                    break;
                case CoreHttpApiKey.foregiftAccountsAlipay:
                case CoreHttpApiKey.foregiftAccountsWxPay:
                    departmentBalanceTv.setText( "" );
                    break;
            }
        }

    }

    /**
     * 获取部门余额
     */
    public void getDepartmentBalance(){
        ModuleHttpApi.getDepartmentBalance();
    }


    /**
     * 处理部门数据
     * @param result
     */
    private void handleDepartmentBalance( String result ) {
        DepartmentAccountModel model = GsonImplHelp.get().toObject( result,DepartmentAccountModel.class );
        if(model != null){

            double departmentAccount =model.getData().getChangeRemainAmount() * 1.0 / 100;
            double departmentMonthRemainAmount =model.getData().getMonthRemainAmount() * 1.0  / 100;

            departmentBalanceTv.setText("¥" +
                    ""+ df.format(departmentAccount));
            departmentMonthRemainAmountTv.setText("¥" +
                    ""+ df.format(departmentMonthRemainAmount));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(flag) && "DEPARTMENT_RECHARGE".equals(flag)){
            getDepartmentBalance();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadingDialog.dismissDialog();
    }
}
