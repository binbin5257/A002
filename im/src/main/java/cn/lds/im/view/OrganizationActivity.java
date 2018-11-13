package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.CommonAdapter;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.ViewHolder;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginResponseModel;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import de.greenrobot.event.EventBus;
import cn.lds.chatcore.data.LoginResponseModel.DataBean.OrganizationsBean;

public class OrganizationActivity extends BaseActivity {
    //topbar
    @ViewInject(R.id.top_back_btn)
    protected Button top_back_btn;
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView top_menu_btn_del;

    @ViewInject(R.id.organization_now_tv)
    protected TextView organization_now_tv;
    @ViewInject(R.id.organization_lv)
    protected ListView organization_lv;

    protected String organization_name;
    protected List<LoginResponseModel.DataBean.OrganizationsBean>  datas = new ArrayList<LoginResponseModel.DataBean.OrganizationsBean>();

    protected OrganizationActivity activity;
    protected int layoutID = R.layout.activity_organization;

    public void setActivity(OrganizationActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(OrganizationActivity.class, this);
        if(null != activity){
            ViewUtils.inject(activity);
        }

        initConfig();
    }

    protected void initConfig() {
        init();
        initData();
        refreshView();
    }

    protected void refreshView() {
    }

    protected void initData() {
        datas.add(new OrganizationsBean("", "社会化"));
        JSONArray jsonArray = CacheHelper.getOrganizationList();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject o = new JSONObject((String) jsonArray.get(i));
                datas.add(new OrganizationsBean(o.optString("id"), o.optString("name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        organization_lv.setAdapter(new CommonAdapter<OrganizationsBean>(mContext, datas, R.layout.item_organization) {
            @Override
            public void convert(ViewHolder helper, final LoginResponseModel.DataBean.OrganizationsBean item) {
                if (CacheHelper.getOrganizationId().equals(item.getId())) {
                    helper.setVisibility(R.id.item_organization_select_iv, true);
                    organization_now_tv.setText("当前选择 : " + item.getName());
                } else {
                    helper.setVisibility(R.id.item_organization_select_iv, false);
                }
                helper.setText(R.id.item_organization_name_tv, item.getName());
                helper.onClick(R.id.item_organization_ibm_ryt, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CacheHelper.setOrganizationId(item.getId());
                        finish();
                    }
                });
            }
        });
    }

    protected void init() {
        top_back_btn.setVisibility(View.VISIBLE);
        top_title_tv.setVisibility(View.VISIBLE);
        top_title_tv.setText("组织");
        top_menu_btn_del.setImageResource(R.drawable.topbar_menu_customerservice);
        top_menu_btn_del.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.top_back_btn, R.id.top_menu_btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
        }
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
            LogHelper.e(getString(R.string.default_bus_unregister), e);
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();
    }

}
