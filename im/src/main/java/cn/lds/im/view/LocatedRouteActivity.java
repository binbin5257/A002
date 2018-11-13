//package cn.lds.im.view;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//import android.widget.Toast;
//
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
//import com.baidu.navisdk.adapter.BNaviSettingManager;
//import com.baidu.navisdk.adapter.BaiduNaviManager;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.BaiduMapHelper;
//import cn.lds.chatcore.common.LoadingDialog;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.data.message.LocationMessageModel;
//import cn.lds.chatcore.view.BaseActivity;
//import cn.lds.im.view.adapter.LocationAdpter;
//
///**
// * 路线规划选择页面
// *
// * @author pengwb
// */
//public class LocatedRouteActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
//
//    protected Context mContext;
//    // key = 0 ： 路径规划选择； 1 ：导航 ； 2：搜索；
//    protected int key = 0;
//    protected Intent mIntent;
//    protected ArrayList<LocationMessageModel> list = new ArrayList<LocationMessageModel>();
//    protected LocationAdpter adapter;
//    protected int plan = 1;
//    protected String address;
//    protected double lat = 0, lng = 0;
//    protected InputMethodManager mInputMethodManager;
//    public static String keyword = null;
//
//
//    protected BaiduMapHelper baiduMapHelper;
//    protected boolean isFirstPoi = true;
//
//    @ViewInject(R.id.top_back_btn)
//    protected Button topBack;
//
//    @ViewInject(R.id.map_resultsearch_confirm)
//    protected Button searchConfirm;
//
//    @ViewInject(R.id.top_menu_btn)
//    protected Button topConfirm;
//
//    @ViewInject(R.id.top_title_tv)
//    protected TextView topTitle;
//
//    @ViewInject(R.id.map_resultlist)
//    protected ListView listView;
//
//    @ViewInject(R.id.map_resultlayout1)
//    protected LinearLayout layout1;
//
//    @ViewInject(R.id.map_resultlayout2)
//    protected LinearLayout layout2;
//
//    @ViewInject(R.id.map_result_backlocated)
//    protected LinearLayout backlocated;
//
//    @ViewInject(R.id.map_result_rouplan_end)
//    protected EditText input1;
//
//    @ViewInject(R.id.map_resultsearch)
//    protected EditText input2;
//
//    @ViewInject(R.id.map_routeplan_rb)
//    protected RadioGroup group;
//
//    @ViewInject(R.id.map_resultdelete)
//    protected ImageButton map_resultdelete;
//
//    protected String mSDCardPath = null;
//    String authinfo = null;
//
//    protected int layoutID = R.layout.activity_map_result;
//    protected LocatedRouteActivity activity;
//
//    protected void setActivity(LocatedRouteActivity activity) {
//        this.activity = activity;
//    }
//    protected void setLayoutID(int layoutID) {
//        this.layoutID = layoutID;
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(layoutID);
//        ViewUtils.inject(LocatedRouteActivity.class,this);
//        if (null != activity) {
//            ViewUtils.inject(activity);
//        }
//        initConfig();
//    }
//
//    protected void initConfig() {
//        mContext = LocatedRouteActivity.this;
//        mIntent = getIntent();
//        key = mIntent.getIntExtra("key", 0);
//
//        baiduMapHelper = new BaiduMapHelper(LocatedRouteActivity.this);
//
////        baiduMapHelper.initPoiSearch(onGetPoiSearchResultListener);
////        BNOuterLogUtil.setLogSwitcher(true);  log日志开关
//
//        if (initDirs()) {
//            initNavi();//初始化百度语音导航
//        }
//        initView();
//        addLisener();
//    }
//
//    protected void initView() {
//        topBack.setVisibility(View.VISIBLE);
//        topTitle.setVisibility(View.VISIBLE);
//        initAdapter();
//        listView.setAdapter(adapter);
//        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//
//        switch (key) {
//            case 0:
//                topTitle.setText(getString(R.string.locatedrouteactivity_routeline));
//                layout2.setVisibility(View.GONE);
//                topConfirm.setText(getString(R.string.locatedrouteactivity_confirm));
//                topConfirm.setVisibility(View.VISIBLE);
//
//                lat = mIntent.getDoubleExtra("lat", 0);
//                lng = mIntent.getDoubleExtra("lng", 0);
//                keyword = mIntent.getStringExtra("adr");
//                address = keyword;
//                if (!ToolsHelper.isNull(keyword))
//                    input1.setText(keyword);
//                break;
//            case 1:
//                topTitle.setText(getString(R.string.locatedrouteactivity_routing));
//                layout1.setVisibility(View.GONE);
//                backlocated.setVisibility(View.GONE);
//                break;
//            case 2:
//                topTitle.setText(getString(R.string.common_search));
//                layout1.setVisibility(View.GONE);
//                input2.setHint(getString(R.string.common_search));
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    protected void initAdapter() {
//        adapter = new LocationAdpter(mContext, list);
//    }
//
//    protected void addLisener() {
//        topBack.setOnClickListener(this);
//        backlocated.setOnClickListener(this);
//        switch (key) {
//            case 0:
//                group.setOnCheckedChangeListener(this);
//                topConfirm.setOnClickListener(this);
//                input1.addTextChangedListener(watcher);
//                input1.setOnEditorActionListener(new OnEditorActionListener() {
//
//                    @Override
//                    public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
//                        if (arg1 == EditorInfo.IME_ACTION_SEARCH || arg1 == EditorInfo.IME_ACTION_DONE
//                                || arg1 == EditorInfo.IME_ACTION_NEXT) {
//                            hideKeyboard(input1);
//
//                            if (ToolsHelper.isNull(keyword))
//                                return true;
//
//                            LoadingDialog.showDialog(mContext, getString(R.string.locatedactivity_searching));
//                            isFirstPoi = true;
//                            baiduMapHelper.searchPoi(new LatLng(lat, lng), keyword);
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//                break;
//
//            default:
//                searchConfirm.setOnClickListener(this);
//                map_resultdelete.setOnClickListener(this);
//                input2.addTextChangedListener(watcher);
//                break;
//        }
//
//        listView.setOnItemClickListener(itemClickListener);
//
//    }
//
////    OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
////        /**
////         * 兴趣点详情
////         */
////        @Override
////        public void onGetPoiDetailResult(PoiDetailResult result) {
////            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
////                Toast.makeText(mContext, getString(R.string.locatedactivity_noresult), Toast.LENGTH_SHORT).show();
////            } else {
////                Toast.makeText(mContext, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT).show();
////            }
////        }
////
////        /**
////         * 兴趣点结果
////         */
////        @Override
////        public void onGetPoiResult(PoiResult result) {
////            LoadingDialog.dismissDialog();
////            if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
////                Toast.makeText(mContext, getString(R.string.locatedactivity_noresult), Toast.LENGTH_LONG).show();
////                return;
////            }
////            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////                // mBaiduMap.clear();
////
////                if (isFirstPoi) {
////                    list.clear();
////                    List<PoiInfo> poiInfoList = result.getAllPoi();
////                    if (!poiInfoList.isEmpty()) {
////                        for (PoiInfo item : poiInfoList) {
////                            LatLng latLng = item.location;
////                            if (null != latLng) {
////                                LocationMessageModel object = new LocationMessageModel(item.name, latLng.latitude, latLng.longitude
////                                        , item.address, false);
////                                list.add(object);
////                            }
////                        }
////                    }
////                    if (!list.isEmpty()) {
////                        LocationMessageModel item = list.get(0);
////                        item.setSlected(true);
////                        listView.setVisibility(View.VISIBLE);
////                        address = item.getAddress();
////                        lat = item.getLatitude();
////                        lng = item.getLongitude();
////
////                    } else {
////                        ToolsHelper.showInfo(mContext, getString(R.string.locatedactivity_noresult_inputagain));
////                    }
////
////                    isFirstPoi = false;
////                    adapter.notifyDataSetChanged();
////                }
////                return;
////            }
////        }
////    };
//
//    OnItemClickListener itemClickListener = new OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            lat = list.get(position).getLatitude();
//            lng = list.get(position).getLongitude();
//            switch (key) {
//                case 0:
//                    input1.setText(list.get(position).getAddress());
//                    break;
//                case 1:
//                    Intent intent1 = new Intent();
//                    intent1.putExtra("lat", lat);
//                    intent1.putExtra("lng", lng);
//                    intent1.putExtra("adr", list.get(position).getAddress());
//                    setResult(1001, intent1);
//                    finish();
//                    break;
//                case 2:
//                    Intent intent2 = new Intent();
//                    intent2.putExtra("lat", lat);
//                    intent2.putExtra("lng", lng);
//                    setResult(1002, intent2);
//                    finish();
//                    break;
//            }
//
//        }
//
//    };
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//        switch (checkedId) {
//
//            case R.id.route_linebus:
//                plan = 1;
//                break;
//            case R.id.route_linecar:
//                plan = 2;
//                break;
//            case R.id.route_linewalk:
//                plan = 3;
//                break;
//            default:
//                break;
//        }
//    }
//
//    TextWatcher watcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (0 == key) {
//                keyword = input1.getText().toString().trim();
//            } else {
//                keyword = input2.getText().toString().trim();
//                if (0 != s.length()) {
//                    map_resultdelete.setVisibility(View.VISIBLE);
//                    searchConfirm.setVisibility(View.VISIBLE);
//                } else {
//                    map_resultdelete.setVisibility(View.GONE);
//                    searchConfirm.setVisibility(View.GONE);
//                }
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.top_back_btn:
//                finish();
//                break;
//            case R.id.map_resultsearch_confirm:
//                hideKeyboard(input2);
//                break;
//            case R.id.map_result_backlocated:
//                Intent intent2 = new Intent();
//                setResult(1003, intent2);
//                finish();
//                break;
//            case R.id.top_menu_btn:
//                if (lat == 0) {
//                    ToolsHelper.showStatus(mContext,false, getString(R.string.locatedrouteactivity_set_newtarget));
//                } else {
//                    Intent intent = new Intent();
//                    intent.putExtra("lat", lat);
//                    intent.putExtra("lng", lng);
//                    intent.putExtra("plan", plan);
//                    intent.putExtra("adr", address);
//                    setResult(1000, intent);
//                    finish();
//                }
//                break;
//            case R.id.map_resultdelete:
//                input2.setText("");
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    /**
//     * 强制隐藏键盘
//     */
//    protected void hideKeyboard(EditText editText) {
//        if (mInputMethodManager.isActive(editText)) {
//            IBinder localIBinder = editText.getWindowToken();
//            mInputMethodManager.hideSoftInputFromWindow(localIBinder, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//    protected void initNavi() {
//
//        BNOuterTTSPlayerCallback ttsCallback = null;
//
//        BaiduNaviManager.getInstance().init(LocatedRouteActivity.this, mSDCardPath, mContext.getString(cn.lds.chatcore.R.string.app_name), new BaiduNaviManager.NaviInitListener() {
//            @Override
//            public void onAuthResult(int status, String msg) {
//                if (0 == status) {
////                    authinfo = "key校验成功!";
//                } else {
//                    authinfo = "key校验失败, " + msg;
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (ToolsHelper.isNull(authinfo))
//                            return;
//                        Toast.makeText(mContext, authinfo, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            public void initSuccess() {
////                Toast.makeText(mContext, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
//                initSetting();
//            }
//
//            public void initStart() {
////                Toast.makeText(mContext, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
//            }
//
//            public void initFailed() {
//                Toast.makeText(mContext, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }, null, ttsHandler, null);
//
//    }
//
//    /**
//     * 内部TTS播报状态回传handler
//     */
//    protected Handler ttsHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            int type = msg.what;
//            switch (type) {
//                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
//                    break;
//                }
//                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    };
//
//    protected void initSetting() {
//        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
//        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
//        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
//        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
//        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
//    }
//
//
//    protected boolean initDirs() {
//        mSDCardPath = getSdcardDir();
//        if (mSDCardPath == null) {
//            return false;
//        }
//        File f = new File(mSDCardPath, mContext.getString(cn.lds.chatcore.R.string.app_name));
//        if (!f.exists()) {
//            try {
//                f.mkdir();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    protected String getSdcardDir() {
//        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
//            return Environment.getExternalStorageDirectory().toString();
//        }
//        return null;
//    }
//}
