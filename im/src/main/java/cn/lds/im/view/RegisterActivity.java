package cn.lds.im.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.ConnectivityHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginModel;
import cn.lds.chatcore.data.RegistModel;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.CountDownButtonHelper;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.FaIlureModel;
import de.greenrobot.event.EventBus;

/**
 * 注册页面
 * Created by E0608 on 2017/12/25.
 */

public class RegisterActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 手机号
    @ViewInject(R.id.et_phone)
    protected EditText phoneEt;
    // 手机号清空
    @ViewInject(R.id.phone_clear)
    protected ImageButton phoneClear;
    // 验证码
    @ViewInject(R.id.identifying_code)
    protected EditText verificationCodeEt;
    // 验证码清空
    @ViewInject(R.id.identifying_code_clear)
    protected ImageButton verificationCodeClear;
    // 密码
    @ViewInject(R.id.password)
    protected EditText passwordEt;
    // 密码清空
    @ViewInject(R.id.password_clear)
    protected ImageButton passwordClear;
    // 确认密码
    @ViewInject(R.id.confirm_password)
    protected EditText confirmPasswordEt;
    // 确认密码清空
    @ViewInject(R.id.confirm_password_clear)
    protected ImageButton confirmPasswordClear;
    // 获取验证码按钮
    @ViewInject(R.id.identifying_getcode)
    protected Button identifyingGetcode;
    private CountDownButtonHelper countDownButtonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuhu_regist);
        ViewUtils.inject(RegisterActivity.class, this);
        init();
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
            R.id.identifying_code_clear,
            R.id.password_clear,
            R.id.phone_clear,
            R.id.confirm_password_clear,
            R.id.identifying_getcode,
            R.id.voice_verification,
            R.id.btn_regist
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case R.id.btn_regist://注册
                regist();
                break;
            case R.id.phone_clear:
                phoneEt.setText("");
                phoneClear.setVisibility(View.GONE);
                break;
            case R.id.identifying_code_clear:
                verificationCodeEt.setText("");
                verificationCodeClear.setVisibility(View.GONE);
                break;
            case R.id.password_clear:
                passwordEt.setText("");
                passwordClear.setVisibility(View.GONE);
                break;
            case R.id.confirm_password_clear:
                confirmPasswordEt.setText("");
                confirmPasswordClear.setVisibility(View.GONE);
                break;
            case R.id.identifying_getcode:
                //获取验证码
                String mobile = phoneEt.getText().toString();
                if (ToolsHelper.isMobileNO(mContext, mobile)) {
                    LoadingDialog.showDialog(mContext, getString(R.string.loginactivity_logininidentify));
                    LoginModel loginRequestModel = new LoginModel();
                    loginRequestModel.setUsername(mobile);
                    loginRequestModel.setType("REGISTER");
                    ModuleHttpApi.getVerificationCode(loginRequestModel);
                }
                break;
            case R.id.voice_verification:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;

        }

    }
    /**
     * 发送验证码
     * 使用手机号注册
     * 注册
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
                    ModuleHttpApiKey.regist.equals(apiNo)))
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
            }else if(ModuleHttpApiKey.regist.equals(apiNo)){
                ToolsHelper.showStatus(mContext, true, "注册成功");
                finish();
            }


        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }
    /**
     * 发送验证码
     * 使用手机号注册
     * 注册
     *
     * @param event http请求完成事件
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.password.equals(apiNo) ||
                    ModuleHttpApiKey.regist.equals(apiNo)))
                return;
            FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(), FaIlureModel.class);
            ToolsHelper.showStatus(this,false,model.getErrors().get(0).getErrmsg());


        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    /**
     * 注册
     */
    private void regist() {
        String phoneStr = phoneEt.getText().toString().trim();
        String verificationCodeStr = verificationCodeEt.getText().toString().trim();
        String passwordStr = passwordEt.getText().toString().trim();
        String confirmPasswordStr = confirmPasswordEt.getText().toString().trim();
        // 没有网络连接
        if (!ConnectivityHelper.isConnected(getApplicationContext())) {
            ToolsHelper.showStatus(mContext, false, getString(R.string.NoConnectionError));
            return;
        }
        if(ToolsHelper.isMobileNO(this,phoneStr)){
            if(TextUtils.isEmpty(verificationCodeStr)){
                ToolsHelper.showStatus(this,false,"验证码不能为空");
                return;
            }
            if(TextUtils.isEmpty(passwordStr)){
                ToolsHelper.showStatus(this,false,"密码不能为空");
                return;
            }
            if(passwordStr.length() < 6){
                ToolsHelper.showStatus(this,false,"密码位数不能小于6位");
                return;
            }
            if(TextUtils.isEmpty(confirmPasswordStr)){
                ToolsHelper.showStatus(this,false,"确认密码不能为空");
                return;
            }
            if(!passwordStr.equals(confirmPasswordStr)){
                ToolsHelper.showStatus(this,false,"两次密码输入不一致");
                return;
            }
            //提交数据注册
            RegistModel model = new RegistModel();
            model.setPhone(phoneStr);
            model.setPassword(passwordStr);
            model.setNoncePassword(verificationCodeStr);
            model.setConfirmPassword(confirmPasswordStr);
            ModuleHttpApi.registUser(model);

        }

    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("注册");
        btnBack.setVisibility(View.VISIBLE);
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    phoneClear.setVisibility(View.GONE);
                } else {
                    phoneClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verificationCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    verificationCodeClear.setVisibility(View.GONE);
                } else {
                    verificationCodeClear.setVisibility(View.VISIBLE);
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
        confirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    confirmPasswordClear.setVisibility(View.GONE);
                } else {
                    confirmPasswordClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
