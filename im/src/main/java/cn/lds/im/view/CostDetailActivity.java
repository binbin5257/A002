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
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.CostDetailModel;
import cn.lds.im.data.EventModel;
import cn.lds.im.view.adapter.CostDetailAdapter;
import cn.lds.im.view.adapter.EventAdapter;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshListView;
import de.greenrobot.event.EventBus;

/**
 * 费用明细
 */

public class CostDetailActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    //客服
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    //上拉加载和下拉刷新的listView
    @ViewInject(R.id.eventactivity_pulllistview)
    protected PullToRefreshListView pullToRefreshListView;
    //无数据
    @ViewInject(R.id.no_data)
    private LinearLayout mNoData;

    protected ListView mListView;
    protected CostDetailAdapter mAdapter;
    protected ArrayList<CostDetailModel.DataBean> mListDataBean;

    //记录查到第几页，从第0页开始
    private int page = 0;


    protected CostDetailActivity activity;
    protected int layoutID = R.layout.activity_cost_detail;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(CostDetailActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(CostDetailActivity.class, this);
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
        topTitle.setText(getString(R.string.wallet_account_details));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.ic_phone);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(onRefreshListener);
        mListView = pullToRefreshListView.getRefreshableView();

        mListDataBean = new ArrayList<>();
        mAdapter = new CostDetailAdapter(mContext, mListDataBean);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
        ModuleHttpApi.getCostDetail(page);
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del
    })
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

    private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase
            .OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
            //下拉刷新
            page = 0;
            ModuleHttpApi.getCostDetail(page);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            //上拉加载更多
            page++;
            ModuleHttpApi.getCostDetail(page);
        }
    };


    protected void F001(String result) {
        CostDetailModel mCostModel = GsonImplHelp.get().toObject(result, CostDetailModel.class);
        if (null == mCostModel)
            return;
        if (0 == page) {
            if (mListDataBean != null) {
                mListDataBean.clear();
            }
            if (mCostModel.getData() == null || mCostModel.getData().size() == 0) {
                mNoData.setVisibility(View.VISIBLE);
                pullToRefreshListView.setVisibility(View.GONE);
            }else {
                mNoData.setVisibility(View.GONE);
                pullToRefreshListView.setVisibility(View.VISIBLE);
            }
        } else {
            mNoData.setVisibility(View.GONE);
            pullToRefreshListView.setVisibility(View.VISIBLE);
            if (null == mCostModel.getData() || 0 == mCostModel.getData().size()) {
                ToolsHelper.showInfo(mContext, getString(R.string.has_no_more_data));
            }
        }
        mListDataBean.addAll(mCostModel.getData());
        if (mListView.getAdapter() == null) {
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getCostDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getCostDetail:
                    F001(httpResult.getResult());
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getCostDetail.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取费用详情失败");
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
    public void onEventMainThread(AccountAvaliableEvent event) {
        //下拉刷新
        page = 0;
        ModuleHttpApi.getCostDetail(page);
    }


}
