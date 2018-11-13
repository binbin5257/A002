package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.widget.ListView.PullToRefreshLayout;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.TripListModel;
import cn.lds.im.enums.TripStatus;
import cn.lds.im.view.adapter.TripListAdapter;
import cn.lds.im.view.widget.ChargingListView;
import cn.lds.im.view.widget.PaginationListView;
import de.greenrobot.event.EventBus;

/**
 * 行程列表页面
 * Created by sibinbin on 2017/7/19.
 */

public class OtherTripListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, PullToRefreshLayout.OnRefreshListener {

    protected PullToRefreshLayout ptr;
    /**
     * 行程列表View
     */
    private PaginationListView tripLv;
    /**
     * 行程信息的集合
     */
    private List<TripListModel.DataBean> mList = new ArrayList<>();
    /**
     * 返回按钮
     */
    private Button backBtn;
    /**
     * 页面标题
     */
    private TextView titleTv;
    private TripListAdapter mAdapter;
    //无数据
    private LinearLayout mNoData;

    // list view 滚动到最后一条的id
    private int lasetItemId = 0;

    int size = 10;

    int page = 0;

    boolean last = false;


    protected OtherTripListActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_trip_list);

        initView();
    }

    /**
     * 初始化界面UI
     */
    private void initView() {

        backBtn = (Button) findViewById(R.id.top_back_btn);
        titleTv = (TextView) findViewById(R.id.top_title_tv);
        tripLv = (PaginationListView) findViewById(R.id.list_trip);
        mNoData = (LinearLayout)findViewById(R.id.no_data);
        ptr = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_view);
        ptr.setOnRefreshListener(this);
        titleTv.setText("部门他人行程");
        backBtn.setVisibility(View.VISIBLE);

        backBtn.setOnClickListener(this);
        tripLv.setOnItemClickListener(this);
        tripLv.setOnScrollListener(this);
        mAdapter = new TripListAdapter(this, mList,"OTHER");
        tripLv.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn: //返回上一页
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TripListModel.DataBean dataBean = mList.get(position);
        if (null != dataBean && TripStatus.PAID.equals(dataBean.getStatus())) {
            IntentHelper.intentTripList(mContext, mIntent, dataBean.getId(), dataBean.getStatus(), dataBean.getType(), dataBean.isDelivered(), dataBean.isScheduledTimeUp());

        }
    }

    protected void F001(String result) {
        TripListModel mTripModel = GsonImplHelp.get().toObject(result, TripListModel.class);
        last = mTripModel.getPageable().isLast();
        if (null == mTripModel)
            return;
        if(page == 0){
            ptr.refreshFinish(PullToRefreshLayout.SUCCEED);
            if (mTripModel.getData() == null || mTripModel.getData().size() == 0) {
                mNoData.setVisibility(View.VISIBLE);
                ptr.setVisibility(View.GONE);
            } else {
                mNoData.setVisibility(View.GONE);
                ptr.setVisibility(View.VISIBLE);
                mList.clear();

            }

        }else{
            ptr.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
        mList.addAll(mTripModel.getData());
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getOtherTripList.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getOtherTripList:
                    F001(httpResult.getResult());
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getTripList.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取行程失败");
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
            ptr.autoRefresh();
        } catch (Exception e) {
            LogHelper.e(ReceiptActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(ReceiptActivity.class.getName(), e);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        LoadingDialog.showDialog(mContext, "请稍后...");
        page = 0;
        ModuleHttpApi.getOtherTripList(size, page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if(last){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL_NO_DATA);
        }else{
            ModuleHttpApi.getOtherTripList(size,page);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 记录滚动到最后一条的数据id 作为下次请求更多的开始参数 beginPos
        if(!last){
            lasetItemId = tripLv.getLastVisiblePosition() + 1;
            page = lasetItemId / size;
        }

    }
}
