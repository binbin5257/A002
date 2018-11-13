package cn.lds.im.view;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.SystemConfig;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.db.MessageTable;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MapFragmentChoiceEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.event.OrganizationChangEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.manager.MessageManager;
import cn.lds.chatcore.manager.VersionManager;
import cn.lds.chatcore.view.BaseFragmentActivity;
import cn.lds.chatcore.view.dialog.ConfirmAndCancelDialog;
import cn.lds.chatcore.view.dialog.annotation.ClickPosition;
import cn.lds.chatcore.view.dialog.callback.OnDialogClickListener;
import cn.lds.chatcore.view.widget.SlidingMenu.SlidingMenu;
import cn.lds.im.common.IntentHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.common.permission.PermissionHelper;
import cn.lds.im.data.CheckUseCarApplyModel;
import cn.lds.im.data.GetProcessModel;
import cn.lds.im.data.HasProcessModel;
import cn.lds.im.data.HomeActiveModel;
import cn.lds.im.data.LeftMenuBean;
import cn.lds.im.data.LeftMenuItemData;
import cn.lds.im.data.NearStationModel;
import cn.lds.im.enums.HomeModelEnum;
import cn.lds.im.fragment.FirstFragment;
import cn.lds.im.fragment.MyActivityFragment;
import cn.lds.im.fragment.SecondFragment;
import cn.lds.im.fragment.ThirdFragment;
import cn.lds.im.view.adapter.LeftMenuAdapter;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

import static cn.lds.chatcore.MyApplication.getInstance;
import static cn.lds.chatcore.MyApplication.org_cancel_order;

/**
 * @author suncf
 */
public class MainActivity extends BaseFragmentActivity {
    // R.id.top__iv
    @ViewInject(R.id.top__iv)
    public ImageView mTop_iv;
    //标题
    @ViewInject(R.id.top_title_tv)
    protected TextView mTopTitle_tv;
    //标题---立即用车和预约用车按钮
    @ViewInject(R.id.top_title_btn)
    protected TextView mTopTitle_btn;
    //侧滑栏按钮
    @ViewInject(R.id.top_menu_left)
    protected Button mTopMenu;
    //我的消息
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView top_menu_btn_del;
    //侧滑栏打开后的阴影
    @ViewInject(R.id.main_back)
    protected View mShadow;
   //侧滑栏打开后的阴影
    @ViewInject(R.id.root_view)
    public RelativeLayout rootView;
    @ViewInject(R.id.center_marker)
    protected ImageView centerMarker;
    //新手阅读手册布局
    @ViewInject(R.id.rl_tutorial)
    public RelativeLayout tutorialRlyt;
    @ViewInject(R.id.top_title_lly)
    protected LinearLayout top_title_lly;


    protected TextView txt_submenu_name;
    protected ImageView iv_submenu_name;
    protected ImageView iv_submenu_head;
    protected TextView submenu_tv;
    protected static String TAG = MainActivity.class.getSimpleName();
    protected Context mContext;
    protected SlidingMenu menu; // 滑动菜单
    // 类型为Fragment的动态数组
    protected ArrayList<MyActivityFragment> fragmentList;
    protected Intent mIntent;
    //记录当前pager index
    protected int currentIndex = 0;
    protected MyActivityFragment mContent;
    protected FirstFragment mapFragment;
    protected int layoutID = R.layout.activity_main;
    protected MainActivity activity;
    protected String reviewtype = null;
    protected static boolean flag = false;
    protected static boolean flag1 = false;
    public static boolean isThought = false;
    private RelativeLayout rl_uncommitted;
    private TextView uncommitted_submenu_name;
    private ImageView iv_uncommitted_submenu_name;
    private TextView uncommitted_submenu_tv;
    private RelativeLayout rl_commit;

    public static MainActivity mainActivity;

    /**
     * 存放侧滑菜单条目的集合
     */
    private List<LeftMenuBean> itemList = new ArrayList<>();


