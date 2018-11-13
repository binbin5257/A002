package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.ViolationDealtWithEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.IllegalListModel;
import cn.lds.im.view.adapter.IllegalListAdapter;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshListView;
import de.greenrobot.event.EventBus;

/**
 * 违章页面
 */

public class IllegalListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {

    //标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    //返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;

    @ViewInject(R.id.peccancyactivity_pulllistview)
    protected PullToRefreshListView pullToRefreshListView;
    protected ListView mListView;
    protected IllegalListAdapter mAdapter;
    protected ArrayList<IllegalListModel.DataBean> mList;
    @ViewInject(R.id.no_data)
    protected LinearLayout mNullview;

    protected IllegalListActivity activity;
    protected int layoutID = R.layout.activity_illegal_list;

    public void setActivity(IllegalListActivity activity) {
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(IllegalListActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        init();
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.btn_submenu_illegal));
        btnBack.setVisibility(View.VISIBLE);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);
        mListView = pullToRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(onItemClickListener);

        mList = new ArrayList<>();
        mAdapter = new IllegalListAdapter(mContext, mList);
        mListView.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
        ModuleHttpApi.getIllegalList();
    }

    @OnClick({R.id.top_back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
        }
    }

   /*
   列表点击事件
    */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            IllegalListModel.DataBean dataBean = mList.get(position - 1);
            if(null != dataBean){
                mIntent.setClass(mContext,IllegalDetailActivity.class);
                mIntent.putExtra("id",dataBean.getId());
                startActivity(mIntent);
            }
        }
    };

    protected void G001(String result) {
        IllegalListModel mIllegal = GsonImplHelp.get().toObject(result, IllegalListModel.class);
        if (null == mIllegal)
            return;
        if (!mIllegal.getData().isEmpty()) {
            mNullview.setVisibility(View.GONE);
            pullToRefreshListView.setVisibility(View.VISIBLE);
            mList.clear();
            mList.addAll(mIllegal.getData());
        } else {
            mNullview.setVisibility(View.VISIBLE);
            pullToRefreshListView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        pullToRefreshListView.onRefreshComplete();
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getIllegalList.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.getIllegalList:
                    G001(httpResult.getResult());
                    break;
            }
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getIllegalList.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取违章信息失败");
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
    public void onEventMainThread(ViolationDealtWithEvent event) {
        LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
        ModuleHttpApi.getIllegalList();
    }

}
