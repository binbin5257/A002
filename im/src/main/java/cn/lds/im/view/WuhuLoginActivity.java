package cn.lds.im.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.ConnectivityHelper;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginModel;
import cn.lds.chatcore.data.LoginRequestModel;
import cn.lds.chatcore.data.LoginResponseModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.enums.LoginType;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.dialog.PromptDialog;
import cn.lds.chatcore.view.dialog.callback.OnDialogClickListener;
import cn.lds.im.common.CountDownButtonHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.FaIlureModel;
import de.greenrobot.event.EventBus;

/**
 * 芜湖项目登录界面
 * Created by E0608 on 2017/12/25.
 */

public class WuhuLoginActivity extends BaseActivity {
    // R.id.top__iv
    @ViewInject(R.id.top__iv)
    public ImageView top__iv;
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 标题栏注册
    @ViewInject(R.id.top_right_tv)
    private TextView topMenuRight;
    // 手机号密码登录
    @ViewInject(R.id.tv_phone_password)
    private TextView phonePasswordLoginTv;
    // 手机号验证码登录
    @ViewInject(R.id.tv_phone_verification_code)
    private TextView phoneVerficarionCodeLoginTv;
    // 手机号密码登录布局
    @ViewInject(R.id.phone_password)
    private LinearLayout phonePasswordLlyt;
    // 手机号验证码登录布局
    @ViewInject(R.id.phone_verification)
    private LinearLayout phoneVerficarionCodeLlyt;

    // 账号密码登录下划线
    @ViewInject(R.id.line_password)
    protected View linePassword;
    // 账号验证码登录下划线
    @ViewInject(R.id.line_verification)
    protected View lineVerification;
    // 忘记密码
    @ViewInject(R.id.tv_forget_password)
    protected TextView forgetPasswordTv;

    // 账号密码登录手机号
    @ViewInject(R.id.phone_password_account_telphone)
    protected EditText phonePasswordTel;
    // 账号密码登录密码
    @ViewInject(R.id.et_password)
    protected EditText passwordEt;
    // 账号验证登录手机号
    @ViewInject(R.id.account_telphone)
    protected EditText phoneVerificationTel;
    // 账号验证码登录验证码
    @ViewInject(R.id.identifying_code)
    protected EditText phoneVerificationCode;

    // 账号密码登录手机号清空
    @ViewInject(R.id.phone_password_account_telphone_clear)
    protected ImageButton phonePasswordTelClear;

    // 账号密码登录密码清空
    @ViewInject(R.id.phone_password_identifying_code_clear)
    protected ImageButton passwordClear;

    // 账号验证码登录手机号清空
    @ViewInject(R.id.account_telphone_clear)
    protected ImageButton phoneVerificationTelClear;

    // 账号验证码登录验证码清空
    @ViewInject(R.id.identifying_code_clear)
    protected ImageButton phoneVerificationCodeClear;

    // 获取验证码按钮
    @ViewInject(R.id.identifying_getcode)
    protected Button identifyingGetcode;
    // 语音验证布局
    @ViewInject(R.id.rl_voice_code)
    protected RelativeLayout voiceCodeRlyt;
    @ViewInject(R.id.main_view)
    protected LinearLayout mainView;

