package cn.lds.im.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import de.greenrobot.event.EventBus;

/**
 * 行程评价
 */
public class RouteComentsActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView topbar_menu_service;

    @ViewInject(R.id.et_textarea)
    protected EditText et_textarea;
    // 字数
    @ViewInject(R.id.rp_num_tv)
    protected TextView numTv;
    // 提交
    @ViewInject(R.id.tv_setting_exit)
    protected TextView tv_setting_exit;
    @ViewInject(R.id.routecoment_ratingBar)
    protected RatingBar routecoment_ratingBar;

    protected int charMaxNum = 200;
    protected String id;
    protected int score = -1;

    protected int layoutID = R.layout.activity_route_coments;
    protected RouteComentsActivity activity;

    protected void setActivity(RouteComentsActivity activity) {
        this.activity = activity;
    }
    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(RouteComentsActivity.class,this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        id = getIntent().getAction();
        init();
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.routecomentsactivity_title));
        btnBack.setVisibility(View.VISIBLE);
        topbar_menu_service.setVisibility(View.VISIBLE);
        topbar_menu_service.setImageResource(R.drawable.topbar_menu_customerservice);


        Editable etext;
        et_textarea.addTextChangedListener(new EditChangedListener(numTv, et_textarea));//设置文字输入监听
        //键盘输入监听
        et_textarea.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {//回车键--功能自定义


                    return true;
                } else {
                    return false;
                }
            }
        });

        //设置光标
        etext = et_textarea.getText();
        Selection.setSelection(etext, etext.length());
        routecoment_ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = (int) rating;
            }
        });
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn_del,
            R.id.tv_setting_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            case R.id.tv_setting_exit:
                try {
                    String text = et_textarea.getText().toString();
                    if (-1 == score || 0 == score) {
                        ToolsHelper.showStatus(mContext, false, getString(R.string.routecomentsactivity_nullscore));
                    } else {
                        LoadingDialog.showDialog(mContext, getString(R.string.routecomentsactivity_commit_loading));
//                        ModuleHttpApi.reservationOrdersComment(id, score + "", text);
                    }
                } catch (Exception e) {
                    LogUtils.d(RouteComentsActivity.class.getName(), e);
                }

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
            if (textView != null) {
                textView.setText(s.length() + "/" + charMaxNum);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (ToolsHelper.isNull(s.toString())) {
                tv_setting_exit.setEnabled(false);
            } else {
                tv_setting_exit.setEnabled(true);
            }
            // 输入文字后的状态
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = editText.getSelectionStart();//获取光标的位置
            editEnd = editText.getSelectionEnd();
            if (temp.length() > charMaxNum) {
                // 你输入的字数已经超过了限制！
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                editText.setText(s);
                editText.setSelection(charMaxNum);
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(getString(R.string.default_bus_register), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }

    public void onEventMainThread(HttpRequestEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrdersComment.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {

            switch (apiNo) {
                case ModuleHttpApiKey.reservationOrdersComment:
                    I001(httpResult.getResult());
                    break;
            }
        }

    }

    protected void I001(String result) {
        ToolsHelper.showStatus(mContext, true, getString(R.string.routecomentsactivity_commit_loadingsuccess));
        finish();
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.reservationOrdersComment.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showStatus(mContext, false, getString(R.string.routecomentsactivity_commit_loadingfailed));
    }
}
