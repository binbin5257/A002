package cn.lds.chatcore.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import cn.lds.chatcore.manager.ActivityStackManager;

public class BaseFragmentActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //讲一个activity添加栈中
        ActivityStackManager.getInstance().pushOneActivity(this);
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
