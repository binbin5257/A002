package cn.lds.im.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;

import cn.lds.chat.R;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.OrderPayModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.event.MessagePay;
import cn.lds.chatcore.event.StartPay;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.PayManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.dialog.ConfirmAndCancelDialog;
import cn.lds.chatcore.view.dialog.PromptDialog;
import cn.lds.chatcore.view.dialog.annotation.ClickPosition;
import cn.lds.chatcore.view.dialog.callback.OnDialogClickListener;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.enums.BusinessType;
import de.greenrobot.event.EventBus;

/**
 * 钱包-->押金 页面
 */
public class CashPledgeActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView mTopTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button mBackBtn;
    //申请退款
    @ViewInject(R.id.top_menu_btn)
    private Button mTop_detail;
    //已缴纳押金
    @ViewInject(R.id.wallet_amount)
    protected TextView mDepositPaid;
    //还需缴纳押金
    @ViewInject(R.id.cash_pledge_deposit)
    protected TextView mNeedPay;
    //微信选择
    @ViewInject(R.id.cash_wechat_select_iv)
    protected ImageView mWechat_iv;
    //支付宝选择
    @ViewInject(R.id.cash_alipay_select_iv)
    protected ImageView mAlipay_iv;
    //充值按钮
    @ViewInject(R.id.rechargeactivity_confirm_tv)
    protected TextView mConfirm;

    //認證狀態
    @ViewInject(R.id.cash_identify_status)
    protected TextView mStatus;
    @ViewInject(R.id.wallet_iv)
    private ImageView mImage;

    protected CashPledgeActivity activity;
    protected int layoutID = R.layout.activity_cash_pledge;
    protected OrderPayModel mOrderModel = new OrderPayModel();
    protected String mDeposit;//需要充值的押金金额
    protected String mPayMethod;//充值方式
    protected String foregiftAmount;//已缴纳押金金额

    public void setActivity(CashPledgeActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(CashPledgeActivity.class, this);
        if(null != activity){
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        CoreHttpApi.enrolleesGet();
        init();
        initdata();
    }

    public void init() {
        mTopTitle.setVisibility(View.VISIBLE);
        mTopTitle.setText(getString(R.string.costsheetactivity_yajin));
        mBackBtn.setVisibility(View.VISIBLE);
        mTop_detail.setText(getString(R.string.cash_pledge_reback));

        //默认微信支付
        mWechat_iv.setImageResource(R.drawable.bg_cash_select);
        mAlipay_iv.setImageResource(R.drawable.bg_cash_cirle);
        mPayMethod = "微信支付";

        AccountsTable table = AccountManager.getInstance().findByNo();
        if (null == table)
            return;
        // 认证
        String reviewtype = table.getReviewType();
        if (ToolsHelper.isNull(reviewtype) || Constants.UNCOMMITTED.equals(reviewtype) || Constants.REFUSED.equals(reviewtype)) {
            mStatus.setText("去认证");
            mImage.setVisibility(View.VISIBLE);
        }else if(Constants.REVIEWING.equals(reviewtype)){
            mStatus.setText("审核中");
            mImage.setVisibility(View.GONE);
        }else {
            mStatus.setText("已认证");
            mImage.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.top_back_btn,//返回按钮
            R.id.top_menu_btn,//申请退款
            R.id.cash_wechat_rly,//微信支付
            R.id.cash_alipay_rly,//支付宝支付
            R.id.rechargeactivity_confirm_tv,//充值
            R.id.wallet_remind_lly//去认证
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn:
                PopWindowHelper.getInstance().reback(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        if(ToolsHelper.isNull(foregiftAmount) || "0".equals(foregiftAmount)){
                            ToolsHelper.showInfo(mContext,"您已没有可退押金");
                            return;
                        }
                        LoadingDialog.showDialog(mContext, "处理中，请稍后");
                        ModuleHttpApi.refund();
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setConfirmTx(getResources().getString(R.string.cash_pledge_reback_money)).show(findViewById(R.id.top__iv));
                break;
            case R.id.cash_wechat_rly:
                mWechat_iv.setImageResource(R.drawable.bg_cash_select);
                mAlipay_iv.setImageResource(R.drawable.bg_cash_cirle);
                mPayMethod = "微信支付";
                break;
            case R.id.cash_alipay_rly:
                mWechat_iv.setImageResource(R.drawable.bg_cash_cirle);
                mAlipay_iv.setImageResource(R.drawable.bg_cash_select);
                mPayMethod = "支付宝";
                break;
            case R.id.rechargeactivity_confirm_tv:
                if (ToolsHelper.isNull(mDeposit)) {
                    return;
                }
                mOrderModel.setAmount(mDeposit);
                mOrderModel.setBusinessType(BusinessType.DEPOSIT);
                LoadingDialog.showDialog(mContext, "正在获取订单信息,请稍候");
                PayManager.getInstance().depositPay(mOrderModel, mPayMethod, CashPledgeActivity.this);
                break;
            case R.id.wallet_remind_lly://认证
                mIntent.setClass(mContext, AuthenticationActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    /**
     * 刷新页面余额
     */
    protected void initdata() {
        AccountsTable table = AccountManager.getInstance().findByNo();
        if (null == table)
            return;
        foregiftAmount = table.getForegiftAmount();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        mDeposit = table.getForegiftQuota();
        //已支付押金
        if (!ToolsHelper.isNull(foregiftAmount))
            mDepositPaid.setText(df.format(ToolsHelper.stringTOdouble(foregiftAmount)/100) + "元");
        else
            mDepositPaid.setText("0.00" + "元");
        //还需支付的金额
        if (!ToolsHelper.isNull(mDeposit) && (!ToolsHelper.isNull(foregiftAmount))){
            double needPay = ToolsHelper.stringTOdouble(mDeposit) - ToolsHelper.stringTOdouble(foregiftAmount);
            mNeedPay.setText(df.format(needPay / 100) + "元");
            if(needPay == 0){
                mConfirm.setBackgroundResource(R.drawable.btn_bg_gray);
                mConfirm.setClickable(false);
            }else {
                mConfirm.setBackgroundResource(R.drawable.btn_confirm_bg);
                mConfirm.setClickable(true);
            }
        }else {
            mNeedPay.setText("0.00" + "元");
            mConfirm.setBackgroundResource(R.drawable.btn_bg_gray);
            mConfirm.setClickable(false);
        }
    }

    /**
     * 是否开始支付，如果开始支付则弹出LoadingDialog
     *
     * @param event
     */
    public void onEventMainThread(StartPay event) {
        if (0 != event.getType())
            return;
        LoadingDialog.dismissDialog();
        if (!event.getStatus()) {
            if (null == event.getHttpResult())
                ToolsHelper.showStatus(mContext, false, "获取支付订单失败");
//            else
//                ToolsHelper.showHttpRequestErrorMsg(mContext, event.getHttpResult());
        } else {
            LoadingDialog.showDialog(mContext, "正在执行支付操作");
        }
    }
    /**
     * 消息推送接收--收到成功消息finish掉支付页面
     *
     * @param event
     */
    public void onEventMainThread(MessagePay event) {
        if (0 != event.getType())
            return;
        LoadingDialog.dismissDialog();
        if (event.getStatus()) {
            ToolsHelper.showStatus(mContext, true, "充值成功");
            finish();
        }

    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.refund.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.refund:
                    mIntent.setClass(mContext,RebackSuccessActivity.class);
                    mIntent.putExtra("foregiftAmount", foregiftAmount);
                    startActivity(mIntent);
                    CoreHttpApi.enrolleesGet();
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.refund.equals(apiNo)
             ||ModuleHttpApiKey.foregiftAccountsWxPay.equals(apiNo)
             ||ModuleHttpApiKey.foregiftAccountsAlipay.equals(apiNo)
        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(),FaIlureModel.class);
        if(model != null && model.getErrors() != null && model.getErrors().size() > 0){
            String errcode = model.getErrors().get(0).getErrcode();
            String errmsg = model.getErrors().get(0).getErrmsg();
            switch (apiNo){
                case ModuleHttpApiKey.refund:
                    refundError(errcode,errmsg); // 申请退款错误结果
                    break;
                case ModuleHttpApiKey.foregiftAccountsWxPay: // 微信充值错误结果
                case ModuleHttpApiKey.foregiftAccountsAlipay:// 支付宝充值款错误结果
                    rechargeError(errcode,errmsg);
                break;
            }

        }

    }

    private void rechargeError(String errcode, String errmsg) {
        if("foregift.withdrawals.exists".equals(errcode)){
            ConfirmAndCancelDialog dialog = new ConfirmAndCancelDialog(this,R.style.MyDialogStyle).setOnDialogClickListener(new OnDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dialog, String clickPosition) {
                    switch (clickPosition){
                        case ClickPosition.ACCEPT:
                            PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                            break;
                        case ClickPosition.CANCEL:

                            break;
                    }
                }
            });
            dialog.setConfirmText("联系客服");
            dialog.setCancelText("暂不联系");
            dialog.setPromptContent(errmsg);
            dialog.show();
        }else{
            ToolsHelper.showStatus(this,false,errmsg);
        }

    }

    /**
     * 申请退款错误结果
     * @param errcode
     * @param errmsg
     */
    private void refundError(String errcode, String errmsg) {
        if(!TextUtils.isEmpty(errcode) && errcode.equals("personal.order.has.unfinished")){
            PromptDialog dialog = new PromptDialog(this,R.style.MyDialogStyle).setOnDialogClickListener(new OnDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dialog, String clickPosition) {

                }
            });
            dialog.setPromptContent(errmsg);
            dialog.show();
        }else{
            ToolsHelper.showStatus(mContext, false, "申请退款失败");
        }
    }

    public void onEventMainThread(MessageArrivedEvent event) {
        initdata();
    }

    public void onEventMainThread(AccountAvaliableEvent event) {
        initdata();
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

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.dismissDialog();
        initdata();
    }
}
