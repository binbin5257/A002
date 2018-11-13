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
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 退款成功
 */
public class RebackSuccessActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;

    @ViewInject(R.id.top_menu_btn)
    protected Button mDirections;
    @ViewInject(R.id.reback_amount)
    protected TextView reback_amount;

    protected String foregiftAmount;//退款金额

    protected RebackSuccessActivity activity;
    protected int layoutID = R.layout.activity_reback_success;

    public void setActivity(RebackSuccessActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(RebackSuccessActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.cash_pledge_reback_result));
        btnBack.setVisibility(View.VISIBLE);
        mDirections.setVisibility(View.GONE);
        mDirections.setText("退款说明");
        Intent intent = getIntent();
        if (intent != null) {
            foregiftAmount = intent.getStringExtra("foregiftAmount");
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        if(ToolsHelper.isNull(foregiftAmount)){
            reback_amount.setText("0.00" + "元");
        } else {
            reback_amount.setText(df.format(ToolsHelper.stringTOdouble(foregiftAmount)/100)+ "元");
        }

    }

    @OnClick({R.id.top_back_btn,
            R.id.receipt_confirm,
            R.id.top_menu_btn
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.receipt_confirm:
                finish();
                break;
            case R.id.top_menu_btn:
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/balanceRefundNote.html", getString(R.string.cash_pledge_reback_directions));
                break;
        }
    }

}
