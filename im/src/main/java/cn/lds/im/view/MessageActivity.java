package cn.lds.im.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.db.MessageTable;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.MessageArrivedEvent;
import cn.lds.chatcore.manager.MessageManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.view.adapter.MessageAdapter;
import de.greenrobot.event.EventBus;

/**
 * 消息页面
 */
public class MessageActivity extends BaseActivity{

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    //忽略未读消息按钮
    @ViewInject(R.id.top_menu_btn)
    private Button top_menu_btn;
    //listview
    @ViewInject(R.id.messageactivity_pulllistview)
    protected ListView mListview;
    //没有消息时的显示页面(当前没有消息)
    @ViewInject(R.id.messageactivity_nullview)
    protected LinearLayout messageactivity_nullview;

//    protected ListView mListview;
    protected MessageAdapter mAdapter;
    protected ArrayList<MessageTable> mList;


    protected MessageActivity activity;
    protected int layoutID = R.layout.activity_message;
    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }
    public void setActivity(MessageActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(MessageActivity.class, this);
        if(null != activity){
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        init();
        initData();
    }

    /**
     * 从数据库中获取消息数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    protected void initData() {
        if (!mList.isEmpty()){
            mList.clear();
        }
        List<MessageTable> messageTables = MessageManager.getInstance().findByNo();
        if (null != messageTables) {
            messageactivity_nullview.setVisibility(View.GONE);
            mList.addAll(messageTables);
        } else{
            messageactivity_nullview.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.btn_submenu_message));
        btnBack.setVisibility(View.VISIBLE);
        top_menu_btn.setVisibility(View.VISIBLE);
        top_menu_btn.setText(R.string.message_not_read);


        mList = new ArrayList<>();
        mAdapter = new MessageAdapter(mContext, mList);
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(onItemClickListener);
    }

    @OnClick({R.id.top_back_btn,
            R.id.top_menu_btn,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case R.id.top_menu_btn://忽略未读消息
                if(mList == null || mList.size() == 0){
                    return;
                }else {
                    for (MessageTable mMessage : mList){
                        if(mMessage.isNew()){
                            mMessage.setIsNew(false);
                            try {
                                DbHelper.getDbUtils().update(mMessage, "isNew");
                            } catch (Exception e) {
                                LogUtils.d(e.toString());
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /*
        * 列表点击事件
        * */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageTable dataBean = mList.get(position);
            dataBean.setIsNew(false);
            try {
                DbHelper.getDbUtils().update(dataBean, "isNew");
            } catch (Exception e) {
                LogUtils.d(e.toString());
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    public void onEventMainThread(HttpRequestEvent event) {

        HttpResult httpResult = event.httpResult;
        LoadingDialog.dismissDialog();
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getActivitiesDetail.equals(apiNo))) {
            return;
        }
        String result = httpResult.getResult();
        if (!ToolsHelper.isNull(result)) {
            switch (apiNo) {
                case ModuleHttpApiKey.getActivitiesDetail:
//                    EventModel.DataBean eventModel = GsonImplHelp.get().toObject(result, EventModel.DataBean.class);
                    WebViewActivityHelper.startWebViewActivity(mContext, httpResult.getResult(), getString(R.string.eventactivity_detail));
                    break;
            }
        }

    }

    @Override
    protected void onPause() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MessageManager.getInstance().updateStatus();
//            }
//        }).start();
        super.onPause();
    }




    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(MessageActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(MessageActivity.class.getName(), e);
        }
    }

    public void onEventMainThread(MessageArrivedEvent event) {
        initData();
    }
}


