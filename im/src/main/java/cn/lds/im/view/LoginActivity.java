package cn.lds.im.view;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.ConnectivityHelper;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.DateHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.SPUtils;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginModel;
import cn.lds.chatcore.data.LoginRequestModel;
import cn.lds.chatcore.data.LoginResponseModel;
import cn.lds.chatcore.enums.DateTimeType;
import cn.lds.chatcore.enums.LoginType;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.CountDownButtonHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import de.greenrobot.event.EventBus;

/**
 * 手机验证码登陆页面
 *
 * @deprecated 当用户进行，业务操作，若是未登录，需要进入此页面进行登录验证
 */
public class LoginActivity extends BaseActivity {
    // R.id.top__iv
    @ViewInject(R.id.top__iv)
    public ImageView top__iv;
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.account_telphone)
    protected EditText account_telphone;

    @ViewInject(R.id.identifying_code)
    protected EditText identifying_code;

    @ViewInject(R.id.account_telphone_clear)
    protected ImageButton account_telphone_clear;

    @ViewInject(R.id.identifying_code_clear)
    protected ImageButton identifying_code_clear;

    @ViewInject(R.id.identifying_getcode)
    protected Button identifying_getcode;

    public static final String ACTION_LOGIN = "1";
    public static final String ACTION_RELOGIN = "2";
    protected String action = "1";//默认为登陆

    protected CountDownButtonHelper countDownButtonHelper;

    protected SmsObserver smsObserver;
    protected Uri SMS_INBOX = Uri.parse("content://sms/");

    protected int layoutID = R.layout.activity_login_identifying_code;
    protected LoginActivity activity;

    protected void setActivity(LoginActivity activity) {
        this.activity = activity;
    }
    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(LoginActivity.class,this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        smsObserver = new SmsObserver(this, null);
        init();
    }

    protected void init() {
        action = getIntent().getAction();
        //设置手机号
        if (!ToolsHelper.isNull(CacheHelper.getLoginid())) {
            account_telphone.setText(CacheHelper.getLoginid());
            account_telphone_clear.setVisibility(View.VISIBLE);
        }
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.loginidentify_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.GONE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);


        account_telphone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (cs.length() <= 0) {
                    account_telphone_clear.setVisibility(View.GONE);
                } else {
                    account_telphone_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        identifying_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (cs.length() <= 0) {
                    identifying_code_clear.setVisibility(View.GONE);
                } else {
                    identifying_code_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.identifying_login,
            R.id.identifying_getcode,
            R.id.identifying_code_clear,
            R.id.account_telphone_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identifying_code_clear:
                identifying_code.setText("");
                identifying_code_clear.setVisibility(View.GONE);
                break;
            case R.id.account_telphone_clear:
                account_telphone.setText("");
                account_telphone_clear.setVisibility(View.GONE);
                break;
            case R.id.identifying_getcode:
                String mobile2 = account_telphone.getText().toString();
                if (ToolsHelper.isMobileNO(mContext, mobile2)) {
                    LoadingDialog.showDialog(mContext, getString(R.string.loginactivity_logininidentify));
                    LoginModel loginRequestModel = new LoginModel();
                    loginRequestModel.setUsername(mobile2);
//                    ModuleHttpApi.password(loginRequestModel);
                }
                break;
            case R.id.identifying_login:
                String mobile = account_telphone.getText().toString();
                String code = identifying_code.getText().toString();

                if (!ToolsHelper.isMobileNO(mContext, mobile)) {
                    return;
                }
                if (ToolsHelper.isNull(code)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.IdentifyCodeNull));
                    return;
                }
                // 没有网络连接
                if (!ConnectivityHelper.isConnected(getApplicationContext())) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.NoConnectionError));
                    return;
                }
                LoadingDialog.showDialog(mContext, getString(R.string.loginactivity_logining));
                LoginRequestModel loginRequestModel = new LoginRequestModel();
                loginRequestModel.setLoginType(LoginType.mobile_captcha);
                loginRequestModel.setUsername(mobile);
                loginRequestModel.setPassword(code);
                ModuleHttpApi.login(loginRequestModel, false);
                break;
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;

        }
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

            if (!(ModuleHttpApiKey.password.equals(apiNo) ||
                    ModuleHttpApiKey.login.equals(apiNo)))
                return;
            // 发送验证码
            if (ModuleHttpApiKey.password.equals(apiNo)) {
                ToolsHelper.showStatus(mContext, true, getString(R.string.forgetpasswordactivity_sendcode_already));
                countDownButtonHelper = new CountDownButtonHelper(identifying_getcode, getString(R.string.activity_forget_password_send_code), 60, 1);
                countDownButtonHelper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {

                    @Override
                    public void finish() {
                        //Toast.makeText(RegisterActivity.this, "倒计时结束", Toast.LENGTH_SHORT).show();
                    }
                });
                countDownButtonHelper.start();


            }
            // 登录
            if (CoreHttpApiKey.login.equals(apiNo)) {
                A001(httpResult.getResult());
            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    protected void A001(String result) {

        LoginResponseModel loginResponseModel = GsonImplHelp.get().toObject(result, LoginResponseModel.class);
        if (null != loginResponseModel && !loginResponseModel.getData().isBlack()) {
            ToolsHelper.showInfo(mContext, mContext.getString(R.string.ResultLoginOK));
            MainActivity.flag = false;
            MainActivity.flag1 = false;
            mIntent.setClass(mContext, MainActivity.class);
//            MyApplication.confirmStatus = loginResponseModel.getData().isConfirmStatus();
            startActivity(mIntent);

        } else {
            ToolsHelper.showStatus(mContext, false, "登录失败，请联系管理员");
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
        if (!(ModuleHttpApiKey.login.equals(apiNo)
                || ModuleHttpApiKey.password.equals(apiNo))) {
            return;
        }
        // 登录处理
        if (CoreHttpApiKey.login.equals(apiNo)) {
            ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
        }
        // 拉取个人信息
        else if (ModuleHttpApiKey.password.equals(apiNo)) {
            // 非401的错误，提交给主线程处理
            ToolsHelper.showHttpRequestErrorMsg(this, httpResult);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onDestroy() {
        if (null != countDownButtonHelper) {
            countDownButtonHelper.end();
            countDownButtonHelper = null;
        }
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            try {
                // 判断是否被踢出到登录界面
//                String kickedTime = CacheHelper.getImKickedTime();
                String kickedTime = (String) SPUtils.get(MyApplication.getInstance(),"ImKickedTime","");
                if (!ToolsHelper.isNull(kickedTime)) {
//                    CacheHelper.setImKickedTime("");
                    SPUtils.put(MyApplication.getInstance(),"ImKickedTime", DateHelper.getSystemDate(DateTimeType.yyyyMMddHHmm));

                    String deviceName;
                    switch (ToolsHelper.toInt(CacheHelper.getImKickedOsType())) {
                        case 1:
                            deviceName = "iPhone";
                            break;
                        case 2:
                            deviceName = "Android";
                            break;
                        default:
                            deviceName = "其他";
                            break;
                    }
//                    CacheHelper.setImKickedOsType("");
                    SPUtils.put(MyApplication.getInstance(),"ImKickedOsType", "");
                    PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
                        @Override
                        public void confirm() {
//                            CacheHelper.setImKickedTime("");
//                            CacheHelper.setImKickedOsType("");
                        }

                        @Override
                        public void cancel() {
//                            CacheHelper.setImKickedTime("");
//                            CacheHelper.setImKickedOsType("");
                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setContentTx(getString(R.string.reloginactivity_account) + kickedTime +
                            getString(R.string.reloginactivity_account_as) + deviceName +
                            getString(R.string.reloginactivity_account_leaking)).show(top__iv);
                }
            } catch (Exception ex) {
                LogHelper.e("", ex);
            }
//            CacheHelper.setImKickedTime("");
        }
    }

    protected boolean canGetSms = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String msm = ToolsHelper.getSmsFromPhone(mContext);
                    identifying_code.setText(msm);
                    break;
            }
        }
    };

    private class SmsObserver extends ContentObserver {
        SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (canGetSms) {
                canGetSms = false;
                handler.sendEmptyMessage(0);
            }
        }
    }

}
