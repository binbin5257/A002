package cn.lds.im.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.enums.FileType;
import cn.lds.chatcore.event.FileAvailableEvent;
import cn.lds.chatcore.event.FileDownloadErrorEvent;
import cn.lds.chatcore.view.BaseActivity;
import de.greenrobot.event.EventBus;

/**
 * 二维码展示画面
 *
 * @author user
 */
public class QRCodeActivity extends BaseActivity {

    public static final String TYPE_STR = "type";
    public static final String ID_STR = "id";
    /* 查看联系人的二维码标识 */
    public static final String TYPE_USER = "user";
    /* 查看群的二维码标识 */
    public static final String TYPE_MUC = "muc";

    protected Context mContext;

    @ViewInject(R.id.top_title_tv)
    protected TextView tvTitle;
    @ViewInject(R.id.mycode_name)
    protected TextView txName;
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    @ViewInject(R.id.mycode_avatar)
    protected ImageView ivAvatar;
    @ViewInject(R.id.iv_room_capture)
    protected ImageView ivQrCode;
    @ViewInject(R.id.mycode_hint)
    protected TextView mycode_hint;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView btnRefresh;

    /* 群或者联系人 */
    protected String type;
    /* 群No或者联系人id */
    protected String id;

    protected int layoutID = R.layout.activity_room_capture;
    protected QRCodeActivity activity;

    protected void setActivity(QRCodeActivity activity) {
        this.activity = activity;
    }
    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(QRCodeActivity.class,this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化
     */
    protected void init() {
        mContext = QRCodeActivity.this;

        type = getIntent().getStringExtra(TYPE_STR);
        id = getIntent().getStringExtra(ID_STR);

        btnBack.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setImageResource(R.drawable.btn_refresh);
        refreshView();


    }

    protected void refreshView() {
    }

    /**
     * 点击事件。回退
     *
     * @param v
     */
    @OnClick({R.id.top_back_btn, R.id.top_menu_btn_del})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn_del:
                break;
        }
    }

    /**
     * 订阅：文件下载成功
     *
     * @param event
     */
    public void onEventMainThread(FileAvailableEvent event) {
        String fileType = event.getFilesTable().getFileType();
        if (!FileType.QRCODE.name().equals(fileType)) {
            return;
        }
        LoadingDialog.dismissDialog();
        refreshView();

    }

    /**
     * 文件下载失败
     *
     * @param event
     */
    public void onEventMainThread(FileDownloadErrorEvent event) {
        FileType fileType = event.getFileType();
        if (!FileType.QRCODE.equals(fileType.name())) {
            return;
        }
        LoadingDialog.dismissDialog();
    }


}
