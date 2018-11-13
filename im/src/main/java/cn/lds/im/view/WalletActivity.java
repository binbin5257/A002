package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.GiveBalanceEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.event.ViolationDealtWithEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import de.greenrobot.event.EventBus;

/**
 * 钱包页面
 */
public class WalletActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView mTopTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button mBack_btn;
    //费用明细
    @ViewInject(R.id.top_menu_btn_del)
    private ImageView mTop_detail;
    //账户余额
    @ViewInject(R.id.wallet_amount)
    protected TextView mAmount;
    //押金
    @ViewInject(R.id.wallet_deposit)
    protected TextView mDeposit;
    //認證狀態
    @ViewInject(R.id.wallet_identify_status)
    protected TextView mStatus;
    @ViewInject(R.id.wallet_iv)
    private ImageView mImage;


    protected WalletActivity activity;
    protected int layoutID = R.layout.activity_wallet;

    public void setActivity(WalletActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(WalletActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
    }

    public void init() {
        mTopTitle.setVisibility(View.VISIBLE);
        mTopTitle.setText(getString(R.string.btn_submenu_wallet));
        mBack_btn.setVisibility(View.VISIBLE);
        mTop_detail.setVisibility(View.VISIBLE);
        mTop_detail.setImageResource(R.drawable.bg_mingxi);
//        mTop_detail.setText(getString(R.string.wallet_details));

    }

    @Override
    protected void onResume() {
        super.onResume();
        CoreHttpApi.enrolleesGet();
        initdata();
    }

    /**
     * 刷新页面余额
     */
    protected void initdata() {
        AccountsTable table = AccountManager.getInstance().findByNo();
        if (null == table)
            return;
        //賬戶餘額
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String accountamount = table.getAccountAmount();
        if (!ToolsHelper.isNull(accountamount))
            mAmount.setText(df.format(ToolsHelper.stringTOdouble(table.getAccountAmount()) / 100) + "元");
        else
            mAmount.setText("0.00" + "元");
        //押金
        String foregiftamount = table.getForegiftAmount();
        if (!ToolsHelper.isNull(foregiftamount))
            mDeposit.setText(df.format(ToolsHelper.stringTOdouble(table.getForegiftAmount()) / 100) + "元");
        else
            mDeposit.setText("0.00" + "元");

        // 认证
        String reviewtype = table.getReviewType();
        if (ToolsHelper.isNull(reviewtype) || Constants.UNCOMMITTED.equals(reviewtype) || Constants.REFUSED.equals(reviewtype)) {
            mStatus.setText("去认证");
            mImage.setVisibility(View.VISIBLE);
        } else if (Constants.REVIEWING.equals(reviewtype)) {
            mStatus.setText("审核中");
            mImage.setVisibility(View.GONE);
        } else {
            mStatus.setText("已认证");
            mImage.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.top_back_btn,//返回按钮
            R.id.top_menu_btn_del,//费用明细
            R.id.wallet_recharge_rly,//充值
            R.id.wallet_deposit_rly,//押金
            R.id.wallet_invoice_rly,//发票
            R.id.wallet_coupon_rly,//优惠劵
            R.id.wallet_remind_lly,//去认证
            R.id.cost_statement//费用说明
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回按钮
                finish();
                break;
            case R.id.top_menu_btn_del://费用明细
                mIntent.setClass(mContext, CostDetailActivity.class);
                startActivity(mIntent);
                break;
            case R.id.wallet_recharge_rly://充值
                mIntent.setClass(mContext, RechargeActivity.class);
                startActivity(mIntent);
                break;
            case R.id.wallet_deposit_rly://押金
                mIntent.setClass(mContext, CashPledgeActivity.class);
                startActivity(mIntent);
                break;
            case R.id.wallet_invoice_rly://发票
                mIntent.setClass(mContext, ReceiptActivity.class);
                startActivity(mIntent);
                break;
            case R.id.wallet_coupon_rly://优惠劵
                mIntent.setClass(mContext, CouponListActivityNew.class);
                mIntent.putExtra("fromWallet", "fromWallet");
                startActivity(mIntent);
                break;
            case R.id.wallet_remind_lly://认证
                mIntent.setClass(mContext, AuthenticationActivity.class);
                startActivity(mIntent);
                break;
            case R.id.cost_statement://费用说明
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/balanceRefundNote.html", getString(R.string.cash_pledge_reback_directions));
                break;

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
            LogHelper.e(MessageActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(MessageActivity.class.getName(), e);
        }
    }

}
