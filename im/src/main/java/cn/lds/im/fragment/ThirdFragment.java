package cn.lds.im.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.OrderCancelEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.RouteModel;
import cn.lds.im.view.MainActivity;
import cn.lds.im.view.adapter.RouteAdpter;
import de.greenrobot.event.EventBus;

/**
 * 行程页面
 */
public class ThirdFragment extends MyActivityFragment {
    public static final String TAG = "ThirdFragment";


    @ViewInject(R.id.route_listview)
    protected ListView mListview;
    @ViewInject(R.id.noneroute_layout)
    protected LinearLayout noneroute_layout;

    protected Context mContext = null;
    protected Intent intent = null;
    protected View view;
    protected List<RouteModel.DataBean> roulists;
    protected RouteAdpter adpter;
//    protected MainActivity mActivity;

//    @SuppressLint("ValidFragment")
//    public ThirdFragment(MainActivity mActivity) {
//        this.mActivity = mActivity;
//    }

    @SuppressLint("ValidFragment")
    public ThirdFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(ThirdFragment.class, this, view);
        init();
        return view;
    }

    protected void init() {
        mContext = getActivity();
        intent = new Intent();

        roulists = new ArrayList<>();
        initAdapter();
        mListview.setAdapter(adpter);
    }

    protected void initAdapter() {
        adpter = new RouteAdpter(mContext, roulists);
    }

    @OnClick({R.id.tv_setting_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting_exit:
                MainActivity mainActivity = (MainActivity) MyApplication.getInstance().getMainActivity();
                if (null != mainActivity)
                    mainActivity.switchContent(0);
//                mActivity.selectPager(0);
                break;
        }
    }

    /**
     * 订阅：相关REST请求失败后
     * <p>
     * 处理"首次拉取好友列表"请求、"首次拉取群聊列表"请求、"首次拉取公众号列表"请求
     * </p>
     * <p>
     * 首次拉取失败后，也可以进入应用，不会阻止应用的使用，在合适的时机还会重新拉取
     * </p>
     *
     * @param event HttpRequestEvent
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        // 只要是本画面的处理。都异常时都关闭等待
        HttpResult httpResult = event.getResult();
        // API区分
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getJourneyList.equals(apiNo)))
            return;

        ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
        roulists.clear();
        updateView();
    }

    /**
     * 登录成功
     * <p>
     * 处理"拉取Token请求"
     * </p>
     *
     * @param event HttpRequestEvent
     */
    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getJourneyList.equals(apiNo)))
            return;
        D001(event.getResult().getResult());
    }

    protected void D001(String result) {
        RouteModel routeModel = GsonImplHelp.get().toObject(result, RouteModel.class);
        if (!roulists.isEmpty())
            roulists.clear();
        roulists.addAll(routeModel.getData());
        adpter.notifyDataSetChanged();
        updateView();
    }

    protected void updateView() {
        if (roulists.isEmpty()) {
            noneroute_layout.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.GONE);
        } else {
            mListview.setVisibility(View.VISIBLE);
            noneroute_layout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        LogHelper.e("onStart");
        if (AccountManager.getInstance().isLogin())
            //获取数据
            ModuleHttpApi.getJourneyList();
        else
            updateView();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e("注册EvenBus", e);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.e("onStop");
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e("注销EvenBus", e);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogHelper.e("setUserVisibleHint::" + isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    @Override
    public void show(FragmentManager manager, MyActivityFragment fragment) {
        super.show(manager, this);
        ModuleHttpApi.getJourneyList();
    }


    public void onEventMainThread(OrderCancelEvent event) {

        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                ModuleHttpApi.getJourneyList();
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setConfirmTx(getString(R.string.order_haode)).setContentTx(getString(R.string.order_yajinbuzu)).setBackKey(true).show(MyApplication.getInstance().getIv__top());

    }
}