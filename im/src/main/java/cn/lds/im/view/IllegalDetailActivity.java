package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.ImageLoaderHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.IllegalDetailModel;
import de.greenrobot.event.EventBus;

/**
 * 违章详情
 */

public class IllegalDetailActivity extends BaseActivity {

    //标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    //返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;

    @ViewInject(R.id.tv_car_model)
    protected TextView tv_car_model;
    @ViewInject(R.id.tv_car_no)
    protected TextView tv_car_no;
    @ViewInject(R.id.img_car_photo)
    protected ImageView img_car_photo;
    @ViewInject(R.id.tv_position)
    protected TextView tv_position;
    @ViewInject(R.id.tv_type)
    protected TextView tv_type;
    @ViewInject(R.id.tv_time)
    protected TextView tv_time;
    @ViewInject(R.id.tv_money)
    protected TextView tv_money;
    @ViewInject(R.id.tv_score)
    protected TextView tv_score;

    private String id;

    protected int layoutID = R.layout.activity_illegal_detail;
    protected RouteDetailActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(IllegalDetailActivity.class, this);
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
        topTitle.setText(getString(R.string.btn_submenu_illegal));
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
        LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
        ModuleHttpApi.getIllegalDetail(id);
    }


    @OnClick({R.id.top_back_btn,
            R.id.illegal_service
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.illegal_service:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
        }
    }

    protected void refreshUI(String result) {
        IllegalDetailModel mDetailModel = GsonImplHelp.get().toObject(result, IllegalDetailModel.class);
        IllegalDetailModel.DataBean dataBean = mDetailModel.getData();
        if (null != dataBean) {
            tv_car_model.setText(ToolsHelper.isNullString(dataBean.getBrandName())
                    + ToolsHelper.isNullString(dataBean.getSeriesName()));
            tv_car_no.setText(ToolsHelper.isNullString(dataBean.getPlateLicenseNo()));
            if(!ToolsHelper.isNull(dataBean.getPicId())){
                ImageLoaderManager.getInstance().displayImageForCar(this,dataBean.getPicId(),img_car_photo);
            }
            tv_position.setText(ToolsHelper.isNullString(dataBean.getIllegalLocation()));
            tv_type.setText(ToolsHelper.isNullString(dataBean.getIllegalDescription()));
            if (ToolsHelper.isNull(dataBean.getIllegalTime())) {
                tv_time.setText("");
            } else {
                tv_time.setText(TimeHelper.getDateStringString(ToolsHelper.stringTOlong(dataBean.getIllegalTime()), TimeHelper.FORMAT6));
            }
            tv_money.setText(ToolsHelper.stringTOint(dataBean.getPrice())/100 + "元");
            if(ToolsHelper.isNull(dataBean.getMark())){
                tv_score.setText("");
            }else {
                tv_score.setText(ToolsHelper.isNullString(dataBean.getMark()) + "分");
            }
        }
    }


    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getIllegalDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getIllegalDetail:
                    refreshUI(httpResult.getResult());
                    break;
            }
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getIllegalDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取违章详情失败");
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_register), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }
}
