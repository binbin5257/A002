package cn.lds.im.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lds.chat.R;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.RuleValidationHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.view.BaseActivity;

/***
 * 个人信息修改
 */
public class ProfileChangeActivity extends BaseActivity implements OnClickListener {

    public static final int SINGLE_LINE_EDITTEXT = 1; //单行文本
    public static final int MULTI_LINE_EDITTEXT = 2; //多行文本
    public static final int MULTI_LINE_ID_CARD = 3; //身份证
    public static final int NotCheck = 00;//不做校验
    public static final int isNULLCheck = 10;//判空校验
    public static final int isEmailCheck = 11;//邮箱校验(包含null判断)
    public static final int isPhoneCheck = 12;//手机号校验
    public static final int isIDCardCheck = 13;//身份证校验
    public static final int isNumCheck = 14;//是否为数字

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView titleTv;
    // 字数
    @ViewInject(R.id.rp_num_tv)
    protected TextView numTv;
    // 返回
    @ViewInject(R.id.top_back_btn)
    protected Button backBtn;
    // 菜单
    @ViewInject(R.id.top_menu_btn)
    protected Button menuBtn;

    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;
    // 单行文本
    @ViewInject(R.id.rl_text)
    protected RelativeLayout rl_text;
    //单行文本提示输入个数
    @ViewInject(R.id.tip_able_input_number)
    protected TextView ableInputNum;
    @ViewInject(R.id.et_text)
    protected EditText et_text;
    // 多行文本
    @ViewInject(R.id.rl_textarea)
    protected RelativeLayout rl_textarea;
    @ViewInject(R.id.et_textarea)
    protected EditText et_textarea;
    // 身份证
    @ViewInject(R.id.rl_text_card)
    protected RelativeLayout rl_text_card;
    @ViewInject(R.id.et_text_card)
    protected EditText et_text_card;

    protected Context context;
    protected int type;
    protected int checkType;
    protected int charMaxNum = 50;
    protected int charMaxNum1 = 20;
    protected String cacheEditString = "";
    protected String titileName = "";
    protected String gender;//性别

    protected ProfileChangeActivity activity;
    protected int layoutID = R.layout.activity_setting_revise_person;

    public void setActivity(ProfileChangeActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ProfileChangeActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
    }

    protected void init() {

        context = ProfileChangeActivity.this;
//        // 设置按钮
//        menuBtn.setText(getString(R.string.common_save));
//        menuBtn.setTextColor(getResources().getColor(R.color.dingxin_red_account_safe_pressed));
//        menuBtn.setEnabled(false);
//        menuBtn.setVisibility(View.VISIBLE);

        topbar_menu_service.setVisibility(View.GONE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);
        // 获取前画面传递参数

        type = getIntent().getIntExtra("type", SINGLE_LINE_EDITTEXT);//显示样式类型
        checkType = getIntent().getIntExtra("checkType", NotCheck);//校验类型类型

        titileName = getIntent().getStringExtra("title_name");
        titleTv.setText(titileName == null ? "" : titileName);//设置标题
        cacheEditString = getIntent().getStringExtra("content");//设置内容

        backBtn.setVisibility(View.VISIBLE);
        menuBtn.setText("保存");
        menuBtn.setVisibility(View.VISIBLE);

        Editable etext;
        // 文本类型
        switch (type) {
            //身份证
            case MULTI_LINE_ID_CARD:
                rl_text_card.setVisibility(View.VISIBLE);

                charMaxNum1 = getIntent().getIntExtra("input_num", charMaxNum1);//可输入字符长度设置
                et_text_card.addTextChangedListener(new EditChangedListener(numTv, et_text_card));//设置文字输入监听
                et_text_card.setText(cacheEditString);

                //设置光标
                etext = et_text_card.getText();
                Selection.setSelection(etext, etext.length());

                break;
            //单行文本
            case SINGLE_LINE_EDITTEXT:
                rl_text.setVisibility(View.VISIBLE);
//                ableInputNum.setVisibility(View.VISIBLE);

                charMaxNum1 = getIntent().getIntExtra("input_num", charMaxNum1);//可输入字符长度设置
                et_text.addTextChangedListener(new EditChangedListener(numTv, et_text));//设置文字输入监听
                et_text.setText(cacheEditString);
//                ableInputNum.setText(String.format("可输入%s个字符", charMaxNum1));

                //设置光标
                etext = et_text.getText();
                Selection.setSelection(etext, etext.length());
//                numTv.setText(String.valueOf(charMaxNum1 - (null == cacheEditString ? 0 : cacheEditString.length())));//显示剩余可输入字数
                break;
            //多行文本
            case MULTI_LINE_EDITTEXT:
                rl_textarea.setVisibility(View.VISIBLE);
                charMaxNum = getIntent().getIntExtra("input_num", charMaxNum);//可输入字符长度设置
                et_textarea.addTextChangedListener(new EditChangedListener(numTv, et_textarea));//设置文字输入监听
                //键盘输入监听
                et_textarea.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_ENTER) {//回车键--功能自定义

                            Intent intent = new Intent();
                            intent.putExtra("data", et_textarea.getText().toString());
                            ProfileChangeActivity.this.setResult(0, intent);
                            ProfileChangeActivity.this.finish();

                            return true;
                        } else {
                            return false;
                        }
                    }
                });
