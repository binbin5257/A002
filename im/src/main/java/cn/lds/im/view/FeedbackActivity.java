package cn.lds.im.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnLongClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.AppHelper;
import cn.lds.chatcore.common.AppIndependentConfigure;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.PopData;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 意见反馈页面
 */
public class FeedbackActivity extends BaseActivity implements TextWatcher {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 返回按钮
    @ViewInject(R.id.tv_content)
    protected TextView contentTv;
    // 返回按钮
    @ViewInject(R.id.tv_text_num)
    protected TextView textNumTv;
  // 返回按钮
    @ViewInject(R.id.btn_submit)
    protected Button submitBtn;


    protected FeedbackActivity activity;
    protected int layoutID = R.layout.activity_feedback;

    public void setActivity(FeedbackActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(FeedbackActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.setting_feedback));
        btnBack.setVisibility(View.VISIBLE);
        contentTv.addTextChangedListener(this);

    }

    @OnClick({R.id.top_back_btn,
            R.id.btn_submit
            })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.btn_submit:
                submitFeedback();
                break;
        }
    }

    /**
     * 提交反馈
     */
    private void submitFeedback() {
        String contentStr = contentTv.getText().toString().trim();
        if(TextUtils.isEmpty(contentStr)){
            ToolsHelper.showStatus(mContext, false, "反馈不能为空");
            return;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int contentLength = contentTv.length();
        textNumTv.setText(contentLength + "/1000");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
