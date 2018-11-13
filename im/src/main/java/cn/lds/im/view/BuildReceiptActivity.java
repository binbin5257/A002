package cn.lds.im.view;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.CreateReceiptModel;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

/**
 * 钱包->发票->开发票页面
 */
public class BuildReceiptActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top__iv)
    protected ImageView top__iv;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    @ViewInject(R.id.buildreceiptactivity_inputjine)
    protected EditText buildreceiptactivity_inputjine;
    @ViewInject(R.id.buildreceiptactivity_inputtaitou)
    protected EditText buildreceiptactivity_inputtaitou;
    @ViewInject(R.id.buildreceiptactivity_inputcontact)
    protected EditText buildreceiptactivity_inputcontact;
    @ViewInject(R.id.buildreceiptactivity_inputphone)
    protected EditText buildreceiptactivity_inputphone;
    @ViewInject(R.id.buildreceiptactivity_inputaddress)
    protected EditText buildreceiptactivity_inputaddress;
    @ViewInject(R.id.tv_receipt_type)
    protected TextView tv_receipt_type;
    @ViewInject(R.id.main_view)
    protected LinearLayout mainView;
    @ViewInject(R.id.receipt_no)
    protected LinearLayout receiptNoLayout;
    @ViewInject(R.id.et_receipt_no)
    protected EditText receiptNoInput;
    @ViewInject(R.id.line)
    protected ImageView line;

    protected List<PopData> lists = new ArrayList<>();
    protected int select = 0;//当前发票类型

    protected String invoice_type = "INDIVIDUAL";
    protected CreateReceiptModel mModel;

    protected String amount;
    int DECIMAL_DIGITS = 2;
    private AccountsTable mAccountsTable;

    protected BuildReceiptActivity activity;
    protected int layoutID = R.layout.activity_build_receipt;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(BuildReceiptActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(BuildReceiptActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
    }

    public void init() {

        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.receiptactivity_kaifapiao));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);
        amount = getIntent().getStringExtra("amount");
        mAccountsTable = AccountManager.getInstance().findByNo();
        buildreceiptactivity_inputphone.setHint(mAccountsTable.getMobile());
        DecimalFormat df = new DecimalFormat("#,##0.00");
        buildreceiptactivity_inputjine.setHint(getString(R.string.buildreceiptactivity_zuigaokekai)
                + df.format(ToolsHelper.stringTOdouble(amount)/100 )+ "" +
                getString(R.string.rechargeactivity_yuan));
        buildreceiptactivity_inputjine.setFilters(new InputFilter[]{lengthfilter});
        tv_receipt_type.setText(R.string.buildreceiptactivity_type_ordinary);

        lists.add(new PopData(0, "普通发票", false));
        lists.add(new PopData(0, "增值税专用发票", false));

    }

    /**
     * 设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.buildreceiptactivity_confirm_tv,
            R.id.receipt_type
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.buildreceiptactivity_confirm_tv:
                mModel = new CreateReceiptModel();
                //开票金额
                final String jine = buildreceiptactivity_inputjine.getText().toString();
                if (ToolsHelper.isNull(jine)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.buildreceiptactivity_kaipiaojine) +
                            getString(R.string.buildreceiptactivity_kaipiao_nonenull));
                    return;
                }else {
                    if(ToolsHelper.stringTOlong(jine)*100 < 20000){
                        ToolsHelper.showInfo(mContext,"开发票金额不能小于200");
                        return;
                    } else {
                        mModel.setAmount(ToolsHelper.stringTOlong(jine)*100 + "");
                    }

                }
                //开票抬头
                final String taitou = buildreceiptactivity_inputtaitou.getText().toString();
                if (ToolsHelper.isNull(taitou)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.buildreceiptactivity_kaipiaotaitou) +
                            getString(R.string.buildreceiptactivity_kaipiao_nonenull));
                    return;
                }else {
                    mModel.setTitle(taitou);
                }
                //发票类型
                final String invoiceType = ToolsHelper.toString(tv_receipt_type.getText().toString());
                if (ToolsHelper.isNull(invoiceType)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.buildreceiptactivity_invoice_type) +
                            getString(R.string.buildreceiptactivity_kaipiao_nonenull));
                } else {
                    if ("普通发票".equals(invoiceType)) {
                        invoice_type = "INDIVIDUAL";
                    } else {
                        String receiptNoStr = receiptNoInput.getText().toString().trim();
                        if(TextUtils.isEmpty(receiptNoStr)){
                            ToolsHelper.showStatus(this,false,"请输入发票税号");
                            return;
                        }
                        invoice_type = "CORPORATION";
                        mModel.setTaxId(receiptNoStr);
                    }
                    mModel.setInvoiceType(invoice_type);
                }
                //contact
                final String contact = buildreceiptactivity_inputcontact.getText().toString();
                if (ToolsHelper.isNull(contact)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.buildreceiptactivity_contact) +
                            getString(R.string.buildreceiptactivity_kaipiao_nonenull));
                    return;
                }else {
                    mModel.setRecipientName(contact);
                }
                //mobile
                String mobile = buildreceiptactivity_inputphone.getText().toString();
                String mobileHint = buildreceiptactivity_inputphone.getHint().toString();
                if(TextUtils.isEmpty(mobile)){
                    if(!TextUtils.isEmpty(mobileHint) && mobileHint.equals(mAccountsTable.getMobile())){
                        mobile = mobileHint;
                    }
                }
                if (!ToolsHelper.isMobileNO(mContext, mobile)) {
                    return;
                }else {
                    mModel.setRecipientPhone(mobile);
                }
                //address
                final String address = buildreceiptactivity_inputaddress.getText().toString();
                if (ToolsHelper.isNull(address)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.buildreceiptactivity_address) +
                            getString(R.string.buildreceiptactivity_kaipiao_nonenull));
                    return;
                }else {
                    mModel.setRecipientAddress(address);
                }
                PopWindowHelper.getInstance().remind(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        LoadingDialog.showDialog(mContext, getString(R.string.buildreceiptactivity_kaipiao_loading));
                        ModuleHttpApi.createReceipt(mModel);
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx(getString(R.string.receipt_remind)).setContentTxSize(14).setConfirmTx("确认").setCancelTx("取消").show(top__iv);
                break;
            case R.id.receipt_type:
                selectedRecciptType();
//                PopWindowHelper.getInstance().menu(mContext, lists, true, new PopWindowListener() {
//                    @Override
//                    public void confirm() {
//                    }
//
//                    @Override
//                    public void cancel() {
//
//                    }
//
//                    @Override
//                    public void onItemListener(int position) {
//                        lists.get(select).setSelect(false);
//                        lists.get(position).setSelect(true);
//                        select = position;
//
//                        if (0 == select) {
//                            tv_receipt_type.setText(getString(R.string.buildreceiptactivity_type_ordinary));
//                        } else if (1 == select) {
//                            tv_receipt_type.setText(getString(R.string.buildreceiptactivity_type_special));
//                        }
//                    }
//                }).setBackKey(true).show(findViewById(R.id.top__iv));
                break;

        }
    }

    /**
     * 选择发票类型
     */
    private void selectedRecciptType() {
        CustomPopuwindow popuWindow = new CustomPopuwindow();
        popuWindow.selectReceiptType(this, mainView, lists, new CustomPopuwindow.OnSelectReceiptTypeListener() {
            @Override
            public void selectedReceiptType(int index) {
                lists.get(select).setSelect(false);
                lists.get(index).setSelect(true);
                select = index;

                if (0 == select) {
                    tv_receipt_type.setText(getString(R.string.buildreceiptactivity_type_ordinary));
                    line.setVisibility(View.GONE);
                    receiptNoLayout.setVisibility(View.GONE);
                } else if (1 == select) {
                    tv_receipt_type.setText(getString(R.string.buildreceiptactivity_type_special));
                    line.setVisibility(View.VISIBLE);
                    receiptNoLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.createReceipt.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.createReceipt:
                    mIntent.setClass(mContext,ReceiptSuccessActivity.class);
                    startActivity(mIntent);
                    finish();
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.createReceipt.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
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
