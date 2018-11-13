package cn.lds.im.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Range;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.widget.ListView.PullToRefreshLayout;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ChargingModel;
import cn.lds.im.data.LocationCtiyModel;
import cn.lds.im.data.RangeModel;
import cn.lds.im.view.adapter.ChargingAdapter;
import cn.lds.im.view.widget.ChargingListView;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

/**
 * 充电桩列表页面
 * Created by E0608 on 2017/10/24.
 */

public class ChargingListActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.charing_lv)
    private ChargingListView chartLv;
    @ViewInject(R.id.input_whichcity)
    private TextView input_whichcity;
    @ViewInject(R.id.triangle_arrow)
    protected ImageView triangle_arrow;  //城市左边向下三角标
    @ViewInject(R.id.distance_triangle_arrow)
    protected ImageView distance_triangle_arrow;  //搜索范围向下三角标

    @ViewInject(R.id.input_keyword)
    protected EditText input_keyword;
    @ViewInject(R.id.no_data)
    protected LinearLayout no_data;
    @ViewInject(R.id.pull_to_refresh_view)
    protected PullToRefreshLayout ptr;
    @ViewInject(R.id.no_data_tv)
    protected TextView no_data_tv;
    @ViewInject(R.id.distance_input_which)
    protected TextView distance_input_which;

    int size = 10;

    int page = 0;

    protected ChargingListActivity activity;

    private List<ChargingModel.DataBean> mDataList = new ArrayList<>();
    private List<LocationCtiyModel.DataBean> mCityList = new  ArrayList<>();
    private List<RangeModel> rangeList = new ArrayList<>(); //搜索范围集合
    //城市代码
    private String mCityCode;
    private ChargingAdapter chargingAdapter;
    private String searchContent = "";
    private long searcheRange = 10000;
    private boolean last = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_list);
        ViewUtils.inject(ChargingListActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @OnClick({R.id.top_back_btn,R.id.input_whichcity,R.id.distance_input_which})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.input_whichcity:
                choiceCity();
                break;
            case R.id.distance_input_which:
                choiceRange();
                break;
        }
    }

    private void choiceRange() {
        setBackGroundColor(this,0.7f);
        CustomPopuwindow.getInstance().showSearchRange(Gravity.RIGHT,this, rangeList, findViewById(R.id.distance_input_which),distance_triangle_arrow, new CustomPopuwindow.OnChoiceCityListener() {

            @Override
            public void selectedCity(Object data, int index) {
                 RangeModel bean = (RangeModel) data;
                searcheRange = bean.getMile();
                 if(bean.getMile() == 0){
                     distance_input_which.setText("不限范围");
                 }else{
                     distance_input_which.setText(bean.getMile()/1000 + "km");
                 }

                 for(RangeModel r : rangeList){
                     if(r.getMile() == bean.getMile()){
                         r.setSelected(true);
                     }else{
                         r.setSelected(false);
                     }

                 }
                ptr.autoRefresh();

            }

            @Override
            public void dismiss() {
                setBackGroundColor(ChargingListActivity.this,1.0f);
            }
        });
    }

    /**
     * 设置窗体透明度
     */
    public void setBackGroundColor(Activity context, float alpha){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
    }
    private void choiceCity() {
        setBackGroundColor(this,0.7f);
        CustomPopuwindow.getInstance().showList(Gravity.LEFT,this, mCityList, findViewById(R.id.input_whichcity),triangle_arrow, new CustomPopuwindow.OnChoiceCityListener() {
            @Override
            public void selectedCity(Object data, int index) {
                searchContent = "";
                searcheRange = 10000;
                LocationCtiyModel.DataBean bean = (LocationCtiyModel.DataBean) data;
                input_keyword.setText(searchContent);
                input_whichcity.setText(bean.getName());
                mCityCode = bean.getCode();
                for(int i = 0; i < mCityList.size();i++){
                    LocationCtiyModel.DataBean cityBean = mCityList.get(i);
                    if(i == index){
                        cityBean.setSelected(true);
                    }else{
                        cityBean.setSelected(false);
                    }
                }
                initRangeData();
                ptr.autoRefresh();

            }

            @Override
            public void dismiss() {
                setBackGroundColor(ChargingListActivity.this,1.0f);
            }
        });
    }
    private void initView() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.ChargingListActivity_title));
        btnBack.setVisibility(View.VISIBLE);
        chartLv.setOnItemClickListener(this);
        ptr.setOnRefreshListener(this);
        chartLv.setOnScrollListener(this);
        input_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchContent = input_keyword.getText().toString();
                if(input_keyword.getText().toString().trim().length() == 0){
                    ptr.autoRefresh();
                }
            }
        });
        input_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideInputService();

                    String keyword = input_keyword.getText().toString().trim();
                    if (ToolsHelper.isNull(keyword))
                        return true;
                    refrushList(keyword);
                    return true;
                }
                return false;

            }



        });

    }
    private void refrushList(String keyword) {
        searchContent = keyword;
        ptr.autoRefresh();
    }
    /**
     * 隐藏键盘
     */
    protected void hideInputService() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void initData() {
        LoadingDialog.showDialog(this,"请稍后...");
        ModuleHttpApi.getLocationsCitys();
        initRangeData(); //初始化搜索范围数据
    }

    /**
     * 初始化范围数据
     */
    private void initRangeData() {
        rangeList.clear();
        RangeModel rangeModel_0 = new RangeModel();
        rangeModel_0.setMile(0);
        rangeModel_0.setSelected(false);
        RangeModel rangeModel_10 = new RangeModel();
        rangeModel_10.setMile(10000);
        rangeModel_10.setSelected(true);
        RangeModel rangeModel_20 = new RangeModel();
        rangeModel_20.setMile(20000);
        rangeModel_20.setSelected(false);
        RangeModel rangeModel_30 = new RangeModel();
        rangeModel_30.setMile(30000);
        rangeModel_30.setSelected(false);
        rangeList.add(rangeModel_10);
        rangeList.add(rangeModel_20);
        rangeList.add(rangeModel_30);
        rangeList.add(rangeModel_0);
        distance_input_which.setText("10km");

    }

    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!(ModuleHttpApiKey.getLocationsCitys.equals(apiNo)
                    || ModuleHttpApiKey.getChargingLocations_refresh.equals(apiNo)
                    || ModuleHttpApiKey.getChargingLocations_loadmore.equals(apiNo)
            )) {
                return;
            }
            switch (apiNo){
                case ModuleHttpApiKey.getLocationsCitys:
                    locationCityList(event.getResult().getResult());// 城市列表
                    break;
                case ModuleHttpApiKey.getChargingLocations_refresh:
                    processChargingLocationsData(ModuleHttpApiKey.getChargingLocations_refresh,event.getResult().getResult());
                    break;
                case ModuleHttpApiKey.getChargingLocations_loadmore:
                    processChargingLocationsData(ModuleHttpApiKey.getChargingLocations_loadmore,event.getResult().getResult());
                    break;
            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    private void processChargingLocationsData(String key,String result) {
        ChargingModel chargingModel = GsonImplHelp.get().toObject(result, ChargingModel.class);
        last = chargingModel.getPageable().isLast();

        List<ChargingModel.DataBean> dataBeanList = chargingModel.getData();
        if(key.equals(ModuleHttpApiKey.getChargingLocations_refresh)){
            ptr.refreshFinish(PullToRefreshLayout.SUCCEED);
            if(dataBeanList != null){
                mDataList.clear();
                mDataList.addAll(dataBeanList);
            }
        }else if(key.equals(ModuleHttpApiKey.getChargingLocations_loadmore)){
            ptr.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            if(dataBeanList != null){
                mDataList.addAll(dataBeanList);
            }
        }

        if(mDataList.size() == 0){
            ptr.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);
            no_data_tv.setText("没有找到充电站信息~~");
        }else{
            ptr.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.GONE);
            if(chargingAdapter == null){
                chargingAdapter = new ChargingAdapter(this,mDataList);
                chartLv.setAdapter(chargingAdapter);
            }else{
                chargingAdapter.notifyDataSetChanged();
            }

        }

    }

    private void locationCityList(String result) {
        LocationCtiyModel locationCityModel = GsonImplHelp.get().toObject(result, LocationCtiyModel.class);
        List<LocationCtiyModel.DataBean> citys = locationCityModel.getData();
        if(citys != null && citys.size() > 0){
            mCityList.clear();
            mCityList.addAll(citys);
        }
        input_whichcity.setText(MyApplication.lastCity + "市");
        for(LocationCtiyModel.DataBean bean:mCityList){
            if(bean.getName().equals(MyApplication.lastCity + "市")){
                mCityCode = bean.getCode();
                bean.setSelected(true);
            }else{
                bean.setSelected(false);
            }
        }
        ptr.autoRefresh();
    }

    /**
     * 请求充电场站列表
     */
    private void requestChargingLocations(String requestKey) {
        ModuleHttpApi.getChargingLocations(requestKey,MyApplication.myLocation,mCityCode,page,searchContent,searcheRange);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ChargingListActivity.this,ChargingDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("distance",mDataList.get(position).getDistance());
        bundle.putInt("id",mDataList.get(position).getId());
        intent.putExtras(bundle);
        ChargingListActivity.this.startActivity(intent);

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 0;
        requestChargingLocations(ModuleHttpApiKey.getChargingLocations_refresh);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if(last){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL_NO_DATA);
        }else{
            requestChargingLocations(ModuleHttpApiKey.getChargingLocations_loadmore);
        }


//
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 记录滚动到最后一条的数据id 作为下次请求更多的开始参数 beginPos
       int lasetItemId = chartLv.getLastVisiblePosition() + 1;
        page = lasetItemId / size;
    }
}
