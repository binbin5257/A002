package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnLongClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppHelper;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {

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

    protected AboutActivity activity;
    protected int layoutID = R.layout.activity_about;

    public void setActivity(AboutActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(AboutActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
    }

    protected void init() {
        lists.add(new PopData(cn.lds.chatcore.R.drawable.ic_launcher, "联通服务:" + AppIndependentConfigure.SERVER_HOST_CUSC_SHARE, false));
        lists.add(new PopData(cn.lds.chatcore.R.drawable.ic_launcher, "阿里云:" + AppIndependentConfigure.SERVER_HOST_CLOUD_ALI_SHARE_WU, false));
        lists.add(new PopData(cn.lds.chatcore.R.drawable.ic_launcher, "内网:" + AppIndependentConfigure.SERVER_HOST_CLOUD_DLNW_SHARE_WU, false));

        if (AppIndependentConfigure.SERVER_HOST_CUSC_SHARE.equals(MyApplication.getInstance().getSERVER_HOST())) {
            lists.get(0).setSelect(true);
        }else if (AppIndependentConfigure.SERVER_HOST_CLOUD_ALI_SHARE_WU.equals(MyApplication.getInstance().getSERVER_HOST())){
            lists.get(1).setSelect(true);
        }else if (AppIndependentConfigure.SERVER_HOST_CLOUD_DLNW_SHARE_WU.equals(MyApplication.getInstance().getSERVER_HOST())){
            lists.get(2).setSelect(true);
        }

        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.aboutactivity_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);


        about_tv_this.setText(getString(R.string.about_version, AppHelper.getVersionName()));


    }

    @OnClick({R.id.top_back_btn, R.id.top_menu_btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.top_back_btn:
                finish();
                break;
        }
    }

    @OnLongClick({R.id.tv_icon})
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.tv_icon:
                PopWindowHelper.getInstance().menu(mContext, lists, true, new PopWindowListener() {
                    @Override
                    public void confirm() {
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {
                        lists.get(select).setSelect(false);
                        lists.get(position).setSelect(true);
                        select = position;

                        if (AccountManager.getInstance().isLogin())
                            MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getLogoutFilter(), "切换环境");

                        if (0 == select) {
                            MyApplication.getInstance().setSERVER_HOST(AppIndependentConfigure.SERVER_HOST_CUSC_SHARE);
                        } else if (1 == select) {
                            MyApplication.getInstance().setSERVER_HOST(AppIndependentConfigure.SERVER_HOST_CLOUD_ALI_SHARE_WU);
                        }else if (2 == select) {
                            MyApplication.getInstance().setSERVER_HOST(AppIndependentConfigure.SERVER_HOST_CLOUD_DLNW_SHARE_WU);
                        }
                        AboutActivity.this.finish();
                    }
                }).setBackKey(true).show(findViewById(R.id.top__iv));
                break;
        }
        return false;
    }

}