    private int VERIFICATION_LOGIN = 1;
    private int PASSWORD_LOGIN = 2;
    private int LOGIN_TYPE = PASSWORD_LOGIN;
    private CountDownButtonHelper countDownButtonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuhu_login);
        ViewUtils.inject(WuhuLoginActivity.class, this);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
        super.onDestroy();
        if(countDownButtonHelper != null){
            countDownButtonHelper.end();
        }
    }

    @OnClick({R.id.top_back_btn,//返回
            R.id.top_right_tv,
            R.id.tv_phone_password,
            R.id.tv_phone_verification_code,
            R.id.btn_login,
            R.id.tv_forget_password,
            R.id.phone_password_account_telphone_clear,
            R.id.phone_password_identifying_code_clear,
            R.id.account_telphone_clear,
            R.id.identifying_getcode,
            R.id.voice_verification,
            R.id.identifying_code_clear
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case R.id.top_right_tv:
                enterRegisterActivity();
                break;
            case R.id.tv_phone_password:
                LOGIN_TYPE = PASSWORD_LOGIN;
                voiceCodeRlyt.setVisibility(View.GONE);
                forgetPasswordTv.setVisibility(View.VISIBLE);
                phonePasswordLlyt.setVisibility(View.VISIBLE);
                phoneVerficarionCodeLlyt.setVisibility(View.GONE);
                phonePasswordLoginTv.setTextColor(getResources().getColor(R.color.important_color_blue));
                phoneVerficarionCodeLoginTv.setTextColor(getResources().getColor(R.color.black));
                linePassword.setBackgroundColor(getResources().getColor(R.color.important_color_blue));
                lineVerification.setBackgroundColor(getResources().getColor(R.color.backgroud));

                break;
            case R.id.tv_phone_verification_code:
                LOGIN_TYPE = VERIFICATION_LOGIN;
                voiceCodeRlyt.setVisibility(View.GONE);
                forgetPasswordTv.setVisibility(View.GONE);
                phonePasswordLlyt.setVisibility(View.GONE);
                phoneVerficarionCodeLlyt.setVisibility(View.VISIBLE);
                phonePasswordLoginTv.setTextColor(getResources().getColor(R.color.black));
                phoneVerficarionCodeLoginTv.setTextColor(getResources().getColor(R.color.important_color_blue));
                linePassword.setBackgroundColor(getResources().getColor(R.color.backgroud));
                lineVerification.setBackgroundColor(getResources().getColor(R.color.important_color_blue));
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.voice_verification:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.tv_forget_password:
                enterForgetPasswordActivityt();
                break;
            case R.id.phone_password_account_telphone_clear:
                phonePasswordTel.setText("");
                phonePasswordTelClear.setVisibility(View.GONE);
                break;
            case R.id.phone_password_identifying_code_clear:
                passwordEt.setText("");
                passwordClear.setVisibility(View.GONE);
                break;
            case R.id.account_telphone_clear:
                phoneVerificationTel.setText("");
                phoneVerificationTelClear.setVisibility(View.GONE);
                break;
            case R.id.identifying_code_clear:
                phoneVerificationCode.setText("");
                phoneVerificationCodeClear.setVisibility(View.GONE);
                break;
            case R.id.identifying_getcode:
                //获取验证码
                String mobile = phoneVerificationTel.getText().toString();
                if (ToolsHelper.isMobileNO(mContext, mobile)) {
                    LoadingDialog.showDialog(mContext, getString(R.string.loginactivity_logininidentify));
                    LoginModel loginRequestModel = new LoginModel();
                    loginRequestModel.setUsername(mobile);
                    loginRequestModel.setType("LOGIN");
                    ModuleHttpApi.getVerificationCode(loginRequestModel);
                }
                break;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        String kickedTime = CacheHelper.getImKickedTime();
        if (!ToolsHelper.isNull(kickedTime)) {
            String log = getIntent().getStringExtra("log");
            if(!TextUtils.isEmpty(log)){
                if(log.equals("账号变更")){
                    changePhone();
                }else if(log.equals("审核权限变化")){
                    approvalAuthorityChange();
                }else if(log.equals("账号被拉黑")){
                    enrolleeBlack();
                }else{
                    MultiLogin();
                }
            }



        }
    }

    /**
     * 账号被拉黑
     */
    private void enrolleeBlack() {
        // 判断是否被踢出到登录界面
        String kickedTime = CacheHelper.getImKickedTime();
        CacheHelper.setImKickedTime("");
        PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
            @Override
            public void confirm() {
                CacheHelper.setImKickedTime("");
            }

            @Override
            public void cancel() {
                CacheHelper.setImKickedTime("");
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(getString(R.string.reloginactivity_account) + kickedTime +
                "被拉入黑名单，无法继续使用").show(top__iv,mainView);
        CacheHelper.setImKickedTime("");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            String log = getIntent().getStringExtra("log");
//            if (!TextUtils.isEmpty(log) && isFirst) {
//                isFirst = false;
//                if(log.equals("账号变更")){
//                    changePhone();
//                }else if(log.equals("审核权限变化")){
//                    approvalAuthorityChange();
//                }else{
//                    MultiLogin();
//                }
//
//
//            }
//
//
//        }

        }

    /**
     * 审批权限变化
     */
    private void approvalAuthorityChange() {
        // 判断是否被踢出到登录界面
        String kickedTime = CacheHelper.getImKickedTime();
        CacheHelper.setImKickedTime("");
        PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
            @Override
            public void confirm() {
                CacheHelper.setImKickedTime("");
            }

            @Override
            public void cancel() {
                CacheHelper.setImKickedTime("");
            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(getString(R.string.reloginactivity_account) + kickedTime +
                "用车审批权限发生变化，请重新登录").show(top__iv,mainView);
        CacheHelper.setImKickedTime("");

    }

    /**
     * 账号变更
     */
    private void changePhone() {
        // 判断是否被踢出到登录界面
        String kickedTime = CacheHelper.getImKickedTime();
        if (!ToolsHelper.isNull(kickedTime)) {
            CacheHelper.setImKickedTime("");
//            PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
//                @Override
//                public void confirm() {
//                    CacheHelper.setImKickedTime("");
//                }
//
//                @Override
//                public void cancel() {
//                    CacheHelper.setImKickedTime("");
//                }
//
//                @Override
//                public void onItemListener(int position) {
//
//                }
//            }).setContentTx(getString(R.string.reloginactivity_account) + kickedTime +
//                    "已经修改，请用新账号重新登录").show(top__iv,mainView);
        PromptDialog dialog = new PromptDialog(this).setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dialog, String clickPosition) {
                CacheHelper.setImKickedTime("");
            }
        });
        dialog.setPromptContent(getString(R.string.reloginactivity_account) + kickedTime +
                "已经修改，请用新账号重新登录");
        dialog.showDialog(mainView);
            CacheHelper.setImKickedTime("");
        }

    }

    /**
     * 异地登录
     */
    private void MultiLogin() {
        try {

            // 判断是否被踢出到登录界面
            String kickedTime = CacheHelper.getImKickedTime();
            if (!ToolsHelper.isNull(kickedTime)) {
                CacheHelper.setImKickedTime("");
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
                CacheHelper.setImKickedOsType("");
                PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        CacheHelper.setImKickedTime("");
                        CacheHelper.setImKickedOsType("");
                    }

                    @Override
                    public void cancel() {
                        CacheHelper.setImKickedTime("");
                        CacheHelper.setImKickedOsType("");
                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx(getString(R.string.reloginactivity_account) + kickedTime +
                        getString(R.string.reloginactivity_account_as) + deviceName +
                        getString(R.string.reloginactivity_account_leaking)).show(top__iv,mainView);
            }

            } catch(Exception ex){
                LogHelper.e("", ex);
            }
                CacheHelper.setImKickedTime("");
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
                    ModuleHttpApiKey.login.equals(apiNo)||
                ModuleHttpApiKey.enrolleesGet.equals(apiNo)))
                return;
            // 发送验证码
            if (ModuleHttpApiKey.password.equals(apiNo)) {
                ToolsHelper.showStatus(mContext, true, getString(R.string.forgetpasswordactivity_sendcode_already));
                countDownButtonHelper = new CountDownButtonHelper(identifyingGetcode, getString(R.string.activity_forget_password_send_code), 60, 1);
                countDownButtonHelper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {

                    @Override
                    public void finish() {
                    }
                });
                countDownButtonHelper.start();


            }
            // 登录
            if (CoreHttpApiKey.login.equals(apiNo)) {
                ToolsHelper.showInfo(mContext, mContext.getString(R.string.ResultLoginOK));
                A001(httpResult.getResult());
            }
