package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ReceiptInfoModel;
import cn.lds.im.data.ReceiptListModel;
import cn.lds.im.enums.ReceiptStatus;
import cn.lds.im.enums.ReceiptStatusEnum;
import de.greenrobot.event.EventBus;

/**
 * 发票详情
 */
public class ReceiptInfoActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.buildreceiptactivity_inputjine)
    protected TextView buildreceiptactivity_inputjine;
    @ViewInject(R.id.buildreceiptactivity_inputtaitou)
    protected TextView buildreceiptactivity_inputtaitou;
    @ViewInject(R.id.buildreceiptactivity_inputcontact)
    protected TextView buildreceiptactivity_inputcontact;
    @ViewInject(R.id.buildreceiptactivity_inputphone)
    protected TextView buildreceiptactivity_inputphone;
    @ViewInject(R.id.buildreceiptactivity_inputaddress)
    protected TextView buildreceiptactivity_inputaddress;
    @ViewInject(R.id.tv_receipt_type)
    protected TextView tv_receipt_type;
    @ViewInject(R.id.receipt_info_status)
    private TextView receipt_info_status;
    @ViewInject(R.id.receipt_no)
    protected LinearLayout receiptNoLayout;
    @ViewInject(R.id.tv_receipt_no)
    protected TextView receiptNoText;
    @ViewInject(R.id.line)
    protected ImageView line;

    protected String id;

    protected float amount;
    int DECIMAL_DIGITS = 2;

    protected ReceiptInfoActivity activity;
    protected int layoutID = R.layout.activity_receipt_info;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(ReceiptInfoActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ReceiptInfoActivity.class, this);
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
        LoadingDialog.showDialog(mContext, "");
        ModuleHttpApi.getReceiptInfo(id);
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
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
        }
    }

    protected void refreshUI(String result) {
        ReceiptInfoModel mModel = GsonImplHelp.get().toObject(result, ReceiptInfoModel.class);
        if (null == mModel)
            return;
        if (mModel.getData() == null) {
            ToolsHelper.showInfo(mContext, "未获取到发票明细");
        } else {
            ReceiptInfoModel.DataBean data = mModel.getData();
            DecimalFormat df = new DecimalFormat("#,##0.00");
            buildreceiptactivity_inputjine.setText(df.format(data.getAmount()/100) + "");
            buildreceiptactivity_inputtaitou.setText(ToolsHelper.isNullString(data.getTitle()));
            buildreceiptactivity_inputcontact.setText(ToolsHelper.isNullString(data.getRecipientName()));
            buildreceiptactivity_inputphone.setText(ToolsHelper.isNullString(data.getRecipientPhone()));
            buildreceiptactivity_inputaddress.setText(ToolsHelper.isNullString(data.getRecipientAddress()));
            String type = data.getInvoiceType();
            if(ToolsHelper.isNull(type)){
                tv_receipt_type.setText("");
            } else {
                switch (type){
                    case "INDIVIDUAL":
                        tv_receipt_type.setText("普通发票");
                        receiptNoLayout.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                        break;
                    case "CORPORATION":
                        tv_receipt_type.setText("增值税专用发票");
                        receiptNoLayout.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);
                        receiptNoText.setText(data.getTaxId());
                        break;
                }

            }
            String status = data.getStatus();
            if(ToolsHelper.isNull(status)){
                receipt_info_status.setText("");
            } else {
                switch (status){
                    case ReceiptStatus.REQUESTED:
                        receipt_info_status.setText(ReceiptStatusEnum.valueOf(status).value());
                        break;
                    case ReceiptStatus.ACCEPTED:
                        receipt_info_status.setText(ReceiptStatusEnum.valueOf(status).value());
                        break;
                    case ReceiptStatus.MAILED:
                        receipt_info_status.setText(ReceiptStatusEnum.valueOf(status).value());
                        break;
                    case ReceiptStatus.REJECTED:
                        receipt_info_status.setText(ReceiptStatusEnum.valueOf(status).value());
                        break;
                }
            }
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getReceiptInfo.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getReceiptInfo:
                    refreshUI(httpResult.getResult());
                    break;
            }
        }

    }


    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getReceiptInfo.equals(apiNo))) {
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
