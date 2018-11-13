package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.lds.chat.R;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 行程结束界面
 */

public class DriveOverActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回按钮
     */
    private Button leftMenuBtn;
    /**
     * 页面标题
     */
    private TextView titleTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_over);
        initView();
    }

    /**
     * 初始化界面UI控件
     */
    private void initView() {
        leftMenuBtn = (Button) findViewById(R.id.top_menu_left);
        titleTv = (TextView) findViewById(R.id.top_title_tv);
        FrameLayout loacitonmapview = (FrameLayout) findViewById(R.id.loacitonmapview);

        titleTv.setText("行程结束");
        leftMenuBtn.setVisibility(View.VISIBLE);
        leftMenuBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_menu_left:
                break;
        }
    }
}
