package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 发票申请成功
 */
public class ReceiptSuccessActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    @ViewInject(R.id.tv_icon)
    protected ImageView tv_icon;

    @ViewInject(R.id.about_tv_this)
    protected TextView about_tv_this;

    protected List<PopData> lists = new ArrayList<>();
    protected int select = 0;//当前支付方式

    protected ReceiptSuccessActivity activity;
    protected int layoutID = R.layout.activity_receipt_success;

    public void setActivity(ReceiptSuccessActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ReceiptSuccessActivity.class, this);
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
        topTitle.setText(getString(R.string.receipt_success));
        btnBack.setVisibility(View.VISIBLE);
        about_tv_this.setText(getString(R.string.receipt_success_remind));


    }

    @OnClick({R.id.top_back_btn,
            R.id.receipt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.receipt_confirm:
                finish();
                break;
        }
    }

}