    protected int choiceType = 3;//模式

    List<HomeActiveModel.DataBean> mActiveList = new ArrayList<>();
    private LeftMenuAdapter leftMenuAdapter;
    private ListView listView;

    protected void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().setMainActivity(this);
        mainActivity = this;
        setContentView(layoutID);
        ViewUtils.inject(MainActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
        checkVersion();
    }

    protected void initConfig() {
        MyApplication.getInstance().setIv__top((ImageView) findViewById(R.id.top__iv));
        mContext = this;
        mIntent = new Intent();
        MyApplication.getInstance().addActivity(this);

        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        MyApplication.getInstance().setHeight(outRect.height());
        //侧滑栏初始化
        initSlidingMenu();
        init();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void switchContent(int current) {

        switch (current) {
            case 0:
                resetTopbar();
                mContent = fragmentList.get(0);
                break;
            case 1:
                mTopTitle_btn.setVisibility(View.GONE);
                mTopTitle_tv.setVisibility(View.VISIBLE);
                mTopTitle_tv.setText(getString(R.string.bottom_rbtn_second));
                mContent = fragmentList.get(1);
                break;
            case 2:
                mTopTitle_btn.setVisibility(View.GONE);
                mTopTitle_tv.setVisibility(View.VISIBLE);
                mTopTitle_tv.setText(getString(R.string.bottom_rbtn_third));
                mContent = fragmentList.get(2);
                break;
        }


        if (!mContent.isAdded()) {
            if (null != getSupportFragmentManager().getFragments() && getSupportFragmentManager().getFragments().size
                    () > 0) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                }
            }

            getSupportFragmentManager().beginTransaction().add(R.id.frame_m, mContent).commit();
        } else {
            mContent.show(getSupportFragmentManager(), mContent);
        }
        //记录当前index
        currentIndex = current;
        menu.showContent();
    }


    /**
     * 初始化侧滑栏
     */
    protected void initSlidingMenu() {
        //初始化菜单
        menu = new SlidingMenu(this);
        //设置显示方式
        menu.setMode(SlidingMenu.LEFT);
        //设置从边缘滑动可打开菜单
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 滑动菜单边缘宽控件
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 滑动菜单边缘图像
        menu.setShadowDrawable(R.drawable.shadow);
        // 滑动菜单背景色
        menu.setBackgroundColor(0XFF001122);
        // 滑动菜单后主页面显示的宽度
        int w = MyApplication.getInstance().width / 2;
        menu.setBehindOffsetRes(w);

        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.submenu_new);
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                mShadow.setVisibility(View.VISIBLE);
            }
        });
        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                mShadow.setVisibility(View.GONE);
            }
        });

        txt_submenu_name = (TextView) menu.findViewById(R.id.txt_submenu_name);
        iv_submenu_name = (ImageView) menu.findViewById(R.id.iv_submenu_name);
        iv_submenu_head = (ImageView) menu.findViewById(R.id.btn_submenu_personhead);
        submenu_tv = (TextView) menu.findViewById(R.id.submenu_tv);
        rl_commit = (RelativeLayout) menu.findViewById(R.id.rl_commit);
        rl_uncommitted = (RelativeLayout) menu.findViewById(R.id.rl_uncommitted);
        uncommitted_submenu_name = (TextView) menu.findViewById(R.id.uncommitted_submenu_name);
        iv_uncommitted_submenu_name = (ImageView) menu.findViewById(R.id.iv_uncommitted_submenu_name);
        uncommitted_submenu_tv = (TextView) menu.findViewById(R.id.uncommitted_submenu_tv);
        iv_submenu_head.setOnClickListener(slideMunuClicker);
        listView = (ListView) menu.findViewById(R.id.lv_left_menu);
    }

    public void init() {

        setFragment();
        resetTopbar();
        mTopMenu.setVisibility(View.VISIBLE);
        top_menu_btn_del.setVisibility(View.VISIBLE);

        //初始化界面时更新个人信息
        AccountsTable table = AccountManager.getInstance().findByNo();
        if(table != null && AccountManager.getInstance().isLogin()){
            updatePersonInfo(AccountManager.getInstance().findByNo());
        }

        switchContent(0);

    }

    protected void setFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<MyActivityFragment>();
        } else {
            fragmentList.clear();
        }
         mapFragment = new FirstFragment();
        fragmentList.add(mapFragment);
    }

    /**
     * 恢复初始topbar
     */
    protected void resetTopbar() {
        mTopTitle_btn.setVisibility(View.VISIBLE);
        mTopTitle_tv.setVisibility(View.GONE);
    }

    protected void setAccount() {
        if (AccountManager.getInstance().isLogin()) {

            AccountsTable table = AccountManager.getInstance().findByNo();

            if (null != table) {
                updatePersonInfo(table);

                //开关按钮，第一次进入flag等于false,弹出框出现，同时flag置为true.再进入该画面时不出现弹框
                if (!flag) {
                    if (ToolsHelper.isNull(reviewtype) || Constants.UNCOMMITTED.equals(reviewtype) || Constants.REFUSED.equals(reviewtype)) {
                        ConfirmAndCancelDialog dialog = new ConfirmAndCancelDialog(this,R.style.MyDialogStyle).setOnDialogClickListener(new OnDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dialog, String clickPosition) {
                                  switch (clickPosition){
                                      case ClickPosition.ACCEPT:
                                          Intent intent = new Intent(mContext, AuthenticationActivity.class);
                                          startActivity(intent);
                                          break;
                                      case ClickPosition.CANCEL:
                                          break;
                                  }
                            }
                        });
                        dialog.setConfirmText(mContext.getString(R.string.pop_window_confirm));
                        dialog.setCancelText(mContext.getString(R.string.pop_window_cancel));
                        dialog.setPromptContent(getString(R.string.pop_window_promt));
                        dialog.showDialog(rootView);
                    } else {
                        if (!flag1) {
                            ModuleHttpApi.getHasProcess();
                            flag1 = true;
                        }
                    }
                    flag = true;
                }
            } else {
                txt_submenu_name.setText("");
            }
            iv_submenu_name.setVisibility(View.VISIBLE);
            submenu_tv.setVisibility(View.VISIBLE);
        } else {
            txt_submenu_name.setText(getString(R.string.loginidentify_title));
            iv_submenu_name.setVisibility(View.GONE);
            submenu_tv.setVisibility(View.GONE);
            iv_submenu_head.setImageResource(R.drawable.bg_headavatar);

        }
    }


    /**
     * 更新个人信息
     * @param table
     */
    private void updatePersonInfo(AccountsTable table) {

        String name = table.getName();
        //头像
        if (ToolsHelper.isNull(table.getAvatar())) {
            iv_submenu_head.setImageResource(R.drawable.bg_headavatar);
        } else {
            ImageLoaderManager.getInstance().displayImage(table.getAvatar(), iv_submenu_head);
        }
        // 认证
        reviewtype = table.getReviewType();
        if (ToolsHelper.isNull(reviewtype)) {
            reviewtype = Constants.UNCOMMITTED;
        } else {
            switch (reviewtype) {
                case Constants.UNCOMMITTED://未认证
                    rl_uncommitted.setVisibility(View.VISIBLE);
                    rl_commit.setVisibility(View.GONE);
                    if(TextUtils.isEmpty(name)){
                        uncommitted_submenu_name.setText(table.getMobile());
                    }else{
                        uncommitted_submenu_name.setText(name);

                    }
                    uncommitted_submenu_tv.setText(Constants.UNCOMMITTEDNAME);
                    uncommitted_submenu_tv.setTextColor(getResources().getColor(R.color.submenu_no_identification));
                    iv_uncommitted_submenu_name.setImageResource(R.drawable.bg_unidentified);
                    break;
                case Constants.REVIEWING://待审核
                    if(TextUtils.isEmpty(name)){
                        uncommitted_submenu_name.setText(table.getMobile());
                    }else{
                        txt_submenu_name.setText(name);

                    }
                    rl_uncommitted.setVisibility(View.GONE);
                    rl_commit.setVisibility(View.VISIBLE);
                    submenu_tv.setText(Constants.REVIEWINGNAME);
                    submenu_tv.setTextColor(getResources().getColor(R.color.submenu_no_identification));
                    iv_submenu_name.setImageResource(R.drawable.bg_unidentified);
                    break;
                case Constants.APPROVED://审核通过
                    rl_uncommitted.setVisibility(View.GONE);
                    rl_commit.setVisibility(View.VISIBLE);
                    submenu_tv.setText(Constants.APPROVEDNAME);
                    txt_submenu_name.setText(table.getName());
                    submenu_tv.setTextColor(getResources().getColor(R.color.submenu_identification));
                    iv_submenu_name.setImageResource(R.drawable.bg_identified);
                    break;
                case Constants.REFUSED://审核拒绝
                    if(TextUtils.isEmpty(name)){
                        uncommitted_submenu_name.setText(table.getMobile());
                    }else{
                        txt_submenu_name.setText(name);

                    }
                    rl_uncommitted.setVisibility(View.GONE);
                    rl_commit.setVisibility(View.VISIBLE);
                    submenu_tv.setText(Constants.REFUSEDNAME);
                    submenu_tv.setTextColor(getResources().getColor(R.color.submenu_no_identification));
                    iv_submenu_name.setImageResource(R.drawable.bg_unidentified);
                    break;
            }
        }
    }

    /**
     * 侧滑菜单栏监听.
     *
     */
    protected View.OnClickListener slideMunuClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submenu_personhead://个人中心
                    if (AccountManager.getInstance().isLogin()) {
                        mIntent.setClass(mContext, ProfileActivity.class);
                        startActivityForResult(mIntent,200);
                    } else {
                        mIntent.setClass(mContext, WuhuLoginActivity.class);
                        startActivity(mIntent);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 刷新侧滑栏条目
     */
    private void reflushLeftMenuItem() {
        if(leftMenuAdapter == null){
            leftMenuAdapter = new LeftMenuAdapter(this,itemList);
            listView.setAdapter(leftMenuAdapter);
        }else{
            leftMenuAdapter.notifyDataSetChanged();
        }
        leftMenuAdapter.setOnItemOnClickListener(new LeftMenuAdapter.OnItemOnClickListener() {
            @Override
            public void OnItemClickEvent(LeftMenuBean bean) {
                switch (bean.getId()){
                    case "wallet":
                        if (AccountManager.getInstance().isLogin()) {
                            mIntent.setClass(mContext, WalletActivity.class);
                            startActivity(mIntent);
                        } else {
                            mIntent.setClass(mContext, WuhuLoginActivity.class);
                            startActivity(mIntent);
                        }
                        break;
                    case "apply":
                        checkCommitted(ApplyActivity.class);
                        break;
                    case "mark":
                        checkCommitted(MarkingActivity.class);
                        break;
                    case "active":
                        mIntent.setClass(mContext, EventActivity.class);
                        startActivity(mIntent);
                        break;
                    case "trip":
                        if (AccountManager.getInstance().isLogin()) {
                            mIntent.setClass(mContext, TripListActivity.class);
                            startActivity(mIntent);
                        } else {
                            mIntent.setClass(mContext, WuhuLoginActivity.class);
                            startActivity(mIntent);
                        }
                        break;
                    case "otherTrip":
                        mIntent.setClass(mContext, OtherTripListActivity.class);
                        startActivity(mIntent);
                        break;
                    case "message":
                        if (AccountManager.getInstance().isLogin()) {
                            mIntent.setClass(mContext, MessageActivity.class);
                            startActivity(mIntent);
                        } else {
                            mIntent.setClass(mContext, WuhuLoginActivity.class);
                            startActivity(mIntent);
                        }
                        break;
                    case "service":
                        WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/customService.html", getString(R.string.btn_submenu_customerservice));
                        break;
                    case "illegal":
                        if (AccountManager.getInstance().isLogin()) {
                            mIntent.setClass(mContext, IllegalListActivity.class);
                            startActivity(mIntent);
                        } else {
                            mIntent.setClass(mContext, WuhuLoginActivity.class);
                            startActivity(mIntent);
                        }
                        break;
                    case "setting":
                        mIntent.setClass(mContext, SettingActivity.class);
                        startActivity(mIntent);
                        break;
                    case "recharge":
                        enterDepartmentRecharge();
                        break;

                }
            }


        });
    }

    public void enterDepartmentRecharge(){
        Intent intent = new Intent(this,RechargeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("FLAG","DEPARTMENT_RECHARGE");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * 检查用户是否认证
     */
    public void checkCommitted(Class<?> s){
        if (Constants.UNCOMMITTED.equals(reviewtype)
                || Constants.REFUSED.equals(reviewtype)) { // 未提交审核或拒绝审核
            ConfirmAndCancelDialog dialog = new ConfirmAndCancelDialog(this,R.style.MyDialogStyle).setOnDialogClickListener(new OnDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dialog, String clickPosition) {
                    switch (clickPosition){
                        case ClickPosition.ACCEPT:
                            Intent intent = new Intent(mContext, AuthenticationActivity.class);
                            startActivity(intent);
                            break;
                        case ClickPosition.CANCEL:
                            break;
                    }
                }
            });
            dialog.setConfirmText(mContext.getString(R.string.pop_window_confirm));
            dialog.setCancelText(mContext.getString(R.string.pop_window_cancel));
            dialog.setPromptContent(getString(R.string.pop_window_promt));
            dialog.showDialog(rootView);
        } else if (Constants.REVIEWING.equals(reviewtype)) { // 审核中
            PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
                @Override
                public void confirm() {
                }

                @Override
                public void cancel() {
                }

                @Override
                public void onItemListener(int position) {

                }
            }).setConfirmTx(getString(R.string.order_haode)).
                    setContentTx(getString(R.string.selectedresourcepopwindow_reviewing)).show(mTop_iv);
        }else{
            mIntent.setClass(mContext, s);
            startActivity(mIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkVersion() {
        VersionManager versionManager = VersionManager.getInstance();
        boolean isCheckVersion = versionManager.isCheckVersion;
        if (isCheckVersion) {
            versionManager.checkVersions(MainActivity.this, findViewById(R.id.ll_popwindow));
            versionManager.setOnUpdateVersionListener(new VersionManager.OnUpdateVersionListener() {
                @Override
                public void forceUpdateVersionEvent(String url) {
                    popForceDialog(url);
                }

                @Override
                public void normalUpdateVersionEvent(String url) {
                    popDialog(url);
                }

                @Override
                public void NoUpdateVersionEvent() {
                    ModuleHttpApi.getAdvertisements();
                }
            });
        }
    }
    /**
     * 新版本提示
     */
    public void popDialog(final String url) {
        final String desc = mContext.getString(cn.lds.chatcore.R.string.versionmanager_version_desc);
        final View view = findViewById(R.id.ll_popwindow);
        view.post(new Runnable() {
            @Override
            public void run() {
                PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        startWeb(url);
                    }

                    @Override
                    public void cancel() {
                        ModuleHttpApi.getAdvertisements();
                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx(String.format(desc)).show(view,rootView);
            }
        });

    }
    /**
     * 新版本强制提示
     */
    public void popForceDialog(final String url) {
        final String desc = mContext.getString(cn.lds.chatcore.R.string.versionmanager_version_desc_force);
        final View view = findViewById(R.id.ll_popwindow);
        view.post(new Runnable() {
            @Override
            public void run() {
                PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        startWeb(url);
                    }

                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx(String.format(desc)).setBackKey(false).show(view,rootView);
            }
        });

    }
    private void startWeb(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setAccount();
                    break;
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    // 按两次返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (PopWindowHelper.getInstance().isShow())
            return true;
        boolean backKeyable = VersionManager.getInstance().getBackKeyable();
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!MyApplication.isExiting) {
                    // ToolsUtil.showInfo(this, "再按一次返回键退出程序！");
                    MyApplication.isExiting = true;
                    return true;
                } else {
                    if (backKeyable) {//版本升级时弹出框 不触发该事件
                        Intent home = new Intent(Intent.ACTION_MAIN);
                        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        home.addCategory(Intent.CATEGORY_HOME);
                        startActivity(home);
                    }
                    return true;
                }
            }
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                // 按打开或关闭滑动菜单
                toggleMenu();

            }

        } catch (Exception e) {
            // ExceptionUtil.handle(e);
        }

        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.top_menu_lly
            , R.id.top_menu_rly
            , R.id.top_title_lly
            , R.id.close_tutorial
            , R.id.tv_read_tutorial
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_menu_lly:
                // 按打开或关闭滑动菜单
                toggleMenu();
                break;
            case R.id.top_menu_rly:
                jumpSearchActivity(); //进入搜索页面
                break;
            case R.id.top_title_lly:

                CustomPopuwindow.getInstance().choiceType(mTop_iv, this, new CustomPopuwindow.OnChoiceTypeListener() {
                    @Override
                    public void onInstantCarEvent() {
                        resetTopbar();
                        choiceType = HomeModelEnum.BUSINESSUSERCAR.getIndex();
                        centerMarker.setVisibility(View.GONE);
                        EventBus.getDefault().post(new MapFragmentChoiceEvent(3));
                    }

                    @Override
                    public void onBookingCarEvent() {
                        resetTopbar();
                        choiceType = HomeModelEnum.PRIVATEUSECAR.getIndex();
                        centerMarker.setVisibility(View.GONE);
                        EventBus.getDefault().post(new MapFragmentChoiceEvent(4));
                    }

                    @Override
                    public void onChargingPileEvent() {
                        resetTopbar();
                        choiceType = HomeModelEnum.CHARGINGPILE.getIndex();
                        centerMarker.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new MapFragmentChoiceEvent(2));
                    }
                });

                break;
            case R.id.tv_read_tutorial:
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/noviceHelp.html", "新手帮助");
                break;
            case R.id.close_tutorial:
                tutorialRlyt.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 进入搜索页面
     */
    private void jumpSearchActivity() {
        if(choiceType == HomeModelEnum.PRIVATEUSECAR.getIndex() || choiceType == HomeModelEnum.BUSINESSUSERCAR.getIndex()){
            //进入附近场站页面
            enterNearStationActivity();
        }else{
            //进入充电桩列表页面
            enterChargingListActivity();

        }

    }

    /**
     * 附近场站列表页面
     */
    private void enterNearStationActivity() {
        Intent intent = new Intent(this,NearStationListActivity.class);
        intent.putExtra("type",choiceType);
        startActivityForResult(intent,choiceType);
    }


    /**
     * 按打开或关闭滑动菜单
     */
    private void toggleMenu() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            menu.showMenu();
        }
    }

    /**
     * 进入充电桩列表页面
     */
    private void enterChargingListActivity() {
        mIntent.setClass(mContext, ChargingListActivity.class);
        startActivity(mIntent);
    }

    public void setTop_title_btn(String s) {
        mTopTitle_btn.setText(s);
    }

    public void setTopTitle(String s) {
        mTopTitle_tv.setText(s);
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!(CoreHttpApiKey.login.equals(apiNo)
                || CoreHttpApiKey.enrolleesGet.equals(apiNo)
                || CoreHttpApiKey.enrollees.equals(apiNo)
                || CoreHttpApiKey.refuse.equals(apiNo)
                || CoreHttpApiKey.confirm.equals(apiNo)
                || CoreHttpApiKey.getHasProcess.equals(apiNo)
                || CoreHttpApiKey.logout.equals(apiNo)
                || CoreHttpApiKey.getProcess.equals(apiNo)
                || ModuleHttpApiKey.checkUseApply.equals(apiNo)
                || ModuleHttpApiKey.getAdvertisements.equals(apiNo)
        ))
            return;

        switch (apiNo) {
            case CoreHttpApiKey.enrollees:
                enrollees(httpResult);
                break;
            case CoreHttpApiKey.confirm:
            case CoreHttpApiKey.refuse:
                AccountManager.getInstance().autoLoginWithNonceToken();
                LoadingDialog.dismissDialog();
                break;
            case CoreHttpApiKey.getHasProcess:
                checkHasProcess(httpResult); //解析数据判断是否有正在进行中的订单
                break;
            case CoreHttpApiKey.getProcess:
                GetProcessModel mProcess = GsonImplHelp.get().toObject(httpResult.getResult(), GetProcessModel.class);
                if (mProcess != null && mProcess.getData() != null) {
                    GetProcessModel.DataBean dataBean = mProcess.getData();
                    IntentHelper.intentTripList(mContext, mIntent, Integer.valueOf(dataBean.getId()), dataBean.getStatus(), dataBean.getType(), dataBean.isDelivered(), dataBean.isScheduledTimeUp());
                }
                break;
            case CoreHttpApiKey.logout:
                txt_submenu_name.setText(getString(R.string.loginidentify_title));
                break;
            case ModuleHttpApiKey.getAdvertisements:
                processActiveData(httpResult.getResult());
                break;
            case ModuleHttpApiKey.checkUseApply:
                handlerUserCarApplyResult(httpResult);
                break;


            default:
                break;
        }
    }
    private void handlerUserCarApplyResult( HttpResult httpResult ) {
        String result = httpResult.getResult();
        CheckUseCarApplyModel model = GsonImplHelp.get().toObject(result,CheckUseCarApplyModel.class);
        if(model != null && model.getData() != null && model.getData().getStatus() != null){
            switch (model.getData().getStatus()){
                case "NO_SUBMIT":
                    noSubmitUseCarApply("您今天还未提交用车申请，\n是否提交用车申请？"); //当日未提交用车申请
                    break;
                case "APPOVING":
                    ToolsHelper.showStatus(this,false,"用车申请正在审核中");
                    break;
                case "APPROVED":
//                    enterConfirmActivity();
                    break;
                case "REJECTED":
                    noSubmitUseCarApply("您当前还没有通过的用车申请，\n是否提交用车申请？");
                    break;
                case "OVER_TIME":
                case "LESS_TIME":
                    PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setConfirmTx("好的")

                            .setContentTx("当日18点后至次日8点前，不允许申请公务用车，如需用车请申请私人用车").show(findViewById(R.id.top__iv));
                    break;
            }
        }
    }

    /**
     * 当日没有提交用车申请
     */
    private void noSubmitUseCarApply(String content) {
        PopWindowHelper.getInstance().remind(this, new PopWindowListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(mContext, ApplyDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setConfirmTx("立即申请")
                .setCancelTx("暂不申请")
                .setContentTx(content).show(this.mTop_iv,this.rootView);
    }

    private void checkHasProcess( HttpResult httpResult ) {
        HasProcessModel mHasProcess = GsonImplHelp.get().toObject(httpResult.getResult(), HasProcessModel.class);
        if (mHasProcess != null && mHasProcess.isData()) {
            ConfirmAndCancelDialog dialog = new ConfirmAndCancelDialog(this, R.style.MyDialogStyle).setOnDialogClickListener(new OnDialogClickListener() {
                @Override
                public void onDialogClick( Dialog dialog, String clickPosition) {
                    switch (clickPosition){
                        case ClickPosition.ACCEPT:
                            ModuleHttpApi.getProcess();
                            break;
                        case ClickPosition.CANCEL:
                            break;
                    }
                }
            });
            dialog.setConfirmText("进入行程");
            dialog.setCancelText("不进入");
            dialog.setPromptContent("您有一个行程已开始\n是否进入?");
            dialog.showDialog(rootView);
        }else{
            //如果没有正在进行中的行程，检查用户是否有用车申请每天显示一次
            if(!TextUtils.isEmpty(CacheHelper.getLastOpenAppDay())){
                if(!TimeHelper.isToday(Long.parseLong(CacheHelper.getLastOpenAppDay()))){
                    CacheHelper.setLastOpenAppDay(System.currentTimeMillis());
                    ModuleHttpApi.checkUseApply(System.currentTimeMillis());
                }
            }else{
                CacheHelper.setLastOpenAppDay(System.currentTimeMillis());
                ModuleHttpApi.checkUseApply(System.currentTimeMillis());
            }

        }
    }

    /**
     * 解析首页活动数据
     * @param result
     */
    private void processActiveData(String result) {
        HomeActiveModel mProcess = GsonImplHelp.get().toObject(result, HomeActiveModel.class);
        List<HomeActiveModel.DataBean> data = mProcess.getData();
        if(data != null && data.size() > 0){
            mActiveList.clear();
            mActiveList.addAll(data);
        }
        //展示活动界面
        if(mActiveList != null && mActiveList.size() > 0){
            CustomPopuwindow.getInstance().showActiveView(this, rootView,mActiveList, new CustomPopuwindow.OnHomeActiveListener() {
                @Override
                public void onCloseActiveEvent() {
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            });
        }else{
            handler.sendEmptyMessageDelayed(0, 1000);
        }



    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!(CoreHttpApiKey.refuse.equals(apiNo)
                || CoreHttpApiKey.confirm.equals(apiNo)
        ))
            return;
        ToolsHelper.showHttpRequestErrorMsg(mContext, httpResult);
    }


    /**
     * 跟新个人信息
     */
    public void onEventMainThread(AccountAvaliableEvent event) {
//        checkVersion();
        setAccount();
    }

    protected void enrollees(HttpResult httpResult) {

        Map<String, String> map = httpResult.getExtras();
        String key = map.get("key");
        String value = map.get("value");
        if ("name".equals(key)) {
            txt_submenu_name.setText(value);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(MainActivity.class.getName(), e);
        }
        //收起侧滑菜单
        if (menu.isMenuShowing())
            menu.showContent(false);
        if (!AccountManager.getInstance().isLogin()) {
            txt_submenu_name.setText(getString(R.string.loginidentify_title));
            //获取未登录侧滑栏条目数据（不包含申请和审批条目）
            List<LeftMenuBean> notLoggedInList = LeftMenuItemData.getNotLoggedIn();
            itemList.clear();
            itemList.addAll(notLoggedInList);
            tutorialRlyt.setVisibility(View.GONE);

        }else{
            AccountsTable table = AccountManager.getInstance().findByNo();
            //判断角色权限，申请条目显示，审批条目根据权限判断是否显示
            if(table != null && table.getApprover() == 1){
                List<LeftMenuBean> notMarkingList = LeftMenuItemData.getAllData();
                itemList.clear();
                itemList.addAll(notMarkingList);
            }else{
                List<LeftMenuBean> notMarkingList = LeftMenuItemData.getNotMarking();
                itemList.clear();
                itemList.addAll(notMarkingList);
            }
        }
        reflushLeftMenuItem();

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(MainActivity.class.getName(), e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 200){
               AccountsTable table = AccountManager.getInstance().findByNo();
                //更新用户信息
                updatePersonInfo(table);
            }else{
                mapFragment.onActivityResult(requestCode,resultCode,data);
            }
        }
    }

    public void closeSlidingmentTouch() {
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    public void openSlidingmentTouch() {
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

}
