package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BaiduMapHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.data.ReservationOrdersLocationsModel;
import cn.lds.im.view.adapter.SearchResultsAdapter;

/**
 * 搜索位置界面
 * Created by E0608 on 2017/7/24.
 */

public class SearchLocationActivity extends BaseActivity implements View.OnClickListener, OnGetSuggestionResultListener {

    private ArrayList<ReservationOrdersLocationsModel.DataBean> allParks = new ArrayList<>();
    private List<SuggestionResult.SuggestionInfo> mSuggestions = new ArrayList<>();
    private ListView resultsLv;
    private SearchResultsAdapter searchResultsAdapter;
    private SuggestionResult.SuggestionInfo selectedInfo; //选中的搜索位置对象
    private EditText input_keyword;
    private String userWay;
    private SuggestionSearch suggestionSearch;
    /**
     * 定位SDK监听函数
     */
    BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //实时更新，定位信息
            MyApplication.myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }


    };
    private TextView whichcity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        initView();
        initBaiduMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.input_whichcity:
                ToolsHelper.showStatus(mContext, false, "城市选择开发中，敬请期待");
                break;


        }

    }

    private void initView() {

        Button backBtn = (Button) findViewById(R.id.top_back_btn);
        TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
        TextView input_whichcity = (TextView) findViewById(R.id.input_whichcity);
        input_keyword = (EditText) findViewById(R.id.input_keyword);
        suggestionSearch = SuggestionSearch.newInstance();
        resultsLv = (ListView) findViewById(R.id.suggestion_results);
        whichcity = (TextView) findViewById(R.id.input_whichcity);
        ImageView searchView = (ImageView) findViewById(R.id.iv_search);
        whichcity.setText(MyApplication.lastCity);
        backBtn.setOnClickListener(this);
        backBtn.setVisibility(View.VISIBLE);
        getIntentExtra(titleTv, input_keyword);
        searchView.setOnClickListener(this);
        addListener(backBtn,input_whichcity, input_keyword, suggestionSearch);

    }

    private void initBaiduMap() {
        BaiduMapHelper baiduMapHelper = new BaiduMapHelper(this);
        baiduMapHelper.initLocation(bdLocationListener);
    }

        /**
     * 增加页面点击事件
     * @param backBtn
     * @param input_whichcity
     * @param input_keyword
     * @param suggestionSearch
     */
    private void addListener(Button backBtn, TextView input_whichcity, final EditText input_keyword, SuggestionSearch suggestionSearch) {
        backBtn.setOnClickListener(this);
        input_whichcity.setOnClickListener(this);
        input_keyword.setOnClickListener(this);
        suggestionSearch.setOnGetSuggestionResultListener(this);
        resultsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedInfo = mSuggestions.get(position);
                input_keyword.setText(selectedInfo.key);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("SEARCH_RESULT",selectedInfo);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        input_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryLocation();

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    /**
     * 查询位置
     */
    String city;
    private void queryLocation() {
        mSuggestions.clear();
        String searchContent = input_keyword.getText().toString().trim();

        if(MyApplication.lastCity.contains("市")){
            city = MyApplication.lastCity;
        }else{
            city = MyApplication.lastCity + "市";
        }

        if(!TextUtils.isEmpty(searchContent)){
            suggestionSearch.requestSuggestion((new SuggestionSearchOption())
                    .keyword(searchContent)
                    .city(city));
        }else{
            if(!TextUtils.isEmpty(userWay) && !userWay.equals("RETURN_CAR")){
                Toast.makeText(mContext,"请输入接车地点",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"请输入还车地点",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void getIntentExtra(TextView titleTv, EditText input_keyword) {
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            userWay = intent.getStringExtra("USER_WAY");
            if(!TextUtils.isEmpty(userWay) && !userWay.equals("RETURN_CAR")){
                titleTv.setText("接车地点");
                input_keyword.setHint("请选择接车地点");
            }else if(!TextUtils.isEmpty(userWay)){
                titleTv.setText("还车地点");
                input_keyword.setHint("请选择还车地点");
            }else{
                titleTv.setText("接车地点");
                input_keyword.setHint("请选择接车地点");
            }
        }else{
            titleTv.setText("接车地点");
            input_keyword.setHint("请选择接车地点");
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if(suggestionResult == null || suggestionResult.getAllSuggestions() == null){
            return; //未找到相关结果
        }else{
            List<SuggestionResult.SuggestionInfo> suggestions = suggestionResult.getAllSuggestions();
            for(SuggestionResult.SuggestionInfo info:suggestions){
                if(info.city.equals(city)){
                    if(!TextUtils.isEmpty(info.city) && !TextUtils.isEmpty(info.district)){}
                    mSuggestions.add(info);
                }
            }
            if(mSuggestions == null || mSuggestions.size() == 0){
                Toast.makeText(mContext,"没有搜索到相关地点",Toast.LENGTH_SHORT).show();
            }
            refrushAdapter();

        }
    }

    private void refrushAdapter() {
        if(searchResultsAdapter == null){
            searchResultsAdapter = new SearchResultsAdapter(this, mSuggestions);
            resultsLv.setAdapter(searchResultsAdapter);
        }else{
            searchResultsAdapter.notifyDataSetChanged();
        }
    }
}