//                 cacheSign = cacheUtil.getAsString(HBTConstant.SIGN);
                et_textarea.setText(cacheEditString);

                //设置光标
                etext = et_textarea.getText();
                Selection.setSelection(etext, etext.length());

                numTv.setText(String.valueOf(charMaxNum - (null == cacheEditString ? 0 : cacheEditString.length())));//初始化-剩余可输入字数
                break;
        }
        switch (checkType) {
            case isNumCheck:
                et_text.setInputType(InputType.TYPE_CLASS_NUMBER);//调用数字键盘
                break;
            case isEmailCheck:
                et_text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);//调用数字键盘
                break;
        }
    }

    // 监听文本
    class EditChangedListener implements TextWatcher {
        protected CharSequence temp;// 监听前的文本
        protected int editStart;// 光标开始位置
        protected int editEnd;// 光标结束位置
        protected TextView textView;
        protected EditText editText;

        public EditChangedListener(TextView textView, EditText editText) {
            this.textView = textView;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // 输入文本之前的状态
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 输入文字中的状态，count是一次性输入字符数
            if (type == SINGLE_LINE_EDITTEXT || type == MULTI_LINE_ID_CARD) {
                if (textView != null) {
                    textView.setText("" + (charMaxNum1 - s.length()));
                }
            } else {
                if (textView != null) {
                    textView.setText("" + (charMaxNum - s.length()));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals(cacheEditString)) {
//                menuBtn.setTextColor(getResources().getColor(R.color.dingxin_red_account_safe_pressed));
//                menuBtn.setEnabled(false);
            } else {
//                menuBtn.setTextColor(getResources().getColor(R.color.dingxin_red_account_safe_normal));
//                menuBtn.setEnabled(true);
            }
            // 输入文字后的状态
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = editText.getSelectionStart();//获取光标的位置
            editEnd = editText.getSelectionEnd();
            if (type == SINGLE_LINE_EDITTEXT || type == MULTI_LINE_ID_CARD) {
                if (temp.length() > charMaxNum1) {
                    // 你输入的字数已经超过了限制！
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    editText.setText(s);
                    editText.setSelection(charMaxNum1);
                }
            } else {
                if (temp.length() > charMaxNum) {
                    // 你输入的字数已经超过了限制！
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    editText.setText(s);
                    editText.setSelection(charMaxNum);
                }
            }
        }
    }

    @OnClick({R.id.top_back_btn
            , R.id.top_menu_btn_del
            , R.id.top_menu_btn})
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn:
                returndata();
                break;
            case R.id.top_menu_btn_del:
//                hideKeyBorad();
//                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returndata();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void hideKeyBorad() {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(et_text.getWindowToken(), 0);
    }

    /**
     * 数据--返回
     */
    protected void returndata() {
        Intent intent;
        String content;
        switch (type) {
            case SINGLE_LINE_EDITTEXT:
                content = et_text.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToolsHelper.showStatus(this,false,"邮箱不能为空");
                    return;
                }
                if(!RuleValidationHelper.isEmail(content)){
                    ToolsHelper.showStatus(this,false,"邮箱格式不正确");
                    return;
                }
                intent = new Intent();
                if (isCheck(content)) {
                    intent.putExtra("data", content);
                }
                this.setResult(RESULT_OK, intent);
                finish();
                break;
            case MULTI_LINE_EDITTEXT:
                content = et_textarea.getText().toString();
                intent = new Intent();
                intent.putExtra("data", content);
                this.setResult(RESULT_OK, intent);
                finish();
                break;
            case MULTI_LINE_ID_CARD:
                content = et_text_card.getText().toString();
                intent = new Intent();
                if(isCheck(content)){
                    intent.putExtra("data", content);
                    this.setResult(RESULT_OK, intent);
                }
                finish();
                break;
        }
    }

    /**
     * 根据type 进行相对应的校验
     *
     * @param strCheck
     * @return
     */
    protected boolean isCheck(String strCheck) {

        boolean flag = true;


        switch (checkType) {
            case isNULLCheck:

                if (ToolsHelper.isNull(strCheck)) {
                    ToolsHelper.showStatus(mContext, false, titileName + "不能为空");
                    flag = false;
                }
                break;
            case isEmailCheck:
                if (!ToolsHelper.isEmail(context, strCheck))
                    flag = false;
                break;
            case isPhoneCheck:
                if (!ToolsHelper.isMobileNO(context, strCheck))
                    flag = false;
                break;
            case isIDCardCheck:
                Pattern pattern = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))");
                Matcher matcher = pattern.matcher(strCheck);
                flag = matcher.matches();
                if (!flag) {
                    ToolsHelper.showStatus(mContext, false, "身份证格式不正确");
                    flag = false;
                }
                break;
            case isNumCheck:
                if (!ToolsHelper.isNumeric(strCheck)) {
                    ToolsHelper.showStatus(mContext, false, getString(R.string.profilechangeactivity_onlynumber));
                    flag = false;
                }
                break;
            default:
                flag = true;
                break;
        }
        return flag;
    }

//    /**
//     * 验证邮箱地址是否正确
//     *
//     * @param email
//     * @return
//     */
//    public boolean checkEmail(String email) {
//        boolean flag = false;
//
//        if (ToolsHelper.isNull(email)) {
//            ToolsHelper.showInfo(context, "邮箱不能为空。");
//            return flag;
//        }
//
//        try {
//            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//            Pattern regex = Pattern.compile(check);
//            Matcher matcher = regex.matcher(email);
//            flag = matcher.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//
//        if (!flag) {
//            ToolsHelper.showInfo(context, "邮箱格式不正确。");
//        }
//
//        return flag;
//    }

//    /**
//     * 验证手机号码
//     *
//     * @param mobiles
//     * @return [0-9]{5,9}
//     */
//
//    public boolean isMobileNO(String mobiles) {
//        boolean flag = false;
//
//        if (ToolsHelper.isNull(mobiles)) {
//            ToolsHelper.showInfo(context, "手机号码不能为空。");
//            return flag;
//        }
//
//        try {
//            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//            Matcher m = p.matcher(mobiles);
//            flag = m.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//
//        if (!flag) {
//            ToolsHelper.showInfo(context, "手机号码格式不正确。");
//        }
//
//        return flag;
//    }
}
