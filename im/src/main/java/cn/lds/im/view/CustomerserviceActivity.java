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
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.view.BaseActivity;

/*
* 客服页面
* **/
public class CustomerserviceActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    protected CustomerserviceActivity activity;
    protected int layoutID = R.layout.activity_customerservice;

    public void setTopTitle(TextView topTitle) {
        this.topTitle = topTitle;
    }

    public void setActivity(CustomerserviceActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(CustomerserviceActivity.class,this);
        if (null != activity){
            ViewUtils.inject(activity);
        }

        initConfig();
    }

    protected void initConfig() {
        init();
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.btn_submenu_customerservice));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.ic_phone);
    }

    @OnClick({R.id.top_back_btn, R.id.top_menu_btn_del, R.id.custommerservice_takephone1, R.id.custommerservice_takephone2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.custommerservice_takephone1:
                PopWindowHelper.getInstance().openCustomerConsult(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.custommerservice_takephone2:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;

        }
    }
}
