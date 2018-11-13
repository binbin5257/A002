package cn.lds.chatcore.view;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;

import cn.lds.chatcore.manager.ActivityStackManager;

/**
 * 基础的Cordova类，目前用于解决关闭activity
 * Created by tzd on 2016/5/24.
 */
public class BaseCordovaActivity extends CordovaActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //讲一个activity添加栈中
        ActivityStackManager.getInstance().pushOneActivity(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //讲一个activity移除栈中
        ActivityStackManager.getInstance().popOneActivity(this);
    }
}
