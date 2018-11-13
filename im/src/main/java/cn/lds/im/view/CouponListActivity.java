package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.SortList;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.CouponListModel;
import cn.lds.im.data.IllegalListModel;
import cn.lds.im.data.ReceiptListModel;
import cn.lds.im.enums.CouponStatus;
import cn.lds.im.view.adapter.CanUseCouponListAdapter;
import cn.lds.im.view.adapter.CouponListAdapter;
import cn.lds.im.view.adapter.ReceiptListAdapter;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshListView;
import de.greenrobot.event.EventBus;

/**
 * 优惠劵
 */

public class CouponListActivity extends BaseActivity {

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
    //没有优惠劵
    @ViewInject(R.id.no_data)
    protected LinearLayout mNoData;
    //没有优惠劵
    @ViewInject(R.id.top_menu_btn)
    protected TextView menuRight;

    protected ListView mListView;
    protected CanUseCouponListAdapter mAdapter;
    protected ArrayList<CouponListModel.DataBean> mListDataBean;

    //记录查到第几页，从第0页开始
    private int page = 0;
    private String from;
    private String orderId;


    protected CouponListActivity activity;
    protected int layoutID = R.layout.activity_coupon_list;
    private double orderPrice;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(CouponListActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(CouponListActivity.class, this);
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
        topTitle.setText(getString(R.string.coupon_title));
        btnBack.setVisibility(View.VISIBLE);
        menuRight.setText("不使用优惠券");
        Intent intent = getIntent();
        if(null != intent){
            from = intent.getStringExtra("fromWallet");
            orderId = intent.getStringExtra("orderId");
        }
        //下拉刷新，上拉加载更多
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(onRefreshListener);
        mListView = pullToRefreshListView.getRefreshableView();

        mListDataBean = new ArrayList<>();
        mAdapter = new CanUseCouponListAdapter(mContext, mListDataBean);
        mAdapter.setOnUseConponListener(new CanUseCouponListAdapter.OnUseConponListener() {
            @Override
            public void immediatelyUseConponEvent(CouponListModel.DataBean dataBean) {
                if ("fromWallet".equals(from)) {
                    switch (dataBean.getStatus()) {
                        case CouponStatus.RELEASE:
                            mIntent.setClass(mContext, MainActivity.class);
                            startActivity(mIntent);
                            finish();
                            break;
                        default:
                            break;
                    }

                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATABEAN", dataBean);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        mListView.setAdapter(mAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            orderPrice = bundle.getDouble("ORDER_PRICE");
        }
    }

    public void initData() {
        if ("fromWallet".equals(from)) {
            LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
            ModuleHttpApi.getCouponList(page,"");
        }else {
            if(!ToolsHelper.isNull(orderId)){
                LoadingDialog.showDialog(mContext, getString(R.string.search_footer_zhengaijiazai));
                ModuleHttpApi.getCouponListRelease(orderId);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.top_menu_btn
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATABEAN", null);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
        }
    }

    protected void F001(String result) {
        CouponListModel mModel = GsonImplHelp.get().toObject(result, CouponListModel.class);
        List<CouponListModel.DataBean> datas = mModel.getData();
        //对集合按照price属性降序排序
        if (null == mModel)
            return;
        if (0 == page) {
            if (mListDataBean != null) {
                mListDataBean.clear();
            }
            if (mModel.getData() == null || mModel.getData().size() == 0) {
                mNoData.setVisibility(View.VISIBLE);
                pullToRefreshListView.setVisibility(View.GONE);
            }else {
                mNoData.setVisibility(View.GONE);
                pullToRefreshListView.setVisibility(View.VISIBLE);
            }
        } else {
            mNoData.setVisibility(View.GONE);
            pullToRefreshListView.setVisibility(View.VISIBLE);
            if (null == mModel.getData() || 0 == mModel.getData().size()) {
                ToolsHelper.showInfo(mContext, getString(R.string.has_no_more_data));
            }
        }
        mListDataBean.addAll(datas);
        SortList<CouponListModel.DataBean> sortList = new SortList<CouponListModel.DataBean>();
        sortList.sortByMethod(mListDataBean,"getPrice",true);
        if (mListView.getAdapter() == null) {
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
        }
    }

    private PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase
            .OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
            //下拉刷新
            page = 0;
            initData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            //上拉加载更多
            page++;
            initData();
        }
    };

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getCouponList.equals(apiNo)
                || ModuleHttpApiKey.getCouponListRelease.equals(apiNo)
        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getCouponList:
                    F001(httpResult.getResult());
                    break;
                case ModuleHttpApiKey.getCouponListRelease:
                    F001(httpResult.getResult());
                    break;
            }
        }

    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getCouponList.equals(apiNo)
                || ModuleHttpApiKey.getCouponListRelease.equals(apiNo)
        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, "获取优惠劵列表失败");
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
