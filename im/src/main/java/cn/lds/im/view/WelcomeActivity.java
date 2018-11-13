package cn.lds.im.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppHelper;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.DeviceHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.SharedPreferencesHelper;
import cn.lds.chatcore.common.StringHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginRequestModel;
import cn.lds.chatcore.enums.LoginType;
import cn.lds.chatcore.event.ApplicationInitializedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.imtp.ImtpManager;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.NotificationManager;
import cn.lds.chatcore.view.BaseFragmentActivity;
import cn.lds.im.fragment.WelcomeFragment;
import cn.lds.im.view.widget.WelcomeGifView;
import de.greenrobot.event.EventBus;

/**
 * 欢迎画面
 * <p>
 * 每次进入欢迎画面，都会校验token有效性
 * </p>
 *
 * @author suncf
 */
public class WelcomeActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener {

    /* 跳转到登录画面 */
    protected final static int HANDLER_MSG_LOGIN = 1;
    /* 跳转到主画面 */
    protected final static int HANDLER_MSG_MAIN = 2;
    /* 跳转到重新登录画面 */
    protected final static int HANDLER_MSG_RELOGIN = 3;
    /* 播放引导页*/
    protected final static int HANDLER_MSG_PLAYING = 4;
    protected final int jumpTime = 2000;


    // 主线程消息处理
    protected WelcomeHandler handler = new WelcomeHandler();
    //一次性token
    protected String nonceToken;

    @ViewInject(R.id.gifview)
    protected WelcomeGifView gifView;
    @ViewInject(R.id.welcome_iv)
    protected ImageView welcome_iv;
    @ViewInject(R.id.welcome_viewpager)
    protected ViewPager welcome_viewpager;
//    @ViewInject(R.id.welcome_radiogroup)
//    protected RadioGroup welcome_radiogroup;

    protected WelcomeActivity activity;
    protected int layoutID = R.layout.activity_welcome;
    private ArrayList<Fragment> fragmentList;

    public void setActivity(WelcomeActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(layoutID);
        ViewUtils.inject(WelcomeActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }


    }
    protected void initConfig() {
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initConfig();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 初始化判断
     */
    protected void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("DEVICEINFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int versionCode = sharedPreferences.getInt("versionCode", 0);
        if (ToolsHelper.isNull(MyApplication.getInstance().getSERVER_HOST()) || versionCode != DeviceHelper.getSoftwareVersionCode(this)) {
            MyApplication.getInstance().setSERVER_HOST(AppIndependentConfigure.SERVER_HOST_CUSC_SHARE);
            //获取后台系统配置
            CoreHttpApi.getSystemConfig();
        }

//        if (SystemConfig.SYS_CONFIG_SHOW_WELCOME_CARTOON) {
//            gifView.setMovieResource(R.raw.welcome);//添加gif图片的路径
//        }
        // 清除通知栏
        NotificationManager.getInstance().cancelAll();

        // 开启IM服务
        ImtpManager.getInstance().startService(this);

        if (CacheHelper.getIsFirstOpen() || AppHelper.getVersionCode() != versionCode) {
            fragmentList = new ArrayList<>();
            WelcomeFragment firstFragment = new WelcomeFragment(0, this);
            WelcomeFragment secondFragment = new WelcomeFragment(1, this);
            WelcomeFragment thirdFragment = new WelcomeFragment(2, this);
            // 将各Fragment加入数组中
            fragmentList.add(firstFragment);
            fragmentList.add(secondFragment);
            fragmentList.add(thirdFragment);
            // 一共加载1页，如果此处不指定，默认加载相邻页
            welcome_viewpager.setOffscreenPageLimit(3);
            // 设置ViewPager的设配器
            welcome_viewpager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
            // ViewPager的页面改变监听器
            welcome_viewpager.setOnPageChangeListener(this);
            if (welcome_iv.getVisibility() == View.VISIBLE)
                welcome_iv.setVisibility(View.GONE);
//            welcome_radiogroup.check(0 + 1);
//            handler.sendEmptyMessageDelayed(HANDLER_MSG_PLAYING, jumpTime);
            editor.putInt("versionCode",DeviceHelper.getSoftwareVersionCode(this));
            editor.apply();
            CacheHelper.setIsFirstOpen(false);
            CacheHelper.setIsNewUser(true);
        } else {
            nonceToken = CacheHelper.getAccessToken();
            // 判断是否有一次性登录token
            if (StringHelper.isNull(nonceToken)) {
                // 判断是否有登录账号
                if (StringHelper.isNull(CacheHelper.getLoginid())) {
                    // 账号密码登录
                    handler.sendEmptyMessageDelayed(HANDLER_MSG_LOGIN, jumpTime);
                } else {
                    // 重新登录
                    handler.sendEmptyMessageDelayed(HANDLER_MSG_LOGIN, jumpTime);
                }
            } else {
                // 使用一次性token自动登录
                AccountManager.getInstance().autoLoginWithNonceToken();
            }
        }
    }

    /**
     * 进入主界面
     */
    public void startMainActivity() {
        LoadingDialog.dismissDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);


    }


