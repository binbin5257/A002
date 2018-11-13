package cn.lds.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.CouponListModel;
import cn.lds.im.view.MainActivity;
import cn.lds.im.view.adapter.CouponListAdapter;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshBase;
import cn.lds.im.view.widget.pullToRefreshView.PullToRefreshListView;
import de.greenrobot.event.EventBus;

/**
 * 已使用优惠券片段
 * Created by E0608 on 2017/9/4.
 */

public class UsedCouponFragment extends Fragment {

    //记录查到第几页，从第0页开始
    private int page = 0;

    List<CouponListModel.DataBean> mListDataBean = new ArrayList<>();
    private ListView mListView;
    private CouponListAdapter mAdapter;
    private PullToRefreshListView pullToRefreshListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_coupon,null);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.coupon_pulllistview);
        //下拉刷新，上拉加载更多
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(onRefreshListener);
        mListView = pullToRefreshListView.getRefreshableView();
        return view;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter = null;
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        ModuleHttpApi.getCouponList(page,"USED");
    }
    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getCouponList + "USED").equals(apiNo)) {
            return;
        }
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getCouponList + "USED":
                    formatData(httpResult.getResult());
                    break;

            }
        }

    }

    private void formatData(String result) {
        CouponListModel mModel = GsonImplHelp.get().toObject(result, CouponListModel.class);
        if (null == mModel)
            return;
        if (0 == page) {
            if (mListDataBean != null) {
                mListDataBean.clear();
            }

        }
        mListDataBean.addAll(mModel.getData());
        if(mAdapter == null){
            mAdapter = new CouponListAdapter(getActivity(), mListDataBean);
            mListView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }


        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
        }
//        mAdapter.setOnUseConponListener(new CouponListAdapter.OnUseConponListener() {
//            @Override
//            public void immediatelyUseConponEvent(CouponListModel.DataBean dataBean) {
////                Intent intent = new Intent(getActivity(), MainActivity.class);
////                startActivity(intent);
//            }
//        });
    }
}
