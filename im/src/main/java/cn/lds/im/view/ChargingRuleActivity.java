package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.FaIlureModel;
import de.greenrobot.event.EventBus;

/**
 * 计费规则页面
 * Created by E0608 on 2017/8/18.
 */

public class ChargingRuleActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回按钮
     */
    private Button backBtn;
    /**
     * 页面标题
     */
    private TextView titleTv;
    private int carId;
    private String orderTime;
    private WebView content;
    private LinearLayout noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_rule);
        EventBus.getDefault().register(this);
        initView();
        getIntentExtra();
        getChargingRuleData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    /**
     * 请求服务器获取计费规则数据
     */
    private void getChargingRuleData() {
        ModuleHttpApi.getRuleDetail(String.valueOf(carId),orderTime);
    }

    /**
     * 获取intent数据
     */
    private void getIntentExtra() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            carId = bundle.getInt("id");  //车辆id
            orderTime = bundle.getString("orderTime"); //下单时间 没有下单不传
        }
    }

    /**
     * 初始化界面ui
     */
    private void initView() {
        backBtn = (Button) findViewById(R.id.top_back_btn);
        titleTv = (TextView) findViewById(R.id.top_title_tv);
        content = (WebView) findViewById(R.id.content);
        noData = (LinearLayout) findViewById(R.id.no_data);
        WebSettings wSet = content.getSettings();
        wSet.setJavaScriptEnabled(true);

        titleTv.setText("计费说明");
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.top_back_btn:
                finish();
                break;

        }
    }
    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        LoadingDialog.dismissDialog();
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getRuleDetail.equals(apiNo))) {
            return;
        }

        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getRuleDetail:
                    content.loadDataWithBaseURL(null, httpResult.getResult().toString(), "text/html" , "utf-8", null);
                    break;

            }
        }else{
            content.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);

        }
    }
    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String result = httpResult.getResult();
        FaIlureModel model = GsonImplHelp.get().toObject(result, FaIlureModel.class);
        if (model != null
                && model.getStatus().equals("failure")
                && model.getErrors() != null
                && model.getErrors().size() > 0) {
            ToolsHelper.showStatus(mContext, false, model.getErrors().get(0).getErrmsg());
        }
        LoadingDialog.dismissDialog();
        String apiNo = httpResult.getApiNo();
        switch (apiNo) {
            case ModuleHttpApiKey.getRuleDetail:
                break;
        }
    }



}
