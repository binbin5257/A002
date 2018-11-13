package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.widget.ListView.PullToRefreshLayout;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.MarkingListModel;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.view.adapter.MarkingListAdapter;
import cn.lds.im.view.widget.PaginationListView;
import de.greenrobot.event.EventBus;

/**
 * 用车审批页面
 * Created by E0608 on 2017/12/22.
 */

public class MarkingActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;


    @ViewInject(R.id.pull_to_refresh_view)
    protected PullToRefreshLayout ptr;

    @ViewInject(R.id.apply_lv)
    private PaginationListView markingLv;
    @ViewInject(R.id.no_data)
    private LinearLayout noData;

 @ViewInject(R.id.tv_no_data)
    private TextView noDataTv;


    private int REFRUSH = 1;
    private int LOAD_MORE = 2;
    private int GET_DATA_TYPE = REFRUSH;
    private int page = 0;
    private List<MarkingListModel.DataBean> mMarkingList = new ArrayList<>();
    private MarkingListAdapter markingListAdapter;
    private boolean last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking_apply);
        ViewUtils.inject(MarkingActivity.class, this);
        init();
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        ptr.autoRefresh();
        LoadingDialog.showDialog(this,"请稍后...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.top_back_btn//返回
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
        }

    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("用车审批");
        btnBack.setVisibility(View.VISIBLE);
        ptr.setOnRefreshListener(this);
        markingLv.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 0;
        GET_DATA_TYPE = REFRUSH;
        requestMarkingListData(page);
    }


    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        GET_DATA_TYPE = LOAD_MORE;
        if(last){
            ptr.loadmoreFinish(PullToRefreshLayout.FAIL_NO_DATA);
        }else{
            page++;
            requestMarkingListData(page);
        }
    }

    /**
     * 请求申请也表数据
     */
    private void requestMarkingListData(int page) {
        ModuleHttpApi.getMarkingList(page);
    }


    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!ModuleHttpApiKey.getMarkingList.equals(apiNo))
                return;
            MarkingListModel markingListModel = GsonImplHelp.get().toObject(httpResult.getResult(), MarkingListModel.class);
            if(markingListModel != null){
                last = markingListModel.getPageable().isLast();
                List<MarkingListModel.DataBean> dataBeen = markingListModel.getData();
                if(dataBeen != null && dataBeen.size() > 0){
                    if(GET_DATA_TYPE == REFRUSH){
                        mMarkingList.clear();
                        ptr.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else{
                        ptr.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    mMarkingList.addAll(dataBeen);
                    refrushList();
                    noData.setVisibility(View.GONE);
                    ptr.setVisibility(View.VISIBLE);
                }else{
                    noData.setVisibility(View.VISIBLE);
                    noDataTv.setText("还没有用车审批记录哦~");
                    ptr.setVisibility(View.GONE);
                }
            }




        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    /**
     * 刷新列表
     */
    private void refrushList() {
        if(markingListAdapter == null){
            markingListAdapter = new MarkingListAdapter(this,mMarkingList);
            markingLv.setAdapter(markingListAdapter);
        }else{
            markingListAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.getMarkingList.equals(apiNo)))
                return;
            FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(), FaIlureModel.class);
            ToolsHelper.showStatus(this,false,model.getErrors().get(0).getErrmsg());


        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,MarkingDetaiAcitivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ID",mMarkingList.get(position).getId()+"");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