    /**
     * 一次性token自动登录完成
     * <p/>
     * 登录成功后的处理在AccountManager中完成，这里只是控制等待时间，待必要数据同步完成后，在进入主画面
     *
     * @param event
     */
    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!CoreHttpApiKey.login.equals(apiNo))
            return;

        LoginType loginType = LoginType.valueOf(httpResult.getExtras().get("loginType"));
        if (!LoginType.nonceToken.equals(loginType))
            return;

        A001();
    }

    /**
     * 一次性token自动登录成功后，等待必要数据
     */
    protected void A001() {
        handler.sendEmptyMessageDelayed(HANDLER_MSG_MAIN, jumpTime);
    }


    /**
     * 订阅-校验TOKEN失败
     *
     * @param event
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.getResult();
        if (!CoreHttpApiKey.login.equals(httpResult.getApiNo()))
            return;
        LoginType loginType = LoginType.valueOf(httpResult.getExtras().get("loginType"));
        if (!LoginType.nonceToken.equals(loginType))
            return;

//        A001();
        //广播应用初始化完成事件
        EventBus.getDefault().post(new ApplicationInitializedEvent());
        startMainActivity();
//        // 跳转到登录画面
//        handler.sendEmptyMessageDelayed(HANDLER_MSG_RELOGIN, 200);

    }

    @SuppressLint("HandlerLeak")
    public class WelcomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 登录
                case HANDLER_MSG_LOGIN:
                    startMainActivity();
                    break;
                // 重新登录
                case HANDLER_MSG_RELOGIN:
//                    intent = new Intent(mContext, ReloginActivity.class);
//                    intent.putExtra("log", "WelcomeActivity");
//                    startActivity(intent);
//                    finish();
                    //直接跳转到首页
                    startMainActivity();
                    break;
                // 登录成功跳转主画面
                case HANDLER_MSG_MAIN:
                    if (!MyApplication.essential.isOK())
                        handler.sendEmptyMessageDelayed(HANDLER_MSG_MAIN, jumpTime);
                    else {
                        startMainActivity();
                        //广播应用初始化完成事件
                        EventBus.getDefault().post(new ApplicationInitializedEvent());
                    }
                    break;
                // 播放引导页
                case HANDLER_MSG_PLAYING:
                    if (welcome_iv.getVisibility() == View.VISIBLE)
                        welcome_iv.setVisibility(View.GONE);
//                    //轮播
                    if (welcome_viewpager.getCurrentItem() != 2) {
//                        welcome_viewpager.setCurrentItem(welcome_viewpager.getCurrentItem() + 1);
                        handler.sendEmptyMessageDelayed(HANDLER_MSG_PLAYING, jumpTime);
                    } else{
                        startMainActivity();
                    }

                    break;
                default:
                    break;
            }
        }
    }


    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;

        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
//        if(arg0 == 2){
//            WelcomeFragment fragment = (WelcomeFragment) fragmentList.get(2);
//            fragment.startCountDown();
//        }else if(arg0 == 1){
//            WelcomeFragment fragment = (WelcomeFragment) fragmentList.get(1);
//            fragment.cancelCountDown();
//        }
//        welcome_radiogroup.clearCheck();
//        welcome_radiogroup.check(arg0 + 1);
    }
}
