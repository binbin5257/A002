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
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.ViewHeightCalculator;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.MapAllParkModel;
import cn.lds.chatcore.db.ChoiceParkHistoryTable;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.view.adapter.ChoiceParkAdpter;
import de.greenrobot.event.EventBus;

/***
 * 选择取还车车场
 */
public class ChoiceBookParkActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.choicebookpark_listview)
    protected ListView choicebookpark_listview;
    @ViewInject(R.id.choicebookpark_historylistview)
    protected ListView choicebookpark_historylistview;

    @ViewInject(R.id.choicebookpark_historyllyt)
    protected LinearLayout choicebookpark_historyllyt;

    @ViewInject(R.id.choicebookpark_allnull)
    protected TextView choicebookpark_allnull;
    @ViewInject(R.id.choicebookpark_title)
    protected TextView choicebookpark_title;

    protected ChoiceParkAdpter choiceParkAdpter;
    protected ChoiceParkAdpter historyParkAdpter;
    protected ArrayList<MapAllParkModel.DataBean> allParks;
    protected ArrayList<MapAllParkModel.DataBean> historyParks;
    protected List<ChoiceParkHistoryTable> tables;

    protected ChoiceBookParkActivity activity;
    protected int layoutID = R.layout.activity_choice_book_park;

    public void setActivity(ChoiceBookParkActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ChoiceBookParkActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }

        initConfig();
    }
    protected void initConfig() {
        init();
    }

    protected void readHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tables = DbHelper.getDbUtils().findAll(Selector.from(ChoiceParkHistoryTable.class).limit(5).orderBy("timestamp", true));
                } catch (Exception e) {
                    LogHelper.e(this.getClass().getName(), e);
                }
                if (null == tables || tables.isEmpty())
                    return;
                if (!historyParks.isEmpty())
                    historyParks.clear();
                for (ChoiceParkHistoryTable table : tables) {
                    MapAllParkModel.DataBean dataBean = new MapAllParkModel.DataBean();
                    dataBean.setId(table.getParkid());
                    dataBean.setName(table.getName());
                    dataBean.setAddress(table.getAddress());
                    dataBean.setChargeSpots(table.getChargeSpotsCount());
                    dataBean.setAvailableVehicle(table.getVehicleCount());
                    dataBean.setLatitude(table.getLatitude());
                    dataBean.setLongitude(table.getLongitude());
                    dataBean.setDistance(table.getDistance());
                    historyParks.add(dataBean);
                }

                EventBus.getDefault().post(new ThreadEvent());
            }
        }).start();


    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.bookcar_choicecar_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);


        allParks = new ArrayList<>();
        historyParks = new ArrayList<>();
        //choiceParkAdpter = new ChoiceParkAdpter(mContext, allParks, 2);
        //historyParkAdpter = new ChoiceParkAdpter(mContext, historyParks, 1);
        initAdapter();
        choicebookpark_listview.setAdapter(choiceParkAdpter);
        choicebookpark_historylistview.setAdapter(historyParkAdpter);

        LoadingDialog.showDialog(mContext, "");
        ModuleHttpApi.locationResourcesScheduled();
    }
    protected  void initAdapter() {
        choiceParkAdpter = new ChoiceParkAdpter(mContext, allParks, 2);
        historyParkAdpter = new ChoiceParkAdpter(mContext, historyParks, 1);
    }
    @Override
    protected void onResume() {
        super.onResume();
        readHistory();
    }

    @OnItemClick(R.id.choicebookpark_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MapAllParkModel.DataBean selectPark = allParks.get(position);
//        mIntent.putExtra("selectPark", selectPark);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    @OnItemClick(R.id.choicebookpark_historylistview)
    public void onHistoryItemClick(AdapterView<?> parent, View view, int position, long id) {
        MapAllParkModel.DataBean selectPark = historyParks.get(position);
//        mIntent.putExtra("selectPark", selectPark);
        setResult(RESULT_OK, mIntent);
        finish();
    }


    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.input_whichcity,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_whichcity:
                ToolsHelper.showStatus(mContext, false, "城市选择开发中，敬请期待");
                break;
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogHelper.e("onStart");
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e("注册EvenBus", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogHelper.e("onPause");
    }

    protected void B001(String httpResult) {
        MapAllParkModel mapHlistModel = GsonImplHelp.get().toObject(httpResult, MapAllParkModel.class);
        if (!allParks.isEmpty())
            allParks.clear();
        allParks.addAll(mapHlistModel.getData());
        choiceParkAdpter.notifyDataSetChanged();
        ViewHeightCalculator.setListViewHeightBasedOnChildren(choicebookpark_listview);

        if (allParks.isEmpty())
            choicebookpark_listview.setVisibility(View.GONE);
        else
            choicebookpark_listview.setVisibility(View.VISIBLE);
    }

    /**
     * 发送验证码
     * 使用手机号注册
     * 登录
     *
     * @param event http请求完成事件
     */
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo))
                return;
            // 全部停车场
            if (ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo)) {
                B001(event.getResult().getResult());
            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }


    /**
     * 订阅：相关REST请求失败后
     *
     * @param event HttpRequestEvent
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        // 只要是本画面的处理。都异常时都关闭等待
        HttpResult httpResult = event.getResult();
        // API区分
        String apiNo = httpResult.getApiNo();
        if (!ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo))
            return;
        // 全部停车场
        if (ModuleHttpApiKey.locationResourcesScheduled.equals(apiNo)) {
            ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
            choicebookpark_allnull.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 订阅：相关REST请求失败后
     *
     * @param event HttpRequestEvent
     */
    public void onEventMainThread(ThreadEvent event) {
        historyParkAdpter.notifyDataSetChanged();
        ViewHeightCalculator.setListViewHeightBasedOnChildren(choicebookpark_historylistview);
        if (historyParks.isEmpty())
            choicebookpark_historyllyt.setVisibility(View.GONE);
        else
            choicebookpark_historyllyt.setVisibility(View.VISIBLE);
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

    class ThreadEvent {
    }

}
