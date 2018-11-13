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
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApiKey;
import de.greenrobot.event.EventBus;

public class SettingActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    //右上角退出登录按钮
    @ViewInject(R.id.top_menu_btn_del)
    private ImageView mExit;

    protected SettingActivity activity;
    protected int layoutID = R.layout.activity_setting;
    public void setActivity(SettingActivity activity) {
        this.activity = activity;
    }
    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(SettingActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }
    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(SettingActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(SettingActivity.class.getName(), e);
        }
    }
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!ModuleHttpApiKey.logout.equals(apiNo))
                return;
            MyApplication.getInstance().sendLogoutBroadcast(Constants.getCoreUrls().getLogoutFilter(), "点击注销按钮");
            finish();






        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }


    protected void initConfig() {
        init();
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.btn_submenu_setting));
        btnBack.setVisibility(View.VISIBLE);
        if (AccountManager.getInstance().isLogin()) {
//            mExit.setText(getString(R.string.setting_exit));
            mExit.setImageResource(R.drawable.bg_exit);
            mExit.setVisibility(View.VISIBLE);
        } else {
            mExit.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.top_back_btn//返回
            , R.id.top_menu_btn_del//退出登录
            , R.id.rlyt_setting_feedback//意见反馈
            , R.id.rlyt_setting_usualproblem//常见问题
            , R.id.rlyt_setting_rules//用户条款
            , R.id.rlyt_setting_about//关于
           })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case R.id.top_menu_btn_del://退出登录
                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        LoadingDialog.showDialog(mContext,"请稍后...");
                        CoreHttpApi.logout();

                    }

                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx("确定要注销？").show(findViewById(R.id.top__iv));
                break;
            case R.id.rlyt_setting_feedback:
                mIntent.setClass(mContext,FeedbackActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rlyt_setting_usualproblem:
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/faq.html", getString(R.string.settingactivity_useualproblem));
                break;
            case R.id.rlyt_setting_rules:
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/carRentalClause.html", getString(R.string.settingactivity_customerrule));
                break;
            case R.id.rlyt_setting_about:
                mIntent.setClass(mContext, AboutActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
