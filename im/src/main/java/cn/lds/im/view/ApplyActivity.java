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
import cn.lds.im.data.ApplyListModel;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.view.adapter.ApplyListAdapter;
import cn.lds.im.view.widget.PaginationListView;
import de.greenrobot.event.EventBus;

/**
 * 用车申请页面
 * Created by E0608 on 2017/12/22.
 */

public class ApplyActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 标题栏申请
    @ViewInject(R.id.top_right_tv)
    private TextView topMenuRight;

    @ViewInject(R.id.pull_to_refresh_view)
    protected PullToRefreshLayout ptr;

    @ViewInject(R.id.apply_lv)
    private PaginationListView applyLv;
    @ViewInject(R.id.no_data)
    private LinearLayout noData;


    private int REFRUSH = 1;
    private int LOAD_MORE = 2;
    private int GET_DATA_TYPE = REFRUSH;
    private int page = 0;
    private List<ApplyListModel.DataBean> mApplyList = new ArrayList<>();
    private ApplyListAdapter applyListAdapter;
    private boolean last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking_apply);
        ViewUtils.inject(ApplyActivity.class, this);
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

    @OnClick({R.id.top_back_btn,//返回
            R.id.top_right_tv
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case  R.id.top_right_tv: //进入申请详情页面
                enterApplyDetailActivity();
                break;
        }

    }

    /**
     * 进入申请详情页面
     */
    private void enterApplyDetailActivity() {
        Intent intent = new Intent(this,ApplyDetailActivity.class);
        startActivity(intent);

    }
    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("用车申请");
        btnBack.setVisibility(View.VISIBLE);
        topMenuRight.setVisibility(View.VISIBLE);
        topMenuRight.setText("申请");
        ptr.setOnRefreshListener(this);
        applyLv.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 0;
        GET_DATA_TYPE = REFRUSH;
        requestApplyListData(page);
    }


    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        GET_DATA_TYPE = LOAD_MORE;
        if(last){
            ptr.loadmoreFinish(PullToRefreshLayout.FAIL_NO_DATA);
        }else{
            page++;
            requestApplyListData(page);
        }
    }

    /**
     * 请求申请也表数据
     */
    private void requestApplyListData(int page) {
        ModuleHttpApi.getApplyList(page);
    }


    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!ModuleHttpApiKey.getApplyList.equals(apiNo))
                return;
            ApplyListModel applyListModel = GsonImplHelp.get().toObject(httpResult.getResult(), ApplyListModel.class);
            if(applyListModel != null){
                last = applyListModel.getPageable().isLast();
                List<ApplyListModel.DataBean> dataBeen = applyListModel.getData();
                if(dataBeen != null && dataBeen.size() > 0){
                    if(GET_DATA_TYPE == REFRUSH){
                        mApplyList.clear();
                        ptr.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else{
                        ptr.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    mApplyList.addAll(dataBeen);
                    refrushList();
                    noData.setVisibility(View.GONE);
                    ptr.setVisibility(View.VISIBLE);
                }else{
                    noData.setVisibility(View.VISIBLE);
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
        if(applyListAdapter == null){
            applyListAdapter = new ApplyListAdapter(this,mApplyList);
            applyLv.setAdapter(applyListAdapter);
        }else{
            applyListAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.getApplyList.equals(apiNo)))
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
        bundle.putString("ID",mApplyList.get(position).getId()+"");
        bundle.putString("FLAG","APPLY");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