//            //获取个人信息
//            if(ModuleHttpApiKey.enrolleesGet.equals(apiNo)){
//
//            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.password.equals(apiNo) ||
                    ModuleHttpApiKey.login.equals(apiNo)||
                    ModuleHttpApiKey.enrolleesGet.equals(apiNo)))
                return;
            FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(), FaIlureModel.class);
            ToolsHelper.showStatus(this,false,model.getErrors().get(0).getErrmsg());


        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }
    /**
     * 跟新个人信息
     */
    public void onEventMainThread(AccountAvaliableEvent event) {
//        AccountsTable table = AccountManager.getInstance().findByNo();


    }

    protected void A001(String result) {

        LoginResponseModel loginResponseModel = GsonImplHelp.get().toObject(result, LoginResponseModel.class);
        if (null != loginResponseModel && !loginResponseModel.getData().isBlack()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.flag1 = false;
                    MainActivity.flag = false;
                    if(MainActivity.mainActivity != null){
                        MainActivity.mainActivity.finish();
                    }
                    mIntent.setClass(mContext, MainActivity.class);
                    startActivity(mIntent);
                    finish();
                }
            }, 2000);

        } else {
            ToolsHelper.showStatus(mContext, false, "登录失败，请联系管理员");
        }
    }

    /**
     * 进入忘记密码页面
     */
    private void enterForgetPasswordActivityt() {
        Intent intent = new Intent(this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void login() {
        if(LOGIN_TYPE == PASSWORD_LOGIN){
            //手机号验证码登录
            String mobile = phonePasswordTel.getText().toString();
            String password = passwordEt.getText().toString();

            if(TextUtils.isEmpty(mobile)){
                ToolsHelper.showStatus(this,false,getString(R.string.toolshelper_phone_nonull));
                return;
            }
            if(mobile.length() != 11){
                ToolsHelper.showStatus(this,false,getString(R.string.toolshelper_phone_wrong));
                return;
            }
            if(TextUtils.isEmpty(password)){
                ToolsHelper.showStatus(this,false,getString(R.string.IdentifyPasswordNull));
                return;
            }
            if(password.length() < 6){
                ToolsHelper.showStatus(this,false,"密码位数不能小于6位");
                return;
            }
            // 没有网络连接
            if (!ConnectivityHelper.isConnected(getApplicationContext())) {
                ToolsHelper.showStatus(mContext, false, getString(R.string.NoConnectionError));
                return;
            }
            LoadingDialog.showDialog(mContext, getString(R.string.loginactivity_logining));
            LoginRequestModel loginRequestModel = new LoginRequestModel();
            loginRequestModel.setLoginType(LoginType.mobile_pass);
            loginRequestModel.setUsername(mobile);
            loginRequestModel.setPassword(password);
            ModuleHttpApi.login(loginRequestModel, false);
        }else if(LOGIN_TYPE == VERIFICATION_LOGIN){
            //手机号验证码登录
            String mobile = phoneVerificationTel.getText().toString();
            String code = phoneVerificationCode.getText().toString();
            if(TextUtils.isEmpty(mobile)){
                ToolsHelper.showStatus(this,false,getString(R.string.toolshelper_phone_nonull));
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToolsHelper.showStatus(mContext, false, getString(R.string.IdentifyCodeNull));
                return;
            }
            if(mobile.length() != 11){
                ToolsHelper.showStatus(this,false,getString(R.string.toolshelper_phone_wrong));
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
        }

    }

    /**
     * 进入注册页面
     */
    private void enterRegisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("登录");
        btnBack.setVisibility(View.VISIBLE);
        topMenuRight.setVisibility(View.VISIBLE);
        topMenuRight.setText("注册");

        phonePasswordTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    phonePasswordTelClear.setVisibility(View.GONE);
                } else {
                    phonePasswordTelClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    passwordClear.setVisibility(View.GONE);
                } else {
                    passwordClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneVerificationTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    phoneVerificationTelClear.setVisibility(View.GONE);
                } else {
                    phoneVerificationTelClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    phoneVerificationCodeClear.setVisibility(View.GONE);
                } else {
                    phoneVerificationCodeClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
