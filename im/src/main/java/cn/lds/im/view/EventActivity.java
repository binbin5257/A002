package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Date;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.ModuleUrls;
import cn.lds.im.data.EventModel;
import cn.lds.im.view.adapter.EventAdapter;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshListView;
import de.greenrobot.event.EventBus;


/**
 * 活动页面
 */

public class EventActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;

    @ViewInject(R.id.eventactivity_pulllistview)
    protected PullToRefreshListView pullToRefreshListView;
    protected ListView mListView;
    protected EventAdapter mAdapter;
    protected ArrayList<EventModel.DataBean> mList;
    @ViewInject(R.id.eventactivity_nullview)
    protected LinearLayout eventactivity_nullview;

    protected EventActivity activity;
    protected int layoutID = R.layout.activity_event;



    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(EventActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(EventActivity.class, this);
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
        topTitle.setText(getString(R.string.btn_submenu_event));
        btnBack.setVisibility(View.VISIBLE);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        pullToRefreshListView.setOnRefreshListener(this);
        mListView = pullToRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(onItemClickListener);

        mList = new ArrayList<>();
        mAdapter = new EventAdapter(mContext, mList);
        mListView.setAdapter(mAdapter);

        LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
        ModuleHttpApi.getActivities();
    }

    @OnClick({R.id.top_back_btn
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
        }
    }

    /*
    * 列表点击事件
    * */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EventModel.DataBean dataBean = mList.get(position - 1);
            String dataBeanId = dataBean.getId() + "";
            String isOpen = MyApplication.getInstance().getCache().getAsString(dataBeanId);
            if (null == isOpen || !isOpen.equals(dataBeanId)) {
                long nowTime = new Date().getTime();
                long saveTime = ToolsHelper.stringTOlong(dataBean.getExecutionEndTime()) - ToolsHelper.stringTOlong(dataBean.getExecutionStartTime());
//                MyApplication.getInstance().getCache().put(dataBeanId, dataBeanId, (int) saveTime);
                MyApplication.getInstance().getCache().put(dataBeanId, dataBeanId);
            }
            String url = dataBean.getUrl();
            WebViewActivityHelper.startWebViewActivity(mContext, url, getString(R.string.eventactivity_detail));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mAdapter)//更新适配器
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        pullToRefreshListView.onRefreshComplete();
    }

    protected void F001(String result) {
        EventModel eventModel = GsonImplHelp.get().toObject(result, EventModel.class);
        if (null == eventModel)
            return;
        if (!eventModel.getData().isEmpty()) {
            eventactivity_nullview.setVisibility(View.GONE);
            pullToRefreshListView.setVisibility(View.VISIBLE);
            mList.clear();
            mList.addAll(eventModel.getData());
        } else {
            eventactivity_nullview.setVisibility(View.VISIBLE);
            pullToRefreshListView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getActivities.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getActivities:
                    F001(httpResult.getResult());
                    break;
            }
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getActivities.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取行程详情失败");
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
