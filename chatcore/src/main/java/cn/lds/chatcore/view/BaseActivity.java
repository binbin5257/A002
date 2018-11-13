package cn.lds.chatcore.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import cn.lds.chatcore.common.StatusBarUtil;
import cn.lds.chatcore.manager.ActivityStackManager;

public class BaseActivity extends Activity {

    public Intent mIntent;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //讲一个activity添加栈中
        ActivityStackManager.getInstance().pushOneActivity(this);
//        StatusBarUtil.StatusBarLightMode(this);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.WHITE);
//            getWindow().setStatusBarColor(Color.WHITE);
//        }
        mIntent = new Intent();
        mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //讲一个activity移除栈中
        ActivityStackManager.getInstance().popOneActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent == null || intent.getComponent() == null) {
            super.startActivity(intent);
        } else {
            String className = intent.getComponent().getClassName();
            String activityName = "";
            int length = 0;
            if (!TextUtils.isEmpty(className)) {
                if (className.contains(".")) {
                    length = className.split("\\.").length;
                    if (length > 0) {
                        activityName = className.split("\\.")[length - 1];
                    }
                    activityName = "cn.lds.im.view.appview.App" + activityName;
                }
            }
            try {
                Class<?> c = (Class<?>) Class.forName(activityName);
                if (c == null) {
                    super.startActivity(intent);
                } else {
                    intent.setClassName(intent.getComponent().getPackageName(), activityName);
                    super.startActivity(intent);
                }
            } catch (ClassNotFoundException e) {
//                LogHelper.e("startActivity", e);
                super.startActivity(intent);
            }
        }
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent.getComponent() == null) {
            super.startActivityForResult(intent, requestCode);
        } else {
            String className = intent.getComponent().getClassName();
            String activityName = "";
            int length = 0;
            if (!TextUtils.isEmpty(className)) {
                if (className.contains(".")) {
                    length = className.split("\\.").length;
                    if (length > 0) {
                        activityName = className.split("\\.")[length - 1];
                    }
                    activityName = "cn.lds.im.view.appview.App" + activityName;
                }
            }
            try {
                Class<?> c = (Class<?>) Class.forName(activityName);
                if (c == null) {
                    super.startActivityForResult(intent, requestCode);
                } else {
                    intent.setClassName(intent.getComponent().getPackageName(), activityName);
                    super.startActivityForResult(intent, requestCode);
                }
            } catch (ClassNotFoundException e) {
//                LogHelper.e("startActivity", e);
                super.startActivityForResult(intent, requestCode);
            }
        }
    }
}
