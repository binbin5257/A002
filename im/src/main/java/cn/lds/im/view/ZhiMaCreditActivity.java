package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnLongClick;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppHelper;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.ZMXYScore;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import de.greenrobot.event.EventBus;

/**
 * 芝麻信用
 */
public class ZhiMaCreditActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 绑定按钮
    @ViewInject(R.id.btn_bind)
    protected Button bindBtn;
    protected ZhiMaCreditActivity activity;
    protected int layoutID = R.layout.activity_zhima;

    public void setActivity(ZhiMaCreditActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        EventBus.getDefault().register(this);
        ViewUtils.inject(ZhiMaCreditActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void initConfig() {
        init();
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("芝麻信用");
        btnBack.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.top_back_btn,R.id.btn_bind, R.id.top_menu_btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.btn_bind:
                // TODO 阿里云服务器上传该功能后打开
                LoadingDialog.showDialog(this,"请稍后...");
                ModuleHttpApi.zhiMaCredit();
//                Toast.makeText(this,"该功能正在努力开发中...",Toast.LENGTH_SHORT).show();


            break;
        }
    }
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!(ModuleHttpApiKey.zhiMaCredit.equals(apiNo)

            )) {
                return;
            }
            switch (apiNo) {
                case ModuleHttpApiKey.zhiMaCredit:
                    JSONObject json = new JSONObject(httpResult.getResult());
                    String url = json.getString("data");
                    //进入芝麻信用授权页面
                    Intent intent = new Intent(this,ZhiMaAuthorizationActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title","芝麻信用授权");
                    startActivity(intent);
                    finish();
                    break;
            }
        }catch (Exception e){

        }
    }


}
