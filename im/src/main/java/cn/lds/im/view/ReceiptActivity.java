package cn.lds.im.view;

import android.content.Intent;
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
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ReceiptModel;
import de.greenrobot.event.EventBus;

/**
 * 钱包-》发票页面
 */
public class ReceiptActivity extends BaseActivity {

    // R.id.top__iv
    @ViewInject(R.id.top__iv)
    public ImageView mTop_iv;

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    //我的发票
    @ViewInject(R.id.top_menu_btn)
    protected Button mMyReceipt;

    @ViewInject(R.id.wallet_figure_textview)
    protected TextView wallet_figure_textview;


    protected ReceiptModel receiptModel;//可开发票总额

    protected ReceiptActivity activity;
    protected int layoutID = R.layout.activity_receipt;

    public void setActivity(ReceiptActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ReceiptActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }
        initConfig();
    }
    protected void initConfig() {
        init();
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.receiptactivity_titile));
        btnBack.setVisibility(View.VISIBLE);
        mMyReceipt.setVisibility(View.VISIBLE);
        mMyReceipt.setText(getString(R.string.receipt_mine));
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn,
            R.id.receiptactivity_confirm_tv,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn:
                mIntent.setClass(mContext, ReceiptListActivity.class);
                startActivity(mIntent);
                break;
            case R.id.receiptactivity_confirm_tv:
                try {
                    long money = Long.parseLong(receiptModel.getData());
                    if (money > 20000) {
                        mIntent.setClass(mContext, BuildReceiptActivity.class);
                        mIntent.putExtra("amount", receiptModel.getData());
                        startActivity(mIntent);
                    } else {
                        //小于200元提示
                        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
                            @Override
                            public void confirm() {

                            }

                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void onItemListener(int position) {

                            }
                        }).setConfirmTx(getString(R.string.receiptactivity_alert_confirm)).
                                setContentTx(getString(R.string.receiptactivity_alert_content)).
                                show(findViewById(R.id.top__iv));
                    }
                } catch (Exception e) {
                }
                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.showDialog(mContext, "获取可开发票总额，请稍候");
        ModuleHttpApi.getAmount();
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getAmount.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getAmount:
                    E001(httpResult.getResult());
                    break;
            }
        }

    }

    protected void E001(String result) {
        receiptModel = GsonImplHelp.get().toObject(result, ReceiptModel.class);
        if(ToolsHelper.isNull(receiptModel.getData())){
            wallet_figure_textview.setText("0.00");
        } else {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            wallet_figure_textview.setText(df.format(ToolsHelper.stringTOdouble(receiptModel.getData())/100 )+ "");
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getAmount.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext,false, "获取可开发票总额失败");
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
